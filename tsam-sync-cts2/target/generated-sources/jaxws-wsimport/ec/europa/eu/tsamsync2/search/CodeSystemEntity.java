
package ec.europa.eu.tsamsync2.search;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for codeSystemEntity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="codeSystemEntity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemEntityVersions" type="{de.fhdo.termserver.types}codeSystemEntityVersion" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codeSystemVersionEntityMemberships" type="{de.fhdo.termserver.types}codeSystemVersionEntityMembership" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="currentVersionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codeSystemEntity", namespace = "de.fhdo.termserver.types", propOrder = {
    "codeSystemEntityVersions",
    "codeSystemVersionEntityMemberships",
    "currentVersionId",
    "id"
})
public class CodeSystemEntity {

    @XmlElement(nillable = true)
    protected List<CodeSystemEntityVersion> codeSystemEntityVersions;
    @XmlElement(nillable = true)
    protected List<CodeSystemVersionEntityMembership> codeSystemVersionEntityMemberships;
    protected Long currentVersionId;
    protected Long id;

    /**
     * Gets the value of the codeSystemEntityVersions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeSystemEntityVersions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeSystemEntityVersions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeSystemEntityVersion }
     * 
     * 
     */
    public List<CodeSystemEntityVersion> getCodeSystemEntityVersions() {
        if (codeSystemEntityVersions == null) {
            codeSystemEntityVersions = new ArrayList<CodeSystemEntityVersion>();
        }
        return this.codeSystemEntityVersions;
    }

    /**
     * Gets the value of the codeSystemVersionEntityMemberships property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeSystemVersionEntityMemberships property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeSystemVersionEntityMemberships().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeSystemVersionEntityMembership }
     * 
     * 
     */
    public List<CodeSystemVersionEntityMembership> getCodeSystemVersionEntityMemberships() {
        if (codeSystemVersionEntityMemberships == null) {
            codeSystemVersionEntityMemberships = new ArrayList<CodeSystemVersionEntityMembership>();
        }
        return this.codeSystemVersionEntityMemberships;
    }

    /**
     * Gets the value of the currentVersionId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCurrentVersionId() {
        return currentVersionId;
    }

    /**
     * Sets the value of the currentVersionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCurrentVersionId(Long value) {
        this.currentVersionId = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

}
