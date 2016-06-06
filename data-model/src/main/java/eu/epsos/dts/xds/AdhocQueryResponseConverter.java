/*
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. epsos@srdc.com.tr
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see http://www.gnu.org/licenses/.
 */
package eu.epsos.dts.xds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import tr.com.srdc.epsos.data.model.xds.QueryResponse;
import tr.com.srdc.epsos.data.model.xds.XDSDocument;
import tr.com.srdc.epsos.data.model.xds.XDSDocumentAssociation;

/**
 * This class represents a Data Transfer Service, used by XCA and
 * AdhocQueryResponse messages.
 *
 */
public final class AdhocQueryResponseConverter {

    /**
     * Transforms a AdhocQueryResponse in a QueryResponse.
     *
     * @param response - in AdhocQueryResponse format
     * @return a QueryReponse object.
     */
    public static QueryResponse convertAdhocQueryResponse(AdhocQueryResponse response) {
        QueryResponse queryResponse = new QueryResponse();

        if (response.getRegistryObjectList() != null) {
            Map<String, String> documentAssociationsMap = new TreeMap<String, String>();
            List<XDSDocument> documents = new ArrayList<XDSDocument>();

            String str;
            // TODO A.R. Why size()-1??? 
            for (int i = 0; i < response.getRegistryObjectList().getIdentifiable().size(); i++) {
                JAXBElement<?> o = (JAXBElement<ExtrinsicObjectType>) response.getRegistryObjectList().getIdentifiable().get(i);
                String declaredTypeName = o.getDeclaredType().getSimpleName();
                // TODO A.R. What should we do with Assotoation?
                if ("ExtrinsicObjectType".equals(declaredTypeName)) {
                    XDSDocument document = new XDSDocument();

                    JAXBElement<ExtrinsicObjectType> eo;
                    eo = (JAXBElement<ExtrinsicObjectType>) response.getRegistryObjectList().getIdentifiable().get(i);

                    //Set id
                    document.setId(eo.getValue().getId());

                    //Set hcid
                    document.setHcid(eo.getValue().getHome());

                    // Set name
                    document.setName(eo.getValue().getName().getLocalizedString().get(0).getValue());

                    // Set documentUniqueId  
                    for (ExternalIdentifierType idenType : eo.getValue().getExternalIdentifier()) {
                        if (idenType.getName().getLocalizedString().get(0).getValue().equalsIgnoreCase("XDSDocumentEntry.uniqueId")) {
                            document.setDocumentUniqueId(idenType.getValue());
                        }
                    }

                    // Set description
                    if (eo.getValue().getDescription() != null) {
                        if (!eo.getValue().getDescription().getLocalizedString().isEmpty()) {
                            document.setDescription(eo.getValue().getDescription().getLocalizedString().get(0).getValue());
                        }
                    }

                    for (int j = 0; j < eo.getValue().getSlot().size(); j++) {
                        str = eo.getValue().getSlot().get(j).getName();

                        // Set creationTime
                        if (str.equals("creationTime")) {
                            document.setCreationTime(eo.getValue().getSlot().get(j).getValueList().getValue().get(0));
                        }

                        // Set repositoryUniqueId
                        if (str.equals("repositoryUniqueId")) {
                            document.setRepositoryUniqueId(eo.getValue().getSlot().get(j).getValueList().getValue().get(0));
                        }

                    }


                    for (int j = 0; j < eo.getValue().getClassification().size(); j++) {
                        str = eo.getValue().getClassification().get(j).getClassificationScheme();
                        //Set isPDF
                        if (str.equals("urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d")) {
                            if (eo.getValue().getClassification().get(j).getNodeRepresentation().equals("urn:ihe:iti:xds-sd:pdf:2008")) {
                                document.setPDF(true);
                            } else {
                                document.setPDF(false);
                            }
                            // Set FormatCode
                            document.setFormatCode(eo.getValue().getClassification().get(j).getSlot().get(0).getValueList().getValue().get(0), eo.getValue().getClassification().get(j).getNodeRepresentation());
                        }

                        // Set healthcareFacility
                        if (str.equals("urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1")) {
                            document.setHealthcareFacility(eo.getValue().getClassification().get(j).getName().getLocalizedString().get(0).getValue());
                        }

                        // Set ClassCode
                        if (str.equals("urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a")) {
                            document.setClassCode(eo.getValue().getClassification().get(j).getSlot().get(0).getValueList().getValue().get(0), eo.getValue().getClassification().get(j).getNodeRepresentation());
                        }

                        // Set AuthorPerson
                        if (str.equals("urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d") && eo.getValue().getClassification().get(j).getSlot() != null) {
                            for (SlotType1 slot : eo.getValue().getClassification().get(j).getSlot()) {
                                if (slot.getName().equals("authorPerson") && slot.getValueList().getValue().get(0) != null) {
                                    document.setAuthorPerson(slot.getValueList().getValue().get(0));
                                }
                            }
                        }
                    }

                    documents.add(document);
                } else if ("AssociationType1".equals(declaredTypeName)) {
                   JAXBElement<AssociationType1> eo;
                    eo = (JAXBElement<AssociationType1>) response.getRegistryObjectList().getIdentifiable().get(i);
                    if (eo.getValue().getAssociationType().equals("urn:ihe:iti:2007:AssociationType:XFRM")) {
                        documentAssociationsMap.put(eo.getValue().getSourceObject(), eo.getValue().getTargetObject());
                    }
                }
            }

            List<XDSDocumentAssociation> documentAssociations = new ArrayList<XDSDocumentAssociation>();
            for (String key : documentAssociationsMap.keySet()) {
                String sourceObjectId = key;
                String targetObjectId = documentAssociationsMap.get(key);

                XDSDocument sourceObject = null;
                XDSDocument targetObject = null;

                for (XDSDocument doc : documents) {
                    if (doc.getId().matches(sourceObjectId)) {
                        sourceObject = doc;
                    } else if (doc.getId().matches(targetObjectId)) {
                        targetObject = doc;
                    } else {
                        continue;
                    }

                    if (sourceObject != null && targetObject != null) {
                        break;
                    }
                }

                if (sourceObject != null && targetObject != null) {
                    XDSDocumentAssociation xdsDocumentAssociation = new XDSDocumentAssociation();

                    if (sourceObject.isPDF()) {
                        xdsDocumentAssociation.setCdaPDF(sourceObject);
                    } else {
                        xdsDocumentAssociation.setCdaXML(sourceObject);
                    }

                    if (targetObject.isPDF()) {
                        xdsDocumentAssociation.setCdaPDF(targetObject);
                    } else {
                        xdsDocumentAssociation.setCdaXML(targetObject);
                    }

                    documentAssociations.add(xdsDocumentAssociation);
                }

                documents.remove(sourceObject);
                documents.remove(targetObject);

            }

            for (XDSDocument doc : documents) {
                XDSDocumentAssociation xdsDocumentAssociation = new XDSDocumentAssociation();
                xdsDocumentAssociation.setCdaPDF(doc.isPDF() ? doc : null);
                xdsDocumentAssociation.setCdaXML(doc.isPDF() ? null : doc);

                documentAssociations.add(xdsDocumentAssociation);
            }

            queryResponse.setDocumentAssociations(documentAssociations);
        }

        if (response.getRegistryErrorList() != null) {
            List<String> errors = new ArrayList<String>(response.getRegistryErrorList().getRegistryError().size());

            for (int i = 0; i < response.getRegistryErrorList().getRegistryError().size(); i++) {
                errors.add(response.getRegistryErrorList().getRegistryError().get(i).getCodeContext());
            }

            queryResponse.setFailureMessages(errors);
        }

        return queryResponse;
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private AdhocQueryResponseConverter() {
    }
}
