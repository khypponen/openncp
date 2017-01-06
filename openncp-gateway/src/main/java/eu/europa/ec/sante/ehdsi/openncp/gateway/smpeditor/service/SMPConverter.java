package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service;

import org.oasis_open.docs.bdxr.ns.smp._2016._05.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import static sun.jdbc.odbc.JdbcOdbcObject.hexStringToByteArray;

@Service
public class SMPConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMPConverter.class);

    /**
     *
     */
    public void converter() {

        System.out.println("==== in converter ====");

        DocumentIdentifier documentIdentifier = new DocumentIdentifier();
        EndpointType endpointType = new EndpointType();
        ExtensionType extensionType = new ExtensionType();
        ParticipantIdentifierType participantIdentifierType = new ParticipantIdentifierType();
        ObjectFactory objectFactory; //??????????????
        ProcessIdentifier processIdentifier = new ProcessIdentifier();
        ProcessListType processListType = new ProcessListType();
        ProcessType processType = new ProcessType();
        RedirectType redirectType = new RedirectType();
        ServiceEndpointList serviceEndpointList = new ServiceEndpointList();
        ServiceGroup serviceGroup;
        ServiceInformationType serviceInformationType = new ServiceInformationType();
        ServiceMetadata serviceMetadata = new ServiceMetadata();
        ServiceMetadataReferenceCollectionType serviceMetadataReferenceCollectionType;
        ServiceMetadataReferenceType serviceMetadataReferenceType = new ServiceMetadataReferenceType();
        SignedServiceMetadata signedServiceMetadata;

        //XML file generated at path
        File file = new File("C:\\file.xml");

        documentIdentifier.setScheme("ehealth-resid-qns");
        documentIdentifier.setValue("urn::epsos##services:extended:epsos::11");//Set by SMP Type

        endpointType.setTransportProfile("urn:ihe:iti:2013:xcpd"); //??
        endpointType.setEndpointURI("https://qaepsos.min-saude.pt:8443/epsos-ws-server/services/XCPD_Service");//Set by user
        endpointType.setRequireBusinessLevelSignature(Boolean.FALSE);
        endpointType.setMinimumAuthenticationLevel("urn:epSOS:loa:1");

        Calendar calendarAD = new GregorianCalendar(2016, 06, 06, 10, 57, 21);
        endpointType.setServiceActivationDate(calendarAD);//Set by user
        Calendar calendarED = new GregorianCalendar(2026, 06, 06, 10, 57, 21);
        endpointType.setServiceExpirationDate(calendarED);//Set by user

        byte[] cert = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        endpointType.setCertificate(cert); //Set by User

        endpointType.setServiceDescription("This is the epSOS Identity Service - Find Identity By Traits of the PT NCP"); //Set by User
        endpointType.setTechnicalContactUrl("john.doe@mail.com"); //Set by User
        endpointType.setTechnicalInformationUrl("john.doe@mail.com"); //Set by User

        participantIdentifierType.setScheme("ehealth-actorid-qns");
        participantIdentifierType.setValue("urn:ehealth:pt:ncpb-idp");

        processIdentifier.setScheme("ehealth-procid-qns");
        processIdentifier.setValue("urn:epsosIdentityService::FindIdentityByTraits"); //Set by SMPType?

        extensionType.setExtensionAgencyID(null);
        extensionType.setExtensionAgencyName(null);
        extensionType.setExtensionAgencyURI(null);
        extensionType.setExtensionID("urn:epsosIdentityService::FindIdentityByTraits");
        extensionType.setExtensionName(null);
        extensionType.setExtensionReason("Teste");
        extensionType.setExtensionReasonCode(null);
        extensionType.setExtensionURI(null);
        extensionType.setExtensionVersionID(null);

        Document docOriginal = null;
        try {
            String xmlAny = "<ex:Test xmlns:ex=\"http://example.org\">Test</ex:Test>";
            System.out.println("==== xmlAny - " + xmlAny);

            // String docString = loadDocumentAsString("/resources/GET_SignedServiceMetadata_response.xml");
            docOriginal = parseDocument(xmlAny);
            System.out.println("==== docOriginal first child - " + docOriginal.getFirstChild());

        } catch (IOException ex) {
            //LoggerFactory.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.error(null, ex);
        } catch (SAXException ex) {
            //LoggerFactory.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.error(null, ex);
        } catch (ParserConfigurationException ex) {
            //LoggerFactory.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.error(null, ex);
        }

        extensionType.setAny((Element) docOriginal.getFirstChild()); //Set by user (upload file)

        serviceMetadataReferenceType.setHref(null);

        processType.setProcessIdentifier(processIdentifier);
        processType.setServiceEndpointList(serviceEndpointList);

        serviceEndpointList.getEndpoints().add(endpointType);
        processListType.getProcesses().add(processType);

        serviceInformationType.setDocumentIdentifier(documentIdentifier);
        serviceInformationType.setParticipantIdentifier(participantIdentifierType);
        serviceInformationType.setProcessList(processListType);
        serviceInformationType.getExtensions().add(extensionType);

        redirectType.setCertificateUID(null);
        redirectType.setHref(null);

        //Um ou outro --> depende da escolha do Utilizador
        serviceMetadata.setServiceInformation(serviceInformationType);
        //serviceMetadata.setRedirect(redirectType);

        try {
            //Ver se são precisas todas as classes (So deve ser a ServiceMetadata)
            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceMetadata.class, ServiceEndpointList.class, EndpointType.class,
                    ServiceMetadataReferenceType.class, ExtensionType.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(serviceMetadata, file);
            jaxbMarshaller.marshal(serviceMetadata, System.out);
        } catch (JAXBException ex) {
            //LoggerFactory.getLogger(SMPConverter.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.error(null, ex);
        }
    }

    /**
     *
     * @param docResourcePath
     * @return
     * @throws IOException
     */
    private String loadDocumentAsString(String docResourcePath) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(docResourcePath);

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    /**
     *
     * @param docContent
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private Document parseDocument(String docContent) throws IOException, SAXException, ParserConfigurationException {
        InputStream inputStream = new ByteArrayInputStream(docContent.getBytes());
        return getDocumentBuilder().parse(inputStream);
    }

    /**
     *
     * @return
     * @throws ParserConfigurationException
     */
    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder();
    }
}
