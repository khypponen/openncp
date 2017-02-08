/*
 *  Copyright 2010 Jerry Dimitriou <jerouris at netsmart.gr>.
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
package epsos.ccd.netsmart.securitymanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertStore;
import java.security.cert.CertStoreParameters;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Certificate Validator is a component that is responsible for validating
 * certificates against the NCP Trust Store. The certificate validation consists
 * of checking if the certificate is trusted and if it is not revoked: All
 * certificates registered with the local NCP trust store are assumed as
 * trusted. For revocation check both CRL retrieval and OCSP are supported.
 *
 * @author Jerry Dimitriou <jerouris at netsmart.gr>
 */
public final class CertificateValidator extends KeySelector {

    private static final Logger logger = LoggerFactory.getLogger(CertificateValidator.class);

    private Certificate cert = null;
    private final KeyStore trustStore;
    private boolean isRevocationEnabled = false;
    public static final String CRLDP_OID = "2.5.29.31";
    public static final String AIA_OID = "1.3.6.1.5.5.7.1.1";
    public static final String PROP_CHECK_FOR_KEYUSAGE = "secman.cert.validator.checkforkeyusage";

    /**
     *
     */
    public boolean CHECK_FOR_KEYUSAGE;

    public CertificateValidator(KeyStore _trustStore) throws FileNotFoundException, IOException {
        CHECK_FOR_KEYUSAGE = false;
        trustStore = _trustStore;
        ConfigurationManagerService cm = ConfigurationManagerService.getInstance();

        if (cm.getProperty(PROP_CHECK_FOR_KEYUSAGE).trim().equalsIgnoreCase("true")) {
            CHECK_FOR_KEYUSAGE = true;
        }

    }

    /**
     * This method verifies the validity of a certificate by a given keyInfo
     * structure
     *
     * @param keyInfo The keyInfo structure that contains the certificate
     * (X509).
     * @throws SMgrException when the validation of the certificate fails
     */
    public void validateCertificate(KeyInfo keyInfo) throws SMgrException {

        for (Iterator<?> it = keyInfo.getContent().iterator(); it.hasNext();) {
            Object object = it.next();

            if (object instanceof X509Data) {
                X509Data data = (X509Data) object;
                Iterator<?> xi = data.getContent().iterator();
                while (xi.hasNext()) {
                    Object o = xi.next();
                    // check X509Certificate
                    if (o instanceof X509Certificate) {
                        X509Certificate xcert = (X509Certificate) o;
                        validateCertificate(xcert);
                        cert = xcert;
                        return;
                    } else {
                        // skip all other entries
                        continue;
                    }
                }

                throw new SMgrException("The KeyInfo Structure does not contain X509Data element: No Certificate Present");
            }
        }
    }

    public void validateCertificate(org.opensaml.xml.signature.KeyInfo keyInfo) throws SMgrException {

        for (Iterator<?> it = keyInfo.getX509Datas().iterator(); it.hasNext();) {
            Object object = it.next();

            if (object instanceof org.opensaml.xml.signature.X509Data) {
                org.opensaml.xml.signature.X509Data data = (org.opensaml.xml.signature.X509Data) object;
                Iterator<?> xi = data.getX509Certificates().iterator();
                while (xi.hasNext()) {
                    Object o = xi.next();
                    // check X509Certificate
                    if (o instanceof org.opensaml.xml.signature.X509Certificate) {
                        X509Certificate xcert = (X509Certificate) o;
                        validateCertificate(xcert);
                        cert = xcert;
                        return;
                    } else {
                        // skip all other entries
                        continue;
                    }
                }

                throw new SMgrException("The KeyInfo Structure does not contain X509Data element: No Certificate Present");
            }
        }
    }

    /**
     * This method verifies the validity of the given X509 certificate by
     * checking the truststore.
     *
     * @param cert the certificate that will be validated
     * @throws SMgrException when the validation of the certificate fails
     */
    public void validateCertificate(X509Certificate cert) throws SMgrException {
        try {
            if (CHECK_FOR_KEYUSAGE) {
                logger.info("Key usage available in conf manager");
                boolean[] keyUsage = cert.getKeyUsage();

                if (keyUsage == null || keyUsage[0] == false) {
                    throw new SMgrException("Certificate Key Usage Extension for Digital Signature Missing");
                }
            }

            try {
                cert.checkValidity(new Date());
            } catch (CertificateExpiredException ex) {
                logger.error( null, ex);
                throw new SMgrException("Certificate Expired", ex);
            } catch (CertificateNotYetValidException ex) {
                logger.error( null, ex);
                throw new SMgrException("Certificate Not Valid Yet", ex);
            }

            // Check if the cert supports the CRLDP
            if (cert.getExtensionValue(AIA_OID) != null) {
                setRevocationEnabled(true);
                setOCSPEnabled(true);
                logger.info("Found AIA Extension. Using OCSP");
            }

            if (cert.getExtensionValue(CRLDP_OID) != null) {
                setRevocationEnabled(true);
                setCRLDPEnabled(true);
                logger.info("Found CRLDP Extension. Using CRLDP");
            }

            CertStoreParameters intermediates
                    = new CollectionCertStoreParameters(Collections.singletonList(cert));

            X509CertSelector target = new X509CertSelector();
            target.setCertificate(cert);

            PKIXBuilderParameters builderParams = new PKIXBuilderParameters(trustStore, target);

            builderParams.addCertStore(CertStore.getInstance("Collection", intermediates));

            builderParams.setRevocationEnabled(isRevocationEnabled);
            builderParams.setDate(new Date());

            //CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");
            //CertPathBuilderResult build = builder.build(builderParams);
            //CertPath cp = build.getCertPath();

            //CertPathValidator cpv = CertPathValidator.getInstance("PKIX");

            PKIXParameters params = new PKIXParameters(trustStore);
            params.setRevocationEnabled(isRevocationEnabled);

            //PKIXCertPathValidatorResult validationResult = (PKIXCertPathValidatorResult) cpv.validate(cp, params);

        } catch (NoSuchAlgorithmException ex) {

            logger.error( null, ex);
            throw new SMgrException("Certificate's Public key algorithm is unknown", ex);

        } catch (KeyStoreException ex) {
            logger.error( null, ex);
            throw new SMgrException("Error when tried to use the TrustStore", ex);
        } catch (InvalidAlgorithmParameterException ex) {
            logger.error( null, ex);
            throw new SMgrException("Invalid Algorith parameters for building Certificate Path", ex);
        }

    }

    public KeySelectorResult select(KeyInfo keyInfo,
            KeySelector.Purpose purpose,
            AlgorithmMethod method,
            XMLCryptoContext context)
            throws KeySelectorException {
        Iterator<?> ki = keyInfo.getContent().iterator();
        while (ki.hasNext()) {
            XMLStructure info = (XMLStructure) ki.next();
            if (!(info instanceof X509Data)) {
                continue;
            }
            X509Data x509Data = (X509Data) info;
            Iterator<?> xi = x509Data.getContent().iterator();
            while (xi.hasNext()) {
                Object o = xi.next();
                if (!(o instanceof X509Certificate)) {
                    continue;
                }
                try {
                    validateCertificate(keyInfo);
                } catch (SMgrException ex) {
                    logger.error( null, ex);
                    throw new KeySelectorException("Validation Failed: " + ex.getMessage());
                }
                final PublicKey key = ((X509Certificate) o).getPublicKey();
                // Make sure the algorithm is compatible
                // with the method.
                if (algEquals(method.getAlgorithm(), key.getAlgorithm())) {
                    return new KeySelectorResult() {
                        public Key getKey() {
                            return key;
                        }
                    };
                }
            }
        }
        throw new KeySelectorException("No key found!");
    }

    static boolean algEquals(String algURI, String algName) {
        if ((algName.equalsIgnoreCase("DSA")
                && algURI.contains("#dsa"))
                || (algName.equalsIgnoreCase("RSA")
                && algURI.contains("#rsa"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the X509 Certificate that is validated from the #validate
     * function
     */
    public Certificate getValidatedCertificate() {
        return cert;
    }

    public void setOCSPEnabled(boolean flag) {
        java.security.Security.setProperty("ocsp.enable", Boolean.toString(flag));
    }

    public void setCRLDPEnabled(boolean flag) {
        System.setProperty("com.sun.security.enableCRLDP", Boolean.toString(flag));
    }

    public void setRevocationEnabled(boolean flag) {
        this.isRevocationEnabled = flag;
    }
}
