package com.gnomon.epsos.filter;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import java.util.Hashtable;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.owasp.esapi.ESAPI;

public final class RequestWrapper extends HttpServletRequestWrapper {

    private Hashtable<Long, Group> portalGroupsHash = null;

    public RequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
        portalGroupsHash = new Hashtable<Long, Group>();
    }

    private Group getGroup() {
        Group retGroup = null;
        try {
            ThemeDisplay themeDisplay
                    = (ThemeDisplay) (HttpServletRequest) getRequest().getAttribute(WebKeys.THEME_DISPLAY);
//            long groupId = PortalUtil.getPortletGroupId((HttpServletRequest) getRequest());
            long groupId = themeDisplay.getLayout().getGroupId();
            retGroup = portalGroupsHash.get(groupId);
            if (retGroup == null) {
                retGroup = GroupLocalServiceUtil.getGroup(groupId);
                portalGroupsHash.put(groupId, retGroup);
            }
        } catch (Exception ex) {
        }
        return retGroup;
    }

    private boolean isGuestGroup() {
        boolean retFlag = true;

        Group retGroup = getGroup();
        if (retGroup != null) {
            retFlag = retGroup.getName().equals(GroupConstants.GUEST);
        }
        return retFlag;
    }

    public String[] getParameterValues(String parameter) {

        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        boolean isGuestGroup = isGuestGroup();

        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i], isGuestGroup);
        }
        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        boolean isGuestGroup = isGuestGroup();
        return cleanXSS(value, isGuestGroup);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        boolean isGuestGroup = isGuestGroup();
        if (value == null) {
            return null;
        }
        return cleanXSS(value, isGuestGroup);

    }

    private String cleanXSS(String value, boolean performEscape) {
        //		String value1=value;
		/*
         * Disabled <> replace due to OUC-323
         * value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
         */
        if (performEscape) {
            value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
            //value = value.replaceAll("'", "&#39;");
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        }
        return value;
    }

    private String stripXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            value = value.replaceAll("", "");

            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid anything in a src='...' type of expression
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }
}
