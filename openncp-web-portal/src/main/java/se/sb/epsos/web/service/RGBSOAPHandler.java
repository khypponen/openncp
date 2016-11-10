/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import se.sb.epsos.web.auth.AuthenticatedUser;

public class RGBSOAPHandler implements SOAPHandler<SOAPMessageContext> {
	public static final Logger LOGGER = LoggerFactory.getLogger(RGBSOAPHandler.class);
	private static final String SECURITY_NAMESPACE_URL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final String SECURITY_NAMESPACE_PREFIX = "wsse";
	private static final String SECURITY_NODENAME = "Security";
	private AssertionHandler assertionHandler = new AssertionHandler();
	private AuthenticatedUser user;
	
	public RGBSOAPHandler(AuthenticatedUser user) {
		this.user = user;
	}

	@Override
	public void close(MessageContext context) {
		context.clear();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		 return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty.booleanValue()) {
            SOAPMessage message = context.getMessage();
            try {
            	SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            	SOAPHeader header = envelope.addHeader();
            	Assertion assertion = user.getAssertion();
        	    
            	// If the user has not received an assertion yet, generate it
            	if(assertion == null) {
	        	    assertion = assertionHandler.createSAMLAssertion(user);
	        		assertionHandler.signSAMLAssertion(assertion);
	        		user.setAssertion(assertion);
            	}
        		AssertionMarshaller marshaller = new AssertionMarshaller();
        		
        		SOAPElement security = header.addChildElement(SECURITY_NODENAME, SECURITY_NAMESPACE_PREFIX, SECURITY_NAMESPACE_URL);
        		
	        	SOAPFactory soapFactory = SOAPFactory.newInstance();
	        	
	        	if(user.getTrcAssertion() != null) {
		        	Element trcAssertionElement = marshaller.marshall(user.getTrcAssertion());
		        	SOAPElement TrcAssertionSOAP = soapFactory.createElement(trcAssertionElement);
		        	security.addChildElement(TrcAssertionSOAP);
	        	}

	        	Element element = marshaller.marshall(assertion);
	        	SOAPElement assertionSOAP = soapFactory.createElement(element);
	        	security.addChildElement(assertionSOAP);	        	
	        	
	        	LOGGER.debug("Soap message: " + getXmlFromSOAPMessage(message));
            } catch (Exception e) {
				LOGGER.error("Failed to handling assertion", e);
			}
        } 
        return true;
	}

	@Override
	public Set<QName> getHeaders() {
		return new TreeSet<QName>();
	}
	
	/**
     * Convert any SOAP object that implements SOAPMessage into a String
     *
     * @param msg SOAP object
     * @return String
     * @throws SOAPException
     * @throws IOException
     */
    private static String getXmlFromSOAPMessage(SOAPMessage msg) throws SOAPException, IOException {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        msg.writeTo(byteArrayOS);
        return new String(byteArrayOS.toByteArray());
    }
}
