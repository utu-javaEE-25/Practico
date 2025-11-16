package uy.edu.fing.tse.dto;

import java.io.Serializable;
import java.util.List;


public class DocumentoDetalleDTO implements Serializable {

  
    private String pacienteNombre;
    private String pacienteNroDocumento;
    private String pacienteFechaNacimiento;
    private String pacienteSexo;

   
    private String instanciaMedica;
    private String fechaAtencion;
    private String lugar;
    private String autor;
    private String documentoId;
    private String fechaGeneracion;
    private String custodio;

    
    private List<String> motivosDeConsulta;
    private List<DiagnosticoDTO> diagnosticos;
    private List<String> instruccionesSeguimiento;

    
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getPacienteNroDocumento() { return pacienteNroDocumento; }
    public void setPacienteNroDocumento(String pacienteNroDocumento) { this.pacienteNroDocumento = pacienteNroDocumento; }
    public String getPacienteFechaNacimiento() { return pacienteFechaNacimiento; }
    public void setPacienteFechaNacimiento(String pacienteFechaNacimiento) { this.pacienteFechaNacimiento = pacienteFechaNacimiento; }
    public String getPacienteSexo() { return pacienteSexo; }
    public void setPacienteSexo(String pacienteSexo) { this.pacienteSexo = pacienteSexo; }
    public String getInstanciaMedica() { return instanciaMedica; }
    public void setInstanciaMedica(String instanciaMedica) { this.instanciaMedica = instanciaMedica; }
    public String getFechaAtencion() { return fechaAtencion; }
    public void setFechaAtencion(String fechaAtencion) { this.fechaAtencion = fechaAtencion; }
    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getDocumentoId() { return documentoId; }
    public void setDocumentoId(String documentoId) { this.documentoId = documentoId; }
    public String getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(String fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
    public String getCustodio() { return custodio; }
    public void setCustodio(String custodio) { this.custodio = custodio; }
    public List<String> getMotivosDeConsulta() { return motivosDeConsulta; }
    public void setMotivosDeConsulta(List<String> motivosDeConsulta) { this.motivosDeConsulta = motivosDeConsulta; }
    public List<DiagnosticoDTO> getDiagnosticos() { return diagnosticos; }
    public void setDiagnosticos(List<DiagnosticoDTO> diagnosticos) { this.diagnosticos = diagnosticos; }
    public List<String> getInstruccionesSeguimiento() { return instruccionesSeguimiento; }
    public void setInstruccionesSeguimiento(List<String> instruccionesSeguimiento) { this.instruccionesSeguimiento = instruccionesSeguimiento; }
}