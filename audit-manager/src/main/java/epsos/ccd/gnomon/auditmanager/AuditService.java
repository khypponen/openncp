/**
 *Licensed to the Apache Software Foundation (ASF) under one
 *or more contributor license agreements.  See the NOTICE file
 *distributed with this work for additional information
 *regarding copyright ownership.  The ASF licenses this file
 *to you under the Apache License, Version 2.0 (the
 *"License"); you may not use this file except in compliance
 *with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 **/

package epsos.ccd.gnomon.auditmanager;

import java.io.Serializable;

import net.RFC3881.AuditMessage;
import org.apache.log4j.Logger;

import epsos.ccd.gnomon.utils.SerializableMessage;
import eu.epsos.util.audit.AuditLogSerializer.Type;
import eu.epsos.util.audit.AuditLogSerializer;
import eu.epsos.util.audit.AuditLogSerializerImpl;
import eu.epsos.util.audit.FailedLogsHandlerService;
import eu.epsos.util.audit.FailedLogsHandlerServiceImpl;
import eu.epsos.util.audit.MessageHandlerListener;

/**
 * 
 * This service provides access to the system defined properties
 * 
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 * @see net.RFC3881 http://www.rfc3881.net/ generated classes using JAXB Library
 *      for populating audit trail entries
 */
public class AuditService implements MessageHandlerListener {
    public static final String KEY_TIME_BETWEEN_FAILED_LOGS_HANDLING = "time.between.failed.logs.handling";
    public static final long DEFAULT_TIME_BETWEEN = 1 * 60 * 60 * 1000; // 1h

    private static Logger log = Logger.getLogger(AuditService.class);
	private FailedLogsHandlerService failedLogsHandlerService;
	private AuditLogSerializer auditLogSerializer;

	public AuditService() {
		initialize();
	}
	
	protected void initialize() {
		Type type = Type.AUDIT_MANAGER;
		auditLogSerializer = new AuditLogSerializerImpl(type);
		failedLogsHandlerService = new FailedLogsHandlerServiceImpl(this, type);
		failedLogsHandlerService.start();
	}
	
	/**
	 * Provides a method to write an Audit Log.
	 * 
	 * @param el
	 * @param facility
	 *            the facility number according to log4j
	 * @param severity
	 *            the severity of the message
	 * @return true if auditLog is attempted to be sent
	 */
	public synchronized Boolean write(Object el, String facility, String severity) {
		try {
			if(el instanceof EventLog) {
				EventLog eventLog = (EventLog) el;
				AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog);
				log.debug("Start of AuditLog transmission");
				AuditTrailUtils.getInstance().sendATNASyslogMessage(auditLogSerializer, am, facility, severity);
			} else if(el instanceof AuditMessage) {
				AuditMessage am = (AuditMessage) el;
				log.debug("Start of AuditLog transmission of backuped audit log");
				AuditTrailUtils.getInstance().sendATNASyslogMessage(null, am, facility, severity);
			} else {
				throw new IllegalArgumentException("Unsupported message format: " + el.getClass().getCanonicalName());
			}
			return true;
		} catch(Exception e) {
			log.warn(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean handleMessage(Serializable message) {
		if(message != null && message instanceof SerializableMessage) {
			SerializableMessage sm = (SerializableMessage)message;
			boolean ok = write(sm.getMessage(), sm.getFacility(), sm.getSeverity());
			log.info("Attempt to write message to OpenATNA server. Result " + ok);
			return ok;
		} else {
			log.warn("Message null or unknown type! Cannot handle message.");
			return false;
		}
	}
}
