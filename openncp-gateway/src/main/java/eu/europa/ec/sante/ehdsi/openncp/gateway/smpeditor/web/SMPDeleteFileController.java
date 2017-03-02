package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.web;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.util.net.ProxyCredentials;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPHttp;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.Alert;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPHttp.ReferenceCollection;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPType;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.SimpleErrorHandler;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
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
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.Audit;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.CustomProxy;
import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service.ReadSMPProperties;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContexts;

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
  @Autowired
  private ReadSMPProperties readProperties = new ReadSMPProperties();

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
   
    String partID ="urn:ehealth:" + smpdelete.getCountry().name() + ":ncpb-idp"; //SPECIFICATION
    String partScheme = env.getProperty("ParticipantIdentifier.Scheme");
    String participantID = partScheme + "::" + partID;
    
     

   /* ParticipantIdentifier participantIdentifier = new ParticipantIdentifier(partID, partScheme);
    DynamicDiscovery smpClient = null;

    if (Boolean.valueOf(proxyCredentials.getProxyAuthenticated())) {
      try {
        smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator(ConfigurationManagerService.getInstance().getProperty("SML_DOMAIN")))
                .fetcher(new DefaultURLFetcher(new CustomProxy(proxyCredentials.getProxyHost(), Integer.parseInt(proxyCredentials.getProxyPort()), proxyCredentials.getProxyUser(), proxyCredentials.getProxyPassword())))
                .build();
      } catch (ConnectionException ex) {
        logger.error("\n ConnectionException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      }
    } else {
      smpClient = DynamicDiscoveryBuilder.newInstance()
              .locator(new DefaultBDXRLocator(ConfigurationManagerService.getInstance().getProperty("SML_DOMAIN")))
              .build();
    }
    List<DocumentIdentifier> documentIdentifiers = new ArrayList<>();
    try {
      documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);
    } catch (TechnicalException ex) {
      logger.error("\n TechnicalException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    logger.debug("\n ********* documentIdentifiers - " + documentIdentifiers.toString());
    
    List<ReferenceCollection> referenceCollection = new ArrayList<>();
    for (int i = 0; i < documentIdentifiers.size(); i++) {
      String smptype = "Unknown type";
      String smpType = env.getProperty(documentIdentifiers.get(i).getIdentifier()); //smpeditor.properties
      logger.debug("\n******** DOC ID - " + documentIdentifiers.get(i).getIdentifier());
      logger.debug("\n******** SMP Type - " + smpType);
      SMPType[] smptypes = SMPType.getALL();
      for (int l = 0; l < smptypes.length; l++) {
        if (smptypes[l].name().equals(smpType)) {
          smptype = smptypes[l].getDescription();
          break;
        }
      }
      ReferenceCollection reference = new ReferenceCollection(documentIdentifiers.get(i).getIdentifier(), smptype, i);
      referenceCollection.add(reference);
    }*/

    String urlServer = ConfigurationManagerService.getInstance().getProperty("SMP_URL");
    if (urlServer.endsWith("/")) {
      urlServer = urlServer.substring(0, urlServer.length() - 1);
    }

    String serviceGroupUrl = "/" + participantID;
    //String serviceGroupUrl = "/" + "ehealth-actorid-qns::urn:poland:ncpb"; /*just for tests with swagger mock services*/
    
    //Removes https:// from entered by the user so it won't repeat in uri set scheme*/
    if (urlServer.startsWith("https")) {
      urlServer = urlServer.substring(8);
    }

    URI uri = null;
    try {
      uri = new URIBuilder()
              .setScheme("https")
              .setHost(urlServer)
              .setPath(serviceGroupUrl)
              .build();
    } catch (URISyntaxException ex) {
      logger.error("\n URISyntaxException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    
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
                .setProxy(new HttpHost(proxyCredentials.getProxyHost(), Integer.parseInt(proxyCredentials.getProxyPort())))
                .build();
      } else {
        httpclient = HttpClients.custom()
                .setProxy(new HttpHost(proxyCredentials.getProxyHost(), Integer.parseInt(proxyCredentials.getProxyPort())))
                .build();
      }

    } else {
      httpclient = HttpClients.createDefault();
    }

    //GET
    HttpGet httpget = new HttpGet(uri);
    CloseableHttpResponse response = null;
    try {
      response = httpclient.execute(httpget);
    } catch (IOException ex) {
      logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      String message = env.getProperty("error.server.failed"); //messages.properties
      redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
      return "redirect:/smpeditor/deletesmpfile";
    }

    /*Get response*/
    smpdelete.setStatusCode(response.getStatusLine().getStatusCode());
    org.apache.http.HttpEntity entity = response.getEntity();
    
    logger.debug("\n ************ RESPONSE CODE - " + response.getStatusLine().getStatusCode());
    logger.debug("\n ************ RESPONSE REASON - " + response.getStatusLine().getReasonPhrase());
    
    //Audit vars 
    //TODO
    String ncp = ConfigurationManagerService.getInstance().getProperty("ncp.country");
    String ncpemail = ConfigurationManagerService.getInstance().getProperty("ncp.email");
    String country = ConfigurationManagerService.getInstance().getProperty("COUNTRY_PRINCIPAL_SUBDIVISION");
    String localip = ConfigurationManagerService.getInstance().getProperty("SMP_URL");//Source Gateway
    String remoteip = ConfigurationManagerService.getInstance().getProperty("SERVER_IP");//Target Gateway
    String smp = ConfigurationManagerService.getInstance().getProperty("SMP_SUPPORT");
    String smpemail = ConfigurationManagerService.getInstance().getProperty("SMP_SUPPORT_EMAIL");
    //ET_ObjectID --> Base64 of url
    String objectID = uri.toString();
    byte[] encodedObjectID = Base64.encodeBase64(objectID.getBytes());

    if (smpdelete.getStatusCode() == 503 || smpdelete.getStatusCode() == 405) {
      String message = env.getProperty("error.server.failed"); //messages.properties
      redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
      //Audit error
      byte[] encodedObjectDetail = Base64.encodeBase64(response.getStatusLine().getReasonPhrase().getBytes());
      Audit.sendAuditQuery(smp, smpemail, ncp, ncpemail, country, localip, remoteip,
              new String(encodedObjectID), Integer.toString(response.getStatusLine().getStatusCode()), encodedObjectDetail); //TODO
      return "redirect:/smpeditor/deletesmpfile";
    } else if (smpdelete.getStatusCode() == 401) {
      String message = env.getProperty("error.nouser"); //messages.properties
      redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
      //Audit error
      byte[] encodedObjectDetail = Base64.encodeBase64(response.getStatusLine().getReasonPhrase().getBytes());
      Audit.sendAuditQuery(smp, smpemail, ncp, ncpemail, country, localip, remoteip,
              new String(encodedObjectID), Integer.toString(response.getStatusLine().getStatusCode()), encodedObjectDetail); //TODO
      return "redirect:/smpeditor/deletesmpfile";
    }

    /* Get BusinessCode and ErrorDescription from response in case of error */
    /* Get ServiceMetadataReference from response in case of success */
    
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
    List<ReferenceCollection> referenceCollection = new ArrayList<>();
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      builder = factory.newDocumentBuilder();
      Document doc = builder.parse(bais);
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
              logger.debug("\n ***************** HREF - " + href);

              //Find SMPType of href
              String smptype = "Unknown type";

              String[] ids = href.split("/services/");
              String documentID="";
              
              String docID = ids[1];
              docID = java.net.URLDecoder.decode(docID, "UTF-8");
              HashMap<String, String> propertiesMap = readProperties.readPropertiesFile();
              String[] nIDs = docID.split(env.getProperty("DocumentIdentifier.Scheme") + "::");//SPECIFICATION May change if Document Identifier specification change
              String docuID = nIDs[1];
              Set set2 = propertiesMap.entrySet();
              Iterator iterator2 = set2.iterator();
              while (iterator2.hasNext()) {
                Map.Entry mentry2 = (Map.Entry) iterator2.next();
                //logger.debug("\n ****** KEY - " + mentry2.getKey().toString());
                if (docuID.equals(mentry2.getKey().toString())) {
                  String[] docs = mentry2.getValue().toString().split("\\.");
                  documentID = docs[0];
                  logger.debug("\n ****** documentID - " + documentID);
                  break;
                }
              }
              
              String smpType = documentID;
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
      logger.error("\n ParserConfigurationException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    } catch (SAXException ex) {
      logger.error("\n SAXException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    } catch (IOException ex) {
      logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
    }
    smpdelete.setReferenceCollection(referenceCollection);
    
    if (!(smpdelete.getStatusCode() == 200 || smpdelete.getStatusCode() == 201)) {
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
      Audit.sendAuditQuery(smp, smpemail, ncp, ncpemail, country, localip, remoteip,
              new String(encodedObjectID), Integer.toString(response.getStatusLine().getStatusCode()), errorresult.getBytes()); //TODO
    }
  

    if (smpdelete.getStatusCode() == 200 || smpdelete.getStatusCode() == 201) {
      //Audit Success TODO
      Audit.sendAuditQuery(smp, smpemail, ncp, ncpemail, country, localip, remoteip,
              new String(encodedObjectID), null, null);
      }
    
     if (referenceCollection.isEmpty()) {
      String message = env.getProperty("error.nodoc"); //messages.properties
      Alert alert = new Alert(message, Alert.alertType.warning);
      smpdelete.setAlert(alert);
      redirectAttributes.addFlashAttribute("alert", alert);
      return "redirect:/smpeditor/deletesmpinfo";
    }  
    

    logger.debug("\n********* MODEL - " + model.toString());
    return "redirect:/smpeditor/deletesmpinfo";

  }

  /**
   * Generate deleteInfo page
   *
   * @param smpdelete
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "smpeditor/deletesmpinfo", method = RequestMethod.GET)
  public String deleteInfo(@ModelAttribute("smpdelete") SMPHttp smpdelete, Model model, final RedirectAttributes redirectAttributes) {
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
     
    String urlServer = ConfigurationManagerService.getInstance().getProperty("SMP_ADMIN_URL");
    if (urlServer.endsWith("/")) {
      urlServer = urlServer.substring(0, urlServer.length() - 1);
    }
    /*Removes https:// from entered by the user so it won't repeat in uri set scheme*/
    if (urlServer.startsWith("https")) {
      urlServer = urlServer.substring(8);
    }
    
    List<String> referencesSelected = new ArrayList<>();
    referencesSelected = smpdelete.getReferenceSelected();
    
    List<SMPHttp> allItems = new ArrayList<SMPHttp>();

    /*
      Iterate through all references selected to delete
    */
    for (int i = 0; i < referencesSelected.size(); i++) {
      SMPHttp itemDelete = new SMPHttp();
      
      logger.debug("\n ************** referencesSelected.get(i) - " + referencesSelected.get(i));
      String[] refs = referencesSelected.get(i).split("&&");

      logger.debug("\n ************** SMPTYPEEEE - " + refs[1]);
      logger.debug("\n ************** reference - " + refs[0]);
      itemDelete.setSmptype(refs[1]);
      
      String reference = refs[0]; 
      itemDelete.setReference(reference);
      if (reference.startsWith("https://")) {
        String url = ConfigurationManagerService.getInstance().getProperty("SMP_URL");
        reference = reference.substring(url.length());
      }
     
      try {
        reference = java.net.URLDecoder.decode(reference, "UTF-8");
      } catch (UnsupportedEncodingException ex) {
        logger.error("\n UnsupportedEncodingException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      }
      logger.debug("\n ************** referencesSelected - " + reference);
     /* String reference = "ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb"
              + "/services/"
       + "ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107";*/
      URI uri = null;
      try {
        uri = new URIBuilder()
                .setScheme("https")
                .setHost(urlServer)
                .setPath(reference)
                .build();
      } catch (URISyntaxException ex) {
        logger.error("\n URISyntaxException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
      }
      
      logger.debug("\n ************** URI - " + uri);
      
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
      // Allow TLSv1 protocol only
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
              sslcontext,
              //  new String[]{"TLSv1"},
              //   null,
              //SSLConnectionSocketFactory.getDefaultHostnameVerifier()
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

          //  ProxyUtil.initProxyConfiguration();
          httpclient = HttpClients.custom()
                  //.useSystemProperties()
                  .setDefaultCredentialsProvider(credentialsProvider)
                  .setSSLSocketFactory(sslsf)
                  .setProxy(new HttpHost(proxyCredentials.getProxyHost(), Integer.parseInt(proxyCredentials.getProxyPort())))
                  .build();
        } else {
          httpclient = HttpClients.custom()
                  //.useSystemProperties()
                  .setSSLSocketFactory(sslsf)
                  .setProxy(new HttpHost(proxyCredentials.getProxyHost(), Integer.parseInt(proxyCredentials.getProxyPort())))
                  .build();
        }

      } else {
        httpclient = HttpClients.custom()
                //.useSystemProperties()
                .setSSLSocketFactory(sslsf)
                .build();
      }

      //DELETE
      HttpDelete httpdelete = new HttpDelete(uri);

      CloseableHttpResponse response = null;
      try {
        response = httpclient.execute(httpdelete);
      } catch (IOException ex) {
        logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        String message = env.getProperty("error.server.failed"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        return "redirect:/smpeditor/deletesmpfile";
      }

      /*Get response*/
      itemDelete.setStatusCode(response.getStatusLine().getStatusCode());
      org.apache.http.HttpEntity entity = response.getEntity();

      logger.debug("\n ************ RESPONSE DELETE - " + response.getStatusLine().getStatusCode());
      logger.debug("\n ************ RESPONSE REASON - " + response.getStatusLine().getReasonPhrase());
      
      //Audit vars
      //TODO
      String ncp = ConfigurationManagerService.getInstance().getProperty("ncp.country");
      String ncpemail = ConfigurationManagerService.getInstance().getProperty("ncp.email");
      String country = ConfigurationManagerService.getInstance().getProperty("COUNTRY_PRINCIPAL_SUBDIVISION");
      String localip = ConfigurationManagerService.getInstance().getProperty("SERVER_IP");//Source Gateway
      String remoteip = ConfigurationManagerService.getInstance().getProperty("SMP_ADMIN_URL");//Target Gateway
      String smp = ConfigurationManagerService.getInstance().getProperty("SMP_SUPPORT");
      String smpemail = ConfigurationManagerService.getInstance().getProperty("SMP_SUPPORT_EMAIL");
      //ET_ObjectID --> Base64 of url
      String objectID = uri.toString();
      byte[] encodedObjectID = Base64.encodeBase64(objectID.getBytes());
      

      if (itemDelete.getStatusCode() == 503 || itemDelete.getStatusCode() == 405) {
        String message = env.getProperty("error.server.failed"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        //Audit error
        byte[] encodedObjectDetail = Base64.encodeBase64(response.getStatusLine().getReasonPhrase().getBytes());
        Audit.sendAuditPush(ncp, ncpemail, smp, smpemail, country, localip, remoteip,
                new String(encodedObjectID), Integer.toString(response.getStatusLine().getStatusCode()), encodedObjectDetail); //TODO

        return "redirect:/smpeditor/deletesmpfile";
      } else if (itemDelete.getStatusCode() == 401) {
        String message = env.getProperty("error.nouser"); //messages.properties
        redirectAttributes.addFlashAttribute("alert", new Alert(message, Alert.alertType.danger));
        //Audit error
        byte[] encodedObjectDetail = Base64.encodeBase64(response.getStatusLine().getReasonPhrase().getBytes());
        Audit.sendAuditPush(ncp, ncpemail, smp, smpemail, country, localip, remoteip,
                new String(encodedObjectID), Integer.toString(response.getStatusLine().getStatusCode()), encodedObjectDetail); //TODO

        return "redirect:/smpeditor/deletesmpfile";
      }
      

      if (!(itemDelete.getStatusCode() == 200 || itemDelete.getStatusCode() == 201)) {
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
              itemDelete.setBusinessCode(businessCode);
            }
            if (nodes.item(j).getNodeName().equals("ErrorDescription")) {
              String errorDescription = nodes.item(j).getTextContent();
              itemDelete.setErrorDescription(errorDescription);
            }
          }
        } catch (ParserConfigurationException ex) {
          logger.error("\n ParserConfigurationException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (SAXException ex) {
          logger.error("\n SAXException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        } catch (IOException ex) {
          logger.error("\n IOException - " + SimpleErrorHandler.printExceptionStackTrace(ex));
        }
        
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
        Audit.sendAuditPush(ncp, ncpemail, smp, smpemail, country, localip, remoteip,
                new String(encodedObjectID), Integer.toString(response.getStatusLine().getStatusCode()), errorresult.getBytes()); //TODO
      }

      if (itemDelete.getStatusCode() == 200 || itemDelete.getStatusCode() == 201) {
        //Audit Success TODO
        Audit.sendAuditPush(ncp, ncpemail, smp, smpemail, country, localip, remoteip,
                new String(encodedObjectID), null, null);
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
    logger.debug("\n==== in deletesmpresult ====");
    
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
