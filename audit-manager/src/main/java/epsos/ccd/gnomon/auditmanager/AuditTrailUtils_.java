/**
 * *Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *//*
    * To change this template, choose Tools | Templates
	* and open the template in the editor.
	*/
package epsos.ccd.gnomon.auditmanager;

import epsos.ccd.gnomon.utils.SecurityMgr;
import epsos.ccd.gnomon.utils.Utils;
import eu.epsos.util.audit.AuditLogSerializer;
import eu.epsos.validation.datamodel.audit.AuditModel;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.services.AuditValidationService;
import net.RFC3881.*;
import net.RFC3881.AuditMessage.ActiveParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides methods for constructing the aufit message and for
 * sending the syslog message to the repository
 *
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun Provides methods for creating AuditMessage
 *          instances of the spSOS defined services, as for constructing and
 *          sending the syslog message to the repository
 */
public enum AuditTrailUtils_ {

    INSTANCE;
    private static final Logger log = LoggerFactory.getLogger(AuditTrailUtils.class);

    public static AuditTrailUtils_ getInstance() {
        return INSTANCE;
    }

    /**
     * Method to create the audit message to be passed to the syslog client
     *
     * @param eventLog
     * @return the Audit Message object
     */
    public AuditMessage createAuditMessage(EventLog eventLog) {
        AuditMessage am = new AuditMessage();
        AuditTrailUtils_ au = AuditTrailUtils_.getInstance();
        if (eventLog.getEventType().equals("epsos-11")) {
            am = au._CreateAuditTrailForIdentificationService(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-21")) {
            am = au._CreateAuditTrailForPatientService(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-22")) {
            am = au._CreateAuditTrailForPatientService(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-31")) {
            am = au._CreateAuditTrailForOrderService(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-32")) {
            am = au._CreateAuditTrailForOrderService(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-41")) {
            am = au._CreateAuditTrailForDispensationService(eventLog, "Initialize");
        }
        if (eventLog.getEventType().equals("epsos-42")) {
            am = au._CreateAuditTrailForDispensationService(eventLog, "Discard");
        }
        if (eventLog.getEventType().equals("epsos-51")) {
            am = au._CreateAuditTrailForConsentService(eventLog, "Put");
        }
        if (eventLog.getEventType().equals("epsos-52")) {
            am = au._CreateAuditTrailForConsentService(eventLog, "Discard");
        }
        if (eventLog.getEventType().equals("epsos-53")) {
            am = au._CreateAuditTrailForConsentService(eventLog, "Pin");
        }
        if (eventLog.getEventType().equals("epsos-91")) {
            am = au._CreateAuditTrailHCPIdentity(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-92")) {
            am = au._CreateAuditTrailTRCA(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-93")) {
            am = au._CreateAuditTrailNCPTrustedServiceList(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-94")) {
            am = au._CreateAuditTrailPivotTranslation(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-cf")) {
            am = au.createAuditTrailForCommunicationFailure(eventLog);
        }
        if (eventLog.getEventType().equals(EventType.epsosPACRetrieve.getCode())) {
            am = au._CreateAuditTrailForPACService(eventLog);
        }
        if (eventLog.getEventType().equals("epsos-96")) {
            am = au._CreateAuditTrailForHCERService(eventLog);
        }
        if (eventLog.getEventType().equals("ITI-38")) {
            am = au._CreateAuditTrailForRequestOfData(eventLog);
        }
        if (eventLog.getEventType().equals("ITI-39")) {
            am = au._CreateAuditTrailForRequestOfData(eventLog);
        }
        if (eventLog.getEventType().equals("ehealth-193")) {
            am = au._CreateAuditTrailForEhealthSMPQuery(eventLog);
        }
        if (eventLog.getEventType().equals("ehealth-194")) {
            am = au._CreateAuditTrailForEhealthSMPPush(eventLog);
        }

        am = AuditTrailUtils.getInstance().addNonRepudiationSection(am, eventLog.getReqM_ParticipantObjectID(),
                eventLog.getReqM_PatricipantObjectDetail(), eventLog.getResM_ParticipantObjectID(),
                eventLog.getResM_PatricipantObjectDetail());

		/* Invoke audit message validation services */
        validateAuditMessage(eventLog, am);

        return am;
    }

    public AuditMessage addNonRepudiationSection(AuditMessage auditm, String ReqM_ParticipantObjectID,
                                                 byte[] ReqM_PatricipantObjectDetail, String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail) {
        AuditMessage am = auditm;

        // Request
        ParticipantObjectIdentificationType poit = new ParticipantObjectIdentificationType();
        poit.setParticipantObjectID(ReqM_ParticipantObjectID);
        poit.setParticipantObjectTypeCode(new Short("4"));
        CodedValueType PS_object = new CodedValueType();
        PS_object.setCode("req");
        PS_object.setCodeSystemName("epSOS Msg");
        PS_object.setDisplayName("Request Message");
        poit.setParticipantObjectIDTypeCode(PS_object);
        if (ReqM_PatricipantObjectDetail != null) {
            TypeValuePairType pod = new TypeValuePairType();
            pod.setType("securityheader");
            pod.setValue(ReqM_PatricipantObjectDetail);
            poit.getParticipantObjectDetail().add(pod);
        }
        am.getParticipantObjectIdentification().add(poit);

        // Response
        ParticipantObjectIdentificationType poit1 = new ParticipantObjectIdentificationType();
        poit1.setParticipantObjectID(ResM_ParticipantObjectID);
        poit1.setParticipantObjectTypeCode(new Short("4"));
        CodedValueType PS_object1 = new CodedValueType();
        PS_object1.setCode("rsp");
        PS_object1.setCodeSystemName("epSOS Msg");
        PS_object1.setDisplayName("Response Message");
        poit1.setParticipantObjectIDTypeCode(PS_object1);
        if (ResM_PatricipantObjectDetail != null) {
            TypeValuePairType pod1 = new TypeValuePairType();
            pod1.setType("securityheader");
            pod1.setValue(ResM_PatricipantObjectDetail);
            poit1.getParticipantObjectDetail().add(pod1);
        }
        am.getParticipantObjectIdentification().add(poit1);

        return am;
    }
    
    /**
     * Constructs an Audit Message for the Patient Privacy Audit schema
     * in eHealth NCP Query
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForEhealthSMPQuery(EventLog eventLog) {
      //TODO
        AuditMessage am = createAuditTrailForEhealthSMPQuery(eventLog);
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), null, "SMP", "eHealth Security", "SignedServiceMetadata");
        return am;
    }
    
    /**
     * Constructs an Audit Message for the Patient Privacy Audit schema
     * in eHealth NCP Push
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForEhealthSMPPush(EventLog eventLog) {
      //TODO
        AuditMessage am = createAuditTrailForEhealthSMPPush(eventLog);
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), null, "SMP", "eHealth Security", "SignedServiceMetadata");
        return am;
    }

    /**
     * Constructs an Audit Message for the epSOS Order Service According schema
     * is HCP Assurcance
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForOrderService(EventLog eventLog) {
        AuditMessage am = createAuditTrailForHCPAssurance(eventLog);
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), new Short("4"), "12", "", new Short("0"));
        return am;
    }

    /**
     * Constructs an Audit Message for HCP Identity According schema is HCP
     * Assurcance
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailHCPIdentity(EventLog eventLog) {
        AuditMessage am = createAuditTrailForHCPIdentity(eventLog);
        // Event Target
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), null, "IdA", "epSOS Security",
                "HCP Identity Assertion");
        return am;
    }

    /**
     * Constructs an Audit Message for NCP Trusted Service List According schema
     * is HCP Assurcance
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailNCPTrustedServiceList(EventLog eventLog) {
        AuditMessage am = createAuditTrailForNCPTrustedServiceList(eventLog);
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), null, "NSL", "epSOS Security",
                "Trusted Service List");
        return am;
    }

    /**
     * Constructs an Audit Message for Pivot Translation According schema is HCP
     * Assurcance
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailPivotTranslation(EventLog eventLog) {
        AuditMessage am = createAuditTrailForPivotTranslation(eventLog);
        // Event Target
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("4"), new Short("5"), "in", "epSOS Tranlation",
                "Input Data");
        addEventTarget(am, eventLog.getET_ObjectID_additional(), new Short("4"), new Short("5"), "out",
                "epSOS Tranlation", "Output Data");
        return am;

    }

    /**
     * Constructs an Audit Message for TRCA According schema is HCP Assurcance
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailTRCA(EventLog eventLog) {
        AuditMessage am = createAuditTrailForTRCA(eventLog);
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), null, "TrcA", "epSOS Security", "TRC Assertion");
        return am;
    }

    /**
     * Constructs an Audit Message for the epSOS Dispensation Service According
     * schema is HCP Assurcance
     *
     * @param eventLog the EventLog object
     * @param action   the action of the service
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForDispensationService(EventLog eventLog, String action) {
        AuditMessage am = createAuditTrailForHCPAssurance(eventLog);
        // Event Target
        if (action.equals("Discard")) {
            addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), new Short("4"), "12", "Discard",
                    new Short("14"));
        } else {
            addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), new Short("4"), "12", "", new Short("0"));
        }

        return am;
    }

    /**
     * Constructs an Audit Message for the epSOS HCER Service According schema
     * is HCP Assurcance
     *
     * @param eventLog the EventLog object
     * @param action   the action of the service
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForHCERService(EventLog eventLog) {
        AuditMessage am = createAuditTrailForHCPAssurance(eventLog);
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), new Short("4"), "12", "", new Short("0"));
        return am;
    }

    /**
     * Constructs an Audit Message for the epSOS Consent Service According
     * schema is HCP Assurcance
     *
     * @param eventLog the EventLog object
     * @param action   the action of the service
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForConsentService(EventLog eventLog, String action) {
        AuditMessage am = createAuditTrailForHCPAssurance(eventLog);
        if (action.equals("Discard")) {
            addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), new Short("4"), "12", action,
                    new Short("14"));
        }

        if (action.equals("Put")) {
            addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), new Short("4"), "12", "Put", new Short("0"));
        }
        if (action.equals("Pin")) {
            addEventTarget(am, eventLog.getET_ObjectID(), new Short("4"), new Short("12"), "PIN", "esSOS Security",
                    "Privacy Information Notice");
        }
        return am;
    }

    /**
     * Constructs an Audit Message for the epSOS Patient Service According
     * schema is Patient Service
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForPatientService(EventLog eventLog) {
        AuditMessage am = createAuditTrailForHCPAssurance(eventLog);
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), new Short("4"), "12", "", new Short("0"));
        return am;
    }

    /**
     * Constructs an Audit Message for the epSOS Identification Service
     * According schema is Mapping Service
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForIdentificationService(EventLog eventLog) {
        // If patient id mapping has occurred (there is a patient source ID),
        // use patient mapping audit scheme
        if (eventLog.getPS_PatricipantObjectID() != null) {
            return createAuditTrailForPatientMapping(eventLog);
        } else {
            return createAuditTrailForHCPAssurance(eventLog);
        }
    }

    /**
     * Constructs an Audit Message for the epSOS Identification Service
     * According schema is Mapping Service
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForPACService(EventLog eventLog) {
        AuditMessage am = createAuditTrailForHCPAssurance(eventLog);
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), new Short("24"), "10", "", new Short("0"));
        return am;
    }

    /**
     * Constructs an Audit Message for the generic Request For Data Scheme
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage _CreateAuditTrailForRequestOfData(EventLog eventLog) {
        AuditMessage am = createAuditTrailForHCPAssurance(eventLog);
        addEventTarget(am, eventLog.getET_ObjectID(), new Short("2"), new Short("24"), "10", "", new Short("0"));
        return am;
    }

    private AuditMessage addAuditSource(AuditMessage am, String auditsource) {
        AuditSourceIdentificationType asit = new AuditSourceIdentificationType();
        asit.setAuditSourceID(auditsource);
        am.getAuditSourceIdentification().add(asit);
        return am;
    }

    private String getMappedEventType(String eventType) {
        IHEEventType mappedEventType = null;
        if (eventType.equals(
                epsos.ccd.gnomon.auditmanager.EventType.epsosIdentificationServiceFindIdentityByTraits.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosIdentificationServiceFindIdentityByTraits.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosCommunicationFailure.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosCommunicationFailure.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosConsentServiceDiscard.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosConsentServiceDiscard.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosConsentServicePin.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosConsentServicePin.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosConsentServicePut.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosConsentServicePut.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosDispensationServiceDiscard.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosDispensationServiceDiscard.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosDispensationServiceInitialize.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosDispensationServiceInitialize.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosHCERPut.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosHCERPut.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosHcpAuthentication.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosHcpAuthentication.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosNCPTrustedServiceList.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosNCPTrustedServiceList.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosOrderServiceList.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosOrderServiceList.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosOrderServiceRetrieve.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosOrderServiceRetrieve.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosPACRetrieve.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosPACRetrieve.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosPatientServiceList.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosPatientServiceList.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosPatientServiceRetrieve.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosPatientServiceRetrieve.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosPivotTranslation.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosPivotTranslation.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosTRCAssertion.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosTRCAssertion.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosMroList.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosMroList.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosMroRetrieve.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.epsosMroRetrieve.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.ehealthSMPQuery.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.ehealthSMPQuery.getCode();
        }
        if (eventType.equals(epsos.ccd.gnomon.auditmanager.EventType.ehealthSMPPush.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHEEventType.ehealthSMPPush.getCode();
        }
        // TODO: Fix this issue, does the mappedEventType should be initialized?
        if (mappedEventType == null) {
            return null;
        } else {
            return mappedEventType.getCode();
        }
    }

    private String getMappedTransactionName(String name) {
        IHETransactionName mappedEventType = null;
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosIdentificationServiceFindIdentityByTraits
                .getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosIdentificationServiceFindIdentityByTraits
                    .getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosCommunicationFailure.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosCommunicationFailure.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosConsentServiceDiscard.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosConsentServiceDiscard.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosConsentServicePin.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosConsentServicePin.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosConsentServicePut.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosConsentServicePut.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosDispensationServiceDiscard.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosDispensationServiceDiscard.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosDispensationServiceInitialize.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosDispensationServiceInitialize.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosHCERPut.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosHCERPut.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosHcpAuthentication.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosHcpAuthentication.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosNCPTrustedServiceList.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosNCPTrustedServiceList.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosOrderServiceList.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosOrderServiceList.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosOrderServiceRetrieve.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosOrderServiceRetrieve.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosPatientServiceList.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosPatientServiceList.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosPatientServiceRetrieve.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosPatientServiceRetrieve.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosPivotTranslation.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosPivotTranslation.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosTRCAssertion.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosTRCAssertion.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosMroServiceList.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosMroServiceList.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.epsosMroServiceRetrieve.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.epsosMroServiceRetrieve.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.ehealthSMPQuery.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.ehealthSMPQuery.getCode();
        }
        if (name.equals(epsos.ccd.gnomon.auditmanager.TransactionName.ehealthSMPPush.getCode())) {
            return epsos.ccd.gnomon.auditmanager.IHETransactionName.ehealthSMPPush.getCode();
        }
        // TODO: Fix this issue, does the mappedEventType should be initialized?
        if (mappedEventType == null) {
            return null;
        } else {
            return mappedEventType.getCode();
        }
    }

    private AuditMessage addEventIdentification(AuditMessage am, String EventType, String transactionName,
                                                String EventActionCode, XMLGregorianCalendar EventDateTime, BigInteger EventOutcomeIndicator) {
        // Change EventType to new ones

        EventIdentificationType eit = new EventIdentificationType();

        CodedValueType iheEventID = new CodedValueType();
        iheEventID.setCode(getMappedEventType(EventType));
        iheEventID.setCodeSystemName("IHE Transactions");
        iheEventID.setDisplayName(getMappedTransactionName(transactionName));
        eit.setEventID(iheEventID);

        CodedValueType eventID = new CodedValueType();
        eventID.setCode(EventType);
        eventID.setCodeSystemName("epSOS Transaction");
        eventID.setDisplayName(transactionName);
        eit.getEventTypeCode().add(eventID);

        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosPatientServiceList.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("60591-5");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("epSOS Patient Summary");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosPatientServiceRetrieve.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("60591-5");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("epSOS Patient Summary");
            eit.getEventTypeCode().add(eventID_epsos);
        }

        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosOrderServiceList.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("57833-6");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("epSOS ePrescription");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosOrderServiceRetrieve.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("57833-6");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("epSOS ePrescription");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosConsentServicePut.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("57016-8");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("Privacy Policy Acknowledgement Document");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosConsentServiceDiscard.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("57016-8");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("Privacy Policy Acknowledgement Document");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosDispensationServiceInitialize.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("60593-1");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("eDispensation");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosDispensationServiceDiscard.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("60593-1");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("eDispensation");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosHCERPut.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("34133-9");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("Summarization of Episode Note");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.epsosPACRetrieve.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("N/A");
            eventID_epsos.setCodeSystemName("epSOS LOINC");
            eventID_epsos.setDisplayName("PAC");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.ehealthSMPQuery.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("SMP");
            eventID_epsos.setCodeSystemName("ehealth-193");
            eventID_epsos.setDisplayName("SMP::Query");
            eit.getEventTypeCode().add(eventID_epsos);
        }
        if (EventType.equals(epsos.ccd.gnomon.auditmanager.EventType.ehealthSMPPush.getCode())) {
            CodedValueType eventID_epsos = new CodedValueType();
            eventID_epsos.setCode("SMP");
            eventID_epsos.setCodeSystemName("ehealth-194");
            eventID_epsos.setDisplayName("SMP::Push");
            eit.getEventTypeCode().add(eventID_epsos);
        }

        // eit.setEventID(eventID);
        eit.setEventActionCode(EventActionCode);
        eit.setEventDateTime(EventDateTime);
        eit.setEventOutcomeIndicator(EventOutcomeIndicator); // (0,1,4,8)
        am.setEventIdentification(eit);

        // <EventTypeCode code="60591-5" codeSystemName="epSOS LOINC"
        // displayName="epSOS Patient Summary"
        // originalText="urn:uuid:1.2.3.4.5.6.7.8.9.10"/>

        return am;
    }

    private AuditMessage addPointOfCare(AuditMessage am, String PC_UserID, String PC_RoleID, boolean UserIsRequestor,
                                        String codeSystem) {
        if (PC_UserID == null || PC_UserID.equals("")) {
            log.info("This is service provider and doesn't need Point of Care");
        } else {
            ActiveParticipant a = new ActiveParticipant();
            a.setUserID(PC_UserID);
            a.setUserIsRequestor(UserIsRequestor);
            CodedValueType roleId = new CodedValueType();
            roleId.setCode(PC_RoleID);
            roleId.setCodeSystem(codeSystem);
            a.getRoleIDCode().add(roleId);
            am.getActiveParticipant().add(a);
        }
        return am;
    }

    private AuditMessage addHumanRequestor(AuditMessage am, String HR_UserID, String HR_AlternativeUserID,
                                           String HR_RoleID, boolean UserIsRequestor) {
        ActiveParticipant hr = new ActiveParticipant();
        hr.setUserID(HR_UserID);
        hr.setAlternativeUserID(HR_AlternativeUserID);
        hr.setUserIsRequestor(UserIsRequestor);
        CodedValueType hrroleId = new CodedValueType();
        hrroleId.setCode(HR_RoleID);
        hr.getRoleIDCode().add(hrroleId);
        am.getActiveParticipant().add(hr);
        return am;
    }

    private AuditMessage addService(AuditMessage am, String SC_UserID, boolean UserIsRequestor, String code,
                                    String codeSystem, String displayName, String ipaddress) {
        if (SC_UserID == null || SC_UserID.equals("")) {
            log.warn("No Service, as this is Service Consumer");
        } else {
            ActiveParticipant sc = new ActiveParticipant();
            sc.setNetworkAccessPointID(ipaddress);
            sc.setNetworkAccessPointTypeCode(new Short("2"));
            sc.setUserID(SC_UserID);
            sc.setUserIsRequestor(UserIsRequestor);
            CodedValueType scroleId = new CodedValueType();
            scroleId.setCode(code);
            scroleId.setCodeSystem(codeSystem);
            scroleId.setDisplayName(displayName);
            sc.getRoleIDCode().add(scroleId);
            am.getActiveParticipant().add(sc);
        }
        return am;
    }

    private AuditMessage addParticipantObject(AuditMessage am, String PS_PatricipantObjectID, Short PS_TypeCode,
                                              Short PS_TypeRole, String PS_Name, String PS_ObjectCode, String PS_ObjectCodeName,
                                              String PS_ObjectCodeValue) {
        ParticipantObjectIdentificationType poit = new ParticipantObjectIdentificationType();
        poit.setParticipantObjectID(PS_PatricipantObjectID);
        poit.setParticipantObjectTypeCode(new Short(PS_TypeCode));
        poit.setParticipantObjectTypeCodeRole(new Short(PS_TypeRole)); // short
        poit.setParticipantObjectName(PS_Name);
        CodedValueType PS_object = new CodedValueType();
        PS_object.setCode(PS_ObjectCode);
        PS_object.setCodeSystemName(PS_ObjectCodeName);
        PS_object.setDisplayName(PS_ObjectCodeValue);
        poit.setParticipantObjectIDTypeCode(PS_object);
        am.getParticipantObjectIdentification().add(poit);
        return am;
    }

    private AuditMessage addError(AuditMessage am, String EM_PatricipantObjectID, byte[] EM_PatricipantObjectDetail,
                                  Short EM_Code, Short EM_CodeRole, String EM_TypeCode, String EM_qualifier) {
        // Error Message
        boolean noErrorOccured = false;
        log.info("The EM_PatricipantObjectID is : " + EM_PatricipantObjectID);
        try {
            noErrorOccured = Utils.isEmpty(EM_PatricipantObjectID);
        } catch (Exception e) {
            noErrorOccured = true;
            log.error("The participant object id is null. So no error occured" + e.getMessage());
        }

        if (noErrorOccured) {
            log.info("There is no error occured");
        } else {
            ParticipantObjectIdentificationType em = new ParticipantObjectIdentificationType();
            em.setParticipantObjectID(EM_PatricipantObjectID);
            em.setParticipantObjectTypeCode(new Short(EM_Code));
            em.setParticipantObjectTypeCodeRole(new Short(EM_CodeRole)); // short
            CodedValueType EM_object = new CodedValueType();
            EM_object.setCode(EM_TypeCode);
            em.setParticipantObjectIDTypeCode(EM_object);
            if (EM_PatricipantObjectDetail != null) {
                TypeValuePairType pod = new TypeValuePairType();
                pod.setType(EM_qualifier);
                pod.setValue(EM_PatricipantObjectDetail);
                em.getParticipantObjectDetail().add(pod);
            }
            am.getParticipantObjectIdentification().add(em);
        }
        return am;
    }

    private AuditMessage addEventTarget(AuditMessage am, String ET_ObjectID, Short TypeCode, Short TypeCodeRole,
                                        String EM_Code, String action, Short ObjectDataLifeCycle) {
        ParticipantObjectIdentificationType em = new ParticipantObjectIdentificationType();
        em.setParticipantObjectID(ET_ObjectID);
        em.setParticipantObjectTypeCode(TypeCode);
        em.setParticipantObjectTypeCodeRole(TypeCodeRole);
        CodedValueType EM_object = new CodedValueType();
        EM_object.setCode(EM_Code);
        if (action.equals("Discard") || action.equals("Pin")) {
            em.setParticipantObjectDataLifeCycle(ObjectDataLifeCycle);
        }
        em.setParticipantObjectIDTypeCode(EM_object);
        am.getParticipantObjectIdentification().add(em);
        return am;
    }

    private AuditMessage addEventTarget(AuditMessage am, String ET_ObjectID, Short ObjectTypeCode,
                                        Short ObjectDataLifeCycle, String EM_Code, String EM_CodeSystemName, String EM_DisplayName) {
        ParticipantObjectIdentificationType em = new ParticipantObjectIdentificationType();
        em.setParticipantObjectID(ET_ObjectID);
        em.setParticipantObjectTypeCode(ObjectTypeCode);
        if (ObjectDataLifeCycle != null) {
            em.setParticipantObjectDataLifeCycle(ObjectDataLifeCycle);
        }
        CodedValueType EM_object = new CodedValueType();
        EM_object.setCode(EM_Code);
        EM_object.setCodeSystemName(EM_CodeSystemName);
        EM_object.setDisplayName(EM_DisplayName);
        em.setParticipantObjectIDTypeCode(EM_object);
        am.getParticipantObjectIdentification().add(em);
        return am;
    }
    
    
    /**
     * Constructs an Audit Message for Patient Privacy Audit Schema for eHealth SMP Query
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage createAuditTrailForEhealthSMPQuery(EventLog eventLog) {
      //TODO
      AuditMessage am = null;
      try {
        ObjectFactory of = new ObjectFactory();
        am = of.createAuditMessage();
        addEventIdentification(am, eventLog.getEventType(), eventLog.getEI_TransactionName(),
                eventLog.getEI_EventActionCode(), eventLog.getEI_EventDateTime(),
                eventLog.getEI_EventOutcomeIndicator());
        addService(am, eventLog.getSC_UserID(), true, "ServiceConsumer", "epSOS", "epSOS Service Consumer",
                eventLog.getSourceip());
        addService(am, eventLog.getSP_UserID(), false, "ServiceProvider", "epSOS", "epSOS Service Provider",
                eventLog.getTargetip());
        addAuditSource(am, eventLog.getAS_AuditSourceId());
        addError(am, eventLog.getEM_PatricipantObjectID(), eventLog.getEM_PatricipantObjectDetail(), new Short("2"),
                    new Short("3"), "9", "errormsg");
      } catch (Exception e) {
        log.error(e.getLocalizedMessage(), e);
      }
      return am;
    }
    
    /**
     * Constructs an Audit Message for Patient Privacy Audit Schema for eHealth SMP Push
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage createAuditTrailForEhealthSMPPush(EventLog eventLog) {
      //TODOO
      AuditMessage am = null;
      try {
        ObjectFactory of = new ObjectFactory();
        am = of.createAuditMessage();
        addEventIdentification(am, eventLog.getEventType(), eventLog.getEI_TransactionName(),
                eventLog.getEI_EventActionCode(), eventLog.getEI_EventDateTime(),
                eventLog.getEI_EventOutcomeIndicator());
        addService(am, eventLog.getSC_UserID(), true, "ServiceConsumer", "epSOS", "epSOS Service Consumer",
                eventLog.getSourceip());
        addService(am, eventLog.getSP_UserID(), false, "ServiceProvider", "epSOS", "epSOS Service Provider",
                eventLog.getTargetip());
        addAuditSource(am, eventLog.getAS_AuditSourceId());
        addError(am, eventLog.getEM_PatricipantObjectID(), eventLog.getEM_PatricipantObjectDetail(), new Short("2"),
                    new Short("3"), "9", "errormsg");
      } catch (Exception e) {
        log.error(e.getLocalizedMessage(), e);
      }
      return am;
    }

    /**
     * Constructs an Audit Message for HCP Assurance Schema
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage createAuditTrailForHCPAssurance(EventLog eventLog) {
        AuditMessage am = null;
        try {
            ObjectFactory of = new ObjectFactory();
            am = of.createAuditMessage();
            addEventIdentification(am, eventLog.getEventType(), eventLog.getEI_TransactionName(),
                    eventLog.getEI_EventActionCode(), eventLog.getEI_EventDateTime(),
                    eventLog.getEI_EventOutcomeIndicator());
            addPointOfCare(am, eventLog.getPC_UserID(), eventLog.getPC_RoleID(), true,
                    "1.3.6.1.4.1.12559.11.10.1.3.2.2.2");
            addHumanRequestor(am, eventLog.getHR_UserID(), eventLog.getHR_AlternativeUserID(), eventLog.getHR_RoleID(),
                    true);
            addService(am, eventLog.getSC_UserID(), true, "ServiceConsumer", "epSOS", "epSOS Service Consumer",
                    eventLog.getSourceip());
            addService(am, eventLog.getSP_UserID(), false, "ServiceProvider", "epSOS", "epSOS Service Provider",
                    eventLog.getTargetip());
            addAuditSource(am, eventLog.getAS_AuditSourceId());
            addParticipantObject(am, eventLog.getPT_PatricipantObjectID(), new Short("1"), new Short("1"), "Patient",
                    "2", "RFC-3881", "Patient Number");
            addError(am, eventLog.getEM_PatricipantObjectID(), eventLog.getEM_PatricipantObjectDetail(), new Short("2"),
                    new Short("3"), "9", "errormsg");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return am;
    }

    /**
     * Constructs an Audit Message for CommunicationFailure
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage createAuditTrailForCommunicationFailure(EventLog eventLog) {
        AuditMessage am = null;
        try {
            ObjectFactory of = new ObjectFactory();
            am = of.createAuditMessage();
            addAuditSource(am, "N/A");
            addEventIdentification(am, eventLog.getEventType(), eventLog.getEI_TransactionName(),
                    eventLog.getEI_EventActionCode(), eventLog.getEI_EventDateTime(),
                    eventLog.getEI_EventOutcomeIndicator());
            addHumanRequestor(am, eventLog.getHR_UserID(), eventLog.getHR_AlternativeUserID(), eventLog.getHR_RoleID(),
                    true);
            addService(am, eventLog.getSC_UserID(), true, "ServiceConsumer", "epSOS", "epSOS Service Consumer",
                    eventLog.getSourceip());
            addService(am, eventLog.getSP_UserID(), false, "ServiceProvider", "epSOS", "epSOS Service Provider",
                    eventLog.getTargetip());
            addParticipantObject(am, eventLog.getPT_PatricipantObjectID(), new Short("1"), new Short("1"), "Patient",
                    "2", "RFC-3881", "Patient Number");
            addError(am, eventLog.getEM_PatricipantObjectID(), eventLog.getEM_PatricipantObjectDetail(), new Short("2"),
                    new Short("3"), "9", "errormsg");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return am;
    }

    /**
     * Constructs an Audit Message for Issuance of a Treatment Relationship
     * Confirmation Assertion
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage createAuditTrailForTRCA(EventLog eventLog) {
        AuditMessage am = null;
        try {
            ObjectFactory of = new ObjectFactory();
            am = of.createAuditMessage();
            // Audit Source
            addAuditSource(am, eventLog.getAS_AuditSourceId());
            // Event Identification
            addEventIdentification(am, eventLog.getEventType(), eventLog.getEI_TransactionName(), "E",
                    eventLog.getEI_EventDateTime(), eventLog.getEI_EventOutcomeIndicator());
            // Point Of Care
            addPointOfCare(am, eventLog.getPC_UserID(), eventLog.getPC_RoleID(), true,
                    "1.3.6.1.4.1.12559.11.10.1.3.2.2.2");
            // Human Requestor
            addHumanRequestor(am, eventLog.getHR_UserID(), eventLog.getHR_AlternativeUserID(), eventLog.getHR_RoleID(),
                    true);
            addService(am, eventLog.getSC_UserID(), true, "ServiceConsumer", "epSOS", "epSOS Service Consumer",
                    eventLog.getSourceip());
            addService(am, eventLog.getSP_UserID(), false, "ServiceProvider", "epSOS", "epSOS Service Provider",
                    eventLog.getTargetip());
            addParticipantObject(am, eventLog.getPT_PatricipantObjectID(), new Short("1"), new Short("1"), "Patient",
                    "2", "RFC-3881", "Patient Number");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return am;
    }

    /**
     * Constructs an Audit Message for NCP Trusted Service List
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage createAuditTrailForNCPTrustedServiceList(EventLog eventLog) {
        AuditMessage am = null;
        try {
            ObjectFactory of = new ObjectFactory();
            am = of.createAuditMessage();
            // Audit Source
            addAuditSource(am, eventLog.getAS_AuditSourceId());
            // Event Identification
            addEventIdentification(am, eventLog.getEventType(), eventLog.getEI_TransactionName(),
                    eventLog.getEI_EventActionCode(), eventLog.getEI_EventDateTime(),
                    eventLog.getEI_EventOutcomeIndicator());
            addService(am, eventLog.getSC_UserID(), true, "ServiceConsumer", "epSOS", "epSOS Service Consumer",
                    eventLog.getSourceip());
            addService(am, eventLog.getSP_UserID(), false, "ServiceProvider", "epSOS", "epSOS Service Provider",
                    eventLog.getTargetip());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return am;
    }

    /**
     * Constructs an Audit Message for Pivot Translation of a Medical Document
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage createAuditTrailForPivotTranslation(EventLog eventLog) {
        AuditMessage am = null;
        try {
            ObjectFactory of = new ObjectFactory();
            am = of.createAuditMessage();
            addAuditSource(am, eventLog.getAS_AuditSourceId());
            addEventIdentification(am, eventLog.getEventType(), eventLog.getEI_TransactionName(), "E",
                    eventLog.getEI_EventDateTime(), eventLog.getEI_EventOutcomeIndicator());

            addService(am, eventLog.getSP_UserID(), false, "ServiceProvider", "epSOS", "epSOS Service Provider",
                    eventLog.getTargetip());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return am;
    }

    /**
     * Constructs an Audit Message for HCP Identity Assertion
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage createAuditTrailForHCPIdentity(EventLog eventLog) {
        AuditMessage am = null;
        try {
            ObjectFactory of = new ObjectFactory();
            am = of.createAuditMessage();
            // Audit Source
            addAuditSource(am, eventLog.getAS_AuditSourceId());
            // Event Identification
            addEventIdentification(am, eventLog.getEventType(), eventLog.getEI_TransactionName(), "E",
                    eventLog.getEI_EventDateTime(), eventLog.getEI_EventOutcomeIndicator());
            // Point Of Care
            addPointOfCare(am, eventLog.getPC_UserID(), eventLog.getPC_RoleID(), true,
                    "1.3.6.1.4.1.12559.11.10.1.3.2.2.2");
            // Human Requestor
            addHumanRequestor(am, eventLog.getHR_UserID(), eventLog.getHR_AlternativeUserID(), eventLog.getHR_RoleID(),
                    true);
            addService(am, eventLog.getSC_UserID(), true, "ServiceConsumer", "epSOS", "epSOS Service Consumer",
                    eventLog.getSourceip());
            addService(am, eventLog.getSP_UserID(), false, "ServiceProvider", "epSOS", "epSOS Service Provider",
                    eventLog.getTargetip());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return am;
    }

    /**
     * Constructs an Audit Message for Patient Mapping Schema
     *
     * @param eventLog the EventLog object
     * @return the created AuditMessage object
     */
    private AuditMessage createAuditTrailForPatientMapping(EventLog eventLog) {
        AuditMessage am = null;
        try {
            ObjectFactory of = new ObjectFactory();
            am = of.createAuditMessage();
            addEventIdentification(am, eventLog.getEventType(), eventLog.getEI_TransactionName(),
                    eventLog.getEI_EventActionCode(), eventLog.getEI_EventDateTime(),
                    eventLog.getEI_EventOutcomeIndicator());
            addHumanRequestor(am, eventLog.getHR_UserID(), eventLog.getHR_AlternativeUserID(), eventLog.getHR_RoleID(),
                    true);
            addService(am, eventLog.getSC_UserID(), true, "ServiceConsumer", "epSOS", "epSOS Service Consumer",
                    eventLog.getSourceip());
            addService(am, eventLog.getSP_UserID(), false, "ServiceProvider", "epSOS", "epSOS Service Provider",
                    eventLog.getTargetip());
            addService(am, eventLog.getSP_UserID(), false, "MasterPatientIndex", "epSOS", "Master Patient Index",
                    eventLog.getTargetip());
            addAuditSource(am, eventLog.getAS_AuditSourceId());
            addParticipantObject(am, eventLog.getPS_PatricipantObjectID(), new Short("1"), new Short("1"),
                    "PatientSource", "2", "RFC-3881", "Patient Number");
            addParticipantObject(am, eventLog.getPT_PatricipantObjectID(), new Short("1"), new Short("1"),
                    "PatientTarget", "2", "RFC-3881", "Patient Number");
            addError(am, eventLog.getEM_PatricipantObjectID(), eventLog.getEM_PatricipantObjectDetail(), new Short("2"),
                    new Short("3"), "9", "errormsg");
            log.info(am.toString());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);

        }
        return am;
    }

    private void writeTestAudits(AuditMessage auditmessage, String auditmsg) {
        String wta = Utils.getProperty("WRITE_TEST_AUDITS");
        log.info("Writing test audits : " + wta);
        if (wta.equals("true")) {
            log.info("Writing test audits");
            String tap = Utils.getProperty("TEST_AUDITS_PATH");
            try {
                Utils.writeXMLToFile(auditmsg,
                        tap + (auditmessage.getEventIdentification().getEventTypeCode().get(0).getDisplayName()
                                .split("::"))[0] + "-"
                                + new SimpleDateFormat("yyyy.MM.dd'at'HH-mm-ss.SSS")
                                .format(new Date(System.currentTimeMillis()))
                                + ".xml");
            } catch (Exception e) {
                try {
                    Utils.writeXMLToFile(auditmsg, tap + new SimpleDateFormat("yyyy.MM.dd'at'HH-mm-ss.SSS")
                            .format(new Date(System.currentTimeMillis())) + ".xml");
                } catch (Exception e2) {
                    log.warn("Unable to write test audit.");
                }
            }
        }
    }

    public synchronized static String constructMessage(AuditMessage auditmessage, boolean sign) {
        String auditmsg = "";
        log.info("Constructing message");
        String eventTypeCode = "EventTypeCode(N/A)";
        try {
            eventTypeCode = auditmessage.getEventIdentification().getEventTypeCode().get(0).getCode();
            log.debug(eventTypeCode + " try to convert the message to xml using JAXB");
        } catch (NullPointerException e) {
            log.warn("Unable to log AuditMessageEventTypeCode.");
        }

        try {
            auditmsg = AuditTrailUtils.convertAuditObjectToXML(auditmessage);
        } catch (JAXBException e) {
            log.error(e.getMessage(), e);
        }
        log.info("Message created");

        INSTANCE.writeTestAudits(auditmessage, auditmsg);

        log.info(eventTypeCode + " message constructed");

        boolean validated = false;
        URL url = null;
        try {
            url = Utils.class.getClassLoader().getResource("RFC3881.xsd");
        } catch (Exception e) {
            log.error(auditmessage.getEventIdentification().getEventID().getCode() + " Error getting xsd url", e);
        }
        try {
            validated = Utils.validateSchema(auditmsg, url);
            log.info(auditmessage.getEventIdentification().getEventID().getCode() + " Validating Schema");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        boolean forcewrite = Boolean.parseBoolean(Utils.getProperty("auditrep.forcewrite", "TRUE", true));
        if (!validated) {
            log.info(auditmessage.getEventIdentification().getEventID().getCode() + " Message not validated");
            if (!forcewrite) {
                auditmsg = "";
            }
        }
        if (validated || forcewrite) {
            if (validated) {
                log.info(auditmessage.getEventIdentification().getEventID().getCode() + " Message validated");
            } else {
                log.info(auditmessage.getEventIdentification().getEventID().getCode() + " message not validated");
            }
            if (forcewrite && !validated) {
                log.info(auditmessage.getEventIdentification().getEventID().getCode()
                        + " AuditManager is force to send the message. So trying ...");
            }

            try {
                // validate xml according to xsd
                log.debug(auditmessage.getEventIdentification().getEventID().getCode()
                        + " XML stuff: Create Dom From String");
                Document doc = Utils.createDomFromString(auditmsg);
                if (sign) {
                    // Gnomon SecMan
                    auditmsg = SecurityMgr.getSignedDocumentAsString(SecurityMgr.signDocumentEnveloped(doc));

                    log.info(auditmessage.getEventIdentification().getEventID().getCode() + " message signed");
                }
            } catch (Exception e) {
                auditmsg = "";
                log.error(auditmessage.getEventIdentification().getEventID().getCode() + " Error signing doc"
                        + e.getMessage(), e);
            }
        }
        return auditmsg;
    }

    /**
     * The method converts the audit message to xml format, having as input the
     * Audit Message. Uses the JAXB library to marshal the audti message object
     *
     * @param am
     * @return
     * @throws JAXBException
     */
    public synchronized static String convertAuditObjectToXML(AuditMessage am) throws JAXBException {
        log.info("Converting message");
        StringWriter sw = new StringWriter();
        log.debug("JAXB marshalling the Audit Object");
        JAXBContext context = JAXBContext.newInstance(AuditMessage.class);
        Marshaller m = context.createMarshaller();
        try {
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (PropertyException e) {
            log.warn("Unable to format converted AuditMessage to XML.");
        }
        m.marshal(am, sw);
        log.info("Converting message finished");
        return sw.toString();
    }

    public synchronized void sendATNASyslogMessage(AuditLogSerializer auditLogSerializer, AuditMessage auditmessage,
                                                   String facility, String severity) {
        MessageSender t = new MessageSender(auditLogSerializer, auditmessage, facility, severity);
        log.debug("Starting new thread for sending message");
        t.start();
    }

    /**
     * Based on a given EventLog this method will infer about the correct model
     * to apply for a message validation.
     *
     * @param eventLog an EventLog object containing information about the audit
     *                 message.
     * @return the Audit Message Model
     */
    private boolean validateAuditMessage(EventLog eventLog, AuditMessage am) {
        String model = "";
        NcpSide ncpSide = null;

        if (eventLog.getPC_UserID() == null || eventLog.getPC_UserID().isEmpty()) {
            ncpSide = NcpSide.NCP_A;
        } else {
            ncpSide = NcpSide.NCP_B;
        }

        // Unsupported EventCodes
        // if (eventLog.getEventType().equals("epsos-22")) {
        // throw new UnsupportedOperationException("EventCode not supported.");
        // }
        // Mark as comments related to the Jira issue AM-25
        // if (eventLog.getEventType().equals("epsos-32")) {
        // throw new UnsupportedOperationException("EventCode not supported.");
        // }
        if (eventLog.getEventType().equals("epsos-cf")) {
            throw new UnsupportedOperationException("EventCode not supported.");
        }

        // Infer model according to NCP Side and EventCode
        switch (ncpSide) {
            case NCP_A: {
                if (eventLog.getEventType().equals("epsos-11")) {
                    model = AuditModel.EPSOS2_IDENTIFICATION_SERVICE_AUDIT_SP.toString();
                }
                if (eventLog.getEventType().equals("epsos-21") || eventLog.getEventType().equals("epsos-22")
                        || eventLog.getEventType().equals("epsos-31") || eventLog.getEventType().equals("epsos-94")
                        || eventLog.getEventType().equals("epsos-96") || eventLog.getEventType().equals("ITI-38")
                        || eventLog.getEventType().equals("ITI-39")
                        || eventLog.getEventType().equals(EventType.epsosPACRetrieve.getCode())) {
                    model = AuditModel.EPSOS2_FETCH_DOC_SERVICE_SP.toString();
                }
                if (eventLog.getEventType().equals("epsos-41") || eventLog.getEventType().equals("epsos-51")) {
                    model = AuditModel.EPSOS2_PROVIDE_DATA_SERVICE_SP.toString();
                }
                break;
            }
            case NCP_B: {
                if (eventLog.getEventType().equals("epsos-11")) {
                    model = AuditModel.EPSOS2_HCP_ASSURANCE_AUDIT.toString();
                }
                if (eventLog.getEventType().equals("epsos-21") || eventLog.getEventType().equals("epsos-22")
                        || eventLog.getEventType().equals("epsos-31") || eventLog.getEventType().equals("epsos-94")
                        || eventLog.getEventType().equals("epsos-96") || eventLog.getEventType().equals("ITI-38")
                        || eventLog.getEventType().equals("ITI-39")
                        || eventLog.getEventType().equals(EventType.epsosPACRetrieve.getCode())) {
                    model = AuditModel.EPSOS2_HCP_ASSURANCE_AUDIT.toString();
                }
                if (eventLog.getEventType().equals("epsos-41") || eventLog.getEventType().equals("epsos-51")) {
                    model = AuditModel.EPSOS2_PROVIDE_DATA_SERVICE_SC.toString();
                }
                if (eventLog.getEventType().equals("epsos-91")) {
                    model = AuditModel.EPSOS2_ISSUANCE_HCP_ASSERTION.toString();
                }
                if (eventLog.getEventType().equals("epsos-92")) {
                    model = AuditModel.EPSOS2_ISSUANCE_TRC_ASSERTION.toString();
                }
                if (eventLog.getEventType().equals("epsos-93")) {
                    model = AuditModel.EPSOS2_IMPORT_NCP_TRUSTED_LIST.toString();
                }
                break;
            }

        }

        return AuditValidationService.getInstance().validateModel(AuditTrailUtils.constructMessage(am, false), model,
                ncpSide);
    }
}
