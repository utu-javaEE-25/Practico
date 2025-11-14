package uy.edu.fing.tse.servicios;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import uy.edu.fing.tse.api.AdminGlobalServiceLocal;
import uy.edu.fing.tse.entidades.AdminHcen;
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
    public boolean esAdminPorCi(String ci) {
        if (ci == null || ci.isBlank()) {
            return false;
        }
        return adminDAO.buscarPorCi(ci) != null;
    }

    @Override
    public boolean esAdminPorEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return adminDAO.buscarPorEmail(email) != null;
    }

    @Override
    public AdminHcen crearAdminManual(String ci, String email) {
        if (ci == null || ci.isBlank()) {
            throw new IllegalArgumentException("La cedula de identidad es obligatoria.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        if (adminDAO.buscarPorCi(ci) != null) {
            throw new IllegalStateException("Ya existe un administrador con esa cedula.");
        }
        if (esAdminPorEmail(email)) {
            throw new IllegalStateException("Ya existe un administrador con ese email.");
        }

        AdminHcen nuevo = new AdminHcen();
        nuevo.setCi(ci.trim());
        nuevo.setEmail(email.trim());
        nuevo.setFechaCreacion(LocalDateTime.now());

        return adminDAO.guardar(nuevo);
    }

    @Override
    public List<AdminHcen> listarAdministradores() {
        return adminDAO.listarTodos();
    }
}
