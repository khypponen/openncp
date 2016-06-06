package epsos.ccd.posam.tm.testcases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import epsos.ccd.posam.tm.util.CodedElementList;
import epsos.ccd.posam.tm.util.CodedElementListItem;


/**
 * Test scenarios for Coded Element List 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.0, 2010, 11 August
 */
public class CodedElementListTest extends TBase {


	
	public void testCodedElementList() {
		CodedElementList codedElementList = CodedElementList.getInstance();
		assertTrue(codedElementList.isConfigurableElementIdentification());
		
		Collection<CodedElementListItem> patientSummaryL3 = codedElementList.getPatientSummaryl3();
		assertNotNull(patientSummaryL3);
		assertFalse(patientSummaryL3.isEmpty());
		
		Collection<CodedElementListItem> patientSummaryL1 = codedElementList.getPatientSummaryl1();
		assertNotNull(patientSummaryL1);
		assertFalse(patientSummaryL1.isEmpty());
		
		Collection<CodedElementListItem> ePrescriptionL3 = codedElementList.getePrescriptionl3();
		assertNotNull(ePrescriptionL3);
		assertFalse(ePrescriptionL3.isEmpty());
		
		Collection<CodedElementListItem> ePrescriptionL1 = codedElementList.getePrescriptionl1();
		assertNotNull(ePrescriptionL1);
		assertFalse(ePrescriptionL1.isEmpty());
		
		Collection<CodedElementListItem> eDispensationL3 = codedElementList.geteDispensationl3();
		assertNotNull(eDispensationL3);
		assertFalse(eDispensationL3.isEmpty());
		
		Collection<CodedElementListItem> eDispensationL1 = codedElementList.geteDispensationl1();
		assertNotNull(eDispensationL1);
		assertFalse(eDispensationL1.isEmpty());
		
		Collection<CodedElementListItem> allCodedElementItems = new ArrayList<CodedElementListItem>();
		allCodedElementItems.addAll(patientSummaryL1);
		allCodedElementItems.addAll(patientSummaryL3);
		allCodedElementItems.addAll(ePrescriptionL1);
		allCodedElementItems.addAll(ePrescriptionL3);
		allCodedElementItems.addAll(eDispensationL1);
		allCodedElementItems.addAll(eDispensationL3);
		
		//elementPath & usage is MANDATORY in all coded element items
		Iterator<CodedElementListItem>  it= allCodedElementItems.iterator();
		while (it.hasNext()) {
			CodedElementListItem item = (CodedElementListItem) it.next();
			assertNotNull(item.getxPath());
			assertFalse(item.getxPath().isEmpty());
			
			assertNotNull(item.getUsage());			
			assertFalse(item.getUsage().isEmpty());			
		}
	}
}
