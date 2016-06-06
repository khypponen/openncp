package epsos.ccd.carecom.tsam.synchronizer.statistics;

import java.util.Map;
import java.util.logging.Level;

import epsos.ccd.carecom.tsam.synchronizer.ApplicationController;

/**
 * An implementation of a gatherer that logs each event to the application log.
 */
public class LogStatisticsGatherer implements Gatherer {
	private Level logLevel;
	private String methodName;
	
	/**
	 * Default constructor.
	 * @param logLevel The log level at which the events are logged.
	 */
	public LogStatisticsGatherer(Level logLevel) {
		this.logLevel = logLevel;
	}
	
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
		ApplicationController.LOG.log(this.logLevel, sb.toString());
		this.methodName = webMethodName;
	}

	public void registerActionEnd(String actionName, int numberOfRecords) {
		ApplicationController.LOG.log(this.logLevel, this.methodName + ":" + numberOfRecords);
	}

	/**
	 * This method does nothing as there is no cleaning up after this gatherer.
	 */
	public void registerGatheringComplete() {
		// Simply ignore. No exception should be thrown because this method will be called.
	}
}
