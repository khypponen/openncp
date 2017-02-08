package com.liferay.portal.security.auth;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.stork.util.PatientSearchAttributes;
import com.liferay.portal.stork.util.Util;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.util.PwdGenerator;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.europa.ec.joinup.ecc.openstork.utils.StorkUtils;
import eu.europa.ec.joinup.ecc.openstork.utils.datamodel.HcpRole;
import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Performs autologin based on the assertion provided by Stork Application.
 *
 * @author Kostas Karkaletsis <k.karkaletsis@gnomon.com.gr>
 */
public class StorkAutoLogin implements AutoLogin {

    private static final Logger _log = LoggerFactory.getLogger(StorkHelper.class.getName());
    private final String USER_AGENT = "Mozilla/5.0";
    private String SAMLResponse;
    private String samlResponseXML;
    private ArrayList<PersonalAttribute> attrList;
    private static Properties configs;
    private static String providerName;
    private static String homepage = "/SP/";
    private String eIdentifier;
    private String givenName;
    private String surname;
    private String emailAddress;
    private String HCPInfo;
    private static IPersonalAttributeList attrs;
    private HcpRole hcpRole;

    private void getSAMLAttributes(HttpServletRequest request) throws UnsupportedEncodingException {
        System.out.println("Getting saml attributes");
        providerName = PropsUtil.get("provider.name");
        SAMLResponse = request.getParameter("SAMLResponse");
        //SAMLResponse = URLDecoder.decode(SAMLResponse);

        System.out.println("SAML RESPONSE IS : " + SAMLResponse);
        byte[] decSamlToken = PEPSUtil.decodeSAMLToken(SAMLResponse);
        samlResponseXML = new String(decSamlToken);
        System.out.println("SAML RESPONSE XML IS : " + samlResponseXML);
        request.setAttribute("USER_samlResponseXML", samlResponseXML);

        STORKAuthnResponse authnResponse = null;
        attrs = null;
        STORKSAMLEngine engine = STORKSAMLEngine.getInstance(Constants.SP_CONF);
        String host = (String) request.getRemoteHost();
        System.out.println("HOST IS : " + host);
        request.setAttribute("USER_test", "kostas");
        try {
            authnResponse = engine.validateSTORKAuthnResponseWithQuery(decSamlToken, host);
            Assertion onBehalf = StorkUtils.convertOnBehalfStorktoHcpAssertion(authnResponse);
            Map<String, String> onbehalfattrs = StorkUtils.getRepresentedPersonInformation(authnResponse);
            try {
                System.out.println("###############: " + onbehalfattrs.size() + " " + onbehalfattrs.get("givenName"));
            } catch (Exception e) {
            }
            request.setAttribute("USER_onbehalfassertion", onBehalf);
            request.setAttribute("USER_onbehalfattributes", onbehalfattrs);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        if (authnResponse.isFail()) {
            System.out.println("Problem with response");
        } else {
            attrs = authnResponse.getTotalPersonalAttributeList();
            if (attrs.isEmpty()) {
                attrs = authnResponse.getPersonalAttributeList();
            }
//          hcpRole = com.liferay.portal.stork.util.StorkConversionUtils.obtainRole(attrs);
//          System.out.println("HCP ROLE : " + hcpRole);

        }
    }

    public String[] login(HttpServletRequest req, HttpServletResponse res) throws AutoLoginException {
        System.out.println("############### STORK AUTO LOGIN ###############");
        try {
            getSAMLAttributes(req);
        } catch (UnsupportedEncodingException ex) {
            //java.util.logging.LoggerFactory.getLogger(StorkAutoLogin.class.getName()).log(Level.SEVERE, null, ex);
            _log.error(null, ex);
        }
        System.out.println("#### USER IS " + getSurname() + " " + getEmailAddress());
        User user = null;
        String[] credentials = null;

        long companyId = PortalUtil.getCompanyId(req);
        String login = null;

        try {
            String hcpInfo = getHCPInfo();
            String hcpRole = getHCPRoleString(hcpInfo);
            String epsosRole = getEpsosRole(hcpRole);
            System.out.println("HCP INFO: " + hcpInfo);
            System.out.println("STORK ROLE: " + hcpRole);
            System.out.println("EPSOS ROLE IS: " + epsosRole);

            System.out.println("Stork Autologin [modified 1]");

            if (!Util.isEnabled(companyId)) {
                return credentials;
            }

            user = loginFromSession(companyId, req);
            if (Validator.isNull(user)) {
                return credentials;
            }

            credentials = new String[3];
            credentials[0] = String.valueOf(user.getUserId());
            credentials[1] = user.getPassword();
            credentials[2] = Boolean.TRUE.toString();
            return credentials;

        } catch (NoSuchUserException e) {
            logError(e);
        } catch (Exception e) {
            logError(e);
            throw new AutoLoginException(e);
        }
        return credentials;
    }

    private User loginFromSession(long companyId, HttpServletRequest request) throws Exception {
        String login = null;
        String emailAddress = null;
        User user = null;
        System.out.println("IN login from STORK Saml Assertion");

        login = (getSurname() + getGivenName()).toLowerCase();
        emailAddress = getEmailAddress();
        if (Validator.isNull(login)) {
            return null;
        }
        if (login.indexOf("@") >= 0) {
            login = login.substring(0, login.indexOf("@"));
        }

        String authType = Util.getAuthType(companyId);

        try {
            user = UserLocalServiceUtil.getUserByEmailAddress(companyId, emailAddress);
            System.out.println("User found: " + user.getScreenName() + " (" + user.getEmailAddress() + ")");
            if (Util.autoUpdateUser(companyId)) {
                System.out.println("Auto-updating user...");
                //updateUserFromSession(companyId, user, request);
            }
        } catch (NoSuchUserException e) {
//			if (Util.autoCreateUser(companyId)) {
            System.out.println("Importing user from session...");
            user = createUserFromSession(companyId);
            System.out.println("Created user with ID: " + user.getUserId());
//			}
        }

        return user;
    }

    private User createUserFromSession(long companyId) throws Exception {
        User user = null;

        String screenName = (getSurname() + getGivenName()).toLowerCase();
        if (Validator.isNull(screenName)) {
            System.out.println("Cannot create user - missing screen name");
            return user;
        }

        /*
         You have to configure LP to not require email address for user
         users.email.address.required=false
         */
        String emailAddress = getEmailAddress();
//                if (Validator.isNull(emailAddress))
//                    emailAddress=screenName + "@stork.eu";
//
//		if (Validator.isNull(emailAddress)) {
//			System.out.println("Cannot create user - missing email");
//			return user;
//		}

        String firstname = getGivenName();
        if (Validator.isNull(firstname)) {
            System.out.println("Cannot create user - missing firstname");
            return user;
        }

        String surname = getSurname();

        if (Validator.isNull(surname)) {
            System.out.println("Cannot create user - missing surname");
            return user;
        }

        System.out.println("Creating user: screen name = [" + screenName + "], emailAddress = [" + emailAddress
                + "], first name = [" + firstname + "], surname = [" + surname + "]");

        user = addUser(companyId, screenName, emailAddress, firstname, surname);
        return user;
    }

    private User addUser(
            long companyId, String screenName, String emailAddress,
            String firstName, String lastName)
            throws Exception {

        long creatorUserId = 0;
        boolean autoPassword = true;
        String password1 = PwdGenerator.getPassword();
        String password2 = password1;
        boolean autoScreenName = true;
        long facebookId = 0;
        String openId = StringPool.BLANK;
        Locale locale = Locale.US;
        String middleName = StringPool.BLANK;
        int prefixId = 0;
        int suffixId = 0;
        boolean male = true;
        int birthdayMonth = Calendar.JANUARY;
        int birthdayDay = 1;
        int birthdayYear = 1970;
        String jobTitle = StringPool.BLANK;
        long[] groupIds = null;
        long[] organizationIds = null;
        long[] roleIds = null;
        long[] userGroupIds = null;
        boolean sendEmail = false;
        ServiceContext serviceContext = new ServiceContext();

        User user = UserLocalServiceUtil.addUser(
                creatorUserId, companyId, autoPassword, password1, password2,
                autoScreenName, screenName, emailAddress, facebookId, openId,
                locale, firstName, middleName, lastName, prefixId, suffixId, male,
                birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds,
                organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);

        //
        String hcpInfo = getHCPInfo();
        String hcpRole = getHCPRoleString(hcpInfo);
        String epsosRole = getEpsosRole(hcpRole);
        _log.info("HCP INFO: " + hcpInfo);
        _log.info("STORK ROLE: " + hcpRole);
        _log.info("EPSOS ROLE IS: " + epsosRole);

        // Add epsos role
        Role role = null;
        try {
            role = RoleLocalServiceUtil.getRole(companyId, epsosRole);
            System.out.println("LIFERAY ROLE FOR " + epsosRole + " IS " + role.getName());
            UserLocalServiceUtil.addRoleUser(role.getRoleId(), user.getUserId());
        } catch (Exception e) {
            System.out.println("Problem adding " + epsosRole + " role to the user");
            e.printStackTrace();
        }

        // Add default patient role
        try {
            role = RoleLocalServiceUtil.getRole(companyId, "patient");
            UserLocalServiceUtil.addRoleUser(role.getRoleId(), user.getUserId());
        } catch (Exception e) {
            System.out.println("Problem adding patient role to the user");
            e.printStackTrace();
        }

        String countryCode = ConfigurationManagerService.getInstance().getProperty("COUNTRY_CODE");
        System.out.println("The country code is :" + countryCode);
        System.out.println("Reading the required attributes from International Search Mask");
        Map<String, String> attributes = PatientSearchAttributes.getRequiredAttributesByCountry(countryCode);
        Iterator it = attributes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println("$$$$ " + pairs.getKey() + " = " + pairs.getValue());
            try {
                String attrName = pairs.getValue().toString(); //"2.16.470.1.100.1.1.1000.990.1";
                String storkKey = pairs.getKey().toString();
                String value = getSamlValue(storkKey);
                try {
                    if (storkKey.equalsIgnoreCase("eIdentifier")) {
                        String[] temp = value.split("/");
                        value = temp[2];
                    } else {
                        value = getSamlValue(storkKey);
                    }
                } catch (Exception e) {
                    System.out.println("Problem with eIdentifier " + value);
                }

                System.out.println("Adding attribute :" + attrName + " with value " + value);
                ExpandoTable table = null;
                table = ExpandoTableLocalServiceUtil.getTable(companyId,
                        User.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME);
                ExpandoColumn column = StorkHelper.addExpandoColumn(companyId, user, role.getRoleId(), attrName);
                StorkHelper.updateColumnValue(table.getTableId(),
                        attrName,
                        value,
                        companyId,
                        user.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // Add custom fields
//                // Add HCP Role
//                System.out.println(hcpRole);
//
        System.out.println("After UserLocalServiceUtil.addUser: " + screenName + ", " + emailAddress + ", " + firstName + " " + lastName);
        UserLocalServiceUtil.updatePasswordReset(user.getUserId(), false);
        UserLocalServiceUtil.updateReminderQuery(user.getUserId(), password1, password2);

        return user;
    }

    private void updateUserFromSession(long companyId, User user, HttpServletRequest request) throws Exception {

        boolean modified = false;

        String emailAddress = (String) request.getAttribute(Util.getEmailHeaderName(companyId));
        System.out.println("updateUserFromSession: User [" + user.getScreenName() + "]: update email address [" + user.getEmailAddress()
                + "] --> [" + emailAddress + "]");
        if (!Validator.isNull(emailAddress) && !user.getEmailAddress().equals(emailAddress)) {
            System.out.println("User [" + user.getScreenName() + "]: update email address [" + user.getEmailAddress()
                    + "] --> [" + emailAddress + "]");
            user.setEmailAddress(emailAddress);
            modified = true;
        }

        String firstname = getGivenName();
        if (firstname.indexOf(";") >= 0) {
            firstname = firstname.substring(0, firstname.indexOf(";"));
        }
        if (!Validator.isNull(firstname) && !user.getFirstName().equals(firstname)) {
            user.setFirstName(firstname);
            modified = true;
        }

        String surname = getSurname();
        if (surname.indexOf(";") >= 0) {
            surname = surname.substring(0, surname.indexOf(";"));
        }
        if (!Validator.isNull(surname) && !user.getLastName().equals(surname)) {
            System.out.println("User [" + user.getScreenName() + "]: update last name [" + user.getLastName() + "] --> ["
                    + surname + "]");
            user.setLastName(surname);
            modified = true;
        }
        UserLocalServiceUtil.updateUser(user);
    }

    public void logError(Exception e) {
        _log.error("Exception message = " + e.getMessage() + " cause = " + e.getCause());
        if (_log.isDebugEnabled()) {
            e.printStackTrace();
        }

    }

    private String getIsoEncoding(String input) throws UnsupportedEncodingException {
        byte[] utf = input.getBytes();
        byte[] b = new String(utf, "UTF-8").getBytes("ISO-8859-1");
        String output = new String(b);
        return output;
    }

    public String[] handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws AutoLoginException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getSamlValue_(String key) {
        String ret = "";
        try {
            ret = attrs.get(key).getValue().get(0);
        } catch (Exception e) {
            System.out.println("Error with value for " + key);
        }
        return ret;
    }

    public String getSamlValue(IPersonalAttributeList attrs, String key) {
        String ret = "";
        try {
            ret = attrs.get(key).getValue().get(0);
        } catch (Exception e) {
            System.out.println("Error with value for " + key);
        }
        return ret;
    }

    public String getSamlValue(String key) {
        String ret = "";
        try {
            ret = attrs.get(key).getValue().get(0);
        } catch (Exception e) {
            System.out.println("Error with value for " + key);
        }
        return ret;
    }

    public String geteIdentifier() {
        return getSamlValue("eIdentifier");
    }

    public void seteIdentifier(String eIdentifier) {
        this.eIdentifier = eIdentifier;
    }

    public String getGivenName() {
        return getSamlValue("givenName");
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return getSamlValue("surname");
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailAddress() {
        return getSamlValue("eMail");
    }

    public String getHCPInfo() {
        return getSamlValue("isHealthCareProfessional");
    }

    public String getEpsosRole(String HCPRole) {
        _log.info("Try to find the role from assertion " + HCPRole);
        if (HCPRole.equalsIgnoreCase("dentist")) {
            return "dentist";
        }
        if (HCPRole.equalsIgnoreCase("nurse")) {
            return "nurse";
        }
        if (HCPRole.equalsIgnoreCase("pharmacist")) {
            return "pharmacist";
        }
        if (HCPRole.equalsIgnoreCase("physician")) {
            return "doctor";
        }
        return "patient";
    }

    public String getHCPRoleString(String xml) {
        xml = xml.replaceAll("^\"|\"$", "");
        String typeOfHCP = "";
        try {
            InputSource source = new InputSource(new StringReader(xml));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(source);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            typeOfHCP = xpath.evaluate("/isHealthCareProfessional/typeOfHCP", document);
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("Error finding xpath value");
        }

        return typeOfHCP;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public HcpRole getHcpRole() {
        return hcpRole;
    }

    public void setHcpRole(HcpRole hcpRole) {
        this.hcpRole = hcpRole;
    }

}
