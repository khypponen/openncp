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
/*
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1  Built on : Aug 31, 2011 (12:22:40 CEST)
 */
package epsos.openncp.protocolterminator.test;

import epsos.openncp.protocolterminator.*;
import epsos.openncp.protocolterminator.clientconnector.DocumentId;
import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.GenericDocumentCode;
import epsos.openncp.protocolterminator.clientconnector.PatientDemographics;
import epsos.openncp.protocolterminator.clientconnector.PatientId;
import epsos.openncp.protocolterminator.clientconnector.QueryDocumentRequest;
import epsos.openncp.protocolterminator.clientconnector.QueryDocuments;
import epsos.openncp.protocolterminator.clientconnector.QueryDocumentsDocument;
import epsos.openncp.protocolterminator.clientconnector.QueryDocumentsResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.QueryPatientDocument;
import epsos.openncp.protocolterminator.clientconnector.QueryPatientRequest;
import epsos.openncp.protocolterminator.clientconnector.QueryPatientResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.RetrieveDocument1;
import epsos.openncp.protocolterminator.clientconnector.RetrieveDocumentDocument1;
import epsos.openncp.protocolterminator.clientconnector.RetrieveDocumentRequest;
import epsos.openncp.protocolterminator.clientconnector.RetrieveDocumentResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.SayHelloDocument;
import epsos.openncp.protocolterminator.clientconnector.SayHelloResponseDocument;
import epsos.openncp.pt.client.ClientConnectorServiceServiceStub;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.util.XMLUtils;
import org.opensaml.saml2.core.Assertion;

/*
 *  ClientConnectorServiceServiceTest Junit test case
 */
public class TestClientConnectorService {

    public static final String EPR = "http://localhost:8080/epsos-client-connector/services/ClientConnectorService";

    public static void main(String[] args) throws Exception {

//        testsayHello();
//        testqueryPatient();
        testqueryDocuments();
//        testretrieveDocument();
    }

    /**
     * Auto generated test method
     */
    public static void testqueryDocuments() throws java.lang.Exception {

        HCPIAssertionCreator idAssertionCreator = new HCPIAssertionCreator();
        Assertion idAssertion = idAssertionCreator.createHCPIAssertion();

        TRCAssertionCreator trcAssertionCreator = new TRCAssertionCreator();
        Assertion trcAssertion = trcAssertionCreator.createTRCAssertion();

        ClientConnectorServiceServiceStub stub = new ClientConnectorServiceServiceStub(EPR);
        addAssertions(stub, idAssertion, trcAssertion);

        QueryDocumentsDocument queryDocumentsDocument = QueryDocumentsDocument.Factory.newInstance();
        QueryDocuments queryDocuments = queryDocumentsDocument.addNewQueryDocuments();
        QueryDocumentRequest queryDocumentRequest = queryDocuments.addNewArg0();

        PatientId patientId = queryDocumentRequest.addNewPatientId();
        patientId.setExtension("123456789");
        patientId.setRoot("2.16.17.710.820.1000.990.1");

        GenericDocumentCode classCode = queryDocumentRequest.addNewClassCode();
        classCode.setValue("queryDocuments");
        classCode.setSchema("queryDocuments");
        classCode.setNodeRepresentation("Patient Summury");

        queryDocumentRequest.setCountryCode("PT");

        QueryDocumentsResponseDocument queryDocumentsResponseDocument = stub.queryDocuments(queryDocumentsDocument);

        EpsosDocument1[] docArray = queryDocumentsResponseDocument.getQueryDocumentsResponse().getReturnArray();

        if (docArray != null && docArray.length != 0) {
            for (EpsosDocument1 doc : docArray) {
                String classCode2 = doc.getClassCode().getNodeRepresentation();
                String formatCode = doc.getFormatCode().getNodeRepresentation();
                System.out.println("___ Document: " + doc.getUuid() + " : " + classCode2 + " : " + formatCode + " : "
                                   + doc.getTitle() + " : " + doc.getDescription());
            }
        } else {
            System.out.println("___ No documents found");
        }


    }

    /**
     * Auto generated test method
     */
    public static void testqueryPatient() throws java.lang.Exception {

        String country = "PT";
        String homeCommunityId = "2.16.17.710.820.1000.990.1";
        String id = "123456789";

        /*
         * Stub
         */
        ClientConnectorServiceServiceStub stub = new ClientConnectorServiceServiceStub(EPR);
        Assertion idAssertion = new HCPIAssertionCreator().createHCPIAssertion();
        addAssertions(stub, idAssertion, null);

        QueryPatientRequest queryPatientRequest = QueryPatientRequest.Factory.newInstance();

        /*
         * Patient
         */
        PatientDemographics pd = queryPatientRequest.addNewPatientDemographics();
        PatientId patientId = pd.addNewPatientId();
        patientId.setExtension(id);
        patientId.setRoot(homeCommunityId);
        pd.setCountry(country);

        /*
         * Request
         */
        QueryPatientDocument queryPatientDocument = QueryPatientDocument.Factory.newInstance();
        queryPatientDocument.addNewQueryPatient().setArg0(queryPatientRequest);

        QueryPatientResponseDocument queryPatientResponseDocument = stub.queryPatient(queryPatientDocument);

        PatientDemographics[] pdArray = queryPatientResponseDocument.getQueryPatientResponse().getReturnArray();

        /*
         * Response
         */
        if (pdArray != null && pdArray.length != 0) {
            for (PatientDemographics pd1 : pdArray) {
                PatientId patientId1 = pd1.getPatientIdArray(0);
                System.out.println("Patient found: " + patientId1.getRoot() + " : " + patientId1.getExtension()
                                   + " : " + pd1.getGivenName() + " " + pd1.getFamilyName() + " : "
                                   + pd1.getAdministrativeGender() + " : "
                                   + pd1.getCity() + ", " + pd1.getCountry());
            }

        } else {
            System.out.println("___ Patient not found!");
        }


    }

    /**
     * Auto generated test method
     */
    public static void testsayHello() throws java.lang.Exception {

        HCPIAssertionCreator idAssertionCreator = new HCPIAssertionCreator();
        Assertion idAssertion = idAssertionCreator.createHCPIAssertion();

        ClientConnectorServiceServiceStub stub = new ClientConnectorServiceServiceStub(EPR);
        addAssertions(stub, idAssertion, null);

        SayHelloDocument sayHelloDocument = SayHelloDocument.Factory.newInstance();
        sayHelloDocument.addNewSayHello().setArg0("John");

        SayHelloResponseDocument sayHelloResponseDocument = stub.sayHello(sayHelloDocument);
        String sayHelloResponse = sayHelloResponseDocument.getSayHelloResponse().getReturn();
        System.out.println("___ sayHello response:" + sayHelloResponse);

    }

    /**
     * Auto generated test method
     */
    public static void testretrieveDocument() throws java.lang.Exception {

        HCPIAssertionCreator idAssertionCreator = new HCPIAssertionCreator();
        Assertion idAssertion = idAssertionCreator.createHCPIAssertion();

        TRCAssertionCreator trcAssertionCreator = new TRCAssertionCreator();
        Assertion trcAssertion = trcAssertionCreator.createTRCAssertion();

        ClientConnectorServiceServiceStub stub = new ClientConnectorServiceServiceStub(EPR);
        addAssertions(stub, idAssertion, trcAssertion);

        RetrieveDocumentDocument1 retrieveDocumentDocument = RetrieveDocumentDocument1.Factory.newInstance();
        RetrieveDocument1 retrieveDocument = retrieveDocumentDocument.addNewRetrieveDocument();
        RetrieveDocumentRequest retrieveDocumentRequest = retrieveDocument.addNewArg0();

        DocumentId documentId = retrieveDocumentRequest.addNewDocumentId();
        documentId.setDocumentUniqueId("bla");
        documentId.setRepositoryUniqueId("2.16.17.710.815.1000.990.1");

        RetrieveDocumentResponseDocument retrieveDocumentResponseDocument = stub.retrieveDocument(retrieveDocumentDocument);

        EpsosDocument1 doc = retrieveDocumentResponseDocument.getRetrieveDocumentResponse().getReturn();

        String classCode = doc.getClassCode().getNodeRepresentation();
        String formatCode = doc.getFormatCode().getNodeRepresentation();
        System.out.println("___ Document: " + doc.getUuid() + " : " + classCode + " : " + formatCode + " : "
                           + doc.getTitle() + " : " + doc.getDescription());
        String CDA = new String(doc.getBase64Binary(), "UTF-8");
        System.out.println(CDA);
    }

    /**
     * Auto generated test method
     */
    public static void testsubmitDocument() throws java.lang.Exception {

        ClientConnectorServiceServiceStub stub =
                                          new ClientConnectorServiceServiceStub();//the default implementation should point to the right endpoint

//		epsos.openncp.protocolterminator.clientconnector.SubmitDocumentDocument1 submitDocument18=
//				(epsos.openncp.protocolterminator.clientconnector.SubmitDocumentDocument1)getTestObject(epsos.openncp.protocolterminator.clientconnector.SubmitDocumentDocument1.class);
        // TODO : Fill in the submitDocument18 here

//		assertNotNull(stub.submitDocument(
//				submitDocument18));




    }

    private static void addAssertions(ClientConnectorServiceServiceStub stub, Assertion idAssertion, Assertion trcAssertion) throws Exception {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMElement omSecurityElement = omFactory.createOMElement(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse"), null);
        if (trcAssertion != null) {
            omSecurityElement.addChild(XMLUtils.toOM(trcAssertion.getDOM()));
        }
        omSecurityElement.addChild(XMLUtils.toOM(idAssertion.getDOM()));
        stub._getServiceClient().addHeader(omSecurityElement);

    }
}
