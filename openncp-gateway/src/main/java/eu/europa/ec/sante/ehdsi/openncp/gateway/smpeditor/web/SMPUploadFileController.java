package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPUpload;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Alert;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SMPConverter;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.XMLValidator;
import java.io.File;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Controller
@SessionAttributes("smpupload")
public class SMPUploadFileController {

  org.slf4j.Logger logger = LoggerFactory.getLogger(SMPUploadFileController.class);

  @Autowired
  private final SMPConverter smpconverter = new SMPConverter();
  @Autowired
  private final XMLValidator xmlValidator = new XMLValidator();
  @Autowired
  private Environment env;

  /**
   * Generate UploadFile page
   *
   * @param model
   * @return
   */
  @RequestMapping(value = "/smpeditor/uploadsmpfile", method = RequestMethod.GET)
  public String uploadFile(Model model) {
    logger.debug("\n==== in uploadFile ====");
    model.addAttribute("smpupload", new SMPUpload());

    return "smpeditor/uploadsmpfile";
  }

  /**
   * UPLOAD files to server
   *
   * @param smpupload
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "smpeditor/uploadsmpfile", method = RequestMethod.POST)
  public String postUpload(@ModelAttribute("smpupload") SMPUpload smpupload, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in postUpload ====");
    model.addAttribute("smpupload", smpupload);
    
    /*Iterate through all chosen files*/
    List<SMPUpload> allItems = new ArrayList<SMPUpload>();
    for (int i = 0; i < smpupload.getUploadFiles().size(); i++) {
      SMPUpload itemUpload = new SMPUpload();

      itemUpload.setUploadFileName(smpupload.getUploadFiles().get(i).getOriginalFilename());

      File convFile = new File("/" + smpupload.getUploadFiles().get(i).getOriginalFilename());
      try {
        smpupload.getUploadFiles().get(i).transferTo(convFile);
      } catch (IOException ex) {
        logger.error("\n IOException - " + ex.getMessage());
      } catch (IllegalStateException ex) {
        logger.error("\n IllegalStateException - " + ex.getMessage());
      }

      boolean valid = xmlValidator.validator(convFile.getPath());
      if (valid) {
        logger.debug("\n****VALID XML File");
      } else {
        logger.debug("\n****NOT VALID XML File");
        String message = env.getProperty("error.notsmp"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        convFile.delete();
        return "redirect:/smpeditor/uploadsmpfile";
      }
      convFile.delete();
      
      String username = smpupload.getServerUsername();
      String password = smpupload.getServerPassword();

      ServiceMetadata serviceMetadata = smpconverter.convertFromXml(smpupload.getUploadFiles().get(i));
      
      boolean isSigned = smpconverter.getIsSignedServiceMetadata();
      if (isSigned) {
        logger.debug("\n****SIGNED SMP File");
        convFile.delete();
        String message = env.getProperty("warning.isSigned.sigmenu"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.warning));
        return "redirect:/smpeditor/uploadsmpfile";
      } else {
        logger.debug("\n****NOT SIGNED File");
      }

      String participantID = "";
      String documentTypeID = "";

      if (serviceMetadata.getRedirect() != null) {
        logger.debug("\n******** REDIRECT");

        if (serviceMetadata.getRedirect().getExtensions().isEmpty()) {
          logger.error("\n******* NOT SIGNED EXTENSION ");
          String message = env.getProperty("error.notsigned"); //messages.properties
          redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
          return "redirect:/smpeditor/uploadsmpfile";
        }

        String href = serviceMetadata.getRedirect().getHref();
        Pattern pattern = Pattern.compile("ehealth-actorid-qns.*");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
          String result = matcher.group(0);
          try {
            result = java.net.URLDecoder.decode(result, "UTF-8");
          } catch (UnsupportedEncodingException ex) {
            logger.error("\n UnsupportedEncodingException - " + ex.getMessage());
          }
          String[] ids = result.split("/services/");
          participantID = ids[0];
          documentTypeID = ids[1];
        } else {
          logger.error("\n****NOT VALID HREF IN REDIRECT");
          String message = env.getProperty("error.redirect.href"); //messages.properties
          redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
          return "redirect:/smpeditor/uploadsmpfile";
        }

      } else if (serviceMetadata.getServiceInformation() != null) {
        logger.debug("\n******** SERVICE INFORMATION");

        if (serviceMetadata.getServiceInformation().getExtensions().isEmpty()) {
          logger.error("\n******* NOT SIGNED EXTENSION ");
          String message = env.getProperty("error.notsigned"); //messages.properties
          redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
          return "redirect:/smpeditor/uploadsmpfile";
        }

        String partID = serviceMetadata.getServiceInformation().getParticipantIdentifier().getValue();
        String partScheme = serviceMetadata.getServiceInformation().getParticipantIdentifier().getScheme();
        participantID = partScheme + "::" + partID;

        String docID = serviceMetadata.getServiceInformation().getDocumentIdentifier().getValue();
        String docScheme = serviceMetadata.getServiceInformation().getDocumentIdentifier().getScheme();
        documentTypeID = docScheme + "::" + docID;
      }

      String urlServer = smpupload.getSmpServer();
      if (urlServer.endsWith("/")) {
        urlServer = urlServer.substring(0, urlServer.length() - 1);
      }

      String serviceGroup = urlServer + "/" + participantID;
      itemUpload.setServiceGroupUrl(serviceGroup);
      String serviceMetdataUrl = "/" + participantID + "/services/" + documentTypeID;
      itemUpload.setSignedServiceMetadataUrl(urlServer + serviceMetdataUrl);

      if (urlServer.startsWith("http")) {
        urlServer = urlServer.substring(7);
      }

      URI uri = null;
      try {
        uri = new URIBuilder()
                .setScheme("http")
                .setHost(urlServer)
                .setPath(serviceMetdataUrl)
                .build();
      } catch (URISyntaxException ex) {
        logger.error("\n URISyntaxException - " + ex.getMessage());
      }

      String content = "";
      try {
        content = new Scanner(smpupload.getUploadFiles().get(i).getInputStream()).useDelimiter("\\Z").next();
      } catch (IOException ex) {
        logger.error("\n IOException - " + ex.getMessage());
      }

      StringEntity entityPut = new StringEntity(content, ContentType.create("application/xml", "UTF-8"));

      CloseableHttpClient httpclient = HttpClients.custom()
              .useSystemProperties()
              .setProxy(new HttpHost("192.168.1.90", 8080))
              .build();
      //PUT
      HttpPut httpput = new HttpPut(uri);
      UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
      try {
        httpput.addHeader(new BasicScheme().authenticate(creds, httpput, null));
      } catch (AuthenticationException ex) {
        logger.error("\n AuthenticationException - " + ex.getMessage());
      }
      httpput.setEntity(entityPut);
      CloseableHttpResponse response = null;
      try {
        response = httpclient.execute(httpput);
      } catch (IOException ex) {
        logger.error("\n IOException - " + ex.getMessage());
        String message = env.getProperty("error.server.failed"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/uploadsmpfile";
      }

      itemUpload.setStatusCode(response.getStatusLine().getStatusCode());
      org.apache.http.HttpEntity entity = response.getEntity();

      if (itemUpload.getStatusCode() == 404 || itemUpload.getStatusCode() == 503 || itemUpload.getStatusCode() == 405) {
        String message = env.getProperty("error.server.failed"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/uploadsmpfile";
      } else if (itemUpload.getStatusCode() == 401) {
        String message = env.getProperty("error.nouser"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/uploadsmpfile";
      }

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder;
      try {
        builder = factory.newDocumentBuilder();
        Document doc = builder.parse(entity.getContent());
        Element element = doc.getDocumentElement();
        NodeList nodes = element.getChildNodes();
        for (int j = 0; j < nodes.getLength(); j++) {
          if (nodes.item(j).getNodeName().equals("BusinessCode")) {
            String businessCode = nodes.item(j).getTextContent();
            itemUpload.setBusinessCode(businessCode);
          }
          if (nodes.item(j).getNodeName().equals("ErrorDescription")) {
            String errorDescription = nodes.item(j).getTextContent();
            itemUpload.setErrorDescription(errorDescription);
          }
        }
      } catch (ParserConfigurationException ex) {
        logger.error("\n ParserConfigurationException - " + ex.getMessage());
      } catch (SAXException ex) {
        logger.error("\n SAXException - " + ex.getMessage());
      } catch (IOException ex) {
        logger.error("\n IOException - " + ex.getMessage());
      }

      itemUpload.setId(i);
      allItems.add(i, itemUpload);
    }
    smpupload.setAllItems(allItems);

    logger.debug("\n********* MODEL - " + model.toString());
    return "redirect:/smpeditor/uploadsmpinfo";

  }

  /**
   * Generate uploadInfo page
   *
   * @param smpupload
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/uploadsmpinfo", method = RequestMethod.GET)
  public String uploadInfo(@ModelAttribute("smpupload") SMPUpload smpupload, Model model) {
    logger.debug("\n==== in uploadInfo ====");
    model.addAttribute("smpupload", smpupload);

    for (int i = 0; i < smpupload.getAllItems().size(); i++) {
      if (smpupload.getAllItems().get(i).getStatusCode() == 200) {
        String message = env.getProperty("http.updated");//messages.properties
        Alert status = new Alert(message, Alert.fontColor.green, "#dff0d8");
        smpupload.getAllItems().get(i).setStatus(status);
        
      } else if(smpupload.getAllItems().get(i).getStatusCode() == 201){
        String message = env.getProperty("http.created");//messages.properties
        Alert status = new Alert(message, Alert.fontColor.green, "#dff0d8");
        smpupload.getAllItems().get(i).setStatus(status);
        
      } else if (smpupload.getAllItems().get(i).getStatusCode() == 400) {
        String messag = env.getProperty("http.failure");//messages.properties
        Alert status = new Alert(messag, Alert.fontColor.red, "#f2dede");
        smpupload.getAllItems().get(i).setStatus(status);
        
        if (smpupload.getAllItems().get(i).getBusinessCode().equals("XSD_INVALID")) {
          String message = "400 (XSD_INVALID): " + env.getProperty("http.400.XSD_INVALID");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.danger);
          smpupload.getAllItems().get(i).setAlert(alert);
        } else if (smpupload.getAllItems().get(i).getBusinessCode().equals("MISSING_FIELD")) {
          String message = "400 (MISSING_FIELD): " + env.getProperty("http.400.MISSING_FIELD");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.danger);
          smpupload.getAllItems().get(i).setAlert(alert);
        } else if (smpupload.getAllItems().get(i).getBusinessCode().equals("WRONG_FIELD")) {
          String message = "400 (WRONG_FIELD): " + env.getProperty("http.400.WRONG_FIELD");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.danger);
          smpupload.getAllItems().get(i).setAlert(alert);
        } else if (smpupload.getAllItems().get(i).getBusinessCode().equals("OUT_OF_RANGE")) {
          String message = "400 (OUT_OF_RANGE): " + env.getProperty("http.400.OUT_OF_RANGE");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.danger);
          smpupload.getAllItems().get(i).setAlert(alert);
        } else if (smpupload.getAllItems().get(i).getBusinessCode().equals("UNAUTHOR_FIELD")) {
          String message = "400 (UNAUTHOR_FIELD): " + env.getProperty("http.400.UNAUTHOR_FIELD");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.danger);
          smpupload.getAllItems().get(i).setAlert(alert);
        } else if (smpupload.getAllItems().get(i).getBusinessCode().equals("FORMAT_ERROR")) {
          String message = "400 (FORMAT_ERROR): " + env.getProperty("http.400.FORMAT_ERROR");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.danger);
          smpupload.getAllItems().get(i).setAlert(alert);
        } else if (smpupload.getAllItems().get(i).getBusinessCode().equals("OTHER_ERROR")) {
          String message = "400 (OTHER_ERROR): " + env.getProperty("http.400.OTHER_ERROR");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.danger);
          smpupload.getAllItems().get(i).setAlert(alert);
        }
      } else if (smpupload.getAllItems().get(i).getStatusCode() == 500) {
        String messag = env.getProperty("http.failure");//messages.properties
        Alert status = new Alert(messag, Alert.fontColor.red, "#f2dede");
        smpupload.getAllItems().get(i).setStatus(status);
        
        String message = "500: " + env.getProperty("http.500"); //messages.properties
        Alert alert = new Alert(message, Alert.alertType.danger);
        smpupload.getAllItems().get(i).setAlert(alert);
      }
    }

    model.addAttribute("items", smpupload.getAllItems());

    logger.debug("\n********* MODEL - " + model.toString());
    return "smpeditor/uploadsmpinfo";
  }
}
