package uy.edu.fing.tse.audit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import uy.edu.fing.tse.api.AuditLogServiceLocal;

public final class AuditHelper {

    private AuditHelper() {
    }

    public static void registrarEvento(AuditLogServiceLocal service, HttpServletRequest request,
    Long actorTenantId, String accion, Long recursoId, String resultado) {
        if (service == null || accion == null) {
            return;
        }

        HttpSession session = request != null ? request.getSession(false) : null;
        String tipoActor = obtenerTipoActor(session);
        Long actorId = obtenerActorId(session);
        String ip = request != null ? request.getRemoteAddr() : null;

        service.registrarEvento(tipoActor, actorId, actorTenantId, accion, recursoId, resultado, ip);
    }

    private static String obtenerTipoActor(HttpSession session) {
        if (session == null) {
            return AuditLogConstants.TipoActor.SYSTEM;
        }
        Object flag = session.getAttribute("isAdmin");
        boolean esAdmin = flag instanceof Boolean && (Boolean) flag;
        return esAdmin
                ? AuditLogConstants.TipoActor.ADMIN_GLOBAL
                : AuditLogConstants.TipoActor.USUARIO;
    }

    private static Long obtenerActorId(HttpSession session) {
        if (session == null) {
            return 0L;
        }
        Object raw = session.getAttribute("usuario_id");
        if (raw instanceof Long) {
            return (Long) raw;
        }
        if (raw instanceof Number) {
            return ((Number) raw).longValue();
        }
        return 0L;
    }
}
