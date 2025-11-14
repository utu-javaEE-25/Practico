package uy.edu.fing.tse.persistencia;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import uy.edu.fing.tse.entidades.AuditLog;

@Stateless
@LocalBean
public class AccesoDocumentoDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    
    public List<AuditLog> obtenerAccesosPorCedula(String ci) {
    //CAMBIAR
    return em.createQuery(
            "SELECT a FROM AuditLog a " +
            "WHERE a.accion = 'CONSULTA_DOC' " +
            "AND a.fechaCreacion >= :fecha_inicio " +
            "AND a.recursoId IN (" +
            "   SELECT d.docId FROM DocumentoClinicoMetadata d " +
            "   WHERE d.userId = (" +
            "       SELECT u.id FROM UsuarioServicioSalud u WHERE u.cedulaIdentidad = :ci" +
            "   )" +
            ")",
            AuditLog.class)
        .setParameter("ci", ci)
        .setParameter("fecha_inicio", LocalDateTime.of(2025, 1, 1, 0, 0))
        .getResultStream()
        .toList();
    }
}