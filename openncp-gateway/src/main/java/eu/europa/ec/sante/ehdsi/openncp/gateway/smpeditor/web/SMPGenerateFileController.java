package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFieldProperties;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SMPConverter;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFile;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.XMLValidator;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
        
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@SessionAttributes("smpfile")
public class SMPGenerateFileController {

  org.slf4j.Logger logger = LoggerFactory.getLogger(SMPGenerateFileController.class);
  
  @Autowired
  private final SMPConverter smpconverter = new SMPConverter();
  @Autowired
  private final XMLValidator xmlValidator = new XMLValidator();
  @Autowired
  private Environment env;

  private final SMPFields smpfields = new SMPFields();
  private String type;

  /**
   * Generate GenerateSMPFile page
   * @param model
   * @return 
   */
  @RequestMapping(value = "/smpeditor/GenerateSMPfile", method = RequestMethod.GET)
  public String generateForm(Model model) {
    logger.debug("\n==== in generateForm ====");
    model.addAttribute("smpfile", new SMPFile());
    return "smpeditor/GenerateSMPfile";
  }

  /**
   * Manage Post from GenerateSMPFile page to NewSMPFile page
   * @param smpfile
   * @param model
   * @return 
   */
  @RequestMapping(value = "/smpeditor/GenerateSMPfile", method = RequestMethod.POST)
  public String post(@ModelAttribute("smpfile") SMPFile smpfile, Model model) {
    logger.debug("\n==== in post ====" + smpfile.toString());
    model.addAttribute("smpfile", smpfile);
    return "redirect:NewSMPFile";
  }

  /**
   * Generate NewSMPFile page 
   * Read from smpeditor.properties -- enable/mandatory definitions of the user-filled fields
   * 
   * @param smpfile
   * @param model
   * @return 
   */
  @RequestMapping(value = "smpeditor/NewSMPFile", method = RequestMethod.GET)
  public String generateFile(@ModelAttribute("smpfile") SMPFile smpfile, Model model) {
    logger.debug("\n==== in generateFile ====");
    model.addAttribute("smpfile", smpfile);
    //logger.debug("\n**** smpfile - " + smpfile.toString());
    //logger.debug("\n**** smpfile type - " + smpfile.getType().name());

    type = env.getProperty("type."+ smpfile.getType().name()); //smpeditor.properties
    //logger.debug("\n******** " + smpfile.getType().name() + " - " + type);
    model.addAttribute(type, "Type "+type);

    Boolean uriEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".uri.enable"));
    Boolean uriMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".uri.mandatory"));
    SMPFieldProperties uri = new SMPFieldProperties();
    uri.setEnable(uriEnable);
    uri.setMandatory(uriMandatory);
    smpfields.setUri(uri);
    
    Boolean serviceActDateEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceActivationDate.enable"));
    Boolean serviceActDateMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceActivationDate.mandatory"));
    SMPFieldProperties serviceActDate = new SMPFieldProperties();
    serviceActDate.setEnable(serviceActDateEnable);
    serviceActDate.setMandatory(serviceActDateMandatory);
    smpfields.setServiceActivationDate(serviceActDate);
    
    Boolean serviceExpDateEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceExpirationDate.enable"));
    Boolean serviceExpDateMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceExpirationDate.mandatory"));
    SMPFieldProperties serviceExpDate = new SMPFieldProperties();
    serviceExpDate.setEnable(serviceExpDateEnable);
    serviceExpDate.setMandatory(serviceExpDateMandatory);
    smpfields.setServiceExpirationDate(serviceExpDate);
    
    Boolean certificateEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificate.enable"));
    Boolean certificateMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificate.mandatory"));
    SMPFieldProperties certificate = new SMPFieldProperties();
    certificate.setEnable(certificateEnable);
    certificate.setMandatory(certificateMandatory);
    smpfields.setCertificate(certificate);
    
    Boolean serviceDescEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceDescription.enable"));
    Boolean serviceDescMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".serviceDescription.mandatory"));
    SMPFieldProperties serviceDesc = new SMPFieldProperties();
    serviceDesc.setEnable(serviceDescEnable);
    serviceDesc.setMandatory(serviceDescMandatory);
    smpfields.setServiceDescription(serviceDesc);
    
    Boolean techContactEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalContactUrl.enable"));
    Boolean techContactMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalContactUrl.mandatory"));
    SMPFieldProperties techContact = new SMPFieldProperties();
    techContact.setEnable(techContactEnable);
    techContact.setMandatory(techContactMandatory);
    smpfields.setTechnicalContactUrl(techContact);
    
    Boolean techInformationEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalInformationUrl.enable"));
    Boolean techInformationMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".technicalInformationUrl.mandatory"));
    SMPFieldProperties techInformation = new SMPFieldProperties();
    techInformation.setEnable(techInformationEnable);
    techInformation.setMandatory(techInformationMandatory);
    smpfields.setTechnicalInformationUrl(techInformation);
    
    Boolean extensionEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".extension.enable"));
    Boolean extensionMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".extension.mandatory"));
    SMPFieldProperties extension = new SMPFieldProperties();
    extension.setEnable(extensionEnable);
    extension.setMandatory(extensionMandatory);
    smpfields.setExtension(extension);
    
    Boolean redirectHrefEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".redirectHref.enable"));
    Boolean redirectHrefMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".redirectHref.mandatory"));
    SMPFieldProperties redirectHref = new SMPFieldProperties();
    redirectHref.setEnable(redirectHrefEnable);
    redirectHref.setMandatory(redirectHrefMandatory);
    smpfields.setRedirectHref(redirectHref);

    Boolean certificateUIDEnable = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificateUID.enable"));
    Boolean certificateUIDMandatory = Boolean.parseBoolean(env.getProperty(smpfile.getType().name() + ".certificateUID.mandatory"));
    SMPFieldProperties certificateUID = new SMPFieldProperties();
    certificateUID.setEnable(certificateUIDEnable);
    certificateUID.setMandatory(certificateUIDMandatory);
    smpfields.setCertificateUID(certificateUID);
    
    model.addAttribute("smpfields", smpfields);
    
    //logger.debug("\n**** model - " + model.toString());
    return "smpeditor/NewSMPFile";
  }
  

  /**
   * Manage Post from NewSMPFile page to SaveSMPFile page
   * Calls SMPConverter to construct the xml file
   * 
   * @param smpfile
   * @param model
   * @param redirectAttributes
   * @return 
   */
  @RequestMapping(value = "smpeditor/NewSMPFile", method = RequestMethod.POST)
  public String postnewfile(@ModelAttribute("smpfile") SMPFile smpfile, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in postnewfile ==== ");
   // logger.debug("\n==== smpfile type - " + smpfile.getType().name());
   // logger.debug("\n==== Type -> " + type);
    
    model.addAttribute("smpfile", smpfile);
    
    String timeStamp = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new java.util.Date());
    String fileName = smpfile.getType().name() + "_" + smpfile.getCountry().toUpperCase() + "_" + timeStamp + ".xml";
    smpfile.setFileName(fileName);

    if (null != smpfile.getType().name()) {
      switch (type) {
        case "ServiceInformation":       
          logger.debug("\n****Type Service Information");
          
          if(!smpfields.getCertificate().isEnable()){
            smpfile.setCertificate(null);
          }
          if(!smpfields.getExtension().isEnable()){
            smpfile.setExtension(null);
          }
          
          smpconverter.converteToXml(smpfile.getType().name(), smpfile.getCountry(), smpfile.getEndpointUri(), smpfile.getServiceDescription(),
                  smpfile.getTechnicalContact(), smpfile.getTechnicalInformation(), smpfile.getServiceActivationDate(),
                  smpfile.getServiceExpirationDate(), smpfile.getExtension(), smpfile.getCertificateFile(), smpfile.getFileName(), 
                  null, null);
          
          if (smpfields.getCertificate().isEnable()) {
            if (smpconverter.getCertificateSubjectName() == null) {
              logger.debug("\n****NOT VALID Certificate File");
              redirectAttributes.addFlashAttribute("valid", "NOT VALID CERTIFICATE FILE!");
              return "redirect:/smpeditor/NewSMPFile";
            }
            smpfile.setCertificate(smpconverter.getCertificateSubjectName());
          }
          
          if (smpfields.getExtension().isEnable()) {
            if (smpconverter.isNullExtension()) {
              logger.debug("\n****NOT VALID Extension File");
              redirectAttributes.addFlashAttribute("valid", "NOT VALID EXTENSION FILE!");
              return "redirect:/smpeditor/NewSMPFile";
            }
          }
          
          break;
        case "Redirect":
          logger.debug("\n****Type Redirect");
          smpconverter.converteToXml(smpfile.getType().name(), null, null, null, null, null, null, null, null, null, 
                  smpfile.getFileName(), smpfile.getCertificateUID(), smpfile.getRedirectHref());
          break;
      }
    }

    smpfile.setGeneratedFile(smpconverter.getFile()); 
    boolean valid = xmlValidator.validator(smpfile.getGeneratedFile().getPath());
    if (valid){
      logger.debug("\n****VALID XML File");
    }else{
      logger.debug("\n****NOT VALID XML File");
      smpfile.getGeneratedFile().deleteOnExit();
      redirectAttributes.addFlashAttribute("valid", "XML NOT VALID - "  + xmlValidator.getReasonInvalid());
      return "redirect:/smpeditor/NewSMPFile";
    }
     
    return "redirect:SaveSMPFile";
  }
  
   /**
   * Generate SaveSMPFile page
   * @param smpfile
   * @param model
   * @return 
   */
  @RequestMapping(value = "smpeditor/SaveSMPFile", method = RequestMethod.GET)
  public String saveFile(@ModelAttribute("smpfile") SMPFile smpfile, Model model) {
    logger.debug("\n==== in saveFile ====");
    model.addAttribute("smpfile", smpfile);
    //logger.debug("\n****extension content: " + smpfile.getExtensionData());
    return "smpeditor/SaveSMPFile"; 
  }
  
   /**
   * Download SMPFile
   * @param smpfile
   * @param request
   * @param response
   * @param model
   */
  @RequestMapping(value = "smpeditor/SaveSMPFile/download", method = RequestMethod.GET)
  public void downloadFile(@ModelAttribute("smpfile") SMPFile smpfile, HttpServletRequest request, 
          HttpServletResponse response, Model model) {
    logger.debug("\n==== in downloadFile ====");
    model.addAttribute("smpfile", smpfile);
    //logger.debug("\n****smpconverter content: " + smpconverter.getCertificateSubjectName());
   // logger.debug("\n****smpfile content: " + smpfile.getCountry());
   // logger.debug("\n****smpfile content: " + smpfile.getFileName());

    response.setContentType("application/xml");
    response.setHeader("Content-Disposition", "attachment; filename=" + smpfile.getFileName());
    response.setContentLength((int) smpfile.getGeneratedFile().length());
    try {
      InputStream inputStream = new BufferedInputStream(new FileInputStream(smpfile.getGeneratedFile()));
      FileCopyUtils.copy(inputStream, response.getOutputStream());
    } catch (FileNotFoundException ex) {
      Logger.getLogger(SMPGenerateFileController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(SMPGenerateFileController.class.getName()).log(Level.SEVERE, null, ex);
    }
  } 
  
  /**
   * Clean SMPFile - clean the generated xml file in server
   * @param smpfile
   * @param model
   * @return 
   */
  @RequestMapping(value = "smpeditor/SMPEditor/clean", method = RequestMethod.GET)
  public String cleanSmpFile(@ModelAttribute("smpfile") SMPFile smpfile, Model model) {
    logger.debug("\n==== in deletedFile ====");
    model.addAttribute("smpfile", smpfile);
    //logger.debug("\n****smpfile content: " + smpfile.getFileName());  
    logger.debug("\n****DELETED ? " + smpfile.getGeneratedFile().delete());
    return "redirect:/smpeditor/SMPEditor";
  } 
  
  /**
   * Clean SMPFile - clean the generated xml file in server
   * @param smpfile
   * @param model
   * @return 
   */
  @RequestMapping(value = "smpeditor/NewSMPFile/clean", method = RequestMethod.GET)
  public String cleanFile(@ModelAttribute("smpfile") SMPFile smpfile, Model model) {
    logger.debug("\n==== in deleteFile ====");
    model.addAttribute("smpfile", smpfile);
   // logger.debug("\n****smpfile content: " + smpfile.getFileName());  
    logger.debug("\n****DELETED ? " + smpfile.getGeneratedFile().delete());
    return "redirect:/smpeditor/NewSMPFile";
  } 
  
}
