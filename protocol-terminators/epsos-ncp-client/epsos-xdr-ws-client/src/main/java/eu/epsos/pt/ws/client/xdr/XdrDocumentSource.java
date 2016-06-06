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
package eu.epsos.pt.ws.client.xdr;

import eu.epsos.exceptions.XdrException;
import eu.epsos.pt.ws.client.xdr.dts.XdrResponseDts;
import java.rmi.RemoteException;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.axis2.AxisFault;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.data.model.XdrRequest;
import tr.com.srdc.epsos.data.model.XdrResponse;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.ws.xdr.client.XDSbRepositoryServiceInvoker;

/**
 * Represents a Document Source Actor, from the IHE XDR (Cross-enterprise
 * Document Reliable Interchange) Profile.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public final class XdrDocumentSource {

    /**
     * Logger.
     */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(XdrDocumentSource.class);

    /**
     * Implements the necessary mechanisms to provide and register a document
     * next to the XDR Document Recipient Actor.
     *
     * @param request a XDR request, encapsulating the CDA and it's Metadata
     * @param countryCode the country code of the requesting country in ISO
     * format.
     */
    public static XdrResponse initialize(final XdrRequest request, final String countryCode) throws XdrException {
        RegistryResponseType response;

        try {
            response = new XDSbRepositoryServiceInvoker().provideAndRegisterDocumentSet(request, countryCode, Constants.ED_CLASSCODE);
            if (response.getRegistryErrorList() != null) {
                RegistryErrorList registryErrorList = response.getRegistryErrorList();
                processRegistryErrors(registryErrorList);
            }
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        } catch (RuntimeException ex) {
            throw ex;
        }
        return XdrResponseDts.newInstance(response);
    }

    /**
     * Implements the necessary mechanisms to provide and register a document
     * next to the XDR Document Recipient Actor.
     *
     * @param request a XDR request, encapsulating the CDA and it's Metadata
     * @param countryCode the country code of the requesting country in ISO
     * format.
     */
    public static XdrResponse provideAndRegisterDocSet(final XdrRequest request, final String countryCode, String docClassCode) throws XdrException {
        RegistryResponseType response;

        try {
            response = new XDSbRepositoryServiceInvoker().provideAndRegisterDocumentSet(request, countryCode, docClassCode);
            if (response.getRegistryErrorList() != null) {
                RegistryErrorList registryErrorList = response.getRegistryErrorList();
                processRegistryErrors(registryErrorList);
            }
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        } catch (RuntimeException ex) {
            throw ex;
        }
        return XdrResponseDts.newInstance(response);
    }

    /**
     * Private constructor to disable class instantiation.
     */
    private XdrDocumentSource() {
    }

    /**
     * Processes all the registry errors (if existing), from the XDR response.
     *
     * @param registryErrorList the Registry Error List to be processed.
     */
    private static void processRegistryErrors(final RegistryErrorList registryErrorList) throws XdrException {

        if (registryErrorList == null) {
            return;
        }

        List<RegistryError> errorList = registryErrorList.getRegistryError();
        if (errorList == null) {
            return;
        }

        StringBuilder srtBuilder = new StringBuilder();
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
                srtBuilder.append(errorCode).append(" ").append(codeContext).append(" ").append(value);
                hasError = true;
            }
            if (hasError) {
                 if(errorCode != null && errorCode.trim().length() > 0) {
                    throw new XdrException(errorCode);
                } else {
                    throw new XdrException(codeContext);
                }

            }
        }


    }
}
