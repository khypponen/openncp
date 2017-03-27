package com.gnomon.epsos.servlet;

import com.gnomon.epsos.model.EpsosDocument;
import com.gnomon.epsos.model.cda.Utils;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.xslt.EpsosXSLTransformer;
import epsos.openncp.protocolterminator.ClientConnectorConsumer;
import epsos.openncp.protocolterminator.clientconnector.DocumentId;
import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.GenericDocumentCode;
import eu.epsos.util.EvidenceUtils;
import eu.epsos.util.IheConstants;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import tr.com.srdc.epsos.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

public class CCDServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(CCDServlet.class.getName());

    public void doGet(HttpServletRequest req,
                      HttpServletResponse res)
            throws ServletException, IOException {

        String exportType = ParamUtil.getString(req, "exportType");

        String cda = null;
        log.info("getting html document");
        try {
            String uuid = req.getParameter("uuid");
            String repositoryId = req.getParameter("repositoryid");
            String hcid = req.getParameter("hcid");

            log.debug("Retrieving XML document");
            log.debug("uuid: " + uuid);
            log.debug("repositoryId: " + repositoryId);
            log.debug("hcid: " + hcid);

            EpsosDocument selectedEpsosDocument = new EpsosDocument();
            String serviceUrl = EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);
            ClientConnectorConsumer clientConectorConsumer = new ClientConnectorConsumer(
                    serviceUrl);

            HttpSession session = req.getSession();

            Assertion hcpAssertion = (Assertion) session.getAttribute("hcpAssertion");
            Assertion trcAssertion = (Assertion) session.getAttribute("trcAssertion");
            String selectedCountry = (String) session.getAttribute("selectedCountry");
            User user = (User) session.getAttribute("user");

            DocumentId documentId = DocumentId.Factory.newInstance();
            documentId.setDocumentUniqueId(uuid);
            documentId.setRepositoryUniqueId(repositoryId);

            GenericDocumentCode classCode = GenericDocumentCode.Factory.newInstance();
            String docType = req.getParameter("docType");
            log.debug("Document : " + uuid + " is " + docType);
            if (docType.equals("ps")) {
                classCode.setNodeRepresentation(Constants.PS_CLASSCODE);
                classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
                classCode.setValue(Constants.PS_TITLE);
            }

            log.debug("selectedCountry: " + selectedCountry);
            log.debug("classCode: " + classCode);

            String lang = user.getLanguageId();
            String ltrlang = ParamUtil.getString(req, "lang");
            if (Validator.isNull(ltrlang)) {
                lang = user.getLanguageId();
            } else {
                lang = ltrlang;
            }

            String lang1 = lang.replace("_", "-");
            lang1 = lang1.replace("en-US", "en");

            log.info("Portal language is : " + lang + " - " + lang1);

            try {
                EvidenceUtils.createEvidenceREMNRO(classCode.toString(),
                        "NI_DR_" + classCode.getValue(),
                        new DateTime(),
                        EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                        "NI_DR_" + classCode.getValue() + "_REQ",
                        trcAssertion.getID());
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }

            EpsosDocument1 eps = clientConectorConsumer.retrieveDocument(
                    hcpAssertion, trcAssertion, selectedCountry, documentId,
                    hcid, classCode, lang1);

            if (Validator.isNotNull(eps)) {
                try {
                    EvidenceUtils.createEvidenceREMNRR(classCode.toString(),
                            "NI_DR" + classCode.getValue(),
                            new DateTime(),
                            EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                            "NI_DR_" + classCode.getValue() + "_RES_SUCC",
                            trcAssertion.getID());
                } catch (Exception e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }
            } else {
                try {
                    EvidenceUtils.createEvidenceREMNRR(classCode.toString(),
                            "NI_DR" + classCode.getValue(),
                            new DateTime(),
                            EventOutcomeIndicator.TEMPORAL_FAILURE.getCode().toString(),
                            "NI_DR_" + classCode.getValue() + "_RES_FAIL",
                            trcAssertion.getID());
                } catch (Exception e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }
            }

            selectedEpsosDocument.setAuthor(eps.getAuthor() + "");
            selectedEpsosDocument.setCreationDate(eps.getCreationDate());
            selectedEpsosDocument.setDescription(eps.getDescription());
            selectedEpsosDocument.setTitle(eps.getTitle());

            String xmlfile = new String(eps.getBase64Binary(), "UTF-8");
            log.info("#### CDA XML Start");
            log.debug(xmlfile);
            log.info("#### CDA XML End");

            final File cdaFile;
            final InputStream cdaInputStream;
            final ByteArrayOutputStream cdaOutputStream;

            boolean isCDA = false;
            try {
                Document doc1 = Utils.createDomFromString(xmlfile);
                isCDA = EpsosHelperService.isCDA(doc1);
                log.info("### Document created");
                log.info("########## IS CDA" + isCDA);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }

//            xmlfile = Utils.getDocumentAsXml(
//                    EpsosHelperService.translateDoc(
//                            Utils.createDomFromString(xmlfile), lang1));
            // Transform to CCD
            String mayoTransformed = "";
            EpsosXSLTransformer xlsClass = new EpsosXSLTransformer();

            if (isCDA) {
                log.info("########### Styling the document that is CDA: " + isCDA + " using standard xsl");
                cda = xlsClass.transformUsingStandardCDAXsl(mayoTransformed);
            } else {
//                transformer.ccdaToEpsos(cdaInputStream, cdaOutputStream, TrilliumBridgeTransformer.Format.XML,null);
//                mayoTransformed = new String(cdaOutputStream.toByteArray());
                log.info("########### Styling the document that is CDA: " + isCDA + " using EPSOS xsl");
                mayoTransformed = xmlfile;
                cda = xlsClass.transform(mayoTransformed, lang1, "");
            }

            // Visualize as HTML using standard stylesheet
            log.info("EXPORT TYPE: " + exportType);
            byte[] output = null;
            if (exportType.equals("xml")) {
                output = mayoTransformed.getBytes();
            } else {
                output = cda.getBytes();
            }
            ByteArrayOutputStream baos = null;
            if (exportType.equals("xml")) {
                res.setHeader("Content-Disposition", "attachment; filename=cda.xml");
                res.setContentType("text/xml");
            } else {
                res.setContentType("text/html");
            }

            res.setHeader("Cache-Control", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setHeader("Pragma", "No-cache");

            OutputStream OutStream = res.getOutputStream();
            OutStream.write(output);
            OutStream.flush();
            OutStream.close();

        } catch (Exception ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            res.setContentType("text/html");
            res.setHeader("Cache-Control", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setHeader("Pragma", "No-cache");

            OutputStream OutStream = res.getOutputStream();
            OutStream.write(ex.getMessage().getBytes());
            OutStream.flush();
            OutStream.close();
            log.error(ExceptionUtils.getStackTrace(ex));
        }
    }
}
