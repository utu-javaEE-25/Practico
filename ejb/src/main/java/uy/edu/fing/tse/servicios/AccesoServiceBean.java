package uy.edu.fing.tse.servicios;

import java.time.LocalDateTime;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import uy.edu.fing.tse.dto.SolicitudAccesoRequestDTO;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;
import uy.edu.fing.tse.entidades.*;
import uy.edu.fing.tse.persistencia.*;

@Stateless
public class AccesoServiceBean {

    @EJB private SolicitudAccesoDAO solicitudDAO;
    @EJB private NotificacionDAO notificacionDAO;
    @EJB private UsuarioDAO usuarioDAO;
    @EJB private PrestadorSaludPerLocal prestadorDAO;
    @EJB private DocumentoClinicoMetadataDAO metadataDAO; 
    
    @Transactional
    public void crearSolicitudDeAcceso(SolicitudAccesoRequestDTO dto) {
       
        UsuarioServicioSalud paciente = usuarioDAO.buscarPorCI(dto.getCedulaPaciente());
        if (paciente == null) throw new IllegalArgumentException("Paciente no encontrado.");

        PrestadorSalud solicitante = prestadorDAO.obtenerPorSchema(dto.getSchemaTenantSolicitante());
        if (solicitante == null) throw new IllegalArgumentException("Tenant solicitante no encontrado.");

        if (dto.getIdProfesionalSolicitante() == null) {
        throw new IllegalArgumentException("El ID del profesional solicitante es obligatorio.");
        }   
       
        SolicitudAcceso solicitud = new SolicitudAcceso();
        solicitud.setRequesterTenantId(solicitante.getTenantId());
        solicitud.setTargetUserId(paciente.getId());
        solicitud.setMotivo(dto.getMotivo());
        solicitud.setEstado("PENDIENTE");
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setIdProfesionalSolicitante(dto.getIdProfesionalSolicitante());
        solicitud.setNombreProfesionalSolicitante(dto.getNombreProfesionalSolicitante());
        
        if (dto.getIdExternaDoc() != null && !dto.getIdExternaDoc().isBlank()) {
            DocumentoClinicoMetadata meta = metadataDAO.findByIdExternaDoc(dto.getIdExternaDoc());
            
            if (meta != null) {
                solicitud.setDocId(meta.getDocId());
            } else {
                System.err.println("Advertencia: Se solicitó acceso a un documento no indexado: " + dto.getIdExternaDoc());
            }
        }

        solicitudDAO.guardar(solicitud);
        
      
        Notificacion notificacion = new Notificacion();
        notificacion.setUserId(paciente.getId());
        notificacion.setSolicitudId(solicitud.getId());
        notificacion.setEvento("SOLICITUD_ACCESO_RECIBIDA");
        notificacion.setEstado("PENDIENTE");
        
        notificacionDAO.guardar(notificacion);
    }
}