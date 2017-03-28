package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

/**
 *
 * @author Inês Garganta
 */

public class Alert {

  private String message;  
  private fontColor color;
  private alertType type;
  private String backgroundColor;
  
  public Alert(String message, alertType type) {
    this.message = message;
    this.type = type;
  }
  
  public Alert(String message, fontColor color, String backgroundColor) {
    this.message = message;
    this.color = color;
    this.backgroundColor = backgroundColor;
  }
  
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
  
  public fontColor getColor() {
    return color;
  }
  
  public alertType getType() {
    return type;
  }
  
  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }
  
 public enum alertType {danger, warning, success, info};
 public enum fontColor {green, red};
}
