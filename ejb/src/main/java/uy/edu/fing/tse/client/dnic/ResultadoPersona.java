
package uy.edu.fing.tse.client.dnic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultadoPersona complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ResultadoPersona">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="objPersona" type="{http://agesic.gub.uy/pdi/ws/dnic}Persona" minOccurs="0"/>
 *         <element name="Errores" type="{http://agesic.gub.uy/pdi/ws/dnic}ListaMensajes" minOccurs="0"/>
 *         <element name="Warnings" type="{http://agesic.gub.uy/pdi/ws/dnic}ListaMensajes" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultadoPersona", propOrder = {
    "objPersona",
    "errores",
    "warnings"
})
public class ResultadoPersona {

    protected Persona objPersona;
    @XmlElement(name = "Errores")
    protected ListaMensajes errores;
    @XmlElement(name = "Warnings")
    protected ListaMensajes warnings;

    /**
     * Gets the value of the objPersona property.
     * 
     * @return
     *     possible object is
     *     {@link Persona }
     *     
     */
    public Persona getObjPersona() {
        return objPersona;
    }

    /**
     * Sets the value of the objPersona property.
     * 
     * @param value
     *     allowed object is
     *     {@link Persona }
     *     
     */
    public void setObjPersona(Persona value) {
        this.objPersona = value;
    }

    /**
     * Gets the value of the errores property.
     * 
     * @return
     *     possible object is
     *     {@link ListaMensajes }
     *     
     */
    public ListaMensajes getErrores() {
        return errores;
    }

    /**
     * Sets the value of the errores property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaMensajes }
     *     
     */
    public void setErrores(ListaMensajes value) {
        this.errores = value;
    }

    /**
     * Gets the value of the warnings property.
     * 
     * @return
     *     possible object is
     *     {@link ListaMensajes }
     *     
     */
    public ListaMensajes getWarnings() {
        return warnings;
    }

    /**
     * Sets the value of the warnings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaMensajes }
     *     
     */
    public void setWarnings(ListaMensajes value) {
        this.warnings = value;
    }

}
