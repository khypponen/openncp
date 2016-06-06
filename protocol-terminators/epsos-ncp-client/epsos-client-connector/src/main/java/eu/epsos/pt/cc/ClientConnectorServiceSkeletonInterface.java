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

import java.text.ParseException;

import epsos.openncp.protocolterminator.clientconnector.QueryDocumentsDocument;
import epsos.openncp.protocolterminator.clientconnector.QueryDocumentsResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.QueryPatientDocument;
import epsos.openncp.protocolterminator.clientconnector.QueryPatientResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.RetrieveDocumentDocument1;
import epsos.openncp.protocolterminator.clientconnector.RetrieveDocumentResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.SayHelloDocument;
import epsos.openncp.protocolterminator.clientconnector.SayHelloResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.SubmitDocumentDocument1;
import epsos.openncp.protocolterminator.clientconnector.SubmitDocumentResponseDocument;
import eu.epsos.exceptions.NoPatientIdDiscoveredException;
import eu.epsos.exceptions.XCAException;
import eu.epsos.exceptions.XdrException;
import org.opensaml.saml2.core.Assertion;

/**
 * ClientConnectorServiceSkeletonInterface java skeleton interface for the
 * axisService
 *
 * This Interface represents the contact point into the NCP-B, allowing the
 * Portal-B to contact and perform requests in NCP-B.
 */
public interface ClientConnectorServiceSkeletonInterface {

    /*
     * XCPD
     */
    /**
     * Specifies the signature of the operation responsible for patient
     * querying. It receives some demographic data to perform the query.
     *
     * @param queryPatient represents the query object.
     * @return a QueryPatientResponseDocument containing the query response(s).
     *
     * @see QueryPatientResponseDocument
     * @see QueryPatientDocument
     */
    public QueryPatientResponseDocument queryPatient(QueryPatientDocument queryPatient, Assertion hcpAssertion)
            throws NoPatientIdDiscoveredException, ParseException;

    /*
     * XCA
     */
    /**
     * Specifies the signature of the operation responsible for document
     * querying, receiving as parameter the required query object.
     *
     * @param queryDocuments represents the query object.
     * @return a QueryDocumentsResponseDocument containing the query
     * response(s).
     *
     * @see QueryDocumentsResponseDocument
     * @see QueryDocumentsDocument
     */
    public QueryDocumentsResponseDocument queryDocuments(QueryDocumentsDocument queryDocuments, Assertion hcpAssertion, Assertion trcAssertion) throws XCAException;

    /**
     * Specifies the signature of the operation responsible for document
     * retrieval, receiving the specific documents to retrieve as parameters.
     *
     * @param retrieveDocument the specific document to retrieve.
     * @return the retrieved document.
     *
     * @see RetrieveDocumentResponseDocument
     * @see RetrieveDocumentDocument1
     */
    public RetrieveDocumentResponseDocument retrieveDocument(RetrieveDocumentDocument1 retrieveDocument, Assertion hcpAssertion, Assertion trcAssertion) throws XCAException;

    /*
     * XDR
     */
    /**
     * Specifies the signature of the operation responsible for document
     * submitting, accepting the documents to submit as parameter.
     *
     * @param submitDocument the document to submit.
     * @return a SubmitDocumentResponseDocument object.
     *
     * @see SubmitDocumentResponseDocument
     * @see SubmitDocumentDocument1
     */
    public SubmitDocumentResponseDocument submitDocument(SubmitDocumentDocument1 submitDocument, Assertion hcpAssertion, Assertion trcAssertion) throws XdrException, ParseException;

    /*
     * Auxiliar
     */
    /**
     * This is a test method signature.
     *
     * @param sayHello a sayHello document.
     * @return a test response.
     */
    public SayHelloResponseDocument sayHello(SayHelloDocument sayHello);
}