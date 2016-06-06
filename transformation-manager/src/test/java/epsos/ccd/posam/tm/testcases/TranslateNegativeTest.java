package epsos.ccd.posam.tm.testcases;

import java.util.List;

import org.w3c.dom.Document;

import epsos.ccd.posam.tm.exception.TMError;
import epsos.ccd.posam.tm.response.TMResponseStructure;
import epsos.ccd.posam.tsam.exception.ITMTSAMEror;

/**
 * Negative test scenarios for method translate 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.4, 2010, 20 October
 */
public class TranslateNegativeTest extends TBase{

	public void testTranslateNull() {
		TMResponseStructure response = tmService.translate(null, null);
		assertNotNull(response);
		assertFalse(response.isStatusSuccess());

		List<ITMTSAMEror> errors = response.getErrors();
		assertNotNull(errors);
		assertTrue(errors.contains(TMError.ERROR_NULL_INPUT_DOCUMENT));		
	}
		
	public void testTranslateNotValidDoc() {
		Document notValidDocument = getInvalidDocument();
		assertNotNull(notValidDocument);
		
		TMResponseStructure response = tmService.translate(notValidDocument, null);
		assertNotNull(response);
		assertFalse(response.isStatusSuccess());
		
		List<ITMTSAMEror> errors =  response.getErrors();
		assertNotNull(errors);
	}	
}
