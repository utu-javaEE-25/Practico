package uy.edu.fing.tse.servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import uy.edu.fing.tse.dto.SolicitudAccesoRequestDTO;
import uy.edu.fing.tse.entidades.DocumentoClinicoMetadata;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.NotificacionDAO;
import uy.edu.fing.tse.persistencia.SolicitudAccesoDAO;
import uy.edu.fing.tse.persistencia.UsuarioDAO;
import uy.edu.fing.tse.persistencia.DocumentoClinicoMetadataDAO;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;

public class AccesoServiceBeanTest {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void crearSolicitudDeAcceso_missingPaciente_throws() throws Exception {
        AccesoServiceBean service = new AccesoServiceBean();
        setField(service, "usuarioDAO", new UsuarioDAO() {
            @Override public UsuarioServicioSalud buscarPorCI(String ci) { return null; }
        });
        setField(service, "solicitudDAO", new SolicitudAccesoDAO() {});
        setField(service, "notificacionDAO", new NotificacionDAO() {});
        setField(service, "prestadorDAO", new FakePrestadorPer(null));
        setField(service, "metadataDAO", new DocumentoClinicoMetadataDAO() {});

        SolicitudAccesoRequestDTO dto = new SolicitudAccesoRequestDTO();
        dto.setCedulaPaciente("000");

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> service.crearSolicitudDeAcceso(dto));
        assertNotNull(ex1);
    }

    @Test
    void crearSolicitudDeAcceso_missingSolicitante_throws() throws Exception {
        AccesoServiceBean service = new AccesoServiceBean();
        setField(service, "usuarioDAO", new UsuarioDAO() {
            @Override public UsuarioServicioSalud buscarPorCI(String ci) { return new UsuarioServicioSalud(); }
        });
        setField(service, "solicitudDAO", new SolicitudAccesoDAO() {});
        setField(service, "notificacionDAO", new NotificacionDAO() {});
        setField(service, "metadataDAO", new DocumentoClinicoMetadataDAO() {});
        setField(service, "prestadorDAO", new FakePrestadorPer(null));

        SolicitudAccesoRequestDTO dto = new SolicitudAccesoRequestDTO();
        dto.setCedulaPaciente("111");
        dto.setSchemaTenantSolicitante("s1");

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> service.crearSolicitudDeAcceso(dto));
        assertNotNull(ex2);
    }

    @Test
    void crearSolicitudDeAcceso_success_savesSolicitudAndNotification() throws Exception {
        AccesoServiceBean service = new AccesoServiceBean();

        final boolean[] solicitudSaved = {false};
        setField(service, "solicitudDAO", new SolicitudAccesoDAO() {
            @Override public void guardar(uy.edu.fing.tse.entidades.SolicitudAcceso s) { solicitudSaved[0]=true; }
        });

        final boolean[] notiSaved = {false};
        setField(service, "notificacionDAO", new NotificacionDAO() {
            @Override public void guardar(uy.edu.fing.tse.entidades.Notificacion n) { notiSaved[0]=true; }
        });

        setField(service, "usuarioDAO", new UsuarioDAO() {
            @Override public UsuarioServicioSalud buscarPorCI(String ci) { UsuarioServicioSalud u = new UsuarioServicioSalud(); u.setId(5L); return u; }
        });

        setField(service, "prestadorDAO", new FakePrestadorPer(new PrestadorSalud(){ { setTenantId(77L); } }));

        setField(service, "metadataDAO", new DocumentoClinicoMetadataDAO() {
            @Override public DocumentoClinicoMetadata findByIdExternaDoc(String ext) { return null; }
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

    // Simple fake implementation of PrestadorSaludPerLocal with default no-op methods
    static class FakePrestadorPer implements PrestadorSaludPerLocal {
        private final PrestadorSalud proveedor;
        FakePrestadorPer(PrestadorSalud p) { this.proveedor = p; }
        @Override public PrestadorSalud crear(PrestadorSalud prestador){ return prestador; }
        @Override public PrestadorSalud obtener(String rut){ return null; }
        @Override public PrestadorSalud obtenerPorId(Long id){ return null; }
        @Override public void actualizar(PrestadorSalud prestador){}
        @Override public void eliminar(String rut){}
        @Override public void actualizarEstado(String rut, boolean activo){}
        @Override public java.util.List<PrestadorSalud> listar(){ return java.util.Collections.emptyList(); }
        @Override public PrestadorSalud obtenerPorRut(String rut){ return null; }
        @Override public boolean existeRut(String rut){ return false; }
        @Override public PrestadorSalud obtenerPorSchema(String schema){ return proveedor; }
    }
}
