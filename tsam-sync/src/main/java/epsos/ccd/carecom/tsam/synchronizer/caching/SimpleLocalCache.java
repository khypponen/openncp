package epsos.ccd.carecom.tsam.synchronizer.caching;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystemConcept;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Designation;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Transcoding;

/**
 * Implements a simple memory cache based on Maps.
 */
public class SimpleLocalCache implements LocalCache {
	
	private Map<Long,List<CodeSystemConcept>> codeSystemConceptsCache;
	private Map<Long,List<Designation>> designationsCache;
	private Map<Long,List<Transcoding>> transcodingsForSourceConceptsCache;
	private Map<Long,List<Transcoding>> transcodingsForTargetConceptsCache;
	
	/**
	 * Default constructor
	 * @param hashSize Initial size of the cache.
	 */
	public SimpleLocalCache(int hashSize) {
		this.codeSystemConceptsCache = new HashMap<Long, List<CodeSystemConcept>>(hashSize);
		this.designationsCache = new HashMap<Long, List<Designation>>(hashSize);
		this.transcodingsForSourceConceptsCache = new HashMap<Long, List<Transcoding>>(hashSize);
		this.transcodingsForTargetConceptsCache = new HashMap<Long, List<Transcoding>>(hashSize);
	}
	
	public boolean saveConcepts(Long uniqueId, List<CodeSystemConcept> concepts) {
		if (this.codeSystemConceptsCache.containsKey(uniqueId) || uniqueId == null || concepts == null) {
			return false;
		}
		this.codeSystemConceptsCache.put(uniqueId,concepts);
		return true;
	}

	public boolean saveDesignations(Long uniqueId,
			List<Designation> designations) {
		if (this.designationsCache.containsKey(uniqueId) || uniqueId == null || designations == null) {
			return false;
		}
		this.designationsCache.put(uniqueId, designations);
		return true;
	}

	public boolean saveTranscodings(Long uniqueId,
			List<Transcoding> transcodings, boolean isSource) {
		if (uniqueId == null || transcodings == null) {
			return false;
		}
		if (isSource && this.transcodingsForSourceConceptsCache.containsKey(uniqueId)) {
			return false;
		}
		if (!isSource && this.transcodingsForTargetConceptsCache.containsKey(uniqueId)) {
			return false;
		}
		if (isSource) {
			this.transcodingsForSourceConceptsCache.put(uniqueId, transcodings);
		} else {
			this.transcodingsForTargetConceptsCache.put(uniqueId, transcodings);
		}
		return true;
	}

	public List<CodeSystemConcept> getConcepts(Long uniqueId) {
		if (uniqueId == null) {
			return Collections.emptyList();
		}
		if (this.codeSystemConceptsCache.containsKey(uniqueId)) {
			return this.codeSystemConceptsCache.get(uniqueId);
		}
		return Collections.emptyList();
	}

	public List<Designation> getDesignations(Long uniqueId) {
		if (uniqueId == null) {
			return Collections.emptyList();
		}
		if (this.designationsCache.containsKey(uniqueId)) {
			return this.designationsCache.get(uniqueId);
		}
		return Collections.emptyList();
	}

	public List<Transcoding> getTranscodings(Long uniqueId, boolean isSource) {
		if (uniqueId == null) {
			return Collections.emptyList();
		}
		if (isSource && this.transcodingsForSourceConceptsCache.containsKey(uniqueId)) {
			return this.transcodingsForSourceConceptsCache.get(uniqueId);
		}
		if (this.transcodingsForTargetConceptsCache.containsKey(uniqueId)) {
			return this.transcodingsForTargetConceptsCache.get(uniqueId);
		}
		return Collections.emptyList();
	}
	
	public void clear() {
		this.codeSystemConceptsCache.clear();
		this.designationsCache.clear();
		this.transcodingsForSourceConceptsCache.clear();
		this.transcodingsForTargetConceptsCache.clear();
	}

}
