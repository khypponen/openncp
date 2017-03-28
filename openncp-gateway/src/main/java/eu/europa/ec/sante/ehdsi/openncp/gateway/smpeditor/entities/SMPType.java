package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

/**
 *
 * @author Inês Garganta
 */

public enum SMPType {
  //SPECIFICATION
  VPN_Gateway_Server("VPN Gateway Server"),
  VPN_Gateway_Client("VPN Gateway Client"),
  Country_B_Identity_Provider("Country B Identity Provider"),
  Patient_Identification_Authentication("Patient Identification and Authentication"),
  Provisioning_of_Data_Provide("Provisioning of Data - Provide"),
  Provisioning_of_Data_BPPC("Provisioning of Data - BPPC"),
  Request_of_Data_Fetch("Request of Data - Fetch"),
  Request_of_Data_Query("Request of Data - Query"),
  Request_of_Data_Retrieve("Request of Data - Retrieve"),
  International_Search_Mask("International Search Mask"),
  Redirect("Redirect");

  public static final SMPType[] ALL = {VPN_Gateway_Server, VPN_Gateway_Client, Country_B_Identity_Provider, 
    Patient_Identification_Authentication, Provisioning_of_Data_Provide, Provisioning_of_Data_BPPC, Request_of_Data_Fetch, 
    Request_of_Data_Query, Request_of_Data_Retrieve, International_Search_Mask, Redirect};

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
