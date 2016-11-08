/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.service.mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.shelob.ws.client.jaxws.ClientConnectorService;
import se.sb.epsos.shelob.ws.client.jaxws.ClientConnectorServiceService;
import se.sb.epsos.shelob.ws.client.jaxws.EpsosDocument;
import se.sb.epsos.shelob.ws.client.jaxws.PatientDemographics;
import se.sb.epsos.shelob.ws.client.jaxws.PatientId;
import se.sb.epsos.shelob.ws.client.jaxws.QueryDocumentRequest;
import se.sb.epsos.shelob.ws.client.jaxws.QueryPatientRequest;
import se.sb.epsos.shelob.ws.client.jaxws.RetrieveDocumentRequest;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.CdaDocument;
import se.sb.epsos.web.model.CountryVO;
import se.sb.epsos.web.model.Dispensation;
import se.sb.epsos.web.model.PatientIdVO;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.TRC;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.NcpServiceFacade;
import se.sb.epsos.web.service.NcpServiceFacadeImpl;
import se.sb.epsos.web.service.TrcServiceHandler;
import se.sb.epsos.web.util.XmlTypeWrapper;
import se.sb.epsos.web.util.XmlUtil;
import epsos.ccd.gnomon.auditmanager.AuditService;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.netsmart.securitymanager.sts.client.TRCAssertionRequest;

public class NcpServiceFacadeMock implements NcpServiceFacade {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(NcpServiceFacadeMock.class);

	private NcpServiceFacade serviceFacade;
	private ClientConnectorService webServiceClient;
	private ClientConnectorServiceService service;
	private TrcServiceHandler trcServiceHandler;
	private AuditService auditService;
	private MockSettings settings = withSettings().serializable();

	public NcpServiceFacadeMock() {
		LOGGER.info("Creating NcpServiceFacadeMock");
		webServiceClient = mock(ClientConnectorService.class, settings);
		service = mock(ClientConnectorServiceService.class, settings);
		mockInitUserResponse(service);
		mockQueryPatentsResponse(webServiceClient);
		mockSetTRCAssertionResponse(webServiceClient);
		mockQueryDocumentsResponse(webServiceClient);
		mockRetrieveDocumentResponse(webServiceClient);
		mockSubmitDocumentResponse(webServiceClient);

		auditService = mock(AuditService.class, settings);
		when(auditService.write((EventLog) Mockito.notNull(), (String) Mockito.notNull(), (String) Mockito.notNull())).thenReturn(true);

		trcServiceHandler = mock(TrcServiceHandler.class, settings);
		mockBuildTrcRequestResponse(trcServiceHandler);

		serviceFacade = new NcpServiceFacadeImpl(service, webServiceClient, trcServiceHandler);
	}

	@Override
	public String about() {
		return getClass().getSimpleName() + " (offline:stubbed)";
	}

	@Override
	public void bindToSession(String sessionId) {
		serviceFacade.bindToSession(sessionId);
	}

	private void mockBuildTrcRequestResponse(TrcServiceHandler trcServiceHandlerMock) {
		try {
			when(trcServiceHandler.buildTrcRequest(any(Assertion.class), anyString(), anyString())).thenAnswer(new Answer<TRCAssertionRequest>() {
				@Override
				public TRCAssertionRequest answer(InvocationOnMock invocation) throws Throwable {
					final Assertion assertion = (Assertion) invocation.getArguments()[0];
					String patientId = (String) invocation.getArguments()[1];
					String purposeOfUse = (String) invocation.getArguments()[2];
					TRCAssertionRequest request = mock(TRCAssertionRequest.class, settings);
					when(request.request()).thenReturn(assertion);
					return request;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mockSubmitDocumentResponse(ClientConnectorService webServiceClientMock) {
//		try {
//			when(webServiceClientMock.submitDocument(any(SourceSubmissionClientDto.class), any(EhrPatientClientDto.class))).thenAnswer(
//					new Answer<EhrPatientClientDto>() {
//						@Override
//						public EhrPatientClientDto answer(InvocationOnMock invocation) throws Throwable {
//							EhrPatientClientDto dto = (EhrPatientClientDto) invocation.getArguments()[1];
//							return dto;
//						}
//					});
//		} catch (EhrException_Exception e) {
//			e.printStackTrace();
//		}
	}

	private void mockRetrieveDocumentResponse(ClientConnectorService webServiceClientMock) {
		when(webServiceClientMock.retrieveDocument(any(RetrieveDocumentRequest.class))).thenAnswer(new Answer<EpsosDocument>() {
			
			@Override
			public EpsosDocument answer(InvocationOnMock invocation) throws Throwable {
				RetrieveDocumentRequest req = (RetrieveDocumentRequest) invocation.getArguments()[0];
				String documentId = req.getDocumentId().getDocumentUniqueId();
				byte[] bytes = DocumentCatalog.get(documentId);
				EpsosDocument mock = new EpsosDocument();
				mock.setBase64Binary(bytes);
				return mock;
			}
		});
	}

	private void mockQueryDocumentsResponse(ClientConnectorService webServiceClientMock) {
		when(webServiceClientMock.queryDocuments(any(QueryDocumentRequest.class))).thenAnswer(new Answer<List<EpsosDocument>>() {
			
			@Override
			public List<EpsosDocument> answer(InvocationOnMock invocation) throws Throwable {
				List<EpsosDocument> result = new ArrayList<EpsosDocument>();
				
				QueryDocumentRequest request = (QueryDocumentRequest) invocation.getArguments()[0];
				PatientId patientId = request.getPatientId();
//						EpsosDocument qargs = (EpsosDocument) invocation.getArguments()[1];
//							if ("57833-6".equals(qargs.getClassCodes().get(0).getNodeRepresentation())) {
//								result = DocumentCatalog.queryEP(createLongPatientId(pids.get(0)));
//							} else if ("60591-5".equals(qargs.getClassCodes().get(0).getNodeRepresentation())) {
//								result = DocumentCatalog.queryPS(createLongPatientId(pids.get(0)));
//							}
				LOGGER.debug("PatientId: " + createLongPatientId(patientId));
				result = DocumentCatalog.queryEP(createLongPatientId(patientId));

                if(LOGGER.isDebugEnabled()){
                    for (EpsosDocument epsosDocument : result) {
                        LOGGER.debug("\n" + XmlUtil.marshallJaxbObject(new XmlTypeWrapper<EpsosDocument>(epsosDocument)));
                        LOGGER.debug("Doc: " + epsosDocument.getTitle());
                    }
                }
				
				//result.add(doc);
				return result;
			}
		});
	}

	private void mockSetTRCAssertionResponse(ClientConnectorService webServiceClientMock) {

	}

	private void mockQueryPatentsResponse(ClientConnectorService webServiceClientMock) {
		when(webServiceClientMock.queryPatient(any(QueryPatientRequest.class))).thenAnswer(new Answer<List<PatientDemographics>>() {
			@Override
			public List<PatientDemographics> answer(InvocationOnMock invocation) throws Throwable {
				List<PatientDemographics> result = new ArrayList<PatientDemographics>();
				QueryPatientRequest query = (QueryPatientRequest) invocation.getArguments()[0];
				String queryId = query.getPatientDemographics().getPatientId().get(0).getExtension() + "^^^&" + query.getPatientDemographics().getPatientId().get(0).getRoot(); // TODO: uses only the first one..
				PatientDemographics pat = PatientCatalog.query(queryId);
				if (pat != null) {
					result.add(pat);
				}
				return result;
			}
		});
	}
	
	 private AuthenticatedUser createUserDetailsMock() {
	        AuthenticatedUser user = mock(AuthenticatedUser.class, settings);
	        when(user.getUsername()).thenReturn("test");
	        when(user.getRoles()).thenReturn(Arrays.asList(new String[]{"ROLE_PHARMACIST"}));
	        when(user.getCommonName()).thenReturn("Unit Test");
	        when(user.getOrganizationName()).thenReturn("TEST");
	        when(user.getOrganizationId()).thenReturn("111");
	        return user;
	    }

	private void mockInitUserResponse(ClientConnectorServiceService service) {
		when(service.getPort(new QName("http://cc.pt.epsos.eu", "ClientConnectorServiceHttpSoap11Endpoint"), ClientConnectorService.class)).thenReturn(webServiceClient);
	}

	@Override
	public List<Person> queryForPatient(AuthenticatedUser user, List<PatientIdVO> list, CountryVO country) throws NcpServiceException {
		return serviceFacade.queryForPatient(user, list, country);
	}

	@Override
	public void initUser(AuthenticatedUser user) throws NcpServiceException {
		LOGGER.info("Method call: " + this.getClass().getSimpleName() + ".initUser()");
		serviceFacade.initUser(user);
	}

	@Override
	public void setTRCAssertion(TRC trc, AuthenticatedUser user) throws NcpServiceException {
		
	}

	@Override
	public List<MetaDocument> queryDocuments(Person person, String doctype, AuthenticatedUser user) throws NcpServiceException {
		return serviceFacade.queryDocuments(person, doctype, user);
	}

	@Override
	public CdaDocument retrieveDocument(MetaDocument doc) throws NcpServiceException {
		return serviceFacade.retrieveDocument(doc);
	}

	private String createLongPatientId(PatientId pid) {
		return pid.getExtension() + "^^^&" + pid.getRoot();
	}

	@Override
	public byte[] submitDocument(Dispensation dispensation, AuthenticatedUser user, Person person, String eD_PageAsString) throws NcpServiceException {
		return serviceFacade.submitDocument(dispensation, user, person, eD_PageAsString);
	}

}
