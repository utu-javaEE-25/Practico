package uy.edu.fing.tse.servicios;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.WebApplicationException;
import uy.edu.fing.tse.api.AuditLogServiceLocal;
import uy.edu.fing.tse.dto.MobileLoginRequestDTO;
import uy.edu.fing.tse.dto.MobileLoginResponseDTO;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.UsuarioDAO;
import uy.edu.fing.tse.seguridad.OidcTokenValidator;

@Stateless
public class MobileAuthService {

    @EJB
    private UsuarioDAO usuarioDAO;

    @EJB
    private AuditLogServiceLocal auditLogService;

    public MobileLoginResponseDTO authenticateMobileUser(MobileLoginRequestDTO dto) {

        // 1) Validar idToken con JWKS
        var claims = OidcTokenValidator.validateIdToken(dto.getIdToken());

        String sub = claims.getString("sub");
        String email = claims.optString("email", null);
        String nombre = claims.optString("given_name", null);
        String apellido = claims.optString("family_name", null);
        String ci = claims.optString("preferred_username", null);

        if (sub == null) {
            throw new WebApplicationException("Token inválido: falta 'sub'", 401);
        }

        // 2) Buscar usuario por sub
        UsuarioServicioSalud usuario = usuarioDAO.buscarPorSub(sub);

        boolean esNuevo = false;

        if (usuario == null) {
            usuario = new UsuarioServicioSalud();
            usuario.setSub(sub);
            usuario.setEmail(email);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCedulaIdentidad(ci);
            usuario.setActivo(true);
            esNuevo = true;
        } else {
            usuario.setEmail(email);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCedulaIdentidad(ci);
        }

        usuarioDAO.guardar(usuario);

        // 3) Auditoría
        auditLogService.registrarEvento(
                "USUARIO_SALUD",
                usuario.getId(),
                null,
                "LOGIN_MOBILE",
                null,
                "SUCCESS",
                null);

        // 4) Respuesta
        return new MobileLoginResponseDTO(
                usuario.getId(),
                usuario.getCedulaIdentidad(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                !esNuevo);
    }
}
