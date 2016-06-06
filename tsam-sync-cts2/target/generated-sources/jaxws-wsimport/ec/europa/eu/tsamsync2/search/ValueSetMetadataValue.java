
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for valueSetMetadataValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="valueSetMetadataValue">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemEntityVersion" type="{de.fhdo.termserver.types}codeSystemEntityVersion" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="metadataParameter" type="{de.fhdo.termserver.types}metadataParameter" minOccurs="0"/>
 *         &lt;element name="parameterValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valuesetVersionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "valueSetMetadataValue", namespace = "de.fhdo.termserver.types", propOrder = {
    "codeSystemEntityVersion",
    "id",
    "metadataParameter",
    "parameterValue",
    "valuesetVersionId"
})
public class ValueSetMetadataValue {

    protected CodeSystemEntityVersion codeSystemEntityVersion;
    protected Long id;
    protected MetadataParameter metadataParameter;
    protected String parameterValue;
    protected Long valuesetVersionId;

    /**
     * Gets the value of the codeSystemEntityVersion property.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemEntityVersion }
     *     
     */
    public CodeSystemEntityVersion getCodeSystemEntityVersion() {
        return codeSystemEntityVersion;
    }

    /**
     * Sets the value of the codeSystemEntityVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemEntityVersion }
     *     
     */
    public void setCodeSystemEntityVersion(CodeSystemEntityVersion value) {
        this.codeSystemEntityVersion = value;
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
     * Gets the value of the metadataParameter property.
     * 
     * @return
     *     possible object is
     *     {@link MetadataParameter }
     *     
     */
    public MetadataParameter getMetadataParameter() {
        return metadataParameter;
    }

    /**
     * Sets the value of the metadataParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadataParameter }
     *     
     */
    public void setMetadataParameter(MetadataParameter value) {
        this.metadataParameter = value;
    }

    /**
     * Gets the value of the parameterValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameterValue() {
        return parameterValue;
    }

    /**
     * Sets the value of the parameterValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameterValue(String value) {
        this.parameterValue = value;
    }

    /**
     * Gets the value of the valuesetVersionId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getValuesetVersionId() {
        return valuesetVersionId;
    }

    /**
     * Sets the value of the valuesetVersionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setValuesetVersionId(Long value) {
        this.valuesetVersionId = value;
    }

}
