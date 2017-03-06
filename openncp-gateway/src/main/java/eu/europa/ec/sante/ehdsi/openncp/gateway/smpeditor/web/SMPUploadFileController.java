package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.util.net.ProxyCredentials;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPHttp;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Alert;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SMPConverter;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.CustomProxy;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SimpleErrorHandler;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.XMLValidator;
import java.io.File;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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
import eu.epsos.util.net.ProxyUtil;
import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.Audit;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContexts;

/**
 *
 * @author InÃªs Garganta
 */

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
    model.addAttribute("smpupload", new SMPHttp());

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
  public String postUpload(@ModelAttribute("smpupload") SMPHttp smpupload, Model model, final RedirectAttributes redirectAttributes) {
    logger.debug("\n==== in postUpload ====");
    model.addAttribute("smpupload", smpupload);
    
    /*Iterate through all chosen files*/
    List<SMPHttp> allItems = new ArrayList<SMPHttp>();
    for (int i = 0; i < smpupload.getUploadFiles().size(); i++) {
      SMPHttp itemUpload = new SMPHttp();

      itemUpload.setUploadFileName(smpupload.getUploadFiles().get(i).getOriginalFilename());

      File convFile = new File("/" + smpupload.getUploadFiles().get(i).getOriginalFilename());
      try {
        smpupload.getUploadFiles().get(i).transferTo(convFile);
      } catch (IOException ex) {
        logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      } catch (IllegalStateException ex) {
        logger.error("\n IllegalStateException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
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
      String partID="";
      String partScheme="";
      String docID = "";
      String docScheme = "";

      if (serviceMetadata.getRedirect() != null) {
        logger.debug("\n******** REDIRECT");

        if (serviceMetadata.getRedirect().getExtensions().isEmpty()) {
          logger.error("\n******* NOT SIGNED EXTENSION (National Authority signature)");
          String message = env.getProperty("error.notsigned"); //messages.properties
          redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
          return "redirect:/smpeditor/uploadsmpfile";
        }

        /* Check if url of redirect is correct */
        String href = serviceMetadata.getRedirect().getHref();
        Pattern pattern = Pattern.compile("ehealth-participantid-qns.*");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
          String result = matcher.group(0);
          try {
            result = java.net.URLDecoder.decode(result, "UTF-8");
          } catch (UnsupportedEncodingException ex) {
            logger.error("\n UnsupportedEncodingException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
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

        partID = serviceMetadata.getServiceInformation().getParticipantIdentifier().getValue();
        partScheme = serviceMetadata.getServiceInformation().getParticipantIdentifier().getScheme();
        participantID = partScheme + "::" + partID;

        docID = serviceMetadata.getServiceInformation().getDocumentIdentifier().getValue();
        docScheme = serviceMetadata.getServiceInformation().getDocumentIdentifier().getScheme();
        documentTypeID = docScheme + "::" + docID;
      }

      String urlServer = ConfigurationManagerService.getInstance().getProperty("SMP_ADMIN_URL");
      if (urlServer.endsWith("/")) {
        urlServer = urlServer.substring(0, urlServer.length() - 1);
      }
      
      String serviceMetdataUrl = "/" + participantID + "/services/" + documentTypeID;

      /*Removes https:// from entered by the user so it won't repeat in uri set scheme*/
      if (urlServer.startsWith("https")) {
        urlServer = urlServer.substring(8);
      }
      

      URI uri = null;
      try {
        uri = new URIBuilder()
                .setScheme("https")
                .setHost(urlServer)
                .setPath(serviceMetdataUrl)
                .build();
      } catch (URISyntaxException ex) {
        logger.error("\n URISyntaxException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      }
      
      logger.debug("\n ************** URI - " + uri);

      String content = "";
      try {
        content = new Scanner(smpupload.getUploadFiles().get(i).getInputStream()).useDelimiter("\\Z").next();
      } catch (IOException ex) {
        logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      }

      StringEntity entityPut = new StringEntity(content, ContentType.create("application/xml", "UTF-8"));
      
      PrivateKeyStrategy privatek = new PrivateKeyStrategy() {
        @Override
        public String chooseAlias(Map<String, PrivateKeyDetails> map, Socket socket) {
          return ConfigurationManagerService.getInstance().getProperty("SC_SMP_CLIENT_PRIVATEKEY_ALIAS");
        }
      };

      
      // Trust own CA and all self-signed certs
      SSLContext sslcontext = null;
      try {
        sslcontext = SSLContexts.custom()
                .loadKeyMaterial(new File(ConfigurationManagerService.getInstance().getProperty("SC_KEYSTORE_PATH")), 
                        ConfigurationManagerService.getInstance().getProperty("SC_KEYSTORE_PASSWORD").toCharArray(), 
                        ConfigurationManagerService.getInstance().getProperty("SC_SMP_CLIENT_PRIVATEKEY_PASSWORD").toCharArray(), //must be the same as SC_KEYSTORE_PASSWORD
                        privatek)
                .loadTrustMaterial(new File(ConfigurationManagerService.getInstance().getProperty("TRUSTSTORE_PATH")), 
                        ConfigurationManagerService.getInstance().getProperty("TRUSTSTORE_PASSWORD").toCharArray(), 
                        new TrustSelfSignedStrategy())
                .build();
      } catch (NoSuchAlgorithmException ex) {
        logger.error("\n NoSuchAlgorithmException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      } catch (KeyStoreException ex) {
        logger.error("\n KeyStoreException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      } catch (CertificateException ex) {
        logger.error("\n CertificateException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      } catch (IOException ex) {
        logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      } catch (KeyManagementException ex) {
        logger.error("\n KeyManagementException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      } catch (UnrecoverableKeyException ex) {
        logger.error("\n UnrecoverableKeyException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      }
     
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
              sslcontext,
              new String[]{"TLSv1"},  // Allow TLSv1 protocol only
              null,
              /*SSLConnectionSocketFactory.getDefaultHostnameVerifier()*/
              new NoopHostnameVerifier());
      
      ProxyCredentials proxyCredentials = null;
      if (ProxyUtil.isProxyAnthenticationMandatory()) {
        proxyCredentials = ProxyUtil.getProxyCredentials();
      }
      CloseableHttpClient httpclient;
      if (proxyCredentials != null) {

        if (proxyCredentials.getProxyUser() != null) {
          CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
          credentialsProvider.setCredentials(
                  new AuthScope(proxyCredentials.getProxyHost(), Integer.parseInt(proxyCredentials.getProxyPort())),
                  new UsernamePasswordCredentials(proxyCredentials.getProxyUser(), proxyCredentials.getProxyPassword()));

          httpclient = HttpClients.custom()
                  .setDefaultCredentialsProvider(credentialsProvider)
                  .setSSLSocketFactory(sslsf)
                  .setProxy(new HttpHost(proxyCredentials.getProxyHost(), Integer.parseInt(proxyCredentials.getProxyPort())))
                  .build();
        } else {
          httpclient = HttpClients.custom()
                  .setSSLSocketFactory(sslsf)
                  .setProxy(new HttpHost(proxyCredentials.getProxyHost(), Integer.parseInt(proxyCredentials.getProxyPort())))
                  .build();
        }

      } else {
        httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
      }


      //PUT
      HttpPut httpput = new HttpPut(uri);
      httpput.setEntity(entityPut);
      CloseableHttpResponse response = null;
      try {
        response = httpclient.execute(httpput);
      } catch (IOException ex) {
        logger.error("\n IOException response - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        String message = env.getProperty("error.server.failed"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/uploadsmpfile";
      }

      /*Get response*/
      itemUpload.setStatusCode(response.getStatusLine().getStatusCode());
      org.apache.http.HttpEntity entity = response.getEntity();
      
      logger.debug("\n ********** response status code - " + response.getStatusLine().getStatusCode());
      logger.debug("\n ********** response reason - " + response.getStatusLine().getReasonPhrase());
      
      //Audit vars
      String ncp = ConfigurationManagerService.getInstance().getProperty("ncp.country");
      String ncpemail = ConfigurationManagerService.getInstance().getProperty("ncp.email");
      String country = ConfigurationManagerService.getInstance().getProperty("COUNTRY_PRINCIPAL_SUBDIVISION");
      String localip = ConfigurationManagerService.getInstance().getProperty("SMP_ADMIN_URL");//Source Gateway
      String remoteip = ConfigurationManagerService.getInstance().getProperty("SERVER_IP");//Target Gateway
      String smp = ConfigurationManagerService.getInstance().getProperty("SMP_SUPPORT");
      String smpemail = ConfigurationManagerService.getInstance().getProperty("SMP_SUPPORT_EMAIL");
      //ET_ObjectID --> Base64 of url
      String objectID = uri.toString(); //ParticipantObjectID
      byte[] encodedObjectID = Base64.encodeBase64(objectID.getBytes());

      if (itemUpload.getStatusCode() == 404 || itemUpload.getStatusCode() == 503 || itemUpload.getStatusCode() == 405) {
        String message = env.getProperty("error.server.failed"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        
        byte[] encodedObjectDetail = Base64.encodeBase64(response.getStatusLine().getReasonPhrase().getBytes());
         Audit.sendAuditPush(smp, smpemail, ncp, ncpemail, country ,localip, remoteip, 
              new String(encodedObjectID), Integer.toString(response.getStatusLine().getStatusCode()), encodedObjectDetail);
         
        return "redirect:/smpeditor/uploadsmpfile";
      } else if (itemUpload.getStatusCode() == 401) {
        String message = env.getProperty("error.nouser"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        
        byte[] encodedObjectDetail = Base64.encodeBase64(response.getStatusLine().getReasonPhrase().getBytes());
         Audit.sendAuditPush(smp, smpemail, ncp, ncpemail, country ,localip, remoteip, 
              new String(encodedObjectID), Integer.toString(response.getStatusLine().getStatusCode()), encodedObjectDetail);
        
        return "redirect:/smpeditor/uploadsmpfile";
      }
      

      if (!(itemUpload.getStatusCode() == 200 || itemUpload.getStatusCode() == 201)) {
        /* Get BusinessCode and ErrorDescription from response */
        
        //Save InputStream of response in ByteArrayOutputStream in order to read it more than once.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
          org.apache.commons.io.IOUtils.copy(entity.getContent(), baos);
        } catch (IOException ex) {
          logger.error("\n IOException response - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (UnsupportedOperationException ex) {
          logger.error("\n UnsupportedOperationException response - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        }
        byte[] bytes = baos.toByteArray();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
          ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
          builder = factory.newDocumentBuilder();
          Document doc = builder.parse(bais);
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
          logger.error("\n ParserConfigurationException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (SAXException ex) {
          logger.error("\n SAXException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (IOException ex) {
          logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        }
    
        /*transform xml to string in order to send in Audit*/
        StringWriter sw = new StringWriter();
        try {
          ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
          builder = factory.newDocumentBuilder();
          Document doc = builder.parse(bais);
          TransformerFactory tf = TransformerFactory.newInstance();
          Transformer transformer;
          transformer = tf.newTransformer();
          transformer.transform(new DOMSource(doc), new StreamResult(sw));
        } catch (TransformerConfigurationException ex) {
          logger.error("\n TransformerConfigurationException response - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (TransformerException ex) {
          logger.error("\n TransformerException response - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (ParserConfigurationException ex) {
          logger.error("\n ParserConfigurationException response - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (UnsupportedOperationException ex) {
          logger.error("\n UnsupportedOperationException response - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (SAXException ex) {
          logger.error("\n SAXException response - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (IOException ex) {
          logger.error("\n IOException response - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        }
        String errorresult = sw.toString();
        logger.debug("\n ***************** ERROR RESULT - " + errorresult);
        //Audit error
        Audit.sendAuditPush(smp, smpemail, ncp, ncpemail, country, localip, remoteip,
                new String(encodedObjectID), Integer.toString(response.getStatusLine().getStatusCode()), errorresult.getBytes());
      }

      if (itemUpload.getStatusCode() == 200 || itemUpload.getStatusCode() == 201) {
        //Audit Success
        Audit.sendAuditPush(smp, smpemail, ncp, ncpemail, country, localip, remoteip,
                new String(encodedObjectID), null, null);
      }
      
      //GET
      Boolean success = true;
      String errorType = "";
      ParticipantIdentifier participantIdentifier = new ParticipantIdentifier(partID, partScheme);
      DocumentIdentifier documentIdentifier = new DocumentIdentifier(docID, docScheme);
      DynamicDiscovery smpClient = null;

      if (proxyCredentials != null) {
        try {
          smpClient = DynamicDiscoveryBuilder.newInstance()
                  .locator(new DefaultBDXRLocator(ConfigurationManagerService.getInstance().getProperty("SML_DOMAIN")))
                  .fetcher(new DefaultURLFetcher(new CustomProxy(proxyCredentials.getProxyHost(), Integer.parseInt(proxyCredentials.getProxyPort()), proxyCredentials.getProxyUser(), proxyCredentials.getProxyPassword())))
                  .build();
        } catch (ConnectionException ex) {
          success = false;
          errorType = "ConnectionException";
          logger.error("\n ConnectionException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        }
      } else {
        smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator(ConfigurationManagerService.getInstance().getProperty("SML_DOMAIN")))
                .build();
      }
      URI smpURI = null;
      try {
        smpURI = smpClient.getService().getMetadataLocator().lookup(participantIdentifier);
      } catch (TechnicalException ex) {
        success = false;
        errorType = "TechnicalException";
        logger.error("\n TechnicalException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      }

      URI serviceGroup = smpClient.getService().getMetadataProvider().resolveDocumentIdentifiers(smpURI, participantIdentifier);
      URI serviceMetadataUri = smpClient.getService().getMetadataProvider().resolveServiceMetadata(smpURI, participantIdentifier, documentIdentifier);  
      
      itemUpload.setServiceGroupUrl(serviceGroup.toString());
      itemUpload.setSignedServiceMetadataUrl(serviceMetadataUri.toString());
      
      if(success){
        localip = smpURI.toString();//Source Gateway
        objectID = serviceMetadataUri.toString(); //ParticipantObjectID
        encodedObjectID = Base64.encodeBase64(objectID.getBytes());
        //Audit Success
        Audit.sendAuditQuery(smp, smpemail, ncp, ncpemail, country, localip, remoteip,
                new String(encodedObjectID), null, null);
      }else {
        localip = smpURI.toString();//Source Gateway
        objectID = serviceMetadataUri.toString(); //ParticipantObjectID
        encodedObjectID = Base64.encodeBase64(objectID.getBytes());
        //Audit Error
        Audit.sendAuditQuery(smp, smpemail, ncp, ncpemail, country, localip, remoteip,
                new String(encodedObjectID), "500", errorType.getBytes());//TODO
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
  public String uploadInfo(@ModelAttribute("smpupload") SMPHttp smpupload, Model model) {
    logger.debug("\n==== in uploadInfo ====");
    model.addAttribute("smpupload", smpupload);

    /* Builds html colors and alerts */
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
