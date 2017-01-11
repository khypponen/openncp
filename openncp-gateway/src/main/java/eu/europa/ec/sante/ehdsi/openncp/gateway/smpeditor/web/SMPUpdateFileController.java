package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFileUpdate;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SMPConverter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.EndpointType;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("smpfileupdate")
public class SMPUpdateFileController {

  org.slf4j.Logger logger = LoggerFactory.getLogger(SMPUpdateFileController.class);
  
  @Autowired
  private final SMPConverter smpconverter = new SMPConverter();

  @RequestMapping(value = "/smpeditor/UpdateFile", method = RequestMethod.GET)
  public String updateFile(Model model) {
    logger.debug("\n==== in updateFile ====");
    model.addAttribute("smpfileupdate", new SMPFileUpdate());
    return "smpeditor/UpdateFile";
  }

  @RequestMapping(value = "smpeditor/UpdateFile", method = RequestMethod.POST)
  public String post(@ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate, Model model) {
    logger.debug("\n==== in post ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    logger.debug("\n**********  smpfileupdate - " + smpfileupdate.toString());
    
    EndpointType endpoint = smpconverter.convertFromXml(smpfileupdate.getUpdateFile());
    
    X509Certificate cert = null;
    String subjectName = null;
    try {
      InputStream in = new ByteArrayInputStream(endpoint.getCertificate());
      cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
      if (cert != null) {
        subjectName = cert.getIssuerX500Principal().getName() + " Serial Number #"
                + cert.getSerialNumber();
      }

    } catch (CertificateException ex) {
      Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    smpfileupdate.setEndpointURI(endpoint.getEndpointURI());
    smpfileupdate.setServiceActivationDate(endpoint.getServiceActivationDate().getTime());
    smpfileupdate.setServiceExpirationDate(endpoint.getServiceExpirationDate().getTime());
    smpfileupdate.setCertificate(subjectName);
    smpfileupdate.setServiceDescription(endpoint.getServiceDescription());
    smpfileupdate.setTechnicalContactUrl(endpoint.getTechnicalContactUrl());
    smpfileupdate.setTechnicalInformationUrl(endpoint.getTechnicalInformationUrl());
    //smpfileupdate.setExtension();
    return "redirect:UpdateFileForm";
  }

  @RequestMapping(value = "smpeditor/UpdateFileForm", method = RequestMethod.GET)
  public String updateFileForm(@ModelAttribute("smpfileupdate") SMPFileUpdate smpfileupdate, Model model) {
    logger.debug("\n==== in updateFileForm ====");
    model.addAttribute("smpfileupdate", smpfileupdate);
    logger.debug("\n**********  smpfileupdate - " + smpfileupdate.toString());
    return "smpeditor/UpdateFileForm";
  }
}
