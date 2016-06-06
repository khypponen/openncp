package epsos.ccd.carecom.tsam.synchronizer.statistics;

import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

import epsos.ccd.carecom.tsam.synchronizer.ApplicationController;

public class DatabaseCallsDurationsGatherer implements Gatherer {
	
	private long accumulatedDuration;
	private Date actionStartDate;
	private Level logLevel;
	
	public DatabaseCallsDurationsGatherer(Level logLevel) {
		this.accumulatedDuration = 0;
		this.logLevel = logLevel;
	}

	public void registerActionStart(String actionName, Map<String, Long> filters) {
		this.actionStartDate = new Date();
	}

	public void registerActionEnd(String actionName, int numberOfRecords) {
		if (this.actionStartDate != null) {
			this.accumulatedDuration += new Date().getTime() - this.actionStartDate.getTime();
			this.actionStartDate = null;
		}
	}

	public void registerGatheringComplete() {
		ApplicationController.LOG.log(this.logLevel, "Accumulated database call duration: " + new TimeSpan(this.accumulatedDuration));
	}
}
