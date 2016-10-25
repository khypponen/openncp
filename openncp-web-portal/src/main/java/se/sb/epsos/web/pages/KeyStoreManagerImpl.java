/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/
package se.sb.epsos.web.pages;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.service.NcpServiceConfigManager;

public final class KeyStoreManagerImpl implements KeyStoreManager {
	protected static final Logger LOGGER = LoggerFactory.getLogger(KeyStoreManagerImpl.class);
	private static final String ASSERTION = "assertion";
	private KeyStore keyStore;
	private KeyStore trustStore;

	public KeyStoreManagerImpl() throws KeyStoreInitializationException {
		keyStore = getKeyStore();
		trustStore = getTrustStore();
	}

	private KeyStore getKeyStore() throws KeyStoreInitializationException {
		try {
			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream(NcpServiceConfigManager.getPrivateKeystoreLocation(ASSERTION));
			if(keystoreStream == null) {
				keystoreStream = new FileInputStream(NcpServiceConfigManager.getPrivateKeystoreLocation(ASSERTION));
			}

			keyStore.load(keystoreStream, NcpServiceConfigManager.getPrivateKeystorePassword(ASSERTION).toCharArray());
			LOGGER.debug("keystore loaded");
		} catch (Exception e) {
			throw new KeyStoreInitializationException("Failed to load keystore " + NcpServiceConfigManager.getPrivateKeystoreLocation(ASSERTION), e);
		}
		return keyStore;
	}

	private KeyStore getTrustStore() throws KeyStoreInitializationException {
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream truststoreStream = getClass().getClassLoader().getResourceAsStream(NcpServiceConfigManager.getTruststoreLocation(ASSERTION));
			if(truststoreStream == null) {
				truststoreStream = new FileInputStream(NcpServiceConfigManager.getTruststoreLocation(ASSERTION));
			}
			trustStore.load(truststoreStream, NcpServiceConfigManager.getTruststorePassword(ASSERTION).toCharArray());
			LOGGER.debug("truststore loaded");
		} catch (Exception e) {
			throw new KeyStoreInitializationException("Failed to load keystore " + NcpServiceConfigManager.getTruststoreLocation(ASSERTION), e);
		}
		return trustStore;
	}

	public KeyPair getPrivateKey() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
		Key key = keyStore.getKey(NcpServiceConfigManager.getPrivateKeyAlias(ASSERTION), NcpServiceConfigManager.getPrivateKeystorePassword(ASSERTION).toCharArray());
		if (key instanceof PrivateKey) {
			Certificate cert = keyStore.getCertificate(NcpServiceConfigManager.getPrivateKeyAlias(ASSERTION));
			PublicKey publicKey = cert.getPublicKey();
			return new KeyPair(publicKey, (PrivateKey) key);
		}
		return null;
	}

	public X509Certificate getCertificate() throws KeyStoreException {
		return (X509Certificate)keyStore.getCertificate(NcpServiceConfigManager.getPrivateKeyAlias(ASSERTION));
	}
}
