package uy.edu.fing.tse.controladores;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.api.UsuarioServicioSaludServiceLocal;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @EJB
    private UsuarioServicioSaludServiceLocal usuarioService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Si ya está logueado, redirigir al inicio
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            resp.sendRedirect(req.getContextPath() + "/index.xhtml");
            return;
        }
        
        // Mostrar página de login
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        String loginType = req.getParameter("loginType");
        
        if ("gubuy".equals(loginType)) {
            handleGubUyLogin(req, resp);
        } else if ("traditional".equals(loginType)) {
            handleTraditionalLogin(req, resp);
        } else {
            req.setAttribute("error", "Tipo de login no válido");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    /**
     * Simula la autenticación con gub.uy (identidad digital del gobierno uruguayo).
     * En una implementación real, esto redirigiría al proveedor OAuth de gub.uy.
     */
    private void handleGubUyLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Simulación: En una implementación real, aquí se redirigiría a gub.uy OAuth
        // y se manejaría el callback con el token de autenticación
        
        // Para efectos de demostración, simulamos que el usuario se autentica con
        // una cédula predeterminada que existe en el sistema
        try {
            // Intentamos obtener un usuario de ejemplo del sistema
            // En producción, la cédula vendría del token de gub.uy
            java.util.List<UsuarioServicioSalud> usuarios = usuarioService.listar();
            
            if (usuarios != null && !usuarios.isEmpty()) {
                // Tomamos el primer usuario activo como ejemplo
                UsuarioServicioSalud usuarioGubUy = null;
                for (UsuarioServicioSalud u : usuarios) {
                    if (u.isActivo()) {
                        usuarioGubUy = u;
                        break;
                    }
                }
                
                if (usuarioGubUy != null) {
                    // Crear sesión
                    HttpSession session = req.getSession(true);
                    session.setAttribute("usuario", usuarioGubUy);
                    session.setAttribute("loginMethod", "gub.uy");
                    session.setMaxInactiveInterval(30 * 60); // 30 minutos
                    
                    resp.sendRedirect(req.getContextPath() + "/index.xhtml");
                    return;
                } else {
                    req.setAttribute("error", "No hay usuarios activos en el sistema para autenticar con gub.uy");
                }
            } else {
                req.setAttribute("error", "No hay usuarios registrados en el sistema. Por favor, registre un usuario primero.");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error al autenticar con gub.uy: " + e.getMessage());
        }
        
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    /**
     * Maneja el login tradicional con cédula y contraseña.
     */
    private void handleTraditionalLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String cedula = req.getParameter("cedula");
        String password = req.getParameter("password");
        
        if (cedula == null || cedula.trim().isEmpty()) {
            req.setAttribute("error", "La cédula es obligatoria");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "La contraseña es obligatoria");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }
        
        try {
            // Buscar usuario por cédula
            UsuarioServicioSalud usuario = usuarioService.obtenerPorCI(cedula.trim());
            
            if (usuario != null && usuario.isActivo()) {
                // En una aplicación real, aquí se verificaría la contraseña hasheada
                // Por ahora, simplemente validamos que el usuario existe y está activo
                
                // Crear sesión
                HttpSession session = req.getSession(true);
                session.setAttribute("usuario", usuario);
                session.setAttribute("loginMethod", "traditional");
                session.setMaxInactiveInterval(30 * 60); // 30 minutos
                
                resp.sendRedirect(req.getContextPath() + "/index.xhtml");
                return;
            } else {
                req.setAttribute("error", "Usuario no encontrado o inactivo");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error al autenticar: " + e.getMessage());
        }
        
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
