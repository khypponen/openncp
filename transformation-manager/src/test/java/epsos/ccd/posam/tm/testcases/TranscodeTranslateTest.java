package epsos.ccd.posam.tm.testcases;

import java.io.File;
import java.io.FileWriter;

import org.w3c.dom.Document;

import epsos.ccd.posam.tm.response.TMResponseStructure;
import epsos.ccd.posam.tm.util.CodedElementList;
import epsos.ccd.posam.tm.util.XmlUtil;

/**
 * Test for transcode and translate as one process
 * 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.3, 2010, 20 October
 */
public class TranscodeTranslateTest extends TBase {

	public void testCEL() {
		Document validDocument = getDocument(new File(samplesDir+"drda_L3.xml"));		
		assertNotNull(validDocument);

		TMResponseStructure response = tmService.toEpSOSPivot(validDocument);
	}
	
	public void test() {
		//Document validDocument = getDocument(new File(samplesDir + "HuberMelanie_PS_L3_valide_Vers.xml"));
		Document validDocument = getDocument();		
		assertNotNull(validDocument);

		TMResponseStructure response = tmService.toEpSOSPivot(validDocument);

		assertNotNull(response);
		assertTrue(response.isStatusSuccess());
		assertNotNull(response.getErrors());		
		assertTrue(response.getErrors().isEmpty());
		
		Document transcodedDoc = response.getResponseCDA();
		assertNotNull(transcodedDoc);
		
		response = tmService.translate(transcodedDoc, "sk-SK");
		//response = service.translate(transcodedDoc, null);		
		assertNotNull(response);
		assertTrue(response.isStatusSuccess());
		assertNotNull(response.getErrors());		
		assertTrue(response.getErrors().isEmpty());		
	}
	
	public void testSchematronBeforeAndAfter() {
		String resources = "src/test/resources/schematron/";
		String[] files= {"epSOS_RTD_eP_SK_Friendly_CDA_L3_001.xml","epSOS_PS_IT_Friendly_CDA_ST_NTSDNN80A19D612X_001.xml","epSOS_RTD_eD_EE_Friendly_CDA_L3_000.xml"};
		for (String file : files) {
			Document doc=XmlUtil.getDocument(new File(resources+file), true);
			TMResponseStructure result=tmService.toEpSOSPivot(doc);
			try {
				FileWriter fw=new FileWriter("pivot_"+file);
				fw.write(XmlUtil.xmlToString(result.getDocument()));
				fw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assertNotNull(result);
			System.out.println(result.getErrors().size());
		}
	}
}
