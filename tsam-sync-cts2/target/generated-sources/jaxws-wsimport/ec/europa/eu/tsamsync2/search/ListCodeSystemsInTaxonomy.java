
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListCodeSystemsInTaxonomy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListCodeSystemsInTaxonomy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameter" type="{http://search.ws.terminologie.fhdo.de/}listCodeSystemsInTaxonomyRequestType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListCodeSystemsInTaxonomy", propOrder = {
    "parameter"
})
public class ListCodeSystemsInTaxonomy {

    protected ListCodeSystemsInTaxonomyRequestType parameter;

    /**
     * Gets the value of the parameter property.
     * 
     * @return
     *     possible object is
     *     {@link ListCodeSystemsInTaxonomyRequestType }
     *     
     */
    public ListCodeSystemsInTaxonomyRequestType getParameter() {
        return parameter;
    }

    /**
     * Sets the value of the parameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListCodeSystemsInTaxonomyRequestType }
     *     
     */
    public void setParameter(ListCodeSystemsInTaxonomyRequestType value) {
        this.parameter = value;
    }

}
