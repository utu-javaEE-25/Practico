
package uy.edu.fing.tse.client.dnic;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uy.edu.fing.tse.client package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uy.edu.fing.tse.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ObtPersonaPorDocRequest }
     * 
     * @return
     *     the new instance of {@link ObtPersonaPorDocRequest }
     */
    public ObtPersonaPorDocRequest createObtPersonaPorDocRequest() {
        return new ObtPersonaPorDocRequest();
    }

    /**
     * Create an instance of {@link ObtPersonaPorDocResponse }
     * 
     * @return
     *     the new instance of {@link ObtPersonaPorDocResponse }
     */
    public ObtPersonaPorDocResponse createObtPersonaPorDocResponse() {
        return new ObtPersonaPorDocResponse();
    }

    /**
     * Create an instance of {@link ResultadoPersona }
     * 
     * @return
     *     the new instance of {@link ResultadoPersona }
     */
    public ResultadoPersona createResultadoPersona() {
        return new ResultadoPersona();
    }

    /**
     * Create an instance of {@link ProductDescRequest }
     * 
     * @return
     *     the new instance of {@link ProductDescRequest }
     */
    public ProductDescRequest createProductDescRequest() {
        return new ProductDescRequest();
    }

    /**
     * Create an instance of {@link ProductDescResponse }
     * 
     * @return
     *     the new instance of {@link ProductDescResponse }
     */
    public ProductDescResponse createProductDescResponse() {
        return new ProductDescResponse();
    }

    /**
     * Create an instance of {@link Persona }
     * 
     * @return
     *     the new instance of {@link Persona }
     */
    public Persona createPersona() {
        return new Persona();
    }

    /**
     * Create an instance of {@link ListaMensajes }
     * 
     * @return
     *     the new instance of {@link ListaMensajes }
     */
    public ListaMensajes createListaMensajes() {
        return new ListaMensajes();
    }

    /**
     * Create an instance of {@link Mensaje }
     * 
     * @return
     *     the new instance of {@link Mensaje }
     */
    public Mensaje createMensaje() {
        return new Mensaje();
    }

}
