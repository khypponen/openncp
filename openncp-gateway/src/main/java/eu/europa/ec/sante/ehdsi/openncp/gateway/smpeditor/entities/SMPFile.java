package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

import java.io.File;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 Structure of a SMPFile
 */
public class SMPFile {

  private SMPType type;
  private String country;
  private String EndpointURI;
  @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm")
  private Date ServiceActivationDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date ServiceExpirationDate; 
  private String Certificate;
  private String ServiceDescription;
  private String TechnicalContactUrl;
  private String TechnicalInformationUrl;
  private MultipartFile Extension;
  private MultipartFile certificateFile;
  private String fileName;
  
  private String href;
  private String CertificateUID;
    
  private File generatedFile;
  private String extensionData;

  public SMPFile() {
    super();
  }
  
  public SMPFile(SMPType type, String country,String EndpointURI, Date ServiceActivationDate, 
          Date ServiceExpirationDate,String Certificate, String ServiceDescription, 
          String TechnicalContactUrl, String TechnicalInformationUrl,
          MultipartFile Extension, MultipartFile certificateFile, String fileName) {
    this.type=type;
    this.country=country;
    this.EndpointURI=EndpointURI;
    this.ServiceActivationDate=ServiceActivationDate;
    this.ServiceExpirationDate=ServiceExpirationDate;
    this.ServiceDescription=ServiceDescription;
    this.TechnicalContactUrl=TechnicalContactUrl;
    this.TechnicalInformationUrl=TechnicalInformationUrl;
    this.Extension=Extension;
    this.certificateFile=certificateFile;
    this.fileName = fileName;
  }
  
  public SMPFile(SMPType type, String country, String href, String CertificateUID, String fileName) {
    this.type=type;
    this.country=country;
    this.href=href;
    this.CertificateUID=CertificateUID;
    this.fileName = fileName;
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
  
  public String getEndpointUri() {
    return EndpointURI;
  }
   public void setEndpointUri(String EndpointURI) {
    this.EndpointURI = EndpointURI;
  }
    
  public Date getServiceActivationDate() {
    return ServiceActivationDate;
  }
  public void setServiceActivationDate(Date ServiceActivationDate) {
    this.ServiceActivationDate = ServiceActivationDate;
  }
  
  public Date getServiceExpirationDate() {
    return ServiceExpirationDate;
  }
  public void setServiceExpirationDate(Date ServiceExpirationDate) {
    this.ServiceExpirationDate = ServiceExpirationDate;
  }
    
  public String getCertificate() {
    return Certificate;
  }
  public void setCertificate(String Certificate) {
    this.Certificate = Certificate;
  }
  
  public String getServiceDescription() {
    return ServiceDescription;
  }
  public void setServiceDescription(String ServiceDescription) {
    this.ServiceDescription = ServiceDescription;
  }
  
  public String getTechnicalContact() {
    return TechnicalContactUrl;
  }
  public void setTechnicalContact(String TechnicalContactUrl) {
    this.TechnicalContactUrl = TechnicalContactUrl;
  }
 
  public String getTechnicalInformation() {
    return TechnicalInformationUrl;
  }
  public void setTechnicalInformation(String TechnicalInformationUrl) {
    this.TechnicalInformationUrl = TechnicalInformationUrl;
  }
   
  public MultipartFile getExtension() {
    return Extension;
  }
  public void setExtension(MultipartFile Extension) {
    this.Extension = Extension;
  }
  
  public MultipartFile getCertificateFile() {
    return certificateFile;
  }
  public void setCertificateFile(MultipartFile certificateFile) {
    this.certificateFile = certificateFile;
  }
  
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  
  public File getGeneratedFile() {
    return generatedFile;
  }

  public void setGeneratedFile(File generatedFile) {
    this.generatedFile = generatedFile;
  }
  
  public void setExtensionData(String extensionData){
   this.extensionData=extensionData;
  }
  public String getExtensionData(){
    return extensionData;
  }
  
  
  public String getRedirectHref() {
    return href;
  }

  public void setRedirectHref(String href) {
    this.href = href;
  }

  public String getCertificateUID() {
    return CertificateUID;
  }

  public void setCertificateUID(String CertificateUID) {
    this.CertificateUID = CertificateUID;
  }    
}
