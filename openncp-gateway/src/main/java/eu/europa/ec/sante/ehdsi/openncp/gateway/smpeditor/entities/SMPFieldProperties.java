package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

/**
 *
 * @author Inês Garganta
 */

public class SMPFieldProperties {

  private boolean enable;
  private boolean mandatory;

  public SMPFieldProperties() {
    super();
  }
  
  public boolean isEnable() {
    return enable;
  }
  public void setEnable(boolean enable) {
    this.enable = enable;
  }
  
  public boolean isMandatory() {
    return mandatory;
  }
  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

}
