package uy.edu.fing.tse.dto;

import java.io.Serializable;

public class SolicitudAccesoRequestDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String schemaTenantSolicitante;
    private String cedulaPaciente;
    private String idExternaDoc;
    private String motivo;
    private Long idProfesionalSolicitante;
    private String nombreProfesionalSolicitante;

    public String getSchemaTenantSolicitante() { return schemaTenantSolicitante; }
    public void setSchemaTenantSolicitante(String schemaTenantSolicitante) { this.schemaTenantSolicitante = schemaTenantSolicitante; }
    public String getCedulaPaciente() { return cedulaPaciente; }
    public void setCedulaPaciente(String cedulaPaciente) { this.cedulaPaciente = cedulaPaciente; }
    public String getIdExternaDoc() { return idExternaDoc; }
    public void setIdExternaDoc(String idExternaDoc) { this.idExternaDoc = idExternaDoc; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Long getIdProfesionalSolicitante() { return idProfesionalSolicitante; }
    public void setIdProfesionalSolicitante(Long idProfesionalSolicitante) { this.idProfesionalSolicitante = idProfesionalSolicitante; }
    public String getNombreProfesionalSolicitante() { return nombreProfesionalSolicitante; }
    public void setNombreProfesionalSolicitante(String nombreProfesionalSolicitante) { this.nombreProfesionalSolicitante = nombreProfesionalSolicitante; }
}