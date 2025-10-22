package com.demo.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import org.json.JSONObject;

import com.demo.dao.UsuarioDAO;
import com.demo.entidad.Usuario;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/callback")
public class CallbackServlet extends HttpServlet {

    private static final String CLIENT_ID = "890192";
    private static final String CLIENT_SECRET = "457d52f181bf11804a3365b49ae4d29a2e03bbabe74997a2f510b179";
    private static final String REDIRECT_URI = "http://localhost:8080/javaee-login/callback";
    private static final String TOKEN_ENDPOINT = "https://auth-testing.iduruguay.gub.uy/oidc/v1/token";

    @Inject
    private UsuarioDAO usuarioDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");

        //Verificar que llegó el parámetro "code"
        String code = req.getParameter("code");
        if (code == null || code.isBlank()) {
            resp.getWriter().println("<p>Error: Falta el parámetro 'code' en la redirección.</p>");
            return;
        }

        //Validar el parámetro "state"
        String returnedState = req.getParameter("state");
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.getWriter().println("<p>Error: No existe sesión para validar el 'state'.</p>");
            return;
        }

        String originalState = (String) session.getAttribute("oauth_state");
        if (!Objects.equals(returnedState, originalState)) {
            resp.getWriter().println("<p>Error: El parámetro 'state' no coincide o es inválido.</p>");
            return;
        }

        //Si el state coincide, podemos continuar con seguridad
        try {
            //Intercambiar el "code" por el "id_token"
            HttpClient client = HttpClient.newHttpClient();

            String body = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                    + "&client_id=" + CLIENT_ID
                    + "&client_secret=" + CLIENT_SECRET
                    + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8)
                    + "&grant_type=authorization_code";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TOKEN_ENDPOINT))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                resp.getWriter().println("<pre>Error del servidor OIDC (" + response.statusCode() + "):\n" + response.body() + "</pre>");
                return;
            }

            JSONObject json = new JSONObject(response.body());
            String idToken = json.optString("id_token", null);
            if (idToken == null) {
                resp.getWriter().println("<p>Error: No se recibió 'id_token' en la respuesta.</p>");
                return;
            }

            //Decodificar el payload del id_token
            String[] parts = idToken.split("\\.");
            if (parts.length < 2) {
                resp.getWriter().println("<p>Error: id_token inválido.</p>");
                return;
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            JSONObject claims = new JSONObject(payload);

            // Crear o actualizar el usuario en base de datos
            Usuario usuario = new Usuario();
            usuario.setSub(claims.optString("sub"));
            usuario.setNombre(claims.optString("given_name"));
            usuario.setApellido(claims.optString("family_name"));
            usuario.setEmail(claims.optString("email"));
            usuario.setNumeroDocumento(claims.optString("numero_documento"));

            usuarioDAO.guardar(usuario);

            // =============================================================
            
            session.setAttribute("nombre", claims.optString("given_name"));
            session.setAttribute("apellido", claims.optString("family_name"));
            session.setAttribute("email", claims.optString("email"));
            session.setAttribute("id_token", idToken);

            
            resp.sendRedirect(req.getContextPath() + "/index.jsp");

        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
        }
    }
}