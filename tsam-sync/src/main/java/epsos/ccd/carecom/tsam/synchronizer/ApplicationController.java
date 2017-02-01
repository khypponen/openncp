package epsos.ccd.carecom.tsam.synchronizer;

import epsos.ccd.carecom.tsam.synchronizer.configuration.Settings;
import epsos.ccd.carecom.tsam.synchronizer.statistics.FileStatisticsGatherer;
import epsos.ccd.carecom.tsam.synchronizer.statistics.LogStatisticsGatherer;
import epsos.ccd.carecom.tsam.synchronizer.statistics.NotifierImpl;
import epsos.ccd.carecom.tsam.synchronizer.statistics.WebMethodCallAuditGatherer;
import epsos.ccd.gnomon.auditmanager.*;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Implements a central application controller.
 */
public final class ApplicationController {

    /**
     * Holds the name of the default system property, whose value points to the location of a log file.
     */
    public static final String DEFAULT_SETTING_LOG_SETTINGS = "java.util.logging.config.file";

    /*
     * Standard date string
     */
    private static final String syncdate = "MMM dd yyyy hh:mm:ss";
    /**
     * This is the main log class used for logging by this application.
     */
    public final static Logger LOG = LoggerFactory.getLogger("TSAM.Synchronizer");

    /**
     * Default text used when shutting the application down.
     */
    private final static String SHUTTING_DOWN_LOG_TEXT = "Cannot continue -> SHUTTING DOWN!";

    /**
     * Holds the notifier for gathering of statistic data.
     */
    public static NotifierImpl STATS = new NotifierImpl();

    /**
     * Holds the number of retries the client should make when an error occurs
     */
    public static int retryNumber;

    static {
        //Level logLevel = Level.parse(Settings.getInstance().getSettingValue("sync.statistics.log"));
        Level logLevel = Level.valueOf(Settings.getInstance().getSettingValue("sync.statistics.log"));

        retryNumber = Settings.getInstance().getRetryNumber();

        //TODO: Check Logs initialization Jerome
        /*if (logLevel != Level. OFF) {
            STATS.addGatherer(new LogStatisticsGatherer(logLevel));
        }*/
        STATS.addGatherer(new LogStatisticsGatherer(logLevel));

        if ("YES".equalsIgnoreCase(Settings.getInstance().getSettingValue("sync.auditmanager.enable"))) {
            STATS.addGatherer(new WebMethodCallAuditGatherer(
                    Settings.getInstance().getSettingValue("sync.auditmanager.facility"),
                    Settings.getInstance().getSettingValue("sync.auditmanager.infoseverity"),
                    Settings.getInstance().getSettingValue("sync.auditmanager.transactionnumber")));

            // Configuring the audit- and configuration managers.
            //DOMConfigurator.configure(System.getProperty("log4j.configuration"));
        }

        String fileName = Settings.getInstance().getSettingValue("sync.statistics.file");
        String onError = Settings.getInstance().getSettingValue("sync.statistics.onerror");
        STATS.addGatherer(new FileStatisticsGatherer(
                fileName,
                (onError != null && onError.equalsIgnoreCase("PRINT")), Level.INFO));
    }

    /**
     * Attempts to write a synchronization date using the <i>Configuration Manager Common Component</i>.
     */
    public static void writeSyncDate() {
        String countryCode = Settings.getInstance().getSettingValue("sync.countrycode");
        ConfigurationManagerService configService = ConfigurationManagerService.getInstance();
        Date currDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(syncdate);
        String date = sdf.format(currDate);
        String result = configService.updateProperty(countryCode + ".tsam.synchronizer.lastsyncdate", date);

        if (result != null && result.equals(date)) {
            LOG.info("Last sync date written to Configuration Manager: " + result);
        } else {
            LOG.warn("Could not verify writing last sync date to Configuration Manager: " + date);
        }
    }

    /**
     * Attempts to get last synchronization date using the <i>Configuration Manager Common Component</i>.
     *
     * @return
     */
    public static Date getLastSyncDate() {
        String countryCode = Settings.getInstance().getSettingValue("sync.countrycode");
        ConfigurationManagerService configService = ConfigurationManagerService.getInstance();
        String result = configService.getProperty(countryCode + ".tsam.synchronizer.lastsyncdate");

        if (result == null || result.length() == 0) {
            LOG.info("No last sync date available in Configuration Manager.");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(syncdate);
            return sdf.parse(result);
        } catch (ParseException e) {
            LOG.warn("Could not parse following string to date: " + result, e);
        }
        return null;
    }

    /**
     * Handles a severe error occurrence.
     *
     * @param e   The exception causing severe error.
     * @param msg Custom message.
     */
    public static void handleSevereError(Throwable e, String msg) {
        LOG.error("Caught " + e.toString() + ": " + msg, e);
        LOG.error(SHUTTING_DOWN_LOG_TEXT);

        // must try to unlock the application before exiting.
        unlockApplication();

        // Report an error to the Audit manager.
        if ("YES".equalsIgnoreCase(Settings.getInstance().getSettingValue("sync.auditmanager.enable"))) {
            AuditService auditService = new AuditService();

            // TODO: When appropriate event log is available change this code.
            EventLog el = EventLog.createEventLogNCPTrustedServiceList(
                    TransactionName.epsosNCPTrustedServiceList,
                    EventActionCode.EXECUTE,
                    Util.convertDateToXMLGregorianCalendar(new Date()),
                    EventOutcomeIndicator.FULL_SUCCESS,
                    "",
                    "",
                    "ERROR",
                    "",
                    new byte[0],
                    "",
                    new byte[0],
                    "",
                    "");

            el.setEventType(EventType.epsosNCPTrustedServiceList);

            auditService.write(el,
                    Settings.getInstance().getSettingValue("sync.auditmanager.facility"),
                    Settings.getInstance().getSettingValue("sync.auditmanager.infoseverity"));
        }

        System.exit(1);
    }

    /**
     * Locks the application by writing a pid file.
     */
    public static void lockApplication() {
        LOG.info("Attempting to lock application.");

        File pidFile = getPidFile();
        String pid = "This file helps the TSAM Synchronizer to determine if an instance is running.";
        try {
            FileWriter fw = new FileWriter(pidFile);
            fw.write(pid);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            LOG.warn("Could not write lock file", e);
            return;
        }
        LOG.info("Lock file written.");
    }

    /**
     * Unlocks the application by deleting the pid file.
     */
    public static void unlockApplication() {
        File pidFile = getPidFile();
        try {
            if (pidFile.exists()) {
                pidFile.delete();
            }
        } catch (SecurityException e) {
            LOG.warn("Could not remove the pid file", e);
        }
        LOG.info("Lock file deleted");
    }

    /**
     * @return <code>true</code> if the application is locked.
     */
    public static boolean isApplicationLocked() {
        File pidFile = getPidFile();
        try {
            return pidFile.exists();
        } catch (SecurityException e) {
            LOG.warn("Could not access the pid file", e);
        }
        return false;
    }

    /**
     * @return The pid file
     */
    private static File getPidFile() {
        String path = System.getProperty("user.dir");
        String seperator = System.getProperty("file.separator");
        File pidFile = new File(path + seperator + "tsam.synchronizer.pid");
        return pidFile;
    }
}
