package uy.edu.fing.tse.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "politica_acceso", schema = "central")
public class PoliticaAcceso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "politica_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "id_profesional_autorizado")
    private Long idProfesionalAutorizado;

    @Column(name = "doc_metadata_id")
    private Long docMetadataId;

    @Column(name = "accion")
    private String accion;

    @Column(name = "ventana_desde")
    private LocalDateTime ventanaDesde;

    @Column(name = "ventana_hasta")
    private LocalDateTime ventanaHasta;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public Long getIdProfesionalAutorizado() { return idProfesionalAutorizado; }
    public void setIdProfesionalAutorizado(Long idProfesionalAutorizado) { this.idProfesionalAutorizado = idProfesionalAutorizado; }
    public Long getDocMetadataId() { return docMetadataId; }
    public void setDocMetadataId(Long docMetadataId) { this.docMetadataId = docMetadataId; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public LocalDateTime getVentanaDesde() { return ventanaDesde; }
    public void setVentanaDesde(LocalDateTime ventanaDesde) { this.ventanaDesde = ventanaDesde; }
    public LocalDateTime getVentanaHasta() { return ventanaHasta; }
    public void setVentanaHasta(LocalDateTime ventanaHasta) { this.ventanaHasta = ventanaHasta; }
}
