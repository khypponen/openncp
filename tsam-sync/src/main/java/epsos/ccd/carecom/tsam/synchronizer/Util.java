package epsos.ccd.carecom.tsam.synchronizer;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Implements a collection of utility static methods, that are used across the implementation.
 */
public final class Util {
	
	/**
	 * Attempts to convert a {@link Date} object to a {@link XMLGregorianCalendar} object. If the attempt fails an {@link UnableToConvertException}
	 * exception is thrown.
	 * @param date The date to convert.
	 * @return The converted object
	 * @throws UnableToConvertException Thrown if the conversion fails.
	 */
	public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) throws UnableToConvertException {
		if (date == null ) {
			return null;
		}
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new UnableToConvertException(date.toString(), 
					Date.class.getName(), 
					XMLGregorianCalendar.class.getName(), 
					e);
		}
		if (datatypeFactory != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			return datatypeFactory.newXMLGregorianCalendar(cal);
		}
		return null;
	}
}
