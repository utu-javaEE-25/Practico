package uy.edu.fing.tse.api;

import jakarta.ejb.Remote;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import java.util.List;

@Remote
public interface UsuarioServicioSaludServiceRemote {

    UsuarioServicioSalud crear(UsuarioServicioSalud usuario);

    UsuarioServicioSalud obtenerPorCI(String cedulaIdentidad);

    void actualizar(UsuarioServicioSalud usuario);

    void eliminar(String cedulaIdentidad);

    List<UsuarioServicioSalud> listar();
}
