package epsos.ccd.posam.tm.testcases;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import epsos.ccd.posam.tm.util.Validator;
import epsos.ccd.posam.tm.util.XmlUtil;



/**
 * Test scenarios for XPath based selection from CDA documents
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.6, 2010, 20 October
 */
public class CDAXPathTest extends TestCase {
	
	protected String samplesDir = ".//src//test//resources//samples//";
	
	@Override
	protected void setUp() throws Exception {
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("ctx_tm_test.xml");		
	}
	
	
	public void testNameSpaceAwarness() {
		Document doc = XmlUtil.getDocument(new File(samplesDir + "validCDA.xml"), true);
		
		Document namespaceAwareDoc = XmlUtil.getNamespaceAwareDocument(doc);
		try {
			Validator.validateToSchema(namespaceAwareDoc);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		Document namespaceNotAwareDoc = XmlUtil.getNamespaceNOTAwareDocument(doc);
		try {
			Validator.validateToSchema(namespaceNotAwareDoc);
		} catch (Exception e) {			
		}		
	}

	public void testXPath() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		Document doc = XmlUtil.getDocument(new File(samplesDir + "validCDA.xml"), false);		
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("/ClinicalDocument/code");

		NodeList nodeList = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		assertNotNull(nodeList);
		
		//exactly 1 document type element should exist
		assertTrue(nodeList.getLength() == 1);
		Element docTypeCode = (Element) nodeList.item(0);
		assertNotNull(docTypeCode);
		
		String docTypeCodeValue = docTypeCode.getAttribute("code");
		assertNotNull(docTypeCodeValue);
		assertFalse(docTypeCodeValue.length() == 0);
		assertEquals("60591-5", docTypeCodeValue);
		
		//"entry" check
		
		/*
		 * entry/act[templateId/@root='2.16.840.1.113883.10.20.1.27']/entryRelationship[@typeCode='SUBJ']/observation[templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.4.6']/code
		 */
		String entryXPathExpression = "//entry/act[templateId/@root='2.16.840.1.113883.10.20.1.27']/entryRelationship[@typeCode='SUBJ']/observation[templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.4.6']/code";
		XPath xpath2 = XPathFactory.newInstance().newXPath();
		XPathExpression expr2 = xpath2.compile(entryXPathExpression);
		
		NodeList nodeList2 = (NodeList)expr2.evaluate(doc, XPathConstants.NODESET);
		assertNotNull(nodeList2);
		assertTrue(nodeList2.getLength() == 1);
		Element element = (Element) nodeList2.item(0);
		assertNotNull(element);
		//System.out.println(XmlUtil.xmlToString(element));
	}
}
