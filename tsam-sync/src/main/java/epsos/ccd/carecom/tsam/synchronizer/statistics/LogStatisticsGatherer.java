package epsos.ccd.carecom.tsam.synchronizer.statistics;

import epsos.ccd.carecom.tsam.synchronizer.ApplicationController;
import org.slf4j.event.Level;

import java.util.Map;


/**
 * An implementation of a gatherer that logs each event to the application log.
 */
public class LogStatisticsGatherer implements Gatherer {

    private Level logLevel;
    private String methodName;

    /**
     * Default constructor.
     *
     * @param logLevel The log level at which the events are logged.
     */
    public LogStatisticsGatherer(Level logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * @param webMethodName
     * @param filters       A map of names and a numeric information that can be either the size or ID of the filter object.
     */
    public void registerActionStart(String webMethodName,
                                    Map<String, Long> filters) {
        StringBuilder sb = new StringBuilder();
        sb.append("Calling #");
        sb.append(webMethodName);
        if (filters != null) {
            sb.append(" with following filters: ");
            for (Map.Entry<String, Long> entry : filters.entrySet()) {
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue());
                sb.append("|");
            }
        }
        ApplicationController.LOG.info(sb.toString());
        this.methodName = webMethodName;
    }

    /**
     * @param actionName      Optional name of an action to end.
     * @param numberOfRecords Number of records fetched by the action.
     */
    public void registerActionEnd(String actionName, int numberOfRecords) {
        ApplicationController.LOG.info(this.methodName + ":" + numberOfRecords);
    }

    /**
     * This method does nothing as there is no cleaning up after this gatherer.
     */
    public void registerGatheringComplete() {
        // Simply ignore. No exception should be thrown because this method will be called.
    }
}
