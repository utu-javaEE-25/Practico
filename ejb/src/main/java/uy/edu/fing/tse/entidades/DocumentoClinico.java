package uy.edu.fing.tse.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "documento_clinico")
public class DocumentoClinico implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;
    
    @Column(name = "paciente_ci", nullable = false, length = 8)
    private String pacienteCI;
    
    @Column(name = "prestador_rut", nullable = false, length = 12)
    private String prestadorRUT;
    
    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;
    
    private boolean firmado;
    
    @Column(length = 50)
    private String tipo;
    
    @Column(columnDefinition = "TEXT")
    private String contenido;

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
}
