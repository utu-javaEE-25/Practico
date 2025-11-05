package uy.edu.fing.tse.persistencia;

import jakarta.ejb.Singleton;
import uy.edu.fing.tse.api.UsuarioServicioSaludPerLocal;
import uy.edu.fing.tse.api.UsuarioServicioSaludPerRemote;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class UsuarioServicioSaludPerBean implements UsuarioServicioSaludPerLocal, UsuarioServicioSaludPerRemote {

    // Simulamos una base de datos en memoria
    private final Map<Long, UsuarioServicioSalud> dato = new LinkedHashMap<>();
    private long secuencia = 1L;

    @Override
    public UsuarioServicioSalud crear(UsuarioServicioSalud usuario) {
        if (usuario.getId() == null || usuario.getId() == 0L) {
            usuario.setId(secuencia++);
        }
        dato.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public UsuarioServicioSalud obtenerPorCI(String cedulaIdentidad) {
        if (cedulaIdentidad == null) return null;
        for (UsuarioServicioSalud u : dato.values()) {
            if (cedulaIdentidad.equals(u.getCedulaIdentidad())) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void actualizar(UsuarioServicioSalud usuario) {
        if (!dato.containsKey(usuario.getId())) {
            throw new IllegalArgumentException("El usuario con ID " + usuario.getId() + " no existe.");
        }
        dato.put(usuario.getId(), usuario);
    }

    @Override
    public void eliminar(String cedulaIdentidad) {
        UsuarioServicioSalud usuario = obtenerPorCI(cedulaIdentidad);
        if (usuario != null) {
            dato.remove(usuario.getId());
        }
    }

    @Override
    public List<UsuarioServicioSalud> listar() {
        return new ArrayList<>(dato.values());
    }

    @Override
    public boolean existeCI(String cedulaIdentidad) {
        return obtenerPorCI(cedulaIdentidad) != null;
    }
}
