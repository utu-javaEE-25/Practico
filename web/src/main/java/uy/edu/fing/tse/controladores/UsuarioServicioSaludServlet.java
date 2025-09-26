package uy.edu.fing.tse.controladores;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uy.edu.fing.tse.api.UsuarioServicioSaludServiceLocal;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/usuarioServicioSalud")
public class UsuarioServicioSaludServlet extends HttpServlet {

    @EJB
    private UsuarioServicioSaludServiceLocal usuarioService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Obtenemos la lista completa de usuarios
        List<UsuarioServicioSalud> lista = usuarioService.listar();

        // La pasamos a la vista (JSP) a través de un atributo en el request
        req.setAttribute("listaUsuarios", lista);
        req.getRequestDispatcher("/vistas/usuarioServicioSalud.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8"); // Para manejar correctamente tildes y caracteres especiales
        String accion = req.getParameter("accion");

        // Lógica para eliminar un usuario
        if ("eliminar".equals(accion)) {
            try {
                String cedula = req.getParameter("cedulaIdentidad");
                usuarioService.eliminar(cedula);
                // Redirigimos para recargar la lista actualizada
                resp.sendRedirect(req.getContextPath() + "/usuarioServicioSalud");
                return;
            } catch (Exception e) {
                // Si hay un error, lo mostramos en la página
                req.setAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
                doGet(req, resp);
                return;
            }
        }

        // Lógica para crear un nuevo usuario (es la acción por defecto si no es "eliminar")
        try {
            UsuarioServicioSalud u = new UsuarioServicioSalud();
            u.setNombreCompleto(req.getParameter("nombreCompleto"));
            u.setCedulaIdentidad(req.getParameter("cedulaIdentidad"));

            // El input type="date" devuelve un String "YYYY-MM-DD", hay que convertirlo a LocalDate
            String fechaNacimientoStr = req.getParameter("fechaNacimiento");
            if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
                u.setFechaNacimiento(LocalDate.parse(fechaNacimientoStr));
            }

            u.setActivo(true); // Valor por defecto al crear

            usuarioService.crear(u);
            // Redirigimos para mostrar la nueva lista
            resp.sendRedirect(req.getContextPath() + "/usuarioServicioSalud");

        } catch (IllegalArgumentException e) {
            // Capturamos el error de la regla de negocio (ej: CI duplicada)
            req.setAttribute("error", e.getMessage());
            // Volvemos a mostrar el formulario con el mensaje de error
            doGet(req, resp);
        } catch (Exception e) {
            // Capturamos otros posibles errores (ej: fecha inválida)
            req.setAttribute("error", "Error al crear el usuario: " + e.getMessage());
            doGet(req, resp);
        }
    }
}