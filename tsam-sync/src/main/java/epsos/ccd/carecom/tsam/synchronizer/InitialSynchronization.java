package epsos.ccd.carecom.tsam.synchronizer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import epsos.ccd.carecom.tsam.synchronizer.configuration.Settings;
import epsos.ccd.carecom.tsam.synchronizer.database.HibernateUnitOfWork;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertCodeSystem;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertCodeSystemVersion;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertConcept;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertDesignation;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertValueSet;
import epsos.ccd.carecom.tsam.synchronizer.database.InsertValueSetVersion;
import epsos.ccd.carecom.tsam.synchronizer.database.MergeTranscoding;
import epsos.ccd.carecom.tsam.synchronizer.statistics.EntityCounterGatherer;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.AuthenticatedCTS2Webservice;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystem;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystemConcept;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Designation;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Transcoding;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.ValueSet;

/**
 * Implements the initial synchronization procedure.
 */
public class InitialSynchronization extends SynchronizeProcedure {

	@Override
	public void execute() {
		init();
		this.service = new AuthenticatedCTS2Webservice(
        		Settings.getInstance().getWebServiceUrl(), 
        		Settings.getInstance().getWebServiceUser(), 
        		Settings.getInstance().getWebServicePassword(), 
        		null, 
        		null);
		
		boolean isDebug = Settings.getInstance().getIsDebug();
		boolean truncateStrings = Settings.getInstance().truncateLongStrings();
		
//		Initial run
		Map<Long, List<Long>> valueSetMap = new LinkedHashMap<Long, List<Long>>();
		
//		1.Call web method listCodeSystems to obtain a list of code systems.
		List<CodeSystem> codeSystems = retrieveCodeSystems();
		
//		2.For each code system do following:
		for (CodeSystem codeSystem : codeSystems) {
//			1.Create a record for the code system in CODE_SYSTEM.
			HibernateUnitOfWork insertCodeSystem = new InsertCodeSystem(codeSystem, isDebug, truncateStrings);

			this.notifier.registerActionStart(null, null);
			this.notifier.registerActionEnd(
					EntityCounterGatherer.ENTITY_CODE_SYSTEM, insertCodeSystem.executeUnitOfWork());
			
//			2.Create the corresponding record in CODE_SYSTEM_VERSION.
			HibernateUnitOfWork insertCodeSystemVersion = new InsertCodeSystemVersion(
					codeSystem,isDebug, truncateStrings);
			
			this.notifier.registerActionStart(null, null);
			this.notifier.registerActionEnd(
					EntityCounterGatherer.ENTITY_CODE_SYSTEM_VERSION, insertCodeSystemVersion.executeUnitOfWork());
			
//			3.Call web method listValueSets to obtain a list of value sets for the specific code system.
			List<ValueSet> valueSets = retrieveValueSets(codeSystem.getId());
			
//			4.For each value set do following:
			for (ValueSet valueSet : valueSets) {
//				1.Create a record for the value set in VALUE_SET.
				HibernateUnitOfWork insertValueSet = new InsertValueSet(
						valueSet, isDebug, truncateStrings);
				
				this.notifier.registerActionStart(null, null);
				this.notifier.registerActionEnd(
						EntityCounterGatherer.ENTITY_VALUE_SET, insertValueSet.executeUnitOfWork());

//				2.Create the corresponding record in the VALUE_SET_VERSION.
				HibernateUnitOfWork insertValueSetVersion = new InsertValueSetVersion(
						valueSet, isDebug, truncateStrings);
				
				this.notifier.registerActionStart(null, null);
				this.notifier.registerActionEnd(
						EntityCounterGatherer.ENTITY_VALUE_SET_VERSION, insertValueSetVersion.executeUnitOfWork());
				
//				3.Call web method listCodeSystemConcepts with the code system id, and value set id as filter.
				List<CodeSystemConcept> concepts = retrieveConcepts(
						codeSystem.getId(), valueSet.getId());
				
				List<Long> conceptIds = new ArrayList<Long>(concepts.size());
//				4.For each concept do following:
				for (CodeSystemConcept concept : concepts) {
//					1.Create a new record in CODE_SYSTEM_CONCEPT.
//					2.Create a reference between the concept an value set in X_CONCEPT_VALUE_SET.
					HibernateUnitOfWork insertConcept = new InsertConcept(
							codeSystem.getVersionId() == null ? codeSystem.getId() : codeSystem.getVersionId(), 
							valueSet.getId(), concept, isDebug, truncateStrings);
					
					this.notifier.registerActionStart(null, null);
					this.notifier.registerActionEnd(
							EntityCounterGatherer.ENTITY_CONCEPT, insertConcept.executeUnitOfWork());

//					3.Add a concept identifier to a list of concept identifiers.
					conceptIds.add(concept.getId());
				}
				
//				5.Add key-value pair consisting of the value set ID as key and the list of concept identifiers as value to a key-value map.
				valueSetMap.put(valueSet.getId(), conceptIds);

//				6.Call web method listDesignations with code system id, and the list of concept idenifiers as filter.
				List<Designation> designations = retrieveDesignations(
						codeSystem.getId(), conceptIds);
				
//				7. For each designation create a new record in DESIGNATION.
				for (Designation designation : designations) {
					HibernateUnitOfWork insertDesignation = new InsertDesignation(
							designation, isDebug, truncateStrings);
					
					this.notifier.registerActionStart(null, null);
					this.notifier.registerActionEnd(
							EntityCounterGatherer.ENTITY_DESIGNATION, insertDesignation.executeUnitOfWork());
				}
			}
		}
		
//		3.For each value set ID key in the key-value map do following:
		for (Map.Entry<Long, List<Long>> valueSetEntry : valueSetMap.entrySet()) {
//			1. Call web method listTranscodings with the list of concepts identifiers corresponding to the value of the 
//		   	value set ID key in the key-value map as filter on the source concepts.
			List<Transcoding> transcodingsReferencingSourceConcept = retrieveTranscodings(valueSetEntry.getValue(), EMPTY_LONG_LIST);

//			2.For each transcoding do following:
			for (Transcoding transcoding : transcodingsReferencingSourceConcept) {
//				1. If source and target concepts exist in CODE_SYSTEM_CONCEPT, and if the transcoding does not exists create a new record in TRANSCODING_ASSOCIATION.
				HibernateUnitOfWork mergeTranscoding = new MergeTranscoding(
						transcoding, isDebug, truncateStrings);
				
				this.notifier.registerActionStart(null, null);
				this.notifier.registerActionEnd(
						EntityCounterGatherer.ENTITY_TRANSCODING, mergeTranscoding.executeUnitOfWork());
			}
//			3. Call web method listTranscodings with the list of concepts identifiers corresponding to the value of the
//			value set ID key in the key-value map as filter on the target concepts.
			List<Transcoding> transcodingsReferencingTargetConcept = retrieveTranscodings(EMPTY_LONG_LIST, valueSetEntry.getValue());

//			4.For each transcoding do following:
			for (Transcoding transcoding : transcodingsReferencingTargetConcept) {
//				1.If source and target concepts exist in CODE_SYSTEM_CONCEPT, and if the transcoding does not exists create a new record in TRANSCODING_ASSOCIATION.
				HibernateUnitOfWork mergeTranscoding = new MergeTranscoding(
						transcoding, isDebug, truncateStrings);
				this.notifier.registerActionStart(null, null);
				this.notifier.registerActionEnd(
						EntityCounterGatherer.ENTITY_TRANSCODING, mergeTranscoding.executeUnitOfWork());
			}
		}
		
		end();
	}
}
