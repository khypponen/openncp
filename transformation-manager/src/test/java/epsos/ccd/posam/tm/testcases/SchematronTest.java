package epsos.ccd.posam.tm.testcases;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;

import epsos.ccd.posam.tm.service.impl.TransformationService;
import epsos.ccd.posam.tm.util.SchematronResult;
import epsos.ccd.posam.tm.util.SchematronValidator;
import epsos.ccd.posam.tm.util.TMConfiguration;
import epsos.ccd.posam.tm.util.TMConstants;
import epsos.ccd.posam.tm.util.Validator;
import epsos.ccd.posam.tm.util.XmlUtil;

/**
 * Test scenarios for schematron validation
 * 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.5, 2010, 20 October
 */
public class SchematronTest extends TBase {
	static final String resources = "src/test/resources/samples/schPassed/";
	
	/**
	 * Tests all reference documents against schematron. Document type is
	 * determined based on it's content (document code and body type).
	 * Friendly or pivot type is determined based on file name convention,
	 * if name contains 'friendly' it's friendly.
	 * 
	 */
	public void testSchematronAll() {
		TransformationService tm=(TransformationService) tmService;
		try {
			File[] docs = new File(resources).listFiles();
			Document xmlDoc;
			boolean friendly;
			boolean allOK=true;
			StringBuilder sb=new StringBuilder();
			for (File doc : docs) {

				if(doc.getName().contains("friendly")){
					friendly=true;
				} else {
					friendly=false;
				}
				
				xmlDoc = getDocument(doc);
				SchematronResult result = Validator.validateSchematron(xmlDoc, tm.getCDADocumentType(xmlDoc), friendly);
				if(!result.isValid()) {
					System.out.println(result);
					allOK=false;
					
				}
				sb.append(doc.getName()+": "+result.getErrors().getLength()+"\n");
			}
			System.out.println(sb);

			assertTrue(allOK);
			

		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
