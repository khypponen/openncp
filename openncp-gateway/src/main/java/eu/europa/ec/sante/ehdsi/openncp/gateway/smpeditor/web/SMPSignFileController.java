package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Countries;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFields;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFile;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFileOps;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPType;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Alert;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SMPConverter;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.XMLValidator;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SignFile;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.ReadSMPProperties;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.exception.GenericException;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SimpleErrorHandler;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Inês Garganta
 */

@Controller
@SessionAttributes("smpfilesign")
public class SMPSignFileController {

  org.slf4j.Logger logger = LoggerFactory.getLogger(SMPSignFileController.class);

  @Autowired
  private final SMPConverter smpconverter = new SMPConverter();
  @Autowired
  private final XMLValidator xmlValidator = new XMLValidator();
  @Autowired
  private Environment env;
  @Autowired
  private ReadSMPProperties readProperties = new ReadSMPProperties();
  
  private SMPType smptype;
  private String type;

  /**
   * Generate SignFile page
   *
   * @param model
   * @return
   */
  @RequestMapping(value = "/smpeditor/signsmpfile", method = RequestMethod.GET)
  public String signFile(Model model) {
    logger.debug("\n==== in signFile ====");
    model.addAttribute("smpfilesign", new SMPFileOps());

    return "smpeditor/signsmpfile";
  }

  /**
   * Generate signsmpfile page for generated files
   *
   * @param model
   * @param smpfile
   * @return
   */
  @RequestMapping(value = "/smpeditor/signsmpfile/generated", method = RequestMethod.GET)
  public String signCreatedFile(Model model, @ModelAttribute("smpfile") SMPFile smpfile) {
    logger.debug("\n==== in signCreatedFile ====");
    SMPFileOps smpfilesign = new SMPFileOps();
    
    if(smpfile.getGeneratedFile() == null){
      throw new GenericException("Not Found", "The requested file does not exists");
    }
    
    File file = new File(smpfile.getGeneratedFile().getPath());
    FileInputStream input = null;
    try {
      input = new FileInputStream(file);
    } catch (FileNotFoundException ex) {
      logger.error("\n FileNotFoundException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    MultipartFile fileSign = null;
    try {
      fileSign = new MockMultipartFile("fileSign", file.getName(), "text/xml", input);
    } catch (IOException ex) {
      logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    
    List<MultipartFile> files = new ArrayList<MultipartFile>();
    files.add(0, fileSign);

    smpfilesign.setSignFiles(files);
    smpfilesign.setSignFileName(fileSign.getOriginalFilename());
    model.addAttribute("hasfile", true);
    model.addAttribute("smpfilesign", smpfilesign);
    
    return "smpeditor/signsmpfile";
  }
  
   /**
   * Generate signsmpfile page for updated files
   *
   * @param model
   * @param smpfileupdate
   * @return
   */
  @RequestMapping(value = "/smpeditor/signsmpfile/updated", method = RequestMethod.GET)
  public String signUpdatedFile(Model model, @ModelAttribute("smpfileupdate") SMPFileOps smpfileupdate) {
    logger.debug("\n==== in signCreatedFile ====");
    SMPFileOps smpfilesign = new SMPFileOps();
    
    if(smpfileupdate.getGeneratedFile() == null){
      throw new GenericException("Not Found", "The requested file does not exists");
    }
    
    File file = new File(smpfileupdate.getGeneratedFile().getPath());
    
    FileInputStream input = null;
    try {
      input = new FileInputStream(file);
    } catch (FileNotFoundException ex) {
      logger.error("\nFileNotFoundException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    MultipartFile fileSign = null;
    try {
      fileSign = new MockMultipartFile("fileSign", file.getName(), "text/xml", input);
    } catch (IOException ex) {
      logger.error("\nIOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    
    List<MultipartFile> files = new ArrayList<MultipartFile>();
    files.add(0, fileSign);

    smpfilesign.setSignFiles(files);
    smpfilesign.setSignFileName(fileSign.getOriginalFilename());
    model.addAttribute("hasfile", true);
    model.addAttribute("smpfilesign", smpfilesign);
    
    return "smpeditor/signsmpfile";
  }

  /**
   * POST of file/files to sign
   * @param smpfilesign
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/smpeditor/signsmpfile", method = RequestMethod.POST)
  public String postSign(@ModelAttribute("smpfilesign") SMPFileOps smpfilesign, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in postSign ====");
    model.addAttribute("smpfilesign", smpfilesign);
    
    List<SMPFileOps> allFiles = new ArrayList<SMPFileOps>();
    List<MultipartFile> signFiles = new ArrayList<MultipartFile>();
    signFiles = smpfilesign.getSignFiles();

    /*Iterate each chosen file*/
    for (int k = 0; k < signFiles.size(); k++) {
      logger.debug("\n***** MULTIPLE FILE NAME " + k + " - " + signFiles.get(k).getOriginalFilename());
      SMPFileOps smpfile = new SMPFileOps();
      SMPFields smpfields = new SMPFields();

      smpfile.setSignFile(signFiles.get(k));

      File convFile = new File("/" + smpfile.getSignFile().getOriginalFilename());
      try {
        smpfile.getSignFile().transferTo(convFile);
      } catch (IOException ex) {
        logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      } catch (IllegalStateException ex) {
        logger.error("\n IllegalStateException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      }

      boolean valid = xmlValidator.validator(convFile.getPath());
      if (valid) {
        logger.debug("\n****VALID XML File");
      } else {
        logger.debug("\n****NOT VALID XML File");
        String message = env.getProperty("error.notsmp"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        convFile.delete();
        return "redirect:/smpeditor/signsmpfile";
      }
      convFile.delete();

      ServiceMetadata serviceMetadata;
      serviceMetadata = smpconverter.convertFromXml(smpfile.getSignFile());

      boolean isSigned = smpconverter.getIsSignedServiceMetadata();
      if (isSigned) {
        logger.debug("\n****SIGNED SMP File");
        convFile.delete();
        String message = env.getProperty("warning.isSigned.sigmenu"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.warning));
        return "redirect:/smpeditor/signsmpfile";
      } else {
        logger.debug("\n****NOT SIGNED File");
      }

      /*
       Condition to know the type of file (Redirect|ServiceInformation) in order to build the form
       */
      if (serviceMetadata.getRedirect() != null) {
        logger.debug("\n******** REDIRECT");
        type = "Redirect";
        smpfile.setType(smptype.Redirect);

        if (!serviceMetadata.getRedirect().getExtensions().isEmpty()) {
          logger.debug("\n******* SIGNED EXTENSION - " + serviceMetadata.getRedirect().getExtensions().get(0).getAny().getNodeName());
          String message = env.getProperty("warning.isSignedExtension");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.warning);
          smpfile.setAlert(alert);
        }
        
        /*
          get documentIdentifier and participantIdentifier from redirect href
        */
        String href = serviceMetadata.getRedirect().getHref();
        String participantID;
        String documentID="";
        Pattern pattern = Pattern.compile(env.getProperty("ParticipantIdentifier.Scheme") + ".*");//SPECIFICATION
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
          String result = matcher.group(0);
          try {
            result = java.net.URLDecoder.decode(result, "UTF-8");
          } catch (UnsupportedEncodingException ex) {
            logger.error("\n UnsupportedEncodingException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
          }
          String[] ids = result.split("/services/");//SPECIFICATION
          participantID = ids[0];
          String[] cc = participantID.split(":");//SPECIFICATION May change if Participant Identifier specification change

          Countries count = null;
          Countries[] countries = count.getALL();
          for (int i = 0; i < countries.length; i++) {
            if (cc[4].equals(countries[i].name())) {
              smpfile.setCountry(cc[4]);
            }
          }
          if (smpfile.getCountry() == null) {
            String message = env.getProperty("error.redirect.href.participantID"); //messages.properties
            redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
            return "redirect:/smpeditor/signsmpfile";
          }
          
          
          String docID = ids[1];
          HashMap<String, String> propertiesMap = readProperties.readPropertiesFile();
          String[] nIDs = docID.split(env.getProperty("DocumentIdentifier.Scheme") + "::");//SPECIFICATION May change if Document Identifier specification change
          String docuID = nIDs[1];
          Set set2 = propertiesMap.entrySet();
          Iterator iterator2 = set2.iterator();
          while (iterator2.hasNext()) {
            Map.Entry mentry2 = (Map.Entry) iterator2.next();
            // logger.debug("\n ****** " + mentry2.getKey().toString() + " = " + mentry2.getValue().toString());
            if (docuID.equals(mentry2.getKey().toString())) {
              String[] docs = mentry2.getValue().toString().split("\\.");
              documentID = docs[0];
              logger.debug("\n ****** documentID - " + documentID);
              break;
            }
          }
          
          String smpType = documentID; //smpeditor.properties
          if (smpType.equals("")) {
            String message = env.getProperty("error.redirect.href.documentID"); //messages.properties
            redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
            return "redirect:/smpeditor/signsmpfile";
          }
          
          /*Builds final file name*/
          String timeStamp = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new java.util.Date());
          String fileName = smpfile.getType().name() + "_" + smpType + "_" + smpfile.getCountry().toUpperCase() + "_Signed_" + timeStamp + ".xml";
          smpfile.setFileName(fileName);
          logger.debug("\n********* FILENAME REDIRECT - " + fileName);

        } else {
          logger.error("\n****NOT VALID HREF IN REDIRECT");
          String message = env.getProperty("error.redirect.href"); //messages.properties
          redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
          return "redirect:/smpeditor/signsmpfile";
        }

      } else if (serviceMetadata.getServiceInformation() != null) { /*Service Information Type*/
        logger.debug("\n******** SERVICE INFORMATION");
        type = "ServiceInformation";

        if (!serviceMetadata.getServiceInformation().getExtensions().isEmpty()) {
          logger.debug("\n******* SIGNED EXTENSION - " + serviceMetadata.getServiceInformation().getExtensions().get(0).getAny().getNodeName());
          String message = env.getProperty("warning.isSignedExtension");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.warning);
          smpfile.setAlert(alert);
        }

        smpfile.setDocumentIdentifier(serviceMetadata.getServiceInformation().getDocumentIdentifier().getValue());
        smpfile.setDocumentIdentifierScheme(serviceMetadata.getServiceInformation().getDocumentIdentifier().getScheme());
        String documentIdentifier = smpfile.getDocumentIdentifier();
        logger.debug("\n************ DOC ID - " + documentIdentifier);
        
        String documentID = "";
        HashMap<String, String> propertiesMap = readProperties.readPropertiesFile();
        Set set2 = propertiesMap.entrySet();
        Iterator iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
          Map.Entry mentry2 = (Map.Entry) iterator2.next();
          // logger.debug("\n ****** " + mentry2.getKey().toString() + " = " + mentry2.getValue().toString());
          if (documentIdentifier.equals(mentry2.getKey().toString())) {
            String[] docs = mentry2.getValue().toString().split("\\.");
            documentID = docs[0];
            logger.debug("\n ****** documentID - " + documentID);
            break;
          }
        }

        SMPType[] smptypes = smptype.getALL();
        for (int i = 0; i < smptypes.length; i++) {
          if (smptypes[i].name().equals(documentID)) {
            smpfile.setType(smptypes[i]);
            break;
          }
        }
        if (smpfile.getType() == null) {
          String message = env.getProperty("error.serviceinformation.documentID"); //messages.properties
          redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
          return "redirect:/smpeditor/signsmpfile";
        }

        String participanteID = serviceMetadata.getServiceInformation().getParticipantIdentifier().getValue();
        logger.debug("\n ************* participanteID - " + participanteID);
        String[] cc = participanteID.split(":");
         for (int i = 0; i < cc.length; i++) {
           logger.debug("\n ************* CC - " + i + " -> " + cc[i]);
         }
        
        
        Countries count = null;
        Countries[] countries = count.getALL();
        for (int i = 0; i < countries.length; i++) {
          if (cc[2].equals(countries[i].name())) {
            smpfile.setCountry(cc[2]);
          }
        }
        if (smpfile.getCountry() == null) {
          String message = env.getProperty("error.serviceinformation.participantID"); //messages.properties
          redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
          return "redirect:/smpeditor/signsmpfile";
        }
        
        /*Builds final file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new java.util.Date());
        String fileName = smpfile.getType().name() + "_" + smpfile.getCountry().toUpperCase() + "_Signed_" + timeStamp + ".xml";
        smpfile.setFileName(fileName);
     
        smpfile.setParticipantIdentifier(participanteID);
        smpfile.setParticipantIdentifierScheme(serviceMetadata.getServiceInformation().getParticipantIdentifier().getScheme());
        smpfile.setProcessIdentifier(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getProcessIdentifier().getValue());
        smpfile.setProcessIdentifierScheme(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getProcessIdentifier().getScheme());
        smpfile.setTransportProfile(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getTransportProfile());
        smpfile.setRequiredBusinessLevelSig(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).isRequireBusinessLevelSignature());
        smpfile.setMinimumAutenticationLevel(serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getMinimumAuthenticationLevel());
      }

      /*
       * Read smpeditor.properties file
       */
      readProperties.readProperties(smpfile);
      
      smpfields.setUri(readProperties.getUri());
      smpfields.setServiceActivationDate(readProperties.getServiceActDate());
      smpfields.setServiceExpirationDate(readProperties.getServiceExpDate());
      smpfields.setCertificate(readProperties.getCertificate());
      smpfields.setServiceDescription(readProperties.getServiceDesc());
      smpfields.setTechnicalContactUrl(readProperties.getTechContact());
      smpfields.setTechnicalInformationUrl(readProperties.getTechInformation());
      smpfields.setExtension(readProperties.getExtension());
      smpfields.setRedirectHref(readProperties.getRedirectHref());
      smpfields.setCertificateUID(readProperties.getCertificateUID());


      if ("ServiceInformation".equals(type)) {
        EndpointType endpoint = serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0);

        X509Certificate cert = null;
        String subjectName = null;
        if (smpfields.getCertificate().isEnable()) {
          try {
            InputStream in = new ByteArrayInputStream(endpoint.getCertificate());
            cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
            if (cert != null) {
              subjectName = "Issuer: " + cert.getIssuerX500Principal().getName() + "\nSerial Number #" + cert.getSerialNumber();
              smpfile.setCertificateContent(subjectName);
            }
          } catch (CertificateException ex) {
            logger.error("\n CertificateException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
          }

          try {
            smpfile.setCertificate(cert.getEncoded());

          } catch (CertificateEncodingException ex) {
            logger.error("\n CertificateEncodingException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
          }
        } else {
          smpfile.setCertificate(null);
        }

        smpfile.setEndpointURI(endpoint.getEndpointURI());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar cal = endpoint.getServiceActivationDate();
        String formatted = format.format(cal.getTime());
        Calendar cal2 = endpoint.getServiceExpirationDate();
        String formatted2 = format.format(cal2.getTime());

        smpfile.setServiceActivationDateS(formatted);
        smpfile.setServiceExpirationDateS(formatted2);
        smpfile.setCertificateContent(subjectName);
        smpfile.setServiceDescription(endpoint.getServiceDescription());
        smpfile.setTechnicalContactUrl(endpoint.getTechnicalContactUrl());
        smpfile.setTechnicalInformationUrl(endpoint.getTechnicalInformationUrl());

        if (smpfields.getExtension().isEnable()) {
          try {
            String content = new Scanner(smpfile.getSignFile().getInputStream()).useDelimiter("\\Z").next();
            String capturedString = content.substring(content.indexOf("<Extension>"), content.indexOf("</Extension>"));
            String[] endA = capturedString.split("<Extension>");
            smpfile.setExtensionContent(endA[1]);
          } catch (IOException ex) {
            logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
          }
        }

      } else if ("Redirect".equals(type)) {
        RedirectType redirect = serviceMetadata.getRedirect();
        smpfile.setCertificateUID(redirect.getCertificateUID());
        smpfile.setHref(redirect.getHref());
      }

      smpfile.setTypeS(smpfile.getType().getDescription());
      smpfile.setId(k);
      smpfile.setSmpfields(smpfields);
      
      allFiles.add(k, smpfile);      
    }
    smpfilesign.setAllFiles(allFiles);

    logger.debug("\n********* MODEL - " + model.toString());

    return "redirect:checksignsmpfile";
  }

  /**
   * Generate UpdateFileForm page
   *
   * @param smpfilesign
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/checksignsmpfile", method = RequestMethod.GET)
  public String signFileForm(@ModelAttribute("smpfilesign") SMPFileOps smpfilesign, Model model) {
    logger.debug("\n==== in signFileForm ====");
    model.addAttribute("smpfilesign", smpfilesign); 
    model.addAttribute("smpfiles", smpfilesign.getAllFiles());  
    
    logger.debug("\n********* MODEL - " + model.toString());
    return "smpeditor/checksignsmpfile";
  }

  /**
   * Sign the file
   * Calls SignFile to sign the xml files
   * @param smpfilesign
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/smpeditor/checksignsmpfile", method = RequestMethod.POST)
  public String signSMPFile(@ModelAttribute("smpfilesign") SMPFileOps smpfilesign, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in signSMPFile ====");
    
    /*
    mock
    sample_national_infrastructure
    mock
    */
    
    File file = new File(ConfigurationManagerService.getInstance().getProperty("NCP_SIG_KEYSTORE_PATH"));
    FileInputStream input = null;
    try {
      input = new FileInputStream(file);
    } catch (FileNotFoundException ex) {
      logger.error("\n FileNotFoundException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    MultipartFile keystore = null;
    try {
      keystore = new MockMultipartFile("keystore", file.getName(), "text/xml", input);
    } catch (IOException ex) {
      logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    
    for (int i = 0; i < smpfilesign.getAllFiles().size(); i++) {
      SignFile signFile = new SignFile();
      try {
        signFile.signFiles(smpfilesign.getAllFiles().get(i).getType().name(),
                smpfilesign.getAllFiles().get(i).getFileName(),
                keystore,
                ConfigurationManagerService.getInstance().getProperty("NCP_SIG_KEYSTORE_PASSWORD"),
                ConfigurationManagerService.getInstance().getProperty("NCP_SIG_PRIVATEKEY_ALIAS"),
                ConfigurationManagerService.getInstance().getProperty("NCP_SIG_PRIVATEKEY_PASSWORD"),
                smpfilesign.getAllFiles().get(i).getSignFile());
      } catch (Exception ex) {
        logger.error("\nException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      }

      if (signFile.isInvalidKeystoreSMP()) {
        logger.error("\n****INVALID KEYSTORE");
        String message = env.getProperty("error.keystore.invalid"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/signsmpfile";
      }
      if (signFile.isInvalidKeyPairSMP()) {
        logger.error("\n****INVALID KEY PAIR");
        String message = env.getProperty("error.keypair.invalid"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/signsmpfile";
      }

      smpfilesign.getAllFiles().get(i).setGeneratedFile(signFile.getGeneratedSignFile());
    }
    
    return "redirect:/smpeditor/savesignedsmpfile";
  }

  /**
   * Generate savesignedsmpfile page
   *
   * @param smpfilesign
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/savesignedsmpfile", method = RequestMethod.GET)
  public String saveSignFile(@ModelAttribute("smpfilesign") SMPFileOps smpfilesign, Model model) {
    logger.debug("\n==== in saveSignFile ====");
    model.addAttribute("smpfilesign", smpfilesign);    
    model.addAttribute("smpfiles", smpfilesign.getAllFiles());

    return "smpeditor/savesignedsmpfile";
  }

  /**
   * Download SignedSMPFile
   *
   * @param smpfilesign
   * @param request
   * @param response
   * @param model
   * @param filename   
   */
  @RequestMapping(value = "smpeditor/savesignedsmpfile/download/{filename}", method = RequestMethod.GET)
  public void downloadFile(@ModelAttribute("smpfilesign") SMPFileOps smpfilesign, HttpServletRequest request,
          HttpServletResponse response, Model model, @PathVariable("filename") String filename) {
    logger.debug("\n==== in download Signed File ====");
    model.addAttribute("smpfilesign", smpfilesign);

    for (int i = 0; i < smpfilesign.getAllFiles().size(); i++) {

      if (smpfilesign.getAllFiles().get(i).getFileName().equals(filename + ".xml") ) {
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=" + smpfilesign.getAllFiles().get(i).getFileName());
        response.setContentLength((int) smpfilesign.getAllFiles().get(i).getGeneratedFile().length());
        try {
          InputStream inputStream = new BufferedInputStream(new FileInputStream(smpfilesign.getAllFiles().get(i).getGeneratedFile()));
          FileCopyUtils.copy(inputStream, response.getOutputStream());

        } catch (FileNotFoundException ex) {
          logger.error("\n FileNotFoundException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (IOException ex) {
          logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        }
      }
    }

    
  }

  /**
   * Clean SMPFile - clean the generated xml file in server
   *
   * @param smpfilesign
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/smpeditor/clean3", method = RequestMethod.GET)
  public String cleanSmpFile(@ModelAttribute("smpfilesign") SMPFileOps smpfilesign, Model model) {
    logger.debug("\n==== in cleanSmpFile ====");
    model.addAttribute("smpfilesign", smpfilesign);
    for (int i = 0; i < smpfilesign.getAllFiles().size(); i++) {
      if(smpfilesign.getAllFiles().get(i).getGeneratedFile() != null){
        logger.debug("\n****DELETED ? " + smpfilesign.getAllFiles().get(i).getGeneratedFile().delete());
      }
    }
    return "redirect:/smpeditor/smpeditor";
  }

  /**
   * Clean SMPFile - clean the generated xml file in server
   *
   * @param smpfilesign
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/checksignsmpfile/clean", method = RequestMethod.GET)
  public String cleanFile(@ModelAttribute("smpfilesign") SMPFileOps smpfilesign, Model model) {
    logger.debug("\n==== in cleanFile ====");
    model.addAttribute("smpfilesign", smpfilesign);
    for (int i = 0; i < smpfilesign.getAllFiles().size(); i++) {
      if(smpfilesign.getAllFiles().get(i).getGeneratedFile() != null){
        logger.debug("\n****DELETED ? " + smpfilesign.getAllFiles().get(i).getGeneratedFile().delete());
      }
    }
    return "redirect:/smpeditor/checksignsmpfile";
  }

}
