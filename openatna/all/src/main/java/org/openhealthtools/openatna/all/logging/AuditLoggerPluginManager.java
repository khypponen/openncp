package org.openhealthtools.openatna.all.logging;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface AuditLoggerPluginManager {

	public void start() throws ClassNotFoundException, InstantiationException, IllegalAccessException;
	
	public void destroy();
	
	public void handleAuditEvent(HttpServletRequest request, Map<String, String> queryParameters, List<Long> messageEntityIds);
	
}
