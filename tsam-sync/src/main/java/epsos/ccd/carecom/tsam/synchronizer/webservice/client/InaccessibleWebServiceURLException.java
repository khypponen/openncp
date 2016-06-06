package epsos.ccd.carecom.tsam.synchronizer.webservice.client;

import java.net.URL;
/**
 * This class implements a simple runtime exception that is thrown in the case where the URL used to access the
 * web service is not correct.
 */
public class InaccessibleWebServiceURLException extends RuntimeException {
	private static final long serialVersionUID = 3302557917852143785L;

	private final static String EXCEPTION_TEXT = "The web service could not be reached at the specified URL: %s";
	
	/**
	 * Default constructor that requires both the incorrect URL and the inner exception causing this exception to 
	 * be thrown.
	 * @param serviceURL The incorrect URL of the web service.
	 * @param ex The exception that is the cause of this exception.
	 */
	public InaccessibleWebServiceURLException(URL serviceURL, Throwable ex) {
		super(String.format(EXCEPTION_TEXT, serviceURL), ex);
	}
}