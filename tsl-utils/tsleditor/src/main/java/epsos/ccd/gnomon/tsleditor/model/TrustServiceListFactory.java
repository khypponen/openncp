/***Licensed to the Apache Software Foundation (ASF) under one
*or more contributor license agreements.  See the NOTICE file
*distributed with this work for additional information
*regarding copyright ownership.  The ASF licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
**/
package epsos.ccd.gnomon.tsleditor.model;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etsi.uri._02231.v2.ObjectFactory;
import org.etsi.uri._02231.v2.TrustStatusListType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TrustServiceListFactory {

	private static final Log LOG = LogFactory.getLog(TrustServiceListFactory.class);

	private TrustServiceListFactory() {
		super();
	}

	/**
	 * Creates a new trust service list from the given file.
	 *
	 * @param tslFile
	 * @return
	 * @throws IOException
	 */
	public static TrustServiceList newInstance(File tslFile) throws IOException {
		if (null == tslFile) {
			throw new IllegalArgumentException();
		}
		Document tslDocument;
		try {
			tslDocument = parseDocument(tslFile);
		} catch (Exception e) {
			throw new IOException("DOM parse error: " + e.getMessage(), e);
		}
		TrustServiceList trustServiceList = newInstance(tslDocument, tslFile);
		return trustServiceList;
	}

	/**
	 * Creates a trust service list from a given DOM document.
	 *
	 * @param tslDocument
	 *            the DOM TSL document.
	 *
	 * @return
	 * @throws IOException
	 */
	public static TrustServiceList newInstance(Document tslDocument, File tslFile) throws IOException {
		if (null == tslDocument) {
			throw new IllegalArgumentException();
		}
		TrustStatusListType trustServiceStatusList;
		try {
			trustServiceStatusList = parseTslDocument(tslDocument);
		} catch (JAXBException e) {
			throw new IOException("TSL parse error: " + e.getMessage(), e);
		}
		return new TrustServiceList(trustServiceStatusList, tslDocument, tslFile);
	}

	public static TrustServiceList newInstance(Document tslDocument) throws IOException {
		return newInstance(tslDocument, null);
	}

	private static Document parseDocument(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		return document;
	}

	private static TrustStatusListType parseTslDocument(Document tslDocument) throws JAXBException {
		Unmarshaller unmarshaller = getUnmarshaller();
		JAXBElement<TrustStatusListType> jaxbElement = (JAXBElement<TrustStatusListType>) unmarshaller
				.unmarshal(tslDocument);
		TrustStatusListType trustServiceStatusList = jaxbElement.getValue();
		return trustServiceStatusList;
	}

	private static Unmarshaller getUnmarshaller() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return unmarshaller;
	}

	/**
	 * Creates a new empty trust service list.
	 *
	 * @return
	 */
	public static TrustServiceList newInstance() {
		return new TrustServiceList();
	}

	public static TrustServiceProvider createTrustServiceProvider(String name, String tradeName) {
		TrustServiceProvider trustServiceProvider = new TrustServiceProvider(name, tradeName);
		return trustServiceProvider;
	}

	public static TrustService createTrustService(X509Certificate certificate) {
		TrustService trustService = new TrustService(certificate);
		return trustService;
	}

	public static TrustService createTrustService(X509Certificate certificate, String... oids) {
		TrustService trustService = new TrustService(certificate, oids);
		return trustService;
	}
}
