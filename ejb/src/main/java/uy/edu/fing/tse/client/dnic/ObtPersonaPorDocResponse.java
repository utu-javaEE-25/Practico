
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
 *         <element name="resultObtPersonaPorDoc" type="{http://agesic.gub.uy/pdi/ws/dnic}ResultadoPersona"/>
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
    "resultObtPersonaPorDoc"
})
@XmlRootElement(name = "ObtPersonaPorDocResponse")
public class ObtPersonaPorDocResponse {

    @XmlElement(required = true)
    protected ResultadoPersona resultObtPersonaPorDoc;

    /**
     * Gets the value of the resultObtPersonaPorDoc property.
     * 
     * @return
     *     possible object is
     *     {@link ResultadoPersona }
     *     
     */
    public ResultadoPersona getResultObtPersonaPorDoc() {
        return resultObtPersonaPorDoc;
    }

    /**
     * Sets the value of the resultObtPersonaPorDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultadoPersona }
     *     
     */
    public void setResultObtPersonaPorDoc(ResultadoPersona value) {
        this.resultObtPersonaPorDoc = value;
    }

}
