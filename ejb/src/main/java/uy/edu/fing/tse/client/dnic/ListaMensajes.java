
package uy.edu.fing.tse.client.dnic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ListaMensajes complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ListaMensajes">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Mensaje" type="{http://agesic.gub.uy/pdi/ws/dnic}Mensaje" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaMensajes", propOrder = {
    "mensaje"
})
public class ListaMensajes {

    @XmlElement(name = "Mensaje")
    protected List<Mensaje> mensaje;

    /**
     * Gets the value of the mensaje property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mensaje property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getMensaje().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mensaje }
     * </p>
     * 
     * 
     * @return
     *     The value of the mensaje property.
     */
    public List<Mensaje> getMensaje() {
        if (mensaje == null) {
            mensaje = new ArrayList<>();
        }
        return this.mensaje;
    }

}
