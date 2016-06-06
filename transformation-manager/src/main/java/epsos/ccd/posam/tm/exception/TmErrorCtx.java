package epsos.ccd.posam.tm.exception;

import epsos.ccd.posam.tsam.exception.ITMTSAMEror;

public class TmErrorCtx implements ITMTSAMEror {

	/**
	 * Exception code
	 */
	private final String code;
	/**
	 * Exception description (issue - is English description/constant enough ?)
	 */
	private final String description;
	
	private String context;

	/**
	 * Default enum constructor
	 * 
	 * @param code
	 * @param descripton
	 */
	public TmErrorCtx(TMError error, String ctx) {
		this.code = error.getCode();
		this.description = error.getDescription();
		this.context=ctx;
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
		return description +" [" + context + "]";
	}

	@Override
	/**
	 * @return String in format code:description
	 */
	public String toString() {
		return code + ": " + getDescription();
	}

}
