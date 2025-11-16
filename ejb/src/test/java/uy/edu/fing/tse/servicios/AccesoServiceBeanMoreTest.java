package uy.edu.fing.tse.servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;


import org.junit.jupiter.api.Test;

import uy.edu.fing.tse.dto.SolicitudAccesoRequestDTO;
import uy.edu.fing.tse.entidades.Notificacion;
import uy.edu.fing.tse.entidades.SolicitudAcceso;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.NotificacionDAO;
import uy.edu.fing.tse.persistencia.SolicitudAccesoDAO;
import uy.edu.fing.tse.persistencia.UsuarioDAO;
import uy.edu.fing.tse.persistencia.DocumentoClinicoMetadataDAO;
import uy.edu.fing.tse.entidades.DocumentoClinicoMetadata;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;

public class AccesoServiceBeanMoreTest {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void crearSolicitud_missingPaciente_throws() throws Exception {
        AccesoServiceBean service = new AccesoServiceBean();
        setField(service, "usuarioDAO", new UsuarioDAO() {
            @Override public UsuarioServicioSalud buscarPorCI(String ci) { return null; }
        });
        setField(service, "solicitudDAO", new SolicitudAccesoDAO() {});
        setField(service, "notificacionDAO", new NotificacionDAO() {});
        setField(service, "prestadorDAO", new PrestadorSaludPerLocal() {
            @Override public PrestadorSalud crear(PrestadorSalud prestador) { return prestador; }
            @Override public PrestadorSalud obtener(String rut) { return null; }
            @Override public PrestadorSalud obtenerPorId(Long id) { return null; }
            @Override public void actualizar(PrestadorSalud prestador) { }
            @Override public void eliminar(String rut) { }
            @Override public void actualizarEstado(String rut, boolean activo) { }
            @Override public java.util.List<PrestadorSalud> listar() { return java.util.Collections.emptyList(); }
            @Override public PrestadorSalud obtenerPorRut(String rut) { return null; }
            @Override public boolean existeRut(String rut) { return false; }
            @Override public PrestadorSalud obtenerPorSchema(String schema) { return null; }
        });

        SolicitudAccesoRequestDTO dto = new SolicitudAccesoRequestDTO();
        dto.setCedulaPaciente(null);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> service.crearSolicitudDeAcceso(dto));
        assertTrue(ex1.getMessage().toLowerCase().contains("paciente"));
    }

    @Test
    void crearSolicitud_success_saves() throws Exception {
        AccesoServiceBean service = new AccesoServiceBean();

        final boolean[] solicitudSaved = {false};
        setField(service, "solicitudDAO", new SolicitudAccesoDAO() {
            @Override public void guardar(SolicitudAcceso s) { solicitudSaved[0] = true; }
        });

        final boolean[] notiSaved = {false};
        setField(service, "notificacionDAO", new NotificacionDAO() {
            @Override public void guardar(Notificacion n) { notiSaved[0] = true; }
        });

        setField(service, "usuarioDAO", new UsuarioDAO() {
            @Override public UsuarioServicioSalud buscarPorCI(String ci) { UsuarioServicioSalud u = new UsuarioServicioSalud(); u.setId(7L); return u; }
        });

        setField(service, "prestadorDAO", new PrestadorSaludPerLocal() {
            @Override public PrestadorSalud crear(PrestadorSalud prestador) { return prestador; }
            @Override public PrestadorSalud obtener(String rut) { return null; }
            @Override public PrestadorSalud obtenerPorId(Long id) { return null; }
            @Override public void actualizar(PrestadorSalud prestador) { }
            @Override public void eliminar(String rut) { }
            @Override public void actualizarEstado(String rut, boolean activo) { }
            @Override public java.util.List<PrestadorSalud> listar() { return java.util.Collections.emptyList(); }
            @Override public PrestadorSalud obtenerPorRut(String rut) { return null; }
            @Override public boolean existeRut(String rut) { return false; }
            @Override public PrestadorSalud obtenerPorSchema(String schema) { PrestadorSalud p = new PrestadorSalud(); p.setTenantId(1L); return p; }
        });

        SolicitudAccesoRequestDTO dto = new SolicitudAccesoRequestDTO();
        dto.setCedulaPaciente("222");
        dto.setSchemaTenantSolicitante("s2");
        dto.setIdProfesionalSolicitante(10L);
        dto.setNombreProfesionalSolicitante("Dr X");

        service.crearSolicitudDeAcceso(dto);

        assertTrue(solicitudSaved[0]);
        assertTrue(notiSaved[0]);
    }

    @Test
    void crearSolicitud_missingSolicitante_throws() throws Exception {
        AccesoServiceBean service = new AccesoServiceBean();
        setField(service, "usuarioDAO", new UsuarioDAO() {
            @Override public UsuarioServicioSalud buscarPorCI(String ci) { UsuarioServicioSalud u = new UsuarioServicioSalud(); u.setId(5L); return u; }
        });
        setField(service, "prestadorDAO", new PrestadorSaludPerLocal() {
            @Override public PrestadorSalud crear(PrestadorSalud prestador) { return prestador; }
            @Override public PrestadorSalud obtener(String rut) { return null; }
            @Override public PrestadorSalud obtenerPorId(Long id) { return null; }
            @Override public void actualizar(PrestadorSalud prestador) { }
            @Override public void eliminar(String rut) { }
            @Override public void actualizarEstado(String rut, boolean activo) { }
            @Override public java.util.List<PrestadorSalud> listar() { return java.util.Collections.emptyList(); }
            @Override public PrestadorSalud obtenerPorRut(String rut) { return null; }
            @Override public boolean existeRut(String rut) { return false; }
            @Override public PrestadorSalud obtenerPorSchema(String schema) { return null; }
        });

        SolicitudAccesoRequestDTO dto = new SolicitudAccesoRequestDTO();
        dto.setCedulaPaciente("111");
        dto.setIdProfesionalSolicitante(2L);

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> service.crearSolicitudDeAcceso(dto));
        assertTrue(ex2.getMessage().toLowerCase().contains("tenant") || ex2.getMessage().toLowerCase().contains("solicitant"));
    }

    @Test
    void crearSolicitud_missingIdProfesional_throws() throws Exception {
        AccesoServiceBean service = new AccesoServiceBean();
        setField(service, "usuarioDAO", new UsuarioDAO() {
            @Override public UsuarioServicioSalud buscarPorCI(String ci) { UsuarioServicioSalud u = new UsuarioServicioSalud(); u.setId(6L); return u; }
        });
        setField(service, "prestadorDAO", new PrestadorSaludPerLocal() {
            @Override public PrestadorSalud crear(PrestadorSalud prestador) { return prestador; }
            @Override public PrestadorSalud obtener(String rut) { return null; }
            @Override public PrestadorSalud obtenerPorId(Long id) { return null; }
            @Override public void actualizar(PrestadorSalud prestador) { }
            @Override public void eliminar(String rut) { }
            @Override public void actualizarEstado(String rut, boolean activo) { }
            @Override public java.util.List<PrestadorSalud> listar() { return java.util.Collections.emptyList(); }
            @Override public PrestadorSalud obtenerPorRut(String rut) { return null; }
            @Override public boolean existeRut(String rut) { return false; }
            @Override public PrestadorSalud obtenerPorSchema(String schema) { PrestadorSalud p = new PrestadorSalud(); p.setTenantId(2L); return p; }
        });

        SolicitudAccesoRequestDTO dto = new SolicitudAccesoRequestDTO();
        dto.setCedulaPaciente("111");
        dto.setSchemaTenantSolicitante("s1");
        dto.setIdProfesionalSolicitante(null);

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> service.crearSolicitudDeAcceso(dto));
        assertTrue(ex3.getMessage().toLowerCase().contains("id del profesional") || ex3.getMessage().toLowerCase().contains("profesional"));
    }

    @Test
    void crearSolicitud_withIdExterna_setsDocId() throws Exception {
        AccesoServiceBean service = new AccesoServiceBean();

        final SolicitudAcceso[] captured = new SolicitudAcceso[1];
        setField(service, "solicitudDAO", new SolicitudAccesoDAO() {
            @Override public void guardar(SolicitudAcceso s) { captured[0] = s; }
        });

        setField(service, "notificacionDAO", new NotificacionDAO() {
            @Override public void guardar(Notificacion n) { }
        });

        setField(service, "usuarioDAO", new UsuarioDAO() {
            @Override public UsuarioServicioSalud buscarPorCI(String ci) { UsuarioServicioSalud u = new UsuarioServicioSalud(); u.setId(8L); return u; }
        });

        setField(service, "prestadorDAO", new PrestadorSaludPerLocal() {
            @Override public PrestadorSalud crear(PrestadorSalud prestador) { return prestador; }
            @Override public PrestadorSalud obtener(String rut) { return null; }
            @Override public PrestadorSalud obtenerPorId(Long id) { return null; }
            @Override public void actualizar(PrestadorSalud prestador) { }
            @Override public void eliminar(String rut) { }
            @Override public void actualizarEstado(String rut, boolean activo) { }
            @Override public java.util.List<PrestadorSalud> listar() { return java.util.Collections.emptyList(); }
            @Override public PrestadorSalud obtenerPorRut(String rut) { return null; }
            @Override public boolean existeRut(String rut) { return false; }
            @Override public PrestadorSalud obtenerPorSchema(String schema) { PrestadorSalud p = new PrestadorSalud(); p.setTenantId(3L); return p; }
        });

        setField(service, "metadataDAO", new DocumentoClinicoMetadataDAO() {
            @Override public DocumentoClinicoMetadata findByIdExternaDoc(String idExterna) { DocumentoClinicoMetadata m = new DocumentoClinicoMetadata(); m.setDocId(99L); return m; }
        });

        SolicitudAccesoRequestDTO dto = new SolicitudAccesoRequestDTO();
        dto.setCedulaPaciente("333");
        dto.setSchemaTenantSolicitante("s3");
        dto.setIdProfesionalSolicitante(11L);
        dto.setIdExternaDoc("ext-1");

        service.crearSolicitudDeAcceso(dto);

        assertNotNull(captured[0]);
        assertEquals(99L, captured[0].getDocId());
    }
}
