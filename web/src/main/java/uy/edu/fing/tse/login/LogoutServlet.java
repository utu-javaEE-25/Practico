package uy.edu.fing.tse.login;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String LOGOUT_ENDPOINT = "https://auth-testing.iduruguay.gub.uy/oidc/v1/logout";
    //private static final String POST_LOGOUT_REDIRECT_URI = "https://hcenuy.web.elasticloud.uy/Laboratorio/logout";
    private static final String POST_LOGOUT_REDIRECT_URI = "http://localhost:8080/Laboratorio/logout";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String state = req.getParameter("state");

        // Detectar si venimos de IdUruguay después del logout
        if ("logout_done".equals(state)) {
            String loginType = getLoginTypeFromCookie(req);
            String redirectPage = "/index.jsp";

            if ("admin".equals(loginType)) {
                redirectPage = "/vistas/index_admin_login.jsp";
            }

            // Eliminar cookie al finalizar
            Cookie cookie = new Cookie("login_type", "");
            cookie.setMaxAge(0);
            cookie.setPath(req.getContextPath());
            resp.addCookie(cookie);

            resp.sendRedirect(req.getContextPath() + redirectPage + "?logged_out=true");
            return;
        }

        // Obtener info de sesión
        String idToken = null;
        String loginType = "user";

        if (session != null) {
            idToken = (String) session.getAttribute("id_token");
            String sessionLoginType = (String) session.getAttribute("login_type");
            if (sessionLoginType != null) {
                loginType = sessionLoginType;
            }
        }

        // Guardar login_type en cookie ANTES de invalidar sesión
        Cookie cookie = new Cookie("login_type", loginType);
        cookie.setHttpOnly(true);
        cookie.setPath(req.getContextPath());
        cookie.setMaxAge(60 * 5); // 5 minutos de vida, suficiente para redirigir tras logout
        resp.addCookie(cookie);

        // Invalidar sesión después de guardar cookie
        if (session != null) {
            session.invalidate();
        }

        // Si no hay token, redirigimos localmente
        if (idToken == null || idToken.isBlank()) {
            String redirectPage = "admin".equals(loginType)
                    ? "/vistas/index_admin_login.jsp"
                    : "/index.jsp";
            resp.sendRedirect(req.getContextPath() + redirectPage + "?logged_out=true");
            return;
        }

        // Redirigir al logout de IdUruguay
        String logoutUrl = LOGOUT_ENDPOINT
                + "?id_token_hint=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8)
                + "&post_logout_redirect_uri=" + URLEncoder.encode(POST_LOGOUT_REDIRECT_URI, StandardCharsets.UTF_8)
                + "&state=logout_done";

        resp.sendRedirect(logoutUrl);
    }

    /**
     * Obtiene el tipo de login ("user" o "admin") desde la cookie si existe.
     */
    private String getLoginTypeFromCookie(HttpServletRequest req) {
        if (req.getCookies() == null) return "user";

        for (Cookie cookie : req.getCookies()) {
            if ("login_type".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "user";
    }
}
