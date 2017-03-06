package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Inês Garganta
 */
public class SMPHttp {

  //Upload
  private MultipartFile uploadFile;
  private List<MultipartFile> uploadFiles;
  private List<SMPHttp> allItems;
  private String uploadFileName;
  private int id;
  private String serviceGroupUrl;

  //Delete
  private List<ReferenceCollection> referenceCollection;
  private List<String> referenceSelected;
  private String reference;
  private String documentType;
  private String smptype;
  private Countries country;

  private String signedServiceMetadataUrl;
  private String smpURI;
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

  public Countries getCountry() {
    return country;
  }

  public void setCountry(Countries country) {
    this.country = country;
  }
  
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
  
  public String getSmpURI() {
    return smpURI;
  }

  public void setSmpURI(String smpURI) {
    this.smpURI = smpURI;
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
