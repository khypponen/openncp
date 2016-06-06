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
package eu.epsos.pt.transformation;

import epsos.ccd.posam.tm.response.TMResponseStructure;
import epsos.ccd.posam.tm.service.ITransformationService;
import epsos.ccd.posam.tsam.exception.ITMTSAMEror;
import eu.epsos.exceptions.DocumentTransformationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis2.util.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 * Encapsulates all the usage of Transformation Manager for transcoding or
 * translation processes.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public final class TMServices {

    private static Logger LOG = LoggerFactory.getLogger(TMServices.class);
    private final static String REPOSITORY_INTERNAL_ERROR = "XDSRepositoryError";

    /**
     * Encapsulates the TM usage, by accepting the document to translate, and
     * the selected language.
     *
     * @param document the document to translate, in a byte array form.
     * @param language the language of NPC-B, where the document is being
     * translated.
     * @return a translated document.
     * @throws DocumentTransformationException
     */
    public static byte[] transformDocument(byte[] document, String language) throws DocumentTransformationException {

        ITransformationService transformationService;
        Document resultDoc = null;
        TMResponseStructure tmResponse;
        byte[] result = null;

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("ctx_tm.xml");
        transformationService = (ITransformationService) applicationContext.getBean(ITransformationService.class.getName());

        resultDoc = byteToDocument(document);

        LOG.debug("STARTING TRANSLATING DOCUMENT TO: " + language + ".");

        tmResponse = transformationService.translate(resultDoc, language); //Perform the translation, according to specified language.

        if (!tmResponse.isStatusSuccess()) {
            throw new DocumentTransformationException(REPOSITORY_INTERNAL_ERROR, Constants.SERVER_IP, "An error has occurred during document translation, please check the following errors: \n" + processErrors(tmResponse.getErrors())); //If the translation process fails, an exception is thrown.
        }
        try {
            resultDoc = tmResponse.getResponseCDA(); //Obtain the translated document in the Document type format, only if translation succeeds.
            result = XMLUtils.toOM(resultDoc.getDocumentElement()).toString().getBytes("UTF-8"); //Obtains a byte array from the translation result.
        } catch (Exception ex) {
            throw new DocumentTransformationException(ex);
        }

        LOG.debug("TRANSLATION SUCCESSFULLY ENDED.");

        return result; //Return the Document as a byte array.
    }

    /**
     * Encapsulates the TM usage, by accepting the document to translate and
     * transcode to the pivot format.
     *
     * @param document the "friendly" document to translate/transcode, in a byte
     * array form.
     * @return pivot document.
     * @throws DocumentTransformationException
     */
    public static byte[] transformDocument(byte[] document) throws DocumentTransformationException {

        ITransformationService transformationService;
        Document resultDoc = null;
        TMResponseStructure tmResponse;
        byte[] result = null;

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("ctx_tm.xml");
        transformationService = (ITransformationService) applicationContext.getBean(ITransformationService.class.getName());

        resultDoc = byteToDocument(document);

        LOG.debug("STARTING TRANSLATING DOCUMENT TO PIVOT.");

        tmResponse = transformationService.toEpSOSPivot(resultDoc); //Perform the translation into pivot.

        if (!tmResponse.isStatusSuccess()) {
            processErrors(tmResponse.getErrors());
            throw new DocumentTransformationException("DOCUMENT TRANSLATION FAILED."); //If the translation process fails, an exception is thrown.
        }
        try {
            resultDoc = tmResponse.getResponseCDA(); //Obtain the translated document in the Document type format, only if translation succeeds.
            result = XMLUtils.toOM(resultDoc.getDocumentElement()).toString().getBytes("UTF-8"); //Obtains a byte array from the translation result.
        } catch (Exception ex) {
            throw new DocumentTransformationException(ex);
        }

        LOG.debug("TRANSLATION SUCCESSFULLY ENDED.");

        return result; //Return the Document as a byte array.
    }

    public static Document byteToDocument(byte[] document) throws DocumentTransformationException {

        String docString;
        Document resultDoc = null;

        try {
            docString = new String(document, "UTF-8"); //Convert document byte array into a String.
        } catch (UnsupportedEncodingException ex) {
            throw new DocumentTransformationException(ex);
        }

        try {
            resultDoc = XMLUtil.parseContent(docString); //Parse the String into a Document object.
        } catch (ParserConfigurationException ex) {
            throw new DocumentTransformationException(ex);
        } catch (SAXException ex) {
            throw new DocumentTransformationException(ex);
        } catch (IOException ex) {
            throw new DocumentTransformationException(ex);
        }

        return resultDoc;
    }

    /**
     * Handles errors resulted from the translation process. It currently only
     * prints them to LOG, later to be replaced with portal anwser.
     *
     * @param errors
     */
    private static String processErrors(List<ITMTSAMEror> errors) {

        StringBuilder sb = new StringBuilder();

        sb.append("List of errors: ");

        LOG.debug("TRANSLATION PROCESS ERRORS:");

        for (ITMTSAMEror error : errors) {
            LOG.info("Error: (Code: " + error.getCode() + ", Description: " + error.getDescription());
            sb.append("Error: (Code: " + error.getCode() + ", Description: " + error.getDescription());
            sb.append("\n");
        }
        return sb.toString();
    }

    private TMServices() {
    }
}
