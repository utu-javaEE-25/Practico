package uy.edu.fing.tse.persistencia;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import uy.edu.fing.tse.entidades.DocumentoClinicoMetadata;
import java.util.Collections;
import java.util.List;

@Stateless
public class DocumentoClinicoMetadataDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    public List<DocumentoClinicoMetadata> findByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return em.createQuery("SELECT m FROM DocumentoClinicoMetadata m WHERE m.userId = :userId ORDER BY m.fechaCreacion DESC", DocumentoClinicoMetadata.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
