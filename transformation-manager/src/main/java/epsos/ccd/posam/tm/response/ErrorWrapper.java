package epsos.ccd.posam.tm.response;

import epsos.ccd.posam.tsam.exception.ITMTSAMEror;

public class ErrorWrapper {
	private ITMTSAMEror error;
	private String context;
	
	public ErrorWrapper(ITMTSAMEror error, String context) {
		this.error=error;
		this.context=context;
	}
	public String getContext() {
		return context;
	}
	public ITMTSAMEror getError() {
		return error;
	}
}
