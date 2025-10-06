package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.api.TrabajadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.TrabajadorSalud;

import java.util.List;

/**
 * REST endpoint for TrabajadorSalud operations.
 * Base path: /api/trabajadores
 */
@Path("/trabajadores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrabajadorSaludResource {

    @EJB
    private TrabajadorSaludServiceLocal trabajadorService;

    /**
     * GET /api/trabajadores - List all workers
     */
    @GET
    public Response listar() {
        try {
            List<TrabajadorSalud> trabajadores = trabajadorService.obtenerTodosLosTrabajadores();
            return Response.ok(trabajadores).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * GET /api/trabajadores/{cedula} - Get worker by cedula
     */
    @GET
    @Path("/{cedula}")
    public Response obtenerPorCedula(@PathParam("cedula") String cedula) {
        try {
            TrabajadorSalud trabajador = trabajadorService.obtenerTrabajadorPorCedula(cedula);
            if (trabajador == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Trabajador no encontrado")).build();
            }
            return Response.ok(trabajador).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * POST /api/trabajadores - Create new worker
     */
    @POST
    public Response crear(TrabajadorSalud trabajador) {
        try {
            trabajadorService.altaTrabajador(trabajador);
            return Response.status(Response.Status.CREATED).entity(trabajador).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
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
