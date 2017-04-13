//package eu.epsos.configmanager.test;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.UnsupportedEncodingException;
//import java.security.cert.Certificate;
//import java.security.cert.X509Certificate;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import epsos.ccd.gnomon.configmanager.SMPSignatureValidator;
//import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
//import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
//import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
//
//public class CustomizedSignatureValidator extends DefaultSignatureValidator {
//	private static final String OASIS_NS = "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05";
//
//	
//	@Override
//	public Certificate verify(Document arg0) throws TechnicalException {
//		try {
//			super.verify(arg0);
//			/*
//			 * What we do here. We first verify the signature of the SMP, then
//			 * the one in the extension.
//			 * 
//			 */
//			printDocument(arg0, System.out);
//			
////			Element smpSigPointer = findSignatureByParentNode(arg0.getDocumentElement());
//			SMPSignatureValidator val = new SMPSignatureValidator();  /// validate SMP signature
////			val.validateSignature(smpSigPointer);
////			X509Certificate smpCert = val.getKeySaved();
//			Element smNode = findFirstElementByName(arg0, "ServiceMetadata"); // pointing to an element with the signature
//			Document docUnwrapped = buildDocWithGivenRoot(smNode); // building a new document out of it
//			Element siSigPointer = findServiceInfoSig(docUnwrapped);
//			val.validateSignature(siSigPointer);
//			
//			return val.getKeySaved();
//		} catch (Exception e) {
//			throw new IllegalStateException(e.getMessage(), e);
//		}
//
//	}
//
//	private Document buildDocWithGivenRoot(Element smNode)
//			throws ParserConfigurationException, TransformerException, IOException, SAXException {
//		Document docUnwrapped = getDocumentBuilder().newDocument();
//		Node sm = docUnwrapped.importNode(smNode, true);
//		docUnwrapped.appendChild(sm);
//
//		// Marshalling and parsing the document - signature validation fails
//		// without this stinky "magic".
//		// _Probably_ SUN's implementation doesn't import correctly signatures
//		// between two different documents.
//		String strUnwrapped = marshall(docUnwrapped);
//		System.out.println(strUnwrapped);
//		return parseDocument(strUnwrapped);
//	}
//
//	private Document parseDocument(String docContent) throws IOException, SAXException, ParserConfigurationException {
//		InputStream inputStream = new ByteArrayInputStream(docContent.getBytes());
//		return getDocumentBuilder().parse(inputStream);
//	}
//
//	private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		dbf.setNamespaceAware(true);
//		return dbf.newDocumentBuilder();
//	}
//
//	private String marshall(Document doc) throws TransformerException, UnsupportedEncodingException {
//		TransformerFactory tf = TransformerFactory.newInstance();
//		Transformer trans = tf.newTransformer();
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		trans.transform(new DOMSource(doc), new StreamResult(stream));
//		return stream.toString("UTF-8");
//	}
//
//	private Element findServiceInfoSig(Document doc) throws ParserConfigurationException, SAXException, IOException {
//		Element extension = findExtensionInServiceInformation(doc);
//		return findSignatureByParentNode(extension);
//	}
//
//	private Element findExtensionInServiceInformation(Document doc)
//			throws ParserConfigurationException, SAXException, IOException {
//		Element serviceInformation = findFirstElementByName(doc, "ServiceInformation");
//
//		Element extension = null;
//		for (Node child = serviceInformation.getFirstChild(); child != null; child = child.getNextSibling()) {
//			if ("Extension".equals(child.getLocalName()) && OASIS_NS.equals(child.getNamespaceURI())) {
//				extension = (Element) child;
//			}
//		}
//
//		if (extension == null) {
//			throw new RuntimeException("Could not find Extension in ServiceInformation tag.");
//		}
//
//		return extension;
//	}
//
//	private Element findFirstElementByName(Document doc, String elementName) {
//		NodeList elements = doc.getElementsByTagNameNS(OASIS_NS, elementName);
//		return (Element) elements.item(0);
//	}
//
//	private Element findSignatureByParentNode(Element sigParent) {
//		for (Node child = sigParent.getFirstChild(); child != null; child = child.getNextSibling()) {
//			if ("Signature".equals(child.getLocalName())
//					&& "http://www.w3.org/2000/09/xmldsig#".equals(child.getNamespaceURI())) {
//				return (Element) child;
//			}
//		}
//		throw new RuntimeException("Signature not found in given node.");
//	}
//
//	public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
//		TransformerFactory tf = TransformerFactory.newInstance();
//		Transformer transformer = tf.newTransformer();
//		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
//
//		transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(out, "UTF-8")));
//	}
//}
