package uy.edu.fing.tse.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificacion", schema = "central")
public class Notificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notif_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "solicitud_id")
    private Long solicitudId;

    @Column(name = "evento")
    private String evento;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSolicitudId() {
        return solicitudId;
    }

    public String getEvento() {
        return evento;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSolicitudId(Long solicitudId) {
        this.solicitudId = solicitudId;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
