
package de.fhdo.terminologie.ws.authoring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for removeTerminologyOrConceptRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="removeTerminologyOrConceptRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deleteInfo" type="{http://authoring.ws.terminologie.fhdo.de/}deleteInfo" minOccurs="0"/>
 *         &lt;element name="loginToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeTerminologyOrConceptRequestType", propOrder = {
    "deleteInfo",
    "loginToken"
})
public class RemoveTerminologyOrConceptRequestType {

    protected DeleteInfo deleteInfo;
    protected String loginToken;

    /**
     * Gets the value of the deleteInfo property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteInfo }
     *     
     */
    public DeleteInfo getDeleteInfo() {
        return deleteInfo;
    }

    /**
     * Sets the value of the deleteInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteInfo }
     *     
     */
    public void setDeleteInfo(DeleteInfo value) {
        this.deleteInfo = value;
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

}
