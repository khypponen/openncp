package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Inês Garganta
 */

/**
 Structure of a SMPFile
 */
public class SMPFile {

  private String fileName;
  private File generatedFile;

  private SMPType type;
  private String country;
  private String clientServer;

  
  //ServiceInformation
  private String endpointURI;
  private String issuanceType;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date serviceActivationDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date serviceExpirationDate; 
  private String certificate;
  private String serviceDescription;
  private String technicalContactUrl;
  private String technicalInformationUrl;
  private String extensionContent;
  private MultipartFile extension;
  private FileInputStream certificateFile;
  
  //Redirect
  private String href;
  private String certificateUID;
    

  public SMPFile() {
    super();
  }
   
  public String getEndpointURI() {
    return endpointURI;
  }

  public void setEndpointURI(String EndpointURI) {
    this.endpointURI = EndpointURI;
  }

  public String getTechnicalContactUrl() {
    return technicalContactUrl;
  }

  public void setTechnicalContactUrl(String TechnicalContactUrl) {
    this.technicalContactUrl = TechnicalContactUrl;
  }

  public String getTechnicalInformationUrl() {
    return technicalInformationUrl;
  }

  public void setTechnicalInformationUrl(String TechnicalInformationUrl) {
    this.technicalInformationUrl = TechnicalInformationUrl;
  }

  public String getExtensionContent() {
    return extensionContent;
  }

  public void setExtensionContent(String ExtensionContent) {
    this.extensionContent = ExtensionContent;
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
  
  public Date getServiceActivationDate() {
    return serviceActivationDate;
  }
  public void setServiceActivationDate(Date ServiceActivationDate) {
    this.serviceActivationDate = ServiceActivationDate;
  }
  
  public Date getServiceExpirationDate() {
    return serviceExpirationDate;
  }
  public void setServiceExpirationDate(Date ServiceExpirationDate) {
    this.serviceExpirationDate = ServiceExpirationDate;
  }
    
  public String getCertificate() {
    return certificate;
  }
  public void setCertificate(String Certificate) {
    this.certificate = Certificate;
  }
  
  public String getServiceDescription() {
    return serviceDescription;
  }
  public void setServiceDescription(String ServiceDescription) {
    this.serviceDescription = ServiceDescription;
  }
   
  public MultipartFile getExtension() {
    return extension;
  }
  public void setExtension(MultipartFile Extension) {
    this.extension = Extension;
  }
  
  public FileInputStream getCertificateFile() {
    return certificateFile;
  }
  public void setCertificateFile(FileInputStream certificateFile) {
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
  
  public String getCertificateUID() {
    return certificateUID;
  }

  public void setCertificateUID(String certificateUID) {
    this.certificateUID = certificateUID;
  }   
  
  public String getIssuanceType() {
    return issuanceType;
  }

  public void setIssuanceType(String issuanceType) {
    this.issuanceType = issuanceType;
  }
  
  public String getClientServer() {
    return clientServer;
  }

  public void setClientServer(String clientServer) {
    this.clientServer = clientServer;
  }
}
