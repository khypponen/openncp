
package de.fhdo.terminologie.ws.administration;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import types.termserver.fhdo.de.AssociationType;
import types.termserver.fhdo.de.CodeSystem;
import types.termserver.fhdo.de.CodeSystemConcept;
import types.termserver.fhdo.de.CodeSystemConceptTranslation;
import types.termserver.fhdo.de.CodeSystemEntity;
import types.termserver.fhdo.de.CodeSystemEntityVersion;
import types.termserver.fhdo.de.CodeSystemEntityVersionAssociation;
import types.termserver.fhdo.de.CodeSystemMetadataValue;
import types.termserver.fhdo.de.CodeSystemVersion;
import types.termserver.fhdo.de.CodeSystemVersionEntityMembership;
import types.termserver.fhdo.de.CodeSystemVersionEntityMembershipId;
import types.termserver.fhdo.de.ConceptValueSetMembership;
import types.termserver.fhdo.de.ConceptValueSetMembershipId;
import types.termserver.fhdo.de.Domain;
import types.termserver.fhdo.de.DomainValue;
import types.termserver.fhdo.de.LicenceType;
import types.termserver.fhdo.de.LicencedUser;
import types.termserver.fhdo.de.LicencedUserId;
import types.termserver.fhdo.de.MetadataParameter;
import types.termserver.fhdo.de.Session;
import types.termserver.fhdo.de.SysParam;
import types.termserver.fhdo.de.TermUser;
import types.termserver.fhdo.de.ValueSet;
import types.termserver.fhdo.de.ValueSetMetadataValue;
import types.termserver.fhdo.de.ValueSetVersion;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.fhdo.terminologie.ws.administration package. 
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

    private final static QName _ImportValueSetResponse_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ImportValueSetResponse");
    private final static QName _Domain_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "domain");
    private final static QName _SysParam_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "sysParam");
    private final static QName _MaintainDomainResponse_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "MaintainDomainResponse");
    private final static QName _CodeSystemConceptTranslation_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystemConceptTranslation");
    private final static QName _ImportCodeSystemCancelResponse_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ImportCodeSystemCancelResponse");
    private final static QName _CodeSystem_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystem");
    private final static QName _CodeSystemVersion_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystemVersion");
    private final static QName _CreateDomainResponse_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "CreateDomainResponse");
    private final static QName _ActualProceedingsResponse_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ActualProceedingsResponse");
    private final static QName _ImportValueSet_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ImportValueSet");
    private final static QName _TermUser_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "termUser");
    private final static QName _ConceptValueSetMembership_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "conceptValueSetMembership");
    private final static QName _ValueSetMetadataValue_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "valueSetMetadataValue");
    private final static QName _ExportCodeSystemContentResponse_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ExportCodeSystemContentResponse");
    private final static QName _CodeSystemConcept_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystemConcept");
    private final static QName _CodeSystemVersionEntityMembershipId_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystemVersionEntityMembershipId");
    private final static QName _ImportCodeSystemResponse_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ImportCodeSystemResponse");
    private final static QName _MaintainDomain_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "MaintainDomain");
    private final static QName _CodeSystemEntity_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystemEntity");
    private final static QName _ExportCodeSystemContent_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ExportCodeSystemContent");
    private final static QName _ValueSetVersion_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "valueSetVersion");
    private final static QName _CodeSystemEntityVersion_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystemEntityVersion");
    private final static QName _MetadataParameter_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "metadataParameter");
    private final static QName _ImportCodeSystemStatus_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ImportCodeSystemStatus");
    private final static QName _ActualProceedings_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ActualProceedings");
    private final static QName _CodeSystemEntityVersionAssociation_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystemEntityVersionAssociation");
    private final static QName _CodeSystemMetadataValue_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystemMetadataValue");
    private final static QName _ConceptValueSetMembershipId_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "conceptValueSetMembershipId");
    private final static QName _LicencedUser_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "licencedUser");
    private final static QName _ImportCodeSystemCancel_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ImportCodeSystemCancel");
    private final static QName _Session_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "session");
    private final static QName _ImportCodeSystemStatusResponse_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ImportCodeSystemStatusResponse");
    private final static QName _ActualProceedingsResponseType_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "actualProceedingsResponseType");
    private final static QName _CreateDomain_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "CreateDomain");
    private final static QName _ExportValueSetContentResponse_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ExportValueSetContentResponse");
    private final static QName _ValueSet_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "valueSet");
    private final static QName _DomainValue_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "domainValue");
    private final static QName _ExportValueSetContent_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ExportValueSetContent");
    private final static QName _LicencedUserId_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "licencedUserId");
    private final static QName _AssociationType_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "associationType");
    private final static QName _CodeSystemVersionEntityMembership_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "codeSystemVersionEntityMembership");
    private final static QName _ImportCodeSystem_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "ImportCodeSystem");
    private final static QName _LicenceType_QNAME = new QName("http://administration.ws.terminologie.fhdo.de/", "licenceType");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.fhdo.terminologie.ws.administration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ImportCodeSystemStatusResponse }
     * 
     */
    public ImportCodeSystemStatusResponse createImportCodeSystemStatusResponse() {
        return new ImportCodeSystemStatusResponse();
    }

    /**
     * Create an instance of {@link ExportValueSetContentResponse }
     * 
     */
    public ExportValueSetContentResponse createExportValueSetContentResponse() {
        return new ExportValueSetContentResponse();
    }

    /**
     * Create an instance of {@link ExportCodeSystemContentResponse }
     * 
     */
    public ExportCodeSystemContentResponse createExportCodeSystemContentResponse() {
        return new ExportCodeSystemContentResponse();
    }

    /**
     * Create an instance of {@link ImportValueSetResponse }
     * 
     */
    public ImportValueSetResponse createImportValueSetResponse() {
        return new ImportValueSetResponse();
    }

    /**
     * Create an instance of {@link MaintainDomainResponse }
     * 
     */
    public MaintainDomainResponse createMaintainDomainResponse() {
        return new MaintainDomainResponse();
    }

    /**
     * Create an instance of {@link CreateDomainResponse }
     * 
     */
    public CreateDomainResponse createCreateDomainResponse() {
        return new CreateDomainResponse();
    }

    /**
     * Create an instance of {@link ImportCodeSystemResponse }
     * 
     */
    public ImportCodeSystemResponse createImportCodeSystemResponse() {
        return new ImportCodeSystemResponse();
    }

    /**
     * Create an instance of {@link MaintainDomain }
     * 
     */
    public MaintainDomain createMaintainDomain() {
        return new MaintainDomain();
    }

    /**
     * Create an instance of {@link CreateDomainResponseType }
     * 
     */
    public CreateDomainResponseType createCreateDomainResponseType() {
        return new CreateDomainResponseType();
    }

    /**
     * Create an instance of {@link ReturnType }
     * 
     */
    public ReturnType createReturnType() {
        return new ReturnType();
    }

    /**
     * Create an instance of {@link ImportCodeSystemStatusResponseType }
     * 
     */
    public ImportCodeSystemStatusResponseType createImportCodeSystemStatusResponseType() {
        return new ImportCodeSystemStatusResponseType();
    }

    /**
     * Create an instance of {@link ImportValueSet }
     * 
     */
    public ImportValueSet createImportValueSet() {
        return new ImportValueSet();
    }

    /**
     * Create an instance of {@link ActualProceedingsResponse }
     * 
     */
    public ActualProceedingsResponse createActualProceedingsResponse() {
        return new ActualProceedingsResponse();
    }

    /**
     * Create an instance of {@link ImportCodeSystemCancelResponse }
     * 
     */
    public ImportCodeSystemCancelResponse createImportCodeSystemCancelResponse() {
        return new ImportCodeSystemCancelResponse();
    }

    /**
     * Create an instance of {@link ImportCodeSystemResponseType }
     * 
     */
    public ImportCodeSystemResponseType createImportCodeSystemResponseType() {
        return new ImportCodeSystemResponseType();
    }

    /**
     * Create an instance of {@link ImportCodeSystem }
     * 
     */
    public ImportCodeSystem createImportCodeSystem() {
        return new ImportCodeSystem();
    }

    /**
     * Create an instance of {@link ExportValueSetContent }
     * 
     */
    public ExportValueSetContent createExportValueSetContent() {
        return new ExportValueSetContent();
    }

    /**
     * Create an instance of {@link CreateDomain }
     * 
     */
    public CreateDomain createCreateDomain() {
        return new CreateDomain();
    }

    /**
     * Create an instance of {@link ImportCodeSystemCancel }
     * 
     */
    public ImportCodeSystemCancel createImportCodeSystemCancel() {
        return new ImportCodeSystemCancel();
    }

    /**
     * Create an instance of {@link ImportValueSetResponseType }
     * 
     */
    public ImportValueSetResponseType createImportValueSetResponseType() {
        return new ImportValueSetResponseType();
    }

    /**
     * Create an instance of {@link ActualProceedings }
     * 
     */
    public ActualProceedings createActualProceedings() {
        return new ActualProceedings();
    }

    /**
     * Create an instance of {@link ImportCodeSystemStatus }
     * 
     */
    public ImportCodeSystemStatus createImportCodeSystemStatus() {
        return new ImportCodeSystemStatus();
    }

    /**
     * Create an instance of {@link ExportCodeSystemContent }
     * 
     */
    public ExportCodeSystemContent createExportCodeSystemContent() {
        return new ExportCodeSystemContent();
    }

    /**
     * Create an instance of {@link MaintainDomainResponseType }
     * 
     */
    public MaintainDomainResponseType createMaintainDomainResponseType() {
        return new MaintainDomainResponseType();
    }

    /**
     * Create an instance of {@link ExportValueSetContentResponseType }
     * 
     */
    public ExportValueSetContentResponseType createExportValueSetContentResponseType() {
        return new ExportValueSetContentResponseType();
    }

    /**
     * Create an instance of {@link ExportType }
     * 
     */
    public ExportType createExportType() {
        return new ExportType();
    }

    /**
     * Create an instance of {@link ExportCodeSystemContentResponseType }
     * 
     */
    public ExportCodeSystemContentResponseType createExportCodeSystemContentResponseType() {
        return new ExportCodeSystemContentResponseType();
    }

    /**
     * Create an instance of {@link ActualProceedingsResponseType }
     * 
     */
    public ActualProceedingsResponseType createActualProceedingsResponseType() {
        return new ActualProceedingsResponseType();
    }

    /**
     * Create an instance of {@link ImportCodeSystemRequestType }
     * 
     */
    public ImportCodeSystemRequestType createImportCodeSystemRequestType() {
        return new ImportCodeSystemRequestType();
    }

    /**
     * Create an instance of {@link MaintainDomainRequestType }
     * 
     */
    public MaintainDomainRequestType createMaintainDomainRequestType() {
        return new MaintainDomainRequestType();
    }

    /**
     * Create an instance of {@link ExportValueSetContentRequestType }
     * 
     */
    public ExportValueSetContentRequestType createExportValueSetContentRequestType() {
        return new ExportValueSetContentRequestType();
    }

    /**
     * Create an instance of {@link ActualProceedingsRequestType }
     * 
     */
    public ActualProceedingsRequestType createActualProceedingsRequestType() {
        return new ActualProceedingsRequestType();
    }

    /**
     * Create an instance of {@link ImportCodeSystemStatusRequestType }
     * 
     */
    public ImportCodeSystemStatusRequestType createImportCodeSystemStatusRequestType() {
        return new ImportCodeSystemStatusRequestType();
    }

    /**
     * Create an instance of {@link ImportValueSetRequestType }
     * 
     */
    public ImportValueSetRequestType createImportValueSetRequestType() {
        return new ImportValueSetRequestType();
    }

    /**
     * Create an instance of {@link ActualProceeding }
     * 
     */
    public ActualProceeding createActualProceeding() {
        return new ActualProceeding();
    }

    /**
     * Create an instance of {@link ImportCodeSystemCancelResponseType }
     * 
     */
    public ImportCodeSystemCancelResponseType createImportCodeSystemCancelResponseType() {
        return new ImportCodeSystemCancelResponseType();
    }

    /**
     * Create an instance of {@link FilecontentListEntry }
     * 
     */
    public FilecontentListEntry createFilecontentListEntry() {
        return new FilecontentListEntry();
    }

    /**
     * Create an instance of {@link ImportType }
     * 
     */
    public ImportType createImportType() {
        return new ImportType();
    }

    /**
     * Create an instance of {@link ImportCodeSystemCancelRequestType }
     * 
     */
    public ImportCodeSystemCancelRequestType createImportCodeSystemCancelRequestType() {
        return new ImportCodeSystemCancelRequestType();
    }

    /**
     * Create an instance of {@link ExportCodeSystemContentRequestType }
     * 
     */
    public ExportCodeSystemContentRequestType createExportCodeSystemContentRequestType() {
        return new ExportCodeSystemContentRequestType();
    }

    /**
     * Create an instance of {@link ExportParameterType }
     * 
     */
    public ExportParameterType createExportParameterType() {
        return new ExportParameterType();
    }

    /**
     * Create an instance of {@link CreateDomainRequestType }
     * 
     */
    public CreateDomainRequestType createCreateDomainRequestType() {
        return new CreateDomainRequestType();
    }

    /**
     * Create an instance of {@link ImportCodeSystemStatusResponse.Return }
     * 
     */
    public ImportCodeSystemStatusResponse.Return createImportCodeSystemStatusResponseReturn() {
        return new ImportCodeSystemStatusResponse.Return();
    }

    /**
     * Create an instance of {@link ExportValueSetContentResponse.Return }
     * 
     */
    public ExportValueSetContentResponse.Return createExportValueSetContentResponseReturn() {
        return new ExportValueSetContentResponse.Return();
    }

    /**
     * Create an instance of {@link ExportCodeSystemContentResponse.Return }
     * 
     */
    public ExportCodeSystemContentResponse.Return createExportCodeSystemContentResponseReturn() {
        return new ExportCodeSystemContentResponse.Return();
    }

    /**
     * Create an instance of {@link ImportValueSetResponse.Return }
     * 
     */
    public ImportValueSetResponse.Return createImportValueSetResponseReturn() {
        return new ImportValueSetResponse.Return();
    }

    /**
     * Create an instance of {@link MaintainDomainResponse.Return }
     * 
     */
    public MaintainDomainResponse.Return createMaintainDomainResponseReturn() {
        return new MaintainDomainResponse.Return();
    }

    /**
     * Create an instance of {@link CreateDomainResponse.Return }
     * 
     */
    public CreateDomainResponse.Return createCreateDomainResponseReturn() {
        return new CreateDomainResponse.Return();
    }

    /**
     * Create an instance of {@link ImportCodeSystemResponse.Return }
     * 
     */
    public ImportCodeSystemResponse.Return createImportCodeSystemResponseReturn() {
        return new ImportCodeSystemResponse.Return();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportValueSetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ImportValueSetResponse")
    public JAXBElement<ImportValueSetResponse> createImportValueSetResponse(ImportValueSetResponse value) {
        return new JAXBElement<ImportValueSetResponse>(_ImportValueSetResponse_QNAME, ImportValueSetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Domain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "domain")
    public JAXBElement<Domain> createDomain(Domain value) {
        return new JAXBElement<Domain>(_Domain_QNAME, Domain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SysParam }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "sysParam")
    public JAXBElement<SysParam> createSysParam(SysParam value) {
        return new JAXBElement<SysParam>(_SysParam_QNAME, SysParam.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainDomainResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "MaintainDomainResponse")
    public JAXBElement<MaintainDomainResponse> createMaintainDomainResponse(MaintainDomainResponse value) {
        return new JAXBElement<MaintainDomainResponse>(_MaintainDomainResponse_QNAME, MaintainDomainResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemConceptTranslation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystemConceptTranslation")
    public JAXBElement<CodeSystemConceptTranslation> createCodeSystemConceptTranslation(CodeSystemConceptTranslation value) {
        return new JAXBElement<CodeSystemConceptTranslation>(_CodeSystemConceptTranslation_QNAME, CodeSystemConceptTranslation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportCodeSystemCancelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ImportCodeSystemCancelResponse")
    public JAXBElement<ImportCodeSystemCancelResponse> createImportCodeSystemCancelResponse(ImportCodeSystemCancelResponse value) {
        return new JAXBElement<ImportCodeSystemCancelResponse>(_ImportCodeSystemCancelResponse_QNAME, ImportCodeSystemCancelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystem")
    public JAXBElement<CodeSystem> createCodeSystem(CodeSystem value) {
        return new JAXBElement<CodeSystem>(_CodeSystem_QNAME, CodeSystem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystemVersion")
    public JAXBElement<CodeSystemVersion> createCodeSystemVersion(CodeSystemVersion value) {
        return new JAXBElement<CodeSystemVersion>(_CodeSystemVersion_QNAME, CodeSystemVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateDomainResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "CreateDomainResponse")
    public JAXBElement<CreateDomainResponse> createCreateDomainResponse(CreateDomainResponse value) {
        return new JAXBElement<CreateDomainResponse>(_CreateDomainResponse_QNAME, CreateDomainResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActualProceedingsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ActualProceedingsResponse")
    public JAXBElement<ActualProceedingsResponse> createActualProceedingsResponse(ActualProceedingsResponse value) {
        return new JAXBElement<ActualProceedingsResponse>(_ActualProceedingsResponse_QNAME, ActualProceedingsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportValueSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ImportValueSet")
    public JAXBElement<ImportValueSet> createImportValueSet(ImportValueSet value) {
        return new JAXBElement<ImportValueSet>(_ImportValueSet_QNAME, ImportValueSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TermUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "termUser")
    public JAXBElement<TermUser> createTermUser(TermUser value) {
        return new JAXBElement<TermUser>(_TermUser_QNAME, TermUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptValueSetMembership }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "conceptValueSetMembership")
    public JAXBElement<ConceptValueSetMembership> createConceptValueSetMembership(ConceptValueSetMembership value) {
        return new JAXBElement<ConceptValueSetMembership>(_ConceptValueSetMembership_QNAME, ConceptValueSetMembership.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueSetMetadataValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "valueSetMetadataValue")
    public JAXBElement<ValueSetMetadataValue> createValueSetMetadataValue(ValueSetMetadataValue value) {
        return new JAXBElement<ValueSetMetadataValue>(_ValueSetMetadataValue_QNAME, ValueSetMetadataValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportCodeSystemContentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ExportCodeSystemContentResponse")
    public JAXBElement<ExportCodeSystemContentResponse> createExportCodeSystemContentResponse(ExportCodeSystemContentResponse value) {
        return new JAXBElement<ExportCodeSystemContentResponse>(_ExportCodeSystemContentResponse_QNAME, ExportCodeSystemContentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemConcept }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystemConcept")
    public JAXBElement<CodeSystemConcept> createCodeSystemConcept(CodeSystemConcept value) {
        return new JAXBElement<CodeSystemConcept>(_CodeSystemConcept_QNAME, CodeSystemConcept.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemVersionEntityMembershipId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystemVersionEntityMembershipId")
    public JAXBElement<CodeSystemVersionEntityMembershipId> createCodeSystemVersionEntityMembershipId(CodeSystemVersionEntityMembershipId value) {
        return new JAXBElement<CodeSystemVersionEntityMembershipId>(_CodeSystemVersionEntityMembershipId_QNAME, CodeSystemVersionEntityMembershipId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportCodeSystemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ImportCodeSystemResponse")
    public JAXBElement<ImportCodeSystemResponse> createImportCodeSystemResponse(ImportCodeSystemResponse value) {
        return new JAXBElement<ImportCodeSystemResponse>(_ImportCodeSystemResponse_QNAME, ImportCodeSystemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainDomain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "MaintainDomain")
    public JAXBElement<MaintainDomain> createMaintainDomain(MaintainDomain value) {
        return new JAXBElement<MaintainDomain>(_MaintainDomain_QNAME, MaintainDomain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemEntity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystemEntity")
    public JAXBElement<CodeSystemEntity> createCodeSystemEntity(CodeSystemEntity value) {
        return new JAXBElement<CodeSystemEntity>(_CodeSystemEntity_QNAME, CodeSystemEntity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportCodeSystemContent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ExportCodeSystemContent")
    public JAXBElement<ExportCodeSystemContent> createExportCodeSystemContent(ExportCodeSystemContent value) {
        return new JAXBElement<ExportCodeSystemContent>(_ExportCodeSystemContent_QNAME, ExportCodeSystemContent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueSetVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "valueSetVersion")
    public JAXBElement<ValueSetVersion> createValueSetVersion(ValueSetVersion value) {
        return new JAXBElement<ValueSetVersion>(_ValueSetVersion_QNAME, ValueSetVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemEntityVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystemEntityVersion")
    public JAXBElement<CodeSystemEntityVersion> createCodeSystemEntityVersion(CodeSystemEntityVersion value) {
        return new JAXBElement<CodeSystemEntityVersion>(_CodeSystemEntityVersion_QNAME, CodeSystemEntityVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MetadataParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "metadataParameter")
    public JAXBElement<MetadataParameter> createMetadataParameter(MetadataParameter value) {
        return new JAXBElement<MetadataParameter>(_MetadataParameter_QNAME, MetadataParameter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportCodeSystemStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ImportCodeSystemStatus")
    public JAXBElement<ImportCodeSystemStatus> createImportCodeSystemStatus(ImportCodeSystemStatus value) {
        return new JAXBElement<ImportCodeSystemStatus>(_ImportCodeSystemStatus_QNAME, ImportCodeSystemStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActualProceedings }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ActualProceedings")
    public JAXBElement<ActualProceedings> createActualProceedings(ActualProceedings value) {
        return new JAXBElement<ActualProceedings>(_ActualProceedings_QNAME, ActualProceedings.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemEntityVersionAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystemEntityVersionAssociation")
    public JAXBElement<CodeSystemEntityVersionAssociation> createCodeSystemEntityVersionAssociation(CodeSystemEntityVersionAssociation value) {
        return new JAXBElement<CodeSystemEntityVersionAssociation>(_CodeSystemEntityVersionAssociation_QNAME, CodeSystemEntityVersionAssociation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemMetadataValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystemMetadataValue")
    public JAXBElement<CodeSystemMetadataValue> createCodeSystemMetadataValue(CodeSystemMetadataValue value) {
        return new JAXBElement<CodeSystemMetadataValue>(_CodeSystemMetadataValue_QNAME, CodeSystemMetadataValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptValueSetMembershipId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "conceptValueSetMembershipId")
    public JAXBElement<ConceptValueSetMembershipId> createConceptValueSetMembershipId(ConceptValueSetMembershipId value) {
        return new JAXBElement<ConceptValueSetMembershipId>(_ConceptValueSetMembershipId_QNAME, ConceptValueSetMembershipId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LicencedUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "licencedUser")
    public JAXBElement<LicencedUser> createLicencedUser(LicencedUser value) {
        return new JAXBElement<LicencedUser>(_LicencedUser_QNAME, LicencedUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportCodeSystemCancel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ImportCodeSystemCancel")
    public JAXBElement<ImportCodeSystemCancel> createImportCodeSystemCancel(ImportCodeSystemCancel value) {
        return new JAXBElement<ImportCodeSystemCancel>(_ImportCodeSystemCancel_QNAME, ImportCodeSystemCancel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Session }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "session")
    public JAXBElement<Session> createSession(Session value) {
        return new JAXBElement<Session>(_Session_QNAME, Session.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportCodeSystemStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ImportCodeSystemStatusResponse")
    public JAXBElement<ImportCodeSystemStatusResponse> createImportCodeSystemStatusResponse(ImportCodeSystemStatusResponse value) {
        return new JAXBElement<ImportCodeSystemStatusResponse>(_ImportCodeSystemStatusResponse_QNAME, ImportCodeSystemStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActualProceedingsResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "actualProceedingsResponseType")
    public JAXBElement<ActualProceedingsResponseType> createActualProceedingsResponseType(ActualProceedingsResponseType value) {
        return new JAXBElement<ActualProceedingsResponseType>(_ActualProceedingsResponseType_QNAME, ActualProceedingsResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateDomain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "CreateDomain")
    public JAXBElement<CreateDomain> createCreateDomain(CreateDomain value) {
        return new JAXBElement<CreateDomain>(_CreateDomain_QNAME, CreateDomain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportValueSetContentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ExportValueSetContentResponse")
    public JAXBElement<ExportValueSetContentResponse> createExportValueSetContentResponse(ExportValueSetContentResponse value) {
        return new JAXBElement<ExportValueSetContentResponse>(_ExportValueSetContentResponse_QNAME, ExportValueSetContentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "valueSet")
    public JAXBElement<ValueSet> createValueSet(ValueSet value) {
        return new JAXBElement<ValueSet>(_ValueSet_QNAME, ValueSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DomainValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "domainValue")
    public JAXBElement<DomainValue> createDomainValue(DomainValue value) {
        return new JAXBElement<DomainValue>(_DomainValue_QNAME, DomainValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportValueSetContent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ExportValueSetContent")
    public JAXBElement<ExportValueSetContent> createExportValueSetContent(ExportValueSetContent value) {
        return new JAXBElement<ExportValueSetContent>(_ExportValueSetContent_QNAME, ExportValueSetContent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LicencedUserId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "licencedUserId")
    public JAXBElement<LicencedUserId> createLicencedUserId(LicencedUserId value) {
        return new JAXBElement<LicencedUserId>(_LicencedUserId_QNAME, LicencedUserId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssociationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "associationType")
    public JAXBElement<AssociationType> createAssociationType(AssociationType value) {
        return new JAXBElement<AssociationType>(_AssociationType_QNAME, AssociationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemVersionEntityMembership }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "codeSystemVersionEntityMembership")
    public JAXBElement<CodeSystemVersionEntityMembership> createCodeSystemVersionEntityMembership(CodeSystemVersionEntityMembership value) {
        return new JAXBElement<CodeSystemVersionEntityMembership>(_CodeSystemVersionEntityMembership_QNAME, CodeSystemVersionEntityMembership.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportCodeSystem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "ImportCodeSystem")
    public JAXBElement<ImportCodeSystem> createImportCodeSystem(ImportCodeSystem value) {
        return new JAXBElement<ImportCodeSystem>(_ImportCodeSystem_QNAME, ImportCodeSystem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LicenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administration.ws.terminologie.fhdo.de/", name = "licenceType")
    public JAXBElement<LicenceType> createLicenceType(LicenceType value) {
        return new JAXBElement<LicenceType>(_LicenceType_QNAME, LicenceType.class, null, value);
    }

}
