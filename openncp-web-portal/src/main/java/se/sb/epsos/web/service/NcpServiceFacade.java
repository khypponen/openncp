/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.service;

import java.io.Serializable;
import java.util.List;

import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.CdaDocument;
import se.sb.epsos.web.model.CountryVO;
import se.sb.epsos.web.model.Dispensation;
import se.sb.epsos.web.model.PatientIdVO;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.TRC;

/**
 * Created by IntelliJ IDEA.
 * User: andreas
 * Date: 2011-06-27
 * Time: 11.20
 * To change this template use File | Settings | File Templates.
 */
public interface NcpServiceFacade extends Serializable {

    public String about();

    public void initUser(AuthenticatedUser userDetails) throws NcpServiceException;

    public List<Person> queryForPatient(AuthenticatedUser userDetails, List<PatientIdVO> patientList, CountryVO country) throws NcpServiceException;

    public void setTRCAssertion(TRC trc, AuthenticatedUser userDetails) throws NcpServiceException;

    public List<MetaDocument> queryDocuments(Person person, String doctype, AuthenticatedUser userDetails) throws NcpServiceException;

    public CdaDocument retrieveDocument(MetaDocument doc) throws NcpServiceException;

    public byte[] submitDocument(Dispensation dispensation, AuthenticatedUser user, Person person, String eD_PageAsString) throws NcpServiceException;
    
    public void bindToSession(String sessionId);
}
