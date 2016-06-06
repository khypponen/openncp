package eu.epsos.epsosxcawsclient;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.exceptions.XCAException;
import org.junit.Ignore;
import org.opensaml.saml2.core.Assertion;

import tr.com.srdc.epsos.data.model.GenericDocumentCode;
import tr.com.srdc.epsos.data.model.PatientId;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.data.model.xds.QueryResponse;
import tr.com.srdc.epsos.data.model.xds.XDSDocument;
import eu.epsos.protocolterminators.integrationtest.common.HCPIAssertionCreator;
import eu.epsos.protocolterminators.integrationtest.common.TRCAssertionCreator;
import eu.epsos.protocolterminators.integrationtest.common.TestConstants;
import eu.epsos.pt.ws.client.xca.XcaInitGateway;
import eu.epsos.util.IheConstants;
import tr.com.srdc.epsos.data.model.xds.XDSDocumentAssociation;

/**
 * Test class for the XCA Query Service. For a successful run you must set
 * EPSOS_PROPS_PATH containing epsos-srdc.properties.
 *
 * @author gareth
 */
@Ignore
public class XCAQueryTest {

    //parameters needed to run this test
    private static final String PATIENT_COUNTRY = TestConstants.PATIENT_COUNTRY;
    private static final String PATIENT_ID = TestConstants.PATIENT_ID;
    private static final String CLASSCODE = Constants.EP_CLASSCODE;
    private static final String CLASSCODE_SCHEMA = IheConstants.ClASSCODE_SCHEME;
    private static final String HOME_COMMUNITY_ID = TestConstants.HOME_CUMMUNITY_ID;

    public XCAQueryTest() {
        TestConstants.checkEnvironmentVariables();
    }

    public void doQuery() throws XCAException {

        // build assertions
        Assertion idAssertion = HCPIAssertionCreator.createHCPIAssertion(XSPARole.PHARMACIST);
        Assertion trcAssertion = TRCAssertionCreator.createTRCAssertion(HOME_COMMUNITY_ID, PATIENT_ID);

        // build patientid
        PatientId patientId = new PatientId();
        patientId.setRoot(HOME_COMMUNITY_ID);
        patientId.setExtension(PATIENT_ID);

        // build GenericDocumentCode
        GenericDocumentCode classcode = new GenericDocumentCode();
        classcode.setSchema(CLASSCODE_SCHEMA);
        classcode.setValue(CLASSCODE);

        // call the service
        QueryResponse result = XcaInitGateway.crossGatewayQuery(
                patientId,
                PATIENT_COUNTRY,
                classcode,
                idAssertion,
                trcAssertion, Constants.PatientService);

        printResult(result);
    }

    private void printResult(QueryResponse result) {

        if (result != null) {
            if (result.getDocumentAssociations() != null) {
                for (XDSDocumentAssociation documentAssociation : result.getDocumentAssociations()) {
                    if (documentAssociation.getCdaXML() != null) {
                        System.out.println("documentUniqueId: " + documentAssociation.getCdaXML().getDocumentUniqueId());
                    }
                    if (documentAssociation.getCdaPDF() != null) {
                        System.out.println("documentUniqueId: " + documentAssociation.getCdaPDF().getDocumentUniqueId());
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws XCAException {

        new XCAQueryTest().doQuery();
        System.out.println("+++++++++++++++++++++++ FINISHED! +++++++++++++++++++++++");
    }
}
