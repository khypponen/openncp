/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.MVC.enums;

/**
 *
 * @author bernamp
 */
public enum SHEET {

    epSOSActCode(true, "HL7", ""), epSOSActiveIngredient(true, "ATC", ""), epSOSActSite(false, "HL7", "ActSite.csv"),
    epSOSAdministrativeGender(false, "HL7", "administrative-gender.csv"), epSOSAdverseEventType(true, "SNOMED_CT", ""),
    epSOSAllergenNoDrugs(true, "SNOMED_CT", ""), epSOSBloodGroup(true, "SNOMED_CT", ""), epSOSBloodPressure(false, "LOINC", "loinc.csv"),
    epSOSCodeNoMedication(true, "SNOMED_CT", ""), epSOSCodeProb(true, "SNOMED_CT", ""),
    epSOSConfidentiality(false, "HL7", "ConfidentialityCode.csv"), epSOSCountry(true, "ISO 3166-1", "countries.csv"),
    epSOSDisplayLabel(true, "", ""), epSOSDocumentCode(false, "LOINC", "loinc.csv"), epSOSDoseForm(true, "EDQM", ""),
    epSOSEntityNamePartQualifier(false, "HL7", "EntityNamePartQualifier"), epSOSHealthcareProfessionalRole(false, "ISCO", "isco.csv"), 
    epSOSIHEActCode(true, "HL7", "IHEActCode.csv"), epSOSIHERoleCode(false, "HL7", "IHERoleCode.csv"), 
    epSOSIllnessesandDisorders(true, "ICD-10", "ICD-10.xml"), epSOSLanguage(true, "ISO 639-1", "language-codes.csv"),
    epSOSMedicalDevices(true, "SNOMED_CT", ""), epSOSMedicalEquipment(true, "SNOMED_CT", ""), epSOSNullFavor(true, "HL7", "NullFlavor.csv"),
    epSOSObservationInterpretation(false, "HL7", "ObservationInterpretation.csv"), epSOSPackage(true, "EDQM", ""),
    epSOSPersonalRelationship(true, "HL7", ""), epSOSPregnancyInformation(false, "LOINC", "loinc.csv"), epSOSProcedures(true, "SNOMED_CT", ""),
    epSOSReactionAllergy(true, "SNOMED_CT", ""), epSOSResolutionOutcome(true, "SNOMED_CT", ""), epSOSRoleClass(false, "HL7", "RoleClass.csv"),
    epSOSRouteofAdministration(true, "EDQM", ""), epSOSSections(false, "LOINC", "loinc.csv"), epSOSSeverity(true, "SNOMED_CT", ""),
    epSOSSocialHistory(true, "SNOMED_CT", ""), epSOSStatusCode(true, "SNOMED_CT", ""), epSOSSubstitutionCode(true, "HL7", ""),
    epSOSTelecomAddress(true, "HL7", ""), epSOSTimingEvent(false, "HL7", ""), epSOSUnits(true, "UCUM Unified Code for Units of Measure", ""),
    epSOSUnknownInformation(true, "SNOMED_CT", ""), epSOSURL(true, "HL7", ""), epSOSVaccine(true, "SNOMED_CT", "");

    private final boolean codeSystem;
    private final String parent;
    private final String filename;

    SHEET(boolean isCodeSystem, String parent, String filename) {
        this.codeSystem = isCodeSystem;
        this.parent = parent;
        this.filename = filename;
    }

    /**
     * @return the hasCodeSystem
     */
    public boolean isCodeSystem() {
        return codeSystem;
    }

    public static boolean isSheetInList(String s) {
        for (SHEET parse : SHEET.values())
            if (s.equals(parse.name()))
                return true;
        return false;
    }
}
