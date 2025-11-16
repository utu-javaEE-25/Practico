package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import uy.edu.fing.tse.api.AuditLogServiceLocal;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.audit.AuditHelper;
import uy.edu.fing.tse.audit.AuditLogConstants;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import java.util.List;
import java.net.URI;
import java.time.LocalDateTime;

@Path("/prestadores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PrestadorSaludResource {

    @EJB
    private PrestadorSaludServiceLocal servicio;
    @EJB
    private AuditLogServiceLocal auditService;
    @Context
    private HttpServletRequest request;

    @GET
    public List<PrestadorSalud> listar() {
        return servicio.listar();
    }

    @GET
    @Path("/{rut}")
    public PrestadorSalud obtener(@PathParam("rut") String rut) {
        return servicio.obtener(rut);
    }

    @POST
    public Response crear(PrestadorSalud prestador, @Context UriInfo uriInfo) {
        LocalDateTime ahora = LocalDateTime.now();
        if (prestador.getFechaCreacion() == null) {
            prestador.setFechaCreacion(ahora);
        }
        prestador.setFechaModificacion(ahora);
        PrestadorSalud creado = servicio.crear(prestador);
        AuditHelper.registrarEvento(
                auditService,
                request,
                 null,
                AuditLogConstants.Acciones.PRESTADOR_ALTA,
                creado != null ? creado.getTenantId() : null,
                AuditLogConstants.Resultados.SUCCESS);
        URI uri = uriInfo.getAbsolutePathBuilder().path(creado.getRut()).build();
        return Response.created(uri).entity(creado).build();
    }

    @DELETE
    @Path("/{rut}")
    public Response eliminar(@PathParam("rut") String rut) {
        PrestadorSalud prestador = servicio.obtener(rut);
        servicio.desactivar(rut);
        AuditHelper.registrarEvento(
                auditService,
                request,
                 null,
                AuditLogConstants.Acciones.PRESTADOR_BAJA,
                prestador != null ? prestador.getTenantId() : null,
                AuditLogConstants.Resultados.SUCCESS);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{rut}/activar")
    public Response activar(@PathParam("rut") String rut) {
        PrestadorSalud prestador = servicio.obtener(rut);
        servicio.activar(rut);
        AuditHelper.registrarEvento(
                auditService,
                request,
                 null,
                AuditLogConstants.Acciones.PRESTADOR_ACTIVACION,
                prestador != null ? prestador.getTenantId() : null,
                AuditLogConstants.Resultados.SUCCESS);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{rut}/desactivar")
    public Response desactivar(@PathParam("rut") String rut) {
        PrestadorSalud prestador = servicio.obtener(rut);
        servicio.desactivar(rut);
        AuditHelper.registrarEvento(
                auditService,
                request,
                 null,
                AuditLogConstants.Acciones.PRESTADOR_BAJA,
                prestador != null ? prestador.getTenantId() : null,
                AuditLogConstants.Resultados.SUCCESS);
        return Response.noContent().build();
    }
}
