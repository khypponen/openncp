
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listGloballySearchedConceptsRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listGloballySearchedConceptsRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loginToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codeSystemConcept" type="{de.fhdo.termserver.types}codeSystemConcept" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listGloballySearchedConceptsRequestType", propOrder = {
    "loginToken",
    "codeSystemConcept"
})
public class ListGloballySearchedConceptsRequestType {

    protected String loginToken;
    protected CodeSystemConcept codeSystemConcept;

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
     * Gets the value of the codeSystemConcept property.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemConcept }
     *     
     */
    public CodeSystemConcept getCodeSystemConcept() {
        return codeSystemConcept;
    }

    /**
     * Sets the value of the codeSystemConcept property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemConcept }
     *     
     */
    public void setCodeSystemConcept(CodeSystemConcept value) {
        this.codeSystemConcept = value;
    }

}
