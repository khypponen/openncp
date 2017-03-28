package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Inês Garganta
 */

@Controller
public class SMPEditorController {

  /**
   * Generates smpeditor page
   * @return
   */
  @RequestMapping("/smpeditor/smpeditor")
  public String SMPEditor() {
    return "smpeditor/smpeditor";
  }

}
