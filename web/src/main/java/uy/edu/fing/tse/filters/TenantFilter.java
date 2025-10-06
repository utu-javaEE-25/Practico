package uy.edu.fing.tse.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.multitenant.TenantContext;

import java.io.IOException;

/**
 * Filtro que establece el tenant actual en el contexto de cada request.
 * El tenant se obtiene de la sesión HTTP.
 */
@WebFilter("/*")
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(false);
            
            // Obtener el tenant de la sesión
            if (session != null) {
                String tenantId = (String) session.getAttribute("tenantId");
                if (tenantId != null) {
                    TenantContext.setCurrentTenant(tenantId);
                }
            }
            
            chain.doFilter(request, response);
        } finally {
            // Limpiar el contexto al finalizar el request
            TenantContext.clear();
        }
    }
}
