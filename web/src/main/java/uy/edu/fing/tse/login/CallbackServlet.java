package uy.edu.fing.tse.login;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.json.JSONObject;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.api.AdminGlobalServiceLocal;
import uy.edu.fing.tse.api.AuditLogServiceLocal;
import uy.edu.fing.tse.audit.AuditHelper;
import uy.edu.fing.tse.audit.AuditLogConstants;
import uy.edu.fing.tse.entidades.AdminHcen;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.UsuarioDAO;
import uy.edu.fing.tse.servicios.VerificacionEdadService;

@WebServlet("/callback")
public class CallbackServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    String clientId = System.getProperty("CLIENT_ID");
    String clientSecret = System.getProperty("CLIENT_SECRET");
    private static final String TOKEN_ENDPOINT = "https://auth-testing.iduruguay.gub.uy/oidc/v1/token";

    private static final String REDIRECT_URI = "https://hcenuy.web.elasticloud.uy/Laboratorio/callback";
    //private static final String REDIRECT_URI = "http://localhost:8080/Laboratorio/callback";

    @EJB
    private UsuarioDAO usuarioDAO;
    @EJB
    private AdminGlobalServiceLocal adminService;
    @EJB
    private AuditLogServiceLocal auditLogService;
    @EJB
    private VerificacionEdadService verificacionEdadService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String code = req.getParameter("code");
        if (code == null || code.isBlank()) {
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            resp.getWriter().println("<p>Error: missing authorization code.</p>");
            return;
        }

        String returnedState = req.getParameter("state");
        HttpSession session = req.getSession(false);
        if (session == null) {
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            resp.getWriter().println("<p>Error: session not found when validating state.</p>");
            return;
        }

        String originalState = (String) session.getAttribute("oauth_state");
        if (!Objects.equals(returnedState, originalState)) {
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            resp.getWriter().println("<p>Error: state parameter does not match the stored value.</p>");
            return;
        }

        String loginType = (String) session.getAttribute("login_type");

        try {
            session.removeAttribute("oauth_state");
            session.removeAttribute("oauth_nonce");
            //session.removeAttribute("login_type");

            String body = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                    + "&client_id=" + clientId
                    + "&client_secret=" + clientSecret
                    + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8)
                    + "&grant_type=authorization_code";

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TOKEN_ENDPOINT))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
                resp.getWriter().println("<pre>OIDC token endpoint error (" + response.statusCode() + "):\n" + response.body() + "</pre>");
                return;
            }

            JSONObject json = new JSONObject(response.body());
            String idToken = json.optString("id_token", null);
            if (idToken == null) {
                registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
                resp.getWriter().println("<p>Error: the token endpoint response is missing id_token.</p>");
                return;
            }

            JSONObject claims = TokenValidator.validar(idToken);
            String nombre = claims.optString("given_name");
            String apellido = claims.optString("family_name");
            String email = claims.optString("email");
            String cedulaIdentidad = claims.optString("numero_documento");
            String sub = claims.optString("sub");

            //Verificar si es mayor de edad
            if (verificarEsMayorDeEdad(req, resp, cedulaIdentidad)) {
                return;
            }
            if (verificarEsMayorDeEdad(req, resp, cedulaIdentidad)) return;

//            UsuarioServicioSalud usuario = new UsuarioServicioSalud();
//            usuario.setSub(sub);
//            usuario.setNombre(nombre);
//            usuario.setApellido(apellido);
//            usuario.setEmail(email);
//            usuario.setCedulaIdentidad(cedulaIdentidad);
//            UsuarioServicioSalud guardado = usuarioDAO.guardar(usuario);
//            if (guardado == null) {
//                guardado = usuarioDAO.buscarPorSub(sub);
//            }

            if ("admin".equalsIgnoreCase(loginType)) {
                 //Verificar si es un admin registrado por CI
                boolean esAdmin = adminService != null && adminService.esAdminPorCi(cedulaIdentidad);
                if (!esAdmin) {
                    resp.getWriter().println("<p>Acceso denegado: el usuario no es un administrador registrado.</p>");
                    return;
                }

                //Actualizar el GubUyId si no lo tiene asignado
                AdminHcen adminActualizado = adminService.actualizarGubUyIdPorCI(cedulaIdentidad, sub);

                session.setAttribute("nombre", nombre);
                session.setAttribute("apellido", apellido);
                session.setAttribute("email", email);
                session.setAttribute("sub", sub);
                session.setAttribute("id_token", idToken);
                session.setAttribute("rol", "ADMIN");
                session.setAttribute("isAdmin", true);
                session.setAttribute("admin_id", adminActualizado != null ? adminActualizado.getId() : null);

                resp.sendRedirect(req.getContextPath() + "/index_admin");
            } else {
                UsuarioServicioSalud usuario = new UsuarioServicioSalud();
                usuario.setSub(sub);
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setEmail(email);
                usuario.setCedulaIdentidad(cedulaIdentidad);
                UsuarioServicioSalud guardado = usuarioDAO.guardar(usuario);
                if (guardado == null) {
                    guardado = usuarioDAO.buscarPorSub(sub);
                }

                session.setAttribute("nombre", nombre);
                session.setAttribute("apellido", apellido);
                session.setAttribute("email", email);
                session.setAttribute("sub", sub);
                session.setAttribute("cedulaIdentidad", cedulaIdentidad);
                session.setAttribute("id_token", idToken);
                if (guardado != null) {
                    session.setAttribute("usuario_id", guardado.getId());
                }
                resp.sendRedirect(req.getContextPath() + "/vistas/index_user.jsp");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            resp.getWriter().println("<p>Callback aborted while contacting the identity provider.</p>");
        } catch (Exception e) {
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            resp.getWriter().println("<pre>Error processing callback:\n" + e.getMessage() + "</pre>");
            e.printStackTrace(resp.getWriter());
        }
    }

    private boolean verificarEsMayorDeEdad(HttpServletRequest req, HttpServletResponse resp, String cedulaIdentidad) {
        try {
            if (!verificacionEdadService.esMayorDeEdad(cedulaIdentidad)) {
                //pintar un error y retornar al login
                registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
                resp.getWriter().println("<p>Error: El usuario debe ser mayor de edad para acceder al sistema.</p>");
                return true;
            }
        }catch (Exception e) {
            //si hay un error en la verificacion de edad, se deja pasar al usuario
        }
        return false;
    }

    private void registrarLogin(HttpServletRequest req, Long recursoId, String resultado) {
        AuditHelper.registrarEvento(
                auditLogService,
                req,
                null,
                AuditLogConstants.Acciones.LOGIN,
                recursoId,
                resultado);
    }
}
