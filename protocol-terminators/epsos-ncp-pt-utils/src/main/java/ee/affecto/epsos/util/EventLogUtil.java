/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 Affecto EE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package ee.affecto.epsos.util;

import epsos.ccd.gnomon.auditmanager.EventActionCode;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.auditmanager.EventType;
import epsos.ccd.gnomon.auditmanager.TransactionName;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.axiom.om.OMElement;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.opensaml.saml2.core.Attribute;
import tr.com.srdc.epsos.util.Constants;

// Common part for client and server logging
// TODO A.R. Should be moved into epsos-util later to avoid duplication
public class EventLogUtil {

    public static void prepareXCPDCommonLog(EventLog eventLog, PRPAIN201305UV02 request, PRPAIN201306UV02 response) {
        // Set Event Identification
        eventLog.setEventType(EventType.epsosIdentificationServiceFindIdentityByTraits);
        eventLog.setEI_TransactionName(TransactionName.epsosIdentificationServiceFindIdentityByTraits);
        eventLog.setEI_EventActionCode(EventActionCode.EXECUTE);

        if (response.getAcknowledgement().get(0).getAcknowledgementDetail().size() > 0) {
            String detail = response.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText().getContent();
            if (detail.startsWith("(")) {
                String code = detail.substring(1, 5);
                if (code.equals("1102")) {
                    eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.TEMPORAL_FAILURE);
                } else {
                    eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.PERMANENT_FAILURE);
                }
            } else {
                eventLog.setEM_PatricipantObjectID("0");
                eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.PERMANENT_FAILURE);
            }
            eventLog.setEM_PatricipantObjectDetail(detail.getBytes());
        } else {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.FULL_SUCCESS);
        }
        // Patient Source is not written, because HCP assurance audit does not include patient mapping information
        // Set Participant Object: Patient Target
        String st2 = "";
        if (response.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().size() > 0) {
            II target_ii = response.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue().get(0);
            if (target_ii.getExtension() != null && target_ii.getRoot() != null) {
                st2 = target_ii.getExtension() + "^^^&" + target_ii.getRoot() + "&ISO";
            }
        }
        eventLog.setPT_PatricipantObjectID(st2);

        // Set Participant Object: Error Message
        if (response.getAcknowledgement().get(0).getAcknowledgementDetail().size() > 0) {
            String error = response.getAcknowledgement().get(0).getAcknowledgementDetail()
                    .get(0).getText().getContent();
            eventLog.setEM_PatricipantObjectID(error);
            eventLog.setEM_PatricipantObjectDetail(error.getBytes());
        }

    }

    public static void prepareXCACommonLogQuery(EventLog eventLog, AdhocQueryRequest request, AdhocQueryResponse response, String classCode) {
        if (classCode.equals(Constants.PS_CLASSCODE)) {
            eventLog.setEventType(EventType.epsosPatientServiceList);
            eventLog.setEI_TransactionName(TransactionName.epsosPatientServiceList);
        } else if (classCode.equals(Constants.EP_CLASSCODE)) {
            eventLog.setEventType(EventType.epsosOrderServiceList);
            eventLog.setEI_TransactionName(TransactionName.epsosOrderServiceList);
        } else if (classCode.equals(Constants.MRO_CLASSCODE)) {
            eventLog.setEventType(EventType.epsosMroList);
            eventLog.setEI_TransactionName(TransactionName.epsosMroServiceList);
        }

        eventLog.setPT_PatricipantObjectID(getDocumentEntryPatientId(request));
        eventLog.setEI_EventActionCode(EventActionCode.READ);

        if (response.getRegistryObjectList() != null) {
            for (int i = 0; i < response.getRegistryObjectList().getIdentifiable().size(); i++) {
                if (!(response.getRegistryObjectList().getIdentifiable().get(i).getValue() instanceof ExtrinsicObjectType)) {
                    continue;
                }
                ExtrinsicObjectType eot = (ExtrinsicObjectType) response.getRegistryObjectList().getIdentifiable().get(i).getValue();
                String documentId = "";
                for (ExternalIdentifierType eit : eot.getExternalIdentifier()) {
                    if (eit.getIdentificationScheme().equals("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab")) {
                        documentId = eit.getValue();
                    }
                }
                // PT-237 add safely the urn:uuid prefix
                eventLog.setET_ObjectID(EventLogClientUtil.appendUrnUuid(Constants.UUID_PREFIX + documentId));
                break;
            }
        }

        if (response.getRegistryObjectList() == null) {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.PERMANENT_FAILURE);
        } else if (response.getRegistryErrorList() == null) {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.FULL_SUCCESS);
        } else {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.TEMPORAL_FAILURE);
        }

        if (response.getRegistryErrorList() != null) {
            RegistryError re = response.getRegistryErrorList().getRegistryError().get(0);
            eventLog.setEM_PatricipantObjectID(re.getErrorCode());
            eventLog.setEM_PatricipantObjectDetail(re.getCodeContext() == null ? null : re.getCodeContext().getBytes());
        }
    }

    public static void prepareXCACommonLogRetrieve(EventLog eventLog, RetrieveDocumentSetRequestType request, RetrieveDocumentSetResponseType response, String classCode) {
        if (classCode.equals(Constants.PS_CLASSCODE)) {
            eventLog.setEventType(EventType.epsosPatientServiceRetrieve);
            eventLog.setEI_TransactionName(TransactionName.epsosPatientServiceRetrieve);
        } else if (classCode.equals(Constants.EP_CLASSCODE)) {
            eventLog.setEventType(EventType.epsosOrderServiceRetrieve);
            eventLog.setEI_TransactionName(TransactionName.epsosOrderServiceRetrieve);
        } else if (classCode.equals(Constants.MRO_CLASSCODE)) {
            eventLog.setEventType(EventType.epsosMroRetrieve);
            eventLog.setEI_TransactionName(TransactionName.epsosMroServiceRetrieve);
        }

        eventLog.setEI_EventActionCode(EventActionCode.READ);

        eventLog.setET_ObjectID(Constants.UUID_PREFIX + request.getDocumentRequest().get(0).getDocumentUniqueId());

        if (response.getDocumentResponse() == null || response.getDocumentResponse().size() == 0 || response.getDocumentResponse().get(0).getDocument() == null) {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.PERMANENT_FAILURE);
        } else if (response.getRegistryResponse().getRegistryErrorList() == null) {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.FULL_SUCCESS);
        } else {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.TEMPORAL_FAILURE);
        }

        if (response.getRegistryResponse().getRegistryErrorList() != null && response.getRegistryResponse().getRegistryErrorList().getRegistryError() != null && response.getRegistryResponse().getRegistryErrorList().getRegistryError().size() > 0) {
            RegistryError re = response.getRegistryResponse().getRegistryErrorList().getRegistryError().get(0);
            // TODO A.R. on TSAM errors currently errorCode=null, codeContext=null - maybe faulty XCA server implementation?
            // What exactly we should log on partial success? Originally was codeContext, but is value OK?
            eventLog.setEM_PatricipantObjectID(re.getErrorCode());
            if (re.getCodeContext() != null) {
                eventLog.setEM_PatricipantObjectDetail(re.getCodeContext().getBytes());
            } else if (re.getValue() != null) {
                eventLog.setEM_PatricipantObjectDetail(re.getValue().getBytes());
            }
        }
    }

    public static void prepareXDRCommonLog(EventLog eventLog, ProvideAndRegisterDocumentSetRequestType request, RegistryErrorList rel) {
        String id = null;
        String classCode = null;
        String eventCode = null;
        String countryCode = null;
        String patientId = null;

        List<JAXBElement<? extends IdentifiableType>> identifList = request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable();
        if (identifList != null) {
            for (JAXBElement<? extends IdentifiableType> identif : identifList) {
                if (!(identif.getValue() instanceof ExtrinsicObjectType)) {
                    continue;
                }
                ExtrinsicObjectType eot = (ExtrinsicObjectType) identif.getValue();
                id = eot.getId();
                for (ClassificationType classif : eot.getClassification()) {
                    if (classif.getClassificationScheme().equals("urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a")) {
                        classCode = classif.getNodeRepresentation();
                    } else if (classif.getClassificationScheme().equals("urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4")) {
                        eventCode = classif.getNodeRepresentation();
                    } else if (classif.getClassificationScheme().equals("urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1")) {
                        countryCode = classif.getNodeRepresentation();
                    }
                }
                for (ExternalIdentifierType externalIdentifier : eot.getExternalIdentifier()) {
                    if (externalIdentifier.getIdentificationScheme().equals("urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427")) {
                        patientId = externalIdentifier.getValue();
                    }
                }
            }
        }
        if (classCode.equals(Constants.CONSENT_CLASSCODE) && eventCode != null) {
            if (eventCode.endsWith(Constants.CONSENT_PUT_SUFFIX)) {
                eventLog.setEventType(EventType.epsosConsentServicePut);
                eventLog.setEI_TransactionName(TransactionName.epsosConsentServicePut);
                eventLog.setEI_EventActionCode(EventActionCode.UPDATE);
            } else if (eventCode.endsWith(Constants.CONSENT_DISCARD_SUFFIX)) {
                eventLog.setEventType(EventType.epsosConsentServiceDiscard);
                eventLog.setEI_TransactionName(TransactionName.epsosConsentServiceDiscard);
                eventLog.setEI_EventActionCode(EventActionCode.DELETE);
            }
        } else if (classCode.equals(Constants.ED_CLASSCODE)) {
            eventLog.setEventType(EventType.epsosDispensationServiceInitialize);
            eventLog.setEI_TransactionName(TransactionName.epsosDispensationServiceInitialize);
            eventLog.setEI_EventActionCode(EventActionCode.UPDATE);
        }
        // TODO: support dispensation revoke operation
        // TODO: Only id of one document (because of audit manager limitation) is included - extend to as many as needed. 
        eventLog.setET_ObjectID(Constants.UUID_PREFIX + request.getDocument().get(0).getId());

        if (rel == null || rel.getRegistryError() == null || rel.getRegistryError().size() == 0) {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.FULL_SUCCESS);
        } else {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.PERMANENT_FAILURE);
            RegistryError re = rel.getRegistryError().get(0);
            eventLog.setEM_PatricipantObjectID(re.getErrorCode());
            eventLog.setEM_PatricipantObjectDetail(re.getCodeContext().getBytes());
        }


    }

    public static String getMessageID(org.apache.axiom.soap.SOAPEnvelope envelope) {
        Iterator<OMElement> it = envelope.getHeader().getChildrenWithName(new QName("http://www.w3.org/2005/08/addressing", "MessageID"));
        if (it.hasNext()) {
            return it.next().getText();
        } else {
            return "NA";
        }
    }

    public static String getAttributeValue(Attribute attribute) {
        String attributeValue = null;
        if (attribute.getAttributeValues().size() > 0) {
            attributeValue = attribute.getAttributeValues().get(0).getDOM().getTextContent();
        }
        return attributeValue;

    }
    
    /**
     * Extracts the XDS patient ID from the XCA query
     */
    private static String getDocumentEntryPatientId(AdhocQueryRequest request) {
        for (SlotType1 sl : request.getAdhocQuery().getSlot()) {
            if (sl.getName().equals("$XDSDocumentEntryPatientId")) {
                String patientId = sl.getValueList().getValue().get(0);
                patientId = patientId.substring(1, patientId.length() - 1);
                return patientId;
            }
        }
        return "$XDSDocumentEntryPatientId Not Found!";
    }

    private EventLogUtil() {
    }
}
