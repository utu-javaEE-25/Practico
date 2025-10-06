package uy.edu.fing.tse.filtros;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.util.TenantContext;

import java.io.IOException;

/**
 * Filter to manage tenant context in a multi-tenant application.
 * Sets the current tenant based on request parameter or session attribute.
 */
@WebFilter("/*")
public class TenantFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        
        try {
            // Check if tenant is provided in request parameter
            String tenantRUT = httpRequest.getParameter("prestadorRUT");
            
            if (tenantRUT != null && !tenantRUT.trim().isEmpty()) {
                // Store in session
                session.setAttribute("currentTenantRUT", tenantRUT);
                TenantContext.setCurrentTenant(tenantRUT);
            } else {
                // Try to get from session
                tenantRUT = (String) session.getAttribute("currentTenantRUT");
                if (tenantRUT != null && !tenantRUT.trim().isEmpty()) {
                    TenantContext.setCurrentTenant(tenantRUT);
                }
            }
            
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
