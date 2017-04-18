/**
 * *Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *//*
	* To change this template, choose Tools | Templates
	* and open the template in the editor.
	*/
package epsos.ccd.gnomon.auditmanager;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import java.math.BigInteger;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Java Bean object for the Event Log object. Getters and setters for all the
 * proerties Constructors for EPSOS audit schemas
 *
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun EventLog is a java object with the minimal input
 *          variables needed to construct an AuditMessage according to RFC3881
 */
public class EventLog {

	// Event Identification
	private EventType eventType; // one of the availbale epsos event ids
	public String EI_TransactionNumber; // Number of the transaction including
										// the 'Epsos-' prefix
	public String EI_TransactionName; // Name of the transaction, specified in
										// the use cases diagram
	private String EI_EventActionCode; // C:create, R:Read,View,Print,Query,
										// U:Update, D:Delete, E:Execute
	public XMLGregorianCalendar EI_EventDateTime;
	private BigInteger EI_EventOutcomeIndicator; // Possible values are: 0:full
													// success,1:partial
													// delivery,4:temporal or
													// recoverable
													// failures,8:permanent
													// failure
	// Point of Care
	public String PC_UserID; // Point of Care: Oid of the department
	public String PC_RoleID; // Point of Care: Role of the department
	// Human Requestor
	public String HR_UserID; // Identifier of the HCP initiated the event
	public String HR_AlternativeUserID; // Human readable name of the HCP as
										// given in the Subject-ID
	public String HR_UserName;
	public String HR_RoleID;
	// Service Consumer NCP
	public String SC_UserID; // The string encoded CN of the TLS certificate of
								// the NCP triggered the epsos operation
	// Service Provider
	public String SP_UserID; // The string encoded CN of the TLS certificate of
								// the NCP triggered the epsos operation
	// Audit Source
	public String AS_AuditSourceId; // The authority that is legally responsible
									// for the audit source
	// Patient Source
	public String PS_PatricipantObjectID; // Patient Code in HL7 format
	// Patient Target
	public String PT_PatricipantObjectID; // Mapped PatientCode in HL7 format
	// Error Message
	public String EM_PatricipantObjectID; // String-encoded error code
	public byte[] EM_PatricipantObjectDetail; // Base64 encoded error message
	// Mapping Service
	public String MS_UserID; // The string encoded OID of the service instance
								// performed the mapping
	public String ET_ObjectID; // The string encoded UUID of the returned
								// document
	public String ET_ObjectID_additional; // The string encoded UUID of the
											// returned document
	// Non-Repudiation
	public String ReqM_ParticipantObjectID; // ReqM_ParticipantObjectID
											// String-encoded UUID of the
											// request message
	public byte[] ReqM_PatricipantObjectDetail; // Base64 encoded error message
	public String ResM_ParticipantObjectID; // ReqM_ParticipantObjectID
											// String-encoded UUID of the
											// response message
	public byte[] ResM_PatricipantObjectDetail; // Base64 encoded error message
	// IP Addresses
	public String sourceip;
	public String targetip;

	public String getHR_RoleID() {
		return HR_RoleID;
	}

	public void setHR_RoleID(String HR_RoleID) {
		this.HR_RoleID = HR_RoleID;
	}

	public String getET_ObjectID_additional() {
		return ET_ObjectID_additional;
	}

	public void setET_ObjectID_additional(String ET_ObjectID_additional) {
		this.ET_ObjectID_additional = ET_ObjectID_additional;
	}

	public String getPC_RoleID() {
		return PC_RoleID;
	}

	public void setPC_RoleID(String PC_RoleID) {
		this.PC_RoleID = PC_RoleID;
	}

	public String getSourceip() {
		return sourceip;
	}

	public void setSourceip(String sourceip) {
		this.sourceip = sourceip;
	}

	public String getTargetip() {
		return targetip;
	}

	public void setTargetip(String targetip) {
		this.targetip = targetip;
	}

	public String getReqM_ParticipantObjectID() {
		return ReqM_ParticipantObjectID;
	}

	public void setReqM_ParticipantObjectID(String ReqM_ParticipantObjectID) {
		this.ReqM_ParticipantObjectID = ReqM_ParticipantObjectID;
	}

	public byte[] getReqM_PatricipantObjectDetail() {
		return ReqM_PatricipantObjectDetail;
	}

	public void setReqM_PatricipantObjectDetail(byte[] ReqM_PatricipantObjectDetail) {
		this.ReqM_PatricipantObjectDetail = ReqM_PatricipantObjectDetail;
	}

	public String getResM_ParticipantObjectID() {
		return ResM_ParticipantObjectID;
	}

	public void setResM_ParticipantObjectID(String ResM_ParticipantObjectID) {
		this.ResM_ParticipantObjectID = ResM_ParticipantObjectID;
	}

	public byte[] getResM_PatricipantObjectDetail() {
		return ResM_PatricipantObjectDetail;
	}

	public void setResM_PatricipantObjectDetail(byte[] ResM_PatricipantObjectDetail) {
		this.ResM_PatricipantObjectDetail = ResM_PatricipantObjectDetail;
	}

	public EventLog() {
	}

	public void setET_ObjectID(String ET_ObjectID) {
		this.ET_ObjectID = ET_ObjectID;
	}

	public String getET_ObjectID() {
		return ET_ObjectID;
	}

	public void setAS_AuditSourceId(String AS_AuditSourceId) {
		this.AS_AuditSourceId = AS_AuditSourceId;
	}

	public void setEI_EventActionCode(EventActionCode EI_EventActionCode) {
		this.EI_EventActionCode = EI_EventActionCode.getCode();
	}

	public void setEI_EventDateTime(XMLGregorianCalendar EI_EventDateTime) {
		this.EI_EventDateTime = EI_EventDateTime;
	}

	public void setEI_EventOutcomeIndicator(EventOutcomeIndicator EI_EventOutcomeIndicator) {
		this.EI_EventOutcomeIndicator = EI_EventOutcomeIndicator.getCode();
	}

	public void setEI_TransactionName(TransactionName EI_TransactionName) {
		this.EI_TransactionName = EI_TransactionName.getCode();
	}

	public void setEI_TransactionNumber(String EI_TransactionNumber) {
		this.EI_TransactionNumber = EI_TransactionNumber;
	}

	public void setEM_PatricipantObjectDetail(byte[] EM_PatricipantObjectDetail) {
		this.EM_PatricipantObjectDetail = EM_PatricipantObjectDetail;
	}

	public void setEM_PatricipantObjectID(String EM_PatricipantObjectID) {
		this.EM_PatricipantObjectID = EM_PatricipantObjectID;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public void setHR_AlternativeUserID(String HR_AlternativeUserID) {
		this.HR_AlternativeUserID = HR_AlternativeUserID;
	}

	public void setHR_UserID(String HR_UserID) {
		this.HR_UserID = HR_UserID;
	}

	public void setHR_UserName(String HR_UserName) {
		this.HR_UserName = HR_UserName;
	}

	public void setMS_UserID(String MS_UserID) {
		this.MS_UserID = MS_UserID;
	}

	public void setPC_UserID(String PC_UserID) {
		this.PC_UserID = PC_UserID;
	}

	public void setPS_PatricipantObjectID(String PS_PatricipantObjectID) {
		this.PS_PatricipantObjectID = PS_PatricipantObjectID;
	}

	public void setPT_PatricipantObjectID(String PT_PatricipantObjectID) {
		this.PT_PatricipantObjectID = PT_PatricipantObjectID;
	}

	public void setSC_UserID(String SC_UserID) {
		this.SC_UserID = SC_UserID;
	}

	public void setSP_UserID(String SP_UserID) {
		this.SP_UserID = SP_UserID;
	}

	public String getEventType() {
		return this.eventType.getCode();
	}

	public EventType getEventTypeEnum() {

		return this.eventType;
	}

	public byte[] getEM_PatricipantObjectDetail() {
		return EM_PatricipantObjectDetail;
	}

	public String getAS_AuditSourceId() {
		return AS_AuditSourceId;
	}

	public String getEI_EventActionCode() {
		return EI_EventActionCode;
	}

	public XMLGregorianCalendar getEI_EventDateTime() {
		return EI_EventDateTime;
	}

	public BigInteger getEI_EventOutcomeIndicator() {
		return EI_EventOutcomeIndicator;
	}

	public String getEI_TransactionName() {
		return EI_TransactionName;
	}

	public String getEI_TransactionNumber() {
		return EI_TransactionNumber;
	}

	public String getEM_PatricipantObjectID() {
		return EM_PatricipantObjectID;
	}

	public String getHR_AlternativeUserID() {
		return HR_AlternativeUserID;
	}

	public String getHR_UserID() {
		return HR_UserID;
	}

	public String getHR_UserName() {
		return HR_UserName;
	}

	public String getMS_UserID() {
		return MS_UserID;
	}

	public String getPC_UserID() {
		return PC_UserID;
	}

	public String getPS_PatricipantObjectID() {
		return PS_PatricipantObjectID;
	}

	public String getPT_PatricipantObjectID() {
		return PT_PatricipantObjectID;
	}

	public String getSC_UserID() {
		return SC_UserID;
	}

	public String getSP_UserID() {
		return SP_UserID;
	}

	/**
	 * This method creates an EventLog object for use in HCP Authentication
	 *
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param PC_UserID
	 *            Point of Care: Oid of the department
	 * @param PC_RoleID
	 *            Role of the department
	 * @param HR_UserID
	 *            Identifier of the HCP initiated the event
	 * @param HR_RoleID
	 *            Role of the HCP initiated the event
	 * @param HR_AlternativeUserID
	 *            Human readable name of the HCP as given in the Subject-ID
	 * @param SC_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            triggered the epsos operation
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 * @param AS_AuditSourceId
	 *            the iso3166-2 code of the country responsible for the audit
	 *            source
	 * @param ET_ObjectID
	 *            The string encoded UUID of the returned document
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param sourceip
	 *            The IP Address of the source Gateway
	 * @param targetip
	 *            The IP Address of the target Gateway
	 *
	 */
	public static EventLog createEventLogHCPIdentity(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String PC_UserID, String PC_RoleID, String HR_UserID,
			String HR_RoleID, String HR_AlternativeUserID, String SC_UserID, String SP_UserID, String AS_AuditSourceId,
			String ET_ObjectID, String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail,
			String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail, String sourceip, String targetip) {
		EventLog el = new EventLog();
		el.setEI_TransactionName(EI_TransactionName);
		el.setEI_EventActionCode(EI_EventActionCode);
		el.setEI_EventDateTime(EI_EventDateTime);
		el.setEI_EventOutcomeIndicator(EI_EventOutcomeIndicator);
		el.setPC_UserID(NullToEmptyString(PC_UserID));
		el.setPC_RoleID(NullToEmptyString(PC_RoleID));
		el.setHR_UserID(NullToEmptyString(HR_UserID));
		el.setHR_RoleID(NullToEmptyString(HR_RoleID));
		el.setHR_AlternativeUserID(NullToEmptyString(HR_AlternativeUserID));
		el.setSC_UserID(NullToEmptyString(SC_UserID));
		el.setSP_UserID(NullToEmptyString(SP_UserID));
		el.setAS_AuditSourceId(NullToEmptyString(AS_AuditSourceId));
		el.setET_ObjectID(NullToEmptyString(ET_ObjectID));
		el.setReqM_ParticipantObjectID(NullToEmptyString(ReqM_ParticipantObjectID));
		el.setReqM_PatricipantObjectDetail(ReqM_PatricipantObjectDetail);
		el.setResM_ParticipantObjectID(NullToEmptyString(ResM_ParticipantObjectID));
		el.setResM_PatricipantObjectDetail(ResM_PatricipantObjectDetail);
		el.setSourceip(NullToEmptyString(sourceip));
		el.setTargetip(NullToEmptyString(targetip));
		return el;
	}

	/**
	 * This method creates an EventLog object for use in NLS Import
	 *
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param SC_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            triggered the epsos operation
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 * @param ET_ObjectID
	 *            The string encoded UUID of the returned document
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param sourceip
	 *            The IP Address of the source Gateway
	 * @param targetip
	 *            The IP Address of the target Gateway
	 *
	 */
	public static EventLog createEventLogNCPTrustedServiceList(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String SC_UserID, String SP_UserID, String ET_ObjectID,
			String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail, String ResM_ParticipantObjectID,
			byte[] ResM_PatricipantObjectDetail, String sourceip, String targetip) {
		EventLog el = new EventLog();
		el.setAS_AuditSourceId("EP-00");
		el.setEI_TransactionName(EI_TransactionName);
		el.setEI_EventActionCode(EI_EventActionCode);
		el.setEI_EventDateTime(EI_EventDateTime);
		el.setEI_EventOutcomeIndicator(EI_EventOutcomeIndicator);
		el.setSC_UserID(NullToEmptyString(SC_UserID));
		el.setSP_UserID(NullToEmptyString(SP_UserID));
		el.setET_ObjectID(NullToEmptyString(ET_ObjectID));
		el.setReqM_ParticipantObjectID(NullToEmptyString(ReqM_ParticipantObjectID));
		el.setReqM_PatricipantObjectDetail(ReqM_PatricipantObjectDetail);
		el.setResM_ParticipantObjectID(NullToEmptyString(ResM_ParticipantObjectID));
		el.setResM_PatricipantObjectDetail(ResM_PatricipantObjectDetail);
		el.setSourceip(NullToEmptyString(sourceip));
		el.setTargetip(NullToEmptyString(targetip));
		return el;
	}

	/**
	 * This method creates an EventLog object for use in Pivot Translation
	 *
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 * @param ET_ObjectID_in
	 *            The string encoded UUID of the source document
	 * @param ET_ObjectID_out
	 *            The string encoded UUID of the target document
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param targetip
	 *            The IP Address of the target Gateway
	 *
	 */
	public static EventLog createEventLogPivotTranslation(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String SP_UserID, String ET_ObjectID_in,
			String ET_ObjectID_out, String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail,
			String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail, String targetip) {

		ConfigurationManagerService cms = ConfigurationManagerService.getInstance();
        String auditSourceId = cms.getProperty("COUNTRY_PRINCIPAL_SUBDIVISION");
		return createEventLogPivotTranslation(EI_TransactionName, EI_EventActionCode, EI_EventDateTime, EI_EventOutcomeIndicator, SP_UserID, ET_ObjectID_in, ET_ObjectID_out, ReqM_ParticipantObjectID, ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID, ResM_PatricipantObjectDetail, targetip, auditSourceId);
	}

	/**
     * This method creates an EventLog object for use in Pivot Translation
     *
     * @param EI_EventActionCode Possible values according to D4.5.6 are E,R,U,D
     * @param EI_EventDateTime The datetime the event occured
     * @param EI_EventOutcomeIndicator <br>
     * 0 for full success <br>
     * 1 in case of partial delivery <br>
     * 4 for temporal failures <br>
     * 8 for permanent failure <br>
     * @param SP_UserID The string encoded CN of the TLS certificate of the NCP
     * processed the epsos operation
     * @param ET_ObjectID_in The string encoded UUID of the source document
     * @param ET_ObjectID_out The string encoded UUID of the target document
     * @param ReqM_ParticipantObjectID String-encoded UUID of the request
     * message
     * @param ReqM_PatricipantObjectDetail The value MUST contain the base64
     * encoded security header.
     * @param ResM_ParticipantObjectID String-encoded UUID of the response
     * message
     * @param ResM_PatricipantObjectDetail The value MUST contain the base64
     * encoded security header.
     * @param targetip The IP Address of the target Gateway
     *
     */
    public static EventLog createEventLogPivotTranslation(TransactionName EI_TransactionName, EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime, EventOutcomeIndicator EI_EventOutcomeIndicator,
            String SP_UserID, String ET_ObjectID_in,
            String ET_ObjectID_out,
            String ReqM_ParticipantObjectID,
            byte[] ReqM_PatricipantObjectDetail,
            String ResM_ParticipantObjectID,
            byte[] ResM_PatricipantObjectDetail,
            String targetip, String auditSourceId) {
        EventLog el = new EventLog();
        // Set Audit Source
        el.setAS_AuditSourceId(auditSourceId);
        	el.setEI_TransactionName(EI_TransactionName);
        el.setEI_EventActionCode(EI_EventActionCode);
        el.setEI_EventDateTime(EI_EventDateTime);
        el.setEI_EventOutcomeIndicator(EI_EventOutcomeIndicator);
        el.setSP_UserID(NullToEmptyString(SP_UserID));
        el.setET_ObjectID(NullToEmptyString(ET_ObjectID_in));
        el.setET_ObjectID_additional(NullToEmptyString(ET_ObjectID_out));
        el.setReqM_ParticipantObjectID(NullToEmptyString(ReqM_ParticipantObjectID));
        el.setReqM_PatricipantObjectDetail(ReqM_PatricipantObjectDetail);
        el.setResM_ParticipantObjectID(NullToEmptyString(ResM_ParticipantObjectID));
        el.setResM_PatricipantObjectDetail(ResM_PatricipantObjectDetail);
        el.setTargetip(NullToEmptyString(targetip));
        return el;
    }

	public static EventLog createEventLogPatientPrivacy(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String HR_UserID, String HR_AlternativeUserID,
			String HR_RoleID, String SC_UserID, String SP_UserID, String AS_AuditSourceId,
			String PT_PatricipantObjectID, String EM_PatricipantObjectID, byte[] EM_PatricipantObjectDetail,
			String ET_ObjectID, String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail,
			String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail, String sourceip, String targetip) {

		EventLog el = EventLog.createEventLogHCPAssurance(EI_TransactionName, EI_EventActionCode, EI_EventDateTime,
				EI_EventOutcomeIndicator, null, null, HR_UserID, HR_AlternativeUserID, HR_RoleID, SC_UserID, SP_UserID,
				AS_AuditSourceId, PT_PatricipantObjectID, EM_PatricipantObjectID, EM_PatricipantObjectDetail,
				ET_ObjectID, ReqM_ParticipantObjectID, ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID,
				ResM_PatricipantObjectDetail, sourceip, targetip);
		return el;
	}

	/**
	 * This method creates an EventLog object for use in Consent PIN
	 *
	 * @param TransactionName
	 *            value is epsosConsentServicePin
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param PC_UserID
	 *            Point of Care: Oid of the department
	 * @param PC_RoleID
	 *            Role of the point of care
	 * @param HR_UserID
	 *            Identifier of the HCP initiated the event
	 * @param HR_AlternativeUserID
	 *            Human readable name of the HCP as given in the Subject-ID
	 * @param HR_RoleID
	 *            Role of the HCP initiated the event
	 * @param SC_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            triggered the epsos operation
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 * @param AS_AuditSourceId
	 *            the iso3166-2 code of the country responsible for the audit
	 *            source
	 * @param PT_PatricipantObjectID
	 *            Patient Identifier in HL7 II format
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param sourceip
	 *            The IP Address of the source Gateway
	 * @param targetip
	 *            The IP Address of the target Gateway
	 * @return the EventLog object
	 *
	 */
	public static EventLog createEventLogConsentPINack(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String PC_UserID, String PC_RoleID, String HR_UserID,
			String HR_AlternativeUserID, String HR_RoleID, String SC_UserID, String SP_UserID, String AS_AuditSourceId,
			String PT_PatricipantObjectID, String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail,
			String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail, String sourceip, String targetip) {

		EventLog el = EventLog.createEventLogHCPAssurance(EI_TransactionName, EI_EventActionCode, EI_EventDateTime,
				EI_EventOutcomeIndicator, PC_UserID, PC_RoleID, HR_UserID, HR_AlternativeUserID, HR_RoleID, SC_UserID,
				SP_UserID, AS_AuditSourceId, PT_PatricipantObjectID, null, null, "PINack", ReqM_ParticipantObjectID,
				ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID, ResM_PatricipantObjectDetail, sourceip,
				targetip);
		return el;
	}

	/**
	 * This method creates an EventLog object for use in Consent PIN
	 *
	 * @param TransactionName
	 *            value is epsosConsentServicePin
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param PC_UserID
	 *            Point of Care: Oid of the department
	 * @param PC_RoleID
	 *            Role of the point of care
	 * @param HR_UserID
	 *            Identifier of the HCP initiated the event
	 * @param HR_AlternativeUserID
	 *            Human readable name of the HCP as given in the Subject-ID
	 * @param HR_RoleID
	 *            Role of the HCP initiated the event
	 * @param SC_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            triggered the epsos operation
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 * @param AS_AuditSourceId
	 *            the iso3166-2 code of the country responsible for the audit
	 *            source
	 * @param PT_PatricipantObjectID
	 *            Patient Identifier in HL7 II format
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param sourceip
	 *            The IP Address of the source Gateway
	 * @param targetip
	 *            The IP Address of the target Gateway
	 * @return the EventLog object
	 *
	 */
	public static EventLog createEventLogConsentPINdny(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String PC_UserID, String PC_RoleID, String HR_UserID,
			String HR_AlternativeUserID, String HR_RoleID, String SC_UserID, String SP_UserID, String AS_AuditSourceId,
			String PT_PatricipantObjectID, String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail,
			String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail, String sourceip, String targetip) {

		EventLog el = EventLog.createEventLogHCPAssurance(EI_TransactionName, EI_EventActionCode, EI_EventDateTime,
				EI_EventOutcomeIndicator, PC_UserID, PC_RoleID, HR_UserID, HR_AlternativeUserID, HR_RoleID, SC_UserID,
				SP_UserID, AS_AuditSourceId, PT_PatricipantObjectID, null, null, "PINdny", ReqM_ParticipantObjectID,
				ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID, ResM_PatricipantObjectDetail, sourceip,
				targetip);
		return el;
	}

	/**
	 * This method creates an EventLog object for use in HCP Assurance Schema
	 *
	 *
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param PC_UserID
	 *            Point of Care: Oid of the department
	 * @param PC_RoleID
	 *            Role of the point of care
	 * @param HR_UserID
	 *            Identifier of the HCP initiated the event
	 * @param HR_RoleID
	 *            Role of the HCP initiated the event
	 * @param HR_AlternativeUserID
	 *            Human readable name of the HCP as given in the Subject-ID
	 * @param SC_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            triggered the epsos operation
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 * @param AS_AuditSourceId
	 *            the iso3166-2 code of the country responsible for the audit
	 *            source
	 * @param PT_PatricipantObjectID
	 *            Patient Identifier in HL7 II format
	 * @param EM_PatricipantObjectID
	 *            The error code included with the response message
	 * @param EM_PatricipantObjectDetail
	 *            Contains the base64 encoded error message
	 * @param ET_ObjectID
	 *            The string encoded UUID of the returned document
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param sourceip
	 *            The IP Address of the source Gateway
	 * @param targetip
	 *            The IP Address of the target Gateway
	 * @return the EventLog object
	 *
	 */
	public static EventLog createEventLogHCPAssurance(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String PC_UserID, String PC_RoleID, String HR_UserID,
			String HR_AlternativeUserID, String HR_RoleID, String SC_UserID, String SP_UserID, String AS_AuditSourceId,
			String PT_PatricipantObjectID, String EM_PatricipantObjectID, byte[] EM_PatricipantObjectDetail,
			String ET_ObjectID, String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail,
			String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail, String sourceip, String targetip) {
		EventLog el = new EventLog();
		el.setEI_TransactionName(EI_TransactionName);
		el.setEI_EventActionCode(EI_EventActionCode);
		el.setEI_EventDateTime(EI_EventDateTime);
		el.setEI_EventOutcomeIndicator(EI_EventOutcomeIndicator);
		el.setPC_UserID(NullToEmptyString(PC_UserID));
		el.setPC_RoleID(NullToEmptyString(PC_RoleID));
		el.setHR_UserID(NullToEmptyString(HR_UserID));
		el.setHR_AlternativeUserID(NullToEmptyString(HR_AlternativeUserID));
		el.setHR_RoleID(NullToEmptyString(HR_RoleID));
		el.setSC_UserID(NullToEmptyString(SC_UserID));
		el.setSP_UserID(NullToEmptyString(SP_UserID));
		el.setAS_AuditSourceId(NullToEmptyString(AS_AuditSourceId));
		el.setPT_PatricipantObjectID(NullToEmptyString(PT_PatricipantObjectID));
		el.setEM_PatricipantObjectID(NullToEmptyString(EM_PatricipantObjectID));
		el.setEM_PatricipantObjectDetail(EM_PatricipantObjectDetail);
		el.setET_ObjectID(NullToEmptyString(ET_ObjectID));
		el.setReqM_ParticipantObjectID(NullToEmptyString(ReqM_ParticipantObjectID));
		el.setReqM_PatricipantObjectDetail(ReqM_PatricipantObjectDetail);
		el.setResM_ParticipantObjectID(NullToEmptyString(ResM_ParticipantObjectID));
		el.setResM_PatricipantObjectDetail(ResM_PatricipantObjectDetail);
		el.setSourceip(NullToEmptyString(sourceip));
		el.setTargetip(NullToEmptyString(targetip));
		return el;
	}

	public static EventLog createEventLogPAC(TransactionName EI_TransactionName, EventActionCode EI_EventActionCode,
			XMLGregorianCalendar EI_EventDateTime, EventOutcomeIndicator EI_EventOutcomeIndicator, String PC_UserID,
			String PC_RoleID, String HR_UserID, String HR_AlternativeUserID, String HR_RoleID, String SC_UserID,
			String SP_UserID, String AS_AuditSourceId, String PT_PatricipantObjectID, String EM_PatricipantObjectID,
			byte[] EM_PatricipantObjectDetail, String ET_ObjectID, String ReqM_ParticipantObjectID,
			byte[] ReqM_PatricipantObjectDetail, String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail,
			String sourceip, String targetip) {
		return EventLog.createEventLogHCPAssurance(EI_TransactionName, EI_EventActionCode, EI_EventDateTime,
				EI_EventOutcomeIndicator, PC_UserID, PC_RoleID, HR_UserID, HR_AlternativeUserID, HR_RoleID, SC_UserID,
				SP_UserID, AS_AuditSourceId, PT_PatricipantObjectID, EM_PatricipantObjectID, EM_PatricipantObjectDetail,
				ET_ObjectID, ReqM_ParticipantObjectID, ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID,
				ResM_PatricipantObjectDetail, sourceip, targetip);
	}

	public static EventLog createEventLogPatientService(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String PC_UserID, String PC_RoleID, String HR_UserID,
			String HR_AlternativeUserID, String HR_RoleID, String SC_UserID, String SP_UserID, String AS_AuditSourceId,
			String PT_PatricipantObjectID, String EM_PatricipantObjectID, byte[] EM_PatricipantObjectDetail,
			String ET_ObjectID, String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail,
			String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail, String sourceip, String targetip) {
		return EventLog.createEventLogHCPAssurance(EI_TransactionName, EI_EventActionCode, EI_EventDateTime,
				EI_EventOutcomeIndicator, PC_UserID, PC_RoleID, HR_UserID, HR_AlternativeUserID, HR_RoleID, SC_UserID,
				SP_UserID, AS_AuditSourceId, PT_PatricipantObjectID, EM_PatricipantObjectID, EM_PatricipantObjectDetail,
				ET_ObjectID, ReqM_ParticipantObjectID, ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID,
				ResM_PatricipantObjectDetail, sourceip, targetip);
	}

	/**
	 * This method creates an EventLog object for use in Issuance of a Treatment
	 * Relationship Confirmation Assertion
	 *
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param PC_UserID
	 *            Point of Care: Oid of the department
	 * @param PC_RoleID
	 *            Role of the Point of Care: Oid of the department
	 * @param HR_UserID
	 *            Identifier of the HCP initiated the event
	 * @param HR_RoleID
	 *            Role of HCP initiated the event
	 * @param HR_AlternativeUserID
	 *            Human readable name of the HCP as given in the Subject-ID
	 * @param SC_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            triggered the epsos operation
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 *
	 * @param AS_AuditSourceId
	 *            the iso3166-2 code of the country responsible for the audit
	 *            source
	 * @param PT_PatricipantObjectID
	 *            Patient Identifier in HL7 II format
	 * @param ET_ObjectID
	 *            The string encoded UUID of the returned document
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param sourceip
	 *            The IP Address of the source Gateway
	 * @param targetip
	 *            The IP Address of the target Gateway
	 * @return the EventLog object
	 *
	 */
	public static EventLog createEventLogTRCA(TransactionName EI_TransactionName, EventActionCode EI_EventActionCode,
			XMLGregorianCalendar EI_EventDateTime, EventOutcomeIndicator EI_EventOutcomeIndicator, String PC_UserID,
			String PC_RoleID, String HR_UserID, String HR_RoleID, String HR_AlternativeUserID, String SC_UserID,
			String SP_UserID, String AS_AuditSourceId, String PT_PatricipantObjectID, String ET_ObjectID,
			String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail, String ResM_ParticipantObjectID,
			byte[] ResM_PatricipantObjectDetail, String sourceip, String targetip) {
		EventLog el = new EventLog();
		el.setEI_TransactionName(EI_TransactionName);
		el.setEI_EventActionCode(EI_EventActionCode);
		el.setEI_EventDateTime(EI_EventDateTime);
		el.setEI_EventOutcomeIndicator(EI_EventOutcomeIndicator);
		el.setPC_UserID(NullToEmptyString(PC_UserID));
		el.setPC_RoleID(NullToEmptyString(PC_RoleID));
		el.setHR_UserID(NullToEmptyString(HR_UserID));
		el.setHR_RoleID(NullToEmptyString(HR_RoleID));
		el.setHR_AlternativeUserID(NullToEmptyString(HR_AlternativeUserID));
		el.setSC_UserID(NullToEmptyString(SC_UserID));
		el.setSP_UserID(NullToEmptyString(SP_UserID));
		el.setAS_AuditSourceId(NullToEmptyString(AS_AuditSourceId));
		el.setPT_PatricipantObjectID(NullToEmptyString(PT_PatricipantObjectID));
		el.setET_ObjectID(NullToEmptyString(ET_ObjectID));
		el.setReqM_ParticipantObjectID(NullToEmptyString(ReqM_ParticipantObjectID));
		el.setReqM_PatricipantObjectDetail(ReqM_PatricipantObjectDetail);
		el.setResM_ParticipantObjectID(NullToEmptyString(ResM_ParticipantObjectID));
		el.setResM_PatricipantObjectDetail(ResM_PatricipantObjectDetail);
		el.setSourceip(NullToEmptyString(sourceip));
		el.setTargetip(NullToEmptyString(targetip));
		return el;
	}

	/**
	 * This method creates an EventLog object for use in Patient Privacy Audit
	 * Schema
	 *
	 * @param EI_TransactionNumber
	 *            The number of transaction including the epsos- prefix
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param HR_UserID
	 *            Identifier of the HCP initiated the event
	 * @param HR_AlternativeUserID
	 *            Human readable name of the HCP as given in the Subject-ID
	 * @param SC_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            triggered the epsos operation
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 * @param AS_AuditSourceId
	 *            the iso3166-2 code of the country responsible for the audit
	 *            source
	 * @param PT_PatricipantObjectID
	 *            Patient Identifier in HL7 II format
	 * @param EM_PatricipantObjectID
	 *            The error code included with the response message
	 * @param EM_PatricipantObjectDetail
	 *            Contains the base64 encoded error message
	 * @param ET_ObjectID
	 *            The string encoded UUID of the returned document
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param sourceip
	 *            The IP Address of the source Gateway
	 * @param targetip
	 *            The IP Address of the target Gateway
	 * @return the EventLog object
	 *
	 */
	// public static EventLog createEventLogPrivacy(String EI_TransactionNumber,
	// TransactionName EI_TransactionName, EventActionCode EI_EventActionCode,
	// XMLGregorianCalendar EI_EventDateTime, EventOutcomeIndicator
	// EI_EventOutcomeIndicator,
	// String HR_UserID, String HR_AlternativeUserID,
	// String SC_UserID, String SP_UserID, String AS_AuditSourceId,
	// String PT_PatricipantObjectID, String EM_PatricipantObjectID, byte[]
	// EM_PatricipantObjectDetail,String ET_ObjectID,
	// String ReqM_ParticipantObjectID,
	// byte[] ReqM_PatricipantObjectDetail,
	// String ResM_ParticipantObjectID,
	// byte[] ResM_PatricipantObjectDetail,
	// String sourceip,
	// String targetip)
	// {
	// EventLog el = new EventLog();
	// el.setEI_TransactionNumber(NullToEmptyString(EI_TransactionNumber));
	// el.setEI_TransactionName(EI_TransactionName);
	// el.setEI_EventActionCode(EI_EventActionCode);
	// el.setEI_EventDateTime(EI_EventDateTime);
	// el.setEI_EventOutcomeIndicator(EI_EventOutcomeIndicator);
	// el.setHR_UserID(NullToEmptyString(HR_UserID));
	// el.setHR_AlternativeUserID(NullToEmptyString(HR_AlternativeUserID));
	// el.setSC_UserID(NullToEmptyString(SC_UserID));
	// el.setSP_UserID(NullToEmptyString(SP_UserID));
	// el.setAS_AuditSourceId(NullToEmptyString(AS_AuditSourceId));
	// el.setPT_PatricipantObjectID(NullToEmptyString(PT_PatricipantObjectID));
	// el.setEM_PatricipantObjectID(NullToEmptyString(EM_PatricipantObjectID));
	// el.setEM_PatricipantObjectDetail(EM_PatricipantObjectDetail);
	// el.setET_ObjectID(NullToEmptyString(ET_ObjectID));
	// el.setReqM_ParticipantObjectID(NullToEmptyString(ReqM_ParticipantObjectID));
	// el.setReqM_PatricipantObjectDetail(ReqM_PatricipantObjectDetail);
	// el.setResM_ParticipantObjectID(NullToEmptyString(ResM_ParticipantObjectID));
	// el.setResM_PatricipantObjectDetail(ResM_PatricipantObjectDetail);
	// el.setSourceip(NullToEmptyString(sourceip));
	// el.setTargetip(NullToEmptyString(targetip));
	// return el;
	// }
	/**
	 * This method creates an EventLog object for use in Patient ID Mapping
	 * Audit Schema
	 *
	 * @param EI_TransactionName
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param HR_UserID
	 *            Identifier of the HCP initiated the event
	 * @param HR_RoleID
	 *            Role of the HCP initiated the event
	 * @param HR_AlternativeUserID
	 *            Human readable name of the HCP as given in the Subject-ID
	 *            attrbute of the HCP identity assertion
	 * @param SC_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            triggered the epsos operation
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 * @param PC_UserID
	 *            Point of Care: Oid of the department
	 * @param AS_AuditSourceId
	 *            the iso3166-2 code of the country responsible for the audit
	 *            source
	 * @param PS_PatricipantObjectID
	 *            Patient Identifier in HL7 II format (Patient Source)
	 * @param PT_PatricipantObjectID
	 *            Patient Identifier in HL7 II format (Patient Target)
	 * @param EM_PatricipantObjectID
	 *            The error code included with the response message
	 * @param EM_PatricipantObjectDetail
	 *            Contains the base64 encoded error message
	 * @param MS_UserID
	 *            The string encoded OID of the service instance performed the
	 *            mapping
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param sourceip
	 *            The IP Address of the source Gateway
	 * @param targetip
	 *            The IP Address of the target Gateway
	 * @return the EventLog object
	 *
	 */
	public static EventLog createEventLogPatientMapping(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String HR_UserID, String HR_RoleID,
			String HR_AlternativeUserID, String SC_UserID, String SP_UserID, String AS_AuditSourceId,
			String PS_PatricipantObjectID, String PT_PatricipantObjectID, String EM_PatricipantObjectID,
			byte[] EM_PatricipantObjectDetail, String MS_UserID, String ReqM_ParticipantObjectID,
			byte[] ReqM_PatricipantObjectDetail, String ResM_ParticipantObjectID, byte[] ResM_PatricipantObjectDetail,
			String sourceip, String targetip) {
		EventLog el = new EventLog();
		el.setEI_TransactionName(EI_TransactionName);
		el.setEI_EventActionCode(EI_EventActionCode);
		el.setEI_EventDateTime(EI_EventDateTime);
		el.setEI_EventOutcomeIndicator(EI_EventOutcomeIndicator);
		el.setHR_UserID(NullToEmptyString(HR_UserID));
		el.setHR_RoleID(NullToEmptyString(HR_RoleID));
		el.setHR_AlternativeUserID(NullToEmptyString(HR_AlternativeUserID));
		el.setSC_UserID(NullToEmptyString(SC_UserID));
		el.setSP_UserID(NullToEmptyString(SP_UserID));
		el.setAS_AuditSourceId(NullToEmptyString(AS_AuditSourceId));
		el.setPS_PatricipantObjectID(NullToEmptyString(PS_PatricipantObjectID));
		el.setPT_PatricipantObjectID(NullToEmptyString(PT_PatricipantObjectID));
		el.setEM_PatricipantObjectID(NullToEmptyString(EM_PatricipantObjectID));
		el.setEM_PatricipantObjectDetail(EM_PatricipantObjectDetail);
		el.setMS_UserID(NullToEmptyString(MS_UserID));
		el.setReqM_ParticipantObjectID(NullToEmptyString(ReqM_ParticipantObjectID));
		el.setReqM_PatricipantObjectDetail(ReqM_PatricipantObjectDetail);
		el.setResM_ParticipantObjectID(NullToEmptyString(ResM_ParticipantObjectID));
		el.setResM_PatricipantObjectDetail(ResM_PatricipantObjectDetail);
		el.setSourceip(NullToEmptyString(sourceip));
		el.setTargetip(NullToEmptyString(targetip));
		return el;
	}

	/**
	 * This method creates an EventLog object for use in HCP Assurance Schema
	 *
	 * @param EI_EventActionCode
	 *            Possible values according to D4.5.6 are E,R,U,D
	 * @param EI_EventDateTime
	 *            The datetime the event occured
	 * @param EI_EventOutcomeIndicator
	 *            <br>
	 *            0 for full success <br>
	 *            1 in case of partial delivery <br>
	 *            4 for temporal failures <br>
	 *            8 for permanent failure <br>
	 * @param PC_UserID
	 *            Point of Care: Oid of the department
	 * @param PC_RoleID
	 *            Point of Care: Role of the department
	 * @param HR_UserID
	 *            Identifier of the HCP initiated the event
	 * @param HR_RoleID
	 *            Role of the HCP initiated the event
	 * @param HR_AlternativeUserID
	 *            Human readable name of the HCP as given in the Subject-ID
	 * @param SC_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            triggered the epsos operation
	 * @param SP_UserID
	 *            The string encoded CN of the TLS certificate of the NCP
	 *            processed the epsos operation
	 * @param PT_PatricipantObjectID
	 *            Patient Identifier in HL7 II format
	 * @param EM_PatricipantObjectID
	 *            The error code included with the response message
	 * @param EM_PatricipantObjectDetail
	 *            Contains the base64 encoded error message
	 * @param ReqM_ParticipantObjectID
	 *            String-encoded UUID of the request message
	 * @param ReqM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param ResM_ParticipantObjectID
	 *            String-encoded UUID of the response message
	 * @param ResM_PatricipantObjectDetail
	 *            The value MUST contain the base64 encoded security header.
	 * @param sourceip
	 *            The IP Address of the source Gateway
	 * @param targetip
	 *            The IP Address of the target Gateway
	 * @return the EventLog object
	 *
	 */
	public static EventLog createEventLogCommunicationFailure(TransactionName EI_TransactionName,
			EventActionCode EI_EventActionCode, XMLGregorianCalendar EI_EventDateTime,
			EventOutcomeIndicator EI_EventOutcomeIndicator, String PC_UserID, String PC_RoleID, String HR_UserID,
			String HR_RoleID, String HR_AlternativeUserID, String SC_UserID, String SP_UserID,
			String PT_PatricipantObjectID, String EM_PatricipantObjectID, byte[] EM_PatricipantObjectDetail,
			String ReqM_ParticipantObjectID, byte[] ReqM_PatricipantObjectDetail, String ResM_ParticipantObjectID,
			byte[] ResM_PatricipantObjectDetail, String sourceip, String targetip) {
		EventLog el = new EventLog();
		el.setEI_TransactionName(EI_TransactionName);
		el.setEI_EventActionCode(EI_EventActionCode);
		el.setEI_EventDateTime(EI_EventDateTime);
		el.setEI_EventOutcomeIndicator(EI_EventOutcomeIndicator);
		el.setPC_UserID(NullToEmptyString(PC_UserID));
		el.setPC_RoleID(NullToEmptyString(PC_RoleID));
		el.setHR_UserID(NullToEmptyString(HR_UserID));
		el.setHR_RoleID(NullToEmptyString(HR_RoleID));
		el.setHR_AlternativeUserID(NullToEmptyString(HR_AlternativeUserID));
		el.setSC_UserID(NullToEmptyString(SC_UserID));
		el.setSP_UserID(NullToEmptyString(SP_UserID));
		el.setPT_PatricipantObjectID(NullToEmptyString(PT_PatricipantObjectID));
		el.setEM_PatricipantObjectID(NullToEmptyString(EM_PatricipantObjectID));
		el.setEM_PatricipantObjectDetail(EM_PatricipantObjectDetail);
		el.setReqM_ParticipantObjectID(NullToEmptyString(ReqM_ParticipantObjectID));
		el.setReqM_PatricipantObjectDetail(ReqM_PatricipantObjectDetail);
		el.setResM_ParticipantObjectID(NullToEmptyString(ResM_ParticipantObjectID));
		el.setResM_PatricipantObjectDetail(ResM_PatricipantObjectDetail);
		el.setSourceip(NullToEmptyString(sourceip));
		el.setTargetip(NullToEmptyString(targetip));
		return el;
	}

	/**
	 *
	 * @param s
	 *            represents a string
	 * @return empty string if the param is null. Eitherwise returns the string
	 *         as is
	 */
	private static String NullToEmptyString(String s) {
		String ret = "";
		if (s == null) {
			ret = "";
		} else {
			ret = s;
		}
		return ret;
	}
}
