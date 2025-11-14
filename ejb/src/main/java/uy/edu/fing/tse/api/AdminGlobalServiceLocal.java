package uy.edu.fing.tse.api;

import java.util.List;

import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.AdminHcen;

@Local
public interface AdminGlobalServiceLocal {

    boolean esAdminPorSub(String sub);

    boolean esAdminPorCi(String ci);

    boolean esAdminPorEmail(String email);

    AdminHcen crearAdminManual(String ci, String email);
    
    boolean esAdminPorCi(String ci);

    AdminHcen actualizarGubUyIdPorCI(String ci, String gubUyId);

    AdminHcen convertirUsuarioEnAdmin(UsuarioServicioSalud usuario);

    List<AdminHcen> listarAdministradores();
}
