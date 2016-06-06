
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for codeSystemVersionEntityMembership complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="codeSystemVersionEntityMembership">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemEntity" type="{de.fhdo.termserver.types}codeSystemEntity" minOccurs="0"/>
 *         &lt;element name="codeSystemVersion" type="{de.fhdo.termserver.types}codeSystemVersion" minOccurs="0"/>
 *         &lt;element name="id" type="{de.fhdo.termserver.types}codeSystemVersionEntityMembershipId" minOccurs="0"/>
 *         &lt;element name="isAxis" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isMainClass" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="orderNr" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codeSystemVersionEntityMembership", namespace = "de.fhdo.termserver.types", propOrder = {
    "codeSystemEntity",
    "codeSystemVersion",
    "id",
    "isAxis",
    "isMainClass",
    "orderNr"
})
public class CodeSystemVersionEntityMembership {

    protected CodeSystemEntity codeSystemEntity;
    protected CodeSystemVersion codeSystemVersion;
    protected CodeSystemVersionEntityMembershipId id;
    protected Boolean isAxis;
    protected Boolean isMainClass;
    protected Long orderNr;

    /**
     * Gets the value of the codeSystemEntity property.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemEntity }
     *     
     */
    public CodeSystemEntity getCodeSystemEntity() {
        return codeSystemEntity;
    }

    /**
     * Sets the value of the codeSystemEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemEntity }
     *     
     */
    public void setCodeSystemEntity(CodeSystemEntity value) {
        this.codeSystemEntity = value;
    }

    /**
     * Gets the value of the codeSystemVersion property.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemVersion }
     *     
     */
    public CodeSystemVersion getCodeSystemVersion() {
        return codeSystemVersion;
    }

    /**
     * Sets the value of the codeSystemVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemVersion }
     *     
     */
    public void setCodeSystemVersion(CodeSystemVersion value) {
        this.codeSystemVersion = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemVersionEntityMembershipId }
     *     
     */
    public CodeSystemVersionEntityMembershipId getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemVersionEntityMembershipId }
     *     
     */
    public void setId(CodeSystemVersionEntityMembershipId value) {
        this.id = value;
    }

    /**
     * Gets the value of the isAxis property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAxis() {
        return isAxis;
    }

    /**
     * Sets the value of the isAxis property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAxis(Boolean value) {
        this.isAxis = value;
    }

    /**
     * Gets the value of the isMainClass property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMainClass() {
        return isMainClass;
    }

    /**
     * Sets the value of the isMainClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMainClass(Boolean value) {
        this.isMainClass = value;
    }

    /**
     * Gets the value of the orderNr property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOrderNr() {
        return orderNr;
    }

    /**
     * Sets the value of the orderNr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOrderNr(Long value) {
        this.orderNr = value;
    }

}
