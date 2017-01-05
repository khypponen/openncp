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

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.NSTestKeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.SPMSTestKeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.TianiTestKeyStoreManager;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Jerry Dimitriou <jerouris at netsmart.gr>
 */
public class MockCertTest {

    protected static final Logger LOG = LoggerFactory.getLogger(MockCertTest.class);

    public MockCertTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        /*
        try {
            Properties databaseProps;
            databaseProps = new Properties();
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("database.properties");
            databaseProps.load(is);

            //* Add JDBC connection parameters to environment, instead of traditional JNDI
            final SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
            final ComboPooledDataSource ds = new ComboPooledDataSource();
            try {
                ds.setDriverClass(databaseProps.getProperty("db.driverclass"));
            } catch (PropertyVetoException ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            }
            ds.setJdbcUrl(databaseProps.getProperty("db.jdbcurl"));
            ds.setUser(databaseProps.getProperty("db.user"));
            ds.setPassword(databaseProps.getProperty("db.password"));
            ds.setMaxPoolSize(1);
            ds.setMaxPoolSize(15);
            ds.setAcquireIncrement(3);
            ds.setMaxStatementsPerConnection(100);
            ds.setNumHelperThreads(20);
            builder.bind(databaseProps.getProperty("db.resname"), ds);
            try {
                builder.activate();
            } catch (IllegalStateException ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            } catch (NamingException ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        */
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Ignore
    @Test
    public void TianiCertTest() throws IOException {
        try {
            KeyStoreManager ksm = new TianiTestKeyStoreManager();
            X509Certificate cert = (X509Certificate) ksm.getCertificate("server1");
            boolean[] ku = cert.getKeyUsage();

            CertificateValidator cv = new CertificateValidator(ksm.getTrustStore());
            cv.validateCertificate(cert);
            assertNull(ku);
        } catch (SMgrException ex) {
            LOG.error(null, ex);
            fail(ex.getMessage());
        }
    }

    @Test
    public void SPMSCertTest() throws IOException {
        try {
            KeyStoreManager ksm = new SPMSTestKeyStoreManager();
            X509Certificate cert = (X509Certificate) ksm.getCertificate("ppt.ncp-signature.epsos.spms.pt");
            boolean[] ku = cert.getKeyUsage();

            CertificateValidator cv = new CertificateValidator(ksm.getTrustStore());
            cv.validateCertificate(cert);
            assertNull(ku);
        } catch (SMgrException ex) {
            LOG.error(null, ex);
            fail(ex.getMessage());
        }
    }

    @Ignore
    @Test
    public void NSCertTest() throws IOException {
        try {
            KeyStoreManager ksm = new NSTestKeyStoreManager();
            X509Certificate cert = (X509Certificate) ksm.getCertificate("testncp");
            boolean[] ku = cert.getKeyUsage();
            if (ku == null) {
                fail("Key Usage not Present");
            }
            System.out.println(ku);
            CertificateValidator cv = new CertificateValidator(ksm.getTrustStore());
            cv.validateCertificate(cert);
        } catch (SMgrException ex) {
            LOG.error(null, ex);
            fail(ex.getMessage());
        }


    }

    @Ignore
    @Test
    public void NSCert2Test() throws IOException {
        try {
            KeyStoreManager ksm = new NSTestKeyStoreManager();
            X509Certificate cert = (X509Certificate) ksm.getCertificate("testncp2");
            boolean[] ku = cert.getKeyUsage();
            if (ku == null) {
                fail("Key Usage not Present");
            }
            System.out.println(ku);
            CertificateValidator cv = new CertificateValidator(ksm.getTrustStore());
            cv.validateCertificate(cert);
        } catch (SMgrException ex) {
            LOG.error(null, ex);
            fail(ex.getMessage());
        }
    }

    @Ignore
    @Test
    public void NSRevokedCertTest() throws IOException {
        try {
            KeyStoreManager ksm = new NSTestKeyStoreManager();
            X509Certificate cert = (X509Certificate) ksm.getCertificate("testrevokedncp");
            boolean[] ku = cert.getKeyUsage();
            if (ku == null) {
                fail("Key Usage not Present");
            }
            CertificateValidator cv = new CertificateValidator(ksm.getTrustStore());
            cv.validateCertificate(cert);

            System.out.println(ku);
        } catch (SMgrException ex) {
            LOG.error(null, ex);
            fail(ex.getMessage());
        }
    }
}