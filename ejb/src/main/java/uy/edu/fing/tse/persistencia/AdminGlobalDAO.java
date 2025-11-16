package uy.edu.fing.tse.persistencia;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import uy.edu.fing.tse.entidades.AdminHcen;

@Stateless
@LocalBean
public class AdminGlobalDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    public AdminHcen guardar(AdminHcen admin) {
        if (admin.getId() == null) {
            if (admin.getFechaCreacion() == null) {
                admin.setFechaCreacion(LocalDateTime.now());
            }
            em.persist(admin);
            return admin;
        }

        admin.setFechaCreacion(
                admin.getFechaCreacion() != null ? admin.getFechaCreacion() : LocalDateTime.now());
        return em.merge(admin);
    }

    public AdminHcen buscarPorId(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(AdminHcen.class, id);
    }

    public AdminHcen buscarPorGubUyId(String gubUyId) {
        if (gubUyId == null || gubUyId.isBlank()) {
            return null;
        }

        return em.createQuery(
                        "SELECT a FROM AdminHcen a WHERE a.gubUyId = :gubUyId",
                        AdminHcen.class)
                .setParameter("gubUyId", gubUyId)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public AdminHcen buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        return em.createQuery(
                        "SELECT a FROM AdminHcen a WHERE LOWER(a.email) = LOWER(:email)",
                        AdminHcen.class)
                .setParameter("email", email.trim())
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public AdminHcen buscarPorCi(String ci) {
        if (ci == null || ci.isBlank()) {
            return null;
        }

        return em.createQuery(
                        "SELECT a FROM AdminHcen a WHERE a.ci = :ci",
                        AdminHcen.class)
                .setParameter("ci", ci.trim())
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public AdminHcen actualizarGubUyIdPorCI(String ci, String gubUyId) {
        if (ci == null || ci.isBlank() || gubUyId == null || gubUyId.isBlank()) {
            return null;
        }

        AdminHcen admin = buscarPorCi(ci);
        if (admin == null) {
            return null;
        }

        admin.setGubUyId(gubUyId);
        return em.merge(admin);
    }
    
    public List<AdminHcen> listarTodos() {
        return em.createQuery(
                        "SELECT a FROM AdminHcen a ORDER BY a.fechaCreacion DESC",
                        AdminHcen.class)
                .getResultList();
    }
}
