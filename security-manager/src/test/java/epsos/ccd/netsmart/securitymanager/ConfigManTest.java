/*
 *  Copyright 2010 Jerry Dimitriou <jerouris at netsmart.gr>.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package epsos.ccd.netsmart.securitymanager;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.DefaultKeyStoreManager;
import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * @author Jerry Dimitriou <jerouris at netsmart.gr>
 */
public class ConfigManTest {

    ConfigurationManagerService configurationManagerService = ConfigurationManagerService.getInstance();

    public ConfigManTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void configManTests() throws IOException {

        configurationManagerService = ConfigurationManagerService.getInstance();
        //Properties cm = new Properties();
        //cm.load(ClassLoader.getSystemResourceAsStream("securitymanager_test.properties"));
        //cm.load(new FileInputStream(System.getProperty("SECMAN_HOME") + "/securitymanager.properties"));
        assertNotNull(configurationManagerService);

        // Constants Initialization
        String KEYSTORE_LOCATION = configurationManagerService.getProperty("NCP_SIG_KEYSTORE_PATH");
        System.out.println("Keystore Location: " + KEYSTORE_LOCATION);

        String TRUSTSTORE_LOCATION = configurationManagerService.getProperty("TRUSTSTORE_PATH");
        System.out.println("Truststore Location:" + TRUSTSTORE_LOCATION);

        String KEYSTORE_PASSWORD = configurationManagerService.getProperty("NCP_SIG_KEYSTORE_PASSWORD");
        String TRUSTSTORE_PASSWORD = configurationManagerService.getProperty("TRUSTSTORE_PASSWORD");

        String PRIVATEKEY_ALIAS = configurationManagerService.getProperty("NCP_SIG_PRIVATEKEY_ALIAS");
        String PRIVATEKEY_PASSWORD = configurationManagerService.getProperty("NCP_SIG_PRIVATEKEY_PASSWORD");

        //String ERROR_PARAM = cm.getProperty("ERRORRRR");

        assertNotNull(KEYSTORE_LOCATION);
        assertNotNull(TRUSTSTORE_LOCATION);

        assertNotNull(KEYSTORE_PASSWORD);
        assertNotNull(TRUSTSTORE_PASSWORD);

        assertNotNull(PRIVATEKEY_ALIAS);
        assertNotNull(PRIVATEKEY_PASSWORD);

        // The expected behavior is to return a not-null empty string (length==0);
        //assertNotNull(ERROR_PARAM);
        //assert (ERROR_PARAM.length() == 0);

        KeyStoreManager km = new DefaultKeyStoreManager();
        assertNotNull(km);
    }
}
