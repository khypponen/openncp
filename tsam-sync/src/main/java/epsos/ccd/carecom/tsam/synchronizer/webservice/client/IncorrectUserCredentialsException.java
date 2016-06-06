package epsos.ccd.carecom.tsam.synchronizer.webservice.client;

/**
 * This class implements a simple exception that is thrown when ever a web method is called with incorrect user
 * credentials. 
 */
public class IncorrectUserCredentialsException extends RuntimeException {

	private static final long serialVersionUID = -1781827435601643752L;

	/**
	 * Default constructor.
	 * @param methodName The name of the web method in which the exception is thrown.
	 * @param cause The exception causing this exception to be thrown.
	 */
	public IncorrectUserCredentialsException(String methodName, Throwable cause) {
		super("Incorrect user credentials used when calling web method: " + methodName, cause);
	}
}
