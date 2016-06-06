
package epsos.ccd.carecom.tsam.synchronizer.webservice.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for listCodeSystems complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listCodeSystems">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemId" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codeSystemOid" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codeSystemName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codeSystemValidFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="codeSystemDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deltaStartTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="deltaEndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listCodeSystems", propOrder = {
    "codeSystemId",
    "codeSystemOid",
    "codeSystemName",
    "codeSystemValidFrom",
    "codeSystemDescription",
    "deltaStartTime",
    "deltaEndTime"
})
public class ListCodeSystems {

    @XmlElement(type = Long.class)
    protected List<Long> codeSystemId;
    protected List<String> codeSystemOid;
    protected String codeSystemName;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar codeSystemValidFrom;
    protected String codeSystemDescription;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deltaStartTime;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deltaEndTime;

    /**
     * Gets the value of the codeSystemId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeSystemId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeSystemId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getCodeSystemId() {
        if (codeSystemId == null) {
            codeSystemId = new ArrayList<Long>();
        }
        return this.codeSystemId;
    }

    /**
     * Gets the value of the codeSystemOid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeSystemOid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeSystemOid().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCodeSystemOid() {
        if (codeSystemOid == null) {
            codeSystemOid = new ArrayList<String>();
        }
        return this.codeSystemOid;
    }

    /**
     * Gets the value of the codeSystemName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * Sets the value of the codeSystemName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeSystemName(String value) {
        this.codeSystemName = value;
    }

    /**
     * Gets the value of the codeSystemValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCodeSystemValidFrom() {
        return codeSystemValidFrom;
    }

    /**
     * Sets the value of the codeSystemValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCodeSystemValidFrom(XMLGregorianCalendar value) {
        this.codeSystemValidFrom = value;
    }

    /**
     * Gets the value of the codeSystemDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeSystemDescription() {
        return codeSystemDescription;
    }

    /**
     * Sets the value of the codeSystemDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeSystemDescription(String value) {
        this.codeSystemDescription = value;
    }

    /**
     * Gets the value of the deltaStartTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDeltaStartTime() {
        return deltaStartTime;
    }

    /**
     * Sets the value of the deltaStartTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDeltaStartTime(XMLGregorianCalendar value) {
        this.deltaStartTime = value;
    }

    /**
     * Gets the value of the deltaEndTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDeltaEndTime() {
        return deltaEndTime;
    }

    /**
     * Sets the value of the deltaEndTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDeltaEndTime(XMLGregorianCalendar value) {
        this.deltaEndTime = value;
    }

}
