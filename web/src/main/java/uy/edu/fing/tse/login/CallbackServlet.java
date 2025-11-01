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
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.UsuarioDAO;

@WebServlet("/callback")
public class CallbackServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String CLIENT_ID = "890192";
    private static final String CLIENT_SECRET = "457d52f181bf11804a3365b49ae4d29a2e03bbabe74997a2f510b179";
    private static final String TOKEN_ENDPOINT = "https://auth-testing.iduruguay.gub.uy/oidc/v1/token";
    private static final String REDIRECT_URI = "https://hcenuy.web.elasticloud.uy/Laboratorio/callback";

    @EJB
    private UsuarioDAO usuarioDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String code = req.getParameter("code");
        if (code == null || code.isBlank()) {
            resp.getWriter().println("<p>Error: missing authorization code.</p>");
            return;
        }

        String returnedState = req.getParameter("state");
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.getWriter().println("<p>Error: session not found when validating state.</p>");
            return;
        }

        String originalState = (String) session.getAttribute("oauth_state");
        if (!Objects.equals(returnedState, originalState)) {
            resp.getWriter().println("<p>Error: state parameter does not match the stored value.</p>");
            return;
        }

        try {
            session.removeAttribute("oauth_state");
            session.removeAttribute("oauth_nonce");

            String body = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                    + "&client_id=" + CLIENT_ID
                    + "&client_secret=" + CLIENT_SECRET
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
                resp.getWriter().println("<pre>OIDC token endpoint error (" + response.statusCode() + "):\n" + response.body() + "</pre>");
                return;
            }

            JSONObject json = new JSONObject(response.body());
            String idToken = json.optString("id_token", null);
            if (idToken == null) {
                resp.getWriter().println("<p>Error: the token endpoint response is missing id_token.</p>");
                return;
            }

            JSONObject claims = TokenValidator.validar(idToken);
            String nombre = claims.optString("given_name");
            String apellido = claims.optString("family_name");
            String email = claims.optString("email");
            String cedulaIdentidad = claims.optString("numero_documento");
            String sub = claims.optString("sub");

            UsuarioServicioSalud usuario = new UsuarioServicioSalud();
            usuario.setSub(sub);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            usuario.setCedulaIdentidad(cedulaIdentidad);
            usuarioDAO.guardar(usuario);

            session.setAttribute("nombre", nombre);
            session.setAttribute("apellido", apellido);
            session.setAttribute("email", email);
            session.setAttribute("id_token", idToken);

            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            resp.getWriter().println("<p>Callback aborted while contacting the identity provider.</p>");
        } catch (Exception e) {
            resp.getWriter().println("<pre>Error processing callback:\n" + e.getMessage() + "</pre>");
            e.printStackTrace(resp.getWriter());
        }
    }
}
