/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.dts.xcpd;

import eu.epsos.util.xcpd.XCPDConstants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.AdxpCity;
import org.hl7.v3.AdxpCountry;
import org.hl7.v3.AdxpPostalCode;
import org.hl7.v3.AdxpStreetName;
import org.hl7.v3.COCTMT090100UV01AssignedPerson;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02MatchCriterionList;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.XActMoodIntentEvent;
import org.hl7.v3.XParticipationAuthorPerformer;
import tr.com.srdc.epsos.data.model.PatientDemographics;
import tr.com.srdc.epsos.data.model.PatientId;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.DateUtil;

/**
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public final class PRPAIN201305UV022DTS {

    public static PRPAIN201305UV02 newInstance(PatientDemographics pd, String dstHomeCommunityId) {
        /*
         * pRPA_IN201305UV022 cration
         */
        PRPAIN201305UV02 pRPA_IN201305UV022 = new PRPAIN201305UV02();

        ObjectFactory of = new ObjectFactory();

        // Set ITS_VERSION element
        pRPA_IN201305UV022.setITSVersion(XCPDConstants.ITS_VERSION);

        // Set id of the message
        pRPA_IN201305UV022.setId(of.createII());
        pRPA_IN201305UV022.getId().setRoot(UUID.randomUUID().toString());
        pRPA_IN201305UV022.getId().setExtension(UUID.randomUUID().toString());

        // Set creationTime
        pRPA_IN201305UV022.setCreationTime(of.createTS());
        Date date = new Date();

        SimpleDateFormat dateNow = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        StringBuilder nowDate = new StringBuilder(dateNow.format(date));
        pRPA_IN201305UV022.getCreationTime().setValue(nowDate.toString());

        // Set versionCode
        pRPA_IN201305UV022.setVersionCode(of.createCS());
        pRPA_IN201305UV022.getVersionCode().setCode(XCPDConstants.HL7_VERSION);

        // Set interactionId
        pRPA_IN201305UV022.setInteractionId(of.createII());
        pRPA_IN201305UV022.getInteractionId().setRoot(XCPDConstants.INTERACTION_IDS_NAMESPACE);
        pRPA_IN201305UV022.getInteractionId().setExtension(XCPDConstants.PATIENT_DISCOVERY_REQUEST);

        // Set processingCode
        pRPA_IN201305UV022.setProcessingCode(of.createCS());
        pRPA_IN201305UV022.getProcessingCode().setCode(XCPDConstants.PROCESSING_CODE);

        // Set processingModeCode
        pRPA_IN201305UV022.setProcessingModeCode(of.createCS());
        pRPA_IN201305UV022.getProcessingModeCode().setCode(XCPDConstants.PROCESSING_MODE_CODE);

        // Set acceptAckCode
        pRPA_IN201305UV022.setAcceptAckCode(of.createCS());
        pRPA_IN201305UV022.getAcceptAckCode().setCode(XCPDConstants.ACCEPT_ACK_CODE);

        // Create receiver
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        pRPA_IN201305UV022.getReceiver().add(receiver);
        CommunicationFunctionType rTypeCode = CommunicationFunctionType.RCV;
        pRPA_IN201305UV022.getReceiver().get(0).setTypeCode(rTypeCode);

        // Create receiver/device
        MCCIMT000100UV01Device rDevice = new MCCIMT000100UV01Device();
        pRPA_IN201305UV022.getReceiver().get(0).setDevice(rDevice);
        EntityClassDevice rClassCode = EntityClassDevice.DEV;
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().setClassCode(rClassCode);
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().setDeterminerCode(XCPDConstants.DETERMINER_CODE_INSTANCE);

        // Set receiver/device/id
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().getId().add(of.createII());
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().getId().get(0).setRoot(dstHomeCommunityId);

        // Create receiver/device/asAgent
        MCCIMT000100UV01Agent rAgent = new MCCIMT000100UV01Agent();
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().setAsAgent(of.createMCCIMT000100UV01DeviceAsAgent(rAgent));
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().getAsAgent().getValue().getClassCode().add(XCPDConstants.CLASS_CODE_AGNT);

        // Create receiver/device/asAgent/representedOrganization
        MCCIMT000100UV01Organization rOrganization = new MCCIMT000100UV01Organization();
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().getAsAgent().getValue().setRepresentedOrganization(of.createMCCIMT000100UV01AgentRepresentedOrganization(rOrganization));
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setClassCode(XCPDConstants.CLASS_CODE_ORG);
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setDeterminerCode(XCPDConstants.DETERMINER_CODE_INSTANCE);

        // Set receiver/device/asAgent/representedOrganization/id
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().add(of.createII());
        pRPA_IN201305UV022.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).setRoot(dstHomeCommunityId);

        // Create sender
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        pRPA_IN201305UV022.setSender(sender);
        CommunicationFunctionType sTypeCode = CommunicationFunctionType.SND;
        pRPA_IN201305UV022.getSender().setTypeCode(sTypeCode);

        // Create sender/device
        MCCIMT000100UV01Device sDevice = new MCCIMT000100UV01Device();
        pRPA_IN201305UV022.getSender().setDevice(sDevice);
        EntityClassDevice sClassCode = EntityClassDevice.DEV;
        pRPA_IN201305UV022.getSender().getDevice().setClassCode(sClassCode);
        pRPA_IN201305UV022.getSender().getDevice().setDeterminerCode(XCPDConstants.DETERMINER_CODE_INSTANCE);

        // Set sender/device/id
        pRPA_IN201305UV022.getSender().getDevice().getId().add(of.createII());
        pRPA_IN201305UV022.getSender().getDevice().getId().get(0).setRoot(Constants.HOME_COMM_ID);

        // Create sender/device/asAgent
        MCCIMT000100UV01Agent sAgent = new MCCIMT000100UV01Agent();
        pRPA_IN201305UV022.getSender().getDevice().setAsAgent(of.createMCCIMT000100UV01DeviceAsAgent(sAgent));
        pRPA_IN201305UV022.getSender().getDevice().getAsAgent().getValue().getClassCode().add(XCPDConstants.CLASS_CODE_AGNT);

        // Create sender/device/asAgent/representedOrganization
        MCCIMT000100UV01Organization sOrganization = new MCCIMT000100UV01Organization();
        pRPA_IN201305UV022.getSender().getDevice().getAsAgent().getValue().setRepresentedOrganization(of.createMCCIMT000100UV01AgentRepresentedOrganization(sOrganization));
        pRPA_IN201305UV022.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setClassCode(XCPDConstants.CLASS_CODE_ORG);
        pRPA_IN201305UV022.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setDeterminerCode(XCPDConstants.DETERMINER_CODE_INSTANCE);

        // Set sender/device/asAgent/representedOrganization/id
        pRPA_IN201305UV022.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().add(of.createII());
        pRPA_IN201305UV022.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).setRoot(Constants.HOME_COMM_ID);

        // Create controlActProcess
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        pRPA_IN201305UV022.setControlActProcess(controlActProcess);
        ActClassControlAct cClassCode = ActClassControlAct.CACT;
        pRPA_IN201305UV022.getControlActProcess().setClassCode(cClassCode);
        XActMoodIntentEvent moodCode = XActMoodIntentEvent.EVN;
        pRPA_IN201305UV022.getControlActProcess().setMoodCode(moodCode);

        // Set controlActProcess/code
        pRPA_IN201305UV022.getControlActProcess().setCode(of.createCD());
        pRPA_IN201305UV022.getControlActProcess().getCode().setCode(XCPDConstants.CONTROL_ACT_PROCESS.CODE);
        pRPA_IN201305UV022.getControlActProcess().getCode().setCodeSystemName(XCPDConstants.INTERACTION_IDS_NAMESPACE);

        // Create controlActProcess/authorOrPerformer
        QUQIMT021001UV01AuthorOrPerformer aop = new QUQIMT021001UV01AuthorOrPerformer();
        pRPA_IN201305UV022.getControlActProcess().getAuthorOrPerformer().add(aop);
        XParticipationAuthorPerformer typeCode = XParticipationAuthorPerformer.AUT;
        pRPA_IN201305UV022.getControlActProcess().getAuthorOrPerformer().get(0).setTypeCode(typeCode);

        // Set controlActProcess/authorOrPerformer/assignedPerson
        COCTMT090100UV01AssignedPerson ap = new COCTMT090100UV01AssignedPerson();
        pRPA_IN201305UV022.getControlActProcess().getAuthorOrPerformer().get(0).setAssignedPerson(of.createMFMIMT700711UV01AuthorOrPerformerAssignedPerson(ap));
        pRPA_IN201305UV022.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedPerson().getValue().setClassCode(XCPDConstants.CLASS_CODE_ASSIGNED);

        // Create controlActProcess/queryByParameter
        PRPAMT201306UV02QueryByParameter queryByParameter = of.createPRPAMT201306UV02QueryByParameter();
        pRPA_IN201305UV022.getControlActProcess().setQueryByParameter(of.createPRPAIN201306UV02MFMIMT700711UV01ControlActProcessQueryByParameter(queryByParameter));

        // Set controlActProcess/queryByParameter/queryId
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().setQueryId(of.createII());
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getQueryId().setRoot(XCPDConstants.CONTROL_ACT_PROCESS.QUERY_BY_PARAMETER_ID_ROOT);

        // Set controlActProcess/queryByParameter/statusCode
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().setStatusCode(of.createCS());
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getStatusCode().setCode(XCPDConstants.CONTROL_ACT_PROCESS.QUERY_BY_PARAMETER_STATUS_CODE);

        // Set controlActProcess/queryByParameter/responseModalityCode
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().setResponseModalityCode(of.createCS());
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getResponseModalityCode().setCode(XCPDConstants.CONTROL_ACT_PROCESS.QUERY_BY_PARAMETER_RESPONSE_MODALITY_CODE);

        // Set controlActProcess/queryByParameter/responsePriorityCode
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().setResponsePriorityCode(of.createCS());
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getResponsePriorityCode().setCode(XCPDConstants.CONTROL_ACT_PROCESS.QUERY_BY_PARAMETER_RESPONSE_PRIORITY_CODE);

        // Set controlActProcess/queryByParameter/matchCriterionList
        PRPAMT201306UV02MatchCriterionList matchCriterionList = new PRPAMT201306UV02MatchCriterionList();
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().setMatchCriterionList(of.createPRPAMT201306UV02QueryByParameterMatchCriterionList(matchCriterionList));

        // Create controlActProcess/queryByParameter/parameterList
        PRPAMT201306UV02ParameterList parameterList = new PRPAMT201306UV02ParameterList();
        pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().setParameterList(parameterList);

        if (pd.getAdministrativeGender() != null) {
            // Create controlActProcess/queryByParameter/parameterList/livingSubjectAdministrativeGender
            PRPAMT201306UV02LivingSubjectAdministrativeGender administrativeGender = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().add(administrativeGender);

            // Set controlActProcess/queryByParameter/parameterList/livingSubjectAdministrativeGender/value
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().get(0).getValue().add(of.createCE());
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().get(0).getValue().get(0).setCode(pd.getAdministrativeGender().toString());

            // Set controlActProcess/queryByParameter/parameterList/livingSubjectAdministrativeGender/semanticsText
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().get(0).setSemanticsText(of.createST());
        }

        if (pd.getBirthDate() != null) {
            // Create controlActProcess/queryByParameter/parameterList/livingSubjectBirthTime
            PRPAMT201306UV02LivingSubjectBirthTime birthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().add(birthTime);

            // Set controlActProcess/queryByParameter/parameterList/livingSubjectBirthTime/value
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().get(0).getValue().add(of.createIVLTS());
            StringBuilder birthDate = new StringBuilder(dateNow.format(pd.getBirthDate()));
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().get(0).getValue().get(0).setValue(birthDate.toString());

            // Set controlActProcess/queryByParameter/parameterList/livingSubjectBirthTime/semanticsText
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().get(0).setSemanticsText(of.createST());
        }

        if (pd.getIdList() != null) {
            for (PatientId patId : pd.getIdList()) {
                // Create controlActProcess/queryByParameter/parameterList/livingSubjectId
                PRPAMT201306UV02LivingSubjectId id = new PRPAMT201306UV02LivingSubjectId();
                // Set controlActProcess/queryByParameter/parameterList/livingSubjectId/value
                II ii = of.createII();
                ii.setRoot(patId.getRoot());
                ii.setExtension(patId.getExtension());
                id.getValue().add(ii);
                // Set controlActProcess/queryByParameter/parameterList/livingSubjectId/semanticsText
                id.setSemanticsText(of.createST());

                pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().add(id);
            }
        }

        if ((pd.getFamilyName() != null) || (pd.getGivenName() != null)) {
            // Set controlActProcess/queryByParameter/parameterList/livingSubjectName
            PRPAMT201306UV02LivingSubjectName name = new PRPAMT201306UV02LivingSubjectName();
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().add(name);

            // Create controlActProcess/queryByParameter/parameterList/livingSubjectName/value
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().get(0).getValue().add(of.createEN());

            if (pd.getFamilyName() != null) {
                // Set controlActProcess/queryByParameter/parameterList/livingSubjectName/value/family
                EnFamily family = of.createEnFamily();
                family.setContent(pd.getFamilyName());
                pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().get(0).getValue().get(0).getContent().add(of.createENFamily(family));
            }
            if (pd.getGivenName() != null) {
                // Set controlActProcess/queryByParameter/parameterList/livingSubjectName/value/given
                EnGiven given = of.createEnGiven();
                given.setContent(pd.getGivenName());
                pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().get(0).getValue().get(0).getContent().add(of.createENGiven(given));
            }

            // Set controlActProcess/queryByParameter/parameterList/livingSubjectName/semanticsText
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().get(0).setSemanticsText(of.createST());
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().get(0).getSemanticsText().setContent(XCPDConstants.CONTROL_ACT_PROCESS.QUERY_BY_PARAMETER_LIVING_SUBJECT_NAME_SEMANTICS);
        }

        if ((pd.getCity() != null) || (pd.getCountry() != null) || (pd.getPostalCode() != null) || (pd.getStreetAddress() != null)) {
            // Create controlActProcess/queryByParameter/parameterList/patientAddress
            PRPAMT201306UV02PatientAddress address = new PRPAMT201306UV02PatientAddress();
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientAddress().add(address);

            // Create controlActProcess/queryByParameter/parameterList/patientAddress/value
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientAddress().get(0).getValue().add(of.createAD());

            if (pd.getStreetAddress() != null) {
                // Set controlActProcess/queryByParameter/parameterList/patientAddress/value/streetName
                AdxpStreetName street = of.createAdxpStreetName();
                street.setContent(pd.getStreetAddress());
                pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientAddress().get(0).getValue().get(0).getContent().add(of.createADStreetName(street));
            }
            if (pd.getCity() != null) {
                // Set controlActProcess/queryByParameter/parameterList/patientAddress/value/city
                AdxpCity city = of.createAdxpCity();
                city.setContent(pd.getCity());
                pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientAddress().get(0).getValue().get(0).getContent().add(of.createADCity(city));
            }
            if (pd.getCountry() != null) {
                // Set controlActProcess/queryByParameter/parameterList/patientAddress/value/country
                AdxpCountry country = of.createAdxpCountry();
                country.setContent(pd.getCountry());
                pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientAddress().get(0).getValue().get(0).getContent().add(of.createADCountry(country));
            }
            if (pd.getPostalCode() != null) {
                // Set controlActProcess/queryByParameter/parameterList/patientAddress/value/postalCode
                AdxpPostalCode postalCode = of.createAdxpPostalCode();
                postalCode.setContent(pd.getPostalCode());
                pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientAddress().get(0).getValue().get(0).getContent().add(of.createADPostalCode(postalCode));
            }

            // Set controlActProcess/queryByParameter/parameterList/patientAddress/semanticsText
            pRPA_IN201305UV022.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientAddress().get(0).setSemanticsText(of.createST());
        }

        return pRPA_IN201305UV022;
    }

    private PRPAIN201305UV022DTS() {}
}
