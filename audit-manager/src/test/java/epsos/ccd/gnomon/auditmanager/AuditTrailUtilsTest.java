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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import java.util.GregorianCalendar;
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
import net.RFC3881.AuditMessage;
import net.RFC3881.AuditMessage.ActiveParticipant;

/**
 * This test class contains solely post implemented tests to verify some existing behavior. These test are necessary to
 * allow the possibility to improve the actual implementation.
 *
 * The original (dummy?) tests are found from a class named 'AuditTrailUtilsIntegrationTest'. These test are not
 * executable as unit tests: In case it is needed to run those tests, you can run them with maven profile
 * 'integration_test_profile'.<br/>
 * Execute integration tests with maven command 'clean test -Pintegration_test_profile' to run these tests.
 *
 * @author nmyrskvi
 */
@RunWith(JUnit4.class)
public class AuditTrailUtilsTest extends TestCase {

  private static final String PC_UserID = "MassimilianoMasi<saml:massi@saml:test.fr>";
  private static final String PC_RoleID = "Hospital";
  private static final String HR_UserID = "MassimilianoMasi<saml:massi@saml:test.fr>";
  private static final String HR_AlternativeUserID = "Massimiliano Masi";
  private static final String HR_RoleID = "dentist";
  private static final String SC_UserID = "Vienna";
  private static final String SP_UserID = "USER";
  private static final String AS_AuditSourceId = "AS-12";
  private static final String PT_PatricipantObjectID = "aaa";
  private static final String EM_PatricipantObjectID = "bbb";
  private static final byte[] EM_PatricipantObjectDetail = new byte[1];
  private static final String ET_ObjectID = "ccc";
  private static final String ReqM_ParticipantObjectID = "ddd";
  private static final byte[] ReqM_PatricipantObjectDetail = new byte[1];
  private static final String ResM_ParticipantObjectID = "eee";
  private static final byte[] ResM_PatricipantObjectDetail = new byte[1];
  private static final String sourceip = "1.2.3.4";
  private static final String targetip = "2.3.4.5";

  private static final String PS_PatricipantObjectID = "AbCD^^122333443";
  private static final String MS_UserID = "fff";

  private static final String ET_ObjectID_in = "ET_ObjectID^^^";
  private static final String ET_ObjectID_out = "22";

  public AuditTrailUtilsTest() {
  }

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
  public void testCreateAuditMessage_epsosPACService_postImplemented() {

	System.out.println("########## createAuditMessage for PAC");

	EventLog eventLog1 = createEventLogHCPAssurance();

	// EventLog eventLog2= EventLog.createEventNonRepudiation("a1", null,
	// "a2", null);

	// eventLog1.setEventType(EventType.epsosPACRetrieve);
	// eventLog2.setEventType(EventType.epsosNonRepudiationService);
	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog1, EventType.epsosPACRetrieve);

	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosPACRetrieve.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
  }

  @Test
  public void testCreateAuditMessage_epsosConsentServicePin_postImplemented() {

	System.out.println("########### createAuditMessage for Consent Service PIN");
	EventLog eventLog1 = this.createEventLogConsentPINdny();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog1, EventType.epsosConsentServicePin);

	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosConsentServicePin.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
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
  public void testCreateAuditMessage_epsosIdentificationServiceFindIdentityByTraits_postImplemented() {

	System.out.println("######## createAuditMessage XCPD");
	EventLog eventLog = this.createEventLogPatientMapping();

	// eventLog1.setEventType(EventType.epsosIdentificationServiceFindIdentityByTraits);
	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog,
	    EventType.epsosIdentificationServiceFindIdentityByTraits);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosIdentificationServiceFindIdentityByTraits.getCode(),
	    am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant hr = am.getActiveParticipant().get(0);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(1);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(2);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	ActiveParticipant mpi = am.getActiveParticipant().get(3);
	assertNotNull(mpi);
	assertNotNull(mpi.getRoleIDCode());
	assertNotNull(mpi.getRoleIDCode().get(0));
	assertEquals("MasterPatientIndex", mpi.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
	this.checkForMasterPatientIndexInfo(mpi);
  }

  @Test
  public void testCreateAuditMessage_epsosPatientService_postImplemented() {

	System.out.println("########## createAuditMessage for patient list");

	EventLog eventLog = createEventLogHCPAssurance();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog, EventType.epsosPatientServiceList);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosPatientServiceList.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
  }

  @Test
  public void testCreateAuditMessage_epsosOrderService_postImplemented() {

	System.out.println("########## createAuditMessage for Order Service");

	EventLog eventLog = this.createEventLogHCPAssurance();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog, EventType.epsosOrderServiceList);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosOrderServiceList.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
  }

  @Test
  public void testCreateAuditMessage_epsosDispensationServiceInit_postImplemented() {

	System.out.println("########## createAuditMessage for Dispensation Service Initialize");
	EventLog eventLog = this.createEventLogHCPAssurance();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog,
	    EventType.epsosDispensationServiceInitialize);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosDispensationServiceInitialize.getCode(),
	    am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
  }

  @Test
  public void testCreateAuditMessage_epsosDispensationServiceDiscard_postImplemented() {

	System.out.println("########## createAuditMessage for Dispensation Service Discard");

	// EventLog eventLog1 = EventLog.createEventLogHCPAssurance(TransactionName.epsosDispensationServiceDiscard,
	// EventActionCode.DELETE, date2, EventOutcomeIndicator.FULL_SUCCESS,
	//
	// "MassimilianoMasi<saml:massi@saml:test.fr>", "Hospital", "MassimilianoMasi<saml:massi@saml:test.fr>", "Massi",
	// "dentist", "MassimilianoMasi<saml:massi@saml:test.fr>", "SMassimilianoMasi<saml:massi@saml:test.fr>", "AS-12",
	// "22", "333", new byte[1], "patienttarget^^^", "11", new byte[1], "22", new byte[1], "194.219.31.2",
	// "222.33.33.3");

	EventLog eventLog = this.createEventLogHCPAssurance();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog,
	    EventType.epsosDispensationServiceDiscard);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosDispensationServiceDiscard.getCode(),
	    am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
  }

  @Test
  public void testCreateAuditMessage_epsosConsentServicePut_postImplemented() {

	System.out.println("########## createAuditMessage for Consent Service Put");

	EventLog eventLog = this.createEventLogHCPAssurance();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog, EventType.epsosConsentServicePut);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosConsentServicePut.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
  }

  @Test
  public void testCreateAuditMessage_epsosConsentServiceDiscard_postImplemented() {

	System.out.println("########## createAuditMessage for Consent Service Discard");

	EventLog eventLog = this.createEventLogHCPAssurance();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog, EventType.epsosConsentServiceDiscard);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosConsentServiceDiscard.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
  }

  @Test
  public void testCreateAuditMessage_epsosHCPIdentity_postImplemented() {

	System.out.println("########## createAuditMessage for HCP Identity");
	EventLog eventLog = this.createEventLogHCPIdentity();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog, EventType.epsosHcpAuthentication);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosHcpAuthentication.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
  }

  @Test
  public void testCreateAuditMessage_epsosTRCA_postImplemented() {

	System.out.println("########## createAuditMessage for TRCA");
	EventLog eventLog = this.createEventLogTRCA();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog, EventType.epsosTRCAssertion);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosTRCAssertion.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(4, am.getActiveParticipant().size());

	ActiveParticipant pc = am.getActiveParticipant().get(0);
	assertNotNull(pc);
	assertNotNull(pc.getRoleIDCode());
	assertNotNull(pc.getRoleIDCode().get(0));
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());

	ActiveParticipant hr = am.getActiveParticipant().get(1);
	assertNotNull(hr);
	assertNotNull(hr.getRoleIDCode());
	assertNotNull(hr.getRoleIDCode().get(0));
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());

	ActiveParticipant sc = am.getActiveParticipant().get(2);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(3);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForPatientCareInfo(pc);
	this.checkForHumanResourceInfo(hr);
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);

  }

  @Test
  public void testCreateAuditMessage_epsosNCPTrustedServiceList_postImplemented() {

	System.out.println("########## createAuditMessage for NCPTrustedServiceList");

	EventLog eventLog = this.createEventLogNCPTrustedServiceList();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog, EventType.epsosNCPTrustedServiceList);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosNCPTrustedServiceList.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(2, am.getActiveParticipant().size());

	ActiveParticipant sc = am.getActiveParticipant().get(0);
	assertNotNull(sc);
	assertNotNull(sc.getRoleIDCode());
	assertNotNull(sc.getRoleIDCode().get(0));
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());

	ActiveParticipant sp = am.getActiveParticipant().get(1);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForServiceConsumerInfo(sc);
	this.checkForServiceProviderInfo(sp);
  }

  @Test
  public void testCreateAuditMessage_epsosPivotTranslation_postImplemented() {

	System.out.println("########## createAuditMessage for PivotTranslation");

	EventLog eventLog = this.createEventLogPivotTranslation();

	AuditMessage am = AuditTrailUtils.getInstance().createAuditMessage(eventLog, EventType.epsosPivotTranslation);
	assertNotNull(am);
	// Some bizarre mapping happens, since this was before I guess this should be the result
	assertEquals(IHEEventType.epsosPivotTranslation.getCode(), am.getEventIdentification().getEventID().getCode());

	// Verify the participant objects
	assertNotNull(am.getActiveParticipant());
	assertEquals(1, am.getActiveParticipant().size());

	ActiveParticipant sp = am.getActiveParticipant().get(0);
	assertNotNull(sp);
	assertNotNull(sp.getRoleIDCode());
	assertNotNull(sp.getRoleIDCode().get(0));
	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());

	// Check that info is mapped at right (?) places (where it was already before adding this test)
	this.checkForServiceProviderInfo(sp);

  }

  /**
   * There is no point in repeating this check phase in every test where this info is supposed to exist. If there is an
   * error at this part, you need to debug or copy-paste these tests to the actual test. This part is to validate that
   * the basic mappings are/stay correct.<br/>
   * Get the participant parts from the AuditMessage created, their order seems to vary between messages, so you need to
   * collect these specifically by the test case.
   *
   * @param pc Patient care (taker) ???
   */
  private synchronized void checkForPatientCareInfo(ActiveParticipant pc) {

	assertNotNull(pc);
	assertEquals(PC_RoleID, pc.getRoleIDCode().get(0).getCode());
	assertNull(pc.getAlternativeUserID());
	assertNull(pc.getNetworkAccessPointID());
	assertEquals(PC_UserID, pc.getUserID());
	assertNull(pc.getUserName());
	assertNull(pc.getNetworkAccessPointTypeCode());
	assertEquals("1.3.6.1.4.1.12559.11.10.1.3.2.2.2", pc.getRoleIDCode().get(0).getCodeSystem());
	assertNull(pc.getRoleIDCode().get(0).getDisplayName());
	assertNull(pc.getRoleIDCode().get(0).getOriginalText());
  }

  /**
   * There is no point in repeating this check phase in every test where this info is supposed to exist. If there is an
   * error at this part, you need to debug or copy-paste these tests to the actual test. This part is to validate that
   * the basic mappings are/stay correct.<br/>
   * Get the participant parts from the AuditMessage created, their order seems to vary between messages, so you need to
   * collect these specifically by the test case.
   *
   * @param hr Human resource ???
   */
  private synchronized void checkForHumanResourceInfo(ActiveParticipant hr) {

	assertNotNull(hr);
	assertEquals(HR_RoleID, hr.getRoleIDCode().get(0).getCode());
	assertEquals(HR_AlternativeUserID, hr.getAlternativeUserID());
	assertNull(hr.getNetworkAccessPointID());
	assertEquals(HR_UserID, hr.getUserID());
	assertNull(hr.getUserName());
	assertNull(hr.getNetworkAccessPointTypeCode());
	assertNull(hr.getRoleIDCode().get(0).getCodeSystem());
	assertNull(hr.getRoleIDCode().get(0).getCodeSystemName());
	assertNull(hr.getRoleIDCode().get(0).getDisplayName());
	assertNull(hr.getRoleIDCode().get(0).getOriginalText());
  }

  /**
   * There is no point in repeating this check phase in every test where this info is supposed to exist. If there is an
   * error at this part, you need to debug or copy-paste these tests to the actual test. This part is to validate that
   * the basic mappings are/stay correct.<br/>
   * Get the participant parts from the AuditMessage created, their order seems to vary between messages, so you need to
   * collect these specifically by the test case.
   *
   * @param sc The service consumer
   */
  private synchronized void checkForServiceConsumerInfo(ActiveParticipant sc) {

	assertNotNull(sc);
	assertEquals("ServiceConsumer", sc.getRoleIDCode().get(0).getCode());
	assertNull(sc.getAlternativeUserID());
	assertEquals(sourceip, sc.getNetworkAccessPointID());
	assertEquals((short) 2, (short) sc.getNetworkAccessPointTypeCode());
	assertEquals("epSOS", sc.getRoleIDCode().get(0).getCodeSystem());
	assertNull(sc.getRoleIDCode().get(0).getCodeSystemName());
	assertEquals("epSOS Service Consumer", sc.getRoleIDCode().get(0).getDisplayName());
	assertNull(sc.getRoleIDCode().get(0).getOriginalText());
	assertEquals(SC_UserID, sc.getUserID());
	assertNull(sc.getUserName());
  }

  /**
   * There is no point in repeating this check phase in every test where this info is supposed to exist. If there is an
   * error at this part, you need to debug or copy-paste these tests to the actual test. This part is to validate that
   * the basic mappings are/stay correct.<br/>
   * Get the participant parts from the AuditMessage created, their order seems to vary between messages, so you need to
   * collect these specifically by the test case.
   *
   * @param sp The service provider
   */
  private synchronized void checkForServiceProviderInfo(ActiveParticipant sp) {

	assertEquals("ServiceProvider", sp.getRoleIDCode().get(0).getCode());
	assertNull(sp.getAlternativeUserID());
	assertEquals(targetip, sp.getNetworkAccessPointID());
	assertEquals("USER", sp.getUserID());
	assertNull(sp.getUserName());
	assertEquals((short) 2, (short) sp.getNetworkAccessPointTypeCode());
	assertEquals("epSOS", sp.getRoleIDCode().get(0).getCodeSystem());
	assertNull(sp.getRoleIDCode().get(0).getCodeSystemName());
	assertEquals("epSOS Service Provider", sp.getRoleIDCode().get(0).getDisplayName());
	assertNull(sp.getRoleIDCode().get(0).getOriginalText());
	assertEquals("USER", sp.getUserID());
  }

  /**
   * There is no point in repeating this check phase in every test where this info is supposed to exist. If there is an
   * error at this part, you need to debug or copy-paste these tests to the actual test. This part is to validate that
   * the basic mappings are/stay correct.<br/>
   * Get the participant parts from the AuditMessage created, their order seems to vary between messages, so you need to
   * collect these specifically by the test case.
   *
   * @param mpi The master patient index
   */
  private synchronized void checkForMasterPatientIndexInfo(ActiveParticipant mpi) {

	assertEquals("MasterPatientIndex", mpi.getRoleIDCode().get(0).getCode());
	assertNull(mpi.getAlternativeUserID());
	assertEquals(targetip, mpi.getNetworkAccessPointID());
	assertEquals("USER", mpi.getUserID());
	assertNull(mpi.getUserName());
	assertEquals((short) 2, (short) mpi.getNetworkAccessPointTypeCode());
	assertEquals("epSOS", mpi.getRoleIDCode().get(0).getCodeSystem());
	assertNull(mpi.getRoleIDCode().get(0).getCodeSystemName());
	assertEquals("Master Patient Index", mpi.getRoleIDCode().get(0).getDisplayName());
	assertNull(mpi.getRoleIDCode().get(0).getOriginalText());
	assertEquals("USER", mpi.getUserID());
  }

  private XMLGregorianCalendar getXMLDate() {

	GregorianCalendar c = new GregorianCalendar();
	c.setTime(new Date());
	XMLGregorianCalendar date = null;
	try {
	  date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	} catch (DatatypeConfigurationException ex) {
	  ex.printStackTrace();
	  fail("DatatypeConfigurationException when creating gregorian calendar");
	}
	return date;
  }

  private EventLog createEventLogHCPAssurance() {

	return EventLog.createEventLogHCPAssurance(TransactionName.epsosPACRetrieve, EventActionCode.QUERY, getXMLDate(),
	    EventOutcomeIndicator.FULL_SUCCESS, PC_UserID, PC_RoleID, HR_UserID, HR_AlternativeUserID, HR_RoleID, SC_UserID,
	    SP_UserID, AS_AuditSourceId, PT_PatricipantObjectID, EM_PatricipantObjectID, EM_PatricipantObjectDetail,
	    ET_ObjectID, ReqM_ParticipantObjectID, ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID,
	    ResM_PatricipantObjectDetail, sourceip, targetip);
  }

  private EventLog createEventLogPatientMapping() {

	return EventLog.createEventLogPatientMapping(TransactionName.epsosIdentificationServiceFindIdentityByTraits,
	    EventActionCode.EXECUTE, getXMLDate(), EventOutcomeIndicator.FULL_SUCCESS, HR_UserID, HR_RoleID,
	    HR_AlternativeUserID, SC_UserID, SP_UserID, AS_AuditSourceId, PS_PatricipantObjectID, PT_PatricipantObjectID,
	    EM_PatricipantObjectID, EM_PatricipantObjectDetail, MS_UserID, ReqM_ParticipantObjectID,
	    ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID, ResM_PatricipantObjectDetail, sourceip, targetip);
  }

  private EventLog createEventLogConsentPINdny() {

	return EventLog.createEventLogConsentPINdny(TransactionName.epsosConsentServicePin, EventActionCode.READ,
	    getXMLDate(), EventOutcomeIndicator.FULL_SUCCESS, PC_UserID, PC_RoleID, HR_UserID, HR_AlternativeUserID,
	    HR_RoleID, SC_UserID, SP_UserID, AS_AuditSourceId, PT_PatricipantObjectID, ReqM_ParticipantObjectID,
	    ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID, ResM_PatricipantObjectDetail, sourceip, targetip);
  }

  private EventLog createEventLogHCPIdentity() {

	return EventLog.createEventLogHCPIdentity(TransactionName.epsosHcpAuthentication, EventActionCode.EXECUTE,
	    getXMLDate(), EventOutcomeIndicator.FULL_SUCCESS, PC_UserID, PC_RoleID, HR_UserID, HR_RoleID,
	    HR_AlternativeUserID, SC_UserID, SP_UserID, AS_AuditSourceId, ET_ObjectID, ReqM_ParticipantObjectID,
	    ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID, ResM_PatricipantObjectDetail, sourceip, targetip);
  }

  private EventLog createEventLogTRCA() {

	return EventLog.createEventLogTRCA(TransactionName.epsosTRCAssertion, EventActionCode.EXECUTE, getXMLDate(),
	    EventOutcomeIndicator.FULL_SUCCESS, PC_UserID, PC_RoleID, HR_UserID, HR_RoleID, HR_AlternativeUserID, SC_UserID,
	    SP_UserID, AS_AuditSourceId, PT_PatricipantObjectID, ET_ObjectID, ReqM_ParticipantObjectID,
	    ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID, ResM_PatricipantObjectDetail, sourceip, targetip);
  }

  private EventLog createEventLogNCPTrustedServiceList() {

	return EventLog.createEventLogNCPTrustedServiceList(TransactionName.epsosNCPTrustedServiceList,
	    EventActionCode.EXECUTE, getXMLDate(), EventOutcomeIndicator.FULL_SUCCESS, SC_UserID, SP_UserID, ET_ObjectID,
	    ReqM_ParticipantObjectID, ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID, ResM_PatricipantObjectDetail,
	    sourceip, targetip);
  }

  private EventLog createEventLogPivotTranslation() {

	  String auditSourceId = "asid";
	return EventLog.createEventLogPivotTranslation(TransactionName.epsosPivotTranslation, EventActionCode.EXECUTE,
	    getXMLDate(), EventOutcomeIndicator.FULL_SUCCESS, SP_UserID, ET_ObjectID_in, ET_ObjectID_out,
	    ReqM_ParticipantObjectID, ReqM_PatricipantObjectDetail, ResM_ParticipantObjectID, ResM_PatricipantObjectDetail,
	    targetip, auditSourceId);
  }
}