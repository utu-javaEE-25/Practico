package uy.edu.fing.tse.rest;

import java.time.LocalDateTime;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;
import uy.edu.fing.tse.dto.DocumentoMetadataMensajeDTO;
import uy.edu.fing.tse.entidades.DocumentoClinicoMetadata;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.persistencia.DocumentoClinicoMetadataDAO;

@Path("/receiver")
public class ReceptorMetadataResource {

    @EJB
    private DocumentoClinicoMetadataDAO metadataDAO;
    
    @EJB
    private PrestadorSaludPerLocal prestadorDAO;

    @POST
    @Path("/metadata")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response recibirMetadata(DocumentoMetadataMensajeDTO dto) {
        try {
            // Busca el tenant por su nombre de schema para obtener el ID numérico
            PrestadorSalud prestador = prestadorDAO.obtenerPorSchema(dto.getTenantSchemaName());
            if (prestador == null) {
                String errorMsg = "Notificación rechazada: No se encontró un prestador con el schema: " + dto.getTenantSchemaName();
                System.err.println(errorMsg);
                return Response.status(Response.Status.BAD_REQUEST).entity(errorMsg).build();
            }

            DocumentoClinicoMetadata metadata = new DocumentoClinicoMetadata();
            metadata.setUserId(dto.getPacienteGlobalId());
            metadata.setTenantId(prestador.getTenantId());
            metadata.setIdExternaDoc(dto.getIdExternaDoc());
            metadata.setTipo(dto.getTipoDocumento());
            metadata.setFechaCreacion(LocalDateTime.now());

            metadataDAO.guardar(metadata);
            
            System.out.println("HCEN (REST): Metadatos para " + dto.getIdExternaDoc() + " guardados exitosamente.");
            
            return Response.ok("Metadatos recibidos y procesados.").build();

        } catch (Exception e) {
            System.err.println("HCEN (REST): Error crítico al procesar la notificación.");
            e.printStackTrace();
            return Response.serverError().entity("Error interno en HCEN: " + e.getMessage()).build();
        }
    }
}