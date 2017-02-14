package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.exception.GenericException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *Service responsible for signing SMP files
 */
@Service
public class SignFile {
  org.slf4j.Logger logger = LoggerFactory.getLogger(SignFile.class);

  private static Signer NATIONAL_INFRASTRUCTURE_SIGNER;

  private static final String C14N_METHOD = CanonicalizationMethod.INCLUSIVE;

  //private static final String KEY_PAIR_NI_ALIAS = "sample_national_infrastructure";
  //private static final String KEY_PAIR_NI_PASS = "mock";
  private static final String OASIS_NS = "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05";
  private static final String XMLDSIG_NS = "http://www.w3.org/2000/09/xmldsig#";

  private File generatedSignFile;
  private boolean invalidKeystoreSMP;
  private boolean invalidKeyPairSMP;

  /**
   * Sign ServiceMetadata files (ServiceInformation or Redirect)
   * @param type
   * @param keystore
   * @param keystorePassword
   * @param keyAlias
   * @param keyPassword
   * @param signFile
   * @throws Exception
   */
  public void signFiles(String type, String fileName, MultipartFile keystore, String keystorePassword, String keyAlias, String keyPassword,
          MultipartFile signFile) throws Exception {
    logger.debug("\n===== SIGNFILE ======");
    invalidKeystoreSMP=false;
    invalidKeyPairSMP=false;

    NATIONAL_INFRASTRUCTURE_SIGNER = new Signer(keystore, keystorePassword, keyAlias, keyPassword);
    invalidKeystoreSMP = NATIONAL_INFRASTRUCTURE_SIGNER.isInvalidKeystore();
    invalidKeyPairSMP = NATIONAL_INFRASTRUCTURE_SIGNER.isInvalidKeyPair();
    
    if(invalidKeystoreSMP){
      return;
    } else if (invalidKeyPairSMP){
      return;
    }

    // ========================================================================================
    // "National Infrastructure" creates PUT ServiceMetadata request, and signs THE WHOLE document.
    // ========================================================================================
    InputStream inputStream = new ByteArrayInputStream(signFile.getBytes());
    signFile.getInputStream().reset();
    Document docPutRequest = getDocumentBuilder().parse(inputStream);
   
    Element extension = newExtension(type, docPutRequest);
    NATIONAL_INFRASTRUCTURE_SIGNER.sign("", extension, C14N_METHOD);

    //Request is ready to send (PUT) - sample test assertions:
    String strPutRequest = marshall(docPutRequest);
    Document docServiceMetadata = parseDocument(strPutRequest);   

    //validate "National Infrastructure" Signature
    //ServiceMetadata element is extracted from response and put as a root in a newly created doc.
    
    Element smNode = findFirstElementByName(docServiceMetadata, "ServiceMetadata");
    Document docUnwrapped = buildDocWithGivenRoot(smNode);
    Element siSigPointer = findSig(type, docUnwrapped);
    SignatureValidator.validateSignature(siSigPointer);
    
    generatedSignFile = new File("/" + fileName);
    Source source = new DOMSource(docServiceMetadata);
    Result result = new StreamResult(generatedSignFile);
    Transformer xformer = TransformerFactory.newInstance().newTransformer();
    xformer.transform(source, result);
  }

  private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    return dbf.newDocumentBuilder();
  }

  private String marshall(Document doc) throws TransformerException, UnsupportedEncodingException {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer trans = tf.newTransformer();
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    trans.transform(new DOMSource(doc), new StreamResult(stream));
    return stream.toString("UTF-8");
  }

  private Document parseDocument(String docContent) throws IOException, SAXException, ParserConfigurationException {
    InputStream inputStream = new ByteArrayInputStream(docContent.getBytes());
    return getDocumentBuilder().parse(inputStream);
  }

  private Element findSig(String type, Document doc) throws ParserConfigurationException, SAXException, IOException {
    Element extension = findExtension(type, doc);
    return findSignatureByParentNode(extension);
  }

  private Element findSignatureByParentNode(Element sigParent) {
    for (Node child = sigParent.getFirstChild(); child != null; child = child.getNextSibling()) {
      if ("Signature".equals(child.getLocalName()) && XMLDSIG_NS.equals(child.getNamespaceURI())) {
        return (Element) child;
      }
    }
    throw new RuntimeException("Signature not found in given node.");
  }

  private Element findExtension(String type, Document doc) throws ParserConfigurationException, SAXException, IOException {
    Element extension = null;
    if (!type.equals("Redirect")) {
      logger.debug("\n ********* ServiceInformation");
      Element serviceInformation = findFirstElementByName(doc, "ServiceInformation");
      for (Node child = serviceInformation.getFirstChild(); child != null; child = child.getNextSibling()) {
        if ("Extension".equals(child.getLocalName()) && OASIS_NS.equals(child.getNamespaceURI())) {
          extension = (Element) child;
        }
      }
      if (extension == null) {
        throw new GenericException("Not Found", "Could not find Extension in ServiceInformation tag.");
      }
    } else if (type.equals("Redirect")) {
      logger.debug("\n ********* Redirect");
      Element redirect = findFirstElementByName(doc, "Redirect");
      for (Node child = redirect.getFirstChild(); child != null; child = child.getNextSibling()) {
        if ("Extension".equals(child.getLocalName()) && OASIS_NS.equals(child.getNamespaceURI())) {
          extension = (Element) child;
        }
      }
      if (extension == null) {
        throw new GenericException("Not Found", "Could not find Extension in ServiceInformation tag.");
      }
    }
    return extension;
  }
  
  private Element newExtension(String type, Document doc) throws ParserConfigurationException, SAXException, IOException {
    Element extension = null;
    if (!type.equals("Redirect")) {
      Element serviceInformation = findFirstElementByName(doc, "ServiceInformation");
      //Find extension, if exists delete all childs
      for (Node child = serviceInformation.getFirstChild(); child != null; child = child.getNextSibling()) {
        if ("Extension".equals(child.getLocalName()) && OASIS_NS.equals(child.getNamespaceURI())) {
          extension = (Element) child;
          while (extension.hasChildNodes()) {
            extension.removeChild(extension.getFirstChild());
          }
        }
      }
      //Add new extension
      if (extension == null) {
        extension = doc.createElementNS(doc.getFirstChild().getNamespaceURI(), "Extension");
        serviceInformation.appendChild(extension);   
      }
    } else if (type.equals("Redirect")) {
      Element redirect = findFirstElementByName(doc, "Redirect");
      //Find extension, if exists delete all childs
      for (Node child = redirect.getFirstChild(); child != null; child = child.getNextSibling()) {
        if ("Extension".equals(child.getLocalName()) && OASIS_NS.equals(child.getNamespaceURI())) {
          extension = (Element) child;
          while (extension.hasChildNodes()) {
            extension.removeChild(extension.getFirstChild());
          }
        }
      }
      //Add new extension
      if (extension == null) {
        extension = doc.createElementNS(doc.getFirstChild().getNamespaceURI(), "Extension");
        redirect.appendChild(extension);
      }
    }
    return extension;
  }

  private Element findFirstElementByName(Document doc, String elementName) {
    NodeList elements = doc.getElementsByTagNameNS(OASIS_NS, elementName);
    return (Element) elements.item(0);
  }

  private Document buildDocWithGivenRoot(Element smNode) throws ParserConfigurationException, TransformerException, IOException, SAXException {
    Document docUnwrapped = getDocumentBuilder().newDocument();
    Node sm = docUnwrapped.importNode(smNode, true);
    docUnwrapped.appendChild(sm);

    // Marshalling and parsing the document - signature validation fails without this stinky "magic".
    // _Probably_ SUN's implementation doesn't import correctly signatures between two different documents.
    String strUnwrapped = marshall(docUnwrapped);
    logger.debug("\n ********* buildDocWithGivenRoot: \n" + strUnwrapped);
    return parseDocument(strUnwrapped);
  }

  
  /*Sets and Gets*/
  public File getGeneratedSignFile() {
    return generatedSignFile;
  }

  public void setGeneratedSignFile(File generatedSignFile) {
    this.generatedSignFile = generatedSignFile;
  }
 
  public boolean isInvalidKeystoreSMP() {
    return invalidKeystoreSMP;
  }

  public void setInvalidKeystoreSMP(boolean invalidKeystoreSMP) {
    this.invalidKeystoreSMP = invalidKeystoreSMP;
  }

  public boolean isInvalidKeyPairSMP() {
    return invalidKeyPairSMP;
  }

  public void setInvalidKeyPairSMP(boolean invalidKeyPairSMP) {
    this.invalidKeyPairSMP = invalidKeyPairSMP;
  }
}
