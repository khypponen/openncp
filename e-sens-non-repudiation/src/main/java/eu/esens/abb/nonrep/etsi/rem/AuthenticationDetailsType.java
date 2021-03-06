//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.29 at 12:47:48 PM CET 
//


package eu.esens.abb.nonrep.etsi.rem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AuthenticationDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AuthenticationDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuthenticationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="AuthenticationMethod" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="AdditionalDetails" type="{http://uri.etsi.org/01903/v1.3.2#}AnyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthenticationDetailsType", namespace = "http://uri.etsi.org/02640/v1#", propOrder = {
    "authenticationTime",
    "authenticationMethod",
    "additionalDetails"
})
public class AuthenticationDetailsType {

    @XmlElement(name = "AuthenticationTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar authenticationTime;
    @XmlElement(name = "AuthenticationMethod", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String authenticationMethod;
    @XmlElement(name = "AdditionalDetails")
    protected AnyType additionalDetails;

    /**
     * Gets the value of the authenticationTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAuthenticationTime() {
        return authenticationTime;
    }

    /**
     * Sets the value of the authenticationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAuthenticationTime(XMLGregorianCalendar value) {
        this.authenticationTime = value;
    }

    /**
     * Gets the value of the authenticationMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    /**
     * Sets the value of the authenticationMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationMethod(String value) {
        this.authenticationMethod = value;
    }

    /**
     * Gets the value of the additionalDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getAdditionalDetails() {
        return additionalDetails;
    }

    /**
     * Sets the value of the additionalDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setAdditionalDetails(AnyType value) {
        this.additionalDetails = value;
    }

}
