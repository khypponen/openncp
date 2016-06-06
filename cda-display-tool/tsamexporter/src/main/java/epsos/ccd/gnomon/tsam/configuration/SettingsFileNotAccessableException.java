package epsos.ccd.gnomon.tsam.configuration;

import java.io.IOException;

/**
 * This exception is implemented to handle any <code>IOException</code> thrown when trying to 
 * access a setting. This is done so that a try-catch is not needed every time the <code>Settings.getInstance()</code>
 * is called.
 */
public class SettingsFileNotAccessableException extends RuntimeException {

	private static final long serialVersionUID = -2036666419897884066L;
	
	/**
	 * Default constructor.
	 * @param msg Error message.
	 * @param cause The <code>IOException</code> object that is the cause of the exception.
	 */
	public SettingsFileNotAccessableException(String msg, IOException cause) {
		super(msg, cause);
	}
}
