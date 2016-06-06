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
package tr.com.srdc.epsos.ws.xcpd.client.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.AdxpCity;
import org.hl7.v3.AdxpCountry;
import org.hl7.v3.AdxpPostalCode;
import org.hl7.v3.AdxpStreetName;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import tr.com.srdc.epsos.data.model.PatientDemographics;

/**
 * This class replicates
 * {@link tr.com.srdc.epsos.ws.xcpd.client.RespondingGateway_RequestSender} API
 * providing an implementation based on mock objects.
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class RespondingGateWay_RequestSender_Mock {
    
    /**
     * This method returns a Mock PRPAIN201306UV02 message. It uses some of the
     * PatientDemographics input parameter fields to fill the return message.
     *
     * @param pd Patient Demographics data.
     * @return A PRPAIN201306UV02 message.
     */
    public PRPAIN201306UV02 mock(final PatientDemographics pd) {
        final PRPAIN201306UV02 result = new PRPAIN201306UV02();

        final ObjectFactory of = new ObjectFactory();

        /* 
         * Message ID 
         */
        result.setId(of.createII());
        result.getId().setRoot("c13986c7-3246-49e6-81f5-4be87f69fbe6");
        result.getId().setExtension("1338476169909");

        /* 
         * ITSVersion element 
         */
        result.setITSVersion("XML_1.0");

        /* 
         * Creation Time
         */
        result.setCreationTime(of.createTS());
        final Date date = new Date();
        final SimpleDateFormat dateNow = new SimpleDateFormat("yyyyMMddkkmmss.SSSZZZZ");
        final StringBuilder nowDate = new StringBuilder(dateNow.format(date));
        result.getCreationTime().setValue(nowDate.toString());

        /* 
         * Version Code 
         */
        result.setVersionCode(of.createCS());
        result.getVersionCode().setCode("V3PR1");

        /*
         * Interaction ID 
         */
        result.setInteractionId(of.createII());
        result.getInteractionId().setRoot("2.16.840.1.113883.1.6");
        result.getInteractionId().setExtension("PRPA_IN201306UV02");

        /* 
         * Processing Code 
         */
        result.setProcessingCode(of.createCS());
        result.getProcessingCode().setCode("P");

        /* 
         * Processing Code Mode 
         */
        result.setProcessingModeCode(of.createCS());
        result.getProcessingModeCode().setCode("T");

        /* 
         * Accept Acknowledgement Code 
         */
        result.setAcceptAckCode(of.createCS());
        result.getAcceptAckCode().setCode("NE");

        /* 
         * Receiver
         */
        result.getReceiver().add(of.createMCCIMT000300UV01Receiver());
        /* Sender: TypeCode */
        result.getReceiver().get(0).setTypeCode(CommunicationFunctionType.RCV);
        /* Receiver > Device */
        result.getReceiver().get(0).setDevice(of.createMCCIMT000300UV01Device());
        /* Receiver > Device: ClassCode */
        result.getReceiver().get(0).getDevice().setClassCode(EntityClassDevice.DEV);
        /* Receiver > Device: DeterminerCode */
        result.getReceiver().get(0).getDevice().setDeterminerCode("INSTANCE");
        /* Receiver > Device > TypeID */
        result.getReceiver().get(0).getDevice().setTypeId(of.createII());
        result.getReceiver().get(0).getDevice().getTypeId().setRoot("2.16.17.710.806.1000.990.1");
        /* Receiver > Device > asAgent */
        result.getReceiver().get(0).getDevice().setAsAgent(of.createMCCIMT000300UV01DeviceAsAgent(null));
        /* Receiver > Device > asAgent > Value */
        result.getReceiver().get(0).getDevice().getAsAgent().setValue(of.createMCCIMT000300UV01Agent());
        /* Receiver > Device > asAgent > Value > Represented Organization */
        result.getReceiver().get(0).getDevice().getAsAgent().getValue().setRepresentedOrganization(of.createMCCIMT000300UV01AgentRepresentedOrganization(null));
        /* Receiver > Device > asAgent > Value > Represented Organization > Value*/
        result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().setValue(of.createMCCIMT000300UV01Organization());
        /* Receiver > Device > asAgent > Value > Represented Organization > ClassCode*/
        result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setClassCode("ORG");
        /* Receiver > Device > asAgent > Value > Represented Organization > DeterminerCode*/
        result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setDeterminerCode("INSTANCE");
        /* Receiver > Device > asAgent > Value > Represented Organization > ID*/
        result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setTypeId(of.createII());
        result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getTypeId().setRoot("2.16.17.710.806.1000.990.1");

        /* 
         * Sender 
         */
        result.setSender(of.createMCCIMT000300UV01Sender());
        /* Sender: TypeCode */
        result.getSender().setTypeCode(CommunicationFunctionType.SND);
        /* Sender > Device */
        result.getSender().setDevice(of.createMCCIMT000300UV01Device());
        /* Sender > Device: ClassCode */
        result.getSender().getDevice().setClassCode(EntityClassDevice.DEV);
        /* Sender > Device: DeterminerCode */
        result.getSender().getDevice().setDeterminerCode("INSTANCE");
        /* Sender > Device > TypeID */
        result.getSender().getDevice().setTypeId(of.createII());
        result.getSender().getDevice().getTypeId().setRoot("2.16.17.710.806.1000.990.1");
        /* Sender > Device > asAgent */
        result.getSender().getDevice().setAsAgent(of.createMCCIMT000300UV01DeviceAsAgent(null));
        /* Sender > Device > asAgent > Value */
        result.getSender().getDevice().getAsAgent().setValue(of.createMCCIMT000300UV01Agent());
        /* Sender > Device > asAgent > Value > Represented Organization */
        result.getSender().getDevice().getAsAgent().getValue().setRepresentedOrganization(of.createMCCIMT000300UV01AgentRepresentedOrganization(null));
        /* Sender > Device > asAgent > Value > Represented Organization > Value*/
        result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().setValue(of.createMCCIMT000300UV01Organization());
        /* Sender > Device > asAgent > Value > Represented Organization > ClassCode*/
        result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setClassCode("ORG");
        /* Sender > Device > asAgent > Value > Represented Organization > DeterminerCode*/
        result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setDeterminerCode("INSTANCE");
        /* Sender > Device > asAgent > Value > Represented Organization > ID*/
        result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().setTypeId(of.createII());
        result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getTypeId().setRoot("2.16.17.710.806.1000.990.1");

        /* 
         * Acknowledgement 
         */
        result.getAcknowledgement().add(of.createMCCIMT000300UV01Acknowledgement());
        /* Acknowledgement > TypeCode */
        result.getAcknowledgement().get(0).setTypeCode(of.createCS());
        result.getAcknowledgement().get(0).getTypeCode().setCode("AA");
        /* Acknowledgement > TargetMessage */
        result.getAcknowledgement().get(0).setTargetMessage(of.createMCCIMT000300UV01TargetMessage());
        /* Acknowledgement > TargetMessage > ID */
        result.getAcknowledgement().get(0).getTargetMessage().setId(of.createII());
        result.getAcknowledgement().get(0).getTargetMessage().getId().setRoot("fd6c13c0-cd3d-455c-aa92-803eb2be9fdd");
        result.getAcknowledgement().get(0).getTargetMessage().getId().setExtension("1338476149099");

        /* 
         * Security Test N/A 
         */
        //result.setSecurityText(null);

        /* 
         * Type ID N/A
         */
        //result.setTypeId(null);

        /*
         * ControlActProcess
         */
        result.setControlActProcess(of.createPRPAIN201306UV02MFMIMT700711UV01ControlActProcess());
        /* ControlActProcess > Code*/
        result.getControlActProcess().setCode(of.createCD());
        result.getControlActProcess().getCode().setCode("PRPA_TE201306UV02");
        /* ControlActProcess > Subject */
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = of.createPRPAIN201306UV02MFMIMT700711UV01Subject1();
        /* ControlActProcess > Subject > RegistrationEvent*/
        subject.setRegistrationEvent(of.createPRPAIN201306UV02MFMIMT700711UV01RegistrationEvent());
        /* ControlActProcess > Subject > RegistrationEvent > ID N/A */
        //TODO: Place nullflavour="NA" here.
        //subject.getRegistrationEvent().getId().add(of.createII());
        /* ControlActProcess > Subject > RegistrationEvent > StatusCode */
        subject.getRegistrationEvent().setStatusCode(of.createCS());
        subject.getRegistrationEvent().getStatusCode().setCode("active");
        /* ControlActProcess > Subject > RegistrationEvent > Subject1 */
        subject.getRegistrationEvent().setSubject1(of.createPRPAIN201306UV02MFMIMT700711UV01Subject2());
        /* ControlActProcess > Subject > RegistrationEvent > Patient */
        subject.getRegistrationEvent().getSubject1().setPatient(of.createPRPAMT201310UV02Patient());
        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson */
        subject.getRegistrationEvent().getSubject1().getPatient().setPatientPerson(of.createPRPAMT201310UV02PatientPatientPerson(null));
        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value */
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().setValue(of.createPRPAMT201310UV02Person());
        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > ID */
        II ii = of.createII();
        if (StringUtils.isEmpty(pd.getId())) {
            ii.setExtension("123456789");
        } else {
            ii.setExtension(pd.getId());
        }
        subject.getRegistrationEvent().getSubject1().getPatient().getId().add(ii);
        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > HomeCommunityID */
        if (pd.getId().isEmpty()) {
            subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).setRoot("2.16.17.710.806.1000.990.1");
        } else {
            subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).setRoot(pd.getHomeCommunityId());
        }
        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > AdministrativeGender*/
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().setAdministrativeGenderCode(of.createCE());
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode().setCode(pd.getAdministrativeGender().toString());

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > BirthTime */
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().setBirthTime(of.createTS());
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime().setValue("19800119000000+0100");
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().add(of.createPN());

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > Name */
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().add(of.createPN());

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > Name > GivenName*/
        EnGiven givenName = of.createEnGiven();

        if (StringUtils.isEmpty(pd.getGivenName())) {
            givenName.setContent("Joana");
        } else {
            givenName.setContent(pd.getGivenName());
        }
        JAXBElement<EnGiven> jaxbGivenName = of.createENGiven(givenName);

        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0).getContent().add(jaxbGivenName);

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > Name > FamilyName*/
        EnFamily familyName = of.createEnFamily();
        if (StringUtils.isEmpty(pd.getFamilyName())) {
            familyName.setContent("Sousa");
        } else {
            familyName.setContent(pd.getFamilyName());
        }
        JAXBElement<EnFamily> jaxbFamilynName = of.createENFamily(familyName);

        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0).getContent().add(jaxbFamilynName);

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > LanguageCommunication */
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getLanguageCommunication().add(of.createPRPAMT201310UV02LanguageCommunication());
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getLanguageCommunication().get(0).setLanguageCode(of.createCE());
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getLanguageCommunication().get(0).getLanguageCode().setCode("pt-PT");

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > LanguageCommunication */
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAddr().add(of.createAD());

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > Address > Country */
        AdxpCountry country = of.createAdxpCountry();
        if (StringUtils.isEmpty(pd.getCountry())) {
            country.setContent("Portugal");
        } else {
            country.setContent(pd.getCountry());
        }
        JAXBElement<AdxpCountry> countryJabxElement = of.createADCountry(country);
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAddr().get(0).getContent().add(countryJabxElement);

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > Address > Street */
        AdxpStreetName street = of.createAdxpStreetName();
        if (StringUtils.isEmpty(pd.getStreetAddress())) {
            street.setContent("Rua de Ceuta, Nº7");
        } else {
            street.setContent(pd.getStreetAddress());
        }
        JAXBElement<AdxpStreetName> streetJabxElement = of.createADStreetName(street);
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAddr().get(0).getContent().add(streetJabxElement);

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > Address > City */
        AdxpCity city = of.createAdxpCity();
        if (StringUtils.isEmpty(pd.getCity())) {
            city.setContent("Aveiro");
        } else {
            city.setContent(pd.getCity());
        }
        JAXBElement<AdxpCity> cityJabxElement = of.createADCity(city);
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAddr().get(0).getContent().add(cityJabxElement);

        /* ControlActProcess > Subject > RegistrationEvent > PatientPerson > Value > Address > City */
        AdxpPostalCode postalCode = of.createAdxpPostalCode();
        if (StringUtils.isEmpty(pd.getPostalCode())) {
            postalCode.setContent("3800-001");
        } else {
            postalCode.setContent(pd.getPostalCode());
        }
        JAXBElement<AdxpPostalCode> pcJabxElement = of.createADPostalCode(postalCode);
        subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAddr().get(0).getContent().add(pcJabxElement);

        result.getControlActProcess().getSubject().add(subject);

        return result;
    }
}
