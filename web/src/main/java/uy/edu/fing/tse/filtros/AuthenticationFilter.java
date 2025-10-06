package uy.edu.fing.tse.filtros;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtro de autenticación que protege las páginas de la aplicación.
 * Redirige a la página de login si el usuario no está autenticado.
 */
@WebFilter(urlPatterns = {"*.xhtml", "/usuarioServicioSalud", "/prestadorSalud", "/documentoClinico"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No se requiere inicialización
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        
        // Permitir acceso a la página de login y recursos relacionados
        if (requestURI.endsWith("login.jsp") || 
            requestURI.contains("/login") || 
            requestURI.contains("/logout") ||
            requestURI.contains("/jakarta.faces.resource/")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Verificar si el usuario está autenticado
        HttpSession session = httpRequest.getSession(false);
        boolean isAuthenticated = (session != null && session.getAttribute("usuario") != null);
        
        if (isAuthenticated) {
            // Usuario autenticado, permitir acceso
            chain.doFilter(request, response);
        } else {
            // Usuario no autenticado, redirigir a login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {
        // No se requiere limpieza
    }
}
