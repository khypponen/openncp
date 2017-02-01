package epsos.ccd.carecom.tsam.synchronizer.statistics;

import epsos.ccd.carecom.tsam.synchronizer.ApplicationController;
import epsos.ccd.carecom.tsam.synchronizer.Util;
import epsos.ccd.gnomon.auditmanager.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements a gatherer that counts the number of each entity that is persisted during synchronization.
 */
public class EntityCounterGatherer implements Gatherer {

    /**
     * Holds the name of the code system entity.
     */
    public static final String ENTITY_CODE_SYSTEM = "CodeSystem";

    /**
     * Holds the name of the code system version entity.
     */
    public static final String ENTITY_CODE_SYSTEM_VERSION = "CodeSystemVersion";

    /**
     * Holds the name of the value set entity.
     */
    public static final String ENTITY_VALUE_SET = "ValueSet";

    /**
     * Holds the name of the code system version version entity.
     */
    public static final String ENTITY_VALUE_SET_VERSION = "ValueSetVersion";

    /**
     * Holds the name of the code system concept entity.
     */
    public static final String ENTITY_CONCEPT = "CodeSystemConcept";

    /**
     * Holds the name of the designation entity.
     */
    public static final String ENTITY_DESIGNATION = "Designation";

    /**
     * Holds the name of the transcoding association entity.
     */
    public static final String ENTITY_TRANSCODING = "TranscodingAssociation";

    private Map<String, Integer> countMap;

    private AuditService auditService;

    private String facility;
    private String severity;
    private String transactionNumber;

    /**
     * Default constructor.
     *
     * @param facility          Facility
     * @param severity          Severity
     * @param transactionNumber Transaction number.
     */
    public EntityCounterGatherer(String facility, String severity, String transactionNumber) {
        this.facility = facility;
        this.severity = severity;
        this.transactionNumber = transactionNumber;

        this.auditService = new AuditService();

        this.countMap = new HashMap<String, Integer>();

        this.countMap.put(EntityCounterGatherer.ENTITY_CODE_SYSTEM, Integer.valueOf(0));
        this.countMap.put(EntityCounterGatherer.ENTITY_CODE_SYSTEM_VERSION, Integer.valueOf(0));
        this.countMap.put(EntityCounterGatherer.ENTITY_CONCEPT, Integer.valueOf(0));
        this.countMap.put(EntityCounterGatherer.ENTITY_VALUE_SET, Integer.valueOf(0));
        this.countMap.put(EntityCounterGatherer.ENTITY_VALUE_SET_VERSION, Integer.valueOf(0));
        this.countMap.put(EntityCounterGatherer.ENTITY_DESIGNATION, Integer.valueOf(0));
        this.countMap.put(EntityCounterGatherer.ENTITY_TRANSCODING, Integer.valueOf(0));
    }

    public void registerActionStart(String actionName, Map<String, Long> data) {
        // Do nothing here
    }

    public void registerActionEnd(String actionName, int numberOfRecords) {
        if (actionName == null || actionName.trim().length() == 0) {
            return;
        }
        if (this.countMap.containsKey(actionName) && numberOfRecords > 0) {
            int count = this.countMap.get(actionName).intValue();
            count += numberOfRecords;
            this.countMap.put(actionName, Integer.valueOf(count));
        }
    }

    public void registerGatheringComplete() {
        for (Map.Entry<String, Integer> actionRec : this.countMap.entrySet()) {
            EventLog ev = createEventLog(actionRec.getKey());
            if (ev == null) {
                continue;
            }
            // TODO: This is definitely not correct, but we have no option.
            ev.setPS_PatricipantObjectID(String.valueOf(actionRec.getValue()));
            writeEventLog(ev);
        }
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
