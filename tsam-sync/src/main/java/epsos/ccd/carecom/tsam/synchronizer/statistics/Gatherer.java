package epsos.ccd.carecom.tsam.synchronizer.statistics;

import java.util.Map;

/**
 * Any class that wants to implement some form of logging or statistics gathering around
 * method calls should use implement and use this interface.
 */
public interface Gatherer {
	
	/**
	 * Should be called just before an action is performed.
	 * @param actionName The name of the action that is going to be performed.
	 * @param filters A map of names and a numeric information that can be either the size or ID of the filter object.
	 */
	public void registerActionStart(String actionName, Map<String,Long> filters);
	
	/**
	 * Should be called right after ending the action.
	 * @param actionName Optional name of an action to end.
	 * @param numberOfRecords Number of records fetched by the action.
	 */
	public void registerActionEnd(String actionName, int numberOfRecords);
	
	/**
	 * Should be called when all gathering is complete.
	 */
	public void registerGatheringComplete();
}
