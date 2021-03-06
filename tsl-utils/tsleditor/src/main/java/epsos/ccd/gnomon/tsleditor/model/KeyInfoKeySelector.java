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

import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KeyInfoKeySelector extends KeySelector implements KeySelectorResult {

	private static final Log LOG = LogFactory.getLog(KeyInfoKeySelector.class);

	private X509Certificate certificate;

	@Override
	public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method, XMLCryptoContext context)
			throws KeySelectorException {
		LOG.debug("select key");
		List<XMLStructure> keyInfoContent = keyInfo.getContent();
		for (XMLStructure keyInfoStructure : keyInfoContent) {
			if (false == (keyInfoStructure instanceof X509Data)) {
				continue;
			}
			X509Data x509Data = (X509Data) keyInfoStructure;
			List<Object> x509DataList = x509Data.getContent();
			for (Object x509DataObject : x509DataList) {
				if (false == (x509DataObject instanceof X509Certificate)) {
					continue;
				}
				this.certificate = (X509Certificate) x509DataObject;
				// stop after first match
				return this;
			}
		}
		throw new KeySelectorException("No key found!");
	}

	@Override
	public Key getKey() {
		return this.certificate.getPublicKey();
	}

	public X509Certificate getCertificate() {
		return this.certificate;
	}
}
