package uy.edu.fing.tse.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tenant_endpoint", schema = "central")
public class TenantEndpoint {

    @Id
    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "uri_base")
    private String uriBase;

    @Column(name = "tipo_auth")
    private String tipoAuth;

    @Column(name = "hash_cliente")
    private String hashCliente;

    @Column(name = "activo")
    private boolean activo;

    public Long getTenantId() {
        return tenantId;
    }

    public String getUriBase() {
        return uriBase;
    }

    public String getTipoAuth() {
        return tipoAuth;
    }

    public String getHashCliente() {
        return hashCliente;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public void setUriBase(String uriBase) {
        this.uriBase = uriBase;
    }

    public void setTipoAuth(String tipoAuth) {
        this.tipoAuth = tipoAuth;
    }

    public void setHashCliente(String hashCliente) {
        this.hashCliente = hashCliente;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
