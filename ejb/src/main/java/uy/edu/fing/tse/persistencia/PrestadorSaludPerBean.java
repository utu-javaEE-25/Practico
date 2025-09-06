package uy.edu.fing.tse.persistencia;

import java.util.*;

import jakarta.ejb.Singleton;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;

@Singleton
public class PrestadorSaludPerBean implements PrestadorSaludPerLocal {

    private final Map<Long, PrestadorSalud> dato = new LinkedHashMap<>();
    private long secuencia = 1L;

    @Override
    public PrestadorSalud crear(PrestadorSalud prestadorSalud) {
        if (prestadorSalud.getId() == 0) {
            prestadorSalud.setId(secuencia++);
        }
        dato.put(prestadorSalud.getId(), prestadorSalud);
        return prestadorSalud;
    }

    @Override
    public PrestadorSalud obtener(String rut) {
        return dato.get(rut);
    }

    @Override
    public void actualizar(PrestadorSalud prestadorSalud) {
        if (!dato.containsKey(prestadorSalud.getId())) {
            throw new IllegalArgumentException("El prestador de salud con ID " + prestadorSalud.getId() + " no existe.");
        }

        dato.put(prestadorSalud.getId(), prestadorSalud);
    }
    
    @Override
    public void eliminar(String rut) {
        PrestadorSalud prestador = obtenerPorRut(rut);
        if (prestador != null) {
            dato.remove(prestador.getId());
        }
    }

    @Override
    public List<PrestadorSalud> listar() {
        return new ArrayList<>(dato.values());
    }

    @Override
    public PrestadorSalud obtenerPorRut(String rut) {
        if (rut == null) return null;
        for (PrestadorSalud p : dato.values()) {
            if (rut.equals(p.getRut())) return p;
        }
        return null;
    }

    @Override
    public boolean existeRut(String rut) {
        return obtenerPorRut(rut) != null;
    }
    

}
