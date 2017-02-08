/**
 * Copyright (c) 2009-2011 University of Cardiff and others
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * <p>
 * Contributors:
 * University of Cardiff - initial API and implementation
 * -
 */
package epsos.ccd.gnomon.auditmanager.ssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 *
 */
public class AuthSSLSocketFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(AuthSSLSocketFactory.class);

    private KeystoreDetails details = null;
    private KeystoreDetails truststore = null;

    private SSLContext sslcontext = null;
    private X509TrustManager defaultTrustManager = null;

    /**
     *
     * @param details
     * @param truststore
     * @param defaultTrustManager
     * @throws IOException
     */
    public AuthSSLSocketFactory(KeystoreDetails details, KeystoreDetails truststore, X509TrustManager defaultTrustManager) throws IOException {
        super();

        if (details != null) {
            this.details = details;
        }
        if (truststore != null) {
            this.truststore = truststore;
        }
        if (defaultTrustManager == null) {
            LOGGER.info(" using sun default trust manager");
            this.defaultTrustManager = KeystoreManager.getDefaultTrustManager();
        } else {
            this.defaultTrustManager = defaultTrustManager;
        }
    }

    /**
     *
     * @param details
     * @param truststore
     * @throws IOException
     */
    public AuthSSLSocketFactory(KeystoreDetails details, KeystoreDetails truststore) throws IOException {
        this(details, truststore, null);
    }

    /**
     *
     * @param details
     * @param defaultTrustManager
     * @throws IOException
     */
    public AuthSSLSocketFactory(KeystoreDetails details, X509TrustManager defaultTrustManager) throws IOException {
        this(details, null, defaultTrustManager);
    }

    /**
     *
     * @param details
     * @throws IOException
     */
    public AuthSSLSocketFactory(KeystoreDetails details) throws IOException {
        this(details, null, null);
    }

    /**
     *
     * @param details
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     */
    private static KeyStore createKeyStore(KeystoreDetails details)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

        if (details.getKeystoreLocation() == null) {
            throw new IllegalArgumentException("Keystore location may not be null");
        }

        LOGGER.info("Initializing key store");
        KeyStore keystore = KeyStore.getInstance(details.getKeystoreType());
        InputStream is = null;
        try {
            is = getKeystoreInputStream(details.getKeystoreLocation());
            if (is == null) {
                throw new IOException("Could not open stream to " + details.getKeystoreLocation());
            }
            String password = details.getKeystorePassword();
            keystore.load(is, password != null ? password.toCharArray() : null);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return keystore;
    }

    /**
     *
     * @param location
     * @return
     */
    private static InputStream getKeystoreInputStream(String location) {
        try {
            File file = new File(location);
            if (file.exists()) {
                return new FileInputStream(file);
            }
        } catch (Exception e) {

        }
        try {
            URL url = new URL(location);
            return url.openStream();
        } catch (Exception e) {

        }
        LOGGER.info("could not open stream to:" + location);
        return null;
    }

    /**
     *
     * @param keystore
     * @param details
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     */
    private KeyManager[] createKeyManagers(final KeyStore keystore, KeystoreDetails details)
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        LOGGER.info("Initializing key manager");
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(details.getAlgType());
        String password = details.getKeyPassword();
        kmfactory.init(keystore, (password == null || password.length() == 0) ? details.getKeystorePassword().toCharArray() : password.toCharArray());
        return kmfactory.getKeyManagers();

    }

    /**
     *
     * @param truststore
     * @param keystore
     * @param defaultTrustManager
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     */
    private TrustManager[] createTrustManagers(KeystoreDetails truststore, final KeyStore keystore, X509TrustManager defaultTrustManager)
            throws KeyStoreException, NoSuchAlgorithmException {

        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        TrustManagerFactory tmfactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());//TrustManagerFactory.getInstance(algorithm);
        tmfactory.init(keystore);
        TrustManager[] trustmanagers = tmfactory.getTrustManagers();
        for (int i = 0; i < trustmanagers.length; i++) {

            if (trustmanagers[i] instanceof X509TrustManager) {
                return new TrustManager[]{
                        new AuthSSLX509TrustManager((X509TrustManager) trustmanagers[i], defaultTrustManager, truststore.getAuthorizedDNs())};
            }
        }
        return trustmanagers;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private SSLContext createSSLContext() throws IOException {
        try {
            KeyManager[] keymanagers = null;
            TrustManager[] trustmanagers = null;
            if (this.details != null) {
                KeyStore keystore = createKeyStore(details);
                Enumeration aliases = keystore.aliases();
                while (aliases.hasMoreElements()) {
                    String alias = (String) aliases.nextElement();
                    Certificate[] certs = keystore.getCertificateChain(alias);
                    if (certs != null) {
                        LOGGER.debug("Certificate chain '" + alias + "':");
                        for (int c = 0; c < certs.length; c++) {
                            if (certs[c] instanceof X509Certificate) {
                                X509Certificate cert = (X509Certificate) certs[c];
                                LOGGER.debug(" Certificate " + (c + 1) + ":");
                                LOGGER.debug("  Subject DN: " + cert.getSubjectDN());
                                LOGGER.debug("  Signature Algorithm: " + cert.getSigAlgName());
                                LOGGER.debug("  Valid from: " + cert.getNotBefore());
                                LOGGER.debug("  Valid until: " + cert.getNotAfter());
                                LOGGER.debug("  Issuer: " + cert.getIssuerDN());
                            }
                        }
                    }
                }
                keymanagers = createKeyManagers(keystore, details);
            }
            if (this.truststore != null) {
                KeyStore keystore = createKeyStore(truststore);
                Enumeration aliases = keystore.aliases();
                while (aliases.hasMoreElements()) {
                    String alias = (String) aliases.nextElement();
                    LOGGER.debug("Trusted certificate '" + alias + "':");
                    Certificate trustedcert = keystore.getCertificate(alias);
                    if (trustedcert != null && trustedcert instanceof X509Certificate) {
                        X509Certificate cert = (X509Certificate) trustedcert;
                        LOGGER.debug("  Subject DN: " + cert.getSubjectDN());
                        LOGGER.debug("  Signature Algorithm: " + cert.getSigAlgName());
                        LOGGER.debug("  Valid from: " + cert.getNotBefore());
                        LOGGER.debug("  Valid until: " + cert.getNotAfter());
                        LOGGER.debug("  Issuer: " + cert.getIssuerDN());
                    }
                }
                trustmanagers = createTrustManagers(truststore, keystore, defaultTrustManager);
            }
            if (trustmanagers == null) {
                LOGGER.info(" created trustmanagers from the default...");
                trustmanagers = new TrustManager[]{defaultTrustManager};
            }

            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(keymanagers, trustmanagers, null);
            return sslcontext;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn(e.getMessage());
            throw new IOException("Unsupported algorithm exception: " + e.getMessage());
        } catch (KeyStoreException e) {
            LOGGER.warn(e.getMessage());
            throw new IOException("Keystore exception: " + e.getMessage());
        } catch (GeneralSecurityException e) {
            LOGGER.warn(e.getMessage());
            throw new IOException("Key management exception: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
            throw new IOException("I/O error reading keystore/truststore file: " + e.getMessage());
        }
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public SSLContext getSSLContext() throws IOException {
        if (this.sslcontext == null) {
            this.sslcontext = createSSLContext();
        }
        return this.sslcontext;
    }

    /**
     *
     * @param host
     * @param port
     * @return
     * @throws IOException
     */
    public Socket createSecureSocket(String host, int port) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    /**
     *
     * @param port
     * @param mutualAuth
     * @return
     * @throws IOException
     */
    public ServerSocket createServerSocket(int port, boolean mutualAuth) throws IOException {
        ServerSocket ss = getSSLContext().getServerSocketFactory().createServerSocket(port);
        if (mutualAuth) {
            ((SSLServerSocket) ss).setNeedClientAuth(true);
        }
        return ss;
    }

    /**
     *
     * @return
     */
    public boolean isSecured() {
        return true;
    }
}
