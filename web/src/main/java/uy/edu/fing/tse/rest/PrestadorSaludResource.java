package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import java.util.List;
import java.net.URI;
import java.time.LocalDate;

@Path("/prestadores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PrestadorSaludResource {

    @EJB
    private PrestadorSaludServiceLocal servicio;

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
        prestador.setFechaAlta(LocalDate.now());
        prestador.setActivo(true);
        PrestadorSalud creado = servicio.crear(prestador);
        URI uri = uriInfo.getAbsolutePathBuilder().path(creado.getRut()).build();
        return Response.created(uri).entity(creado).build();
    }

    @DELETE
    @Path("/{rut}")
    public Response eliminar(@PathParam("rut") String rut) {
        servicio.eliminar(rut);
        return Response.noContent().build();
    }
}
