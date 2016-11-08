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

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.shelob.ws.client.jaxws.ClientConnectorServiceService;

public class NcpClientConnector {
	public static final Logger LOGGER = LoggerFactory.getLogger(NcpClientConnector.class);
	private static ClientConnectorServiceService service;

	public static synchronized ClientConnectorServiceService createClientConnector() {
		String namespaceUrl = "http://cc.pt.epsos.eu";
		String clientConnectorWsdlUrl = System.getProperty("client-connector-wsdl-url");
		LOGGER.debug("client-connector-wsdl-url: " + clientConnectorWsdlUrl);
		if (clientConnectorWsdlUrl == null) {
			throw new IllegalArgumentException("client-connector-wsdl-url is not set");
		}
		try {
			service = new ClientConnectorServiceService(new URL(clientConnectorWsdlUrl), new QName(namespaceUrl, "ClientConnectorService"));
		} catch (MalformedURLException ex) {
			throw new RuntimeException("System property 'client-connector-wsdl-url' is malformed: " + clientConnectorWsdlUrl);
		}
		return service;
	}
}
