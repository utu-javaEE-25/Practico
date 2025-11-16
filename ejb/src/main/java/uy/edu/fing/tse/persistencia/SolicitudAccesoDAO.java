package uy.edu.fing.tse.persistencia;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import uy.edu.fing.tse.entidades.SolicitudAcceso;
import java.util.List;

@Stateless
public class SolicitudAccesoDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    public SolicitudAcceso findById(Long id) {
        return em.find(SolicitudAcceso.class, id);
    }

    public void guardar(SolicitudAcceso solicitud) {
        em.persist(solicitud);
    }

    public List<SolicitudAcceso> findPendientesPorUsuario(Long userId) {
        return em.createQuery(
            "SELECT s FROM SolicitudAcceso s WHERE s.targetUserId = :userId AND s.estado = 'PENDIENTE'", 
            SolicitudAcceso.class)
            .setParameter("userId", userId)
            .getResultList();
    }
}