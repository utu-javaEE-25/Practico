package uy.edu.fing.tse.api;

import jakarta.ejb.Local;

@Local
public interface AuditLogServiceLocal {

    void registrarEvento(String tipoActor, Long actorId, Long actorTenantId, String accion, Long recursoId, String resultado, String ip);
}
