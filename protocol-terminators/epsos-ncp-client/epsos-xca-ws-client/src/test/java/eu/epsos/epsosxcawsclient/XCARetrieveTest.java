package eu.epsos.epsosxcawsclient;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.exceptions.XCAException;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.io.UnsupportedEncodingException;

import org.junit.Ignore;
import org.opensaml.saml2.core.Assertion;

import tr.com.srdc.epsos.data.model.xds.XDSDocument;
import eu.epsos.protocolterminators.integrationtest.common.HCPIAssertionCreator;
import eu.epsos.protocolterminators.integrationtest.common.TRCAssertionCreator;
import eu.epsos.protocolterminators.integrationtest.common.TestConstants;
import eu.epsos.pt.ws.client.xca.XcaInitGateway;
import tr.com.srdc.epsos.util.Constants;

/**
 * Test class for the XCA Retreive Service.
 * For a successful run you must set EPSOS_PROPS_PATH containing epsos-srdc.properties.
 * @author gareth
 */

@Ignore
public class XCARetrieveTest {
	
	//parameters needed to run this test
	private static final String REPOSITORY_ID = TestConstants.REPOSITORY_ID;
	private static final String HOME_COMMUNITY_ID = TestConstants.HOME_CUMMUNITY_ID;
	private static final String DOCUMENT_ID = TestConstants.DOCUMENT_ID_PDF;
	private static final String PATIENT_COUNTRY = TestConstants.PATIENT_COUNTRY;

	public XCARetrieveTest() {
		TestConstants.checkEnvironmentVariables();
	}

	public void doRetrieve() throws XCAException {

		// build assertions
		Assertion idAssertion = new HCPIAssertionCreator().createHCPIAssertion(XSPARole.PHARMACIST);
		Assertion trcAssertion = TRCAssertionCreator.createTRCAssertion(HOME_COMMUNITY_ID, PATIENT_COUNTRY);

		// build XDS document
		XDSDocument document = new XDSDocument();
		document.setDocumentUniqueId(DOCUMENT_ID);
		document.setRepositoryUniqueId(REPOSITORY_ID);

		// call the service
		DocumentResponse result = XcaInitGateway.crossGatewayRetrieve(
				document,
				HOME_COMMUNITY_ID,
				PATIENT_COUNTRY,
                                TestConstants.TARGET_LANGUAGE,
				idAssertion,
				trcAssertion, Constants.PatientService);
		
		printResult(result);
	}
	
	private void printResult(DocumentResponse result) {
		if (result != null) {
			if (result.getDocument() != null) {
				try {
					System.out.println("document: " + new String(result.getDocument(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws XCAException {
		new XCARetrieveTest().doRetrieve();
		System.out.println("+++++++++++++++++++++++ FINISHED! +++++++++++++++++++++++");
	}
}
