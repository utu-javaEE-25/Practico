package com.demo.dao;

import com.demo.entidad.Usuario;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UsuarioDAO {

    @PersistenceContext(unitName = "JavaEELoginPU")
    private EntityManager em;

    public void guardar(Usuario usuario) {
    Usuario existente = em.createQuery(
        "SELECT u FROM Usuario u WHERE u.sub = :sub", Usuario.class)
        .setParameter("sub", usuario.getSub())
        .getResultStream()
        .findFirst()
        .orElse(null);

    if (existente == null) {
        em.persist(usuario);
    } else {
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setEmail(usuario.getEmail());
        em.merge(existente);
    }
}

    public Usuario buscarPorSub(String sub) {
        return em.createQuery("SELECT u FROM Usuario u WHERE u.sub = :sub", Usuario.class)
                .setParameter("sub", sub)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
