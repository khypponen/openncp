package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service;

import org.w3c.dom.Element;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by gutowpa on 28/11/2016.
 * Adapted by Inês Garganta
 */
public class Signer {

  org.slf4j.Logger logger = LoggerFactory.getLogger(Signer.class);

  private XMLSignatureFactory sigFactory;

  private KeyStore.PrivateKeyEntry privateKeyEntry;
  private KeyInfo keyInfo;
  
  private boolean invalidKeystore;
  private boolean invalidKeyPair;

  public Signer(MultipartFile keystoreResPath, String keystorePass, String keyPairAlias, String keyPairPass){
    // Initialize all stuff needed for signing: Load keys from keystore and prepare signature factory
    invalidKeystore = false;
    invalidKeyPair = false;
    
    sigFactory = XMLSignatureFactory.getInstance("DOM");
    KeyStore ks = null;
    try {
      ks = KeyStore.getInstance("JKS");
      // InputStream keystoreStream = this.getClass().getResourceAsStream(keystoreResPath);
    } catch (KeyStoreException ex) {
      logger.error("\nKeyStoreException 1 - " + SimpleErrorHandler.printExceptionStackTrace(ex), ex);
    }

    try {
      ks.load(keystoreResPath.getInputStream(), keystorePass.toCharArray());
    } catch (IOException ex) {
      invalidKeystore = true;
      logger.error("\nIOException 2 - " + SimpleErrorHandler.printExceptionStackTrace(ex), ex);
      return;
    } catch (NoSuchAlgorithmException ex) {
      invalidKeystore = true;
      logger.error("\nNoSuchAlgorithmException 2 " + SimpleErrorHandler.printExceptionStackTrace(ex), ex);
      return;
    } catch (CertificateException ex) {
      invalidKeystore = true;
      logger.error("\nCertificateException 2 - " + SimpleErrorHandler.printExceptionStackTrace(ex), ex);
      return;
    }
    KeyStore.PasswordProtection passProtection = new KeyStore.PasswordProtection(keyPairPass.toCharArray());

    try {
      privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(keyPairAlias, passProtection);
    } catch (KeyStoreException ex) {
      invalidKeyPair = true;
      logger.error("\nKeyStoreException 3 - " + SimpleErrorHandler.printExceptionStackTrace(ex), ex);
      return;
    } catch (NoSuchAlgorithmException ex) {
      invalidKeyPair = true;
      logger.error("\nNoSuchAlgorithmException 3 - " + SimpleErrorHandler.printExceptionStackTrace(ex), ex);
      return;
    } catch (UnrecoverableEntryException ex) {
      invalidKeyPair = true;
      logger.error("\nUnrecoverableEntryException 3 - " + SimpleErrorHandler.printExceptionStackTrace(ex), ex);
      return;
    }
    
    X509Certificate cert = null;
    try {
      cert = (X509Certificate) privateKeyEntry.getCertificate();
    } catch (NullPointerException ex) {
      invalidKeyPair = true;
      logger.error("\nKeyStoreException 3 - " + SimpleErrorHandler.printExceptionStackTrace(ex), ex);
      return;
    }
    KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
    List x509Content = new ArrayList();
    x509Content.add(cert.getSubjectX500Principal().getName());
    x509Content.add(cert);
    X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
    keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
  }

  
  
  public void sign(String refUri, Element xtPointer, String c14nMethod) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, MarshalException, XMLSignatureException {

    Reference ref = sigFactory.newReference(
            refUri,
            sigFactory.newDigestMethod(DigestMethod.SHA256, null),
            Collections.singletonList(sigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
            null,
            null);

    SignedInfo signedInfo = sigFactory.newSignedInfo(
            sigFactory.newCanonicalizationMethod(c14nMethod, (C14NMethodParameterSpec) null),
            sigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
            Collections.singletonList(ref));

    DOMSignContext signContext = new DOMSignContext(privateKeyEntry.getPrivateKey(), xtPointer);

    // Create the XMLSignature, but don't sign it yet.
    XMLSignature signature = sigFactory.newXMLSignature(signedInfo, keyInfo);

    // Marshal, generate, and sign the enveloped signature.
    signature.sign(signContext);
  }
  
  
  public boolean isInvalidKeystore() {
    return invalidKeystore;
  }

  public void setInvalidKeystore(boolean invalidKeystore) {
    this.invalidKeystore = invalidKeystore;
  }

  public boolean isInvalidKeyPair() {
    return invalidKeyPair;
  }

  public void setInvalidKeyPair(boolean invalidKeyPair) {
    this.invalidKeyPair = invalidKeyPair;
  }
}
