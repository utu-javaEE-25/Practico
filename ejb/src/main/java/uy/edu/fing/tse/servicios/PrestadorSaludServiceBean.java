package uy.edu.fing.tse.servicios;

import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;

@Stateless
public class PrestadorSaludServiceBean implements PrestadorSaludServiceLocal {

    @EJB
    private PrestadorSaludPerLocal per;

    @Override
    public PrestadorSalud crear(PrestadorSalud prestador) {
        if (prestador.getNombre() == null || prestador.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del prestador de salud no puede ser nulo o vacío.");
            
        }
        if (prestador.getRut() == null || prestador.getRut().isEmpty()) {
            throw new IllegalArgumentException("El RUT del prestador de salud no puede ser nulo o vacío.");
            
        }
        
        return per.crear(prestador);
    }

    @Override
    public PrestadorSalud obtener(long id) {
        return per.obtener(id);
    }

    @Override
    public void actualizar(PrestadorSalud prestador) {
        per.actualizar(prestador);
    }

    @Override
    public void eliminar(long id) {
        per.eliminar(id);
    }

    @Override
    public List<PrestadorSalud> listar() {
        return per.listar();
    }

}
