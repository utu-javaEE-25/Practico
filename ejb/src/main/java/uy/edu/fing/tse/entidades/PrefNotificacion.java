package uy.edu.fing.tse.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pref_notificacion", schema = "central")
public class PrefNotificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pref_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "alerta_acceso")
    private boolean alertaAcceso;

    @Column(name = "solicitud_acceso")
    private boolean solicitudAcceso;

    @Column(name = "canal")
    private String canal;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isAlertaAcceso() {
        return alertaAcceso;
    }

    public boolean isSolicitudAcceso() {
        return solicitudAcceso;
    }

    public String getCanal() {
        return canal;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAlertaAcceso(boolean alertaAcceso) {
        this.alertaAcceso = alertaAcceso;
    }

    public void setSolicitudAcceso(boolean solicitudAcceso) {
        this.solicitudAcceso = solicitudAcceso;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }
}
