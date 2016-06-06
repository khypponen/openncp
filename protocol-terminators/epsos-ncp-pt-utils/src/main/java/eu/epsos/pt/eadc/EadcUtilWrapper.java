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
package eu.epsos.pt.eadc;

import com.spirit.epsos.cc.adc.EadcEntry;
import ee.affecto.epsos.util.EventLogClientUtil;
import eu.epsos.pt.eadc.datamodel.ObjectFactory;
import eu.epsos.pt.eadc.datamodel.TransactionInfo;
import eu.epsos.pt.eadc.util.EadcUtil;
import eu.epsos.pt.eadc.util.EadcUtil.Direction;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.util.XMLUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tr.com.srdc.epsos.securityman.helper.Helper;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 * This class wraps the EADC invocation. As it gathers several aspects required
 * to its proper usage, such as the compilation and preparation of transaction
 * details.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class EadcUtilWrapper {

    /*
     * Date format according to RFC 2822 specifications.
     */
    private static final SimpleDateFormat RFC822DATEFORMAT = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.ROOT);

    /**
     * Main EADC Wrapper operation. It receives as input all the required
     * information to successfully fill a transaction object.
     *
     * @param reqMsgCtx the request Servlet Message Context
     * @param respMsgCtx the response Servlet Message Context
     * @param CDA the (optional) CDA document
     * @param idAssertion the Identity Assertion
     * @param startTime the transaction start time
     * @param endTime the transaction end time
     * @param rcvgIso the country A ISO Code
     *
     * @throws ParserConfigurationException
     * @throws Exception
     */
    public static void invokeEadc(MessageContext reqMsgCtx,
                                  MessageContext respMsgCtx,
                                  ServiceClient serviceClient,
                                  Document CDA,
                                  Date startTime,
                                  Date endTime,
                                  String rcvgIso,
                                  EadcEntry.DsTypes dsType,
                                  Direction direction) throws ParserConfigurationException, Exception {

        EadcUtil.invokeEadc(reqMsgCtx,
                            respMsgCtx,
                            CDA,
                            buildTransactionInfo(reqMsgCtx, respMsgCtx, serviceClient, direction, startTime, endTime, rcvgIso),
                            dsType);
    }

    /**
     * Builds a Transaction Info object based on a set of information.
     *
     * @param reqMsgContext the request Servlet Message Context
     * @param rspMsgContext the response Servlet Message Context
     * @param direction the request direction, INBOUND or OUTBOUND
     * @param idAssertion the Identity Assertion
     * @param startTime the transaction start time
     * @param endTime the transaction end time
     * @param countryAcode the country A ISO Code
     *
     * @return the filled Transaction Info object
     */
    private static TransactionInfo buildTransactionInfo(MessageContext reqMsgContext,
                                                        MessageContext rspMsgContext,
                                                        ServiceClient serviceClient,
                                                        Direction direction,
                                                        Date startTime,
                                                        Date endTime,
                                                        String countryAcode) throws Exception {

        TransactionInfo result = new ObjectFactory().createComplexTypeTransactionInfo();

        result.setAuthentificationLevel("");

        result.setDirection(direction.toString());

        result.setStartTime(getDateAsRFC822String(startTime));
        result.setEndTime(getDateAsRFC822String(endTime));
        result.setDuration(String.valueOf(endTime.getTime() - startTime.getTime()));

        result.setHomeAddress(EventLogClientUtil.getLocalIpAddress());

        if (reqMsgContext != null && reqMsgContext.getOptions() != null && reqMsgContext.getOptions().getFrom() != null && reqMsgContext.getOptions().getFrom().getAddress() != null) {
            result.setHomeHost(reqMsgContext.getOptions().getFrom().getAddress());
        }
        result.setHomeHCID(Constants.HOME_COMM_ID);
        result.setHomeISO(Constants.COUNTRY_CODE);
        result.setHomeNCPOID("");

        result.setHumanRequestor(extractAssertionInfo(getAssertion(reqMsgContext), "urn:oasis:names:tc:xacml:1.0:subject:subject-id"));

        if (reqMsgContext != null && reqMsgContext.getOptions() != null && reqMsgContext.getOptions().getUserName() != null) {
            result.setUserId(reqMsgContext.getOptions().getUserName());
        }

        result.setPOC(extractAssertionInfo(getAssertion(reqMsgContext), "urn:epsos:names:wp3.4:subject:healthcare-facility-type"));
        result.setPOCID("");

        result.setReceivingISO(countryAcode);
        if (serviceClient != null && serviceClient.getOptions() != null && serviceClient.getOptions().getTo() != null && serviceClient.getOptions().getTo().getAddress() != null) {
            result.setReceivingHost(serviceClient.getOptions().getTo().getAddress());
            result.setReceivingAddr(EventLogClientUtil.getServerIpAddress(serviceClient.getOptions().getTo().getAddress()));
        }
        

        if (reqMsgContext != null && reqMsgContext.getOptions() != null && reqMsgContext.getOptions().getAction() != null) {
            result.setRequestAction(reqMsgContext.getOptions().getAction());
        }
        if (rspMsgContext != null && rspMsgContext.getOptions() != null && rspMsgContext.getOptions().getAction() != null) {
            result.setResponseAction(rspMsgContext.getOptions().getAction());
        }
        if (reqMsgContext != null && reqMsgContext.getOperationContext() != null && reqMsgContext.getOperationContext().getServiceName() != null) {
            result.setServiceName(reqMsgContext.getOperationContext().getServiceName());
        }
        result.setServiceType(null);

        result.setTransactionCounter("");
        result.setTransactionPK(UUID.randomUUID().toString());


        return result;
    }

    /**
     * Extracts and assertion from a given message context
     * @param requestMessageContext
     * @return
     * @throws Exception 
     */
    private static Assertion getAssertion(MessageContext requestMessageContext) throws Exception {

        SOAPHeader soapHeader = requestMessageContext.getEnvelope().getHeader();
        Element soapHeaderElement = XMLUtils.toDOM(soapHeader);
        return Helper.getHCPAssertion(soapHeaderElement);
    }

    /**
     * Assertion utility method. Will extract information of a specific
     * assertion, based on a given expression.
     *
     * @param idAssertion the Identity Assertion
     * @param expression the expression to evaluate
     *
     * @return a string representing the information presented on the specified
     * node
     */
    private static String extractAssertionInfo(Assertion idAssertion, String expression) {
        for (AttributeStatement attributeStatement : idAssertion.getAttributeStatements()) {
            for (Attribute attribute : attributeStatement.getAttributes()) {
                if (attribute.getName().equals(expression)) {
                    return getAttributeValue(attribute);
                }
            }
        }
        return null;
    }

    /**
     * Extracts information from a given Assertion attribute.
     *
     * @param attribute the Assertion attribute
     *
     * @return a string containing the value of the attribute
     */
    private static String getAttributeValue(Attribute attribute) {
        String attributeValue = null;
        if (attribute.getAttributeValues().size() > 0) {
            attributeValue = attribute.getAttributeValues().get(0).getDOM().getTextContent();
        }
        return attributeValue;

    }

    /**
     * Utility method to convert a specific date to the RFC 2822 format.
     *
     * @param date the date object to be converted
     *
     * @return the RFC 2822 string representation of the date
     */
    private static String getDateAsRFC822String(Date date) {
        return RFC822DATEFORMAT.format(date);
    }

    /**
     * Extracts a CDA document from a RetrieveDocumentSetResponseType
     * 
     * @param retrieveDocumentSetResponseType
     * @return
     * @throws UnsupportedEncodingException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public static Document getCDA(ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType retrieveDocumentSetResponseType) throws UnsupportedEncodingException, ParserConfigurationException, SAXException, IOException {

        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse documentResponse;

        if (retrieveDocumentSetResponseType != null && retrieveDocumentSetResponseType.getDocumentResponse() != null && !retrieveDocumentSetResponseType.getDocumentResponse().isEmpty()) {
            documentResponse = retrieveDocumentSetResponseType.getDocumentResponse().get(0);

            byte[] documentData = documentResponse.getDocument();
            return convertToDomDocument(documentData);
        }
        return null;
    }

    /**
     * Converts a set of bytes into a Document
     * 
     * @param documentData
     * @return
     * @throws UnsupportedEncodingException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    private static Document convertToDomDocument(byte[] documentData) throws UnsupportedEncodingException, ParserConfigurationException, SAXException, IOException {
        Document xmlDocument = null;
        String xmlStr = new String(documentData, "UTF-8");
        xmlDocument = XMLUtil.parseContent(xmlStr);
        return xmlDocument;
    }
}
