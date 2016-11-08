/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/

package se.sb.epsos.web.pages;

import static org.junit.Assert.assertNotNull;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyStoreManagerImplTest {
	protected static final Logger LOGGER = LoggerFactory.getLogger(KeyStoreManagerImplTest.class);
	
	@Test
	@Ignore
	public void testGetKeyStore() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, KeyStoreInitializationException {
		KeyStoreManager keyMgr = new KeyStoreManagerImpl();
		assertNotNull(keyMgr);
		assertNotNull(keyMgr.getPrivateKey());
		assertNotNull(keyMgr.getCertificate());
		assertNotNull(keyMgr.getPrivateKey().getPrivate());
		assertNotNull(keyMgr.getPrivateKey().getPublic());
	}
}
