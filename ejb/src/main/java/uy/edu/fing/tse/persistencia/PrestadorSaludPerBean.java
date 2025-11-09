package uy.edu.fing.tse.persistencia;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;

@Singleton
public class PrestadorSaludPerBean implements PrestadorSaludPerLocal {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    public PrestadorSalud obtenerPorId(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(PrestadorSalud.class, id);
    }
    
    @Override
    public PrestadorSalud crear(PrestadorSalud prestadorSalud) {
        if (prestadorSalud == null) {
            throw new IllegalArgumentException("El prestador de salud no puede ser nulo.");
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (prestadorSalud.getFechaCreacion() == null) {
            prestadorSalud.setFechaCreacion(ahora);
        }
        prestadorSalud.setFechaModificacion(ahora);
        if (prestadorSalud.getEstado() == null) {
            prestadorSalud.setEstado(Boolean.TRUE);
        }

        em.persist(prestadorSalud);
        return prestadorSalud;
    }

    @Override
    public PrestadorSalud obtener(String rut) {
        return obtenerPorRut(rut);
    }

    @Override
    public void actualizar(PrestadorSalud prestadorSalud) {
        if (prestadorSalud == null || prestadorSalud.getTenantId() == null) {
            throw new IllegalArgumentException("El prestador debe tener un identificador valido para actualizarse.");
        }

        PrestadorSalud existente = em.find(PrestadorSalud.class, prestadorSalud.getTenantId());
        if (existente == null) {
            throw new IllegalArgumentException("El prestador de salud con ID " + prestadorSalud.getTenantId() + " no existe.");
        }

        prestadorSalud.setFechaCreacion(existente.getFechaCreacion());
        prestadorSalud.setFechaModificacion(LocalDateTime.now());

        em.merge(prestadorSalud);
    }
    
    @Override
    public void eliminar(String rut) {
        actualizarEstado(rut, false);
    }

    @Override
    public List<PrestadorSalud> listar() {
        return em.createQuery("SELECT p FROM PrestadorSalud p ORDER BY p.nombre", PrestadorSalud.class).getResultList();
    }

    @Override
    public PrestadorSalud obtenerPorRut(String rut) {
        if (rut == null || rut.isBlank()) {
            return null;
        }

        TypedQuery<PrestadorSalud> query = em.createQuery(
                "SELECT p FROM PrestadorSalud p WHERE p.rut = :rut",
                PrestadorSalud.class);
        query.setParameter("rut", rut);
        query.setMaxResults(1);

        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public boolean existeRut(String rut) {
        return obtenerPorRut(rut) != null;
    }

    @Override
    public void actualizarEstado(String rut, boolean activo) {
        PrestadorSalud prestador = obtenerPorRut(rut);
        if (prestador == null) {
            throw new IllegalArgumentException("No se encontró un prestador con RUT " + rut);
        }

        PrestadorSalud administrado = em.contains(prestador) ? prestador : em.merge(prestador);
        administrado.setEstado(activo);
        administrado.setFechaModificacion(LocalDateTime.now());
    }
}
