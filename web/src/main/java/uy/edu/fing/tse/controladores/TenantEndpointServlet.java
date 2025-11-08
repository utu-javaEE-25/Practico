package uy.edu.fing.tse.controladores;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.api.AuditLogServiceLocal;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.api.TenantEndpointServiceLocal;
import uy.edu.fing.tse.audit.AuditHelper;
import uy.edu.fing.tse.audit.AuditLogConstants;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.entidades.TenantEndpoint;

@WebServlet("/tenant_endpoints")
public class TenantEndpointServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String VISTA = "/vistas/tenant_endpoints.jsp";

    @EJB
    private PrestadorSaludServiceLocal prestadorService;

    @EJB
    private TenantEndpointServiceLocal endpointService;
    @EJB
    private AuditLogServiceLocal auditService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!esSesionAdmin(session)) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        List<PrestadorSalud> prestadores = prestadorService.listar();
        Map<Long, TenantEndpoint> endpointsPorTenant = endpointService.listar()
                .stream()
                .collect(Collectors.toMap(TenantEndpoint::getTenantId, e -> e, (a, b) -> a, HashMap::new));

        List<PrestadorSalud> prestadoresSinEndpoint = prestadores.stream()
                .filter(p -> p.getTenantId() != null && !endpointsPorTenant.containsKey(p.getTenantId()))
                .collect(Collectors.toList());

        String editParam = req.getParameter("editTenantId");
        TenantEndpoint endpointEnEdicion = null;
        PrestadorSalud prestadorEnEdicion = null;
        if (editParam != null && !editParam.isBlank()) {
            try {
                Long tenantId = Long.parseLong(editParam);
                endpointEnEdicion = endpointsPorTenant.get(tenantId);
                prestadorEnEdicion = buscarPrestadorPorId(prestadores, tenantId);
                if (endpointEnEdicion == null) {
                    req.setAttribute("endpoint_error", "El tenant seleccionado no tiene un endpoint configurado.");
                }
            } catch (NumberFormatException ex) {
                req.setAttribute("endpoint_error", "Identificador de tenant invalido.");
            }
        }

        req.setAttribute("prestadores", prestadores);
        req.setAttribute("prestadoresSinEndpoint", prestadoresSinEndpoint);
        req.setAttribute("endpointsPorTenant", endpointsPorTenant);
        req.setAttribute("endpointEnEdicion", endpointEnEdicion);
        req.setAttribute("prestadorEnEdicion", prestadorEnEdicion);

        transferirMensaje(session, req, "endpoint_success");
        transferirMensaje(session, req, "endpoint_error");

        req.getRequestDispatcher(VISTA).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!esSesionAdmin(session)) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String accion = req.getParameter("accion");
        try {
            if ("crear".equalsIgnoreCase(accion)) {
                manejarCreacion(req, session);
            } else if ("actualizar".equalsIgnoreCase(accion)) {
                manejarActualizacion(req, session);
            } else if ("desactivar".equalsIgnoreCase(accion)) {
                manejarDesactivacion(req, session);
            } else {
                session.setAttribute("endpoint_error", "Accion no soportada.");
            }
        } catch (IllegalArgumentException ex) {
            session.setAttribute("endpoint_error", ex.getMessage());
        } catch (Exception ex) {
            session.setAttribute("endpoint_error", "Ocurrio un error al procesar la accion solicitada.");
        }

        resp.sendRedirect(req.getContextPath() + "/tenant_endpoints");
    }

    private void manejarCreacion(HttpServletRequest req, HttpSession session) {
        Long tenantId = parseTenantId(req.getParameter("tenantId"));
        if (tenantId == null) {
            session.setAttribute("endpoint_error", "Debe seleccionar un tenant valido.");
            return;
        }

        String uriBase = req.getParameter("uriBase");
        String tipoAuth = req.getParameter("tipoAuth");
        String hashCliente = req.getParameter("hashCliente");

        endpointService.crear(tenantId, uriBase, tipoAuth, hashCliente);
        AuditHelper.registrarEvento(
                auditService,
                req,
                AuditLogConstants.Acciones.ENDPOINT_ALTA,
                tenantId,
                AuditLogConstants.Resultados.SUCCESS);

        String nombrePrestador = obtenerNombrePrestador(tenantId);
        session.setAttribute("endpoint_success", "Endpoint creado para " + nombrePrestador + ".");
    }

    private void manejarActualizacion(HttpServletRequest req, HttpSession session) {
        Long tenantId = parseTenantId(req.getParameter("tenantId"));
        if (tenantId == null) {
            session.setAttribute("endpoint_error", "Debe indicar el tenant a modificar.");
            return;
        }

        String uriBase = req.getParameter("uriBase");
        String tipoAuth = req.getParameter("tipoAuth");
        String hashCliente = req.getParameter("hashCliente");
        boolean activo = req.getParameter("activo") != null;

        endpointService.actualizar(tenantId, uriBase, tipoAuth, hashCliente, activo);
        AuditHelper.registrarEvento(
                auditService,
                req,
                AuditLogConstants.Acciones.ENDPOINT_MODIFICACION,
                tenantId,
                AuditLogConstants.Resultados.SUCCESS);

        String nombrePrestador = obtenerNombrePrestador(tenantId);
        session.setAttribute("endpoint_success", "Endpoint actualizado para " + nombrePrestador + ".");
    }

    private void manejarDesactivacion(HttpServletRequest req, HttpSession session) {
        Long tenantId = parseTenantId(req.getParameter("tenantId"));
        if (tenantId == null) {
            session.setAttribute("endpoint_error", "Identificador de tenant invalido.");
            return;
        }

        endpointService.desactivar(tenantId);
        AuditHelper.registrarEvento(
                auditService,
                req,
                AuditLogConstants.Acciones.ENDPOINT_BAJA,
                tenantId,
                AuditLogConstants.Resultados.SUCCESS);

        String nombrePrestador = obtenerNombrePrestador(tenantId);
        session.setAttribute("endpoint_success", "Endpoint desactivado para " + nombrePrestador + ".");
    }

    private boolean esSesionAdmin(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object flag = session.getAttribute("isAdmin");
        return flag instanceof Boolean && (Boolean) flag;
    }

    private void transferirMensaje(HttpSession session, HttpServletRequest req, String key) {
        if (session == null) {
            return;
        }
        Object value = session.getAttribute(key);
        if (value != null) {
            req.setAttribute(key, value);
            session.removeAttribute(key);
        }
    }

    private PrestadorSalud buscarPrestadorPorId(List<PrestadorSalud> prestadores, Long tenantId) {
        if (prestadores == null || tenantId == null) {
            return null;
        }
        return prestadores.stream()
                .filter(p -> tenantId.equals(p.getTenantId()))
                .findFirst()
                .orElse(null);
    }

    private String obtenerNombrePrestador(Long tenantId) {
        if (tenantId == null) {
            return "el tenant indicado";
        }
        return prestadorService.listar()
                .stream()
                .filter(p -> tenantId.equals(p.getTenantId()))
                .map(PrestadorSalud::getNombre)
                .findFirst()
                .orElse("el tenant " + tenantId);
    }

    private Long parseTenantId(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
