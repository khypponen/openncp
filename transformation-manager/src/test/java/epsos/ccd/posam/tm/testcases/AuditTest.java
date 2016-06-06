package epsos.ccd.posam.tm.testcases;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import junit.framework.TestCase;
import epsos.ccd.gnomon.auditmanager.AuditService;
import epsos.ccd.gnomon.auditmanager.EventActionCode;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.auditmanager.EventType;
import epsos.ccd.gnomon.auditmanager.TransactionName;

/**  
 * TM Junit test suite
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.2, 2010, 20 October
 */
public class AuditTest extends TestCase {
	//private static Logger log = LoggerFactory.getLogger(AuditTest.class);

	public void testAudit() {
		try {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			
			EventLog logg=EventLog.createEventLogPivotTranslation(
					TransactionName.epsosPivotTranslation, EventActionCode.EXECUTE,
					DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar), 
					EventOutcomeIndicator.FULL_SUCCESS, 
					"", "", "","",
					new byte[0], "", 
					new byte[0], "");
			logg.setEventType(EventType.epsosPivotTranslation);
			//AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(logg);
			boolean result=new AuditService().write(logg, "testfacility", "testseverity");
			assertTrue(result);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
	}
}
