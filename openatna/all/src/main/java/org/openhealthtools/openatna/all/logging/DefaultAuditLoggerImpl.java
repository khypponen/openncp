package org.openhealthtools.openatna.all.logging;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Dummy implementation of OpenATNALogViewLogger implementation
 */
public class DefaultAuditLoggerImpl implements AuditLogger {
	private static final Log log = LogFactory.getLog(DefaultAuditLoggerImpl.class);

	public void logViewRequest(HttpServletRequest request, Map<String, String> queryParameters,
			List<Long> messageEntityIds) {
		log.info("Cookies: " + cookiesToString(request.getCookies()));
		log.info("Search parameters: " + queryParameters);
		log.info("Logrow entity ids: " + messageEntityIds);
	}

	public void start() {
	}

	public void destroy() {
	}

	private String cookiesToString(Cookie[] cookies) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < cookies.length; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append("[");
			if (cookies[i].getComment() != null) {
				sb.append(" Comment=" + cookies[i].getComment());
			}
			if (cookies[i].getDomain() != null) {
				sb.append(" Domain=" + cookies[i].getDomain());
			}
			if (cookies[i].getMaxAge() != -1) {
				sb.append(" MaxAge=" + cookies[i].getMaxAge());
			}
			if (cookies[i].getName() != null) {
				sb.append(" Name=" + cookies[i].getName());
			}
			if (cookies[i].getPath() != null) {
				sb.append(" Path=" + cookies[i].getPath());
			}
			if (cookies[i].getValue() != null) {
				sb.append(" Value=" + cookies[i].getValue());
			}
			if (cookies[i].getVersion() != 0) {
				sb.append(" Version=" + cookies[i].getVersion());
			}
			sb.append(" Secure=" + cookies[i].getSecure());
			sb.append("]");
		}
		sb.append("]");
		return sb.toString();
	}
}
