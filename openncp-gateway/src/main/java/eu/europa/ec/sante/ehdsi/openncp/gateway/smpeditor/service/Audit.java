package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import epsos.ccd.gnomon.auditmanager.AuditService;
import epsos.ccd.gnomon.auditmanager.EventActionCode;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.auditmanager.EventType;
import epsos.ccd.gnomon.auditmanager.TransactionName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Inês Garganta
 */
@Service
public class Audit {
  
  static Logger logger = LoggerFactory.getLogger(Audit.class);
  
  public static void sendAuditQuery(String sc_fullname, String sc_email, String sp_fullname, String sp_email, String partid, String sourceip, String targetip,
          String objectID, String EM_PatricipantObjectID, byte[] EM_PatricipantObjectDetail) {
    try {
      AuditService asd = new AuditService();
      GregorianCalendar c = new GregorianCalendar();
      c.setTime(new Date());
      XMLGregorianCalendar date2 = null;
      try {
        date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
      } catch (DatatypeConfigurationException ex) {
        logger.error(null, ex);
      }
      
      /*
       *
       * @param EI_TransactionName
       * @param EI_EventActionCode
       * @param EI_EventDateTime
       * @param EI_EventOutcomeIndicator
       * @param xHR_UserID
       * @param xHR_AlternativeUserID
       * @param xHR_RoleID
       * @param SC_UserID
       * @param SP_UserID
       * @param AS_AuditSourceId
       * @param xPT_PatricipantObjectID
       * @param EM_PatricipantObjectID
       * @param EM_PatricipantObjectDetail
       * @param ET_ObjectID
       * @param ReqM_ParticipantObjectID
       * @param ReqM_PatricipantObjectDetail
       * @param ResM_ParticipantObjectID
       * @param ResM_PatricipantObjectDetail
       * @param sourceip
       * @param targetip
       * @return
       */
      String sc_userid = sc_fullname + "<saml:" + sc_email + ">";
      String sp_userid = sp_fullname + "<saml:" + sp_email + ">";
      EventLog eventLog1 = EventLog.createEventLogPatientPrivacy(
              TransactionName.ehealthSMPQuery,
              EventActionCode.EXECUTE,
              date2,
              EventOutcomeIndicator.FULL_SUCCESS,
              null, null, null,
              sc_userid,
              sp_userid,
              partid,
              null, 
              EM_PatricipantObjectID,
              EM_PatricipantObjectDetail,
              objectID,
              "urn:uuid:00000000-0000-0000-0000-000000000000",
              new byte[1],
              "urn:uuid:00000000-0000-0000-0000-000000000000",
              new byte[1], // Base64 encoded error message
              sourceip, targetip);
      eventLog1.setEventType(EventType.ehealthSMPQuery);
      //facility = 13 --> log audit | severity = 2 --> Critical: critical conditions
      //Acording to https://tools.ietf.org/html/rfc5424 (Syslog Protocol)
      asd.write(eventLog1, "13", "2");
    /*  try {
        Thread.sleep(10000);
      } catch (InterruptedException ex) {
        logger.error(null, ex);
      }*/
    } catch (Exception e) {
      logger.error("Error sending audit for eHealth SMP Query");
    }
  }
  
  
  public static void sendAuditPush(String sc_fullname, String sc_email, String sp_fullname, String sp_email, String partid, String sourceip, 
          String targetip, String objectID, String EM_PatricipantObjectID, byte[] EM_PatricipantObjectDetail) {
    try {
      AuditService asd = new AuditService();
      GregorianCalendar c = new GregorianCalendar();
      c.setTime(new Date());
      XMLGregorianCalendar date2 = null;
      try {
        date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
      } catch (DatatypeConfigurationException ex) {
        logger.error(null, ex);
      }
      
      /*
       *
       * @param EI_TransactionName
       * @param EI_EventActionCode
       * @param EI_EventDateTime
       * @param EI_EventOutcomeIndicator
       * @param xHR_UserID
       * @param xHR_AlternativeUserID
       * @param xHR_RoleID
       * @param SC_UserID
       * @param SP_UserID
       * @param AS_AuditSourceId
       * @param xPT_PatricipantObjectID
       * @param EM_PatricipantObjectID
       * @param EM_PatricipantObjectDetail
       * @param ET_ObjectID
       * @param ReqM_ParticipantObjectID
       * @param ReqM_PatricipantObjectDetail
       * @param ResM_ParticipantObjectID
       * @param ResM_PatricipantObjectDetail
       * @param sourceip
       * @param targetip
       * @return
       */
      String sc_userid = sc_fullname + "<saml:" + sc_email + ">";
      String sp_userid = sp_fullname + "<saml:" + sp_email + ">"; 
      EventLog eventLog1 = EventLog.createEventLogPatientPrivacy(
              TransactionName.ehealthSMPPush,
              EventActionCode.EXECUTE,
              date2,
              EventOutcomeIndicator.FULL_SUCCESS,
              null, null, null,
              sc_userid,
              sp_userid,
              partid,
              null, 
              EM_PatricipantObjectID,
              EM_PatricipantObjectDetail,
              objectID,
              "urn:uuid:00000000-0000-0000-0000-000000000000",
              new byte[1],
              "urn:uuid:00000000-0000-0000-0000-000000000000",
              new byte[1], // Base64 encoded error message
              sourceip, targetip);
      eventLog1.setEventType(EventType.ehealthSMPPush);
      //facility = 13 --> log audit | severity = 2 --> Critical: critical conditions
      //Acording to https://tools.ietf.org/html/rfc5424 (Syslog Protocol)
      asd.write(eventLog1, "13", "2"); 
     /* try {
        Thread.sleep(10000);
      } catch (InterruptedException ex) {
        logger.error(null, ex);
      }*/
    } catch (Exception e) {
      logger.error("Error sending audit for eHealth SMP Push");
    }
  }

}
