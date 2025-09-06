package uy.edu.fing.tse.controladores;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;

@WebServlet("/prestadorSalud")
public class PrestadorSaludServlet extends HttpServlet {

    @EJB
    private PrestadorSaludServiceLocal prestadorService;

   @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String accion = req.getParameter("accion");

    if ("eliminar".equals(accion)) {
        try {
            String rut = req.getParameter("rut");
            prestadorService.eliminar(rut);  // <-- ver método del servicio abajo
            resp.sendRedirect(req.getContextPath() + "/prestadorSalud");
            return;
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
            return;
        }
    }

    
    PrestadorSalud p = new PrestadorSalud();
    p.setNombre(req.getParameter("nombre"));
    p.setRut(req.getParameter("rut"));
    p.setFechaAlta(java.time.LocalDate.now());
    p.setActivo(true);

    try {
        prestadorService.crear(p);
        resp.sendRedirect(req.getContextPath() + "/prestadorSalud");
    } catch (IllegalArgumentException e) {
        req.setAttribute("error", e.getMessage());
        doGet(req, resp);
    }
}


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<PrestadorSalud> lista = prestadorService.listar();

        req.setAttribute("listaPrestadorSalud", lista);
        req.getRequestDispatcher("/vistas/prestadorSalud.jsp").forward(req, resp);
    }
}
