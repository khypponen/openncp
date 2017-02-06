package com.gnomon.epsos;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.portlet.*;

/**
 *
 */
public class ConfigurationAction extends DefaultConfigurationAction {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationAction.class.getName());

    /**
     *
     * @param request
     * @param key
     * @return
     */
    public static String getConfigParam(PortletRequest request, String key) {

        PortletPreferences prefs = request.getPreferences();
        return prefs.getValue(key, "");

    }

    /**
     *
     * @param portletConfig
     * @param actionRequest
     * @param actionResponse
     * @throws Exception
     */
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        String client_connector_url = ParamUtil.getString(actionRequest, "client_connector_url");
        String check_permissions = ParamUtil.getString(actionRequest, "check_permissions");
        String portletResource = ParamUtil.getString(actionRequest, "portletResource");
        PortletPreferences prefs = null;
        if ((portletResource != null) && (!portletResource.equals(""))) {
            prefs = PortletPreferencesFactoryUtil.getPortletSetup(
                    actionRequest, portletResource);
        }
        //check.permissions.for.buttons=true

        prefs.setValue("client_connector_url", client_connector_url);
        prefs.setValue("check_permissions", check_permissions);

        prefs.store();

        super.processAction(portletConfig, actionRequest, actionResponse);
    }

    /**
     *
     * @param portletConfig
     * @param renderRequest
     * @param renderResponse
     * @return
     * @throws Exception
     */
    public String render(PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {

        //PortletConfig selPortletConfig = getSelPortletConfig(renderRequest);
        PortletConfig selPortletConfig = getSelPortletConfig(PortalUtil.getHttpServletRequest(renderRequest));
        String configTemplate = selPortletConfig.getInitParameter(
                "config-template");

        if (Validator.isNotNull(configTemplate)) {
            return configTemplate;
        }

        String configJSP = selPortletConfig.getInitParameter("config-jsp");

        if (Validator.isNotNull(configJSP)) {
            return configJSP;
        }
        return "/html/configuration.jsp";
    }
}
