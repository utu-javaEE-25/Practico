package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.api.HistoriaClinicaServiceLocal;
import uy.edu.fing.tse.dto.DocumentoMetadataDTO;
import uy.edu.fing.tse.entidades.DocumentoClinicoMetadata;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.persistencia.DocumentoClinicoMetadataDAO;
import uy.edu.fing.tse.servicios.AccesoNoAutorizadoException;
import uy.edu.fing.tse.servicios.AccesoNoAutorizadoException;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;


import java.util.List;

@Path("/historia-clinica")
public class HistoriaClinicaResource {

    @EJB private HistoriaClinicaServiceLocal historiaService;
    @EJB private PrestadorSaludPerLocal prestadorDAO;
    @EJB private DocumentoClinicoMetadataDAO metadataDAO;

    @GET
    @Path("/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHistoriaPorCedula(@PathParam("cedula") String cedula) {
        try {
            List<DocumentoMetadataDTO> historia = historiaService.getHistoriaMetadataPorCedula(cedula);
            return Response.ok(historia).build();
        } catch (Exception e) {
            return Response.serverError().entity("Error al obtener la historia clínica: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/documento-externo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDocumentoExterno(
            @QueryParam("docId") String docId,
            @QueryParam("cedulaPaciente") String cedulaPaciente,
            @QueryParam("schemaSolicitante") String schemaSolicitante,
            @QueryParam("idProfesional") Long idProfesional) {
        
        try {
            if (idProfesional == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("El ID del profesional es obligatorio.").build();
            }
            
            PrestadorSalud solicitante = prestadorDAO.obtenerPorSchema(schemaSolicitante);
            if (solicitante == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Tenant solicitante no válido.").build();
            }

            DocumentoClinicoMetadata metadata = metadataDAO.findByIdExternaDoc(docId);
            if (metadata == null) return Response.status(Response.Status.NOT_FOUND).entity("Documento no encontrado en el índice.").build();

            Object documento = historiaService.verificarYObtenerDocumento(cedulaPaciente, docId, solicitante.getTenantId(), idProfesional, metadata.getDocId());
            return Response.ok(documento).build();

        } catch (AccesoNoAutorizadoException e) {
            // Si el servicio lanza nuestra excepción, devolvemos un 403 Forbidden.
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity("Error interno al procesar la solicitud.").build();
        }
    }
}
