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

    public PoliticaAcceso findPoliticaActiva(Long userId, Long tenantId, Long idProfesional, Long docMetadataId) {
        if (userId == null || tenantId == null) {
            return null;
        }
        try {
            return em.createQuery(
                "SELECT p FROM PoliticaAcceso p WHERE p.userId = :userId " +
                "AND p.accion = 'permitir' " +
                "AND (p.ventanaDesde IS NULL OR p.ventanaDesde <= :now) " +
                "AND (p.ventanaHasta IS NULL OR p.ventanaHasta >= :now) " +
                "AND (" +
                "    (p.tenantId = :tenantId AND p.idProfesionalAutorizado = :idProfesional AND p.docMetadataId = :docMetadataId) OR " + 
                "    (p.tenantId = :tenantId AND p.idProfesionalAutorizado IS NULL AND p.docMetadataId = :docMetadataId) OR " +       
                "    (p.tenantId = :tenantId AND p.idProfesionalAutorizado = :idProfesional AND p.docMetadataId IS NULL) OR " +       
                "    (p.tenantId = :tenantId AND p.idProfesionalAutorizado IS NULL AND p.docMetadataId IS NULL)" +                    
                ") ORDER BY p.docMetadataId DESC, p.idProfesionalAutorizado DESC", 
                PoliticaAcceso.class)
                .setParameter("userId", userId)
                .setParameter("tenantId", tenantId)
                .setParameter("idProfesional", idProfesional)
                .setParameter("docMetadataId", docMetadataId)
                .setParameter("now", LocalDateTime.now())
                .setMaxResults(1)
                .getSingleResult();
        } catch (NoResultException e) {
            return null; // No se encontró política de acceso.
        }
    }
}
