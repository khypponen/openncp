
package de.fhdo.terminologie.ws.authoring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateConceptValueSetMembershipStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateConceptValueSetMembershipStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameter" type="{http://authoring.ws.terminologie.fhdo.de/}updateConceptValueSetMembershipStatusRequestType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateConceptValueSetMembershipStatus", propOrder = {
    "parameter"
})
public class UpdateConceptValueSetMembershipStatus {

    protected UpdateConceptValueSetMembershipStatusRequestType parameter;

    /**
     * Gets the value of the parameter property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateConceptValueSetMembershipStatusRequestType }
     *     
     */
    public UpdateConceptValueSetMembershipStatusRequestType getParameter() {
        return parameter;
    }

    /**
     * Sets the value of the parameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateConceptValueSetMembershipStatusRequestType }
     *     
     */
    public void setParameter(UpdateConceptValueSetMembershipStatusRequestType value) {
        this.parameter = value;
    }

}
