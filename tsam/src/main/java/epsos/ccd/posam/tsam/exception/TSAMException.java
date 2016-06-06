package epsos.ccd.posam.tsam.exception;

/**
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 */
public class TSAMException extends Exception {
	
	private TSAMError reason;
	private String ctx;
	
	public TSAMException(TSAMError reason) {
		this.reason = reason;
	}

	public TSAMException(TSAMError reason, String ctx) {
		this.reason = reason;
		this.ctx=ctx;
	}

	public TSAMError getReason() {
		return reason;
	}
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(reason.getCode());
		stringBuilder.append(": ");
		stringBuilder.append(reason.getDescription());
		if(ctx != null) {
			stringBuilder.append("[ ");
			stringBuilder.append(ctx);
			stringBuilder.append(" ]");
		}
		return stringBuilder.toString();
	}
}
