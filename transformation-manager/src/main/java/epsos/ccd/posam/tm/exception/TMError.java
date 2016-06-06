package epsos.ccd.posam.tm.exception;

import epsos.ccd.posam.tsam.exception.ITMTSAMEror;

/**
 * Enumeration for TM Errors & Warnings codes. Provides get/set methods for code
 * and description
 * 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.8, 2010, 20 October
 */
public enum TMError implements ITMTSAMEror {
	// ERRORS
	/**
	 * Error; Software processing error (e.g. "out of memory" )<br>
	 * code, description:<br>
	 * "4500","Processing error (sw failure)"
	 */
	ERROR_PROCESSING_ERROR("4500", "Processing error (sw failure)"),
	/**
	 * Error; Input CDA document is null <br>
	 * code, description:<br>
	 * "4501","Input document is null"
	 */
	ERROR_NULL_INPUT_DOCUMENT("4501", "Input document is null"),
	/**
	 * Error; List of item(s) missing or wrong <br>
	 * code, description:<br>
	 * "4502","Not conformant input CDA Header"
	 */
	ERROR_NOT_CONFORMANT_CDA_HEADER("4502", "Not conformant input CDA Header"),
	/**
	 * Error; Input object is not valid pdf file <br>
	 * code, description:<br>
	 * "4503","Not conformant input object"
	 */
	ERROR_NOT_CONFORMANT_INPUT_OBJECT("4503", "Not conformant input object"),

	/**
	 * Error; required coded element was not transcoded <br>
	 * code, description:<br>
	 * "4505","Required coded element was not transcoded"
	 */
	ERROR_REQUIRED_CODED_ELEMENT_NOT_TRANSCODED("4505",
			"Required coded element was not transcoded"),
	/**
	 * Error; Processing error (sw failure); failing coded element + Software
	 * processing error (e.g. "out of memory" ) <br>
	 * code, description:<br>
	 * 4506","Header coded processing failure"
	 */
	ERROR_HEADER_CODED_ELEMENT_PROCESSING_FAILURE("4506",
			"Header coded processing failure"),
	/**
	 * Error; List of item(s) missing or wrong <br>
	 * code, description:<br>
	 * "4507","Not conformant input CDA"
	 */
	ERROR_NOT_CONFORMANT_INPUT_CDA("4507", "Not conformant input CDA"),
	/**
	 * Error; Input pdf file is null <br>
	 * code, description:<br>
	 * "4508","Input pdf file is null"
	 */
	ERROR_NULL_INPUT_PDF_FILE("4508", "Input pdf file is null"),
	/**
	 * Error; CDA Document code missing <br>
	 * code, description:<br>
	 * "4509","CDA Document code missing"
	 */
	ERROR_DOCUMENT_CODE_NOT_EXIST("4509", "CDA Document code missing"),
	/**
	 * Error; Unknown CDA Document code<br>
	 * code, description:<br>
	 * "4510","Unknown CDA Document code"
	 */
	ERROR_DOCUMENT_CODE_UNKNOWN("4510", "Unknown CDA Document code"),
	/**
	 * Error; Required coded element missing<br>
	 * code, description:<br>
	 * "4511","Required coded element missing"
	 */
	ERROR_REQUIRED_CODED_ELEMENT_MISSING("4511",
			"required coded element missing"),
	/**
	 * Error; Required coded element was not translated<br>
	 * code, description:<br>
	 * "4512","Required coded element was not translated"
	 */
	ERROR_REQUIRED_CODED_ELEMENT_NOT_TRANSLATED("4512",
			"Required coded element was not translated"),

	// WARNINGS
	WARNING_INPUT_XSD_VALIDATION_FAILED("2401", "XSD validation of input document failed"), 
	
	WARNING_INPUT_SCHEMATRON_VALIDATION_FAILED("2402", "Schematron validation of input document failed"), 
			
	WARNING_INPUT_MDA_VALIDATION_FAILED("2403", "MDA validation of input document failed"),

	WARNING_OUTPUT_XSD_VALIDATION_FAILED("2404", "XSD validation of output document failed"), 
	
	WARNING_OUTPUT_SCHEMATRON_VALIDATION_FAILED("2405", "Schematron validation of output document failed"), 
			
	WARNING_OUTPUT_MDA_VALIDATION_FAILED("2406", "MDA validation of output document failed"),
	/**
	 * Warning; Header coded element transcoding failure <br>
	 * code, description:<br>
	 * "2500","Header coded element transcoding failure"
	 */
	WARNING_HEADER_CODED_ELEMENT_NOT_TRANSCODED("2500",
			"Header coded element transcoding failure"),
	/**
	 * Warning; Parent document not correctly referenced <br>
	 * code, description:<br>
	 * "2501","Parent document not correctly referenced"
	 */
	WARNING_PARENT_DOCUMENT_NOT_CORRECTLY_REFERENCED("2501",
			"Parent document not correctly referenced"),
	/**
	 * Warning; Coded element transcoding failure; Unknown input Code System
	 * Version; Input concept <br>
	 * code, description:<br>
	 * "2502","Coded element transcoding failure"
	 */
	WARNING_CODED_ELEMENT_TRANSCODING_FAILURE("2502",
			"Coded element transcoding failure"),
	/**
	 * Warning; Processing error (sw failure); failing coded element + Software
	 * processing error (e.g. "out of memory" ) <br>
	 * code, description:<br>
	 * "2503","Coded element processing failure"
	 */
	WARNING_CODED_ELEMENT_PROCESSING_FAILURE("2503",
			"Coded element processing failure"),
	/**
	 * Warning; Not defined target language designation, Input concept <br>
	 * code, description:<br>
	 * "2504","Coded element translation failure"
	 */
	WARNING_CODED_ELEMENT_TRANSLATION_FAILURE("2504",
			"Coded element translation failure"),
	/**
	 * Warning; Input Target Language <br>
	 * code, description:<br>
	 * "2505","Unknown Target Language"
	 */
	WARNING_UNKNOWN_TARGET_LANGUAGE("2505", "Unknown Target Language"),
	/**
	 * Warning; Configuration Coded Element List is Empty.<br>
	 * code, description:<br>
	 * "2506","Configuration Coded Element List is Empty."
	 */
	WARNING_CODED_ELEMENT_LIST_EMPTY("2506",
			"Configuration Coded Element List is Empty"),
	/**
	 * Warning; Element does not have a proper type.<br>
	 * code, description:<br>
	 * "2507","Element does not have a proper type."
	 */
	WARNING_CODED_ELEMENT_NOT_PROPER_TYPE("2507",
			"Element does not have a proper type"),

	WARNING_MANDATORY_ATTRIBUTES_MISSING("2508",
			"Some mandatory attributes (code, codeSystem) are missing"),

	;

	/**
	 * Exception code
	 */
	private final String code;
	/**
	 * Exception description (issue - is English description/constant enough ?)
	 */
	private final String description;

	/**
	 * Default enum constructor
	 * 
	 * @param code
	 * @param descripton
	 */
	private TMError(String code, String descripton) {
		this.code = code;
		this.description = descripton;
	}

	/**
	 * 
	 * @return String - code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * @return String - Description
	 */
	public String getDescription() {
		return description;
	}

	@Override
	/**
	 * @return String in format code:description
	 */
	public String toString() {
		return code + ": " + description;
	}
}
