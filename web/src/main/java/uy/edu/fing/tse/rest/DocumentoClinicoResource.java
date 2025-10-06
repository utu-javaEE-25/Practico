package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.api.DocumentoClinicoServiceLocal;
import uy.edu.fing.tse.entidades.DocumentoClinico;

import java.util.List;

/**
 * REST endpoint for DocumentoClinico operations.
 * Base path: /api/documentos
 */
@Path("/documentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentoClinicoResource {

    @EJB
    private DocumentoClinicoServiceLocal documentoService;

    /**
     * GET /api/documentos - List all documents
     */
    @GET
    public Response listar(@QueryParam("pacienteCI") String pacienteCI) {
        try {
            List<DocumentoClinico> documentos;
            if (pacienteCI != null && !pacienteCI.trim().isEmpty()) {
                documentos = documentoService.buscarPorPacienteCI(pacienteCI);
            } else {
                documentos = documentoService.listar();
            }
            return Response.ok(documentos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * GET /api/documentos/{codigo} - Get document by codigo
     */
    @GET
    @Path("/{codigo}")
    public Response obtenerPorCodigo(@PathParam("codigo") String codigo) {
        try {
            DocumentoClinico documento = documentoService.obtenerPorCodigo(codigo);
            if (documento == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Documento no encontrado")).build();
            }
            return Response.ok(documento).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * POST /api/documentos - Create new document
     */
    @POST
    public Response crear(DocumentoClinico documento) {
        try {
            DocumentoClinico creado = documentoService.crear(documento);
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
     * PUT /api/documentos/{codigo} - Update document
     */
    @PUT
    @Path("/{codigo}")
    public Response actualizar(@PathParam("codigo") String codigo, DocumentoClinico documento) {
        try {
            documento.setCodigo(codigo);
            documentoService.actualizar(documento);
            return Response.ok(documento).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * DELETE /api/documentos/{codigo} - Delete document
     */
    @DELETE
    @Path("/{codigo}")
    public Response eliminar(@PathParam("codigo") String codigo) {
        try {
            documentoService.eliminarPorCodigo(codigo);
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
