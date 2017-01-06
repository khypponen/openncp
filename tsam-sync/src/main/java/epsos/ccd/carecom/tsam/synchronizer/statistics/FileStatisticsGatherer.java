package epsos.ccd.carecom.tsam.synchronizer.statistics;

import epsos.ccd.carecom.tsam.synchronizer.ApplicationController;
import org.slf4j.event.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Implements a gatherer that maintains all statistics records and at the end prints them to
 * some specified print stream which can either be a file or <code>System.out</code>.
 */
public class FileStatisticsGatherer implements Gatherer {

    private List<ActionRecord> records;
    private ActionRecord current;
    private String fileName;
    private boolean handleError;
    private Level logLevel;
    private long accumulatedDuration;

    /**
     * Default constructor.
     *
     * @param fileName    Name of the file to write to.
     * @param handleError Indicates if an error should be handled or ignored.
     */
    public FileStatisticsGatherer(String fileName, boolean handleError, Level logLevel) {
        this.records = new LinkedList<ActionRecord>();
        this.fileName = fileName;
        this.handleError = handleError;
        this.logLevel = logLevel;
        this.accumulatedDuration = 0;
    }

    public void registerActionStart(String webMethodName,
                                    Map<String, Long> filters) {
        this.current = new ActionRecord();
        this.current.setActionName(webMethodName);
        this.current.setFilters(filters);
        this.current.setStartTime(new Date());
    }

    public void registerActionEnd(String actionName, int numberOfRecords) {
        this.current.setEndTime(new Date());
        this.current.setNumberOfRecordsFetched(numberOfRecords);
        this.records.add(this.current);

        this.accumulatedDuration += this.current.getEndTime().getTime() - this.current.getStartTime().getTime();
    }

    public void registerGatheringComplete() {
        ApplicationController.LOG.info("Accumulated Webservice duration: " + new TimeSpan(this.accumulatedDuration));

        if (this.fileName != null && this.fileName.length() > 0) {
            File file = new File(this.fileName);
            try {
                if (file.exists()) {
                    file.delete();
                }
            } catch (SecurityException e) {
                if (this.handleError) {
                    printToStream(System.out);
                }
                // Else ignore
            }
            try {
                PrintStream ps = new PrintStream(file);
                printToStream(ps);
                ps.close();
            } catch (FileNotFoundException e) {
                if (this.handleError) {
                    printToStream(System.out);
                }
                // Else ignore
            }
        } else {
            printToStream(System.out);
        }
    }

    private void printToStream(PrintStream ps) {
        for (ActionRecord record : this.records) {
            ps.println(record);
        }
    }
}
