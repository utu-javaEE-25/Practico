
package uy.edu.fing.tse.client.dnic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Mensaje complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="Mensaje">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="CodMensaje" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="Descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Mensaje", propOrder = {
    "codMensaje",
    "descripcion"
})
public class Mensaje {

    @XmlElement(name = "CodMensaje")
    protected int codMensaje;
    @XmlElement(name = "Descripcion", required = true)
    protected String descripcion;

    /**
     * Gets the value of the codMensaje property.
     * 
     */
    public int getCodMensaje() {
        return codMensaje;
    }

    /**
     * Sets the value of the codMensaje property.
     * 
     */
    public void setCodMensaje(int value) {
        this.codMensaje = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

}
