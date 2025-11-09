package uy.edu.fing.tse.servicios;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uy.edu.fing.tse.dto.apiperiferico.DocumentoClinicoApiDTO;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.entidades.TenantEndpoint;
import uy.edu.fing.tse.persistencia.PrestadorSaludPerBean;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;
import uy.edu.fing.tse.persistencia.TenantEndpointDAO;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Stateless
public class PerifericoApiClient {

    @EJB
    private TenantEndpointDAO endpointDAO;
    @EJB
    private PrestadorSaludPerLocal prestadorDAO;

    private static final String SHARED_SECRET = "UnSecretoMuyLargoSeguroYComplejoQueNadieDebeAdivinarParaElComponentePeriferico12345";

    public DocumentoClinicoApiDTO getDocumento(String idExternaDoc, Long tenantCustodioId) throws Exception {
        // 1. Obtener los datos del prestador custodio
        PrestadorSalud custodio = prestadorDAO.obtenerPorId(tenantCustodioId);
        TenantEndpoint endpoint = endpointDAO.findByTenantId(tenantCustodioId);

        if (custodio == null || endpoint == null) {
            throw new Exception("Configuración del prestador custodio (ID: " + tenantCustodioId + ") no encontrada en HCEN.");
        }
        
        // 2. Generar el token JWT usando el nombre del schema del custodio
        String token = generarTokenDeServicio(custodio.getNombreSchema());

        // 3. Llamar a la API del periférico con el token
        return llamarApiConToken(endpoint, custodio.getNombreSchema(), idExternaDoc, token);
    }

    private String generarTokenDeServicio(String schemaName) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SHARED_SECRET.getBytes(StandardCharsets.UTF_8));
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            Date expiryDate = new Date(nowMillis + 60000); // 1 minuto de validez

            return Jwts.builder()
                    .setSubject("hcen_system_service")
                    .claim("tenant_id", schemaName)
                    .claim("rol", "SYSTEM")
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error fatal al generar el token de servicio.", e);
        }
    }

    private DocumentoClinicoApiDTO llamarApiConToken(TenantEndpoint endpoint, String schemaName, String idExternaDoc, String token) {
        Client client = ClientBuilder.newClient();
        try {
            // Construye la URL: http://{uriBase}/{schemaName}/api/documentos/externo/{idExternaDoc}
            WebTarget target = client.target(endpoint.getUriBase())
                                     .path("/{tenantId}/api/documentos/{idExternaDoc}")
                                     .resolveTemplate("tenantId", schemaName)
                                     .resolveTemplate("idExternaDoc", idExternaDoc);
            
            Invocation.Builder requestBuilder = target.request(MediaType.APPLICATION_JSON);
            requestBuilder.header("Authorization", "Bearer " + token);

            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(DocumentoClinicoApiDTO.class);
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                return null;
            } else {
                String errorBody = response.readEntity(String.class);
                throw new RuntimeException("La API del prestador devolvió un error. Código: " + response.getStatus() + ". Mensaje: " + errorBody);
            }
        } finally {
            client.close();
        }
    }
}
