package uy.edu.fing.tse.login;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String CLIENT_ID = "890192";
    private static final String AUTH_ENDPOINT = "https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize";
    private static final String REDIRECT_URI = "https://hcenuy.web.elasticloud.uy/Laboratorio/callback";
    //private static final String REDIRECT_URI = "http://localhost:8080/Laboratorio/callback";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String state = UUID.randomUUID().toString();
        String nonce = UUID.randomUUID().toString();

        HttpSession session = req.getSession(true);
        session.setAttribute("oauth_state", state);
        session.setAttribute("oauth_nonce", nonce);

        String scope = "openid email profile personal_info document";

        String redirectUrl = AUTH_ENDPOINT + "?" +
                "client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8) +
                "&response_type=code" +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8) +
                "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8) +
                "&nonce=" + URLEncoder.encode(nonce, StandardCharsets.UTF_8);

        resp.sendRedirect(redirectUrl);
    }
}
