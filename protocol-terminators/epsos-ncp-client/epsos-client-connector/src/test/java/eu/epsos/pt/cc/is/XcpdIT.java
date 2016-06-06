/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.pt.cc.is;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.pt.cc.ClientGenericIT;
import java.util.ArrayList;
import java.util.Collection;
import javax.naming.NamingException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opensaml.saml2.core.Assertion;

/**
 * XCPD Integration Testing class.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class XcpdIT extends ClientGenericIT {

    /**
     * Folder under test/resources were are soap requests for this tests.
     */
    private static final String REQ_FOLDER = "xcpd/";

    @BeforeClass
    public static void setUpClass() throws NamingException {
        ClientGenericIT.setUpClass();
        LOG.info("----------------------------");
        LOG.info(" Query Patient");
        LOG.info("----------------------------");
    }

    /*
     * Normal usage
     */
    @Test
    public void testQuery() {
        assertions = getAssertions(REQ_FOLDER + "PT_CLIENT_XCPD_#0.xml", XSPARole.PHYSICIAN);
        testGood("PT_CLIENT_XCPD_#0", REQ_FOLDER + "PT_CLIENT_XCPD_#0.xml");
    }


    /*
     * Invalid scenarios
     * see D3.4.2 | 3.2.1.9
     */
    /**
     * (WARNING)
     *
     * The service requestor tried an identification based on an ID only or did
     * not provide enough data to univocally identify the patient. (WARNING) The
     * HCP SHOULD ask the patient for further demographics and re-issue the
     * request.
     *
     * AA (application accept) is returned in Acknowledgement.typeCode
     * (transmission wrapper). OK (data found, no errors) is returned in
     * QueryAck.queryResponseCode (control act wrapper)
     */
    @Test
    @Ignore
    public void testQueryIdOnly() {
        testFailScenario("PT_CLIENT_XCPD_#1", "AdditionalDemographicsRequested", REQ_FOLDER + "PT_CLIENT_XCPD_#1.xml");
    }

    /**
     * (WARNING)
     *
     * The service provider only allows for patient identification by
     * national/shared ID (WARNING) The HCP SHOULD ask the patient for a
     * national (health care) identification card and reissue the request using
     * Shared/national Patient Identifier Query and Feed mode.
     *
     * AA (application accept) is returned in Acknowledgement.typeCode
     * (transmission wrapper). AE (application error) is returned in
     * QueryAck.queryResponseCode (control act wrapper)
     */
    @Test
    @Ignore
    public void testQueryDemographicsOnly() {
        testFailScenario("PT_CLIENT_XCPD_#2", "DemographicsQueryNotAllowed", REQ_FOLDER + "PT_CLIENT_XCPD_#2.xml");
    }

    /**
     * (WARNING)
     *
     * The service provider only allows for patient identification by national
     * health card or EHIC. Queries based on demographics only are not supported
     * (WARNING) The HCP SHOULD ask the patient for a health care identification
     * card and re-issue the request.
     *
     * AA (application accept) is returned in Acknowledgement.typeCode
     * (transmissionwrapper). AE (application error) is returned in
     * QueryAck.queryResponseCode (control act wrapper)
     */
    @Test
    @Ignore
    public void testQueryEHICardOnly() {
        testFailScenario("PT_CLIENT_XCPD_#3", "EHICDataRequested", REQ_FOLDER + "PT_CLIENT_XCPD_#3.xml");
    }

    /**
     * (ERROR)
     *
     * The service provider does not accept the query because responding MAY
     * lead to a disclosure of private patient data (ERROR). The HCP SHOULD
     * limit the provided traits and re-issue the request.
     *
     * AA (application accept) is returned in Acknowledgement.typeCode
     * (transmission wrapper). AE (application error) is returned in
     * QueryAck.queryResponseCode (control act wrapper)
     */
    @Test
    @Ignore
    public void testQueryPrivateData() {
        testFailScenario("PT_CLIENT_XCPD_#4", "PrivacyViolation", REQ_FOLDER + "PT_CLIENT_XCPD_#4.xml");
    }

    /**
     * (ERROR)
     *
     * The requestor has insufficient rights to query for patient’s identity
     * data (ERROR). If access to the patient’s medical data is required at the
     * PoC this MUST be performed by a person with additional permissions.
     *
     * AA (application accept) is returned in Acknowledgement.typeCode
     * (transmission wrapper). AE (application error) is returned in
     * QueryAck.queryResponseCode (control act wrapper)
     */
    @Test
    public void testQueryInsRights() {
        this.assertions = super.hcpAssertionCreate(new ArrayList<String>(0), XSPARole.PHYSICIAN);

        testFailScenario("PT_CLIENT_XCPD_#5", "InsufficientRights", REQ_FOLDER + "PT_CLIENT_XCPD_#0.xml");
    }

    /**
     * (ERROR)
     *
     * Patient authentication MUST be piggybacked with patient identification. A
     * respective identifier (e.g. GSS TAN) was not provided (ERROR) The HCP at
     * the PoC SHOULD ask the patient for a respective identifier and SHOULD
     * re-issue the request.
     *
     * AA (application accept) is returned in Acknowledgement.typeCode
     * (transmission wrapper). AE (application error) is returned in
     * QueryAck.queryResponseCode (control act wrapper)
     */
    @Test
    @Ignore
    public void testQueryPatientAuthId() {
        testFailScenario("PT_CLIENT_XCPD_#6", "PatientAuthenticationRequired", REQ_FOLDER + "PT_CLIENT_XCPD_#6.xml");
    }

    /**
     * (INFO)
     *
     * The service provider did not find a match with the given minimum
     * accuracy. (INFO) The service consumer SHOULD re-issue the request with a
     * lower minimum confidence level.
     *
     * AA (application accept) is returned in Acknowledgement.typeCode
     * (transmission wrapper). OK (data found) is returned in
     * QueryAck.queryResponseCode (control act wrapper)
     */
    @Test
    public void testQueryMinAccur() {
        this.assertions = getAssertions(epr, XSPARole.PHYSICIAN);
        testFailScenario("PT_CLIENT_XCPD_#7", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#7.xml");
    }

    /**
     * (ERROR)
     *
     * The identity traits provided by the service consumer are not supported by
     * the service provider. (ERROR) The service consumer SHOULD re-issue the
     * request with a different set of identity traits.
     *
     * AA (application accept) is returned in Acknowledgement.typeCode
     * (transmission wrapper). AE (application error) is returned in
     * QueryAck.queryResponseCode (control act wrapper)
     */
    @Test
    @Ignore
    public void testQueryInvalidTraits() {
        testFailScenario("PT_CLIENT_XCPD_#8", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#8.xml");
    }

    /**
     * (INFO)
     *
     * The service consumer defined a confidence level that conflicts with the
     * security policy of the service provider. (INFO) The service provider
     * SHOULD respond only with the candidate matches that it is allowed to
     * provide wrt. its security policy.
     *
     * AA (application accept) is returned in Acknowledgement.typeCode
     * (transmission wrapper). AE (application error) is returned in
     * QueryAck.queryResponseCode (control act wrapper)
     */
    @Test
    @Ignore
    public void testQueryConfConflict() {
        testFailScenario("PT_CLIENT_XCPD_#9", "PolicyViolation", REQ_FOLDER + "PT_CLIENT_XCPD_#9.xml");
    }

    /*
     * Usage restrictions
     * see D3.4.2 | 3.2.1.2
     */
    /**
     * <p>
     * Asserts that a NOT well formulated &lt;LivingSubjectID&gt; results in a
     * NOT valid response.</p>
     *
     * <p>
     * Normal usage condition: SHOULD contain zero or more living subject Id.
     * When present, it shall contain both an assigning authority identifier
     * (root) and individual ID (extension). If multiple subject IDs are given
     * for the same patient, each identifier MUST be provided as a dedicated
     * &lt;LivingSubject- ID/&gt; element.</p>
     */
    @Test
    public void testBadLivingSubjectID() {
        this.assertions = getAssertions(epr, XSPARole.PHYSICIAN);
        testFailScenario("PT_CLIENT_XCPD_#10", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#10.xml");
    }

    /**
     * <p>
     * Asserts that a well formulated &lt;LivingSubjectID&gt; results in a valid
     * response.</p>
     *
     * <p>
     * Normal usage condition: SHOULD contain zero or more living subject Id.
     * When present, it shall contain both an assigning authority identifier
     * (root) and individual ID (extension). If multiple subject IDs are given
     * for the same patient, each identifier MUST be provided as a dedicated
     * &lt;LivingSubject- ID/&gt; element.</p>
     */
    @Test
    public void testGoodLivingSubjectID() {
        this.assertions = getAssertions(epr, XSPARole.PHYSICIAN);
        testGood("PT_CLIENT_XCPD_#10.1", REQ_FOLDER + "PT_CLIENT_XCPD_#10.1.xml");
    }

    /**
     * <p>
     * Asserts that a NOT well formulated &lt;LivingSubjectName&gt; results in a
     * NOT valid response.</p>
     *
     * <p>
     * Normal usage condition: Family name and given name MUST both be
     * given.</p>
     */
    @Test
    @Ignore
    public void testBadLivingSubjectName() {
        testFailScenario("PT_CLIENT_XCPD_#11", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#11.xml");
    }

    /**
     * <p>
     * Asserts that a well formulated &lt;LivingSubjectName&gt; results in a
     * valid response.</p>
     *
     * <p>
     * Normal usage condition: Family name and given name MUST both be given</p>
     */
    @Test
    @Ignore
    public void testGoodLivingSubjectName() {
        testFailScenario("PT_CLIENT_XCPD_#11.1", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#11.1.xml");
    }

    /**
     * <p>
     * Asserts that a NOT well formulated &lt;LivingSubjectBirthTime&gt; results
     * in a NOT valid response.</p>
     *
     * <p>
     * Normal usage condition: NOTE: MUST be encoded
     * “YYYY[MM[DD[HHMM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ]”</p>
     */
    @Test
    @Ignore
    public void testBadLivingSubjectBirthTime() {
        testFailScenario("PT_CLIENT_XCPD_#12", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#12.xml");
    }

    /**
     * <p>
     * Asserts that a well formulated &lt;LivingSubjectBirthTime&gt; results in
     * a valid response.</p>
     *
     * <p>
     * Normal usage condition: NOTE: MUST be encoded
     * “YYYY[MM[DD[HHMM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ]”</p>
     */
    @Test
    @Ignore
    public void testGoodLivingSubjectBirthTime() {
        testGood("PT_CLIENT_XCPD_#12.1", REQ_FOLDER + "PT_CLIENT_XCPD_#12.1.xml");
    }

    /**
     * <p>
     * Asserts that a NOT well formulated &lt;LivingSubjectGender&gt; results in
     * a NOT valid response.</p>
     *
     * <p>
     * Normal usage condition: MUST be “M” or “F”</p>
     */
    @Test
    @Ignore
    public void testBadLivingSubjectGender() {
        testFailScenario("PT_CLIENT_XCPD_#13", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#13.xml");
    }

    /**
     * <p>
     * Asserts that a well formulated &lt;LivingSubjectGender&gt; results in a
     * valid response.</p>
     *
     * <p>
     * Normal usage condition: MUST be “M” or “F”</p>
     */
    @Test
    @Ignore
    public void testGoodLivingSubjectGender() {
        testFailScenario("PT_CLIENT_XCPD_#13.1", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#13.1.xml");
    }

    /**
     * <p>
     * Asserts that a NOT well formulated &lt;LivingSubjectBirthPlaceAddress&gt;
     * results in a NOT valid response.</p>
     *
     * <p>
     * Normal usage condition: SHOULD contain country and city.</p>
     */
    @Test
    @Ignore
    public void testBadLivingSubjectBirthPlaceAddress() {
        testFailScenario("PT_CLIENT_XCPD_#14", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#14.xml");
    }

    /**
     * <p>
     * Asserts that a well formulated &lt;LivingSubjectBirthPlaceAddress&gt;
     * results in a valid response.</p>
     *
     * <p>
     * Normal usage condition: SHOULD contain country and city.</p>
     */
    @Test
    @Ignore
    public void testGoodLivingSubjectBirthPlaceAddress() {
        testFailScenario("PT_CLIENT_XCPD_#14.1", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#14.1.xml");
    }

    /**
     * <p>
     * Asserts that a NOT well formulated &lt;PatientAddress&gt; results in a
     * NOT valid response.</p>
     *
     * <p>
     * Normal usage condition: SHOULD contain country and city.</p>
     */
    @Test
    @Ignore
    public void testBadPatientAddress() {
        testFailScenario("PT_CLIENT_XCPD_#15", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#15.xml");
    }

    /**
     * <p>
     * Asserts that a well formulated &lt;PatientAddress&gt; results in a valid
     * response.</p>
     *
     * <p>
     * Normal usage condition: SHOULD contain country and city.</p>
     */
    @Test
    @Ignore
    public void testGoodPatientAddress() {
        testFailScenario("PT_CLIENT_XCPD_#15.1", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#15.1.xml");
    }


    /*
     * Test invalid fields
     */
    /**
     * <p>
     * Asserts that a well and full formulated request results in a valid
     * response.</p>
     */
    @Test
    @Ignore
    public void TestInvalidField() {
        testFailScenario("PT_CLIENT_XCPD_#16", "AnswerNotAvailable", REQ_FOLDER + "PT_CLIENT_XCPD_#16.xml");
    }

    /*
     * Auxiliar methods
     */
    @Override
    protected Collection<Assertion> getAssertions(String requestPath, XSPARole role) {
        return hcpAssertionCreate(role);
    }
}
