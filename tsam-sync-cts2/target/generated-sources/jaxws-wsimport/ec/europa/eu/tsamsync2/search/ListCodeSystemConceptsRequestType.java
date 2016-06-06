
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listCodeSystemConceptsRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listCodeSystemConceptsRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loginToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codeSystem" type="{de.fhdo.termserver.types}codeSystem"/>
 *         &lt;element name="codeSystemEntity" type="{de.fhdo.termserver.types}codeSystemEntity" minOccurs="0"/>
 *         &lt;element name="searchParameter" type="{http://search.ws.terminologie.fhdo.de/}searchType" minOccurs="0"/>
 *         &lt;element name="pagingParameter" type="{http://search.ws.terminologie.fhdo.de/}pagingType" minOccurs="0"/>
 *         &lt;element name="lookForward" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="sortingParameter" type="{http://search.ws.terminologie.fhdo.de/}sortingType" minOccurs="0"/>
 *         &lt;element name="loadMetadata" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="loadTranslation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="hierarchicalOutput" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="upperLevels" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listCodeSystemConceptsRequestType", propOrder = {
    "loginToken",
    "codeSystem",
    "codeSystemEntity",
    "searchParameter",
    "pagingParameter",
    "lookForward",
    "sortingParameter",
    "loadMetadata",
    "loadTranslation",
    "hierarchicalOutput",
    "upperLevels"
})
public class ListCodeSystemConceptsRequestType {

    protected String loginToken;
    @XmlElement(required = true)
    protected CodeSystem codeSystem;
    protected CodeSystemEntity codeSystemEntity;
    protected SearchType searchParameter;
    protected PagingType pagingParameter;
    protected boolean lookForward;
    protected SortingType sortingParameter;
    protected Boolean loadMetadata;
    protected Boolean loadTranslation;
    protected Boolean hierarchicalOutput;
    protected Integer upperLevels;

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
     * Gets the value of the codeSystem property.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystem }
     *     
     */
    public CodeSystem getCodeSystem() {
        return codeSystem;
    }

    /**
     * Sets the value of the codeSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystem }
     *     
     */
    public void setCodeSystem(CodeSystem value) {
        this.codeSystem = value;
    }

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
     * Gets the value of the searchParameter property.
     * 
     * @return
     *     possible object is
     *     {@link SearchType }
     *     
     */
    public SearchType getSearchParameter() {
        return searchParameter;
    }

    /**
     * Sets the value of the searchParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchType }
     *     
     */
    public void setSearchParameter(SearchType value) {
        this.searchParameter = value;
    }

    /**
     * Gets the value of the pagingParameter property.
     * 
     * @return
     *     possible object is
     *     {@link PagingType }
     *     
     */
    public PagingType getPagingParameter() {
        return pagingParameter;
    }

    /**
     * Sets the value of the pagingParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link PagingType }
     *     
     */
    public void setPagingParameter(PagingType value) {
        this.pagingParameter = value;
    }

    /**
     * Gets the value of the lookForward property.
     * 
     */
    public boolean isLookForward() {
        return lookForward;
    }

    /**
     * Sets the value of the lookForward property.
     * 
     */
    public void setLookForward(boolean value) {
        this.lookForward = value;
    }

    /**
     * Gets the value of the sortingParameter property.
     * 
     * @return
     *     possible object is
     *     {@link SortingType }
     *     
     */
    public SortingType getSortingParameter() {
        return sortingParameter;
    }

    /**
     * Sets the value of the sortingParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link SortingType }
     *     
     */
    public void setSortingParameter(SortingType value) {
        this.sortingParameter = value;
    }

    /**
     * Gets the value of the loadMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLoadMetadata() {
        return loadMetadata;
    }

    /**
     * Sets the value of the loadMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLoadMetadata(Boolean value) {
        this.loadMetadata = value;
    }

    /**
     * Gets the value of the loadTranslation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLoadTranslation() {
        return loadTranslation;
    }

    /**
     * Sets the value of the loadTranslation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLoadTranslation(Boolean value) {
        this.loadTranslation = value;
    }

    /**
     * Gets the value of the hierarchicalOutput property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHierarchicalOutput() {
        return hierarchicalOutput;
    }

    /**
     * Sets the value of the hierarchicalOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHierarchicalOutput(Boolean value) {
        this.hierarchicalOutput = value;
    }

    /**
     * Gets the value of the upperLevels property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUpperLevels() {
        return upperLevels;
    }

    /**
     * Sets the value of the upperLevels property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUpperLevels(Integer value) {
        this.upperLevels = value;
    }

}
