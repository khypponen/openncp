package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Inês Garganta
 */

public class SMPUpload {

  private MultipartFile uploadFile;
  private List<MultipartFile> uploadFiles;
  private List<SMPUpload> allItems;
  private String uploadFileName;
  private String smpServer;
  private String serverUsername;
  private String serverPassword;
  private int statusCode;
  private String businessCode;
  private String errorDescription;
  private int id;
  private String signedServiceMetadataUrl;
  private String serviceGroupUrl; 
  private Alert alert;
  private Alert status;

  
  public String getServiceGroupUrl() {
    return serviceGroupUrl;
  }

  public void setServiceGroupUrl(String serviceGroupUrl) {
    this.serviceGroupUrl = serviceGroupUrl;
  }

  public String getSignedServiceMetadataUrl() {
    return signedServiceMetadataUrl;
  }

  public void setSignedServiceMetadataUrl(String signedServiceMetadataUrl) {
    this.signedServiceMetadataUrl = signedServiceMetadataUrl;
  }
  
  
  public MultipartFile getUploadFile() {
    return uploadFile;
  }

  public void setUploadFile(MultipartFile uploadFile) {
    this.uploadFile = uploadFile;
  }

  public List<MultipartFile> getUploadFiles() {
    return uploadFiles;
  }

  public void setUploadFiles(List<MultipartFile> uploadFiles) {
    this.uploadFiles = uploadFiles;
  }

  public String getUploadFileName() {
    return uploadFileName;
  }

  public void setUploadFileName(String uploadFileName) {
    this.uploadFileName = uploadFileName;
  }

  public String getSmpServer() {
    return smpServer;
  }

  public void setSmpServer(String smpServer) {
    this.smpServer = smpServer;
  }

  public String getServerUsername() {
    return serverUsername;
  }

  public void setServerUsername(String serverUsername) {
    this.serverUsername = serverUsername;
  }

  public String getServerPassword() {
    return serverPassword;
  }

  public void setServerPassword(String serverPassword) {
    this.serverPassword = serverPassword;
  }
  
  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
  
  public List<SMPUpload> getAllItems() {
    return allItems;
  }

  public void setAllItems(List<SMPUpload> allItems) {
    this.allItems = allItems;
  }
  
  public String getBusinessCode() {
    return businessCode;
  }

  public void setBusinessCode(String businessCode) {
    this.businessCode = businessCode;
  }
  
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  
  public Alert getAlert() {
    return alert;
  }

  public void setAlert(Alert alert) {
    this.alert = alert;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }
  
  public Alert getStatus() {
    return status;
  }

  public void setStatus(Alert status) {
    this.status = status;
  }
  
}
