package uy.edu.fing.tse.controladores;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uy.edu.fing.tse.api.TenantPerLocal;
import uy.edu.fing.tse.entidades.Tenant;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para gestionar tenants y seleccionar el tenant activo.
 * Demuestra el enfoque de multitenancy con tabla única y FK a tenant.
 */
@WebServlet("/tenant")
public class TenantServlet extends HttpServlet {

    @EJB
    private TenantPerLocal tenantPer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");

        if ("seleccionar".equals(accion)) {
            String tenantId = req.getParameter("tenantId");
            if (tenantId != null && !tenantId.isEmpty()) {
                req.getSession().setAttribute("tenantId", tenantId);
                resp.sendRedirect(req.getContextPath() + "/tenant");
                return;
            }
        }

        // Obtener el tenant actual de la sesión
        String currentTenantId = (String) req.getSession().getAttribute("tenantId");
        req.setAttribute("currentTenantId", currentTenantId);

        // Listar todos los tenants
        List<Tenant> tenants = tenantPer.listar();
        req.setAttribute("listaTenants", tenants);

        req.getRequestDispatcher("/vistas/tenant.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String accion = req.getParameter("accion");

        if ("crear".equals(accion)) {
            try {
                Tenant tenant = new Tenant();
                tenant.setCodigo(req.getParameter("codigo"));
                tenant.setNombre(req.getParameter("nombre"));
                tenant.setEsquema(req.getParameter("esquema"));
                tenant.setActivo(true);

                if (tenantPer.existeCodigo(tenant.getCodigo())) {
                    throw new IllegalArgumentException("Ya existe un tenant con el código: " + tenant.getCodigo());
                }

                tenantPer.crear(tenant);
                resp.sendRedirect(req.getContextPath() + "/tenant");
                return;
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
                doGet(req, resp);
                return;
            }
        }

        if ("limpiarTenant".equals(accion)) {
            req.getSession().removeAttribute("tenantId");
            resp.sendRedirect(req.getContextPath() + "/tenant");
            return;
        }

        doGet(req, resp);
    }
}
