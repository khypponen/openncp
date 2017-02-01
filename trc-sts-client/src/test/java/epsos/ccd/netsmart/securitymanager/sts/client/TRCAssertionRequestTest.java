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
package epsos.ccd.netsmart.securitymanager.sts.client;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.fail;

@RunWith(JMockit.class)
public class TRCAssertionRequestTest {

    private static final Logger logger = LoggerFactory.getLogger(TRCAssertionRequestTest.class);

    @Mocked
    TRCAssertionRequest req;
    @Mocked
    Marshaller marshaller;

    protected static final Logger LOG = LoggerFactory.getLogger(TRCAssertionRequestTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {

        try {
            Properties databaseProps;
            databaseProps = new Properties();
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("database.properties");
            databaseProps.load(is);

            /* Add JDBC connection parameters to environment, instead of traditional JNDI */
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

        Properties systemProps = System.getProperties();
        systemProps.put(
                "javax.net.ssl.trustStore",
                "C:/Users/Jerouris/testKeystore.jks");
        systemProps.put(
                "javax.net.ssl.keyStore",
                "C:/Users/Jerouris/testKeystore.jks");
        systemProps.put(
                "javax.net.ssl.trustStorePassword",
                "tomcat");
        systemProps.put(
                "javax.net.ssl.keyStorePassword",
                "tomcat");
        //  systemProps.put("javax.net.debug", "SSL,handshake");
        //  systemProps.put("java.security.debug", "all");

        System.setProperties(systemProps);
        //for localhost testing only

        DefaultBootstrap.bootstrap();


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

    /**
     * Test of request method, of class TRCAssertionRequest.
     */
    @Test
    public void testMakeRequestSSL() throws Exception {

        new NonStrictExpectations() {{
            req.request();
            returns((Assertion) any);
        }};

        try {
            //makeRequest - with SSL
            Assertion idAs = loadSamlAssertionAsResource("SampleIdAssertion.xml");

            TRCAssertionRequest req = new TRCAssertionRequest.Builder(idAs, "anId")
                    .PurposeOfUse("TREATMENT")
                    .build();

            Assertion trc = req.request();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document signedDoc = dbf.newDocumentBuilder().newDocument();

            marshaller.marshall(trc, signedDoc);

            try {
                XMLUtils.sendXMLtoStream(signedDoc, new FileOutputStream("SignedTRCAssertion.xml"));
            } catch (FileNotFoundException ex) {
                logger.error(null, ex);
            }

        } catch (Exception ex) {
            logger.error(null, ex);
            fail("Could not make the request: " + ex.getMessage());
        }
    }

    @Test
    public void testMakeRequestPlainConnection() {
        try {
            // makeRequest - No SSL
            Assertion idAs = loadSamlAssertionAsResource("SampleIdAssertion.xml");

            TRCAssertionRequest req = new TRCAssertionRequest.Builder(idAs, "TestId2")
                    .STSLocation("http://localhost:8080/TRC-STS/SecurityTokenService")
                    .PurposeOfUse("EMERGENCY")
                    .build();

            Assertion trc = req.request();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document signedDoc = dbf.newDocumentBuilder().newDocument();

            marshaller.marshall(trc, signedDoc);
            try {
                XMLUtils.sendXMLtoStream(signedDoc, new FileOutputStream("SignedTRCAssertion.xml"));
            } catch (FileNotFoundException ex) {
                logger.error(null, ex);
            }

        } catch (Exception ex) {
            logger.error(null, ex);
            fail("Could not make the request: " + ex.getMessage());
        }
    }

    private Assertion loadSamlAssertionAsResource(String filename) {
        Assertion hcpIdentityAssertion = null;
        try {
            BasicParserPool ppMgr = new BasicParserPool();
            ppMgr.setNamespaceAware(true);
            // Parse metadata file
            InputStream in = ClassLoader.getSystemResourceAsStream(filename);
            Document samlas = ppMgr.parse(in);
            Element samlasRoot = samlas.getDocumentElement();
            // Get apropriate unmarshaller
            UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
            Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(samlasRoot);
            // Unmarshall using the document root element, an EntitiesDescriptor in this case
            hcpIdentityAssertion = (Assertion) unmarshaller.unmarshall(samlasRoot);
        } catch (UnmarshallingException ex) {
            logger.error(null, ex);
        } catch (XMLParserException ex) {
            logger.error(null, ex);
        }
        return hcpIdentityAssertion;
    }
}
