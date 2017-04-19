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

	private ConfigurationManagerService cm = null;

	// @BeforeClass
	// public static void setHibernateConfig() {
	//
	// HibernateConfigFile.name =
	// "src/test/resources/configmanager.hibernate.xml";
	// }

	@Before
	public void init() {

		cm = ConfigurationManagerService.getInstance();
		assertNotNull(cm);
		// cm.load(ClassLoader.getSystemResourceAsStream("securitymanager_test.properties"));
	}

	@Test
	public void test() {

		cm = ConfigurationManagerService.getInstance();
		assertNotNull(cm);
	}
}
