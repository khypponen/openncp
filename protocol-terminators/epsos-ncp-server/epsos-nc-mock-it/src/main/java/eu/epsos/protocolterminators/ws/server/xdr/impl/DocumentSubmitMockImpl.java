/**
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 Kela (The Social Insurance Institution of Finland)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contact email: epsos@kanta.fi or Konstantin.Hypponen@kela.fi
 */
package eu.epsos.protocolterminators.ws.server.xdr.impl;

import eu.epsos.protocolterminators.ws.server.common.NationalConnectorGateway;
import eu.epsos.protocolterminators.ws.server.exception.NIException;
import eu.epsos.protocolterminators.ws.server.xdr.DocumentProcessingException;
import eu.epsos.protocolterminators.ws.server.xdr.DocumentSubmitInterface;
import fi.kela.se.epsos.data.model.ConsentDocumentMetaData;
import fi.kela.se.epsos.data.model.DocumentAssociation;
import fi.kela.se.epsos.data.model.EDDocumentMetaData;
import fi.kela.se.epsos.data.model.EPSOSDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;
import tr.com.srdc.epsos.util.PrettyPrinter;
import tr.com.srdc.epsos.util.XMLUtil;

import javax.xml.transform.TransformerException;

/**
 * Mock implementation of the DocumentSubmitInterface, to be replaced nationally.
 * @author danielgronberg
 * @author Konstantin.Hypponen@kela.fi
 */
public class DocumentSubmitMockImpl extends NationalConnectorGateway implements DocumentSubmitInterface {

    public static Logger logger = LoggerFactory.getLogger(DocumentSubmitMockImpl.class);

    public DocumentSubmitMockImpl() {
        logger.info("Instantiating DocumentSubmitMockImpl");
    }

    /**
     * Stores a dispensation in the national infrastructure
     * @param dispensation eDispensation document in epSOS pivot (CDA) form
     * @throws DocumentProcessingException
     */
    @Override
    public void submitDispensation(EPSOSDocument dispensationDocument) throws NIException, InsufficientRightsException {
        String dispensation = null;
        try {
            dispensation = XMLUtil.prettyPrint(dispensationDocument.getDocument().getFirstChild());
        } catch (TransformerException e) {
            throwDocumentProcessingException("Cannot parse dispensation!", "4106");
        }
        logger.info("eDispensation document content:");
        logger.info(dispensation);


        if (dispensation == null || dispensation.isEmpty()) {
            throwDocumentProcessingException("dispensation is null or empty!", "4106");
        }

        if (dispensation.contains("testSubmitNoEP")) {
            logger.error("Tried to submit dispensation with no matching ePrescription.");
            throwDocumentProcessingException("testSubmitNoEP", "4105");
        }

        if (dispensation.contains("testSubmitDispEP")) {
            logger.error("Tried to submit already dispensed ePrescription.");
            throwDocumentProcessingException("testSubmitDispEP", "4106");
        }
    }

    /**
     * Discards a previously submitted dispensation
     * @param epsosOID Id of the dispensation to be discarded
     */
    @Override
    public void cancelDispensation(DocumentAssociation<EDDocumentMetaData> dispensationToDiscard) throws NIException, InsufficientRightsException {
        logger.info("eDispensation to be discarded:");
        logger.info(dispensationToDiscard.getXMLDocumentMetaData().getId());
    }

    /**
     * Discards a previously submitted consent
     * @param consentToDiscard Metadata of the consent to be discarded (XML and PDF versions)
     */
    @Override
    public void cancelConsent(DocumentAssociation<ConsentDocumentMetaData> consentToDiscard) throws NIException, InsufficientRightsException {
        logger.info("Consent to be discarded:");
        logger.info(consentToDiscard.getXMLDocumentMetaData().getId());
    }

    /**
     * Stores a patient consent in the national infrastructure
     * @param patient consent document in epSOS pivot (CDA) form
     */
    @Override
    public void submitPatientConsent(EPSOSDocument consentDocument) throws NIException, InsufficientRightsException {
        String consent = null;
        try {
            consent = PrettyPrinter.prettyPrint(consentDocument.getDocument());
        } catch (TransformerException e) {
            throwDocumentProcessingException("Cannot parse consent!", "4106");
        }
        logger.info("Patient consent content:");
        logger.info(consent);
    }

    private void throwDocumentProcessingException(String message, String code) throws DocumentProcessingException {
        DocumentProcessingException dpe = new DocumentProcessingException();
        dpe.setMessage(message);
        dpe.setCode(code);
        throw dpe;
    }

    @Override
    public void submitHCER(EPSOSDocument hcerDocument) throws DocumentProcessingException {
        String consent = null;
        try {
            consent = PrettyPrinter.prettyPrint(hcerDocument.getDocument());
        } catch (TransformerException e) {
            throwDocumentProcessingException("Cannot parse HCER!", "4106");
        }
        logger.info("HCER document content:");
        logger.info(consent);
    }
}
