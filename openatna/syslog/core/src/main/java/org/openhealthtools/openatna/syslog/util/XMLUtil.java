/*
 * XMLUtil.java
 *
 * Created on November 9, 2007, 1:13 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.openhealthtools.openatna.syslog.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.Locale;
import javax.xml.bind.Unmarshaller;



/**
 *
 * @author tuncay
 */
public class XMLUtil {
    
    
    /** Creates a new instance of XMLUtil */
    public XMLUtil() {
    }
    
    /** returns null if Node is null */
    public static Node extractFromDOMTree(Node node) throws ParserConfigurationException{
        if (node == null) {
            return null;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document theDocument = db.newDocument();
        theDocument.appendChild(theDocument.importNode(node,true));
        //logger.info(XMLUtil.convertToString(theDocument));
        return (Node) theDocument.getDocumentElement();
    }
    
    public static org.w3c.dom.Document parseContent(byte [] byteContent) throws ParserConfigurationException, SAXException, IOException{
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
    
    public static org.w3c.dom.Document parseContent(String content) throws ParserConfigurationException, SAXException, IOException{
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
    
    public static String prettyPrint(Node node) throws TransformerException{
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	String result = null;
    	OutputFormat format = new OutputFormat("XML", "UTF-8", true);
    	format.setIndent(3);
    	XMLSerializer output = new XMLSerializer(baos, format);
    	try {
			output.asDOMSerializer();		
	    	output.serialize((Element) node);
	    	result = baos.toString("UTF-8");
	    	baos.close();
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
    
    public static byte[] convertToByteArray(Node node) throws TransformerException{
    	/** FIXME: We assume that Transfor deals with encoding/charset internally */
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
        transformer.transform(new DOMSource(node), new StreamResult(bos));
        return bos.toByteArray();
    }
    
    public static Map<String, String> parseNamespaceBindings(String namespaceBindings){
        if(namespaceBindings == null)
            return null;
        //remove { and } 
        namespaceBindings = namespaceBindings.substring(1,namespaceBindings.length()-1);
        String []bindings = namespaceBindings.split(",");
        Map<String, String> namespaces = new HashMap<String,String>();
        for(int i=0;i < bindings.length;i++){
            String []pair = bindings[i].trim().split("=");
            String prefix = pair[0].trim();
            String namespace = pair[1].trim();
            //Remove ' and '
            //namespace = namespace.substring(1,namespace.length()-1);
            namespaces.put(prefix,namespace);
        }
        return namespaces;
    }
    
    public static Document marshall(Object object, String context, String schemaLocation){
        Locale oldLocale = Locale.getDefault();
	Locale.setDefault(new Locale("en"));
        try{
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
            marshaller.marshal( object, doc );
            Locale.setDefault(oldLocale);
            return doc;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        Locale.setDefault(oldLocale);
        return null;
    }
    
    public static Object unmarshall(String context, String schemaLocation, String content){
        Locale oldLocale = Locale.getDefault();
	Locale.setDefault(new Locale("en"));
        try{
            JAXBContext jc = JAXBContext.newInstance(
                    context);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaLocation));
            unmarshaller.setSchema(schema);

            Object obj = unmarshaller.unmarshal(new StringReader(content));
            Locale.setDefault(oldLocale);
            return obj;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        Locale.setDefault(oldLocale);
        return null;
    }
    
    public static Object unmarshallWithoutValidation(String context, String schemaLocation, String content){
        Locale oldLocale = Locale.getDefault();
        Locale.setDefault(new Locale("en"));
        try{
            JAXBContext jc = JAXBContext.newInstance(
                    context);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaLocation));
            Object obj = unmarshaller.unmarshal(new StringReader(content));
            Locale.setDefault(oldLocale);
            return obj;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        Locale.setDefault(oldLocale);
        return null;
    }
    
    public static void main(String args[]){
        try{
        String xmlString = "<RegistryResponse xmlns=\"urn:oasis:names:tc:ebxml-regrep:registry:xsd:2.1\" status=\"Success\"><Slot/></RegistryResponse>";
        org.w3c.dom.Document xmlDoc = XMLUtil.parseContent(xmlString.getBytes());
        }catch(Exception ex){ex.printStackTrace();}
    } 
    
}
