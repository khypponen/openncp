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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * @author jerouris
 */
public class GlassfishTestKeyStoreManager implements KeyStoreManager {

    private static final Logger logger = LoggerFactory.getLogger(GlassfishTestKeyStoreManager.class);

    private static final String TEST_KEYSTORE_LOCATION = "/home/jerouris/bin/glassfish-3.0.1/glassfish/domains/domain1/config/keystore.jks";
    private static final String TEST_TRUSTSTORE_LOCATION = "/home/jerouris/bin/glassfish-3.0.1/glassfish/domains/domain1/config/cacerts.jks";
    private static final String TEST_KEYSTORE_PASSWORD = "changeit";
    private static final String TEST_TRUSTSTORE_PASSWORD = "changeit";

    private static final String TEST_PRIVATEKEY_ALIAS = "xws-security-server";
    private static final String TEST_PRIVATEKEY_PASSWORD = "changeit";

    private KeyStore keyStore;
    private KeyStore trustStore;

    public GlassfishTestKeyStoreManager() {
        // For testing purposes...
        if (keyStore == null) {
            keyStore = getTestKeyStore();
            trustStore = getTestTrustStore();
        }
    }

    public KeyPair getPrivateKey(String alias, char[] password) throws SMgrException {

        try {
            // Get private key
            Key key = keyStore.getKey(alias, password);
            if (key instanceof PrivateKey) {
                // Get certificate of public key
                java.security.cert.Certificate cert = keyStore.getCertificate(alias);

                // Get public key
                PublicKey publicKey = cert.getPublicKey();

                // Return a key pair
                return new KeyPair(publicKey, (PrivateKey) key);
            }
        } catch (UnrecoverableKeyException e) {
            logger.error(null, e);
            throw new SMgrException("Key with alias:" + alias + " is unrecoverable", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error(null, e);
            throw new SMgrException("Key with alias:" + alias + " uses an incompatible algorithm", e);
        } catch (KeyStoreException e) {
            logger.error(null, e);
            throw new SMgrException("Key with alias:" + alias + " not found", e);
        }
        return null;
    }

    private KeyStore getTestKeyStore() {
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keystoreStream = new FileInputStream(TEST_KEYSTORE_LOCATION);
            keyStore.load(keystoreStream, TEST_KEYSTORE_PASSWORD.toCharArray());

            return keyStore;

        } catch (IOException ex) {
            logger.error(null, ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(null, ex);
        } catch (CertificateException ex) {
            logger.error(null, ex);
        } catch (KeyStoreException ex) {
            logger.error(null, ex);
        }
        return null;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public KeyStore getTrustStore() {
        return trustStore;
    }

    public Certificate getCertificate(String alias) throws SMgrException {
        try {
            java.security.cert.Certificate cert = keyStore.getCertificate(alias);
            return cert;
        } catch (KeyStoreException ex) {
            logger.error(null, ex);
            throw new SMgrException("Certificate with alias:" + alias + " not found in keystore", ex);
        }
    }

    private KeyStore getTestTrustStore() {
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keystoreStream = new FileInputStream(TEST_TRUSTSTORE_LOCATION);
            trustStore.load(keystoreStream, TEST_TRUSTSTORE_PASSWORD.toCharArray());
            return trustStore;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(GlassfishTestKeyStoreManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(GlassfishTestKeyStoreManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            java.util.logging.Logger.getLogger(GlassfishTestKeyStoreManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            java.util.logging.Logger.getLogger(GlassfishTestKeyStoreManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return null;
    }

    public KeyPair getDefaultPrivateKey() throws SMgrException {
        return getPrivateKey(TEST_PRIVATEKEY_ALIAS, TEST_PRIVATEKEY_PASSWORD.toCharArray());
    }

    public Certificate getDefaultCertificate() throws SMgrException {
        return getCertificate(TEST_PRIVATEKEY_ALIAS);
    }
}
