package uy.edu.fing.tse.persistencia;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import uy.edu.fing.tse.entidades.DocumentoClinicoMetadata;
import java.util.Collections;
import java.util.List;

@Stateless
public class DocumentoClinicoMetadataDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;


    public DocumentoClinicoMetadata findById(Long docId) {
        if (docId == null) {
            return null;
        }
        return em.find(DocumentoClinicoMetadata.class, docId);
    }

    public List<DocumentoClinicoMetadata> findByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return em.createQuery("SELECT m FROM DocumentoClinicoMetadata m WHERE m.userId = :userId ORDER BY m.fechaCreacion DESC", DocumentoClinicoMetadata.class)
                .setParameter("userId", userId)
                .getResultList();
    }

     public void guardar(DocumentoClinicoMetadata metadata) {
        if (metadata == null) {
            throw new IllegalArgumentException("El objeto de metadatos no puede ser nulo.");
        }
        em.persist(metadata);
    }

     public DocumentoClinicoMetadata findByIdExternaDoc(String idExternaDoc) {
        if (idExternaDoc == null || idExternaDoc.isBlank()) {
            return null;
        }
        try {
            return em.createQuery(
                "SELECT m FROM DocumentoClinicoMetadata m WHERE m.idExternaDoc = :idExternaDoc", 
                DocumentoClinicoMetadata.class)
                .setParameter("idExternaDoc", idExternaDoc)
                .getSingleResult();
        } catch (NoResultException e) {
            // Esto es normal y significa que no se encontró ningún documento con ese ID.
            return null;
        }
    }
}
