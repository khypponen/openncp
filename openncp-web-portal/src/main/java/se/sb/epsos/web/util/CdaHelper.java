/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
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

import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.sb.epsos.web.model.Ingredient;
import se.sb.epsos.web.model.Prescription;
import se.sb.epsos.web.model.PrescriptionRow;
import se.sb.epsos.web.model.QuantityVO;

public class CdaHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(CdaHelper.class);
	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(new Locale("sv"));
	private static final String CHAR_FORWARD_SLASH = "/";
	private static final String CHAR_SPACE = " ";
	private static final String CHAR_ONE = "1";

	public void parsePrescriptionFromDocument(Prescription prescription) {
		ArrayList<PrescriptionRow> rows = new ArrayList<PrescriptionRow>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(new ByteArrayInputStream(prescription.getBytes()));

			XPath xpath = XPathFactory.newInstance().newXPath();

			// for each prescription component, search for its entries and make up the list
			XPathExpression prescriptionIDExpr = xpath
					.compile("/ClinicalDocument/component/structuredBody/component/section[templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']");
			NodeList prescriptionIDNodes = (NodeList) prescriptionIDExpr.evaluate(dom, XPathConstants.NODESET);
			if (prescriptionIDNodes != null && prescriptionIDNodes.getLength() > 0) {
				XPathExpression idExpr = xpath.compile("id");

				for (int p = 0; p < prescriptionIDNodes.getLength(); p++) {
					String prescriptionID = "";
					String prescriptionIDRoot = "";
					Node sectionNode = prescriptionIDNodes.item(p);
					Node pIDNode = (Node) idExpr.evaluate(sectionNode, XPathConstants.NODE);
					if (pIDNode != null) {
                        if (pIDNode.getAttributes().getNamedItem("extension") != null) {
                            prescriptionID = pIDNode.getAttributes().getNamedItem("extension").getNodeValue();
                        } else {
                            prescriptionID = pIDNode.getAttributes().getNamedItem("root").getNodeValue();
                        }
                        prescriptionIDRoot = pIDNode.getAttributes().getNamedItem("root").getNodeValue();
					}

					XPathExpression prefixExpr = xpath.compile("author/assignedAuthor/assignedPerson/name/prefix");
					XPathExpression givenNameExpr = xpath.compile("author/assignedAuthor/assignedPerson/name/given");
					XPathExpression familyNameExpr = xpath.compile("author/assignedAuthor/assignedPerson/name/family");
					String prescriber = handleAssignedPerson(sectionNode, prefixExpr, givenNameExpr, familyNameExpr);

					XPathExpression performerPrefixExpr = xpath.compile("/ClinicalDocument/author/assignedAuthor/assignedPerson/name/prefix");
					XPathExpression performerGivenNameExpr = xpath.compile("/ClinicalDocument/author/assignedAuthor/assignedPerson/name/given");
					XPathExpression performerSurnameExpr = xpath.compile("/ClinicalDocument/author/assignedAuthor/assignedPerson/name/family");
					String performer = handleAssignedPerson(dom, performerPrefixExpr, performerGivenNameExpr, performerSurnameExpr);
					if (prescriber == null || prescriber.isEmpty()) {
						prescriber = performer;
					}

					// prescription header information
					prescription.setPerformer(performer);

					List<String> list = handlePrescriberTelecom(dom, xpath);
					if (list != null && list.size() != 0) {
						if (list.size() == 2) {
							prescription.setContact2(list.get(1));
						}
						prescription.setContact1(list.get(0));
					}

					prescription.setCreateDate(handlePrescriptionDate(dom, xpath));
					prescription.setProfession(handleProfession(dom, xpath));
					prescription.setFacility(handleFacility(dom, xpath));
					prescription.setAddress(handleAdress(dom, xpath));
					prescription.setCountry(handleCountryPrescriber(dom, xpath));

					// PRESCRIPTION ITEMS
					XPathExpression entryExpr = xpath.compile("entry");
					NodeList entryList = (NodeList) entryExpr.evaluate(sectionNode, XPathConstants.NODESET);
					if (entryList != null && entryList.getLength() > 0) {
						for (int i = 0; i < entryList.getLength(); i++) {
							PrescriptionRow row = new PrescriptionRow();

							Node entryNode = entryList.item(i);

							String materialID = "";

							XPathExpression productIdExpr = xpath
									.compile("substanceAdministration/consumable/manufacturedProduct/manufacturedMaterial/code");
							Node materialIDNode = (Node) productIdExpr.evaluate(entryNode, XPathConstants.NODE);
							if (materialIDNode != null) {
								materialID = materialIDNode.getAttributes().getNamedItem("code").getNodeValue();
							}

							XPathExpression nameExpr = xpath
									.compile("substanceAdministration/consumable/manufacturedProduct/manufacturedMaterial/name");
							Node materialName = (Node) nameExpr.evaluate(entryNode, XPathConstants.NODE);
							String name = materialName.getTextContent().trim();

							String formCode = null;
							String packsString = "";
							XPathExpression doseFormExpr = xpath
									.compile("substanceAdministration/consumable/manufacturedProduct/manufacturedMaterial/formCode");
							Node doseForm = (Node) doseFormExpr.evaluate(entryNode, XPathConstants.NODE);
							if (doseForm != null) {
								packsString = doseForm.getAttributes().getNamedItem("displayName").getNodeValue();
							}

							formCode = doseForm.getAttributes().getNamedItem("code").getNodeValue();
							XPathExpression packQuantityExpr = xpath
									.compile("substanceAdministration/consumable/manufacturedProduct/manufacturedMaterial/asContent/quantity/numerator[@type='epsos:PQ']");
							Node packQuant = (Node) packQuantityExpr.evaluate(entryNode, XPathConstants.NODE);

							XPathExpression packQuantityExpr2 = xpath
									.compile("substanceAdministration/consumable/manufacturedProduct/manufacturedMaterial/asContent/quantity/denominator[@type='epsos:PQ']");
							Node packQuant2 = (Node) packQuantityExpr2.evaluate(entryNode, XPathConstants.NODE);
							if (packQuant != null && packQuant2 != null) {
								String unit = packQuant.getAttributes().getNamedItem("unit").getNodeValue();
								if (unit != null && !unit.equals(CHAR_ONE)) {
									packsString += CHAR_SPACE + unit;
								}

								String denom = packQuant2.getAttributes().getNamedItem("value").getNodeValue();
								if (denom != null && !denom.equals(CHAR_ONE)) {
									packsString += CHAR_FORWARD_SLASH + denom;
									unit = packQuant2.getAttributes().getNamedItem("unit").getNodeValue();
									if (unit != null && !unit.equals(CHAR_ONE)) {
										packsString += CHAR_SPACE + unit;
									}
								}
							}

							row.setIngredient(handleIngredients(xpath, entryNode));

							String doseString = "";
							XPathExpression doseExpr = xpath.compile("substanceAdministration/doseQuantity");
							Node dose = (Node) doseExpr.evaluate(entryNode, XPathConstants.NODE);
							if (dose != null) {
								if (dose.getAttributes().getNamedItem("value") != null) {
									doseString = dose.getAttributes().getNamedItem("value").getNodeValue();
									if (dose.getAttributes().getNamedItem("unit") != null) {
										String unit = dose.getAttributes().getNamedItem("unit").getNodeValue();
										if (unit != null && !unit.equals(CHAR_ONE)) {
											doseString += CHAR_SPACE + unit;
										}
									}
								} else {
									String lowString = "";
									String highString = "";
									XPathExpression doseExprLow = xpath.compile("low");
									Node lowDoseNode = (Node) doseExprLow.evaluate(dose, XPathConstants.NODE);
									if (lowDoseNode != null && lowDoseNode.getAttributes().getNamedItem("value") != null) {
										lowString = lowDoseNode.getAttributes().getNamedItem("value").getNodeValue();
										if (lowDoseNode.getAttributes().getNamedItem("unit") != null) {
											String unit = lowDoseNode.getAttributes().getNamedItem("unit").getNodeValue();
											if (unit != null && !unit.equals(CHAR_ONE)) {
												lowString += CHAR_SPACE + unit;
											}
										}
									}
									XPathExpression doseExprHigh = xpath.compile("high");
									Node highDoseNode = (Node) doseExprHigh.evaluate(dose, XPathConstants.NODE);
									if (highDoseNode != null && highDoseNode.getAttributes().getNamedItem("value") != null) {
										highString = highDoseNode.getAttributes().getNamedItem("value").getNodeValue();
										if (highDoseNode.getAttributes().getNamedItem("unit") != null) {
											String unit = highDoseNode.getAttributes().getNamedItem("unit").getNodeValue();
											if (unit != null && !unit.equals(CHAR_ONE)) {
												highString += CHAR_SPACE + unit;
											}
										}
									}

									doseString = !Validator.isNull(lowString) ? lowString : "";
									if (!Validator.isNull(highString) && !lowString.equals(highString)) {
										doseString = !Validator.isNull(doseString) ? doseString + " - " + highString : highString;
									}

									if (Validator.isNull(doseString)) {
										doseString = dose.getAttributes().getNamedItem("nullFlavor").getNodeValue();
										if (!Validator.isNull(doseString)) {
											doseString = NullFlavorManager.getNullFlavor(doseString);
										}
									}
								}
							}

							XPathExpression substituteInstrExpr = xpath
									.compile("substanceAdministration/entryRelationship[@typeCode='SUBJ'][@inversionInd='true']/observation[@classCode='OBS']/value");
							Node substituteNode = (Node) substituteInstrExpr.evaluate(entryNode, XPathConstants.NODE);
							SubstitutionPermitted sp = handleSubstitution(substituteNode);

							row.setProductName(name);
							row.setProductId(materialID);

							row.setFormName(packsString);
							row.setFormCode(formCode);

							row.setDosage(doseString);

							XPathExpression packageSize = xpath
									.compile("substanceAdministration/consumable/manufacturedProduct/manufacturedMaterial/asContent/containerPackagedMedicine/capacityQuantity");
							row.setPackageSize(handleQuantity(packageSize, entryNode));

							XPathExpression nrOfPacksExpr = xpath.compile("substanceAdministration/entryRelationship/supply/quantity");
							row.setNbrPackages(handleQuantity(nrOfPacksExpr, entryNode));
							row.setTypeOfPackage(handleTypeOfPackage(xpath, entryNode));

							row.setFrequency(handleEffectiveTimeTypePivl_Ts(xpath, entryNode));
							row.setRoute(handleRoute(xpath, entryNode));

							handleEffectiveTime_IVL_TS(xpath, row, entryNode);

							handleInstructions(prescription, row, xpath, entryNode);

							row.setPrescriber(prescriber);

							// entry header information
							row.setPrescriptionId(prescriptionID);
							row.setPrescriptionIdRoot(prescriptionIDRoot);
							row.setMaterialId(materialID);
							row.setSubstitutionPermittedText(sp.getSubstitutionPermittedText());
							row.setSubstitutionPermitted(sp.isSubstitutionPermitted());

							rows.add(row);
						}
					}
					prescription.setRows(rows);
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Exception: ", e);
		}
	}

	private void handleInstructions(Prescription prescription, PrescriptionRow row, XPath xpath, Node entryNode) throws UnsupportedEncodingException,
			XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {

		String patientString = "";
		XPathExpression patientInstrEexpr = xpath
				.compile("substanceAdministration/entryRelationship/act/code[@code='PINSTRUCT']/../text/reference[@value]");
		Node patientInfo = (Node) patientInstrEexpr.evaluate(entryNode, XPathConstants.NODE);
		if (patientInfo != null) {
			patientString = patientInfo.getAttributes().getNamedItem("value").getNodeValue();
		}
		
		if (patientString.startsWith("#")) {
			patientString = handleReferenceXPath(xpath, entryNode, patientString);
			row.setPatientInstructions(patientString);
		} else {
			row.setPatientInstructions(patientString);
		}

		String fillerString = "";
		XPathExpression fillerInstrEexpr = xpath
				.compile("substanceAdministration/entryRelationship/act/code[@code='FINSTRUCT']/../text/reference[@value]");
		Node fillerInfo = (Node) fillerInstrEexpr.evaluate(entryNode, XPathConstants.NODE);
		if (fillerInfo != null) {
			fillerString = fillerInfo.getAttributes().getNamedItem("value").getNodeValue();
		}

		if (fillerString.startsWith("#")) {
			fillerString = handleReferenceXPath(xpath, entryNode, fillerString);
			row.setPharmacistInstructions(fillerString);
		} else {
			row.setPharmacistInstructions(fillerString);
		}
	}

	private void handleEffectiveTime_IVL_TS(XPath xpath, PrescriptionRow row, Node entryNode) throws XPathExpressionException {
		XPathExpression lowExpr = xpath.compile("substanceAdministration/effectiveTime[@type='IVL_TS']/low");
		Node nodeLow = (Node) lowExpr.evaluate(entryNode, XPathConstants.NODE);
		Date dateLow = getDate(nodeLow);
		if (dateLow != null) {
			row.setStartDate(DateUtil.formatDate(dateLow, "yyyy-MM-dd"));
		} else {
			String nodeLowString = nodeLow.getAttributes().getNamedItem("nullFlavor").getNodeValue();
			if (!Validator.isNull(nodeLowString)) {
				nodeLowString = NullFlavorManager.getNullFlavor(nodeLowString);
			}
			row.setStartDate(nodeLowString);
		}

		XPathExpression highExpr = xpath.compile("substanceAdministration/effectiveTime[@type='IVL_TS']/high");
		Node nodeHigh = (Node) highExpr.evaluate(entryNode, XPathConstants.NODE);
		Date dateHigh = getDate(nodeHigh);
		if (dateHigh != null) {
			row.setEndDate(DateUtil.formatDate(dateHigh, "yyyy-MM-dd"));
		} else {
			String nodeHighString = nodeHigh.getAttributes().getNamedItem("nullFlavor").getNodeValue();
			if (!Validator.isNull(nodeHighString)) {
				nodeHighString = NullFlavorManager.getNullFlavor(nodeHighString);
			}
			row.setEndDate(nodeHighString);
		}
	}

	private List<Ingredient> handleIngredients(XPath xpath, Node entryNode) throws XPathExpressionException {
		List<Ingredient> ingredientList = new ArrayList<Ingredient>();
		XPathExpression ingredientRowExpr = xpath
				.compile("substanceAdministration/consumable/manufacturedProduct/manufacturedMaterial/ingredient[@classCode='ACTI']");
		NodeList ingredientRowNodeList = (NodeList) ingredientRowExpr.evaluate(entryNode, XPathConstants.NODESET);
		if (ingredientRowNodeList != null && ingredientRowNodeList.getLength() > 0) {
			LOGGER.info("IngredientRow Length: " + ingredientRowNodeList.getLength());
			for (int a = 0; a < ingredientRowNodeList.getLength(); a++) {
				Ingredient ingredientVO = new Ingredient();

				Node ingredientNode = ingredientRowNodeList.item(a);

				ingredientVO.setActiveIngredient(handleIngredient(xpath, ingredientNode));
				ingredientVO.setStrength(handleStrength(xpath, ingredientNode));
				ingredientList.add(ingredientVO);
			}
		}
		return ingredientList;
	}

	private String handleStrength(XPath xpath, Node ingredientNode) throws XPathExpressionException {
		String strength = "";
		XPathExpression quantityExpr = xpath.compile("quantity");
		Node quantityNode = (Node) quantityExpr.evaluate(ingredientNode, XPathConstants.NODE);
		if (quantityNode != null && !quantityNode.hasAttributes()) {
			XPathExpression numeratorExpression = xpath.compile("numerator");
			XPathExpression denumeratorExpression = xpath.compile("denominator");
			Node numeratorNode = (Node) numeratorExpression.evaluate(quantityNode, XPathConstants.NODE);
			Node denumeratorNode = (Node) denumeratorExpression.evaluate(quantityNode, XPathConstants.NODE);
			if (numeratorNode != null && denumeratorNode != null && numeratorNode.getAttributes().getNamedItem("nullFlavor") == null) {
				String value = numeratorNode.getAttributes().getNamedItem("value").getNodeValue();
				String unit = numeratorNode.getAttributes().getNamedItem("unit").getNodeValue();
				if (unit != null && !unit.equals(CHAR_ONE) && value != null && value.length() > 0) {
					unit = translateStrengthUnit(unit);
					LOGGER.debug("value (before numberFormat): " + value);
					strength = NUMBER_FORMAT.format(new Double(value)) + CHAR_SPACE + unit;
				}

				if (denumeratorNode.getAttributes().getNamedItem("nullFlavor") == null) {
					String dNValue = denumeratorNode.getAttributes().getNamedItem("value").getNodeValue();
					String dNUnit = denumeratorNode.getAttributes().getNamedItem("unit").getNodeValue();
					if (dNUnit != null && !dNUnit.equals("") && !dNUnit.equals(CHAR_ONE)) {
						dNUnit = translateStrengthUnit(dNUnit);
						if (dNValue != null && !dNValue.equals(CHAR_ONE)) {
							LOGGER.debug("dNValue (before numberFormat): " + dNValue);
							strength += CHAR_FORWARD_SLASH + NUMBER_FORMAT.format(new Double(dNValue)) + CHAR_SPACE + dNUnit;
						} else {
							strength += CHAR_FORWARD_SLASH + dNUnit;
						}
					}
				} else {
					String nullflavor = denumeratorNode.getAttributes().getNamedItem("nullFlavor").getNodeValue();
					if (!Validator.isNull(nullflavor)) {
						strength += CHAR_FORWARD_SLASH + NullFlavorManager.getNullFlavor(nullflavor);
					}
				}
			} else {
				strength = numeratorNode.getAttributes().getNamedItem("nullFlavor").getNodeValue();
				if (!Validator.isNull(strength)) {
					strength = NullFlavorManager.getNullFlavor(strength);
				}
			}
		} else {
			strength = quantityNode.getAttributes().getNamedItem("nullFlavor").getNodeValue();
			if (!Validator.isNull(strength)) {
				strength = NullFlavorManager.getNullFlavor(strength);
			}
		}
		LOGGER.debug("strength: " + strength);
		
		return strength;
	}

	private String translateStrengthUnit(String dNUnit) {
		String translated = new StringResourceModel("strength.unit." + dNUnit, null, dNUnit).getString();
		LOGGER.debug("Strength unit original " + dNUnit + ", translated: " + translated);
		return translated;
	}
	
	//	private StrengthVO handleStrength2(XPath xpath, Node ingredientNode) throws XPathExpressionException {
	//		XPathExpression strengthExpr = xpath.compile("quantity/numerator[@type='epsos:PQ']");
	//		XPathExpression strengthExpr2 = xpath.compile("quantity/denominator[@type='epsos:PQ']");
	//
	//		return new StrengthVO(handleQuantity(strengthExpr, ingredientNode), handleQuantity(strengthExpr2, ingredientNode));
	//	}

	private String handleIngredient(XPath xpath, Node ingredientNode) throws XPathExpressionException {
		String ingredient = "";
		XPathExpression ingredientExpression = xpath.compile("ingredient/code");
		Node ingrNode = (Node) ingredientExpression.evaluate(ingredientNode, XPathConstants.NODE);
		if (ingrNode != null) {
			ingredient += ingrNode.getAttributes().getNamedItem("code").getNodeValue() + " - "
					+ ingrNode.getAttributes().getNamedItem("displayName").getNodeValue();
		}
		return ingredient;
	}

	private QuantityVO handleQuantity(XPathExpression exp, Node inNode) throws XPathExpressionException {
		String quantityValue = "";
		String quantityUnit = "";
		String quantityUnitUcum = "";
		Node node = (Node) exp.evaluate(inNode, XPathConstants.NODE);
		if (node != null) {
			if (node.getAttributes().getNamedItem("value") != null) {
				quantityValue = node.getAttributes().getNamedItem("value").getNodeValue();
				LOGGER.debug("Value: " + quantityValue);
				if (quantityValue.contains(",")) {
					quantityValue = quantityValue.replace(",", ".");
				} else if (quantityValue.contains(".")) {
					double d = Double.parseDouble(quantityValue);
					DecimalFormat df = new DecimalFormat("#0.0#");
					quantityValue = df.format(d).replace(",", ".");
				}
				LOGGER.debug("Formatted value: " + quantityValue);	
			}

			if (node.getAttributes().getNamedItem("unit") != null) {
				quantityUnitUcum = node.getAttributes().getNamedItem("unit").getNodeValue();
				LOGGER.debug("Unit in UCUM: " + quantityUnitUcum);
				quantityUnit = translateQuantityUnit( quantityUnitUcum );
			}
		}
		return new QuantityVO(quantityValue, quantityUnit, quantityUnitUcum);
	}
	
	private String translateQuantityUnit(String cdaQU) {
		String translated = new StringResourceModel("quantity.unit." + cdaQU, null, cdaQU).getString();
		LOGGER.debug("Translated unit: " + translated);
		return translated;
	}
	
	private String handleAssignedPerson(Node sectionNode, XPathExpression prefixExpr, XPathExpression givenNameExpr, XPathExpression familyNameExpr)
			throws XPathExpressionException {
		String returnString = "";

		Node prefix = (Node) prefixExpr.evaluate(sectionNode, XPathConstants.NODE);
		if (prefix != null) {
			returnString += prefix.getTextContent().trim() + CHAR_SPACE;
		}

		Node familyName = (Node) familyNameExpr.evaluate(sectionNode, XPathConstants.NODE);
		if (familyName != null) {
			returnString += familyName.getTextContent().trim() + "," + CHAR_SPACE;
		}

		NodeList givenNames = (NodeList) givenNameExpr.evaluate(sectionNode, XPathConstants.NODESET);
		if (givenNames != null) {
			for(int i = 0; i < givenNames.getLength(); i++) {
				returnString += givenNames.item(i).getTextContent().trim() + CHAR_SPACE;
			}
		}
		
		return returnString.trim();
	}

	private List<String> handlePrescriberTelecom(Document dom, XPath xpath) throws XPathExpressionException {
		String prescriberContact = "";
		List<String> list = new ArrayList<String>();
		XPathExpression telecomExpression = xpath.compile("/ClinicalDocument/author/assignedAuthor/telecom");
		NodeList prescriberTelecomNodeList = (NodeList) telecomExpression.evaluate(dom, XPathConstants.NODESET);
		if (prescriberTelecomNodeList != null && prescriberTelecomNodeList.getLength() > 0) {
			for (int i = 0; i < prescriberTelecomNodeList.getLength(); i++) {
				if (prescriberTelecomNodeList.item(i).getAttributes().getNamedItem("value") != null) {
					prescriberContact = prescriberTelecomNodeList.item(i).getAttributes().getNamedItem("value").getTextContent();
				}

				if (Validator.isNull(prescriberContact)) {
					prescriberContact = prescriberTelecomNodeList.item(i).getAttributes().getNamedItem("nullFlavor").getNodeValue();
					if (!Validator.isNull(prescriberContact)) {
						if (i == 0) {
							prescriberContact = NullFlavorManager.getNullFlavor(prescriberContact);
						} else {
							prescriberContact = "";
						}
					}
				}
				list.add(prescriberContact);
				prescriberContact = "";
			}
		}
		return list;
	}

	private Date handlePrescriptionDate(Document dom, XPath xpath) throws XPathExpressionException {
		Date prescriptionDate = null;
		XPathExpression prescriptionDateExpr = xpath.compile("/ClinicalDocument/author/time");
		Node prescrDate = (Node) prescriptionDateExpr.evaluate(dom, XPathConstants.NODE);
		if (prescrDate != null) {
			prescriptionDate = getDate(prescrDate);
		}
		return prescriptionDate;
	}

	private String handleFacility(Document dom, XPath xpath) throws XPathExpressionException {
		String facility = "";
		XPathExpression facilityNameExpr = xpath.compile("/ClinicalDocument/author/assignedAuthor/representedOrganization/name");
		Node facilityNode = (Node) facilityNameExpr.evaluate(dom, XPathConstants.NODE);
		if (facilityNode != null) {
			facility = facilityNode.getTextContent().trim();
			if (Validator.isNull(facility)) {
				facility = facilityNode.getAttributes().getNamedItem("nullFlavor").getNodeValue();
				if (!Validator.isNull(facility)) {
					facility = NullFlavorManager.getNullFlavor(facility);
				}
			}
		}
		return facility;
	}

	private String handleAdress(Document dom, XPath xpath) throws XPathExpressionException {
		String address = "";

		XPathExpression facilityAddressStreetExpr = xpath
				.compile("/ClinicalDocument/author/assignedAuthor/representedOrganization/addr/streetAddressLine");
		Node street = (Node) facilityAddressStreetExpr.evaluate(dom, XPathConstants.NODE);
		if (street != null) {
			address += street.getTextContent().trim();
		}

		XPathExpression facilityAddressZipExpr = xpath.compile("/ClinicalDocument/author/assignedAuthor/representedOrganization/addr/postalCode");
		Node zip = (Node) facilityAddressZipExpr.evaluate(dom, XPathConstants.NODE);
		if (zip != null) {
			address += ", " + zip.getTextContent().trim();
		}

		XPathExpression facilityAddressCityExpr = xpath.compile("/ClinicalDocument/author/assignedAuthor/representedOrganization/addr/city");
		Node city = (Node) facilityAddressCityExpr.evaluate(dom, XPathConstants.NODE);
		if (city != null) {
			address += ", " + city.getTextContent().trim();
		}
		String country = handleCountryPrescriber(dom, xpath);
		if (country != null) {
			address += ", " + country;
		}

		return address;
	}

	private String handleCountryPrescriber(Document dom, XPath xpath) throws XPathExpressionException {
		String country = "";
		XPathExpression facilityAddressCountryExpr = xpath.compile("/ClinicalDocument/author/assignedAuthor/representedOrganization/addr/country");
		Node countryNode = (Node) facilityAddressCountryExpr.evaluate(dom, XPathConstants.NODE);
		if (countryNode != null) {
			country = countryNode.getTextContent().trim();
		}
		return country;
	}

	private String handleEffectiveTimeTypePivl_Ts(XPath xpath, Node entryNode) throws XPathExpressionException {
		XPathExpression effectiveTime = xpath.compile("substanceAdministration/effectiveTime[@type='PIVL_TS']");
		XPathExpression period = xpath.compile("substanceAdministration/effectiveTime[@type='PIVL_TS']/period");
		String freqString = "";
		Node effectiveTimeNode = (Node) effectiveTime.evaluate(entryNode, XPathConstants.NODE);
		if (effectiveTimeNode != null) {
			Node periodNode = (Node) period.evaluate(entryNode, XPathConstants.NODE);
			if (periodNode != null && periodNode.getAttributes().getNamedItem("value") != null
					&& periodNode.getAttributes().getNamedItem("unit") != null) {
				freqString = periodNode.getAttributes().getNamedItem("value").getNodeValue() + CHAR_SPACE
						+ periodNode.getAttributes().getNamedItem("unit").getNodeValue();
			} else {
				if (effectiveTimeNode.getAttributes().getNamedItem("nullFlavor") != null) {
					freqString = effectiveTimeNode.getAttributes().getNamedItem("nullFlavor").getNodeValue();
				} else if (periodNode.getAttributes().getNamedItem("nullFlavor") != null) {
					freqString = periodNode.getAttributes().getNamedItem("nullFlavor").getNodeValue();
				}
				if (!Validator.isNull(freqString)) {
					freqString = NullFlavorManager.getNullFlavor(freqString);
				}
			}
		}
		return freqString;
	}

	private String handleTypeOfPackage(XPath xpath, Node entryNode) throws XPathExpressionException {
		String typeOfPackage = "";
		XPathExpression packTypeExpr = xpath
				.compile("substanceAdministration/consumable/manufacturedProduct/manufacturedMaterial/asContent/containerPackagedMedicine/formCode");
		Node packType = (Node) packTypeExpr.evaluate(entryNode, XPathConstants.NODE);
		if (packType != null) {
			if (packType.getAttributes().getNamedItem("displayName") != null) {
				typeOfPackage = packType.getAttributes().getNamedItem("displayName").getNodeValue();
			}

			if (Validator.isNull(typeOfPackage)) {
				typeOfPackage = packType.getAttributes().getNamedItem("nullFlavor").getNodeValue();
				if (!Validator.isNull(typeOfPackage)) {
					typeOfPackage = NullFlavorManager.getNullFlavor(typeOfPackage);
				}
			}
		}
		return typeOfPackage;
	}

	private String handleProfession(Document dom, XPath xpath) throws XPathExpressionException {
		String profession = "";
		XPathExpression professionExpr = xpath.compile("/ClinicalDocument/author/functionCode");
		Node professionNode = (Node) professionExpr.evaluate(dom, XPathConstants.NODE);
		if (professionNode != null) {
			profession = professionNode.getAttributes().getNamedItem("displayName").getNodeValue();
			if (Validator.isNull(profession)) {
				profession = professionNode.getAttributes().getNamedItem("nullFlavor").getNodeValue();
				if (!Validator.isNull(profession)) {
					profession = NullFlavorManager.getNullFlavor(profession);
				}
			}
		}
		return profession;
	}

	private String handleRoute(XPath xpath, Node entryNode) throws XPathExpressionException {
		String routeString = "";
		XPathExpression routeExpr = xpath.compile("substanceAdministration/routeCode");
		Node route = (Node) routeExpr.evaluate(entryNode, XPathConstants.NODE);
		if (route != null) {
			if (route.getAttributes().getNamedItem("displayName") != null) {
				routeString = route.getAttributes().getNamedItem("displayName").getNodeValue();
			}
			if (Validator.isNull(routeString)) {
				routeString = route.getAttributes().getNamedItem("nullFlavor").getNodeValue();
				if (!Validator.isNull(routeString)) {
					routeString = NullFlavorManager.getNullFlavor(routeString);
				}
			}
		}
		return routeString;
	}

	private SubstitutionPermitted handleSubstitution(Node substituteNode) {
		String substituteValue = "";
		
		if (substituteNode != null) {
			if (substituteNode.getAttributes().getNamedItem("code") != null) {
				substituteValue = substituteNode.getAttributes().getNamedItem("code").getNodeValue();
			}
			if (Validator.isNull(substituteValue)) {
				substituteValue = substituteNode.getAttributes().getNamedItem("nullFlavor").getNodeValue();
				if (!Validator.isNull(substituteValue)) {
					return new SubstitutionPermitted(false, NullFlavorManager.getNullFlavor(substituteValue));
				}
			}
		}

		if (substituteValue.equals("TE")) {
			return new SubstitutionPermitted(true, new StringResourceModel("prescription.substitute.therapeutic", null, "").getString());
		} else if (substituteValue.equals("G")) {
			return new SubstitutionPermitted(true, new StringResourceModel("prescription.substitute.generic", null, "").getString());
		} else {
			return new SubstitutionPermitted(false, new StringResourceModel("prescription.substitute.null", null, "").getString());
		}
	}

	private Date getDate(Node node) {
		Date date = null;
		if (node != null) {
			if (node.getAttributes().getNamedItem("value") != null) {
				try {
					String str = node.getAttributes().getNamedItem("value").getNodeValue();
					date = DateUtil.formatStringToDate(str.substring(0, 8));
				} catch (Exception e) {
					LOGGER.error("Error formating node from string to date.", e);
				}
			} else {
				return null;
			}
		}
		return date;
	}

	private String handleReferenceXPath(XPath xpath, Node entryNode, String reference) throws XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
		String refValue = "";
		if(reference != null && reference.length() > 1) {
			XPathExpression referenceExpr = xpath.compile("//*[@ID='" + reference.substring(1) + "']");
			Node refValueNode = (Node) referenceExpr.evaluate(entryNode, XPathConstants.NODE);
            refValue = transformNodeContentToString(refValueNode);
            LOGGER.info("Reference(" + reference + ") found: " + refValue);
		}
		return refValue;
	}
	
	private String transformNodeContentToString(Node node) throws TransformerFactoryConfigurationError, TransformerException {
        if(node != null && node.hasChildNodes()) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            StringWriter stw = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(stw));
            String nodeContent = stw.toString();
            return nodeContent.substring(nodeContent.indexOf('>') + 1, nodeContent.lastIndexOf('<'));
        } else {
            return "";
        }
    }
	
	public static class Validator {
		public static boolean isNull(String str) {
			return (str == null || str.isEmpty());
		}
	}

	private class SubstitutionPermitted {
		private String substitutionPermittedText;
		private boolean substitutionPermitted;
		
		private SubstitutionPermitted(boolean substitutionPermitted, String substitutionPermittedText) {
			this.substitutionPermitted = substitutionPermitted;
			this.substitutionPermittedText = substitutionPermittedText;
		}
		
		public String getSubstitutionPermittedText() {
			return substitutionPermittedText;
		}
		public boolean isSubstitutionPermitted() {
			return substitutionPermitted;
		}
	}
}
