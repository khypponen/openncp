/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2015  GNOMON Informatics)
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
 * Contact email: k.karkaletsis@gnomon.com.gr
 */
package eu.epsos.util;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.DefaultKeyStoreManager;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import org.apache.log4j.Logger;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.OidUtil;
import tr.com.srdc.epsos.util.http.HTTPUtil;

/**
 *
 * @author karkaletsis
 */
public class CertificateUtils {

    public static Logger logger = Logger.getLogger(CertificateUtils.class);

    public static String getServerCertificate(String country) {
        String oid = OidUtil.getHomeCommunityId(country);
        String endpoint = ConfigurationManagerService.getInstance().getProperty(country + ".PatientIdentificationService.WSE");
        logger.info("OID IS " + oid + " for country " + country);
        logger.info("ENDPOINT " + endpoint);
        return HTTPUtil.getServerCertificate(endpoint);
    }

    public static X509Certificate getClientCertificate() {
        String KEYSTORE_LOCATION = Constants.NCP_SIG_KEYSTORE_PATH;
        String KEY_STORE_PASS = Constants.NCP_SIG_KEYSTORE_PASSWORD;
        String KEY_ALIAS = Constants.NCP_SIG_PRIVATEKEY_ALIAS;
        String PRIVATE_KEY_PASS = Constants.NCP_SIG_PRIVATEKEY_PASSWORD;
        X509Certificate cert = null;

        try {
            KeyStoreManager keyManager = new DefaultKeyStoreManager();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            File file = new File(KEYSTORE_LOCATION);
            keyStore.load(new FileInputStream(file),
                    KEY_STORE_PASS.toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS,
                    PRIVATE_KEY_PASS.toCharArray());
            cert = (X509Certificate) keyManager.getCertificate(KEY_ALIAS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cert;
    }

}
