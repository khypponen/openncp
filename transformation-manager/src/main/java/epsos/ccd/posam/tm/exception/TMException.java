package epsos.ccd.posam.tm.exception;

import epsos.ccd.posam.tsam.exception.ITMTSAMEror;

/**
 * Transformation Manager Exception class.<br>
 * Enables throwing TM specific Exception during processing.
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.4, 2010, 20 October
 */
public class TMException extends Exception {

	private ITMTSAMEror reason;

	public TMException(TMError reason) {
		this.reason = reason;
	}
	
	public TMException(TmErrorCtx reason) {
		this.reason = reason;
	}


	public ITMTSAMEror getReason() {
		return reason;
	}

	@Override
	public String toString() {
		return reason.getCode() + ": " + reason.getDescription();
	}
}
