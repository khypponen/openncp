package epsos.ccd.gnomon.tsam.configuration;

/**
 * Implements an exception that should be thrown when a required system property is not defined.
 */
public class MissingSystemPropertyException extends RuntimeException {

	private static final long serialVersionUID = 4325630131818157535L;

	/**
	 * Default constructor.
	 * @param propertyName Name of the missing system property.
	 */
	public MissingSystemPropertyException(String propertyName) {
		super("Following system property is not defined: '" + propertyName + "'.");
	}
}
