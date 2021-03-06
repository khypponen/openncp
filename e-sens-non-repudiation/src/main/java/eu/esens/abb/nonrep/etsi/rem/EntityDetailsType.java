//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.29 at 12:47:48 PM CET 
//


package eu.esens.abb.nonrep.etsi.rem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EntityDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02640/v1#}NamesPostalAddresses" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ElectronicAddress" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v1#}CertificateDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityDetailsType", namespace = "http://uri.etsi.org/02640/v1#", propOrder = {
    "namesPostalAddresses",
    "electronicAddress",
    "certificateDetails"
})
public class EntityDetailsType {

    @XmlElement(name = "NamesPostalAddresses")
    protected NamesPostalAddresses namesPostalAddresses;
    @XmlElement(name = "ElectronicAddress", namespace = "http://uri.etsi.org/02231/v2#")
    protected ElectronicAddress electronicAddress;
    @XmlElement(name = "CertificateDetails")
    protected CertificateDetails certificateDetails;

    /**
     * Gets the value of the namesPostalAddresses property.
     * 
     * @return
     *     possible object is
     *     {@link NamesPostalAddresses }
     *     
     */
    public NamesPostalAddresses getNamesPostalAddresses() {
        return namesPostalAddresses;
    }

    /**
     * Sets the value of the namesPostalAddresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesPostalAddresses }
     *     
     */
    public void setNamesPostalAddresses(NamesPostalAddresses value) {
        this.namesPostalAddresses = value;
    }

    /**
     * Gets the value of the electronicAddress property.
     * 
     * @return
     *     possible object is
     *     {@link ElectronicAddress }
     *     
     */
    public ElectronicAddress getElectronicAddress() {
        return electronicAddress;
    }

    /**
     * Sets the value of the electronicAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElectronicAddress }
     *     
     */
    public void setElectronicAddress(ElectronicAddress value) {
        this.electronicAddress = value;
    }

    /**
     * Gets the value of the certificateDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CertificateDetails }
     *     
     */
    public CertificateDetails getCertificateDetails() {
        return certificateDetails;
    }

    /**
     * Sets the value of the certificateDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificateDetails }
     *     
     */
    public void setCertificateDetails(CertificateDetails value) {
        this.certificateDetails = value;
    }

}
