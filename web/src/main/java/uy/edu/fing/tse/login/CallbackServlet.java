package uy.edu.fing.tse.login;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
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

    // private static final String REDIRECT_URI
    // ="https://hcenuy.web.elasticloud.uy/Laboratorio/callback";
    private static final String REDIRECT_URI = "http://localhost:8080/Laboratorio/callback";

    private static final String LOGOUT_ENDPOINT = "https://auth-testing.iduruguay.gub.uy/oidc/v1/logout";
    private static final String POST_LOGOUT_REDIRECT_URI = "http://localhost:8080/Laboratorio/logout";
    // private static final String POST_LOGOUT_REDIRECT_URI =
    // "https://hcenuy.web.elasticloud.uy/Laboratorio/logout";

    @EJB
    private UsuarioDAO usuarioDAO;
    @EJB
    private AdminGlobalServiceLocal adminService;
    @EJB
    private AuditLogServiceLocal auditLogService;
    @EJB
    private VerificacionEdadService verificacionEdadService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String code = req.getParameter("code");
        if (code == null || code.isBlank()) {
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            forwardError(req, resp, "Error", "Missing authorization code.", null);
            return;
        }

        String returnedState = req.getParameter("state");
        HttpSession session = req.getSession(false);
        if (session == null) {
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            forwardError(req, resp, "Error", "Session not found.", null);
            return;
        }

        String originalState = (String) session.getAttribute("oauth_state");
        if (!returnedState.equals(originalState)) {
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            forwardError(req, resp, "Error", "State parameter does not match the stored value.", null);
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
                forwardError(req, resp, "Error", "OIDC token endpoint error (" + response.statusCode() + ")",
                        response.body());
                return;
            }

            JSONObject json = new JSONObject(response.body());
            String idToken = json.optString("id_token", null);
            if (idToken == null) {
                registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
                forwardError(req, resp, "Error", "The token endpoint response is missing id_token.", null);
                return;
            }

            JSONObject claims = TokenValidator.validar(idToken);
            String nombre = claims.optString("given_name");
            String apellido = claims.optString("family_name");
            String email = claims.optString("email");
            String cedulaIdentidad = claims.optString("numero_documento");
            String sub = claims.optString("sub");

            // Verificar si es mayor de edad
            if (verificarEsMayorDeEdad(req, resp, cedulaIdentidad, idToken)) {
                return;
            }

            if ("admin".equalsIgnoreCase(loginType)) {
                 //Verificar si es un admin registrado por CI
                boolean esAdmin = adminService != null && adminService.esAdminPorCi(cedulaIdentidad);
                if (!esAdmin) {
                    registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
                    forwardError(req, resp, "Acceso denegado", "El usuario no es un administrador registrado.", null);
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

                Boolean esMobile = (Boolean) session.getAttribute("es_mobile_login");

                if (Boolean.TRUE.equals(esMobile)) {
                    // 1. Limpieza
                    session.removeAttribute("es_mobile_login");
                    String sessionId = session.getId();

                    // 2. Tu URL de Expo
                    String deepLink = "hcenapp://?jsessionid=" + sessionId;

                    System.out.println("--- MOBILE: Redirigiendo a: " + deepLink);

                    // 3. EN LUGAR DE sendRedirect, devolvemos HTML
                    resp.setContentType("text/html;charset=UTF-8");
                    var out = resp.getWriter();

                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                    out.println("<title>Volviendo a la App</title>");
                    out.println("</head>");
                    out.println("<body style='text-align:center; font-family:sans-serif; padding-top:40px;'>");
                    out.println("<h2>Login Exitoso</h2>");
                    out.println("<p>Si no vuelves automáticamente, pulsa el botón:</p>");

                    // Botón Grande y Azul
                    out.println("<a href='" + deepLink + "' style='display:inline-block; background:#007bff; color:white; padding:15px 25px; text-decoration:none; border-radius:8px; font-size:18px; margin-top:20px;'>Volver a la App</a>");

                    // Script para intentar hacerlo automático
                    out.println("<script>window.location.href='" + deepLink + "';</script>");

                    out.println("</body>");
                    out.println("</html>");

                    return; // IMPORTANTE: Cortar aquí

                } else {
                    // --- CASO WEB ---
                    resp.sendRedirect(req.getContextPath() + "/vistas/index_user.jsp");
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            forwardError(req, resp, "Error", "Callback aborted while contacting the identity provider.",
                    e.getMessage());
        } catch (Exception e) {
            registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);
            forwardError(req, resp, "Error processing callback", "An internal error occurred.", e.getMessage());
        }
    }

    private boolean verificarEsMayorDeEdad(HttpServletRequest req, HttpServletResponse resp, String cedulaIdentidad,
            String idToken) throws ServletException, IOException {
        try {
            if (!verificacionEdadService.esMayorDeEdad(cedulaIdentidad)) {
                registrarLogin(req, null, AuditLogConstants.Resultados.FAILURE);

                // Construir URL de logout externo
                String logoutUrl = l
                        + "?id_token_hint=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8)
                        + "&post_logout_redirect_uri="
                        + URLEncoder.encode(POST_LOGOUT_REDIRECT_URI, StandardCharsets.UTF_8)
                        + "&state=logout_done";

                req.setAttribute("returnUrl", logoutUrl);
                forwardError(req, resp, "Acceso denegado", "El usuario debe ser mayor de edad para acceder al sistema.",
                        "No está permitido el acceso al sistema a menores de edad.");
                return true;
            }
        } catch (Exception e) {
            // si hay un error en la verificacion de edad, se deja pasar al usuario
        }
        return false;
    }

    private void forwardError(HttpServletRequest req, HttpServletResponse resp, String title, String message,
            String details) throws ServletException, IOException {
        req.setAttribute("errorTitle", title);
        req.setAttribute("errorMessage", message);
        req.setAttribute("errorDetails", details);
        if (req.getAttribute("returnUrl") == null) {
            req.setAttribute("returnUrl", req.getContextPath() + "/");
        }
        RequestDispatcher rd = req.getRequestDispatcher("/vistas/error.jsp");
        rd.forward(req, resp);
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