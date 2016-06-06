package eu.epsos.protocolterminators.ws.server.exception;

public class NIException extends Exception {

    private static final long serialVersionUID = 2148051521948531853L;
    private String code;
    private String codeSystem;
    private String message;

    public NIException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public NIException(String code, String message, String codeSystem) {
        this(code, message);
        this.codeSystem = codeSystem;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getMessage() {
        return message;

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
