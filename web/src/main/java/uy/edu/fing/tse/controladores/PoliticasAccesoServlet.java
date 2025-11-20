package uy.edu.fing.tse.controladores;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.entidades.DocumentoClinicoMetadata;
import uy.edu.fing.tse.entidades.PoliticaAcceso;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.persistencia.DocumentoClinicoMetadataDAO;
import uy.edu.fing.tse.persistencia.PoliticaAccesoDAO;
import uy.edu.fing.tse.servicios.GestionPermisosService;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;

@WebServlet("/politicas")
public class PoliticasAccesoServlet extends HttpServlet {

    @EJB private PoliticaAccesoDAO politicaDAO;
    @EJB private GestionPermisosService permisosService;
    @EJB private PrestadorSaludServiceLocal prestadorService;
    @EJB private DocumentoClinicoMetadataDAO metadataDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Long userId = session != null ? (Long) session.getAttribute("usuario_id") : null;
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        transferirMensaje(session, req, "successMessage");
        transferirMensaje(session, req, "errorMessage");

        List<PoliticaAcceso> politicas = politicaDAO.listarPorUsuario(userId);
        List<PrestadorSalud> prestadores = prestadorService.listar();
        List<DocumentoClinicoMetadata> documentos = metadataDAO.findByUserId(userId);

        Map<Long, PrestadorSalud> prestadoresPorId = prestadores.stream()
                .filter(p -> p.getTenantId() != null)
                .collect(Collectors.toMap(PrestadorSalud::getTenantId, Function.identity(), (a, b) -> a));

        req.setAttribute("politicas", politicas);
        req.setAttribute("prestadores", prestadores);
        req.setAttribute("prestadoresMapa", prestadoresPorId);
        req.setAttribute("documentos", documentos);

        req.getRequestDispatcher("/vistas/politicas_acceso.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Long userId = session != null ? (Long) session.getAttribute("usuario_id") : null;
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("delete".equalsIgnoreCase(action)) {
                Long politicaId = parseLong(req.getParameter("politicaId"));
                permisosService.revocarPolitica(politicaId, userId);
                session.setAttribute("successMessage", "Politica revocada.");
            } else {
                Long tenantId = parseLong(req.getParameter("tenantId"));
                Long profesionalId = parseLong(req.getParameter("profesionalId"));
                Long docId = parseLong(req.getParameter("docId"));
                Integer vigenciaDias = parseInt(req.getParameter("vigenciaDias"));

                if (profesionalId != null && profesionalId <= 0) {
                    session.setAttribute("errorMessage", "El ID de profesional debe ser mayor a cero.");
                    resp.sendRedirect(req.getContextPath() + "/politicas");
                    return;
                }

                LocalDateTime desde = LocalDateTime.now();
                LocalDateTime hasta = (vigenciaDias != null && vigenciaDias > 0) ? desde.plusDays(vigenciaDias) : null;

                permisosService.crearPoliticaManual(userId, tenantId, profesionalId, docId, desde, hasta);
                session.setAttribute("successMessage", "Politica creada correctamente.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "No se pudo procesar la politica: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/politicas");
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
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
