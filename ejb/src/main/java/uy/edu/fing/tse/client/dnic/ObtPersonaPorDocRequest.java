
package uy.edu.fing.tse.client.dnic;

import jakarta.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="organizacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="passwordEntidad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="NroDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="TipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "organizacion",
    "passwordEntidad",
    "nroDocumento",
    "tipoDocumento"
})
@XmlRootElement(name = "ObtPersonaPorDocRequest")
public class ObtPersonaPorDocRequest {

    @XmlElement(required = true)
    protected String organizacion;
    @XmlElement(required = true)
    protected String passwordEntidad;
    @XmlElement(name = "NroDocumento", required = true)
    protected String nroDocumento;
    @XmlElement(name = "TipoDocumento", required = true)
    protected String tipoDocumento;

    /**
     * Gets the value of the organizacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizacion() {
        return organizacion;
    }

    /**
     * Sets the value of the organizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizacion(String value) {
        this.organizacion = value;
    }

    /**
     * Gets the value of the passwordEntidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordEntidad() {
        return passwordEntidad;
    }

    /**
     * Sets the value of the passwordEntidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordEntidad(String value) {
        this.passwordEntidad = value;
    }

    /**
     * Gets the value of the nroDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNroDocumento() {
        return nroDocumento;
    }

    /**
     * Sets the value of the nroDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNroDocumento(String value) {
        this.nroDocumento = value;
    }

    /**
     * Gets the value of the tipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Sets the value of the tipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

}
