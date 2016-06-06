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
package eu.epsos.pt.cc;

import epsos.openncp.protocolterminator.clientconnector.*;
import eu.epsos.exceptions.NoPatientIdDiscoveredException;
import eu.epsos.exceptions.XCAException;
import eu.epsos.exceptions.XdrException;
import eu.epsos.pt.cc.dts.axis2.DocumentDts;
import eu.epsos.pt.cc.dts.axis2.QueryPatientResponseDts;
import eu.epsos.pt.cc.dts.axis2.RetrieveDocumentResponseDTS;
import eu.epsos.pt.cc.dts.axis2.SubmitDocumentResponseDts;
import eu.epsos.pt.cc.dts.axis2.XdsDocumentDts;
import eu.epsos.pt.cc.stub.ConsentService;
import eu.epsos.pt.cc.stub.DispensationService;
import eu.epsos.pt.cc.stub.HcerService;
import eu.epsos.pt.cc.stub.IdentificationService;
import eu.epsos.pt.cc.stub.MroService;
import eu.epsos.pt.cc.stub.OrderService;
import eu.epsos.pt.cc.stub.PatientService;
import eu.epsos.util.IheConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.text.ParseException;
import java.util.List;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.spms.epsos.utils.logging.LoggingSlf4j;
import tr.com.srdc.epsos.data.model.XdrResponse;
import tr.com.srdc.epsos.data.model.xds.QueryResponse;
import tr.com.srdc.epsos.data.model.xds.XDSDocument;
import tr.com.srdc.epsos.util.Constants;

/**
 * ClientConnectorServiceSkeleton java skeleton for the axisService.
 *
 * This class implements the contact point into the NCP-B, allowing the Portal-B
 * to contact and perform requests in NCP-B.
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class ClientConnectorServiceSkeleton implements ClientConnectorServiceSkeletonInterface {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ClientConnectorServiceSkeleton.class);

    /*
     * XCPD
     */
    /**
     * Performs international search for a patient, filtering by a set of
     * demographics. This method is an adapter for usage of a XCPD client.
     *
     * @param queryPatient axis wrapper for element: <code>queryPatient</code>.
     * This encapsulates, destination Country Code and Patient's demographics.
     *
     * @return a QueryPatientResponseDocument containing the query response(s).
     * @throws ParseException
     */
    @Override
    public QueryPatientResponseDocument queryPatient(final QueryPatientDocument queryPatient, Assertion assertion)
            throws NoPatientIdDiscoveredException, ParseException {

        /*
         * Setup
         */
        final String methodName = "queryPatient";
        LoggingSlf4j.start(LOG, methodName);
        String cc = Constants.COUNTRY_CODE;

        QueryPatientResponseDocument result = QueryPatientResponseDocument.Factory.newInstance();

        /*
         * Body
         */
        try {
            /* create request */
            List<tr.com.srdc.epsos.data.model.PatientDemographics> xcpdResp;

            tr.com.srdc.epsos.data.model.PatientDemographics request;
            QueryPatientRequest arg0 = queryPatient.getQueryPatient().getArg0();
            PatientDemographics pDemographic = arg0.getPatientDemographics();
            request = eu.epsos.pt.cc.dts.PatientDemographicsDts.newInstance(pDemographic);

            String countryCode = arg0.getCountryCode();

            xcpdResp = IdentificationService.findIdentityByTraits(request, assertion, countryCode); // call XCPD Client

            /* result */
            QueryPatientResponse response;
            List<PatientDemographics> aux;
            aux = eu.epsos.pt.cc.dts.axis2.PatientDemographicsDts.newInstance(xcpdResp);
            response = QueryPatientResponseDts.newInstance(aux);

            result.setQueryPatientResponse(response);

        } catch (RuntimeException ex) {
            LoggingSlf4j.error(LOG, methodName);
            throw ex;
        }

        LoggingSlf4j.end(LOG, methodName);
        return result;
    }

    /*
     * XCA
     */
    /**
     * Performs international search for documents. Filtering by patient and
     * document code. This method is an adapter for the usage of a XCA client.
     *
     * @param queryDocuments axis wrapper for * * * * * * *
     * element: <code>queryDocuments</code>. This encapsulates, destination
     * Country Code, patient's identification and documents class code.
     *
     * @return a QueryDocumentsResponseDocument containing the query
     * response(s).
     */
    @Override
    public QueryDocumentsResponseDocument queryDocuments(QueryDocumentsDocument queryDocuments, Assertion hcpAssertion, Assertion trcAssertion) throws XCAException {
        /*
         * Setup
         */
        final String methodName = "queryDocuments";
        LoggingSlf4j.start(LOG, methodName);

        QueryDocumentsResponse result = QueryDocumentsResponse.Factory.newInstance();

        /*
         * Body
         */

        /* retrive data from parameters */
        QueryDocuments queryDocuments1 = queryDocuments.getQueryDocuments();
        QueryDocumentRequest arg0 = queryDocuments1.getArg0();
        String countryCode = arg0.getCountryCode();


        PatientId tmp = arg0.getPatientId();
        tr.com.srdc.epsos.data.model.PatientId patientId = eu.epsos.pt.cc.dts.PatientIdDts.newInstance(tmp);

        GenericDocumentCode tmpCode = arg0.getClassCode();
        tr.com.srdc.epsos.data.model.GenericDocumentCode documentCode = eu.epsos.pt.cc.dts.GenericDocumentCodeDts.newInstance(tmpCode);

        if (documentCode.getSchema().equals(IheConstants.ClASSCODE_SCHEME) == false) {
            throw new RuntimeException("Unsupported Class Code scheme: " + documentCode.getSchema());
        }

        /* perform the call */
        try {
            QueryResponse response = null;

            if (documentCode.getValue().equals(tr.com.srdc.epsos.util.Constants.PS_CLASSCODE)) {
                response = PatientService.list(patientId, countryCode, documentCode, hcpAssertion, trcAssertion);
            } else if (documentCode.getValue().equals(tr.com.srdc.epsos.util.Constants.EP_CLASSCODE)) {
                response = OrderService.list(patientId, countryCode, documentCode, hcpAssertion, trcAssertion);
            } else if (documentCode.getValue().equals(tr.com.srdc.epsos.util.Constants.MRO_CLASSCODE)) {
                response = MroService.list(patientId, countryCode, documentCode, hcpAssertion, trcAssertion);
            } else {
                throw new RuntimeException("Unsupported Class Code: " + documentCode.getValue());
            }

            if (response.getDocumentAssociations() != null && !response.getDocumentAssociations().isEmpty()) {
                result.setReturnArray(DocumentDts.newInstance(response.getDocumentAssociations()));
            }

        } catch (RuntimeException ex) {
            LoggingSlf4j.error(LOG, methodName);
            throw ex;
        }


        /* create return wrapper */
        QueryDocumentsResponseDocument wapper = QueryDocumentsResponseDocument.Factory.newInstance();
        wapper.setQueryDocumentsResponse(result);


        LoggingSlf4j.end(LOG, methodName);
        return wapper;
    }

    /**
     * Performs international search for documents. Filtering by patient and
     * document code. This method is an adapter for usage of a XCA client.
     *
     * It makes use of the XCA Service Client library.
     *
     * @param retrieveDocument axis wrapper for * * * * * * *
     * element: <code>retrieveDocument</code>. This encapsulates, destination
     * Country Code, patient's identification and document's identification.
     *
     * @return the retrieved document.
     */
    @Override
    public RetrieveDocumentResponseDocument retrieveDocument(RetrieveDocumentDocument1 retrieveDocument, Assertion hcpAssertion, Assertion trcAssertion) throws XCAException {
        /*
         * Setup
         */
        final String methodName = "retrieveDocument";
        LoggingSlf4j.start(LOG, methodName);


        RetrieveDocumentResponse result;
        /*
         * Body
         */
        RetrieveDocument1 retrieveDocument1 = retrieveDocument.getRetrieveDocument();
        RetrieveDocumentRequest arg0 = retrieveDocument1.getArg0();
        String countryCode = arg0.getCountryCode();
        DocumentId xdsDocument = arg0.getDocumentId();
        String homeCommunityId = arg0.getHomeCommunityId();
        String targetLanguage = arg0.getTargetLanguage();

        GenericDocumentCode tmpCode = arg0.getClassCode();
        tr.com.srdc.epsos.data.model.GenericDocumentCode documentCode = eu.epsos.pt.cc.dts.GenericDocumentCodeDts.newInstance(tmpCode);

        if (documentCode.getSchema().equals(IheConstants.ClASSCODE_SCHEME) == false) {
            throw new RuntimeException("Unsupported Class Code scheme: " + documentCode.getSchema());
        }

        try {
            DocumentResponse response = null;
            XDSDocument request = XdsDocumentDts.newInstance(xdsDocument);
            request.setClassCode(documentCode);

            if (documentCode.getValue().equals(tr.com.srdc.epsos.util.Constants.PS_CLASSCODE)) {
                response = PatientService.retrieve(request, homeCommunityId, countryCode, targetLanguage, hcpAssertion, trcAssertion);
            } else if (documentCode.getValue().equals(tr.com.srdc.epsos.util.Constants.EP_CLASSCODE)) {
                response = OrderService.retrieve(request, homeCommunityId, countryCode, targetLanguage, hcpAssertion, trcAssertion);
            } else if (documentCode.getValue().equals(tr.com.srdc.epsos.util.Constants.MRO_CLASSCODE)) {
                response = MroService.retrieve(request, homeCommunityId, countryCode, targetLanguage, hcpAssertion, trcAssertion);
            } else {
                throw new RuntimeException("Unsupported Class Code: " + documentCode.getValue());
            }

            result = RetrieveDocumentResponseDTS.newInstance(response);

        } catch (RuntimeException ex) {
            LoggingSlf4j.error(LOG, methodName);
            throw ex;
        }

        /* create return wrapper */
        RetrieveDocumentResponseDocument wrapper = RetrieveDocumentResponseDocument.Factory.newInstance();
        wrapper.setRetrieveDocumentResponse(result);
        LoggingSlf4j.end(LOG, methodName);
        return wrapper;
    }

    /*
     * XDR
     */
    /**
     * Submits a document to a foreign country. This method is an adapter for
     * usage of a XDR client.
     *
     * This method makes use of the XDR Service Client library.
     *
     * @param submitDocument axis wrapper for * * * * * * *
     * element: <code>submitDocument</code>. This encapsulates, destination
     * Country Code and the document to submit with some Metadata.
     * @return a SubmitDocumentResponseDocument object.
     * @throws ParseException
     */
    @Override
    public SubmitDocumentResponseDocument submitDocument(final SubmitDocumentDocument1 submitDocument, Assertion hcpAssertion, Assertion trcAssertion) throws XdrException, ParseException {

        /*
         * Setup
         */
        final String methodName = "submitDocument";
        LoggingSlf4j.start(LOG, methodName);

        SubmitDocumentResponseDocument result = SubmitDocumentResponseDocument.Factory.newInstance();

        try {

            XdrResponse response = null;

            /*  create Xdr request */
            SubmitDocument1 submitDocument1 = submitDocument.getSubmitDocument();
            SubmitDocumentRequest arg0 = submitDocument1.getArg0();
            String countryCode = arg0.getCountryCode();
            EpsosDocument1 document = arg0.getDocument();
            PatientDemographics patient = arg0.getPatientDemographics();

            String classCode_node;
            GenericDocumentCode classCode = document.getClassCode();
            if (classCode.getSchema().equals(IheConstants.ClASSCODE_SCHEME) == false) {
                throw new RuntimeException("Unsupported Class Code scheme: " + classCode.getSchema());
            }
            classCode_node = classCode.getNodeRepresentation();


            if (classCode_node.equals(Constants.CONSENT_CLASSCODE)) {
                response = ConsentService.put(document, patient, countryCode, hcpAssertion, trcAssertion); // call XDR Client for Consent

            } else if (classCode_node.equals(Constants.ED_CLASSCODE)) {
                response = DispensationService.initialize(document, patient, countryCode, hcpAssertion, trcAssertion); // call XDR Client for eP

            } else if (classCode_node.equals(Constants.HCER_CLASSCODE)) {
                response = HcerService.submit(document, patient, countryCode, hcpAssertion, trcAssertion); // call XDR Client for HCER

            } else {
                throw new RuntimeException("Unsupported Class Code: " + classCode_node);
            }

            result.setSubmitDocumentResponse(SubmitDocumentResponseDts.newInstance(response));

        } catch (RuntimeException ex) {
            LoggingSlf4j.error(LOG, methodName);
            throw ex;
        }

        LoggingSlf4j.end(LOG, methodName);
        return result;
    }

    /*
     * Auxiliar
     */
    /**
     * Greets someone by saying hello. This is an auxiliary operation for
     * diagnosis purposes.
     *
     * @param sayHello axis wrapper for element: <code>sayHello</code>. This
     * encapsulates a <code>String</code>.
     * @return a text in the format: Hello + <code>sayHello</code>.
     */
    @Override
    public SayHelloResponseDocument sayHello(SayHelloDocument sayHello) {

        /*
         * Setup
         */
        final String methodName = "sayHello";
        LoggingSlf4j.start(LOG, methodName);

        SayHelloResponseDocument result = SayHelloResponseDocument.Factory.newInstance();

        /*
         * Body
         */
        SayHelloResponse resp;
        resp = SayHelloResponse.Factory.newInstance();
        resp.setReturn("Hello " + sayHello.getSayHello().getArg0());

        result.setSayHelloResponse(resp);

        LoggingSlf4j.end(LOG, methodName);
        return result;
    }
}
