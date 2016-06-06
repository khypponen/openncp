package epsos.ccd.carecom.tsam.synchronizer.statistics;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents an action record that is used by the statistics part of the application.
 * A action record could represent a call to a specific web method/method or some other
 * action that fetches/writes some data which needs to be logged.
 */
public class ActionRecord {
	private String actionName;
	private Date startTime;
	private Date endTime;
	private int numberOfRecordsFetched;
	private Map<String,Long> filters;
	
	/**
	 * Default constructor.
	 */
	public ActionRecord() {
		this.filters = new LinkedHashMap<String,Long>();
	}
	
	/**
	 * @return The action start date.
	 */
	public Date getStartTime() {
		return this.startTime;
	}
	
	/**
	 * Sets the action start date.
	 * @param startTime Start date.
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * @return The action end date.
	 */
	public Date getEndTime() {
		return this.endTime;
	}
	
	/**
	 * Sets the action end date.
	 * @param endTime Action end date.
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * @return The number of records fetched by the action.
	 */
	public long getNumberOfRecordsFetched() {
		return this.numberOfRecordsFetched;
	}
	
	/**
	 * Sets the number of records fetched by the action.
	 * @param numberOfRecordsFetched Number of records.
	 */
	public void setNumberOfRecordsFetched(int numberOfRecordsFetched) {
		this.numberOfRecordsFetched = numberOfRecordsFetched;
	}
	
	/**
	 * @return The filters used by the action.
	 */
	public Map<String, Long> getFilters() {
		return this.filters;
	}

	/**
	 * Sets the filters used by the action.
	 * @param filters The filters.
	 */
	public void setFilters(Map<String, Long> filters) {
		this.filters = filters;
	}

	/**
	 * @return The action name.
	 */
	public String getActionName() {
		return this.actionName;
	}

	/**
	 * Set the action name.
	 * @param actionName Action name.
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@Override
	public String toString() {
		String format = "%s, %s, %tc, %tc, %d, %s";
		StringBuilder filtersText = new StringBuilder();
		if (this.filters != null) {
			for (Map.Entry<String, Long> filter : this.filters.entrySet()) {
				filtersText.append(filter.getKey());
				filtersText.append(":");
				filtersText.append(filter.getValue());
				filtersText.append("|");
			}
		}
		return String.format(
				format, 
				this.actionName, 
				new TimeSpan(this.startTime, this.endTime), 
				this.startTime, 
				this.endTime, 
				Long.valueOf(this.numberOfRecordsFetched), 
				filtersText);
	}
}
