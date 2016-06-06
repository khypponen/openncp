
package de.fhdo.terminologie.ws.authoring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import types.termserver.fhdo.de.CodeSystem;


/**
 * <p>Java class for maintainCodeSystemVersionRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="maintainCodeSystemVersionRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="assignTaxonomy" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="codeSystem" type="{de.fhdo.termserver.types}codeSystem" minOccurs="0"/>
 *         &lt;element name="loginToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="versioning" type="{http://authoring.ws.terminologie.fhdo.de/}versioningType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "maintainCodeSystemVersionRequestType", propOrder = {
    "assignTaxonomy",
    "codeSystem",
    "loginToken",
    "versioning"
})
public class MaintainCodeSystemVersionRequestType {

    protected Boolean assignTaxonomy;
    protected CodeSystem codeSystem;
    protected String loginToken;
    protected VersioningType versioning;

    /**
     * Gets the value of the assignTaxonomy property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAssignTaxonomy() {
        return assignTaxonomy;
    }

    /**
     * Sets the value of the assignTaxonomy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAssignTaxonomy(Boolean value) {
        this.assignTaxonomy = value;
    }

    /**
     * Gets the value of the codeSystem property.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystem }
     *     
     */
    public CodeSystem getCodeSystem() {
        return codeSystem;
    }

    /**
     * Sets the value of the codeSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystem }
     *     
     */
    public void setCodeSystem(CodeSystem value) {
        this.codeSystem = value;
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
     * Gets the value of the versioning property.
     * 
     * @return
     *     possible object is
     *     {@link VersioningType }
     *     
     */
    public VersioningType getVersioning() {
        return versioning;
    }

    /**
     * Sets the value of the versioning property.
     * 
     * @param value
     *     allowed object is
     *     {@link VersioningType }
     *     
     */
    public void setVersioning(VersioningType value) {
        this.versioning = value;
    }

}
