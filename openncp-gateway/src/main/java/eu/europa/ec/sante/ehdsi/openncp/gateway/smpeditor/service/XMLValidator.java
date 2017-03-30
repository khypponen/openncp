package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

/**
 *
 * @author Inês Garganta
 */

/**
 Service responsible for validate the generated xml file againts the xsd file.
 */
@Service
public class XMLValidator {
  
  org.slf4j.Logger logger = LoggerFactory.getLogger(XMLValidator.class);

  private String reasonInvalid;
  
  public String getReasonInvalid() {
    return reasonInvalid;
  }

  public void setReasonInvalid(String reasonInvalid) {
    this.reasonInvalid = reasonInvalid;
  }
 
  public boolean validator(String XMLFileSource) {
    logger.debug("\n====== XMLFileSource - " + XMLFileSource);
    
    boolean valid = true;  
    
    ClassLoader classLoader = getClass().getClassLoader();
    File schemaFile = new File(classLoader.getResource("/src/main/resources/smpeditor/bdx-smp-201605.xsd").getFile()); //SPECIFICATION
    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Source xmlFile = new StreamSource(new File(XMLFileSource));
    try {
      Schema schema = schemaFactory.newSchema(schemaFile);
      Validator validator = schema.newValidator();
      validator.validate(xmlFile);
      logger.debug("\n" + xmlFile.getSystemId() + " is valid");
    } catch (SAXException ex) {
      valid = false;
      reasonInvalid = ex.getMessage();
      logger.debug("\n" +xmlFile.getSystemId() + " is NOT valid reason:" + ex);
      logger.error("\n SAXException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    } catch (IOException ex) {
      valid = false;
      logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    
    return valid;
  }
}
