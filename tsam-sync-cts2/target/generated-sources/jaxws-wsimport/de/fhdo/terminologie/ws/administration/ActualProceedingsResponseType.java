
package de.fhdo.terminologie.ws.administration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for actualProceedingsResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="actualProceedingsResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actualProceedings" type="{http://administration.ws.terminologie.fhdo.de/}actualProceeding" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="returnInfos" type="{http://administration.ws.terminologie.fhdo.de/}returnType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actualProceedingsResponseType", propOrder = {
    "actualProceedings",
    "returnInfos"
})
public class ActualProceedingsResponseType {

    @XmlElement(nillable = true)
    protected List<ActualProceeding> actualProceedings;
    protected ReturnType returnInfos;

    /**
     * Gets the value of the actualProceedings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actualProceedings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActualProceedings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActualProceeding }
     * 
     * 
     */
    public List<ActualProceeding> getActualProceedings() {
        if (actualProceedings == null) {
            actualProceedings = new ArrayList<ActualProceeding>();
        }
        return this.actualProceedings;
    }

    /**
     * Gets the value of the returnInfos property.
     * 
     * @return
     *     possible object is
     *     {@link ReturnType }
     *     
     */
    public ReturnType getReturnInfos() {
        return returnInfos;
    }

    /**
     * Sets the value of the returnInfos property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnType }
     *     
     */
    public void setReturnInfos(ReturnType value) {
        this.returnInfos = value;
    }

}
