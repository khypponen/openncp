package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

/**
 *
 * @author Inês Garganta
 */

public enum SMPType {

  VPN_Gateway_A("VPN Gateway A"),
  VPN_Gateway_B("VPN Gateway B"),
  Patient_Service("Patient Service"),
  Identity_Provider("Identity Provider"),
  Patient_Identification_Service("Patient Identification Service"),
  Consent_Service_Put("Consent Service Put"),
  Consent_Service_Discard("Consent Service Discard"),
  Order_Service("Order Service"),
  Dispensation_Service_Initialize("Dispensation Service Initialize"),
  Dispensation_Service_Discard("Dispensation Service Discard"),
  Redirect("Redirect"),
  International_Search_Mask("International Search Mask");

  public static final SMPType[] ALL = {VPN_Gateway_A, VPN_Gateway_B, Patient_Service, Identity_Provider, 
    Patient_Identification_Service, Consent_Service_Put, Consent_Service_Discard, Order_Service, 
    Dispensation_Service_Initialize, Dispensation_Service_Discard, Redirect, International_Search_Mask};

  private final String description;
  
  public static SMPType[] getALL() {
    return ALL;
  }

  private SMPType(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }

  @Override
  public String toString() {
    return getDescription();
  }
}
