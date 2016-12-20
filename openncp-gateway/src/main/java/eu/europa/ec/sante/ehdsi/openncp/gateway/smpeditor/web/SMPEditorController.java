/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SMPEditorController {

    @RequestMapping("/smpeditor/SMPEditor")
    public String SMPEditor() {
        return "smpeditor/SMPEditor";
    }

}