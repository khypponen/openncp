/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 Affecto EE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package ee.affecto.epsos.util;

import ee.affecto.epsos.ws.handler.DummyMustUnderstandHandler;
import epsos.ccd.gnomon.auditmanager.AuditService;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import org.apache.axis2.description.HandlerDescription;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;
import org.apache.axis2.phaseresolver.PhaseException;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.util.http.HTTPUtil;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.GregorianCalendar;
import java.util.List;

public class EventLogClientUtil {

    private static Logger LOG = LoggerFactory.getLogger(EventLogClientUtil.class);

    private EventLogClientUtil() {
    }

    public static void createDummyMustUnderstandHandler(org.apache.axis2.client.Stub stub) {
        HandlerDescription description = new HandlerDescription("DummyMustUnderstandHandler");
        description.setHandler(new DummyMustUnderstandHandler());
        AxisConfiguration axisConfiguration = stub._getServiceClient().getServiceContext().getConfigurationContext().getAxisConfiguration();
        List<Phase> phasesList = axisConfiguration.getInFlowPhases();
        Phase myPhase = new Phase("MyPhase");
        try {
            myPhase.addHandler(description);
        } catch (PhaseException ex) {
            throw new RuntimeException(ex);
        }
        phasesList.add(0, myPhase);
        axisConfiguration.setInFaultPhases(phasesList);
    }

    public static String getServerCertificateDN(String epr) {
        String dn = null;

        try {
            // TODO A.R. Silly, very silly  to open connection again just for getting server certificate.
            // But can we get it from AXIS2?

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            String ts = (String) System.getProperties().get("javax.net.ssl.trustStore");

            URL url = new URL(epr);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.connect();
            Certificate certs[] = con.getServerCertificates();
            // Peers certificate is first
            if (certs != null && certs.length > 0) {
                X509Certificate cert = (X509Certificate) certs[0];
                dn = cert.getSubjectDN().getName();
            }
            con.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dn;
    }

    public static String getLocalIpAddress() {
        // TODO A.R. should be changed for multihomed hosts... It's better to get address from AXIS2 actual socket but how?
        String ipAddress = null;
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            LOG.error(null, e);
        }
        return ipAddress;
    }

    public static String getServerIpAddress(String epr) {
        URL url;
        String ipAddress = null;
        try {
            url = new URL(epr);
            ipAddress = InetAddress.getByName(url.getHost()).getHostAddress();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error(null, e);
        }
        return ipAddress;
    }

    public static EventLog prepareEventLog(org.apache.axis2.context.MessageContext msgContext, org.apache.axiom.soap.SOAPEnvelope _returnEnv, String address) {
        EventLog eventLog = new EventLog();

        //eventLog.setEI_EventDateTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        try {
            eventLog.setEI_EventDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        // Set Active Participant Identification: Service Consumer NCP
        eventLog.setSC_UserID(HTTPUtil.getSubjectDN(false));
        eventLog.setSP_UserID(HTTPUtil.getServerCertificate(address));

        // Set Audit Source
        ConfigurationManagerService cms = ConfigurationManagerService.getInstance();
        eventLog.setAS_AuditSourceId(cms.getProperty("COUNTRY_PRINCIPAL_SUBDIVISION"));

        // Set Source Ip
        String localIp = EventLogClientUtil.getLocalIpAddress();
        if (localIp != null) {
            eventLog.setSourceip(localIp);
        }

        // Set Target Ip
        String serverIp = EventLogClientUtil.getServerIpAddress(address);
        if (serverIp != null) {
            eventLog.setTargetip(serverIp);
        }


        // Set Participant Object: Request Message
        String reqMessageId = appendUrnUuid(EventLogUtil.getMessageID(msgContext.getEnvelope()));
        eventLog.setReqM_ParticipantObjectID(reqMessageId);
        eventLog.setReqM_PatricipantObjectDetail(msgContext.getEnvelope().getHeader().toString().getBytes());

        // Set Participant Object: ResponseMessage
        String rspMessageId = appendUrnUuid(EventLogUtil.getMessageID(_returnEnv));
        eventLog.setResM_ParticipantObjectID(rspMessageId);
        eventLog.setResM_PatricipantObjectDetail(_returnEnv.getHeader().toString().getBytes());

        return eventLog;
    }

    public static void logIdAssertion(EventLog eventLog, Assertion idAssertion) {

        ConfigurationManagerService cms = ConfigurationManagerService.getInstance();
        eventLog.setHR_UserID(cms.getProperty("ncp.country") + "<" + idAssertion.getSubject().getNameID().getValue() +
                "@" + cms.getProperty("ncp.country") + ">");
        for (AttributeStatement attributeStatement : idAssertion.getAttributeStatements()) {
            for (Attribute attribute : attributeStatement.getAttributes()) {
                if (attribute.getName().equals("urn:oasis:names:tc:xacml:1.0:subject:subject-id")) {
                    eventLog.setHR_AlternativeUserID(EventLogUtil.getAttributeValue(attribute));
                } else if (attribute.getName().equals("urn:oasis:names:tc:xacml:2.0:subject:role")) {
                    eventLog.setHR_RoleID(EventLogUtil.getAttributeValue(attribute));
                } else if (attribute.getName().equals("urn:epsos:names:wp3.4:subject:healthcare-facility-type")) {
                    eventLog.setPC_RoleID(EventLogUtil.getAttributeValue(attribute));
                } else if (attribute.getName().equals("urn:oasis:names:tc:xspa:1.0:environment:locality")) {
                    eventLog.setPC_UserID(EventLogUtil.getAttributeValue(attribute));
                }
            }
        }

    }

    public static void logTrcAssertion(EventLog eventLog, Assertion idAssertion) {

        for (AttributeStatement attributeStatement : idAssertion.getAttributeStatements()) {
            for (Attribute attribute : attributeStatement.getAttributes()) {
                if (attribute.getName().equals("urn:oasis:names:tc:xacml:1.0:resource:resource-id")) {
                    eventLog.setPT_PatricipantObjectID(EventLogUtil.getAttributeValue(attribute));
                    break;
                }
            }
        }

    }

    public static void sendEventLog(EventLog eventLog) {
        AuditService auditService = new AuditService();
        auditService.write(eventLog, "", "1");
    }

    public static String appendUrnUuid(String uuid) {
        if (uuid == null || uuid.isEmpty() || uuid.startsWith("urn:uuid:")) {
            return uuid;
        } else return "urn:uuid:" + uuid;
    }
}
