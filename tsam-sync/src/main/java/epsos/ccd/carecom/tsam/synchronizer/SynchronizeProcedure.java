package epsos.ccd.carecom.tsam.synchronizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import epsos.ccd.carecom.tsam.synchronizer.configuration.Settings;
import epsos.ccd.carecom.tsam.synchronizer.configuration.SettingsFileNotAccessableException;
import epsos.ccd.carecom.tsam.synchronizer.database.HibernateSessionFactory;
import epsos.ccd.carecom.tsam.synchronizer.statistics.DatabaseCallsDurationsGatherer;
import epsos.ccd.carecom.tsam.synchronizer.statistics.EntityCounterGatherer;
import epsos.ccd.carecom.tsam.synchronizer.statistics.Notifier;
import epsos.ccd.carecom.tsam.synchronizer.statistics.NotifierImpl;
import epsos.ccd.carecom.tsam.synchronizer.statistics.TimeSpan;
import epsos.ccd.carecom.tsam.synchronizer.statistics.WebMethodCallAuditGatherer;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.AuthenticatedCTS2Webservice;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystem;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystemConcept;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Designation;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Transcoding;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.ValueSet;

/**
 * This interface should be implemented by a class providing a synchronizing procedure.
 */
public abstract class SynchronizeProcedure {
	
	/**
	 * Holds an empty list of type Long.
	 */
	public static final List<Long> EMPTY_LONG_LIST = Collections.emptyList();
	
	/**
	 * Holds an instance of the CTS2 web service.
	 */
	protected AuthenticatedCTS2Webservice service;
	
	/**
	 * Holds a notifier instance that notifies all observers about specific events during synchronization. 
	 */
	protected Notifier notifier;
	
	private Date startDate;
	
	/**
	 * This method performs the synchronization.
	 */
	public abstract void execute();
	
	/**
	 * This method should always be called to initialize logging and settings.
	 */
	protected void init() {
		// This try-catch is performed only when initializing procedures, to achieve a lazy try-catch.
		// The lazy try-catch makes it only necessary to do a try-catch once, because this we know that'
		// only when the Settings class is initialized that the exceptions can be thrown.
		try {
			Settings.getInstance();
		} catch (SettingsFileNotAccessableException e) {
			ApplicationController.handleSevereError(e, "Could not access settings file.");
		} catch (MissingSystemPropertyException e) {
			ApplicationController.handleSevereError(e, "No settings file specified.");
		}
		this.startDate = new Date();
		
		ApplicationController.LOG.info("Starting Synchronization, making " + ApplicationController.retryNumber + " retries in case of temporary failure.");
		
		this.notifier = new NotifierImpl();
		if ("YES".equalsIgnoreCase(Settings.getInstance().getSettingValue("sync.auditmanager.enable"))) {
			this.notifier.addGatherer(new EntityCounterGatherer(
					Settings.getInstance().getSettingValue("sync.auditmanager.facility"),
					Settings.getInstance().getSettingValue("sync.auditmanager.infoseverity"),
					Settings.getInstance().getSettingValue("sync.auditmanager.transactionnumber")));
		}
		
		this.notifier.addGatherer(new DatabaseCallsDurationsGatherer(Level.INFO));
	}
	
	/**
	 * Should be called when execution has ended.
	 */
	protected void end() {
		HibernateSessionFactory.getInstance().close();
		
		ApplicationController.STATS.registerGatheringComplete();
		
		this.notifier.registerGatheringComplete();
		
		ApplicationController.LOG.info("Synchronizing Done in " + new TimeSpan(this.startDate, new Date()));
	}

	/**
	 * Retrieves transcodings, handles any exceptions, and returns the result.
	 * @param sourceConceptIds A list of source code system IDs.
	 * @param targetConceptIds A list of target code system IDs.
	 * @return A list of transcodings.
	 */
	protected List<Transcoding> retrieveTranscodings(List<Long> sourceConceptIds, List<Long> targetConceptIds) {
		Map<String,Long> filters = new LinkedHashMap<String, Long>(2);
		
		if (sourceConceptIds != null) {
			filters.put("SourceConceptIds",
					Long.valueOf(sourceConceptIds.size()));
		}
		if (targetConceptIds != null) {
			filters.put("TargetConceptIds",
					Long.valueOf(targetConceptIds.size()));
		}
		ApplicationController.STATS.registerActionStart(WebMethodCallAuditGatherer.WEB_METHOD_CALL_TRANSCODINGS, filters);
		
		List<Transcoding> transcodings = new ArrayList<Transcoding>(0);

		Throwable exception = null;
		int retries = 0;
		while (retries < ApplicationController.retryNumber) {
			try {
				transcodings = this.service.getTranscodings(sourceConceptIds, targetConceptIds);
				break;
			} catch (Throwable e) {
				exception = e;
				ApplicationController.LOG.info((++retries) + " retry while getting Transcodings.");
			}
		}
		if ((retries == ApplicationController.retryNumber) && (exception!=null)) {
			ApplicationController.handleSevereError(exception, "Unable to retrieve Transcodings.");
		}

		ApplicationController.STATS.registerActionEnd("", transcodings.size());
		return transcodings;
	}

	/**
	 * Retrieves designations, handles any exceptions, and returns the result.
	 * @param codeSystemIds A list of code system IDs.
	 * @param conceptIds A list of concept IDs.
	 * @return A list of designations.
	 */
	protected List<Designation> retrieveDesignations(Long codeSystemId, List<Long> conceptIds) {
		List<Long> codeSystemIds = null;
		Map<String,Long> filters = new LinkedHashMap<String, Long>();
		if (codeSystemId != null) {
			codeSystemIds = new ArrayList<Long>();
			codeSystemIds.add(codeSystemId);

			filters.put("CodeSystemId", codeSystemId);
		}
		List<Designation> designations = new ArrayList<Designation>();

		filters.put("ConceptsIds", Long.valueOf(conceptIds.size()));
		ApplicationController.STATS.registerActionStart(WebMethodCallAuditGatherer.WEB_METHOD_CALL_DESIGNATIONS, filters);

		Throwable exception = null;
		int retries = 0;
		while (retries < ApplicationController.retryNumber) { 
			try {
				designations = this.service.getDesignations(codeSystemIds, conceptIds);
				break;
			} catch (Throwable e) {
				exception = e;
				ApplicationController.LOG.info((++retries) + " retry while getting Designations.");
			}
		}
		if ((retries == ApplicationController.retryNumber) && (exception!=null)) {
			ApplicationController.handleSevereError(exception, "Unable to retrieve Designations.");
		}
		ApplicationController.STATS.registerActionEnd("", designations.size());
		return designations;
	}

	/**
	 * Retrieves code system concepts, handles any exceptions, and returns the result.
	 * @param codeSystemIds A list of code system IDs.
	 * @param valueSetIds A list of value set IDs. 
	 * @return A list of code system concepts.
	 */
	protected List<CodeSystemConcept> retrieveConcepts(Long codeSystemId, Long valueSetId) {
		List<Long> codeSystemIds = null;
		Map<String,Long> filters = new LinkedHashMap<String, Long>();
		if (codeSystemId != null) {
			codeSystemIds = new ArrayList<Long>(1);
			codeSystemIds.add(codeSystemId);
			filters.put("CodeSystemId", codeSystemId);
		}
		
		List<Long> valueSetIds = new ArrayList<Long>(1);
		valueSetIds.add(valueSetId);
		List<CodeSystemConcept> concepts = new ArrayList<CodeSystemConcept>(0);

		filters.put("ValueSetId", valueSetId);
		ApplicationController.STATS.registerActionStart(WebMethodCallAuditGatherer.WEB_METHOD_CALL_CODE_SYSTEM_CONCEPTS, filters);

		Throwable exception = null;
		int retries = 0;
		while (retries < ApplicationController.retryNumber) {
			try {
				concepts = this.service.getConcepts(codeSystemIds, valueSetIds);
				break;
			} catch (Throwable e) {
				exception = e;
				ApplicationController.LOG.info((++retries) + " retry while getting Code Systems Concepts.");
			}
		}
		if ((retries == ApplicationController.retryNumber) && (exception!=null)) { 
			ApplicationController.handleSevereError(exception, "Unable to retrieve Code Systems Concepts.");
		}

		ApplicationController.STATS.registerActionEnd("", concepts.size());
		return concepts;
	}

	/**
	 * Retrieves value sets, handles any exceptions, and returns the result.
	 * @param codeSystemIds A list of code system IDs.
	 * @return A list of value sets.
	 */
	protected List<ValueSet> retrieveValueSets(Long codeSystemId) {
		List<Long> codeSystemIds = null;
		Map<String,Long> filters = new LinkedHashMap<String, Long>(1);
		List<ValueSet> valueSets = new ArrayList<ValueSet>(0);
		
		if (codeSystemId != null) {
			codeSystemIds = new ArrayList<Long>(1);
			codeSystemIds.add(codeSystemId);

			filters.put("CodeSystemId", codeSystemId);
		}
		
		ApplicationController.STATS.registerActionStart(WebMethodCallAuditGatherer.WEB_METHOD_CALL_VALUE_SETS, filters);
		
		Throwable exception = null;
		int retries = 0;
		while (retries < ApplicationController.retryNumber) {
			try {
				valueSets = this.service.getValueSets(codeSystemIds);
				break;
			} catch (Throwable e) {
				exception = e;
				ApplicationController.LOG.info((++retries) + " retry while getting Value Sets.");
			}
		}
		if ((retries == ApplicationController.retryNumber) && (exception!=null)) {
			ApplicationController.handleSevereError(exception, "Unable to retrieve Value Sets.");
		}

		ApplicationController.STATS.registerActionEnd("", valueSets.size());
		return valueSets;
	}

	/**
	 * Retrieves code systems, handles any exceptions, and returns the result.
	 * @return A list of code systems.
	 */
	protected List<CodeSystem> retrieveCodeSystems() {
		List<CodeSystem> codeSystems = new ArrayList<CodeSystem>(0);
		
		ApplicationController.STATS.registerActionStart(WebMethodCallAuditGatherer.WEB_METHOD_CALL_CODE_SYSTEMS, null);

		Throwable exception = null;
		int retries = 0;
		while (retries < ApplicationController.retryNumber) {
			try {
				codeSystems = this.service.getCodeSystems();
				break;
			} catch (Throwable e) {
				exception = e;
				ApplicationController.LOG.info((++retries) + " retry while getting Code Systems.");
			}
		}
		if ((retries == ApplicationController.retryNumber) && (exception!=null)) {
			ApplicationController.handleSevereError(exception, "Unable to retrieve Code Systems.");
		}

		ApplicationController.STATS.registerActionEnd("", codeSystems.size());
		return codeSystems;
	}
}
