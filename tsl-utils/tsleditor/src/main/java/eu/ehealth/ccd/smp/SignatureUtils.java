/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehealth.ccd.smp;

import java.io.*;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.ElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class contains some methods that can be used to apply XML Digital Signatures to files.
 * Only the last method is currently used by other classes, the others are just experiments with APIs.
 * 
 * @author joao.cunha
 */
public class SignatureUtils {
    
    private static final String PRIVATE_KEY_ALIAS = "ppt.ncp-signature.epsos.spms.pt";
    private static final String PRIVATE_KEY_PASS = "testKS";
    private static final String KEY_STORE_PASS = "testKS";
    private static final String KEY_STORE_TYPE = "JKS";
    
    public static ByteArrayOutputStream sign(InputStream xmlFile, File privateKeyFile) throws Exception {
        Init.init();
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        final KeyStore keyStore = loadKeyStore(privateKeyFile);
        final Key privateKey = keyStore.getKey(PRIVATE_KEY_ALIAS, PRIVATE_KEY_PASS.toCharArray());
        final X509Certificate cert = (X509Certificate)keyStore.getCertificate(PRIVATE_KEY_ALIAS);
        sign(doc, cert, (PrivateKey)privateKey);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS).canonicalizeSubtree(doc));
        return outputStream;
    }
    
    private static void sign(Document doc, X509Certificate cert, PrivateKey key)
			throws XMLSecurityException {
		String BaseURI = "./";
		XMLSignature sig = new XMLSignature(
				doc,
				BaseURI,
				org.apache.xml.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256);
		doc.getDocumentElement().appendChild(sig.getElement());

//		doc.appendChild(doc.createComment(" Comment after "));

		Transforms transforms = new Transforms(doc);

		transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
//		sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
                sig.addDocument("", transforms, DigestMethod.SHA256);

		sig.addKeyInfo(cert);
		sig.addKeyInfo(cert.getPublicKey());

		sig.sign(key);               
	}
            
    public static ByteArrayOutputStream signFile(InputStream xmlFile, File privateKeyFile) throws Exception {
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        Init.init();
        ElementProxy.setDefaultPrefix(Constants.SignatureSpecNS, "");
        final KeyStore keyStore = loadKeyStore(privateKeyFile);
        final XMLSignature sig = new XMLSignature(doc, null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256);
        final Transforms transforms = new Transforms(doc);
        transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
        sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
        final Key privateKey = keyStore.getKey(PRIVATE_KEY_ALIAS, PRIVATE_KEY_PASS.toCharArray());
        final X509Certificate cert = (X509Certificate)keyStore.getCertificate(PRIVATE_KEY_ALIAS);
        sig.addKeyInfo(cert);
        sig.addKeyInfo(cert.getPublicKey());
        sig.sign(privateKey);
        doc.getDocumentElement().appendChild(sig.getElement());
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS).canonicalizeSubtree(doc));
        return outputStream;
    }
    
    private static KeyStore loadKeyStore(File privateKeyFile) throws Exception {
        final InputStream fileInputStream = new FileInputStream(privateKeyFile);
        try {
            final KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            keyStore.load(fileInputStream, KEY_STORE_PASS.toCharArray());
            return keyStore;
        }
        finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    public static void output(ByteArrayOutputStream signedOutputStream, String fileName) throws IOException {
        final OutputStream fileOutputStream = new FileOutputStream(fileName);
        try {
            fileOutputStream.write(signedOutputStream.toByteArray());
            fileOutputStream.flush();
        }
        finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }
    
    /* Using InputStream */
    private static KeyStore loadKeyStore(InputStream privateKeyFile) throws Exception {
        try {
            final KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            keyStore.load(privateKeyFile, KEY_STORE_PASS.toCharArray());
            return keyStore;
        }
        finally {
            IOUtils.closeQuietly(privateKeyFile);
        }
    }
        
    public static ByteArrayOutputStream sign(InputStream xmlFile, InputStream privateKeyFile) throws Exception {
        Init.init();
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        final KeyStore keyStore = loadKeyStore(privateKeyFile);
        final Key privateKey = keyStore.getKey(PRIVATE_KEY_ALIAS, PRIVATE_KEY_PASS.toCharArray());
        final X509Certificate cert = (X509Certificate)keyStore.getCertificate(PRIVATE_KEY_ALIAS);
        sign(doc, cert, (PrivateKey)privateKey);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        outputStream.write(Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS).canonicalizeSubtree(doc));
        
        // Skip canonicalization
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(outputStream);
        transformer.transform(source, result);
        
        return outputStream;
    }
    
    /* Passing keystore and private key info as parameters */
    public static ByteArrayOutputStream sign(InputStream xmlFile, File privateKeyFile, String keystorePassword, String privateKeyAlias, String privateKeyPassword) throws Exception {
        Init.init();
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        final KeyStore keyStore = loadKeyStore(privateKeyFile, keystorePassword);
        final Key privateKey = keyStore.getKey(privateKeyAlias, privateKeyPassword.toCharArray());
        final X509Certificate cert = (X509Certificate)keyStore.getCertificate(privateKeyAlias);
        sign(doc, cert, (PrivateKey)privateKey);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // canonicalize
        outputStream.write(Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS).canonicalizeSubtree(doc));
        
        // Skip canonicalization
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        DOMSource source = new DOMSource(doc);
//        StreamResult result = new StreamResult(outputStream);
//        transformer.transform(source, result);
        
        return outputStream;
    }
    
    /** privateKeyFile: keystsore filepath
        keystorePassword: password for the keystore */
    private static KeyStore loadKeyStore(File privateKeyFile, String keystorePassword) throws Exception {
        final InputStream fileInputStream = new FileInputStream(privateKeyFile);
        try {
            final KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            keyStore.load(fileInputStream, keystorePassword.toCharArray());
            return keyStore;
        }
        finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("Begin signing....");
        final InputStream fileInputStream = new FileInputStream("src/resources/Patient_Identification_Service_LU.xml");
        try {
//            output(signFile(fileInputStream, new File("src/resources/pt-signature-keystore.jks")), "src/resources/signed-Patient_Identification_Service_LU.xml");
            // using nonrep code
            output(sign(fileInputStream, new File("src/resources/pt-signature-keystore.jks")), "src/resources/signed-Patient_Identification_Service_LU.xml");
            System.out.println("Successfully signed!");
        }
        finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }
    
    /**
     * Method used to sign the Extension element of SMP files.
     * @param xtPointer Reference to the Element where the Signature should be stored.
     * @param xmlFile XML input file.
     * @param privateKeyFile Path to the keystore (JKS).
     * @param keystorePassword Keystore password.
     * @param privateKeyAlias Alias of the private key stored in the keystore.
     * @param privateKeyPassword Password to access the private key stored in the keystore.
     * @throws Exception 
     */
    public static void sign(Element xtPointer, InputStream xmlFile, File privateKeyFile, String keystorePassword, String privateKeyAlias, String privateKeyPassword) throws Exception {
        try (FileInputStream ksfis = new FileInputStream(privateKeyFile)) {
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
 
            Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA256, null),
                    Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                    null, null);
 
            // Create the SignedInfo.
            SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS, (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));
 
            // Load the KeyStore and get the signing key and certificate.
            KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);
            ks.load(ksfis, keystorePassword.toCharArray());
            KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(privateKeyAlias,
                    new KeyStore.PasswordProtection(privateKeyPassword.toCharArray()));
            X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
 
            // Create the KeyInfo containing the X509Data.
            KeyInfoFactory kif = fac.getKeyInfoFactory();
            List x509Content = new ArrayList();
            x509Content.add(cert.getSubjectX500Principal().getName());
            x509Content.add(cert);
            X509Data xd = kif.newX509Data(x509Content);
            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));
 
            // Create a DOMSignContext and specify the RSA PrivateKey and
            // location of the resulting XMLSignature's parent element.
            DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), xtPointer);
 
            // Create the XMLSignature, but don't sign it yet.
            javax.xml.crypto.dsig.XMLSignature signature = fac.newXMLSignature(si, ki);
 
            // Marshal, generate, and sign the enveloped signature.
            signature.sign(dsc);
        } catch (Exception e) {
            throw e;
        }
    }
}
