/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.sb.epsos.web.admin;

import java.util.List;

import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.model.CdaDocument;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.NcpServiceFacade;

/**
 *
 * @author danielgronberg
 */
public class NcpStatusHelper {

    public static CdaDocument getEpForPerson(NcpServiceFacade facade, Person person) throws NcpServiceException {
        List<MetaDocument> docs;
        CdaDocument ep = null;
        docs = facade.queryDocuments(person, "EP", null);
        if (docs.size() > 0) {
            MetaDocument epDoc = docs.get(0);
            ep = facade.retrieveDocument(epDoc);
        }
        return ep;
    }

    public static CdaDocument getPSForPerson(NcpServiceFacade facade, Person person) throws NcpServiceException {
        List<MetaDocument> docs;
        CdaDocument ps = null;
        docs = facade.queryDocuments(person, "PS", null);
        if (docs.size() > 0) {
            MetaDocument epDoc = docs.get(0);
            ps = facade.retrieveDocument(epDoc);
        }
        return ps;
    }

    public static void initFacade(NcpServiceFacade facade) throws NcpServiceException {
        EpsosAuthenticatedWebSession session = (EpsosAuthenticatedWebSession) EpsosAuthenticatedWebSession.get();
        facade.bindToSession(session.getId());
        facade.initUser(session.getUserDetails());
    }
}
