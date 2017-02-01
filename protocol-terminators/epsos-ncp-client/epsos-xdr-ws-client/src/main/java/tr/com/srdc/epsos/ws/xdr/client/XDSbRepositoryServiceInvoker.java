/*
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. epsos@srdc.com.tr
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see http://www.gnu.org/licenses/.
 */
package tr.com.srdc.epsos.ws.xdr.client;

import ee.affecto.epsos.util.EventLogClientUtil;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.util.IheConstants;
import eu.epsos.util.xca.XCAConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import tr.com.srdc.epsos.data.model.PatientDemographics;
import tr.com.srdc.epsos.data.model.PatientId;
import tr.com.srdc.epsos.data.model.XdrRequest;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.DateUtil;
import tr.com.srdc.epsos.util.FileUtil;
import eu.epsos.util.xdr.XDRConstants;
import java.util.List;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.epsos.exceptions.DocumentTransformationException;
import eu.epsos.pt.ws.client.xdr.transformation.TMServices;
import eu.epsos.validation.datamodel.cda.CdaModel;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.services.CdaValidationService;

import org.apache.axis2.util.XMLUtils;

/**
 * @author erdem
 *
 */
public class XDSbRepositoryServiceInvoker {

    private static final ObjectFactory ofRim = new ObjectFactory();
    private static Logger LOG = LoggerFactory.getLogger(XDSbRepositoryServiceInvoker.class);

    /*
     * Provide And Register Document Set
     */
    public RegistryResponseType provideAndRegisterDocumentSet(final XdrRequest request, final String countryCode, String docClassCode)
            throws AxisFault, RemoteException {
        RegistryResponseType response;

        /*
         * Stub
         */
        DocumentRecipient_ServiceStub stub;
        String epr = null;

        if (docClassCode.equals(Constants.ED_CLASSCODE)) {
            epr = ConfigurationManagerService.getInstance().getServiceWSE(countryCode.toLowerCase(Locale.ENGLISH), Constants.DispensationService);
        } else if (docClassCode.equals(Constants.CONSENT_CLASSCODE)) {
            epr = ConfigurationManagerService.getInstance().getServiceWSE(countryCode.toLowerCase(Locale.ENGLISH), Constants.ConsentService);
        } else if (docClassCode.equals(Constants.HCER_CLASSCODE)) {
            epr = ConfigurationManagerService.getInstance().getServiceWSE(countryCode.toLowerCase(Locale.ENGLISH), Constants.ConsentService);
        }

        stub = new DocumentRecipient_ServiceStub(epr);
        stub._getServiceClient().getOptions().setTo(new EndpointReference(epr));
        stub.setCountryCode(countryCode);

        // Dummy handler for any mustUnderstand header within server response
        EventLogClientUtil.createDummyMustUnderstandHandler(stub);

        /*
         * ProvideAndRegisterDocumentSetRequestType
         */
        ProvideAndRegisterDocumentSetRequestType prds;

        ihe.iti.xds_b._2007.ObjectFactory ofXds = new ihe.iti.xds_b._2007.ObjectFactory();
        oasis.names.tc.ebxml_regrep.xsd.lcm._3.ObjectFactory ofLcm = new oasis.names.tc.ebxml_regrep.xsd.lcm._3.ObjectFactory();

        prds = ofXds.createProvideAndRegisterDocumentSetRequestType();
        prds.setSubmitObjectsRequest(ofLcm.createSubmitObjectsRequest());
        prds.getSubmitObjectsRequest().setRegistryObjectList(ofRim.createRegistryObjectListType());

        /* XDS Document */
        String uuid = Constants.UUID_PREFIX + UUID.randomUUID().toString();
        ExtrinsicObjectType eotXML = makeExtrinsicObject(request, uuid, docClassCode);
        prds.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable().add(ofRim.createExtrinsicObject(eotXML));

        RegistryPackageType rptXML = prepareRegistryPackage(request, docClassCode);
        prds.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable().add(ofRim.createRegistryPackage(rptXML));

        ClassificationType clXML = prepareClassification();
        prds.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable().add(ofRim.createClassification(clXML));

        AssociationType1 astXML = prepareAssociation(uuid);
        prds.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable().add(ofRim.createAssociation(astXML));

        /* XDR Document */
        ProvideAndRegisterDocumentSetRequestType.Document xdrDocument;
        xdrDocument = new ProvideAndRegisterDocumentSetRequestType.Document();
        xdrDocument.setId(uuid);

        byte[] cdaBytes = null;
        CdaValidationService cdaValidationService = CdaValidationService.getInstance();
        try {
            cdaBytes = request.getCda().getBytes(FileUtil.UTF_8);

            /* Validate CDA epSOS Friendly */
            cdaValidationService.validateModel(
                    XMLUtils.toOM(eu.epsos.pt.transformation.TMServices.byteToDocument(cdaBytes).getDocumentElement()).toString(),
                    CdaModel.obtainCdaModel(docClassCode, false),
                    NcpSide.NCP_B);

        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        } catch (DocumentTransformationException ex) {
            LOG.error(null, ex);
        } catch (Exception ex) {
            LOG.error(null, ex);
        }
        try {
            byte[] transformedCda = TMServices.transformDocument(cdaBytes);
            xdrDocument.setValue(transformedCda);
            
            /* Validate CDA epSOS Pivot */
            cdaValidationService.validateModel(
                    XMLUtils.toOM(eu.epsos.pt.transformation.TMServices.byteToDocument(transformedCda).getDocumentElement()).toString(),
                    CdaModel.obtainCdaModel(docClassCode, true),
                    NcpSide.NCP_B);
        } catch (DocumentTransformationException ex) {
            LOG.error(ex.getLocalizedMessage(), ex);
            xdrDocument.setValue(cdaBytes);
        } catch (Exception ex) {
            LOG.error(null, ex);
        }
        prds.getDocument().add(xdrDocument);

        response = stub.documentRecipient_ProvideAndRegisterDocumentSetB(prds, request.getIdAssertion(), request.getTrcAssertion());

        return response;
    }

    /*
     * Private Methods
     */
    private SlotType1 makeSlot(String name, String value) {
        SlotType1 sl = ofRim.createSlotType1();
        sl.setName(name);
        sl.setValueList(ofRim.createValueListType());
        sl.getValueList().getValue().add(value);
        return sl;
    }

    private ClassificationType makeClassification(String classificationScheme, String classifiedObject, String nodeRepresentation, String value, String name) {
        String uuid = Constants.UUID_PREFIX + UUID.randomUUID().toString();
        ClassificationType cl = ofRim.createClassificationType();
        cl.setId(uuid);
        cl.setNodeRepresentation(nodeRepresentation);
        cl.setClassificationScheme(classificationScheme);
        cl.setClassifiedObject(classifiedObject);
        cl.getSlot().add(makeSlot("codingScheme", value));

        cl.setName(ofRim.createInternationalStringType());
        cl.getName().getLocalizedString().add(ofRim.createLocalizedStringType());
        cl.getName().getLocalizedString().get(0).setValue(name);
        return cl;
    }

    private ClassificationType makeClassification0(String classificationScheme, String classifiedObject, String nodeRepresentation) {
        String uuid = Constants.UUID_PREFIX + UUID.randomUUID().toString();
        ClassificationType cl = ofRim.createClassificationType();
        cl.setId(uuid);
        cl.setNodeRepresentation(nodeRepresentation);
        cl.setClassificationScheme(classificationScheme);
        cl.setClassifiedObject(classifiedObject);
        return cl;
    }

    private InternationalStringType makeInternationalString(String value) {
        InternationalStringType internationalString = new InternationalStringType();
        internationalString.getLocalizedString().add(ofRim.createLocalizedStringType());
        internationalString.getLocalizedString().get(0).setValue(value);
        return internationalString;
    }

    private ExternalIdentifierType makeExternalIdentifier(String identificationScheme, String registryObject, String value, String name) {
        String uuid = Constants.UUID_PREFIX + UUID.randomUUID().toString();
        ExternalIdentifierType ex = ofRim.createExternalIdentifierType();
        ex.setId(uuid);
        ex.setIdentificationScheme(identificationScheme);
        ex.setObjectType(XDRConstants.REGREP_EXT_IDENT);
        ex.setRegistryObject(registryObject);
        ex.setValue(value);

        ex.setName(ofRim.createInternationalStringType());
        ex.getName().getLocalizedString().add(ofRim.createLocalizedStringType());
        ex.getName().getLocalizedString().get(0).setValue(name);
        return ex;
    }

    private ExtrinsicObjectType makeExtrinsicObject(XdrRequest request, String uuid, String docClassCode) {
        return makeExtrinsicObject(request, uuid, docClassCode, Boolean.FALSE);
    }

    // TODO A.R. isPDF unfinished...
    // revised by Luis Pinto @ 2012-08-24
    private ExtrinsicObjectType makeExtrinsicObject(XdrRequest request, String uuid, String docClassCode, Boolean isPDF) {
        ExtrinsicObjectType result = ofRim.createExtrinsicObjectType();
        PatientDemographics patient = request.getPatient();
        PatientId patientId = patient.getIdList().get(0);

        /*
         * Attribute
         */

        result.setId(uuid); // Id
        result.setMimeType(Constants.MIME_TYPE); // mimeType
        result.setObjectType(XCAConstants.XDS_DOC_ENTRY_CLASSIFICATION_NODE); // objectType
        result.setStatus(IheConstants.REGREP_STATUSTYPE_APPROVED); // Status

        /* rim:Slot */
        String now = DateUtil.getDateByDateFormat(XDRConstants.EXTRINSIC_OBJECT.DATE_FORMAT, new Date());
        result.getSlot().add(makeSlot(XDRConstants.EXTRINSIC_OBJECT.CREATION_TIME, now)); // creationTime
        result.getSlot().add(makeSlot(XDRConstants.EXTRINSIC_OBJECT.LANGUAGE_CODE_STR, XDRConstants.EXTRINSIC_OBJECT.LANGUAGE_CODE_VALUE)); // LanguageCode
        result.getSlot().add(makeSlot(XDRConstants.EXTRINSIC_OBJECT.SOURCE_PATIENT_ID, patientId.toString())); // Source Patient Id

        /*
         * Classification
         */

        // Healthcare facility code
        result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.HEALTHCAREFACILITY_CODE_SCHEME,
                uuid,
                request.getCountryCode(),
                XDRConstants.EXTRINSIC_OBJECT.HEALTHCAREFACILITY_CODE_VALUE,
                request.getCountryName()));

        // Practice Setting code
        result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.PRACTICE_SETTING_CODE_SCHEME,
                uuid,
                Constants.NOT_USED_FIELD,
                XDRConstants.EXTRINSIC_OBJECT.PRACTICE_SETTING_CODE_NODEREPR,
                Constants.NOT_USED_FIELD));

        // Confidentiality Code
        result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.CONFIDENTIALITY_CODE_SCHEME,
                uuid,
                XDRConstants.EXTRINSIC_OBJECT.CONFIDENTIALITY_CODE_NODEREPR,
                XDRConstants.EXTRINSIC_OBJECT.CONFIDENTIALITY_CODE_VALUE,
                XDRConstants.EXTRINSIC_OBJECT.CONFIDENTIALITY_CODE_STR));

        // Class code
        if (docClassCode.equals(Constants.CONSENT_CLASSCODE)) {
            result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.CLASS_CODE_SCHEME,
                    uuid,
                    Constants.CONSENT_CLASSCODE,
                    XDRConstants.EXTRINSIC_OBJECT.CLASS_CODE_VALUE,
                    XDRConstants.EXTRINSIC_OBJECT.CLASS_CODE_CONS_STR));
        } else if (docClassCode.equals(Constants.ED_CLASSCODE)) {
            result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.CLASS_CODE_SCHEME,
                    uuid,
                    Constants.ED_CLASSCODE,
                    XDRConstants.EXTRINSIC_OBJECT.CLASS_CODE_VALUE,
                    XDRConstants.EXTRINSIC_OBJECT.CLASS_CODE_ED_STR));
        } else if (docClassCode.equals(Constants.HCER_CLASSCODE)) {
            result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.CLASS_CODE_SCHEME,
                    uuid,
                    Constants.HCER_CLASSCODE,
                    XDRConstants.EXTRINSIC_OBJECT.CLASS_CODE_VALUE,
                    XDRConstants.EXTRINSIC_OBJECT.CLASS_CODE_HCER_STR));

        }

        // FormatCode
        if (docClassCode.equals(Constants.CONSENT_CLASSCODE)) {
            result.getClassification().add(makeClassification(
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.FORMAT_CODE_SCHEME,
                    uuid,
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.Consent.NotScannedDocument.NODE_REPRESENTATION,
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.Consent.NotScannedDocument.CODING_SCHEME,
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.Consent.NotScannedDocument.DISPLAY_NAME));
        } else if (docClassCode.equals(Constants.ED_CLASSCODE)) {
            result.getClassification().add(makeClassification(
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.FORMAT_CODE_SCHEME,
                    uuid,
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.EDispensation.EpsosPivotCoded.NODE_REPRESENTATION,
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.EDispensation.EpsosPivotCoded.CODING_SCHEME,
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.EDispensation.EpsosPivotCoded.DISPLAY_NAME));
        } else if (docClassCode.equals(Constants.HCER_CLASSCODE)) {
            result.getClassification().add(makeClassification(
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.FORMAT_CODE_SCHEME,
                    uuid,
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.Hcer.EpsosPivotCoded.NODE_REPRESENTATION,
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.Hcer.EpsosPivotCoded.CODING_SCHEME,
                    XDRConstants.EXTRINSIC_OBJECT.FormatCode.Hcer.EpsosPivotCoded.DISPLAY_NAME));
        }


        if (docClassCode.equals(Constants.CONSENT_CLASSCODE)) {
            result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.EVENT_CODE_SCHEME,
                    uuid,
                    getConsentOptCode(request.getCda()),
                    XDRConstants.EXTRINSIC_OBJECT.EVENT_CODING_SCHEME,
                    getConsentOptName(request.getCda())));
        }
        /*
         * External Identifiers
         */

        // XDSDocumentEntry.PatientId
        result.getExternalIdentifier().add(makeExternalIdentifier(XDRConstants.EXTRINSIC_OBJECT.XDSDOCENTRY_PATID_SCHEME,
                uuid, patientId.toString(), XDRConstants.EXTRINSIC_OBJECT.XDSDOCENTRY_PATID_STR));

        /* XDSDocument.EntryUUID */
        if (docClassCode.equals(Constants.CONSENT_CLASSCODE)) {
            // TODO missing XDSDocument.EntryUUID for Consent
        }


        /* XDSDocument.UniqueId */
        result.getExternalIdentifier().add(makeExternalIdentifier(XDRConstants.EXTRINSIC_OBJECT.XDSDOC_UNIQUEID_SCHEME,
                uuid, request.getCdaId(), XDRConstants.EXTRINSIC_OBJECT.XDSDOC_UNIQUEID_STR));

        /*
         * Other elements not defined in 3.4.2
         */

        //eot.setHome(Constants.HOME_COMM_ID);
        //eot.setLid(uuid);

        // Version Info - Marcelo: Commented, since IHE simulator does not supports it, and the message is still valid at EVS.
        //result.setVersionInfo(ofRim.createVersionInfoType());
        //result.getVersionInfo().setVersionName(XDRConstants.EXTRINSIC_OBJECT.VERSION_INFO);

        // sourcePatientInfo
        SlotType1 slId = makeSlot(XDRConstants.EXTRINSIC_OBJECT.SRC_PATIENT_INFO_STR, "PID-3|" + patientId.toString());
        slId.getValueList().getValue().add("PID-5|" + patient.getFamilyName() + "^" + patient.getGivenName());
        slId.getValueList().getValue().add("PID-7|" + new SimpleDateFormat("yyyyMMddkkmmss.SSSZZZZ", Locale.ENGLISH).format(patient.getBirthDate()));
        result.getSlot().add(slId);

        // Type code (not written in 3.4.2)
        if (docClassCode.equals(Constants.CONSENT_CLASSCODE)) {
            result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.TypeCode.TYPE_CODE_SCHEME,
                    uuid,
                    XDRConstants.EXTRINSIC_OBJECT.TypeCode.Consent.NODE_REPRESENTATION,
                    XDRConstants.EXTRINSIC_OBJECT.TypeCode.Consent.CODING_SCHEME,
                    XDRConstants.EXTRINSIC_OBJECT.TypeCode.Consent.DISPLAY_NAME));
        } else if (docClassCode.equals(Constants.ED_CLASSCODE)) {
            result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.TypeCode.TYPE_CODE_SCHEME,
                    uuid,
                    XDRConstants.EXTRINSIC_OBJECT.TypeCode.EDispensation.NODE_REPRESENTATION,
                    XDRConstants.EXTRINSIC_OBJECT.TypeCode.EDispensation.CODING_SCHEME,
                    XDRConstants.EXTRINSIC_OBJECT.TypeCode.EDispensation.DISPLAY_NAME));
        } else if (docClassCode.equals(Constants.HCER_CLASSCODE)) {
            result.getClassification().add(makeClassification(XDRConstants.EXTRINSIC_OBJECT.TypeCode.TYPE_CODE_SCHEME,
                    uuid,
                    XDRConstants.EXTRINSIC_OBJECT.TypeCode.Hcer.NODE_REPRESENTATION,
                    XDRConstants.EXTRINSIC_OBJECT.TypeCode.Hcer.CODING_SCHEME,
                    XDRConstants.EXTRINSIC_OBJECT.TypeCode.Hcer.DISPLAY_NAME));
        }
        return result;
    }

    private RegistryPackageType prepareRegistryPackage(XdrRequest request, String docClassCode) {
        RegistryPackageType rpt = ofRim.createRegistryPackageType();

        PatientId patientId = request.getPatient().getIdList().get(0);

        rpt.setId(XDRConstants.REGISTRY_PACKAGE.SUBMISSION_SET_STR);
        rpt.setObjectType(XDRConstants.REGISTRY_PACKAGE.OBJECT_TYPE_UUID);

        rpt.getSlot().add(makeSlot(XDRConstants.REGISTRY_PACKAGE.SUBMISSION_TIME_STR, DateUtil.getDateByDateFormat(XDRConstants.REGISTRY_PACKAGE.SUBMISSION_TIME_FORMAT)));
        rpt.setName(makeInternationalString(getNameFromClassCode(docClassCode)));
        rpt.setDescription(makeInternationalString(getDescrFromClassCode(docClassCode)));

        ClassificationType classification;
        classification = makeClassification0(XDRConstants.REGISTRY_PACKAGE.AUTHOR_CLASSIFICATION_UUID, XDRConstants.REGISTRY_PACKAGE.SUBMISSION_SET_STR, "");
        classification.getSlot().add(makeSlot(XDRConstants.REGISTRY_PACKAGE.AUTHOR_INSTITUTION_STR, getAuthorInstitution(request)));
        classification.getSlot().add(makeSlot(XDRConstants.REGISTRY_PACKAGE.AUTHOR_PERSON_STR, getAuthorPerson(request)));
        rpt.getClassification().add(classification);

        rpt.getClassification().add(makeClassification(XDRConstants.REGISTRY_PACKAGE.CODING_SCHEME_UUID,
                XDRConstants.REGISTRY_PACKAGE.SUBMISSION_SET_STR,
                docClassCode,
                XDRConstants.REGISTRY_PACKAGE.CODING_SCHEME_VALUE,
                getSbmsSetFromClassCode(docClassCode)));

        rpt.getExternalIdentifier().add(makeExternalIdentifier(XDRConstants.REGISTRY_PACKAGE.XDSSUBMSET_UNIQUEID_SCHEME,
                XDRConstants.REGISTRY_PACKAGE.SUBMISSION_SET_STR,
                request.getCdaId(),
                XDRConstants.REGISTRY_PACKAGE.XDSSUBMSET_UNIQUEID_STR));

        rpt.getExternalIdentifier().add(makeExternalIdentifier(XDRConstants.REGISTRY_PACKAGE.XDSSUBMSET_PATIENTID_SCHEME,
                XDRConstants.REGISTRY_PACKAGE.SUBMISSION_SET_STR,
                patientId.toString(),
                XDRConstants.REGISTRY_PACKAGE.XDSSUBMSET_PATIENTID_STR));

        // Marcelo: The XDSSubmissionSet.SourceId value should be the OID of the sender (e.g. HCID), according to ITI TF-3, Table 4.1-6, but not all OID prefixes are supported in IHE validator;
        rpt.getExternalIdentifier().add(makeExternalIdentifier(XDRConstants.REGISTRY_PACKAGE.XDSSUBMSET_SOURCEID_SCHEME,
                XDRConstants.REGISTRY_PACKAGE.SUBMISSION_SET_STR,
                XDRConstants.REGISTRY_PACKAGE.XDSSUBMSET_SOURCEID_VALUE,
                XDRConstants.REGISTRY_PACKAGE.XDSSUBMSET_SOURCEID_STR));
        return rpt;
    }

    private ClassificationType prepareClassification() {
        ClassificationType classification = ofRim.createClassificationType();
        String uuid = Constants.UUID_PREFIX + UUID.randomUUID().toString();
        classification.setId(uuid);
        classification.setClassificationNode(XDRConstants.SUBMISSION_SET_CLASSIFICATION.CLASSIFICATION_NODE_UUID);
        classification.setClassifiedObject(XDRConstants.REGISTRY_PACKAGE.SUBMISSION_SET_STR);

        return classification;
    }

    private AssociationType1 prepareAssociation(String targetObject) {
        String uuid = Constants.UUID_PREFIX + UUID.randomUUID().toString();

        AssociationType1 ast = ofRim.createAssociationType1();
        ast.setId(uuid);
        ast.setAssociationType(XDRConstants.REGREP_HAS_MEMBER);
        ast.setSourceObject(XDRConstants.REGISTRY_PACKAGE.SUBMISSION_SET_STR);
        ast.setTargetObject(targetObject);
        ast.getSlot().add(makeSlot(XDRConstants.SUBMISSION_SET_STATUS_STR, XDRConstants.ORIGINAL_STR));

        return ast;
    }

    /**
     * Obtains the AuthorInstitution information, namely Name and Id from the
     * assertion. And builds an HL7 V2.5 representation of the information.
     *
     * @param xdrRequest an XDR Request containing the assertion.
     * @return an HL7 V2.5 representation of the obtained information.
     */
    private String getAuthorInstitution(final XdrRequest xdrRequest) {
        String result = null;

        String organization = "Hospital";
        String organizationId = "1.2.3.4.5.6.7.8.9.1789.45";

        Assertion hcpAssertion = null;
        List<AttributeStatement> attrStatements = null;
        List<Attribute> attrs;

        hcpAssertion = xdrRequest.getIdAssertion();
        attrStatements = hcpAssertion.getAttributeStatements();

        if (attrStatements.size() != 1) {
            return null;
        }

        attrs = attrStatements.get(0).getAttributes();

        for (Attribute attr : attrs) {
            if (attr.getName().equals("urn:oasis:names:tc:xspa:1.0:subject:organization")) {
                organization = attr.getAttributeValues().get(0).getDOM().getTextContent();
            }
            if (attr.getName().equals("urn:oasis:names:tc:xspa:1.0:subject:organization-id")) {
                organizationId = attr.getAttributeValues().get(0).getDOM().getTextContent();
            }
        }

        if (organizationId.startsWith("urn:oid:")) {
            result = organization + "^^^^^^^^^" + organizationId.split(":")[2];
        } else {
            result = organization + "^^^^^^^^^" + organizationId;
        }

        return result;
    }

    /**
     * Obtains the AuthorPerson information, namely the Author Identifier and
     * Assigning AuthorityId . Then builds an HL7 V2.5 representation of the
     * information.
     *
     * @param xdrRequest an XDR Request containing the assertion.
     * @return an HL7 V2.5 representation of the obtained information.
     */
    private String getAuthorPerson(final XdrRequest xdrRequest) {

        String result = null;

        String authorIdentifier = "Dr. Doctor";
        String assigningAuthorityId = "1.2.3.4.5.6.7.8.9.1789.45";

        Assertion hcpAssertion = null;
        List<AttributeStatement> attrStatements = null;
        List<Attribute> attrs;

        hcpAssertion = xdrRequest.getIdAssertion();
        attrStatements = hcpAssertion.getAttributeStatements();

        if (attrStatements.size() != 1) {
            return null;
        }

        attrs = attrStatements.get(0).getAttributes();

        for (Attribute attr : attrs) {
            if (attr.getName().equals("urn:oasis:names:tc:xacml:1.0:subject:subject-id")) {
                authorIdentifier = attr.getAttributeValues().get(0).getDOM().getTextContent();
            }
            if (attr.getName().equals("urn:oasis:names:tc:xspa:1.0:subject:organization-id")) {
                assigningAuthorityId = attr.getAttributeValues().get(0).getDOM().getTextContent();
            }
        }

        if (assigningAuthorityId.startsWith("urn:oid:")) {
            result = authorIdentifier + "&amp;" + assigningAuthorityId.split(":")[2] + "&amp;ISO";
        } else {
            result = authorIdentifier + "&amp;" + assigningAuthorityId + "&amp;ISO";
        }

        return result;
    }

    /**
     * This method will determine which EventCode (Name) is present in the
     * Consent Document.
     *
     * @param document the consent CDA
     * @return the EventCode
     */
    private String getConsentOptName(String document) {
        if (document.contains(XDRConstants.EXTRINSIC_OBJECT.EVENT_CODE_NODE_REPRESENTATION_OPT_OUT)) {
            return XDRConstants.EXTRINSIC_OBJECT.EVENT_CODE_NODE_NAME_OPT_OUT;
        } else if (document.contains(XDRConstants.EXTRINSIC_OBJECT.EVENT_CODE_NODE_REPRESENTATION_OPT_IN)) {
            return XDRConstants.EXTRINSIC_OBJECT.EVENT_CODE_NODE_NAME_OPT_IN;
        } else {
            LOG.error("Event Code not found in consent document!");
            return null;
        }

    }

    /**
     * This method will determine which EventCode (Code) is present in the
     * Consent Document.
     *
     * @param document the consent CDA
     * @return the EventCode
     */
    private String getConsentOptCode(String document) {
        if (document.contains(XDRConstants.EXTRINSIC_OBJECT.EVENT_CODE_NODE_REPRESENTATION_OPT_OUT)) {
            return XDRConstants.EXTRINSIC_OBJECT.EVENT_CODE_NODE_REPRESENTATION_OPT_OUT;
        } else if (document.contains(XDRConstants.EXTRINSIC_OBJECT.EVENT_CODE_NODE_REPRESENTATION_OPT_IN)) {
            return XDRConstants.EXTRINSIC_OBJECT.EVENT_CODE_NODE_REPRESENTATION_OPT_IN;
        } else {
            LOG.error("Event Code not found in consent document!");
            return null;
        }

    }

    private String getNameFromClassCode(String classCode) {
        if (classCode.equals(Constants.HCER_CLASSCODE)) {
            return XDRConstants.REGISTRY_PACKAGE.NAME_HCER;
        } else if (classCode.equals(Constants.CONSENT_CLASSCODE)) {
            return XDRConstants.REGISTRY_PACKAGE.NAME_CONSENT;
        }
        if (classCode.equals(Constants.ED_CLASSCODE)) {
            return XDRConstants.REGISTRY_PACKAGE.NAME_ED;
        } else {
            LOG.error("Class code does not have a matching name!");
            return null;
        }
    }

    private String getDescrFromClassCode(String classCode) {
        if (classCode.equals(Constants.HCER_CLASSCODE)) {
            return XDRConstants.REGISTRY_PACKAGE.DESCRIPTION_HCER;
        } else if (classCode.equals(Constants.CONSENT_CLASSCODE)) {
            return XDRConstants.REGISTRY_PACKAGE.DESCRIPTION_CONSENT;
        }
        if (classCode.equals(Constants.ED_CLASSCODE)) {
            return XDRConstants.REGISTRY_PACKAGE.DESCRIPTION_ED;
        } else {
            LOG.error("Class code does not have a matching description!");
            return null;
        }
    }

    private String getSbmsSetFromClassCode(String classCode) {
        if (classCode.equals(Constants.HCER_CLASSCODE)) {
            return XDRConstants.REGISTRY_PACKAGE.NAME_HCER;
        } else if (classCode.equals(Constants.CONSENT_CLASSCODE)) {
            return XDRConstants.REGISTRY_PACKAGE.CODING_SCHEME_CONS_STR;
        }
        if (classCode.equals(Constants.ED_CLASSCODE)) {
            return XDRConstants.REGISTRY_PACKAGE.NAME_ED;
        } else {
            LOG.error("Class code does not have a matching submission set designation!");
            return null;
        }
    }
}
