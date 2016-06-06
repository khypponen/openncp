package epsos.ccd.carecom.tsam.synchronizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import epsos.ccd.carecom.tsam.synchronizer.caching.LocalCache;
import epsos.ccd.carecom.tsam.synchronizer.caching.SimpleLocalCache;
import epsos.ccd.carecom.tsam.synchronizer.configuration.Settings;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertCodeSystem;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertCodeSystemVersion;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertConcept;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertDesignation;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertValueSet;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertValueSetVersion;
import epsos.ccd.carecom.tsam.synchronizer.database.MergeTranscoding;
import epsos.ccd.carecom.tsam.synchronizer.database.SelectAllValueSetIds;
import epsos.ccd.carecom.tsam.synchronizer.database.SelectConceptIdsByValueSetId;
import epsos.ccd.carecom.tsam.synchronizer.statistics.EntityCounterGatherer;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.AuthenticatedCTS2Webservice;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystem;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystemConcept;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Designation;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Transcoding;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.ValueSet;

/**
 * Implements the subsequent synchronization procedure.
 */
public class SubsequentSynchronization extends SynchronizeProcedure {

	public SubsequentSynchronization(Date start, Date end) {
		this.service = new AuthenticatedCTS2Webservice(
				Settings.getInstance().getWebServiceUrl(), 
				Settings.getInstance().getWebServiceUser(), 
				Settings.getInstance().getWebServicePassword(), 
				start, 
				end);
	}
	@Override
	public void execute() {
		init();
		
		// CACHING LOCALLY
		List<CodeSystem> codeSystems = retrieveCodeSystems();
		
		List<ValueSet> valueSets = retrieveValueSets(null);
		
		// Contains all available unique value set IDs both from the web service and the
		// local repository.
		Set<Long> valueSetIds = new HashSet<Long>();
		for (ValueSet valueSet : valueSets) {
			valueSetIds.add(valueSet.getId());
		}
		boolean isDebug = Settings.getInstance().getIsDebug();
		boolean truncateStrings = Settings.getInstance().truncateLongStrings();
		
		SelectAllValueSetIds selectAllValueSets = new SelectAllValueSetIds(isDebug, truncateStrings);
		selectAllValueSets.executeUnitOfWork();
		valueSetIds.addAll(selectAllValueSets.getValueSetIds());
		
		// Initialize local caching
		LocalCache localCache = new SimpleLocalCache(valueSets.size());

		// Save data to local cache
		for (Long valueSetId : valueSetIds) {
			List<CodeSystemConcept> codeSystemConcepts = retrieveConcepts(null, valueSetId);
			// Save Code System Concepts to cache.
			localCache.saveConcepts(valueSetId, codeSystemConcepts);
			
			// Retrieve Concept Ids
			Set<Long>	codeSystemConceptIds = new HashSet<Long>();
			for (CodeSystemConcept concept : codeSystemConcepts) {
				codeSystemConceptIds.add(concept.getId());
			}
			
			// Get local concepts as well
			SelectConceptIdsByValueSetId selectLocalConcepts = new SelectConceptIdsByValueSetId(isDebug, truncateStrings, valueSetId);
			this.notifier.registerActionStart(null, null);
			this.notifier.registerActionEnd(null, selectLocalConcepts.executeUnitOfWork());
			codeSystemConceptIds.addAll(selectLocalConcepts.getConceptIds());
			
			List<Long> conceptIdsAsList = new ArrayList<Long>(codeSystemConceptIds);
			List<Designation> designations = retrieveDesignations(null, conceptIdsAsList);
			
			// Save designations to cache
			localCache.saveDesignations(valueSetId, designations);
			
			List<Transcoding> transcodingsForSourceConcepts = retrieveTranscodings(conceptIdsAsList, null);
			localCache.saveTranscodings(valueSetId, transcodingsForSourceConcepts, true);
			
			List<Transcoding> transcodingsForTargetConcepts = retrieveTranscodings(null, conceptIdsAsList);
			localCache.saveTranscodings(valueSetId, transcodingsForTargetConcepts, false);
		}
		
		// WRITING TO THE LOCAL REPOSITORY:
		for (CodeSystem codeSystem : codeSystems) {
			InsertCodeSystem insertCodeSystem = new InsertCodeSystem(codeSystem, isDebug, truncateStrings);
			this.notifier.registerActionStart(null, null);
			this.notifier.registerActionEnd(
					EntityCounterGatherer.ENTITY_CODE_SYSTEM, insertCodeSystem.executeUnitOfWork());
			
			InsertCodeSystemVersion insertCodeSystemVersion = new InsertCodeSystemVersion(codeSystem, isDebug, truncateStrings);
			this.notifier.registerActionStart(null, null);
			this.notifier.registerActionEnd(
					EntityCounterGatherer.ENTITY_CODE_SYSTEM_VERSION, insertCodeSystemVersion.executeUnitOfWork());
		}
		
		// The valueSet list only contains the result of the web sevice call.
		for (ValueSet valueSet : valueSets) {
			InsertValueSet insertValueSet = new InsertValueSet(valueSet, isDebug, truncateStrings);
			this.notifier.registerActionStart(null, null);
			this.notifier.registerActionEnd(
					EntityCounterGatherer.ENTITY_VALUE_SET, insertValueSet.executeUnitOfWork());
			
			InsertValueSetVersion insertValueSetVersion = new InsertValueSetVersion(valueSet, isDebug, truncateStrings);
			this.notifier.registerActionStart(null, null);
			this.notifier.registerActionEnd(
					EntityCounterGatherer.ENTITY_VALUE_SET_VERSION, insertValueSetVersion.executeUnitOfWork());
		}
		
		// The valueSetIds is a set of all value set ids available at this moment.
		for (Long valueSetId : valueSetIds) {
			List<CodeSystemConcept> concepts = localCache.getConcepts(valueSetId);			
			
			for (CodeSystemConcept concept : concepts ) {
				InsertConcept insertConcept = new InsertConcept(concept.getCodeSystemVersionId(), valueSetId, concept, isDebug, truncateStrings);
				this.notifier.registerActionStart(null, null);
				this.notifier.registerActionEnd(
						EntityCounterGatherer.ENTITY_CONCEPT, insertConcept.executeUnitOfWork());
			}
			
			List<Designation> designations = localCache.getDesignations(valueSetId);
			for (Designation designation : designations) {
				InsertDesignation insertDesignation = new InsertDesignation(designation, isDebug, truncateStrings);
				this.notifier.registerActionStart(null, null);
				this.notifier.registerActionEnd(
						EntityCounterGatherer.ENTITY_DESIGNATION, insertDesignation.executeUnitOfWork());
			}
			
			List<Transcoding> transcodingsForSourceConcepts = localCache.getTranscodings(valueSetId, true);
			for (Transcoding transcoding : transcodingsForSourceConcepts) {
				MergeTranscoding mergeTranscoding = new MergeTranscoding(transcoding, isDebug, truncateStrings);
				this.notifier.registerActionStart(null, null);
				this.notifier.registerActionEnd(
						EntityCounterGatherer.ENTITY_TRANSCODING, mergeTranscoding.executeUnitOfWork());
			}
			
			List<Transcoding> transcodingsForTargetConcepts = localCache.getTranscodings(valueSetId, false);
			for (Transcoding transcoding : transcodingsForTargetConcepts) {
				MergeTranscoding mergeTranscoding = new MergeTranscoding(transcoding, isDebug, truncateStrings);
				this.notifier.registerActionStart(null, null);
				this.notifier.registerActionEnd(
						EntityCounterGatherer.ENTITY_TRANSCODING, mergeTranscoding.executeUnitOfWork());
			}
		}
		
		end();
	}
}
