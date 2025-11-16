package uy.edu.fing.tse.api;

import jakarta.ejb.Local;
import uy.edu.fing.tse.dto.DocumentoDetalleDTO;
import uy.edu.fing.tse.dto.DocumentoMetadataDTO;
import uy.edu.fing.tse.dto.apiperiferico.DocumentoClinicoApiDTO;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import java.util.List;

@Local
public interface HistoriaClinicaServiceLocal {
    List<DocumentoMetadataDTO> getHistoriaMetadata(Long usuarioId);
    DocumentoDetalleDTO getDocumentoDetalle(String idExternaDoc, Long tenantCustodioId);
    List<DocumentoMetadataDTO> getHistoriaMetadataPorCedula(String cedula);
    DocumentoClinicoApiDTO verificarYObtenerDocumento(String cedulaPaciente, String idExternaDoc, PrestadorSalud tenantSolicitante, Long idProfesional, Long docMetadataId);
}