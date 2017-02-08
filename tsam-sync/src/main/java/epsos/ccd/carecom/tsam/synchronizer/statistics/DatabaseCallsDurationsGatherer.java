package epsos.ccd.carecom.tsam.synchronizer.statistics;

import epsos.ccd.carecom.tsam.synchronizer.ApplicationController;
import org.slf4j.event.Level;

import java.util.Date;
import java.util.Map;


/**
 *
 */
public class DatabaseCallsDurationsGatherer implements Gatherer {

    private long accumulatedDuration;
    private Date actionStartDate;
    private Level logLevel;

    /**
     * @param logLevel
     */
    public DatabaseCallsDurationsGatherer(Level logLevel) {
        this.accumulatedDuration = 0;
        this.logLevel = logLevel;
    }

    /**
     * @param actionName The name of the action that is going to be performed.
     * @param filters    A map of names and a numeric information that can be either the size or ID of the filter object.
     */
    public void registerActionStart(String actionName, Map<String, Long> filters) {
        this.actionStartDate = new Date();
    }

    /**
     * @param actionName      Optional name of an action to end.
     * @param numberOfRecords Number of records fetched by the action.
     */
    public void registerActionEnd(String actionName, int numberOfRecords) {
        if (this.actionStartDate != null) {
            this.accumulatedDuration += new Date().getTime() - this.actionStartDate.getTime();
            this.actionStartDate = null;
        }
    }

    /**
     *
     */
    public void registerGatheringComplete() {
        ApplicationController.LOG.info("Accumulated database call duration: " + new TimeSpan(this.accumulatedDuration));
    }
}
