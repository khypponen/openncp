package epsos.ccd.gnomon.configmanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;

/**
 * Customized signature validation code. This class uses the code from DIGIT to validate
 * the signature of the SMP record, and adds the custom valiation to enforce the direct 
 * brokered trust. Part of this code comes from Pawel Gutowsky from DIGIT.
 * @author max
 *
 */
public class CustomizedSignatureValidator extends DefaultSignatureValidator {
	
	/** OASIS BDXR namespace. */
	private static final String OASIS_NS = "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05";


	/**
	 * Constructor. 
	 * @param trustStore The truststore which MUST contain the certificate and the CA path for the SMP signature.
	 * @throws TechnicalException
	 */
	public CustomizedSignatureValidator(KeyStore trustStore) throws TechnicalException {
		super(trustStore);
	}


	/**
	 * Verify all the signatures. It calls the verify in the super() and then it applies the verification for the
	 * OpenNCP signature. 
	 */
	@Override
	public X509Certificate verify(Document arg0) throws TechnicalException {
		try {
			super.verify(arg0);
			/*
			 * What we do here. We first verify the signature of the SMP, then
			 * the one in the extension.
			 * 
			 */
			
			SMPSignatureValidator val = new SMPSignatureValidator();  /// validate SMP signature

			Element smNode = findFirstElementByName(arg0, "ServiceMetadata"); // pointing to an element with the signature
			Document docUnwrapped = buildDocWithGivenRoot(smNode); // building a new document out of it
			Element siSigPointer = findServiceInfoSig(docUnwrapped);
			val.validateSignature(siSigPointer);
			
			return val.getKeySaved();
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}

	}

	/**
	 * We need to build a new document to validate the signature of the SMP. Probably it's a bug
	 * of the jaxp parser. 
	 * @param smNode The node for the service metadata
	 * @return The newly created document with all the other values
	 * @throws ParserConfigurationException 
	 * @throws TransformerException
	 * @throws IOException
	 * @throws SAXException
	 */
	private Document buildDocWithGivenRoot(Element smNode)
			throws ParserConfigurationException, TransformerException, IOException, SAXException {
		Document docUnwrapped = getDocumentBuilder().newDocument();
		Node sm = docUnwrapped.importNode(smNode, true);
		docUnwrapped.appendChild(sm);

		// Marshalling and parsing the document - signature validation fails
		// without this stinky "magic".
		// _Probably_ SUN's implementation doesn't import correctly signatures
		// between two different documents.
		String strUnwrapped = marshall(docUnwrapped);
		return parseDocument(strUnwrapped);
	}

	/**
	 * Parse an input stream into DOM
	 * @param docContent 
	 * @return the DOM
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private Document parseDocument(String docContent) throws IOException, SAXException, ParserConfigurationException {
		InputStream inputStream = new ByteArrayInputStream(docContent.getBytes());
		return getDocumentBuilder().parse(inputStream);
	}

	/**
	 * 
	 * @return the DocumentBuilderFactory
	 * @throws ParserConfigurationException
	 */
	private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		return dbf.newDocumentBuilder();
	}

	/**
	 * From DOM to String
	 * @param doc the DOM
	 * @return the string
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException
	 */
	private String marshall(Document doc) throws TransformerException, UnsupportedEncodingException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		trans.transform(new DOMSource(doc), new StreamResult(stream));
		return stream.toString("UTF-8");
	}

	/** 
	 * Searches for the ServiceInformation, element who holds the extension and the signature for the NCP
	 * @param doc the DOM
	 * @return the element with the signature
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Element findServiceInfoSig(Document doc) throws ParserConfigurationException, SAXException, IOException {
		Element extension = findExtensionInServiceInformation(doc);
		return findSignatureByParentNode(extension);
	}

	/**
	 * Get the Extension element (holding the NCP signature)
	 * @param doc the DOM
	 * @return the signature's encapsulated in the extension
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Element findExtensionInServiceInformation(Document doc)
			throws ParserConfigurationException, SAXException, IOException {
		Element serviceInformation = findFirstElementByName(doc, "ServiceInformation");

		Element extension = null;
		for (Node child = serviceInformation.getFirstChild(); child != null; child = child.getNextSibling()) {
			if ("Extension".equals(child.getLocalName()) && OASIS_NS.equals(child.getNamespaceURI())) {
				extension = (Element) child;
			}
		}

		if (extension == null) {
			throw new RuntimeException("Could not find Extension in ServiceInformation tag.");
		}

		return extension;
	}

	/**
	 * 
	 * @param doc
	 * @param elementName
	 * @return
	 */
	private Element findFirstElementByName(Document doc, String elementName) {
		NodeList elements = doc.getElementsByTagNameNS(OASIS_NS, elementName);
		return (Element) elements.item(0);
	}

	/**
	 * Find the Signature element
	 * @param sigParent
	 * @return
	 */
	private Element findSignatureByParentNode(Element sigParent) {
		for (Node child = sigParent.getFirstChild(); child != null; child = child.getNextSibling()) {
			if ("Signature".equals(child.getLocalName())
					&& "http://www.w3.org/2000/09/xmldsig#".equals(child.getNamespaceURI())) {
				return (Element) child;
			}
		}
		throw new RuntimeException("Signature not found in given node.");
	}

	public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}
}