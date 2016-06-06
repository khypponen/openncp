package epsos.ccd.posam.tm.testcases;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import epsos.ccd.posam.tm.response.TMResponseStructure;

/**
 * Positive test scenarios for method translate 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.7, 2010, 20 October
 */
public class TranslatePositiveTest extends TBase {
	
	public void testTranslatePatientSummaryL3() {
			// valid document - PatientSummary
			Document document = getDocument(new File(
					samplesDir+ "PS_Katzlmacher.xml"));
					//samplesDir+ "transcodedCDA2.xml"));					
			assertNotNull(document);

			TMResponseStructure response = tmService.translate(document, "sk-SK");
			//TMResponseStructure response = service.translate(document, "fr");			
			try {
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(new DOMSource(response.getDocument()), new StreamResult(new FileOutputStream("tmresult.xml")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			assertNotNull(response);
			assertTrue(response.isStatusSuccess());
			assertNotNull(response.getErrors());
			assertTrue(response.getErrors().isEmpty());			
	}
}
