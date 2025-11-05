package uy.edu.fing.tse.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Data Transfer Object que representa la vista detallada de un documento clínico.
 * Es un objeto "plano" diseñado específicamente para ser consumido por la vista (JSP).
 * El servicio EJB se encarga de mapear la respuesta de la API (DocumentoClinicoApiDTO) a este DTO.
 */
public class DocumentoDetalleDTO implements Serializable {

    // --- Datos del Paciente ---
    private String pacienteNombre;
    private String pacienteNroDocumento;
    private String pacienteFechaNacimiento;
    private String pacienteSexo;

    // --- Datos de la Instancia Médica ---
    private String instanciaMedica;
    private String fechaAtencion;
    private String lugar;
    private String autor;
    private String documentoId;
    private String fechaGeneracion;
    private String custodio;

    // --- Contenido Clínico Estructurado ---
    private String motivoConsulta;
    private DiagnosticoDTO diagnostico;
    private List<String> instruccionesSeguimiento; // Usamos un String con saltos de línea (\n)

    // Constructor vacío
    public DocumentoDetalleDTO() {}

    // --- Getters y Setters para todos los campos ---

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

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }

    public DiagnosticoDTO getDiagnostico() { return diagnostico; }
    public void setDiagnostico(DiagnosticoDTO diagnostico) { this.diagnostico = diagnostico; }

    public List<String> getInstruccionesSeguimiento() { return instruccionesSeguimiento; }
    public void setInstruccionesSeguimiento(List<String> instruccionesSeguimiento) { this.instruccionesSeguimiento = instruccionesSeguimiento; }
}