package uy.edu.fing.tse.persistencia;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;

@Stateless
@LocalBean
public class UsuarioDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    public UsuarioServicioSalud guardar(UsuarioServicioSalud usuario) {
        UsuarioServicioSalud existente = buscarPorSub(usuario.getSub());

        if (existente == null) {
            if (usuario.getFechaCreacion() == null) {
                usuario.setFechaCreacion(LocalDateTime.now());
            }
            usuario.setFechaModificacion(usuario.getFechaCreacion());
            if (!usuario.isActivo()) {
                usuario.setActivo(true);
            }
            em.persist(usuario);
            return usuario;
        }

        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setEmail(usuario.getEmail());
        existente.setCedulaIdentidad(usuario.getCedulaIdentidad());
        existente.setFechaModificacion(LocalDateTime.now());
        return em.merge(existente);
    }

    public UsuarioServicioSalud buscarPorSub(String sub) {
        if (sub == null || sub.isBlank()) {
            return null;
        }

        return em.createQuery(
                        "SELECT u FROM UsuarioServicioSalud u WHERE u.sub = :sub", UsuarioServicioSalud.class)
                .setParameter("sub", sub)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public UsuarioServicioSalud buscarPorId(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(UsuarioServicioSalud.class, id);
    }

    public UsuarioServicioSalud buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        return em.createQuery(
                        "SELECT u FROM UsuarioServicioSalud u WHERE LOWER(u.email) = LOWER(:email)",
                        UsuarioServicioSalud.class)
                .setParameter("email", email.trim())
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<UsuarioServicioSalud> listarTodos() {
        return em.createQuery(
                        "SELECT u FROM UsuarioServicioSalud u ORDER BY u.nombre, u.apellido",
                        UsuarioServicioSalud.class)
                .getResultList();
    }

    public UsuarioServicioSalud buscarPorCI(String cedulaIdentidad) {
    if (cedulaIdentidad == null || cedulaIdentidad.isBlank()) {
        return null;
    }
    try {
        return em.createQuery(
                        "SELECT u FROM UsuarioServicioSalud u WHERE u.cedulaIdentidad = :ci", UsuarioServicioSalud.class)
                .setParameter("ci", cedulaIdentidad)
                .getSingleResult();
    } catch (jakarta.persistence.NoResultException e) {
        return null;
    }
    }

}
