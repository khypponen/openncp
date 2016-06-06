/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.pt.cc.dts.axis2;

import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.data.model.xds.XDSDocument;
import tr.com.srdc.epsos.data.model.xds.XDSDocumentAssociation;
import tr.com.srdc.epsos.util.Constants;

/**
 * This is an Data Transformation Service. This provide functions to transform
 * data into a Document object.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class DocumentDts {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentDts.class);

    /**
     * Private constructor to disable class instantiation.
     */
    private DocumentDts() {
    }

    /**
     * Converts a XDSDocument into a Document.
     *
     * @param document the document to be converted.
     * @return the result of the conversion, as a Document.
     */
    public static EpsosDocument1 newInstance(XDSDocument document) {
        /*
         * PRE-CONDITIONS
         */
        if (document == null) {
            return null;
        }

        /*
         * BODY
         */

        final EpsosDocument1 result = EpsosDocument1.Factory.newInstance();

        result.setUuid(document.getDocumentUniqueId());
        result.setDescription(document.getDescription());
        result.setCreationDate(convertDate(document.getCreationTime()));
        result.setClassCode(GenericDocumentCodeDts.newInstance(document.getClassCode()));
        result.setFormatCode(GenericDocumentCodeDts.newInstance(document.getFormatCode()));
        result.setRepositoryId(document.getRepositoryUniqueId());
        result.setHcid(document.getHcid());
        result.setAuthor(document.getAuthorPerson());

        if (result.getClassCode() != null && !result.getClassCode().getNodeRepresentation().isEmpty()) {
            if (result.getClassCode().getNodeRepresentation().equals(Constants.PS_CLASSCODE)) {
                result.setTitle(Constants.PS_TITLE);
            } else if (result.getClassCode().getNodeRepresentation().equals(Constants.EP_CLASSCODE)) {
                result.setTitle(Constants.EP_TITLE);
            } else if (result.getClassCode().getNodeRepresentation().equals(Constants.ED_CLASSCODE)) {
                result.setTitle(Constants.ED_TITLE);
            }
        }

        return result;
    }

    /**
     * Converts a list of XDSDocument to a list of Document.
     *
     * @param documentAssociation the list of XDSDocument.
     * @return the result of the conversion, as a list of Document.
     */
    public static EpsosDocument1[] newInstance(List<XDSDocumentAssociation> documentAssociation) {
        /*
         * PRE-CONDITIONS
         */

        if (documentAssociation == null || documentAssociation.isEmpty()) {
            return null;
        }

        /*
         * BODY
         */


        List<EpsosDocument1> resultList = new ArrayList<EpsosDocument1>();

        for (XDSDocumentAssociation doc : documentAssociation) {
            EpsosDocument1 xmlDoc = DocumentDts.newInstance(doc.getCdaXML());
            EpsosDocument1 pdfDoc = DocumentDts.newInstance(doc.getCdaPDF());

            if (xmlDoc != null && pdfDoc != null) {
                xmlDoc.setAssociatedDocumentsArray(new EpsosDocument1[]{pdfDoc});
                pdfDoc.setAssociatedDocumentsArray(new EpsosDocument1[]{xmlDoc});
                resultList.add(xmlDoc);
                resultList.add(pdfDoc);
            }
        }

        final EpsosDocument1[] result = resultList.toArray(new EpsosDocument1[resultList.size()]);
        return result;

    }

    /**
     * Converts a DocumentResponse into a Document new instance.
     *
     * @param documentResponse the document to be converted.
     * @return the result of the conversion, as a Document.
     */
    public static EpsosDocument1 newInstance(DocumentResponse documentResponse) {
        /*
         * PRE-CONDITIONS
         */

        if (documentResponse == null) {
            return null;
        }

        /*
         * BODY
         */

        final EpsosDocument1 result = EpsosDocument1.Factory.newInstance();

        result.setUuid(documentResponse.getDocumentUniqueId());
        result.setMimeType(documentResponse.getMimeType());
        result.setBase64Binary(documentResponse.getDocument());

        return result;

    }

    /**
     * Converts a string containing a date in the yyyyMMddHHmmss format to a
     * Calendar instance.
     *
     * @param dateString a String representation of the Date.
     * @return a Calendar instance, with the given String values.
     */
    private static Calendar convertDate(String dateString) {

        String pattern1 = "yyyyMMddHHmmss";
        String pattern2 = "yyyyMMdd";
        String selectedPattern;

        if (dateString.length() == pattern1.length()) {
            selectedPattern = pattern1;
        } else if (dateString.length() == pattern2.length()) {
            selectedPattern = pattern2;
        } else {
            return null;
        }

        DateFormat formatter;
        Date date;
        Calendar cal = null;

        formatter = new SimpleDateFormat(selectedPattern);
        try {
            date = (Date) formatter.parse(dateString);
            cal = Calendar.getInstance();
            cal.setTime(date);
        } catch (ParseException ex) {
            new RuntimeException(ex);
        }

        return cal;
    }
}
