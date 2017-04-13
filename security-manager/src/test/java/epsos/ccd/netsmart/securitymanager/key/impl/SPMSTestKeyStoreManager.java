/*
 *  Copyright 2010 jerouris.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package epsos.ccd.netsmart.securitymanager.key.impl;

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * @author jerouris
 */
public class SPMSTestKeyStoreManager implements KeyStoreManager {

    private static final Logger logger = LoggerFactory.getLogger(GlassfishTestKeyStoreManager.class);

    private static final String TEST_KEYSTORE_LOCATION = "keystores/spms/pt-server-keystore.jks";
    private static final String TEST_TRUSTSTORE_LOCATION = "keystores/spms/pt-server-truststore.jks";
    private static final String TEST_KEYSTORE_PASSWORD = "changeit";

    private static final String TEST_PRIVATEKEY_ALIAS = "ppt.ncp-signature.epsos.spms.pt";
    private static final String TEST_PRIVATEKEY_PASSWORD = "changeit";

    private KeyStore keyStore;
    private KeyStore trustStore;

    public SPMSTestKeyStoreManager() {
        // For testing purposes...
        if (keyStore == null) {
            keyStore = getTestKeyStore();
            trustStore = getTestTrustStore();
        }
    }

    @Override
    public KeyPair getPrivateKey(String alias, char[] password) {

        try {

            // Get private key
            Key key = keyStore.getKey(alias, password);
            if (key instanceof PrivateKey) {
                // Get certificate of public key
                java.security.cert.Certificate cert = keyStore
                        .getCertificate(alias);

                // Get public key
                PublicKey publicKey = cert.getPublicKey();

                // Return a key pair
                return new KeyPair(publicKey, (PrivateKey) key);
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            logger.error(null, e);
        }
        return null;
    }

    private KeyStore getTestKeyStore() {
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keystoreStream = ClassLoader
                    .getSystemResourceAsStream(TEST_KEYSTORE_LOCATION);
            keyStore.load(keystoreStream, TEST_KEYSTORE_PASSWORD.toCharArray());

            return keyStore;

        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
            logger.error(null, ex);
        }
        return null;
    }

    @Override
    public KeyStore getKeyStore() {
        return keyStore;
    }

    @Override
    public KeyStore getTrustStore() {
        return trustStore;

    }

    private KeyStore getTestTrustStore() {
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keystoreStream = ClassLoader
                    .getSystemResourceAsStream(TEST_TRUSTSTORE_LOCATION);
            trustStore.load(keystoreStream,
                    TEST_KEYSTORE_PASSWORD.toCharArray());

            return trustStore;

        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
            logger.error(null, ex);
        }
        return null;

    }

    @Override
    public Certificate getCertificate(String alias) {
        try {
            return keyStore
                    .getCertificate(alias);
        } catch (KeyStoreException ex) {
            logger.error(null,
                    ex);
        }
        return null;
    }

    @Override
    public KeyPair getDefaultPrivateKey() throws SMgrException {
        return getPrivateKey(TEST_PRIVATEKEY_ALIAS,
                TEST_PRIVATEKEY_PASSWORD.toCharArray());
    }

    @Override
    public Certificate getDefaultCertificate() throws SMgrException {
        return getCertificate(TEST_PRIVATEKEY_ALIAS);
    }
}
