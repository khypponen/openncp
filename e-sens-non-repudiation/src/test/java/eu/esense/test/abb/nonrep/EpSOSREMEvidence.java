package eu.esense.test.abb.nonrep;

import eu.esens.abb.nonrep.Utilities;
import eu.esens.abb.nonrep.etsi.rem.*;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.UUID;

public class EpSOSREMEvidence {

    static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
    private static final String xadesNs = "http://uri.etsi.org/01903/v1.3.2#";
    private static final String dsigNs = "http://www.w3.org/2000/09/xmldsig#";
    private static X509Certificate cert;
    private static PrivateKey key;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("test/testData/s1.keystore"),
                "spirit".toCharArray());
        cert = (X509Certificate) ks.getCertificate("server1");
        key = (PrivateKey) ks.getKey("server1", "spirit".toCharArray());
        org.apache.xml.security.Init.init();
    }

    @Test
    public void testNRO() throws JAXBException, CertificateEncodingException,
            ParserConfigurationException, IOException, SAXException, DatatypeConfigurationException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, URISyntaxException,
            MarshalException, XMLSignatureException, TransformerException, XMLSecurityException {
        JAXBContext jc = JAXBContext
                .newInstance("eu.esens.abb.nonrep.etsi.rem");
        ObjectFactory of = new ObjectFactory();
        REMEvidenceType type = new REMEvidenceType();
        type.setVersion("2");
        type.setEventCode("Acceptance");

        type.setEvidenceIdentifier(UUID.randomUUID().toString());

        AuthenticationDetailsType adt = new AuthenticationDetailsType();
        adt.setAuthenticationMethod("TLS");
        type.setSenderAuthenticationDetails(adt);

		/*
         * ISO Token mappings
		 */
        // This is the Pol field of the ISO13888 token
        EvidenceIssuerPolicyID eipid = new EvidenceIssuerPolicyID();
        eipid.getPolicyIDs().add("urn:oid:1.2.3.4");
        type.setEvidenceIssuerPolicyID(eipid);

        // The flag f1 is the AcceptanceRejection (the evidence type)

        // This is the A field
        EntityDetailsType edt1 = new EntityDetailsType();
        CertificateDetails cd1 = new CertificateDetails();
        edt1.setCertificateDetails(cd1);
        cd1.setX509Certificate(cert.getEncoded());
        type.setSenderDetails(edt1);

        // This is the B field
        RecipientsDetails rd = new RecipientsDetails();
        rd.getEntityDetails().add(edt1);
        type.setRecipientsDetails(rd);

        // Evidence Issuer Details is the C field of the ISO token
        EntityDetailsType edt = new EntityDetailsType();
        CertificateDetails cd = new CertificateDetails();
        edt.setCertificateDetails(cd);
        cd.setX509Certificate(cert.getEncoded());
        type.setEvidenceIssuerDetails(edt);

        // This is the T_g field
        DateTime dt = new DateTime();
        //type.setEventTime(new XMLGregorianCalendarImpl(dt.toGregorianCalendar()));
        type.setEventTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(dt.toGregorianCalendar()));

        // This is the T_1 field
        DateTime dt1 = new DateTime();
        //type.setSubmissionTime(new XMLGregorianCalendarImpl(dt1.toGregorianCalendar()));
        type.setSubmissionTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(dt1.toGregorianCalendar()));

        // This is mandated by REM. If this is the full message,
        // we can avoid to build up the NROT Token as text||z_1||sa(z_1)
        MessageDetailsType mdt = new MessageDetailsType();
        DigestMethod dm = new DigestMethod();
        dm.setAlgorithm("SHA256");
        mdt.setDigestMethod(dm);
        mdt.setDigestValue("asdfasdfadsf".getBytes());
        mdt.setIsNotification(false);
        mdt.setMessageSubject("epSOS-31");
        mdt.setUAMessageIdentifier("urn:oid:12345");

        mdt.setDigestMethod(dm);
        type.setSenderMessageDetails(mdt);
        // Imp is the signature

        JAXBElement<REMEvidenceType> back = of
                .createSubmissionAcceptanceRejection(type);
        Marshaller marshaller = jc.createMarshaller();
        File f = File.createTempFile("massi", "masi");
        marshaller.marshal(back, f);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(f);


        Utilities.serialize(doc.getDocumentElement());

		/*
         * Sign it
		 */
        String BaseURI = f.toURI().toURL().toString();
        XMLSignature sig =
                new XMLSignature(doc, BaseURI, org.apache.xml.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA);
        doc.getDocumentElement().appendChild(sig.getElement());

        doc.appendChild(doc.createComment(" Comment after "));

        Transforms transforms = new Transforms(doc);

        transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
        transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
        sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

        sig.addKeyInfo(cert);
        sig.addKeyInfo(cert.getPublicKey());

        sig.sign(key);
        Utilities.serialize(doc.getDocumentElement());


    }

    @Test
    public void testNRD() throws JAXBException, CertificateEncodingException,
            ParserConfigurationException, IOException, SAXException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException, NoSuchAlgorithmException, DatatypeConfigurationException,
            InvalidAlgorithmParameterException, URISyntaxException,
            MarshalException, XMLSignatureException, TransformerException, XMLSecurityException {
        JAXBContext jc = JAXBContext
                .newInstance("eu.esens.abb.nonrep.etsi.rem");
        ObjectFactory of = new ObjectFactory();
        REMEvidenceType type = new REMEvidenceType();
        type.setVersion("2");
        type.setEventCode("Acceptance");

        type.setEvidenceIdentifier(UUID.randomUUID().toString());

        AuthenticationDetailsType adt = new AuthenticationDetailsType();
        adt.setAuthenticationMethod("TLS");
        type.setSenderAuthenticationDetails(adt);

		/*
         * ISO Token mappings
		 */
        // This is the Pol field of the ISO13888 token
        EvidenceIssuerPolicyID eipid = new EvidenceIssuerPolicyID();
        eipid.getPolicyIDs().add("urn:oid:1.2.3.4");
        type.setEvidenceIssuerPolicyID(eipid);

        // The flag f1 is the AcceptanceRejection (the evidence type)

        // This is the A field
        EntityDetailsType edt1 = new EntityDetailsType();
        CertificateDetails cd1 = new CertificateDetails();
        edt1.setCertificateDetails(cd1);
        cd1.setX509Certificate(cert.getEncoded());
        type.setSenderDetails(edt1);

        // This is the B field
        RecipientsDetails rd = new RecipientsDetails();
        rd.getEntityDetails().add(edt1);
        type.setRecipientsDetails(rd);

        // Evidence Issuer Details is the C field of the ISO token
        EntityDetailsType edt = new EntityDetailsType();
        CertificateDetails cd = new CertificateDetails();
        edt.setCertificateDetails(cd);
        cd.setX509Certificate(cert.getEncoded());
        type.setEvidenceIssuerDetails(edt);

        // This is the T_g field
        DateTime dt = new DateTime();
        //type.setEventTime(new XMLGregorianCalendarImpl(dt.toGregorianCalendar()));
        type.setEventTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(dt.toGregorianCalendar()));

        // This is the T_1 field
        DateTime dt1 = new DateTime();
        //type.setSubmissionTime(new XMLGregorianCalendarImpl(dt1.toGregorianCalendar()));
        type.setSubmissionTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(dt1.toGregorianCalendar()));

        // This is mandated by REM. If this is the full message,
        // we can avoid to build up the NROT Token as text||z_1||sa(z_1)
        MessageDetailsType mdt = new MessageDetailsType();
        DigestMethod dm = new DigestMethod();
        dm.setAlgorithm("SHA256");
        mdt.setDigestMethod(dm);
        mdt.setDigestValue("asdfasdfadsf".getBytes());
        mdt.setIsNotification(false);
        mdt.setMessageSubject("epSOS-31");
        mdt.setUAMessageIdentifier("urn:oid:12345");

        mdt.setDigestMethod(dm);
        type.setSenderMessageDetails(mdt);
        // Imp is the signature

        JAXBElement<REMEvidenceType> back = of
                .createDeliveryNonDeliveryToRecipient(type);
        Marshaller marshaller = jc.createMarshaller();
        File f = File.createTempFile("massi", "masi");
        marshaller.marshal(back, f);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(f);


        Utilities.serialize(doc.getDocumentElement());

		/*
		 * Sign it
		 */
        String BaseURI = f.toURI().toURL().toString();
        XMLSignature sig =
                new XMLSignature(doc, BaseURI, org.apache.xml.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA);
        doc.getDocumentElement().appendChild(sig.getElement());

        doc.appendChild(doc.createComment(" Comment after "));

        Transforms transforms = new Transforms(doc);

        transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
        transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
        sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

        sig.addKeyInfo(cert);
        sig.addKeyInfo(cert.getPublicKey());

        sig.sign(key);
        Utilities.serialize(doc.getDocumentElement());


    }


}
