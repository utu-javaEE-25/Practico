package uy.edu.fing.tse.client;

import jakarta.ejb.Stateless;
import jakarta.xml.ws.WebServiceRef;
import uy.edu.fing.tse.client.dnic.*;
import uy.edu.fing.tse.exception.PDIClientException;

@Stateless
public class PDIClient {

    // 1. Inyección del servicio generado usando @WebServiceRef
    // La wsdlLocation es opcional si el servidor ya la conoce.
    @WebServiceRef(wsdlLocation = "https://env-3816318.web.elasticloud.uy/ws/dnic.wsdl")
    private DnicPortService service;

    // 2. Inyección de la contraseña de la entidad
    private static final String PASSWORD_ENTIDAD = "hcen-uy-pwd-secreta";
    private static final String ORGANIZACION = "HCEN-UY"; // Asumiendo un valor estático
    private static final String TIPO_DOCUMENTO = "CI"; // Asumiendo Cédula de Identidad

    /**
     * Consume la operación ObtPersonaPorDoc del servicio externo.
     * * @param nroDocumento Número de documento de la persona.
     *
     * @return Objeto Persona con los datos consultados, o null/excepción si falla.
     */
    public Persona obtenerPersonaPorDocumento(String nroDocumento) {

        try {
            // Obtener el proxy (port) para la comunicación
            ResultadoPersona resultado = getResultadoPersona(nroDocumento);

            if (resultado != null && resultado.getObjPersona() != null) {
                return resultado.getObjPersona();
            } else if (resultado != null && resultado.getErrores() != null) {
                // Manejar errores de negocio reportados en la respuesta SOAP
            }
            return null;

        } catch (Exception e) {
            throw new PDIClientException("Fallo en la comunicación con el servicio Dnic.", e);
        }
    }

    private ResultadoPersona getResultadoPersona(String nroDocumento) {
        DnicPort port = service.getDnicPortSoap11();

        // 3. Construir la Solicitud (Request)
        ObtPersonaPorDocRequest request = new ObtPersonaPorDocRequest();
        request.setOrganizacion(ORGANIZACION);
        request.setPasswordEntidad(PASSWORD_ENTIDAD);
        request.setNroDocumento(nroDocumento); // Variable
        request.setTipoDocumento(TIPO_DOCUMENTO);

        // 4. Invocar la operación remota
        ObtPersonaPorDocResponse response = port.obtPersonaPorDoc(request);

        // 5. Procesar la Respuesta
        return response.getResultObtPersonaPorDoc();
    }
}
