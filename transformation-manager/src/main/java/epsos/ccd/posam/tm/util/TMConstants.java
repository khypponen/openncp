package epsos.ccd.posam.tm.util;

/**
 * Constants for TM module
 * 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.10, 2010, 20 October 
 */
public interface TMConstants {
	public static final String NEWLINE = System.getProperty("line.separator");
	
	//transcoding & translation
	public static final String TRANSLATION = "translation";
	public static final String CODE = "code";
	public static final String CODE_SYSTEM = "codeSystem";
	public static final String CODE_SYSTEM_NAME = "codeSystemName";	
	public static final String CODE_SYSTEM_VERSION = "codeSystemVersion";
	public static final String DISPLAY_NAME = "displayName";
	public static final String ORIGINAL_TEXT = "originalText";
	public static final String REFERENCE = "reference";
	
	//responseStructure
	public static final String RESPONSE_ELEMENT = "responseElement";
	public static final String RESPONSE_STATUS = "responseStatus";
	public static final String STATUS = "status";
	public static final String RESULT = "result";
	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAILURE = "failure";
	public static final String ERRORS = "errors";
	public static final String ERROR = "error";
	public static final String WARNINGS = "warnings";
	public static final String WARNING = "warning";
	public static final String DESCRIPTION = "description";
	
	public static final String DOCUMENT = "Document:";
	public static final String COLON = ":";	
	
	//unstructured CDA
	public static final String COMPONENT = "component";
	public static final String NON_XML_BODY = "nonXMLBody";
	public static final String TEXT = "text";
	public static final String MEDIA_TYPE = "mediaType";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String REPRESENTATION = "representation";
	public static final String B64 = "B64";
	
	//Posam test version comment
	public static final String POSAM_COMMENT = "This CDA document was processed (transcoded/translated) by test version of PosAm Transformation Manager component.";
	
	//Coded Element List
	public static final String  CODED_ELEMENT = "codedElement";
	public static final String  ELEMENT_PATH = "elementPath";
	public static final String  USAGE = "usage";
	public static final String  VALUE_SET = "valueSet";
	public static final String  VALUE_SET_VERSION = "valueSetVersion";
	public static final String  TARGET_LANGUAGE_CODE = "targetLanguageCode";	
	//Document types	
	public static final String  PATIENT_SUMMARY3 = "patientSummaryCDAl3";
	public static final String  PATIENT_SUMMARY1 = "patientSummaryCDAl1pdf";
	public static final String  EPRESCRIPTION3 = "ePrescriptionCDAl3";
	public static final String  EPRESCRIPTION1 = "ePrescriptionCDAl1pdf";
	public static final String  EDISPENSATION3 = "eDispensationCDAl3";
	public static final String  EDISPENSATION1 = "eDispensationCDAl1pdf";
	public static final String  HCER3 = "HCERDocCDAl3";
	public static final String  HCER1= "HCERDocCDAl1pdf";
	public static final String  MRO3 = "MRODocCDAl3";
	public static final String  MRO1= "MRODocCDAl1pdf";
	public static final String  SCANNED1= "scannedDocCDAl1pdf";
	public static final String  SCANNED3= "scannedCDAl1pdf";
		
	//Usage/Optionality of Coded Elements
	public static final String  R = "R";
	public static final String  RNFA = "RNFA";
	public static final String  O = "O";
	public static final String  NA = "NA";
	
	//Reference values
	public static final String VALUE = "value";
	public static final String HASH = "#";
	public static final String ID = "ID";		
	
	//XPaths called from code
	public static final String XPATH_CLINICALDOCUMENT_CODE = "/ClinicalDocument/code";
	public static final String XPATH_STRUCTUREDBODY = "/ClinicalDocument/component/structuredBody";
	public static final String XPATH_NONXMLBODY = "/ClinicalDocument/component/nonXMLBody";
	public static final String XPATH_ALL_ELEMENTS_WITH_CODE_ATTR = "//*[@code and @codeSystem]";
	public static final String XPATH_ORIGINAL_TEXT_REFERENCE_VALUE = "originalText/reference[@value]";
	public static final String XPATH_ALL_ELEMENTS_WITH_ID_ATTR = "//*/*[@ID]";
	public static final String XPATH_TRANSLATION = "translation";
	
	//XmlUtil Constants
	public static final String  EMPTY_XMLNS = "xmlns=\"\"";
	public static final String  EMPTY_STRING = "";
	
	//xsi:type check
	public static final String  XSI_TYPE = "xsi:type";
	public static final String  CE = "CE";
	public static final String  CD = "CD";		
	
	//other (log, ...)
	public static final String  TRANSCODING = "transcoding";	
}
