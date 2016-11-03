/*
 *  AssertionUtils.java
 *  Created on 22/09/2014, 3:04:49 PM
 *  
 *  TRILLIUM.SECURITY.UTILS
 *  eu.europa.ec.joinup.ecc.trilliumsecurityutils.saml
 *  
 *  Copyright (C) 2014 iUZ Technologies, Lda - http://www.iuz.pt
 */
package eu.europa.ec.joinup.ecc.trilliumsecurityutils.saml;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.DefaultKeyStoreManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.opensaml.Configuration;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.saml.SAML;

/**
 *
 * Utility class, with multiple helper methods for assertion extraction and
 * manipulation.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class AssertionUtils {

    /**
     * Class logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(AssertionUtils.class);

    /**
     * Extracts a TRC Assertion, from the Security Header of a given SOAP
     * Header.
     *
     * @param soapHeader - full SOAP Header
     * @return extracted TRC Assertion
     */
    public static Assertion extractTrcAssertion(final Element soapHeader) {
        if (soapHeader == null) {
            LOG.error("SOAP Header element is null.");
            return null;
        }

        final NodeList assertionList;
        Element assertionElement;
        Assertion assertion;
        Assertion result = null;

        assertionList = extractAssertionLIst(soapHeader);

        if (assertionList.getLength() == 0) {
            LOG.error("Assertion List is Empty.");
        } else {
            for (int i = 0; i < assertionList.getLength(); i++) {
                assertionElement = (Element) assertionList.item(i);
                try {
                    assertion = (Assertion) SAML.fromElement(assertionElement);
                    if (isTrcAssertion(assertion)) {
                        result = assertion;
                        break;
                    }
                } catch (IOException ex) {
                    LOG.error("An error has occurred during the assertion extraction.");
                } catch (UnmarshallingException ex) {
                    LOG.error("An error has occurred during the assertion extraction.");
                } catch (SAXException ex) {
                    LOG.error("An error has occurred during the assertion extraction.");
                }
            }
        }

        return result;
    }

    /**
     * Extracts a ID Assertion form the Security Header, present in the SOAP
     * Header.
     *
     * @param soapHeader - the full SOAP Header
     * @return
     */
    public static Assertion extractIdAssertion(final Element soapHeader) {
        if (soapHeader == null) {
            LOG.error("SOAP Header element is null.");
            return null;
        }

        final NodeList assertionList;
        Element assertionElement;
        Assertion assertion;
        Assertion result = null;

        assertionList = extractAssertionLIst(soapHeader);

        if (assertionList.getLength() == 0) {
            LOG.error("Assertion List is Empty.");
        } else {
            for (int i = 0; i < assertionList.getLength(); i++) {
                assertionElement = (Element) assertionList.item(i);
                try {
                    assertion = (Assertion) SAML.fromElement(assertionElement);
                    if (isIdAssertion(assertion)) {
                        result = assertion;
                        break;
                    }
                } catch (IOException ex) {
                    LOG.error("An error has occurred during the assertion extraction.");
                } catch (UnmarshallingException ex) {
                    LOG.error("An error has occurred during the assertion extraction.");
                } catch (SAXException ex) {
                    LOG.error("An error has occurred during the assertion extraction.");
                }
            }
        }

        return result;
    }

    /**
     * This method extracts the assertion list from a full SOAP Header, the list
     * can have the 0..* cardinality.
     *
     * @param soapHeader - the full SOAP Header.
     * @return the assertion list.
     */
    public static NodeList extractAssertionLIst(final Element soapHeader) {
        if (soapHeader == null) {
            LOG.error("SOAP Header element is null.");
            return null;
        }

        final String ASSERTIONS_NS = "urn:oasis:names:tc:SAML:2.0:assertion";
        final String ASSERTION = "Assertion";

        Element securityHeader;
        NodeList result;

        securityHeader = extractSecurityHeader(soapHeader);
        result = securityHeader.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion");

        return result;
    }

    /**
     * This method extracts the Security Header from the full SOAP Header.
     *
     * @param soapHeader - the full SOAP Header.
     * @return the Security Header.
     */
    public static Element extractSecurityHeader(final Element soapHeader) {
        if (soapHeader == null) {
            LOG.error("SOAP Header element is null.");
            return null;
        }
        Element result = null;
        NodeList securityList;

        securityList = soapHeader.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");

        if (securityList.getLength() == 0) {
            LOG.error("Security Header is Missing.");
        } else {
            result = (Element) securityList.item(0);
        }
        return result;
    }

    public static String getRoleFromKPAssertion(final Assertion assertion) {
        return "doctor";
    }
    
    
    /**
     * This method checks if a given Assertion is an Identity Assertion.
     *
     * @param assertion - the Assertion to check.
     * @return a Boolean result, declaring if the assertion is an Identity
     * assertion.
     */
    public static boolean isIdAssertion(final Assertion assertion) {
        if (assertion == null) {
            LOG.error("Provided Assertion is null.");
            return false;
        }
        boolean result = false;

        if (assertion.getAdvice() == null) {
            result = true;
        }

        return result;
    }

    /**
     * This method checks if a given Assertion is a Treatment Relationship
     * Confirmation Assertion.
     *
     * @param assertion - the Assertion to check.
     * @return a Boolean result, declaring if the assertion is a Treatment
     * Relationship Confirmation Assertion.
     */
    public static boolean isTrcAssertion(final Assertion assertion) {
        if (assertion == null) {
            LOG.error("Provided Assertion is null.");
            return false;
        }
        boolean result = false;

        if (assertion.getAdvice() != null) {
            result = true;
        }

        return result;
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private AssertionUtils() {
    }

    
    @SuppressWarnings("deprecation")
    public static void signSAMLAssertion(SignableSAMLObject as,
            String keyAlias, char[] keyPassword) throws Exception {

        ConfigurationManagerService cms = ConfigurationManagerService
                .getInstance();
        String KEYSTORE_LOCATION = Constants.NCP_SIG_KEYSTORE_PATH; //cms.getProperty("javax.net.ssl.keyStore");
        String KEY_STORE_PASS = Constants.NCP_SIG_KEYSTORE_PASSWORD;  //cms.getProperty("javax.net.ssl.keyStorePassword"); // GetterUtil.getString(GnPropsUtil.get("portalb",
        // "KEYSTORE_PASSWORD"),"spirit");
        String KEY_ALIAS = Constants.NCP_SIG_PRIVATEKEY_ALIAS; //cms.getProperty("javax.net.ssl.key.alias"); // GetterUtil.getString(GnPropsUtil.get("portalb",
        // "PRIVATEKEY_ALIAS"),"server1");
        String PRIVATE_KEY_PASS = Constants.NCP_SIG_PRIVATEKEY_PASSWORD; //cms.getProperty("javax.net.ssl.privateKeyPassword"); // GetterUtil.getString(GnPropsUtil.get("portalb",
        // "PRIVATEKEY_PASSWORD"),"spirit");


        KeyStoreManager keyManager = new DefaultKeyStoreManager();
        // KeyPair kp = null;
        X509Certificate cert = null;
        // check if we must use the default key
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        if (keyAlias == null) {
            cert = (X509Certificate) keyManager.getDefaultCertificate();
        } else {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            File file = new File(KEYSTORE_LOCATION);
            keyStore.load(new FileInputStream(file),
                    KEY_STORE_PASS.toCharArray());
            privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS,
                    PRIVATE_KEY_PASS.toCharArray());
            X509Certificate cert1 = (X509Certificate) keyStore
                    .getCertificate(KEY_ALIAS);
            publicKey = cert1.getPublicKey();
            cert = (X509Certificate) keyManager.getCertificate(keyAlias);
        }

        org.opensaml.xml.signature.Signature sig = (org.opensaml.xml.signature.Signature) Configuration
                .getBuilderFactory()
                .getBuilder(
                org.opensaml.xml.signature.Signature.DEFAULT_ELEMENT_NAME)
                .buildObject(
                org.opensaml.xml.signature.Signature.DEFAULT_ELEMENT_NAME);
        Credential signingCredential = SecurityHelper.getSimpleCredential(cert,
                privateKey);

        sig.setSigningCredential(signingCredential);
        sig.setSignatureAlgorithm("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
        sig.setCanonicalizationAlgorithm("http://www.w3.org/2001/10/xml-exc-c14n#");

        SecurityConfiguration secConfig = Configuration
                .getGlobalSecurityConfiguration();
        try {
            SecurityHelper.prepareSignatureParams(sig, signingCredential,
                    secConfig, null);
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
            org.opensaml.xml.signature.Signer.signObject(sig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    public static String getDocumentAsXml(org.w3c.dom.Document doc, boolean header)
	  {
	  String resp="";       
	  try
	  {
			DOMSource domSource = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			String omit="yes";
			if (header) omit="no";else omit="yes";
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,omit);
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			//transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
			// we want to pretty format the XML output
			// note : this is broken in jdk1.5 beta!
			transformer.setOutputProperty
			   ("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//
			java.io.StringWriter sw = new java.io.StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);
			resp = sw.toString();
	  }
	  catch (Exception e)
	  {
		  e.printStackTrace();  
	  }
	  return resp;
	}    
    
}
