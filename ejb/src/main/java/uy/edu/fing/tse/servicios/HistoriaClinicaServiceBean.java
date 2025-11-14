package uy.edu.fing.tse.servicios;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
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
    public DocumentoDetalleDTO verificarYObtenerDocumento(String cedulaPaciente, String idExternaDoc, Long idTenantSolicitante, Long idProfesional, Long docMetadataId) {
        UsuarioServicioSalud paciente = usuarioDAO.buscarPorCI(cedulaPaciente);
        if (paciente == null) throw new IllegalArgumentException("Paciente no encontrado.");

        // 1. VERIFICAR PERMISO EN PoliticaAcceso
        PoliticaAcceso politica = politicaDAO.findPoliticaActiva(paciente.getId(), idTenantSolicitante, idProfesional, docMetadataId);
        
        if (politica == null) {
            // Si no hay política, lanzamos la excepción específica.
            throw new AccesoNoAutorizadoException("Acceso no permitido por política del usuario. Debe solicitar permiso.");
        }

        // 2. SI HAY PERMISO, OBTENER EL DOCUMENTO
        // Buscamos el ID del tenant custodio a partir del documento
        DocumentoClinicoMetadata metadata = metadataDAO.findByIdExternaDoc(idExternaDoc); // Necesitarás este método en el DAO
        if (metadata == null) throw new IllegalArgumentException("Metadatos del documento no encontrados.");
        
        return getDocumentoDetalle(idExternaDoc, metadata.getTenantId());
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

        if (apiDto.getProfesional() != null) {
            viewDto.setAutor(apiDto.getProfesional().getNombreCompleto());
        }

        viewDto.setDocumentoId(apiDto.getIdExternaDoc());
        viewDto.setFechaGeneracion(apiDto.getFechaGeneracion() != null ? apiDto.getFechaGeneracion().toString() : "N/A");
        viewDto.setCustodio(apiDto.getCustodio());
        
        if (apiDto.getMotivosDeConsulta() != null && !apiDto.getMotivosDeConsulta().isEmpty()) {
            viewDto.setMotivoConsulta(apiDto.getMotivosDeConsulta().get(0).getDescripcion());
        }
        
        if (apiDto.getDiagnosticos() != null && !apiDto.getDiagnosticos().isEmpty()) {
            DocumentoClinicoApiDTO.DiagnosticoDTO diagApi = apiDto.getDiagnosticos().get(0);
            viewDto.setDiagnostico(new DiagnosticoDTO(diagApi.getDescripcion(), diagApi.getFechaInicio(), diagApi.getEstadoProblema(), diagApi.getGradoCerteza()));
        }
        
        if (apiDto.getInstruccionesDeSeguimiento() != null) {
            List<String> instrucciones = apiDto.getInstruccionesDeSeguimiento().stream()
                .map(inst -> inst.getDescripcion()) 
                .collect(Collectors.toList());
        viewDto.setInstruccionesSeguimiento(instrucciones);
    }
        
        return viewDto;
    }
}
