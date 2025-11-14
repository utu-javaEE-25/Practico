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
import uy.edu.fing.tse.entidades.AuditLog;
import uy.edu.fing.tse.persistencia.AccesoDocumentoDAO;

@WebServlet("/historialAccesos")
public class HistorialAccesosServlet extends HttpServlet {

    @EJB
    private AccesoDocumentoDAO accesoDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("cedulaIdentidad") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String pacienteCi = (String) session.getAttribute("cedulaIdentidad");

        List<AuditLog> accesos = accesoDAO.obtenerAccesosPorCedula(pacienteCi);
        
        req.setAttribute("accesos", accesos);

        req.getRequestDispatcher("/vistas/historial_accesos.jsp").forward(req, resp);
    }
}