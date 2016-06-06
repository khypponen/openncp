
package ec.europa.eu.tsamsync2.search;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for licenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="licenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemVersion" type="{de.fhdo.termserver.types}codeSystemVersion" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="licencedUsers" type="{de.fhdo.termserver.types}licencedUser" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="typeTxt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "licenceType", namespace = "de.fhdo.termserver.types", propOrder = {
    "codeSystemVersion",
    "id",
    "licencedUsers",
    "typeTxt"
})
public class LicenceType {

    protected CodeSystemVersion codeSystemVersion;
    protected Long id;
    @XmlElement(nillable = true)
    protected List<LicencedUser> licencedUsers;
    protected String typeTxt;

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

    /**
     * Gets the value of the licencedUsers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the licencedUsers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLicencedUsers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LicencedUser }
     * 
     * 
     */
    public List<LicencedUser> getLicencedUsers() {
        if (licencedUsers == null) {
            licencedUsers = new ArrayList<LicencedUser>();
        }
        return this.licencedUsers;
    }

    /**
     * Gets the value of the typeTxt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeTxt() {
        return typeTxt;
    }

    /**
     * Sets the value of the typeTxt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeTxt(String value) {
        this.typeTxt = value;
    }

}
