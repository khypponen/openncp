package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

import java.io.File;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 Structure of a SMPFile
 */
public class SMPFile {

  private String fileName;
  private File generatedFile;
  private MultipartFile updateFile;

  private SMPType type;
  private String country;
  private String DocumentIdentifier;
  
  //ServiceInformation
  private String EndpointURI;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date ServiceActivationDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date ServiceExpirationDate; 
  private String Certificate;
  private String ServiceDescription;
  private String TechnicalContactUrl;
  private String TechnicalInformationUrl;
  private String ExtensionContent;
  private MultipartFile Extension;
  private MultipartFile certificateFile;
  
  //Redirect
  private String href;
  private String CertificateUID;
    

  public SMPFile() {
    super();
  }
   
  public MultipartFile getUpdateFile() {
    return updateFile;
  }

  public void setUpdateFile(MultipartFile updateFile) {
    this.updateFile = updateFile;
  }

  public String getDocumentIdentifier() {
    return DocumentIdentifier;
  }

  public void setDocumentIdentifier(String DocumentIdentifier) {
    this.DocumentIdentifier = DocumentIdentifier;
  }

  public String getEndpointURI() {
    return EndpointURI;
  }

  public void setEndpointURI(String EndpointURI) {
    this.EndpointURI = EndpointURI;
  }

  public String getTechnicalContactUrl() {
    return TechnicalContactUrl;
  }

  public void setTechnicalContactUrl(String TechnicalContactUrl) {
    this.TechnicalContactUrl = TechnicalContactUrl;
  }

  public String getTechnicalInformationUrl() {
    return TechnicalInformationUrl;
  }

  public void setTechnicalInformationUrl(String TechnicalInformationUrl) {
    this.TechnicalInformationUrl = TechnicalInformationUrl;
  }

  public String getExtensionContent() {
    return ExtensionContent;
  }

  public void setExtensionContent(String ExtensionContent) {
    this.ExtensionContent = ExtensionContent;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
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
