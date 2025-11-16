package uy.edu.fing.tse.servicios;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import uy.edu.fing.tse.api.AuditLogServiceLocal;
import uy.edu.fing.tse.api.HistoriaClinicaServiceLocal;
import uy.edu.fing.tse.dto.DocumentoDetalleDTO;
import uy.edu.fing.tse.dto.DiagnosticoDTO;
import uy.edu.fing.tse.dto.DocumentoMetadataDTO;
import uy.edu.fing.tse.dto.apiperiferico.DocumentoClinicoApiDTO;
import uy.edu.fing.tse.entidades.DocumentoClinicoMetadata;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;
import uy.edu.fing.tse.persistencia.DocumentoClinicoMetadataDAO;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;
import uy.edu.fing.tse.persistencia.UsuarioDAO; 
import uy.edu.fing.tse.persistencia.PoliticaAccesoDAO;
import uy.edu.fing.tse.entidades.PoliticaAcceso;
//import uy.edu.fing.tse.persistencia.PrestadorSaludPerBean;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class HistoriaClinicaServiceBean implements HistoriaClinicaServiceLocal {

    @EJB private DocumentoClinicoMetadataDAO metadataDAO;
    @EJB private PrestadorSaludPerLocal prestadorDAO;
    @EJB private PerifericoApiClient apiClient;
    @EJB private UsuarioDAO usuarioDAO;
    @EJB private PoliticaAccesoDAO politicaDAO;
    @EJB private AuditLogServiceLocal auditLogService;

    @Override
    public List<DocumentoMetadataDTO> getHistoriaMetadataPorCedula(String cedula) {
        // 1. Buscar el usuario en la base de datos central por su cédula
        UsuarioServicioSalud usuario = usuarioDAO.buscarPorCI(cedula); // Asumimos que este método existe
        if (usuario == null) {
            // Si el usuario no existe en HCEN, no tiene historia clínica.
            System.out.println("Solicitud de historia para CI " + cedula + ", pero el usuario no existe en HCEN.");
            return new ArrayList<>();
        }

        // 2. Reutilizar la lógica existente para obtener los metadatos con el ID del usuario
        return getHistoriaMetadata(usuario.getId());
    }

    @Override
    public List<DocumentoMetadataDTO> getHistoriaMetadata(Long usuarioId) {
        List<DocumentoClinicoMetadata> metadatos = metadataDAO.findByUserId(usuarioId);
        List<DocumentoMetadataDTO> dtos = new ArrayList<>();
        
        for (DocumentoClinicoMetadata metaEntity : metadatos) {
            PrestadorSalud p = prestadorDAO.obtenerPorId(metaEntity.getTenantId());
            DocumentoMetadataDTO dto = new DocumentoMetadataDTO();
            dto.setIdExternaDoc(metaEntity.getIdExternaDoc());
            dto.setIdCustodio(metaEntity.getTenantId());
            dto.setTipoDocumento(metaEntity.getTipo());
            dto.setFechaCreacion(metaEntity.getFechaCreacion());
            if (p != null) {
                dto.setNombrePrestador(p.getNombre());
                dto.setSchemaCustodio(p.getNombreSchema());
            } else {
                dto.setNombrePrestador("Prestador Desconocido");
                dto.setSchemaCustodio("desconocido");
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public DocumentoDetalleDTO getDocumentoDetalle(String idExternaDoc, Long tenantCustodioId) {
        try {
            DocumentoClinicoApiDTO docFromApi = apiClient.getDocumento(idExternaDoc, tenantCustodioId);
            return mapApiDtoToViewDto(docFromApi);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el documento del prestador: " + e.getMessage(), e);
        }
    }

     @Override
    public DocumentoClinicoApiDTO verificarYObtenerDocumento(String cedulaPaciente, String idExternaDoc, PrestadorSalud tenantSolicitante, Long idProfesional, Long docMetadataId) {
        UsuarioServicioSalud paciente = usuarioDAO.buscarPorCI(cedulaPaciente);
        if (paciente == null) throw new IllegalArgumentException("Paciente no encontrado.");

        
        PoliticaAcceso politica = politicaDAO.findPoliticaActiva(paciente.getId(), tenantSolicitante.getTenantId(), idProfesional, docMetadataId);
        
        if (politica == null) {
            
           auditLogService.registrarEvento("PROFESIONAL", idProfesional, tenantSolicitante.getTenantId(), "VISUALIZAR_DOCUMENTO_EXTERNO", docMetadataId, "FAILURE - NO_PERMISSION", null);
            throw new AccesoNoAutorizadoException("Acceso no permitido por política del usuario. Debe solicitar permiso.");
        }

        
        DocumentoClinicoMetadata metadata = metadataDAO.findById(docMetadataId); 
        if (metadata == null) {
             throw new IllegalArgumentException("Metadatos del documento no encontrados con ID: " + docMetadataId);
        }

         DocumentoClinicoApiDTO documentoOriginal;
        try {
            documentoOriginal = apiClient.getDocumento(idExternaDoc, metadata.getTenantId());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el documento del prestador custodio: " + e.getMessage(), e);
        }

        auditLogService.registrarEvento("PROFESIONAL", idProfesional, tenantSolicitante.getTenantId(), "VISUALIZAR_DOCUMENTO_EXTERNO", docMetadataId, "SUCCESS", null);
        
        return documentoOriginal;
    }
    
    private DocumentoDetalleDTO mapApiDtoToViewDto(DocumentoClinicoApiDTO apiDto) {
    if (apiDto == null) return null;
    
    DocumentoDetalleDTO viewDto = new DocumentoDetalleDTO();
    
    if (apiDto.getPaciente() != null) {
        viewDto.setPacienteNombre(apiDto.getPaciente().getNombreCompleto());
        viewDto.setPacienteNroDocumento(apiDto.getPaciente().getNroDocumento());
        viewDto.setPacienteFechaNacimiento(apiDto.getPaciente().getFechaNacimiento());
        viewDto.setPacienteSexo(apiDto.getPaciente().getSexo());
    }

    viewDto.setInstanciaMedica(apiDto.getInstanciaMedica());
    viewDto.setFechaAtencion(apiDto.getFechaAtencion());
    viewDto.setLugar(apiDto.getLugar());
    viewDto.setAutor(apiDto.getProfesional() != null ? apiDto.getProfesional().getNombreCompleto() : null);
    viewDto.setDocumentoId(apiDto.getIdExternaDoc());
    viewDto.setFechaGeneracion(apiDto.getFechaGeneracion() != null ? apiDto.getFechaGeneracion().toString() : "N/A");
    viewDto.setCustodio(apiDto.getCustodio());

    if (apiDto.getMotivosDeConsulta() != null) {
        viewDto.setMotivosDeConsulta(
            apiDto.getMotivosDeConsulta().stream()
                .map(DocumentoClinicoApiDTO.MotivoConsultaDTO::getDescripcion)
                .collect(Collectors.toList())
        );
    }
    
    if (apiDto.getDiagnosticos() != null) {
        viewDto.setDiagnosticos(
            apiDto.getDiagnosticos().stream()
                .map(diagApi -> new DiagnosticoDTO(diagApi.getDescripcion(), diagApi.getFechaInicio(), diagApi.getEstadoProblema(), diagApi.getGradoCerteza()))
                .collect(Collectors.toList())
        );
    }
    
    if (apiDto.getInstruccionesDeSeguimiento() != null) {
        viewDto.setInstruccionesSeguimiento(
            apiDto.getInstruccionesDeSeguimiento().stream()
                .map(DocumentoClinicoApiDTO.InstruccionSeguimientoDTO::getDescripcion)
                .collect(Collectors.toList())
        );
    }
    return viewDto;
    }
}
