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
package epsos.openncp.protocolterminator;

import epsos.openncp.protocolterminator.clientconnector.*;
import epsos.openncp.pt.client.ClientConnectorServiceServiceStub;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.util.XMLUtils;
import org.opensaml.saml2.core.Assertion;

import javax.xml.namespace.QName;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

/*
 *  ClientConnectorServiceServiceTest Junit test case
 */
public class ClientConnectorConsumer {

    private final long TIMEOUT = 3 * 60 * 1000; // Three minutes
    private String epr;

    public ClientConnectorConsumer(String epr) {
        this.epr = epr;
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

    /**
     * Auto generated test method
     */
    public List<EpsosDocument1> queryDocuments(Assertion idAssertion, Assertion trcAssertion, String countryCode,
                                               PatientId patientId, GenericDocumentCode classCode) {
        ClientConnectorServiceServiceStub stub;
        try {
            stub = new ClientConnectorServiceServiceStub(epr);
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(TIMEOUT);
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        }
        try {
            addAssertions(stub, idAssertion, trcAssertion);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        QueryDocumentsDocument queryDocumentsDocument = QueryDocumentsDocument.Factory.newInstance();
        QueryDocuments queryDocuments = queryDocumentsDocument.addNewQueryDocuments();
        QueryDocumentRequest queryDocumentRequest = queryDocuments.addNewArg0();
        queryDocumentRequest.setClassCode(classCode);
        queryDocumentRequest.setPatientId(patientId);
        queryDocumentRequest.setCountryCode(countryCode);

        QueryDocumentsResponseDocument queryDocumentsResponseDocument;
        try {
            queryDocumentsResponseDocument = stub.queryDocuments(queryDocumentsDocument);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        EpsosDocument1[] docArray = queryDocumentsResponseDocument.getQueryDocumentsResponse().getReturnArray();
        return Arrays.asList(docArray);
    }

    /**
     *
     * @param idAssertion
     * @param countryCode
     * @param pd
     * @return
     */
    public List<PatientDemographics> queryPatient(Assertion idAssertion, String countryCode,
                                                  PatientDemographics pd) {
        /*
         * Stub
         */
        ClientConnectorServiceServiceStub stub;
        try {
            stub = new ClientConnectorServiceServiceStub(epr);
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(TIMEOUT);
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        }
        try {
            addAssertions(stub, idAssertion, null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        QueryPatientRequest queryPatientRequest = QueryPatientRequest.Factory.newInstance();

        /*
         * Patient
         */
        queryPatientRequest.setPatientDemographics(pd);
        queryPatientRequest.setCountryCode(countryCode);

        /*
         * Request
         */
        QueryPatientDocument queryPatientDocument = QueryPatientDocument.Factory.newInstance();
        queryPatientDocument.addNewQueryPatient().setArg0(queryPatientRequest);

        QueryPatientResponseDocument queryPatientResponseDocument;
        try {
            queryPatientResponseDocument = stub.queryPatient(queryPatientDocument);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        PatientDemographics[] pdArray = queryPatientResponseDocument.getQueryPatientResponse().getReturnArray();
        return Arrays.asList(pdArray);
    }

    /**
     * Auto generated test method
     */
    public String sayHello(String name) {
        ClientConnectorServiceServiceStub stub;
        try {
            stub = new ClientConnectorServiceServiceStub(epr);
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(TIMEOUT);
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        }

        SayHelloDocument sayHelloDocument = SayHelloDocument.Factory.newInstance();
        sayHelloDocument.addNewSayHello().setArg0(name);

        SayHelloResponseDocument sayHelloResponseDocument;
        try {
            sayHelloResponseDocument = stub.sayHello(sayHelloDocument);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        return sayHelloResponseDocument.getSayHelloResponse().getReturn();
    }

    /**
     * Auto generated test method
     */
    public EpsosDocument1 retrieveDocument(Assertion idAssertion, Assertion trcAssertion, String countryCode,
                                           DocumentId documentId, String homeCommunityId, GenericDocumentCode classCode, String targetLanguage) {
        ClientConnectorServiceServiceStub stub;
        try {
            stub = new ClientConnectorServiceServiceStub(epr);
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(TIMEOUT);
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        }
        try {
            addAssertions(stub, idAssertion, trcAssertion);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        RetrieveDocumentDocument1 retrieveDocumentDocument = RetrieveDocumentDocument1.Factory.newInstance();
        RetrieveDocument1 retrieveDocument = retrieveDocumentDocument.addNewRetrieveDocument();

        RetrieveDocumentRequest retrieveDocumentRequest = retrieveDocument.addNewArg0();
        retrieveDocumentRequest.setDocumentId(documentId);
        retrieveDocumentRequest.setHomeCommunityId(homeCommunityId);
        retrieveDocumentRequest.setCountryCode(countryCode);
        retrieveDocumentRequest.setClassCode(classCode);
        retrieveDocumentRequest.setTargetLanguage(targetLanguage);

        RetrieveDocumentResponseDocument retrieveDocumentResponseDocument;
        try {
            retrieveDocumentResponseDocument = stub.retrieveDocument(retrieveDocumentDocument);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return retrieveDocumentResponseDocument.getRetrieveDocumentResponse().getReturn();
    }

    /**
     * @param idAssertion
     * @param trcAssertion
     * @param countryCode
     * @param documentId
     * @param homeCommunityId
     * @param classCode
     * @return
     */
    @Deprecated
    public EpsosDocument1 retrieveDocument(Assertion idAssertion, Assertion trcAssertion, String countryCode,
                                           DocumentId documentId, String homeCommunityId, GenericDocumentCode classCode) {
        return retrieveDocument(idAssertion, trcAssertion, countryCode, documentId, homeCommunityId, classCode, null);
    }

    /**
     * Auto generated test method
     */
    public SubmitDocumentResponse submitDocument(Assertion idAssertion, Assertion trcAssertion, String countryCode,
                                                 EpsosDocument1 document, PatientDemographics pd) {

        SubmitDocumentResponse response;
        ClientConnectorServiceServiceStub stub;
        try {
            stub = new ClientConnectorServiceServiceStub(epr);
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(TIMEOUT);
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        }
        try {
            addAssertions(stub, idAssertion, trcAssertion);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        SubmitDocumentDocument1 submitDocumentDoc = SubmitDocumentDocument1.Factory.newInstance();
        SubmitDocument1 submitDocument = SubmitDocument1.Factory.newInstance();
        SubmitDocumentRequest submitDocRequest = SubmitDocumentRequest.Factory.newInstance();

        submitDocRequest.setPatientDemographics(pd);
        submitDocRequest.setDocument(document);
        submitDocRequest.setCountryCode(countryCode);
        submitDocument.setArg0(submitDocRequest);
        submitDocumentDoc.setSubmitDocument(submitDocument);


        try {
            response = stub.submitDocument(submitDocumentDoc).getSubmitDocumentResponse();
        } catch (RemoteException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return response;
    }
}
