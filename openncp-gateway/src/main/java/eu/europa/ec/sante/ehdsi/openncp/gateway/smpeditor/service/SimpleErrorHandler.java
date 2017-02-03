package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service;

import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SimpleErrorHandler implements ErrorHandler {
  org.slf4j.Logger logger = LoggerFactory.getLogger(SimpleErrorHandler.class);
  
  public void warning(SAXParseException e) throws SAXException {
      logger.error(e.getMessage());
    }

    public void error(SAXParseException e) throws SAXException {
      logger.error(e.getMessage());
    }

    public void fatalError(SAXParseException e) throws SAXException {
       logger.error(e.getMessage());
    }
  
}
