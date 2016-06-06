
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReturnConceptAssociationTypeDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReturnConceptAssociationTypeDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameter" type="{http://search.ws.terminologie.fhdo.de/}returnConceptAssociationTypeDetailsRequestType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReturnConceptAssociationTypeDetails", propOrder = {
    "parameter"
})
public class ReturnConceptAssociationTypeDetails {

    protected ReturnConceptAssociationTypeDetailsRequestType parameter;

    /**
     * Gets the value of the parameter property.
     * 
     * @return
     *     possible object is
     *     {@link ReturnConceptAssociationTypeDetailsRequestType }
     *     
     */
    public ReturnConceptAssociationTypeDetailsRequestType getParameter() {
        return parameter;
    }

    /**
     * Sets the value of the parameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnConceptAssociationTypeDetailsRequestType }
     *     
     */
    public void setParameter(ReturnConceptAssociationTypeDetailsRequestType value) {
        this.parameter = value;
    }

}
