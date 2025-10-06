package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.api.UsuarioServicioSaludServiceLocal;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;

import java.util.List;

/**
 * REST endpoint for UsuarioServicioSalud operations.
 * Base path: /api/usuarios
 */
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioServicioSaludResource {

    @EJB
    private UsuarioServicioSaludServiceLocal usuarioService;

    /**
     * GET /api/usuarios - List all users
     */
    @GET
    public Response listar() {
        try {
            List<UsuarioServicioSalud> usuarios = usuarioService.listar();
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * GET /api/usuarios/{ci} - Get user by cedula
     */
    @GET
    @Path("/{ci}")
    public Response obtenerPorCI(@PathParam("ci") String ci) {
        try {
            UsuarioServicioSalud usuario = usuarioService.obtenerPorCI(ci);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Usuario no encontrado")).build();
            }
            return Response.ok(usuario).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * POST /api/usuarios - Create new user
     */
    @POST
    public Response crear(UsuarioServicioSalud usuario) {
        try {
            UsuarioServicioSalud creado = usuarioService.crear(usuario);
            return Response.status(Response.Status.CREATED).entity(creado).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * PUT /api/usuarios/{ci} - Update user
     */
    @PUT
    @Path("/{ci}")
    public Response actualizar(@PathParam("ci") String ci, UsuarioServicioSalud usuario) {
        try {
            usuario.setCedulaIdentidad(ci);
            usuarioService.actualizar(usuario);
            return Response.ok(usuario).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * DELETE /api/usuarios/{ci} - Delete user
     */
    @DELETE
    @Path("/{ci}")
    public Response eliminar(@PathParam("ci") String ci) {
        try {
            usuarioService.eliminar(ci);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * Error response class
     */
    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
