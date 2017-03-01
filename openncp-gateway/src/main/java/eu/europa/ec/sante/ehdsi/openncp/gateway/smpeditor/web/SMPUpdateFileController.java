package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFields;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFileOps;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Countries;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPType;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Alert;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SMPConverter;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.XMLValidator;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.ReadSMPProperties;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Inês Garganta
 */

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
  @Autowired
  private ReadSMPProperties readProperties = new ReadSMPProperties();

  SMPType smptype;

  private final SMPFields smpfields = new SMPFields();
  private String type;
  private boolean isSigned;

  /**
   * Generate updatesmpfile page
   *
   * @param model
   * @return
   */
  @RequestMapping(value = "/smpeditor/updatesmpfile", method = RequestMethod.GET)
  public String updateFile(Model model) {
    logger.debug("\n==== in updateFile ====");
    model.addAttribute("smpfileupdate", new SMPFileOps());    
    return "smpeditor/updatesmpfile";
  }

  /**
   * Manage Post from updatesmpfile page to NewSMPFileUpdate page
   *
   * @param smpfileupdate
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "smpeditor/updatesmpfile", method = RequestMethod.POST)
  public String post(@ModelAttribute("smpfileupdate") SMPFileOps smpfileupdate, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in post ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    logger.debug("\n**********  smpfileupdate - " + smpfileupdate.toString());
    isSigned = false;
    
    File convFile = new File("/" + smpfileupdate.getUpdateFile().getOriginalFilename());
    try {
      smpfileupdate.getUpdateFile().transferTo(convFile);
    } catch (IOException ex) {
      logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    } catch (IllegalStateException ex) {
      logger.error("\n IllegalStateException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    
    /*Validate xml file*/
    boolean valid = xmlValidator.validator(convFile.getPath());
    if (valid) {
      logger.debug("\n****VALID XML File");
    } else {
      logger.debug("\n****NOT VALID XML File");
      String message = env.getProperty("error.notsmp"); //messages.properties
      redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
      convFile.delete();
      return "redirect:/smpeditor/updatesmpfile";
    }
    convFile.delete();
    
    /*Read data from xml*/
    ServiceMetadata serviceMetadata;
    serviceMetadata = smpconverter.convertFromXml(smpfileupdate.getUpdateFile());
        
    /*
     Condition to know the type of file (Redirect|ServiceInformation) in order to build the form
     */
    if (serviceMetadata.getRedirect() != null) {
      logger.debug("\n******** REDIRECT");
      type = "Redirect";
      smpfileupdate.setType(smptype.Redirect);
      
      if (!serviceMetadata.getRedirect().getExtensions().isEmpty()) {
        logger.debug("\n******* SIGNED EXTENSION - " + serviceMetadata.getRedirect().getExtensions().get(0).getAny().getNodeName());
        isSigned = true;
      }

      /*
        get participantIdentifier from redirect href
      */
      /*May change if Participant Identifier specification change*/
      String href = serviceMetadata.getRedirect().getHref();
      String participantID;
      Pattern pattern = Pattern.compile(env.getProperty("ParticipantIdentifier.Scheme") + ".*");//SPECIFICATION
      Matcher matcher = pattern.matcher(href);
      if (matcher.find()) {
        String result = matcher.group(0);
        try {
          result = java.net.URLDecoder.decode(result, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
          logger.error("\n UnsupportedEncodingException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        }
        String[] ids = result.split("/services/");
        participantID = ids[0];
        String[] cc = participantID.split(":");       
        
        Countries count = null;
        Countries[] countries = count.getALL();
        for (int i = 0; i < countries.length; i++) {
          if (cc[4].equals(countries[i].name())) {
            smpfileupdate.setCountry(cc[4]);

          }
        }
        if(smpfileupdate.getCountry() == null){
          String message = env.getProperty("error.redirect.href.participantID"); //messages.properties
          redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
          return "redirect:/smpeditor/updatesmpfile";
        }


      } else {
        logger.error("\n****NOT VALID HREF IN REDIRECT");
        String message = env.getProperty("error.redirect.href"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/updatesmpfile";
      }

    } else if (serviceMetadata.getServiceInformation() != null) {
      logger.debug("\n******** SERVICE INFORMATION");
      type = "ServiceInformation";
      
      if (!serviceMetadata.getServiceInformation().getExtensions().isEmpty()) {
        logger.debug("\n******* SIGNED EXTENSION - " + serviceMetadata.getServiceInformation().getExtensions().get(0).getAny().getNodeName());
        isSigned = true;
      }
      
      smpfileupdate.setDocumentIdentifier(serviceMetadata.getServiceInformation().getDocumentIdentifier().getValue());
      smpfileupdate.setDocumentIdentifierScheme(serviceMetadata.getServiceInformation().getDocumentIdentifier().getScheme());
      String documentIdentifier = smpfileupdate.getDocumentIdentifier();
      logger.debug("\n******** DOC ID 1 - " + documentIdentifier);

      /*
      Used to check SMP File type in order to render html updatesmpfileform page
      */
      String documentID="";
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
          if (docs.length > 2) { /*Country_B_Identity_Provider case - can have two differents DocIds */
            smpfileupdate.setIssuanceType(docs[2]);
          }
          break;
        }
      }

      SMPType[] smptypes = smptype.getALL();
      for (int i = 0; i < smptypes.length; i++) {
        if (smptypes[i].name().equals(documentID)) {
          smpfileupdate.setType(smptypes[i]);
          break;
        }
      }
      if (smpfileupdate.getType() == null) {
        String message = env.getProperty("error.serviceinformation.documentID"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/updatesmpfile";
      }

      String participanteID = serviceMetadata.getServiceInformation().getParticipantIdentifier().getValue();
      String[] cc = participanteID.split(":");//SPECIFICATION
      Countries count = null;
      Countries[] countries = count.getALL();
      for (int i = 0; i < countries.length; i++) {
        if (cc[2].equals(countries[i].name())) {
          smpfileupdate.setCountry(cc[2]);
        }
      }
      if (smpfileupdate.getCountry() == null) {
        String message = env.getProperty("error.serviceinformation.participantID"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/updatesmpfile";
      }
      
      /*Builds final file name*/
      String timeStamp = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new java.util.Date());
      String fileName = smpfileupdate.getType().name() + "_" + smpfileupdate.getCountry().toUpperCase() + "_" + timeStamp + ".xml";
      smpfileupdate.setFileName(fileName);

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
    readProperties.readProperties(smpfileupdate);
     
    smpfields.setUri(readProperties.getUri());
    smpfields.setIssuanceType(readProperties.getIssuanceType());    
    smpfields.setServiceActivationDate(readProperties.getServiceActDate());
    smpfields.setServiceExpirationDate(readProperties.getServiceExpDate());
    smpfields.setCertificate(readProperties.getCertificate());
    smpfields.setServiceDescription(readProperties.getServiceDesc());
    smpfields.setTechnicalContactUrl(readProperties.getTechContact());
    smpfields.setTechnicalInformationUrl(readProperties.getTechInformation());
    smpfields.setExtension(readProperties.getExtension());
    smpfields.setRedirectHref(readProperties.getRedirectHref());
    smpfields.setCertificateUID(readProperties.getCertificateUID());

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
            smpfileupdate.setCertificateContent(subjectName);
          }
        } catch (CertificateException ex) {
          logger.error("\n CertificateException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        }

        try {
          smpfileupdate.setCertificate(cert.getEncoded());
        } catch (CertificateEncodingException ex) {
          logger.error("\n CertificateEncodingException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
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
        logger.error("\n ParseException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
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
          
          Document docOriginal = smpconverter.parseDocument(endA[1]);
          docOriginal.getDocumentElement().normalize();
          
          smpfileupdate.setExtension(docOriginal.getDocumentElement());
         
        } catch (IOException ex) {
          logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (SAXException ex) {
          logger.error("\n SAXException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (ParserConfigurationException ex) {
          logger.error("\n ParserConfigurationException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        }
      }

    } else if ("Redirect".equals(type)) {
      RedirectType redirect = serviceMetadata.getRedirect();
      smpfileupdate.setCertificateUID(redirect.getCertificateUID());
      smpfileupdate.setHref(redirect.getHref());
    }
    
    if(!isSigned){
      isSigned = smpconverter.getIsSignedServiceMetadata();
    }
    
    logger.debug("\n********* MODEL - " + model.toString());
    return "redirect:updatesmpfileform";
  }

  /**
   * Generate updatesmpfileform page
   *
   * @param smpfileupdate
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/updatesmpfileform", method = RequestMethod.GET)
  public String updateFileForm(@ModelAttribute("smpfileupdate") SMPFileOps smpfileupdate, Model model) {
    logger.debug("\n==== in updateFileForm ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    model.addAttribute("smpfields", smpfields);
    model.addAttribute(type, "Type " + type);
    model.addAttribute(smpfileupdate.getType().name(), "SMPType " + smpfileupdate.getType().name());
    model.addAttribute("signed" + String.valueOf(isSigned), "Signed -> " + isSigned);
          
    logger.debug("\n********* MODEL - " + model.toString());
    return "smpeditor/updatesmpfileform";
  }

  /**
   * Manage Post from NewSMPFileUpdate page to saveupdatedsmpfile page 
   * Calls SMPConverter to construct the xml file
   *
   * @param smpfileupdate
   * @param model
   * @param redirectAttributes
   * @param action
   * @return
   */
  @RequestMapping(value = "smpeditor/updatesmpfileform", method = RequestMethod.POST)
  public String updatenewfile(@ModelAttribute("smpfileupdate") SMPFileOps smpfileupdate, Model model, 
          final RedirectAttributes redirectAttributes, @RequestParam(value="action", required=true) String action) {
    logger.debug("\n==== in updatenewfile ==== ");

    model.addAttribute("smpfileupdate", smpfileupdate);

    if (null != smpfileupdate.getType().name()) {
      switch (type) {
        case "ServiceInformation":
          logger.debug("\n****Type Service Information");

          if (!smpfields.getCertificate().isEnable()) {
            smpfileupdate.setCertificateFile(null);
          } else {

            String certPath = env.getProperty(smpfileupdate.getType().name() + ".certificate");
            String certificatePath = ConfigurationManagerService.getInstance().getProperty(certPath);

            FileInputStream fis = null;
            try {
              fis = new FileInputStream(certificatePath);
            } catch (FileNotFoundException ex) {
              logger.error("\n FileNotFoundException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
            }

            smpfileupdate.setCertificateFile(fis);
          }
          
          if (smpfields.getExtension().isEnable()) {
            if (smpfileupdate.getExtensionFile().getOriginalFilename().equals("")) {
              smpfileupdate.setExtensionFile(null);
            }
          }
          
          if(!smpfileupdate.getIssuanceType().equals("")){
            smpfileupdate.setDocumentIdentifier(env.getProperty(smpfileupdate.getType().name() + ".DocumentIdentifier." + smpfileupdate.getIssuanceType()));
            smpfileupdate.setProcessIdentifier(env.getProperty(smpfileupdate.getType().name() + ".ProcessIdentifier." + smpfileupdate.getIssuanceType()));
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
                logger.error("\n****NOT VALID Certificate File");
                String message = env.getProperty("error.certificate.invalid"); //messages.properties
                redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
                return "redirect:/smpeditor/updatesmpfileform";
              }
            }
          }
          if (smpfields.getExtension().isEnable()) {
            if (smpfileupdate.getExtensionFile() != null) {
              if (smpconverter.isNullExtension()) {
                logger.error("\n****NOT VALID Extension File");
                String message = env.getProperty("error.extension.invalid"); //messages.properties
                redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
                return "redirect:/smpeditor/updatesmpfileform";
              }
            }
          }
          break;
        case "Redirect":
          logger.debug("\n****Type Redirect");

          /*get documentIdentifier from redierct href*/
          /*May change if Document Identifier specification change*/
          String href = smpfileupdate.getHref();
          String documentID="";
          Pattern pattern = Pattern.compile(env.getProperty("ParticipantIdentifier.Scheme") + ".*"); //SPECIFICATION
          Matcher matcher = pattern.matcher(href);
          if (matcher.find()) {
            String result = matcher.group(0);
            try {
              result = java.net.URLDecoder.decode(result, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
              logger.error("\n UnsupportedEncodingException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
            }
            String[] ids = result.split("/services/");//SPECIFICATION
            
            String docID = ids[1];
            HashMap<String, String> propertiesMap = readProperties.readPropertiesFile();
            String[] nIDs = docID.split(env.getProperty("DocumentIdentifier.Scheme") + "::");//SPECIFICATION May change if Document Identifier specification change
            String docuID = nIDs[1];
            logger.debug("\n ****** docuID - " + docuID);
            Set set2 = propertiesMap.entrySet();
            Iterator iterator2 = set2.iterator();
            while (iterator2.hasNext()) {
              Map.Entry mentry2 = (Map.Entry) iterator2.next();
             // logger.debug("\n ****** " + mentry2.getKey().toString() + " = " + mentry2.getValue().toString());
              if(docuID.equals(mentry2.getKey().toString())){
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
              return "redirect:/smpeditor/updatesmpfileform";
            }
            
            /*Builds final file name*/
            String timeStamp = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new java.util.Date());
            String fileName = smpfileupdate.getType().name() + "_" + smpType + "_" + smpfileupdate.getCountry().toUpperCase() + "_" + timeStamp + ".xml";
            smpfileupdate.setFileName(fileName);
            
          } else {
            logger.error("\n****NOT VALID HREF IN REDIRECT");
            String message = env.getProperty("error.redirect.href"); //messages.properties
            redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
            return "redirect:/smpeditor/updatesmpfile";
          }

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
      String message = env.getProperty("error.file.xsd"); //messages.properties
      redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
      return "redirect:/smpeditor/updatesmpfileform";
    }

    /*chosen options sign file in html modal*/
    if (action.equals("sign")) {
      logger.debug("\n****ACTION SIGN");
      return "redirect:saveupdatedsmpfile/sign"; 
    }

    return "redirect:saveupdatedsmpfile";  
  }

  /**
   * Generate saveupdatedsmpfile page
   *
   * @param smpfileupdate
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/saveupdatedsmpfile", method = RequestMethod.GET)
  public String saveFile(@ModelAttribute("smpfileupdate") SMPFileOps smpfileupdate, Model model) {
    logger.debug("\n==== in saveFile ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    return "smpeditor/saveupdatedsmpfile";
  }
  
  /**
   * Sign Updated SMPFile 
   * @param smpfileupdate
   * @param redirectAttributes
   * @return 
   */
  @RequestMapping(value = "/smpeditor/saveupdatedsmpfile/sign", method = RequestMethod.GET)
  public String signFile(@ModelAttribute("smpfileupdate") SMPFileOps smpfileupdate, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in Update signFile ====");
     redirectAttributes.addFlashAttribute("smpfileupdate", smpfileupdate);
    return "redirect:/smpeditor/signsmpfile/updated";
  }

  /**
   * Download SMPFile
   *
   * @param smpfileupdate
   * @param request
   * @param response
   * @param model
   */
  @RequestMapping(value = "smpeditor/saveupdatedsmpfile/download", method = RequestMethod.GET)
  public void downloadFile(@ModelAttribute("smpfileupdate") SMPFileOps smpfileupdate, HttpServletRequest request,
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
      logger.error("\n FileNotFoundException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    } catch (IOException ex) {
      logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
  }

  /**
   * Clean SMPFile - clean the generated xml file in server
   *
   * @param smpfileupdate
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/smpeditor/clean2", method = RequestMethod.GET)
  public String cleanSmpFile(@ModelAttribute("smpfileupdate") SMPFileOps smpfileupdate, Model model) {
    logger.debug("\n==== in cleanSmpFile ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    if(smpfileupdate.getGeneratedFile() != null){
      logger.debug("\n****DELETED ? " + smpfileupdate.getGeneratedFile().delete());
    }
    return "redirect:/smpeditor/smpeditor";
  }

  /**
   * Clean SMPFile - clean the generated xml file in server
   *
   * @param smpfileupdate
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/updatesmpfileform/clean", method = RequestMethod.GET)
  public String cleanFile(@ModelAttribute("smpfileupdate") SMPFileOps smpfileupdate, Model model) {
    logger.debug("\n==== in cleanFile ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    if(smpfileupdate.getGeneratedFile() != null){
      logger.debug("\n****DELETED ? " + smpfileupdate.getGeneratedFile().delete());
    }
    return "redirect:/smpeditor/updatesmpfileform";
  }
}
