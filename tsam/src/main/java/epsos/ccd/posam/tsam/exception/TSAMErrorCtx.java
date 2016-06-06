package epsos.ccd.posam.tsam.exception;

public class TSAMErrorCtx implements ITMTSAMEror{
	
	public TSAMErrorCtx(TSAMError err, String ctx) {
		this.code=err.getCode();
		this.description=err.getDescription();
		this.context=ctx;
	}
	/**
	 * Exception code
	 */
	private String code;
	/**
	 * Exception description (issue - is English description/constant enough ?)
	 */
	private String description;
	
	private String context;
	
	public String getCode() {
		return code;
	}
	public String getDescription() {
		return description +" [" + context + "]";
	}
	
	public String getContext() {
		return context;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TSAMErrorCtx other = (TSAMErrorCtx) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return code + ": " + description +" [" + context + "]";
	}
}
