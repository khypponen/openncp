package fi.kela.se.epsos.data.model;

/**
 * DocumentAssociation which includes XML and PDF versions of DocumentMetaData.
 * @author mimyllyv
 *
 * @param <T>
 */
public interface DocumentAssociation<T extends EPSOSDocumentMetaData> {
	
	T getXMLDocumentMetaData();

	T getPDFDocumentMetaData();

	String getDocumentClassCode(String documentId);
	
	String getPatientId(String documentId);
}
