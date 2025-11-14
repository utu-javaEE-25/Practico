package uy.edu.fing.tse.dto;

import java.io.Serializable;

public class DocumentoMetadataMensajeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long pacienteGlobalId;
    private String tenantSchemaName;
    private String idExternaDoc;
    private String tipoDocumento;

    // Getters y Setters
    public Long getPacienteGlobalId() { return pacienteGlobalId; }
    public void setPacienteGlobalId(Long pacienteGlobalId) { this.pacienteGlobalId = pacienteGlobalId; }
    
    public String getTenantSchemaName() { return tenantSchemaName; }
    public void setTenantSchemaName(String tenantSchemaName) { this.tenantSchemaName = tenantSchemaName; }

    public String getIdExternaDoc() { return idExternaDoc; }
    public void setIdExternaDoc(String idExternaDoc) { this.idExternaDoc = idExternaDoc; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
}