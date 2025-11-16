package uy.edu.fing.tse.rest;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.UsuarioDAO;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {
@EJB
    private UsuarioDAO usuarioDAO;
@GET
    @Path("/ci/{cedula}")
    public Response obtenerPorCI(@PathParam("cedula") String cedula) {
        if (cedula == null || cedula.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("La cédula no puede ser nula o vacía.")
                           .build();
        }
        UsuarioServicioSalud usuario = usuarioDAO.buscarPorCI(cedula);
        if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No se encontró un usuario con la cédula: " + cedula)
                           .build();
        }
    }
}

