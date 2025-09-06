package uy.edu.fing.tse.servicios;

import java.util.List;

import jakarta.ejb.Stateless;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.persistencia.PrestadorSaludPerBean;

@Stateless
public class PrestadorSaludServiceBean implements PrestadorSaludServiceLocal {

    private PrestadorSaludPerBean prestadorSaludPerBean;

    @Override
    public PrestadorSalud crear(PrestadorSalud prestador) {
        if (prestador.getNombre() == null || prestador.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del prestador de salud no puede ser nulo o vacío.");
            
        }
        if (prestador.getRut() == null || prestador.getRut().isEmpty()) {
            throw new IllegalArgumentException("El RUT del prestador de salud no puede ser nulo o vacío.");
            
        }
        
        return prestadorSaludPerBean.crear(prestador);
    }

    @Override
    public PrestadorSalud obtener(long id) {
        return prestadorSaludPerBean.obtener(id);
    }

    @Override
    public void actualizar(PrestadorSalud prestador) {
        prestadorSaludPerBean.actualizar(prestador);
    }

    @Override
    public void eliminar(long id) {
        prestadorSaludPerBean.eliminar(id);
    }

    @Override
    public List<PrestadorSalud> listar() {
        return prestadorSaludPerBean.listarTodos();
    }

}
