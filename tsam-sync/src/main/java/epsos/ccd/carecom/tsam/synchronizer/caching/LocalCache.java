package epsos.ccd.carecom.tsam.synchronizer.caching;

import java.util.List;

import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystemConcept;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Designation;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Transcoding;
/**
 * Defines an interface for a handling a local caching service. Any class handling caching of web service
 * results should implement this interface.
 */
public interface LocalCache {
	/**
	 * Saves concepts into cache.
	 * @param uniqueId The unique key used to retrieve the records.
	 * @param concepts The concepts to save.
	 * @return <code>true</code> if saving the concepts succeeded.
	 */
	public boolean saveConcepts(Long uniqueId, List<CodeSystemConcept> concepts);
	
	/**
	 * Saves designations into cache.
	 * @param uniqueId The unique key used to retrieve the records.
	 * @param designations The designations to save.
	 * @return <code>true</code> if saving the designations succeeded.
	 */
	public boolean saveDesignations(Long uniqueId, List<Designation> designations);
	
	/**
	 * Saves transcodings into cache.
	 * @param uniqueId The unique key used to retrieve the records.
	 * @param transcodings The transcodings to save.
	 * @param isSource Indicated if the transcodings are fetched based on source concepts.
	 * @return <code>true</code> if saving the transcodings succeeded.
	 */
	public boolean saveTranscodings(Long uniqueId, List<Transcoding> transcodings, boolean isSource);
	
	/**
	 * @param uniqueId The unique key used to retrieve the records.
	 * @return A list of concepts if saved.
	 */
	public List<CodeSystemConcept> getConcepts(Long uniqueId);
	
	/**
	 * @param uniqueId Id The unique key used to retrieve the records.
	 * @return A list of designations if saved.
	 */
	public List<Designation> getDesignations(Long uniqueId);
	
	/**
	 * @param uniqueId The unique key used to retrieve the records.
	 * @param isSource Indicates if the records are based on source concepts.
	 * @return A list of transcodings if saved.
	 */
	public List<Transcoding> getTranscodings(Long uniqueId, boolean isSource);
	
	/**
	 * Clears the cache.
	 */
	public void clear();
}
