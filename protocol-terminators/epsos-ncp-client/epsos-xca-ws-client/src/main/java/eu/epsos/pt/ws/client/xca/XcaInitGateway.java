/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.pt.ws.client.xca;

import ee.affecto.epsos.util.EventLogClientUtil;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.dts.xds.AdhocQueryRequestCreator;
import eu.epsos.dts.xds.AdhocQueryResponseConverter;
import eu.epsos.exceptions.DocumentTransformationException;
import eu.epsos.exceptions.XCAException;
import eu.epsos.pt.transformation.TMServices;
import eu.epsos.validation.datamodel.cda.CdaModel;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.services.CdaValidationService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.phaseresolver.PhaseException;
import org.apache.axis2.util.XMLUtils;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.data.model.GenericDocumentCode;
import tr.com.srdc.epsos.data.model.PatientId;
import tr.com.srdc.epsos.data.model.xds.QueryResponse;
import tr.com.srdc.epsos.data.model.xds.XDSDocument;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.ws.xca.client.RespondingGateway_ServiceStub;
import tr.com.srdc.epsos.ws.xca.client.retrieve.RetrieveDocumentSetRequestTypeCreator;

/**
 * XCA Initiating Gateway
 *
 * This is an implementation of a IHE XCA Initiation Gateway. This class
 * provides the necessary operations to query and retrieve documents.
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class XcaInitGateway {

    private static Logger LOG = LoggerFactory.getLogger(XcaInitGateway.class);

    public static QueryResponse crossGatewayQuery(final PatientId pid, final String countryCode, final GenericDocumentCode documentCode,
            final Assertion idAssertion, final Assertion trcAssertion, String service) throws XCAException {

        QueryResponse result = null;

        try {

            /* queryRequest */
            AdhocQueryRequest queryRequest;
            queryRequest = AdhocQueryRequestCreator.createAdhocQueryRequest(pid.getExtension(), pid.getRoot(), documentCode);

            /* Stub */
            RespondingGateway_ServiceStub stub = new RespondingGateway_ServiceStub();
            String epr = ConfigurationManagerService.getInstance().getServiceWSE(countryCode.toLowerCase(Locale.ENGLISH), service);
            stub.setAddr(epr);
            stub._getServiceClient().getOptions().setTo(new EndpointReference(epr));
            EventLogClientUtil.createDummyMustUnderstandHandler(stub);

            /* queryRespose */
            AdhocQueryResponse queryResponse = stub.respondingGateway_CrossGatewayQuery(queryRequest, idAssertion, trcAssertion, documentCode.getValue());   // Request
            processRegistryErrors(queryResponse.getRegistryErrorList()); // Check result //TODO: Review generic exception throwed and remove last catch from try, catch sequence.

            if (queryResponse.getRegistryObjectList() != null) {
                result = AdhocQueryResponseConverter.convertAdhocQueryResponse(queryResponse);
            }

        } catch (PhaseException ex) {
            throw new RuntimeException(ex);
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        } catch (RuntimeException ex) {
            throw ex;
        }


        return result;
    }

    public static DocumentResponse crossGatewayRetrieve(
            final XDSDocument document,
            final String homeCommunityId,
            final String countryCode,
            final String targetLanguage,
            final Assertion idAssertion,
            final Assertion trcAssertion,
            String service) throws XCAException {

        DocumentResponse result = null;

        RetrieveDocumentSetResponseType queryResponse;

        String classCode = null;
        try {

            /* Request */
            RetrieveDocumentSetRequestType queryRequest;
            queryRequest = new RetrieveDocumentSetRequestTypeCreator().createRetrieveDocumentSetRequestType(document.getDocumentUniqueId(), homeCommunityId, document.getRepositoryUniqueId());

            /* Stub */
            RespondingGateway_ServiceStub stub;
            stub = new RespondingGateway_ServiceStub();

            String epr;
            if (service.equals(Constants.MroService)) {
                epr = ConfigurationManagerService.getInstance().getServiceWSE(countryCode.toLowerCase(Locale.ENGLISH), Constants.PatientService);
            } else {
                epr = ConfigurationManagerService.getInstance().getServiceWSE(countryCode.toLowerCase(Locale.ENGLISH), service);
            }
            stub.setAddr(epr);
            stub._getServiceClient().getOptions().setTo(new EndpointReference(epr));
            stub.setCountryCode(countryCode);
            EventLogClientUtil.createDummyMustUnderstandHandler(stub);
            // This is a rather dirty hack, but document.getClassCode() returns null for some reason.
            if (service.equals(Constants.OrderService)) {
                classCode = Constants.EP_CLASSCODE;
            } else if (service.equals(Constants.PatientService)) {
                classCode = Constants.PS_CLASSCODE;
            } else if (service.equals(Constants.MroService)) {
                classCode = Constants.MRO_CLASSCODE;
            }

            /* Request */
            queryResponse = stub.respondingGateway_CrossGatewayRetrieve(queryRequest, idAssertion, trcAssertion, classCode); // Request

            if (queryResponse.getRegistryResponse() != null) {
                RegistryErrorList registryErrorList = queryResponse.getRegistryResponse().getRegistryErrorList();
                processRegistryErrors(registryErrorList);
            }

        } catch (PhaseException ex) {
            LOG.error(ex.getLocalizedMessage(), ex);
            throw new RuntimeException(ex);
        } catch (AxisFault ex) {
            LOG.error(ex.getLocalizedMessage(), ex);
            throw new RuntimeException(ex);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }

        if (!queryResponse.getDocumentResponse().isEmpty()) {
            if (queryResponse.getDocumentResponse().size() > 1) {
                LOG.error("More than one documents where retrieved for request: id: " + document.getDocumentUniqueId()
                        + " comunit: " + homeCommunityId
                        + "registry: " + document.getRepositoryUniqueId());
            }
            try {
                CdaValidationService cdaValidationService = CdaValidationService.getInstance();

                /* Validate CDA epSOS Pivot */
                cdaValidationService.validateModel(XMLUtils.toOM(TMServices.byteToDocument(queryResponse.getDocumentResponse().get(0).getDocument()).getDocumentElement()).toString(), CdaModel.obtainCdaModel(document.getClassCode().getValue(), true), NcpSide.NCP_B);
                
                queryResponse.getDocumentResponse().get(0).setDocument(TMServices.transformDocument(queryResponse.getDocumentResponse().get(0).getDocument(), targetLanguage)); //Resets the response document to a translated version.

                /* Validate CDA epSOS Friendly-B */
                cdaValidationService.validateModel(XMLUtils.toOM(TMServices.byteToDocument(queryResponse.getDocumentResponse().get(0).getDocument()).getDocumentElement()).toString(), CdaModel.obtainCdaModel(document.getClassCode().getValue(), true), NcpSide.NCP_B);
            
            } catch (DocumentTransformationException ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            } catch (Exception ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            } finally {
                result = queryResponse.getDocumentResponse().get(0); //Returns the original document, even if the translation process fails.
            }
        }

        return result;
    }

    /**
     * Private constructor to disable class instantiation.
     */
    private XcaInitGateway() {
    }

    /**
     * Processes registry errors from the {@link AdhocQueryResponse} message, by
     * reporting them to the logging system.
     *
     * @param registryErrorList the list of errors from the
     * {@link AdhocQueryResponse} message.
     * @throws Exception thrown when an error has a severity of
     * "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error" type.
     */
    private static void processRegistryErrors(RegistryErrorList registryErrorList) throws XCAException {
        // A.R. ++ Error processing. For retrive. Is it needed?
        // We don't want to break on TSAM errors anyway...

        if (registryErrorList != null) {
            List<RegistryError> errorList = registryErrorList.getRegistryError();

            if (errorList != null) {
                String msg = "";
                boolean hasError = false;
                for (RegistryError error : errorList) {
                    String errorCode = error.getErrorCode();
                    String value = error.getValue();
                    String location = error.getLocation();
                    String severity = error.getSeverity();
                    String codeContext = error.getCodeContext();
                    LOG.error("errorCode=" + errorCode + "\ncodeContext=" + codeContext
                            + "\nlocation=" + location + "\nseverity=" + severity + "\n" + value + "\n");

                    if ("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error".equals(severity)) {
                        msg += errorCode + " " + codeContext + " " + value;
                        hasError = true;
                    } else if (errorCode.equals("1101") || errorCode.equals("1102")) { // Marcelo Fonseca: Added error situation where no document is found or registered, 1101/2. (Needs to be revised according to new error communication strategy to the portal).
                        msg += errorCode + " " + codeContext + " " + value;
                        hasError = true;
                    }

                    // Avoid the transformation errors to abort process - this way they are only logged in the upper instructions
                    if (checkTransformationErrors(errorCode)) {
                        continue;
                    }


                    //Throw all the remaining errors
                    if (hasError) {
                        LOG.error(msg);
                        throw new XCAException(errorCode);
                    }

                }
            }
        }
    }

    /**
     * This method will check if a given code is related to the document
     * transformation errors
     *
     * @param errorCode
     * @return
     */
    private static boolean checkTransformationErrors(String errorCode) {
        List<String> errorCodes = new ArrayList<String>() {
            {
                add("4500");
                add("4501");
                add("4502");
                add("4503");
                add("4504");
                add("4505");
                add("4506");
                add("4507");
                add("4508");
                add("4509");
                add("4510");
                add("4511");
                add("4512");
                add("2500");
                add("2501");
                add("2502");
                add("2503");
                add("2504");
                add("2505");
                add("2506");
                add("2507");
                add("2508");
            }
        };

        return errorCodes.contains(errorCode);
    }
}
