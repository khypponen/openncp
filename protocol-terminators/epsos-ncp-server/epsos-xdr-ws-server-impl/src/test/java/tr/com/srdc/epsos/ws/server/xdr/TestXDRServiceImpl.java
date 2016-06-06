package tr.com.srdc.epsos.ws.server.xdr;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.soap.SOAPModelBuilder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.w3c.dom.Element;

import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;
import tr.com.srdc.epsos.securityman.exceptions.InvalidFieldException;
import tr.com.srdc.epsos.securityman.exceptions.MissingFieldException;
import tr.com.srdc.epsos.util.FileUtil;

import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import eu.epsos.protocolterminators.ws.server.xdr.DocumentProcessingException;
import eu.epsos.protocolterminators.ws.server.xdr.DocumentSubmitInterface;
import eu.epsos.util.IheConstants;
import fi.kela.se.epsos.data.model.EPSOSDocument;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestXDRServiceImpl {

	//@Test
	public void testSaveHCERSuccess() throws Exception {
		File inFile = new File("src/test/resources/xdr-request.xml");
		
		EventLog el = mock(EventLog.class);
		DocumentSubmitInterface dsi = mock(DocumentSubmitInterface.class);
		oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory ofRs = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory();

		XDRServiceImpl xdrService = new XDRServiceImpl(dsi, ofRs) {
			// Mock SAML2Validator
		    protected String validateXDRHeader(Element sh, String classCode) throws MissingFieldException, InvalidFieldException, SMgrException, InsufficientRightsException  {
		    	return "sigCode";
		    }
		    
		    // Mock ConfigurationManagerService
		    protected String getLocation() {
		    	return "SomeLocation";
		    }
		};
		
		RegistryResponseType rrt = xdrService.saveHCER(getProvideAndRegisterDocumentSetRequestTypeHelper(inFile), getSOAPHeaderHelper(inFile), el);
		assertEquals(IheConstants.REGREP_RESPONSE_SUCCESS, rrt.getStatus());
		assertNull(rrt.getRegistryErrorList());
		assertNull(rrt.getResponseSlotList());
		
		ArgumentCaptor<Element> element = ArgumentCaptor.forClass(Element.class);
		verify(dsi).setSOAPHeader(element.capture());
		assertEquals(1, element.getAllValues().size());
		
		ArgumentCaptor<EPSOSDocument> epsosDoc = ArgumentCaptor.forClass(EPSOSDocument.class);
		verify(dsi).submitHCER(epsosDoc.capture());
		assertEquals(1, epsosDoc.getAllValues().size());
		
		verifyNoMoreInteractions(dsi, el);
	}

	//@Test
	public void testSaveHCERFailureNoSOAPHeader() throws Exception {
		File inFile = new File("src/test/resources/xdr-request.xml");
		
		EventLog el = mock(EventLog.class);
		DocumentSubmitInterface dsi = mock(DocumentSubmitInterface.class);
		oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory ofRs = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory();

		XDRServiceImpl xdrService = new XDRServiceImpl(dsi, ofRs) {
			// Mock SAML2Validator
		    protected String validateXDRHeader(Element sh, String classCode) throws MissingFieldException, InvalidFieldException, SMgrException, InsufficientRightsException  {
		    	return "sigCode";
		    }
		    
		    // Mock ConfigurationManagerService
		    protected String getLocation() {
		    	return "SomeLocation";
		    }
		};
		
		RegistryResponseType rrt = xdrService.saveHCER(getProvideAndRegisterDocumentSetRequestTypeHelper(inFile), null, el);
		assertEquals(IheConstants.REGREP_RESPONSE_SUCCESS, rrt.getStatus());
		assertNull(rrt.getRegistryErrorList());
		assertNull(rrt.getResponseSlotList());
		
		ArgumentCaptor<Element> element = ArgumentCaptor.forClass(Element.class);
		verify(dsi).setSOAPHeader(element.capture());
		assertEquals(1, element.getAllValues().size());
		
		ArgumentCaptor<EPSOSDocument> epsosDoc = ArgumentCaptor.forClass(EPSOSDocument.class);
		verify(dsi).submitHCER(epsosDoc.capture());
		assertEquals(1, epsosDoc.getAllValues().size());
		
		verifyNoMoreInteractions(dsi, el);
	}

	//@Test
	public void testSaveHCERFailureValidateXDRHeaderThrowsInsufficientRightsException() throws Exception {
		File inFile = new File("src/test/resources/xdr-request.xml");
		
		EventLog el = mock(EventLog.class);
		DocumentSubmitInterface dsi = mock(DocumentSubmitInterface.class);
		oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory ofRs = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory();

		XDRServiceImpl xdrService = new XDRServiceImpl(dsi, ofRs) {
			// Mock SAML2Validator
		    protected String validateXDRHeader(Element sh, String classCode) throws MissingFieldException, InvalidFieldException, SMgrException, InsufficientRightsException  {
		    	throw new InsufficientRightsException();
		    }
		    
		    // Mock ConfigurationManagerService
		    protected String getLocation() {
		    	return "SomeLocation";
		    }
		};
		
		RegistryResponseType rrt = xdrService.saveHCER(getProvideAndRegisterDocumentSetRequestTypeHelper(inFile), null, el);
		assertEquals(IheConstants.REGREP_RESPONSE_FAILURE, rrt.getStatus());
		assertEquals(1, rrt.getRegistryErrorList().getRegistryError().size());
		assertNull(rrt.getResponseSlotList());
		
		ArgumentCaptor<Element> element = ArgumentCaptor.forClass(Element.class);
		verify(dsi).setSOAPHeader(element.capture());
		assertEquals(1, element.getAllValues().size());
		
		ArgumentCaptor<EPSOSDocument> epsosDoc = ArgumentCaptor.forClass(EPSOSDocument.class);
		verify(dsi).submitHCER(epsosDoc.capture());
		assertEquals(1, epsosDoc.getAllValues().size());
		
		verifyNoMoreInteractions(dsi, el);
	}

	//@Test
	public void testSaveHCERFailureValidateXDRHeaderThrowsSMgrException() throws Exception {
		File inFile = new File("src/test/resources/xdr-request.xml");
		
		EventLog el = mock(EventLog.class);
		DocumentSubmitInterface dsi = mock(DocumentSubmitInterface.class);
		oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory ofRs = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory();

		XDRServiceImpl xdrService = new XDRServiceImpl(dsi, ofRs) {
			// Mock SAML2Validator
		    protected String validateXDRHeader(Element sh, String classCode) throws MissingFieldException, InvalidFieldException, SMgrException, InsufficientRightsException  {
		    	throw new SMgrException("ERROR");
		    }
		    
		    // Mock ConfigurationManagerService
		    protected String getLocation() {
		    	return "SomeLocation";
		    }
		};
		
		RegistryResponseType rrt = xdrService.saveHCER(getProvideAndRegisterDocumentSetRequestTypeHelper(inFile), null, el);
		assertEquals(IheConstants.REGREP_RESPONSE_FAILURE, rrt.getStatus());
		assertEquals(1, rrt.getRegistryErrorList().getRegistryError().size());
		assertNull(rrt.getResponseSlotList());
		
		ArgumentCaptor<Element> element = ArgumentCaptor.forClass(Element.class);
		verify(dsi).setSOAPHeader(element.capture());
		assertEquals(1, element.getAllValues().size());
		
		ArgumentCaptor<EPSOSDocument> epsosDoc = ArgumentCaptor.forClass(EPSOSDocument.class);
		verify(dsi).submitHCER(epsosDoc.capture());
		assertEquals(1, epsosDoc.getAllValues().size());
		
		verifyNoMoreInteractions(dsi, el);
	}

	//@Test
	public void testSaveHCERFailureValidateXDRHeaderThrowsInvalidFieldException() throws Exception {
		File inFile = new File("src/test/resources/xdr-request.xml");
		
		EventLog el = mock(EventLog.class);
		DocumentSubmitInterface dsi = mock(DocumentSubmitInterface.class);
		oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory ofRs = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory();

		XDRServiceImpl xdrService = new XDRServiceImpl(dsi, ofRs) {
			// Mock SAML2Validator
		    protected String validateXDRHeader(Element sh, String classCode) throws MissingFieldException, InvalidFieldException, SMgrException, InsufficientRightsException  {
		    	throw new InvalidFieldException("ERROR");
		    }
		    
		    // Mock ConfigurationManagerService
		    protected String getLocation() {
		    	return "SomeLocation";
		    }
		};
		
		RegistryResponseType rrt = xdrService.saveHCER(getProvideAndRegisterDocumentSetRequestTypeHelper(inFile), null, el);
		assertEquals(IheConstants.REGREP_RESPONSE_FAILURE, rrt.getStatus());
		assertEquals(1, rrt.getRegistryErrorList().getRegistryError().size());
		assertNull(rrt.getResponseSlotList());
		
		ArgumentCaptor<Element> element = ArgumentCaptor.forClass(Element.class);
		verify(dsi).setSOAPHeader(element.capture());
		assertEquals(1, element.getAllValues().size());
		
		ArgumentCaptor<EPSOSDocument> epsosDoc = ArgumentCaptor.forClass(EPSOSDocument.class);
		verify(dsi).submitHCER(epsosDoc.capture());
		assertEquals(1, epsosDoc.getAllValues().size());
		
		verifyNoMoreInteractions(dsi, el);
	}

	//@Test
	public void testSaveHCERFailureValidateXDRHeaderThrowsMissingFieldException() throws Exception {
		File inFile = new File("src/test/resources/xdr-request.xml");
		
		EventLog el = mock(EventLog.class);
		DocumentSubmitInterface dsi = mock(DocumentSubmitInterface.class);
		oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory ofRs = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory();

		XDRServiceImpl xdrService = new XDRServiceImpl(dsi, ofRs) {
			// Mock SAML2Validator
		    protected String validateXDRHeader(Element sh, String classCode) throws MissingFieldException, InvalidFieldException, SMgrException, InsufficientRightsException  {
		    	throw new MissingFieldException("ERROR");
		    }
		    
		    // Mock ConfigurationManagerService
		    protected String getLocation() {
		    	return "SomeLocation";
		    }
		};
		
		RegistryResponseType rrt = xdrService.saveHCER(getProvideAndRegisterDocumentSetRequestTypeHelper(inFile), null, el);
		assertEquals(IheConstants.REGREP_RESPONSE_FAILURE, rrt.getStatus());
		assertEquals(1, rrt.getRegistryErrorList().getRegistryError().size());
		assertNull(rrt.getResponseSlotList());
		
		ArgumentCaptor<Element> element = ArgumentCaptor.forClass(Element.class);
		verify(dsi).setSOAPHeader(element.capture());
		assertEquals(1, element.getAllValues().size());
		
		ArgumentCaptor<EPSOSDocument> epsosDoc = ArgumentCaptor.forClass(EPSOSDocument.class);
		verify(dsi).submitHCER(epsosDoc.capture());
		assertEquals(1, epsosDoc.getAllValues().size());
		
		verifyNoMoreInteractions(dsi, el);
	}

	//@Test
	public void testSaveHCERFailureValidateXDRHeaderThrowsDocumentProcessingException() throws Exception {
		File inFile = new File("src/test/resources/xdr-request.xml");
		
		EventLog el = mock(EventLog.class);
		DocumentSubmitInterface dsi = mock(DocumentSubmitInterface.class);
		oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory ofRs = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory();

		XDRServiceImpl xdrService = new XDRServiceImpl(dsi, ofRs) {
			// Mock SAML2Validator
		    protected String validateXDRHeader(Element sh, String classCode) throws MissingFieldException, InvalidFieldException, SMgrException, InsufficientRightsException  {
		    	return "sigCode";
		    }
		    
		    // Mock ConfigurationManagerService
		    protected String getLocation() {
		    	return "SomeLocation";
		    }
		};
		
		doThrow(new DocumentProcessingException()).when(dsi).submitHCER(any(EPSOSDocument.class));
		
		RegistryResponseType rrt = xdrService.saveHCER(getProvideAndRegisterDocumentSetRequestTypeHelper(inFile), null, el);
		assertEquals(IheConstants.REGREP_RESPONSE_FAILURE, rrt.getStatus());
		assertEquals(1, rrt.getRegistryErrorList().getRegistryError().size());
		assertNull(rrt.getResponseSlotList());
		
		ArgumentCaptor<Element> element = ArgumentCaptor.forClass(Element.class);
		verify(dsi).setSOAPHeader(element.capture());
		assertEquals(1, element.getAllValues().size());
		
		ArgumentCaptor<EPSOSDocument> epsosDoc = ArgumentCaptor.forClass(EPSOSDocument.class);
		verify(dsi).submitHCER(epsosDoc.capture());
		assertEquals(1, epsosDoc.getAllValues().size());
		
		verifyNoMoreInteractions(dsi, el);
	}
	private SOAPHeader getSOAPHeaderHelper(File inFile) throws Exception {
		SOAPModelBuilder smb = OMXMLBuilderFactory.createSOAPModelBuilder(new FileReader(inFile));
		return smb.getSOAPEnvelope().getHeader();
	}
	
	private ProvideAndRegisterDocumentSetRequestType getProvideAndRegisterDocumentSetRequestTypeHelper(File inFile) throws IOException {
		ProvideAndRegisterDocumentSetRequestType ret = new ProvideAndRegisterDocumentSetRequestType();

		String content = FileUtil.readWholeFile(inFile);
		
		ProvideAndRegisterDocumentSetRequestType.Document doc = new ProvideAndRegisterDocumentSetRequestType.Document();
		doc.setValue(content.getBytes());
		ret.getDocument().add(doc);
		
		SubmitObjectsRequest sob = new SubmitObjectsRequest();
		RegistryObjectListType rolt = new RegistryObjectListType();
		ExtrinsicObjectType eot = new ExtrinsicObjectType();
		JAXBElement<IdentifiableType> ji = new JAXBElement<IdentifiableType>(new QName(""), IdentifiableType.class, eot);
		ji.setValue(eot);
		SlotType1 st1 = new SlotType1();
		st1.setName("sourcePatientId");
		ValueListType vlt = new ValueListType();
		vlt.getValue().add("SomeId^^^SomeOid");
		st1.setValueList(vlt);
		eot.getSlot().add(st1);
		rolt.getIdentifiable().add(ji);
		sob.setRegistryObjectList(rolt);
		ret.setSubmitObjectsRequest(sob);
		return ret;
	}
}
