/***Licensed to the Apache Software Foundation (ASF) under one
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package epsos.ccd.gnomon.auditmanager;

import epsos.ccd.gnomon.auditmanager.ssl.AuthSSLSocketFactory;
import epsos.ccd.gnomon.auditmanager.ssl.KeystoreDetails;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.gnomon.utils.SerializableMessage;
import epsos.ccd.gnomon.utils.Utils;
import eu.epsos.util.audit.AuditLogSerializer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import javax.net.ssl.SSLSocket;
import net.RFC3881.AuditMessage;
import org.apache.log4j.Logger;

/**
 * Thread for sending the messages to the syslog repository. Each message is
 * being sent using a different thread. If a message can;t be send immediately,
 * it tries for a time interval
 * 
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 */
public class MessageSender extends java.lang.Thread {
	private static final Logger log = Logger.getLogger(MessageSender.class);

	private AuditLogSerializer auditLogSerializer;
	private AuditMessage auditmessage;
	private String facility;
	private String severity;

	private static String enabledProtocols[] = { "TLSv1" };
	private static String AUDIT_REPOSITORY_URL = "audit.repository.url";
	private static String AUDIT_REPOSITORY_PORT = "audit.repository.port";
	private static String KEYSTORE_FILE = "NCP_SIG_KEYSTORE_PATH";
	private static String KEYSTORE_PWD = "NCP_SIG_KEYSTORE_PASSWORD";
	private static String TRUSTSTORE = "TRUSTSTORE_PATH";
	private static String TRUSTSTORE_PWD = "TRUSTSTORE_PASSWORD";
	private static String KEY_ALIAS = "NCP_SIG_PRIVATEKEY_ALIAS";

	public MessageSender(AuditLogSerializer auditLogSerializer, AuditMessage auditmessage, String facility, String severity) {
		super();
		this.auditLogSerializer = auditLogSerializer;
		this.auditmessage = auditmessage;
		this.facility = facility;
		this.severity = severity;
	}

	public void run() {
		boolean sent = false;
		log.info("Try to construct the message");
		try {
			log.info(auditmessage.getEventIdentification().getEventTypeCode().get(0).getCode() + " Try to construct the message");
			String auditmsg = AuditTrailUtils.constructMessage(auditmessage, true);
			log.debug(auditmsg);
			
			if(!Utils.isEmpty(auditmsg)) {
				long timeout = Long.parseLong( Utils.getProperty("audit.time.to.try", "60000", true) );
				boolean timeouted = false;
				log.info("Try to send the message for " + timeout + " msec");
				timeout += System.currentTimeMillis();

				do {
					try {
						sent = sendMessage(auditmsg, facility, severity);
					} catch(Exception e) {
						log.error(e.getMessage(), e);
					}
					timeouted = System.currentTimeMillis() > timeout;
					if(!sent && !timeouted) {
						Utils.sleep(1000);
					}
				} while(!sent && !timeouted);
				
				if(timeouted) {
					log.info("The time set to epsos.properties in order to retry sending the audit has passed");
				}
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if(!sent) {
				if(auditLogSerializer != null) {
					auditLogSerializer.writeObjectToFile(new SerializableMessage(auditmessage, facility, severity));
				} else {
					log.info("Failed to send backuped audit message to OpenATNA. Retry later.");
				}
			}
		}
	}

	/**
	 * This class is responsible for sending the audit message to the
	 * repository. Creates UDP logs for every step.
	 * 
	 * @param auditmessage
	 * @param facility
	 * @param severity
	 * @return true/false depending of the success of sending the message
	 */
	protected boolean sendMessage(String auditmsg, String facility, String severity) {
		boolean sent = false;
		String facsev = facility + severity;
		ConfigurationManagerService cms = ConfigurationManagerService.getInstance();

		String host = cms.getProperty(AUDIT_REPOSITORY_URL); // "office.tiani-spirit.com";
		int port = Integer.parseInt(cms.getProperty(AUDIT_REPOSITORY_PORT));
		if (log.isDebugEnabled()) {
			log.debug("Set the security properties");
			log.debug(cms.getProperty(KEYSTORE_FILE));
			log.debug(cms.getProperty(KEYSTORE_PWD));
			log.debug(cms.getProperty(TRUSTSTORE));
			log.debug(cms.getProperty(TRUSTSTORE_PWD));
			log.debug(cms.getProperty(KEY_ALIAS));
		}

		if (log.isTraceEnabled()) {
			try {
				KeyStore ks = KeyStore.getInstance("JKS");
				ks.load(Utils.fullStream(cms.getProperty(KEYSTORE_FILE)), cms.getProperty(KEYSTORE_PWD).toCharArray());
				X509Certificate cert = (X509Certificate) ks.getCertificate(cms.getProperty(KEY_ALIAS));
				log.trace("KEYSTORE");
				log.trace(cert.toString());
				KeyStore ks1 = KeyStore.getInstance("JKS");
				ks1.load(Utils.fullStream(cms.getProperty(TRUSTSTORE)), cms.getProperty(TRUSTSTORE_PWD).toCharArray());
				Enumeration<String> enu = ks1.aliases();
				int i = 0;
				while (enu.hasMoreElements()) {
					i++;
					String alias = enu.nextElement();
					log.trace("ALIAS " + i + " " + alias);
					log.trace(ks1.getCertificate(alias));
				}
			} catch (Exception e) {
				log.error("Error logging keystore file", e);
			}
		}

		BufferedOutputStream bos = null;
		SSLSocket sslsocket = null;
		try {
			log.debug(auditmessage.getEventIdentification().getEventID().getCode() + " Initialize the SSL socket");
			File u = new File(cms.getProperty(TRUSTSTORE));
			KeystoreDetails trust = new KeystoreDetails(u.toString(), cms.getProperty(TRUSTSTORE_PWD),
					cms.getProperty(KEY_ALIAS));
			File uu = new File(cms.getProperty(KEYSTORE_FILE));
			KeystoreDetails key = new KeystoreDetails(uu.toString(), cms.getProperty(KEYSTORE_PWD),
					cms.getProperty(KEY_ALIAS), cms.getProperty(KEYSTORE_PWD));
			AuthSSLSocketFactory f = new AuthSSLSocketFactory(key, trust);
			log.debug(auditmessage.getEventIdentification().getEventID().getCode() + " Create socket");

			sslsocket = (SSLSocket) f.createSecureSocket(host, port);
			log.debug(auditmessage.getEventIdentification().getEventID().getCode() + " Enabling protocols");
			sslsocket.setEnabledProtocols(enabledProtocols);

			String[] suites = sslsocket.getSupportedCipherSuites();
			sslsocket.setEnabledCipherSuites(suites);

			bos = new BufferedOutputStream(sslsocket.getOutputStream());
			// Syslog Header
			String hostName = sslsocket.getLocalAddress().getHostName();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date now = new Date();
			StringBuffer nowStr = new StringBuffer(sdf.format(now));
			if (nowStr.charAt(4) == '0') {
				nowStr.setCharAt(4, ' ');
			}
			
			String header = "<" + facsev + ">1 " + nowStr + " " + hostName + " - - - - ";

			// set body of syslog message.
			int length = header.getBytes().length + 3 + auditmsg.getBytes().length;
			bos.write((length + " ").getBytes());
			bos.write(header.getBytes());
			// Sets the bom for utf-8
	        bos.write( new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF} );
			bos.flush();
			log.debug(auditmessage.getEventIdentification().getEventID().getCode() + " Write the object to bos");
			// Write the syslog message to repository
			bos.write(auditmsg.getBytes());
			log.info(auditmessage.getEventIdentification().getEventID().getCode() + " Message sent");
			sent = true;
		} catch (Exception e) {
			log.error(auditmessage.getEventIdentification().getEventID().getCode() + " Error sending message" + e.getMessage(), e);
		} finally {
			// closes the boom and the socket
			Utils.close(bos);
			try {
				if (sslsocket != null)
					sslsocket.close();
			} catch (IOException e) {
				log.warn("Unable to close SSLSocket", e);
			}
		}

		return sent;
	}
}
