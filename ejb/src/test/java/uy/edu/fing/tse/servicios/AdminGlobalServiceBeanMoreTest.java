package uy.edu.fing.tse.servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import uy.edu.fing.tse.entidades.AdminHcen;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.AdminGlobalDAO;

public class AdminGlobalServiceBeanMoreTest {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void esAdminPorCi_and_email_behaviour() throws Exception {
        AdminGlobalServiceBean service = new AdminGlobalServiceBean();
        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorCi(String ci) { return new AdminHcen(); }
            @Override public AdminHcen buscarPorEmail(String email) { return new AdminHcen(); }
        });

        assertFalse(service.esAdminPorCi(null));
        assertTrue(service.esAdminPorCi("123"));
        assertFalse(service.esAdminPorEmail("   "));
        assertTrue(service.esAdminPorEmail("x@y.com"));
    }

    @Test
    void crearAdminManual_nulls_and_blanks_throw() throws Exception {
        AdminGlobalServiceBean service = new AdminGlobalServiceBean();
        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorCi(String ci) { return null; }
            @Override public AdminHcen guardar(AdminHcen a) { return a; }
        });

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> service.crearAdminManual(null, "a@b.com"));
        assertNotNull(ex1);
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> service.crearAdminManual("  ", "a@b.com"));
        assertNotNull(ex2);
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> service.crearAdminManual("123", null));
        assertNotNull(ex3);
        IllegalArgumentException ex4 = assertThrows(IllegalArgumentException.class, () -> service.crearAdminManual("123", "  "));
        assertNotNull(ex4);
    }

    @Test
    void convertirUsuarioEnAdmin_null_and_conflicts() throws Exception {
        AdminGlobalServiceBean service = new AdminGlobalServiceBean();
        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorCi(String ci) { return null; }
            @Override public AdminHcen buscarPorEmail(String email) { return null; }
            @Override public AdminHcen guardar(AdminHcen a) { return a; }
        });

        IllegalArgumentException ex5 = assertThrows(IllegalArgumentException.class, () -> service.convertirUsuarioEnAdmin(null));
        assertNotNull(ex5);

        // conflict by existing CI
        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorCi(String ci) { return new AdminHcen(); }
        });
        UsuarioServicioSalud u = new UsuarioServicioSalud(); u.setCedulaIdentidad("1"); u.setEmail("e@e.com");
        IllegalStateException ex6 = assertThrows(IllegalStateException.class, () -> service.convertirUsuarioEnAdmin(u));
        assertNotNull(ex6);

        // conflict by existing email
        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorCi(String ci) { return null; }
            @Override public AdminHcen buscarPorEmail(String email) { return new AdminHcen(); }
        });
        IllegalStateException ex7 = assertThrows(IllegalStateException.class, () -> service.convertirUsuarioEnAdmin(u));
        assertNotNull(ex7);
    }

    @Test
    void convertirUsuarioEnAdmin_success_and_list_update_and_updateGubUy() throws Exception {
        AdminGlobalServiceBean service = new AdminGlobalServiceBean();

        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorCi(String ci) { return null; }
            @Override public AdminHcen buscarPorEmail(String email) { return null; }
            @Override public AdminHcen guardar(AdminHcen a) { a.setId(99L); return a; }
            @Override public java.util.List<AdminHcen> listarTodos() { return Arrays.asList(new AdminHcen("1","a@b.com", LocalDateTime.now())); }
            @Override public AdminHcen actualizarGubUyIdPorCI(String ci, String gubUyId) { AdminHcen x = new AdminHcen(); x.setCi(ci); return x; }
        });

        UsuarioServicioSalud u = new UsuarioServicioSalud(); u.setCedulaIdentidad("33"); u.setEmail("zz@zz.com");
        AdminHcen created = service.convertirUsuarioEnAdmin(u);
        assertNotNull(created);
        assertEquals(99L, created.getId());

        assertNotNull(service.listarAdministradores());

        AdminHcen updated = service.actualizarGubUyIdPorCI("33","gub");
        assertEquals("33", updated.getCi());
    }
}
