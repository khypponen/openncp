/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.util.http;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.util.proxy.CustomProxySelector;
import eu.epsos.util.proxy.ProxyCredentials;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import tr.com.srdc.epsos.util.Constants;

public class HTTPUtil {

    public static Logger logger = Logger.getLogger(HTTPUtil.class);

    public static String getClientCertificate(HttpServletRequest req) {
        String result;
        X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");

        if (certs != null) {
            //			for (int i = 0; i < certs.length; i++) {
            //				logger.info("Client Certificate [" + i + "] = " + certs[i].getSubjectDN().getName());
            //			}
            result = certs[0].getSubjectDN().getName();
        } else {
            if ("https".equals(req.getScheme())) {
                logger.warn("This was an HTTPS request, " + "but no client certificate is available");
            } else {
                logger.warn("This was not an HTTPS request, " + "so no client certificate is available");
            }
            result = "Warning!: No Client certificate found!";
        }

        return result;
    }

    public static String getServerCertificate(String endpoint) {
        logger.info("Trying to find certificate from : " + endpoint);
        String result = "";
        HttpsURLConnection con = null;
        try {
            if (!endpoint.startsWith("https")) {
                result = "Warning!: No Server certificate found!";
            } else {
                SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

                URL url;
                url = new URL(endpoint);
                con = (HttpsURLConnection) url.openConnection();
                con.setSSLSocketFactory(sslsocketfactory);
                con.connect();
                Certificate certs[] = con.getServerCertificates();

                // Get the first certificate
                //
                if (certs == null) {
                    result = "Warning!: No Server certificate found!";
                } else {
                    X509Certificate cert = (X509Certificate) certs[0];
                    result = cert.getSubjectDN().getName();
                }
            }
        } catch (MalformedURLException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return result;

    }

    public static String getSubjectDN(boolean isProvider) {
        FileInputStream is;
        Certificate cert;
        try {
            if (isProvider) {
                is = new FileInputStream(Constants.SP_KEYSTORE_PATH);
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(is, Constants.SP_KEYSTORE_PASSWORD.toCharArray());
                cert = keystore.getCertificate(Constants.SP_PRIVATEKEY_ALIAS);
            } else {
                is = new FileInputStream(Constants.SC_KEYSTORE_PATH);
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(is, Constants.SC_KEYSTORE_PASSWORD.toCharArray());
                cert = keystore.getCertificate(Constants.SC_PRIVATEKEY_ALIAS);
            }
            if (cert instanceof X509Certificate) {
                X509Certificate x509cert = (X509Certificate) cert;

                // Get subject
                Principal principal = x509cert.getSubjectDN();
                String subjectDn = principal.getName();

                return subjectDn;

            }
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } catch (KeyStoreException e) {
            logger.error("", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
        } catch (CertificateException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        }
        return "";
    }

    public static boolean isProxyAnthenticationMandatory() {

        ConfigurationManagerService configService = ConfigurationManagerService
                .getInstance();

        return Boolean.parseBoolean(configService
                .getProperty("APP_BEHIND_PROXY"));
    }

    public static ProxyCredentials getProxyCredentials() {
        ProxyCredentials credentials = new ProxyCredentials();
        ConfigurationManagerService configService = ConfigurationManagerService.getInstance();
        credentials.setProxyAuthenticated(Boolean.parseBoolean(configService
                .getProperty("APP_BEHIND_PROXY")));
        credentials.setHostname(configService.getProperty("APP_PROXY_HOST"));
        credentials.setPassword(configService.getProperty("APP_PROXY_PASSWORD"));
        credentials.setPort(configService.getProperty("APP_PROXY_PORT"));
        credentials.setUsername(configService.getProperty("APP_PROXY_USERNAME"));
        return credentials;
    }

    public CustomProxySelector setCustomProxyServerForURLConnection() {
        CustomProxySelector ps = null;
        if (isProxyAnthenticationMandatory()) {
            ProxyCredentials proxyCredentials = getProxyCredentials();
            ps = new CustomProxySelector(ProxySelector.getDefault(), proxyCredentials);
            return ps;
        }
        return null;
    }

}
