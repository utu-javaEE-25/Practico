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
    
    @Transactional
    public void crearSolicitudDeAcceso(SolicitudAccesoRequestDTO dto) {
        // 1. Validar y encontrar las entidades
        UsuarioServicioSalud paciente = usuarioDAO.buscarPorCI(dto.getCedulaPaciente());
        if (paciente == null) throw new IllegalArgumentException("Paciente no encontrado.");

        PrestadorSalud solicitante = prestadorDAO.obtenerPorSchema(dto.getSchemaTenantSolicitante());
        if (solicitante == null) throw new IllegalArgumentException("Tenant solicitante no encontrado.");

        // 2. Crear la Solicitud de Acceso
        SolicitudAcceso solicitud = new SolicitudAcceso();
        solicitud.setRequesterTenantId(solicitante.getTenantId());
        solicitud.setTargetUserId(paciente.getId());
        solicitud.setMotivo(dto.getMotivo());
        solicitud.setEstado("PENDIENTE");
        solicitud.setFechaSolicitud(LocalDateTime.now());
        // En una versión futura, buscaríamos el docId a partir del idExternaDoc
        
        solicitudDAO.guardar(solicitud);
        
        // 3. Crear la Notificación para el Paciente
        Notificacion notificacion = new Notificacion();
        notificacion.setUserId(paciente.getId());
        notificacion.setSolicitudId(solicitud.getId());
        notificacion.setEvento("SOLICITUD_ACCESO_RECIBIDA");
        notificacion.setEstado("PENDIENTE");
        
        notificacionDAO.guardar(notificacion);
    }
}