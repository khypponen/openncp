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
import java.util.List;

public class PrescriptionRow implements Serializable {
	private static final long serialVersionUID = -5914345465484415553L;

	private String prescriptionId;
	private String prescriptionIdRoot;
	private String productName;
	private String productId;
	private String formCode;
	private String formName;
	private String atcCode;
	private String atcName;
	private String dosage;
	private String frequency;
	private String route;
	private String patientInstructions;
	private String pharmacistInstructions;
	private String prescriber;
	private String materialId;
	private String substitutionPermittedText;
	private String typeOfPackage;

	private String startDate;
	private String endDate;

	private List<Ingredient> ingredient;

	private QuantityVO packageSize;
	private QuantityVO nbrPackages;
	private boolean substitutionPermitted = false;

	public String getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(String prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getPrescriptionIdRoot() {
		return prescriptionIdRoot;
	}

	public void setPrescriptionIdRoot(String prescriptionIdRoot) {
		this.prescriptionIdRoot = prescriptionIdRoot;
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

	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getAtcCode() {
		return atcCode;
	}

	public void setAtcCode(String atcCode) {
		this.atcCode = atcCode;
	}

	public String getAtcName() {
		return atcName;
	}

	public void setAtcName(String atcName) {
		this.atcName = atcName;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getPatientInstructions() {
		return patientInstructions;
	}

	public void setPatientInstructions(String patientInstructions) {
		this.patientInstructions = patientInstructions;
	}

	public String getPharmacistInstructions() {
		return pharmacistInstructions;
	}

	public void setPharmacistInstructions(String pharmacistInstructions) {
		this.pharmacistInstructions = pharmacistInstructions;
	}

	public String getPrescriber() {
		return prescriber;
	}

	public void setPrescriber(String prescriber) {
		this.prescriber = prescriber;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getSubstitutionPermittedText() {
		return substitutionPermittedText;
	}

	public void setSubstitutionPermittedText(String substitutionPermittedText) {
		this.substitutionPermittedText = substitutionPermittedText;
	}

	public boolean isSubstitutionPermitted() {
		return substitutionPermitted;
	}

	public void setSubstitutionPermitted(boolean substitutionPermitted) {
		this.substitutionPermitted = substitutionPermitted;
	}

	public String getTypeOfPackage() {
		return typeOfPackage;
	}

	public void setTypeOfPackage(String typeOfPackage) {
		this.typeOfPackage = typeOfPackage;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Ingredient> getIngredient() {
		return ingredient;
	}

	public void setIngredient(List<Ingredient> ingredient) {
		this.ingredient = ingredient;
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

	public String getDescription() {
		String descr = "";
		if (this.ingredient != null) {
			for (Ingredient in : this.ingredient) {
				descr = descr + (in.getActiveIngredient() != null ? (in.getActiveIngredient()) : "");
				descr = descr + (in.getStrength() != null ? (" " + in.getStrength()) : "");
				descr = descr + ", ";
			}
		}

		descr = descr + (this.packageSize.getQuantityValue() != null ? ("" + this.packageSize.getQuantityValue()) : "");
		descr = descr + (this.packageSize.getQuantityUnit() != null ? (" " + this.packageSize.getQuantityUnit()) : "");
		descr = descr + (this.formName != null ? (" " + this.formName) : "");
		descr = descr + (this.productName != null ? (" (" + this.productName + ")") : "");
		return descr;
	}

}
