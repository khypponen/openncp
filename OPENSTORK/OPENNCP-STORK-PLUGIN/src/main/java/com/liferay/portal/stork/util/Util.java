package com.liferay.portal.stork.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.model.CompanyConstants;

/**
 * @author Kostas Karkaletsis <k.karkaletsis@gnomon.com.gr>
 */

public final class Util {
    
    private static final String SQL_DELETE_OBJECT = "DELETE FROM serialized_java_objects WHERE userid=?";
    private static final String SQL_SERIALIZE_OBJECT = "INSERT INTO serialized_java_objects(userid, serialized_object) VALUES (?, ?)";
    private static final String SQL_DESERIALIZE_OBJECT = "SELECT serialized_object FROM serialized_java_objects WHERE userid = ?";
    
    private Util() {
    }

    public static boolean isEnabled(long companyId) throws Exception {
        return GetterUtil.get(
                getValue(companyId, StorkPropsKeys.STORK_ENABLED),
                StorkPropsValues.STORK_ENABLED);
    }

    public static boolean isLogoutEnabled(long companyId) throws Exception {
        return GetterUtil.get(
                getValue(companyId,
                        StorkPropsKeys.STORK_LOGOUT_ENABLE),
                StorkPropsValues.STORK_LOGOUT_ENABLE);
    }

    public static String getLogoutUrl(long companyId) throws Exception {
        return GetterUtil.getString(
                getValue(companyId, StorkPropsKeys.STORK_LOGOUT_URL),
                StorkPropsValues.STORK_LOGOUT_URL);
    }

    public static String getHeaderName(long companyId) throws Exception {
        return GetterUtil.getString(
                getValue(companyId, StorkPropsKeys.STORK_HEADER),
                StorkPropsValues.STORK_HEADER);
    }
    
    public static String getEmailHeaderName(long companyId) throws Exception {
        return GetterUtil.getString(
                getValue(companyId, StorkPropsKeys.STORK_HEADER_EMAIL),
                StorkPropsValues.STORK_HEADER_EMAIL);
    }
    
    public static String getFirstnameHeaderName(long companyId) throws Exception {
        return GetterUtil.getString(
                getValue(companyId, StorkPropsKeys.STORK_HEADER_FIRSTNAME),
                StorkPropsValues.STORK_HEADER_FIRSTNAME);
    }
    
    public static String getSurnameHeaderName(long companyId) throws Exception {
        return GetterUtil.getString(
                getValue(companyId, StorkPropsKeys.STORK_HEADER_SURNAME),
                StorkPropsValues.STORK_HEADER_SURNAME);
    }
    
    public static boolean autoCreateUser(long companyId) throws Exception {
        return GetterUtil.get(
                getValue(companyId,
                        StorkPropsKeys.STORK_USER_AUTO_CREATE),
                StorkPropsValues.STORK_USER_AUTO_CREATE);
    }
    
    public static boolean autoUpdateUser(long companyId) throws Exception {
        return GetterUtil.get(
                getValue(companyId,
                        StorkPropsKeys.STORK_USER_AUTO_UPDATE),
                StorkPropsValues.STORK_USER_AUTO_UPDATE);
    }

    public static String getAuthType(long companyId) throws Exception {
        return GetterUtil.getString(
                getValue(companyId, PropsKeys.COMPANY_SECURITY_AUTH_TYPE), CompanyConstants.AUTH_TYPE_EA);
    }

    private static String getValue(long companyId, String key) throws Exception {
        return PrefsPropsUtil.getString(companyId, key);
    }
    
}
