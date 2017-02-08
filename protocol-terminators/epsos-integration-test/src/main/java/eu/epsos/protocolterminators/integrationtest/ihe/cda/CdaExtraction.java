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
package eu.epsos.protocolterminators.integrationtest.ihe.cda;

import eu.epsos.protocolterminators.integrationtest.common.AbstractIT;
import eu.epsos.protocolterminators.integrationtest.ihe.cda.dto.DetailedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import tr.com.srdc.epsos.util.xpath.XPathEvaluator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class CdaExtraction {

    private static final Logger logger = LoggerFactory.getLogger(CdaExtraction.class);

    public static String extract(MessageType msgType, String filePath) {
        Document msgDoc;
        msgDoc = AbstractIT.readDoc(filePath);

        return extract(msgType, msgDoc);
    }

    public static String extract(MessageType msgType, Document message) {
        HashMap<String, String> ns = new HashMap<>();
        Document msgDoc;
        XPathEvaluator evaluator;
        String xpathExpr = null;

        switch (msgType) {
            case PORTAL: {
                ns.put("cc", "http://clientconnector.protocolterminator.openncp.epsos/");
                xpathExpr = "//base64Binary";
                break;
            }
            case IHE: {
                ns.put("", "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0");
                ns.put("ns2", "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0");
                ns.put("ns3", "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0");
                ns.put("ns4", "urn:ihe:iti:xds-b:2007");
                ns.put("ns5", "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0");
                xpathExpr = "//ns4:Document";
                break;
            }
        }

        evaluator = new XPathEvaluator(ns);

        return evaluator.evaluate(message, xpathExpr).item(0).getFirstChild().getNodeValue();
    }

    public static DetailedResult unmarshalDetails(String xmlDetails) {

        InputStream is = new ByteArrayInputStream(xmlDetails.getBytes());

        JAXBContext jc = null;
        try {
            jc = JAXBContext.newInstance(DetailedResult.class);
        } catch (JAXBException ex) {
            logger.error(null, ex);
        }

        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = jc.createUnmarshaller();
        } catch (JAXBException ex) {
            logger.error(null, ex);
        }
        DetailedResult result = null;
        try {
            result = (DetailedResult) unmarshaller.unmarshal(is);
        } catch (JAXBException ex) {
            logger.error(null, ex);
        }

        return result;
    }

    private CdaExtraction() {
    }

    public enum MessageType {

        HL7, IHE, PORTAL;
    }
}
