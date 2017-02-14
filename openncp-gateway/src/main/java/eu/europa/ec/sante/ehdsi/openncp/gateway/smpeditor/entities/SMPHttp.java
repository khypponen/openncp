package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Inês Garganta
 */
public class SMPHttp {

  public String getSmptype() {
    return smptype;
  }

  public void setSmptype(String smptype) {
    this.smptype = smptype;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public List<String> getReferenceSelected() {
    return referenceSelected;
  }

  public void setReferenceSelected(List<String> referenceSelected) {
    this.referenceSelected = referenceSelected;
  }

  public List<ReferenceCollection> getReferenceCollection() {
    return referenceCollection;
  }

  public void setReferenceCollection(List<ReferenceCollection> referenceCollection) {
    this.referenceCollection = referenceCollection;
  }

  //Upload
  private MultipartFile uploadFile;
  private List<MultipartFile> uploadFiles;
  private List<SMPHttp> allItems;
  private String uploadFileName;
  private int id;
  private String signedServiceMetadataUrl;
  private String serviceGroupUrl;

  //Delete
  private List<ReferenceCollection> referenceCollection;
  private List<String> referenceSelected;
  private String reference;
  private String documentType;
  private List<String> smpDocTypes;
  private String smptype;
  private Countries country;

  private String smpServer;
  private String serverUsername;
  private String serverPassword;
  private int statusCode;
  private String businessCode;
  private String errorDescription;
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

  public List<SMPHttp> getAllItems() {
    return allItems;
  }

  public void setAllItems(List<SMPHttp> allItems) {
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

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  public List<String> getSmpDocTypes() {
    return smpDocTypes;
  }

  public void setSmpDocTypes(List<String> smpDocTypes) {
    this.smpDocTypes = smpDocTypes;
  }

  public Countries getCountry() {
    return country;
  }

  public void setCountry(Countries country) {
    this.country = country;
  }

  public static class ReferenceCollection {
    private String reference;
    private String smptype;
    private int id;

    public ReferenceCollection(String reference, String smptype, int id) {
      this.reference = reference;
      this.smptype = smptype;
      this.id=id;
    }

    public String getReference() {
      return reference;
    }

    public void setReference(String reference) {
      this.reference = reference;
    }

    public String getSmptype() {
      return smptype;
    }

    public void setSmptype(String smptype) {
      this.smptype = smptype;
    }
    
    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }
  }

}
