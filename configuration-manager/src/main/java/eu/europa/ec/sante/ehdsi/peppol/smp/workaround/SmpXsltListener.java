/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.sante.ehdsi.peppol.smp.workaround;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgoncalves
 * 
 *
 * From the Javadoc:
 * If an application does not register its own custom ErrorListener, the default ErrorListener is used which reports all warnings and errors to System.err 
 * and does not throw any Exceptions. Applications are strongly encouraged to register and use ErrorListeners that insure proper behavior for warnings and errors.
 * 
 * @author joao.cunha
 *
 */
public class SmpXsltListener implements ErrorListener {
	private static final Logger logger = LoggerFactory.getLogger(SmpXsltListener.class);

	@Override
	public void error(TransformerException exception) throws TransformerException {
		// TODO Auto-generated method stub
		logger.error(printExceptionStackTrace(exception));

	}

	@Override
	public void fatalError(TransformerException exception)
			throws TransformerException {
		// TODO Auto-generated method stub
		logger.error(printExceptionStackTrace(exception));

	}

	@Override
	public void warning(TransformerException exception) throws TransformerException {
		// TODO Auto-generated method stub
		logger.warn(printExceptionStackTrace(exception));
	} 
        
    /**
     * Pretty-prints an Exception into its stack trace
     *
     * @param e The Exception instance that we want to print the stack trace
     * @return The string representation of the Exception's stack trace
     */
    public static String printExceptionStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder(e.toString());
        for (StackTraceElement ste : e.getStackTrace()) {
            sb.append("\n\tat ");
            sb.append(ste);
        }
        String trace = sb.toString();
        return trace;
    }
}