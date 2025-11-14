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
import uy.edu.fing.tse.entidades.SolicitudAcceso;
import uy.edu.fing.tse.persistencia.SolicitudAccesoDAO;
import uy.edu.fing.tse.servicios.GestionPermisosService;

@WebServlet("/notificaciones")
public class PortalNotificacionesServlet extends HttpServlet {

    @EJB private SolicitudAccesoDAO solicitudDAO;
    @EJB private GestionPermisosService permisosService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Long userId = (Long) session.getAttribute("usuario_id");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Transferir mensajes de la sesión al request para mostrarlos una vez
        transferirMensaje(session, req, "successMessage");
        transferirMensaje(session, req, "errorMessage");

        List<SolicitudAcceso> solicitudes = solicitudDAO.findPendientesPorUsuario(userId);
        req.setAttribute("solicitudesPendientes", solicitudes);
        
        req.getRequestDispatcher("/vistas/notificaciones.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Long userId = (Long) session.getAttribute("usuario_id");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        try {
            Long solicitudId = Long.parseLong(req.getParameter("solicitudId"));
            int diasVigencia = Integer.parseInt(req.getParameter("diasVigencia"));
            
            permisosService.aprobarSolicitud(solicitudId, userId, diasVigencia);
            
            session.setAttribute("successMessage", "Permiso concedido exitosamente.");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Datos de solicitud inválidos.");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al procesar la solicitud: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/notificaciones");
    }
    
    private void transferirMensaje(HttpSession session, HttpServletRequest req, String key) {
        if (session != null) {
            Object value = session.getAttribute(key);
            if (value != null) {
                req.setAttribute(key, value);
                session.removeAttribute(key);
            }
        }
    }
}