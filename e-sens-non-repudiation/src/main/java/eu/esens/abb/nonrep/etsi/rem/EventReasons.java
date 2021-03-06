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
 * <p>Java class for EventReasonsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventReasonsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02640/v1#}EventReason" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventReasonsType", namespace = "http://uri.etsi.org/02640/v1#", propOrder = {
    "eventReasons"
})
@XmlRootElement(name = "EventReasons", namespace = "http://uri.etsi.org/02640/v1#")
public class EventReasons {

    @XmlElement(name = "EventReason", required = true)
    protected List<EventReason> eventReasons;

    /**
     * Gets the value of the eventReasons property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventReasons property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEventReasons().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventReason }
     * 
     * 
     */
    public List<EventReason> getEventReasons() {
        if (eventReasons == null) {
            eventReasons = new ArrayList<EventReason>();
        }
        return this.eventReasons;
    }

}
