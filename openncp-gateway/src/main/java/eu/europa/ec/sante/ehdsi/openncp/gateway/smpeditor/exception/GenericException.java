package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.exception;

public class GenericException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;

  private String code;
  private String message;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
  
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public GenericException(String code, String message) {
    this.code = code;
    this.message = message;
  }
  
}
