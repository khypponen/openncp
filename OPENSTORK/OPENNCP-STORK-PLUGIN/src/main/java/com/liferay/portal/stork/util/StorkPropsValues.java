package com.liferay.portal.stork.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.stork.util.StorkPropsKeys;

/**
 * @author Kostas Karkaletsis <k.karkaletsis@gnomon.com.gr>
 */
public class StorkPropsValues {
    public static final boolean STORK_ENABLED = GetterUtil.getBoolean(PropsUtil.get(StorkPropsKeys.STORK_ENABLED));
    public static final String STORK_HEADER = PropsUtil.get(StorkPropsKeys.STORK_HEADER);
    public static final String STORK_HEADER_EMAIL = PropsUtil.get(StorkPropsKeys.STORK_HEADER_EMAIL);
    public static final String STORK_HEADER_FIRSTNAME = PropsUtil.get(StorkPropsKeys.STORK_HEADER_FIRSTNAME);
    public static final String STORK_HEADER_SURNAME = PropsUtil.get(StorkPropsKeys.STORK_HEADER_SURNAME);
    public static final boolean STORK_USER_AUTO_CREATE = GetterUtil.getBoolean(PropsUtil.get(StorkPropsKeys.STORK_USER_AUTO_CREATE));
    public static final boolean STORK_USER_AUTO_UPDATE = GetterUtil.getBoolean(PropsUtil.get(StorkPropsKeys.STORK_USER_AUTO_UPDATE));
    public static final boolean STORK_LOGOUT_ENABLE = GetterUtil.getBoolean(PropsUtil.get(StorkPropsKeys.STORK_LOGOUT_ENABLE));
    public static final String STORK_LOGOUT_URL = PropsUtil.get(StorkPropsKeys.STORK_LOGOUT_URL);

}
