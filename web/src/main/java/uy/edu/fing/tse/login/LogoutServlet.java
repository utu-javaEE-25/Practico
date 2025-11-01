package uy.edu.fing.tse.login;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String LOGOUT_ENDPOINT = "https://auth-testing.iduruguay.gub.uy/oidc/v1/logout";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String state = req.getParameter("state");

        if ("logout_done".equals(state)) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?logged_out=true");
            return;
        }

        String idToken = null;
        if (session != null) {
            idToken = (String) session.getAttribute("id_token");
            session.invalidate();
        }

        if (idToken == null || idToken.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?logged_out=true");
            return;
        }

        String postLogoutRedirect = OidcEndpoints.buildRedirectUri(req, "/logout");
        String logoutUrl = LOGOUT_ENDPOINT
                + "?id_token_hint=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8)
                + "&post_logout_redirect_uri=" + URLEncoder.encode(postLogoutRedirect, StandardCharsets.UTF_8)
                + "&state=logout_done";

        resp.sendRedirect(logoutUrl);
    }
}
