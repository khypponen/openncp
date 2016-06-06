package epsos.ccd.posam.tm.testcases;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import junit.framework.TestCase;

import org.jaxen.NamespaceContext;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import epsos.ccd.posam.tm.service.impl.TransformationService;
import epsos.ccd.posam.tm.util.TMConstants;
import epsos.ccd.posam.tm.util.XmlUtil;

public class XmlTest extends TestCase{
	private String samplesDir="./src/test/resources/samples/";
	

	
	
	public void testNameSpace() {
		try {
			XMLReader reader= XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new DefaultHandler() {
				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {
					//System.out.println(uri+":"+localName);
				}
			});
			reader.parse(new InputSource(new FileReader(samplesDir + "unstructuredCDANS.xml")));
			//System.out.println();
			Document doc = XmlUtil.getDocument(new File(samplesDir + "unstructuredCDANS.xml"),false);
			printNode(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void printNode(Node n) {
		NodeList children=n.getChildNodes();
		for(int i=0;i<children.getLength();i++) {
			Node child=children.item(i);
			if(child.getNodeType()==Node.ELEMENT_NODE) {
				//System.out.println(child.getPrefix()+": "+child.getNodeName());
			}
			if(child.hasChildNodes()) {
				printNode(child);
			}
		}
	}
	public void testXpath() {
		Document doc = XmlUtil.getDocument(new File(samplesDir + "unstructuredCDANS.xml"),true);
		try {
			DOMXPath xpath=new DOMXPath("/my:ClinicalDocument/my:code");
			xpath.setNamespaceContext(new NamespaceContext() {
				
				public String translateNamespacePrefixToUri(String arg0) {
					return "urn:hl7-org:v3";
				}
			});
			List<Node> result=xpath.selectNodes(doc);
			assertTrue(result.size()>0);
			for (Node node : result) {
				System.out.println(node.getNamespaceURI()+": "+node.getNodeName());
			}
			//System.out.println();
			XPath jxpath = XPathFactory.newInstance().newXPath();
			jxpath.setNamespaceContext(new javax.xml.namespace.NamespaceContext() {
				
				public Iterator getPrefixes(String namespaceURI) {
					return null;
				}
				
				public String getPrefix(String namespaceURI) {
					//System.out.println("XmlTest.testXpath().new NamespaceContext() {...}.getPrefix()");
					return "my";
				}
				
				public String getNamespaceURI(String prefix) {
					//System.out.println("XmlTest.testXpath().new NamespaceContext() {...}.getNamespaceURI()");
					return "urn:hl7-org:v3";
				}
			});
			XPathExpression expr = jxpath.compile("/my:ClinicalDocument/my:code");

			NodeList nodeList = (NodeList)expr.evaluate(doc,
					XPathConstants.NODESET);
			assertTrue(nodeList.getLength() > 0);
			for(int i=0;i<nodeList.getLength();i++) {
				Node n=nodeList.item(i);
				System.out.println(n.getNamespaceURI()+": "+n.getNodeName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testNodePath() {
		Document doc = XmlUtil.getDocument(new File(samplesDir + "drda_L3.xml"),true);
		try {
			List<Node> list = XmlUtil.getNodeList(doc, TMConstants.XPATH_ALL_ELEMENTS_WITH_CODE_ATTR);
			for (Node node : list) {
				String elementPath = XmlUtil.getElementPath(node);
				System.out.println(elementPath);
				elementPath=elementPath.replaceAll("\\w+:", "");
				Node node2 = XmlUtil.getNode(doc, elementPath);
				assertNotNull(elementPath +" je null",node2);
				assertTrue(node.getLocalName().equals(node2.getLocalName()));
				assertTrue(XmlUtil.getNodeList(doc, elementPath).size()==1);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
}
