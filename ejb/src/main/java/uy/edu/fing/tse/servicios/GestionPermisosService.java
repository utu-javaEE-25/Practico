package uy.edu.fing.tse.servicios;

import java.time.LocalDateTime;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import uy.edu.fing.tse.entidades.PoliticaAcceso;
import uy.edu.fing.tse.entidades.SolicitudAcceso;
import uy.edu.fing.tse.persistencia.PoliticaAccesoDAO;
import uy.edu.fing.tse.persistencia.SolicitudAccesoDAO;

@Stateless
public class GestionPermisosService {

    @EJB private SolicitudAccesoDAO solicitudDAO;
    @EJB private PoliticaAccesoDAO politicaDAO;

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
}
