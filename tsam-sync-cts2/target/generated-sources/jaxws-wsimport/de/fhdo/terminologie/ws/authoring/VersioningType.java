
package de.fhdo.terminologie.ws.authoring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for versioningType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="versioningType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="copyConcepts" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="createNewVersion" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="majorUpdate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="minorUpdate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "versioningType", propOrder = {
    "copyConcepts",
    "createNewVersion",
    "majorUpdate",
    "minorUpdate"
})
public class VersioningType {

    protected Boolean copyConcepts;
    protected Boolean createNewVersion;
    protected Boolean majorUpdate;
    protected Boolean minorUpdate;

    /**
     * Gets the value of the copyConcepts property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCopyConcepts() {
        return copyConcepts;
    }

    /**
     * Sets the value of the copyConcepts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCopyConcepts(Boolean value) {
        this.copyConcepts = value;
    }

    /**
     * Gets the value of the createNewVersion property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCreateNewVersion() {
        return createNewVersion;
    }

    /**
     * Sets the value of the createNewVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCreateNewVersion(Boolean value) {
        this.createNewVersion = value;
    }

    /**
     * Gets the value of the majorUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMajorUpdate() {
        return majorUpdate;
    }

    /**
     * Sets the value of the majorUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMajorUpdate(Boolean value) {
        this.majorUpdate = value;
    }

    /**
     * Gets the value of the minorUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMinorUpdate() {
        return minorUpdate;
    }

    /**
     * Sets the value of the minorUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMinorUpdate(Boolean value) {
        this.minorUpdate = value;
    }

}
