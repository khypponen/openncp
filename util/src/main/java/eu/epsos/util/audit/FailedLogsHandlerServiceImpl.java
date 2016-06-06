package eu.epsos.util.audit;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.util.audit.AuditLogSerializer.Type;

public class FailedLogsHandlerServiceImpl implements FailedLogsHandlerService {
    private static Log log = LogFactory.getLog("eu.epsos.util.audit.FailedLogsHandlerServiceImpl");

    public static final String KEY_SCHEDULED_TIME_BETWEEN_FAILED_LOGS_HANDLING = "scheduled.time.between.failed.logs.handling.minutes";
	public static final int SCHEDULED_THREAD_POOL_SIZE = 1;
	public static final int WAIT_FOR_TERMINATION = 5000;
	private ScheduledExecutorService service = null;
	private FailedLogsHandler failedLogsHandlerCommand = null;
	private MessageHandlerListener listener;
	private Type type;
	
	public FailedLogsHandlerServiceImpl(MessageHandlerListener listener, Type type) {
		this.listener = listener;
		this.type = type;
	}
	
	public synchronized void start() {
		if(service == null) {
			failedLogsHandlerCommand = new FailedLogsHandlerImpl(listener, type);
			service = new ScheduledThreadPoolExecutor( SCHEDULED_THREAD_POOL_SIZE );
			service.scheduleWithFixedDelay(failedLogsHandlerCommand, getTimeBetween(), getTimeBetween(), TimeUnit.MINUTES);
			log.info("Started FailedLogsHandlerService. Logs will be scanned every " + getTimeBetween() + " minutes.");
		} else {
			log.warn("Attempted to start FailedLogsHandlerService even already running.");
		}
	}

	public synchronized void stop() {
		log.info("Shutting down FailedLogsHandlerService");
		if(service != null) {
			service.shutdown();

			boolean shutdownOk = false;
			try {
				shutdownOk = service.awaitTermination(WAIT_FOR_TERMINATION, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// Ignored
			}
			
			if(!shutdownOk) {
				service.shutdownNow();
			}
			
			service = null;
		} else {
			log.warn("Unable to stop FailedLogsHandlerService. Service is not running.");
		}
	}
	
	private long getTimeBetween() {
		String sValue = ConfigurationManagerService.getInstance().getProperty(KEY_SCHEDULED_TIME_BETWEEN_FAILED_LOGS_HANDLING);
		if(sValue == null || sValue.isEmpty()) {
			return DEFAULT_SCHEDULER_TIME_MINUTES;
		}
		
		long l;
		try {
			l = Long.parseLong(sValue);
		} catch(Exception e) {
			return DEFAULT_SCHEDULER_TIME_MINUTES;
		}
		return l;
	}
}
