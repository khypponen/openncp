package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFieldProperties;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFile;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFileOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ReadSMPProperties {

  @Autowired
  private Environment env;

  private String type;
  private SMPFieldProperties uri;
  private SMPFieldProperties serviceActDate;
  private SMPFieldProperties serviceExpDate;
  private SMPFieldProperties certificate;
  private SMPFieldProperties serviceDesc;
  private SMPFieldProperties techContact;
  private SMPFieldProperties techInformation;
  private SMPFieldProperties extension;
  private SMPFieldProperties redirectHref;
  private SMPFieldProperties certificateUID;
  
  /**
   * Read the properties for the html forms from the smpeditor.properties file
   * @param smpfile
   */
  public void readProperties(SMPFile smpfile) {
    type = env.getProperty("type." + smpfile.getType().name()); //smpeditor.properties

    Boolean uriEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".uri.enable"));
    Boolean uriMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".uri.mandatory"));
    uri = new SMPFieldProperties();
    uri.setEnable(uriEnable);
    uri.setMandatory(uriMandatory);

    Boolean serviceActDateEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceActivationDate.enable"));
    Boolean serviceActDateMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceActivationDate.mandatory"));
    serviceActDate = new SMPFieldProperties();
    serviceActDate.setEnable(serviceActDateEnable);
    serviceActDate.setMandatory(serviceActDateMandatory);

    Boolean serviceExpDateEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceExpirationDate.enable"));
    Boolean serviceExpDateMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceExpirationDate.mandatory"));
    serviceExpDate = new SMPFieldProperties();
    serviceExpDate.setEnable(serviceExpDateEnable);
    serviceExpDate.setMandatory(serviceExpDateMandatory);

    Boolean certificateEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificate.enable"));
    Boolean certificateMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificate.mandatory"));
    certificate = new SMPFieldProperties();
    certificate.setEnable(certificateEnable);
    certificate.setMandatory(certificateMandatory);

    Boolean serviceDescEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceDescription.enable"));
    Boolean serviceDescMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceDescription.mandatory"));
    serviceDesc = new SMPFieldProperties();
    serviceDesc.setEnable(serviceDescEnable);
    serviceDesc.setMandatory(serviceDescMandatory);

    Boolean techContactEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalContactUrl.enable"));
    Boolean techContactMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalContactUrl.mandatory"));
    techContact = new SMPFieldProperties();
    techContact.setEnable(techContactEnable);
    techContact.setMandatory(techContactMandatory);

    Boolean techInformationEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalInformationUrl.enable"));
    Boolean techInformationMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalInformationUrl.mandatory"));
    techInformation = new SMPFieldProperties();
    techInformation.setEnable(techInformationEnable);
    techInformation.setMandatory(techInformationMandatory);

    Boolean extensionEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".extension.enable"));
    Boolean extensionMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".extension.mandatory"));
    extension = new SMPFieldProperties();
    extension.setEnable(extensionEnable);
    extension.setMandatory(extensionMandatory);

    Boolean redirectHrefEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".redirectHref.enable"));
    Boolean redirectHrefMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".redirectHref.mandatory"));
    redirectHref = new SMPFieldProperties();
    redirectHref.setEnable(redirectHrefEnable);
    redirectHref.setMandatory(redirectHrefMandatory);

    Boolean certificateUIDEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificateUID.enable"));
    Boolean certificateUIDMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificateUID.mandatory"));
    certificateUID = new SMPFieldProperties();
    certificateUID.setEnable(certificateUIDEnable);
    certificateUID.setMandatory(certificateUIDMandatory);
  }
  
  /**
   * Read the properties for the html forms from the smpeditor.properties file
   * @param smpfile
   */
  public void readProperties(SMPFileOps smpfile) {
    
    Boolean uriEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".uri.enable"));
    Boolean uriMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".uri.mandatory"));
    uri = new SMPFieldProperties();
    uri.setEnable(uriEnable);
    uri.setMandatory(uriMandatory);

    Boolean serviceActDateEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceActivationDate.enable"));
    Boolean serviceActDateMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceActivationDate.mandatory"));
    serviceActDate = new SMPFieldProperties();
    serviceActDate.setEnable(serviceActDateEnable);
    serviceActDate.setMandatory(serviceActDateMandatory);

    Boolean serviceExpDateEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceExpirationDate.enable"));
    Boolean serviceExpDateMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceExpirationDate.mandatory"));
    serviceExpDate = new SMPFieldProperties();
    serviceExpDate.setEnable(serviceExpDateEnable);
    serviceExpDate.setMandatory(serviceExpDateMandatory);

    Boolean certificateEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificate.enable"));
    Boolean certificateMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificate.mandatory"));
    certificate = new SMPFieldProperties();
    certificate.setEnable(certificateEnable);
    certificate.setMandatory(certificateMandatory);

    Boolean serviceDescEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceDescription.enable"));
    Boolean serviceDescMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceDescription.mandatory"));
    serviceDesc = new SMPFieldProperties();
    serviceDesc.setEnable(serviceDescEnable);
    serviceDesc.setMandatory(serviceDescMandatory);

    Boolean techContactEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalContactUrl.enable"));
    Boolean techContactMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalContactUrl.mandatory"));
    techContact = new SMPFieldProperties();
    techContact.setEnable(techContactEnable);
    techContact.setMandatory(techContactMandatory);

    Boolean techInformationEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalInformationUrl.enable"));
    Boolean techInformationMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalInformationUrl.mandatory"));
    techInformation = new SMPFieldProperties();
    techInformation.setEnable(techInformationEnable);
    techInformation.setMandatory(techInformationMandatory);

    Boolean extensionEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".extension.enable"));
    Boolean extensionMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".extension.mandatory"));
    extension = new SMPFieldProperties();
    extension.setEnable(extensionEnable);
    extension.setMandatory(extensionMandatory);

    Boolean redirectHrefEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".redirectHref.enable"));
    Boolean redirectHrefMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".redirectHref.mandatory"));
    redirectHref = new SMPFieldProperties();
    redirectHref.setEnable(redirectHrefEnable);
    redirectHref.setMandatory(redirectHrefMandatory);

    Boolean certificateUIDEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificateUID.enable"));
    Boolean certificateUIDMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificateUID.mandatory"));
    certificateUID = new SMPFieldProperties();
    certificateUID.setEnable(certificateUIDEnable);
    certificateUID.setMandatory(certificateUIDMandatory);
  }
  
  
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public SMPFieldProperties getUri() {
    return uri;
  }

  public void setUri(SMPFieldProperties uri) {
    this.uri = uri;
  }

  public SMPFieldProperties getServiceActDate() {
    return serviceActDate;
  }

  public void setServiceActDate(SMPFieldProperties serviceActDate) {
    this.serviceActDate = serviceActDate;
  }

  public SMPFieldProperties getServiceExpDate() {
    return serviceExpDate;
  }

  public void setServiceExpDate(SMPFieldProperties serviceExpDate) {
    this.serviceExpDate = serviceExpDate;
  }

  public SMPFieldProperties getCertificate() {
    return certificate;
  }

  public void setCertificate(SMPFieldProperties certificate) {
    this.certificate = certificate;
  }

  public SMPFieldProperties getServiceDesc() {
    return serviceDesc;
  }

  public void setServiceDesc(SMPFieldProperties serviceDesc) {
    this.serviceDesc = serviceDesc;
  }

  public SMPFieldProperties getTechContact() {
    return techContact;
  }

  public void setTechContact(SMPFieldProperties techContact) {
    this.techContact = techContact;
  }

  public SMPFieldProperties getTechInformation() {
    return techInformation;
  }

  public void setTechInformation(SMPFieldProperties techInformation) {
    this.techInformation = techInformation;
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
}
