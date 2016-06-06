
package de.fhdo.terminologie.ws.administration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for returnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="returnType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="count" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="overallErrorCategory" type="{http://administration.ws.terminologie.fhdo.de/}overallErrorCategory" minOccurs="0"/>
 *         &lt;element name="status" type="{http://administration.ws.terminologie.fhdo.de/}status" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "returnType", propOrder = {
    "count",
    "message",
    "overallErrorCategory",
    "status"
})
public class ReturnType {

    protected int count;
    protected String message;
    protected OverallErrorCategory overallErrorCategory;
    protected Status status;

    /**
     * Gets the value of the count property.
     * 
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     */
    public void setCount(int value) {
        this.count = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the overallErrorCategory property.
     * 
     * @return
     *     possible object is
     *     {@link OverallErrorCategory }
     *     
     */
    public OverallErrorCategory getOverallErrorCategory() {
        return overallErrorCategory;
    }

    /**
     * Sets the value of the overallErrorCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link OverallErrorCategory }
     *     
     */
    public void setOverallErrorCategory(OverallErrorCategory value) {
        this.overallErrorCategory = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

}
