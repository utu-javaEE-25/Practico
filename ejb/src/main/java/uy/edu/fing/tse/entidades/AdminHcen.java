package uy.edu.fing.tse.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_global", schema = "central")
public class AdminHcen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(name = "gubuy_id")
    private String gubUyId;

    @Column(name = "email")
    private String email;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name= "ci")
    private String ci;

    public Long getId() {
        return id;
    }

    public String getGubUyId() {
        return gubUyId;
    }

    public String getEmail() {
        return email;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGubUyId(String gubUyId) {
        this.gubUyId = gubUyId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

}
