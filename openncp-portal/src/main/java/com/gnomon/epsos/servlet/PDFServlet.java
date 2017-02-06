package com.gnomon.epsos.servlet;

import com.gnomon.epsos.model.EpsosDocument;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.model.User;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
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
import tr.com.srdc.epsos.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

public class PDFServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(PDFServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        byte[] pdf = null;

        log.debug("getting pdf document");

        try {
            String uuid = req.getParameter("uuid");
            String repositoryId = req.getParameter("repositoryid");
            String hcid = req.getParameter("hcid");

            log.debug("Retrieving PDF document");
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

            DocumentId documentId = DocumentId.Factory.newInstance();
            documentId.setDocumentUniqueId(uuid);
            documentId.setRepositoryUniqueId(repositoryId);

            GenericDocumentCode classCode = GenericDocumentCode.Factory.newInstance();
            String docType = req.getParameter("docType");

            if ("ep".equals(docType)) {
                classCode.setNodeRepresentation(Constants.EP_CLASSCODE);
                classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
                classCode.setValue(Constants.EP_TITLE);
            }

            if ("ps".equals(docType)) {
                classCode.setNodeRepresentation(Constants.PS_CLASSCODE);
                classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
                classCode.setValue(Constants.PS_TITLE);
            }

            if ("mro".equals(docType)) {
                classCode.setNodeRepresentation(Constants.MRO_CLASSCODE);
                classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
                classCode.setValue(Constants.MRO_TITLE);
            }

            User user = (User) session.getAttribute("user");
            String lang = user.getLanguageId();
            String ltrlang = ParamUtil.getString(req, "lang");

            if (Validator.isNull(ltrlang)) {
                lang = user.getLanguageId();
            } else {
                lang = ltrlang;
            }

            String lang1 = lang.replace("_", "-");
            lang1 = lang1.replace("en-US", "en");

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
            log.debug(xmlfile);

            log.debug("The requested XML-PDF file for " + uuid + "\n" + xmlfile);

            pdf = EpsosHelperService.extractPdfPartOfDocument(eps.getBase64Binary());

            res.setContentType("application/pdf");
            res.setHeader("Content-Disposition", "inline; filename=cdapdf.pdf");
            res.setHeader("Cache-Control", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setHeader("Pragma", "No-cache");
            log.info("##########3 Serve pdf file");
            OutputStream OutStream = res.getOutputStream();
            OutStream.write(pdf);
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
