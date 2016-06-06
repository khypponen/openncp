package epsos.ccd.posam.tm.testcases;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;

import epsos.ccd.posam.tm.exception.TMException;
import epsos.ccd.posam.tm.service.impl.TransformationService;
import epsos.ccd.posam.tm.util.CodedElementList;
import epsos.ccd.posam.tm.util.CodedElementListItem;
import epsos.ccd.posam.tm.util.TMConfiguration;
import epsos.ccd.posam.tm.util.TMConstants;

/**
 * Test scenarios for Spring configurable CodedElementList and TMConfiguration
 * 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.5, 2010, 20 October
 */
public class ConfigurationTest extends TBase {

	public void testCodedElementList() {
		CodedElementList codedElementList = CodedElementList.getInstance();
		assertTrue(codedElementList.isConfigurableElementIdentification());

		Collection<CodedElementListItem> patientSummaryL3 = codedElementList
				.getPatientSummaryl3();
		assertNotNull(patientSummaryL3);
		assertFalse(patientSummaryL3.isEmpty());
		
		Collection<CodedElementListItem> patientSummaryL3b = codedElementList.getList(TMConstants.PATIENT_SUMMARY3);
		assertNotNull(patientSummaryL3b);
		assertFalse(patientSummaryL3b.isEmpty());

		Collection<CodedElementListItem> patientSummaryL1 = codedElementList
				.getPatientSummaryl1();
		assertNotNull(patientSummaryL1);
		assertFalse(patientSummaryL1.isEmpty());
		
		Collection<CodedElementListItem> patientSummaryL1b = codedElementList.getList(TMConstants.PATIENT_SUMMARY1);
		assertNotNull(patientSummaryL1b);
		assertFalse(patientSummaryL1b.isEmpty());
		
		Collection<CodedElementListItem> ePrescriptionL3 = codedElementList
				.getePrescriptionl3();
		assertNotNull(ePrescriptionL3);
		assertFalse(ePrescriptionL3.isEmpty());
		
		Collection<CodedElementListItem> ePrescriptionL3b = codedElementList.getList(TMConstants.EPRESCRIPTION3);
		assertNotNull(ePrescriptionL3b);
		assertFalse(ePrescriptionL3b.isEmpty());

		Collection<CodedElementListItem> ePrescriptionL1 = codedElementList
				.getePrescriptionl1();
		assertNotNull(ePrescriptionL1);
		assertFalse(ePrescriptionL1.isEmpty());
		
		Collection<CodedElementListItem> ePrescriptionL1b = codedElementList.getList(TMConstants.EPRESCRIPTION1);
		assertNotNull(ePrescriptionL1b);
		assertFalse(ePrescriptionL1b.isEmpty());

		Collection<CodedElementListItem> eDispensationL3 = codedElementList
				.geteDispensationl3();
		assertNotNull(eDispensationL3);
		assertFalse(eDispensationL3.isEmpty());
		
		Collection<CodedElementListItem> eDispensationL3b = codedElementList.getList(TMConstants.EDISPENSATION3);
		assertNotNull(eDispensationL3b);
		assertFalse(eDispensationL3b.isEmpty());

		Collection<CodedElementListItem> eDispensationL1 = codedElementList
				.geteDispensationl1();
		assertNotNull(eDispensationL1);
		assertFalse(eDispensationL1.isEmpty());

		Collection<CodedElementListItem> eDispensationL1b = codedElementList.getList(TMConstants.EDISPENSATION1);
		assertNotNull(eDispensationL1b);
		assertFalse(eDispensationL1b.isEmpty());
		
		Collection<CodedElementListItem> hcerDocL3 = codedElementList.getList(TMConstants.HCER3);
		assertNotNull(hcerDocL3);
		assertFalse(hcerDocL3.isEmpty());

		Collection<CodedElementListItem> hcerDocL1 = codedElementList.getList(TMConstants.HCER1);
		assertNotNull(hcerDocL1);
		assertFalse(hcerDocL1.isEmpty());

		Collection<CodedElementListItem> mroDocL3 = codedElementList.getList(TMConstants.MRO3);
		assertNotNull(mroDocL3);
		assertFalse(mroDocL3.isEmpty());

		Collection<CodedElementListItem> mroDocL1 = codedElementList.getList(TMConstants.MRO1);
		assertNotNull(mroDocL1);
		assertFalse(mroDocL1.isEmpty());
		
		
		Collection<CodedElementListItem> allCodedElementItems = new ArrayList<CodedElementListItem>();
		allCodedElementItems.addAll(patientSummaryL1);
		allCodedElementItems.addAll(patientSummaryL3);
		allCodedElementItems.addAll(ePrescriptionL1);
		allCodedElementItems.addAll(ePrescriptionL3);
		allCodedElementItems.addAll(eDispensationL1);
		allCodedElementItems.addAll(eDispensationL3);
		allCodedElementItems.addAll(hcerDocL1);
		allCodedElementItems.addAll(hcerDocL3);
		allCodedElementItems.addAll(mroDocL1);
		allCodedElementItems.addAll(mroDocL3);

		// elementPath & usage is MANDATORY in all coded element items
		Iterator<CodedElementListItem> it = allCodedElementItems.iterator();
		while (it.hasNext()) {
			CodedElementListItem item = (CodedElementListItem) it.next();
			assertNotNull(item.getxPath());
			assertFalse(item.getxPath().length() == 0);

			assertNotNull(item.getUsage());
			assertFalse(item.getUsage().length() == 0);
		}
	}
	
	public void testDocumentTypeRecognition() {
		Document hcerL1 = getDocument(new File("src/test/resources/samples/hcerL1-CDA-pdf.xml"));
		Document hcerL3 = getDocument(new File("src/test/resources/samples/hcerL3-CDA.xml"));
		Document mroL1 = getDocument(new File("src/test/resources/samples/mroL1-CDA-pdf.xml"));
		Document mroL3 = getDocument(new File("src/test/resources/samples/mroL3-CDA.xml"));
		TransformationService tm=(TransformationService) tmService;
		try {
			assertEquals(tm.getCDADocumentType(hcerL1),TMConstants.HCER1);
			assertEquals(tm.getCDADocumentType(hcerL3),TMConstants.HCER3);
			assertEquals(tm.getCDADocumentType(mroL1),TMConstants.MRO1);
			assertEquals(tm.getCDADocumentType(mroL3),TMConstants.MRO3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testTMConfiguration() {
		TMConfiguration config = TMConfiguration.getInstance();
		assertNotNull(config);

		assertNotNull(config.geteDispensationCode());
		assertNotNull(config.getePrescriptionCode());
		assertNotNull(config.getPatientSummaryCode());
		assertNotNull(config.getPatientSummarySchematronFriendlyPath());
		assertNotNull(config.geteDispensationSchematronFriendlyPath());
		assertNotNull(config.getePrescriptionSchematronFriendlyPath());
		assertNotNull(config.getPatientSummarySchematronPivotPath());
		assertNotNull(config.geteDispensationSchematronPivotPath());
		assertNotNull(config.getePrescriptionSchematronPivotPath());
		assertNotNull(config.getSchemaFilePath());

		assertFalse(config.geteDispensationCode().length() == 0);
		assertFalse(config.getePrescriptionCode().length() == 0);
		assertFalse(config.getPatientSummaryCode().length() == 0);
		assertFalse(config.getPatientSummarySchematronFriendlyPath().length() == 0);
		assertFalse(config.geteDispensationSchematronFriendlyPath().length() == 0);
		assertFalse(config.getePrescriptionSchematronFriendlyPath().length() == 0);
		assertFalse(config.getPatientSummarySchematronPivotPath().length() == 0);
		assertFalse(config.geteDispensationSchematronPivotPath().length() == 0);
		assertFalse(config.getePrescriptionSchematronPivotPath().length() == 0);
		assertFalse(config.getSchemaFilePath().length() == 0);

		assertEquals("60591-5", config.getPatientSummaryCode());
		assertEquals("57833-6", config.getePrescriptionCode());
		assertEquals("60593-1", config.geteDispensationCode());
		
		assertNotNull(config.getDocTypesCollection());
		assertFalse(config.getDocTypesCollection().isEmpty());
		assertTrue(config.getDocTypesCollection().size() == 3);
		assertTrue(config.getDocTypesCollection().contains("60591-5"));
		assertTrue(config.getDocTypesCollection().contains("57833-6"));
		assertTrue(config.getDocTypesCollection().contains("60593-1"));		
	}
}
