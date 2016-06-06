
package de.fhdo.terminologie.ws.administration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for exportParameterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="exportParameterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="associationInfos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codeSystemInfos" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dateFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="translations" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exportParameterType", propOrder = {
    "associationInfos",
    "codeSystemInfos",
    "dateFrom",
    "translations"
})
public class ExportParameterType {

    protected String associationInfos;
    protected boolean codeSystemInfos;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateFrom;
    protected boolean translations;

    /**
     * Gets the value of the associationInfos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssociationInfos() {
        return associationInfos;
    }

    /**
     * Sets the value of the associationInfos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssociationInfos(String value) {
        this.associationInfos = value;
    }

    /**
     * Gets the value of the codeSystemInfos property.
     * 
     */
    public boolean isCodeSystemInfos() {
        return codeSystemInfos;
    }

    /**
     * Sets the value of the codeSystemInfos property.
     * 
     */
    public void setCodeSystemInfos(boolean value) {
        this.codeSystemInfos = value;
    }

    /**
     * Gets the value of the dateFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateFrom() {
        return dateFrom;
    }

    /**
     * Sets the value of the dateFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateFrom(XMLGregorianCalendar value) {
        this.dateFrom = value;
    }

    /**
     * Gets the value of the translations property.
     * 
     */
    public boolean isTranslations() {
        return translations;
    }

    /**
     * Sets the value of the translations property.
     * 
     */
    public void setTranslations(boolean value) {
        this.translations = value;
    }

}
