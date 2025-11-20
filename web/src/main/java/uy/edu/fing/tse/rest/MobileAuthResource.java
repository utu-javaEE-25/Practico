package uy.edu.fing.tse.rest;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import uy.edu.fing.tse.servicios.MobileAuthService;
import uy.edu.fing.tse.dto.MobileLoginRequestDTO;
import uy.edu.fing.tse.dto.MobileLoginResponseDTO;

@Path("/mobile/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MobileAuthResource {

    @EJB
    private MobileAuthService mobileAuthService;

    @POST
    @Path("/login")
    public Response loginMobile(@Valid MobileLoginRequestDTO dto) {
        try {
            MobileLoginResponseDTO response = mobileAuthService.authenticateMobileUser(dto);
            return Response.ok(response).build();

        } catch (WebApplicationException ex) {
            return Response.status(ex.getResponse().getStatus())
                    .entity("{\"error\": \"" + ex.getMessage() + "\"}")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno en autenticación móvil.\"}")
                    .build();
        }
    }
}
