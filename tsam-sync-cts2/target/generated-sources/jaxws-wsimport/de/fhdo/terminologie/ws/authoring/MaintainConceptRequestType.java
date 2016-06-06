
package de.fhdo.terminologie.ws.authoring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import types.termserver.fhdo.de.CodeSystemEntity;


/**
 * <p>Java class for maintainConceptRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="maintainConceptRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemEntity" type="{de.fhdo.termserver.types}codeSystemEntity" minOccurs="0"/>
 *         &lt;element name="codeSystemVersionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
@XmlType(name = "maintainConceptRequestType", propOrder = {
    "codeSystemEntity",
    "codeSystemVersionId",
    "loginToken",
    "versioning"
})
public class MaintainConceptRequestType {

    protected CodeSystemEntity codeSystemEntity;
    protected Long codeSystemVersionId;
    protected String loginToken;
    protected VersioningType versioning;

    /**
     * Gets the value of the codeSystemEntity property.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemEntity }
     *     
     */
    public CodeSystemEntity getCodeSystemEntity() {
        return codeSystemEntity;
    }

    /**
     * Sets the value of the codeSystemEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemEntity }
     *     
     */
    public void setCodeSystemEntity(CodeSystemEntity value) {
        this.codeSystemEntity = value;
    }

    /**
     * Gets the value of the codeSystemVersionId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodeSystemVersionId() {
        return codeSystemVersionId;
    }

    /**
     * Sets the value of the codeSystemVersionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodeSystemVersionId(Long value) {
        this.codeSystemVersionId = value;
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
