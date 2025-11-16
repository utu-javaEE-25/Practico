package uy.edu.fing.tse.servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import uy.edu.fing.tse.entidades.AdminHcen;
import uy.edu.fing.tse.persistencia.AdminGlobalDAO;

public class AdminGlobalServiceBeanTest {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void esAdminPorSub_nullOrBlank_returnsFalse() throws Exception {
        AdminGlobalServiceBean service = new AdminGlobalServiceBean();
        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorGubUyId(String g) { return null; }
        });

        assertFalse(service.esAdminPorSub(null));
        assertFalse(service.esAdminPorSub("   "));
    }

    @Test
    void esAdminPorSub_found_returnsTrue() throws Exception {
        AdminGlobalServiceBean service = new AdminGlobalServiceBean();
        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorGubUyId(String g) { return new AdminHcen(); }
        });

        assertTrue(service.esAdminPorSub("sub1"));
    }

    @Test
    void crearAdminManual_existing_throws() throws Exception {
        AdminGlobalServiceBean service = new AdminGlobalServiceBean();
        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorCi(String ci) { return new AdminHcen(ci, "a@b.com", LocalDateTime.now()); }
        });

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.crearAdminManual("123", "a@b.com"));
        assertNotNull(ex);
    }

    @Test
    void crearAdminManual_success_returnsAdmin() throws Exception {
        AdminGlobalServiceBean service = new AdminGlobalServiceBean();
        setField(service, "adminDAO", new AdminGlobalDAO() {
            @Override public AdminHcen buscarPorCi(String ci) { return null; }
            @Override public AdminHcen guardar(AdminHcen a) { return a; }
        });

        AdminHcen res = service.crearAdminManual("456", "c@d.com");
        assertNotNull(res);
        assertEquals("456", res.getCi());
        assertEquals("c@d.com", res.getEmail());
    }
}
