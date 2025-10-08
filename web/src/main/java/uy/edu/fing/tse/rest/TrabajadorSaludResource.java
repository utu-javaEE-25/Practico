package uy.edu.fing.tse.rest;

import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.api.TrabajadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.TrabajadorSalud;

@Path("/trabajadores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrabajadorSaludResource {

    @EJB
    private TrabajadorSaludServiceLocal trabajadorService;

    @POST
    public Response altaTrabajador(TrabajadorSalud trabajador) {
        try {
            trabajadorService.altaTrabajador(trabajador);
            return Response.status(Response.Status.CREATED).entity(trabajador).build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response obtenerTodosLosTrabajadores() {
        List<TrabajadorSalud> trabajadores = trabajadorService.obtenerTodosLosTrabajadores();
        return Response.ok(trabajadores).build();
    }

    @GET
    @Path("/{cedula}")
    public Response obtenerTrabajadorPorCedula(@PathParam("cedula") String cedula) {
        TrabajadorSalud trabajador = trabajadorService.obtenerTrabajadorPorCedula(cedula);
        if (trabajador != null) {
            return Response.ok(trabajador).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
