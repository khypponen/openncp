
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
 * <p>Java class for listValueSets complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listValueSets">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemId" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codeSystemOid" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="valueSetId" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="valueSetOid" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="valueSetName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valueSetValidFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="valueSetStatus" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "listValueSets", propOrder = {
    "codeSystemId",
    "codeSystemOid",
    "valueSetId",
    "valueSetOid",
    "valueSetName",
    "valueSetValidFrom",
    "valueSetStatus",
    "deltaStartTime",
    "deltaEndTime"
})
public class ListValueSets {

    @XmlElement(type = Long.class)
    protected List<Long> codeSystemId;
    protected List<String> codeSystemOid;
    @XmlElement(type = Long.class)
    protected List<Long> valueSetId;
    protected List<String> valueSetOid;
    protected String valueSetName;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar valueSetValidFrom;
    @XmlElement(type = Integer.class)
    protected List<Integer> valueSetStatus;
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
     * Gets the value of the valueSetId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueSetId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueSetId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getValueSetId() {
        if (valueSetId == null) {
            valueSetId = new ArrayList<Long>();
        }
        return this.valueSetId;
    }

    /**
     * Gets the value of the valueSetOid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueSetOid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueSetOid().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getValueSetOid() {
        if (valueSetOid == null) {
            valueSetOid = new ArrayList<String>();
        }
        return this.valueSetOid;
    }

    /**
     * Gets the value of the valueSetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueSetName() {
        return valueSetName;
    }

    /**
     * Sets the value of the valueSetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueSetName(String value) {
        this.valueSetName = value;
    }

    /**
     * Gets the value of the valueSetValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValueSetValidFrom() {
        return valueSetValidFrom;
    }

    /**
     * Sets the value of the valueSetValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValueSetValidFrom(XMLGregorianCalendar value) {
        this.valueSetValidFrom = value;
    }

    /**
     * Gets the value of the valueSetStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueSetStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueSetStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getValueSetStatus() {
        if (valueSetStatus == null) {
            valueSetStatus = new ArrayList<Integer>();
        }
        return this.valueSetStatus;
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
