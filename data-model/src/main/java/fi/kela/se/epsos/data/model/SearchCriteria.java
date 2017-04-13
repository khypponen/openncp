package fi.kela.se.epsos.data.model;

import java.util.Iterator;
import org.w3c.dom.Document;

/**
 * SearchCriteria class will be used for XCAService interface to allow search of documents with
 * different criteria.
 * 
 * The class can be used like:
 * DocumentFactory.createSearchCriteria(Criteria.PatientId, "PatientId");
 * or
 * DocumentFactory.createSearchCriteria().add(Criteria.PatientId, "PatientId").add(Criteria.RepositoryId, "repositoryId");
 * 
 */
public interface SearchCriteria {
	
	enum Criteria {
		PatientId,
		RepositoryId,
		DocumentId
	}
	
	SearchCriteria add(Criteria c, String value);

	String getCriteriaValue(Criteria c);
	
	Iterator<Criteria> getSearchCriteriaKeys();
        
        Document asXml();
}
