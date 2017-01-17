package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFields;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFieldProperties;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFileUpdate;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Countries;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPType;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SMPConverter;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.XMLValidator;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.EndpointType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.RedirectType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("smpfileupdate")
public class SMPUpdateFileController {

  org.slf4j.Logger logger = LoggerFactory.getLogger(SMPUpdateFileController.class);

  @Autowired
  private final SMPConverter smpconverter = new SMPConverter();
  @Autowired
  private final XMLValidator xmlValidator = new XMLValidator();
  @Autowired
  private Environment env;

  SMPType smptype;

  private final SMPFields smpfields = new SMPFields();
  private String type;
  private boolean isSigned;
  private boolean hasCountry;

  /**
   * Generate UpdateFile page
   *
   * @param model
   * @return
   */
  @RequestMapping(value = "/smpeditor/UpdateFile", method = RequestMethod.GET)
  public String updateFile(Model model) {
    logger.debug("\n==== in updateFile ====");
    model.addAttribute("smpfileupdate", new SMPFileUpdate());
    
    Locale locale = LocaleContextHolder.getLocale();
    String country = locale.getCountry();
    
    logger.debug("\n******** LOCALE - " + country);
    
    return "smpeditor/UpdateFile";
  }

  /**
   * Manage Post from UpdateFile page to NewSMPFileUpdate page
   *
   * @param smpfileupdate
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "smpeditor/UpdateFile", method = RequestMethod.POST)
  public String post(@ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in post ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    logger.debug("\n**********  smpfileupdate - " + smpfileupdate.toString());
    
    File convFile = new File("/" + smpfileupdate.getUpdateFile().getOriginalFilename());
    try {
      smpfileupdate.getUpdateFile().transferTo(convFile);
    } catch (IOException ex) {
      Logger.getLogger(SMPUpdateFileController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalStateException ex) {
      Logger.getLogger(SMPUpdateFileController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    boolean valid = xmlValidator.validator(convFile.getPath());
    if (valid) {
      logger.debug("\n****VALID XML File");
    } else {
      logger.debug("\n****NOT VALID XML File");
      redirectAttributes.addFlashAttribute("notsmp", "Not a SMP File");
      convFile.delete();
      return "redirect:/smpeditor/UpdateFile";
    }
    convFile.delete();
    
        
    ServiceMetadata serviceMetadata;
    serviceMetadata = smpconverter.convertFromXml(smpfileupdate.getUpdateFile());
    logger.debug("\n****SERVICE METADATA -- " + serviceMetadata);

    /*
     Condition to know the type of file (Redirect|ServiceInformation) in order to build the form
     */
    if (serviceMetadata.getRedirect() != null) {
      logger.debug("\n******** REDIRECT");
      type = "Redirect";
      smpfileupdate.setType(smptype.Redirect);
      
      Countries count = null;
   
      String fileName = smpfileupdate.getUpdateFile().getOriginalFilename();
      String[] cc = fileName.split("_");
      if (cc.length < 2) {
        logger.debug("\n ****** Country of Redirect not found ");
        hasCountry = false;
      } else {
        Countries[] countries = count.getALL();
        for (int i = 0; i < countries.length; i++) {
          if (cc[1].toLowerCase().equals(countries[i].name())) {
            smpfileupdate.setCountry(cc[1].toLowerCase());
            hasCountry = true;
          }
        } 
        if(smpfileupdate.getCountry() == null){
          hasCountry = false;
        }
      }


    } else if (serviceMetadata.getServiceInformation() != null) {
      logger.debug("\n******** SERVICE INFORMATION");
      hasCountry = true;
      smpfileupdate.setDocumentIdentifier(serviceMetadata.getServiceInformation().getDocumentIdentifier().getValue());
      smpfileupdate.setDocumentIdentifierScheme(serviceMetadata.getServiceInformation().getDocumentIdentifier().getScheme());
      String documentIdentifier = smpfileupdate.getDocumentIdentifier();
      logger.debug("\n******** DOC ID 1 - " + documentIdentifier);

      SMPType[] smptypes = smptype.getALL();
      for (int i = 0; i < smptypes.length; i++) {
        String docID = env.getProperty(smptypes[i].name() + ".DocumentIdentifier");
        if (docID == null ? documentIdentifier == null : docID.equals(documentIdentifier)) {
          smpfileupdate.setType(smptypes[i]);
          break;
        }
      }
      logger.debug("\n******** SMP Type - " + smpfileupdate.getType().name());
      //type -> ServiceInformation
      type = env.getProperty("type." + smpfileupdate.getType().name()); //smpeditor.properties

      String participanteID = serviceMetadata.getServiceInformation().getParticipantIdentifier().getValue();
      String[] cc = participanteID.split(":");
      smpfileupdate.setCountry(cc[2]);
      smpfileupdate.setParticipantIdentifier(participanteID);
      smpfileupdate.setParticipantIdentifierScheme(serviceMetadata.getServiceInformation().getParticipantIdentifier().getScheme());
      smpfileupdate.setProcessIdentifier(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getProcessIdentifier().getValue());
      smpfileupdate.setProcessIdentifierScheme(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getProcessIdentifier().getScheme());
      smpfileupdate.setTransportProfile(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getTransportProfile());
      smpfileupdate.setRequiredBusinessLevelSig(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).isRequireBusinessLevelSignature());
      smpfileupdate.setMinimumAutenticationLevel(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getMinimumAuthenticationLevel());
    }

    /*
     * Read smpeditor.properties file
     */
    Boolean uriEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".uri.enable"));
    Boolean uriMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".uri.mandatory"));
    SMPFieldProperties uri = new SMPFieldProperties();
    uri.setEnable(uriEnable);
    uri.setMandatory(uriMandatory);
    smpfields.setUri(uri);

    Boolean serviceActDateEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".serviceActivationDate.enable"));
    Boolean serviceActDateMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".serviceActivationDate.mandatory"));
    SMPFieldProperties serviceActDate = new SMPFieldProperties();
    serviceActDate.setEnable(serviceActDateEnable);
    serviceActDate.setMandatory(serviceActDateMandatory);
    smpfields.setServiceActivationDate(serviceActDate);

    Boolean serviceExpDateEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".serviceExpirationDate.enable"));
    Boolean serviceExpDateMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".serviceExpirationDate.mandatory"));
    SMPFieldProperties serviceExpDate = new SMPFieldProperties();
    serviceExpDate.setEnable(serviceExpDateEnable);
    serviceExpDate.setMandatory(serviceExpDateMandatory);
    smpfields.setServiceExpirationDate(serviceExpDate);

    Boolean certificateEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".certificate.enable"));
    Boolean certificateMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".certificate.mandatory"));
    SMPFieldProperties certificate = new SMPFieldProperties();
    certificate.setEnable(certificateEnable);
    certificate.setMandatory(certificateMandatory);
    smpfields.setCertificate(certificate);

    Boolean serviceDescEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".serviceDescription.enable"));
    Boolean serviceDescMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".serviceDescription.mandatory"));
    SMPFieldProperties serviceDesc = new SMPFieldProperties();
    serviceDesc.setEnable(serviceDescEnable);
    serviceDesc.setMandatory(serviceDescMandatory);
    smpfields.setServiceDescription(serviceDesc);

    Boolean techContactEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".technicalContactUrl.enable"));
    Boolean techContactMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".technicalContactUrl.mandatory"));
    SMPFieldProperties techContact = new SMPFieldProperties();
    techContact.setEnable(techContactEnable);
    techContact.setMandatory(techContactMandatory);
    smpfields.setTechnicalContactUrl(techContact);

    Boolean techInformationEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".technicalInformationUrl.enable"));
    Boolean techInformationMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".technicalInformationUrl.mandatory"));
    SMPFieldProperties techInformation = new SMPFieldProperties();
    techInformation.setEnable(techInformationEnable);
    techInformation.setMandatory(techInformationMandatory);
    smpfields.setTechnicalInformationUrl(techInformation);

    Boolean extensionEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".extension.enable"));
    Boolean extensionMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".extension.mandatory"));
    SMPFieldProperties extension = new SMPFieldProperties();
    extension.setEnable(extensionEnable);
    extension.setMandatory(extensionMandatory);
    smpfields.setExtension(extension);

    Boolean redirectHrefEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".redirectHref.enable"));
    Boolean redirectHrefMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".redirectHref.mandatory"));
    SMPFieldProperties redirectHref = new SMPFieldProperties();
    redirectHref.setEnable(redirectHrefEnable);
    redirectHref.setMandatory(redirectHrefMandatory);
    smpfields.setRedirectHref(redirectHref);

    Boolean certificateUIDEnable = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".certificateUID.enable"));
    Boolean certificateUIDMandatory = Boolean.parseBoolean(env.getProperty(smpfileupdate.getType().name() + ".certificateUID.mandatory"));
    SMPFieldProperties certificateUID = new SMPFieldProperties();
    certificateUID.setEnable(certificateUIDEnable);
    certificateUID.setMandatory(certificateUIDMandatory);
    smpfields.setCertificateUID(certificateUID);

    model.addAttribute("smpfields", smpfields);
    
    

    if ("ServiceInformation".equals(type)) {
      EndpointType endpoint = serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0);

      X509Certificate cert = null;
      String subjectName = null;
      try {
        InputStream in = new ByteArrayInputStream(endpoint.getCertificate());
        cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
        if (cert != null) {
          subjectName = "Issuer: " +  cert.getIssuerX500Principal().getName() + "\nSerial Number #"
                  + cert.getSerialNumber();
          smpfileupdate.setCertificateContent(subjectName);
        }
      } catch (CertificateException ex) {
        Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
      }
      if (smpfields.getCertificate().isEnable()) {
        try {
          smpfileupdate.setCertificate(cert.getEncoded());
        } catch (CertificateEncodingException ex) {
          Logger.getLogger(SMPUpdateFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
      } else {
        smpfileupdate.setCertificate(null);
      }

      smpfileupdate.setEndpointURI(endpoint.getEndpointURI());

      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      Calendar cal = endpoint.getServiceActivationDate();
      String formatted = format.format(cal.getTime());
      Date datead = null;
      Date dateed = null;
      Calendar cal2 = endpoint.getServiceExpirationDate();
      String formatted2 = format.format(cal2.getTime());
      try {
        datead = format.parse(formatted);
        dateed = format.parse(formatted2);
      } catch (ParseException ex) {
        Logger.getLogger(SMPUpdateFileController.class.getName()).log(Level.SEVERE, null, ex);
      }

      smpfileupdate.setServiceActivationDate(datead);
      smpfileupdate.setServiceExpirationDate(dateed);
      smpfileupdate.setCertificateContent(subjectName);
      smpfileupdate.setServiceDescription(endpoint.getServiceDescription());
      smpfileupdate.setTechnicalContactUrl(endpoint.getTechnicalContactUrl());
      smpfileupdate.setTechnicalInformationUrl(endpoint.getTechnicalInformationUrl());

      if (smpfields.getExtension().isEnable()) {
        try {
          String content = new Scanner(smpfileupdate.getUpdateFile().getInputStream()).useDelimiter("\\Z").next();
          String capturedString = content.substring(content.indexOf("<Extension>"), content.indexOf("</Extension>"));
          String[] endA = capturedString.split("<Extension>");
          logger.debug("\n*****Content from Extension 1 : \n" + endA[1]);
          smpfileupdate.setExtensionContent(endA[1]);
          smpfileupdate.setExtension(endpoint.getExtensions());
        } catch (IOException ex) {
          Logger.getLogger(SMPUpdateFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

    } else if ("Redirect".equals(type)) {
      RedirectType redirect = serviceMetadata.getRedirect();
      smpfileupdate.setCertificateUID(redirect.getCertificateUID());
      smpfileupdate.setHref(redirect.getHref());
    }
    
    isSigned = smpconverter.getIsSignedServiceMetadata();

    logger.debug("\n********* MODEL - " + model.toString());
    return "redirect:UpdateFileForm";
  }

  /**
   * Generate UpdateFileForm page
   *
   * @param smpfileupdate
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/UpdateFileForm", method = RequestMethod.GET)
  public String updateFileForm(@ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate, Model model) {
    logger.debug("\n==== in updateFileForm ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    model.addAttribute("smpfields", smpfields);
    model.addAttribute(type, "Type " + type);
    model.addAttribute(smpfileupdate.getType().name(), "SMPType " + smpfileupdate.getType().name());
    model.addAttribute("signed" + String.valueOf(isSigned), "Signed -> " + isSigned);
    if(hasCountry){
      model.addAttribute("hasCountry", true);
    }
          
    logger.debug("\n********* MODEL - " + model.toString());
    return "smpeditor/UpdateFileForm";
  }

  /**
   * Manage Post from NewSMPFileUpdate page to SaveUpdatedSMPFile page Calls
   * SMPConverter to construct the xml file
   *
   * @param smpfileupdate
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "smpeditor/UpdateFileForm", method = RequestMethod.POST)
  public String updatenewfile(@ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in updatenewfile ==== ");

    model.addAttribute("smpfileupdate", smpfileupdate);

    String timeStamp = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new java.util.Date());
    String fileName = smpfileupdate.getType().name() + "_" + smpfileupdate.getCountry().toUpperCase() + "_" + timeStamp + ".xml";
    smpfileupdate.setFileName(fileName);
    logger.debug("\n********* FILENAME " + fileName);

    if (null != smpfileupdate.getType().name()) {
      switch (type) {
        case "ServiceInformation":
          logger.debug("\n****Type Service Information");

          if (smpfields.getCertificate().isEnable()) {
            if (smpfileupdate.getCertificateFile().getOriginalFilename().equals("")) {
              smpfileupdate.setCertificateFile(null);
            }
          }
          
          if (smpfields.getExtension().isEnable()) {
            if (smpfileupdate.getExtensionFile().getOriginalFilename().equals("")) {
              smpfileupdate.setExtensionFile(null);
            }
          }

          smpconverter.updateToXml(smpfileupdate.getType().name(), smpfileupdate.getCountry(), smpfileupdate.getDocumentIdentifier(),
                  smpfileupdate.getDocumentIdentifierScheme(), smpfileupdate.getParticipantIdentifier(), smpfileupdate.getParticipantIdentifierScheme(),
                  smpfileupdate.getProcessIdentifier(), smpfileupdate.getProcessIdentifierScheme(), smpfileupdate.getTransportProfile(),
                  smpfileupdate.getRequiredBusinessLevelSig(), smpfileupdate.getMinimumAutenticationLevel(),
                  smpfileupdate.getEndpointURI(), smpfileupdate.getServiceDescription(), smpfileupdate.getTechnicalContactUrl(),
                  smpfileupdate.getTechnicalInformationUrl(), smpfileupdate.getServiceActivationDate(),
                  smpfileupdate.getServiceExpirationDate(), smpfileupdate.getCertificate(), smpfileupdate.getCertificateFile(),
                  smpfileupdate.getExtension(), smpfileupdate.getExtensionFile(),
                  smpfileupdate.getFileName(), null, null);

          if (smpfields.getCertificate().isEnable()) {
            if (smpfileupdate.getCertificateFile() != null) {
              if (smpconverter.getCertificateSubjectName() == null) {
                logger.debug("\n****NOT VALID Certificate File");
                redirectAttributes.addFlashAttribute("certificateinvalid", "Certificate invalid!");
                return "redirect:/smpeditor/UpdateFileForm";
              }
            }
          }
          if (smpfields.getExtension().isEnable()) {
            if (smpfileupdate.getExtensionFile() != null) {
              if (smpconverter.isNullExtension()) {
                logger.debug("\n****NOT VALID Extension File");
                redirectAttributes.addFlashAttribute("extensioninvalid", "Extension invalid!");
                return "redirect:/smpeditor/UpdateFileForm";
              }
            }
          }
          break;
        case "Redirect":
          logger.debug("\n****Type Redirect");
          smpconverter.updateToXml(smpfileupdate.getType().name(), null, null, null, null, null, null, null, null, null,
                  null, null, null, null, null, null, null, null, null, null, null,
                  smpfileupdate.getFileName(), smpfileupdate.getCertificateUID(), smpfileupdate.getHref());
          break;
      }
    }

    smpfileupdate.setGeneratedFile(smpconverter.getFile());
    boolean valid = xmlValidator.validator(smpfileupdate.getGeneratedFile().getPath());
    if (valid) {
      logger.debug("\n****VALID XML File");
    } else {
      logger.debug("\n****NOT VALID XML File");
      smpfileupdate.getGeneratedFile().deleteOnExit();
      redirectAttributes.addFlashAttribute("xsdinvalid", "Extension not compliant with xsd");
      return "redirect:/smpeditor/UpdateFileForm";
    }

    return "redirect:SaveUpdatedSMPFile";
  }

  /**
   * Generate SaveUpdatedSMPFile page
   *
   * @param smpfileupdate
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/SaveUpdatedSMPFile", method = RequestMethod.GET)
  public String saveFile(@ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate, Model model) {
    logger.debug("\n==== in saveFile ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    //logger.debug("\n****extension content: " + smpfile.getExtensionData());
    return "smpeditor/SaveUpdatedSMPFile";
  }

  /**
   * Download SMPFile
   *
   * @param smpfileupdate
   * @param request
   * @param response
   * @param model
   */
  @RequestMapping(value = "smpeditor/SaveUpdatedSMPFile/download", method = RequestMethod.GET)
  public void downloadFile(@ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate, HttpServletRequest request,
          HttpServletResponse response, Model model) {
    logger.debug("\n==== in download Updated File ====");
    model.addAttribute("smpfileupdate", smpfileupdate);

    response.setContentType("application/xml");
    response.setHeader("Content-Disposition", "attachment; filename=" + smpfileupdate.getFileName());
    response.setContentLength((int) smpfileupdate.getGeneratedFile().length());
    try {
      InputStream inputStream = new BufferedInputStream(new FileInputStream(smpfileupdate.getGeneratedFile()));
      FileCopyUtils.copy(inputStream, response.getOutputStream());
    } catch (FileNotFoundException ex) {
      Logger.getLogger(SMPGenerateFileController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(SMPGenerateFileController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Clean SMPFile - clean the generated xml file in server
   *
   * @param smpfileupdate
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/SMPEditor/clean2", method = RequestMethod.GET)
  public String cleanSmpFile(@ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate, Model model) {
    logger.debug("\n==== in cleanSmpFile ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    logger.debug("\n****DELETED ? " + smpfileupdate.getGeneratedFile().delete());
    return "redirect:/smpeditor/SMPEditor";
  }

  /**
   * Clean SMPFile - clean the generated xml file in server
   *
   * @param smpfileupdate
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/UpdateFileForm/clean", method = RequestMethod.GET)
  public String cleanFile(@ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate, Model model) {
    logger.debug("\n==== in cleanFile ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    logger.debug("\n****DELETED ? " + smpfileupdate.getGeneratedFile().delete());
    return "redirect:/smpeditor/UpdateFileForm";
  }
}
