
package epsos.ccd.carecom.tsam.synchronizer.webservice.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the epsos.ccd.carecom.tsam.synchronizer.webservice.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ListPropertiesResponse_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listPropertiesResponse");
    private final static QName _CodeSystem_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "codeSystem");
    private final static QName _ListValueSets_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listValueSets");
    private final static QName _ListCodeSystems_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listCodeSystems");
    private final static QName _Transcoding_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "transcoding");
    private final static QName _ListTranscodings_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listTranscodings");
    private final static QName _ListTranscodingsResponse_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listTranscodingsResponse");
    private final static QName _HealthTermWebServiceException_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "HealthTermWebServiceException");
    private final static QName _ListValueSetsResponse_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listValueSetsResponse");
    private final static QName _ListCodeSystemsResponse_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listCodeSystemsResponse");
    private final static QName _Designation_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "designation");
    private final static QName _ListCodeSystemConcepts_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listCodeSystemConcepts");
    private final static QName _ValueSet_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "valueSet");
    private final static QName _Property_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "property");
    private final static QName _CodeSystemConcept_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "codeSystemConcept");
    private final static QName _ListProperties_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listProperties");
    private final static QName _ListDesignationsResponse_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listDesignationsResponse");
    private final static QName _ListCodeSystemConceptsResponse_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listCodeSystemConceptsResponse");
    private final static QName _ListDesignations_QNAME = new QName("http://cts2.webservice.ht.carecom.dk/", "listDesignations");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: epsos.ccd.carecom.tsam.synchronizer.webservice.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Designation }
     * 
     */
    public Designation createDesignation() {
        return new Designation();
    }

    /**
     * Create an instance of {@link ListCodeSystemConceptsResponse }
     * 
     */
    public ListCodeSystemConceptsResponse createListCodeSystemConceptsResponse() {
        return new ListCodeSystemConceptsResponse();
    }

    /**
     * Create an instance of {@link ListTranscodings }
     * 
     */
    public ListTranscodings createListTranscodings() {
        return new ListTranscodings();
    }

    /**
     * Create an instance of {@link ListPropertiesResponse }
     * 
     */
    public ListPropertiesResponse createListPropertiesResponse() {
        return new ListPropertiesResponse();
    }

    /**
     * Create an instance of {@link CodeSystem }
     * 
     */
    public CodeSystem createCodeSystem() {
        return new CodeSystem();
    }

    /**
     * Create an instance of {@link ListDesignations }
     * 
     */
    public ListDesignations createListDesignations() {
        return new ListDesignations();
    }

    /**
     * Create an instance of {@link ListValueSets }
     * 
     */
    public ListValueSets createListValueSets() {
        return new ListValueSets();
    }

    /**
     * Create an instance of {@link HealthTermWebServiceException }
     * 
     */
    public HealthTermWebServiceException createHealthTermWebServiceException() {
        return new HealthTermWebServiceException();
    }

    /**
     * Create an instance of {@link ListCodeSystemConcepts }
     * 
     */
    public ListCodeSystemConcepts createListCodeSystemConcepts() {
        return new ListCodeSystemConcepts();
    }

    /**
     * Create an instance of {@link ListTranscodingsResponse }
     * 
     */
    public ListTranscodingsResponse createListTranscodingsResponse() {
        return new ListTranscodingsResponse();
    }

    /**
     * Create an instance of {@link ListCodeSystemsResponse }
     * 
     */
    public ListCodeSystemsResponse createListCodeSystemsResponse() {
        return new ListCodeSystemsResponse();
    }

    /**
     * Create an instance of {@link ListProperties }
     * 
     */
    public ListProperties createListProperties() {
        return new ListProperties();
    }

    /**
     * Create an instance of {@link CodeSystemConcept }
     * 
     */
    public CodeSystemConcept createCodeSystemConcept() {
        return new CodeSystemConcept();
    }

    /**
     * Create an instance of {@link ListValueSetsResponse }
     * 
     */
    public ListValueSetsResponse createListValueSetsResponse() {
        return new ListValueSetsResponse();
    }

    /**
     * Create an instance of {@link ListDesignationsResponse }
     * 
     */
    public ListDesignationsResponse createListDesignationsResponse() {
        return new ListDesignationsResponse();
    }

    /**
     * Create an instance of {@link ListCodeSystems }
     * 
     */
    public ListCodeSystems createListCodeSystems() {
        return new ListCodeSystems();
    }

    /**
     * Create an instance of {@link ValueSet }
     * 
     */
    public ValueSet createValueSet() {
        return new ValueSet();
    }

    /**
     * Create an instance of {@link Transcoding }
     * 
     */
    public Transcoding createTranscoding() {
        return new Transcoding();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListPropertiesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listPropertiesResponse")
    public JAXBElement<ListPropertiesResponse> createListPropertiesResponse(ListPropertiesResponse value) {
        return new JAXBElement<ListPropertiesResponse>(_ListPropertiesResponse_QNAME, ListPropertiesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "codeSystem")
    public JAXBElement<CodeSystem> createCodeSystem(CodeSystem value) {
        return new JAXBElement<CodeSystem>(_CodeSystem_QNAME, CodeSystem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListValueSets }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listValueSets")
    public JAXBElement<ListValueSets> createListValueSets(ListValueSets value) {
        return new JAXBElement<ListValueSets>(_ListValueSets_QNAME, ListValueSets.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListCodeSystems }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listCodeSystems")
    public JAXBElement<ListCodeSystems> createListCodeSystems(ListCodeSystems value) {
        return new JAXBElement<ListCodeSystems>(_ListCodeSystems_QNAME, ListCodeSystems.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Transcoding }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "transcoding")
    public JAXBElement<Transcoding> createTranscoding(Transcoding value) {
        return new JAXBElement<Transcoding>(_Transcoding_QNAME, Transcoding.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListTranscodings }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listTranscodings")
    public JAXBElement<ListTranscodings> createListTranscodings(ListTranscodings value) {
        return new JAXBElement<ListTranscodings>(_ListTranscodings_QNAME, ListTranscodings.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListTranscodingsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listTranscodingsResponse")
    public JAXBElement<ListTranscodingsResponse> createListTranscodingsResponse(ListTranscodingsResponse value) {
        return new JAXBElement<ListTranscodingsResponse>(_ListTranscodingsResponse_QNAME, ListTranscodingsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HealthTermWebServiceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "HealthTermWebServiceException")
    public JAXBElement<HealthTermWebServiceException> createHealthTermWebServiceException(HealthTermWebServiceException value) {
        return new JAXBElement<HealthTermWebServiceException>(_HealthTermWebServiceException_QNAME, HealthTermWebServiceException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListValueSetsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listValueSetsResponse")
    public JAXBElement<ListValueSetsResponse> createListValueSetsResponse(ListValueSetsResponse value) {
        return new JAXBElement<ListValueSetsResponse>(_ListValueSetsResponse_QNAME, ListValueSetsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListCodeSystemsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listCodeSystemsResponse")
    public JAXBElement<ListCodeSystemsResponse> createListCodeSystemsResponse(ListCodeSystemsResponse value) {
        return new JAXBElement<ListCodeSystemsResponse>(_ListCodeSystemsResponse_QNAME, ListCodeSystemsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Designation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "designation")
    public JAXBElement<Designation> createDesignation(Designation value) {
        return new JAXBElement<Designation>(_Designation_QNAME, Designation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListCodeSystemConcepts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listCodeSystemConcepts")
    public JAXBElement<ListCodeSystemConcepts> createListCodeSystemConcepts(ListCodeSystemConcepts value) {
        return new JAXBElement<ListCodeSystemConcepts>(_ListCodeSystemConcepts_QNAME, ListCodeSystemConcepts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "valueSet")
    public JAXBElement<ValueSet> createValueSet(ValueSet value) {
        return new JAXBElement<ValueSet>(_ValueSet_QNAME, ValueSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Property }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "property")
    public JAXBElement<Property> createProperty(Property value) {
        return new JAXBElement<Property>(_Property_QNAME, Property.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemConcept }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "codeSystemConcept")
    public JAXBElement<CodeSystemConcept> createCodeSystemConcept(CodeSystemConcept value) {
        return new JAXBElement<CodeSystemConcept>(_CodeSystemConcept_QNAME, CodeSystemConcept.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListProperties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listProperties")
    public JAXBElement<ListProperties> createListProperties(ListProperties value) {
        return new JAXBElement<ListProperties>(_ListProperties_QNAME, ListProperties.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListDesignationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listDesignationsResponse")
    public JAXBElement<ListDesignationsResponse> createListDesignationsResponse(ListDesignationsResponse value) {
        return new JAXBElement<ListDesignationsResponse>(_ListDesignationsResponse_QNAME, ListDesignationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListCodeSystemConceptsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listCodeSystemConceptsResponse")
    public JAXBElement<ListCodeSystemConceptsResponse> createListCodeSystemConceptsResponse(ListCodeSystemConceptsResponse value) {
        return new JAXBElement<ListCodeSystemConceptsResponse>(_ListCodeSystemConceptsResponse_QNAME, ListCodeSystemConceptsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListDesignations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cts2.webservice.ht.carecom.dk/", name = "listDesignations")
    public JAXBElement<ListDesignations> createListDesignations(ListDesignations value) {
        return new JAXBElement<ListDesignations>(_ListDesignations_QNAME, ListDesignations.class, null, value);
    }

}
