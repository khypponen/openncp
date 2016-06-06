package eu.epsos.protocolterminators.integrationtest.common;

/**
 * Constants for protocol terminators integration tests.
 * @author gareth
 */
public class TestConstants {

	public static final String HOME_CUMMUNITY_ID = "2.16.17.710.811";
	public static final String PATIENT_ID = "199604092388";
	public static final String REPOSITORY_ID = "1234";
	public static final String DOCUMENT_ID_CDA = "EP.199604092388.199592261.2.199592261.CDA";
	public static final String DOCUMENT_ID_PDF = "EP.199604092388.199592261.2.199592261.PDF";
	public static final String PATIENT_COUNTRY = "SE";
	public static final String PATIENT_COUNTRY_NAME = "Sweden";
	public static final String EPSOS_PROPS_PATH = "C:\\_WORKSPACE\\EPSOS_shelob\\epsos_config";
	public static final String CDA_EDISP_RESOURCE_NAME = "cda-edisp.xml";
	public static final String CDA_EDISP_ID = "1234";
	public static final String TARGET_LANGUAGE = "pt-PT";
        private static final String EPSOS_PROPS_PATH_NAME = "EPSOS_PROPS_PATH";
	

	/**
	 * Check if environment variable EPSOS_PROPS_PATH is set.
	 * If unavailable then use Java constant instead.
	 */
	public static void checkEnvironmentVariables() {
		System.out.println(EPSOS_PROPS_PATH_NAME + ": " + System.getenv(EPSOS_PROPS_PATH_NAME));
		if (System.getenv(EPSOS_PROPS_PATH_NAME) == null) {
			System.out.println(EPSOS_PROPS_PATH_NAME + " is not set, using: " + TestConstants.EPSOS_PROPS_PATH);
			System.setProperty(EPSOS_PROPS_PATH_NAME, TestConstants.EPSOS_PROPS_PATH);
		}
	}
}
