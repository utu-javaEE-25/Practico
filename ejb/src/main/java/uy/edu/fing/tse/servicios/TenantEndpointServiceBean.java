package uy.edu.fing.tse.servicios;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import uy.edu.fing.tse.api.TenantEndpointServiceLocal;
import uy.edu.fing.tse.entidades.TenantEndpoint;

@Stateless
public class TenantEndpointServiceBean implements TenantEndpointServiceLocal {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    @Override
    public List<TenantEndpoint> listar() {
        TypedQuery<TenantEndpoint> query = em.createQuery(
                "SELECT t FROM TenantEndpoint t ORDER BY t.tenantId",
                TenantEndpoint.class);
        return query.getResultList();
    }

    @Override
    public TenantEndpoint obtenerPorTenant(Long tenantId) {
        if (tenantId == null) {
            return null;
        }
        return em.find(TenantEndpoint.class, tenantId);
    }

    @Override
    public TenantEndpoint crear(Long tenantId, String uriBase, String tipoAuth, String hashCliente) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Debe indicar el tenant.");
        }
        if (obtenerPorTenant(tenantId) != null) {
            throw new IllegalArgumentException("El tenant ya tiene un endpoint configurado.");
        }

        TenantEndpoint endpoint = new TenantEndpoint();
        endpoint.setTenantId(tenantId);
        actualizarValores(endpoint, uriBase, tipoAuth, hashCliente, true);

        em.persist(endpoint);
        return endpoint;
    }

    @Override
    public TenantEndpoint actualizar(Long tenantId, String uriBase, String tipoAuth, String hashCliente, boolean activo) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Debe indicar el tenant.");
        }

        TenantEndpoint existente = obtenerPorTenant(tenantId);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un endpoint configurado para el tenant.");
        }

        actualizarValores(existente, uriBase, tipoAuth, hashCliente, activo);
        return existente;
    }

    @Override
    public void desactivar(Long tenantId) {
        TenantEndpoint endpoint = obtenerPorTenant(tenantId);
        if (endpoint == null) {
            throw new IllegalArgumentException("No existe un endpoint configurado para el tenant.");
        }
        endpoint.setActivo(false);
    }

    private void actualizarValores(TenantEndpoint endpoint, String uriBase, String tipoAuth, String hashCliente, boolean activo) {
        endpoint.setUriBase(normalizarUri(uriBase));
        endpoint.setTipoAuth(normalizarTexto(tipoAuth));
        endpoint.setHashCliente(normalizarTexto(hashCliente));
        endpoint.setActivo(activo);
    }

    private String normalizarUri(String uriBase) {
        if (uriBase == null || uriBase.isBlank()) {
            throw new IllegalArgumentException("La URI base no puede ser nula.");
        }

        String valor = uriBase.trim();
        try {
            URI uri = new URI(valor);
            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new IllegalArgumentException("La URI base debe incluir el esquema y el host.");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("La URI base no tiene un formato valido.");
        }

        if (!valor.endsWith("/")) {
            valor = valor + "/";
        }
        return valor;
    }

    private String normalizarTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
