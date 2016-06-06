package fi.kela.se.epsos.data.model;

import fi.kela.se.epsos.data.model.SearchCriteria.Criteria;
import java.util.Date;
import org.w3c.dom.Document;
import tr.com.srdc.epsos.util.Constants;

/**
 * Factory for fi.kela.se.epsos.data.model interfaces.
 *
 * @author mimyllyv
 *
 */
public class DocumentFactory {

    /**
     * SearchCriteria
     */
    public static SearchCriteria createSearchCriteria() {
        return new SearchCriteriaImpl();
    }

    public static SearchCriteria createSearchCriteria(Criteria c, String value) {
        SearchCriteria sc = new SearchCriteriaImpl();
        sc.add(c, value);
        return sc;
    }

    /**
     * DocumentAssociation
     */
    public static <T extends EPSOSDocumentMetaData> DocumentAssociation<T> createDocumentAssociation(T xml, T pdf) {
        return new DocumentAssociationImpl<T>(xml, pdf);
    }

    /**
     * EPSOSDocument
     */
    public static EPSOSDocument createEPSOSDocument(String patientId, String classCode, Document document) {
        return new EPSOSDocumentImpl(patientId, classCode, document);
    }

    /**
     * EPDocument
     */
    private static EPDocumentMetaData createEPDocument(int documentFormat, String id, String patientId,
            Date effectiveDate, String repositoryId, String title, String author) {
        EPSOSDocumentMetaData metaData = new EPSOSDocumentMetaDataImpl(id, patientId, documentFormat, effectiveDate, Constants.EP_CLASSCODE, repositoryId, title, author);
        return new EPDocumentMetaDataImpl(metaData);
    }

    public static EPDocumentMetaData createEPDocumentPDF(String id, String patientId, Date effectiveDate,
            String repositoryId, String title, String author) {
        return createEPDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_PDF, id, patientId, effectiveDate, repositoryId, title, author);
    }

    public static EPDocumentMetaData createEPDocumentXML(String id, String patientId, Date effectiveDate,
            String repositoryId, String title, String author) {
        return createEPDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_XML, id, patientId, effectiveDate, repositoryId, title, author);
    }

    /**
     * PSDocument
     */
    private static PSDocumentMetaData createPSDocument(int documentFormat, String id, String patientId,
            Date effectiveDate, String repositoryId, String title, String author) {

        EPSOSDocumentMetaData metaData = new EPSOSDocumentMetaDataImpl(id, patientId, documentFormat, effectiveDate, Constants.PS_CLASSCODE, repositoryId, title, author);
        return new PSDocumentMetaDataImpl(metaData);
    }

    public static PSDocumentMetaData createPSDocumentPDF(String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        return createPSDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_PDF, id, patientId, effectiveDate, repositoryId, title, author);
    }

    public static PSDocumentMetaData createPSDocumentXML(String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        return createPSDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_XML, id, patientId, effectiveDate, repositoryId, title, author);
    }

    /**
     * EDDocument
     */
    private static EDDocumentMetaData createEDDocument(int documentFormat, String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        EPSOSDocumentMetaData metaData = new EPSOSDocumentMetaDataImpl(id, patientId, documentFormat, effectiveDate, Constants.ED_CLASSCODE, repositoryId, title, author);
        return new EDDocumentMetaDataImpl(metaData);
    }

    public static EDDocumentMetaData createEDDocumentPDF(String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        return createEDDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_PDF, id, patientId, effectiveDate, repositoryId, title, author);
    }

    public static EDDocumentMetaData createEDDocumentXML(String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        return createEDDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_XML, id, patientId, effectiveDate, repositoryId, title, author);
    }

    /**
     * ConsentDocument
     */
    private static ConsentDocumentMetaData createConsentDocument(int documentFormat, String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        EPSOSDocumentMetaData metaData = new EPSOSDocumentMetaDataImpl(id, patientId, documentFormat, effectiveDate, Constants.CONSENT_CLASSCODE, repositoryId, title, author);
        return new ConsentDocumentMetaDataImpl(metaData);
    }

    public static ConsentDocumentMetaData createConsentDocumentPDF(String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        return createConsentDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_PDF, id, patientId, effectiveDate, repositoryId, title, author);
    }

    public static ConsentDocumentMetaData createConsentDocumentXML(String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        return createConsentDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_XML, id, patientId, effectiveDate, repositoryId, title, author);
    }

    /**
     * MRODocument
     */
    private static MroDocumentMetaData createMroDocument(int documentFormat, String id, String patientId,
            Date effectiveDate, String repositoryId, String title, String author) {

        EPSOSDocumentMetaData metaData = new EPSOSDocumentMetaDataImpl(id, patientId, documentFormat, effectiveDate, Constants.MRO_CLASSCODE, repositoryId, title, author);
        return new MroDocumentMetaDataImpl(metaData);
    }

    public static MroDocumentMetaData createMroDocumentPDF(String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        return createMroDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_PDF, id, patientId, effectiveDate, repositoryId, title, author);
    }

    public static MroDocumentMetaData createMroDocumentXML(String id, String patientId, Date effectiveDate, String repositoryId, String title, String author) {
        return createMroDocument(EPSOSDocumentMetaData.EPSOSDOCUMENT_FORMAT_XML, id, patientId, effectiveDate, repositoryId, title, author);
    }
}
