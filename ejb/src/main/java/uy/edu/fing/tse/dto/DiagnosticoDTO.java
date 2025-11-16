package uy.edu.fing.tse.dto;

import java.io.Serializable;

public class DiagnosticoDTO implements Serializable {

    private String descripcion;
    private String fechaInicio;
    private String estadoProblema;
    private String gradoCerteza;

    
    public DiagnosticoDTO() {}

   
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
