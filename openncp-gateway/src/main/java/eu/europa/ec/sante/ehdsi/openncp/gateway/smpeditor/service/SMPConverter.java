package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service;

import eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities.SMPFileUpdate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.Locale;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.oasis_open.docs.bdxr.ns.smp._2016._05.DocumentIdentifier;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.EndpointType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ExtensionType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ParticipantIdentifierType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ObjectFactory;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ProcessIdentifier;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ProcessListType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ProcessType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.RedirectType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceEndpointList;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceInformationType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;


/**
 Service responsible for converting the data introduced by the user to a xml file
 */
@Service
public class SMPConverter {
  
  @Autowired
  private Environment env;

  org.slf4j.Logger logger = LoggerFactory.getLogger(SMPConverter.class);
  
  private String certificateSubjectName;
  private File generatedFile;
  private boolean nullExtension = false;
  
  
  public String getCertificateSubjectName() {
    return certificateSubjectName;
  }
  public void setCertificateSubjectName(String certificateSubjectName) {
    this.certificateSubjectName = certificateSubjectName;
  }
  
  public File getFile() {
    return generatedFile;
  }
  public void setFile(File generatedFile) {
    this.generatedFile = generatedFile;
  }

  public boolean isNullExtension() {
    return nullExtension;
  }

  public void setNullExtension(boolean nullExtension) {
    this.nullExtension = nullExtension;
  }

  /**
   Converts the data received from the SMPGenerateFileController to a xml file 
   * @param type
   * @param CC
   * @param endpointUri
   * @param servDescription
   * @param servExpDate
   * @param certificateFile
   * @param tecContact
   * @param tecInformation
   * @param servActDate
   * @param extension
   * @param fileName
   * @param certificateUID
   * @param redirectHref
   */
  public void converteToXml(String type, String CC, String endpointUri, String servDescription,
          String tecContact, String tecInformation, Date servActDate, Date servExpDate, 
          MultipartFile extension, MultipartFile certificateFile, String fileName,
          String certificateUID, String redirectHref){

    logger.debug("\n==== in converter ====");
    
    ObjectFactory objectFactory = new ObjectFactory();
    ServiceMetadata serviceMetadata = objectFactory.createServiceMetadata();
    //XML file generated at path
    generatedFile = new File("/" + fileName);
        
    //Type of SMP File -> Redirect | Service Information
    if (type == "Redirect") {
      /*
        Redirect SMP Type
      */
      logger.debug("\n******* Redirect ************");
      RedirectType redirectType = objectFactory.createRedirectType();

      redirectType.setCertificateUID(certificateUID);
      redirectType.setHref(redirectHref);

      serviceMetadata.setRedirect(redirectType);
    } else {
      /*
        ServiceInformation SMP Type
      */
      logger.debug("\n******* ServiceInformation ************");
      DocumentIdentifier documentIdentifier = objectFactory.createDocumentIdentifier();
      EndpointType endpointType = objectFactory.createEndpointType();
      ExtensionType extensionType = objectFactory.createExtensionType();
      ParticipantIdentifierType participantIdentifierType = objectFactory.createParticipantIdentifierType();
      ProcessIdentifier processIdentifier = objectFactory.createProcessIdentifier();
      ProcessListType processListType = objectFactory.createProcessListType();
      ProcessType processType = objectFactory.createProcessType();
      ServiceEndpointList serviceEndpointList = objectFactory.createServiceEndpointList();
      ServiceInformationType serviceInformationType = objectFactory.createServiceInformationType();

     
      /*
       Document and Participant identifiers definition
       */
      participantIdentifierType.setScheme(env.getProperty(type + ".ParticipantIdentifier.Scheme")); ///in smpeditor.properties
      participantIdentifierType.setValue("urn:ehealth:" + CC + ":ncpb-idp"); //set by user (CC - country)

      documentIdentifier.setScheme(env.getProperty(type + ".DocumentIdentifier.Scheme"));//in smpeditor.properties
      documentIdentifier.setValue(env.getProperty(type + ".DocumentIdentifier"));//in smpeditor.properties

      /*
       Process identifiers definition
       */
      processIdentifier.setScheme(env.getProperty(type + ".ProcessIdentifier.Scheme"));//in smpeditor.properties
      if (type == "International_Search_Mask") {
        processIdentifier.setValue("urn:ehealth:ncp:" + CC + ":ism");
      } else {
        processIdentifier.setValue(env.getProperty(type + ".ProcessIdentifier")); //in smpeditor.properties
      }

      /*
       Endpoint Transport Profile definition
       */
      endpointType.setTransportProfile(env.getProperty(type + ".transportProfile")); //in smpeditor.properties

      /*
       BusinessLevelSignature and MinimumAuthenticationLevel definition
       */
      if (type != "VPN_Gateway_A" && type != "VPN_Gateway_B" && type != "Identity_Provider" && type != "International_Search_Mask") {
        Boolean requireBusinessLevelSignature = Boolean.parseBoolean(env.getProperty(type + ".RequireBusinessLevelSignature"));
        endpointType.setRequireBusinessLevelSignature(requireBusinessLevelSignature); //in smpeditor.properties
        endpointType.setMinimumAuthenticationLevel(env.getProperty(type + ".MinimumAuthenticationLevel")); //in smpeditor.properties
      }

      /*
       * URI definition
       */
      if (type == "VPN_Gateway_A") {
        endpointType.setEndpointURI("ipsec:" + endpointUri);//Set by user
      } else if (type == "VPN_Gateway_B" || type == "International_Search_Mask") {
        endpointType.setEndpointURI("");
      } else {
        endpointType.setEndpointURI(endpointUri);//Set by user
      }

      /*
       * Dates parse to Calendar
       */   
      Calendar calad = Calendar.getInstance();
      calad.setTime(servActDate);
      int yearad = calad.get(Calendar.YEAR);
      int monthad = calad.get(Calendar.MONTH);
      int dayad = calad.get(Calendar.DAY_OF_MONTH);
      int hourat = calad.get(Calendar.HOUR_OF_DAY);
      int minat = calad.get(Calendar.MINUTE);

      Calendar calendarAD = new GregorianCalendar(yearad, monthad, dayad, hourat, minat);
      endpointType.setServiceActivationDate(calendarAD);//Set by user

      if (servExpDate == null) {
        endpointType.setServiceExpirationDate(null);
      } else {
        Calendar caled = Calendar.getInstance();
        caled.setTime(servExpDate);
        int yeared = caled.get(Calendar.YEAR);
        int monthed = caled.get(Calendar.MONTH);
        int dayed = caled.get(Calendar.DAY_OF_MONTH);
        int houret = caled.get(Calendar.HOUR_OF_DAY);
        int minet = caled.get(Calendar.MINUTE);

        Calendar calendarED = new GregorianCalendar(yeared, monthed, dayed, houret, minet);
        endpointType.setServiceExpirationDate(calendarED);//Set by user
      }

      /**
       * certificate parse
       */
      if (certificateFile != null) {
        X509Certificate cert = null;
        certificateSubjectName = null;
        try {
          cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(certificateFile.getInputStream());
          endpointType.setCertificate(cert.getEncoded()); //Set by User      
          if (cert != null) {
            certificateSubjectName = cert.getIssuerX500Principal().getName() + " Serial Number #"
                    + cert.getSerialNumber();
          }

        } catch (CertificateException ex) {
          Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
          Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
      } else {
        byte[] by = "".getBytes();
        endpointType.setCertificate(by);
      }

      /*
       Endpoint Service Description, Technical ContactUrl and Technical InformationUrl definition
       */
      endpointType.setServiceDescription(servDescription); //Set by User
      endpointType.setTechnicalContactUrl(tecContact); //Set by User
      endpointType.setTechnicalInformationUrl(tecInformation); //Set by User

      /*Not used*/
      extensionType.setExtensionAgencyID(null);
      extensionType.setExtensionAgencyName(null);
      extensionType.setExtensionAgencyURI(null);
      extensionType.setExtensionID(null);
      extensionType.setExtensionName(null);
      extensionType.setExtensionReason(null);
      extensionType.setExtensionReasonCode(null);
      extensionType.setExtensionURI(null);
      extensionType.setExtensionVersionID(null);

      /*
       Endpoint Extension file parse
       */
      if (extension != null) {
        nullExtension = false;
        Document docOriginal = null;
        try {
          String content = new Scanner(extension.getInputStream()).useDelimiter("\\Z").next();
          //logger.debug("\n*****Content from extension file - " + content);

          docOriginal = parseDocument(content);

        } catch (FileNotFoundException ex) {
          nullExtension = true;
          Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
          nullExtension = true;
          Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
          nullExtension = true;
          Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
          nullExtension = true;
          Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (nullExtension) {
          //Does not add extension
        } else {
          docOriginal.getDocumentElement().normalize();
          extensionType.setAny(docOriginal.getDocumentElement()); //Set by user
          endpointType.getExtensions().add(extensionType);
        }
      } else {
        //Does not add extension
      }

      processType.setProcessIdentifier(processIdentifier);
      processType.setServiceEndpointList(serviceEndpointList);

      serviceEndpointList.getEndpoints().add(endpointType);
      processListType.getProcesses().add(processType);

      serviceInformationType.setDocumentIdentifier(documentIdentifier);
      serviceInformationType.setParticipantIdentifier(participantIdentifierType);
      serviceInformationType.setProcessList(processListType);

      serviceMetadata.setServiceInformation(serviceInformationType);
    }
    
    
    /*
      Generates the final SMP XML file
    */
    XMLStreamWriter xsw = null;
    FileOutputStream generatedFileOS = null;
    
    try {
      generatedFileOS = new FileOutputStream(generatedFile);
      
      xsw = XMLOutputFactory.newFactory().createXMLStreamWriter(generatedFileOS, "UTF-8");
      xsw.setNamespaceContext(new NamespaceContext() {
        @Override
        public Iterator getPrefixes(String namespaceURI) {
          return null;
        }

        @Override
        public String getPrefix(String namespaceURI) {
          return "";
        }

        @Override
        public String getNamespaceURI(String prefix) {
          return null;
        }
      });
      
     
      JAXBContext jaxbContext = JAXBContext.newInstance(ServiceMetadata.class);      
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      
      jaxbMarshaller.marshal(serviceMetadata, xsw);
     // jaxbMarshaller.marshal(serviceMetadata, generatedFile);
     // jaxbMarshaller.marshal(serviceMetadata, System.out);
      
      generatedFileOS.flush();
      generatedFileOS.close();
      
      
    } catch (JAXBException ex) {
      Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
    } catch (XMLStreamException ex) {
      Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (xsw != null) {
        try {
          xsw.close();
        } catch (XMLStreamException ex) {
          Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

  }
  
  /**
   * Parse the xml of the file to be updated
   */
  public EndpointType convertFromXml(MultipartFile fileUpdate) {
    logger.debug("\n======= in convertFromXml ======= ");
    logger.debug("\n************* fileUpdate - " + fileUpdate);
    
    ObjectFactory objectFactory = new ObjectFactory();
    ServiceMetadata serviceMetadata = objectFactory.createServiceMetadata();
    EndpointType endpoint = objectFactory.createEndpointType();
            
    if (fileUpdate != null) {

      try {
        logger.debug("\n******* TRY");
        JAXBContext jaxbContext = JAXBContext.newInstance(ServiceMetadata.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        serviceMetadata = (ServiceMetadata) jaxbUnmarshaller.unmarshal(fileUpdate.getInputStream());
        logger.debug("\n******* CONVERTER SMPFILE - " + serviceMetadata);
      } catch (JAXBException ex) {
        Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      endpoint = serviceMetadata.getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0);
      
      X509Certificate cert = null;
      String subjectName = null;
      try {
        InputStream in = new ByteArrayInputStream(endpoint.getCertificate());
        cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
        if (cert != null) {
          subjectName = cert.getIssuerX500Principal().getName() + " Serial Number #"
                  + cert.getSerialNumber();
        }

      } catch (CertificateException ex) {
        Logger.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      logger.debug("\n******* getEndpointURI - " + endpoint.getEndpointURI());
      logger.debug("\n******* getServiceDescription - " + endpoint.getServiceDescription());
      logger.debug("\n******* getCertificate - " + subjectName);
      logger.debug("\n******* getServiceActivationDate - " + endpoint.getServiceActivationDate().getTime());

    } else {
      //Does nothing
    }
    
    return endpoint;
  }

  
  
  /*TODO: TO DOC Auxiliary*/
  private Document parseDocument(String docContent) throws IOException, SAXException, ParserConfigurationException {
    InputStream inputStream = new ByteArrayInputStream(docContent.getBytes());
    getDocumentBuilder().setErrorHandler(new SimpleErrorHandler());
    return getDocumentBuilder().parse(inputStream);
  }

  private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    return dbf.newDocumentBuilder();
  }

}
