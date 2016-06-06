
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
 * <p>Java class for listTranscodings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listTranscodings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transcodingSourceConceptId" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="transcodingSourceExternalCode" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="transcodingTargetConceptId" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="transcodingTargetExternalCode" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="transcodingValidFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="transcodingStatus" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "listTranscodings", propOrder = {
    "transcodingSourceConceptId",
    "transcodingSourceExternalCode",
    "transcodingTargetConceptId",
    "transcodingTargetExternalCode",
    "transcodingValidFrom",
    "transcodingStatus",
    "deltaStartTime",
    "deltaEndTime"
})
public class ListTranscodings {

    @XmlElement(type = Long.class)
    protected List<Long> transcodingSourceConceptId;
    protected List<String> transcodingSourceExternalCode;
    @XmlElement(type = Long.class)
    protected List<Long> transcodingTargetConceptId;
    protected List<String> transcodingTargetExternalCode;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transcodingValidFrom;
    @XmlElement(type = Integer.class)
    protected List<Integer> transcodingStatus;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deltaStartTime;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deltaEndTime;

    /**
     * Gets the value of the transcodingSourceConceptId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transcodingSourceConceptId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTranscodingSourceConceptId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getTranscodingSourceConceptId() {
        if (transcodingSourceConceptId == null) {
            transcodingSourceConceptId = new ArrayList<Long>();
        }
        return this.transcodingSourceConceptId;
    }

    /**
     * Gets the value of the transcodingSourceExternalCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transcodingSourceExternalCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTranscodingSourceExternalCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTranscodingSourceExternalCode() {
        if (transcodingSourceExternalCode == null) {
            transcodingSourceExternalCode = new ArrayList<String>();
        }
        return this.transcodingSourceExternalCode;
    }

    /**
     * Gets the value of the transcodingTargetConceptId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transcodingTargetConceptId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTranscodingTargetConceptId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getTranscodingTargetConceptId() {
        if (transcodingTargetConceptId == null) {
            transcodingTargetConceptId = new ArrayList<Long>();
        }
        return this.transcodingTargetConceptId;
    }

    /**
     * Gets the value of the transcodingTargetExternalCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transcodingTargetExternalCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTranscodingTargetExternalCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTranscodingTargetExternalCode() {
        if (transcodingTargetExternalCode == null) {
            transcodingTargetExternalCode = new ArrayList<String>();
        }
        return this.transcodingTargetExternalCode;
    }

    /**
     * Gets the value of the transcodingValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTranscodingValidFrom() {
        return transcodingValidFrom;
    }

    /**
     * Sets the value of the transcodingValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTranscodingValidFrom(XMLGregorianCalendar value) {
        this.transcodingValidFrom = value;
    }

    /**
     * Gets the value of the transcodingStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transcodingStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTranscodingStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getTranscodingStatus() {
        if (transcodingStatus == null) {
            transcodingStatus = new ArrayList<Integer>();
        }
        return this.transcodingStatus;
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
