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
        validarCampos(prestador);
        
        if (per.existeRut(prestador.getRut())) {
            throw new IllegalArgumentException("El RUT ya existe en el sistema.");
        }

        return per.crear(prestador);
    }

    @Override
    public PrestadorSalud obtener(String rut) {
        return per.obtenerPorRut(rut);
    }

    @Override
    public void actualizar(PrestadorSalud prestador) {
        validarCampos(prestador);

        PrestadorSalud existente = per.obtenerPorRut(prestador.getRut());

        if (existente == null) {
            throw new IllegalArgumentException("No existe el prestador a actualizar.");
        }

        per.actualizar(prestador);
    }

    @Override
    public void eliminar(String rut) {
        per.eliminar(rut);
    }

    @Override
    public List<PrestadorSalud> listar() {
        return per.listar();
    }

    private void validarCampos(PrestadorSalud prestador) {
        if (prestador.getNombre() == null || prestador.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del prestador de salud no puede ser nulo o vacío.");
        }
        if (prestador.getRut() == null || prestador.getRut().isEmpty()) {
            throw new IllegalArgumentException("El RUT del prestador de salud no puede ser nulo o vacío.");
        }
    }

    private boolean existeRut(String rut) {
        return per.existeRut(rut);
    }

}
