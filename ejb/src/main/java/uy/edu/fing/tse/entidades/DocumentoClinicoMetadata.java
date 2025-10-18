package uy.edu.fing.tse.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "documento_clinico_metadata", schema = "central")
public class DocumentoClinicoMetadata {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long docId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "id_externa_doc")
    private String idExternaDoc;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public Long getDocId() {
        return docId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getIdExternaDoc() {
        return idExternaDoc;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public void setIdExternaDoc(String idExternaDoc) {
        this.idExternaDoc = idExternaDoc;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
