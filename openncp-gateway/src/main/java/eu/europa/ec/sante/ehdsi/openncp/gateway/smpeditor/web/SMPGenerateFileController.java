package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SMPConverter;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFile;

@Controller
public class SMPGenerateFileController {
  @Autowired
  private SMPConverter smpconverter = new SMPConverter();

  @RequestMapping(value = "/smpeditor/GenerateSMPfile", method=RequestMethod.GET)
  public String generateForm(Model model) {
    System.out.println("==== in generateForm ====");
    model.addAttribute("smpfile", new SMPFile());
    System.out.println("Model " + model.toString());
    return "smpeditor/GenerateSMPfile";
  }

  @RequestMapping(value = "/smpeditor/GenerateSMPfile", method=RequestMethod.POST)
  public String  post(@ModelAttribute("smpfile") SMPFile smpfile, final RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("smpfile", smpfile );
    redirectAttributes.addFlashAttribute("message", "Successfully added..");
    System.out.println("==== in post ==== " + smpfile.toString());
    return "redirect:NewSMPFile";
  }
  
  @RequestMapping(value = "smpeditor/NewSMPFile", method=RequestMethod.GET)
  public String generateFile(@ModelAttribute("smpfile") SMPFile smpfile) {
    System.out.println("file: " + smpfile.toString());
    System.out.println("==== in generateFile ====");
    smpconverter.converter();
    return "smpeditor/NewSMPFile";
  }
  
  
  @RequestMapping(value = "/smpeditor/NewSMPFile", method=RequestMethod.POST)
  public String  postnewfile(@ModelAttribute("smpfile") SMPFile smpfile, final RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("smpfile", smpfile );
    redirectAttributes.addFlashAttribute("message", "Successfully added..");
    System.out.println("==== in postnewfile ==== " + smpfile.toString());
    return "redirect:SaveSMPFile";
  }
  
  @RequestMapping(value = "smpeditor/SaveSMPFile", method=RequestMethod.GET)
  public String saveFile(@ModelAttribute("smpfile") SMPFile smpfile) {
    System.out.println("file: " + smpfile.toString());
    System.out.println("==== in saveFile ====");
    smpconverter.converter();
    return "smpeditor/SaveSMPFile";
  }
}
