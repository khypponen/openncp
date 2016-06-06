
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for licencedUser complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="licencedUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemVersion" type="{de.fhdo.termserver.types}codeSystemVersion" minOccurs="0"/>
 *         &lt;element name="id" type="{de.fhdo.termserver.types}licencedUserId" minOccurs="0"/>
 *         &lt;element name="licenceType" type="{de.fhdo.termserver.types}licenceType" minOccurs="0"/>
 *         &lt;element name="termUser" type="{de.fhdo.termserver.types}termUser" minOccurs="0"/>
 *         &lt;element name="validFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="validTo" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "licencedUser", namespace = "de.fhdo.termserver.types", propOrder = {
    "codeSystemVersion",
    "id",
    "licenceType",
    "termUser",
    "validFrom",
    "validTo"
})
public class LicencedUser {

    protected CodeSystemVersion codeSystemVersion;
    protected LicencedUserId id;
    protected LicenceType licenceType;
    protected TermUser termUser;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validFrom;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validTo;

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
     *     {@link LicencedUserId }
     *     
     */
    public LicencedUserId getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link LicencedUserId }
     *     
     */
    public void setId(LicencedUserId value) {
        this.id = value;
    }

    /**
     * Gets the value of the licenceType property.
     * 
     * @return
     *     possible object is
     *     {@link LicenceType }
     *     
     */
    public LicenceType getLicenceType() {
        return licenceType;
    }

    /**
     * Sets the value of the licenceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link LicenceType }
     *     
     */
    public void setLicenceType(LicenceType value) {
        this.licenceType = value;
    }

    /**
     * Gets the value of the termUser property.
     * 
     * @return
     *     possible object is
     *     {@link TermUser }
     *     
     */
    public TermUser getTermUser() {
        return termUser;
    }

    /**
     * Sets the value of the termUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link TermUser }
     *     
     */
    public void setTermUser(TermUser value) {
        this.termUser = value;
    }

    /**
     * Gets the value of the validFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the value of the validFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidFrom(XMLGregorianCalendar value) {
        this.validFrom = value;
    }

    /**
     * Gets the value of the validTo property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidTo() {
        return validTo;
    }

    /**
     * Sets the value of the validTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidTo(XMLGregorianCalendar value) {
        this.validTo = value;
    }

}
