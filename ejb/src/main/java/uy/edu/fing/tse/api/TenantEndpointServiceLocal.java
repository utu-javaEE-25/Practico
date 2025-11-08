package uy.edu.fing.tse.api;

import java.util.List;

import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.TenantEndpoint;

@Local
public interface TenantEndpointServiceLocal {

    List<TenantEndpoint> listar();

    TenantEndpoint obtenerPorTenant(Long tenantId);

    TenantEndpoint crear(Long tenantId, String uriBase, String tipoAuth, String hashCliente);

    TenantEndpoint actualizar(Long tenantId, String uriBase, String tipoAuth, String hashCliente, boolean activo);

    void desactivar(Long tenantId);
}
