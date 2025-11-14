package uy.edu.fing.tse.persistencia;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import uy.edu.fing.tse.entidades.Notificacion;

@Stateless
public class NotificacionDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    public void guardar(Notificacion notificacion) {
        em.persist(notificacion);
    }
}
