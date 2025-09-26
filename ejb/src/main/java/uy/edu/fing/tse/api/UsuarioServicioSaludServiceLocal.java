package uy.edu.fing.tse.api;

import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import java.util.List;

@Local
public interface UsuarioServicioSaludServiceLocal {
    UsuarioServicioSalud crear(UsuarioServicioSalud usuario);
    UsuarioServicioSalud obtenerPorCI(String cedulaIdentidad);
    void actualizar(UsuarioServicioSalud usuario);
    void eliminar(String cedulaIdentidad);
    List<UsuarioServicioSalud> listar();
}