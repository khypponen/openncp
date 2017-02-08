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

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.DefaultKeyStoreManager;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.security.auth.x500.X500Principal;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;

import org.opensaml.common.SignableSAMLObject;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * The NCP Signature Manager is a JAVA library for applying and verifying
 * detached digital signatures on XML documents and for applying and verifying
 * enveloped signatures on SAML assertions and Audit Trail Messages
 *
 * @author Jerry Dimitriou <jerouris at netsmart.gr>
 */
public class SignatureManager {

    private static final Logger logger = LoggerFactory.getLogger(SignatureManager.class);

    private KeyStoreManager keyManager;
    private String signatureAlgorithm;
    private static final String SIG_ALG_PROP = "secman.signature.algorithm.default";
    private static final String DGST_ALG_PROP = "secman.digest.algorithm.default";
    private String digestAlgorithm;

    public SignatureManager() throws IOException {

        //Default constructor now defaults to the test keyStoreManager
        //TODO: Create a keystore manager to handle official Keystores;
        keyManager = new DefaultKeyStoreManager();
        init();

    }

    public SignatureManager(KeyStoreManager keyStoreManager) throws IOException {

        //Constructor for unit testing. Sort of DI
        this.keyManager = keyStoreManager;
        init();

    }

    private void init() throws IOException {
        ConfigurationManagerService cm = ConfigurationManagerService.getInstance();
        signatureAlgorithm = cm.getProperty(SIG_ALG_PROP);

        // If not defined
        if (signatureAlgorithm.length() == 0) {
            signatureAlgorithm = SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256;
            //cm.updateProperty(SIG_ALG_PROP, signatureAlgorithm);
        }

        digestAlgorithm = cm.getProperty(DGST_ALG_PROP);

        // If not defined
        if (digestAlgorithm.length() == 0) {
            digestAlgorithm = SignatureConstants.ALGO_ID_DIGEST_SHA1;
            //cm.updateProperty(DGST_ALG_PROP, digestAlgorithm);
        }

    }

    /**
     * Verifies the enveloped SAML signature and checks that the Assertion is
     * signed against that signature. This method returns nothing when the
     * signature is valid. When the signature is not valid though, it throws an
     * SMgrException with the Error Message that caused the signature to fail
     * validation.
     *
     * @param sa The SAML Assertion that will be validated by the method.
     * @throws SMgrException When the validation of the signature fails
     */
    public String verifySAMLAssestion(Assertion sa) throws SMgrException, IOException {

        String sigCountryCode = null;

        try {
            SAMLSignatureProfileValidator profileValidator = new SAMLSignatureProfileValidator();
            Signature sig = sa.getSignature();
            try {
                profileValidator.validate(sig);
            } catch (ValidationException e) {
                // Indicates signature did not conform to SAML Signature profile
                logger.error(e.getMessage());
                throw new SMgrException("SAML Signature Profile Validation: " + e.getMessage());
            }

            X509Certificate cert = null;
            List<X509Certificate> certificates = KeyInfoHelper.getCertificates(sig.getKeyInfo());
            if (certificates.size() == 1) {
                cert = certificates.get(0);
                // Mustafa: When not called through https, we can use the country code of the signature cert
                String DN = cert.getSubjectDN().getName();
                sigCountryCode = DN.substring(DN.indexOf("C=") + 2, DN.indexOf("C=") + 4);

            } else {
                throw new SMgrException("More than one certificate found in keyinfo");
            }

            BasicX509Credential verificationCredential = new BasicX509Credential();
            verificationCredential.setEntityCertificate(cert);
            SignatureValidator sigValidator = new SignatureValidator(verificationCredential);
            try {
                sigValidator.validate(sig);
            } catch (ValidationException e) {
                // Indicates signature was not cryptographically valid, or possibly a processing error
                logger.error(e.getMessage());
                throw new SMgrException("Signature Validation: " + e.getMessage());
            }
            CertificateValidator cv = new CertificateValidator(keyManager.getTrustStore());
            cv.validateCertificate(cert);
        } catch (CertificateException ex) {
            logger.error(null, ex);
        }

        return sigCountryCode;
    }

    /**
     * Verifies the enveloped XML signature and checks that the XML Document is
     * signed against that signature. This method returns nothing when the
     * signature is valid. When the signature is not valid though, it throws an
     * SMgrException with the Error Message that caused the signature to fail
     * validation.
     *
     * @param doc The XML Document that will be validated.
     * @throws SMgrException When the validation of the signature fails
     * @throws java.io.IOException
     */
    public void verifyEnvelopedSignature(Document doc) throws SMgrException, IOException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

            NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");

            if (nl.getLength() == 0) {
                throw new SMgrException("Cannot find Signature element");
            } // and document context.

            CertificateValidator cv = new CertificateValidator(keyManager.getTrustStore());

            DOMValidateContext valContext = new DOMValidateContext(cv, nl.item(0));

            // Unmarshal the XMLSignature.
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);
            // Validate the XMLSignature.

            boolean coreValidity = signature.validate(valContext);

            if (coreValidity == false) {
                throw new SMgrException("Invalid Signature: Mathematic check failed");
            }

        } catch (XMLSignatureException ex) {
            logger.error(null, ex);
            throw new SMgrException("Signature Invalid: " + ex.getMessage(), ex);
        } catch (MarshalException ex) {
            logger.error(null, ex);
            throw new SMgrException("Signature Invalid: " + ex.getMessage(), ex);
        }

    }

    /**
     * Signs a Signable SAML Object using the private key with alias
     * <i>keyAlias</i>. Uses the opesaml2 library
     *
     * @param as The Signable SAML Object that is going to be signed. Usually a
     * SAML Assertion
     * @param keyAlias The NCP Trust Store Key Alias of the private key that
     * will be used for signing.
     * @param keyPassword
     * @throws SMgrException When signing fails
     * @see org.opensaml.common.SignableSAMLObject
     */
    public void signSAMLAssertion(SignableSAMLObject as, String keyAlias, char[] keyPassword)
            throws SMgrException {

        KeyPair kp = null;
        X509Certificate cert = null;
        //check if we must use the default key
        if (keyAlias == null) {
            kp = keyManager.getDefaultPrivateKey();
            cert = (X509Certificate) keyManager.getDefaultCertificate();

        } else {
            kp = keyManager.getPrivateKey(keyAlias, keyPassword);
            cert = (X509Certificate) keyManager.getCertificate(keyAlias);
        }

        Signature sig = (Signature) Configuration.getBuilderFactory().getBuilder(Signature.DEFAULT_ELEMENT_NAME).buildObject(Signature.DEFAULT_ELEMENT_NAME);

        Credential signingCredential = SecurityHelper.getSimpleCredential(cert, kp.getPrivate());

        //sig.setCanonicalizationAlgorithm(SignatureConstants.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
        sig.setSigningCredential(signingCredential);
        // sig.setKeyInfo(SecurityHelper.getKeyInfoGenerator(signingCredential, null, null).generate(signingCredential));
        sig.setSignatureAlgorithm(signatureAlgorithm);
        sig.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_WITH_COMMENTS);

        SecurityConfiguration secConfig = Configuration.getGlobalSecurityConfiguration();
        try {
            SecurityHelper.prepareSignatureParams(sig, signingCredential, secConfig, null);
        } catch (SecurityException e) {
            throw new SMgrException(e.getMessage(), e);
        }

        as.setSignature(sig);
        try {
            Configuration.getMarshallerFactory().getMarshaller(as).marshall(as);
        } catch (MarshallingException e) {
            throw new SMgrException(e.getMessage(), e);
        }
        try {
            Signer.signObject(sig);
        } catch (org.opensaml.xml.signature.SignatureException ex) {
            throw new SMgrException(ex.getMessage(), ex);
        }

    }

    /**
     * Signs an XML document using the default private key as it is configured
     * in the Configuration Manager. Uses enveloped XML Signatures
     *
     * @param doc The Document that is going to be signed. be used for signing.
     * @throws SMgrException When signing fails
     */
    public void signXMLWithEnvelopedSig(Document doc) throws SMgrException {
        signXMLWithEnvelopedSig(doc, null, null);
    }

    /**
     * Signs an XML document using the private key with alias <i>keyAlias</i>.
     * Uses enveloped XML Signatures
     *
     * @param doc The Document that is going to be signed.
     * @param keyAlias The NCP Trust Store Key Alias of the private key that
     * will be used for signing.
     * @param keyPassword
     * @throws SMgrException When signing fails
     */
    public void signXMLWithEnvelopedSig(Document doc, String keyAlias, char[] keyPassword)
            throws SMgrException {

        Logger logger = LoggerFactory.getLogger(SignatureManager.class.getName());

        KeyPair kp = null;
        X509Certificate cert = null;

        if (keyAlias == null) {
            kp = keyManager.getDefaultPrivateKey();
            cert = (X509Certificate) keyManager.getDefaultCertificate();
        } else {
            kp = keyManager.getPrivateKey(keyAlias, keyPassword);
            cert = (X509Certificate) keyManager.getCertificate(keyAlias);

        }

        try {
            String providerName = System.getProperty("jsr105Provider",
                    "org.jcp.xml.dsig.internal.dom.XMLDSigRI");

            XMLSignatureFactory fac = XMLSignatureFactory
                    .getInstance("DOM", (Provider) Class.forName(providerName).newInstance());

            Reference ref
                    = fac.newReference("", fac.newDigestMethod(digestAlgorithm, null),
                            Collections.singletonList(
                                    fac.newTransform(Transform.ENVELOPED, (XMLStructure) null)),
                            null, null);

            SignedInfo si
                    = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null),
                            fac.newSignatureMethod(signatureAlgorithm, null),
                            Collections.singletonList(ref));

            KeyInfoFactory kif = fac.getKeyInfoFactory();

            List<Serializable> x509Content = new ArrayList<Serializable>();
            x509Content.add(cert.getSubjectX500Principal().getName(X500Principal.RFC1779));
            x509Content.add(cert);
            X509Data xd = kif.newX509Data(x509Content);

            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

            DOMSignContext dsc = new DOMSignContext(kp.getPrivate(), doc.getDocumentElement());
            XMLSignature signature = fac.newXMLSignature(si, ki);
            signature.sign(dsc);

        } catch (ClassNotFoundException ex) {
            logger.error(null, ex);
            throw new SMgrException(ex.getMessage(), ex);
        } catch (InstantiationException ex) {
            logger.error(null, ex);
            throw new SMgrException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            logger.error(null, ex);
            throw new SMgrException(ex.getMessage(), ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(null, ex);
            throw new SMgrException(ex.getMessage(), ex);
        } catch (InvalidAlgorithmParameterException ex) {
            logger.error(null, ex);
            throw new SMgrException(ex.getMessage(), ex);
        } catch (MarshalException ex) {
            logger.error(null, ex);
            throw new SMgrException(ex.getMessage(), ex);
        } catch (XMLSignatureException ex) {
            logger.error(null, ex);
            throw new SMgrException(ex.getMessage(), ex);
        }
    }

    /**
     * Signs a Signable SAML Object using the default key that is configured in
     * the Configuration Manager. Uses the opesaml2 library
     *
     * @param trc The Signable SAML Object that is going to be signed. Usually a
     * SAML Assertion
     * @throws SMgrException When signing fails
     * @see org.opensaml.common.SignableSAMLObject
     * @see epsos.ccd.gnomon.configmanager.ConfigurationManagerService
     */
    public void signSAMLAssertion(Assertion trc) throws SMgrException {
        signSAMLAssertion(trc, null, null);
    }
}
