package com.gnomon.epsos.model.cda;

public class EDDetail {

    public String relativePrescriptionLineId;
    public String dispensedQuantity;
    public String dispensedQuantityUnit;
    public String medicineBarcode;
    public String medicineTainiaGnisiotitas;
    public String medicineEofCode;
    public String medicineCommercialName;
    public String medicinePackageFormCode;
    public String medicinePackageFormCodeDescription;
    public String medicineFormCode;
    public String medicineFormCodeDescription;
    public String medicineCapacityQuantity;
    public String medicineDrastikiATCCode;
    public String medicineDrastikiName;
    public String patientInstructions;
    public String medicinePrice;
    public String medicineRefPrice;
    public String patientParticipation;
    public String tameioParticipation;
    public String patientDifference;
    public String medicineExecutionCase;
    //Detail substitution indicator
    private boolean substituted;

    public String getRelativePrescriptionLineId() {
        return relativePrescriptionLineId;
    }

    public void setRelativePrescriptionLineId(String relativePrescriptionLineId) {
        this.relativePrescriptionLineId = relativePrescriptionLineId;
    }

    public String getDispensedQuantity() {
        return dispensedQuantity;
    }

    public void setDispensedQuantity(String dispensedQuantity) {
        this.dispensedQuantity = dispensedQuantity;
    }

    public String getMedicineBarcode() {
        return medicineBarcode;
    }

    public void setMedicineBarcode(String medicineBarcode) {
        this.medicineBarcode = medicineBarcode;
    }

    public String getMedicineEofCode() {
        return medicineEofCode;
    }

    public void setMedicineEofCode(String medicineEofCode) {
        this.medicineEofCode = medicineEofCode;
    }

    public String getMedicineCommercialName() {
        return medicineCommercialName;
    }

    public void setMedicineCommercialName(String medicineCommercialName) {
        this.medicineCommercialName = medicineCommercialName;
    }

    public String getMedicineFormCode() {
        return medicineFormCode;
    }

    public void setMedicineFormCode(String medicineFormCode) {
        this.medicineFormCode = medicineFormCode;
    }

    public String getMedicineFormCodeDescription() {
        return medicineFormCodeDescription;
    }

    public void setMedicineFormCodeDescription(String medicineFormCodeDescription) {
        this.medicineFormCodeDescription = medicineFormCodeDescription;
    }

    public String getMedicineCapacityQuantity() {
        return medicineCapacityQuantity;
    }

    public void setMedicineCapacityQuantity(String medicineCapacityQuantity) {
        this.medicineCapacityQuantity = medicineCapacityQuantity;
    }

    public String getMedicineDrastikiATCCode() {
        return medicineDrastikiATCCode;
    }

    public void setMedicineDrastikiATCCode(String medicineDrastikiATCCode) {
        this.medicineDrastikiATCCode = medicineDrastikiATCCode;
    }

    public String getMedicineDrastikiName() {
        return medicineDrastikiName;
    }

    public void setMedicineDrastikiName(String medicineDrastikiName) {
        this.medicineDrastikiName = medicineDrastikiName;
    }

    public String getPatientInstructions() {
        return patientInstructions;
    }

    public void setPatientInstructions(String patientInstructions) {
        this.patientInstructions = patientInstructions;
    }

    public String getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(String medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    public String getMedicineRefPrice() {
        return medicineRefPrice;
    }

    public void setMedicineRefPrice(String medicineRefPrice) {
        this.medicineRefPrice = medicineRefPrice;
    }

    public String getPatientParticipation() {
        return patientParticipation;
    }

    public void setPatientParticipation(String patientParticipation) {
        this.patientParticipation = patientParticipation;
    }

    public String getTameioParticipation() {
        return tameioParticipation;
    }

    public void setTameioParticipation(String tameioParticipation) {
        this.tameioParticipation = tameioParticipation;
    }

    public String getPatientDifference() {
        return patientDifference;
    }

    public void setPatientDifference(String patientDifference) {
        this.patientDifference = patientDifference;
    }

    public String getMedicineExecutionCase() {
        return medicineExecutionCase;
    }

    public void setMedicineExecutionCase(String medicineExecutionCase) {
        this.medicineExecutionCase = medicineExecutionCase;
    }

    public String getMedicineTainiaGnisiotitas() {
        return medicineTainiaGnisiotitas;
    }

    public void setMedicineTainiaGnisiotitas(String medicineTainiaGnisiotitas) {
        this.medicineTainiaGnisiotitas = medicineTainiaGnisiotitas;
    }

    public String getDispensedQuantityUnit() {
        return dispensedQuantityUnit;
    }

    public void setDispensedQuantityUnit(String dispensedQuantityUnit) {
        this.dispensedQuantityUnit = dispensedQuantityUnit;
    }

    public String getMedicinePackageFormCode() {
        return medicinePackageFormCode;
    }

    public void setMedicinePackageFormCode(String medicinePackageFormCode) {
        this.medicinePackageFormCode = medicinePackageFormCode;
    }

    public String getMedicinePackageFormCodeDescription() {
        return medicinePackageFormCodeDescription;
    }

    public void setMedicinePackageFormCodeDescription(
            String medicinePackageFormCodeDescription) {
        this.medicinePackageFormCodeDescription = medicinePackageFormCodeDescription;
    }

    /**
     * A method returning if the detail is substituted.
     *
     * @return if the detail is substituted.
     */
    public boolean isSubstituted() {
        return substituted;
    }

    /**
     * A method setting the detailed as substituted.
     *
     * @param substituted the substitution flag to set.
     */
    public void setSubstituted(boolean substituted) {
        this.substituted = substituted;
    }
}
