package uy.edu.fing.tse.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_acceso", schema = "central")
public class SolicitudAcceso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitud_id")
    private Long id;

    @Column(name = "requester_tenant_id")
    private Long requesterTenantId;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "doc_id")
    private Long docId;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    public Long getId() {
        return id;
    }

    public Long getRequesterTenantId() {
        return requesterTenantId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public Long getDocId() {
        return docId;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequesterTenantId(Long requesterTenantId) {
        this.requesterTenantId = requesterTenantId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
}
