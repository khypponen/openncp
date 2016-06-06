/*
 *  WsSecurityUtils.java
 *  Created on 15/09/2014, 9:34:46 AM
 *  
 *  TRILLIUM.SECURITY.UTILS
 *  eu.europa.ec.joinup.ecc.trilliumsecurityutils.wssecurity
 *  
 *  Copyright (C) 2014 iUZ Technologies, Lda - http://www.iuz.pt and Gnomon Informatics - http://www.gnomon.com.gr 
 */
package eu.europa.ec.joinup.ecc.trilliumsecurityutils.wssecurity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.namespace.QName;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.util.XMLUtils;
import org.apache.ws.security.message.WSSecTimestamp;
import org.apache.xerces.dom.DocumentImpl;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.ws.wssecurity.KeyIdentifier;
import org.opensaml.ws.wssecurity.SecurityTokenReference;
import org.opensaml.xml.signature.impl.KeyInfoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.saml.SAML;

/**
 * Utility class with helper methods for WS-Security
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class WsSecurityUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(WsSecurityUtils.class);

    public static Element timeStampBuilder() {
        final int TIME_TO_LIVE = 300; // 300 seconds = 5 minutes
        final WSSecTimestamp timeStampBuilder;
        final Element result;

        timeStampBuilder = new WSSecTimestamp();
        timeStampBuilder.setTimeToLive(TIME_TO_LIVE);
        timeStampBuilder.prepare(new DocumentImpl());
        result = timeStampBuilder.getElement();

        return result;
    }

    public static org.opensaml.xml.signature.KeyInfo keyInfoSecurityTokenRefBuilder(final String assertionId) {
        Element result = null;
        final KeyInfoBuilder keyInfoBuilder;
        final org.opensaml.xml.signature.KeyInfo keyInfo;

        keyInfoBuilder = new KeyInfoBuilder();
        keyInfo = (org.opensaml.xml.signature.KeyInfo) keyInfoBuilder.buildObject();

        SecurityTokenReference securityTokenReference = new SAML().create(SecurityTokenReference.class, SecurityTokenReference.ELEMENT_NAME);
        securityTokenReference.getUnknownAttributes().put(new QName("TokenType"), "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0");

        KeyIdentifier keyIdentifier = new SAML().create(KeyIdentifier.class, KeyIdentifier.ELEMENT_NAME);
        keyIdentifier.setValueType("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID");
        keyIdentifier.setValue(assertionId);

        securityTokenReference.getUnknownXMLObjects().add(keyIdentifier);

        keyInfo.getXMLObjects().add(securityTokenReference);

        return keyInfo;

    }

    public static Element signSecurityHeader(SOAPHeaderBlock securityHeader, Assertion assertion) {
        Element result = null;
        final Element securityTokeReference;
        try {
            XMLSignatureFactory factory;
            KeyStore keyStore;
            KeyPair keyPair;
            KeyInfo keyInfo;

            // Set factory
            String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
            factory = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());

            // Set keyStore
            FileInputStream is = new FileInputStream(Constants.SC_KEYSTORE_PATH.split(Constants.EPSOS_PROPS_PATH)[0]);
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(is, Constants.SC_KEYSTORE_PASSWORD.toCharArray());
            is.close();

            KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(Constants.SC_PRIVATEKEY_PASSWORD.toCharArray());
            KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(Constants.SC_PRIVATEKEY_ALIAS, pp);

            // Set keyPair
            keyPair = new KeyPair(entry.getCertificate().getPublicKey(), entry.getPrivateKey());

            // Create Signature/SignedInfo/Reference
            List<Transform> lst = new ArrayList<Transform>();
            lst.add(factory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
            lst.add(factory.newTransform(CanonicalizationMethod.EXCLUSIVE, (TransformParameterSpec) null));
            Reference ref = factory.newReference("", factory.newDigestMethod(DigestMethod.SHA1, null), lst, null, null);

            SignedInfo signedInfo = factory.newSignedInfo(factory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null),
                    factory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

            // Sign Assertion
            DOMSignContext signContext = new DOMSignContext(keyPair.getPrivate(), XMLUtils.toDOM(securityHeader));
            XMLSignature signature = factory.newXMLSignature(signedInfo, null);
            signature.sign(signContext);

            result = (Element) signContext.getParent().getLastChild();

            securityTokeReference = SAML.addToElement(keyInfoSecurityTokenRefBuilder(assertion.getID()), (Element) signContext.getParent().getLastChild());

            result.appendChild(securityTokeReference);

        } catch (ClassNotFoundException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (InstantiationException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (IllegalAccessException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (FileNotFoundException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (KeyStoreException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (IOException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (NoSuchAlgorithmException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (CertificateException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (UnrecoverableEntryException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (InvalidAlgorithmParameterException ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        } catch (Exception ex) {
            LOG.error("An error has occurred during the SECURITY HEADER signing process.", ex);
        }
        return result;
    }

}
