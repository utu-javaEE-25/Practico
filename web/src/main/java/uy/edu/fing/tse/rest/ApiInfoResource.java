package uy.edu.fing.tse.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Root REST endpoint providing API information.
 * Base path: /api
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ApiInfoResource {

    /**
     * GET /api - Get API information
     */
    @GET
    public Response getApiInfo() {
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("name", "Health Services Mobile API");
        apiInfo.put("version", "1.0");
        apiInfo.put("description", "REST API for mobile access to health services system");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("usuarios", "/api/usuarios");
        endpoints.put("trabajadores", "/api/trabajadores");
        endpoints.put("prestadores", "/api/prestadores");
        endpoints.put("documentos", "/api/documentos");
        
        apiInfo.put("endpoints", endpoints);
        apiInfo.put("documentation", "See API_DOCUMENTATION.md for detailed information");
        
        return Response.ok(apiInfo).build();
    }
}
