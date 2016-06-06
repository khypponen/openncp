package fi.kela.se.epsos.data.model;

/**
 * EPSOSDocument interface. Includes EPSOSDocumentMetaData and includes DOM Document.
 * @author mimyllyv
 *
 */

public interface EPSOSDocument {
	
	String getPatientId();
	
	String getClassCode();

	org.w3c.dom.Document getDocument();
	
	boolean matchesCriteria(SearchCriteria sc);
}
