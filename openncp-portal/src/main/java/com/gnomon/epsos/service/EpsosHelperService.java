package com.gnomon.epsos.service;

import com.gnomon.LiferayUtils;
import com.gnomon.epsos.MyServletContextListener;
import com.gnomon.epsos.model.Country;
import com.gnomon.epsos.model.*;
import com.gnomon.epsos.model.cda.*;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.*;
import com.liferay.portal.kernel.service.AddressLocalServiceUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.PhoneLocalServiceUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.portlet.PortletProps;
import edu.mayo.trilliumbridge.core.TrilliumBridgeTransformer;
import edu.mayo.trilliumbridge.core.xslt.XsltTrilliumBridgeTransformer;
import epsos.ccd.gnomon.auditmanager.*;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.gnomon.xslt.EpsosXSLTransformer;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.DefaultKeyStoreManager;
import epsos.ccd.netsmart.securitymanager.sts.client.TRCAssertionRequest;
import epsos.ccd.posam.tm.response.TMResponseStructure;
import epsos.ccd.posam.tm.service.ITransformationService;
import epsos.openncp.protocolterminator.ClientConnectorConsumer;
import epsos.openncp.protocolterminator.clientconnector.*;
import eu.epsos.util.IheConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.allcolor.yahp.converter.CYaHPConverter;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer.CHeaderFooter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tools.ant.util.DateUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.saml2.core.*;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.XSURI;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import tr.com.srdc.epsos.util.Constants;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class EpsosHelperService {

    public static final String PORTAL_CLIENT_CONNECTOR_URL = "PORTAL_CLIENT_CONNECTOR_URL";
    public static final String PORTAL_DOCTOR_PERMISSIONS = "PORTAL_DOCTOR_PERMISSIONS";
    public static final String PORTAL_PHARMACIST_PERMISSIONS = "PORTAL_PHARMACIST_PERMISSIONS";
    public static final String PORTAL_NURSE_PERMISSIONS = "PORTAL_NURSE_PERMISSIONS";
    public static final String PORTAL_ADMIN_PERMISSIONS = "PORTAL_ADMIN_PERMISSIONS";
    public static final String PORTAL_PATIENT_PERMISSIONS = "PORTAL_PATIENT_PERMISSIONS";
    public static final String PORTAL_TEST_ASSERTIONS = "PORTAL_TEST_ASSERTIONS";
    public static final String PORTAL_CHECK_PERMISSIONS = "PORTAL_CHECK_PERMISSIONS";
    public static final String PORTAL_HCER_ENABLED = "PORTAL_HCER_ENABLED";
    public static final String PORTAL_PACREP_ENABLED = "PORTAL_PACREP_ENABLED";
    public static final String PORTAL_MRO_ENABLED = "PORTAL_MRO_ENABLED";
    public static final String PORTAL_CCD_ENABLED = "PORTAL_CCD_ENABLED";
    public static final String PORTAL_CONSENT_ENABLED = "PORTAL_CONSENT_ENABLED";
    public static final String PORTAL_CDA_STYLESHEET = "CDA_STYLESHEET";
    public static final String PORTAL_DISPENSATION_COUNTRY = "PORTAL_DISPENSATION_COUNTRY";
    public static final String PORTAL_LEGAL_AUTHENTICATOR_FIRSTNAME = "PORTAL_LEGAL_AUTHENTICATOR_FIRSTNAME";
    public static final String PORTAL_LEGAL_AUTHENTICATOR_LASTNAME = "PORTAL_LEGAL_AUTHENTICATOR_LASTNAME";
    public static final String PORTAL_LEGAL_AUTHENTICATOR_CITY = "PORTAL_LEGAL_AUTHENTICATOR_CITY";
    public static final String PORTAL_LEGAL_AUTHENTICATOR_POSTALCODE = "PORTAL_LEGAL_AUTHENTICATOR_POSTALCODE";
    public static final String PORTAL_CUSTODIAN_NAME = "PORTAL_CUSTODIAN_NAME";
    public static final String PORTAL_CONSENT_OID = "PORTAL_CONSENT_OID";
    public static final String PORTAL_DISPENSATION_OID = "PORTAL_DISPENSATION_OID";
    public static final String PORTAL_PATIENTS_OID = "PORTAL_PATIENTS_OID";
    public static final String PORTAL_PHARMACIST_OID = "PORTAL_PHARMACIST_OID";
    public static final String PORTAL_PHARMACIES_OID = "PORTAL_PHARMACIES_OID";
    public static final String PORTAL_DOCTOR_OID = "PORTAL_DOCTOR_OID";
    public static final String PORTAL_HOSPITAL_OID = "PORTAL_HOSPITAL_OID";
    public static final String PORTAL_ORDER_OID = "PORTAL_ORDER_OID";
    public static final String PORTAL_ENTRY_OID = "PORTAL_ENTRY_OID";
    public static final String PORTAL_CUSTODIAN_OID = "PORTAL_CUSTODIAN_OID";
    public static final String PORTAL_LEGAL_AUTHENTICATOR_PERSON_OID = "PORTAL_LEGAL_AUTHENTICATOR_PERSON_OID";
    public static final String PORTAL_LEGAL_AUTHENTICATOR_ORG_OID = "PORTAL_LEGAL_AUTHENTICATOR_ORG_OID";

    private static final Logger log = LoggerFactory.getLogger(EpsosHelperService.class.getName());
    private static final Base64 decode = new Base64();
    public final static SimpleDateFormat dateMetaDataFormat = new SimpleDateFormat(
            "yyyyMMdd");

    public EpsosHelperService() {
        super();
    }

    public static List getEHPLTRLanguages() {
        Map<String, String> langs = new HashMap<String, String>();
        List<String> ltrLanguages = new ArrayList();
        try {
            ITransformationService tService = MyServletContextListener
                    .getTransformationService();

            ltrLanguages = tService.getLtrLanguages();

            for (int i = 0; i < ltrLanguages.size(); i++) {
                String language = ltrLanguages.get(i);
                language = language.replaceAll("-", "_");
                ltrLanguages.set(i, language);
            }
        } catch (Exception e) {
            log.error("Error getting ltrlanguages");
            log.error(ExceptionUtils.getStackTrace(e));
        }
        if (langs.isEmpty()) {
            langs.put("en_GB", "en_GB");
        }
        return ltrLanguages;
    }

    public static Map<String, String> getLTRLanguages() {
        Map<String, String> langs = new HashMap<String, String>();
        try {
            ITransformationService tService = MyServletContextListener
                    .getTransformationService();

            List<String> ltrLanguages = tService.getLtrLanguages();

            for (int i = 0; i < ltrLanguages.size(); i++) {
                langs.put(ltrLanguages.get(i).trim(), ltrLanguages.get(i)
                        .trim());
                log.debug("Language is: " + ltrLanguages.get(i));
            }
        } catch (Exception e) {
            log.error("Error getting ltrlanguages");
            log.error(ExceptionUtils.getStackTrace(e));
        }
        if (langs.isEmpty()) {
            langs.put("en-GB", "en-GB");
        }
        return langs;
    }

    public static CDAHeader setPharmInfo(CDAHeader cda, PersonDetail pd) {
        cda.setPharmacistAddress(pd.getAddress());
        cda.setPharmacistCity(pd.getCity());
        cda.setPharmacistPostalCode(pd.getPostalCode());
        cda.setPharmacistCountry(pd.getCountry());
        cda.setPharmacistTelephone(pd.getTelephone());
        cda.setPharmacistEmail(pd.getEmail());
        cda.setPharmacistFamilyName(pd.getLastname());
        cda.setPharmacistGivenName(pd.getFirstname());
        cda.setPharmacistOrgId(pd.getOrgid());
        cda.setPharmacistOrgName(pd.getOrgname());
        cda.setPharmacistOrgAddress(pd.getAddress());
        cda.setPharmacistOrgTelephone(pd.getTelephone());
        cda.setPharmacistOrgEmail(pd.getEmail());
        cda.setPharmacistOrgCity(pd.getCity());
        cda.setPharmacistOrgPostalCode(pd.getPostalCode());
        cda.setPharmacistOrgCountry(pd.getCountry());
        return cda;
    }

    public static String createConsent(Patient patient, String consentCode,
                                       String consentDisplayName, String consentStartDate,
                                       String consentEndDate) {
        String rolename = "";
        String pharmacistsOid = EpsosHelperService
                .getConfigProperty(EpsosHelperService.PORTAL_PHARMACIST_OID);
        String doctorsOid = EpsosHelperService
                .getConfigProperty(EpsosHelperService.PORTAL_DOCTOR_OID);

        User user = LiferayUtils.getPortalUser();
        PersonDetail pd = getUserInfo(pharmacistsOid, user);

        CDAHeader cda = new CDAHeader();
        Date now = new Date();
        cda.setEffectiveTime(EpsosHelperService.formatDateHL7(now));
        cda.setLanguageCode(user.getLanguageId());
        cda.setConsentCode(consentCode);
        cda.setConsentDisplayName(consentDisplayName);
        cda.setConsentStartDate(consentStartDate);
        cda.setConsentEndDate(consentEndDate);
        if (LiferayUtils.isDoctor(user.getUserId(), user.getCompanyId())) {
            rolename = "doctor";
            cda.setDoctorOid(doctorsOid);
            cda = setPharmInfo(cda, pd);
        }
        if (LiferayUtils.isPharmacist(user.getUserId(), user.getCompanyId())) {
            rolename = "pharmacist";
            cda.setPharmacistOid(pharmacistsOid);
            cda = setPharmInfo(cda, pd);
        }

        cda.setPatientId(patient.getExtension());
        cda.setPatientFamilyName(patient.getName());
        cda.setPatientGivenName(patient.getFamilyName());
        cda.setPatientCountry(patient.getCountry());
        cda.setPatientBirthDate(patient.getBirthDate());
        cda.setPatientSex(patient.getAdministrativeGender());
        cda.setPatientCity(patient.getCity());
        cda.setPatientCountry(patient.getCountry());
        cda.setPatientTelephone(patient.getTelephone());
        cda.setPatientEmail(patient.getEmail());
        cda.setPatientPostalCode(patient.getPostalCode());
        String consent = CDAUtils.CDAModelToConsent(cda, rolename);
        log.info("#### Consent CDA Start");
        log.info(consent);
        log.info("#### Consent CDA End");
        return consent;
    }

    private static PersonDetail getUserInfo(String oid, User user) {

        if (Validator.isNotNull(oid)) {
            oid = oid + ".";
        }
        PersonDetail pd = new PersonDetail();
        pd.setFirstname(user.getFirstName());
        pd.setLastname(user.getLastName());
        pd.setOrgname(user.getLastName() + " " + user.getFirstName());
        pd.setEmail(user.getEmailAddress());
        pd.setOrgid(oid + user.getUserId() + "");
        pd.setTelephone("");

        List<Address> addresses;
        List<Phone> phones;
        try {
            addresses = AddressLocalServiceUtil.getAddresses(
                    user.getCompanyId(), Contact.class.getName(),
                    user.getContactId());
            phones = PhoneLocalServiceUtil.getPhones(user.getCompanyId(),
                    Contact.class.getName(), user.getContactId());
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                pd.setAddress(address.getStreet1());
                pd.setCity(address.getCity());
                pd.setPostalCode(address.getZip());
            }
            pd.setCountry(ConfigurationManagerService.getInstance()
                    .getProperty("ncp.country"));
            if (!phones.isEmpty()) {
                pd.setTelephone(phones.get(0).getNumber());
            }
        } catch (SystemException e1) {
            log.error("Error getting contact info addreses");
            log.error(ExceptionUtils.getStackTrace(e1));
        }

        return pd;
    }

    public static byte[] generateDispensationDocumentFromPrescription2(
            byte[] bytes, List<ViewResult> dispensedLines, User user) {

        PersonDetail pd = getUserInfo("", user);

        String edDoc = "";
        CDAHeader cda = new CDAHeader();
        Date now = new Date();
        String language = user.getLanguageId().replace("_", "-");
        cda.setEffectiveTime(EpsosHelperService.formatDateHL7(now));
        cda.setLanguageCode(language);
        cda = setPharmInfo(cda, pd);

        List<EDDetail> edDetails = new ArrayList<EDDetail>();
        for (int i = 0; i < dispensedLines.size(); i++) {
            ViewResult d_line = (ViewResult) dispensedLines.get(i);
            EDDetail ed = new EDDetail();
            ed.setRelativePrescriptionLineId(d_line.getField1().toString());
            ed.setDispensedQuantity(d_line.getField7().toString());
            ed.setMedicineFormCode(d_line.getField5().toString());
            ed.setMedicineCommercialName(d_line.getField2().toString());
            // Setting the substitution indicator
            ed.setSubstituted((Boolean) d_line.getField3());
            edDetails.add(ed);
        }
        cda.setEDDetail(edDetails);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document epDoc = db.parse(new ByteArrayInputStream(bytes));
            cda.setPrescriptionBarcode(CDAUtils
                    .getRelativePrescriptionBarcode(epDoc));
            cda.setDispensationId("D-"
                    + CDAUtils.getRelativePrescriptionBarcode(epDoc));
            edDoc = CDAUtils.createDispensation(epDoc, cda);
            log.info("### DISPENSATION START ###");
            log.info(edDoc);
            log.info("### DISPENSATION END ###");
        } catch (Exception e) {
            log.error("error creating disp doc");
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return edDoc.getBytes();
    }

    public static ByteArrayOutputStream ConvertHTMLtoPDF(String htmlin,
                                                         String uri, String fontpath) {

        String cleanCDA = "";
        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        // props.setTreatUnknownTagsAsContent(true);
        props.setOmitUnknownTags(true);
        log.info("Cleaner init");
        TagNode node = cleaner.clean(htmlin);
        cleanCDA = new PrettyXmlSerializer(props).getAsString(node);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CYaHPConverter converter = new CYaHPConverter();

        try {
            List<CHeaderFooter> headerFooterList = new ArrayList<CHeaderFooter>();
            Map<String, String> properties = new HashMap<String, String>();
            headerFooterList
                    .add(new IHtmlToPdfTransformer.CHeaderFooter(
                            "<table width=\"100%\"><tbody><tr><td align=\"left\">Generated by OpenNCP Portal.</td><td align=\"right\">Page <pagenumber>/<pagecount></td></tr></tbody></table>",
                            IHtmlToPdfTransformer.CHeaderFooter.HEADER));
            headerFooterList.add(new IHtmlToPdfTransformer.CHeaderFooter(
                    "© 2014 Generated by OpenNCP Portal",
                    IHtmlToPdfTransformer.CHeaderFooter.FOOTER));

            properties.put(IHtmlToPdfTransformer.PDF_RENDERER_CLASS,
                    IHtmlToPdfTransformer.FLYINGSAUCER_PDF_RENDERER);
            properties.put(IHtmlToPdfTransformer.FOP_TTF_FONT_PATH, fontpath);

            converter.convertToPdf(cleanCDA, IHtmlToPdfTransformer.A4P,
                    headerFooterList, uri, out, properties);

            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error converting html to pdf");
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return out;
    }

    public static List<ViewResult> parsePrescriptionDocumentForPrescriptionLines(
            byte[] bytes) {
        List<ViewResult> lines = new ArrayList<ViewResult>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(new ByteArrayInputStream(bytes));

            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new CDANameSpaceContext());

            XPathExpression performerPrefixExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:author/xsi:assignedAuthor/xsi:assignedPerson/xsi:name/xsi:prefix");
            XPathExpression performerSurnameExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:author/xsi:assignedAuthor/xsi:assignedPerson/xsi:name/xsi:family");
            XPathExpression performerGivenNameExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:author/xsi:assignedAuthor/xsi:assignedPerson/xsi:name/xsi:given");
            XPathExpression professionExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:author/xsi:functionCode");
            XPathExpression facilityNameExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:author/xsi:assignedAuthor/xsi:representedOrganization/xsi:name");
            XPathExpression facilityAddressStreetExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:author/xsi:assignedAuthor/xsi:representedOrganization/xsi:addr/xsi:streetAddressLine");
            XPathExpression facilityAddressZipExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:author/xsi:assignedAuthor/xsi:representedOrganization/xsi:addr/xsi:postalCode");
            XPathExpression facilityAddressCityExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:author/xsi:assignedAuthor/xsi:representedOrganization/xsi:addr/xsi:city");
            XPathExpression facilityAddressCountryExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:author/xsi:assignedAuthor/xsi:representedOrganization/xsi:addr/xsi:country");
            XPathExpression prescriptionIDExpr = xpath
                    .compile("/xsi:ClinicalDocument/xsi:component/xsi:structuredBody/xsi:component/xsi:section[xsi:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']");

            String performer = "";
            Node performerPrefix = (Node) performerPrefixExpr.evaluate(dom,
                    XPathConstants.NODE);
            if (performerPrefix != null) {
                performer += performerPrefix.getTextContent().trim() + " ";
            }
            Node performerSurname = (Node) performerSurnameExpr.evaluate(dom,
                    XPathConstants.NODE);
            if (performerSurname != null) {
                performer += performerSurname.getTextContent().trim();
            }
            Node performerGivenName = (Node) performerGivenNameExpr.evaluate(
                    dom, XPathConstants.NODE);
            if (performerGivenName != null) {
                performer += " " + performerGivenName.getTextContent().trim();
            }

            String profession = "";
            Node professionNode = (Node) professionExpr.evaluate(dom,
                    XPathConstants.NODE);
            if (professionNode != null) {
                profession += professionNode.getAttributes()
                        .getNamedItem("displayName").getNodeValue();
            }

            String facility = "";
            Node facilityNode = (Node) facilityNameExpr.evaluate(dom,
                    XPathConstants.NODE);
            if (facilityNode != null) {
                facility += facilityNode.getTextContent().trim();
            }

            String address = "";
            Node street = (Node) facilityAddressStreetExpr.evaluate(dom,
                    XPathConstants.NODE);
            if (street != null) {
                address += street.getTextContent().trim();
            }
            Node zip = (Node) facilityAddressZipExpr.evaluate(dom,
                    XPathConstants.NODE);
            if (zip != null) {
                address += ", " + zip.getTextContent().trim();
            }
            Node city = (Node) facilityAddressCityExpr.evaluate(dom,
                    XPathConstants.NODE);
            if (city != null) {
                address += ", " + city.getTextContent().trim();
            }
            Node country = (Node) facilityAddressCountryExpr.evaluate(dom,
                    XPathConstants.NODE);
            if (country != null) {
                address += ", " + country.getTextContent().trim();
            }

            // for each prescription component, search for its entries and make
            // up the list
            String prescriptionID = "";
            NodeList prescriptionIDNodes = (NodeList) prescriptionIDExpr
                    .evaluate(dom, XPathConstants.NODESET);
            if (prescriptionIDNodes != null
                    && prescriptionIDNodes.getLength() > 0) {
                XPathExpression idExpr = xpath.compile("xsi:id");
                XPathExpression entryExpr = xpath
                        .compile("xsi:entry/xsi:substanceAdministration");
                XPathExpression nameExpr = xpath
                        .compile("xsi:consumable/xsi:manufacturedProduct/xsi:manufacturedMaterial/xsi:name");

                XPathExpression freqExpr = xpath
                        .compile("xsi:effectiveTime[@type='PIVL_TS']/xsi:period");
                XPathExpression doseExpr = xpath.compile("xsi:doseQuantity");
                XPathExpression doseExprLow = xpath.compile("xsi:low");
                XPathExpression doseExprHigh = xpath.compile("xsi:high");
                XPathExpression doseFormExpr = xpath
                        .compile("xsi:consumable/xsi:manufacturedProduct/xsi:manufacturedMaterial/epsos:formCode");
                XPathExpression packQuantityExpr = xpath
                        .compile("xsi:consumable/xsi:manufacturedProduct/xsi:manufacturedMaterial/epsos:asContent/epsos:quantity/epsos:numerator[@type='epsos:PQ']");
                XPathExpression packQuantityExpr2 = xpath
                        .compile("xsi:consumable/xsi:manufacturedProduct/xsi:manufacturedMaterial/epsos:asContent/epsos:quantity/epsos:denominator[@type='epsos:PQ']");
                XPathExpression packTypeExpr = xpath
                        .compile("xsi:consumable/xsi:manufacturedProduct/xsi:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode");

                XPathExpression packageExpr = xpath
                        .compile("xsi:consumable/xsi:manufacturedProduct/xsi:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine/epsos:capacityQuantity");

                XPathExpression ingredientExpr = xpath
                        .compile("xsi:consumable/xsi:manufacturedProduct/xsi:manufacturedMaterial/epsos:ingredient[@classCode='ACTI']/epsos:ingredient/epsos:code");
                XPathExpression strengthExpr = xpath
                        .compile("xsi:consumable/xsi:manufacturedProduct/xsi:manufacturedMaterial/epsos:ingredient[@classCode='ACTI']/epsos:quantity/epsos:numerator[@type='epsos:PQ']");
                XPathExpression strengthExpr2 = xpath
                        .compile("xsi:consumable/xsi:manufacturedProduct/xsi:manufacturedMaterial/epsos:ingredient[@classCode='ACTI']/epsos:quantity/epsos:denominator[@type='epsos:PQ']");

                XPathExpression nrOfPacksExpr = xpath
                        .compile("xsi:entryRelationship/xsi:supply/xsi:quantity");
                // XPathExpression nrOfPacksExpr =
                // xpath.compile("consumable/manufacturedProduct/manufacturedMaterial/asContent/quantity/denominator[@type='epsos:PQ']");

                XPathExpression routeExpr = xpath.compile("xsi:routeCode");
                XPathExpression lowExpr = xpath
                        .compile("xsi:effectiveTime[@type='IVL_TS']/xsi:low");
                XPathExpression highExpr = xpath
                        .compile("xsi:effectiveTime[@type='IVL_TS']/xsi:high");
                XPathExpression patientInstrEexpr = xpath
                        .compile("xsi:entryRelationship/xsi:act/xsi:code[@code='PINSTRUCT']/../xsi:text/xsi:reference[@value]");
                XPathExpression fillerInstrEexpr = xpath
                        .compile("xsi:entryRelationship/xsi:act/xsi:code[@code='FINSTRUCT']/../xsi:text/xsi:reference[@value]");
                XPathExpression substituteInstrExpr = xpath
                        .compile("xsi:entryRelationship[@typeCode='SUBJ'][@inversionInd='true']/xsi:observation[@classCode='OBS']/xsi:value");

                XPathExpression prescriberPrefixExpr = xpath
                        .compile("xsi:author/xsi:assignedAuthor/xsi:assignedPerson/xsi:name/xsi:prefix");
                XPathExpression prescriberSurnameExpr = xpath
                        .compile("xsi:author/xsi:assignedAuthor/xsi:assignedPerson/xsi:name/xsi:family");
                XPathExpression prescriberGivenNameExpr = xpath
                        .compile("xsi:author/xsi:assignedAuthor/xsi:assignedPerson/xsi:name/xsi:given");

                for (int p = 0; p < prescriptionIDNodes.getLength(); p++) {
                    Node sectionNode = prescriptionIDNodes.item(p);
                    Node pIDNode = (Node) idExpr.evaluate(sectionNode,
                            XPathConstants.NODE);
                    if (pIDNode != null) {
                        try {
                            prescriptionID = pIDNode.getAttributes()
                                    .getNamedItem("extension").getNodeValue();
                            // prescriptionID =
                            // pIDNode.getAttributes().getNamedItem("root").getNodeValue();
                        } catch (Exception e) {
                            log.error(ExceptionUtils.getStackTrace(e));
                        }
                    } else {
                        prescriptionID = "";
                    }

                    String prescriber = "";
                    Node prescriberPrefix = (Node) prescriberPrefixExpr
                            .evaluate(sectionNode, XPathConstants.NODE);
                    if (prescriberPrefix != null) {
                        prescriber += prescriberPrefix.getTextContent().trim()
                                + " ";
                    }
                    Node prescriberSurname = (Node) prescriberSurnameExpr
                            .evaluate(sectionNode, XPathConstants.NODE);
                    if (prescriberSurname != null) {
                        prescriber += prescriberSurname.getTextContent().trim();
                    }
                    Node prescriberGivenName = (Node) prescriberGivenNameExpr
                            .evaluate(sectionNode, XPathConstants.NODE);
                    if (prescriberGivenName != null) {
                        prescriber += " "
                                + prescriberGivenName.getTextContent().trim();
                    }

                    if (Validator.isNull(prescriber)) {
                        prescriber = performer;
                    }

                    // PRESCRIPTION ITEMS
                    NodeList entryList = (NodeList) entryExpr.evaluate(
                            sectionNode, XPathConstants.NODESET);
                    if (entryList != null && entryList.getLength() > 0) {
                        for (int i = 0; i < entryList.getLength(); i++) {
                            ViewResult line = new ViewResult(i);

                            Node entryNode = entryList.item(i);

                            String materialID = "";
                            Node materialIDNode = (Node) idExpr.evaluate(
                                    entryNode, XPathConstants.NODE);
                            if (materialIDNode != null) {
                                try {
                                    materialID = materialIDNode.getAttributes()
                                            .getNamedItem("extension")
                                            .getNodeValue();
                                } catch (Exception e) {
                                    log.error("Error getting material");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                }
                            }

                            Node materialName = (Node) nameExpr.evaluate(
                                    entryNode, XPathConstants.NODE);

                            String name = "";
                            try {
                                name = materialName.getTextContent().trim();
                            } catch (Exception e) {
                                log.error("Error getting material name");
                                log.error(ExceptionUtils.getStackTrace(e));
                            }

                            String packsString = "";
                            Node doseForm = (Node) doseFormExpr.evaluate(
                                    entryNode, XPathConstants.NODE);
                            if (doseForm != null) {
                                packsString = doseForm.getAttributes()
                                        .getNamedItem("displayName")
                                        .getNodeValue();
                            }

                            Node packageExpr1 = (Node) packageExpr.evaluate(
                                    entryNode, XPathConstants.NODE);
                            Node packType = (Node) packTypeExpr.evaluate(
                                    entryNode, XPathConstants.NODE);
                            Node packQuant = (Node) packQuantityExpr.evaluate(
                                    entryNode, XPathConstants.NODE);
                            Node packQuant2 = (Node) packQuantityExpr2
                                    .evaluate(entryNode, XPathConstants.NODE);

                            String dispensedPackage = "";
                            String dispensedPackageUnit = "";
                            if (packageExpr1 != null) {
                                dispensedPackage = packageExpr1.getAttributes()
                                        .getNamedItem("value").getNodeValue();
                                dispensedPackageUnit = packageExpr1
                                        .getAttributes().getNamedItem("unit")
                                        .getNodeValue();
                            }
                            if (packQuant != null && packType != null
                                    && packQuant2 != null) {
                                packsString += "#"
                                        + packType.getAttributes()
                                        .getNamedItem("displayName")
                                        .getNodeValue()
                                        + "#"
                                        + packQuant.getAttributes()
                                        .getNamedItem("value")
                                        .getNodeValue();
                                String unit = packQuant.getAttributes()
                                        .getNamedItem("unit").getNodeValue();
                                if (unit != null && !unit.equals("1")) {
                                    packsString += " " + unit;
                                }
                                String denom = packQuant2.getAttributes()
                                        .getNamedItem("value").getNodeValue();
                                if (denom != null && !denom.equals("1")) {
                                    packsString += " / " + denom;
                                    unit = packQuant2.getAttributes()
                                            .getNamedItem("unit")
                                            .getNodeValue();
                                    if (unit != null && !unit.equals("1")) {
                                        packsString += " " + unit;
                                    }
                                }

                            }

                            String ingredient = "";
                            Node ingrNode = (Node) ingredientExpr.evaluate(
                                    entryNode, XPathConstants.NODE);
                            if (ingrNode != null) {
                                ingredient += ingrNode.getAttributes()
                                        .getNamedItem("code").getNodeValue()
                                        + " - "
                                        + ingrNode.getAttributes()
                                        .getNamedItem("displayName")
                                        .getNodeValue();
                            }

                            String strength = "";
                            Node strengthExprNode = (Node) strengthExpr
                                    .evaluate(entryNode, XPathConstants.NODE);
                            Node strengthExprNode2 = (Node) strengthExpr2
                                    .evaluate(entryNode, XPathConstants.NODE);
                            if (strengthExprNode != null
                                    && strengthExprNode2 != null) {
                                try {
                                    strength = strengthExprNode.getAttributes()
                                            .getNamedItem("value")
                                            .getNodeValue();
                                } catch (Exception e) {
                                    log.error("Error parsing strength");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                    strength = "";
                                }
                                String unit = "";
                                String unit2 = "";
                                try {
                                    unit = strengthExprNode.getAttributes()
                                            .getNamedItem("unit")
                                            .getNodeValue();
                                } catch (Exception e) {
                                    log.error("Error parsing unit");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                }
                                if (unit != null && !unit.equals("1")) {
                                    strength += " " + unit;
                                }
                                String denom = "";
                                try {
                                    denom = strengthExprNode2.getAttributes()
                                            .getNamedItem("value")
                                            .getNodeValue();
                                } catch (Exception e) {
                                    log.error("Error parsing denom");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                }
                                if (denom != null) // && !denom.equals("1"))
                                {
                                    strength += " / " + denom;
                                    try {
                                        unit2 = strengthExprNode2
                                                .getAttributes()
                                                .getNamedItem("unit")
                                                .getNodeValue();
                                    } catch (Exception e) {
                                        log.error("Error parsing unit 2");
                                        log.error(ExceptionUtils
                                                .getStackTrace(e));
                                    }
                                    if (unit2 != null && !unit2.equals("1")) {
                                        strength += " " + unit2;
                                    }
                                }

                            }

                            String nrOfPacks = "";
                            Node nrOfPacksNode = (Node) nrOfPacksExpr.evaluate(
                                    entryNode, XPathConstants.NODE);
                            if (nrOfPacksNode != null) {
                                if (nrOfPacksNode.getAttributes().getNamedItem(
                                        "value") != null) {
                                    nrOfPacks = nrOfPacksNode.getAttributes()
                                            .getNamedItem("value")
                                            .getNodeValue();
                                }
                                if (nrOfPacksNode.getAttributes().getNamedItem(
                                        "unit") != null) {
                                    String unit = nrOfPacksNode.getAttributes()
                                            .getNamedItem("unit")
                                            .getNodeValue();
                                    if (unit != null && !unit.equals("1")) {
                                        nrOfPacks += " " + unit;
                                    }
                                }
                            }

                            String doseString = "";
                            Node dose = (Node) doseExpr.evaluate(entryNode,
                                    XPathConstants.NODE);
                            if (dose != null) {
                                if (dose.getAttributes().getNamedItem("value") != null) {
                                    doseString = dose.getAttributes()
                                            .getNamedItem("value")
                                            .getNodeValue();
                                    if (dose.getAttributes().getNamedItem(
                                            "unit") != null) {
                                        String unit = dose.getAttributes()
                                                .getNamedItem("unit")
                                                .getNodeValue();
                                        if (unit != null && !unit.equals("1")) {
                                            doseString += " " + unit;
                                        }
                                    }
                                } else {
                                    String lowString = "", highString = "";
                                    Node lowDoseNode = (Node) doseExprLow
                                            .evaluate(dose, XPathConstants.NODE);
                                    if (lowDoseNode != null
                                            && lowDoseNode.getAttributes()
                                            .getNamedItem("value") != null) {
                                        lowString = lowDoseNode.getAttributes()
                                                .getNamedItem("value")
                                                .getNodeValue();
                                        if (lowDoseNode.getAttributes()
                                                .getNamedItem("unit") != null) {
                                            String unit = lowDoseNode
                                                    .getAttributes()
                                                    .getNamedItem("unit")
                                                    .getNodeValue();
                                            if (unit != null
                                                    && !unit.equals("1")) {
                                                lowString += " " + unit;
                                            }
                                        }
                                    }
                                    Node highDoseNode = (Node) doseExprHigh
                                            .evaluate(dose, XPathConstants.NODE);
                                    if (highDoseNode != null
                                            && highDoseNode.getAttributes()
                                            .getNamedItem("value") != null) {
                                        highString = highDoseNode
                                                .getAttributes()
                                                .getNamedItem("value")
                                                .getNodeValue();
                                        if (highDoseNode.getAttributes()
                                                .getNamedItem("unit") != null) {
                                            String unit = highDoseNode
                                                    .getAttributes()
                                                    .getNamedItem("unit")
                                                    .getNodeValue();
                                            if (unit != null
                                                    && !unit.equals("1")) {
                                                highString += " " + unit;
                                            }
                                        }
                                    }

                                    doseString = Validator.isNotNull(lowString) ? lowString
                                            : "";
                                    if (Validator.isNotNull(highString)
                                            && !lowString.equals(highString)) {
                                        doseString = Validator
                                                .isNotNull(doseString) ? doseString
                                                + " - " + highString
                                                : highString;
                                    }
                                }
                            }

                            String freqString = "";
                            Node period = (Node) freqExpr.evaluate(entryNode,
                                    XPathConstants.NODE);
                            if (period != null) {
                                try {
                                    freqString = getSafeString(period
                                            .getAttributes()
                                            .getNamedItem("value")
                                            .getNodeValue()
                                            + period.getAttributes()
                                            .getNamedItem("unit")
                                            .getNodeValue());
                                } catch (Exception e) {
                                    log.error("Error getting freqstring");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                }
                            }

                            String routeString = "";
                            Node route = (Node) routeExpr.evaluate(entryNode,
                                    XPathConstants.NODE);
                            if (route != null) {
                                try {
                                    routeString = getSafeString(route
                                            .getAttributes()
                                            .getNamedItem("displayName")
                                            .getNodeValue());
                                } catch (Exception e) {
                                    log.error("error getting route string");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                }
                            }

                            String patientString = "";
                            Node patientInfo = (Node) patientInstrEexpr
                                    .evaluate(entryNode, XPathConstants.NODE);
                            if (patientInfo != null) {
                                try {
                                    patientString = getSafeString(patientInfo
                                            .getAttributes()
                                            .getNamedItem("value")
                                            .getNodeValue());
                                } catch (Exception e) {
                                    log.error("error getting route string");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                }
                            }

                            String fillerString = "";
                            Node fillerInfo = (Node) fillerInstrEexpr.evaluate(
                                    entryNode, XPathConstants.NODE);
                            if (fillerInfo != null) {
                                try {
                                    fillerString = getSafeString(fillerInfo
                                            .getAttributes()
                                            .getNamedItem("value")
                                            .getNodeValue());
                                } catch (Exception e) {
                                    log.error("error getting route string");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                }
                            }

                            String lowString = "";
                            Node lowNode = (Node) lowExpr.evaluate(entryNode,
                                    XPathConstants.NODE);
                            if (lowNode != null) {
                                try {
                                    lowString = lowNode.getAttributes()
                                            .getNamedItem("value")
                                            .getNodeValue();
                                    lowString = dateDecorate(lowString);
                                } catch (Exception e) {
                                    log.error("Error parsing low node ...");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                }

                            }

                            String highString = "";
                            Node highNode = (Node) highExpr.evaluate(entryNode,
                                    XPathConstants.NODE);
                            if (highNode != null) {
                                try {
                                    highString = highNode.getAttributes()
                                            .getNamedItem("value")
                                            .getNodeValue();
                                    highString = dateDecorate(highString);
                                } catch (Exception e) {
                                    log.error("Error parsing high node ...");
                                    log.error(ExceptionUtils.getStackTrace(e));
                                }
                            }

                            Boolean substitutionPermitted = Boolean.TRUE;
                            Node substituteNode = (Node) substituteInstrExpr
                                    .evaluate(entryNode, XPathConstants.NODE);
                            if (substituteNode != null) {
                                String substituteValue = "";
                                try {
                                    substituteValue = substituteNode
                                            .getAttributes()
                                            .getNamedItem("code")
                                            .getNodeValue();
                                } catch (Exception e) {
                                    substituteValue = "N";
                                }
                                if (substituteValue.equals("N")) {
                                    substitutionPermitted = false;
                                }
                                if (substituteValue.equals("EC")) {
                                    substitutionPermitted = true;
                                }
                                if (!substituteValue.equals("N")
                                        && !substituteValue.equals("EC")) {
                                    substitutionPermitted = false;
                                }

                            }

                            line.setField1(name);
                            line.setField2(ingredient);
                            line.setField3(strength);
                            line.setField4(packsString);

                            line.setField5(doseString);
                            line.setField6(freqString);
                            line.setField7(routeString);
                            line.setField8(nrOfPacks);

                            line.setField9(lowString);
                            line.setField10(highString);

                            line.setField11(patientString);
                            line.setField12(fillerString);

                            line.setField13(prescriber);

                            // entry header information
                            line.setField14(prescriptionID);

                            // prescription header information
                            line.setField15(performer);
                            line.setField16(profession);
                            line.setField17(facility);
                            line.setField18(address);

                            line.setField19(materialID);

                            line.setField20(substitutionPermitted);
                            line.setField21(dispensedPackage);
                            line.setField22(dispensedPackageUnit);
                            line.setMainid(lines.size());

                            lines.add(line);
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return lines;
    }

    private static void signSAMLAssertion(SignableSAMLObject as, String keyAlias)
            throws Exception {

        String KEYSTORE_LOCATION = Constants.NCP_SIG_KEYSTORE_PATH;
        String KEY_STORE_PASS = Constants.NCP_SIG_KEYSTORE_PASSWORD;
        String KEY_ALIAS = Constants.NCP_SIG_PRIVATEKEY_ALIAS;
        String PRIVATE_KEY_PASS = Constants.NCP_SIG_PRIVATEKEY_PASSWORD;

        KeyStoreManager keyManager = new DefaultKeyStoreManager();
        X509Certificate cert = null;
        PrivateKey privateKey = null;
        if (keyAlias == null) {
            cert = (X509Certificate) keyManager.getDefaultCertificate();
        } else {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            File file = new File(KEYSTORE_LOCATION);
            keyStore.load(new FileInputStream(file),
                    KEY_STORE_PASS.toCharArray());
            privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS,
                    PRIVATE_KEY_PASS.toCharArray());
            cert = (X509Certificate) keyManager.getCertificate(keyAlias);
        }

        org.opensaml.xml.signature.Signature sig = (org.opensaml.xml.signature.Signature) Configuration
                .getBuilderFactory()
                .getBuilder(
                        org.opensaml.xml.signature.Signature.DEFAULT_ELEMENT_NAME)
                .buildObject(
                        org.opensaml.xml.signature.Signature.DEFAULT_ELEMENT_NAME);
        Credential signingCredential = SecurityHelper.getSimpleCredential(cert,
                privateKey);

        sig.setSigningCredential(signingCredential);
        sig.setSignatureAlgorithm("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
        sig.setCanonicalizationAlgorithm("http://www.w3.org/2001/10/xml-exc-c14n#");

        SecurityConfiguration secConfig = Configuration
                .getGlobalSecurityConfiguration();
        try {
            SecurityHelper.prepareSignatureParams(sig, signingCredential,
                    secConfig, null);
        } catch (SecurityException e) {
            throw new SMgrException(e.getMessage(), e);
        }

        as.setSignature(sig);
        try {
            Configuration.getMarshallerFactory().getMarshaller(as).marshall(as);
        } catch (MarshallingException e) {
            throw new SMgrException(e.getMessage(), e);
        }
        try {
            org.opensaml.xml.signature.Signer.signObject(sig);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public static Object getUserAssertion() {
        User user = LiferayUtils.getPortalUser();
        return getUserAssertion(user);
    }

    public static Object getUserAssertion(User user) {

        log.info("User is: " + user.getScreenName());
        Assertion assertion = null;

        try {
            boolean isPhysician = LiferayUtils.isDoctor(user.getUserId(),
                    user.getCompanyId());
            boolean isPharmacist = LiferayUtils.isPharmacist(user.getUserId(),
                    user.getCompanyId());
            boolean isNurse = LiferayUtils.isNurse(user.getUserId(),
                    user.getCompanyId());
            boolean isAdministrator = LiferayUtils.isAdministrator(
                    user.getUserId(), user.getCompanyId());
            boolean isPatient = LiferayUtils.isPatient(user.getUserId(),
                    user.getCompanyId());

            if (isPhysician || isPharmacist || isNurse || isAdministrator
                    || isPatient) {
                log.info("The portal role is one of the expected. Continuing ...");
            } else {
                log.error("The portal role is NOT one of the expected. Break");
                return "Error creating assertion for user. Role not compatible with EPSOS";
            }

            String orgName = "";

            Vector perms = new Vector();

            String username = user.getScreenName();
            String rolename = "";
            String prefix = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:";

            if (isPhysician) {
                rolename = "medical doctor";

                String doctor_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_DOCTOR_PERMISSIONS);
                String p[] = doctor_perms.split(",");
                for (int k = 0; k < p.length; k++) {
                    perms.add(prefix + p[k]);
                }
            }
            if (isPharmacist) {
                rolename = "pharmacist";
                String pharm_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_PHARMACIST_PERMISSIONS);
                String p1[] = pharm_perms.split(",");
                for (int k = 0; k < p1.length; k++) {
                    perms.add(prefix + p1[k]);
                }
            }

            if (isNurse) {
                rolename = "nurse";
                String nurse_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_NURSE_PERMISSIONS);
                String p1[] = nurse_perms.split(",");
                for (int k = 0; k < p1.length; k++) {
                    perms.add(prefix + p1[k]);
                }
            }

            if (isPatient) {
                rolename = "patient";
                String patient_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_PATIENT_PERMISSIONS);
                String p1[] = patient_perms.split(",");
                for (int k = 0; k < p1.length; k++) {
                    perms.add(prefix + p1[k]);
                }
            }

            if (isAdministrator) {
                rolename = "administrator";
                String admin_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_ADMIN_PERMISSIONS);
                String p1[] = admin_perms.split(",");
                for (int k = 0; k < p1.length; k++) {
                    perms.add(prefix + p1[k]);
                }
            }
            Company company = CompanyLocalServiceUtil.getCompany(user
                    .getCompanyId());
            orgName = company.getName();
            String poc = "POC";
            // fixed for consent creation AuthorInstitution Validation problem
            String orgId = company.getCompanyId() + ".1";
            List depts = user.getOrganizations();
            String orgType = "Other";
            if (isPharmacist) {
                orgType = "Pharmacy";
            } else {
                if (isPhysician && depts.isEmpty()) {
                    orgType = "Resident Physician";
                } else {
                    orgType = "Hospital";
                }
            }
            assertion = EpsosHelperService.createAssertion(username, rolename,
                    orgName, orgId, orgType, "TREATMENT", poc, perms);

            // send Audit message
            // GUI-27
            if (Validator.isNotNull(assertion)) {
                log.info("AUDIT URL: "
                        + ConfigurationManagerService.getInstance()
                        .getProperty("audit.repository.url"));
                log.debug("Sending epsos-91 audit message for "
                        + user.getFullName());
                EpsosHelperService.sendAuditEpsos91(user.getFullName(),
                        user.getEmailAddress(), orgName, orgType, rolename,
                        assertion.getID());
            }
            // GUI-25
            if (isPhysician || isPharmacist || isNurse || isAdministrator
                    || isPatient) {

                String KEY_ALIAS = Constants.NCP_SIG_PRIVATEKEY_ALIAS;
                log.info("KEY ALIAS: " + KEY_ALIAS);
                String PRIVATE_KEY_PASS = Constants.NCP_SIG_PRIVATEKEY_PASSWORD;

                signSAMLAssertion(assertion, KEY_ALIAS);
                AssertionMarshaller marshaller = new AssertionMarshaller();
                Element element = null;

                element = marshaller.marshall(assertion);

                Document document = element.getOwnerDocument();

                String hcpa = Utils.getDocumentAsXml(document, false);
                log.info("#### HCPA Start");
                log.info(hcpa);
                log.info("#### HCPA End");

            }

            log.info("Assertion: " + assertion.getID());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return e.getMessage();
        }

        LiferayUtils.storeToSession("hcpAssertion", assertion);

        return assertion;
    }

    public static Object getUserAssertion(String screenname, String fullname,
                                          String emailaddress, String role) {

        log.info("Screenname is: " + screenname + " and role is " + role);
        Assertion assertion = null;

        try {
            boolean isPhysician = role
                    .equalsIgnoreCase(LiferayUtils.LPDoctorRole);
            boolean isPharmacist = role
                    .equalsIgnoreCase(LiferayUtils.LPPharmacistRole);
            boolean isNurse = role.equalsIgnoreCase(LiferayUtils.LPNurseRole);
            boolean isAdministrator = role
                    .equalsIgnoreCase(LiferayUtils.LPAdministratorRole);
            boolean isPatient = role
                    .equalsIgnoreCase(LiferayUtils.LPPatientRole);

            if (isPhysician || isPharmacist || isNurse || isAdministrator
                    || isPatient) {
                log.info("The portal role is one of the expected. Continuing ...");
            } else {
                log.error("The portal role is NOT one of the expected. Break");
                return "Error creating assertion for user. Role not compatible with EPSOS";
            }

            String orgName = "";

            Vector perms = new Vector();

            String username = screenname;
            String rolename = "";
            String prefix = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:";

            if (isPhysician) {
                rolename = "medical doctor";

                String doctor_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_DOCTOR_PERMISSIONS);
                String p[] = doctor_perms.split(",");
                for (int k = 0; k < p.length; k++) {
                    perms.add(prefix + p[k]);
                }
            }
            if (isPharmacist) {
                rolename = "pharmacist";
                String pharm_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_PHARMACIST_PERMISSIONS);
                String p1[] = pharm_perms.split(",");
                for (int k = 0; k < p1.length; k++) {
                    perms.add(prefix + p1[k]);
                }
            }

            if (isNurse) {
                rolename = "nurse";
                String nurse_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_NURSE_PERMISSIONS);
                String p1[] = nurse_perms.split(",");
                for (int k = 0; k < p1.length; k++) {
                    perms.add(prefix + p1[k]);
                }
            }

            if (isPatient) {
                rolename = "patient";
                String patient_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_PATIENT_PERMISSIONS);
                String p1[] = patient_perms.split(",");
                for (int k = 0; k < p1.length; k++) {
                    perms.add(prefix + p1[k]);
                }
            }

            if (isAdministrator) {
                rolename = "administrator";
                String admin_perms = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_ADMIN_PERMISSIONS);
                String p1[] = admin_perms.split(",");
                for (int k = 0; k < p1.length; k++) {
                    perms.add(prefix + p1[k]);
                }
            }
            orgName = "eHealthPass";
            String poc = "POC";
            // fixed for consent creation AuthorInstitution Validation problem
            String orgId = "57111.1";
            String orgType = "Other";
            if (isPharmacist) {
                orgType = "Pharmacy";
            }
            if (isPhysician) {
                orgType = "Resident Physician";
            }
            assertion = EpsosHelperService.createAssertion(username, rolename,
                    orgName, orgId, orgType, "TREATMENT", poc, perms);

            // send Audit message
            // GUI-27
            if (Validator.isNotNull(assertion)) {
                log.info("AUDIT URL: "
                        + ConfigurationManagerService.getInstance()
                        .getProperty("audit.repository.url"));
                log.debug("Sending epsos-91 audit message for " + fullname);
                EpsosHelperService.sendAuditEpsos91(fullname, emailaddress,
                        orgName, orgType, rolename, assertion.getID());
            }
            // GUI-25
            if (isPhysician || isPharmacist || isNurse || isAdministrator
                    || isPatient) {

                String KEY_ALIAS = Constants.NCP_SIG_PRIVATEKEY_ALIAS;
                log.info("KEY ALIAS: " + KEY_ALIAS);
                String PRIVATE_KEY_PASS = Constants.NCP_SIG_PRIVATEKEY_PASSWORD;

                signSAMLAssertion(assertion, KEY_ALIAS);
                AssertionMarshaller marshaller = new AssertionMarshaller();
                Element element = null;

                element = marshaller.marshall(assertion);

                Document document = element.getOwnerDocument();

                String hcpa = Utils.getDocumentAsXml(document, false);
                log.debug("#### HCPA Start");
                log.debug(hcpa);
                log.debug("#### HCPA End");

            }

            log.info("Assertion: " + assertion.getID());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return e.getMessage();
        }

        LiferayUtils.storeToSession("hcpAssertion", assertion);

        return assertion;
    }

    /**
     * @param EI_EventActionCode           Possible values according to D4.5.6 are E,R,U,D
     * @param EI_EventDateTime             The datetime the event occured
     * @param EI_EventOutcomeIndicator     <br>
     *                                     0 for full success <br>
     *                                     1 in case of partial delivery <br>
     *                                     4 for temporal failures <br>
     *                                     8 for permanent failure <br>
     * @param PC_UserID                    Point of Care: Oid of the department
     * @param PC_RoleID                    Role of the department
     * @param HR_UserID                    Identifier of the HCP initiated the event
     * @param HR_RoleID                    Role of the HCP initiated the event
     * @param HR_AlternativeUserID         Human readable name of the HCP as given in
     *                                     the Subject-ID
     * @param SC_UserID                    The string encoded CN of the TLS certificate of the NCP
     *                                     triggered the epsos operation
     * @param SP_UserID                    The string encoded CN of the TLS certificate of the NCP
     *                                     processed the epsos operation
     * @param AS_AuditSourceId             the iso3166-2 code of the country responsible for
     *                                     the audit source
     * @param ET_ObjectID                  The string encoded UUID of the returned document
     * @param ReqM_ParticipantObjectID     String-encoded UUID of the request
     *                                     message
     * @param ReqM_PatricipantObjectDetail The value MUST contain the base64
     *                                     encoded security header.
     * @param ResM_ParticipantObjectID     String-encoded UUID of the response
     *                                     message
     * @param ResM_PatricipantObjectDetail The value MUST contain the base64
     *                                     encoded security header.
     * @param sourceip                     The IP Address of the source Gateway
     * @param targetip                     The IP Address of the target Gateway
     */
    public static void sendAuditEpsos91(String fullname, String email,
                                        String orgName, String orgType, String rolename, String message) {

        String KEY_ALIAS = Constants.NCP_SIG_PRIVATEKEY_ALIAS;
        String KEYSTORE_LOCATION = Constants.NCP_SIG_KEYSTORE_PATH;
        String KEY_STORE_PASS = Constants.NCP_SIG_KEYSTORE_PASSWORD;
        log.info("KEY_ALIAS: " + Constants.NCP_SIG_PRIVATEKEY_ALIAS);
        if (Validator.isNull(KEY_ALIAS)) {
            log.error("Problem reading configuration parameters");
            return;
        }
        java.security.cert.Certificate cert = null;
        String name = "";
        try {
            // Load the keystore in the user's home directory
            FileInputStream is = new FileInputStream(KEYSTORE_LOCATION);
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(is, KEY_STORE_PASS.toCharArray());
            log.info("Keystore loaded ...");
            // Get certificate
            cert = keystore.getCertificate(KEY_ALIAS);
            log.info("Certificate loaded ..." + cert.getPublicKey().toString());
            // List the aliases
            Enumeration enum1 = keystore.aliases();
            for (; enum1.hasMoreElements(); ) {
                String alias = (String) enum1.nextElement();
                log.info("ALIAS IS " + alias);
                if (cert instanceof X509Certificate) {
                    X509Certificate x509cert = (X509Certificate) cert;

                    // Get subject
                    Principal principal = x509cert.getSubjectDN();
                    String subjectDn = principal.getName();
                    name = subjectDn;
                    // Get issuer
                    principal = x509cert.getIssuerDN();
                }
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        log.info("##########");
        String secHead = "No security header provided";
        String basedSecHead = Base64.encodeBase64String(secHead.getBytes());
        String reqm_participantObjectID = "urn:uuid:00000000-0000-0000-0000-000000000000";
        String resm_participantObjectID = "urn:uuid:00000000-0000-0000-0000-000000000000";

        InetAddress sourceIP = null;
        try {
            sourceIP = InetAddress.getLocalHost();
            log.info("Source IP: " + sourceIP);
        } catch (UnknownHostException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }

        String PC_UserID = orgName + "<saml:" + email + ">";
        String PC_RoleID = orgType;
        String HR_UserID = fullname + "<saml:" + email + ">";
        String HR_RoleID = rolename;
        String HR_AlternativeUserID = "";
        String SC_UserID = name;
        String SP_UserID = name;

        String AS_AuditSourceId = Constants.COUNTRY_PRINCIPAL_SUBDIVISION;
        String ET_ObjectID = "urn:uuid:" + message;
        byte[] ResM_PatricipantObjectDetail = new byte[1];

        AuditService asd = new AuditService();
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar date2 = null;
        try {
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }
        EventLog eventLog1 = EventLog.createEventLogHCPIdentity(
                TransactionName.epsosHcpAuthentication,
                EventActionCode.EXECUTE, date2,
                EventOutcomeIndicator.FULL_SUCCESS, PC_UserID, PC_RoleID,
                HR_UserID, HR_RoleID, HR_AlternativeUserID, SC_UserID,
                SP_UserID, AS_AuditSourceId, ET_ObjectID,
                reqm_participantObjectID, basedSecHead.getBytes(),
                resm_participantObjectID, ResM_PatricipantObjectDetail,
                sourceIP.getHostAddress(), "N/A");
        log.info("The audit has been prepared");
        eventLog1.setEventType(EventType.epsosHcpAuthentication);
        asd.write(eventLog1, "13", "2");
    }

    private static Attribute createAttribute(
            XMLObjectBuilderFactory builderFactory, String FriendlyName,
            String oasisName) {
        Attribute attrPID = create(Attribute.class,
                Attribute.DEFAULT_ELEMENT_NAME);
        attrPID.setFriendlyName(FriendlyName);
        attrPID.setName(oasisName);
        attrPID.setNameFormat(Attribute.URI_REFERENCE);
        return attrPID;
    }

    private static Attribute AddAttributeValue(
            XMLObjectBuilderFactory builderFactory, Attribute attribute,
            String value, String namespace, String xmlschema) {
        XMLObjectBuilder stringBuilder = builderFactory
                .getBuilder(XSString.TYPE_NAME);
        XSString attrValPID = (XSString) stringBuilder.buildObject(
                AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
        attrValPID.setValue(value);
        attribute.getAttributeValues().add(attrValPID);
        return attribute;
    }

    private static Attribute createAttribute(
            XMLObjectBuilderFactory builderFactory, String FriendlyName,
            String oasisName, String value, String namespace, String xmlschema) {
        Attribute attrPID = create(Attribute.class,
                Attribute.DEFAULT_ELEMENT_NAME);
        attrPID.setFriendlyName(FriendlyName);
        attrPID.setName(oasisName);
        attrPID.setNameFormat(Attribute.URI_REFERENCE);
        // Create and add the Attribute Value

        XMLObjectBuilder stringBuilder = null;

        if (namespace.equals("")) {
            XSString attrValPID = null;
            stringBuilder = builderFactory.getBuilder(XSString.TYPE_NAME);
            attrValPID = (XSString) stringBuilder.buildObject(
                    AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
            attrValPID.setValue(value);
            attrPID.getAttributeValues().add(attrValPID);
        } else {
            XSURI attrValPID = null;
            stringBuilder = builderFactory.getBuilder(XSURI.TYPE_NAME);
            attrValPID = (XSURI) stringBuilder.buildObject(
                    AttributeValue.DEFAULT_ELEMENT_NAME, XSURI.TYPE_NAME);
            attrValPID.setValue(value);
            attrPID.getAttributeValues().add(attrValPID);
        }

        return attrPID;
    }

    private static <T> T create(Class<T> cls, QName qname) {
        return (T) ((XMLObjectBuilder) Configuration.getBuilderFactory()
                .getBuilder(qname)).buildObject(qname);
    }

    private static Assertion createAssertion(String username, String role,
                                             String organization, String organizationId, String facilityType,
                                             String purposeOfUse, String xspaLocality,
                                             java.util.Vector permissions) {

        return createStorkAssertion(username, role, organization,
                organizationId, facilityType, purposeOfUse, xspaLocality,
                permissions, null);
    }

    private static Assertion createStorkAssertion(String username, String role,
                                                  String organization, String organizationId, String facilityType,
                                                  String purposeOfUse, String xspaLocality,
                                                  java.util.Vector permissions, String onBehalfId) {
        // assertion
        log.info("username:" + username);
        log.info("role:" + role);
        log.info("organization:" + organization);
        log.info("organizationId:" + organizationId);
        log.info("facilityType:" + facilityType);
        log.info("purposeOfUse:" + purposeOfUse);
        log.info("xspaLocality:" + xspaLocality);

        Assertion assertion = null;
        try {
            DefaultBootstrap.bootstrap();
            XMLObjectBuilderFactory builderFactory = Configuration
                    .getBuilderFactory();

            // Create the NameIdentifier
            SAMLObjectBuilder nameIdBuilder = (SAMLObjectBuilder) builderFactory
                    .getBuilder(NameID.DEFAULT_ELEMENT_NAME);
            NameID nameId = (NameID) nameIdBuilder.buildObject();
            nameId.setValue(username);
            // nameId.setNameQualifier(strNameQualifier);
            nameId.setFormat(NameID.UNSPECIFIED);

            assertion = create(Assertion.class, Assertion.DEFAULT_ELEMENT_NAME);

            String assId = "_" + UUID.randomUUID().toString();
            assertion.setID(assId);
            assertion.setVersion(SAMLVersion.VERSION_20);
            assertion.setIssueInstant(new org.joda.time.DateTime()
                    .minusHours(3));

            Subject subject = create(Subject.class,
                    Subject.DEFAULT_ELEMENT_NAME);
            assertion.setSubject(subject);
            subject.setNameID(nameId);

            // Create and add Subject Confirmation
            SubjectConfirmation subjectConf = create(SubjectConfirmation.class,
                    SubjectConfirmation.DEFAULT_ELEMENT_NAME);
            subjectConf.setMethod(SubjectConfirmation.METHOD_SENDER_VOUCHES);
            assertion.getSubject().getSubjectConfirmations().add(subjectConf);

            // Create and add conditions
            Conditions conditions = create(Conditions.class,
                    Conditions.DEFAULT_ELEMENT_NAME);
            org.joda.time.DateTime now = new org.joda.time.DateTime();
            conditions.setNotBefore(now.minusMinutes(1));
            conditions.setNotOnOrAfter(now.plusHours(2)); // According to Spec
            assertion.setConditions(conditions);

            Issuer issuer = new IssuerBuilder().buildObject();
            issuer.setValue("urn:idp:countryB");
            issuer.setNameQualifier("urn:epsos:wp34:assertions");
            assertion.setIssuer(issuer);

            // Add and create the authentication statement
            AuthnStatement authStmt = create(AuthnStatement.class,
                    AuthnStatement.DEFAULT_ELEMENT_NAME);
            authStmt.setAuthnInstant(now.minusHours(2));
            assertion.getAuthnStatements().add(authStmt);

            // Create and add AuthnContext
            AuthnContext ac = create(AuthnContext.class,
                    AuthnContext.DEFAULT_ELEMENT_NAME);
            AuthnContextClassRef accr = create(AuthnContextClassRef.class,
                    AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
            accr.setAuthnContextClassRef(AuthnContext.PASSWORD_AUTHN_CTX);
            ac.setAuthnContextClassRef(accr);
            authStmt.setAuthnContext(ac);

            AttributeStatement attrStmt = create(AttributeStatement.class,
                    AttributeStatement.DEFAULT_ELEMENT_NAME);

            // XSPA Subject
            Attribute attrPID = createAttribute(builderFactory, "XSPA subject",
                    "urn:oasis:names:tc:xacml:1.0:subject:subject-id",
                    username, "", "");
            attrStmt.getAttributes().add(attrPID);
            // XSPA Role
            Attribute attrPID_1 = createAttribute(builderFactory, "XSPA role",
                    "urn:oasis:names:tc:xacml:2.0:subject:role", role, "", "");
            attrStmt.getAttributes().add(attrPID_1);
            // HITSP Clinical Speciality
            /*
             * Attribute attrPID_2 =
             * createAttribute(builderFactory,"HITSP Clinical Speciality",
             * "urn:epsos:names:wp3.4:subject:clinical-speciality",role,"","");
             * attrStmt.getAttributes().add(attrPID_2);
             */
            // XSPA Organization
            Attribute attrPID_3 = createAttribute(builderFactory,
                    "XSPA Organization",
                    "urn:oasis:names:tc:xspa:1.0:subject:organization",
                    organization, "", "");
            attrStmt.getAttributes().add(attrPID_3);
            // XSPA Organization ID
            Attribute attrPID_4 = createAttribute(builderFactory,
                    "XSPA Organization ID",
                    "urn:oasis:names:tc:xspa:1.0:subject:organization-id",
                    organizationId, "AA", "");
            attrStmt.getAttributes().add(attrPID_4);

            // // On behalf of
            if (Validator.isNotNull(onBehalfId)) {
                Attribute attrPID_41 = createAttribute(builderFactory,
                        "OnBehalfOf",
                        "urn:epsos:names:wp3.4:subject:on-behalf-of",
                        onBehalfId, role, "");
                attrStmt.getAttributes().add(attrPID_41);
                attrStmt.getAttributes().add(attrPID_41);
            }

            // epSOS Healthcare Facility Type
            Attribute attrPID_5 = createAttribute(builderFactory,
                    "epSOS Healthcare Facility Type",
                    "urn:epsos:names:wp3.4:subject:healthcare-facility-type",
                    facilityType, "", "");
            attrStmt.getAttributes().add(attrPID_5);
            // XSPA Purpose of Use
            Attribute attrPID_6 = createAttribute(builderFactory,
                    "XSPA Purpose Of Use",
                    "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse",
                    purposeOfUse, "", "");
            attrStmt.getAttributes().add(attrPID_6);
            // XSPA Locality
            Attribute attrPID_7 = createAttribute(builderFactory,
                    "XSPA Locality",
                    "urn:oasis:names:tc:xspa:1.0:environment:locality",
                    xspaLocality, "", "");
            attrStmt.getAttributes().add(attrPID_7);
            // HL7 Permissions
            Attribute attrPID_8 = createAttribute(builderFactory,
                    "Hl7 Permissions",
                    "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission");
            Iterator itr = permissions.iterator();
            while (itr.hasNext()) {
                attrPID_8 = AddAttributeValue(builderFactory, attrPID_8, itr
                        .next().toString(), "", "");
            }
            attrStmt.getAttributes().add(attrPID_8);

            assertion.getStatements().add(attrStmt);

        } catch (ConfigurationException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        log.info("AssertionId " + assertion.getID());
        return assertion;
    }

    public static String getCountriesFromCS() {
        log.debug("get Countries from CS");
        String listOfCountries = "";
        String filename = "InternationalSearch.xml";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            String wi = Constants.EPSOS_PROPS_PATH;

            String path = wi + "forms" + File.separator + filename;
            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("country");
            String seperator = "";
            for (int s = 0; s < nodeLst.getLength(); s++) {
                // if (s>0) {seperator=",";}
                if (listOfCountries.length() > 1) {
                    seperator = ",";
                }
                Element link = (Element) nodeLst.item(s);
                String a1 = link.getAttribute("code");
                if (Validator.isNotNull(getCountryIdsFromCS(a1).get(0))) {
                    listOfCountries = listOfCountries + seperator + a1;
                }
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return listOfCountries;
    }

    public static List<Country> getCountriesFromCS(String lang,
                                                   String portalPath) {
        log.info("get Countries from CS with lang " + lang);
        List<Country> listOfCountries = new ArrayList<Country>();
        String filename = "InternationalSearch.xml";
        try {
            String wi = Constants.EPSOS_PROPS_PATH;
            String path = wi + "forms" + File.separator + filename;

            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("country");
            for (int s = 0; s < nodeLst.getLength(); s++) {
                Element link = (Element) nodeLst.item(s);
                String countryCode = link.getAttribute("code");
                String countryName = EpsosHelperService.getCountryName(
                        countryCode, lang);
                log.debug("Lang is " + lang + " and Country code: "
                        + countryCode + " name is " + countryName);
                Country country = new Country(countryName, countryCode);
                listOfCountries.add(country);
            }
            getCountryListNameFromCS(lang, listOfCountries);

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            log.error("getCountriesFromCS: " + e.getMessage());
        }
        return listOfCountries;
    }

    public static List<Country> getCountriesFromCS(String lang) {
        log.info("get Countries from CS with lang " + lang);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String wi = externalContext.getRealPath("/");
        log.info("Countries definition path is " + wi);
        return getCountriesFromCS(lang, wi);
    }

    public static String getCountryName(String countryCode, String lang) {
        String translation = countryCode;
        translation = LiferayUtils.getPortalTranslation(countryCode, lang);
        return translation;
    }

    public static void getCountryListNameFromCS(String lang,
                                                List<Country> countriesList) {

        try {
            for (int i = 0; i < countriesList.size(); i++) {
                Country country = countriesList.get(i);
                String translation = country.getCode();
                translation = LiferayUtils.getPortalTranslation(
                        country.getCode(), lang);
                country.setName(translation);
                log.info("Country is : " + country.getName());
            }
        } catch (Exception ex) {
            log.error("getCountriesNamesFromCS: " + ex.getMessage());
            log.error(ExceptionUtils.getStackTrace(ex));
        }

    }

    public static String getCountriesLabelsFromCS(String language) {
        log.debug("get Countries labels from CS");
        String listOfCountries = "";

        String filename = "InternationalSearch.xml";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            String wi = Constants.EPSOS_PROPS_PATH;
            // String path = cl.getResource(".").getPath();
            // log.info("#### 1: " + a1);
            String path = wi + "forms" + File.separator + filename;
            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("country");
            String seperator = "";
            for (int s = 0; s < nodeLst.getLength(); s++) {
                if (listOfCountries.length() > 1) {
                    seperator = ",";
                }
                Element link = (Element) nodeLst.item(s);
                String a1 = EpsosHelperService.getPortalTranslation(
                        link.getAttribute("code"), language);
                Vector v = getCountryIdsFromCS(link.getAttribute("code"));
                SearchMask sm = (SearchMask) v.get(0);
                if (sm.getDomain() != null) {
                    listOfCountries = listOfCountries + seperator + a1;
                }

            }

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return listOfCountries;
    }

    public static Vector getCountryIdsFromCS(String country, String portalPath) {
        String path = "";
        Vector v = new Vector();
        String filename = "InternationalSearch_" + country + ".xml";
        path = getSearchMaskPath() + "forms" + File.separator + filename;
        log.info("#### Path is :" + path);
        try {
            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("id");
            for (int s = 0; s < nodeLst.getLength(); s++) {
                Element link = (Element) nodeLst.item(s);
                SearchMask sm = new SearchMask();
                sm.setDomain(link.getAttribute("domain"));
                sm.setLabel(link.getAttribute("label"));
                sm.setFriendlyName(link.getAttribute("friendlyName"));
                v.add(sm);
            }
        } catch (Exception e) {
            log.error("Error getting country ids " + e.getMessage());
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return v;
    }

    public static List<Identifier> getCountryIdentifiers(String country,
                                                         String language, String path, User user) {
        List<Identifier> identifiers = new ArrayList<Identifier>();

        Vector vec = EpsosHelperService.getCountryIdsFromCS(country, path);
        for (int i = 0; i < vec.size(); i++) {
            Identifier id = new Identifier();
            id.setKey(EpsosHelperService.getPortalTranslation(
                    ((SearchMask) vec.get(i)).getLabel(), language)
                    + "*");
            id.setDomain(((SearchMask) vec.get(i)).getDomain());
            if (id.getKey().equals("") || id.getKey().equals("*")) {
                id.setKey(((SearchMask) vec.get(i)).getLabel() + "*");
            }

            id.setFriendlyName(((SearchMask) vec.get(i)).getFriendlyName());

            if (Validator.isNotNull(user)) {
                String idvalue = (String) user.getExpandoBridge().getAttribute(
                        id.getDomain());
                log.info("Identifiers: " + id.getKey() + "_" + idvalue);
                id.setUserValue(idvalue);
            }

            identifiers.add(id);
        }
        return identifiers;
    }

    public static List<Demographics> getCountryDemographics(String country,
                                                            String language, String path, User user) {
        List<Demographics> demographics = new ArrayList<Demographics>();
        Vector vec = EpsosHelperService.getCountryDemographicsFromCS(country,
                path);
        for (int i = 0; i < vec.size(); i++) {
            Demographics id = new Demographics();
            if (((Demographics) vec.get(i)).getMandatory()) {
                id.setLabel(EpsosHelperService.getPortalTranslation(
                        ((Demographics) vec.get(i)).getLabel(), language) + "*");
            } else {
                id.setLabel(EpsosHelperService.getPortalTranslation(
                        ((Demographics) vec.get(i)).getLabel(), language));
            }
            id.setLength(((Demographics) vec.get(i)).getLength());
            id.setKey(((Demographics) vec.get(i)).getKey());
            id.setMandatory(((Demographics) vec.get(i)).getMandatory());
            id.setType(((Demographics) vec.get(i)).getType());
            id.setFriendlyName(((Demographics) vec.get(i)).getFriendlyName());

            if (Validator.isNotNull(user)) {
                String idvalue = (String) user.getExpandoBridge().getAttribute(
                        id.getKey());
                id.setUserValue(idvalue);
            }

            demographics.add(id);
        }
        return demographics;
    }

    public static Vector getCountryIdsFromCS(String country) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        return getCountryIdsFromCS(country, externalContext.getRealPath("/"));
    }

    public static String getSearchMaskPath() {
        return Constants.EPSOS_PROPS_PATH;
    }

    public static Vector getCountryDemographicsFromCS(String country,
                                                      String portalPath) {
        Vector v = new Vector();
        String filename = "InternationalSearch_" + country + ".xml";

        String path = getSearchMaskPath() + "forms" + File.separator + filename;
        try {
            File file = new File(path);
            // File file = new File(internationalSearchPath+filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            // textFields
            NodeList nodeLst = doc.getElementsByTagName("textField");
            for (int s = 0; s < nodeLst.getLength(); s++) {
                Element link = (Element) nodeLst.item(s);
                Demographics dem = new Demographics();
                dem.setLabel(link.getAttribute("label"));
                dem.setKey(link.getAttribute("label"));
                dem.setType("text");
                dem.setLength(Integer.parseInt(link.getAttribute("min")));
                dem.setFriendlyName(link.getAttribute("friendlyName"));

                // search for mandatory items
                NodeList nodeLst2 = doc.getElementsByTagName("field");
                for (int i = 0; i < nodeLst2.getLength(); i++) {
                    Element link2 = (Element) nodeLst2.item(i);
                    if (link2.getAttribute("label").equals(dem.getLabel())) {
                        dem.setMandatory(true);
                        break;
                    }
                }
                v.add(dem);
            }
            // sex
            nodeLst = doc.getElementsByTagName("sex");
            for (int s = 0; s < nodeLst.getLength(); s++) {
                Element link = (Element) nodeLst.item(s);
                Demographics dem = new Demographics();
                dem.setLabel(link.getAttribute("label"));
                dem.setKey(link.getAttribute("label"));
                dem.setType("text");
                v.add(dem);
            }
            // birth date
            nodeLst = doc.getElementsByTagName("birthDate");
            for (int s = 0; s < nodeLst.getLength(); s++) {
                Element link = (Element) nodeLst.item(s);
                Demographics dem = new Demographics();
                dem.setLabel(link.getAttribute("label"));
                dem.setKey(link.getAttribute("label"));
                dem.setType("calendar");
                v.add(dem);
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        log.info("Demographics size :" + v.size());
        return v;
    }

    public static Vector getCountryDemographicsFromCS(String country) {
        Vector v = new Vector();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        return getCountryDemographicsFromCS(country,
                externalContext.getRealPath("/"));
    }

    public static String getPortalTranslation(String key, String language) {
        return LiferayUtils.getPortalTranslation(key, language);
    }

    public static String getPortalTranslationFromServlet(
            HttpServletRequest req, String key, String language) {
        return LiferayUtils.getPortalTranslation(key, language);
    }

    public static void printAssertion(Assertion ass)
            throws MarshallingException {
        AssertionMarshaller marshaller = new AssertionMarshaller();
        Element element = null;
        element = marshaller.marshall(ass);
        Document document = element.getOwnerDocument();

        String asstring = Utils.getDocumentAsXml(document, false);

        log.info("##################### ASSERTION Start");
        log.info(asstring);
        log.info("##################### ASSERTION End");
    }

    public static Assertion createPatientConfirmationPlain(String purpose,
                                                           Assertion idAs, PatientId patient) throws Exception {
        Assertion trc = null;
        log.debug("Try to create TRCA for patient : " + patient.getExtension());
        String pat = "";
        pat = patient.getExtension() + "^^^&" + patient.getRoot() + "&ISO";
        log.info("TRCA Patient ID : " + pat);
        log.info("Assertion ID :" + idAs.getID());
        log.info("SECMAN URL: "
                + ConfigurationManagerService.getInstance().getProperty(
                "secman.sts.url"));
        TRCAssertionRequest req1 = new TRCAssertionRequest.Builder(idAs, pat)
                .PurposeOfUse(purpose).build();
        trc = req1.request();

        AssertionMarshaller marshaller = new AssertionMarshaller();
        Element element = null;
        element = marshaller.marshall(trc);
        Document document = element.getOwnerDocument();

        String trca = Utils.getDocumentAsXml(document, false);

        log.info("#### TRCA Start");
        log.info(trca);
        log.info("#### TRCA End");

        log.debug("TRCA CREATED: " + trc.getID());
        log.debug("TRCA WILL BE STORED TO SESSION: " + trc.getID());
        LiferayUtils.storeToSession("trcAssertion", trc);
        return trc;
    }

    public static String extractPdfPartOfDocument(String cda) {

        String result = cda;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(new ByteArrayInputStream(cda.getBytes()));

            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new CDANameSpaceContext());

            XPathExpression pdfTag = xpath
                    .compile("//xsi:component/xsi:nonXMLBody/xsi:text[@mediaType='application/pdf']");
            Node pdfNode = (Node) pdfTag.evaluate(dom, XPathConstants.NODE);
            if (pdfNode != null) {
                String base64EncodedPdfString = pdfNode.getTextContent().trim();
                log.info("##### base64EncodedPdfString: "
                        + base64EncodedPdfString);
                result = base64EncodedPdfString;//decode.decode(base64EncodedPdfString);
                result = "data:application/pdf;base64," + result;
            } else {
                pdfTag = xpath
                        .compile("//component/nonXMLBody/text[@mediaType='application/pdf']");
                pdfNode = (Node) pdfTag.evaluate(dom, XPathConstants.NODE);
                if (pdfNode != null) {
                    String base64EncodedPdfString = pdfNode.getTextContent()
                            .trim();
                    log.info("##### base64EncodedPdfString: "
                            + base64EncodedPdfString);
                    result = base64EncodedPdfString;//decode.decode(base64EncodedPdfString.getBytes());
                    result = "data:application/pdf;base64," + result;
                }

            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public static byte[] extractPdfPartOfDocument(byte[] bytes) {

        byte[] result = bytes;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(new ByteArrayInputStream(bytes));

            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new CDANameSpaceContext());

            XPathExpression pdfTag = xpath
                    .compile("//xsi:component/xsi:nonXMLBody/xsi:text[@mediaType='application/pdf']");
            Node pdfNode = (Node) pdfTag.evaluate(dom, XPathConstants.NODE);
            if (pdfNode != null) {
                String base64EncodedPdfString = pdfNode.getTextContent().trim();
                log.info("##### base64EncodedPdfString: "
                        + base64EncodedPdfString);
                result = decode.decode(base64EncodedPdfString.getBytes());
            } else {
                pdfTag = xpath
                        .compile("//component/nonXMLBody/text[@mediaType='application/pdf']");
                pdfNode = (Node) pdfTag.evaluate(dom, XPathConstants.NODE);
                if (pdfNode != null) {
                    String base64EncodedPdfString = pdfNode.getTextContent()
                            .trim();
                    log.info("##### base64EncodedPdfString: "
                            + base64EncodedPdfString);
                    result = decode.decode(base64EncodedPdfString.getBytes());
                }

            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return result;
    }

    public static String getSafeString(String arg0) {
        String result = "";
        try {
            if (Validator.isNull(arg0)) {
                log.error("Error getting safe string. USING N/A");
                result = "N/A";
            } else {
                result = arg0;
            }
        } catch (Exception e) {
            log.error("Error getting safe string");
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public static void changeNode(Document dom, XPath xpath, String path,
                                  String nodeName, String value) {
        try {
            XPathExpression salRO = xpath.compile(path + "/" + nodeName);
            NodeList salRONodes = (NodeList) salRO.evaluate(dom,
                    XPathConstants.NODESET);

            if (salRONodes.getLength() > 0) {
                for (int t = 0; t < salRONodes.getLength(); t++) {
                    Node AddrNode = salRONodes.item(t);

                    if (AddrNode.getNodeName().equals(nodeName)) {
                        AddrNode.setTextContent(value);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error fixing node ...");
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * Create an attribute node of the form attributeName="attributeValue" and
     * add it to node
     *
     * @param dom
     * @param node
     * @param attributeName
     * @param attributeValue
     */
    public static void addAttribute(Document dom, Node node,
                                    String attributeName, String attributeValue) {
        Attr rootAttr = dom.createAttribute(attributeName);
        rootAttr.setValue(attributeValue);
        node.getAttributes().setNamedItem(rootAttr);
    }

    public static String formatDateHL7(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }

    private static String dateDecorate(String input) {
        String result = input;
        if (input != null) {
            try {
                String year = input.substring(0, 4);
                String month = input.substring(4, 6);
                String day = input.substring(6);
                result = day + "/" + month + "/" + year;
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static String getUniqueId() {
        String uniqueId = "";
        ConfigurationManagerService cms = ConfigurationManagerService
                .getInstance();
        String pnoid = cms.getProperty("HOME_COMM_ID");
        String prop = "pn.uniqueid";
        String id = cms.getProperty(prop);
        int pid = 0;
        if (Validator.isNull(id)) {
            cms.updateProperty(prop, "1");
            uniqueId = pnoid + "." + "1";
        } else {
            pid = Integer.parseInt(cms.getProperty(prop));
            pid = pid + 1;
            uniqueId = pnoid + "." + pid;
            cms.updateProperty(prop, pid + "");
        }
        return uniqueId;
    }

    public static void initConfigWithPortletProperties() {

        String propertiesUpdated = "FALSE";
        propertiesUpdated = EpsosHelperService
                .getConfigProperty("PORTAL_PROPERTIES_UPDATED");
        if (!(propertiesUpdated.equals("TRUE"))) {
            String serviceUrl = PortletProps
                    .get("client.connector.service.url");
            String doctorPerms = PortletProps.get("medical.doctor.perms");
            String pharmPerms = PortletProps.get("pharmacist.perms");
            String nursePerms = PortletProps.get("nurse.perms");
            String adminPerms = PortletProps.get("administrator.perms");
            String patientPerms = PortletProps.get("patient.perms");
            String testAssertions = PortletProps.get("create.test.assertions");
            String checkPermissions = PortletProps
                    .get("check.permissions.for.buttons");

            String edCountry = PortletProps.get("ed.country");
            String laf = PortletProps.get("legal.authenticator.firstname");
            String lal = PortletProps.get("legal.authenticator.lastname");
            String lac = PortletProps.get("legal.authenticator.city");
            String lapc = PortletProps.get("legal.authenticator.postalcode");
            String lacn = PortletProps.get("custodian.name");
            String lacoid = PortletProps.get("consent.oid");
            String edoid = PortletProps.get("ed.oid");
            String patoid = PortletProps.get("patients.oid");
            String pharmoid = PortletProps.get("pharmacists.oid");
            String pharmaoid = PortletProps.get("pharmacies.oid");
            String doctorid = PortletProps.get("doctors.oid");
            String hospoid = PortletProps.get("hospitals.oid");
            String orderoid = PortletProps.get("order.oid");
            String entryoid = PortletProps.get("entry.oid");
            String custoid = PortletProps.get("custodian.oid");
            String lpoid = PortletProps.get("legalauth.person.oid");

            updatePortalProperty(PORTAL_CLIENT_CONNECTOR_URL, serviceUrl);
            updatePortalProperty(PORTAL_DOCTOR_PERMISSIONS, doctorPerms);
            updatePortalProperty(PORTAL_PHARMACIST_PERMISSIONS, pharmPerms);
            updatePortalProperty(PORTAL_NURSE_PERMISSIONS, nursePerms);
            updatePortalProperty(PORTAL_ADMIN_PERMISSIONS, adminPerms);
            updatePortalProperty(PORTAL_PATIENT_PERMISSIONS, patientPerms);
            updatePortalProperty(PORTAL_TEST_ASSERTIONS, testAssertions);
            updatePortalProperty(PORTAL_CHECK_PERMISSIONS, checkPermissions);

            updatePortalProperty(PORTAL_DISPENSATION_COUNTRY, edCountry);
            updatePortalProperty(PORTAL_LEGAL_AUTHENTICATOR_FIRSTNAME, laf);
            updatePortalProperty(PORTAL_LEGAL_AUTHENTICATOR_LASTNAME, lal);
            updatePortalProperty(PORTAL_LEGAL_AUTHENTICATOR_CITY, lac);
            updatePortalProperty(PORTAL_LEGAL_AUTHENTICATOR_POSTALCODE, lapc);
            updatePortalProperty(PORTAL_CUSTODIAN_NAME, lacn);
            updatePortalProperty(PORTAL_CONSENT_OID, lacoid);
            updatePortalProperty(PORTAL_DISPENSATION_OID, edoid);
            updatePortalProperty(PORTAL_PATIENTS_OID, patoid);
            updatePortalProperty(PORTAL_PHARMACIST_OID, pharmoid);
            updatePortalProperty(PORTAL_PHARMACIES_OID, pharmaoid);
            updatePortalProperty(PORTAL_DOCTOR_OID, doctorid);
            updatePortalProperty(PORTAL_HOSPITAL_OID, hospoid);
            updatePortalProperty(PORTAL_ORDER_OID, orderoid);
            updatePortalProperty(PORTAL_ENTRY_OID, entryoid);
            updatePortalProperty(PORTAL_CUSTODIAN_OID, custoid);
            updatePortalProperty(PORTAL_LEGAL_AUTHENTICATOR_PERSON_OID, lpoid);
            updatePortalProperty(PORTAL_LEGAL_AUTHENTICATOR_ORG_OID, lpoid);
            updatePortalProperty("PORTAL_PROPERTIES_UPDATED", "TRUE");
        }
    }

    private static void updatePortalProperty(String key, String value) {
        if (Validator.isNull(ConfigurationManagerService.getInstance()
                .getProperty(key))) {
            ConfigurationManagerService.getInstance()
                    .updateProperty(key, value);
        }
    }

    public static String getConfigProperty(String key) {
        return ConfigurationManagerService.getInstance().getProperty(key);
    }

    public static byte[] getConsentReport(String lang2, String fullname,
                                          Patient patient) {
        byte[] bytes = null;
        try {
            String language = "";
            String country = "";
            String langFromCountry = "";
            try {
                country = patient.getCountry();
                langFromCountry = LocaleUtils.languagesByCountry(country)
                        .get(0) + "";
            } catch (Exception e) {
                log.error("Error getting country from patient");
                log.error(ExceptionUtils.getStackTrace(e));
            }

            // String patientLang = patient.getLanguage();
            String patientLang = "";
            if (Validator.isNotNull(patientLang)) {
                language = patientLang;
            }
            if (Validator.isNull(language)) {
                language = langFromCountry;
            }
            if (Validator.isNull(language)) {
                language = "en_GB";
            }
            String language2 = lang2;
            log.debug("LANGUAGE=" + language + "-" + lang2);
            Map parameters = new HashMap();
            parameters.put("IS_IGNORE_PAGINATION", false);
            String birthDate = patient.getBirthDate();
            // Date newDate = xgc.toGregorianCalendar().getTime();
            parameters.put("givenname", patient.getFamilyName());
            parameters.put("givenname_label_lang1", LiferayUtils
                    .getPortalTranslation("patient.data.givenname", language));
            parameters.put("givenname_label_lang2", LiferayUtils
                    .getPortalTranslation("patient.data.givenname", language2));
            parameters.put("familyname", patient.getFamilyName());
            parameters.put("familyname_label_lang1", LiferayUtils
                    .getPortalTranslation("patient.data.surname", language));
            parameters.put("familyname_label_lang2", LiferayUtils
                    .getPortalTranslation("patient.data.surname", language2));
            parameters.put("streetaddress", patient.getAddress());
            parameters.put("streetaddress_label_lang1", LiferayUtils
                    .getPortalTranslation("patient.data.street.address",
                            language));
            parameters.put("streetaddress_label_lang2", LiferayUtils
                    .getPortalTranslation("patient.data.street.address",
                            language2));
            parameters.put("zipcode", patient.getPostalCode());
            parameters.put("zipcode_label_lang1", LiferayUtils
                    .getPortalTranslation("patient.data.code", language));
            parameters.put("zipcode_label_lang2", LiferayUtils
                    .getPortalTranslation("patient.data.code", language2));
            parameters.put("city", patient.getCity());
            parameters.put("city_label_lang1", LiferayUtils
                    .getPortalTranslation("patient.data.city", language));
            parameters.put("city_label_lang2", LiferayUtils
                    .getPortalTranslation("patient.data.city", language2));
            parameters.put("country", patient.getCountry());
            parameters.put("country_label_lang1", LiferayUtils
                    .getPortalTranslation("patient.data.country", language));
            parameters.put("country_label_lang2", LiferayUtils
                    .getPortalTranslation("patient.data.country", language2));
            parameters.put("birthdate", birthDate);
            parameters.put("birthdate_label_lang1", LiferayUtils
                    .getPortalTranslation("patient.data.birth.date", language));
            parameters
                    .put("birthdate_label_lang2", LiferayUtils
                            .getPortalTranslation("patient.data.birth.date",
                                    language2));
            String consentText = getConsentText("en_US");
            consentText = getConsentText(language);
            String consentText2 = getConsentText("en_US");
            consentText2 = getConsentText(language2);
            parameters.put("consent_text", consentText);
            parameters.put("consent_text_2", consentText2);
            parameters.put("printedby", fullname);
            parameters.put("lang1", language);
            parameters.put("lang2", language2);
            parameters.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl
                    .getResource("com/gnomon/epsos/reports/epsosConsent.jasper");
            String path = url.getPath();
            log.debug("PATH IS " + path);
            bytes = generatePdfReport(LiferayUtils.getCurrentConnection(),
                    path, parameters);
        } catch (Exception e) {
            log.error("Error creating pin document. " + e.getMessage());
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return bytes;
    }

    public static String getConsentText(String language) {
        String translation = "";
        language = language.replaceAll("_", "-");
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource("content/consent/Consent_LegalText_"
                    + language + ".xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setFeature(
                    "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(url.getFile());
            doc.getDocumentElement().normalize();
            XPath xpath = XPathFactory.newInstance().newXPath();

            String xpathExpression = "/Consent/LegalText";
            NodeList nodes = (NodeList) xpath.evaluate(xpathExpression, doc,
                    XPathConstants.NODESET);
            translation = nodes.item(0).getTextContent();
        } catch (Exception e) {
            log.error("Error getting consent text for country " + language);
        }
        return translation;
    }

    public static byte[] generatePdfReport(Connection conn,
                                           String jasperFilePath, Map parameters) throws JRException,
            SQLException {
        ByteArrayOutputStream baos = null;
        try {
            File reportFile = new File(jasperFilePath);
            JasperFillManager
                    .fillReport(reportFile.getPath(), parameters, conn);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    reportFile.getPath(), parameters, conn);
            JRPdfExporter exporter = new JRPdfExporter();
            baos = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
            exporter.exportReport();
        } finally {
            conn.close();
        }
        return baos.toByteArray();
    }

    public static List<Patient> getMockPatients() {
        List<Patient> mockpatients = new ArrayList();

        Patient patient = new Patient();
        patient.setName("Patient name");
        patient.setFamilyName("Patient family name");
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = LiferayUtils.getPortalUserDateFormat();
        patient.setBirthDate(sdf.format(cal.getTime()));
        patient.setCity("Thessaloniki");
        patient.setAddress("somewhrere");
        patient.setAdministrativeGender("F");
        patient.setCountry("Greece");
        patient.setEmail("test@email.com");
        patient.setPostalCode("56001");
        patient.setTelephone("21212121");
        patient.setRoot("root");
        patient.setExtension("extension");
        // patient.setPatientDemographics(aux);
        mockpatients.add(patient);
        return mockpatients;
    }

    public static List<PatientDocument> getMockPSDocuments() {
        List<PatientDocument> mockdocs = new ArrayList();

        String repositoryId = "repID";
        String hcID = "hcID";

        GenericDocumentCode formatCode = null;
        formatCode.setNodeRepresentation("");
        formatCode.setSchema("");
        formatCode.setValue("urn:epSOS:ps:ps:2010");

        PatientDocument document = new PatientDocument();
        document.setDescription("Patient Summary");
        document.setHealthcareFacility("");
        document.setTitle("ps title");
        // document.setFile(aux.getBase64Binary());
        document.setUuid(UUID.randomUUID().toString());
        document.setFormatCode(formatCode);
        document.setRepositoryId(repositoryId);
        document.setHcid(hcID);
        document.setDocType("ps");
        mockdocs.add(document);
        return mockdocs;
    }

    public static List<PatientDocument> getMockEPDocuments() {
        List<PatientDocument> mockdocs = new ArrayList();

        GenericDocumentCode formatCode = null;
        formatCode.setNodeRepresentation("");
        formatCode.setSchema("");
        formatCode.setValue("urn:epSOS:ep:pre:2010");

        String repositoryId = "repID";
        String hcID = "hcID";

        PatientDocument document = new PatientDocument();
        document.setDescription("Patient Summary");
        document.setHealthcareFacility("");
        document.setTitle("ps title");
        // document.setFile(aux.getBase64Binary());
        document.setUuid(UUID.randomUUID().toString());
        document.setFormatCode(formatCode);
        document.setRepositoryId(repositoryId);
        document.setHcid(hcID);
        document.setDocType("ps");
        mockdocs.add(document);
        return mockdocs;
    }

    public static Document translateDoc(Document doc, String lang) {
        ITransformationService tService = MyServletContextListener
                .getTransformationService();
        if (Validator.isNotNull(tService)) {
            log.info("The Transformation Service started correctly. Translating to "
                    + lang);
            TMResponseStructure tmResponse = tService.translate(doc, lang);
            Document translatedDoc = tmResponse.getResponseCDA();
            return translatedDoc;
        } else {
            log.info("The Transformation Service did not started correctly");
            return doc;
        }
    }

    public static String styleDoc(String input, String lang,
                                  boolean commonstyle, String actionUrl, boolean shownarrative) {
        String convertedcda = "";
        EpsosXSLTransformer xlsClass = new EpsosXSLTransformer();

        if (commonstyle) {
            log.info("Transform the document using standard stylesheet as this is ccda");
            convertedcda = xlsClass.transformUsingStandardCDAXsl(input);
        } else {
            log.info("Transform the document using cdadisplay tool as this is epsos cda");
            convertedcda = xlsClass.transform(input, lang, actionUrl);
        }

        return convertedcda;
    }

    public static String styleDoc(String input, String lang,
                                  boolean commonstyle, String actionUrl) {
        return styleDoc(input, lang, commonstyle, actionUrl, false);
    }

    public static String transformDoc(String input) throws IOException {
        boolean isCDA = false;
        try {
            Document doc1 = com.gnomon.epsos.model.cda.Utils
                    .createDomFromString(input);
            isCDA = EpsosHelperService.isCDA(doc1);
            log.info("########## IS CDA" + isCDA);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        InputStream cdaInputStream;
        ByteArrayOutputStream cdaOutputStream;

        TrilliumBridgeTransformer transformer = new XsltTrilliumBridgeTransformer();
        cdaInputStream = new ByteArrayInputStream(input.getBytes());
        cdaOutputStream = new ByteArrayOutputStream();
        String mayoTransformed = "";
        if (isCDA) {
            transformer.epsosToCcda(cdaInputStream, cdaOutputStream,
                    TrilliumBridgeTransformer.Format.XML, null);
            mayoTransformed = new String(cdaOutputStream.toByteArray());
        } else {
            transformer.ccdaToEpsos(cdaInputStream, cdaOutputStream,
                    TrilliumBridgeTransformer.Format.XML, null);
            mayoTransformed = new String(cdaOutputStream.toByteArray());
        }
        return mayoTransformed;
    }

    public static boolean isCDA(Document xmlDocument)
            throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new CDANameSpaceContext());
        boolean isCDA = false;
        boolean isHCER = false;

        String expression1 = "/xsi:ClinicalDocument/xsi:templateId[@root='1.3.6.1.4.1.12559.11.10.1.3.1.1.3']";
        String epExpression = "/xsi:ClinicalDocument/xsi:templateId[@root='1.3.6.1.4.1.12559.11.10.1.3.1.1.1']";
        Node node = (Node) xPath.compile(expression1).evaluate(xmlDocument,
                XPathConstants.NODE);
        Node epnode = (Node) xPath.compile(epExpression).evaluate(xmlDocument,
                XPathConstants.NODE);
        isCDA = !Validator.isNull(node) || !Validator.isNull(epnode);
        expression1 = "/xsi:ClinicalDocument/xsi:templateId[@root='1.3.6.1.4.1.12559.11.10.1.3.1.1.4']";
        node = (Node) xPath.compile(expression1).evaluate(xmlDocument,
                XPathConstants.NODE);
        if (Validator.isNull(node)) {
            isHCER = false;
        } else {
            isCDA = true;
        }

        return isCDA || isHCER;
    }

    public static Document loadXMLFromString(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            doc = builder.parse(is);
        } catch (ParserConfigurationException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        } catch (SAXException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        } catch (IOException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }

        return doc;
    }

    public static List<Patient> searchPatients(Assertion assertion,
                                               PatientDemographics pd, String country) {
        List<Patient> patients = null;
        log.info("Selected country is: " + country);
        String runningMode = MyServletContextListener.getRunningMode();
        if (runningMode.equals("demo")) {
            patients = EpsosHelperService.getMockPatients();
        } else {
            try {
                patients = new ArrayList<Patient>();
                String serviceUrl = EpsosHelperService
                        .getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);
                log.info("CONNECTOR URL IS: " + serviceUrl);
                ClientConnectorConsumer proxy = MyServletContextListener
                        .getClientConnectorConsumer();
                Assertion ass = assertion;
                log.info("Searching for patients in " + country);
                log.info("Assertion id: " + ass.getID());
                log.info("PD: " + pd.toString());
                List<PatientDemographics> queryPatient = proxy.queryPatient(
                        ass, country, pd);
                for (PatientDemographics aux : queryPatient) {
                    Patient patient = new Patient();
                    patient.setName(aux.getGivenName());
                    patient.setFamilyName(aux.getFamilyName());
                    Calendar cal = aux.getBirthDate();
                    // DateFormat sdf = LiferayUtils.getPortalUserDateFormat();
                    // patient.setBirthDate(sdf.format(cal.getTime()));
                    patient.setCity(aux.getCity());
                    patient.setAddress(aux.getStreetAddress());
                    patient.setAdministrativeGender(aux
                            .getAdministrativeGender());
                    patient.setCountry(aux.getCountry());
                    patient.setEmail(aux.getEmail());
                    patient.setPostalCode(aux.getPostalCode());
                    patient.setTelephone(aux.getTelephone());
                    patient.setRoot(aux.getPatientIdArray()[0].getRoot());
                    patient.setExtension(aux.getPatientIdArray()[0]
                            .getExtension());
                    patient.setPatientDemographics(aux);
                    patients.add(patient);
                }
                log.info("Found " + patients.size() + " patients");
            } catch (Exception ex) {
                log.error(ExceptionUtils.getStackTrace(ex));
                log.error(ex.getMessage());
                patients = new ArrayList<Patient>();
            }
        }
        return patients;
    }

    public static List<PatientDocument> getPSDocs(Assertion assertion,
                                                  Assertion trca, String root, String extension, String country) {
        log.info("getPSDocs");
        List<PatientDocument> patientDocuments = null;
        PatientId patientId = null;
        try {
            patientDocuments = new ArrayList<PatientDocument>();
            String serviceUrl = EpsosHelperService
                    .getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);
            log.info("CLIENTCONNECTOR: " + serviceUrl);
            ClientConnectorConsumer clientConectorConsumer = MyServletContextListener
                    .getClientConnectorConsumer();
            patientId = PatientId.Factory.newInstance();
            patientId.setRoot(root);
            patientId.setExtension(extension);
            GenericDocumentCode classCode = GenericDocumentCode.Factory
                    .newInstance();
            classCode.setNodeRepresentation(Constants.PS_CLASSCODE);
            classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
            classCode.setValue(Constants.PS_TITLE); // Patient

            log.info("PS QUERY: Getting ps documents for : "
                    + patientId.getExtension() + " from " + country);
            List<EpsosDocument1> queryDocuments = clientConectorConsumer
                    .queryDocuments(assertion, trca, country, patientId,
                            classCode);
            log.info("PS QUERY: Found " + queryDocuments.size() + " for : "
                    + patientId.getRoot() + "-" + patientId.getExtension() + " from " + country);
            for (EpsosDocument1 aux : queryDocuments) {
                PatientDocument document = new PatientDocument();
                document.setAuthor(aux.getAuthor());
                Calendar cal = aux.getCreationDate();
                log.info("DATE IS " + aux.getCreationDate());
                // DateFormat sdf = LiferayUtils.getPortalUserDateFormat();
                // try {
                // document.setCreationDate(sdf.format(cal.getTime()));
                // } catch (Exception e) {
                // document.setCreationDate(aux.getCreationDate() + "");
                // log.error("Problem converting date" + aux.getCreationDate());
                // };
                document.setDescription(aux.getDescription());
                document.setHealthcareFacility("");
                document.setTitle(aux.getTitle());
                document.setFile(aux.getBase64Binary());
                document.setUuid(URLEncoder.encode(aux.getUuid(), "UTF-8"));
                document.setFormatCode(aux.getFormatCode());
                document.setRepositoryId(aux.getRepositoryId());
                document.setHcid(aux.getHcid());
                document.setDocType("ps");
                patientDocuments.add(document);
            }
            log.debug("Selected Country: " + country);
        } catch (Exception ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }
        return patientDocuments;
    }

    public static List<PatientDocument> getEPDocs(Assertion assertion,
                                                  Assertion trca, String root, String extension, String country) {
        List<PatientDocument> patientDocuments = null;
        PatientId patientId = null;
        try {
            patientDocuments = new ArrayList<PatientDocument>();
            String serviceUrl = EpsosHelperService
                    .getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL); // serviceUrl
            // =
            // LiferayUtils.getFromPrefs("client_connector_url");
            log.info("CLIENTCONNECTOR: " + serviceUrl);
            ClientConnectorConsumer clientConectorConsumer = MyServletContextListener
                    .getClientConnectorConsumer();
            patientId = PatientId.Factory.newInstance();
            patientId.setRoot(root);
            patientId.setExtension(extension);
            GenericDocumentCode classCode = GenericDocumentCode.Factory
                    .newInstance();
            classCode.setNodeRepresentation(Constants.EP_CLASSCODE);
            classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
            classCode.setValue(Constants.EP_TITLE); // Patient

            log.info("EP QUERY: Getting ep documents for : "
                    + patientId.getExtension() + " from " + country);
            List<EpsosDocument1> queryDocuments = clientConectorConsumer
                    .queryDocuments(assertion, trca, country, patientId,
                            classCode);
            log.info("EP QUERY: Found " + queryDocuments.size() + " for : "
                    + patientId.getExtension() + " from " + country);
            for (EpsosDocument1 aux : queryDocuments) {
                PatientDocument document = new PatientDocument();
                document.setAuthor(aux.getAuthor());
                Calendar cal = aux.getCreationDate();
                log.info("DATE IS " + aux.getCreationDate());
                // DateFormat sdf = LiferayUtils.getPortalUserDateFormat();
                // try {
                // document.setCreationDate(sdf.format(cal.getTime()));
                // } catch (Exception e) {
                // document.setCreationDate(aux.getCreationDate() + "");
                // log.error("Problem converting date" + aux.getCreationDate());
                // };
                document.setDescription(aux.getDescription());
                document.setHealthcareFacility("");
                document.setTitle(aux.getTitle());
                document.setFile(aux.getBase64Binary());
                document.setUuid(URLEncoder.encode(aux.getUuid(), "UTF-8"));
                document.setFormatCode(aux.getFormatCode());
                document.setRepositoryId(aux.getRepositoryId());
                document.setHcid(aux.getHcid());
                document.setDocType("ps");
                patientDocuments.add(document);
            }
            log.debug("Selected Country: " + country);
        } catch (Exception ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            // if (ex.getMessage().contains("4701")) {
            // //throw new ConsentException();
            // }
        }
        return patientDocuments;
    }

    public static String getDocument(Assertion assertion, Assertion trca,
                                     String country, String repositoryid, String homecommunityid,
                                     String documentid, String doctype, String lang)
            throws UnsupportedEncodingException {
        EpsosDocument selectedEpsosDocument = new EpsosDocument();
        String serviceUrl = EpsosHelperService
                .getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);
        ClientConnectorConsumer clientConectorConsumer = MyServletContextListener
                .getClientConnectorConsumer();

        Assertion hcpAssertion = assertion;
        Assertion trcAssertion = trca;
        String selectedCountry = country;

        log.info("HCP ASS: " + hcpAssertion.getID());
        log.info("TRCA ASS: " + trcAssertion.getID());
        log.info("SELECTED COUNTRY: " + selectedCountry);

        DocumentId documentId = DocumentId.Factory.newInstance();
        log.info("Setting DocumenUniqueID " + documentid);
        documentId.setDocumentUniqueId(documentid);
        log.info("Setting RepositoryUniqueId " + repositoryid);
        documentId.setRepositoryUniqueId(repositoryid);
        GenericDocumentCode classCode = GenericDocumentCode.Factory
                .newInstance();
        log.info("Document : " + documentid + " is " + doctype);
        if (doctype.equals("ep")) {
            classCode.setNodeRepresentation(Constants.EP_CLASSCODE);
            classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
            classCode.setValue(Constants.EP_TITLE);
        }
        if (doctype.equals("ps")) {
            classCode.setNodeRepresentation(Constants.PS_CLASSCODE);
            classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
            classCode.setValue(Constants.PS_TITLE);
        }
        if (doctype.equals("mro")) {
            classCode.setNodeRepresentation(Constants.MRO_CLASSCODE);
            classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
            classCode.setValue(Constants.MRO_TITLE);
        }

        log.info("selectedCountry: " + selectedCountry);
        log.info("classCode: " + classCode);

        String lang1 = lang.replace("_", "-");
        lang1 = lang1.replace("en-US", "en");

        log.info("Selected language is : " + lang + " - " + lang1);
        EpsosDocument1 eps = null;
        try {
            eps = clientConectorConsumer.retrieveDocument(
                    hcpAssertion, trcAssertion, selectedCountry, documentId,
                    homecommunityid, classCode, lang1);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error getting document " + documentid);
        }
        String xmlfile = "";
        if (Validator.isNotNull(eps)) {
            selectedEpsosDocument.setAuthor(eps.getAuthor() + "");
            try {
                selectedEpsosDocument.setCreationDate(eps.getCreationDate());
            } catch (Exception ex) {
                log.error(ExceptionUtils.getStackTrace(ex));
            }
            selectedEpsosDocument.setDescription(eps.getDescription());
            selectedEpsosDocument.setTitle(eps.getTitle());

            xmlfile = new String(eps.getBase64Binary(), "UTF-8");

            log.debug("#### CDA XML Start");
            log.info(xmlfile);
            log.debug("#### CDA XML End");
        }
        return xmlfile;
    }

    public static PatientDocument populateDocument(EpsosDocument1 aux,
                                                   String doctype) throws UnsupportedEncodingException {
        PatientDocument document = new PatientDocument();
        document.setAuthor(aux.getAuthor());
        Calendar cal = aux.getCreationDate();
        DateFormat sdf = LiferayUtils.getPortalUserDateFormat();
        try {
            document.setCreationDate(sdf.format(cal.getTime()));
        } catch (Exception e) {
            document.setCreationDate(aux.getCreationDate() + "");
            log.error("Problem converting date" + aux.getCreationDate());
            log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
        }
        document.setDescription(aux.getDescription());
        document.setHealthcareFacility("");
        document.setTitle(aux.getTitle());
        document.setFile(aux.getBase64Binary());
        document.setUuid(URLEncoder.encode(aux.getUuid(), "UTF-8"));
        document.setFormatCode(aux.getFormatCode());
        document.setRepositoryId(aux.getRepositoryId());
        document.setHcid(aux.getHcid());
        document.setDocType(doctype);
        return document;
    }

    public static Patient populatePatient(PatientDemographics aux) {
        Patient patient = new Patient();
        patient.setName(aux.getGivenName());
        patient.setFamilyName(aux.getFamilyName());
        Calendar cal = aux.getBirthDate();
        DateFormat sdf = LiferayUtils.getPortalUserDateFormat();
        patient.setBirthDate(sdf.format(cal.getTime()));
        patient.setCity(aux.getCity());
        patient.setAdministrativeGender(aux.getAdministrativeGender());
        patient.setCountry(aux.getCountry());
        patient.setEmail(aux.getEmail());
        patient.setPostalCode(aux.getPostalCode());
        patient.setTelephone(aux.getTelephone());
        patient.setRoot(aux.getPatientIdArray()[0].getRoot());
        patient.setExtension(aux.getPatientIdArray()[0].getExtension());
        patient.setPatientDemographics(aux);
        return patient;
    }

    public static PatientDemographics createPatientDemographicsForQuery(
            List<Identifier> identifiers, List<Demographics> demographics) {
        PatientDemographics pd = PatientDemographics.Factory.newInstance();
        PatientId[] idArray = new PatientId[identifiers.size()];
        for (int i = 0; i < identifiers.size(); i++) {
            PatientId id = PatientId.Factory.newInstance();
            id.setRoot(identifiers.get(i).getDomain());
            id.setExtension(identifiers.get(i).getUserValue());
            idArray[i] = id;
            log.info(identifiers.get(i).getDomain() + ": "
                    + identifiers.get(i).getUserValue());
        }

        for (int i = 0; i < demographics.size(); i++) {
            Demographics dem = demographics.get(i);
            if (dem.getKey().equals("patient.data.surname")) {
                pd.setFamilyName(dem.getUserValue());
            } else if (dem.getKey().equals("patient.data.givenname")) {
                pd.setGivenName(dem.getUserValue());
            } else if (dem.getKey().equals("patient.data.birth.date")) {
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dem.getUserDateValue());
                    pd.setBirthDate(cal);
                } catch (Exception ex) {
                    log.error("Invalid Date Format for date "
                            + dem.getUserValue());
                }
            } else if (dem.getKey().equals("patient.data.street.address")) {
                pd.setStreetAddress(dem.getUserValue());
            } else if (dem.getKey().equals("patient.data.code")) {
                pd.setPostalCode(dem.getUserValue());
            } else if (dem.getKey().equals("patient.data.city")) {
                pd.setCity(dem.getUserValue());
            } else if (dem.getKey().equals("patient.data.sex")) {
                pd.setAdministrativeGender(dem.getUserValue());
            }
            log.info(demographics.get(i).getKey() + ": "
                    + demographics.get(i).getUserValue());
        }

        pd.setPatientIdArray(idArray);
        return pd;
    }
}
