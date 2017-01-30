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
package se.sb.epsos.web.service.mock;

import org.apache.xmlbeans.impl.util.HexBin;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import se.sb.epsos.shelob.ws.client.jaxws.EpsosDocument;
import se.sb.epsos.shelob.ws.client.jaxws.GenericDocumentCode;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DocumentCatalog {

    private static final Map<String, List<EpsosDocument>> prescriptions = createPrescriptions();
    private static final Map<String, List<EpsosDocument>> patientsummaries = createPatientSummaries();

    private static Map<String, List<EpsosDocument>> createPrescriptions() {

        Map<String, List<EpsosDocument>> map = new HashMap<>();
        map.put("191212121212^^^&2.16.17.710.807.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPrescription("191212121212", "1.1.1", "Insulin (human)", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("191212121212", "1.2.1", "Paracetamol", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                }));

        map.put("193508249079^^^&2.16.17.710.807.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPrescription("193508249079", "2.1.1", "Terbutaline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("193508249079", "2.2.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("193508249079", "2.3.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("193508249079", "2.4.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("193508249079", "2.5.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("193508249079", "2.6.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("193508249079", "2.7.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("193508249079", "2.8.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("193508249079", "2.9.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                }));

        map.put("199604082397^^^&2.16.17.710.807.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPrescription("199604082397", "3.1.1", "Flucloxacillin", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("199604082397", "3.2.1", "Furosemide", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                }));

        map.put("192405038569^^^&2.16.17.710.807.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPrescription("192405038569", "4.1.1", "Fenoximetylpenicillin (penivillin V)", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("192405038569", "4.2.1", "Natriumkromoglikat", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                }));

        // SE Special
        map.put("192405038569^^^&2.16.17.710.888.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPrescription("192405038569", "4.1.1", "Fenoximetylpenicillin (penivillin V)", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("192405038569", "4.2.1", "Natriumkromoglikat", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                }));


        // DK prescriptions
        map.put("2601010001^^^&2.16.17.710.802.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPrescription("2601010001", "6.1.1", "Panodil, filmovertrukne tabletter", "Terri Dalsgaard", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("2601010001", "6.2.1", "Fosavance, tabletter", "Terri Dalsgaard", "2012-03-19T00:00:00.000+01:00"),
                }));

        // FI prescriptions
        map.put("031082-9958^^^&1.2.246.556.12001.4.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPrescription("031082-9958", "1.1.1", "Insulin (human)", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "1.2.1", "Paracetamol", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "2.1.1", "Terbutaline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "2.2.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "2.3.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "2.4.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "2.5.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "2.6.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "2.7.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "2.8.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("031082-9958", "2.9.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                }));

        // EU prescriptions
        map.put("1^^^&2.16.17.710.850.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPrescription("1", "1.1.1", "Insulin (human)", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "1.2.1", "Paracetamol", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "2.1.1", "Terbutaline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "2.2.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "2.3.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "2.4.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "2.5.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "2.6.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "2.7.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "2.8.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                        createPrescription("1", "2.9.1", "Doxycycline", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                }));
        return map;
    }

    private static Map<String, List<EpsosDocument>> createPatientSummaries() {
        Map<String, List<EpsosDocument>> map = new HashMap<>();
        map.put("192405038569^^^&2.16.17.710.807.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPatientSummary("192405038569", "192405038569.1.1", "CDA Patientsummary", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                }));
        map.put("2512484916^^^&2.16.17.710.802.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPatientSummary("2512484916", "2512484916.1.1", "Patient Summary for id: 2512484916.cda.ps.1.0:1", "National Board of E-Health", "2012-03-19T00:00:00.000+01:00"),
                }));
        map.put("1^^^&2.16.17.710.850.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPatientSummary("1", "192405038569.1.1", "CDA Patientsummary", "Lars L\u00E4kare", "2012-03-19T00:00:00.000+01:00"),
                }));
        map.put("1^^^&2.16.17.710.850.1000.990.1",
                Arrays.asList(new EpsosDocument[]{
                        createPatientSummary("1", "2512484916.1.1", "Patient Summary for id: 2512484916.cda.ps.1.0:1", "National Board of E-Health", "2012-03-19T00:00:00.000+01:00"),
                }));
        return map;
    }

    public static List<EpsosDocument> queryEP(String patientId) {
        List<EpsosDocument> result = prescriptions.get(patientId);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public static List<EpsosDocument> queryPS(String patientId) {
        List<EpsosDocument> result = patientsummaries.get(patientId);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public static byte[] get(String documentId) {
        InputStream is = null;
        if (documentId.startsWith("EP")) {
            is = DocumentCatalog.class.getResourceAsStream("ep/" + documentId + ".xml");
        } else if (documentId.startsWith("PS")) {
            is = DocumentCatalog.class.getResourceAsStream("ps/" + documentId + ".xml");
        }
        ;
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] bytes = new byte[512];
        int readBytes;
        try {
            while ((readBytes = is.read(bytes)) > 0) {
                os.write(bytes, 0, readBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return os.toByteArray();
    }

    private static EpsosDocument createPrescription(String patientId, String documentId, String description, String author, String createDate) {
        EpsosDocument epDoc = createDocument(patientId, documentId,
                "2.16.17.710.807.1000.990.1", "EP", "CDA", "57833-6", "ePrescription", "2.16.840.1.113883.6.1",
                "urn:epSOS:ep:pre:2010", "epSOS coded ePrescription", "epSOS formatCodes",
                "ePrescription",
                description, author, getDate(createDate));

        EpsosDocument pdf = createDocument(patientId, documentId,
                "2.16.17.710.807.1000.990.1", "EP", "PDF", "57833-6", "ePrescription", "2.16.840.1.113883.6.1",
                "urn:ihe:iti:xds-sd:pdf:2008", "PDF/A coded document", "epSOS formatCodes",
                "ePrescription",
                description, author, getDate(createDate));
//		pdf.setParentDocumentRelationShip("XFRM");
//		pdf.setParentDocumentId(epDoc.getUUID());
        epDoc.getAssociatedDocuments().add(pdf);
        return epDoc;
    }

    private static EpsosDocument createPatientSummary(String patientId, String documentId, String description, String author, String createDate) {
        EpsosDocument psDoc = createDocument(patientId, documentId,
                "2.16.17.710.807.1000.990.1", "PS", "CDA", "60591-5", "Patient Summary", "2.16.840.1.113883.6.1",
                "urn:epSOS:ps:ps:2010", "epSOS coded Patient Summary", "epSOS formatCodes",
                "Patient Summary",
                description, author, getDate(createDate));

        EpsosDocument pdf = createDocument(patientId, documentId,
                "2.16.17.710.807.1000.990.1", "PS", "PDF", "60591-5", "Patient Summary", "2.16.840.1.113883.6.1",
                "urn:ihe:iti:xds-sd:pdf:2008", "PDF/A coded document", "epSOS formatCodes",
                "Patient Summary",
                description, author, getDate(createDate));
//		pdf.setParentDocumentRelationShip("XFRM");
//		pdf.setParentDocumentId(psDoc.getUUID());
        psDoc.getAssociatedDocuments().add(pdf);
        return psDoc;
    }

    private static EpsosDocument createDocument(String patientId,
                                                String documentId,
                                                String authUniversalID,
                                                String docTypePrefix,
                                                String docTypeSuffix,
                                                String classCodeNodeRepr,
                                                String classCodeValue,
                                                String classCodeSchema,
                                                String formatCodeNodeRepre,
                                                String formatCodeValue,
                                                String formatCodeSchema,
                                                String title,
                                                String description,
                                                String author,
                                                XMLGregorianCalendar createDate) {

        EpsosDocument doc = new EpsosDocument();


        String uniqueId = docTypePrefix + "." + documentId + "." + docTypeSuffix;
        doc.setUuid(uniqueId);
        doc.setTitle(title);
        doc.setAuthor(author);
        doc.setDescription(description);
        doc.setMimeType("text/xml");
        doc.setCreationDate(createDate);
        GenericDocumentCode classCode = new GenericDocumentCode();
        classCode.setNodeRepresentation(classCodeNodeRepr);
        doc.setClassCode(classCode);

        GenericDocumentCode formatCode = new GenericDocumentCode();
        formatCode.setNodeRepresentation(formatCodeNodeRepre);
        doc.setFormatCode(formatCode);

        return doc;
    }

    private static String generateUUID(String uniqueId) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("MD5");
        byte[] digest = digester.digest(uniqueId.getBytes());
        String hexDigest = HexBin.bytesToString(digest);
        String uuid = hexDigest.substring(0, 8) + "-" +
                hexDigest.substring(8, 12) + "-" +
                hexDigest.substring(12, 16) + "-" +
                hexDigest.substring(16, 20) + "-" +
                hexDigest.substring(20);
        return uuid.toLowerCase();
    }

    private static XMLGregorianCalendar getXMLGregorian(GregorianCalendar calendar) {
        XMLGregorianCalendar date2 = null;
        try {
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return date2;
    }

    private static XMLGregorianCalendar getDate(String indate) {
        DateTimeFormatter df = ISODateTimeFormat.dateTime();
        DateTime dateTime = df.parseDateTime(indate);
        return getXMLGregorian(dateTime.toGregorianCalendar());
    }
}
