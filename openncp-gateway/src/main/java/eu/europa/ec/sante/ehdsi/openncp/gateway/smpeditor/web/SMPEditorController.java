package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SMPEditorController {

  @RequestMapping("/smpeditor/smpeditor")
  public String SMPEditor() {
    return "smpeditor/smpeditor";
  }

}
