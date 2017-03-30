package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

/**
 *
 * @author Inês Garganta
 */

public class SMPFields {

  private SMPFieldProperties uri;
  private SMPFieldProperties issuanceType;
  private SMPFieldProperties serviceActivationDate;
  private SMPFieldProperties serviceExpirationDate;
  private SMPFieldProperties certificate;
  private SMPFieldProperties serviceDescription;
  private SMPFieldProperties technicalContactUrl;
  private SMPFieldProperties technicalInformationUrl;
  private SMPFieldProperties extension;
  private SMPFieldProperties requireBusinessLevelSignature;
  private SMPFieldProperties minimumAuthLevel;

  private SMPFieldProperties redirectHref;
  private SMPFieldProperties certificateUID;

  public SMPFields() {
    super();
  }

  public SMPFieldProperties getUri() {
    return uri;
  }

  public void setUri(SMPFieldProperties uri) {
    this.uri = uri;
  }
  
  public SMPFieldProperties getIssuanceType() {
    return issuanceType;
  }

  public void setIssuanceType(SMPFieldProperties issuanceType) {
    this.issuanceType = issuanceType;
  }

  public SMPFieldProperties getServiceActivationDate() {
    return serviceActivationDate;
  }

  public void setServiceActivationDate(SMPFieldProperties serviceActivationDate) {
    this.serviceActivationDate = serviceActivationDate;
  }

  public SMPFieldProperties getServiceExpirationDate() {
    return serviceExpirationDate;
  }

  public void setServiceExpirationDate(SMPFieldProperties serviceExpirationDate) {
    this.serviceExpirationDate = serviceExpirationDate;
  }

  public SMPFieldProperties getCertificate() {
    return certificate;
  }

  public void setCertificate(SMPFieldProperties certificate) {
    this.certificate = certificate;
  }

  public SMPFieldProperties getServiceDescription() {
    return serviceDescription;
  }

  public void setServiceDescription(SMPFieldProperties serviceDescription) {
    this.serviceDescription = serviceDescription;
  }

  public SMPFieldProperties getTechnicalContactUrl() {
    return technicalContactUrl;
  }

  public void setTechnicalContactUrl(SMPFieldProperties technicalContactUrl) {
    this.technicalContactUrl = technicalContactUrl;
  }

  public SMPFieldProperties getTechnicalInformationUrl() {
    return technicalInformationUrl;
  }

  public void setTechnicalInformationUrl(SMPFieldProperties technicalInformationUrl) {
    this.technicalInformationUrl = technicalInformationUrl;
  }

  public SMPFieldProperties getExtension() {
    return extension;
  }

  public void setExtension(SMPFieldProperties extension) {
    this.extension = extension;
  }

  public SMPFieldProperties getRedirectHref() {
    return redirectHref;
  }

  public void setRedirectHref(SMPFieldProperties redirectHref) {
    this.redirectHref = redirectHref;
  }

  public SMPFieldProperties getCertificateUID() {
    return certificateUID;
  }

  public void setCertificateUID(SMPFieldProperties certificateUID) {
    this.certificateUID = certificateUID;
  }
  
  public SMPFieldProperties getRequireBusinessLevelSignature() {
    return requireBusinessLevelSignature;
  }

  public void setRequireBusinessLevelSignature(SMPFieldProperties requireBusinessLevelSignature) {
    this.requireBusinessLevelSignature = requireBusinessLevelSignature;
  }

  public SMPFieldProperties getMinimumAuthLevel() {
    return minimumAuthLevel;
  }

  public void setMinimumAuthLevel(SMPFieldProperties minimumAuthLevel) {
    this.minimumAuthLevel = minimumAuthLevel;
  }
}
