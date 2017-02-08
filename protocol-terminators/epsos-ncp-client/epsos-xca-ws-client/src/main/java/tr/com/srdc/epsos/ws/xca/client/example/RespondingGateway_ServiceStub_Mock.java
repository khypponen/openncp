/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
package tr.com.srdc.epsos.ws.xca.client.example;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.apache.axiom.om.*;
import org.apache.axis2.util.XMLUtils;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import tr.com.srdc.epsos.util.XMLUtil;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import java.util.UUID;

/**
 * Represents a mock of the RespondingGateway_ServiceStub class, with
 * corresponding mock operations and results.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class RespondingGateway_ServiceStub_Mock {

    private static final Logger logger = LoggerFactory.getLogger(RespondingGateway_ServiceStub_Mock.class);

    private oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory ofRim;
    private ihe.iti.xds_b._2007.ObjectFactory ofXds;
    private OMFactory factory;
    private final String HCID = "2.16.17.710.820.1000.990.1.1.1";
    private final String RUID = "2.16.17.710.820.1000.990.1.1.1";
    private final String DOCID = "urn:oid:2.16.17.710.820.1000.990.1.1.1.6b13180-a63e-11e1-b3dd-0800200c9a61";

    /**
     * Class constructor.
     */
    public RespondingGateway_ServiceStub_Mock() {
        ofRim = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        ofXds = new ihe.iti.xds_b._2007.ObjectFactory();
        factory = OMAbstractFactory.getOMFactory();
    }

    public AdhocQueryResponse respondingGateway_CrossGatewayQuery(AdhocQueryRequest adhocQueryRequest,
                                                                  Assertion idAssertion,
                                                                  Assertion trcAssertion) {

        AdhocQueryResponse mockResponse = new AdhocQueryResponse();

        mockResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
        // Create Registry Object List
        mockResponse.setRegistryObjectList(ofRim.createRegistryObjectListType());
        String xmlUUID = "";
        ExtrinsicObjectType eotXML = ofRim.createExtrinsicObjectType();
        xmlUUID = prepareExtrinsicObjectPS(eotXML, false, DOCID);
        mockResponse.getRegistryObjectList().getIdentifiable().add(ofRim.createExtrinsicObject(eotXML));

        return mockResponse;
    }

    public RetrieveDocumentSetResponseType respondingGateway_CrossGatewayRetrieve(RetrieveDocumentSetRequestType retrieveDocumentSetRequest,
                                                                                  Assertion idAssertion,
                                                                                  Assertion trcAssertion) {

        RetrieveDocumentSetResponseType mockResponse = ofXds.createRetrieveDocumentSetResponseType();

        try {

            OMNamespace ns = factory.createOMNamespace("urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", "");
            OMElement registryResponse = factory.createOMElement("RegistryResponse", ns);

            OMNamespace ns2 = factory.createOMNamespace("DocumentResponseNameSpace", "DRNS"); //Not sure if correct. 
            OMElement documentResponse = factory.createOMElement("DocumentResponse", ns2);

            OMElement homeCommunityId = factory.createOMElement("HomeCommunityId", ns2);
            homeCommunityId.setText(HCID);
            documentResponse.addChild(homeCommunityId);

            OMElement repositoryUniqueId = factory.createOMElement("RepositoryUniqueId", ns2);
            repositoryUniqueId.setText(RUID);
            documentResponse.addChild(repositoryUniqueId);

            OMElement documentUniqueId = factory.createOMElement("DocumentUniqueId", ns2);
            documentUniqueId.setText(DOCID);
            documentResponse.addChild(documentUniqueId);

            OMElement mimeType = factory.createOMElement("mimeType", ns2);
            mimeType.setText("text/xml");
            documentResponse.addChild(mimeType);

            mockResponse.getDocumentResponse().add(ofXds.createRetrieveDocumentSetResponseTypeDocumentResponse());
            mockResponse.getDocumentResponse().get(0).setDocumentUniqueId(DOCID);
            mockResponse.getDocumentResponse().get(0).setHomeCommunityId(HCID);
            mockResponse.getDocumentResponse().get(0).setRepositoryUniqueId(RUID);
            mockResponse.getDocumentResponse().get(0).setMimeType("text/xml");

            OMElement document = factory.createOMElement("Document", factory.createOMNamespace("urn:ihe:iti:xds-b:2007", ""));

            Document doc = XMLUtil.parseContent(DummyCDA.CDA);

            mockResponse.getRegistryResponse().setStatus("urn:ihe:iti:2007:ResponseStatusType:PartialSuccess");
            registryResponse.addAttribute(factory.createOMAttribute("status", null, "urn:ihe:iti:2007:ResponseStatusType:PartialSuccess"));


            ByteArrayDataSource dataSource = new ByteArrayDataSource(XMLUtils.toOM(doc.getDocumentElement()).toString().getBytes(), "text/xml;charset=UTF-8");
            DataHandler dataHandler = new DataHandler(dataSource);
            OMText textData = factory.createOMText(dataHandler, true);
            textData.setOptimize(true);
            document.addChild(textData);

            mockResponse.getDocumentResponse().get(0).setDocument(document.getText().getBytes("UTF-8"));
            documentResponse.addChild(document);

        } catch (Exception ex) {
            logger.error(null, ex);
        }

        return mockResponse;
    }

    private String prepareExtrinsicObjectPS(ExtrinsicObjectType eot, Boolean isPDF, String documentId) {
        String name = "Patient Summary";
        String uuid = "urn:uuid:" + UUID.randomUUID().toString();
        // Set Extrinsic Object
        eot.setStatus("urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");
        eot.setHome("2.16.17.710.820.1000.990.1.1.1");
        eot.setId(uuid);
        eot.setLid(uuid);
        eot.setObjectType("urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1");

        // Status
        eot.setMimeType("text/xml");

        // Name
        eot.setName(ofRim.createInternationalStringType());
        eot.getName().getLocalizedString().add(ofRim.createLocalizedStringType());
        eot.getName().getLocalizedString().get(0).setValue("Patient Summary");

        // Description (optional)
        eot.setDescription(ofRim.createInternationalStringType());
        eot.getDescription().getLocalizedString().add(ofRim.createLocalizedStringType());
        if (isPDF) {
            eot.getDescription().getLocalizedString().get(0).setValue("The Patient Summary document (CDA L1 / PDF body) for patient 123456789");
        } else {
            eot.getDescription().getLocalizedString().get(0).setValue("The Patient Summary document (CDA L3 / Structured body) for patient 123456789");
        }

        // Version Info
        eot.setVersionInfo(ofRim.createVersionInfoType());
        eot.getVersionInfo().setVersionName("1.1");

        // Creation Date (optional)
        if (isPDF) {
            eot.getSlot().add(makeSlot("creationTime", "20120817091805"));
        } else {
            eot.getSlot().add(makeSlot("creationTime", "20120817091805"));
        }

        // Source Patient Id
        eot.getSlot().add(makeSlot("sourcePatientId", "123456789^^^&amp;2.16.17.710.820.1000.990.1&amp;ISO"));

        // LanguageCode (optional)
        eot.getSlot().add(makeSlot("languageCode", "PT"));

        // repositoryUniqueId (optional)
        eot.getSlot().add(makeSlot("repositoryUniqueId", "2.16.17.710.820.1000.990.1.1.1"));


        eot.getClassification().add(makeClassification(
                "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a",
                uuid,
                "60591-5",
                "2.16.840.1.113883.6.1",
                name));
        // Type code (not written in 3.4.2)
        eot.getClassification().add(makeClassification(
                "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983",
                uuid,
                "60591-5",
                "2.16.840.1.113883.6.1",
                name));
        // Confidentiality Code
        eot.getClassification().add(makeClassification(
                "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f",
                uuid,
                "N",
                "2.16.840.1.113883.5.25",
                "Normal"));
        // FormatCode
        if (isPDF) {
            eot.getClassification().add(makeClassification(
                    "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d",
                    uuid,
                    "urn:ihe:iti:xds-sd:pdf:2008",
                    "epSOS formatCodes",
                    "PDF/A coded document"));
        } else {
            eot.getClassification().add(makeClassification(
                    "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d",
                    uuid,
                    "urn:epSOS:ps:ps:2010",
                    "epSOS formatCodes",
                    "epSOS coded Patient Summary"));
        }
        // Healthcare facility code
        /**
         * TODO: Get healthcare facility info from national implementaition
         */
        eot.getClassification().add(makeClassification(
                "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1",
                uuid,
                "Not Used",
                "NotAvailable",
                "NotAvailable"));
        // Practice Setting code
        eot.getClassification().add(makeClassification(
                "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead",
                uuid,
                "Not Used",
                "epSOS Practice Setting Codes-Not Used",
                "Not Used"));

        // External Identifiers
        eot.getExternalIdentifier().add(makeExternalIdentifier(
                "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427",
                uuid,
                "123456789^^^&amp;2.16.17.710.820.1000.990.1&amp;ISO",
                "XDSDocumentEntry.patientId"));

        eot.getExternalIdentifier().add(makeExternalIdentifier(
                "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab",
                uuid,
                documentId,
                "XDSDocumentEntry.uniqueId"));

        return uuid;
    }

    private ExternalIdentifierType makeExternalIdentifier(String identificationScheme, String registryObject, String value, String name) {
        String uuid = "urn:uuid:" + UUID.randomUUID().toString();
        ExternalIdentifierType ex = ofRim.createExternalIdentifierType();
        ex.setId(uuid);
        ex.setIdentificationScheme(identificationScheme);
        ex.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");
        ex.setRegistryObject(registryObject);
        ex.setValue(value);

        ex.setName(ofRim.createInternationalStringType());
        ex.getName().getLocalizedString().add(ofRim.createLocalizedStringType());
        ex.getName().getLocalizedString().get(0).setValue(name);
        return ex;
    }

    private SlotType1 makeSlot(String name, String value) {
        SlotType1 sl = ofRim.createSlotType1();
        sl.setName(name);
        sl.setValueList(ofRim.createValueListType());
        sl.getValueList().getValue().add(value);
        return sl;
    }

    private ClassificationType makeClassification(String classificationScheme, String classifiedObject, String nodeRepresentation, String value, String name) {
        String uuid = "urn:uuid:" + UUID.randomUUID().toString();
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
}

/**
 * Provides a Dummy CDA for testing purposes.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
class DummyCDA {

    public static String CDA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
            + "<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:epsos=\"urn:epsos-org:ep:medication\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 CDA.xsd\"> "
            + "	<typeId extension=\"POCD_HD000040\" root=\"2.16.840.1.113883.1.3\" /> "
            + "	<templateId root='1.3.6.1.4.1.12559.11.10.1.3.1.1.3'/> "
            + "	<id root=\"2.16.17.710.820\"/> "
            + "	<code code=\"60591-5\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"epSOS LOINC\" displayName=\"Patient Summary\" /> "
            + "	<title>Patient Summary</title> "
            + "	<effectiveTime value=\"20001017163756+0300\" /> "
            + "	<confidentialityCode code=\"N\" codeSystem=\"2.16.840.1.113883.5.25\" codeSystemName=\"HL7:Confidentiality\" displayName=\"normal\" /> "
            + "	<languageCode code='pt-PT'/> "
            + "	<!--###################### Patient Demographics ######################--> "
            + "	<recordTarget contextControlCode=\"OP\" typeCode=\"RCT\"> "
            + "		<patientRole classCode=\"PAT\"> "
            + "			<!--Patient Identifier, Portuguese SNS Number--> "
            + "			<id extension=\"123456789\" root=\"2.16.17.710.820\" />			 "
            + "			<!--Patient Address--> "
            + "			<addr use=\"HP\"> "
            + "				<country>PT</country> "
            + "				<state>Aveiro</state> "
            + "				<city>Aveiro</city> "
            + "				<postalCode>3800-001</postalCode> "
            + "				<streetAddressLine>Rua de Ceuta, Nº1</streetAddressLine> "
            + "			</addr> "
            + "			<!--Patient e-mail and phone--> "
            + "			<telecom use=\"H\" value=\"tel:+351254156888\"/> "
            + "			<telecom use=\"H\" value=\"mailto:prova@exemplo2.pt\"/> "
            + "			<patient> "
            + "				<!--Patient name--> "
            + "				<name> "
            + "					<given>João</given> "
            + "					<family>Gonçalves Martins</family> "
            + "				</name> "
            + "				<!--Gender code--> "
            + "				<administrativeGenderCode code=\"M\" codeSystem=\"2.16.840.1.113883.5.1\" codeSystemName=\"HL7:AdministrativeGender\" displayName=\"Masculino\"/>									 "
            + "				<!--Birthdate--> "
            + "				<birthTime value=\"19630806\"/> "
            + "			</patient> "
            + "		</patientRole> "
            + "	</recordTarget> "
            + "	<!--Document author--> "
            + "	<author> "
            + "		<time value=\"20101202163756+0300\" /> "
            + "		<assignedAuthor classCode=\"ASSIGNED\"> "
            + "			<id assigningAuthorityName=\"SPMS\" root=\"2.16.17.710.820\" /> "
            + "			<assignedAuthoringDevice> "
            + "				<manufacturerModelName>Serviços Partilhados do Ministério da Saúde</manufacturerModelName> "
            + "				<softwareName>Serviços Partilhados do Ministério da Saúde</softwareName> "
            + "			</assignedAuthoringDevice> "
            + "			<representedOrganization> "
            + "				<id root=\"2.16.17.710.820\" /> "
            + "				<name>Ministério da Saúde</name> "
            + "				<telecom use=\"WP\" value=\"tel:+351029384759\" /> "
            + "				<telecom use=\"WP\" value=\"mailto:fakeemail@fakedomain.pt\" /> "
            + "				<addr> "
            + "					<country>PT</country> "
            + "					<city>Lisboa</city> "
            + "					<postalCode>1000-000</postalCode> "
            + "					<streetAddressLine>Rua Augusta, Nº1</streetAddressLine> "
            + "					<state nullFlavor=\"NI\" /> "
            + "				</addr> "
            + "			</representedOrganization> "
            + "		</assignedAuthor> "
            + "	</author> "
            + "	<!--Custodian--> "
            + "	<custodian> "
            + "		<assignedCustodian> "
            + "		  <representedCustodianOrganization> "
            + "			<id root=\"2.16.17.710.820\"/> "
            + "			<name>Ministério da Saúde</name> "
            + "			<telecom use=\"WP\" value=\"Telecom\"/> "
            + "			<addr> "
            + "			  <country>PT</country> "
            + "			  <state>Portugal</state> "
            + "			  <city>Lisboa</city> "
            + "			  <postalCode>1000-000</postalCode> "
            + "			  <streetAddressLine>Rua Augusta, Nº1</streetAddressLine> "
            + "			</addr> "
            + "		  </representedCustodianOrganization> "
            + "		</assignedCustodian> "
            + "	</custodian> "
            + "	<!--Legal Authenticator--> "
            + "	<legalAuthenticator> "
            + "		<time value=\"20111014133146+0200\"/> "
            + "		<signatureCode code=\"S\"/> "
            + "		<assignedEntity> "
            + "		  <id extension=\"physician\" root=\"2.16.17.710.820\"/> "
            + "		  <addr> "
            + "			<country>PT</country> "
            + "			<city>Lisboa</city> "
            + "			<postalCode>1000-000</postalCode> "
            + "			<streetAddressLine>Praça de Espanha, Nº1</streetAddressLine> "
            + "		  </addr> "
            + "		  <telecom nullFlavor=\"NI\"/> "
            + "		  <assignedPerson> "
            + "			<name> "
            + "			  <given>Dr Costa</given> "
            + "			  <family>Alberto</family> "
            + "			</name> "
            + "		  </assignedPerson> "
            + "		  <representedOrganization> "
            + "			<id root=\"2.16.17.710.820\"/> "
            + "			<name>Ministério da Saúde</name> "
            + "			<telecom use=\"WP\" value=\"tel:+351219876543\"/> "
            + "			<telecom use=\"WP\" value=\"mailto:geral@min-saude.pt\"/> "
            + "			<addr> "
            + "			  <country>PT</country> "
            + "			  <state>Portugal</state> "
            + "			  <city>Lisboa</city> "
            + "			  <postalCode>1000-000</postalCode> "
            + "			  <streetAddressLine>Rua Augusta, Nº1</streetAddressLine> "
            + "			</addr> "
            + "		  </representedOrganization> "
            + "		</assignedEntity> "
            + "	</legalAuthenticator> "
            + "	<!--Participant--> "
            + "	<participant typeCode=\"IND\"> "
            + "		<associatedEntity classCode=\"ECON\"> "
            + "			<addr nullFlavor=\"NI\"/> "
            + "			<telecom nullFlavor=\"NI\"/> "
            + "		</associatedEntity> "
            + "	</participant> "
            + "	<!--Document relations (between NI previous document)--> "
            + "	<relatedDocument typeCode=\"XFRM\"> "
            + "      <parentDocument> "
            + "        <id extension=\"urn:uuid:2.16.17.710.820.1000.990.1.20001017163756.1\" root=\"2.16.17.710.820\"/> "
            + "      </parentDocument> "
            + "    </relatedDocument> "
            + "	<component> "
            + "		<structuredBody> "
            + "			<!--###################### Current Medications ######################--> "
            + "			<component> "
            + "				<section> "
            + "					<!--Section templates--> "
            + "					<templateId root=\"2.16.840.1.113883.10.20.1.8\"/> "
            + "					<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.2.3\"/> "
            + "					<!--<id root=\"\" extension=\"\"/>--> "
            + "					<code code=\"10160-0\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"epSOS LOINC\" codeSystemVersion=\"June 2010\" displayName=\"Resumo da medicação\"/> "
            + "					<title>Medicamentos Usados</title> "
            + "					<!--Description table--> "
            + "					<text> "
            + "						<table border=\"1\"> "
            + "							<tbody> "
            + "								<tr> "
            + "									<th>Nº da Receita</th> "
            + "									<th>Data Início</th> "
            + "									<th>Código ATC</th> "
            + "									<th>Frequência de Aplicação</th> "
            + "									<th>Dose</th> "
            + "									<th>Formato</th> "
            + "									<th>Substância Activa</th> "
            + "									<th>Descrição</th> "
            + "								</tr> "
            + "								<tr ID=\"med1\"> "
            + "									<td>00313646488</td> "
            + "									<td>1990</td> "
            + "									<td>R03CC02</td> "
            + "									<td>Todos os dias, antes do pequeno-almoço</td> "
            + "									<td>100 ug / 1</td> "
            + "									<td>Pó Inalação</td> "
            + "									<td>Salbutamol</td> "
            + "									<td/> "
            + "								</tr> "
            + "								<tr ID=\"med2\"> "
            + "									<td>00313646485</td> "
            + "									<td>2002</td> "
            + "									<td>N05AN01</td> "
            + "									<td>Antes das refeiçoes</td> "
            + "									<td>400 mg</td> "
            + "									<td>Comprimido</td> "
            + "									<td>Lithium</td> "
            + "									<td/> "
            + "								</tr> "
            + "								<tr ID=\"med3\"> "
            + "									<td>00319646485</td> "
            + "									<td>2004</td> "
            + "									<td>C09AA02</td> "
            + "									<td>A cada 12h</td> "
            + "									<td>10 mg</td> "
            + "									<td>Comprimido</td> "
            + "									<td>Enalapril</td> "
            + "									<td/> "
            + "								</tr> "
            + "								<tr ID=\"med4\"> "
            + "									<td>00319780585</td> "
            + "									<td>2010/11/25</td> "
            + "									<td>J01MA02</td> "
            + "									<td>A cada 8h</td> "
            + "									<td>1.2mg/0.4mg</td> "
            + "									<td>Gotas</td> "
            + "									<td>Ciprolox</td> "
            + "									<td>Durante 14 dias</td> "
            + "								</tr> "
            + "							</tbody> "
            + "						</table> "
            + "					</text> "
            + "					<!--###################### Medication 1 ######################--> "
            + "					<entry> "
            + "						<substanceAdministration classCode=\"SBADM\" moodCode=\"EVN\"> "
            + "								<!--IHE CCD Template--> "
            + "								<templateId root=\"2.16.840.1.113883.10.20.1.24\"/> "
            + "								<!--IHE PCC--> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7\"/> "
            + "								<!--epSOS Template--> "
            + "								<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.4\" /> "
            + "								<!--Normal Dosing Template (No special processing needed)--> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.1\" /> "
            + "								<!--Substance Administration ID (Country OID used as root and Prescription ID used as extension)--> "
            + "								<id root=\"2.16.17.710.820\" extension=\"00313646488\"/> "
            + "								<text> "
            + "									<reference value=\"#med1\"/> "
            + "								</text> "
            + "								<statusCode code=\"completed\"/> "
            + "								<!--Start and Stop Date--> "
            + "								<effectiveTime xsi:type=\"IVL_TS\"> "
            + "									<low value=\"19900102\" /> "
            + "									<high nullFlavor=\"UNK\" /> "
            + "								</effectiveTime> "
            + "								<!--Frequency--> "
            + "								<effectiveTime operator=\"A\" xsi:type=\"EIVL_TS\"> "
            + "									<event code=\"ACM\" codeSystem=\"2.16.840.1.113883.5.139\"/>									 "
            + "								</effectiveTime> "
            + "								<!--Route--> "
            + "								<routeCode code=\"20020000\" displayName=\"Inalação\" codeSystem=\"1.3.6.1.4.1.12559.11.10.1.3.1.44.1\" codeSystemName=\"EDQM\"/> "
            + "								<!--Dose--> "
            + "								<doseQuantity> "
            + "									<low unit=\"mg\" value=\"0.1\"/>																			 "
            + "									<high unit=\"mg\" value=\"0.1\"/>																		 "
            + "								</doseQuantity> "
            + "								<!--Product--> "
            + "								<consumable> "
            + "									<manufacturedProduct  classCode=\"MANU\"> "
            + "										<!--IHE Required Templates--> "
            + "										<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.2\" /> "
            + "										<templateId root=\"2.16.840.1.113883.10.20.1.53\" /> "
            + "										<manufacturedMaterial>											 "
            + "											<!--Form of the medication (e.g. tablet, capsule, liquid)--> "
            + "											<epsos:formCode nullFlavor=\"NI\"/> "
            + "											<!--Active Ingredient List--> "
            + "											<epsos:ingredient classCode=\"ACTI\"> "
            + "												<epsos:quantity> "
            + "													<epsos:numerator xsi:type=\"epsos:PQ\" value=\"100\" unit=\"ug\"/>																										 "
            + "													<epsos:denominator xsi:type=\"epsos:PQ\" unit=\"1\" value=\"1\"/>																										 "
            + "												</epsos:quantity> "
            + "												<epsos:ingredient classCode=\"MMAT\" determinerCode=\"KIND\"> "
            + "													<!--Coded representation of the ingredient--> "
            + "													<epsos:code code=\"R03CC02\" codeSystem=\"2.16.840.1.113883.6.73\" displayName=\"salbutamol\" codeSystemName=\"Anatomical Therapeutic Chemical\"/>		 "
            + "												</epsos:ingredient>												 "
            + "											</epsos:ingredient>											 "
            + "										</manufacturedMaterial> "
            + "									</manufacturedProduct> "
            + "								</consumable> "
            + "						</substanceAdministration> "
            + "					</entry> "
            + "					<!--###################### Medication 2 ######################-->					 "
            + "					<entry> "
            + "						<substanceAdministration classCode=\"SBADM\" moodCode=\"EVN\"> "
            + "								<!--IHE CCD Template--> "
            + "								<templateId root=\"2.16.840.1.113883.10.20.1.24\"/> "
            + "								<!--IHE PCC--> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7\"/> "
            + "								<!--epSOS Template--> "
            + "								<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.4\" /> "
            + "								<!--Normal Dosing Template (No special processing needed)--> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.1\" /> "
            + "								<!--Substance Administration ID (Country OID used as root and Prescription ID used as extension)--> "
            + "								<id root=\"2.16.17.710.820\" extension=\"00313646485\"/> "
            + "								<text> "
            + "									<reference value=\"#med2\"/> "
            + "								</text> "
            + "								<statusCode code=\"completed\"/> "
            + "								<!--Start and Stop Date--> "
            + "								<effectiveTime xsi:type=\"IVL_TS\"> "
            + "									<low value=\"20020102\" /> "
            + "									<high nullFlavor=\"UNK\" /> "
            + "								</effectiveTime> "
            + "								<!--Frequency--> "
            + "								<effectiveTime operator=\"A\" xsi:type=\"EIVL_TS\"> "
            + "									<event code=\"AC\" codeSystem=\"2.16.840.1.113883.5.139\"/>	 "
            + "								</effectiveTime> "
            + "								<!--Route--> "
            + "								<routeCode code=\"20053000\" displayName=\"Uso oral\" codeSystem=\"1.3.6.1.4.1.12559.11.10.1.3.1.44.1\" codeSystemName=\"EDQM\"/> "
            + "								<!--Dose--> "
            + "								<doseQuantity> "
            + "									<low unit=\"mg\" value=\"400\"/>										 "
            + "									<high unit=\"mg\" value=\"400\"/> "
            + "								</doseQuantity> "
            + "								<!--Product--> "
            + "								<consumable> "
            + "									<manufacturedProduct> "
            + "										<!--IHE Required Templates--> "
            + "										<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.2\" /> "
            + "										<templateId root=\"2.16.840.1.113883.10.20.1.53\" /> "
            + "										<manufacturedMaterial>											 "
            + "											<!--Form of the medication (e.g. tablet, capsule, liquid)--> "
            + "											<epsos:formCode nullFlavor=\"NI\"/> "
            + "											<!--Active Ingredient List--> "
            + "											<epsos:ingredient classCode=\"ACTI\"> "
            + "												<epsos:quantity> "
            + "													<epsos:numerator xsi:type=\"epsos:PQ\" value=\"400\" unit=\"mg\"/>																										 "
            + "													<epsos:denominator xsi:type=\"epsos:PQ\" nullFlavor=\"NI\"/>																										 "
            + "												</epsos:quantity> "
            + "												<epsos:ingredient classCode=\"MMAT\" determinerCode=\"KIND\"> "
            + "													<!--Coded representation of the ingredient--> "
            + "													<epsos:code code=\"N05AN01\" codeSystem=\"2.16.840.1.113883.6.73\" displayName=\"lithium\" codeSystemName=\"Anatomical Therapeutic Chemical\"/>		 "
            + "												</epsos:ingredient>												 "
            + "											</epsos:ingredient>											 "
            + "										</manufacturedMaterial> "
            + "									</manufacturedProduct> "
            + "								</consumable> "
            + "						</substanceAdministration> "
            + "					</entry>					 "
            + "					<!--###################### Medication 3 ######################--> "
            + "					<entry> "
            + "						<substanceAdministration classCode=\"SBADM\" moodCode=\"EVN\"> "
            + "								<!--IHE CCD Template--> "
            + "								<templateId root=\"2.16.840.1.113883.10.20.1.24\"/> "
            + "								<!--IHE PCC--> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7\"/> "
            + "								<!--epSOS Template--> "
            + "								<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.4\" /> "
            + "								<!--Normal Dosing Template (No special processing needed)--> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.1\" /> "
            + "								<!--Substance Administration ID (Country OID used as root and Prescription ID used as extension)--> "
            + "								<id root=\"2.16.17.710.820\" extension=\"00319646485\"/> "
            + "								<text> "
            + "									<reference value=\"#med3\"/> "
            + "								</text> "
            + "								<statusCode code=\"completed\"/> "
            + "								<!--Start and Stop Date--> "
            + "								<effectiveTime xsi:type=\"IVL_TS\"> "
            + "									<low value=\"20040102\" /> "
            + "									<high nullFlavor=\"UNK\" /> "
            + "								</effectiveTime> "
            + "								<!--Frequency--> "
            + "								<effectiveTime operator=\"A\" xsi:type=\"PIVL_TS\"> "
            + "									<period value=\"12\" unit=\"h\"/>	 "
            + "								</effectiveTime> "
            + "								<!--Route--> "
            + "								<routeCode code=\"20053000\" displayName=\"Uso oral\" codeSystem=\"1.3.6.1.4.1.12559.11.10.1.3.1.44.1\" codeSystemName=\"EDQM\"/> "
            + "								<!--Dose--> "
            + "								<doseQuantity> "
            + "									<low unit=\"mg\" value=\"10\"/> "
            + "									<high unit=\"mg\" value=\"10\"/> "
            + "								</doseQuantity> "
            + "								<!--Product--> "
            + "								<consumable> "
            + "									<manufacturedProduct> "
            + "										<!--IHE Required Templates--> "
            + "										<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.2\" /> "
            + "										<templateId root=\"2.16.840.1.113883.10.20.1.53\" /> "
            + "										<manufacturedMaterial>											 "
            + "											<!--Form of the medication (e.g. tablet, capsule, liquid)--> "
            + "											<epsos:formCode nullFlavor=\"NI\"/> "
            + "											<!--Active Ingredient List--> "
            + "											<epsos:ingredient classCode=\"ACTI\"> "
            + "												<epsos:quantity> "
            + "													<epsos:numerator xsi:type=\"epsos:PQ\" value=\"10\" unit=\"mg\"/>																										 "
            + "													<epsos:denominator xsi:type=\"epsos:PQ\" nullFlavor=\"NI\"/>																										 "
            + "												</epsos:quantity> "
            + "												<epsos:ingredient classCode=\"MMAT\" determinerCode=\"KIND\"> "
            + "													<!--Coded representation of the ingredient--> "
            + "													<epsos:code code=\"C09AA02\" codeSystem=\"2.16.840.1.113883.6.73\" displayName=\"enalapril\" codeSystemName=\"Anatomical Therapeutic Chemical\"/>		 "
            + "												</epsos:ingredient>												 "
            + "											</epsos:ingredient>											 "
            + "										</manufacturedMaterial> "
            + "									</manufacturedProduct> "
            + "								</consumable> "
            + "						</substanceAdministration> "
            + "					</entry> "
            + "					<!--###################### Medication 4 ######################-->					 "
            + "					<entry> "
            + "						<substanceAdministration classCode=\"SBADM\" moodCode=\"EVN\"> "
            + "								<!--IHE CCD Template--> "
            + "								<templateId root=\"2.16.840.1.113883.10.20.1.24\"/> "
            + "								<!--IHE PCC--> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7\"/> "
            + "								<!--epSOS Template--> "
            + "								<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.4\" /> "
            + "								<!--Normal Dosing Template (No special processing needed)--> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.1\" /> "
            + "								<!--Substance Administration ID (Country OID used as root and Prescription ID used as extension)--> "
            + "								<id root=\"2.16.17.710.820\" extension=\"00319780585\"/> "
            + "								<text> "
            + "									<reference value=\"#med4\"/> "
            + "								</text> "
            + "								<statusCode code=\"completed\"/> "
            + "								<!--Start and Stop Date--> "
            + "								<effectiveTime xsi:type=\"IVL_TS\"> "
            + "									<low value=\"20101125\" /> "
            + "									<high nullFlavor=\"UNK\" /> "
            + "								</effectiveTime> "
            + "								<!--Frequency--> "
            + "								<effectiveTime operator=\"A\" xsi:type=\"PIVL_TS\"> "
            + "									<period value=\"8\" unit=\"h\"/> "
            + "								</effectiveTime> "
            + "								<!--Route--> "
            + "								<routeCode code=\"20053000\" displayName=\"Uso oral\" codeSystem=\"1.3.6.1.4.1.12559.11.10.1.3.1.44.1\" codeSystemName=\"EDQM\"/> "
            + "								<!--Dose--> "
            + "								<doseQuantity> "
            + "									<low unit=\"mg\" value=\"1.2\"/> "
            + "									<high unit=\"mg\" value=\"1.2\"/>									 "
            + "								</doseQuantity> "
            + "								<!--Product--> "
            + "								<consumable> "
            + "									<manufacturedProduct> "
            + "										<!--IHE Required Templates--> "
            + "										<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.2\" /> "
            + "										<templateId root=\"2.16.840.1.113883.10.20.1.53\" /> "
            + "										<manufacturedMaterial>											 "
            + "											<!--Form of the medication (e.g. tablet, capsule, liquid)--> "
            + "											<epsos:formCode nullFlavor=\"NI\"/> "
            + "											<!--Active Ingredient List--> "
            + "											<epsos:ingredient classCode=\"ACTI\"> "
            + "												<epsos:quantity> "
            + "													<epsos:numerator xsi:type=\"epsos:PQ\" value=\"1.2\" unit=\"mg\"/>																										 "
            + "													<epsos:denominator xsi:type=\"epsos:PQ\" value=\"0.4\" unit=\"ml\"/>																										 "
            + "												</epsos:quantity> "
            + "												<epsos:ingredient classCode=\"MMAT\" determinerCode=\"KIND\"> "
            + "													<!--Coded representation of the ingredient--> "
            + "													<epsos:code code=\"S03AA07\" codeSystem=\"2.16.840.1.113883.6.73\" displayName=\"ciprofloxacina\" codeSystemName=\"Anatomical Therapeutic Chemical\"/>		 "
            + "												</epsos:ingredient>												 "
            + "											</epsos:ingredient>											 "
            + "										</manufacturedMaterial> "
            + "									</manufacturedProduct> "
            + "								</consumable> "
            + "						</substanceAdministration> "
            + "					</entry> "
            + "				</section> "
            + "			</component> "
            + "			<!--###################### Allergies and other adverse reaction ######################--> "
            + "			<component> "
            + "				<section> "
            + "					<!--Section templates--> "
            + "					<templateId root=\"2.16.840.1.113883.10.20.1.2\"/> "
            + "					<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.3.13\"/> "
            + "					<id root=\"1f40cda0-99b0-11e1-a8b0-0800200c9a66\"/> "
            + "					<code code='48765-2' displayName='Alergias, reacções adversas, alertas' codeSystem='2.16.840.1.113883.6.1' codeSystemName='LOINC'/>						 "
            + "					<title>Alergias, reacções adversas, alertas</title> "
            + "					<text> "
            + "						<table border=\"1\"> "
            + "							<tbody> "
            + "								<tr> "
            + "									<th>Códido ATC</th>									 "
            + "									<th>Designação</th>									 "
            + "									<th>Data de diagnóstico</th> "
            + "									<th>Manifestação de Alergia</th> "
            + "								</tr> "
            + "								<tr ID=\"allergy1\"> "
            + "									<td ID=\"allergy1_code\">C01EB16</td> "
            + "									<td>Intolerância a ibuprofeno</td> "
            + "									<td>N/A</td> "
            + "									<td ID=\"manifest-1\">N/A</td> "
            + "								</tr>								 "
            + "							</tbody> "
            + "						</table> "
            + "					</text> "
            + "					<entry> "
            + "						<act classCode=\"ACT\" moodCode=\"EVN\"> "
            + "							<!--Required templates--> "
            + "							<templateId root='2.16.840.1.113883.10.20.1.27'/> "
            + "							<templateId root='1.3.6.1.4.1.19376.1.5.3.1.4.5.1'/> "
            + "							<templateId root='1.3.6.1.4.1.19376.1.5.3.1.4.5.3'/> "
            + "							<id root=\"7b620cb0-99b1-11e1-a8b0-0800200c9a66\"/> "
            + "							<code nullFlavor=\"NA\"/> "
            + "							<statusCode code=\"active\"/> "
            + "							<effectiveTime> "
            + "								<low nullFlavor=\"UNK\"/> "
            + "								<high nullFlavor=\"UNK\"/> "
            + "							</effectiveTime> "
            + "							<entryRelationship inversionInd=\"false\" typeCode=\"SUBJ\"> "
            + "								<observation classCode=\"OBS\" moodCode=\"EVN\"> "
            + "									<!--Required Templates--> "
            + "									<templateId root=\"2.16.840.1.113883.10.20.1.18\"/> "
            + "									<templateId root=\"2.16.840.1.113883.10.20.1.28\"/> "
            + "									<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.6\"/> "
            + "									<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5\"/> "
            + "									<id root=\"a03710d0-99b1-11e1-a8b0-0800200c9a66\"/> "
            + "									<code code=\"59037007\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED-CT\" displayName=\"Intolerancia a medicamento\"/> "
            + "									<text> "
            + "										<reference value=\"#allergy1\"/> "
            + "									</text> "
            + "									<statusCode code=\"completed\"/> "
            + "									<effectiveTime> "
            + "										<low value=\"20090727\"/> "
            + "										<high nullFlavor=\"UNK\"/> "
            + "									</effectiveTime> "
            + "									<value xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CD\"> "
            + "										<originalText> "
            + "											<reference value=\"#allergy1_code\"/> "
            + "										</originalText> "
            + "									</value> "
            + "									<participant typeCode=\"CSM\"> "
            + "										<participantRole classCode=\"MANU\"> "
            + "											<playingEntity classCode=\"MMAT\"> "
            + "												<code code=\"C01EB16\" codeSystem=\"2.16.840.1.113883.6.73\" codeSystemName=\"ATC WHO\" displayName=\"ibuprofeno\"> "
            + "													<originalText> "
            + "														<reference value=\"#allergy1\"/> "
            + "													</originalText> "
            + "												</code> "
            + "											</playingEntity> "
            + "										</participantRole> "
            + "									</participant> "
            + "								</observation> "
            + "							</entryRelationship>							 "
            + "						</act> "
            + "					</entry> "
            + "				</section> "
            + "			</component> "
            + "			<!--###################### History of past hillness ######################--> "
            + "			<component> "
            + "				<section> "
            + "					<!--Section Template--> "
            + "					<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.3.8\"/> "
            + "					<!--<id extension=\"\" root=\"\"/>--> "
            + "					<code code=\"11348-0\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" codeSystemVersion=\"2.34\" displayName=\"Historial de doenças\"/>						 "
            + "					<title>Historial de doenças</title> "
            + "					<text> "
            + "						<content ID=\"nopasthillness\">Sem historial de doenças</content> "
            + "					</text> "
            + "					<entry> "
            + "					  <act classCode=\"ACT\" moodCode=\"EVN\"> "
            + "						<!--Required templates--> "
            + "						<templateId root=\"2.16.840.1.113883.10.20.1.27\"/> "
            + "						<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.1\"/> "
            + "						<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.2\"/> "
            + "						<id root=\"1f40cda1-99b0-11e1-a8b0-0800200c9a66\"/> "
            + "						<code nullFlavor=\"NA\"/> "
            + "						<statusCode code=\"completed\"/> "
            + "						<effectiveTime> "
            + "						  <low nullFlavor=\"NI\"/> "
            + "						</effectiveTime> "
            + "						<entryRelationship inversionInd=\"false\" typeCode=\"SUBJ\"> "
            + "						  <observation classCode=\"OBS\" moodCode=\"EVN\"> "
            + "							<!--Required templates--> "
            + "							<templateId root=\"2.16.840.1.113883.10.20.1.28\"/> "
            + "							<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5\"/> "
            + "							<id root=\"c9251fa0-99b1-11e1-a8b0-0800200c9a66\"/> "
            + "							<code nullFlavor=\"NI\"/> "
            + "							<text> "
            + "							  <reference nullFlavor=\"NI\"/> "
            + "							</text> "
            + "							<statusCode code=\"completed\"/> "
            + "							<effectiveTime> "
            + "							  <low nullFlavor=\"NI\"/> "
            + "							</effectiveTime> "
            + "							<value nullFlavor=\"NI\" xsi:type=\"CD\"/> "
            + "						  </observation> "
            + "						</entryRelationship> "
            + "					  </act> "
            + "					</entry> "
            + "				</section> "
            + "			</component> "
            + "			<!--###################### Active Problems ######################--> "
            + "			<component> "
            + "				<section> "
            + "					<!--Required templates--> "
            + "					<templateId root=\"2.16.840.1.113883.10.20.1.11\"/> "
            + "					<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.3.6\"/> "
            + "					<!--<id extension=\"\" root=\"\"/>--> "
            + "					<code code=\"11450-4\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" codeSystemVersion=\"2.34\" displayName=\"Lista de problemas activos\"/>					   "
            + "					<title>Lista de problemas activos</title> "
            + "					<text> "
            + "					  <table> "
            + "						<thead> "
            + "						  <tr> "
            + "							<th>Código ICD-10</th> "
            + "							<th>Descrição do Problema</th> "
            + "							<th>Data Surgimento</th> "
            + "							<th>Diagnostico</th> "
            + "							<th>Estado</th> "
            + "						  </tr> "
            + "						</thead> "
            + "						<tbody> "
            + "						  <tr ID=\"dgn1\"> "
            + "							<td>J45</td> "
            + "							<td ID=\"dgn1_description\">Asma</td> "
            + "							<td>1982</td> "
            + "							<td ID=\"dgn1_diagnosis\">Asma</td> "
            + "							<td>Activo</td>							 "
            + "						  </tr> "
            + "						  <tr ID=\"dgn2\"> "
            + "							<td>F31</td> "
            + "							<td ID=\"dgn2_description\">Doença Pipolar</td> "
            + "							<td>2000</td> "
            + "							<td ID=\"dgn2_diagnosis\">Doença bipolar</td> "
            + "							<td>Activo</td>							 "
            + "						  </tr> "
            + "						  <tr ID=\"dgn3\"> "
            + "							<td>I10</td> "
            + "							<td ID=\"dgn3_description\">Hipertenção Primária (ou Essencial)</td> "
            + "							<td>2002</td> "
            + "							<td ID=\"dgn3_diagnosis\">Hipertenção Primária (ou Essencial)</td> "
            + "							<td>Activo</td>							 "
            + "						  </tr> "
            + "						  <tr ID=\"dgn4\"> "
            + "							<td>H60</td> "
            + "							<td ID=\"dgn4_description\">Otite Externa</td> "
            + "							<td>2010/11/25</td> "
            + "							<td ID=\"dgn4_diagnosis\">Otite Externa</td> "
            + "							<td>Activo</td>							 "
            + "						  </tr> "
            + "						</tbody> "
            + "					  </table> "
            + "					</text> "
            + "					<!--###################### Problem 1 ######################--> "
            + "					<entry> "
            + "					  <act classCode=\"ACT\" moodCode=\"EVN\"> "
            + "						<!--Required templates--> "
            + "						<templateId root=\"2.16.840.1.113883.10.20.1.27\"/> "
            + "						<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.1\"/> "
            + "						<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.2\"/> "
            + "						<id root=\"76ca5190-99b0-11e1-a8b0-0800200c9a66\"/> "
            + "						<code nullFlavor=\"NA\"/> "
            + "						<statusCode code=\"active\"/> "
            + "						<effectiveTime> "
            + "						  <low value=\"19820809\"/> "
            + "						</effectiveTime> "
            + "						<entryRelationship inversionInd=\"false\" typeCode=\"SUBJ\"> "
            + "						  <observation classCode=\"OBS\" moodCode=\"EVN\" negationInd=\"false\"> "
            + "							<!--Required templates--> "
            + "							<templateId root=\"2.16.840.1.113883.10.20.1.28\"/> "
            + "							<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5\"/> "
            + "							<id root=\"c9251fa1-99b1-11e1-a8b0-0800200c9a66\"/> "
            + "							<!--Doença SNOMED Code--> "
            + "							<code code=\"64572001\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" codeSystemVersion=\"July 2009\" displayName=\"Doença\"/>																 "
            + "							<text> "
            + "							  <reference value=\"#dgn1\"/> "
            + "							</text> "
            + "							<!--Status--> "
            + "							<statusCode code=\"completed\"/> "
            + "							<!--Problem start and stop dates--> "
            + "							<effectiveTime> "
            + "							  <low value=\"19820809\"/> "
            + "							</effectiveTime> "
            + "							<!--Problem ICD-10 code--> "
            + "							<value code=\"J45\" codeSystem=\"1.3.6.1.4.1.12559.11.10.1.3.1.44.2\" codeSystemName=\"ICD-10\" displayName=\"Asma\" xsi:type=\"CD\"> "
            + "								<originalText> "
            + "									<reference value=\"#dgn1_diagnosis\" /> "
            + "								</originalText>												 "
            + "							</value> "
            + "						  </observation> "
            + "						</entryRelationship> "
            + "					  </act> "
            + "					</entry>				   "
            + "					<!--###################### Problem 2 ######################--> "
            + "					<entry> "
            + "						<act classCode=\"ACT\" moodCode=\"EVN\"> "
            + "							<!--Required templates--> "
            + "							<templateId root=\"2.16.840.1.113883.10.20.1.27\"/> "
            + "							<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.1\"/> "
            + "							<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.2\"/> "
            + "							<id root=\"8f8e5870-99b0-11e1-a8b0-0800200c9a66\"/> "
            + "							<code nullFlavor=\"NA\"/> "
            + "							<statusCode code=\"active\"/> "
            + "							<effectiveTime> "
            + "							  <low value=\"20001013\"/> "
            + "							</effectiveTime> "
            + "							<entryRelationship inversionInd=\"false\" typeCode=\"SUBJ\"> "
            + "							  <observation classCode=\"OBS\" moodCode=\"EVN\" negationInd=\"false\"> "
            + "								<!--Required templates--> "
            + "								<templateId root=\"2.16.840.1.113883.10.20.1.28\"/> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5\"/> "
            + "								<id root=\"c9251fa2-99b1-11e1-a8b0-0800200c9a66\"/> "
            + "								<!--Doença SNOMED Code--> "
            + "								<code code=\"64572001\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" codeSystemVersion=\"July 2009\" displayName=\"Doença\"/>								 "
            + "								<text> "
            + "								  <reference value=\"#dgn2\"/> "
            + "								</text> "
            + "								<!--Status--> "
            + "								<statusCode code=\"completed\"/> "
            + "								<!--Problem start and stop dates--> "
            + "								<effectiveTime> "
            + "								  <low value=\"20001013\"/> "
            + "								</effectiveTime> "
            + "								<!--Problem ICD-10 code--> "
            + "								<value code=\"F31\" codeSystem=\"1.3.6.1.4.1.12559.11.10.1.3.1.44.2\" codeSystemName=\"ICD-10\" displayName=\"Perturbação Bipolar, Não Específicada\" xsi:type=\"CD\"> "
            + "									<originalText> "
            + "										<reference value=\"#dgn2_diagnosis\" /> "
            + "									</originalText>					 "
            + "									</value> "
            + "							  </observation> "
            + "							</entryRelationship> "
            + "						</act>					 "
            + "					</entry> "
            + "					<!--###################### Problem 3 ######################--> "
            + "					<entry> "
            + "						<act classCode=\"ACT\" moodCode=\"EVN\"> "
            + "							<!--Required templates--> "
            + "							<templateId root=\"2.16.840.1.113883.10.20.1.27\"/> "
            + "							<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.1\"/> "
            + "							<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.2\"/> "
            + "							<id root=\"8f8e5871-99b0-11e1-a8b0-0800200c9a66\"/> "
            + "							<code nullFlavor=\"NA\"/> "
            + "							<statusCode code=\"active\"/> "
            + "							<effectiveTime> "
            + "							  <low value=\"20021021\"/> "
            + "							</effectiveTime> "
            + "							<entryRelationship inversionInd=\"false\" typeCode=\"SUBJ\"> "
            + "							  <observation classCode=\"OBS\" moodCode=\"EVN\" negationInd=\"false\"> "
            + "								<!--Required templates--> "
            + "								<templateId root=\"2.16.840.1.113883.10.20.1.28\"/> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5\"/> "
            + "								<id root=\"c9251fa3-99b1-11e1-a8b0-0800200c9a66\"/> "
            + "								<!--Doença SNOMED Code--> "
            + "								<code code=\"64572001\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" codeSystemVersion=\"July 2009\" displayName=\"Doença\"/> "
            + "								<text> "
            + "								  <reference value=\"#dgn3\"/> "
            + "								</text> "
            + "								<!--Status--> "
            + "								<statusCode code=\"completed\"/> "
            + "								<!--Problem start and stop dates--> "
            + "								<effectiveTime> "
            + "								  <low value=\"20021021\"/> "
            + "								</effectiveTime> "
            + "								<!--Problem ICD-10 code--> "
            + "								<value code=\"I10\" codeSystem=\"1.3.6.1.4.1.12559.11.10.1.3.1.44.2\" codeSystemName=\"ICD-10\" displayName=\"Hipertenção Primária (ou Essencial)\" xsi:type=\"CD\"> "
            + "									<originalText> "
            + "										<reference value=\"#dgn3_diagnosis\" /> "
            + "									</originalText>														 "
            + "								</value> "
            + "							  </observation> "
            + "							</entryRelationship> "
            + "						</act>		 "
            + "					</entry> "
            + "					<!--###################### Problem 4 ######################--> "
            + "					<entry>	 "
            + "						<act classCode=\"ACT\" moodCode=\"EVN\"> "
            + "							<!--Required templates--> "
            + "							<templateId root=\"2.16.840.1.113883.10.20.1.27\"/> "
            + "							<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.1\"/> "
            + "							<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.2\"/> "
            + "							<id root=\"8f8e5872-99b0-11e1-a8b0-0800200c9a66\"/> "
            + "							<code nullFlavor=\"NA\"/> "
            + "							<statusCode code=\"active\"/> "
            + "							<effectiveTime> "
            + "							  <low value=\"20101125\"/> "
            + "							</effectiveTime> "
            + "							<entryRelationship inversionInd=\"false\" typeCode=\"SUBJ\"> "
            + "							  <observation classCode=\"OBS\" moodCode=\"EVN\" negationInd=\"false\"> "
            + "								<!--Required templates--> "
            + "								<templateId root=\"2.16.840.1.113883.10.20.1.28\"/> "
            + "								<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5\"/> "
            + "								<id root=\"c9251fa4-99b1-11e1-a8b0-0800200c9a66\"/> "
            + "								<!--Doença SNOMED Code--> "
            + "								<code code=\"64572001\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" codeSystemVersion=\"July 2009\" displayName=\"Doença\"/> "
            + "								<text> "
            + "								  <reference value=\"#dgn4\"/> "
            + "								</text> "
            + "								<!--Status--> "
            + "								<statusCode code=\"completed\"/> "
            + "								<!--Problem start and stop dates--> "
            + "								<effectiveTime> "
            + "								  <low value=\"20101125\"/> "
            + "								</effectiveTime> "
            + "								<!--Problem ICD-10 code--> "
            + "								<value code=\"H60\" codeSystem=\"1.3.6.1.4.1.12559.11.10.1.3.1.44.2\" codeSystemName=\"ICD-10\" displayName=\"Otite Externa Crónica\" xsi:type=\"CD\"> "
            + "									<originalText> "
            + "										<reference value=\"#dgn4_diagnosis\" /> "
            + "									</originalText>														 "
            + "								</value> "
            + "							  </observation> "
            + "							</entryRelationship> "
            + "						</act>		 "
            + "					</entry> "
            + "					</section> "
            + "			</component> "
            + "			<!--###################### Medical Devices ######################--> "
            + "			<component> "
            + "				<section> "
            + "				  <!--Section Templates--> "
            + "				  <templateId root=\"2.16.840.1.113883.10.20.1.7\"/> "
            + "				  <templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.1.5.3.5\"/> "
            + "				  <templateId assigningAuthorityName=\"epSOS\" root=\"1.3.6.1.4.1.12559.11.10.1.3.1.2.4\"/> "
            + "				  <code code=\"46264-8\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Dispositivos Médicos e Implantes\"/> "
            + "				  <title>Dispositivos Médicos e Implantes</title> "
            + "				  <text> "
            + "					<table> "
            + "					  <thead> "
            + "						<tr> "
            + "						  <th>Código SNOMED</th> "
            + "						  <th>Dispositivo Médico</th> "
            + "						  <th>Data</th> "
            + "						  <th>Descrição</th> "
            + "						</tr> "
            + "					  </thead> "
            + "					  <tbody> "
            + "						<tr ID=\"meddevices1\"> "
            + "						  <td>303500007</td> "
            + "						  <td ID=\"meddevices1_observation\">Implante Auditivo</td> "
            + "						  <td ID=\"meddevices1_datefrom\">14.07.1998</td> "
            + "						  <td ID=\"meddevices1_description\"/> "
            + "						</tr> "
            + "					  </tbody> "
            + "					</table> "
            + "				  </text> "
            + "				  <entry> "
            + "					<supply classCode=\"SPLY\" moodCode=\"EVN\"> "
            + "					  <templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.5\"/> "
            + "					  <!--<id root=\"\" extension=\"\"/>--> "
            + "					  <text> "
            + "						<reference value=\"#meddevices1\"/> "
            + "					  </text> "
            + "					  <effectiveTime value=\"19980714\"/> "
            + "					  <participant typeCode=\"DEV\"> "
            + "						<participantRole classCode=\"MANU\"> "
            + "						  <!--<id root=\"\" extension=\"\"/>--> "
            + "						  <playingDevice classCode=\"DEV\" determinerCode=\"INSTANCE\"> "
            + "							<code code=\"303500007\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"Implante Auditivo\"/> "
            + "						  </playingDevice> "
            + "						</participantRole> "
            + "					  </participant> "
            + "					</supply> "
            + "				  </entry> "
            + "				</section> "
            + "			</component> "
            + "			<!--###################### Surgical Procedures ######################--> "
            + "			<component> "
            + "				<section> "
            + "				  <!--Section templates--> "
            + "				  <templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.3.11\"/> "
            + "				  <templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.3.12\"/> "
            + "				  <templateId root=\"2.16.840.1.113883.10.20.1.12\"/> "
            + "				  <code code=\"47519-4\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Intervenções Cirúgicas\"/> "
            + "				  <title>Intervenções Cirúgicas</title> "
            + "				  <text> "
            + "					<content ID=\"nosurgeries\">Sem cirurgias conhecidas</content> "
            + "				  </text> "
            + "				  <entry> "
            + "					<procedure classCode=\"PROC\" moodCode=\"EVN\" nullFlavor=\"NA\"> "
            + "						<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.19\"/> "
            + "						<templateId root=\"2.16.840.1.113883.10.20.1.29\"/> "
            + "					</procedure> "
            + "				  </entry> "
            + "				</section> "
            + "			</component> "
            + "		</structuredBody> "
            + "	</component> "
            + "</ClinicalDocument> ";
}
