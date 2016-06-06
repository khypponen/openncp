package epsos.ccd.posam.tsam.exception;

public interface ITMTSAMEror {
	/**
	 * 
	 * @return String - code
	 */
	public String getCode();


	/**
	 * 
	 * @return String - Description
	 */
	public String getDescription();

	@Override
	/**
	 * @return String in format code:description
	 */
	public String toString();
}
