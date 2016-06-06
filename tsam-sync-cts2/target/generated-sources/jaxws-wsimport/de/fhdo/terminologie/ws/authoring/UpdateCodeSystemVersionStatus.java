
package de.fhdo.terminologie.ws.authoring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateCodeSystemVersionStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateCodeSystemVersionStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameter" type="{http://authoring.ws.terminologie.fhdo.de/}updateCodeSystemVersionStatusRequestType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateCodeSystemVersionStatus", propOrder = {
    "parameter"
})
public class UpdateCodeSystemVersionStatus {

    protected UpdateCodeSystemVersionStatusRequestType parameter;

    /**
     * Gets the value of the parameter property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateCodeSystemVersionStatusRequestType }
     *     
     */
    public UpdateCodeSystemVersionStatusRequestType getParameter() {
        return parameter;
    }

    /**
     * Sets the value of the parameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateCodeSystemVersionStatusRequestType }
     *     
     */
    public void setParameter(UpdateCodeSystemVersionStatusRequestType value) {
        this.parameter = value;
    }

}
