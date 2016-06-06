package org.openhealthtools.openatna.all.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuditLoggerPluginManagerImpl implements AuditLoggerPluginManager {
    private static final Log log = LogFactory.getLog(AuditLoggerPluginManagerImpl.class);
	private List<AuditLogger> auditLoggers = new ArrayList<AuditLogger>();
	private String loggers = null;
	private String splitChar = ",";
	private String notDefinedLoggersValue = "${openATNA.auditLoggers}";

	public void start() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		log.info("Starting AuditLoggerPluginManager");
		
		if(loggers == null || loggers.length() == 0 || notDefinedLoggersValue.equals(loggers)) {
			log.info("No auditloggers defined. Using DefaultAuditLoggerImpl.");
			auditLoggers.add( new DefaultAuditLoggerImpl() );
		} else {
			String[] classes = loggers.split(splitChar);
			for(String clazz : classes) {
				log.info("Initializing auditlogger " + clazz + ".");
				Class<AuditLogger> c = (Class<AuditLogger>) Class.forName(clazz);
				AuditLogger logger = c.newInstance();
				auditLoggers.add(logger);
			}
		}

		for(AuditLogger al : auditLoggers) {
			log.info("Starting auditlogger " + al.getClass().getName() + ".");
			al.start();
		}
	}

	public void destroy() {
		for(AuditLogger al : auditLoggers) {
			try {
				log.info("Destroying auditlogger " + al.getClass().getName() + ".");
				al.destroy();
			} catch(Exception e) {
				log.fatal("Unable to destroy AuditLogger!", e);
			}
		}
	}
	
	public void handleAuditEvent(HttpServletRequest request, Map<String, String> queryParameters, List<Long> messageEntityIds) {
		for(AuditLogger al : auditLoggers) {
			log.info("Forwarding auditEvent for auditlogger " + al.getClass().getName() + ".");
			al.logViewRequest(request, queryParameters, messageEntityIds);
		}
	}

	public String getSplitChar() {
		return splitChar;
	}

	public void setSplitChar(String splitChar) {
		this.splitChar = splitChar;
	}

	public String getLoggers() {
		return loggers;
	}

	public void setLoggers(String loggers) {
		this.loggers = loggers;
	}
}
