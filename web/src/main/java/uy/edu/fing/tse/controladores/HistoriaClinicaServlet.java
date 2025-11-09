package uy.edu.fing.tse.controladores;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.api.HistoriaClinicaServiceLocal;
import uy.edu.fing.tse.dto.DocumentoMetadataDTO;

import java.io.IOException;
import java.util.List;

@WebServlet("/historia")
public class HistoriaClinicaServlet extends HttpServlet {

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
            Long usuarioId = (Long) session.getAttribute("usuario_id");
            List<DocumentoMetadataDTO> metadatos = historiaService.getHistoriaMetadata(usuarioId);
            req.setAttribute("historiaMetadata", metadatos);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "No se pudo recuperar el índice de su historia clínica: " + e.getMessage());
        }
        
        req.getRequestDispatcher("/vistas/historiaClinica.jsp").forward(req, resp);
    }
}
