package eu.europa.ec.sante.ehdsi.tsam.sync.db;

public class DatabaseToolException extends RuntimeException {

    public DatabaseToolException(String message) {
        super(message);
    }

    public DatabaseToolException(String message, Throwable cause) {
        super(message, cause);
    }
}
