package com.gnomon.epsos;

import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import epsos.ccd.posam.tm.service.ITransformationService;
import epsos.openncp.protocolterminator.ClientConnectorConsumer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tr.com.srdc.epsos.util.Constants;

@WebListener
public class MyServletContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger("MyServletContextListener");
    private ServletContext context = null;
    private static String runningMode;
    private static String encryptionKey;
    private static ITransformationService tService;
    private static ClientConnectorConsumer clientConectorConsumer;

    // Public constructor is required by servlet spec
    public MyServletContextListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Context Listener > Initialized");
        try {
            runningMode = GetterUtil.get(PropsUtil.get("running.mode"), "live");
            encryptionKey = PropsUtil.get("ehealthpass.encryption.key");

        } catch (Exception e) {
            runningMode = "live";
            log.error(ExceptionUtils.getStackTrace(e));
        }

        log.info("Initiating OpenNCP Portal");
        try {
            System.setProperty("javax.net.ssl.keyStore", Constants.NCP_SIG_KEYSTORE_PATH);
            System.setProperty("javax.net.ssl.keyStorePassword", Constants.NCP_SIG_KEYSTORE_PASSWORD);
            System.setProperty("javax.net.ssl.key.alias", Constants.NCP_SIG_PRIVATEKEY_ALIAS);
            System.setProperty("javax.net.ssl.privateKeyPassword", Constants.NCP_SIG_PRIVATEKEY_PASSWORD);
            System.setProperty("javax.net.ssl.trustStore", Constants.TRUSTSTORE_PATH);
            System.setProperty("javax.net.ssl.trustStorePassword", Constants.TRUSTSTORE_PASSWORD);
        } catch (Exception e) {
            log.error("#### ERROR INITIALIZING KEYSTORE/TRUSTSTORE ####");
        }

        log.info("Initializing TM component");
        try {
            ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("ctx_tm.xml");
            tService = (ITransformationService) applicationContext.getBean(ITransformationService.class.getName());
        } catch (Exception e) {
            log.error("#### ERROR INITIALIZING TM ####", e);
        }
        try {
            String serviceUrl = EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);			//serviceUrl = LiferayUtils.getFromPrefs("client_connector_url");
            log.info("SERVICE URL IS " + serviceUrl);
            clientConectorConsumer = new ClientConnectorConsumer(serviceUrl);
        } catch (Exception e) {
            log.error("ERROR INITIALIZING CLIENT CONNECTOR PROXY");
        }

        log.info("Running Mode: " + runningMode);
    }

    public void contextDestroyed(ServletContextEvent sce) {

        this.context = null;
    }

    public static ITransformationService getTransformationService() {
        return tService;
    }

    public static ClientConnectorConsumer getClientConnectorConsumer() {
        return clientConectorConsumer;
    }

    public static String getRunningMode() {
        return runningMode;
    }

    public static void setRunningMode(String runningMode) {
        MyServletContextListener.runningMode = runningMode;
    }

    public static String getEncryptionKey() {
        return encryptionKey;
    }

    public static void setEncryptionKey(String encryptionKey) {
        MyServletContextListener.encryptionKey = encryptionKey;
    }
}
