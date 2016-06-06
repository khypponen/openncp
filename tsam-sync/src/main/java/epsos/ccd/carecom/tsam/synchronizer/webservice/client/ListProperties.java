
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
 * <p>Java class for listProperties complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listProperties">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemId" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codeSystemOid" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="componentId" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="conceptExternalCode" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="propertyTypeId" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="propertyTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="propertyReferenceId" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="propertyLanguageCode" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="propertyValidFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="propertyStatus" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "listProperties", propOrder = {
    "codeSystemId",
    "codeSystemOid",
    "componentId",
    "conceptExternalCode",
    "propertyTypeId",
    "propertyTypeCode",
    "propertyReferenceId",
    "propertyLanguageCode",
    "propertyValidFrom",
    "propertyStatus",
    "deltaStartTime",
    "deltaEndTime"
})
public class ListProperties {

    @XmlElement(type = Long.class)
    protected List<Long> codeSystemId;
    protected List<String> codeSystemOid;
    @XmlElement(type = Long.class)
    protected List<Long> componentId;
    protected List<String> conceptExternalCode;
    @XmlElement(type = Integer.class)
    protected List<Integer> propertyTypeId;
    protected List<String> propertyTypeCode;
    @XmlElement(type = Long.class)
    protected List<Long> propertyReferenceId;
    protected List<String> propertyLanguageCode;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar propertyValidFrom;
    @XmlElement(type = Integer.class)
    protected List<Integer> propertyStatus;
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
     * Gets the value of the componentId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the componentId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponentId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getComponentId() {
        if (componentId == null) {
            componentId = new ArrayList<Long>();
        }
        return this.componentId;
    }

    /**
     * Gets the value of the conceptExternalCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the conceptExternalCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConceptExternalCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getConceptExternalCode() {
        if (conceptExternalCode == null) {
            conceptExternalCode = new ArrayList<String>();
        }
        return this.conceptExternalCode;
    }

    /**
     * Gets the value of the propertyTypeId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyTypeId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyTypeId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getPropertyTypeId() {
        if (propertyTypeId == null) {
            propertyTypeId = new ArrayList<Integer>();
        }
        return this.propertyTypeId;
    }

    /**
     * Gets the value of the propertyTypeCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyTypeCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyTypeCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPropertyTypeCode() {
        if (propertyTypeCode == null) {
            propertyTypeCode = new ArrayList<String>();
        }
        return this.propertyTypeCode;
    }

    /**
     * Gets the value of the propertyReferenceId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyReferenceId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyReferenceId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getPropertyReferenceId() {
        if (propertyReferenceId == null) {
            propertyReferenceId = new ArrayList<Long>();
        }
        return this.propertyReferenceId;
    }

    /**
     * Gets the value of the propertyLanguageCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyLanguageCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyLanguageCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPropertyLanguageCode() {
        if (propertyLanguageCode == null) {
            propertyLanguageCode = new ArrayList<String>();
        }
        return this.propertyLanguageCode;
    }

    /**
     * Gets the value of the propertyValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPropertyValidFrom() {
        return propertyValidFrom;
    }

    /**
     * Sets the value of the propertyValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPropertyValidFrom(XMLGregorianCalendar value) {
        this.propertyValidFrom = value;
    }

    /**
     * Gets the value of the propertyStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getPropertyStatus() {
        if (propertyStatus == null) {
            propertyStatus = new ArrayList<Integer>();
        }
        return this.propertyStatus;
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
