/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehealth.ccd.smp;

import static eu.europa.ec.cipa.smp.client.SMPServiceCallerReadonly.getConvertedException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.busdox.servicemetadata.publishing._1.CompleteServiceGroupType;
import org.busdox.servicemetadata.publishing._1.ServiceGroupType;
import org.busdox.servicemetadata.publishing._1.ServiceMetadataReferenceCollectionType;
import org.busdox.servicemetadata.publishing._1.ServiceMetadataReferenceType;
import org.busdox.servicemetadata.publishing._1.ServiceMetadataType;
import org.busdox.transport.identifiers._1.DocumentIdentifierType;
import org.busdox.transport.identifiers._1.ParticipantIdentifierType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.helger.commons.ValueEnforcer;
import com.helger.web.http.basicauth.BasicAuthClientCredentials;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import eu.ehealth.ccd.exceptions.NoServiceGroupException;
import eu.europa.ec.cipa.busdox.identifier.IReadonlyDocumentTypeIdentifier;
import eu.europa.ec.cipa.busdox.identifier.IReadonlyParticipantIdentifier;
import eu.europa.ec.cipa.peppol.identifier.IdentifierUtils;
import eu.europa.ec.cipa.peppol.identifier.doctype.SimpleDocumentTypeIdentifier;
import eu.europa.ec.cipa.peppol.identifier.participant.SimpleParticipantIdentifier;
import eu.europa.ec.cipa.peppol.identifier.process.SimpleProcessIdentifier;
import eu.europa.ec.cipa.peppol.utils.ConfigFile;
import eu.europa.ec.cipa.smp.client.SMPServiceCaller;
import eu.europa.ec.cipa.smp.client.tools.SMPUtils;

/**
 *
 * @author joao.cunha
 */
public class SMP {
    private static final Logger logger = LoggerFactory.getLogger(SMP.class);
    private static final ConfigFile configurationsFile = ConfigFile.getInstance();
    private static final String IDENTIFIERS_NAMESPACE = "http://busdox.org/transport/identifiers/1.0/";
    
    private SMPServiceCaller aClient;
    private SMPConnection smpConnection;
    
    public SMP(SMPConnection smpConnection) {
        // open/read the application context file
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        this.smpConnection = smpConnection;
        logger.info(this.smpConnection.toString());
        aClient = this.createSMPClient();
    }
    
    public SMPParticipantInformation getSMPParticipantInformation(String country) throws Exception {
        SMPParticipantInformation smpParticipantInformation = new SMPParticipantInformation();
        List<String> smpRefs = new ArrayList<String>();
        final SimpleParticipantIdentifier PARTICIPANT_ID = getParticipantID(country);
        final ServiceGroupType serviceGroup = aClient.getServiceGroupOrNull(PARTICIPANT_ID);
        final ServiceMetadataReferenceCollectionType aSMRC = serviceGroup.getServiceMetadataReferenceCollection();
        if (aSMRC != null && !aSMRC.getServiceMetadataReference().isEmpty()) {     
        for (final ServiceMetadataReferenceType aSMR : aSMRC.getServiceMetadataReference())
            smpRefs.add(aSMR.getHref());
        }
        String serviceGroupURL = aClient.getSMPHost().toString() + "/" + IdentifierUtils.getIdentifierURIPercentEncoded(PARTICIPANT_ID);
        smpParticipantInformation.setServiceGroupURL(serviceGroupURL);
        smpParticipantInformation.setServiceMetadataURLs(smpRefs);
        return smpParticipantInformation;
    }
    
    public SMPParticipantInformation uploadSMPFiles(List<File> smpFiles) throws Exception {
        String country = null;
        for (File smpFile : smpFiles) {
            country = getCountryFromSmpFile(smpFile);
            uploadSMPFile(smpFile, country);
        }
        if (country == null) {
            return null;
        } else {
            // printing CompleteServiceGroup information just for logging purposes
            printCompleteServiceGroup(getParticipantID(country));
            return getSMPParticipantInformation(country);
        }
    }
    
    public void uploadSMPFile(File smpFile, String country) throws Exception {
        final BasicAuthClientCredentials SMP_CREDENTIALS = getSMPCredentials();
        final SimpleParticipantIdentifier PARTICIPANT_ID = getParticipantID(country);
        logger.info("ParticipanID: " + PARTICIPANT_ID);       
        
        // Ensure that the service group exists
        logger.info ("Checking if service group exists...");
        // We need the ServiceGroup structure that has the List<ServiceMetadata>
//        ServiceGroupType serviceGroup = aClient.getServiceGroupOrNull(PARTICIPANT_ID);
        final CompleteServiceGroupType aCompleteServiceGroup = aClient.getCompleteServiceGroupOrNull(PARTICIPANT_ID);
        if (aCompleteServiceGroup == null) {
            logger.info("Complete service group is null! Ending process...");
        } else {
            ServiceGroupType serviceGroup = aCompleteServiceGroup.getServiceGroup();
            if (serviceGroup == null) {
                // only the SMP admin creates ServiceGroups. If it doens't exist, we end the process
                logger.error("ServiceGroup for " + country.toUpperCase() + " doesn't exist! Ending process...");
                throw new NoServiceGroupException("ServiceGroup for " + country.toUpperCase() + " doesn't exist!");
    //            aClient.saveServiceGroup(ptParticipantId, SMP_CREDENTIALS);
    //            logger.info("Created!");
            } else {   
                logger.info("ServiceGroup for " + PARTICIPANT_ID.getValue() + " exists!");
                logger.info(SMPUtils.getAsString(serviceGroup));
                String documentSubtypeID = getDocumentSubtypeIdFromSmpFile(smpFile);
                final SimpleDocumentTypeIdentifier DOC_ID = getDocumentTypeID(documentSubtypeID);
                
//                logger.info("Checking if ServiceMetadata for ParticipantID " + PARTICIPANT_ID.getValue() + " and DocumentID " + DOC_ID.getValue() + " exists...");
                // this GETs a Signed file which makes a validation of the signature, which is not working. Trying a workaround to instead get the unsigned SMP file. TODO: Review this later
    //            final SignedServiceMetadataType aSignedServiceMetadata = aClient.getServiceRegistrationOrNull(PARTICIPANT_ID, DOC_ID);

//                final boolean hasServiceMetadata = hasServiceMetadata(aCompleteServiceGroup, PARTICIPANT_ID, DOC_ID);

//                if (!hasServiceMetadata) {
                    // There's no ServiceMetadata for PARTICIPANT_ID + DOC_ID
                    logger.info("Creating ServiceMetadata for ParticipantID " + PARTICIPANT_ID.getValue() + " and DocumentID " + DOC_ID.getValue() + " ...");
                    saveServiceRegistration(PARTICIPANT_ID, DOC_ID, SMP_CREDENTIALS, smpFile);
                    logger.info("Finished the creation of ServiceMetadata!");
                    printServiceGroup(country, PARTICIPANT_ID);
//                    printCompleteServiceGroup(PARTICIPANT_ID, aClient);
//                } else {
//                    // Workaround for now: if ServiceMetadata exists we delete it first and add a new one
//                    logger.info("Found a ServiceMetadata for ParticipantID " + PARTICIPANT_ID.getValue() + " and DocumentID " + DOC_ID.getValue() + "! Deleting...");
//                    aClient.deleteServiceRegistration(PARTICIPANT_ID, DOC_ID, SMP_CREDENTIALS);
//                    logger.info("ServiceMetadata deleted!");
//
////                    logger.info("Checking if ServiceMetadata was really deleted...");
//    //                final SignedServiceMetadataType deletedSignedServiceMetadata = aClient.getServiceRegistrationOrNull(PARTICIPANT_ID, DOC_ID);
//
//                    logger.info("ServiceMetadata was deleted. Proceeding with the new creation...");
//                    logger.info("Creating new ServiceMetadata for ParticipantID " + PARTICIPANT_ID.getValue() + " and DocumentID " + DOC_ID.getValue() + "!");
//                    logger.info("BEGIN SAVING METADATA!");
//                    saveServiceRegistration(aClient, PARTICIPANT_ID, DOC_ID, SMP_CREDENTIALS, smpFile);
//                    logger.info("END SAVING METADATA!");
//                    printServiceGroup(country, PARTICIPANT_ID, aClient);
//                    printCompleteServiceGroup(PARTICIPANT_ID, aClient);
//                }
            }
        }
    }

               
//
//                // Get the service group reference list
//                logger.info("Going to print service registration for " + PARTICIPANT_ID + " and " + DOC_ID);
//                final SignedServiceMetadataType aSignedServiceMetadata = aClient.getServiceRegistrationOrNull (PARTICIPANT_ID, DOC_ID);
//                if (aSignedServiceMetadata == null) {
//                  logger.error ("Failed to get service registration for " + PARTICIPANT_ID + " and " + DOC_ID);
//                } else {
//                  logger.info (SMPUtils.getAsString (aSignedServiceMetadata.getServiceMetadata ()));
//                }
//            }
    
        
            

            /** TEST - create ServiceMetadata **/
            // criar o URI do recurso percent-encoded
            
//            logger.info("ParticipantID: " + PARTICIPANT_ID + " | DocumentID: " + DOC_ID);
//            final String sPath = IdentifierUtils.getIdentifierURIPercentEncoded (PARTICIPANT_ID) +
//                             "/services/" +
//                             IdentifierUtils.getIdentifierURIPercentEncoded (DOC_ID);
    //        final String sPath = SMP_URI.toString() + PARTICIPANT_ID + "/services/" + DOC_ID;
    //        final String encondedPath = BusdoxURLUtils.createPercentEncodedURL(sPath);
//            logger.info("Path: " + sPath);
    //        logger.info("Encoded path: " + encondedPath);
    //        final String resourceUri = BusdoxURLUtils.createPercentEncodedURL(SMP_URI);
    //        logger.info("Final URI: " + resourceUri);
//            WebResource resource = Client.create().resource(new URI(SMP_URI.toString() + "/" + sPath));
//            logger.info("Resource URI: " + resource.getURI());
//            final WebResource.Builder aBuilderWithAuth = resource.header (HttpHeaders.AUTHORIZATION, SMP_CREDENTIALS.getRequestValue ());
//            String file = "/home/joao.cunha/Transferências/TEST_XSLT_APP/LU/Patient_Identification_Service_LU_PUT.xml";
    //        ClientResponse response = aBuilderWithAuth.type(MediaType.TEXT_XML).accept(MediaType.TEXT_PLAIN).entity(new File(file)).put(ClientResponse.class);
    //        logger.info("Response: " + response);


    //        logger.info("Creating the new service group");
            // SimpleParticipantIdentifier(scheme, id);
    //        SimpleParticipantIdentifier newParticipantId = new SimpleParticipantIdentifier("ehealth-participantid-qns","urn:ehealth:pt:ncpb-idp");
    //        aClient.saveServiceGroup (newParticipantId, SMP_CREDENTIALS);
    //        logger.info("ServiceGroup for PT created");
    
    private SMPServiceCaller createSMPClient() {
        final URI SMP_URI = getSMPURI(this.smpConnection.getUri());
        // The main SMP client
        final SMPServiceCaller aClient = new SMPServiceCaller(SMP_URI);
        logger.info("SMP client created to: " + SMP_URI);
        return aClient;
    }
    
    // Return value of XML tag, given a root element
    private String getXmlTagValue(String tagName, String namespace, Element element) {
        NodeList list = element.getElementsByTagNameNS(namespace, tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }

        return null;
    }
    
    public String getCountryFromSmpFile(File smpFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(smpFile);
        Element rootElement = document.getDocumentElement();
        String participantIdentifier = getXmlTagValue("ParticipantIdentifier", IDENTIFIERS_NAMESPACE, rootElement);
        String country = participantIdentifier.split(":")[2]; // E.g., urn:ehealth:lu:ncpb-idp -> lu   
        logger.info("Country: " + country);
        return country;
    }
    
    public String getDocumentSubtypeIdFromSmpFile(File smpFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(smpFile);
        Element rootElement = document.getDocumentElement();
        String documentIdentifier = getXmlTagValue("DocumentIdentifier", IDENTIFIERS_NAMESPACE, rootElement);
        String documentSubtypeID = documentIdentifier.split("(?<!:):(?!:)")[2]; // split with single colon but not double colon. E.g., urn::epsos##services:extended:epsos::11 -> epsos::11     
        logger.info("DocumentSubtypeID: " + documentSubtypeID);
        return documentSubtypeID;
    }
    
    /* Checks if a CompleteServiceGroup has a ServiceMetadata with the provided ParticipantID and DocumentID */
    public boolean hasServiceMetadata(@Nonnull final CompleteServiceGroupType aCompleteServiceGroup, 
                                            @Nonnull final SimpleParticipantIdentifier aParticipantIdentifier, 
                                            @Nonnull final SimpleDocumentTypeIdentifier aDocumentIdentifier) {
        ValueEnforcer.notNull(aCompleteServiceGroup, "CompleteServiceGroup");
        ValueEnforcer.notNull(aParticipantIdentifier, "ParticipantIdentifier");
        ValueEnforcer.notNull(aDocumentIdentifier, "DocumentIdentifier");
        
        Iterator<ServiceMetadataType> serviceMetadataIterator = aCompleteServiceGroup.getServiceMetadata().iterator();
        boolean found = false;
        ServiceMetadataType aServiceMetadata = null;
        while (serviceMetadataIterator.hasNext() && !found) {
            aServiceMetadata = serviceMetadataIterator.next();
            ParticipantIdentifierType serviceMetadataParticipantIdentifier = (ParticipantIdentifierType)aServiceMetadata.getServiceInformation().getParticipantIdentifier();
            DocumentIdentifierType serviceMetadataDocumentIdentifier = (DocumentIdentifierType)aServiceMetadata.getServiceInformation().getDocumentIdentifier();
            if (IdentifierUtils.areIdentifiersEqual(serviceMetadataParticipantIdentifier, aParticipantIdentifier) && IdentifierUtils.areIdentifiersEqual(serviceMetadataDocumentIdentifier, aDocumentIdentifier)) {
                found = true;
            }
        }
        return found;
    }
    
    public void printServiceGroup(String country, SimpleParticipantIdentifier participantId) throws Exception {
        logger.info("Retrieving ServiceGroup information for " + country.toUpperCase());
        ServiceGroupType newServiceGroup = aClient.getServiceGroupOrNull(participantId);
        if (newServiceGroup == null) {
            logger.error ("Failed to get service group infos for " + participantId);
        } else {
            // Prints ParticipantIdentifier, ServiceMetadataReferenceCollection and Extension
            logger.info(SMPUtils.getAsString(newServiceGroup));
        }
    }   
    
    public void printCompleteServiceGroup(SimpleParticipantIdentifier participantId) throws Exception {
//         Get the service group reference list
        final CompleteServiceGroupType aCompleteServiceGroup = aClient.getCompleteServiceGroupOrNull(participantId);

        if (aCompleteServiceGroup == null) {
          logger.error("Failed to get complete service group for " + participantId);
        } else {
          logger.info(SMPUtils.getAsString(aCompleteServiceGroup.getServiceGroup()));
          logger.info("Printing all metadata for " + participantId);
          for (final ServiceMetadataType aServiceMetadata : aCompleteServiceGroup.getServiceMetadata())
            logger.info(SMPUtils.getAsString(aServiceMetadata));
        }
    }

    /* Reimplement these 2 methods in a subclass of SMPServiceCaller? Analyze... */
    public void saveServiceRegistration (@Nonnull final IReadonlyParticipantIdentifier aServiceGroupID,
                                        @Nonnull final IReadonlyDocumentTypeIdentifier aDocumentTypeID,
                                        @Nonnull final BasicAuthClientCredentials aCredentials,
                                        @Nonnull final File smpFile) throws Exception {
        ValueEnforcer.notNull(aClient, "Client");
        ValueEnforcer.notNull(aServiceGroupID, "ServiceGroup");
        ValueEnforcer.notNull(aDocumentTypeID, "DocumentType");
        ValueEnforcer.notNull(aCredentials, "Credentials");
        ValueEnforcer.notNull(smpFile, "SMP File");

        final WebResource aFullResource = aClient.getServiceRegistrationResource (aServiceGroupID, aDocumentTypeID);
        saveServiceRegistration (aFullResource, aCredentials, smpFile);
    }
    
    private void saveServiceRegistration (@Nonnull final WebResource aFullResource,
                                              @Nonnull final BasicAuthClientCredentials aCredentials,
                                              @Nonnull final File smpFile) throws Exception {
        ValueEnforcer.notNull (aFullResource, "FullResource");
        ValueEnforcer.notNull (aCredentials, "Credentials");
        ValueEnforcer.notNull(smpFile, "SMP File");

        if (logger.isDebugEnabled ())
            logger.debug ("_saveServiceRegistration from " + aFullResource.getURI ());

        try {
            final Builder aBuilderWithAuth = aFullResource.header (HttpHeaders.AUTHORIZATION, aCredentials.getRequestValue ());
            
            // Create JAXBElement!
            aBuilderWithAuth.type (MediaType.TEXT_XML).put(smpFile);
        }
        catch (final UniformInterfaceException e) {
            throw getConvertedException (e);
        }
    }
    
    /** https://joinup.ec.europa.eu/svn/cipaedelivery/tags/2.2.4/cipa-core/Implementation/java/cipa-smp-client-library/src/main/java/eu/europa/ec/cipa/smp/client/SMPServiceCaller.java
     * @param uri The SMP server URI as a String (e.g. "http://test-smp...")
     * @return The address of the SMP service. Must be port 80 and basic http only (no https!). Example: http://smpcompany.company.org 
     */
    public final URI getSMPURI(final String uri) {
        try {
          return new URI(uri);
        }
        catch (final URISyntaxException ex) {
          throw new IllegalStateException (ex);
        }
    }
    
    public final String getSMPUserName() {
        return smpConnection.getUsername();
    }
    
    public final String getSMPPassword() {
        return smpConnection.getPassword();
    }
    
    public final BasicAuthClientCredentials getSMPCredentials() {
        return new BasicAuthClientCredentials(getSMPUserName(), getSMPPassword());
    }
    
    public final SimpleParticipantIdentifier getParticipantID(String country) {
        return new SimpleParticipantIdentifier(configurationsFile.getString("participantid.scheme"), configurationsFile.getString("participantid.prefix") + country + configurationsFile.getString("participantid.suffix"));
    }
    
    @Nonnull
    public final SimpleDocumentTypeIdentifier getDocumentTypeID (String documentSubtypeID) {
        return new SimpleDocumentTypeIdentifier(configurationsFile.getString("documenttypeid.scheme"), configurationsFile.getString ("documenttypeid") + documentSubtypeID);
    }

    @Nonnull
    public final SimpleProcessIdentifier getProcessTypeID () {
        return SimpleProcessIdentifier.createWithDefaultScheme (configurationsFile.getString ("processtypeid"));
    }
    
    public static void main(String[] args) throws Exception {
        String file = "/home/joao.cunha/Transferências/TEST_XSLT_APP/LU/Patient_Identification_Service_LU_PUT_fix_2.xml";
        /* args[0] = "http://smp.org" (server URL)
         * args[1] = "test-username"
         * args[2] = "test-password"
         */
        SMPConnection smpConnection = new SMPConnection(args[0], args[1], args[2]);
        SMP smp = new SMP(smpConnection);
        File smpFile = new File(file);
        String country = smp.getCountryFromSmpFile(smpFile);
        smp.uploadSMPFile(smpFile, country);
    }
}
