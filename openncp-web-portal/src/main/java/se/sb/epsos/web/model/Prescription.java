/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import se.sb.epsos.shelob.ws.client.jaxws.EpsosDocument;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.util.CdaHelper;

public class Prescription extends CdaDocument implements Serializable{

	private static final long serialVersionUID = 4296393437480963323L;
	
	private List<PrescriptionRow> rows;

	private String performer;
	private String profession;
	private String facility;
	private String address;
	private String contact1;
	private String contact2;
	private String country;
        
    private Date createDate;
	
	public Prescription(MetaDocument metaDoc) {
		super(metaDoc);
	}

//	public Prescription(MetaDocument metaDoc, byte[] bytes, EhrXdsRetrRsp rsp) throws ParserConfigurationException, SAXException, IOException {
//		super(metaDoc,bytes, rsp);
//		CdaHelper cdaHelper = new CdaHelper();
//		cdaHelper.parsePrescriptionFromDocument(this);
//	}

    public Prescription(MetaDocument metaDoc, byte[] bytes, EpsosDocument epsosDocument) throws ParserConfigurationException, SAXException, IOException {
        super(metaDoc,bytes, epsosDocument);
        CdaHelper cdaHelper = new CdaHelper();
        cdaHelper.parsePrescriptionFromDocument(this);
    }

    public List<PrescriptionRow> getRows() {
		return this.rows;
	}
	
	public void setRows(List<PrescriptionRow> rows) {
		this.rows = rows;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPerformer() {
		return performer;
	}

	public String getFacility() {
		return facility;
	}

	public String getProfession() {
		return profession;
	}

	public String getAddress() {
		return address;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getContact1() {
		return contact1;
	}

	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}

	public String getContact2() {
		return contact2;
	}

	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}	
	
}
