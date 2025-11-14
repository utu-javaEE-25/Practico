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

    public static final String MULTITENANT_URI_PREFIX = "http://pruebamulti.web.elasticloud.uy/";
    private static final String MULTITENANT_SHARED_SECRET = "UnSecretoMuyLargoSeguroYComplejoQueNadieDebeAdivinarParaElComponentePeriferico12345";

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
    public TenantEndpoint crear(Long tenantId, boolean esMultitenant, String uriBaseInput, String tipoAuth, String hashCliente) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Debe indicar el tenant.");
        }
        if (obtenerPorTenant(tenantId) != null) {
            throw new IllegalArgumentException("El tenant ya tiene un endpoint configurado.");
        }

        TenantEndpoint endpoint = new TenantEndpoint();
        endpoint.setTenantId(tenantId);
        actualizarValores(endpoint, esMultitenant, uriBaseInput, tipoAuth, hashCliente, true);

        em.persist(endpoint);
        return endpoint;
    }

    @Override
    public TenantEndpoint actualizar(Long tenantId, boolean esMultitenant, String uriBaseInput, String tipoAuth, String hashCliente, boolean activo) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Debe indicar el tenant.");
        }

        TenantEndpoint existente = obtenerPorTenant(tenantId);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un endpoint configurado para el tenant.");
        }

        actualizarValores(existente, esMultitenant, uriBaseInput, tipoAuth, hashCliente, activo);
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

    private void actualizarValores(TenantEndpoint endpoint, boolean esMultitenant, String uriInput, String tipoAuth, String hashCliente, boolean activo) {
        endpoint.setEsMultitenant(esMultitenant);
        endpoint.setUriBase(resolverUriBase(uriInput, esMultitenant));
        endpoint.setTipoAuth(normalizarTexto(tipoAuth));
        endpoint.setHashCliente(resolverHash(hashCliente, esMultitenant));
        endpoint.setActivo(activo);
    }

    private String resolverUriBase(String valor, boolean esMultitenant) {
        if (esMultitenant) {
            return construirUriMultitenant(valor);
        }
        return normalizarUri(valor);
    }

    private String construirUriMultitenant(String sufijo) {
        String limpio = normalizarTexto(sufijo);
        if (limpio == null) {
            throw new IllegalArgumentException("Debe indicar el sufijo para construir la URI multitenant.");
        }
        limpio = limpiarSlashes(limpio);
        if (limpio.isEmpty()) {
            throw new IllegalArgumentException("El sufijo para la URI multitenant no puede quedar vacio.");
        }
        return MULTITENANT_URI_PREFIX + limpio + "/";
    }

    private String limpiarSlashes(String valor) {
        String resultado = valor;
        while (resultado.startsWith("/")) {
            resultado = resultado.substring(1);
        }
        while (resultado.endsWith("/")) {
            resultado = resultado.substring(0, resultado.length() - 1);
        }
        return resultado;
    }

    private String resolverHash(String hashCliente, boolean esMultitenant) {
        if (esMultitenant) {
            return MULTITENANT_SHARED_SECRET;
        }
        String valor = normalizarTexto(hashCliente);
        if (valor == null) {
            throw new IllegalArgumentException("Debe indicar el hash/secreto del cliente para endpoints externos.");
        }
        return valor;
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
