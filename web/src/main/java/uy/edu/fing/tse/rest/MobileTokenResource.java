package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.api.AuditLogServiceLocal;
import uy.edu.fing.tse.controladores.DTO.MobileTokenRequestDTO;
import uy.edu.fing.tse.persistencia.UsuarioDAO;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MobileTokenResource {

    @EJB
    private UsuarioDAO usuarioDAO;

    @EJB
    private AuditLogServiceLocal auditLogService;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/{userCI}/device-tokens")
    public Response registerDeviceToken(
            @PathParam("userCI") @NotNull String ci,
            @Valid MobileTokenRequestDTO request) {

        var usuario = usuarioDAO.buscarPorCI(ci);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Usuario no encontrado.\"}")
                    .build();
        } else {
            usuario.setTokenId(request.getTokenValue());
            usuarioDAO.guardar(usuario);

            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Token registrado exitosamente.\"}")
                    .build();
        }
    }
}
