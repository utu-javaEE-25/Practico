package uy.edu.fing.tse.servicios;

import java.time.LocalDateTime;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import uy.edu.fing.tse.entidades.PoliticaAcceso;
import uy.edu.fing.tse.entidades.SolicitudAcceso;
import uy.edu.fing.tse.entidades.DocumentoClinicoMetadata;
import uy.edu.fing.tse.persistencia.PoliticaAccesoDAO;
import uy.edu.fing.tse.persistencia.SolicitudAccesoDAO;
import uy.edu.fing.tse.persistencia.DocumentoClinicoMetadataDAO;

@Stateless
public class GestionPermisosService {

    @EJB private SolicitudAccesoDAO solicitudDAO;
    @EJB private PoliticaAccesoDAO politicaDAO;
    @EJB private DocumentoClinicoMetadataDAO metadataDAO;

    @Transactional
    public void aprobarSolicitud(Long solicitudId, Long userIdPropietario, int diasDeVigencia) {
        // 1. Validar la solicitud
        SolicitudAcceso solicitud = solicitudDAO.findById(solicitudId);
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no existe.");
        }
        if (!solicitud.getTargetUserId().equals(userIdPropietario)) {
            throw new SecurityException("No tiene permiso para gestionar esta solicitud.");
        }
        if (!"PENDIENTE".equals(solicitud.getEstado())) {
            throw new IllegalStateException("Esta solicitud ya ha sido gestionada.");
        }

        // 2. Actualizar el estado de la solicitud
        solicitud.setEstado("APROBADA");
        solicitudDAO.guardar(solicitud);

        // 3. Crear la nueva política de acceso
        PoliticaAcceso politica = new PoliticaAcceso();
        politica.setUserId(userIdPropietario);
        politica.setTenantId(solicitud.getRequesterTenantId());
        politica.setAccion("permitir"); 
        politica.setVentanaDesde(LocalDateTime.now());
        politica.setVentanaHasta(LocalDateTime.now().plusDays(diasDeVigencia));
        politica.setIdProfesionalAutorizado(solicitud.getIdProfesionalSolicitante());
        politica.setDocMetadataId(solicitud.getDocId());

        politicaDAO.guardar(politica);
    }

    @Transactional
    public PoliticaAcceso crearPoliticaManual(Long userIdPropietario, Long tenantSolicitanteId,
                                              Long idProfesionalAutorizado, Long docMetadataId,
                                              LocalDateTime ventanaDesde, LocalDateTime ventanaHasta) {
        if (userIdPropietario == null) {
            throw new IllegalArgumentException("Usuario requerido.");
        }
        if (tenantSolicitanteId == null) {
            throw new IllegalArgumentException("Debe indicar el tenant solicitante.");
        }
        if (ventanaDesde != null && ventanaHasta != null && ventanaHasta.isBefore(ventanaDesde)) {
            throw new IllegalArgumentException("La ventana de vigencia es inv��lida: fecha hasta anterior a desde.");
        }

        if (docMetadataId != null) {
            DocumentoClinicoMetadata meta = metadataDAO.findById(docMetadataId);
            if (meta == null || !userIdPropietario.equals(meta.getUserId())) {
                throw new SecurityException("El documento no pertenece al usuario.");
            }
        }

        PoliticaAcceso politica = new PoliticaAcceso();
        politica.setUserId(userIdPropietario);
        politica.setTenantId(tenantSolicitanteId);
        politica.setAccion("permitir");
        politica.setVentanaDesde(ventanaDesde);
        politica.setVentanaHasta(ventanaHasta);
        politica.setIdProfesionalAutorizado(idProfesionalAutorizado);
        politica.setDocMetadataId(docMetadataId);

        politicaDAO.guardar(politica);
        return politica;
    }

    @Transactional
    public void revocarPolitica(Long politicaId, Long userIdPropietario) {
        PoliticaAcceso politica = politicaDAO.findById(politicaId);
        if (politica == null) {
            return;
        }
        if (!userIdPropietario.equals(politica.getUserId())) {
            throw new SecurityException("No puede revocar pol��ticas de otro usuario.");
        }
        politicaDAO.eliminar(politicaId);
    }
}
