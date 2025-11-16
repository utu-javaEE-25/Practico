package uy.edu.fing.tse.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_log", schema = "central")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long id;

    @Column(name = "tipo_actor")
    private String tipoActor;

    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "actor_tenant_id") 
    private Long actorTenantId;   

    @Column(name = "accion")
    private String accion;

    @Column(name = "recurso_id")
    private Long recursoId;

    @Column(name = "resultado")
    private String resultado;

    @Column(name = "ip")
    private String ip;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public Long getId() {
        return id;
    }

    public String getTipoActor() {
        return tipoActor;
    }

    public Long getActorId() {
        return actorId;
    }

    public String getAccion() {
        return accion;
    }

    public Long getRecursoId() {
        return recursoId;
    }

    public String getResultado() {
        return resultado;
    }

    public String getIp() {
        return ip;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipoActor(String tipoActor) {
        this.tipoActor = tipoActor;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setRecursoId(Long recursoId) {
        this.recursoId = recursoId;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getActorTenantId() { 
        return actorTenantId;
    }

    public void setActorTenantId(Long actorTenantId) { 
        this.actorTenantId = actorTenantId;
    }
}
