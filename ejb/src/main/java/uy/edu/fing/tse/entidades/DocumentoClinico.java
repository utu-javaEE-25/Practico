package uy.edu.fing.tse.entidades;

import java.io.Serializable;
import java.time.LocalDate;

public class DocumentoClinico implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String codigo;
    private String pacienteCI;
    private String prestadorRUT;
    private LocalDate fechaEmision;
    private boolean firmado;
    private String tipo;
    private String contenido;
    private String tenantId; // Para enfoque de tabla única con FK a tenant

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPacienteCI() {
        return pacienteCI;
    }

    public void setPacienteCI(String pacienteCI) {
        this.pacienteCI = pacienteCI;
    }

    public String getPrestadorRUT() {
        return prestadorRUT;
    }

    public void setPrestadorRUT(String prestadorRUT) {
        this.prestadorRUT = prestadorRUT;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
