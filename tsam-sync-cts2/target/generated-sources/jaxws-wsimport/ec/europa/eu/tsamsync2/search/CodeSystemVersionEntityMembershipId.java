
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for codeSystemVersionEntityMembershipId complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="codeSystemVersionEntityMembershipId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemEntityId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="codeSystemVersionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codeSystemVersionEntityMembershipId", namespace = "de.fhdo.termserver.types", propOrder = {
    "codeSystemEntityId",
    "codeSystemVersionId"
})
public class CodeSystemVersionEntityMembershipId {

    protected Long codeSystemEntityId;
    protected Long codeSystemVersionId;

    /**
     * Gets the value of the codeSystemEntityId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodeSystemEntityId() {
        return codeSystemEntityId;
    }

    /**
     * Sets the value of the codeSystemEntityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodeSystemEntityId(Long value) {
        this.codeSystemEntityId = value;
    }

    /**
     * Gets the value of the codeSystemVersionId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodeSystemVersionId() {
        return codeSystemVersionId;
    }

    /**
     * Sets the value of the codeSystemVersionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodeSystemVersionId(Long value) {
        this.codeSystemVersionId = value;
    }

}
