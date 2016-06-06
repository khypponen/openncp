package eu.epsos.util.audit;

public interface FailedLogsHandlerService {

	public static final long DEFAULT_SCHEDULER_TIME_MINUTES = 60; // 1h
	
	public void start();
	
	public void stop();
	
}
