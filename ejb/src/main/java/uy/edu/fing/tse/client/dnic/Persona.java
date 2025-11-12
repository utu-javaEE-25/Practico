
package uy.edu.fing.tse.client.dnic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Persona complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="Persona">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="codTipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="nroDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="nombre1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="Apellido1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="fechaNacimiento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Persona", propOrder = {
    "codTipoDocumento",
    "nroDocumento",
    "nombre1",
    "apellido1",
    "fechaNacimiento"
})
public class Persona {

    @XmlElement(required = true)
    protected String codTipoDocumento;
    @XmlElement(required = true)
    protected String nroDocumento;
    @XmlElement(required = true)
    protected String nombre1;
    @XmlElement(name = "Apellido1", required = true)
    protected String apellido1;
    @XmlElement(required = true)
    protected String fechaNacimiento;

    /**
     * Gets the value of the codTipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoDocumento() {
        return codTipoDocumento;
    }

    /**
     * Sets the value of the codTipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoDocumento(String value) {
        this.codTipoDocumento = value;
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
     * Gets the value of the nombre1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre1() {
        return nombre1;
    }

    /**
     * Sets the value of the nombre1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre1(String value) {
        this.nombre1 = value;
    }

    /**
     * Gets the value of the apellido1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Sets the value of the apellido1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellido1(String value) {
        this.apellido1 = value;
    }

    /**
     * Gets the value of the fechaNacimiento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Sets the value of the fechaNacimiento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaNacimiento(String value) {
        this.fechaNacimiento = value;
    }

}
