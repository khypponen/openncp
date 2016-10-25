/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/
package se.sb.epsos.web.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.util.Base64;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import epsos.ccd.gnomon.configmanager.OLDConfigurationManagerDb;
import epsosOrgEpMedication.COCTMT230100UVPackagedMedicine;
import hl7OrgV3.CD;
import hl7OrgV3.ClinicalDocumentDocument1;
import hl7OrgV3.POCDMT000040Act;
import hl7OrgV3.POCDMT000040Author;
import hl7OrgV3.POCDMT000040EntryRelationship;
import hl7OrgV3.POCDMT000040Material;
import hl7OrgV3.POCDMT000040Organization;
import hl7OrgV3.POCDMT000040Person;
import hl7OrgV3.POCDMT000040Section;
import hl7OrgV3.POCDMT000040SubstanceAdministration;
import hl7OrgV3.StrucDocTr;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.Dispensation;
import se.sb.epsos.web.model.DispensationRow;
import se.sb.epsos.web.util.DateUtil;
import se.sb.epsos.web.util.EpsosWebConstants;
import se.sb.epsos.web.util.FileHelper;

public class ePtoeDMapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ePtoeDMapper.class);
	private static final String NULLFLAVOR_UNK = "UNK";
	private static final String NULLFLAVOR_NI = "NI";
	private static final String ED_XML_TEMPLATE_PATH_DB_KEY = "EDISPENSATION_CDA_XML_TEMPLATE_PATH";
	private static final String ED_PDF_TEMPLATE_PATH_DB_KEY = "EDISPENSATION_CDA_PDF_TEMPLATE_PATH";

	private static final String DEFAULT_ED_TEMPLATE_PATH = "xml";
	private static final String DEFAULT_ED_XML_TEMPLATE_NAME = "eD_Template.xml";
	private static final String DEFAULT_ED_PDF_TEMPLATE_NAME = "eD_PDF_Template.xml";
	private static String ED_XML_TEMPLATE_PATH = DEFAULT_ED_TEMPLATE_PATH + File.separatorChar
			+ DEFAULT_ED_XML_TEMPLATE_NAME;
	private static String ED_PDF_TEMPLATE_PATH = DEFAULT_ED_TEMPLATE_PATH + File.separatorChar
			+ DEFAULT_ED_PDF_TEMPLATE_NAME;

	private XmlCursor cursor = null;

	public ePtoeDMapper() {
		String xmlTemp = getConfigurationProperty(ED_XML_TEMPLATE_PATH_DB_KEY);
		if (FileHelper.fileExists(xmlTemp)) {
			ED_XML_TEMPLATE_PATH = xmlTemp;
		} else if (FileHelper.fileExists(xmlTemp + File.separator + DEFAULT_ED_XML_TEMPLATE_NAME)) {
			ED_XML_TEMPLATE_PATH = xmlTemp + File.separator + DEFAULT_ED_XML_TEMPLATE_NAME;
		}
		LOGGER.info("Using file (" + ED_XML_TEMPLATE_PATH + ") for cda xml template.");

		String pdfTemp = getConfigurationProperty(ED_PDF_TEMPLATE_PATH_DB_KEY);
		if (FileHelper.fileExists(pdfTemp)) {
			ED_PDF_TEMPLATE_PATH = pdfTemp;
		} else if (FileHelper.fileExists(pdfTemp + File.separator + DEFAULT_ED_PDF_TEMPLATE_NAME)) {
			ED_PDF_TEMPLATE_PATH = pdfTemp + File.separator + DEFAULT_ED_PDF_TEMPLATE_NAME;
		}
		LOGGER.info("Using file (" + ED_PDF_TEMPLATE_PATH + ") for cda pdf template.");
	}

	protected String getConfigurationProperty(String key) {
		try {
			return OLDConfigurationManagerDb.getInstance().getProperty(key);
		} catch (Exception e) { // Fix for not been able to run ui with jetty;
								// without configuration db.
			LOGGER.warn("ConfigurationManagerDb.getProperty did throw.", e);
			return null;
		}
	}

	private ClinicalDocumentDocument1 buildCdafromTemplate(InputStream is) throws XmlException, IOException {
		ClinicalDocumentDocument1 docCDA = ClinicalDocumentDocument1.Factory.parse(is);
		return (docCDA == null ? null : docCDA);
	}

	public ClinicalDocumentDocument1 createDispensation_PDF(byte[] bytesPDF, Dispensation dispensation,
			AuthenticatedUser user, String cdaIdExtension, String pdfIdExtension) throws XmlException, IOException {
		Date date = new Date();
		ClinicalDocumentDocument1 eD_PDF_Document = buildCdafromTemplate(
				FileHelper.getResourceFromClassPathOrFileSystem(ED_PDF_TEMPLATE_PATH));
		InputStream is = new ByteArrayInputStream(dispensation.getPrescription().getBytes());
		ClinicalDocumentDocument1 eP_Document = buildCdafromTemplate(is);

		// Document id
		eD_PDF_Document.getClinicalDocument().getId().setExtension(pdfIdExtension);
		// Link to the XML version
		eD_PDF_Document.getClinicalDocument().getRelatedDocumentArray(0).getParentDocument().getIdArray(0)
				.setExtension(cdaIdExtension);
		// One more link, hope to remove it later
		eD_PDF_Document.getClinicalDocument().getRelatedDocumentArray(1).getParentDocument().getIdArray(0)
				.setExtension(cdaIdExtension);

		// effectiveTime of the document
		eD_PDF_Document.getClinicalDocument().getEffectiveTime()
				.setValue(DateUtil.formatDate(date, EpsosWebConstants.DATEFORMATSEC));
		// Patient information
		eD_PDF_Document.getClinicalDocument().getRecordTargetArray(0)
				.setPatientRole(eP_Document.getClinicalDocument().getRecordTargetArray(0).getPatientRole());

		// Author time
		eD_PDF_Document.getClinicalDocument().getAuthorArray(0).getTime()
				.setValue(DateUtil.formatDate(date, EpsosWebConstants.DATEFORMATSEC));
		// Pharmacist's id
		eD_PDF_Document.getClinicalDocument().getAuthorArray(0).getAssignedAuthor().getIdArray(0)
				.setExtension(user.getUserId());

		// Other pharmacist data
		POCDMT000040Person person = eD_PDF_Document.getClinicalDocument().getAuthorArray(0).getAssignedAuthor()
				.getAssignedPerson();
		createAssignedPerson(user, person);

		// Pharmacy info
		POCDMT000040Organization representedOrganization = eD_PDF_Document.getClinicalDocument().getAuthorArray(0)
				.getAssignedAuthor().getRepresentedOrganization();
		mapPhoneNr(representedOrganization.getTelecomArray(0), user.getTelecom());
		createRepresentedOrganization(user, representedOrganization);

		eD_PDF_Document.getClinicalDocument().getInFulfillmentOfArray(0).getOrder()
				.setIdArray(eP_Document.getClinicalDocument().getComponent().getStructuredBody().getComponentArray(0)
						.getSection().getEntryArray(0).getSubstanceAdministration().getIdArray());

		String base64 = new String(Base64.encode(bytesPDF));
		cursor = eD_PDF_Document.getClinicalDocument().getComponent().getNonXMLBody().getText().newCursor();
		cursor.toFirstContentToken();
		cursor.insertChars(base64);

		return eD_PDF_Document;
	}

	public byte[] createDispensationFromPrescription(Dispensation dispensation, AuthenticatedUser user,
			String cdaIdExtension, String pdfIdExtension) throws Exception {
		if (user == null || dispensation == null) {
			throw new Exception("user or dispensation = null");
		}

		Date date = new Date();
		InputStream is = new ByteArrayInputStream(dispensation.getPrescription().getBytes());
		ClinicalDocumentDocument1 eP_Document = buildCdafromTemplate(is);

		ClinicalDocumentDocument1 eD_Document = buildCdafromTemplate(
				FileHelper.getResourceFromClassPathOrFileSystem(ED_XML_TEMPLATE_PATH));
		// Document id
		eD_Document.getClinicalDocument().getId().setExtension(cdaIdExtension);
		// Link to the PDF version
		eD_Document.getClinicalDocument().getRelatedDocumentArray(0).getParentDocument().getIdArray(0)
				.setExtension(pdfIdExtension);
		// One more link, hope to remove it later
		eD_Document.getClinicalDocument().getRelatedDocumentArray(1).getParentDocument().getIdArray(0)
				.setExtension(pdfIdExtension);
		// Structured body section
		POCDMT000040Section eD_Document_section = eD_Document.getClinicalDocument().getComponent().getStructuredBody()
				.getComponentArray(0).getSection();

		// Section id, the same as doc id
		eD_Document_section.getId().setExtension(cdaIdExtension);
		// Supply id, the same as doc id
		eD_Document_section.getEntryArray(0).getSupply().getIdArray(0).setExtension(cdaIdExtension);

		// effectiveTime of the document
		eD_Document.getClinicalDocument().getEffectiveTime()
				.setValue(DateUtil.formatDate(date, EpsosWebConstants.DATEFORMATSEC));
		// Patient information
		eD_Document.getClinicalDocument().getRecordTargetArray(0)
				.setPatientRole(eP_Document.getClinicalDocument().getRecordTargetArray(0).getPatientRole());

		// eD_Document.getClinicalDocument().getAuthorArray(0).getFunctionCode().setDisplayName(AssertionHandlerConfigManager.getRoleDisplayName(user.getRoles().get(0)));
		// Author time
		eD_Document.getClinicalDocument().getAuthorArray(0).getTime()
				.setValue(DateUtil.formatDate(date, EpsosWebConstants.DATEFORMATSEC));
		// Pharmacist's id
		eD_Document.getClinicalDocument().getAuthorArray(0).getAssignedAuthor().getIdArray(0)
				.setExtension(user.getUserId());

		// Other pharmacist data
		POCDMT000040Person person = eD_Document.getClinicalDocument().getAuthorArray(0).getAssignedAuthor()
				.getAssignedPerson();
		createAssignedPerson(user, person);

		// Pharmacy info
		POCDMT000040Organization representedOrganization = eD_Document.getClinicalDocument().getAuthorArray(0)
				.getAssignedAuthor().getRepresentedOrganization();
		mapPhoneNr(representedOrganization.getTelecomArray(0), user.getTelecom());
		createRepresentedOrganization(user, representedOrganization);

		// Performer time, same as author time
		eD_Document_section.getEntryArray(0).getSupply().getPerformerArray(0).getTime()
				.setValue(DateUtil.formatDate(date, EpsosWebConstants.DATEFORMATSEC));
		// Performer's organization id
		eD_Document_section.getEntryArray(0).getSupply().getPerformerArray(0).getAssignedEntity().getIdArray(0)
				.setExtension(user.getUserId());

		// Other performer data
		person = eD_Document_section.getEntryArray(0).getSupply().getPerformerArray(0).getAssignedEntity()
				.getAssignedPerson();
		createAssignedPerson(user, person);

		// Pharmacy info
		representedOrganization = eD_Document_section.getEntryArray(0).getSupply().getPerformerArray(0)
				.getAssignedEntity().getRepresentedOrganization();
		mapPhoneNr(representedOrganization.getTelecomArray(0), user.getTelecom());
		createRepresentedOrganization(user, representedOrganization);

		// This causes problems with Croatian dispensations and should not be
		// needed anyway, as eP is not a parent document for eD
		// if
		// (eP_Document.getClinicalDocument().getRelatedDocumentArray(0).getParentDocument().getIdArray(0).getExtension()
		// != null) {
		// eD_Document.getClinicalDocument().getRelatedDocumentArray(0).getParentDocument().getIdArray(0)
		// .setExtension(eP_Document.getClinicalDocument().getRelatedDocumentArray(0).getParentDocument().getIdArray(0).getExtension());
		// }

		// Copy substance administration from eP
		copySubstanceAdministrationFromEP(eP_Document, eD_Document_section);

		// Manufactured material
		POCDMT000040Material eD_Document_material = eD_Document_section.getEntryArray(0).getSupply().getProduct()
				.getManufacturedProduct().getManufacturedMaterial();

		ArrayList<String> refTargetsToPreserve = new ArrayList<String>();

		for (DispensationRow dispRow : dispensation.getRows()) {
			eD_Document_section.getText().getTableArray(0).getTbodyArray(0).addNewTr();
			for (int i = 0; i <= 9; i++) {
				eD_Document_section.getText().getTableArray(0).getTbodyArray(0).getTrArray(0).addNewTd();
			}
			// Narrative text of the eD
			StrucDocTr eD_Document_Tr = eD_Document_section.getText().getTableArray(0).getTbodyArray(0).getTrArray(0);
			eD_Document_Tr.getTdArray(0).newCursor().setTextValue(dispRow.getProductId());
			eD_Document_Tr.getTdArray(1).newCursor().setTextValue(dispRow.getProductName());
			eD_Document_Tr.getTdArray(1).setID("dispensedProduct");
			eD_Document_Tr.getTdArray(2).newCursor()
					.setTextValue(dispRow.getPrescriptionRow().getIngredient().get(0).getStrength());
			eD_Document_Tr.getTdArray(3).newCursor().setTextValue(dispRow.getPrescriptionRow().getFormCode());
			eD_Document_Tr.getTdArray(4).newCursor().setTextValue(dispRow.getPrescriptionRow().getTypeOfPackage());
			eD_Document_Tr.getTdArray(5).newCursor().setTextValue(
					dispRow.getPackageSize().getQuantityValue() + " " + dispRow.getPackageSize().getQuantityUnit());
			eD_Document_Tr.getTdArray(6).newCursor().setTextValue(
					dispRow.getNbrPackages().getQuantityValue() + " " + dispRow.getNbrPackages().getQuantityUnit());
			eD_Document_Tr.getTdArray(7).newCursor()
					.setTextValue(dispRow.getPrescriptionRow().getPatientInstructions());
			// replace 3 reference targets to match those in the eP
			Document doc = transformCDADocumenttoDomDocument(eP_Document);
			String ref = fetchReference("//act[code/@code='PINSTRUCT']/text/reference", doc, refTargetsToPreserve);
			if (ref != null && ref.length() > 0) {
				eD_Document_Tr.getTdArray(7).setID(ref);
			} else {
				eD_Document_Tr.getTdArray(7).setID("PINSTRUCT");
			}
			eD_Document_Tr.getTdArray(8).newCursor()
					.setTextValue(dispRow.getPrescriptionRow().getPharmacistInstructions());
			ref = fetchReference("//act[code/@code='FINSTRUCT']/text/reference", doc, refTargetsToPreserve);
			if (ref != null && ref.length() > 0) {
				eD_Document_Tr.getTdArray(8).setID(ref);
			} else {
				eD_Document_Tr.getTdArray(8).setID("FINSTRUCT");
			}
			eD_Document_Tr.getTdArray(9).newCursor().setTextValue(dispRow.getPrescriptionRow().getProductName());
			ref = fetchReference("//manufacturedMaterial/code/originalText/reference", doc, refTargetsToPreserve);
			if (ref != null && ref.length() > 0) {
				eD_Document_Tr.getTdArray(9).setID(ref);
			} else {
				eD_Document_Tr.getTdArray(9).setID("nameAsText");
			}

			// Number or packages
			eD_Document_section.getEntryArray(0).getSupply().getQuantity()
					.setValue(dispRow.getNbrPackages().getQuantityValue());

			// Dispensed Medicine Id - difficult to interpret what this means,
			// assume it's again the id of the dispensation
			// as the medicine code comes next anyway
			eD_Document_material.getId().setExtension(cdaIdExtension);

			// VNR number
			eD_Document_material.getCode().setCode(dispRow.getProductId());
			eD_Document_material.getCode().setDisplayName(dispRow.getProductName());
			eD_Document_material.getName().newCursor().setTextValue(dispRow.getProductName());

			// Form code
			eD_Document_material.getFormCode().setCode(dispRow.getPrescriptionRow().getFormCode());
			eD_Document_material.getFormCode().setDisplayName(dispRow.getPrescriptionRow().getFormName());

			// Package size
			eD_Document_material.getAsContent().getContainerPackagedMedicine().getCapacityQuantity()
					.setValue(dispRow.getPackageSize().getQuantityValue());
			eD_Document_material.getAsContent().getContainerPackagedMedicine().getCapacityQuantity()
					.setUnit(dispRow.getPackageSize().getQuantityUnitUcum());

			// Link to the prescription ID (inFulfillmentOf)
			eD_Document.getClinicalDocument().getInFulfillmentOfArray(0).getOrder().getIdArray(0)
					.setExtension(dispRow.getPrescriptionRow().getPrescriptionId());
			eD_Document.getClinicalDocument().getInFulfillmentOfArray(0).getOrder().getIdArray(0)
					.setRoot(dispRow.getPrescriptionRow().getPrescriptionIdRoot());

			// Handle substitution; Remove substitution part from template if
			// NOT substitute
			LOGGER.debug("Row isSubstitute: " + dispRow.isSubstitute());
			if (!dispRow.isSubstitute()) {
				int index = -1;
				for (int i = 0; i < eD_Document_section.getEntryArray(0).getSupply()
						.getEntryRelationshipArray().length; i++) {
					POCDMT000040EntryRelationship relation = eD_Document_section.getEntryArray(0).getSupply()
							.getEntryRelationshipArray()[i];

					POCDMT000040Act act = relation.getAct();
					if (act != null) {
						CD cd = act.getCode();
						if ("SUBST".equals(cd.getCode()) && "2.16.840.1.113883.5.6".equals(cd.getCodeSystem())
								&& "ActClass".equals(cd.getCodeSystemName())
								&& "Substitution".equals(cd.getDisplayName())) {
							index = i;
							break;
						}
					}
				}

				if (index != -1) {
					eD_Document_section.getEntryArray(0).getSupply().removeEntryRelationship(index);
					LOGGER.debug("Substitution block removed from the template");
				}
			}
		}

		// Package description
		COCTMT230100UVPackagedMedicine packedMedicine = eP_Document.getClinicalDocument().getComponent()
				.getStructuredBody().getComponentArray(0).getSection().getEntryArray(0).getSubstanceAdministration()
				.getConsumable().getManufacturedProduct().getManufacturedMaterial().getAsContent()
				.getContainerPackagedMedicine();

		// Name of the dispensed product once again
		if (packedMedicine.getNameArray() != null && packedMedicine.getNameArray().length != 0) {
			String text = packedMedicine.getNameArray(0).newCursor().getTextValue();
			eD_Document_material.getAsContent().getContainerPackagedMedicine().getNameArray(0).newCursor()
					.setTextValue(text);
		}

		// Package type
		if (packedMedicine.getFormCode() != null && packedMedicine.getFormCode().getCode() != null
				&& packedMedicine.getFormCode().getDisplayName() != null
				&& packedMedicine.getFormCode().getCode().length() != 0) {
			eD_Document_material.getAsContent().getContainerPackagedMedicine().getFormCode()
					.setCodeSystem("1.3.6.1.4.1.12559.11.10.1.3.1.44.1");
			eD_Document_material.getAsContent().getContainerPackagedMedicine().getFormCode().setCodeSystemName("EDQM");
			eD_Document_material.getAsContent().getContainerPackagedMedicine().getFormCode()
					.setCode(packedMedicine.getFormCode().getCode());
			eD_Document_material.getAsContent().getContainerPackagedMedicine().getFormCode()
					.setDisplayName(packedMedicine.getFormCode().getDisplayName());
		} else {
			eD_Document_material.getAsContent().getContainerPackagedMedicine().getFormCode()
					.setNullFlavor(NULLFLAVOR_NI);
		}

		// Overall strength
		eD_Document_material.getIngredientArray(0)
				.setQuantity(eP_Document.getClinicalDocument().getComponent().getStructuredBody().getComponentArray(0)
						.getSection().getEntryArray(0).getSubstanceAdministration().getConsumable()
						.getManufacturedProduct().getManufacturedMaterial().getIngredientArray(0).getQuantity());

		// Active ingredient code
		eD_Document_material.getIngredientArray(0).getIngredient().getCode()
				.setCode(eP_Document.getClinicalDocument().getComponent().getStructuredBody().getComponentArray(0)
						.getSection().getEntryArray(0).getSubstanceAdministration().getConsumable()
						.getManufacturedProduct().getManufacturedMaterial().getIngredientArray(0).getIngredient()
						.getCode().getCode());

		// Active ingredient display name
		eD_Document_material.getIngredientArray(0).getIngredient().getCode()
				.setDisplayName(eP_Document.getClinicalDocument().getComponent().getStructuredBody()
						.getComponentArray(0).getSection().getEntryArray(0).getSubstanceAdministration().getConsumable()
						.getManufacturedProduct().getManufacturedMaterial().getIngredientArray(0).getIngredient()
						.getCode().getDisplayName());

		// Active ingredient name
		eD_Document_material.getIngredientArray(0).getIngredient().getNameArray(0).newCursor()
				.setTextValue(eP_Document.getClinicalDocument().getComponent().getStructuredBody().getComponentArray(0)
						.getSection().getEntryArray(0).getSubstanceAdministration().getConsumable()
						.getManufacturedProduct().getManufacturedMaterial().getIngredientArray(0).getIngredient()
						.getNameArray(0).newCursor().getTextValue());

		// Update references

		return handleReferences(eP_Document, eD_Document, refTargetsToPreserve);
	}

	public byte[] handleReferences(ClinicalDocumentDocument1 eP_Document, ClinicalDocumentDocument1 eD_Document,
			ArrayList<String> refTargetsToPreserve) {
		Document doc = transformCDADocumenttoDomDocument(eD_Document);

		Pattern pattern = Pattern.compile("<[^<>]*ID=\"([^\"]*)\"[^<>]*>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(eP_Document.getClinicalDocument().getComponent().getStructuredBody()
				.getComponentArray(0).getSection().getText().toString());
		while (matcher.find()) {
			LOGGER.info("Reference(" + matcher.group(1) + ") found");
			if (refTargetsToPreserve.contains(matcher.group(1))) {
				LOGGER.info("Preserving reference(" + matcher.group(1) + ")");
				continue;
			}
			XPath xpath = XPathFactory.newInstance().newXPath();

			try {
				XPathExpression path = xpath.compile("//reference[@value='#" + matcher.group(1) + "']");
				NodeList nodeList = (NodeList) path.evaluate(doc, XPathConstants.NODESET);
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node != null && node.getNodeName().equals("reference")) {
						LOGGER.info("Reference(Node: " + node.getNodeName() + "('"
								+ node.getAttributes().getNamedItem("value").getTextContent() + "'), Parent node: "
								+ node.getParentNode().getNodeName() + ") removed");
						node.getParentNode().getParentNode().removeChild(node.getParentNode());
					}
				}
			} catch (XPathExpressionException e) {
				LOGGER.error("XPathExpressionException: ", e);
			}
		}
		return serializeDocToByteArray(doc);
	}

	private String fetchReference(String pattern, Document doc, ArrayList<String> refTargetsToPreserve) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		// 1. Patient instructions
		try {
			XPathExpression path = xpath.compile(pattern);
			NodeList nodeList = (NodeList) path.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node != null && node.getAttributes() != null
						&& node.getAttributes().getNamedItem("value") != null) {
					String ref = node.getAttributes().getNamedItem("value").getTextContent().substring(1);
					refTargetsToPreserve.add(ref);
					return ref;
				}
			}
		} catch (XPathExpressionException e) {
			LOGGER.error("XPathExpressionException: ", e);
		}
		return null;
	}

	private Document transformCDADocumenttoDomDocument(ClinicalDocumentDocument1 eD_Document) {
		String xml = eD_Document.xmlText(new XmlOptions().setCharacterEncoding("UTF-8").setSavePrettyPrint());
		byte[] eD_Document_bytes = null;
		try {
			eD_Document_bytes = xml.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("UnsupportedEncodingException: ", e);
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new ByteArrayInputStream(eD_Document_bytes));
		} catch (SAXException e) {
			LOGGER.error("SAXException: ", e);
		} catch (IOException e) {
			LOGGER.error("IOException: ", e);
		} catch (ParserConfigurationException e) {
			LOGGER.error("ParserConfigurationException: ", e);
		}
		return doc;
	}

	public byte[] serializeDocToByteArray(Document doc) {
		if (doc == null) {
			return null;
		}

		DOMSource source = new DOMSource(doc);
		StringWriter xmlAsWriter = new StringWriter();
		StreamResult result = new StreamResult(xmlAsWriter);

		try {
			TransformerFactory.newInstance().newTransformer().transform(source, result);
		} catch (TransformerConfigurationException e) {
			LOGGER.error("TransformerConfigurationException: ", e);
		} catch (TransformerException e) {
			LOGGER.error("TransformerException: ", e);
		} catch (TransformerFactoryConfigurationError e) {
			LOGGER.error("TransformerFactoryConfigurationError: ", e);
		}

		byte[] bytes = null;
		try {
			bytes = xmlAsWriter.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("UnsupportedEncodingException: ", e);
		}
		return bytes;
	}

	private void copySubstanceAdministrationFromEP(ClinicalDocumentDocument1 eP_Document,
			POCDMT000040Section eD_Document_section) {
		POCDMT000040SubstanceAdministration eP_Document_substance = eP_Document.getClinicalDocument().getComponent()
				.getStructuredBody().getComponentArray(0).getSection().getEntryArray(0).getSubstanceAdministration();
		eD_Document_section.getEntryArray(0).getSupply().getEntryRelationshipArray(0)
				.setSubstanceAdministration(eP_Document_substance);

		POCDMT000040SubstanceAdministration eD_Document_substance = eD_Document_section.getEntryArray(0).getSupply()
				.getEntryRelationshipArray(0).getSubstanceAdministration();
		if (eD_Document_substance.getAuthorArray() == null || eD_Document_substance.getAuthorArray().length == 0) {
			POCDMT000040Author[] array = new POCDMT000040Author[] {
					eP_Document.getClinicalDocument().getAuthorArray(0) };
			eD_Document_section.getEntryArray(0).getSupply().getEntryRelationshipArray(0).getSubstanceAdministration()
					.setAuthorArray(array);
		}
	}

	private void createAssignedPerson(AuthenticatedUser user, POCDMT000040Person person) {
		if (user.getFamilyName() != null) {
			person.getNameArray(0).getFamilyArray(0).newCursor().setTextValue(user.getFamilyName());
		} else {
			person.getNameArray(0).getFamilyArray(0).setNullFlavor(NULLFLAVOR_UNK);
		}

		if (user.getGivenName() != null) {
			person.getNameArray(0).getGivenArray(0).newCursor().setTextValue(user.getGivenName());
		} else {
			person.getNameArray(0).getGivenArray(0).setNullFlavor(NULLFLAVOR_UNK);
		}
	}

	private void createRepresentedOrganization(AuthenticatedUser user,
			POCDMT000040Organization representedOrganization) {
		representedOrganization.getIdArray(0).setExtension(user.getOrganizationId());
		representedOrganization.getNameArray(0).newCursor().setTextValue(user.getOrganizationName());

		if (user.getStreet() != null) {
			representedOrganization.getAddrArray(0).getStreetAddressLineArray(0).newCursor()
					.setTextValue(user.getStreet());
		} else {
			representedOrganization.getAddrArray(0).getStreetAddressLineArray(0).setNullFlavor(NULLFLAVOR_UNK);
		}

		if (user.getPostalCode() != null) {
			representedOrganization.getAddrArray(0).getPostalCodeArray(0).newCursor()
					.setTextValue(user.getPostalCode());
		} else {
			representedOrganization.getAddrArray(0).getPostalCodeArray(0).setNullFlavor(NULLFLAVOR_UNK);
		}

		if (user.getCity() != null) {
			representedOrganization.getAddrArray(0).getCityArray(0).newCursor().setTextValue(user.getCity());
		} else {
			representedOrganization.getAddrArray(0).getCityArray(0).setNullFlavor(NULLFLAVOR_UNK);
		}

		representedOrganization.getAddrArray(0).getCityArray(0).newCursor().setTextValue(user.getCity());
	}

	private void mapPhoneNr(hl7OrgV3.TEL telecomArray, String phoneNr) {
		if (phoneNr != null) {
			telecomArray.setValue(phoneNr);
		} else {
			telecomArray.unsetUse();
			telecomArray.unsetValue();
			telecomArray.setNullFlavor(NULLFLAVOR_NI);
		}
	}
}
