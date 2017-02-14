package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPHttp;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Alert;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFileOps;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPHttp.ReferenceCollection;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
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
//import eu.epsos.util.net.ProxyUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;

/**
 *
 * @author InÃªs Garganta
 */

@Controller
@SessionAttributes("smpdelete")
public class SMPDeleteFileController {

  org.slf4j.Logger logger = LoggerFactory.getLogger(SMPUploadFileController.class);

  @Autowired
  private Environment env;

  /**
   * Generate UploadFile page
   *
   * @param model
   * @return
   */
  @RequestMapping(value = "/smpeditor/deletesmpfile", method = RequestMethod.GET)
  public String deleteFile(Model model) {
    logger.debug("\n==== in deleteFile ====");
    model.addAttribute("smpdelete", new SMPHttp());

    return "smpeditor/deletesmpfile";
  }

  /**
   * UPLOAD files to server
   *
   * @param smpdelete
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "smpeditor/deletesmpfile", method = RequestMethod.POST)
  public String postDelete(@ModelAttribute("smpdelete") SMPHttp smpdelete, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in postDelete ====");
    model.addAttribute("smpdelete", smpdelete);

    String username = smpdelete.getServerUsername();
    String password = smpdelete.getServerPassword();
    
    String partID ="urn:ehealth:" + smpdelete.getCountry().name() + ":ncpb-idp";
    String partScheme = env.getProperty("ParticipantIdentifier.Scheme");
    String participantID = partScheme + "::" + partID;

    String urlServer = smpdelete.getSmpServer();
    if (urlServer.endsWith("/")) {
      urlServer = urlServer.substring(0, urlServer.length() - 1);
    }

    //String serviceGroupUrl = "/" + participantID;
    String serviceGroupUrl = "/" + "ehealth-actorid-qns::urn:poland:ncpb"; /*TODO: comment just for tests with swagger*/
    
    /*Removes https:// from entered by the user so it won't repeat in uri set scheme*/
    if (urlServer.startsWith("https")) {
      urlServer = urlServer.substring(8);
    }

    URI uri = null;
    try {
      uri = new URIBuilder()
              .setScheme("http") /*TODO: Needs to be changed to https*/
              .setHost(urlServer)
              .setPath(serviceGroupUrl)
              .build();
    } catch (URISyntaxException ex) {
      logger.error("\n URISyntaxException - " + ex.getMessage());
    }

    //ProxyUtil.initProxyConfiguration();

    CloseableHttpClient httpclient = HttpClients.custom()
            .useSystemProperties()
            .setProxy(new HttpHost("192.168.1.90", 8080)) /*If you want to test with proxy without NCP Environment set*/
            .build();

    //DELETE
    HttpGet httpget = new HttpGet(uri);
    UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
    try {
      httpget.addHeader(new BasicScheme().authenticate(creds, httpget, null));
    } catch (AuthenticationException ex) {
      logger.error("\n AuthenticationException - " + ex.getMessage());
      String message = env.getProperty("error.nouser"); //messages.properties
      redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
      return "redirect:/smpeditor/deletesmpfile";
    }

    CloseableHttpResponse response = null;
    try {
      response = httpclient.execute(httpget);
    } catch (IOException ex) {
      logger.error("\n IOException - " + ex.getMessage());
      String message = env.getProperty("error.server.failed"); //messages.properties
      redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
      return "redirect:/smpeditor/deletesmpfile";
    }

    /*Get response*/
    smpdelete.setStatusCode(response.getStatusLine().getStatusCode());
    org.apache.http.HttpEntity entity = response.getEntity();
    
    logger.debug("\n ************ RESPONSE GET - " + response.getStatusLine().getStatusCode());

    if (smpdelete.getStatusCode() == 503 || smpdelete.getStatusCode() == 405) {
      String message = env.getProperty("error.server.failed"); //messages.properties
      redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
      return "redirect:/smpeditor/deletesmpfile";
    } else if (smpdelete.getStatusCode() == 401) {
      String message = env.getProperty("error.nouser"); //messages.properties
      redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
      return "redirect:/smpeditor/deletesmpfile";
    }

    /* Get BusinessCode and ErrorDescription from response */
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    List<ReferenceCollection> referenceCollection = new ArrayList<>();
    try {
      builder = factory.newDocumentBuilder();
      Document doc = builder.parse(entity.getContent());
      Element element = doc.getDocumentElement();
      NodeList nodes = element.getChildNodes();
      for (int j = 0; j < nodes.getLength(); j++) {
        if (nodes.item(j).getNodeName().equals("BusinessCode")) {
          String businessCode = nodes.item(j).getTextContent();
          smpdelete.setBusinessCode(businessCode);
        }
        if (nodes.item(j).getNodeName().equals("ErrorDescription")) {
          String errorDescription = nodes.item(j).getTextContent();
          smpdelete.setErrorDescription(errorDescription);
        }
        if (nodes.item(j).getNodeName().equals("ServiceMetadataReferenceCollection")) {
          NodeList references = nodes.item(j).getChildNodes();
          for (int k = 0; k < references.getLength(); k++) {
            if (references.item(k).getNodeName().equals("ServiceMetadataReference")) {
              Element attr = (Element) references.item(k);
              String href = attr.getAttribute("href");

              /*Find SMPType of href*/
              String smptype = "Unknown type";

              String[] ids = href.split("/services/");
              String document = ids[1];
              document = java.net.URLDecoder.decode(document, "UTF-8");
              String[] docId = document.split(":"); /*May change if Document Identifier specification change*/
              
              for(int n=0; n<docId.length; n++){
                logger.debug("\n******** DOC IDs- " + docId[n]);
              }

              String documentID = docId[5];/*May change if Document Identifier specification change*/
              String[] doct = document.split("##");
              documentID = doct[1];
              
              String smpType = env.getProperty(documentID); //smpeditor.properties
              logger.debug("\n******** DOC ID - " + documentID);
              logger.debug("\n******** SMP Type - " + smpType);
              SMPType[] smptypes = SMPType.getALL();
              for (int l = 0; l < smptypes.length; l++) {
                if (smptypes[l].name().equals(smpType)) {
                  smptype = smptypes[l].getDescription();
                  break;
                }
              }
              ReferenceCollection reference = new ReferenceCollection(href, smptype, k);
              referenceCollection.add(reference);
            }
          }
        }
      }
    } catch (ParserConfigurationException ex) {
      logger.error("\n ParserConfigurationException - " + ex.getMessage());
    } catch (SAXException ex) {
      logger.error("\n SAXException - " + ex.getMessage());
    } catch (IOException ex) {
      logger.error("\n IOException - " + ex.getMessage());
    }
    
    smpdelete.setReferenceCollection(referenceCollection);

    logger.debug("\n********* MODEL - " + model.toString());
    return "redirect:/smpeditor/deletesmpinfo";

  }

  /**
   * Generate deleteInfo page
   *
   * @param smpdelete
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/deletesmpinfo", method = RequestMethod.GET)
  public String deleteInfo(@ModelAttribute("smpdelete") SMPHttp smpdelete, Model model) {
    logger.debug("\n==== in deleteInfo ====");


    /* Builds html colors and alerts */
    if (smpdelete.getStatusCode() == 400) {
      String messag = env.getProperty("http.failure");//messages.properties
      Alert status = new Alert(messag, Alert.fontColor.red, "#f2dede");
      smpdelete.setStatus(status);
      if (smpdelete.getBusinessCode().equals("OTHER_ERROR")) {
        String message = "400 (OTHER_ERROR): " + env.getProperty("http.400.OTHER_ERROR");//messages.properties
        Alert alert = new Alert(message, Alert.alertType.danger);
        smpdelete.setAlert(alert);
      }
      
    } else if (smpdelete.getStatusCode() == 404) {
      String messag = env.getProperty("http.failure");//messages.properties
      Alert status = new Alert(messag, Alert.fontColor.red, "#f2dede");
      smpdelete.setStatus(status);

      String message = "404: " + env.getProperty("http.404"); //messages.properties
      Alert alert = new Alert(message, Alert.alertType.danger);
      smpdelete.setAlert(alert);
      
    } else if (smpdelete.getStatusCode() == 500) {
      String messag = env.getProperty("http.failure");//messages.properties
      Alert status = new Alert(messag, Alert.fontColor.red, "#f2dede");
      smpdelete.setStatus(status);

      String message = "500: " + env.getProperty("http.500"); //messages.properties
      Alert alert = new Alert(message, Alert.alertType.danger);
      smpdelete.setAlert(alert);
    }
    

    model.addAttribute("smpdelete", smpdelete);
    model.addAttribute("referenceCollection", smpdelete.getReferenceCollection());
   
    logger.debug("\n********* MODEL - " + model.toString());
    return "smpeditor/deletesmpinfo";
  }
  
  /**
   * deleteInfo post
   *
   * @param smpdelete
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "smpeditor/deletesmpinfo", method = RequestMethod.POST)
  public String deletePost(@ModelAttribute("smpdelete") SMPHttp smpdelete, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in deletePost ====");
    model.addAttribute("smpdelete", smpdelete);
    
    String username = smpdelete.getServerUsername();
    String password = smpdelete.getServerPassword();
    
    List<String> smptypes = new ArrayList<>();
    smptypes = smpdelete.getSmpDocTypes();
    for (int i = 0; i < smptypes.size(); i++) {
      logger.debug("\n ************** smptypes 1 - " + smptypes.get(i));
    }
    
    String host = "smp-digit-mock.publisher.ehealth.acc.edelivery.tech.ec.europa.eu/";
    
    List<String> referencesSelected = new ArrayList<>();
    referencesSelected = smpdelete.getReferenceSelected();
    
    String getServer = "";
    if (smpdelete.getSmpServer().startsWith("https")) {
      getServer = smpdelete.getSmpServer().substring(8);
    }
    
    List<SMPHttp> allItems = new ArrayList<SMPHttp>();

    /*
      Iterate through all references selected to delete
    */
    for (int i = 0; i < referencesSelected.size(); i++) {
      SMPHttp itemDelete = new SMPHttp();

      logger.debug("\n ************** smptype - " + smpdelete.getSmpDocTypes().get(i) );
      itemDelete.setSmptype(smpdelete.getSmpDocTypes().get(i));
      
      String reference = referencesSelected.get(i);
      itemDelete.setReference(reference);
      String[] ref = reference.split(getServer);
      String serviceMetadataUrl = ref[1];   
      
     /* String serviceMetadataUrl = "ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb"
              + "/services/"
              + "ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107";*/

      URI uri = null;
      try {
        uri = new URIBuilder()
                .setScheme("http") /*TODO: Needs to be changed to https*/
                .setHost(host)
                .setPath(serviceMetadataUrl)
                .build();
      } catch (URISyntaxException ex) {
        logger.error("\n URISyntaxException - " + ex.getMessage());
      }

    //ProxyUtil.initProxyConfiguration();
      CloseableHttpClient httpclient = HttpClients.custom()
              .useSystemProperties()
              .setProxy(new HttpHost("192.168.1.90", 8080)) /*If you want to test with proxy without NCP Environment set*/
              .build();

      //DELETE
      HttpDelete httpdelete = new HttpDelete(uri);
      UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
      try {
        httpdelete.addHeader(new BasicScheme().authenticate(creds, httpdelete, null));
      } catch (AuthenticationException ex) {
        logger.error("\n AuthenticationException - " + ex.getMessage());
        String message = env.getProperty("error.nouser"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/deletesmpfile";
      }

      CloseableHttpResponse response = null;
      try {
        response = httpclient.execute(httpdelete);
      } catch (IOException ex) {
        logger.error("\n IOException - " + ex.getMessage());
        String message = env.getProperty("error.server.failed"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/deletesmpfile";
      }

      /*Get response*/
      itemDelete.setStatusCode(response.getStatusLine().getStatusCode());
      org.apache.http.HttpEntity entity = response.getEntity();

      logger.debug("\n ************ RESPONSE DELETE - " + response.getStatusLine().getStatusCode());

      if (itemDelete.getStatusCode() == 503 || itemDelete.getStatusCode() == 405) {
        String message = env.getProperty("error.server.failed"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/deletesmpfile";
      } else if (itemDelete.getStatusCode() == 401) {
        String message = env.getProperty("error.nouser"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/deletesmpfile";
      }

      /* Get BusinessCode and ErrorDescription from response */
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
            itemDelete.setBusinessCode(businessCode);
          }
          if (nodes.item(j).getNodeName().equals("ErrorDescription")) {
            String errorDescription = nodes.item(j).getTextContent();
            itemDelete.setErrorDescription(errorDescription);
          }
        }
      } catch (ParserConfigurationException ex) {
        logger.error("\n ParserConfigurationException - " + ex.getMessage());
      } catch (SAXException ex) {
        logger.error("\n SAXException - " + ex.getMessage());
      } catch (IOException ex) {
        logger.error("\n IOException - " + ex.getMessage());
      }
      
      itemDelete.setId(i);
      allItems.add(i, itemDelete);
    }
    smpdelete.setAllItems(allItems);

    logger.debug("\n********* MODEL - " + model.toString());
    return "redirect:/smpeditor/deletesmpresult";
  }
  
  
  /**
   * Generate deleteInfo page
   *
   * @param smpdelete
   * @param model
   * @return
   */
  @RequestMapping(value = "smpeditor/deletesmpresult", method = RequestMethod.GET)
  public String postInfo(@ModelAttribute("smpdelete") SMPHttp smpdelete, Model model) {
    logger.debug("\n==== in postInfo ====");

    for (int i = 0; i < smpdelete.getSmpDocTypes().size(); i++) {
      logger.debug("\n ************** smptypes - " + smpdelete.getSmpDocTypes().get(i) );
    }
    
    /* Builds html colors and alerts */
    for (int i = 0; i < smpdelete.getAllItems().size(); i++) {
      if (smpdelete.getAllItems().get(i).getStatusCode() == 200) {
        String message = env.getProperty("http.deleted");//messages.properties
        Alert status = new Alert(message, Alert.fontColor.green, "#dff0d8");
        smpdelete.getAllItems().get(i).setStatus(status);

      } else if (smpdelete.getAllItems().get(i).getStatusCode() == 400) {
        String messag = env.getProperty("http.failure");//messages.properties
        Alert status = new Alert(messag, Alert.fontColor.red, "#f2dede");
        smpdelete.getAllItems().get(i).setStatus(status);
        if (smpdelete.getAllItems().get(i).getBusinessCode().equals("OTHER_ERROR")) {
          String message = "400 (OTHER_ERROR): " + env.getProperty("http.400.OTHER_ERROR");//messages.properties
          Alert alert = new Alert(message, Alert.alertType.danger);
          smpdelete.getAllItems().get(i).setAlert(alert);
        }

      } else if (smpdelete.getAllItems().get(i).getStatusCode() == 404) {
        String messag = env.getProperty("http.failure");//messages.properties
        Alert status = new Alert(messag, Alert.fontColor.red, "#f2dede");
        smpdelete.getAllItems().get(i).setStatus(status);

        String message = "404: " + env.getProperty("http.404"); //messages.properties
        Alert alert = new Alert(message, Alert.alertType.danger);
        smpdelete.getAllItems().get(i).setAlert(alert);

      } else if (smpdelete.getAllItems().get(i).getStatusCode() == 500) {
        String messag = env.getProperty("http.failure");//messages.properties
        Alert status = new Alert(messag, Alert.fontColor.red, "#f2dede");
        smpdelete.getAllItems().get(i).setStatus(status);

        String message = "500: " + env.getProperty("http.500"); //messages.properties
        Alert alert = new Alert(message, Alert.alertType.danger);
        smpdelete.getAllItems().get(i).setAlert(alert);
      }
    }
    model.addAttribute("smpdelete", smpdelete);
    model.addAttribute("items", smpdelete.getAllItems());
    

    logger.debug("\n********* MODEL - " + model.toString());
    return "smpeditor/deletesmpresult";
  }
}
