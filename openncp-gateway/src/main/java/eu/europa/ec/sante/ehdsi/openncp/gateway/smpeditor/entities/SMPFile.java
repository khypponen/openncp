package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

public class SMPFile {

  private SMPType type;
  private String country;
  private String endpoint_uri;
  private Date service_activation_date;
  private Date service_expiration_date;
  private String certificate;
  private String service_description;
  private String technical_contact;
  private String technical_information;
  private String extension;
  private String destination_smp;
  

  public SMPFile() {
    super();
  }

  public SMPFile(final SMPType type) {
    this.type = type;
  }
  
  public SMPFile(String country,String endpoint_uri, Date service_activation_date, Date service_expiration_date, 
          String certificate, String service_description, String technical_contact, String technical_information,
          String extension) {
    this.country=country;
    this.endpoint_uri=endpoint_uri;
    this.service_activation_date=service_activation_date;
    this.service_expiration_date=service_expiration_date;
    this.certificate=certificate;
    this.service_description=service_description;
    this.technical_contact=technical_contact;
    this.technical_information=technical_information;
    this.extension=extension;
  }
  
  public SMPFile(String destination_smp, String certificate, String extension) {
    this.destination_smp=destination_smp;
    this.certificate=certificate;
    this.extension=extension;
  }
  

  public SMPType getType() {
    return this.type;
  }
  public void setType(SMPType type) {
    this.type = type;
  }
  
  public String getCountry(){
    return this.country;
  }
  public void setCountry(String country){
    this.country = country;
  }
  
  public String getEndpoint_uri() {
    return endpoint_uri;
  }
   public void setEndpoint_uri(String endpoint_uri) {
    this.endpoint_uri = endpoint_uri;
  }
    
  public Date getService_activation_date() {
    return service_activation_date;
  }
  public void setService_activation_date(Date service_activation_date) {
    this.service_activation_date = service_activation_date;
  }
  
  public Date getService_expiration_date() {
    return service_expiration_date;
  }
  public void setService_expiration_date(Date service_expiration_date) {
    this.service_expiration_date = service_expiration_date;
  }
  
  public String getCertificate() {
    return certificate;
  }
  public void setCertificate(String certificate) {
    this.certificate = certificate;
  }
  
  public String getService_description() {
    return service_description;
  }
  public void setService_description(String service_description) {
    this.service_description = service_description;
  }
  
  public String getTechnical_contact() {
    return technical_contact;
  }
  public void setTechnical_contact(String technical_contact) {
    this.technical_contact = technical_contact;
  }
 
  public String getTechnical_information() {
    return technical_information;
  }
  public void setTechnical_information(String technical_information) {
    this.technical_information = technical_information;
  }
   
  public String getExtension() {
    return extension;
  }
  public void setExtension(String extension) {
    this.extension = extension;
  }
  
  public String getDestination_smp() {
    return destination_smp;
  }
  public void setDestination_smp(String destination_smp) {
    this.destination_smp = destination_smp;
  }

  @Override
  public String toString() {
    return "SMPFile " + "[ SMPType=" + this.type + "]";
  }
  
  public String toString2() {
    return "SMPFile " + "[ endpoint_uri=" + this.endpoint_uri +", service_activation_date" + 
            this.service_activation_date + ", service_expiration_date" + this.service_expiration_date + 
            ", certificate" + this.certificate + ", service_description" + this.service_description +
            ", technical_contact" + this.technical_contact + ", technical_information" +
            this.technical_information + ", extension" + this.extension + "]";
  }
  
  public String toString3() {
    return "SMPFile " + "[ destination_smp=" + this.destination_smp + ", certificate" + this.certificate  + 
            ", extension" + this.extension +  "]";
  }
    
}
