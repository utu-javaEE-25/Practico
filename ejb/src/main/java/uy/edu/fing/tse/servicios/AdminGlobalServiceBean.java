package uy.edu.fing.tse.servicios;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import uy.edu.fing.tse.api.AdminGlobalServiceLocal;
import uy.edu.fing.tse.entidades.AdminHcen;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.AdminGlobalDAO;

@Stateless
public class AdminGlobalServiceBean implements AdminGlobalServiceLocal {

    @EJB
    private AdminGlobalDAO adminDAO;

    @Override
    public boolean esAdminPorSub(String sub) {
        if (sub == null || sub.isBlank()) {
            return false;
        }
        return adminDAO.buscarPorGubUyId(sub) != null;
    }

    @Override
    public boolean esAdminPorEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return adminDAO.buscarPorEmail(email) != null;
    }

    @Override
    public boolean esAdminPorCi(String ci) {
        if (ci == null || ci.isBlank()) {
            return false;
        }
        return adminDAO.buscarPorCI(ci) != null;
    }

    @Override
    public AdminHcen convertirUsuarioEnAdmin(UsuarioServicioSalud usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }

        if (esAdminPorSub(usuario.getSub()) || esAdminPorEmail(usuario.getEmail())) {
            throw new IllegalStateException("El usuario ya se encuentra registrado como administrador.");
        }

        AdminHcen nuevo = new AdminHcen();
        nuevo.setGubUyId(usuario.getSub());
        nuevo.setEmail(usuario.getEmail());
        nuevo.setEstado("ACTIVO");
        nuevo.setFechaCreacion(LocalDateTime.now());

        return adminDAO.guardar(nuevo);
    }

    @Override
    public List<AdminHcen> listarAdministradores() {
        return adminDAO.listarTodos();
    }

    @Override
    public AdminHcen actualizarGubUyIdPorCI(String ci, String gubUyId) {
        return adminDAO.actualizarGubUyIdPorCI(ci, gubUyId);
    }
}
