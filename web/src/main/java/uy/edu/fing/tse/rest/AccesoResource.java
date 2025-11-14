package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.dto.SolicitudAccesoRequestDTO;
import uy.edu.fing.tse.servicios.AccesoServiceBean;

@Path("/accesos")
public class AccesoResource {

    @EJB
    private AccesoServiceBean accesoService;

    @POST
    @Path("/solicitar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response solicitarAcceso(SolicitudAccesoRequestDTO dto) {
        try {
            accesoService.crearSolicitudDeAcceso(dto);
            return Response.ok("Solicitud de acceso creada exitosamente. Pendiente de aprobación.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity("Error interno al procesar la solicitud.").build();
        }
    }
}