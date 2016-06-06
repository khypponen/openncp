package epsos.ccd.carecom.tsam.synchronizer;


/**
 * This class implements a simple exception that is thrown when a conversion between two different types cannot
 * be performed for some reason.
 */
public class UnableToConvertException extends RuntimeException {

	private static final long serialVersionUID = 6912771792674949706L;

	/**
	 * Default constructor.
	 * @param value The string value of the type that could not be converted.
	 * @param sourceType The type of the source object.
	 * @param targetType The type of the target object.
	 * @param cause The original exception causing this exception to be thrown.
	 */
	public UnableToConvertException(String value, String sourceType, String targetType, Throwable cause) {
		super(String.format("Could not convert object of type %s with value %s to following type %s", sourceType, value, targetType), cause);
	}

}
