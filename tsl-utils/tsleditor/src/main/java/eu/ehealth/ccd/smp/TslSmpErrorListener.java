package eu.ehealth.ccd.smp;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * From the Javadoc:
 * If an application does not register its own custom ErrorListener, the default ErrorListener is used which reports all warnings and errors to System.err 
 * and does not throw any Exceptions. Applications are strongly encouraged to register and use ErrorListeners that insure proper behavior for warnings and errors.
 * 
 * @author joao.cunha
 *
 */
public class TslSmpErrorListener implements ErrorListener {
	private static final Logger logger = LoggerFactory.getLogger(TslSmpErrorListener.class);

	@Override
	public void error(TransformerException exception) throws TransformerException {
		// TODO Auto-generated method stub
		logger.error(GenericUtils.printExceptionStackTrace(exception));

	}

	@Override
	public void fatalError(TransformerException exception)
			throws TransformerException {
		// TODO Auto-generated method stub
		logger.error(GenericUtils.printExceptionStackTrace(exception));

	}

	@Override
	public void warning(TransformerException exception) throws TransformerException {
		// TODO Auto-generated method stub
		logger.warn(GenericUtils.printExceptionStackTrace(exception));
	}

}
