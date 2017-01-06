package com.gnomon;

import com.gnomon.epsos.FacesService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.LiferayPortletSession;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LiferayUtils {

    private static final Logger log = LoggerFactory.getLogger("LiferayUtils");
    public static final String LPPharmacistRole = "Pharmacist";
    public static final String LPDoctorRole = "Doctor";
    public static final String LPNurseRole = "Nurse";
    public static final String LPAdministratorRole = "Administrator";
    public static final String LPPatientRole = "Patient";

    private static PortletRequest getPortletRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        PortletRequest portletRequest = (PortletRequest) externalContext
                .getRequest();
        return portletRequest;
    }

    public static String getFromPrefs(String key) {
        PortletPreferences prefs = getPortletRequest().getPreferences();
        String value = prefs.getValue(key, "");
        return value;
    }

    public static void storeToSession(String param, Object value) {
        try {
            log.info("Try to store to session the parameter : " + param + " with value : " + value);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            PortletRequest portletRequest = (PortletRequest) externalContext
                    .getRequest();
            PortletSession prtSession = portletRequest.getPortletSession();
            prtSession.setAttribute(param, value, PortletSession.APPLICATION_SCOPE);
        } catch (Exception e) {
            log.error("ERROR: While trying to store to session the parameter : " + param + " with value : " + value + ": " + e.getMessage());
        }
    }

    public static Object getPortletRequestParam(String param) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        PortletRequest portletRequest = (PortletRequest) externalContext
                .getRequest();
        return portletRequest.getAttribute(param);
    }

    public static Object getFromSession(String param) {
        Object ret = null;
        try {
            log.info("Try to get from session the parameter : " + param);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            PortletRequest portletRequest = (PortletRequest) externalContext
                    .getRequest();
            PortletSession prtSession = portletRequest.getPortletSession();
            ret = prtSession.getAttribute(param, PortletSession.APPLICATION_SCOPE);
        } catch (Exception e) {
            log.error("ERROR: While trying to get from session the parameter : " + param + ": " + e.getMessage());
        }
        return ret;
    }

    public static boolean isPharmacist(long userid, long companyid) {
        return userHasRole(userid, companyid, LPPharmacistRole);
    }

    public static boolean isDoctor(long userid, long companyid) {
        return userHasRole(userid, companyid, LPDoctorRole);
    }

    public static boolean isNurse(long userid, long companyid) {
        return userHasRole(userid, companyid, LPNurseRole);
    }

    public static boolean isAdministrator(long userid, long companyid) {
        return userHasRole(userid, companyid, LPAdministratorRole);
    }

    public static boolean isPatient(long userid, long companyid) {
        return userHasRole(userid, companyid, LPPatientRole);
    }

    public static boolean userHasRole(long userId, long companyId, String rolename) {
        boolean hasRole = false;
        try {
            hasRole = RoleLocalServiceUtil.hasUserRole(userId, companyId, rolename, false);
            if (hasRole) {
                log.info("User has role: " + rolename);
            }
        } catch (PortalException e) {
            // TODO Auto-generated catch block
            log.error(ExceptionUtils.getStackTrace(e));
        } catch (SystemException e) {
            // TODO Auto-generated catch block
            log.error(ExceptionUtils.getStackTrace(e));
        }
//		try {
//			List<Role> userRoles = RoleServiceUtil. getUserRoles(userid);
//
//			for (int i=0;i<userRoles.size();i++)	{
//				Role role = userRoles.get(i);
//				log.info("ROLE: " + role.getName());
//				if (role.getName().equalsIgnoreCase(rolename))
//					return true;
//			}
//		} catch (PortalException e) {
//			// TODO Auto-generated catch block
//			log.error(ExceptionUtils.getStackTrace(e));
//		} catch (SystemException e) {
//			// TODO Auto-generated catch block
//			log.error(ExceptionUtils.getStackTrace(e));
//		}
        return hasRole;
    }

    public static DateFormat getPortalUserDateFormat() {
        User user = null;
        DateFormat df = null;
        PortletRequest portletRequest = getPortletRequest();
        try {
            user = PortalUtil.getUser(portletRequest);
            df = DateFormat.getDateInstance(SimpleDateFormat.LONG, user.getLocale());
        } catch (Exception e1) {
            log.error(ExceptionUtils.getStackTrace(e1));
        }
        return df;
    }

    public static DateFormat getSearchMaskDateFormat() {
        DateFormat df = null;
        try {
            String pattern = "yyyyMMdd";
            df = new SimpleDateFormat(pattern);

        } catch (Exception e1) {
            log.error(ExceptionUtils.getStackTrace(e1));
        }
        return df;
    }

    public static String getPortalTranslation(String path, String key, String lang) {
        return FacesService.translate(key, lang);
    }

    public static String getPortalTranslation(String key, String lang) {
        return FacesService.translate(key, lang);
    }

    public static String getPortalTranslation(String key) {
        return FacesService.translate(key);
    }

    public static Connection getCurrentConnection() {
        Connection conn = null;
        String driverClassName = PropsUtil.get("jdbc.default.driverClassName");
        String url = PropsUtil.get("jdbc.default.url");
        String username = PropsUtil.get("jdbc.default.username");
        String password = PropsUtil.get("jdbc.default.password");

        try {
            Class.forName(driverClassName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            log.error("Error getting session");
        }
        return conn;
    }

    public static User getPortalUser() {
        User user = null;
        PortletRequest portletRequest = getPortletRequest();
        try {
            user = PortalUtil.getUser(portletRequest);
        } catch (PortalException e1) {
            // TODO Auto-generated catch block
            log.error(ExceptionUtils.getStackTrace(e1));
        } catch (SystemException ex) {
            log.error(null, ex);
        }
        return user;
    }

    public static Company getPortalCompany() {
        Company company = null;
        PortletRequest portletRequest = getPortletRequest();
        try {
            company = PortalUtil.getCompany(portletRequest);
        } catch (PortalException e1) {
            // TODO Auto-generated catch block
            log.error(ExceptionUtils.getStackTrace(e1));
        } catch (SystemException e1) {
            // TODO Auto-generated catch block
            log.error(ExceptionUtils.getStackTrace(e1));
        }
        return company;
    }

    public static String getPortalLanguage() {
        PortletRequest portletRequest = getPortletRequest();
        String lang = portletRequest.getLocale().getLanguage() + "-"
                + portletRequest.getLocale().getCountry();
        return lang;
    }

    /**
     * Check the permission for the given actions
     *
     * @param actionKey String
     * @return boolean
     */
    public static boolean checkPermission(String actionKey) {
        PortletRequest request = ((PortletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest()));

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        PermissionChecker permissionChecker = themeDisplay.getPermissionChecker();

        String name = PortalUtil.getPortletId(request);
        String primKey = themeDisplay.getLayout().getPlid() + LiferayPortletSession.LAYOUT_SEPARATOR + name;
        long groupId = themeDisplay.getScopeGroupId();
        request.isUserInRole("administrator");
        return permissionChecker.hasPermission(groupId, name, primKey, actionKey);
    }

    public static boolean isAdministrator() {
        PortletRequest request = ((PortletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest()));
        return request.isUserInRole("administrator");
    }

}
