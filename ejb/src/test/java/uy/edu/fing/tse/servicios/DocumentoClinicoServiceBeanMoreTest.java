package uy.edu.fing.tse.servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import uy.edu.fing.tse.api.DocumentoClinicoPerLocal;
import uy.edu.fing.tse.entidades.DocumentoClinico;

public class DocumentoClinicoServiceBeanMoreTest {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void crear_validations_and_duplicate() throws Exception {
        DocumentoClinicoServiceBean service = new DocumentoClinicoServiceBean();
        setField(service, "per", new DocumentoClinicoPerLocal() {
            @Override public boolean existeCodigo(String codigo) { return false; }
            @Override public DocumentoClinico crear(DocumentoClinico d) { d.setId(1L); return d; }
            @Override public DocumentoClinico obtenerPorCodigo(String codigo) { return null; }
            @Override public List<DocumentoClinico> listar() { return List.of(); }
            @Override public void actualizar(DocumentoClinico d) {}
            @Override public void eliminarPorCodigo(String codigo) {}
        });

        DocumentoClinico missing = new DocumentoClinico();
        missing.setCodigo(null);
        missing.setPacienteCI("123");
        missing.setFechaEmision(LocalDate.now());
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> service.crear(missing));
        assertNotNull(ex1);

        DocumentoClinico future = new DocumentoClinico();
        future.setCodigo("C1");
        future.setPacienteCI("123");
        future.setFechaEmision(LocalDate.now().plusDays(1));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> service.crear(future));
        assertNotNull(ex2);

        // duplicate code
        setField(service, "per", new DocumentoClinicoPerLocal() {
            @Override public boolean existeCodigo(String codigo) { return true; }
            @Override public DocumentoClinico crear(DocumentoClinico d) { return d; }
            @Override public DocumentoClinico obtenerPorCodigo(String codigo) { return null; }
            @Override public List<DocumentoClinico> listar() { return List.of(); }
            @Override public void actualizar(DocumentoClinico d) {}
            @Override public void eliminarPorCodigo(String codigo) {}
        });

        DocumentoClinico dup = new DocumentoClinico();
        dup.setCodigo("X"); dup.setPacienteCI("p"); dup.setFechaEmision(LocalDate.now());
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> service.crear(dup));
        assertNotNull(ex3);
    }

    @Test
    void crear_success_and_delegations() throws Exception {
        DocumentoClinicoServiceBean service = new DocumentoClinicoServiceBean();
        final boolean[] created = {false};
        setField(service, "per", new DocumentoClinicoPerLocal() {
            @Override public boolean existeCodigo(String codigo) { return false; }
            @Override public DocumentoClinico crear(DocumentoClinico d) { created[0]=true; d.setId(5L); return d; }
            @Override public DocumentoClinico obtenerPorCodigo(String codigo) { return new DocumentoClinico(); }
            @Override public List<DocumentoClinico> listar() { return List.of(); }
            @Override public void actualizar(DocumentoClinico d) { d.setId(9L); }
            @Override public void eliminarPorCodigo(String codigo) { /* no-op */ }
        });

        DocumentoClinico ok = new DocumentoClinico();
        ok.setCodigo("OK"); ok.setPacienteCI("P1"); ok.setFechaEmision(LocalDate.now());
        DocumentoClinico res = service.crear(ok);
        assertTrue(created[0]);
        assertNotNull(res.getId());

        assertNotNull(service.obtenerPorCodigo("x"));
        service.eliminarPorCodigo("x");
    }

    @Test
    void actualizar_behaviour() throws Exception {
        DocumentoClinicoServiceBean service = new DocumentoClinicoServiceBean();

        // nonexistent
        setField(service, "per", new DocumentoClinicoPerLocal() {
            @Override public boolean existeCodigo(String codigo) { return false; }
            @Override public DocumentoClinico crear(DocumentoClinico d) { return d; }
            @Override public DocumentoClinico obtenerPorCodigo(String codigo) { return null; }
            @Override public List<DocumentoClinico> listar() { return List.of(); }
            @Override public void actualizar(DocumentoClinico d) {}
            @Override public void eliminarPorCodigo(String codigo) {}
        });

        DocumentoClinico d = new DocumentoClinico(); d.setCodigo("A"); d.setPacienteCI("1"); d.setFechaEmision(LocalDate.now());
        IllegalArgumentException ex4 = assertThrows(IllegalArgumentException.class, () -> service.actualizar(d));
        assertNotNull(ex4);

        // existent
        final boolean[] updated = {false};
        setField(service, "per", new DocumentoClinicoPerLocal() {
            @Override public boolean existeCodigo(String codigo) { return true; }
            @Override public DocumentoClinico crear(DocumentoClinico d) { return d; }
            @Override public DocumentoClinico obtenerPorCodigo(String codigo) { DocumentoClinico x = new DocumentoClinico(); x.setId(11L); return x; }
            @Override public List<DocumentoClinico> listar() { return List.of(); }
            @Override public void actualizar(DocumentoClinico d) { updated[0]=true; }
            @Override public void eliminarPorCodigo(String codigo) {}
        });

        service.actualizar(d);
        assertTrue(updated[0]);
    }

    @Test
    void buscarPorPacienteCI_filters_correctly() throws Exception {
        DocumentoClinicoServiceBean service = new DocumentoClinicoServiceBean();
        DocumentoClinico a = new DocumentoClinico(); a.setPacienteCI("ABC");
        DocumentoClinico b = new DocumentoClinico(); b.setPacienteCI("abc");
        DocumentoClinico c = new DocumentoClinico(); c.setPacienteCI(null);

        setField(service, "per", new DocumentoClinicoPerLocal() {
            @Override public boolean existeCodigo(String codigo) { return false; }
            @Override public DocumentoClinico crear(DocumentoClinico d) { return d; }
            @Override public DocumentoClinico obtenerPorCodigo(String codigo) { return null; }
            @Override public List<DocumentoClinico> listar() { return Arrays.asList(a,b,c); }
            @Override public void actualizar(DocumentoClinico d) {}
            @Override public void eliminarPorCodigo(String codigo) {}
        });

        List<DocumentoClinico> out = service.buscarPorPacienteCI("abc");
        assertEquals(2, out.size());
    }
}
