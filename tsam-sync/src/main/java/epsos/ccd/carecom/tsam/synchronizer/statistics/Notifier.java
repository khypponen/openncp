package epsos.ccd.carecom.tsam.synchronizer.statistics;

import java.util.Map;

/**
 * Any class responsible for notifying gatherers should implement this interface.
 */
public interface Notifier {
	
	/**
	 * Adds a gatherer to a list of gatherers that will be notified of one of following events:
	 * <ol>
	 * 	<li>Action start.
	 * 	<li>Action end.
	 *  <li>Gathering is complete.
	 * </ol> 
	 * @param gatherer The gatherer to add to a list.
	 */
	public void addGatherer(Gatherer gatherer);
	
	/**
	 * Registers start of an action and notifies all gatherers. 
	 * @param actionName Name of the action.
	 * @param filters Filters used by the action.
	 */
	public void registerActionStart(String actionName, Map<String,Long> filters);
	
	/**
	 * Registers end of an action and notifies all gatherers.  
	 * @param actionName TODO
	 * @param numberOfRecords Number of records fetched by the action.
	 */
	public void registerActionEnd(String actionName, int numberOfRecords);
	
	/**
	 * Registers end of gathering and notifies all gatherers.
	 */
	public void registerGatheringComplete();
}