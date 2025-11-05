package uy.edu.fing.tse.controladores;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.api.AdminGlobalServiceLocal;
import uy.edu.fing.tse.entidades.AdminHcen;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.UsuarioDAO;

@WebServlet("/index_admin")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DASHBOARD_JSP = "/vistas/index_admin.jsp";

    @EJB
    private UsuarioDAO usuarioDAO;

    @EJB
    private AdminGlobalServiceLocal adminService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!esSesionAdmin(session)) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        List<UsuarioServicioSalud> usuarios = usuarioDAO.listarTodos();
        List<AdminHcen> administradores = adminService.listarAdministradores();

        Set<String> adminSubs = new HashSet<>();
        Set<String> adminEmails = new HashSet<>();
        administradores.forEach(a -> {
            if (a.getGubUyId() != null) {
                adminSubs.add(a.getGubUyId());
            }
            if (a.getEmail() != null) {
                adminEmails.add(a.getEmail().toLowerCase());
            }
        });

        req.setAttribute("usuarios", usuarios);
        req.setAttribute("administradores", administradores);
        req.setAttribute("adminSubs", adminSubs);
        req.setAttribute("adminEmails", adminEmails);

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

        String idParam = req.getParameter("userId");
        if (idParam == null || idParam.isBlank()) {
            session.setAttribute("admin_error", "Debe seleccionar un usuario válido.");
            resp.sendRedirect(req.getContextPath() + "/index_admin");
            return;
        }

        try {
            Long userId = Long.parseLong(idParam);
            UsuarioServicioSalud usuario = usuarioDAO.buscarPorId(userId);
            if (usuario == null) {
                session.setAttribute("admin_error", "No se encontró el usuario indicado.");
            } else {
                adminService.convertirUsuarioEnAdmin(usuario);
                session.setAttribute("admin_success", "El usuario " + usuario.getEmail() + " fue promovido a administrador.");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("admin_error", "Identificador de usuario inválido.");
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
