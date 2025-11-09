package uy.edu.fing.tse.api;

import jakarta.ejb.Local;
import uy.edu.fing.tse.dto.DocumentoDetalleDTO;
import uy.edu.fing.tse.dto.DocumentoMetadataDTO;
import java.util.List;

@Local
public interface HistoriaClinicaServiceLocal {
    List<DocumentoMetadataDTO> getHistoriaMetadata(Long usuarioId);
    DocumentoDetalleDTO getDocumentoDetalle(String idExternaDoc, Long tenantCustodioId);
}