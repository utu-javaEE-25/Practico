package uy.edu.fing.tse.controladores;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.api.HistoriaClinicaServiceLocal;
import uy.edu.fing.tse.dto.DocumentoDetalleDTO;

import java.io.IOException;

@WebServlet("/documento")
public class VerDocumentoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private HistoriaClinicaServiceLocal historiaService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario_id") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        try {
            String docIdExterno = req.getParameter("docId");
            Long custodioId = Long.parseLong(req.getParameter("custodioId"));
            
            DocumentoDetalleDTO documento = historiaService.getDocumentoDetalle(docIdExterno, custodioId);
            req.setAttribute("documento", documento);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error al obtener el documento: " + e.getMessage());
        }
        
        req.getRequestDispatcher("/vistas/verDocumento.jsp").forward(req, resp);
    }
}
