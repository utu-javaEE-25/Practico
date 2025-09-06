package uy.edu.fing.tse.persistencia;

import java.util.*;

import jakarta.ejb.Singleton;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;

@Singleton
public class PrestadorSaludPerBean implements PrestadorSaludPerLocal {

    private final Map<Long, PrestadorSalud> dato = new LinkedHashMap<>();
    private long secuencia = 1L;


    public PrestadorSalud crear(PrestadorSalud prestadorSalud) {
        if (prestadorSalud.getId() == 0) {
            prestadorSalud.setId(secuencia++);
        }
        dato.put(prestadorSalud.getId(), prestadorSalud);
        return prestadorSalud;
    }

    public PrestadorSalud obtener(long id) {
        return dato.get(id);
    }

    public void actualizar(PrestadorSalud prestadorSalud) {
        if (!dato.containsKey(prestadorSalud.getId())) {
            throw new IllegalArgumentException("El prestador de salud con ID " + prestadorSalud.getId() + " no existe.");
        }

        dato.put(prestadorSalud.getId(), prestadorSalud);
    }
    
    public void eliminar(long id) {

        dato.remove(id);
    }

    public List<PrestadorSalud> listar() {
        return new ArrayList<>(dato.values());
    }
    

}
