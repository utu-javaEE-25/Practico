package uy.edu.fing.tse.controladores;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import uy.edu.fing.tse.api.AuditLogServiceLocal;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.audit.AuditHelper;
import uy.edu.fing.tse.audit.AuditLogConstants;
import uy.edu.fing.tse.entidades.PrestadorSalud;

@WebServlet("/prestadorSalud")
public class PrestadorSaludServlet extends HttpServlet {

    @EJB
    private PrestadorSaludServiceLocal prestadorService;
    @EJB
    private AuditLogServiceLocal auditLogService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");

        if ("eliminar".equals(accion)) {
            accion = "desactivar";
        }

        if ("desactivar".equals(accion)) {
            try {
                String rut = req.getParameter("rut");
                PrestadorSalud prestador = prestadorService.obtener(rut);
                prestadorService.desactivar(rut);
                AuditHelper.registrarEvento(
                        auditLogService,
                        req,
                        null,
                        AuditLogConstants.Acciones.PRESTADOR_BAJA,
                        prestador != null ? prestador.getTenantId() : null,
                        AuditLogConstants.Resultados.SUCCESS);
                resp.sendRedirect(req.getContextPath() + "/prestadorSalud");
                return;
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
                doGet(req, resp);
                return;
            }
        } else if ("activar".equals(accion)) {
            try {
                String rut = req.getParameter("rut");
                PrestadorSalud prestador = prestadorService.obtener(rut);
                prestadorService.activar(rut);
                AuditHelper.registrarEvento(
                        auditLogService,
                        req,
                        null,
                        AuditLogConstants.Acciones.PRESTADOR_ACTIVACION,
                        prestador != null ? prestador.getTenantId() : null,
                        AuditLogConstants.Resultados.SUCCESS);
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
        LocalDateTime ahora = LocalDateTime.now();
        p.setFechaCreacion(ahora);
        p.setFechaModificacion(ahora);
        p.setEstado(Boolean.TRUE);

        try {
            PrestadorSalud creado = prestadorService.crear(p);
            AuditHelper.registrarEvento(
                    auditLogService,
                    req,
                    null,
                    AuditLogConstants.Acciones.PRESTADOR_ALTA,
                    creado != null ? creado.getTenantId() : null,
                    AuditLogConstants.Resultados.SUCCESS);
            resp.sendRedirect(req.getContextPath() + "/prestadorSalud");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String buscarRut = trim(req.getParameter("buscarRut"));
        String buscarNombre = trim(req.getParameter("buscarNombre"));

        List<PrestadorSalud> lista = prestadorService.listar();
        List<PrestadorSalud> resultado = new ArrayList<>();

        if (isEmpty(buscarRut) && isEmpty(buscarNombre)) {
            resultado = lista;
        } else {
            for (PrestadorSalud p : lista) {
                boolean match = true;

                if (!isEmpty(buscarRut)) {
                    match = match && p.getRut() != null &&
                            p.getRut().trim().equalsIgnoreCase(buscarRut);
                }
                if (!isEmpty(buscarNombre)) {
                    match = match && p.getNombre() != null &&
                            p.getNombre().toLowerCase().contains(buscarNombre.toLowerCase());
                }

                if (match) {
                    resultado.add(p);
                }
            }
        }

        req.setAttribute("listaPrestadorSalud", resultado);
        req.getRequestDispatcher("/vistas/prestadorSalud.jsp").forward(req, resp);
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
