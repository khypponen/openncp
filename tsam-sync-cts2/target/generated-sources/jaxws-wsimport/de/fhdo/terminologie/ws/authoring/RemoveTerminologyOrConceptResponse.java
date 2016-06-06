
package de.fhdo.terminologie.ws.authoring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RemoveTerminologyOrConceptResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RemoveTerminologyOrConceptResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://authoring.ws.terminologie.fhdo.de/}removeTerminologyOrConceptResponseType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoveTerminologyOrConceptResponse", propOrder = {
    "_return"
})
public class RemoveTerminologyOrConceptResponse {

    @XmlElement(name = "return")
    protected RemoveTerminologyOrConceptResponseType _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link RemoveTerminologyOrConceptResponseType }
     *     
     */
    public RemoveTerminologyOrConceptResponseType getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoveTerminologyOrConceptResponseType }
     *     
     */
    public void setReturn(RemoveTerminologyOrConceptResponseType value) {
        this._return = value;
    }

}
