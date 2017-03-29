package com.gnomon.epsos.rest;

import com.gnomon.epsos.model.*;
import com.gnomon.epsos.model.adapter.PatientAdapter;
import com.gnomon.epsos.model.adapter.PatientDocumentAdapter;
import com.gnomon.epsos.model.queries.Info;
import com.gnomon.epsos.model.queries.PatientDiscovery;
import com.gnomon.epsos.service.Demographics;
import com.gnomon.epsos.service.EpsosHelperService;
import com.gnomon.epsos.service.Utils;
import com.gnomon.epsos.util.DictionaryServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import epsos.ccd.gnomon.xslt.EpsosXSLTransformer;
import epsos.openncp.protocolterminator.clientconnector.PatientDemographics;
import epsos.openncp.protocolterminator.clientconnector.PatientId;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import static com.gnomon.epsos.filter.RestAuthenticationFilter.AUTHENTICATION_HEADER;

/**
 * @author karkaletsis
 */
@Path("/")
public class EpsosRestService {

    public static final long companyId = 10157;
    private static final int bypassRefererChecking = 1;
    private static final Logger log = LoggerFactory.getLogger("EpsosRestService");
    private static final Utils utils = new Utils();
    @Context
    private static HttpServletRequest servletRequest;
    private PatientDiscovery pdq = null;
    private PatientDemographics pd = null;
    private Info ru = null;
    private com.gnomon.epsos.model.queries.Document document;

    @GET
    @Path("/dictionary/conditions")
    public Response getListOfConditions() throws PortalException, SystemException {
        Map<String, List<DictionaryTerm>> problemsDictionary = DictionaryServices.getProblemsDictionaryTerms();
        String output = new Gson().toJson(problemsDictionary);
        return Response.ok().entity(output).build();
    }

    /**
     * Get the list of family roles
     *
     * @return
     */
    @GET
    @Path("/dictionary/familyroles")
    public Response getListOfFamilyRoles() throws PortalException, SystemException {
        Map<String, List<DictionaryTerm>> rolesDictionary = DictionaryServices.getFamilyRolesDictionaryTerms();
        String output = new Gson().toJson(rolesDictionary);
        return Response.ok().entity(output).build();
    }

    /**
     * Get the list of procedures. The list is coming from the LTR
     *
     * @return
     */
    @GET
    @Path("/dictionary/procedures")
    public Response getListOfProcedures() throws PortalException, SystemException {
        Map<String, List<DictionaryTerm>> snomedDictionary = DictionaryServices.getProceduresDictionaryTerms();
        String output = new Gson().toJson(snomedDictionary);
        return Response.ok().entity(output).build();
    }

    /**
     * Get the list of Allergies. The list is coming from the LTR
     *
     * @return
     */
    @GET
    @Path("/dictionary/allergies")
    public Response getListOfAllergies() throws PortalException, SystemException {
        Map<String, List<DictionaryTerm>> snomedDictionary = DictionaryServices.getAllergiesDictionaryTerms();
        String output = new Gson().toJson(snomedDictionary);
        return Response.ok().entity(output).build();
    }

    @GET
    @Path("/{param}")
    public Response test(@Context HttpServletRequest request, @PathParam("param") String msg) {
        String ip = request.getRemoteAddr();
        log.info("IP ADDRESS IS : " + ip);
        String output = "OpenNCP says : " + msg;
        return Response.status(200).entity(output).build();
    }

    @POST
    @Path("/users/login")
    public Response userLogin(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @HeaderParam("referer") String referer) throws PortalException, SystemException, UnsupportedEncodingException {
        if (!validReferer(referer)) {
            return Response.status(404).entity("Invalid Request").build();
        }
        log.info("user: " + username);
        log.info("password: " + password);
        username = URLDecoder.decode(username, "UTF-8");
        log.info("companyId: " + companyId);
        int usertype = 100;
        User user = null;
        String ret = "";
        try {
            log.info("Try to find user with screenname :" + username);
            user = UserLocalServiceUtil.getUserByScreenName(companyId, username);
        } catch (Exception e) {
            log.error("Error find user by screenname " + username);
        }
        if (Validator.isNull(user)) {
            try {
                log.info("Try to find user with email :" + username);
                user = UserLocalServiceUtil.getUserByEmailAddress(companyId, username);
            } catch (Exception e) {
                log.error("Error find user by emailaddress " + username);
            }
        }

        long auth = 0;
        if (Validator.isNotNull(user)) {

            if (!user.isActive()) {
                log.info("USER ACTIVE: " + user.isActive());
                ret = "401";
            } else {

                auth = UserLocalServiceUtil.authenticateForBasic(companyId,
                        CompanyConstants.AUTH_TYPE_EA,
                        username, password);
                if (auth < 1) {
                    auth = UserLocalServiceUtil.authenticateForBasic(companyId,
                            CompanyConstants.AUTH_TYPE_SN,
                            username, password);
                }
                log.info("auth: " + auth);
                if (auth == 0) {
                    ret = "400";
                }
            }
        }
        log.info("# AUTH " + auth);
        UserData ud = new UserData();
        if (auth > 0) {
            ud.setStatus("1");
            ud.setRet("200");
        } else {
            ud.setStatus("0");
            ud.setRet("400");
            ud.setEmailaddress("");
            ud.setUserId("0");
        }
        ud.setRet(ret);
        try {
            String encauth = Utils.encrypt(auth + "");
            log.info("### : " + encauth);
            if (auth > 0) {
                ud.setRet("200");
                ud.setUserId(encauth);
                ud.setScreenname(user.getScreenName());
                ud.setEmailaddress(user.getEmailAddress());
                ud.setUsertype(usertype);
            }
        } catch (Exception ex) {
            log.error("Error encrypring");
        }
        servletRequest.getSession(true).setAttribute("test", "kostas");
        log.info("SESSION SET: " + "kostas");
        Gson gson = new Gson();
        ret = gson.toJson(ud);
        return Response.ok().entity(ret).build();
    }

    /**
     * Returns the list of countries available in epSOS node.
     *
     * @param language
     * @param referer
     * @return
     * @throws PortalException
     * @throws SystemException
     * @throws Exception
     */
    @POST
    @Path("/get/countries/names/{language}")
    public Response getCountries(
            @PathParam("language") String language,
            @HeaderParam("referer") String referer) throws PortalException, SystemException, Exception {
        log.info("Get Countries for language: " + language);
        String ret = "";
        String path = servletRequest.getSession().getServletContext().getRealPath("/");
        log.info("Application Path is: " + path);
        List<Country> countries = EpsosHelperService.getCountriesFromCS(language, path);
        Gson gson = new Gson();
        ret = gson.toJson(countries);
        return Response.ok().entity(ret).build();
    }

    @GET
    @Path("/languages/get")
    public Response getAvailableLanguages(
            @HeaderParam("referer") String referer) throws PortalException, SystemException, Exception {

        List ltrlanguages = EpsosHelperService.getEHPLTRLanguages();
        Gson gson = new Gson();
        HashMap hp = new HashMap();
        List langs = new ArrayList();
        langs.add(ltrlanguages);
        hp.put("languages", ltrlanguages);
        String ret = gson.toJson(hp);
        return Response.ok().entity(ret).build();
    }

    @POST
    @Path("/get/countries/attributes/{country}/{language}")
    public Response getCountryAttributes(
            @Context HttpServletRequest request,
            @PathParam("country") String country,
            @PathParam("language") String language,
            @HeaderParam("referer") String referer) throws PortalException, SystemException, Exception {
        log.info("Get attributes for language: " + country);
        String ret = "";
        String path = request.getSession().getServletContext().getRealPath("/") + "/WEB-INF/";
        log.info("#################: " + path);
        Vector countryIdentifiers = EpsosHelperService.getCountryIdsFromCS(country, path);
        List<Identifier> identifiers = EpsosHelperService.getCountryIdentifiers(country, language, path, null);

        Vector countryDemographics = EpsosHelperService.getCountryDemographicsFromCS(country, path);
        List<Demographics> demographics = EpsosHelperService.getCountryDemographics(country, language, path, null);

        HashMap hp = new HashMap();
        hp.put("identifiers", identifiers);
        hp.put("demographics", demographics);

        Gson gson = new Gson();
        ret = gson.toJson(hp);
        return Response.ok().entity(ret).build();
    }

    /*
     <PatientDiscovery>
     <info>
     <screenname>doctor</screenname>
     <fullname>Kostas</fullname>
     <emailaddress>k.karkaletsis@gmail.com</emailaddress>
     <orgid>1</orgid>
     <orgname>portal</orgname>
     <orgtype>other</orgtype>
     <rolename>patient</rolename>
     </info>
     <identifiers>
     <identifier root="2.16.840.1.113883.2.9.4.3.2" extension="CRRPLA47H13A794V" />
     <identifier root="2.16.840.1.113883.2.9.4.1.4" extension="1234" />
     </identifiers>
     <demographics>
     <firstname>Martha</firstname>
     <lastname>Token</lastname>
     <country>KP</country>
     </demographics>
     </PatientDiscovery>
     */
    @POST
    @Path("/search/patients")
    public Response searchPatients(
            StreamSource streamSource) throws Exception {
        log.info("$$$$$$ Try to search patients");
        //log.info("Get into search patients: " + getUsernameFromHeaders());
        populatePatient(streamSource);
        log.info("Searching patients: " + ru.getEmailaddress() + " " + ru.getFullname() + " " + ru.getOrgid() + " " + ru.getOrgname() + " " + ru.getOrgtype() + " " + ru.getRolename() + " " + ru.getScreenname() + " " + pdq.getDemographics().getBirthDate());
        String ret = "";
        Object ass = EpsosHelperService.getUserAssertion(ru.getScreenname(), ru.getFullname(), ru.getEmailaddress(), ru.getRolename());
        log.info("The assertion for user has been created: " + ((Assertion) ass).getID());

        List<Patient> patients = EpsosHelperService.searchPatients((Assertion) ass, pd, pdq.getDemographics().getCountry());
        log.info("Patients found: " + patients.size());

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(Patient.class,
                new PatientAdapter()).create();
        ret = gson.toJson(patients);
        return Response.ok().entity(ret).build();
    }

    @POST
    @Path("/documents/transform")
    public Response transformDocument(StreamSource streamSource
    ) {
        String soapMessage = "";
        String convertedcda = "";
        try {
            Document doc = Utils.readXml(streamSource);
            soapMessage = Utils.transformDomToString(doc);
            EpsosXSLTransformer xlsClass = new EpsosXSLTransformer();
            convertedcda = xlsClass.transformUsingStandardCDAXsl(soapMessage);
        } catch (Exception e) {
            log.error("Error processing request");
            log.error(ExceptionUtils.getStackTrace(e));
            return Response.status(404).entity("Invalid Request").build();
        }

        return Response.ok().entity(convertedcda).build();

    }

    @POST
    @Path("/search/documents/ps/{username}")
    public Response getPatientPSDocuments(
            @FormParam("country") String country,
            @FormParam("root") String root,
            @FormParam("extension") String extension) throws Exception {

        log.info("Get into search patients");
        String ret = "";
        String username = getUsernameFromHeaders();
        User user = utils.getLiferayUserUnEncrypted(username);
        Object ass = EpsosHelperService.getUserAssertion(user);
        log.info("The assertion for user has been created: " + ((Assertion) ass).getID());
        PatientId patientId = PatientId.Factory.newInstance();
        patientId.setExtension(extension);
        patientId.setRoot(root);
        String purposeOfUse = "TREATMENT";
        log.info("TRCA: Creating trca for hcpAssertion : " + ((Assertion) ass).getID() + " for patient " + patientId.getRoot() + ". Purpose of use is : " + purposeOfUse);
        Object trcAssertion = EpsosHelperService.createPatientConfirmationPlain(purposeOfUse, (Assertion) ass, patientId);
        log.info("TRCA: Created " + ((Assertion) trcAssertion).getID() + " for : " + ((Assertion) ass).getID() + " for patient " + patientId.getRoot() + "_" + patientId.getExtension() + ". Purpose of use is : " + purposeOfUse);
        List<PatientDocument> patientDocuments = EpsosHelperService.getPSDocs(
                (Assertion) ass,
                (Assertion) trcAssertion,
                root,
                extension,
                country);
        log.info("PS Docs found: " + patientDocuments.size());

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(PatientDocument.class,
                new PatientDocumentAdapter()).create();
        ret = gson.toJson(patientDocuments);
        return Response.ok().entity(ret).build();
    }

    @POST
    @Path("/search/documents/ep/{country}/{patientid}")
    public Response getPatientEPDocuments(
            @PathParam("country") String country,
            @PathParam("root") String root,
            @PathParam("extension") String extension) throws Exception {

        log.info("Get into search patients");
        String ret = "";
        String username = getUsernameFromHeaders();
        User user = utils.getLiferayUserUnEncrypted(username);

        Object ass = EpsosHelperService.getUserAssertion(user);
        log.info("The assertion for user has been created: " + ((Assertion) ass).getID());
        PatientId patientId = PatientId.Factory.newInstance();
        patientId.setExtension(extension);
        patientId.setRoot(root);
        String purposeOfUse = "TREATMENT";
        log.info("TRCA: Creating trca for hcpAssertion : " + ((Assertion) ass).getID() + " for patient " + patientId.getRoot() + ". Purpose of use is : " + purposeOfUse);
        Object trcAssertion = EpsosHelperService.createPatientConfirmationPlain(purposeOfUse, (Assertion) ass, patientId);
        log.info("TRCA: Created " + ((Assertion) trcAssertion).getID() + " for : " + ((Assertion) ass).getID() + " for patient " + patientId.getRoot() + "_" + patientId.getExtension() + ". Purpose of use is : " + purposeOfUse);
        List<PatientDocument> patientDocuments = EpsosHelperService.getPSDocs(
                (Assertion) ass,
                (Assertion) trcAssertion,
                root,
                extension,
                country);
        log.info("PS Docs found: " + patientDocuments.size());

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(PatientDocument.class,
                new PatientDocumentAdapter()).create();
        ret = gson.toJson(patientDocuments);
        return Response.ok().entity(ret).build();
    }

    @POST
    @Path("/search/mydocuments/{doctype}")
    public Response getMyPatientDocuments(
            StreamSource streamSource,
            @PathParam("doctype") String doctype) throws Exception {
        String ret = "";
        String purposeOfUse = "TREATMENT";

        populatePatient(streamSource);
        Object ass = EpsosHelperService.getUserAssertion(ru.getScreenname(), ru.getFullname(), ru.getEmailaddress(), ru.getRolename());
        log.info("The assertion for user has been created: " + ((Assertion) ass).getID());
//        List<Patient> patients = EpsosHelperService.searchPatients((Assertion) ass, pd, pdq.getDemographics().getCountry());
        String root = pdq.getIdentifiers().getIdentifier().get(0).getRoot();
        String extension = pdq.getIdentifiers().getIdentifier().get(0).getExtension();
        String country = pdq.getDemographics().getCountry();
//        if (country.equalsIgnoreCase("KP")) {
//            country = "USA";
//        }
//        if (patients.size() > 0) {
        if (Validator.isNotNull(extension)) {
            log.info("PATIENT FOUND: " + extension);
            PatientId patientId = PatientId.Factory.newInstance();
//            patientId.setExtension(patients.get(0).getExtension());
//            patientId.setRoot(patients.get(0).getRoot());
            patientId.setExtension(extension);
            patientId.setRoot(root);
            Patient pat = new Patient();
            pat.setRoot(patientId.getRoot());
            pat.setExtension(patientId.getExtension());
            Object trcAssertion = EpsosHelperService.createPatientConfirmationPlain(purposeOfUse, (Assertion) ass, patientId);
            List<PatientDocument> patientDocuments = new ArrayList();
            log.info("$$$$$$$$$ 1:" + ((Assertion) ass).getID() + "-2:" + ((Assertion) trcAssertion).getID() + "-3: " + root + " -4: " + extension + "-5: " + country);
            if (doctype.equalsIgnoreCase("ps")) {
                patientDocuments = EpsosHelperService.getPSDocs(
                        (Assertion) ass,
                        (Assertion) trcAssertion,
                        root,
                        extension,
                        country);
            }
            if (doctype.equalsIgnoreCase("ep")) {
                patientDocuments = EpsosHelperService.getEPDocs(
                        (Assertion) ass,
                        (Assertion) trcAssertion,
                        root,
                        extension,
                        country);
            }
            DocumentExt dext = new DocumentExt();
            dext.setDocuments(patientDocuments);
            dext.setPatient(pat);

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.registerTypeAdapter(PatientDocument.class,
                    new PatientDocumentAdapter()).create();
            ret = gson.toJson(dext);
            log.info(ret);
        } else {
            return Response.status(404).entity("Invalid Request").build();
        }
        return Response.ok().entity(ret).build();
    }
    /*
     <PatientDiscovery>
     <Document>
     <root>2.16.840.1.113883.2.9.4.3.2</root>
     <extension>CRRPLA47H13A794V</extension>
     <repositoryid>2.16.840.1.113883.19.5.88888.1</repositoryid>
     <hcid>2.16.17.710.806.1000.990.1</hcid>
     <uuid>2.16.840.1.113883.19.5.88888.1%5ETB1.PS.2</uuid>
     </Document>
     <info>
     <screenname>doctor</screenname>
     <fullname>Kostas</fullname>
     <emailaddress>k.karkaletsis@gmail.com</emailaddress>
     <orgid>1</orgid>
     <orgname>portal</orgname>
     <orgtype>other</orgtype>
     <rolename>patient</rolename>
     </info>
     <identifiers>
     <identifier root="1.3.6.1.4.1.26580.10" extension="551121234" />
     </identifiers>
     <demographics>
     <firstname>Martha</firstname>
     <lastname>Token</lastname>
     <country>KP</country>
     </demographics>
     </PatientDiscovery>
     */

    @POST
    @Path("/get/document/cda/{doctype}/{language}")
    public Response retrieveDocument(
            StreamSource streamSource,
            @PathParam("doctype") String doctype,
            @PathParam("language") String language
    ) throws Exception {
        log.info("#######################: " + servletRequest.getSession(true).getAttribute("test"));

        String ret = "";
        populatePatient(streamSource);
        log.info("COUNTRY: " + pd.getCountry());
        Object ass = EpsosHelperService.getUserAssertion(ru.getScreenname(), ru.getFullname(), ru.getEmailaddress(), ru.getRolename());
        log.info("The assertion for user has been created: " + ((Assertion) ass).getID());
        PatientId patientId = PatientId.Factory.newInstance();
        patientId.setExtension(document.getExtension());
        patientId.setRoot(document.getRoot());
        String purposeOfUse = "TREATMENT";
        log.info("TRCA: Creating trca for hcpAssertion : " + ((Assertion) ass).getID() + " for patient " + patientId.getRoot() + ". Purpose of use is : " + purposeOfUse);
        Object trcAssertion = EpsosHelperService.createPatientConfirmationPlain(purposeOfUse, (Assertion) ass, patientId);
        log.info("TRCA: Created " + ((Assertion) trcAssertion).getID() + " for : " + ((Assertion) ass).getID() + " for patient " + patientId.getRoot() + "_" + patientId.getExtension() + ". Purpose of use is : " + purposeOfUse);
        String cda = EpsosHelperService.getDocument((Assertion) ass, (Assertion) trcAssertion, pd.getCountry(), document.getRepositoryid(), document.getHcid(), document.getUuid(), doctype, language);
        if (Validator.isNotNull(cda)) {
            String pdf = EpsosHelperService.extractPdfPartOfDocument(cda);
            if (pdf.contains("data:application/pdf;base64")) {
                cda = new String(pdf);
            }
        }
//        if (transform) {
//            cda = EpsosHelperService.styleDoc(cda, language, true, "");
//        }
        return Response.ok().entity(cda).build();
    }

    @POST
    @Path("/translate/document/{language}")
    public Response translateDocument(
            @FormParam("cda") String cda,
            @PathParam("language") String language
    ) throws Exception {
        String ret = "";
        String translatedcda = EpsosHelperService.styleDoc(cda, language, false, "", true);
        return Response.ok().entity(translatedcda).build();
    }

    @POST
    @Path("/get/document/{doctype}/{language}/{transform}")
    public Response retrieveDocument(
            StreamSource streamSource,
            @PathParam("doctype") String doctype,
            @PathParam("language") String language,
            @PathParam("transform") boolean transform
    ) throws Exception {
        log.info("Get into retrieve document with transform : " + transform);
        String ret = "";
        populatePatient(streamSource);
        log.info("COUNTRY: " + pd.getCountry());
        Object ass = EpsosHelperService.getUserAssertion(ru.getScreenname(), ru.getFullname(), ru.getEmailaddress(), ru.getRolename());
        log.info("The assertion for user has been created: " + ((Assertion) ass).getID());
        PatientId patientId = PatientId.Factory.newInstance();
        patientId.setExtension(document.getExtension());
        patientId.setRoot(document.getRoot());
        String purposeOfUse = "TREATMENT";
        log.info("TRCA: Creating trca for hcpAssertion : " + ((Assertion) ass).getID() + " for patient " + patientId.getRoot() + ". Purpose of use is : " + purposeOfUse);
        Object trcAssertion = EpsosHelperService.createPatientConfirmationPlain(purposeOfUse, (Assertion) ass, patientId);
        log.info("TRCA: Created " + ((Assertion) trcAssertion).getID() + " for : " + ((Assertion) ass).getID() + " for patient " + patientId.getRoot() + "_" + patientId.getExtension() + ". Purpose of use is : " + purposeOfUse);
        String cda = EpsosHelperService.getDocument((Assertion) ass, (Assertion) trcAssertion, pd.getCountry(), document.getRepositoryid(), document.getHcid(), document.getUuid(), doctype, language);
        if (transform) {
            cda = EpsosHelperService.styleDoc(cda, language, false, "", false);
        }
        return Response.ok().entity(cda).build();
    }

    @POST
    @Path("/transform/document/{language}")
    public Response transformDocument(
            @FormParam("cda") String cda,
            @PathParam("language") String language
    ) {
        String ret = "";
        String convertedcda = "";
        try {
            convertedcda = com.gnomon.epsos.model.cda.Utils.getDocumentAsXml(EpsosHelperService.translateDoc(
                    com.gnomon.epsos.model.cda.Utils.createDomFromString(cda), language)
            );
            log.info("Converted cda is " + convertedcda.substring(0, 100));
            convertedcda = EpsosHelperService.styleDoc(convertedcda, language, false, "", false);
            log.info("Styled cda is " + convertedcda.substring(0, 100));

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(403).entity("Error transforming document").build();
        }
        return Response.ok().entity(convertedcda).build();
    }

    @POST
    @Path("/submit/document/ed/{username}/{country}")
    public String submitEDDocument() {
        return "";
    }

    @POST
    @Path("/submit/document/hcer/{username}/{country}")
    public String submitHCERDocument() {
        return "";
    }

    private boolean validReferer(String referer) {
        log.info("##### bypassRefererChecking " + bypassRefererChecking);
        if (bypassRefererChecking == 1) {
            return true;
        }
        if (Validator.isNull(referer)) {
            return false;
        }
        return referer.contains("gnomon");
    }

    private void populatePatient(StreamSource streamSource) {
        log.info("Populating patient...");
        String soapMessage = "";
        try {
            Document doc = Utils.readXml(streamSource);
            soapMessage = Utils.transformDomToString(doc);
            log.info(soapMessage);
        } catch (Exception e) {
            log.error("Error processing request");
            log.error(ExceptionUtils.getStackTrace(e));
        }

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(PatientDiscovery.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(soapMessage);
            pdq = (PatientDiscovery) jaxbUnmarshaller.unmarshal(reader);
            log.info("getDemographics(): " + pdq.getDemographics().getAddress());
            log.info("getIdentifier: " + pdq.getIdentifiers().getIdentifier().get(0).getExtension());
            pd = PatientDemographics.Factory.newInstance();
            ru = new Info();
            document = new com.gnomon.epsos.model.queries.Document();
            int pisize = pdq.getIdentifiers().getIdentifier().size();
            PatientId[] idArray = new PatientId[pisize];
            for (int i = 0; i < pisize; i++) {
                PatientId id = PatientId.Factory.newInstance();
                id.setRoot(pdq.getIdentifiers().getIdentifier().get(i).getRoot());
                id.setExtension(pdq.getIdentifiers().getIdentifier().get(i).getExtension());
                idArray[i] = id;
            }
            if (Validator.isNotNull(pdq.getDocument())) {
                document.setExtension(pdq.getDocument().getExtension());
                document.setRoot(pdq.getDocument().getRoot());
                document.setRepositoryid(pdq.getDocument().getRepositoryid());
                document.setHcid(pdq.getDocument().getHcid());
                document.setUuid(pdq.getDocument().getUuid());
            }

            if (Validator.isNotNull(pdq.getInfo())) {
                ru.setRolename(pdq.getInfo().getRolename());
                ru.setScreenname(pdq.getInfo().getScreenname());
                ru.setEmailaddress(pdq.getInfo().getEmailaddress());
                ru.setOrgid(pdq.getInfo().getOrgid());
                ru.setOrgname(pdq.getInfo().getOrgname());
                ru.setOrgtype(pdq.getInfo().getOrgtype());
                ru.setFullname(pdq.getInfo().getFullname());
            }

            if (Validator.isNotNull(pd)) {
                pd.setCountry(pdq.getDemographics().getCountry());
                pd.setFamilyName(pdq.getDemographics().getLastname());
                pd.setGivenName(pdq.getDemographics().getFirstname());
                pd.setStreetAddress(pdq.getDemographics().getAddress());
                pd.setCity(pdq.getDemographics().getCity());
                pd.setPostalCode(pdq.getDemographics().getPostalCode());
                pd.setAdministrativeGender(pdq.getDemographics().getGender());
                log.info("PRE DATE: " + pdq.getDemographics().getBirthDate());
                if (Validator.isNotNull(pdq.getDemographics().getBirthDate())) {
                    try {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(pdq.getDemographics().getBirthDate());
                        pd.setBirthDate(cal);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.error(ex.getMessage());
                    }
                }
            }
            log.info("firstname: " + pd.getFamilyName());
            log.info("getBirthDate:  " + pd.getBirthDate());
            log.info("PatientDemographic" + pd);
            pd.setPatientIdArray(idArray);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Problem during patient population");
        }
    }

    private String getUsernameFromHeaders() {
        String username = "";
        String authCredentials = servletRequest.getHeader(AUTHENTICATION_HEADER);
        log.info("try to authencticate with: " + authCredentials);
        final String encodedUserPassword = authCredentials.replaceFirst("Basic"
                + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.decodeBase64(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
            log.info("Decoded password: " + usernameAndPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        boolean authenticationStatus = false;
        try {
            username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();
            log.info("username: " + username + " password: " + password);
            // we have fixed the userid and password as admin
            // call some UserService/LDAP here
            long auth = UserLocalServiceUtil.authenticateForBasic(EpsosRestService.companyId,
                    CompanyConstants.AUTH_TYPE_SN,
                    username, password);
            log.info("Authentication status for username: " + username + " is " + auth);
            authenticationStatus = auth > 0;
//            authenticationStatus = "admin".equals(username)
//                    && "admin".equals(password);
        } catch (Exception e) {
            log.error("Error decoding username password");
        }
        return username;
    }
}
