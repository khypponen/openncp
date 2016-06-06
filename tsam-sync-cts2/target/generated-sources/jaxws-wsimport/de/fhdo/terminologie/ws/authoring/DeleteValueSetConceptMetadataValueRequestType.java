
package de.fhdo.terminologie.ws.authoring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import types.termserver.fhdo.de.ValueSetMetadataValue;


/**
 * <p>Java class for deleteValueSetConceptMetadataValueRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteValueSetConceptMetadataValueRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loginToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valueSetMetadataValue" type="{de.fhdo.termserver.types}valueSetMetadataValue" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteValueSetConceptMetadataValueRequestType", propOrder = {
    "loginToken",
    "valueSetMetadataValue"
})
public class DeleteValueSetConceptMetadataValueRequestType {

    protected String loginToken;
    protected ValueSetMetadataValue valueSetMetadataValue;

    /**
     * Gets the value of the loginToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginToken() {
        return loginToken;
    }

    /**
     * Sets the value of the loginToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginToken(String value) {
        this.loginToken = value;
    }

    /**
     * Gets the value of the valueSetMetadataValue property.
     * 
     * @return
     *     possible object is
     *     {@link ValueSetMetadataValue }
     *     
     */
    public ValueSetMetadataValue getValueSetMetadataValue() {
        return valueSetMetadataValue;
    }

    /**
     * Sets the value of the valueSetMetadataValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueSetMetadataValue }
     *     
     */
    public void setValueSetMetadataValue(ValueSetMetadataValue value) {
        this.valueSetMetadataValue = value;
    }

}
