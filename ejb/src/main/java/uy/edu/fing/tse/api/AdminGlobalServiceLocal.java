package uy.edu.fing.tse.api;

import java.util.List;

import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.AdminHcen;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;

@Local
public interface AdminGlobalServiceLocal {

    boolean esAdminPorSub(String sub);

    boolean esAdminPorEmail(String email);

    AdminHcen convertirUsuarioEnAdmin(UsuarioServicioSalud usuario);

    List<AdminHcen> listarAdministradores();
}
