/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtil {

    private static Logger logger = Logger.getLogger(XMLUtil.class);

    /**
     * Creates a new instance of XMLUtil
     */
    public XMLUtil() {
    }

    /**
     * returns null if Node is null
     */
    public static Node extractFromDOMTree(Node node) throws ParserConfigurationException {
        if (node == null) {
            return null;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document theDocument = db.newDocument();
        theDocument.appendChild(theDocument.importNode(node, true));
        //logger.info(XMLUtil.convertToString(theDocument));
        return (Node) theDocument.getDocumentElement();
    }

    /**
     * Gets a DOM document and canonicalize it using OMIT_COMMENTS. 
     * 
     * Add by massi - 29/12/2016
     * @param doc The document to be canonicalized
     * @return the canonicalized document
     * @throws Exception either the document is null, there is no available DOM factory, or a generic c14n error
     */
    public static Document canonicalize(Document doc) throws Exception {
    	 Canonicalizer canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS);
         byte[] back = canon.canonicalizeSubtree(doc);
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setNamespaceAware(true);

         Document docCanon = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(back));
         return docCanon;
    }
    public static org.w3c.dom.Document parseContent(byte[] byteContent) throws ParserConfigurationException, SAXException, IOException {
        org.w3c.dom.Document doc = null;
        String content = new String(byteContent);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //dbf.setIgnoringComments(false);
        dbf.setNamespaceAware(true);

        //dbf.setNamespaceAware(false);
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        StringReader lReader = new StringReader(content);
        InputSource inputSource = new InputSource(lReader);
        doc = docBuilder.parse(inputSource);
        return doc;
    }

    public static org.w3c.dom.Document parseContent(String content) throws ParserConfigurationException, SAXException, IOException {
        org.w3c.dom.Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //dbf.setIgnoringComments(false);
        dbf.setNamespaceAware(true);
        //dbf.setNamespaceAware(false);
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        StringReader lReader = new StringReader(content);
        InputSource inputSource = new InputSource(lReader);
        doc = docBuilder.parse(inputSource);
        return doc;
    }

    public static String DocumentToString(Document doc) throws TransformerConfigurationException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }

    public static String prettyPrint(Node node) throws TransformerException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result = null;
        OutputFormat format = new OutputFormat("XML", "UTF-8", true);
        format.setIndent(3);
        XMLSerializer output = new XMLSerializer(baos, format);
        try {
            output.asDOMSerializer();

            Element el = null;
            if (node instanceof Element) {
            	el = (Element)node;
            } else if (node instanceof Document) {
            	el = ((Document)node).getDocumentElement();
            } else {
            	throw new IllegalArgumentException("Invalid class given: " + node.getClass().getName());
            }
            output.serialize(el);
            result = baos.toString("UTF-8");
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static void prettyPrint(Document doc, OutputStream out) {
        OutputFormat format = new OutputFormat("XML", "UTF-8", true);
        format.setIndenting(true);
        format.setIndent(3);
        XMLSerializer serializer = new XMLSerializer(out, format);
        try {
            serializer.serialize(doc);
            out.close();
        } catch (IOException e) {
            logger.error("", e);
        }

    }

    // Has issues with character encoding DO NOT USE
//    public static byte[] convertToByteArray(Node node) throws TransformerException{
//    	/** FIXME: We assume that Transfor deals with encoding/charset internally */
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
//        transformer.transform(new DOMSource(node), new StreamResult(bos));
//        return bos.toByteArray();
//    }
    public static Map<String, String> parseNamespaceBindings(String namespaceBindings) {
        if (namespaceBindings == null) {
            return null;
        }
        //remove { and }
        namespaceBindings = namespaceBindings.substring(1, namespaceBindings.length() - 1);
        String[] bindings = namespaceBindings.split(",");
        Map<String, String> namespaces = new HashMap<String, String>();
        for (int i = 0; i < bindings.length; i++) {
            String[] pair = bindings[i].trim().split("=");
            String prefix = pair[0].trim();
            String namespace = pair[1].trim();
            //Remove ' and '
            //namespace = namespace.substring(1,namespace.length()-1);
            namespaces.put(prefix, namespace);
        }
        return namespaces;
    }

    public static Document marshall(Object object, String context, String schemaLocation) {
        Locale oldLocale = Locale.getDefault();
        Locale.setDefault(new Locale("en"));
        try {
            JAXBContext jc = JAXBContext.newInstance(
                    context);
            Marshaller marshaller = jc.createMarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaLocation));
            marshaller.setSchema(schema);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            marshaller.marshal(object, doc);
            Locale.setDefault(oldLocale);
            return doc;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Locale.setDefault(oldLocale);
        return null;
    }

    public static Object unmarshall(String context, String schemaLocation, String content) {
        Locale oldLocale = Locale.getDefault();
        Locale.setDefault(new Locale("en"));
        try {
            JAXBContext jc = JAXBContext.newInstance(
                    context);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaLocation));
            unmarshaller.setSchema(schema);

            Object obj = unmarshaller.unmarshal(new StringReader(content));
            Locale.setDefault(oldLocale);
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Locale.setDefault(oldLocale);
        return null;
    }

    public static Object unmarshallWithoutValidation(String context, String schemaLocation, String content) {
        Locale oldLocale = Locale.getDefault();
        Locale.setDefault(new Locale("en"));
        try {
            JAXBContext jc = JAXBContext.newInstance(
                    context);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaLocation));
            Object obj = unmarshaller.unmarshal(new StringReader(content));
            Locale.setDefault(oldLocale);
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Locale.setDefault(oldLocale);
        return null;
    }

    public static void main(String args[]) {
        try {
            String xmlString = "<RegistryResponse xmlns=\"urn:oasis:names:tc:ebxml-regrep:registry:xsd:2.1\" status=\"Success\"><Slot/></RegistryResponse>";
            org.w3c.dom.Document xmlDoc = XMLUtil.parseContent(xmlString.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Document newDocumentFromInputStream(InputStream in) {
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document ret = null;

        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            ret = builder.parse(new InputSource(in));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
