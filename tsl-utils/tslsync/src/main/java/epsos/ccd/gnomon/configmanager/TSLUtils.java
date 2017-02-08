/**
 * *Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package epsos.ccd.gnomon.configmanager;

import epsos.ccd.netsmart.securitymanager.SignatureManager;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.*;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.Provider;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

/**
 * Helper utils form validating xnl, extracting nodes from xml.
 *
 * Massi: why all the fields are static? By removing the staticity, a lot of code can be 
 * re-factored. If not, we can still re-factor by adding a static block. 
 *
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 */
public class TSLUtils {

    private static Logger logger = LoggerFactory.getLogger(TSLUtils.class);

    static private Base64 coder = new Base64();
    private static String KEY_STORE_TYPE = "JKS"; // this variable is never used.
    private static String KEY_STORE_NAME = "C://mesa//test_keystore_server1.jks";// this variable is never used.
    private static String KEY_STORE_PASS = "spirit";// this variable is never used.
    private static String PRIVATE_KEY_PASS = "spirit";// this variable is never used.
    private static String KEY_ALIAS = "server1";// this variable is never used.
    private static String TRUST_STORE = "C://mesa//test_keystore_server1.jks";
    private static String TRUST_STORE_PASS = "spirit";
    private static String TSLNS = "http://uri.etsi.org/02231/v2#";

    public static void init_variables() {

		/*
		 * The configuration manager should have a method to group functionalities. 
		 * Everytime load a keystore, and passing all these values is bad code. 
		 */

        ConfigurationManagerService cms = ConfigurationManagerService.getInstance();
        KEY_STORE_NAME = cms.getProperty("NCP_SIG_KEYSTORE_PATH");
        KEY_STORE_PASS = cms.getProperty("NCP_SIG_KEYSTORE_PASSWORD");
        PRIVATE_KEY_PASS = cms.getProperty("NCP_SIG_PRIVATEKEY_PASSWORD");
        KEY_ALIAS = cms.getProperty("NCP_SIG_PRIVATEKEY_ALIAS");
        TRUST_STORE = cms.getProperty("TRUSTSTORE_PATH");
        TRUST_STORE_PASS = cms.getProperty("TRUSTSTORE_PASSWORD");

    }

    public static String getTSLId(String filename, String countrycode) {
        String tagValue = "0";
        try {


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = new URL(filename).openStream();
            Document doc = db.parse(is);
            is.close();
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagNameNS(TSLNS, "TSLSequenceNumber");
            Node td = nodeLst.item(0);
            Element tdElement = (Element) td;
            tagValue = tdElement.getTextContent();
        } catch (Exception e) {
            logger.error(countrycode + "_" + tagValue.trim() + " NOT processed successfully");
        }
        return tagValue;
    }

    /**
     * Given the path of the tsl file and the service name, this method exports
     * its certifivate to der format
     *
     * @param filename
     *            is the path of the tsl file
     * @return in a hashtable the services defined in the thsl file
     */
    public static String exportSSLFromTSL(String filename, String countrycode) {
        boolean export = false;
        // TODO check TSPTradeName to see if it is NCP-A
        StringBuffer sb = new StringBuffer();
        String tagValue = "";
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputStream is = new URL(filename).openStream();
            Document doc = db.parse(is);
            is.close();
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagNameNS(TSLNS, "TSPServices");
            logger.info("###### COUNT OF SERVICES: " + nodeLst.getLength());
            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) fstNode;
                    NodeList services = eElement.getElementsByTagNameNS(TSLNS, "ServiceInformation");

                    for (int s1 = 0; s1 < services.getLength(); s1++) {
                        Node secNode = services.item(s1);
                        if (secNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element bElement = (Element) secNode;
                            tagValue = getServiceNameFromURL(getTagValue("ServiceTypeIdentifier", bElement));
                            logger.info("########## tsl:ServiceTypeIdentifier: " + tagValue);
                            if (tagValue.equals("PatientIdenitificationService")) {
                                tagValue = "PatientIdentificationService";
                            }

                            NodeList sn = bElement.getElementsByTagNameNS(TSLNS, "ServiceName");
                            String tdValue = "";
                            for (int t1 = 0; t1 < sn.getLength(); t1++) {
                                Node snNode = sn.item(t1);
                                Element snElement = (Element) snNode;
                                NodeList tradeNames = snElement.getElementsByTagNameNS(TSLNS, "Name");
                                Node td = tradeNames.item(0);
                                Element tdElement = (Element) td;
                                tdValue = tdElement.getTextContent();
                                logger.info("########## TSL:NAME: " + tdValue);
                            }

                            if (!(tdValue.startsWith("NCP-B"))) {

                                NodeList xmlSigs = bElement.getElementsByTagNameNS(TSLNS, "ServiceDigitalIdentity");

                                for (int s2 = 0; s2 < xmlSigs.getLength(); s2++) {
                                    Node thirdNode = xmlSigs.item(s2);
                                    Element cElement = (Element) thirdNode;
                                    NodeList digitalids = cElement.getElementsByTagNameNS(TSLNS, "DigitalId");

                                    for (int s3 = 0; s3 < digitalids.getLength(); s3++) {
                                        Node fourthNode = digitalids.item(s3);
                                        Element dElement = (Element) fourthNode;
                                        NodeList certs = dElement.getElementsByTagNameNS(TSLNS, "X509Certificate");

                                        for (int s4 = 0; s4 < certs.getLength(); s4++) {
                                            Node certNode = certs.item(s4);
                                            logger.debug("CERT START");
                                            logger.debug(certNode.getTextContent().substring(0, 100));
                                            logger.debug("CERT END");
                                            InputStream in = new ByteArrayInputStream(
                                                    coder.decodeBase64(certNode.getTextContent()));

                                            X509Certificate t = TSLUtils.getCertificateFromIS(in);
                                            // create a keypair value with
                                            // certid and country code
                                            ConfigurationManagerService cm = ConfigurationManagerService.getInstance();
                                            String storepath = cm.getProperty("certificates.storepath");
                                            cm.updateProperty(t.getSerialNumber() + "",
                                                    getNCP(s + "") + "_" + countrycode + "_" + tagValue.trim());
                                            // export the certificate to der
                                            // format
                                            boolean exp1 = TSLUtils.exportCertificate(t,
                                                    new File(storepath + getNCP(s + "") + "_" + countrycode + "_"
                                                            + tagValue.trim() + "_" + s3 + ".der"),
                                                    true);
                                            if (exp1) {
                                                sb.append("Certificate " + getNCP(s + "") + "_" + countrycode + "_"
                                                        + tagValue.trim() + "_" + s3 + ".der exported successfully");
                                                sb.append("<br/>");
                                            } else {
                                                sb.append("Certificate " + getNCP(s + "") + "_" + countrycode + "_"
                                                        + tagValue.trim() + "_" + s3 + ".der not exported");
                                            }
                                            sb.append("<br/>");
                                            boolean exp2 = TSLUtils.storeCertificateToTrustStore(t,
                                                    tdValue + getNCP(s + "") + "_" + countrycode + "_" + s3 + "_"
                                                            + tagValue.trim());
                                            if (exp2) {
                                                sb.append("Certificate " + tdValue + getNCP(s + "") + "_" + countrycode
                                                        + "_" + s3 + "_" + tagValue.trim() + " exported successfully");
                                                sb.append("<br/>");
                                            } else {
                                                sb.append("Certificate " + tdValue + getNCP(s + "") + "_" + countrycode
                                                        + "_" + s3 + "_" + tagValue.trim() + " not exported");
                                            }
                                            sb.append("<br/>");

                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
            export = true;
        } catch (Exception e) {
            logger.error(countrycode + "_" + tagValue.trim() + " NOT processed successfully");
        }
        return sb.toString();
    }

    public static String exportNCPSignFromTSL(String filename, String countrycode) {
        boolean ncptsl = false;
        // TODO check TSPTradeName to see if it is NCP-A
        StringBuilder sb = new StringBuilder();
        String tagValue = "";
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            InputStream is = new URL(filename).openStream();
            Document doc = db.parse(is);
            is.close();
            doc.getDocumentElement().normalize();
            NodeList certs = doc.getElementsByTagName("ds:X509Certificate");

            for (int s4 = 0; s4 < certs.getLength(); s4++) {
                Node certNode = certs.item(s4);
                InputStream in = new ByteArrayInputStream(coder.decodeBase64(certNode.getTextContent()));
                X509Certificate t = TSLUtils.getCertificateFromIS(in);
                ConfigurationManagerService cm = ConfigurationManagerService.getInstance();
                String storepath = cm.getProperty("certificates.storepath");
                // export the certificate to der format
                boolean exp = TSLUtils.exportCertificate(t,
                        new File(storepath + countrycode + "_" + "NCPSign" + "_" + s4 + ".der"), true);
                if (exp) {
                    sb.append(countrycode + "_" + "NCPSign" + "_" + s4 + ".der" + " exported successfully");
                    logger.info(countrycode + "_" + "NCPSign" + "_" + s4 + ".der" + " exported successfully");
                } else {
                    sb.append(countrycode + "_" + "NCPSign" + "_" + s4 + ".der" + " not exported");
                    logger.info(countrycode + "_" + "NCPSign" + "_" + s4 + ".der" + " not exported");
                }
                boolean exp1 = TSLUtils.storeCertificateToTrustStore(t, "NCPSign " + "_" + "_" + countrycode);
                if (exp1) {
                    sb.append("NCPSign " + "_" + "_" + countrycode + " stored to truststore");
                    sb.append("<br/>");
                } else {
                    sb.append("NCPSign " + "_" + "_" + countrycode + " not stored to truststore");
                }
                sb.append("<br/>");
            }
            ncptsl = true;

        } catch (Exception e) {
            logger.error(countrycode + "_" + "NCP Signature NOT processed successfully");
        }
        return sb.toString();

    }

    /**
     * Extracts the service names defined in the tsl file. As A service name
     * defined the last part of the service url. E.g if the service URL is
     * http://epsos.e/ccd/PatientService the service name is PatientService
     *
     * @param filename
     *            is the path stored the tsl file
     * @return a hashtable of the service names and the endpoints
     */
    public static Hashtable getServicesFromTSL(String filename) {
        Hashtable serviceNames = new Hashtable();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = new URL(filename).openStream();
            Document doc = db.parse(is);
            is.close();
            doc.getDocumentElement().normalize();
            logger.debug("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagNameNS(TSLNS, "TSPServices");
            // System.out.println("Information of all services");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) fstNode;
                    NodeList services = eElement.getElementsByTagNameNS(TSLNS, "ServiceInformation");

                    for (int s1 = 0; s1 < services.getLength(); s1++) {
                        Node secNode = services.item(s1);
                        if (secNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element bElement = (Element) secNode;

                            NodeList sn = bElement.getElementsByTagNameNS(TSLNS, "ServiceName");
                            String tdValue = "";
                            for (int t1 = 0; t1 < sn.getLength(); t1++) {
                                tdValue = "";
                                Node snNode = sn.item(t1);
                                Element snElement = (Element) snNode;
                                NodeList tradeNames = snElement.getElementsByTagNameNS(TSLNS, "Name");
                                Node td = tradeNames.item(0);
                                Element tdElement = (Element) td;
                                try {
                                    tdValue = tdElement.getTextContent();
                                } catch (Exception e) {
                                    logger.error(e.getMessage());
                                }
                            }
                            if (!tdValue.startsWith("NCP-B")) {
                                tdValue = "";
                                String sname = getTagValue("ServiceTypeIdentifier", bElement);
                                NodeList suppnodes = bElement.getElementsByTagNameNS(TSLNS, "ServiceSupplyPoints");
                                for (int t1 = 0; t1 < suppnodes.getLength(); t1++) {
                                    Node suppNode = suppnodes.item(t1);
                                    Element snElement = (Element) suppNode;
                                    NodeList suppnode = snElement.getElementsByTagNameNS(TSLNS, "ServiceSupplyPoint");
                                    Node td = suppnode.item(0);
                                    Element tdElement = (Element) td;
                                    try {
                                        tdValue = tdElement.getTextContent();
                                    } catch (Exception e) {
                                        logger.error(sname + " " + e.getMessage());
                                    }
                                }
                                sname = tdValue;
                                if (!tdValue.equals("")) {
                                    serviceNames.put(getNCP(s + "")
                                                    + getServiceNameFromURL(getTagValue("ServiceTypeIdentifier", bElement)),
                                            sname);
                                    logger.info(
                                            "Service URL " + sname + " added to configuration " + getServiceNameFromURL(
                                                    getTagValue("ServiceTypeIdentifier", bElement)));
                                }
                            } else {
                                logger.info("IGNORED: It was an NCP-B Service "
                                        + getServiceNameFromURL(getTagValue("ServiceTypeIdentifier", bElement)));
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();

        }
        return serviceNames;
    }

    public static String getNCP(String s) {
        String ret = "_";
        if (s.equals("0")) {
            ret = "";
        }
        if (s.equals("1")) {
            ret = "NCPB_";
        }
        return ret;
    }

    /**
     *
     * Given a nodelist the method returns the x509 certificate which is inside
     * the signature of the file
     *
     * @param nl
     *            is a nodelist of a dom element
     * @return the X509 Certificate
     */
    public static X509Certificate getCertificateFromNodeList(NodeList nl) {
        X509Certificate cert = null;
        try {
            String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
                    (Provider) Class.forName(providerName).newInstance());
            KeyInfoKeySelector keyInfoKeySelector = new KeyInfoKeySelector();
            DOMValidateContext valContext = new DOMValidateContext(keyInfoKeySelector, nl.item(0));
            // cert = new KeyInfoKeySelector().getCertificate();

            XMLSignature signature = null;
            try {
                signature = fac.unmarshalXMLSignature(valContext);
            } catch (MarshalException e) {
                logger.debug("XML signature parse error: " + e.getMessage());
            }

            boolean coreValidity = signature.validate(valContext);
            if (coreValidity == false) {
                logger.debug("Signature failed");
            } else {
                logger.debug("Signature passed");
            }
            cert = keyInfoKeySelector.getCertificate();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return cert;
    }

    /**
     * Extracts the signature in pem format from the tsl file
     *
     * @param filename
     *            is the path of the tsl file
     * @return the signature of the tsl file in pem format
     */
    public static X509Certificate getCertificateFromTSL(String filename) {
        Document doc = null;
        X509Certificate cert = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = new URL(filename).openStream();
            doc = db.parse(is);
            is.close();

            // doc = db.parse(file);
            Element root = doc.getDocumentElement();
            NodeList xmlSigs = root.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
            if (xmlSigs.getLength() == 0) {
                logger.debug("Cannot find Signature element");
            }
            cert = getCertificateFromNodeList(xmlSigs);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return cert;
    }

    private static String getServiceNameFromURL(String url) {
        String[] parts = url.split("/");
        int partsLength = parts.length;
        String serviceName = parts[partsLength - 1];
        return serviceName;
    }

    /**
     *
     * @param sTag
     *            the tag name of an element of a dom
     * @param eElement
     *            the selected dom element
     * @return the value of the specific element
     */
    private static String getTagValue(String sTag, Element eElement) {
        logger.info("Tag is " + sTag);
        NodeList nlList = eElement.getElementsByTagNameNS(TSLNS, sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();
    }

    /**
     * Given an inputstream we can extract the x509 certificate included
     *
     * @param in
     * @return the x509 certificate
     */
    public static X509Certificate getCertificateFromIS(InputStream in) {
        X509Certificate t = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate c = cf.generateCertificate(in);
            in.close();
            t = (X509Certificate) c;
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return t;
    }

    public static X509Certificate getCertificateFromString(String cert) {
        Base64 encoder = new Base64();
        InputStream in = new ByteArrayInputStream(encoder.decodeBase64(cert));
        X509Certificate t = getCertificateFromIS(in);
        return t;
    }

    /**
     * This method takes a certificate and imports it to the truststore defined
     * in the configuration file
     *
     * @param cert
     *            the certificate to be imported
     */
    public static boolean storeCertificateToTrustStore(java.security.cert.Certificate cert, String certalias) {
        boolean exp = false;
        init_variables();
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            File keystoreFile = new File(TRUST_STORE);
            // Load the keystore contents
            FileInputStream in = new FileInputStream(keystoreFile);
            keystore.load(in, TRUST_STORE_PASS.toCharArray());
            in.close();

            keystore.setCertificateEntry(certalias, cert);
            logger.debug("CERTALIAS: " + certalias);
            // Save the new keystore contents
            FileOutputStream out = new FileOutputStream(keystoreFile);
            keystore.store(out, TRUST_STORE_PASS.toCharArray());
            out.close();
            exp = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return exp;
    }

    /**
     * This method exports a certificate either to text (pem format), either to
     * binary (der format)
     *
     * @param cert
     * @param file
     * @param binary
     */
    public static boolean exportCertificate(java.security.cert.Certificate cert, File file, boolean binary) {
        boolean exp = false;
        try {
            // Get the encoded form which is suitable for exporting
            byte[] buf = cert.getEncoded();
            FileOutputStream os = new FileOutputStream(file);
            if (binary) { // Write in binary form
                os.write(buf);
            } else { // Write in text form
                Writer wr = new OutputStreamWriter(os, Charset.forName("UTF-8"));
                wr.write("-----BEGIN CERTIFICATE-----\n");
                wr.write(new String(coder.encodeBase64(buf)));
                wr.write("\n-----END CERTIFICATE-----\n");
                wr.flush();
            }
            os.close();
            exp = true;
        } catch (Exception e) {
            exp = false;
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return exp;
    }

    /**
     * Wrapper method to Security Manager
     *
     */
    public static boolean VerifyTSL(Document doc) {
        boolean verified = false;
        try {
            SignatureManager sm = new SignatureManager();
            sm.verifyEnvelopedSignature(doc);
            verified = true;
        } catch (Exception e) {
            logger.error("Error validating the signature");
        }
        return verified;
    }

    /**
     * Given a URL containing an xml file, it parses it and returns the dom
     * object
     *
     * @param inputFile
     *            the url path as String
     * @return org.w3c.dom.Document
     */
    public static Document createDomFromURL(String strURL) {
        init_variables();
        Document doc = null;
        // Instantiate the document to be signed
        try {
            URL url = new URL(strURL); // Getting URL to invoke
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection(); // Opening
            // connection
            // with
            // the
            // URL
            // specified
            urlCon.setReadTimeout(10000); // Set Read Time out in milliseconds,
            // Setting 1 second as read timeout
            urlCon.connect(); // Connecting
            InputStream iStream = urlCon.getInputStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            doc = dbFactory.newDocumentBuilder().parse(iStream);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return doc;

    }

    public static Document createDomFromURLUsingHttps(String strURL) {
        init_variables();
        Document doc = null;

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
            // URL url = new URL("https://econfig.nczisk.sk:8443");
            URL url = new URL(strURL);
            HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
            urlCon.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });

            // Instantiate the document to be signed
            urlCon.setReadTimeout(10000); // Set Read Time out in milliseconds,
            // Setting 1 second as read timeout
            urlCon.connect(); // Connecting
            InputStream iStream = urlCon.getInputStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            doc = dbFactory.newDocumentBuilder().parse(iStream);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return doc;

    }

    /**
     * Given the pem string of the signature this method returns the X509
     * Certificate of the signature
     *
     * @param pem
     *            String fortmat of the signature
     * @return the X509 certificate
     */
    // public static X509Certificate getCertificateFromString(String pem)
    // {
    // X509Certificate t = null;
    // try
    // {
    // CertificateFactory cf = CertificateFactory.getInstance("X.509");
    // InputStream in = StringToStream(pem);
    // java.security.cert.Certificate c = cf. generateCertificate(in);
    // in.close();
    // t = (X509Certificate) c;
    // }
    // catch (Exception e)
    // {logger.info(e.getMessage());}
    // return t;
    // }
}
