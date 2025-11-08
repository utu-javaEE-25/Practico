package uy.edu.fing.tse.controladores;

import java.io.IOException;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.api.AdminGlobalServiceLocal;
import uy.edu.fing.tse.entidades.AdminHcen;

@WebServlet("/index_admin")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DASHBOARD_JSP = "/vistas/index_admin.jsp";

    @EJB
    private AdminGlobalServiceLocal adminService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!esSesionAdmin(session)) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        List<AdminHcen> administradores = adminService.listarAdministradores();

        req.setAttribute("administradores", administradores);

        transferirMensaje(session, req, "admin_success");
        transferirMensaje(session, req, "admin_error");

        req.getRequestDispatcher(DASHBOARD_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!esSesionAdmin(session)) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String gubUyId = req.getParameter("gubUyId");
        String email = req.getParameter("email");

        try {
            AdminHcen nuevo = adminService.crearAdminManual(gubUyId, email);
            session.setAttribute("admin_success", "Se registro al administrador " + nuevo.getEmail() + ".");
        } catch (Exception e) {
            session.setAttribute("admin_error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/index_admin");
    }

    private boolean esSesionAdmin(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object flag = session.getAttribute("isAdmin");
        return flag instanceof Boolean && (Boolean) flag;
    }

    private void transferirMensaje(HttpSession session, HttpServletRequest req, String key) {
        if (session == null) {
            return;
        }
        Object value = session.getAttribute(key);
        if (value != null) {
            req.setAttribute(key, value);
            session.removeAttribute(key);
        }
    }
}
