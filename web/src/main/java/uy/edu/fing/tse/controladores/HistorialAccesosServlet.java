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
import uy.edu.fing.tse.persistencia.AccesoDocumentoDAO;

@WebServlet("/historialAccesos")
public class HistorialAccesosServlet extends HttpServlet {

    @EJB
    private AccesoDocumentoDAO accesoDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        
        // El 'cedulaIdentidad' se guarda en la sesión durante el callback del login
        String pacienteCi = (session != null) ? (String) session.getAttribute("cedulaIdentidad") : null;

        if (pacienteCi == null) {
            // Si por alguna razón no está en la sesión, redirigimos al login
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Llamamos al DAO refactorizado
        List<Object[]> accesos = accesoDAO.obtenerAccesosPorCedula(pacienteCi);
        
        // Pasamos la lista de arrays de objetos directamente al JSP
        req.setAttribute("accesos", accesos);

        req.getRequestDispatcher("/vistas/historial_accesos.jsp").forward(req, resp);
    }
}