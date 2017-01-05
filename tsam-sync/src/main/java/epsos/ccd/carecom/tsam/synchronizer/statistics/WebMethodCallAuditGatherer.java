package epsos.ccd.carecom.tsam.synchronizer.statistics;

import epsos.ccd.carecom.tsam.synchronizer.ApplicationController;
import epsos.ccd.carecom.tsam.synchronizer.Util;
import epsos.ccd.gnomon.auditmanager.*;

import java.util.Date;
import java.util.Map;

/**
 * Implements a gatherer for the number of times each web method is called during a synchronization.
 */
public class WebMethodCallAuditGatherer implements Gatherer {

    /**
     * Holds the name of the web method that returns the code systems.
     */
    public static final String WEB_METHOD_CALL_CODE_SYSTEMS = "listCodeSystems";

    /**
     * Holds the name of the web method that returns the code system concepts.
     */
    public static final String WEB_METHOD_CALL_CODE_SYSTEM_CONCEPTS = "listCodeSystemConcepts";

    /**
     * Holds the name of the web method that returns the value sets.
     */
    public static final String WEB_METHOD_CALL_VALUE_SETS = "listValueSets";

    /**
     * Holds the name of the web method that returns the designations.
     */
    public static final String WEB_METHOD_CALL_DESIGNATIONS = "listDesignations";

    /**
     * Holds the name of the web method that returns the transcoding associations.
     */
    public static final String WEB_METHOD_CALL_TRANSCODINGS = "listTranscodings";

    private AuditService auditService;
    private EventLog eventLog;

    private String facility;
    private String severity;
    private String transactionNumber;

    /**
     * Default constructor.
     *
     * @param facility          Facility
     * @param severity          Severity
     * @param transactionNumber Transaction number
     */
    public WebMethodCallAuditGatherer(String facility, String severity, String transactionNumber) {
        this.facility = facility;
        this.severity = severity;
        this.transactionNumber = transactionNumber;

        this.auditService = new AuditService();

        this.eventLog = createEventLog("StartSync");
        writeEventLog(this.eventLog);
    }

    public void registerActionStart(String actionName, Map<String, Long> data) {
        this.eventLog = createEventLog(actionName);
    }

    public void registerActionEnd(String actionName, int numberOfRecords) {
        if (this.eventLog == null) {
            return;
        }
        writeEventLog(this.eventLog);
    }

    public void registerGatheringComplete() {
        this.eventLog = createEventLog("EndSync");
        writeEventLog(this.eventLog);
    }

    private void writeEventLog(EventLog ev) {
        this.auditService.write(ev, this.facility, this.severity);
    }

    private EventLog createEventLog(String actionName) {
        try {
            // TODO: This is definitely not correct, but we have no option.
            EventLog el = EventLog.createEventLogNCPTrustedServiceList(
                    TransactionName.epsosNCPTrustedServiceList,
                    EventActionCode.EXECUTE,
                    Util.convertDateToXMLGregorianCalendar(new Date()),
                    EventOutcomeIndicator.FULL_SUCCESS,
                    "",
                    "",
                    actionName,
                    "",
                    new byte[0],
                    "",
                    new byte[0],
                    "",
                    "");
            el.setEventType(EventType.epsosNCPTrustedServiceList);

            return el;
        } catch (Throwable e) {
            ApplicationController.LOG.warn("Could not create audit event log", e);
        }
        return null;
    }
}
