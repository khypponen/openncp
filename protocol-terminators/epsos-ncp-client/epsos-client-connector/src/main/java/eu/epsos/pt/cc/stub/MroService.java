/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2013 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
package eu.epsos.pt.cc.stub;

import eu.epsos.exceptions.XCAException;
import eu.epsos.pt.ws.client.xca.XcaInitGateway;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.opensaml.saml2.core.Assertion;
import tr.com.srdc.epsos.data.model.GenericDocumentCode;
import tr.com.srdc.epsos.data.model.PatientId;
import tr.com.srdc.epsos.data.model.xds.QueryResponse;
import tr.com.srdc.epsos.data.model.xds.XDSDocument;
import tr.com.srdc.epsos.util.Constants;

/**
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class MroService {

    public static QueryResponse list(final PatientId pid, final String countryCode, final GenericDocumentCode documentCode,
            final Assertion idAssertion, final Assertion trcAssertion) throws XCAException {
        return XcaInitGateway.crossGatewayQuery(pid, countryCode, documentCode, idAssertion, trcAssertion, Constants.MroService);
    }

    public static RetrieveDocumentSetResponseType.DocumentResponse retrieve(
            final XDSDocument document,
            final String homeCommunityId,
            final String countryCode,
            final String targetLanguage,
            final Assertion hcpAssertion,
            final Assertion trcAssertion) throws XCAException {
        return XcaInitGateway.crossGatewayRetrieve(document, homeCommunityId, countryCode, targetLanguage, hcpAssertion, trcAssertion, Constants.MroService);
    }

    private MroService() {
    }
}
