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

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import eu.epsos.util.xca.XCAConstants;
import tr.com.srdc.epsos.data.model.GenericDocumentCode;

public class AdhocQueryRequestCreator {

    public static AdhocQueryRequest createAdhocQueryRequest(String id, String homeCommunityId, GenericDocumentCode docClassCode) {
        AdhocQueryRequest aqr = new AdhocQueryRequest();

        // Set AdhocQueryRequest/ResponseOption
        ResponseOptionType rot = new ResponseOptionType();
        rot.setReturnComposedObjects(true);
        rot.setReturnType(XCAConstants.AdHocQueryRequest.RESPONSE_OPTIONS_RETURN_TYPE);
        aqr.setResponseOption(rot);

        // Create AdhocQueryRequest
        aqr.setAdhocQuery(new AdhocQueryType());
        aqr.getAdhocQuery().setId(XCAConstants.AdHocQueryRequest.ID);

        // Set XDSDocumentEntryPatientId Slot
        SlotType1 patientId = new SlotType1();
        patientId.setName(XCAConstants.AdHocQueryRequest.XDS_DOCUMENT_ENTRY_PATIENTID_SLOT_NAME);
        ValueListType v1 = new ValueListType();
        v1.getValue().add("'" + id + "^^^&" + homeCommunityId + "&" + "ISO'");
        patientId.setValueList(v1);
        aqr.getAdhocQuery().getSlot().add(patientId);

        // Set XDSDocumentEntryStatus Slot
        SlotType1 entryStatus = new SlotType1();
        entryStatus.setName(XCAConstants.AdHocQueryRequest.XDS_DOCUMENT_ENTRY_STATUS_SLOT_NAME);
        ValueListType v2 = new ValueListType();
        v2.getValue().add(XCAConstants.AdHocQueryRequest.XDS_DOCUMENT_ENTRY_STATUS_SLOT_VALUE);
        entryStatus.setValueList(v2);
        aqr.getAdhocQuery().getSlot().add(entryStatus);

        // Set XDSDocumentEntryClassCode Slot
        SlotType1 entryClassCode = new SlotType1();
        entryClassCode.setName(XCAConstants.AdHocQueryRequest.XDS_DOCUMENT_ENTRY_CLASSCODE_SLOT_NAME);
        ValueListType v3 = new ValueListType();
        v3.getValue().add("('"+docClassCode.getValue()+"^^"+docClassCode.getSchema()+"')");
        entryClassCode.setValueList(v3);
        aqr.getAdhocQuery().getSlot().add(entryClassCode);

        return aqr;
    }
}
