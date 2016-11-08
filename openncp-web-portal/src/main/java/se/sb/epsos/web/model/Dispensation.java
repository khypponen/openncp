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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import se.sb.epsos.web.service.DocumentClientDtoCacheKey;


public class Dispensation extends CdaDocument {

	private static final long serialVersionUID = -308023631714191605L;
	
	private Prescription prescription;
	
	private List<DispensationRow> rows;
	
	public Dispensation(String sessionId, String patientId, Prescription prescription) { 
        super(new DocumentClientDtoCacheKey(sessionId, patientId, "urn:uuid:"+UUID.randomUUID().toString()));
        this.prescription = prescription;
        rows = new ArrayList<DispensationRow>();
        if(prescription != null) {
            for (PrescriptionRow prescriptionRow : prescription.getRows()) {
                rows.add(new DispensationRow(prescriptionRow));
            }
        }
	}
	
	public List<DispensationRow> getRows() {
		return this.rows;
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

}
