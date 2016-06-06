package org.openhealthtools.openatna.web;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.service.AuditService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class OpenATNAServiceLoaderListener implements ApplicationContextAware {
    private static final Log log = LogFactory.getLog(OpenATNAServiceLoaderListener.class);
	private static AuditService service = null;
	private ApplicationContext context;

	public synchronized void start() {
		if(service == null) {
			log.info("Starting OpenATNA service..");
			
			AtnaFactory.initialize(context);
			
	        service = (AuditService) context.getBean("auditService");
	        try {
	            service.start();
	        } catch (Exception e) {
	            log.fatal("Unable to start AuditService", e);
	        }
		}
	}

	public synchronized void destroy() {
		if(service != null) {
			try {
				service.stop();
			} catch (IOException e) {
				log.fatal("Unable to stop AuditService.", e);
			}
		}
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;		
	}
}
