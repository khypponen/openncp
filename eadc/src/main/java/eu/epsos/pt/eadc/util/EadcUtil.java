/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2013  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
package eu.epsos.pt.eadc.util;

import com.spirit.epsos.cc.adc.EadcEntry;
import com.spirit.epsos.cc.adc.EadcReceiverImpl;
import eu.epsos.pt.eadc.datamodel.ObjectFactory;
import eu.epsos.pt.eadc.datamodel.Transaction;
import eu.epsos.pt.eadc.datamodel.TransactionInfo;
import eu.epsos.pt.eadc.helper.TransactionHelper;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.util.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * The purpose of this class is to help the eADC invocation process, by
 * providing utility methods to invoke the service.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class EadcUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EadcUtil.class);
    private static String defaultDsPath = null;

    /**
     * This utility method will invoke eADC for a given transaction. You will
     * need to provide the Axis Request and Response message context, together
     * with optional document, a Transaction Info object containing transaction
     * details and a selected data source.
     *
     * @param reqMsgContext the Servlet request message context
     * @param rspMsgContext the Servlet response message context
     * @param cdaDocument   the optional CDA document, leave as null if not
     *                      necessary
     * @param transInfo     the Transaction Info object
     * @throws ParserConfigurationException
     * @throws Exception
     */
    public static void invokeEadc(MessageContext reqMsgContext, MessageContext rspMsgContext, Document cdaDocument, TransactionInfo transInfo, EadcEntry.DsTypes datasource) throws ParserConfigurationException, Exception {
        Document reqEnv = null;
        Document respEnv = null;
        EadcEntry eadcEntry = null;
        EadcReceiverImpl eadcReceiver = null;
        Transaction transaction;
        Document transDoc = null;


        LOGGER.info("START eADC TRANSACTION PROCESSING...");

        reqEnv = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        respEnv = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        if (reqMsgContext != null && reqMsgContext.getEnvelope() != null) {
            reqEnv.adoptNode(XMLUtils.toDOM(reqMsgContext.getEnvelope()));
        }
        if (rspMsgContext != null && rspMsgContext.getEnvelope() != null) {
            respEnv.adoptNode(XMLUtils.toDOM(rspMsgContext.getEnvelope()));
        }

        transaction = buildTransaction(transInfo);

        if (cdaDocument != null) {
            transDoc = TransactionHelper.insertCdaInTransaction(transaction, cdaDocument);
        } else {
            transDoc = TransactionHelper.convertTransaction(transaction);
        }

        eadcEntry = EadcFactory.INSTANCE.getEntry(datasource.toString(), transDoc, reqEnv, respEnv);
        eadcReceiver = new EadcReceiverImpl();
        eadcReceiver.process(eadcEntry);

        LOGGER.info("FINISH eADC TRANSACTION PROCESSING.");
    }

    /**
     * Fills a Transaction object with a Transaction Info element.
     *
     * @param transInfo
     * @return
     */
    private static Transaction buildTransaction(TransactionInfo transInfo) {
        Transaction result = new ObjectFactory().createComplexTypeTransaction();

        result.setTransactionInfo(transInfo);

        return result;
    }

    /**
     * @return the defaultDsPath
     */
    public static String getDefaultDsPath() {
        if (defaultDsPath == null) {
            defaultDsPath = getEpsosPropsPath();
        }
        return defaultDsPath;
    }

    /**
     * Helper method to return the system epSOS Properties Path
     *
     * @return the full epSOS Props Path currently on the system.
     */
    public static String getEpsosPropsPath() {
        String path = System.getenv("EPSOS_PROPS_PATH");

        if (path == null) {
            path = System.getProperty("EPSOS_PROPS_PATH");
            if (path == null) {
                LOGGER.error("EPSOS_PROPS_PATH not found!");
                return null;
            }
        }
        return path;
    }

    /**
     * convertXMLDocumentToString
     *
     * @param xmlDocument
     * @return String
     * @throws Exception
     */
    public static String convertXMLDocumentToString(Document xmlDocument) throws Exception {
        if (xmlDocument == null) {
            // debug
            //
            LOGGER.warn("XML Document is NULL. Can't convert XML Document to String.");
            return "";
        }
        // set the TransformFactory to use the Saxon TransformerFactoryImpl method
        System.setProperty("javax.xml.transform.TransformerFactory",
                "net.sf.saxon.TransformerFactoryImpl");
        StringWriter objStrWriter = new StringWriter();
        StreamResult objStreamResult = new StreamResult(objStrWriter);
        TransformerFactory objTransFactory = TransformerFactory.newInstance();
        Transformer objTransformer = objTransFactory.newTransformer();
        objTransformer.setOutputProperty(OutputKeys.ENCODING,
                "utf-8");
        objTransformer.setOutputProperty(OutputKeys.INDENT,
                "yes");
        objTransformer.setOutputProperty(OutputKeys.METHOD,
                "xml");
        objTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
                "4");
        objTransformer.transform(new DOMSource(xmlDocument.getDocumentElement()),
                objStreamResult);
        return objStreamResult.getWriter()
                .toString();
    }

    /**
     * This enum represents the transaction directions
     */
    public static enum Direction {

        INBOUND("INBOUND"), OUTBOUND("OUTBOUND");
        private String value;

        private Direction(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
