package uy.edu.fing.tse.controladores;

import java.io.IOException;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.api.AdminReportServiceLocal;
import uy.edu.fing.tse.dto.reportes.ActividadUsuariosDTO;
import uy.edu.fing.tse.dto.reportes.ResumenGeneralDTO;

@WebServlet("/reportes_admin")
public class AdminReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String REPORTES_JSP = "/vistas/reportes_admin.jsp";

    @EJB
    private AdminReportServiceLocal reportService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!esSesionAdmin(session)) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        ResumenGeneralDTO resumen = reportService.obtenerResumenGeneral();
        ActividadUsuariosDTO actividad = reportService.obtenerActividadUsuarios();

        req.setAttribute("resumenGeneral", resumen);
        req.setAttribute("actividadUsuarios", actividad);

        req.getRequestDispatcher(REPORTES_JSP).forward(req, resp);
    }

    private boolean esSesionAdmin(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object flag = session.getAttribute("isAdmin");
        return flag instanceof Boolean && (Boolean) flag;
    }
}
