package uy.edu.fing.tse.controladores;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import uy.edu.fing.tse.api.DocumentoClinicoServiceLocal;
import uy.edu.fing.tse.entidades.DocumentoClinico;

@WebServlet("/documentoClinico")
public class DocumentoClinicoServlet extends HttpServlet {

    @EJB
    private DocumentoClinicoServiceLocal docService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String buscarCI = trim(req.getParameter("buscarCI"));
        String buscarCodigo = trim(req.getParameter("buscarCodigo"));

        List<DocumentoClinico> lista;
        if (!isEmpty(buscarCodigo)) {
            DocumentoClinico doc = docService.obtenerPorCodigo(buscarCodigo);
            lista = (doc != null) ? Collections.singletonList(doc) : Collections.emptyList();
        } else if (!isEmpty(buscarCI)) {
            lista = docService.buscarPorPacienteCI(buscarCI);
        } else {
            lista = docService.listar();
        }

        req.setAttribute("listaDocs", lista);
        req.getRequestDispatcher("/vistas/documentoClinico.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String accion = req.getParameter("accion");

        if ("eliminar".equals(accion)) {
            try {
                docService.eliminarPorCodigo(req.getParameter("codigo"));
                resp.sendRedirect(req.getContextPath() + "/documentoClinico");
                return;
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
                doGet(req, resp);
                return;
            }
        }

        try {
            DocumentoClinico d = new DocumentoClinico();
            d.setCodigo(req.getParameter("codigo"));
            d.setPacienteCI(req.getParameter("pacienteCI"));
            d.setPrestadorRUT(req.getParameter("prestadorRUT"));
            d.setTipo(req.getParameter("tipo"));
            d.setContenido(req.getParameter("contenido"));
            d.setFirmado("on".equals(req.getParameter("firmado")));

            String fechaStr = req.getParameter("fechaEmision");
            if (fechaStr != null && !fechaStr.isBlank()) {
                d.setFechaEmision(LocalDate.parse(fechaStr));
            }

            docService.crear(d);
            resp.sendRedirect(req.getContextPath() + "/documentoClinico");

        } catch (EJBException ex) {
            String msg = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getMessage();
            req.setAttribute("error", msg);
            doGet(req, resp);
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            doGet(req, resp);
        }
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }
}
