package uy.edu.fing.tse.entidades;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenant", schema = "central")
public class PrestadorSalud implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id @Column(name="tenant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tenantId;

    @Column(name="nombre_schema")
    private String nombreSchema;

    @Column(name="nombre")
    private String nombre;

    @Column(name="rut")
    private String rut;

    @Column(name="estado")
    private String estado;

    @Column(name="contacto_mail")
    private String contactoMail;

    @Column(name="tipo")
    private String tipo;

    @Column(name="fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name="fecha_modificacion")
    private LocalDateTime fechaModificacion;

    public Integer getTenantId() {
        return tenantId;
    }
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getNombreSchema() {
        return nombreSchema;
    }
    public void setNombreSchema(String nombreSchema) {
        this.nombreSchema = nombreSchema;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getRut() {
        return rut;
    }
    public void setRut(String rut) {
        this.rut = rut;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getContactoMail() {
        return contactoMail;
    }
    public void setContactoMail(String contactoMail) {
        this.contactoMail = contactoMail;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

}
