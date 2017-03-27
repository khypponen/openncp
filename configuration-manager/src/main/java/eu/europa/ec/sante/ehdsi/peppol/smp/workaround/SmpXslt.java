/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.sante.ehdsi.peppol.smp.workaround;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Iterator;

/**
 * This class provides the set of methods to apply in order to workaround the
 * PEPPOL-SMP issue (fetching the file from PEPPOL-SMP means rebuilding the file
 * from DB, which may create invalid scheme operator signature). It takes into
 * account that the scheme operator signature is placed in the ServiceInformation/Extension
 * element.
 * <p>
 * The chain of methods is the following:
 * 1) unsignedSmpFile = removeSmpSignatureFromFile(smpFile): removes the SMP signature from the SMP file
 * 2) applyXsltToSmp(unsignedSmpFile, originalSmpFile): applies XSLT to the downloaded
 * file in order to recreate the original one that was uploaded
 * 3) validateXml(originalSmpFile, namespace, element): validates the scheme
 * operator signature of the SMP file, which was created agains the original file
 *
 * @author jgoncalves
 */
public class SmpXslt {
    // log4j2
//    private static final Logger logger = LogManager.getLogger(SmpXslt.class);

    // log4j 1.2
    private static final Logger logger = LoggerFactory.getLogger(SmpXslt.class);

    public static void main(String[] args) throws Exception {
        //BasicConfigurator.configure();
//        String inputFile = "consent_discard_from_smp_new.xml";
//        String outputFile = "consent_discard_result_new.xml";
        String inputFile = "ism_from_smp_new.xml";
        String outputFile = "ism_result_new.xml";
        Document originalSmpRecordWithoutSmpSignature = removeSmpSignatureFromFile(inputFile);
        printDocument(originalSmpRecordWithoutSmpSignature, System.out);
        applyXsltToSmp(originalSmpRecordWithoutSmpSignature, outputFile);
        final String ns = "http://busdox.org/serviceMetadata/publishing/1.0/";
        validateXml(outputFile, ns, "ServiceInformation");
    }

    public static Document removeSmpSignatureFromFile(String file) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try (FileInputStream fis = new FileInputStream(new File(file))) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document smpRecord = db.parse(fis);
            removeSmpSignature(smpRecord);
            return smpRecord;
        }
    }

    public static void removeSmpSignature(Document smpRecord) {
        logger.info("Removing the SMP signature");
        // I have to remove the signature element created by the SMP record
        NodeList removeSMPSignatureEl = smpRecord.getDocumentElement().getChildNodes();
        int removeSize = removeSMPSignatureEl.getLength();
        for (int i = 0; i < removeSize; i++) {
            Node nodeToRemove = removeSMPSignatureEl.item(i);
            if (nodeToRemove.getNodeType() == Node.ELEMENT_NODE && nodeToRemove.getLocalName().equals("Signature")
                    && nodeToRemove.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) {
                Element el = (Element) nodeToRemove;
                smpRecord.getDocumentElement().removeChild(el);
            }
        }
    }

    public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
        logger.info("Printing document...");
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }

    public static void applyXsltToSmp(Document smpRecord, String outputFile) throws TransformerConfigurationException, TransformerException, FileNotFoundException {
        logger.info("Applying XSLT...");
        TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
        Source smpFile = new DOMSource(smpRecord);
        Source xslt = new StreamSource(SmpXslt.class.getResourceAsStream("/xslt/format-smp.xsl"));
        Transformer transformer = factory.newTransformer(xslt);
        transformer.setErrorListener(new SmpXsltListener());
        transformer.transform(smpFile, new StreamResult(new FileOutputStream(new File(outputFile))));
    }

    public static Document applyXsltToSmp(Document smpRecord) throws TransformerConfigurationException, TransformerException, SAXException, IOException, ParserConfigurationException {

        logger.info("Applying XSLT...");
        TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
        Source smpFile = new DOMSource(smpRecord);
        Source xslt = new StreamSource(SmpXslt.class.getResourceAsStream("/xslt/format-smp.xsl"));
        Transformer transformer = factory.newTransformer(xslt);
        transformer.setErrorListener(new SmpXsltListener());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transformer.transform(smpFile, new StreamResult(baos));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder().parse(new ByteArrayInputStream(baos.toByteArray()));
    }

    public static void validateSignature(final Document smpRecord, Element xtPointer) throws Exception {

        logger.info("Starting signature validation...");
        // Find Signature element.
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        // NodeList elements = smpRecord.getElementsByTagNameNS(ns,
        // "Extension");
        // Node n = elements.item(0);
        // Element xtPointer = (Element) n;


        // NodeList nl = xtPointer.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");

        NodeList nl = xtPointer.getChildNodes();
        if (nl.getLength() == 0) {
            throw new Exception("Unable to find child nodes on the element");
        }

        int size = nl.getLength();
        Element signatureel = null;
        for (int i = 0; i < size; i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) n;
                if (el.getLocalName().equals("Signature") && el.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) {
                    signatureel = el;
                }
            }
        }

        if (signatureel == null) {
            throw new Exception("Unable to get the signature");
        }

        // Create a DOMValidateContext and specify a KeySelector
        // and document context.
        DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), signatureel);

        valContext.setProperty("javax.xml.crypto.dsig.cacheReference", Boolean.TRUE);

        // Unmarshal the XMLSignature.
        XMLSignature signature = fac.unmarshalXMLSignature(valContext);
        // Validate the XMLSignature.
        boolean coreValidity = signature.validate(valContext);

        Iterator i = signature.getSignedInfo().getReferences().iterator();
        for (int j = 0; i.hasNext(); j++) {
            logger.debug("Iterating: " + j);
            final Reference ref = (Reference) i.next();
            InputStream is = (ref).getDigestInputStream();
            // Display the data.
            //byte[] a = IOUtils.readFully(is, 0, true);
            byte[] a = IOUtils.readFully(is, 0);
            logger.debug("Reference: " + new String(a));
        }

        // // Unmarshal the XMLSignature.
        // XMLSignature signature = fac.unmarshalXMLSignature(valContext);
        //
        // // Validate the XMLSignature.
        // boolean coreValidity = signature.validate(valContext);

        // Check core validation status.
        if (coreValidity == false) {
            logger.error("Signature failed core validation");
            boolean sv = signature.getSignatureValue().validate(valContext);
            logger.debug("signature validation status: " + sv);
            if (sv == false) {
                // Check the validation status of each Reference.
                Iterator i1 = signature.getSignedInfo().getReferences().iterator();
                for (int j = 0; i1.hasNext(); j++) {
                    boolean refValid = ((Reference) i1.next()).validate(valContext);
                    logger.debug("ref[" + j + "] validity status: " + refValid);
                }
            }
        } else {
            logger.debug("+++ Signature passed core validation +++ ");
        }

    }

    // TODO MASSI REFACTOR A LOT
    public static void validateXml(Document doc, String namespace, String element) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, Exception {

        NodeList elements = doc.getElementsByTagNameNS(namespace, element);
        Node serviceInformation = elements.item(0);
        Node n = serviceInformation.getLastChild();
        Element xtPointer = (Element) n;
        validateSignature(doc, xtPointer);

    }

    public static void validateXml(String file, String namespace, String element) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try (FileInputStream fis = new FileInputStream(new File(file))) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document smpRecord = db.parse(fis);
            NodeList elements = smpRecord.getElementsByTagNameNS(namespace, element);
            Node serviceInformation = elements.item(0);
            Node n = serviceInformation.getLastChild();
            Element xtPointer = (Element) n;
            validateSignature(smpRecord, xtPointer);
        }
    }
}
