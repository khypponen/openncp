
package de.fhdo.terminologie.ws.authoring;

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
 * generated in the de.fhdo.terminologie.ws.authoring package. 
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

    private final static QName _CreateConceptAssociationType_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateConceptAssociationType");
    private final static QName _CreateValueSetResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateValueSetResponse");
    private final static QName _LicencedUserId_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "licencedUserId");
    private final static QName _AssociationType_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "associationType");
    private final static QName _CodeSystemVersionEntityMembership_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystemVersionEntityMembership");
    private final static QName _UpdateConceptStatusResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "UpdateConceptStatusResponse");
    private final static QName _LicenceType_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "licenceType");
    private final static QName _MaintainConceptValueSetMembershipResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainConceptValueSetMembershipResponse");
    private final static QName _MaintainValueSetConceptMetadataValue_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainValueSetConceptMetadataValue");
    private final static QName _CreateValueSetConceptMetadataValueResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateValueSetConceptMetadataValueResponse");
    private final static QName _CodeSystemMetadataValue_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystemMetadataValue");
    private final static QName _Session_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "session");
    private final static QName _ConceptValueSetMembershipId_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "conceptValueSetMembershipId");
    private final static QName _LicencedUser_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "licencedUser");
    private final static QName _RemoveValueSetContent_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "RemoveValueSetContent");
    private final static QName _MaintainValueSetConceptMetadataValueResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainValueSetConceptMetadataValueResponse");
    private final static QName _DeleteValueSetConceptMetadataValue_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "DeleteValueSetConceptMetadataValue");
    private final static QName _ValueSetMetadataValue_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "valueSetMetadataValue");
    private final static QName _UpdateCodeSystemVersionStatus_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "UpdateCodeSystemVersionStatus");
    private final static QName _RemoveValueSetContentResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "RemoveValueSetContentResponse");
    private final static QName _CodeSystemVersionEntityMembershipId_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystemVersionEntityMembershipId");
    private final static QName _UpdateConceptValueSetMembershipStatus_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "UpdateConceptValueSetMembershipStatus");
    private final static QName _MaintainCodeSystemVersion_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainCodeSystemVersion");
    private final static QName _CodeSystemEntity_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystemEntity");
    private final static QName _MaintainConceptAssociationType_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainConceptAssociationType");
    private final static QName _CodeSystem_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystem");
    private final static QName _CodeSystemVersion_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystemVersion");
    private final static QName _UpdateValueSetStatus_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "UpdateValueSetStatus");
    private final static QName _MaintainCodeSystemConceptMetadataValue_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainCodeSystemConceptMetadataValue");
    private final static QName _DomainValue_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "domainValue");
    private final static QName _ValueSet_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "valueSet");
    private final static QName _MaintainCodeSystemVersionResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainCodeSystemVersionResponse");
    private final static QName _MaintainConceptResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainConceptResponse");
    private final static QName _CreateValueSet_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateValueSet");
    private final static QName _CodeSystemEntityVersionAssociation_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystemEntityVersionAssociation");
    private final static QName _MaintainConceptAssociationTypeResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainConceptAssociationTypeResponse");
    private final static QName _CreateConceptResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateConceptResponse");
    private final static QName _UpdateConceptStatus_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "UpdateConceptStatus");
    private final static QName _DeleteValueSetConceptMetadataValueResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "DeleteValueSetConceptMetadataValueResponse");
    private final static QName _CreateConceptAssociationTypeResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateConceptAssociationTypeResponse");
    private final static QName _ConceptValueSetMembership_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "conceptValueSetMembership");
    private final static QName _RemoveTerminologyOrConceptResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "RemoveTerminologyOrConceptResponse");
    private final static QName _TermUser_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "termUser");
    private final static QName _CreateValueSetConceptMetadataValue_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateValueSetConceptMetadataValue");
    private final static QName _CodeSystemConcept_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystemConcept");
    private final static QName _UpdateConceptValueSetMembershipStatusResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "UpdateConceptValueSetMembershipStatusResponse");
    private final static QName _UpdateValueSetStatusResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "UpdateValueSetStatusResponse");
    private final static QName _MaintainConceptValueSetMembership_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainConceptValueSetMembership");
    private final static QName _CodeSystemEntityVersion_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystemEntityVersion");
    private final static QName _MetadataParameter_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "metadataParameter");
    private final static QName _CreateCodeSystemResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateCodeSystemResponse");
    private final static QName _ValueSetVersion_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "valueSetVersion");
    private final static QName _UpdateCodeSystemVersionStatusResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "UpdateCodeSystemVersionStatusResponse");
    private final static QName _MaintainCodeSystemConceptMetadataValueResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainCodeSystemConceptMetadataValueResponse");
    private final static QName _MaintainConcept_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainConcept");
    private final static QName _CreateValueSetContent_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateValueSetContent");
    private final static QName _Domain_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "domain");
    private final static QName _CreateValueSetContentResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateValueSetContentResponse");
    private final static QName _RemoveTerminologyOrConcept_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "RemoveTerminologyOrConcept");
    private final static QName _CodeSystemConceptTranslation_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "codeSystemConceptTranslation");
    private final static QName _CreateCodeSystem_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateCodeSystem");
    private final static QName _SysParam_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "sysParam");
    private final static QName _MaintainValueSet_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainValueSet");
    private final static QName _MaintainValueSetResponse_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "MaintainValueSetResponse");
    private final static QName _CreateConcept_QNAME = new QName("http://authoring.ws.terminologie.fhdo.de/", "CreateConcept");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.fhdo.terminologie.ws.authoring
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MaintainConceptAssociationTypeResponse }
     * 
     */
    public MaintainConceptAssociationTypeResponse createMaintainConceptAssociationTypeResponse() {
        return new MaintainConceptAssociationTypeResponse();
    }

    /**
     * Create an instance of {@link DeleteValueSetConceptMetadataValueResponse }
     * 
     */
    public DeleteValueSetConceptMetadataValueResponse createDeleteValueSetConceptMetadataValueResponse() {
        return new DeleteValueSetConceptMetadataValueResponse();
    }

    /**
     * Create an instance of {@link CreateConceptResponse }
     * 
     */
    public CreateConceptResponse createCreateConceptResponse() {
        return new CreateConceptResponse();
    }

    /**
     * Create an instance of {@link CreateConceptAssociationTypeResponse }
     * 
     */
    public CreateConceptAssociationTypeResponse createCreateConceptAssociationTypeResponse() {
        return new CreateConceptAssociationTypeResponse();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemVersionResponse }
     * 
     */
    public MaintainCodeSystemVersionResponse createMaintainCodeSystemVersionResponse() {
        return new MaintainCodeSystemVersionResponse();
    }

    /**
     * Create an instance of {@link MaintainConceptResponse }
     * 
     */
    public MaintainConceptResponse createMaintainConceptResponse() {
        return new MaintainConceptResponse();
    }

    /**
     * Create an instance of {@link UpdateCodeSystemVersionStatusResponse }
     * 
     */
    public UpdateCodeSystemVersionStatusResponse createUpdateCodeSystemVersionStatusResponse() {
        return new UpdateCodeSystemVersionStatusResponse();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemConceptMetadataValueResponse }
     * 
     */
    public MaintainCodeSystemConceptMetadataValueResponse createMaintainCodeSystemConceptMetadataValueResponse() {
        return new MaintainCodeSystemConceptMetadataValueResponse();
    }

    /**
     * Create an instance of {@link CreateValueSetContentResponse }
     * 
     */
    public CreateValueSetContentResponse createCreateValueSetContentResponse() {
        return new CreateValueSetContentResponse();
    }

    /**
     * Create an instance of {@link MaintainValueSetResponse }
     * 
     */
    public MaintainValueSetResponse createMaintainValueSetResponse() {
        return new MaintainValueSetResponse();
    }

    /**
     * Create an instance of {@link UpdateValueSetStatusResponse }
     * 
     */
    public UpdateValueSetStatusResponse createUpdateValueSetStatusResponse() {
        return new UpdateValueSetStatusResponse();
    }

    /**
     * Create an instance of {@link UpdateConceptValueSetMembershipStatusResponse }
     * 
     */
    public UpdateConceptValueSetMembershipStatusResponse createUpdateConceptValueSetMembershipStatusResponse() {
        return new UpdateConceptValueSetMembershipStatusResponse();
    }

    /**
     * Create an instance of {@link CreateCodeSystemResponse }
     * 
     */
    public CreateCodeSystemResponse createCreateCodeSystemResponse() {
        return new CreateCodeSystemResponse();
    }

    /**
     * Create an instance of {@link CreateValueSetConceptMetadataValueResponse }
     * 
     */
    public CreateValueSetConceptMetadataValueResponse createCreateValueSetConceptMetadataValueResponse() {
        return new CreateValueSetConceptMetadataValueResponse();
    }

    /**
     * Create an instance of {@link MaintainValueSetConceptMetadataValueResponse }
     * 
     */
    public MaintainValueSetConceptMetadataValueResponse createMaintainValueSetConceptMetadataValueResponse() {
        return new MaintainValueSetConceptMetadataValueResponse();
    }

    /**
     * Create an instance of {@link CreateValueSetResponse }
     * 
     */
    public CreateValueSetResponse createCreateValueSetResponse() {
        return new CreateValueSetResponse();
    }

    /**
     * Create an instance of {@link UpdateConceptStatusResponse }
     * 
     */
    public UpdateConceptStatusResponse createUpdateConceptStatusResponse() {
        return new UpdateConceptStatusResponse();
    }

    /**
     * Create an instance of {@link MaintainConceptValueSetMembershipResponse }
     * 
     */
    public MaintainConceptValueSetMembershipResponse createMaintainConceptValueSetMembershipResponse() {
        return new MaintainConceptValueSetMembershipResponse();
    }

    /**
     * Create an instance of {@link CreateCodeSystemResponseType }
     * 
     */
    public CreateCodeSystemResponseType createCreateCodeSystemResponseType() {
        return new CreateCodeSystemResponseType();
    }

    /**
     * Create an instance of {@link ReturnType }
     * 
     */
    public ReturnType createReturnType() {
        return new ReturnType();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemVersion }
     * 
     */
    public MaintainCodeSystemVersion createMaintainCodeSystemVersion() {
        return new MaintainCodeSystemVersion();
    }

    /**
     * Create an instance of {@link RemoveValueSetContentResponse }
     * 
     */
    public RemoveValueSetContentResponse createRemoveValueSetContentResponse() {
        return new RemoveValueSetContentResponse();
    }

    /**
     * Create an instance of {@link UpdateCodeSystemVersionStatus }
     * 
     */
    public UpdateCodeSystemVersionStatus createUpdateCodeSystemVersionStatus() {
        return new UpdateCodeSystemVersionStatus();
    }

    /**
     * Create an instance of {@link UpdateConceptValueSetMembershipStatus }
     * 
     */
    public UpdateConceptValueSetMembershipStatus createUpdateConceptValueSetMembershipStatus() {
        return new UpdateConceptValueSetMembershipStatus();
    }

    /**
     * Create an instance of {@link MaintainValueSetResponseType }
     * 
     */
    public MaintainValueSetResponseType createMaintainValueSetResponseType() {
        return new MaintainValueSetResponseType();
    }

    /**
     * Create an instance of {@link UpdateValueSetStatus }
     * 
     */
    public UpdateValueSetStatus createUpdateValueSetStatus() {
        return new UpdateValueSetStatus();
    }

    /**
     * Create an instance of {@link MaintainConceptAssociationType }
     * 
     */
    public MaintainConceptAssociationType createMaintainConceptAssociationType() {
        return new MaintainConceptAssociationType();
    }

    /**
     * Create an instance of {@link UpdateCodeSystemVersionStatusResponseType }
     * 
     */
    public UpdateCodeSystemVersionStatusResponseType createUpdateCodeSystemVersionStatusResponseType() {
        return new UpdateCodeSystemVersionStatusResponseType();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemConceptMetadataValueResponseType }
     * 
     */
    public MaintainCodeSystemConceptMetadataValueResponseType createMaintainCodeSystemConceptMetadataValueResponseType() {
        return new MaintainCodeSystemConceptMetadataValueResponseType();
    }

    /**
     * Create an instance of {@link CreateConceptAssociationType }
     * 
     */
    public CreateConceptAssociationType createCreateConceptAssociationType() {
        return new CreateConceptAssociationType();
    }

    /**
     * Create an instance of {@link DeleteValueSetConceptMetadataValueResponseType }
     * 
     */
    public DeleteValueSetConceptMetadataValueResponseType createDeleteValueSetConceptMetadataValueResponseType() {
        return new DeleteValueSetConceptMetadataValueResponseType();
    }

    /**
     * Create an instance of {@link UpdateValueSetStatusResponseType }
     * 
     */
    public UpdateValueSetStatusResponseType createUpdateValueSetStatusResponseType() {
        return new UpdateValueSetStatusResponseType();
    }

    /**
     * Create an instance of {@link DeleteValueSetConceptMetadataValue }
     * 
     */
    public DeleteValueSetConceptMetadataValue createDeleteValueSetConceptMetadataValue() {
        return new DeleteValueSetConceptMetadataValue();
    }

    /**
     * Create an instance of {@link MaintainConceptAssociationTypeResponseType }
     * 
     */
    public MaintainConceptAssociationTypeResponseType createMaintainConceptAssociationTypeResponseType() {
        return new MaintainConceptAssociationTypeResponseType();
    }

    /**
     * Create an instance of {@link RemoveValueSetContent }
     * 
     */
    public RemoveValueSetContent createRemoveValueSetContent() {
        return new RemoveValueSetContent();
    }

    /**
     * Create an instance of {@link CreateValueSetContentResponseType }
     * 
     */
    public CreateValueSetContentResponseType createCreateValueSetContentResponseType() {
        return new CreateValueSetContentResponseType();
    }

    /**
     * Create an instance of {@link MaintainConceptValueSetMembershipResponseType }
     * 
     */
    public MaintainConceptValueSetMembershipResponseType createMaintainConceptValueSetMembershipResponseType() {
        return new MaintainConceptValueSetMembershipResponseType();
    }

    /**
     * Create an instance of {@link MaintainValueSetConceptMetadataValue }
     * 
     */
    public MaintainValueSetConceptMetadataValue createMaintainValueSetConceptMetadataValue() {
        return new MaintainValueSetConceptMetadataValue();
    }

    /**
     * Create an instance of {@link CreateValueSetResponseType }
     * 
     */
    public CreateValueSetResponseType createCreateValueSetResponseType() {
        return new CreateValueSetResponseType();
    }

    /**
     * Create an instance of {@link MaintainConceptValueSetMembership }
     * 
     */
    public MaintainConceptValueSetMembership createMaintainConceptValueSetMembership() {
        return new MaintainConceptValueSetMembership();
    }

    /**
     * Create an instance of {@link UpdateConceptStatusResponseType }
     * 
     */
    public UpdateConceptStatusResponseType createUpdateConceptStatusResponseType() {
        return new UpdateConceptStatusResponseType();
    }

    /**
     * Create an instance of {@link CreateValueSetConceptMetadataValue }
     * 
     */
    public CreateValueSetConceptMetadataValue createCreateValueSetConceptMetadataValue() {
        return new CreateValueSetConceptMetadataValue();
    }

    /**
     * Create an instance of {@link RemoveTerminologyOrConceptResponse }
     * 
     */
    public RemoveTerminologyOrConceptResponse createRemoveTerminologyOrConceptResponse() {
        return new RemoveTerminologyOrConceptResponse();
    }

    /**
     * Create an instance of {@link MaintainValueSet }
     * 
     */
    public MaintainValueSet createMaintainValueSet() {
        return new MaintainValueSet();
    }

    /**
     * Create an instance of {@link CreateConcept }
     * 
     */
    public CreateConcept createCreateConcept() {
        return new CreateConcept();
    }

    /**
     * Create an instance of {@link RemoveTerminologyOrConcept }
     * 
     */
    public RemoveTerminologyOrConcept createRemoveTerminologyOrConcept() {
        return new RemoveTerminologyOrConcept();
    }

    /**
     * Create an instance of {@link MaintainValueSetConceptMetadataValueResponseType }
     * 
     */
    public MaintainValueSetConceptMetadataValueResponseType createMaintainValueSetConceptMetadataValueResponseType() {
        return new MaintainValueSetConceptMetadataValueResponseType();
    }

    /**
     * Create an instance of {@link CreateCodeSystem }
     * 
     */
    public CreateCodeSystem createCreateCodeSystem() {
        return new CreateCodeSystem();
    }

    /**
     * Create an instance of {@link CreateValueSetContent }
     * 
     */
    public CreateValueSetContent createCreateValueSetContent() {
        return new CreateValueSetContent();
    }

    /**
     * Create an instance of {@link MaintainConcept }
     * 
     */
    public MaintainConcept createMaintainConcept() {
        return new MaintainConcept();
    }

    /**
     * Create an instance of {@link UpdateConceptValueSetMembershipStatusResponseType }
     * 
     */
    public UpdateConceptValueSetMembershipStatusResponseType createUpdateConceptValueSetMembershipStatusResponseType() {
        return new UpdateConceptValueSetMembershipStatusResponseType();
    }

    /**
     * Create an instance of {@link CreateConceptAssociationTypeResponseType }
     * 
     */
    public CreateConceptAssociationTypeResponseType createCreateConceptAssociationTypeResponseType() {
        return new CreateConceptAssociationTypeResponseType();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemVersionResponseType }
     * 
     */
    public MaintainCodeSystemVersionResponseType createMaintainCodeSystemVersionResponseType() {
        return new MaintainCodeSystemVersionResponseType();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemConceptMetadataValue }
     * 
     */
    public MaintainCodeSystemConceptMetadataValue createMaintainCodeSystemConceptMetadataValue() {
        return new MaintainCodeSystemConceptMetadataValue();
    }

    /**
     * Create an instance of {@link CreateValueSetConceptMetadataValueResponseType }
     * 
     */
    public CreateValueSetConceptMetadataValueResponseType createCreateValueSetConceptMetadataValueResponseType() {
        return new CreateValueSetConceptMetadataValueResponseType();
    }

    /**
     * Create an instance of {@link UpdateConceptStatus }
     * 
     */
    public UpdateConceptStatus createUpdateConceptStatus() {
        return new UpdateConceptStatus();
    }

    /**
     * Create an instance of {@link MaintainConceptResponseType }
     * 
     */
    public MaintainConceptResponseType createMaintainConceptResponseType() {
        return new MaintainConceptResponseType();
    }

    /**
     * Create an instance of {@link CreateConceptResponseType }
     * 
     */
    public CreateConceptResponseType createCreateConceptResponseType() {
        return new CreateConceptResponseType();
    }

    /**
     * Create an instance of {@link CreateValueSet }
     * 
     */
    public CreateValueSet createCreateValueSet() {
        return new CreateValueSet();
    }

    /**
     * Create an instance of {@link CreateConceptAssociationTypeRequestType }
     * 
     */
    public CreateConceptAssociationTypeRequestType createCreateConceptAssociationTypeRequestType() {
        return new CreateConceptAssociationTypeRequestType();
    }

    /**
     * Create an instance of {@link CreateValueSetContentRequestType }
     * 
     */
    public CreateValueSetContentRequestType createCreateValueSetContentRequestType() {
        return new CreateValueSetContentRequestType();
    }

    /**
     * Create an instance of {@link RemoveTerminologyOrConceptResponseType }
     * 
     */
    public RemoveTerminologyOrConceptResponseType createRemoveTerminologyOrConceptResponseType() {
        return new RemoveTerminologyOrConceptResponseType();
    }

    /**
     * Create an instance of {@link MaintainValueSetConceptMetadataValueRequestType }
     * 
     */
    public MaintainValueSetConceptMetadataValueRequestType createMaintainValueSetConceptMetadataValueRequestType() {
        return new MaintainValueSetConceptMetadataValueRequestType();
    }

    /**
     * Create an instance of {@link DeleteInfo }
     * 
     */
    public DeleteInfo createDeleteInfo() {
        return new DeleteInfo();
    }

    /**
     * Create an instance of {@link CreateValueSetRequestType }
     * 
     */
    public CreateValueSetRequestType createCreateValueSetRequestType() {
        return new CreateValueSetRequestType();
    }

    /**
     * Create an instance of {@link DeleteValueSetConceptMetadataValueRequestType }
     * 
     */
    public DeleteValueSetConceptMetadataValueRequestType createDeleteValueSetConceptMetadataValueRequestType() {
        return new DeleteValueSetConceptMetadataValueRequestType();
    }

    /**
     * Create an instance of {@link VersioningType }
     * 
     */
    public VersioningType createVersioningType() {
        return new VersioningType();
    }

    /**
     * Create an instance of {@link CreateConceptRequestType }
     * 
     */
    public CreateConceptRequestType createCreateConceptRequestType() {
        return new CreateConceptRequestType();
    }

    /**
     * Create an instance of {@link RemoveTerminologyOrConceptRequestType }
     * 
     */
    public RemoveTerminologyOrConceptRequestType createRemoveTerminologyOrConceptRequestType() {
        return new RemoveTerminologyOrConceptRequestType();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemConceptMetadataValueRequestType }
     * 
     */
    public MaintainCodeSystemConceptMetadataValueRequestType createMaintainCodeSystemConceptMetadataValueRequestType() {
        return new MaintainCodeSystemConceptMetadataValueRequestType();
    }

    /**
     * Create an instance of {@link MaintainConceptAssociationTypeRequestType }
     * 
     */
    public MaintainConceptAssociationTypeRequestType createMaintainConceptAssociationTypeRequestType() {
        return new MaintainConceptAssociationTypeRequestType();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemVersionRequestType }
     * 
     */
    public MaintainCodeSystemVersionRequestType createMaintainCodeSystemVersionRequestType() {
        return new MaintainCodeSystemVersionRequestType();
    }

    /**
     * Create an instance of {@link CreateValueSetConceptMetadataValueRequestType }
     * 
     */
    public CreateValueSetConceptMetadataValueRequestType createCreateValueSetConceptMetadataValueRequestType() {
        return new CreateValueSetConceptMetadataValueRequestType();
    }

    /**
     * Create an instance of {@link MaintainValueSetRequestType }
     * 
     */
    public MaintainValueSetRequestType createMaintainValueSetRequestType() {
        return new MaintainValueSetRequestType();
    }

    /**
     * Create an instance of {@link MaintainConceptValueSetMembershipRequestType }
     * 
     */
    public MaintainConceptValueSetMembershipRequestType createMaintainConceptValueSetMembershipRequestType() {
        return new MaintainConceptValueSetMembershipRequestType();
    }

    /**
     * Create an instance of {@link MaintainConceptRequestType }
     * 
     */
    public MaintainConceptRequestType createMaintainConceptRequestType() {
        return new MaintainConceptRequestType();
    }

    /**
     * Create an instance of {@link UpdateConceptStatusRequestType }
     * 
     */
    public UpdateConceptStatusRequestType createUpdateConceptStatusRequestType() {
        return new UpdateConceptStatusRequestType();
    }

    /**
     * Create an instance of {@link CreateCodeSystemRequestType }
     * 
     */
    public CreateCodeSystemRequestType createCreateCodeSystemRequestType() {
        return new CreateCodeSystemRequestType();
    }

    /**
     * Create an instance of {@link UpdateCodeSystemVersionStatusRequestType }
     * 
     */
    public UpdateCodeSystemVersionStatusRequestType createUpdateCodeSystemVersionStatusRequestType() {
        return new UpdateCodeSystemVersionStatusRequestType();
    }

    /**
     * Create an instance of {@link UpdateConceptValueSetMembershipStatusRequestType }
     * 
     */
    public UpdateConceptValueSetMembershipStatusRequestType createUpdateConceptValueSetMembershipStatusRequestType() {
        return new UpdateConceptValueSetMembershipStatusRequestType();
    }

    /**
     * Create an instance of {@link RemoveValueSetContentResponseType }
     * 
     */
    public RemoveValueSetContentResponseType createRemoveValueSetContentResponseType() {
        return new RemoveValueSetContentResponseType();
    }

    /**
     * Create an instance of {@link RemoveValueSetContentRequestType }
     * 
     */
    public RemoveValueSetContentRequestType createRemoveValueSetContentRequestType() {
        return new RemoveValueSetContentRequestType();
    }

    /**
     * Create an instance of {@link UpdateValueSetStatusRequestType }
     * 
     */
    public UpdateValueSetStatusRequestType createUpdateValueSetStatusRequestType() {
        return new UpdateValueSetStatusRequestType();
    }

    /**
     * Create an instance of {@link MaintainConceptAssociationTypeResponse.Return }
     * 
     */
    public MaintainConceptAssociationTypeResponse.Return createMaintainConceptAssociationTypeResponseReturn() {
        return new MaintainConceptAssociationTypeResponse.Return();
    }

    /**
     * Create an instance of {@link DeleteValueSetConceptMetadataValueResponse.Return }
     * 
     */
    public DeleteValueSetConceptMetadataValueResponse.Return createDeleteValueSetConceptMetadataValueResponseReturn() {
        return new DeleteValueSetConceptMetadataValueResponse.Return();
    }

    /**
     * Create an instance of {@link CreateConceptResponse.Return }
     * 
     */
    public CreateConceptResponse.Return createCreateConceptResponseReturn() {
        return new CreateConceptResponse.Return();
    }

    /**
     * Create an instance of {@link CreateConceptAssociationTypeResponse.Return }
     * 
     */
    public CreateConceptAssociationTypeResponse.Return createCreateConceptAssociationTypeResponseReturn() {
        return new CreateConceptAssociationTypeResponse.Return();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemVersionResponse.Return }
     * 
     */
    public MaintainCodeSystemVersionResponse.Return createMaintainCodeSystemVersionResponseReturn() {
        return new MaintainCodeSystemVersionResponse.Return();
    }

    /**
     * Create an instance of {@link MaintainConceptResponse.Return }
     * 
     */
    public MaintainConceptResponse.Return createMaintainConceptResponseReturn() {
        return new MaintainConceptResponse.Return();
    }

    /**
     * Create an instance of {@link UpdateCodeSystemVersionStatusResponse.Return }
     * 
     */
    public UpdateCodeSystemVersionStatusResponse.Return createUpdateCodeSystemVersionStatusResponseReturn() {
        return new UpdateCodeSystemVersionStatusResponse.Return();
    }

    /**
     * Create an instance of {@link MaintainCodeSystemConceptMetadataValueResponse.Return }
     * 
     */
    public MaintainCodeSystemConceptMetadataValueResponse.Return createMaintainCodeSystemConceptMetadataValueResponseReturn() {
        return new MaintainCodeSystemConceptMetadataValueResponse.Return();
    }

    /**
     * Create an instance of {@link CreateValueSetContentResponse.Return }
     * 
     */
    public CreateValueSetContentResponse.Return createCreateValueSetContentResponseReturn() {
        return new CreateValueSetContentResponse.Return();
    }

    /**
     * Create an instance of {@link MaintainValueSetResponse.Return }
     * 
     */
    public MaintainValueSetResponse.Return createMaintainValueSetResponseReturn() {
        return new MaintainValueSetResponse.Return();
    }

    /**
     * Create an instance of {@link UpdateValueSetStatusResponse.Return }
     * 
     */
    public UpdateValueSetStatusResponse.Return createUpdateValueSetStatusResponseReturn() {
        return new UpdateValueSetStatusResponse.Return();
    }

    /**
     * Create an instance of {@link UpdateConceptValueSetMembershipStatusResponse.Return }
     * 
     */
    public UpdateConceptValueSetMembershipStatusResponse.Return createUpdateConceptValueSetMembershipStatusResponseReturn() {
        return new UpdateConceptValueSetMembershipStatusResponse.Return();
    }

    /**
     * Create an instance of {@link CreateCodeSystemResponse.Return }
     * 
     */
    public CreateCodeSystemResponse.Return createCreateCodeSystemResponseReturn() {
        return new CreateCodeSystemResponse.Return();
    }

    /**
     * Create an instance of {@link CreateValueSetConceptMetadataValueResponse.Return }
     * 
     */
    public CreateValueSetConceptMetadataValueResponse.Return createCreateValueSetConceptMetadataValueResponseReturn() {
        return new CreateValueSetConceptMetadataValueResponse.Return();
    }

    /**
     * Create an instance of {@link MaintainValueSetConceptMetadataValueResponse.Return }
     * 
     */
    public MaintainValueSetConceptMetadataValueResponse.Return createMaintainValueSetConceptMetadataValueResponseReturn() {
        return new MaintainValueSetConceptMetadataValueResponse.Return();
    }

    /**
     * Create an instance of {@link CreateValueSetResponse.Return }
     * 
     */
    public CreateValueSetResponse.Return createCreateValueSetResponseReturn() {
        return new CreateValueSetResponse.Return();
    }

    /**
     * Create an instance of {@link UpdateConceptStatusResponse.Return }
     * 
     */
    public UpdateConceptStatusResponse.Return createUpdateConceptStatusResponseReturn() {
        return new UpdateConceptStatusResponse.Return();
    }

    /**
     * Create an instance of {@link MaintainConceptValueSetMembershipResponse.Return }
     * 
     */
    public MaintainConceptValueSetMembershipResponse.Return createMaintainConceptValueSetMembershipResponseReturn() {
        return new MaintainConceptValueSetMembershipResponse.Return();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateConceptAssociationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateConceptAssociationType")
    public JAXBElement<CreateConceptAssociationType> createCreateConceptAssociationType(CreateConceptAssociationType value) {
        return new JAXBElement<CreateConceptAssociationType>(_CreateConceptAssociationType_QNAME, CreateConceptAssociationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateValueSetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateValueSetResponse")
    public JAXBElement<CreateValueSetResponse> createCreateValueSetResponse(CreateValueSetResponse value) {
        return new JAXBElement<CreateValueSetResponse>(_CreateValueSetResponse_QNAME, CreateValueSetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LicencedUserId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "licencedUserId")
    public JAXBElement<LicencedUserId> createLicencedUserId(LicencedUserId value) {
        return new JAXBElement<LicencedUserId>(_LicencedUserId_QNAME, LicencedUserId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssociationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "associationType")
    public JAXBElement<AssociationType> createAssociationType(AssociationType value) {
        return new JAXBElement<AssociationType>(_AssociationType_QNAME, AssociationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemVersionEntityMembership }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystemVersionEntityMembership")
    public JAXBElement<CodeSystemVersionEntityMembership> createCodeSystemVersionEntityMembership(CodeSystemVersionEntityMembership value) {
        return new JAXBElement<CodeSystemVersionEntityMembership>(_CodeSystemVersionEntityMembership_QNAME, CodeSystemVersionEntityMembership.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateConceptStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "UpdateConceptStatusResponse")
    public JAXBElement<UpdateConceptStatusResponse> createUpdateConceptStatusResponse(UpdateConceptStatusResponse value) {
        return new JAXBElement<UpdateConceptStatusResponse>(_UpdateConceptStatusResponse_QNAME, UpdateConceptStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LicenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "licenceType")
    public JAXBElement<LicenceType> createLicenceType(LicenceType value) {
        return new JAXBElement<LicenceType>(_LicenceType_QNAME, LicenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainConceptValueSetMembershipResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainConceptValueSetMembershipResponse")
    public JAXBElement<MaintainConceptValueSetMembershipResponse> createMaintainConceptValueSetMembershipResponse(MaintainConceptValueSetMembershipResponse value) {
        return new JAXBElement<MaintainConceptValueSetMembershipResponse>(_MaintainConceptValueSetMembershipResponse_QNAME, MaintainConceptValueSetMembershipResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainValueSetConceptMetadataValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainValueSetConceptMetadataValue")
    public JAXBElement<MaintainValueSetConceptMetadataValue> createMaintainValueSetConceptMetadataValue(MaintainValueSetConceptMetadataValue value) {
        return new JAXBElement<MaintainValueSetConceptMetadataValue>(_MaintainValueSetConceptMetadataValue_QNAME, MaintainValueSetConceptMetadataValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateValueSetConceptMetadataValueResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateValueSetConceptMetadataValueResponse")
    public JAXBElement<CreateValueSetConceptMetadataValueResponse> createCreateValueSetConceptMetadataValueResponse(CreateValueSetConceptMetadataValueResponse value) {
        return new JAXBElement<CreateValueSetConceptMetadataValueResponse>(_CreateValueSetConceptMetadataValueResponse_QNAME, CreateValueSetConceptMetadataValueResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemMetadataValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystemMetadataValue")
    public JAXBElement<CodeSystemMetadataValue> createCodeSystemMetadataValue(CodeSystemMetadataValue value) {
        return new JAXBElement<CodeSystemMetadataValue>(_CodeSystemMetadataValue_QNAME, CodeSystemMetadataValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Session }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "session")
    public JAXBElement<Session> createSession(Session value) {
        return new JAXBElement<Session>(_Session_QNAME, Session.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptValueSetMembershipId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "conceptValueSetMembershipId")
    public JAXBElement<ConceptValueSetMembershipId> createConceptValueSetMembershipId(ConceptValueSetMembershipId value) {
        return new JAXBElement<ConceptValueSetMembershipId>(_ConceptValueSetMembershipId_QNAME, ConceptValueSetMembershipId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LicencedUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "licencedUser")
    public JAXBElement<LicencedUser> createLicencedUser(LicencedUser value) {
        return new JAXBElement<LicencedUser>(_LicencedUser_QNAME, LicencedUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveValueSetContent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "RemoveValueSetContent")
    public JAXBElement<RemoveValueSetContent> createRemoveValueSetContent(RemoveValueSetContent value) {
        return new JAXBElement<RemoveValueSetContent>(_RemoveValueSetContent_QNAME, RemoveValueSetContent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainValueSetConceptMetadataValueResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainValueSetConceptMetadataValueResponse")
    public JAXBElement<MaintainValueSetConceptMetadataValueResponse> createMaintainValueSetConceptMetadataValueResponse(MaintainValueSetConceptMetadataValueResponse value) {
        return new JAXBElement<MaintainValueSetConceptMetadataValueResponse>(_MaintainValueSetConceptMetadataValueResponse_QNAME, MaintainValueSetConceptMetadataValueResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteValueSetConceptMetadataValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "DeleteValueSetConceptMetadataValue")
    public JAXBElement<DeleteValueSetConceptMetadataValue> createDeleteValueSetConceptMetadataValue(DeleteValueSetConceptMetadataValue value) {
        return new JAXBElement<DeleteValueSetConceptMetadataValue>(_DeleteValueSetConceptMetadataValue_QNAME, DeleteValueSetConceptMetadataValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueSetMetadataValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "valueSetMetadataValue")
    public JAXBElement<ValueSetMetadataValue> createValueSetMetadataValue(ValueSetMetadataValue value) {
        return new JAXBElement<ValueSetMetadataValue>(_ValueSetMetadataValue_QNAME, ValueSetMetadataValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCodeSystemVersionStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "UpdateCodeSystemVersionStatus")
    public JAXBElement<UpdateCodeSystemVersionStatus> createUpdateCodeSystemVersionStatus(UpdateCodeSystemVersionStatus value) {
        return new JAXBElement<UpdateCodeSystemVersionStatus>(_UpdateCodeSystemVersionStatus_QNAME, UpdateCodeSystemVersionStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveValueSetContentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "RemoveValueSetContentResponse")
    public JAXBElement<RemoveValueSetContentResponse> createRemoveValueSetContentResponse(RemoveValueSetContentResponse value) {
        return new JAXBElement<RemoveValueSetContentResponse>(_RemoveValueSetContentResponse_QNAME, RemoveValueSetContentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemVersionEntityMembershipId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystemVersionEntityMembershipId")
    public JAXBElement<CodeSystemVersionEntityMembershipId> createCodeSystemVersionEntityMembershipId(CodeSystemVersionEntityMembershipId value) {
        return new JAXBElement<CodeSystemVersionEntityMembershipId>(_CodeSystemVersionEntityMembershipId_QNAME, CodeSystemVersionEntityMembershipId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateConceptValueSetMembershipStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "UpdateConceptValueSetMembershipStatus")
    public JAXBElement<UpdateConceptValueSetMembershipStatus> createUpdateConceptValueSetMembershipStatus(UpdateConceptValueSetMembershipStatus value) {
        return new JAXBElement<UpdateConceptValueSetMembershipStatus>(_UpdateConceptValueSetMembershipStatus_QNAME, UpdateConceptValueSetMembershipStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainCodeSystemVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainCodeSystemVersion")
    public JAXBElement<MaintainCodeSystemVersion> createMaintainCodeSystemVersion(MaintainCodeSystemVersion value) {
        return new JAXBElement<MaintainCodeSystemVersion>(_MaintainCodeSystemVersion_QNAME, MaintainCodeSystemVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemEntity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystemEntity")
    public JAXBElement<CodeSystemEntity> createCodeSystemEntity(CodeSystemEntity value) {
        return new JAXBElement<CodeSystemEntity>(_CodeSystemEntity_QNAME, CodeSystemEntity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainConceptAssociationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainConceptAssociationType")
    public JAXBElement<MaintainConceptAssociationType> createMaintainConceptAssociationType(MaintainConceptAssociationType value) {
        return new JAXBElement<MaintainConceptAssociationType>(_MaintainConceptAssociationType_QNAME, MaintainConceptAssociationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystem")
    public JAXBElement<CodeSystem> createCodeSystem(CodeSystem value) {
        return new JAXBElement<CodeSystem>(_CodeSystem_QNAME, CodeSystem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystemVersion")
    public JAXBElement<CodeSystemVersion> createCodeSystemVersion(CodeSystemVersion value) {
        return new JAXBElement<CodeSystemVersion>(_CodeSystemVersion_QNAME, CodeSystemVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateValueSetStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "UpdateValueSetStatus")
    public JAXBElement<UpdateValueSetStatus> createUpdateValueSetStatus(UpdateValueSetStatus value) {
        return new JAXBElement<UpdateValueSetStatus>(_UpdateValueSetStatus_QNAME, UpdateValueSetStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainCodeSystemConceptMetadataValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainCodeSystemConceptMetadataValue")
    public JAXBElement<MaintainCodeSystemConceptMetadataValue> createMaintainCodeSystemConceptMetadataValue(MaintainCodeSystemConceptMetadataValue value) {
        return new JAXBElement<MaintainCodeSystemConceptMetadataValue>(_MaintainCodeSystemConceptMetadataValue_QNAME, MaintainCodeSystemConceptMetadataValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DomainValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "domainValue")
    public JAXBElement<DomainValue> createDomainValue(DomainValue value) {
        return new JAXBElement<DomainValue>(_DomainValue_QNAME, DomainValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "valueSet")
    public JAXBElement<ValueSet> createValueSet(ValueSet value) {
        return new JAXBElement<ValueSet>(_ValueSet_QNAME, ValueSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainCodeSystemVersionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainCodeSystemVersionResponse")
    public JAXBElement<MaintainCodeSystemVersionResponse> createMaintainCodeSystemVersionResponse(MaintainCodeSystemVersionResponse value) {
        return new JAXBElement<MaintainCodeSystemVersionResponse>(_MaintainCodeSystemVersionResponse_QNAME, MaintainCodeSystemVersionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainConceptResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainConceptResponse")
    public JAXBElement<MaintainConceptResponse> createMaintainConceptResponse(MaintainConceptResponse value) {
        return new JAXBElement<MaintainConceptResponse>(_MaintainConceptResponse_QNAME, MaintainConceptResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateValueSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateValueSet")
    public JAXBElement<CreateValueSet> createCreateValueSet(CreateValueSet value) {
        return new JAXBElement<CreateValueSet>(_CreateValueSet_QNAME, CreateValueSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemEntityVersionAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystemEntityVersionAssociation")
    public JAXBElement<CodeSystemEntityVersionAssociation> createCodeSystemEntityVersionAssociation(CodeSystemEntityVersionAssociation value) {
        return new JAXBElement<CodeSystemEntityVersionAssociation>(_CodeSystemEntityVersionAssociation_QNAME, CodeSystemEntityVersionAssociation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainConceptAssociationTypeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainConceptAssociationTypeResponse")
    public JAXBElement<MaintainConceptAssociationTypeResponse> createMaintainConceptAssociationTypeResponse(MaintainConceptAssociationTypeResponse value) {
        return new JAXBElement<MaintainConceptAssociationTypeResponse>(_MaintainConceptAssociationTypeResponse_QNAME, MaintainConceptAssociationTypeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateConceptResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateConceptResponse")
    public JAXBElement<CreateConceptResponse> createCreateConceptResponse(CreateConceptResponse value) {
        return new JAXBElement<CreateConceptResponse>(_CreateConceptResponse_QNAME, CreateConceptResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateConceptStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "UpdateConceptStatus")
    public JAXBElement<UpdateConceptStatus> createUpdateConceptStatus(UpdateConceptStatus value) {
        return new JAXBElement<UpdateConceptStatus>(_UpdateConceptStatus_QNAME, UpdateConceptStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteValueSetConceptMetadataValueResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "DeleteValueSetConceptMetadataValueResponse")
    public JAXBElement<DeleteValueSetConceptMetadataValueResponse> createDeleteValueSetConceptMetadataValueResponse(DeleteValueSetConceptMetadataValueResponse value) {
        return new JAXBElement<DeleteValueSetConceptMetadataValueResponse>(_DeleteValueSetConceptMetadataValueResponse_QNAME, DeleteValueSetConceptMetadataValueResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateConceptAssociationTypeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateConceptAssociationTypeResponse")
    public JAXBElement<CreateConceptAssociationTypeResponse> createCreateConceptAssociationTypeResponse(CreateConceptAssociationTypeResponse value) {
        return new JAXBElement<CreateConceptAssociationTypeResponse>(_CreateConceptAssociationTypeResponse_QNAME, CreateConceptAssociationTypeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptValueSetMembership }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "conceptValueSetMembership")
    public JAXBElement<ConceptValueSetMembership> createConceptValueSetMembership(ConceptValueSetMembership value) {
        return new JAXBElement<ConceptValueSetMembership>(_ConceptValueSetMembership_QNAME, ConceptValueSetMembership.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveTerminologyOrConceptResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "RemoveTerminologyOrConceptResponse")
    public JAXBElement<RemoveTerminologyOrConceptResponse> createRemoveTerminologyOrConceptResponse(RemoveTerminologyOrConceptResponse value) {
        return new JAXBElement<RemoveTerminologyOrConceptResponse>(_RemoveTerminologyOrConceptResponse_QNAME, RemoveTerminologyOrConceptResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TermUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "termUser")
    public JAXBElement<TermUser> createTermUser(TermUser value) {
        return new JAXBElement<TermUser>(_TermUser_QNAME, TermUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateValueSetConceptMetadataValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateValueSetConceptMetadataValue")
    public JAXBElement<CreateValueSetConceptMetadataValue> createCreateValueSetConceptMetadataValue(CreateValueSetConceptMetadataValue value) {
        return new JAXBElement<CreateValueSetConceptMetadataValue>(_CreateValueSetConceptMetadataValue_QNAME, CreateValueSetConceptMetadataValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemConcept }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystemConcept")
    public JAXBElement<CodeSystemConcept> createCodeSystemConcept(CodeSystemConcept value) {
        return new JAXBElement<CodeSystemConcept>(_CodeSystemConcept_QNAME, CodeSystemConcept.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateConceptValueSetMembershipStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "UpdateConceptValueSetMembershipStatusResponse")
    public JAXBElement<UpdateConceptValueSetMembershipStatusResponse> createUpdateConceptValueSetMembershipStatusResponse(UpdateConceptValueSetMembershipStatusResponse value) {
        return new JAXBElement<UpdateConceptValueSetMembershipStatusResponse>(_UpdateConceptValueSetMembershipStatusResponse_QNAME, UpdateConceptValueSetMembershipStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateValueSetStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "UpdateValueSetStatusResponse")
    public JAXBElement<UpdateValueSetStatusResponse> createUpdateValueSetStatusResponse(UpdateValueSetStatusResponse value) {
        return new JAXBElement<UpdateValueSetStatusResponse>(_UpdateValueSetStatusResponse_QNAME, UpdateValueSetStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainConceptValueSetMembership }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainConceptValueSetMembership")
    public JAXBElement<MaintainConceptValueSetMembership> createMaintainConceptValueSetMembership(MaintainConceptValueSetMembership value) {
        return new JAXBElement<MaintainConceptValueSetMembership>(_MaintainConceptValueSetMembership_QNAME, MaintainConceptValueSetMembership.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemEntityVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystemEntityVersion")
    public JAXBElement<CodeSystemEntityVersion> createCodeSystemEntityVersion(CodeSystemEntityVersion value) {
        return new JAXBElement<CodeSystemEntityVersion>(_CodeSystemEntityVersion_QNAME, CodeSystemEntityVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MetadataParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "metadataParameter")
    public JAXBElement<MetadataParameter> createMetadataParameter(MetadataParameter value) {
        return new JAXBElement<MetadataParameter>(_MetadataParameter_QNAME, MetadataParameter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCodeSystemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateCodeSystemResponse")
    public JAXBElement<CreateCodeSystemResponse> createCreateCodeSystemResponse(CreateCodeSystemResponse value) {
        return new JAXBElement<CreateCodeSystemResponse>(_CreateCodeSystemResponse_QNAME, CreateCodeSystemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueSetVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "valueSetVersion")
    public JAXBElement<ValueSetVersion> createValueSetVersion(ValueSetVersion value) {
        return new JAXBElement<ValueSetVersion>(_ValueSetVersion_QNAME, ValueSetVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCodeSystemVersionStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "UpdateCodeSystemVersionStatusResponse")
    public JAXBElement<UpdateCodeSystemVersionStatusResponse> createUpdateCodeSystemVersionStatusResponse(UpdateCodeSystemVersionStatusResponse value) {
        return new JAXBElement<UpdateCodeSystemVersionStatusResponse>(_UpdateCodeSystemVersionStatusResponse_QNAME, UpdateCodeSystemVersionStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainCodeSystemConceptMetadataValueResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainCodeSystemConceptMetadataValueResponse")
    public JAXBElement<MaintainCodeSystemConceptMetadataValueResponse> createMaintainCodeSystemConceptMetadataValueResponse(MaintainCodeSystemConceptMetadataValueResponse value) {
        return new JAXBElement<MaintainCodeSystemConceptMetadataValueResponse>(_MaintainCodeSystemConceptMetadataValueResponse_QNAME, MaintainCodeSystemConceptMetadataValueResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainConcept }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainConcept")
    public JAXBElement<MaintainConcept> createMaintainConcept(MaintainConcept value) {
        return new JAXBElement<MaintainConcept>(_MaintainConcept_QNAME, MaintainConcept.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateValueSetContent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateValueSetContent")
    public JAXBElement<CreateValueSetContent> createCreateValueSetContent(CreateValueSetContent value) {
        return new JAXBElement<CreateValueSetContent>(_CreateValueSetContent_QNAME, CreateValueSetContent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Domain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "domain")
    public JAXBElement<Domain> createDomain(Domain value) {
        return new JAXBElement<Domain>(_Domain_QNAME, Domain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateValueSetContentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateValueSetContentResponse")
    public JAXBElement<CreateValueSetContentResponse> createCreateValueSetContentResponse(CreateValueSetContentResponse value) {
        return new JAXBElement<CreateValueSetContentResponse>(_CreateValueSetContentResponse_QNAME, CreateValueSetContentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveTerminologyOrConcept }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "RemoveTerminologyOrConcept")
    public JAXBElement<RemoveTerminologyOrConcept> createRemoveTerminologyOrConcept(RemoveTerminologyOrConcept value) {
        return new JAXBElement<RemoveTerminologyOrConcept>(_RemoveTerminologyOrConcept_QNAME, RemoveTerminologyOrConcept.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSystemConceptTranslation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "codeSystemConceptTranslation")
    public JAXBElement<CodeSystemConceptTranslation> createCodeSystemConceptTranslation(CodeSystemConceptTranslation value) {
        return new JAXBElement<CodeSystemConceptTranslation>(_CodeSystemConceptTranslation_QNAME, CodeSystemConceptTranslation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCodeSystem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateCodeSystem")
    public JAXBElement<CreateCodeSystem> createCreateCodeSystem(CreateCodeSystem value) {
        return new JAXBElement<CreateCodeSystem>(_CreateCodeSystem_QNAME, CreateCodeSystem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SysParam }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "sysParam")
    public JAXBElement<SysParam> createSysParam(SysParam value) {
        return new JAXBElement<SysParam>(_SysParam_QNAME, SysParam.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainValueSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainValueSet")
    public JAXBElement<MaintainValueSet> createMaintainValueSet(MaintainValueSet value) {
        return new JAXBElement<MaintainValueSet>(_MaintainValueSet_QNAME, MaintainValueSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaintainValueSetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "MaintainValueSetResponse")
    public JAXBElement<MaintainValueSetResponse> createMaintainValueSetResponse(MaintainValueSetResponse value) {
        return new JAXBElement<MaintainValueSetResponse>(_MaintainValueSetResponse_QNAME, MaintainValueSetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateConcept }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://authoring.ws.terminologie.fhdo.de/", name = "CreateConcept")
    public JAXBElement<CreateConcept> createCreateConcept(CreateConcept value) {
        return new JAXBElement<CreateConcept>(_CreateConcept_QNAME, CreateConcept.class, null, value);
    }

}
