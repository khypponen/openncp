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

import java.io.Serializable;
import java.util.Date;

public class DispensationRow implements Serializable {

	private static final long serialVersionUID = -7340918304594660151L;

	private PrescriptionRow prescriptionRow;

	private boolean dispense;
	private String productName;
	private String productId;
	private QuantityVO packageSize;
	private QuantityVO nbrPackages;
	private boolean substitute;
	private Date dispensationDate = new Date();
		
	public DispensationRow(PrescriptionRow prescriptionRow) {
		this.prescriptionRow = prescriptionRow;
	}

	public PrescriptionRow getPrescriptionRow() {
		return this.prescriptionRow;
	}

	public boolean isDispense() {
		return dispense;
	}

	public void setDispense(boolean dispense) {
		this.dispense = dispense;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public QuantityVO getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(QuantityVO packageSize) {
		this.packageSize = packageSize;
	}

	public QuantityVO getNbrPackages() {
		return nbrPackages;
	}

	public void setNbrPackages(QuantityVO nbrPackages) {
		this.nbrPackages = nbrPackages;
	}

	public void setPrescriptionRow(PrescriptionRow prescriptionRow) {
		this.prescriptionRow = prescriptionRow;
	}

	public void setSubstitute(boolean substitute) {
		this.substitute = substitute;
	}

	public boolean isSubstitute() {
		return substitute;
	}
	public Date getDispensationDate() {
		return dispensationDate;
	}

	public void setDispensationDate(Date dispensationDate) {
		this.dispensationDate = dispensationDate;
	}

	
}
