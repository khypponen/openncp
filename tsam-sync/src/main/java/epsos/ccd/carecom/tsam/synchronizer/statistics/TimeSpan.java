package epsos.ccd.carecom.tsam.synchronizer.statistics;

import java.util.Date;

/**
 * This class implements a time span between two points in time.
 */
public class TimeSpan {
	private long days;
	private long hours;
	private long minutes;
	private long seconds;
	private long milliseconds;
	
	/**
	 * Creates an instance of time span with the provided milliseconds.
	 * @param durationMilliseconds Duration in milliseconds.
	 */
	public TimeSpan(long durationMilliseconds) {
		this.milliseconds = durationMilliseconds % 1000;
		long diff = durationMilliseconds / 1000;
		this.seconds = diff % 60;
		this.minutes = (diff = (diff / 60)) % 60;
		this.hours = (diff = (diff / 60)) % 24;
		this.days = (diff = (diff / 24));
	}
	
	/**
	 * Creates an instance of time span between two dates.
	 * @param start Start date.
	 * @param end End date.
	 */
	public TimeSpan(Date start, Date end) {
		this(end.getTime() - start.getTime());
	}
	
	/**
	 * @return Number of days of the time span.
	 */
	public long getDays() {
		return this.days;
	}
	
	/**
	 * @return Number of hours of the time span.
	 */
	public long getHours() {
		return this.hours;
	}

	/**
	 * @return Number of minutes of the time span.
	 */
	public long getMinutes() {
		return this.minutes;
	}

	/**
	 * @return Number of seconds of the time span.
	 */
	public long getSeconds() {
		return this.seconds;
	}
	
	/**
	 * @return Number of milliseconds of the time span.
	 */
	public long getMilliseconds() {
		return this.milliseconds;
	}
	
	@Override
	public String toString() {
		// TODO: create a better format.
		StringBuilder sb = new StringBuilder("");
		sb.append(this.days);
		sb.append("d ");
		sb.append(this.hours);
		sb.append("h ");
		sb.append(this.minutes);
		sb.append("m ");
		sb.append(this.seconds);
		sb.append("s ");
		sb.append(this.milliseconds);
		sb.append("ml");
		return sb.toString();
	}
}
