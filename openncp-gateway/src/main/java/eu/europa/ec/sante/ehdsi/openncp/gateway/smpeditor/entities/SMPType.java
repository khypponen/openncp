package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

public enum SMPType {

  VPN_GATEWAY_A("VPN Gateway A"),
  VPN_GATEWAY_B("VPN Gateway B"),
  PATIENT_SERVICE("Patient Service"),
  IDENTITY_PROVIDER("Identity Provider"),
  PATIENT_ID_SERVICE("Patient Identification Service"),
  CONSENT_SERVICE("Consent Service"),
  ORDER_SERVICE("Order Service"),
  DISPENSATION_SERVICE("Dispensation Service"),
  REDIRECT("Redirect"),
  INT_SEARCHMASK("International Search Mask");

  public static final SMPType[] ALL = {VPN_GATEWAY_A, VPN_GATEWAY_B, PATIENT_SERVICE, IDENTITY_PROVIDER, PATIENT_ID_SERVICE, CONSENT_SERVICE, ORDER_SERVICE, DISPENSATION_SERVICE, REDIRECT, INT_SEARCHMASK};

  private final String description;

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
