
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sysParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sysParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="domainValueByModifyLevel" type="{de.fhdo.termserver.types}domainValue" minOccurs="0"/>
 *         &lt;element name="domainValueByValidityDomain" type="{de.fhdo.termserver.types}domainValue" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="javaDatatype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="objectId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sysParam", namespace = "de.fhdo.termserver.types", propOrder = {
    "description",
    "domainValueByModifyLevel",
    "domainValueByValidityDomain",
    "id",
    "javaDatatype",
    "name",
    "objectId",
    "value"
})
public class SysParam {

    protected String description;
    protected DomainValue domainValueByModifyLevel;
    protected DomainValue domainValueByValidityDomain;
    protected Long id;
    protected String javaDatatype;
    protected String name;
    protected Long objectId;
    protected String value;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the domainValueByModifyLevel property.
     * 
     * @return
     *     possible object is
     *     {@link DomainValue }
     *     
     */
    public DomainValue getDomainValueByModifyLevel() {
        return domainValueByModifyLevel;
    }

    /**
     * Sets the value of the domainValueByModifyLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainValue }
     *     
     */
    public void setDomainValueByModifyLevel(DomainValue value) {
        this.domainValueByModifyLevel = value;
    }

    /**
     * Gets the value of the domainValueByValidityDomain property.
     * 
     * @return
     *     possible object is
     *     {@link DomainValue }
     *     
     */
    public DomainValue getDomainValueByValidityDomain() {
        return domainValueByValidityDomain;
    }

    /**
     * Sets the value of the domainValueByValidityDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainValue }
     *     
     */
    public void setDomainValueByValidityDomain(DomainValue value) {
        this.domainValueByValidityDomain = value;
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
     * Gets the value of the javaDatatype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaDatatype() {
        return javaDatatype;
    }

    /**
     * Sets the value of the javaDatatype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaDatatype(String value) {
        this.javaDatatype = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the objectId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * Sets the value of the objectId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setObjectId(Long value) {
        this.objectId = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

}
