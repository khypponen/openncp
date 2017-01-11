package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

import java.io.File;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

public class SMPFileUpdate {

  private SMPType type;
  private String country;
  private String fileName;
  private File generatedFile;
  private MultipartFile updateFile;
  
  private String EndpointURI;
  private Date ServiceActivationDate;
  private Date ServiceExpirationDate;
  private String Certificate;
  private String ServiceDescription;
  private String TechnicalContactUrl;
  private String TechnicalInformationUrl;
  private String Extension;
  
  private String href;
  private String CertificateUID;

  public SMPType getType() {
    return type;
  }

  public void setType(SMPType type) {
    this.type = type;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
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

  public MultipartFile getUpdateFile() {
    return updateFile;
  }

  public void setUpdateFile(MultipartFile updateFile) {
    this.updateFile = updateFile;
  }

  
  public String getEndpointURI() {
    return EndpointURI;
  }
  public void setEndpointURI(String EndpointURI) {
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

  public String getExtension() {
    return Extension;
  }
  public void setExtension(String Extension) {
    this.Extension = Extension;
  }

  public String getHref() {
    return href;
  }
  public void setHref(String href) {
    this.href = href;
  }

  public String getCertificateUID() {
    return CertificateUID;
  }
  public void setCertificateUID(String CertificateUID) {
    this.CertificateUID = CertificateUID;
  }

}
