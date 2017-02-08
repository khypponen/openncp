package eu.epsos.epsosxcpdwsclient;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.exceptions.NoPatientIdDiscoveredException;
import eu.epsos.protocolterminators.integrationtest.common.HCPIAssertionCreator;
import eu.epsos.protocolterminators.integrationtest.common.TestConstants;
import eu.epsos.pt.ws.client.xcpd.XcpdInitGateway;
import org.junit.Ignore;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.data.model.PatientDemographics;
import tr.com.srdc.epsos.data.model.PatientId;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for invoking the XCPD Web Services (epsos-xcpd-ws-server) using the epsos-xcpd-ws-client. For a successful
 * run you must set EPSOS_PROPS_PATH containing epsos-srdc.properties.
 *
 * @author gareth
 */

@Ignore
public class XCPDTest {

    private static final Logger logger = LoggerFactory.getLogger(XCPDTest.class);

    //parameters needed to run this test
    private static final String PATIENT_COUNTRY = TestConstants.PATIENT_COUNTRY;
    private static final String PATIENT_ID = TestConstants.PATIENT_ID;
    private static final String HOME_COMMUNITY_ID = TestConstants.HOME_CUMMUNITY_ID;

    public XCPDTest() {
        TestConstants.checkEnvironmentVariables();
    }

    public void getPatientDemographics() {

        PatientDemographics patientDemographics = new PatientDemographics();

        // build patientid
        PatientId patientId = new PatientId();
        patientId.setRoot(HOME_COMMUNITY_ID);
        patientId.setExtension(PATIENT_ID);
        List<PatientId> patientIds = new ArrayList<PatientId>();
        patientIds.add(patientId);
        patientDemographics.setIdList(patientIds);

        // countryId
        patientDemographics.setCountry(PATIENT_COUNTRY);

        // assertions
        Assertion idAssertion = new HCPIAssertionCreator().createHCPIAssertion(XSPARole.PHYSICIAN);

        // Call the service
        try {
            List<PatientDemographics> result;
            result = XcpdInitGateway.patientDiscovery(patientDemographics,
                    idAssertion,
                    PATIENT_COUNTRY);

            System.out.println("result: " + result);

        } catch (NoPatientIdDiscoveredException ex) {
            logger.error(null, ex);
        }
    }

    public static void main(String[] args) {

        new XCPDTest().getPatientDemographics();
        System.out.println("+++++++++++++++++++++++ FINISHED! +++++++++++++++++++++++");
    }
}
