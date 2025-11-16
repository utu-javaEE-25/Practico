package uy.edu.fing.tse.servicios;

import java.time.LocalDateTime;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute; 
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import uy.edu.fing.tse.api.AuditLogServiceLocal;
import uy.edu.fing.tse.audit.AuditLogConstants;
import uy.edu.fing.tse.entidades.AuditLog;

@Stateless
public class AuditLogServiceBean implements AuditLogServiceLocal {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void registrarEvento(String tipoActor, Long actorId, Long actorTenantId, String accion, Long recursoId, String resultado, String ip){
        if (accion == null || accion.isBlank()) {
            throw new IllegalArgumentException("La accion de auditoria es obligatoria.");
        }

        AuditLog registro = new AuditLog();
        String actor = tipoActor != null ? tipoActor : AuditLogConstants.TipoActor.SYSTEM;
        String resultadoFinal = resultado != null ? resultado : AuditLogConstants.Resultados.SUCCESS;

        registro.setTipoActor(actor);
        registro.setActorId(actorId != null ? actorId : 0L);
        registro.setActorTenantId(actorTenantId);
        registro.setAccion(accion);
        registro.setRecursoId(recursoId);
        registro.setResultado(resultadoFinal);
        registro.setIp(ip);
        registro.setFechaCreacion(LocalDateTime.now());

        em.persist(registro);
    }
}
