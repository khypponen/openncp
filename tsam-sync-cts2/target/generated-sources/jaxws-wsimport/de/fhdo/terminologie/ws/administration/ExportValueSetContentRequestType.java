
package de.fhdo.terminologie.ws.administration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import types.termserver.fhdo.de.ValueSet;


/**
 * <p>Java class for exportValueSetContentRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="exportValueSetContentRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="exportInfos" type="{http://administration.ws.terminologie.fhdo.de/}exportType" minOccurs="0"/>
 *         &lt;element name="exportParameter" type="{http://administration.ws.terminologie.fhdo.de/}exportParameterType" minOccurs="0"/>
 *         &lt;element name="loginToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valueSet" type="{de.fhdo.termserver.types}valueSet" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exportValueSetContentRequestType", propOrder = {
    "exportInfos",
    "exportParameter",
    "loginToken",
    "valueSet"
})
public class ExportValueSetContentRequestType {

    protected ExportType exportInfos;
    protected ExportParameterType exportParameter;
    protected String loginToken;
    protected ValueSet valueSet;

    /**
     * Gets the value of the exportInfos property.
     * 
     * @return
     *     possible object is
     *     {@link ExportType }
     *     
     */
    public ExportType getExportInfos() {
        return exportInfos;
    }

    /**
     * Sets the value of the exportInfos property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExportType }
     *     
     */
    public void setExportInfos(ExportType value) {
        this.exportInfos = value;
    }

    /**
     * Gets the value of the exportParameter property.
     * 
     * @return
     *     possible object is
     *     {@link ExportParameterType }
     *     
     */
    public ExportParameterType getExportParameter() {
        return exportParameter;
    }

    /**
     * Sets the value of the exportParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExportParameterType }
     *     
     */
    public void setExportParameter(ExportParameterType value) {
        this.exportParameter = value;
    }

    /**
     * Gets the value of the loginToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginToken() {
        return loginToken;
    }

    /**
     * Sets the value of the loginToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginToken(String value) {
        this.loginToken = value;
    }

    /**
     * Gets the value of the valueSet property.
     * 
     * @return
     *     possible object is
     *     {@link ValueSet }
     *     
     */
    public ValueSet getValueSet() {
        return valueSet;
    }

    /**
     * Sets the value of the valueSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueSet }
     *     
     */
    public void setValueSet(ValueSet value) {
        this.valueSet = value;
    }

}
