package epsos.ccd.gnomon.auditmanager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import eu.epsos.configmanager.database.HibernateConfigFile;
import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class AuditTrailUtilsIntegrationTest extends TestCase {

  @BeforeClass
  public static void setUpClass() throws Exception {

	HibernateConfigFile.name = "src/test/resources/configmanager.hibernate.xml";
  }

  @AfterClass
  public static void tearDownClass() throws Exception {

  }

  @Override
  @Before
  public void setUp() {

  }

  @Override
  @After
  public void tearDown() {

  }

  public void writeXMLToFile(String am, String filename) {

	try {
	  // Create file
	  FileWriter fstream = new FileWriter("/home/karkaletsis/Documents/projects/epsos/" + filename);
	  BufferedWriter out = new BufferedWriter(fstream);
	  out.write(am);
	  // Close the output stream
	  out.close();
	} catch (Exception e) {// Catch exception if any
	  System.err.println("Error: " + e.getMessage());
	}
  }

  @Test
  public void testCreateAuditMessage_epsosPACService() {

	System.out.println("########## createAuditMessage for PAC");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}
	EventLog eventLog1 = EventLog.createEventLogHCPAssurance(TransactionName.epsosPACRetrieve, EventActionCode.QUERY,
	    date2, EventOutcomeIndicator.FULL_SUCCESS, "MassimilianoMasi<saml:massi@saml:test.fr>", "Hospital",
	    "MassimilianoMasi<saml:massi@saml:test.fr>", "AT", "dentist", "Vienna", "USER", "AS-12", "aaa", "aaa",
	    new byte[1],

	    "aaa",

	    "aaa", new byte[1], "aaa", new byte[1], "1.2.3.4", "1.2.3.4");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosPACRetrieve);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	// AuditMessage am =
	// AuditTrailUtils.getInstance().createAuditMessage(eventLog1);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(10000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosConsentServicePin() {

	System.out.println("########### createAuditMessage for Consent Service PIN");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogConsentPINdny(TransactionName.epsosConsentServicePin,
	    EventActionCode.READ, date2, EventOutcomeIndicator.FULL_SUCCESS, "MassimilianoMasi<saml:massi@saml:test.fr>",
	    "Hospital", "MassimilianoMasi<saml:massi@saml:test.fr>", "Massi", "doctor",
	    "MassimilianoMasi<saml:massi@saml:test.fr>", "MassimilianoMasi<saml:massi@saml:test.fr>", "AS-12", "22", "11",
	    new byte[1], "22", new byte[1], "194.219.31.2", "222.33.33.3");

	eventLog1.setEventType(EventType.epsosConsentServicePin);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  // @Test
  // public void testCreateAuditMessage_epsosCommunicationFailure() {
  // System.out.println("createAuditMessage for comm failure");
  // //
  // AuditService asd = new AuditService();
  // GregorianCalendar c = new GregorianCalendar();
  // c.setTime(new Date());
  // XMLGregorianCalendar date2 = null;
  // try {
  // date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
  // } catch (DatatypeConfigurationException ex) {
  //
  // }
  //
  //
  //
  // EventLog eventLog1 = EventLog.createEventLogCommunicationFailure(
  // TransactionName.epsosCommunicationFailure,
  // EventActionCode.QUERY,
  // date2,
  // EventOutcomeIndicator.FULL_SUCCESS,
  //
  // "PC_USERID","Hospital","aaa","dentist","AT","Vienna AKH",null,null,
  //
  // null, new byte[1],
  // null, new byte[1],null,new byte[1],
  // "1.2.3.4","1.2.3.4");
  //
  // // EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
  // "a2", null);
  //
  // eventLog1.setEventType(EventType.epsosCommunicationFailure);
  // //eventLog2.setEventType(EventType.epsosNonRepudiationService);
  // AuditMessage am =
  // AuditTrailUtils.getInstance().createAuditMessage(eventLog1);
  // asd.write(eventLog1, "13", "1");
  // try {
  // Thread.sleep(30000);
  // } catch (InterruptedException ex) {
  // Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE,
  // null, ex);
  // }
  // }

  @Test
  public void testCreateAuditMessage_epsosIdentificationServiceFindIdentityByTraits() {

	System.out.println("######## createAuditMessage XCPD");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogPatientMapping(
	    TransactionName.epsosIdentificationServiceFindIdentityByTraits, EventActionCode.EXECUTE, date2,
	    EventOutcomeIndicator.FULL_SUCCESS, "MassimilianoMasi<saml:massi@saml:test.fr>", "dentist", "Massimiliano Masi",
	    "MassimilianoMasi<saml:massi@saml:test.fr>", "MassimilianoMasi<saml:massi@saml:test.fr>", "AS-12",
	    "AbCD^^122333443", "0", null, new byte[0], "aa", "aa", new byte[1], "aa", new byte[1], "194.219.31.2",
	    "222.33.33.3");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosIdentificationServiceFindIdentityByTraits);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosPatientService() {

	System.out.println("########## createAuditMessage for patient list");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}
	EventLog eventLog1 = EventLog.createEventLogHCPAssurance(TransactionName.epsosPatientServiceList,
	    EventActionCode.QUERY, date2, EventOutcomeIndicator.FULL_SUCCESS, "MassimilianoMasi<saml:massi@saml:test.fr>",
	    "Hospital", "MassimilianoMasi<saml:massi@saml:test.fr>", "AT", "dentist", "Vienna", "USER", "AS-12", "aaa",
	    "aaa", new byte[1],

	    "aaa",

	    "aaa", new byte[1], "aaa", new byte[1], "1.2.3.4", "1.2.3.4");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosPatientServiceList);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	// AuditMessage am =
	// AuditTrailUtils.getInstance().createAuditMessage(eventLog1);
	asd.write(eventLog1, "13", "1");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosOrderService() {

	System.out.println("########## createAuditMessage for Order Service");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogHCPAssurance(TransactionName.epsosOrderServiceList,
	    EventActionCode.READ, date2, EventOutcomeIndicator.FULL_SUCCESS,

	    "MassimilianoMasi<saml:massi@saml:test.fr>", "Hospital", "MassimilianoMasi<saml:massi@saml:test.fr>",
	    "MassimilianoMasi<saml:massi@saml:test.fr>", "dentist", "MassimilianoMasi<saml:massi@saml:test.fr>",
	    "MassimilianoMasi<saml:massi@saml:test.fr>", "AS-12", "22", "333", new byte[1], "patienttarget^^^", "11",
	    new byte[1], "22", new byte[1], "194.219.31.2", "222.33.33.3");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosOrderServiceList);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);

	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosDispensationServiceInit() {

	System.out.println("########## createAuditMessage for Dispensation Service Initialize");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogHCPAssurance(TransactionName.epsosDispensationServiceInitialize,
	    EventActionCode.UPDATE, date2, EventOutcomeIndicator.FULL_SUCCESS,

	    "MassimilianoMasi<saml:massi@saml:test.fr>", "Hospital", "MassimilianoMasi<saml:massi@saml:test.fr>",
	    "Massimiliano", "dentist", "MassimilianoMasi<saml:massi@saml:test.fr>",
	    "MassimilianoMasi<saml:massi@saml:test.fr>", "AS-13", "22", "333", new byte[1], "patienttarget^^^", "11",
	    new byte[1], "22", new byte[1], "194.219.31.2", "222.33.33.3");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosDispensationServiceInitialize);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);

	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosDispensationServiceDiscard() {

	System.out.println("########## createAuditMessage for Dispensation Service Discard");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogHCPAssurance(TransactionName.epsosDispensationServiceDiscard,
	    EventActionCode.DELETE, date2, EventOutcomeIndicator.FULL_SUCCESS,

	    "MassimilianoMasi<saml:massi@saml:test.fr>", "Hospital", "MassimilianoMasi<saml:massi@saml:test.fr>", "Massi",
	    "dentist", "MassimilianoMasi<saml:massi@saml:test.fr>", "SMassimilianoMasi<saml:massi@saml:test.fr>", "AS-12",
	    "22", "333", new byte[1], "patienttarget^^^", "11", new byte[1], "22", new byte[1], "194.219.31.2",
	    "222.33.33.3");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosDispensationServiceDiscard);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosConsentServicePut() {

	System.out.println("########## createAuditMessage for Consent Service Put");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogHCPAssurance(TransactionName.epsosConsentServicePut,
	    EventActionCode.UPDATE, date2, EventOutcomeIndicator.FULL_SUCCESS,

	    "MassimilianoMasi<saml:massi@saml:test.fr>", "Hospital", "MassimilianoMasi<saml:massi@saml:test.fr>", "Massi",
	    "dentist", "MassimilianoMasi<saml:massi@saml:test.fr>", "MassimilianoMasi<saml:massi@saml:test.fr>", "AS-12",
	    "22", "333", new byte[1], "patienttarget^^^", "11", new byte[1], "22", new byte[1], "194.219.31.2",
	    "222.33.33.3");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosConsentServicePut);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosConsentServiceDiscard() {

	System.out.println("########## createAuditMessage for Consent Service Discard");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogHCPAssurance(

	    TransactionName.epsosConsentServiceDiscard, EventActionCode.DELETE, date2, EventOutcomeIndicator.FULL_SUCCESS,

	    "MassimilianoMasi<saml:massi@saml:test.fr>", "Hospital", "Massimiliano Masi",
	    "MassimilianoMasi<saml:massi@saml:test.fr>", "dentist", "MassimilianoMasi<saml:massi@saml:test.fr>",
	    "MassimilianoMasi<saml:massi@saml:test.fr>", "AS-12", "22", "333", new byte[1], "patienttarget^^^", "11",
	    new byte[1], "22", new byte[1], "194.219.31.2", "222.33.33.3");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosConsentServiceDiscard);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosHCPIdentity() {

	System.out.println("########## createAuditMessage for HCP Identity");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogHCPIdentity(TransactionName.epsosHcpAuthentication,
	    EventActionCode.EXECUTE, date2, EventOutcomeIndicator.FULL_SUCCESS,

	    "MassimilianoMasi<saml:massi@saml:test.fr>", "Hospital", "MassimilianoMasi<saml:massi@saml:test.fr>", "dentist",
	    "dentdsdsdsist", "MassimilianoMasi<saml:massi@saml:test.fr>", "MassimilianoMasi<saml:massi@saml:test.fr>",
	    "AS-12",

	    "ssasa",

	    "aa", new byte[1], "aaa", new byte[1], "AA", "AA");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosHcpAuthentication);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosTRCA() {

	System.out.println("########## createAuditMessage for TRCA");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogTRCA(TransactionName.epsosTRCAssertion, EventActionCode.EXECUTE, date2,
	    EventOutcomeIndicator.FULL_SUCCESS,

	    "MassimilianoMasi<saml:massi@saml:test.fr>", "Hospital", "MassimilianoMasi<saml:massi@saml:test.fr>", "dentist",
	    "massi", "MassimilianoMasi<saml:massi@saml:test.fr>", "MassimilianoMasi<saml:massi@saml:test.fr>", "AS-12",

	    "PS_PatricipantObjectID", "ET_ObjectID^^^",

	    "11", new byte[1], "22", new byte[1], "194.219.31.2", "222.33.33.3");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosTRCAssertion);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosNCPTrustedServiceList() {

	System.out.println("########## createAuditMessage for NCPTrustedServiceList");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogNCPTrustedServiceList(TransactionName.epsosNCPTrustedServiceList,
	    EventActionCode.EXECUTE, date2, EventOutcomeIndicator.FULL_SUCCESS,

	    "MassimilianoMasi<saml:massi@saml:test.fr>", "MassimilianoMasi<saml:massi@saml:test.fr>",

	    "ET_ObjectID^^^",

	    "11", new byte[1], "22", new byte[1], "194.219.31.2", "222.33.33.3");

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	eventLog1.setEventType(EventType.epsosNCPTrustedServiceList);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  @Test
  public void testCreateAuditMessage_epsosPivotTranslation() {

	System.out.println("########## createAuditMessage for PivotTranslation");
	//
	AuditService asd = new AuditService();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date2 = null;
	try {
	  date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {

	}

	EventLog eventLog1 = EventLog.createEventLogPivotTranslation(TransactionName.epsosPivotTranslation,
	    EventActionCode.EXECUTE, date2, EventOutcomeIndicator.FULL_SUCCESS,

	    "MassimilianoMasi<saml:massi@saml:test.fr>", "ET_ObjectID^^^", "22", "11", new byte[1], "22", new byte[1],
	    "194.219.31.2");

	eventLog1.setEventType(EventType.epsosPivotTranslation);
	asd.write(eventLog1, "13", "2");
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException ex) {
	  Logger.getLogger(AuditTrailUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}

  }
}
