
package de.fhdo.terminologie.ws.administration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for actualProceeding complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="actualProceeding">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastChangeDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminologieName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminologieType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminologieVersionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actualProceeding", propOrder = {
    "lastChangeDate",
    "status",
    "terminologieName",
    "terminologieType",
    "terminologieVersionName"
})
public class ActualProceeding {

    protected String lastChangeDate;
    protected String status;
    protected String terminologieName;
    protected String terminologieType;
    protected String terminologieVersionName;

    /**
     * Gets the value of the lastChangeDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastChangeDate() {
        return lastChangeDate;
    }

    /**
     * Sets the value of the lastChangeDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastChangeDate(String value) {
        this.lastChangeDate = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the terminologieName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminologieName() {
        return terminologieName;
    }

    /**
     * Sets the value of the terminologieName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminologieName(String value) {
        this.terminologieName = value;
    }

    /**
     * Gets the value of the terminologieType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminologieType() {
        return terminologieType;
    }

    /**
     * Sets the value of the terminologieType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminologieType(String value) {
        this.terminologieType = value;
    }

    /**
     * Gets the value of the terminologieVersionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminologieVersionName() {
        return terminologieVersionName;
    }

    /**
     * Sets the value of the terminologieVersionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminologieVersionName(String value) {
        this.terminologieVersionName = value;
    }

}
