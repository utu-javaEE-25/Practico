package uy.edu.fing.tse.dto.apiperiferico;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoClinicoApiDTO implements Serializable {

    private PacienteInfo paciente;
    private ProfesionalInfo profesional;
    private String instanciaMedica;
    private String fechaAtencion;
    private String lugar;
    private String idExternaDoc;
    private LocalDateTime fechaGeneracion;
    private String custodio;
    private List<MotivoConsultaDTO> motivosDeConsulta;
    private List<DiagnosticoDTO> diagnosticos;
    private List<InstruccionSeguimientoDTO> instruccionesDeSeguimiento;

    // --- Getters y Setters para la clase principal ---

    public PacienteInfo getPaciente() { return paciente; }
    public void setPaciente(PacienteInfo paciente) { this.paciente = paciente; }

    public ProfesionalInfo getProfesional() { return profesional; }
    public void setProfesional(ProfesionalInfo profesional) { this.profesional = profesional; }

    public String getInstanciaMedica() { return instanciaMedica; }
    public void setInstanciaMedica(String instanciaMedica) { this.instanciaMedica = instanciaMedica; }

    public String getFechaAtencion() { return fechaAtencion; }
    public void setFechaAtencion(String fechaAtencion) { this.fechaAtencion = fechaAtencion; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public String getIdExternaDoc() { return idExternaDoc; }
    public void setIdExternaDoc(String idExternaDoc) { this.idExternaDoc = idExternaDoc; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getCustodio() { return custodio; }
    public void setCustodio(String custodio) { this.custodio = custodio; }

    public List<MotivoConsultaDTO> getMotivosDeConsulta() { return motivosDeConsulta; }
    public void setMotivosDeConsulta(List<MotivoConsultaDTO> motivosDeConsulta) { this.motivosDeConsulta = motivosDeConsulta; }

    public List<DiagnosticoDTO> getDiagnosticos() { return diagnosticos; }
    public void setDiagnosticos(List<DiagnosticoDTO> diagnosticos) { this.diagnosticos = diagnosticos; }

    public List<InstruccionSeguimientoDTO> getInstruccionesDeSeguimiento() { return instruccionesDeSeguimiento; }
    public void setInstruccionesDeSeguimiento(List<InstruccionSeguimientoDTO> instruccionesDeSeguimiento) { this.instruccionesDeSeguimiento = instruccionesDeSeguimiento; }

    // --- Clases anidadas estáticas con sus getters y setters ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PacienteInfo implements Serializable {
        private String nombreCompleto;
        private String nroDocumento;
        private String fechaNacimiento;
        private String sexo;

        public String getNombreCompleto() { return nombreCompleto; }
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

        public String getNroDocumento() { return nroDocumento; }
        public void setNroDocumento(String nroDocumento) { this.nroDocumento = nroDocumento; }

        public String getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

        public String getSexo() { return sexo; }
        public void setSexo(String sexo) { this.sexo = sexo; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProfesionalInfo implements Serializable {
        private String nombreCompleto;

        public String getNombreCompleto() { return nombreCompleto; }
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MotivoConsultaDTO implements Serializable {
        private String descripcion;

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DiagnosticoDTO implements Serializable {
        private String descripcion;
        private String fechaInicio;
        private String estadoProblema;
        private String gradoCerteza;

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public String getFechaInicio() { return fechaInicio; }
        public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

        public String getEstadoProblema() { return estadoProblema; }
        public void setEstadoProblema(String estadoProblema) { this.estadoProblema = estadoProblema; }

        public String getGradoCerteza() { return gradoCerteza; }
        public void setGradoCerteza(String gradoCerteza) { this.gradoCerteza = gradoCerteza; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InstruccionSeguimientoDTO implements Serializable {
        private String tipo;
        private String descripcion;

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }
}