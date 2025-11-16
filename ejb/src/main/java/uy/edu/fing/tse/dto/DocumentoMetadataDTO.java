package uy.edu.fing.tse.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DocumentoMetadataDTO implements Serializable {

    private String idExternaDoc; 
    private Long idCustodio;   
    private String tipoDocumento;
    private LocalDateTime fechaCreacion;
    private String nombrePrestador;
    private String schemaCustodio;

  
    public String getIdExternaDoc() { return idExternaDoc; }
    public void setIdExternaDoc(String idExternaDoc) { this.idExternaDoc = idExternaDoc; }
    public Long getIdCustodio() { return idCustodio; }
    public void setIdCustodio(Long idCustodio) { this.idCustodio = idCustodio; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getNombrePrestador() { return nombrePrestador; }
    public void setNombrePrestador(String nombrePrestador) { this.nombrePrestador = nombrePrestador; }
    public String getSchemaCustodio() { return schemaCustodio; }
    public void setSchemaCustodio(String schemaCustodio) { this.schemaCustodio = schemaCustodio; }
}