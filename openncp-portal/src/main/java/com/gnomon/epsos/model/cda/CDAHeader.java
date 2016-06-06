package com.gnomon.epsos.model.cda;

import java.util.List;

public class CDAHeader {
	
	public String effectiveTime;
	public String expireDate;
	public String languageCode;
	public String patientId;
	public String patientAmka;
	public String patientAma;
	public String patientTameio;
	public String patientDikaiouxosEkas;
	public String patientAmesaAsfalismenos;
	public String patientAsfalistikiIkanotita;
	public String patientEidosEmesaAsfalismenou;
	public String patientAsfalistikiIkanotitaEndDate;
	public String patientAddress;
	public String patientCity;
	public String patientPostalCode;
	public String patientCountry;
	public String patientFamilyName;
	public String patientPrefix;
	public String patientGivenName;
	public String patientSex;
	public String patientBirthDate;
	public String patientTelephone;
	public String patientEmail;
	public String patientFax;
	public String patientLanguageCommunication;
	
	public String guardianAmka;
	public String guardianAddress;
	public String guardianCity;
	public String guardianPostalCode;
	public String guardianCountry;
	public String guardianFamilyName;
	public String guardianPrefix;
	public String guardianGivenName;
	public String guardianSex;
	public String guardianBirthDate;
	public String guardianTelephone;
	public String guardianEmail;
	public String guardianFax;

	public boolean hasParticipant;
	public boolean hasGuardian;
	public String participantType;
	public String participantAddress;
	public String participantCity;
	public String participantPostalCode;
	public String participantCountry;
	public String participantFamilyName;
	public String participantPrefix;
	public String participantGivenName;
	public String participantSex;
	public String participantBirthDate;
	public String participantTelephone;
	public String participantEmail;
	public String participantFax;

	public String pharmacistAMKA;
	public String pharmacistAddress;
	public String pharmacistCity;
	public String pharmacistPostalCode;
	public String pharmacistCountry;	
	public String pharmacistFamilyName;
	public String pharmacistPrefix;
	public String pharmacistGivenName;
	public String pharmacistSex;
	public String pharmacistBirthDate;
	public String pharmacistTelephone;
	public String pharmacistEmail;
	public String pharmacistFax;
	public String pharmacistOrgId;
	public String pharmacistOrgName;
	public String pharmacistOrgAddress;
	public String pharmacistOrgCity;
	public String pharmacistOrgPostalCode;
	public String pharmacistOrgCountry;
	public String pharmacistOrgTelephone;
	public String pharmacistOrgEmail;
	public String pharmacistOid;
	
	public String doctorSpeciality;
	public String prescriptionIssueDate;
	public String prescriptionStartDate;
	public String prescriptionEndDate;
	public String doctorAMKA;
	public String doctorETAA;
	public String doctorAddress;
	public String doctorCity;
	public String doctorPostalCode;
	public String doctorCountry;	
	public String doctorFamilyName;
	public String doctorPrefix;
	public String doctorGivenName;
	public String doctorSex;
	public String doctorBirthDate;
	public String doctorTelephone;
	public String doctorEmail;
	public String doctorFax;
	public String doctorUnit;
	public String doctorOrgId;
	public String doctorOrgName;
	public String doctorOrgAddress;
	public String doctorOrgCity;
	public String doctorOrgPostalCode;
	public String doctorOrgCountry;
	public String doctorOrgTelephone;
	public String doctorOrgEmail;
	public String doctorOid;
	
	public String prescriptionIDRecurrent;
	public String prescriptionBarcodeRecurrent;
	public String prescriptionType;
	public String prescriptionRecurrent;
	public String prescriptionBarcode;
	public String prescriptionTitle;
	public String dispensationId;
	
	public String consentCode;
	public String consentDisplayName;
	public String consentStartDate;
	public String consentEndDate;
	
	public List EPDetail;
	public List EDDetail;
	
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public String getPatientAmka() {
		return patientAmka;
	}
	public void setPatientAmka(String patientAmka) {
		this.patientAmka = patientAmka;
	}
	public String getPatientAma() {
		return patientAma;
	}
	public void setPatientAma(String patientAma) {
		this.patientAma = patientAma;
	}
	public String getPatientTameio() {
		return patientTameio;
	}
	public void setPatientTameio(String patientTameio) {
		this.patientTameio = patientTameio;
	}
	public String getPatientDikaiouxosEkas() {
		return patientDikaiouxosEkas;
	}
	public void setPatientDikaiouxosEkas(String patientDikaiouxosEkas) {
		this.patientDikaiouxosEkas = patientDikaiouxosEkas;
	}
	public String getPatientAmesaAsfalismenos() {
		return patientAmesaAsfalismenos;
	}
	public void setPatientAmesaAsfalismenos(String patientAmesaAsfalismenos) {
		this.patientAmesaAsfalismenos = patientAmesaAsfalismenos;
	}
	public String getPatientAsfalistikiIkanotita() {
		return patientAsfalistikiIkanotita;
	}
	public void setPatientAsfalistikiIkanotita(String patientAsfalistikiIkanotita) {
		this.patientAsfalistikiIkanotita = patientAsfalistikiIkanotita;
	}
	public String getPatientAsfalistikiIkanotitaEndDate() {
		return patientAsfalistikiIkanotitaEndDate;
	}
	public void setPatientAsfalistikiIkanotitaEndDate(
			String patientAsfalistikiIkanotitaEndDate) {
		this.patientAsfalistikiIkanotitaEndDate = patientAsfalistikiIkanotitaEndDate;
	}
	public String getPatientAddress() {
		return patientAddress;
	}
	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}
	public String getPatientCity() {
		return patientCity;
	}
	public void setPatientCity(String patientCity) {
		this.patientCity = patientCity;
	}
	public String getPatientPostalCode() {
		return patientPostalCode;
	}
	public void setPatientPostalCode(String patientPostalCode) {
		this.patientPostalCode = patientPostalCode;
	}
	public String getPatientCountry() {
		return patientCountry;
	}
	public void setPatientCountry(String patientCountry) {
		this.patientCountry = patientCountry;
	}
	public String getPatientFamilyName() {
		return patientFamilyName;
	}
	public void setPatientFamilyName(String patientFamilyName) {
		this.patientFamilyName = patientFamilyName;
	}
	public String getPatientPrefix() {
		return patientPrefix;
	}
	public void setPatientPrefix(String patientPrefix) {
		this.patientPrefix = patientPrefix;
	}
	public String getPatientGivenName() {
		return patientGivenName;
	}
	public void setPatientGivenName(String patientGivenName) {
		this.patientGivenName = patientGivenName;
	}
	public String getPatientSex() {
		return patientSex;
	}
	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}
	public String getPatientBirthDate() {
		return patientBirthDate;
	}
	public void setPatientBirthDate(String patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}
	public String getPatientTelephone() {
		return patientTelephone;
	}
	public void setPatientTelephone(String patientTelephone) {
		this.patientTelephone = patientTelephone;
	}
	public String getPatientEmail() {
		return patientEmail;
	}
	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}
	public String getPatientFax() {
		return patientFax;
	}
	public void setPatientFax(String patientFax) {
		this.patientFax = patientFax;
	}
	public String getGuardianAmka() {
		return guardianAmka;
	}
	public void setGuardianAmka(String guardianAmka) {
		this.guardianAmka = guardianAmka;
	}
	public String getGuardianAddress() {
		return guardianAddress;
	}
	public void setGuardianAddress(String guardianAddress) {
		this.guardianAddress = guardianAddress;
	}
	public String getGuardianCity() {
		return guardianCity;
	}
	public void setGuardianCity(String guardianCity) {
		this.guardianCity = guardianCity;
	}
	public String getGuardianPostalCode() {
		return guardianPostalCode;
	}
	public void setGuardianPostalCode(String guardianPostalCode) {
		this.guardianPostalCode = guardianPostalCode;
	}
	public String getGuardianCountry() {
		return guardianCountry;
	}
	public void setGuardianCountry(String guardianCountry) {
		this.guardianCountry = guardianCountry;
	}
	public String getGuardianFamilyName() {
		return guardianFamilyName;
	}
	public void setGuardianFamilyName(String guardianFamilyName) {
		this.guardianFamilyName = guardianFamilyName;
	}
	public String getGuardianPrefix() {
		return guardianPrefix;
	}
	public void setGuardianPrefix(String guardianPrefix) {
		this.guardianPrefix = guardianPrefix;
	}
	public String getGuardianGivenName() {
		return guardianGivenName;
	}
	public void setGuardianGivenName(String guardianGivenName) {
		this.guardianGivenName = guardianGivenName;
	}
	public String getGuardianSex() {
		return guardianSex;
	}
	public void setGuardianSex(String guardianSex) {
		this.guardianSex = guardianSex;
	}
	public String getGuardianBirthDate() {
		return guardianBirthDate;
	}
	public void setGuardianBirthDate(String guardianBirthDate) {
		this.guardianBirthDate = guardianBirthDate;
	}
	public String getGuardianTelephone() {
		return guardianTelephone;
	}
	public void setGuardianTelephone(String guardianTelephone) {
		this.guardianTelephone = guardianTelephone;
	}
	public String getGuardianEmail() {
		return guardianEmail;
	}
	public void setGuardianEmail(String guardianEmail) {
		this.guardianEmail = guardianEmail;
	}
	public String getGuardianFax() {
		return guardianFax;
	}
	public void setGuardianFax(String guardianFax) {
		this.guardianFax = guardianFax;
	}
	public String getDoctorSpeciality() {
		return doctorSpeciality;
	}
	public void setDoctorSpeciality(String doctorSpeciality) {
		this.doctorSpeciality = doctorSpeciality;
	}
	public String getPrescriptionIssueDate() {
		return prescriptionIssueDate;
	}
	public void setPrescriptionIssueDate(String prescriptionIssueDate) {
		this.prescriptionIssueDate = prescriptionIssueDate;
	}
	public String getDoctorAMKA() {
		return doctorAMKA;
	}
	public void setDoctorAMKA(String doctorAMKA) {
		this.doctorAMKA = doctorAMKA;
	}
	public String getDoctorETAA() {
		return doctorETAA;
	}
	public void setDoctorETAA(String doctorETAA) {
		this.doctorETAA = doctorETAA;
	}
	public String getDoctorFamilyName() {
		return doctorFamilyName;
	}
	public void setDoctorFamilyName(String doctorFamilyName) {
		this.doctorFamilyName = doctorFamilyName;
	}
	public String getDoctorPrefix() {
		return doctorPrefix;
	}
	public void setDoctorPrefix(String doctorPrefix) {
		this.doctorPrefix = doctorPrefix;
	}
	public String getDoctorGivenName() {
		return doctorGivenName;
	}
	public void setDoctorGivenName(String doctorGivenName) {
		this.doctorGivenName = doctorGivenName;
	}
	public String getDoctorSex() {
		return doctorSex;
	}
	public void setDoctorSex(String doctorSex) {
		this.doctorSex = doctorSex;
	}
	public String getDoctorBirthDate() {
		return doctorBirthDate;
	}
	public void setDoctorBirthDate(String doctorBirthDate) {
		this.doctorBirthDate = doctorBirthDate;
	}
	public String getDoctorTelephone() {
		return doctorTelephone;
	}
	public void setDoctorTelephone(String doctorTelephone) {
		this.doctorTelephone = doctorTelephone;
	}
	public String getDoctorEmail() {
		return doctorEmail;
	}
	public void setDoctorEmail(String doctorEmail) {
		this.doctorEmail = doctorEmail;
	}
	public String getDoctorFax() {
		return doctorFax;
	}
	public void setDoctorFax(String doctorFax) {
		this.doctorFax = doctorFax;
	}
	public String getPatientEidosEmesaAsfalismenou() {
		return patientEidosEmesaAsfalismenou;
	}
	public void setPatientEidosEmesaAsfalismenou(
			String patientEidosEmesaAsfalismenou) {
		this.patientEidosEmesaAsfalismenou = patientEidosEmesaAsfalismenou;
	}
	public String getPatientLanguageCommunication() {
		return patientLanguageCommunication;
	}
	public void setPatientLanguageCommunication(String patientLanguageCommunication) {
		this.patientLanguageCommunication = patientLanguageCommunication;
	}
	public String getDoctorAddress() {
		return doctorAddress;
	}
	public void setDoctorAddress(String doctorAddress) {
		this.doctorAddress = doctorAddress;
	}
	public String getDoctorCity() {
		return doctorCity;
	}
	public void setDoctorCity(String doctorCity) {
		this.doctorCity = doctorCity;
	}
	public String getDoctorPostalCode() {
		return doctorPostalCode;
	}
	public void setDoctorPostalCode(String doctorPostalCode) {
		this.doctorPostalCode = doctorPostalCode;
	}
	public String getDoctorCountry() {
		return doctorCountry;
	}
	public void setDoctorCountry(String doctorCountry) {
		this.doctorCountry = doctorCountry;
	}
	public String getParticipantType() {
		return participantType;
	}
	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}
	public String getParticipantAddress() {
		return participantAddress;
	}
	public void setParticipantAddress(String participantAddress) {
		this.participantAddress = participantAddress;
	}
	public String getParticipantCity() {
		return participantCity;
	}
	public void setParticipantCity(String participantCity) {
		this.participantCity = participantCity;
	}
	public String getParticipantPostalCode() {
		return participantPostalCode;
	}
	public void setParticipantPostalCode(String participantPostalCode) {
		this.participantPostalCode = participantPostalCode;
	}
	public String getParticipantCountry() {
		return participantCountry;
	}
	public void setParticipantCountry(String participantCountry) {
		this.participantCountry = participantCountry;
	}
	public String getParticipantFamilyName() {
		return participantFamilyName;
	}
	public void setParticipantFamilyName(String participantFamilyName) {
		this.participantFamilyName = participantFamilyName;
	}
	public String getParticipantPrefix() {
		return participantPrefix;
	}
	public void setParticipantPrefix(String participantPrefix) {
		this.participantPrefix = participantPrefix;
	}
	public String getParticipantGivenName() {
		return participantGivenName;
	}
	public void setParticipantGivenName(String participantGivenName) {
		this.participantGivenName = participantGivenName;
	}
	public String getParticipantSex() {
		return participantSex;
	}
	public void setParticipantSex(String participantSex) {
		this.participantSex = participantSex;
	}
	public String getParticipantBirthDate() {
		return participantBirthDate;
	}
	public void setParticipantBirthDate(String participantBirthDate) {
		this.participantBirthDate = participantBirthDate;
	}
	public String getParticipantTelephone() {
		return participantTelephone;
	}
	public void setParticipantTelephone(String participantTelephone) {
		this.participantTelephone = participantTelephone;
	}
	public String getParticipantEmail() {
		return participantEmail;
	}
	public void setParticipantEmail(String participantEmail) {
		this.participantEmail = participantEmail;
	}
	public String getParticipantFax() {
		return participantFax;
	}
	public void setParticipantFax(String participantFax) {
		this.participantFax = participantFax;
	}
	public boolean isHasParticipant() {
		return hasParticipant;
	}
	public void setHasParticipant(boolean hasParticipant) {
		this.hasParticipant = hasParticipant;
	}
	public String getPrescriptionIDRecurrent() {
		return prescriptionIDRecurrent;
	}
	public void setPrescriptionIDRecurrent(String prescriptionIDRecurrent) {
		this.prescriptionIDRecurrent = prescriptionIDRecurrent;
	}
	public String getPrescriptionBarcodeRecurrent() {
		return prescriptionBarcodeRecurrent;
	}
	public void setPrescriptionBarcodeRecurrent(String prescriptionBarcodeRecurrent) {
		this.prescriptionBarcodeRecurrent = prescriptionBarcodeRecurrent;
	}
	public String getPrescriptionType() {
		return prescriptionType;
	}
	public void setPrescriptionType(String prescriptionType) {
		this.prescriptionType = prescriptionType;
	}
	public String getPrescriptionBarcode() {
		return prescriptionBarcode;
	}
	public void setPrescriptionBarcode(String prescriptionBarcode) {
		this.prescriptionBarcode = prescriptionBarcode;
	}
	public String getPrescriptionTitle() {
		return prescriptionTitle;
	}
	public void setPrescriptionTitle(String prescriptionTitle) {
		this.prescriptionTitle = prescriptionTitle;
	}
	public boolean isHasGuardian() {
		return hasGuardian;
	}
	public void setHasGuardian(boolean hasGuardian) {
		this.hasGuardian = hasGuardian;
	}

	public String getPrescriptionRecurrent() {
		return prescriptionRecurrent;
	}
	public void setPrescriptionRecurrent(String prescriptionRecurrent) {
		this.prescriptionRecurrent = prescriptionRecurrent;
	}
	public String getPharmacistAMKA() {
		return pharmacistAMKA;
	}
	public void setPharmacistAMKA(String pharmacistAMKA) {
		this.pharmacistAMKA = pharmacistAMKA;
	}
	public String getPharmacistAddress() {
		return pharmacistAddress;
	}
	public void setPharmacistAddress(String pharmacistAddress) {
		this.pharmacistAddress = pharmacistAddress;
	}
	public String getPharmacistCity() {
		return pharmacistCity;
	}
	public void setPharmacistCity(String pharmacistCity) {
		this.pharmacistCity = pharmacistCity;
	}
	public String getPharmacistPostalCode() {
		return pharmacistPostalCode;
	}
	public void setPharmacistPostalCode(String pharmacistPostalCode) {
		this.pharmacistPostalCode = pharmacistPostalCode;
	}
	public String getPharmacistCountry() {
		return pharmacistCountry;
	}
	public void setPharmacistCountry(String pharmacistCountry) {
		this.pharmacistCountry = pharmacistCountry;
	}
	public String getPharmacistFamilyName() {
		return pharmacistFamilyName;
	}
	public void setPharmacistFamilyName(String pharmacistFamilyName) {
		this.pharmacistFamilyName = pharmacistFamilyName;
	}
	public String getPharmacistPrefix() {
		return pharmacistPrefix;
	}
	public void setPharmacistPrefix(String pharmacistPrefix) {
		this.pharmacistPrefix = pharmacistPrefix;
	}
	public String getPharmacistGivenName() {
		return pharmacistGivenName;
	}
	public void setPharmacistGivenName(String pharmacistGivenName) {
		this.pharmacistGivenName = pharmacistGivenName;
	}
	public String getPharmacistSex() {
		return pharmacistSex;
	}
	public void setPharmacistSex(String pharmacistSex) {
		this.pharmacistSex = pharmacistSex;
	}
	public String getPharmacistBirthDate() {
		return pharmacistBirthDate;
	}
	public void setPharmacistBirthDate(String pharmacistBirthDate) {
		this.pharmacistBirthDate = pharmacistBirthDate;
	}
	public String getPharmacistTelephone() {
		return pharmacistTelephone;
	}
	public void setPharmacistTelephone(String pharmacistTelephone) {
		this.pharmacistTelephone = pharmacistTelephone;
	}
	public String getPharmacistEmail() {
		return pharmacistEmail;
	}
	public void setPharmacistEmail(String pharmacistEmail) {
		this.pharmacistEmail = pharmacistEmail;
	}
	public String getPharmacistFax() {
		return pharmacistFax;
	}
	public void setPharmacistFax(String pharmacistFax) {
		this.pharmacistFax = pharmacistFax;
	}
	public String getDispensationId() {
		return dispensationId;
	}
	public void setDispensationId(String dispensationId) {
		this.dispensationId = dispensationId;
	}
	public String getPharmacistOrgName() {
		return pharmacistOrgName;
	}
	public void setPharmacistOrgName(String pharmacistOrgName) {
		this.pharmacistOrgName = pharmacistOrgName;
	}
	public List getEPDetail() {
		return EPDetail;
	}
	public void setEPDetail(List ePDetail) {
		EPDetail = ePDetail;
	}
	public List getEDDetail() {
		return EDDetail;
	}
	public void setEDDetail(List eDDetail) {
		EDDetail = eDDetail;
	}
	public String getPharmacistOrgAddress() {
		return pharmacistOrgAddress;
	}
	public void setPharmacistOrgAddress(String pharmacistOrgAddress) {
		this.pharmacistOrgAddress = pharmacistOrgAddress;
	}
	public String getPharmacistOrgCity() {
		return pharmacistOrgCity;
	}
	public void setPharmacistOrgCity(String pharmacistOrgCity) {
		this.pharmacistOrgCity = pharmacistOrgCity;
	}
	public String getPharmacistOrgPostalCode() {
		return pharmacistOrgPostalCode;
	}
	public void setPharmacistOrgPostalCode(String pharmacistOrgPostalCode) {
		this.pharmacistOrgPostalCode = pharmacistOrgPostalCode;
	}
	public String getPharmacistOrgCountry() {
		return pharmacistOrgCountry;
	}
	public void setPharmacistOrgCountry(String pharmacistOrgCountry) {
		this.pharmacistOrgCountry = pharmacistOrgCountry;
	}
	public String getPharmacistOrgTelephone() {
		return pharmacistOrgTelephone;
	}
	public void setPharmacistOrgTelephone(String pharmacistOrgTelephone) {
		this.pharmacistOrgTelephone = pharmacistOrgTelephone;
	}
	public String getPharmacistOrgEmail() {
		return pharmacistOrgEmail;
	}
	public void setPharmacistOrgEmail(String pharmacistOrgEmail) {
		this.pharmacistOrgEmail = pharmacistOrgEmail;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getDoctorUnit() {
		return doctorUnit;
	}
	public void setDoctorUnit(String doctorUnit) {
		this.doctorUnit = doctorUnit;
	}
	public String getPrescriptionEndDate() {
		return prescriptionEndDate;
	}
	public void setPrescriptionEndDate(String prescriptionEndDate) {
		this.prescriptionEndDate = prescriptionEndDate;
	}
	public String getPrescriptionStartDate() {
		return prescriptionStartDate;
	}
	public void setPrescriptionStartDate(String prescriptionStartDate) {
		this.prescriptionStartDate = prescriptionStartDate;
	}
	public String getPharmacistOrgId() {
		return pharmacistOrgId;
	}
	public void setPharmacistOrgId(String pharmacistOrgId) {
		this.pharmacistOrgId = pharmacistOrgId;
	}
	public String getDoctorOrgId() {
		return doctorOrgId;
	}
	public void setDoctorOrgId(String doctorOrgId) {
		this.doctorOrgId = doctorOrgId;
	}
	public String getDoctorOrgName() {
		return doctorOrgName;
	}
	public void setDoctorOrgName(String doctorOrgName) {
		this.doctorOrgName = doctorOrgName;
	}
	public String getDoctorOrgAddress() {
		return doctorOrgAddress;
	}
	public void setDoctorOrgAddress(String doctorOrgAddress) {
		this.doctorOrgAddress = doctorOrgAddress;
	}
	public String getDoctorOrgCity() {
		return doctorOrgCity;
	}
	public void setDoctorOrgCity(String doctorOrgCity) {
		this.doctorOrgCity = doctorOrgCity;
	}
	public String getDoctorOrgPostalCode() {
		return doctorOrgPostalCode;
	}
	public void setDoctorOrgPostalCode(String doctorOrgPostalCode) {
		this.doctorOrgPostalCode = doctorOrgPostalCode;
	}
	public String getDoctorOrgCountry() {
		return doctorOrgCountry;
	}
	public void setDoctorOrgCountry(String doctorOrgCountry) {
		this.doctorOrgCountry = doctorOrgCountry;
	}
	public String getDoctorOrgTelephone() {
		return doctorOrgTelephone;
	}
	public void setDoctorOrgTelephone(String doctorOrgTelephone) {
		this.doctorOrgTelephone = doctorOrgTelephone;
	}
	public String getDoctorOrgEmail() {
		return doctorOrgEmail;
	}
	public void setDoctorOrgEmail(String doctorOrgEmail) {
		this.doctorOrgEmail = doctorOrgEmail;
	}
	public String getConsentCode() {
		return consentCode;
	}
	public void setConsentCode(String consentCode) {
		this.consentCode = consentCode;
	}
	public String getConsentDisplayName() {
		return consentDisplayName;
	}
	public void setConsentDisplayName(String consentDisplayName) {
		this.consentDisplayName = consentDisplayName;
	}
	public String getConsentStartDate() {
		return consentStartDate;
	}
	public void setConsentStartDate(String consentStartDate) {
		this.consentStartDate = consentStartDate;
	}
	public String getConsentEndDate() {
		return consentEndDate;
	}
	public void setConsentEndDate(String consentEndDate) {
		this.consentEndDate = consentEndDate;
	}
	public String getPharmacistOid() {
		return pharmacistOid;
	}
	public void setPharmacistOid(String pharmacistOid) {
		this.pharmacistOid = pharmacistOid;
	}
	public String getDoctorOid() {
		return doctorOid;
	}
	public void setDoctorOid(String doctorOid) {
		this.doctorOid = doctorOid;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	
}
