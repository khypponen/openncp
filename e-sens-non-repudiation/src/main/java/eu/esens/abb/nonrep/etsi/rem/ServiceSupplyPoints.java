//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.29 at 12:47:48 PM CET 
//


package eu.esens.abb.nonrep.etsi.rem;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceSupplyPointsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceSupplyPointsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="ServiceSupplyPoint" type="{http://uri.etsi.org/02231/v2#}NonEmptyURIType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceSupplyPointsType", namespace = "http://uri.etsi.org/02231/v2#", propOrder = {
    "serviceSupplyPoints"
})
@XmlRootElement(name = "ServiceSupplyPoints", namespace = "http://uri.etsi.org/02231/v2#")
public class ServiceSupplyPoints {

    @XmlElement(name = "ServiceSupplyPoint", required = true)
    protected List<String> serviceSupplyPoints;

    /**
     * Gets the value of the serviceSupplyPoints property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceSupplyPoints property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceSupplyPoints().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getServiceSupplyPoints() {
        if (serviceSupplyPoints == null) {
            serviceSupplyPoints = new ArrayList<String>();
        }
        return this.serviceSupplyPoints;
    }

}
