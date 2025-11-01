package uy.edu.fing.tse.persistencia;

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
            em.persist(usuario);
            return usuario;
        }

        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setEmail(usuario.getEmail());
        existente.setCedulaIdentidad(usuario.getCedulaIdentidad());
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
}
