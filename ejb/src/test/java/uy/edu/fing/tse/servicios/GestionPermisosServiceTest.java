package uy.edu.fing.tse.servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import uy.edu.fing.tse.entidades.SolicitudAcceso;
import uy.edu.fing.tse.persistencia.PoliticaAccesoDAO;
import uy.edu.fing.tse.persistencia.SolicitudAccesoDAO;

public class GestionPermisosServiceTest {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void aprobarSolicitud_nonexistent_throws() throws Exception {
        GestionPermisosService service = new GestionPermisosService();
        setField(service, "solicitudDAO", new SolicitudAccesoDAO() {
            @Override public SolicitudAcceso findById(Long id) { return null; }
        });
        setField(service, "politicaDAO", new PoliticaAccesoDAO() {
            @Override public void guardar(uy.edu.fing.tse.entidades.PoliticaAcceso politica) {}
        });

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> service.aprobarSolicitud(1L, 2L, 5));
        assertNotNull(ex1);
    }

    @Test
    void aprobarSolicitud_notOwner_throws() throws Exception {
        GestionPermisosService service = new GestionPermisosService();
        SolicitudAcceso s = new SolicitudAcceso();
        s.setTargetUserId(99L);
        s.setEstado("PENDIENTE");

        setField(service, "solicitudDAO", new SolicitudAccesoDAO() {
            @Override public SolicitudAcceso findById(Long id) { return s; }
        });
        setField(service, "politicaDAO", new PoliticaAccesoDAO() {
            @Override public void guardar(uy.edu.fing.tse.entidades.PoliticaAcceso politica) {}
        });

        SecurityException ex2 = assertThrows(SecurityException.class, () -> service.aprobarSolicitud(2L, 1L, 3));
        assertNotNull(ex2);
    }

    @Test
    void aprobarSolicitud_success_persistsPolitica() throws Exception {
        GestionPermisosService service = new GestionPermisosService();
        SolicitudAcceso s = new SolicitudAcceso();
        s.setTargetUserId(10L);
        s.setRequesterTenantId(20L);
        s.setEstado("PENDIENTE");
        s.setIdProfesionalSolicitante(777L);
        s.setDocId(55L);

        final boolean[] saved = {false};
        setField(service, "solicitudDAO", new SolicitudAccesoDAO() {
            @Override public SolicitudAcceso findById(Long id) { return s; }
            @Override public void guardar(SolicitudAcceso solicitud) { saved[0]=true; }
        });

        final boolean[] politicaSaved = {false};
        setField(service, "politicaDAO", new PoliticaAccesoDAO() {
            @Override public void guardar(uy.edu.fing.tse.entidades.PoliticaAcceso politica) { politicaSaved[0]=true; }
        });

        service.aprobarSolicitud(3L, 10L, 7);

        assertTrue(saved[0]);
        assertTrue(politicaSaved[0]);
    }
}
