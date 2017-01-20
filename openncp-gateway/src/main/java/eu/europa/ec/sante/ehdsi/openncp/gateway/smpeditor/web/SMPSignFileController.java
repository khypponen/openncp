package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Countries;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFieldProperties;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFields;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFile;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFileUpdate;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPType;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SMPConverter;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.XMLValidator;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SignFile;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.EndpointType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.RedirectType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("smpfilesign")
public class SMPSignFileController {

  org.slf4j.Logger logger = LoggerFactory.getLogger(SMPSignFileController.class);

  @Autowired
  private final SMPConverter smpconverter = new SMPConverter();
  @Autowired
  private final XMLValidator xmlValidator = new XMLValidator();
  @Autowired
  private final SignFile signFile = new SignFile();
  @Autowired
  private Environment env;

  private final SMPFields smpfields = new SMPFields();
  private SMPType smptype;
  private String type;
  private boolean hasCountry;
  private boolean isSignedExtension;

  /**
   * Generate SignFile page
   *
   * @param model
   * @return
   */
  @RequestMapping(value = "/smpeditor/SignSMPFile", method = RequestMethod.GET)
  public String signFile(Model model) {
    logger.debug("\n==== in signFile ====");
    model.addAttribute("smpfilesign", new SMPFileUpdate());

    return "smpeditor/SignSMPFile";
  }

  /**
   * Generate SignSMPFile page for generated files
   *
   * @param model
   * @param smpfile
   * @return
   */
  @RequestMapping(value = "/smpeditor/SignSMPFile/generated", method = RequestMethod.GET)
  public String signCreatedFile(Model model, @ModelAttribute("smpfile") SMPFile smpfile) {
    logger.debug("\n==== in signCreatedFile ====");
    SMPFileUpdate smpfilesign = new SMPFileUpdate();
    
    File file = new File(smpfile.getGeneratedFile().getPath());
    FileInputStream input = null;
    try {
      input = new FileInputStream(file);
    } catch (FileNotFoundException ex) {
      logger.error("\nFileNotFoundException - " + ex.getMessage());
      Logger.getLogger(SMPSignFileController.class.getName()).log(Level.SEVERE, null, ex);
    }
    MultipartFile fileSign = null;
    try {
      fileSign = new MockMultipartFile("fileSign", file.getName(), "text/xml", input);
    } catch (IOException ex) {
      logger.error("\nIOException - " + ex.getMessage());
      Logger.getLogger(SMPSignFileController.class.getName()).log(Level.SEVERE, null, ex);
    }

    smpfilesign.setSignFile(fileSign);
    smpfilesign.setSignFileName(fileSign.getOriginalFilename());
    model.addAttribute("hasfile", true);
    model.addAttribute("smpfilesign", smpfilesign);
    
    return "smpeditor/SignSMPFile";
  }
  
   /**
   * Generate SignSMPFile page for updated files
   *
   * @param model
   * @param smpfileupdate
   * @return
   */
  @RequestMapping(value = "/smpeditor/SignSMPFile/updated", method = RequestMethod.GET)
  public String signUpdatedFile(Model model, @ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate) {
    logger.debug("\n==== in signCreatedFile ====");
    SMPFileUpdate smpfilesign = new SMPFileUpdate();
    logger.debug("\n*********** signCreatedFile SMPFILE - " + smpfileupdate);
    logger.debug("\n*********** signCreatedFile SMPFILE FILE - " + smpfileupdate.getGeneratedFile().getPath());
    
    File file = new File(smpfileupdate.getGeneratedFile().getPath());
    
    FileInputStream input = null;
    try {
      input = new FileInputStream(file);
    } catch (FileNotFoundException ex) {
      logger.error("\nFileNotFoundException - " + ex.getMessage());
      Logger.getLogger(SMPSignFileController.class.getName()).log(Level.SEVERE, null, ex);
    }
    MultipartFile fileSign = null;
    try {
      fileSign = new MockMultipartFile("fileSign", file.getName(), "text/xml", input);
    } catch (IOException ex) {
      logger.error("\nIOException - " + ex.getMessage());
      Logger.getLogger(SMPSignFileController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    smpfilesign.setSignFile(fileSign);
    smpfilesign.setSignFileName(fileSign.getOriginalFilename());
    model.addAttribute("hasfile", true);
    model.addAttribute("smpfilesign", smpfilesign);
    
    return "smpeditor/SignSMPFile";
  }

  @RequestMapping(value = "/smpeditor/SignSMPFile", method = RequestMethod.POST)
  public String postSign(@ModelAttribute("smpfilesign") SMPFileUpdate smpfilesign, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in postSign ====");
    model.addAttribute("smpfilesign", smpfilesign);
   
    smpfilesign.setFileName(smpfilesign.getSignFile().getOriginalFilename());
    File convFile = new File("/" + smpfilesign.getSignFile().getOriginalFilename());
    try {
      smpfilesign.getSignFile().transferTo(convFile);

    } catch (IOException ex) {
      Logger.getLogger(SMPUpdateFileController.class
              .getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalStateException ex) {
      Logger.getLogger(SMPUpdateFileController.class
              .getName()).log(Level.SEVERE, null, ex);
    }

    boolean valid = xmlValidator.validator(convFile.getPath());
    if (valid) {
      logger.debug("\n****VALID XML File");
    } else {
      logger.debug("\n****NOT VALID XML File");
      redirectAttributes.addFlashAttribute("notsmp", "Not a SMP File");
      convFile.delete();
      return "redirect:/smpeditor/SignSMPFile";
    }
    convFile.delete();

    ServiceMetadata serviceMetadata;
    serviceMetadata = smpconverter.convertFromXml(smpfilesign.getSignFile());
    logger.debug("\n****SERVICE METADATA -- " + serviceMetadata);

    boolean isSigned = smpconverter.getIsSignedServiceMetadata();
    if (isSigned) {
      logger.debug("\n****SIGNED SMP File");
      redirectAttributes.addFlashAttribute("signed", "Signed SMP File");
      convFile.delete();
      return "redirect:/smpeditor/SignSMPFile";
    } else {
      logger.debug("\n****NOT SIGNED File");
    }

    /*
     Condition to know the type of file (Redirect|ServiceInformation) in order to build the form
     */
    if (serviceMetadata.getRedirect() != null) {
      logger.debug("\n******** REDIRECT");
      type = "Redirect";
      smpfilesign.setType(smptype.Redirect);

      if (!serviceMetadata.getRedirect().getExtensions().isEmpty()) {
        logger.debug("\n******* SIGNED EXTENSION - " + serviceMetadata.getRedirect().getExtensions().get(0).getAny().getNodeName());
        isSignedExtension = true;
      }

      //Get redirect country
      Countries count = null;
      String fileName = smpfilesign.getSignFile().getOriginalFilename();
      String[] cc = fileName.split("_");
      if (cc.length < 2) {
        logger.debug("\n ****** Country of Redirect not found ");
        hasCountry = false;
      } else {
        Countries[] countries = count.getALL();
        for (int i = 0; i < countries.length; i++) {
          if (cc[1].toLowerCase().equals(countries[i].name())) {
            smpfilesign.setCountry(cc[1].toLowerCase());
            hasCountry = true;
          }
        }
        if (smpfilesign.getCountry() == null) {
          hasCountry = false;
        }
      }

    } else if (serviceMetadata.getServiceInformation() != null) {
      logger.debug("\n******** SERVICE INFORMATION");
      type = "ServiceInformation";
      hasCountry = true;

      if (!serviceMetadata.getServiceInformation().getExtensions().isEmpty()) {
        logger.debug("\n******* SIGNED EXTENSION - " + serviceMetadata.getServiceInformation().getExtensions().get(0).getAny().getNodeName());
        isSignedExtension = true;
      }

      smpfilesign.setDocumentIdentifier(serviceMetadata.getServiceInformation().getDocumentIdentifier().getValue());
      smpfilesign.setDocumentIdentifierScheme(serviceMetadata.getServiceInformation().getDocumentIdentifier().getScheme());
      String documentIdentifier = smpfilesign.getDocumentIdentifier();
      logger.debug("\n******** DOC ID 1 - " + documentIdentifier);

      SMPType[] smptypes = smptype.getALL();
      for (int i = 0; i < smptypes.length; i++) {
        String docID = env.getProperty(smptypes[i].name() + ".DocumentIdentifier");
        if (docID == null ? documentIdentifier == null : docID.equals(documentIdentifier)) {
          smpfilesign.setType(smptypes[i]);
          break;
        }
      }
      logger.debug("\n******** SMP Type - " + smpfilesign.getType().name());

      String participanteID = serviceMetadata.getServiceInformation().getParticipantIdentifier().getValue();
      String[] cc = participanteID.split(":");
      smpfilesign.setCountry(cc[2]);
      smpfilesign.setParticipantIdentifier(participanteID);
      smpfilesign.setParticipantIdentifierScheme(serviceMetadata.getServiceInformation().getParticipantIdentifier().getScheme());
      smpfilesign.setProcessIdentifier(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getProcessIdentifier().getValue());
      smpfilesign.setProcessIdentifierScheme(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getProcessIdentifier().getScheme());
      smpfilesign.setTransportProfile(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getTransportProfile());
      smpfilesign.setRequiredBusinessLevelSig(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).isRequireBusinessLevelSignature());
      smpfilesign.setMinimumAutenticationLevel(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getMinimumAuthenticationLevel());
    }

    /*
     * Read smpeditor.properties file
     */
    Boolean uriEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".uri.enable"));
    Boolean uriMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".uri.mandatory"));
    SMPFieldProperties uri = new SMPFieldProperties();
    uri.setEnable(uriEnable);
    uri.setMandatory(uriMandatory);
    smpfields.setUri(uri);

    Boolean serviceActDateEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".serviceActivationDate.enable"));
    Boolean serviceActDateMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".serviceActivationDate.mandatory"));
    SMPFieldProperties serviceActDate = new SMPFieldProperties();
    serviceActDate.setEnable(serviceActDateEnable);
    serviceActDate.setMandatory(serviceActDateMandatory);
    smpfields.setServiceActivationDate(serviceActDate);

    Boolean serviceExpDateEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".serviceExpirationDate.enable"));
    Boolean serviceExpDateMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".serviceExpirationDate.mandatory"));
    SMPFieldProperties serviceExpDate = new SMPFieldProperties();
    serviceExpDate.setEnable(serviceExpDateEnable);
    serviceExpDate.setMandatory(serviceExpDateMandatory);
    smpfields.setServiceExpirationDate(serviceExpDate);

    Boolean certificateEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".certificate.enable"));
    Boolean certificateMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".certificate.mandatory"));
    SMPFieldProperties certificate = new SMPFieldProperties();
    certificate.setEnable(certificateEnable);
    certificate.setMandatory(certificateMandatory);
    smpfields.setCertificate(certificate);

    Boolean serviceDescEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".serviceDescription.enable"));
    Boolean serviceDescMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".serviceDescription.mandatory"));
    SMPFieldProperties serviceDesc = new SMPFieldProperties();
    serviceDesc.setEnable(serviceDescEnable);
    serviceDesc.setMandatory(serviceDescMandatory);
    smpfields.setServiceDescription(serviceDesc);

    Boolean techContactEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".technicalContactUrl.enable"));
    Boolean techContactMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".technicalContactUrl.mandatory"));
    SMPFieldProperties techContact = new SMPFieldProperties();
    techContact.setEnable(techContactEnable);
    techContact.setMandatory(techContactMandatory);
    smpfields.setTechnicalContactUrl(techContact);

    Boolean techInformationEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".technicalInformationUrl.enable"));
    Boolean techInformationMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".technicalInformationUrl.mandatory"));
    SMPFieldProperties techInformation = new SMPFieldProperties();
    techInformation.setEnable(techInformationEnable);
    techInformation.setMandatory(techInformationMandatory);
    smpfields.setTechnicalInformationUrl(techInformation);

    Boolean extensionEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".extension.enable"));
    Boolean extensionMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".extension.mandatory"));
    SMPFieldProperties extension = new SMPFieldProperties();
    extension.setEnable(extensionEnable);
    extension.setMandatory(extensionMandatory);
    smpfields.setExtension(extension);

    Boolean redirectHrefEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".redirectHref.enable"));
    Boolean redirectHrefMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".redirectHref.mandatory"));
    SMPFieldProperties redirectHref = new SMPFieldProperties();
    redirectHref.setEnable(redirectHrefEnable);
    redirectHref.setMandatory(redirectHrefMandatory);
    smpfields.setRedirectHref(redirectHref);

    Boolean certificateUIDEnable = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".certificateUID.enable"));
    Boolean certificateUIDMandatory = Boolean.parseBoolean(env.getProperty(smpfilesign.getType().name() + ".certificateUID.mandatory"));
    SMPFieldProperties certificateUID = new SMPFieldProperties();
    certificateUID.setEnable(certificateUIDEnable);
    certificateUID.setMandatory(certificateUIDMandatory);
    smpfields.setCertificateUID(certificateUID);

    model.addAttribute("smpfields", smpfields);

    if ("ServiceInformation".equals(type)) {
      EndpointType endpoint = serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0);

      X509Certificate cert = null;
      String subjectName = null;
      if (smpfields.getCertificate().isEnable()) {
        try {
          InputStream in = new ByteArrayInputStream(endpoint.getCertificate());
          cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
          if (cert != null) {
            subjectName = "Issuer: " + cert.getIssuerX500Principal().getName() + "\nSerial Number #"
                    + cert.getSerialNumber();
            smpfilesign.setCertificateContent(subjectName);

          }
        } catch (CertificateException ex) {
          Logger.getLogger(SMPConverter.class
                  .getName()).log(Level.SEVERE, null, ex);
        }

        try {
          smpfilesign.setCertificate(cert.getEncoded());

        } catch (CertificateEncodingException ex) {
          Logger.getLogger(SMPUpdateFileController.class
                  .getName()).log(Level.SEVERE, null, ex);
        }
      } else {
        smpfilesign.setCertificate(null);
      }

      smpfilesign.setEndpointURI(endpoint.getEndpointURI());

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
        Logger.getLogger(SMPUpdateFileController.class
                .getName()).log(Level.SEVERE, null, ex);
      }

      smpfilesign.setServiceActivationDate(datead);
      smpfilesign.setServiceExpirationDate(dateed);
      smpfilesign.setCertificateContent(subjectName);
      smpfilesign.setServiceDescription(endpoint.getServiceDescription());
      smpfilesign.setTechnicalContactUrl(endpoint.getTechnicalContactUrl());
      smpfilesign.setTechnicalInformationUrl(endpoint.getTechnicalInformationUrl());

      if (smpfields.getExtension().isEnable()) {
        try {
          String content = new Scanner(smpfilesign.getSignFile().getInputStream()).useDelimiter("\\Z").next();
          String capturedString = content.substring(content.indexOf("<Extension>"), content.indexOf("</Extension>"));
          String[] endA = capturedString.split("<Extension>");
          logger.debug("\n*****Content from Extension 1 : \n" + endA[1]);
          smpfilesign.setExtensionContent(endA[1]);
          //smpfilesign.setExtension(endpoint.getExtensions());
        } catch (IOException ex) {
          Logger.getLogger(SMPUpdateFileController.class
                  .getName()).log(Level.SEVERE, null, ex);
        }
      }

    } else if ("Redirect".equals(type)) {
      RedirectType redirect = serviceMetadata.getRedirect();
      smpfilesign.setCertificateUID(redirect.getCertificateUID());
      smpfilesign.setHref(redirect.getHref());
    }

    //isSigned = smpconverter.getIsSignedServiceMetadata();
    logger.debug("\n********* MODEL - " + model.toString());

    return "redirect:CheckSignSMPFile";
  }

  /**
   * Generate UpdateFileForm page
   *
   * @param smpfilesign
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/CheckSignSMPFile", method = RequestMethod.GET)
  public String signFileForm(@ModelAttribute("smpfilesign") SMPFileUpdate smpfilesign, Model model) {
    logger.debug("\n==== in signFileForm ====");
    model.addAttribute("smpfilesign", smpfilesign);
    model.addAttribute("smpfields", smpfields);
    model.addAttribute(type, "Type " + type);
    if (isSignedExtension) {
      model.addAttribute("isSignedExtension", isSignedExtension);
    }
    if (hasCountry) {
      model.addAttribute("hasCountry", true);
    }

    logger.debug("\n********* MODEL - " + model.toString());
    return "smpeditor/CheckSignSMPFile";
  }

  /**
   * Sign the file
   * @param smpfilesign
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/smpeditor/CheckSignSMPFile", method = RequestMethod.POST)
  public String signSMPFile(@ModelAttribute("smpfilesign") SMPFileUpdate smpfilesign, Model model, final RedirectAttributes redirectAttributes) {

    try {
      signFile.signFiles(type, smpfilesign.getKeystoreFile(), smpfilesign.getKeystorePassword(), smpfilesign.getKeyAlias(), smpfilesign.getKeyPassword(),
              smpfilesign.getSignFile());

    } catch (Exception ex) {
      Logger.getLogger(SMPGenerateFileController.class
              .getName()).log(Level.SEVERE, null, ex);
    }

    if (signFile.isInvalidKeystoreSMP()) {
      logger.debug("\n****INVALID KEYSTORE");
      redirectAttributes.addFlashAttribute("keystoreinvalid", "Invalid keystore!");
      return "redirect:/smpeditor/SignSMPFile";
    }
    if (signFile.isInvalidKeyPairSMP()) {
      logger.debug("\n****INVALID KEY PAIR");
      redirectAttributes.addFlashAttribute("keypair", "Invalid key pair!");
      return "redirect:/smpeditor/SignSMPFile";
    }

    smpfilesign.setGeneratedFile(signFile.getGeneratedSignFile());
    smpfilesign.setFileName(signFile.getFileName());

    return "smpeditor/SaveSignedSMPFile";
  }

  /**
   * Generate SaveSignedSMPFile page
   *
   * @param smpfilesign
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/SaveSignedSMPFile", method = RequestMethod.GET)
  public String saveSignFile(@ModelAttribute("smpfilesign") SMPFileUpdate smpfilesign, Model model) {
    logger.debug("\n==== in saveSignFile ====");
    model.addAttribute("smpfilesign", smpfilesign);
    return "smpeditor/SaveSignedSMPFile";
  }

  /**
   * Download SignedSMPFile
   *
   * @param smpfilesign
   * @param request
   * @param response
   * @param model
   */
  @RequestMapping(value = "smpeditor/SaveSignedSMPFile/download", method = RequestMethod.GET)
  public void downloadFile(@ModelAttribute("smpfilesign") SMPFileUpdate smpfilesign, HttpServletRequest request,
          HttpServletResponse response, Model model) {
    logger.debug("\n==== in download Signed File ====");
    model.addAttribute("smpfilesign", smpfilesign);

    response.setContentType("application/xml");
    response.setHeader("Content-Disposition", "attachment; filename=" + smpfilesign.getFileName());
    response.setContentLength((int) smpfilesign.getGeneratedFile().length());
    try {
      InputStream inputStream = new BufferedInputStream(new FileInputStream(smpfilesign.getGeneratedFile()));
      FileCopyUtils.copy(inputStream, response.getOutputStream());

    } catch (FileNotFoundException ex) {
      Logger.getLogger(SMPGenerateFileController.class
              .getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(SMPGenerateFileController.class
              .getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Clean SMPFile - clean the generated xml file in server
   *
   * @param smpfilesign
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/SMPEditor/clean3", method = RequestMethod.GET)
  public String cleanSmpFile(@ModelAttribute("smpfilesign") SMPFileUpdate smpfilesign, Model model) {
    logger.debug("\n==== in cleanSmpFile ====");
    model.addAttribute("smpfilesign", smpfilesign);
    logger.debug("\n****DELETED ? " + smpfilesign.getGeneratedFile().delete());
    return "redirect:/smpeditor/SMPEditor";
  }

  /**
   * Clean SMPFile - clean the generated xml file in server
   *
   * @param smpfilesign
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/CheckSignSMPFile/clean", method = RequestMethod.GET)
  public String cleanFile(@ModelAttribute("smpfilesign") SMPFileUpdate smpfilesign, Model model) {
    logger.debug("\n==== in cleanFile ====");
    model.addAttribute("smpfilesign", smpfilesign);
    logger.debug("\n****DELETED ? " + smpfilesign.getGeneratedFile().delete());
    return "redirect:/smpeditor/CheckSignSMPFile";
  }

}
