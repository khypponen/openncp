package epsos.ccd.posam.tm.testcases;

import java.io.File;
import java.util.List;

import org.w3c.dom.Document;

import epsos.ccd.posam.tm.exception.TMError;
import epsos.ccd.posam.tm.response.TMResponseStructure;
import epsos.ccd.posam.tsam.exception.ITMTSAMEror;

/**
 * Negative test scenarios for method toEpsosPivot
 * 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.6, 2010, 20 October
 */
public class ToEpsosPivotNegativeTest extends TBase {

	public void testToEpSOSPivotNull() {
		TMResponseStructure response = tmService.toEpSOSPivot(null);
		assertNotNull(response);
		assertFalse(response.isStatusSuccess());

		List<ITMTSAMEror> errors = response.getErrors();
		assertNotNull(errors);
		assertTrue(errors.contains(TMError.ERROR_NULL_INPUT_DOCUMENT));
	}

	public void testToEpSOSPivotNotValidDoc() {
		Document notValidDocument = getInvalidDocument();
		assertNotNull(notValidDocument);

		TMResponseStructure response = tmService.toEpSOSPivot(notValidDocument);
		assertNotNull(response);
	}
	
	public void testToEpSOSPivotRequiredCENotTranscoded() {
		Document validDocument = getDocument(new File(samplesDir + "validCDA2.xml"));
		assertNotNull(validDocument);

		TMResponseStructure response = tmService.toEpSOSPivot(validDocument);
		assertNotNull(response);
		assertFalse(response.isStatusSuccess());

		List<ITMTSAMEror> errors = response.getErrors();
		assertNotNull(errors);
		assertTrue(containsError(errors,TMError.ERROR_REQUIRED_CODED_ELEMENT_NOT_TRANSCODED));
	}	
}
