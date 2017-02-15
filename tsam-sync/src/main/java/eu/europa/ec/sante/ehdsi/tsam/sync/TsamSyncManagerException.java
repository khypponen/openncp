package eu.europa.ec.sante.ehdsi.tsam.sync;

public class TsamSyncManagerException extends RuntimeException {

    public TsamSyncManagerException(String message) {
        super(message);
    }

    public TsamSyncManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
