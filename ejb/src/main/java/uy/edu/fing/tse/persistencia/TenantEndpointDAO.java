package uy.edu.fing.tse.persistencia;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import uy.edu.fing.tse.entidades.TenantEndpoint;

@Stateless
public class TenantEndpointDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    public TenantEndpoint findByTenantId(Long tenantId) {
        if (tenantId == null) return null;
        try {
            // Asumimos que la tabla tenant_endpoint tiene una columna tenant_id
            return em.createQuery("SELECT te FROM TenantEndpoint te WHERE te.tenantId = :tenantId", TenantEndpoint.class)
                     .setParameter("tenantId", tenantId)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
