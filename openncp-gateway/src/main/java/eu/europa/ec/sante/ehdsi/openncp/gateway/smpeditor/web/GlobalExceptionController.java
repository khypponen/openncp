package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.exception.GenericException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {
  
  /**
   * Manage Custom Exceptions
   */
  @ExceptionHandler(GenericException.class)
  public String handleCustomException(GenericException ex, Model model) {
    model.addAttribute("code", ex.getCode());
    model.addAttribute("message", ex.getMessage());
    return "smpeditor/error/error";
  }

  /**
   * Manage all exceptions
   */
  @ExceptionHandler(Exception.class)
  public String handleAllException(Exception ex, Model model) {
    model.addAttribute("message", "An error occurred. For more information check the log file.");
    return "smpeditor/error/error";
  }
  
}
