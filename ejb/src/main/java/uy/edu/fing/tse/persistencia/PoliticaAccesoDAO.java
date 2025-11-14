package uy.edu.fing.tse.persistencia;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import uy.edu.fing.tse.entidades.PoliticaAcceso;
import java.time.LocalDateTime;

@Stateless
public class PoliticaAccesoDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    public void guardar(PoliticaAcceso politica) {
        em.persist(politica);
    }

    public PoliticaAcceso findPoliticaActiva(Long userId, Long tenantId) {
        if (userId == null || tenantId == null) {
            return null;
        }
        try {
            return em.createQuery(
                "SELECT p FROM PoliticaAcceso p WHERE p.userId = :userId AND p.tenantId = :tenantId " +
                "AND p.accion = 'permitir' " +
                "AND (p.ventanaDesde IS NULL OR p.ventanaDesde <= :now) " +
                "AND (p.ventanaHasta IS NULL OR p.ventanaHasta >= :now)", 
                PoliticaAcceso.class)
                .setParameter("userId", userId)
                .setParameter("tenantId", tenantId)
                .setParameter("now", LocalDateTime.now())
                .getSingleResult();
        } catch (NoResultException e) {
            return null; // No se encontró una política activa que permita el acceso
        }
    }
}
