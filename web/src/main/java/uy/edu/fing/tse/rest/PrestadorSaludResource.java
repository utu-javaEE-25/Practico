package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;

import java.util.List;

/**
 * REST endpoint for PrestadorSalud operations.
 * Base path: /api/prestadores
 */
@Path("/prestadores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PrestadorSaludResource {

    @EJB
    private PrestadorSaludServiceLocal prestadorService;

    /**
     * GET /api/prestadores - List all health providers
     */
    @GET
    public Response listar() {
        try {
            List<PrestadorSalud> prestadores = prestadorService.listar();
            return Response.ok(prestadores).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * GET /api/prestadores/{rut} - Get provider by RUT
     */
    @GET
    @Path("/{rut}")
    public Response obtenerPorRut(@PathParam("rut") String rut) {
        try {
            PrestadorSalud prestador = prestadorService.obtener(rut);
            if (prestador == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Prestador no encontrado")).build();
            }
            return Response.ok(prestador).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * POST /api/prestadores - Create new provider
     */
    @POST
    public Response crear(PrestadorSalud prestador) {
        try {
            PrestadorSalud creado = prestadorService.crear(prestador);
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
     * PUT /api/prestadores/{rut} - Update provider
     */
    @PUT
    @Path("/{rut}")
    public Response actualizar(@PathParam("rut") String rut, PrestadorSalud prestador) {
        try {
            prestador.setRut(rut);
            prestadorService.actualizar(prestador);
            return Response.ok(prestador).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * DELETE /api/prestadores/{rut} - Delete provider
     */
    @DELETE
    @Path("/{rut}")
    public Response eliminar(@PathParam("rut") String rut) {
        try {
            prestadorService.eliminar(rut);
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
