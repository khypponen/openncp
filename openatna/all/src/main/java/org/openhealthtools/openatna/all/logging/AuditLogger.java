package org.openhealthtools.openatna.all.logging;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface AuditLogger {
	
	public void start();
	
	public void logViewRequest(HttpServletRequest request, Map<String,String> queryParameters, List<Long> messageEntityIds);
	
	public void destroy();
}
