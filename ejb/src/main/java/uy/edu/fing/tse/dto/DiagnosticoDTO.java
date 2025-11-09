package uy.edu.fing.tse.dto;

import java.io.Serializable;

/**
 * DTO auxiliar para representar la información estructurada de un diagnóstico.
 * Es utilizado por DocumentoDetalleDTO.
 */
public class DiagnosticoDTO implements Serializable {

    private String descripcion;
    private String fechaInicio;
    private String estadoProblema;
    private String gradoCerteza;

    // Constructor vacío
    public DiagnosticoDTO() {}

    // Constructor con todos los campos para facilitar su creación
    public DiagnosticoDTO(String descripcion, String fechaInicio, String estadoProblema, String gradoCerteza) {
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.estadoProblema = estadoProblema;
        this.gradoCerteza = gradoCerteza;
    }

    // --- Getters y Setters ---

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getEstadoProblema() { return estadoProblema; }
    public void setEstadoProblema(String estadoProblema) { this.estadoProblema = estadoProblema; }

    public String getGradoCerteza() { return gradoCerteza; }
    public void setGradoCerteza(String gradoCerteza) { this.gradoCerteza = gradoCerteza; }
}
