package com.gnomon.epsos.model;

import com.gnomon.LiferayUtils;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.util.PortalUtil;
import epsos.openncp.protocolterminator.ClientConnectorConsumer;
import epsos.openncp.protocolterminator.clientconnector.DocumentId;
import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.GenericDocumentCode;
import eu.epsos.util.IheConstants;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.util.Constants;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@ManagedBean
@RequestScoped
public class pdfModelBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger("pdfModelBean");
    private byte[] pdf;

    public pdfModelBean() {
        pdf = getCDA();
    }

    public byte[] getCDA() {

        byte[] pdf = null;
        log.info("getting pdf document");

        try {
            EpsosDocument selectedEpsosDocument = new EpsosDocument();
            String serviceUrl = EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);
            ClientConnectorConsumer clientConectorConsumer = new ClientConnectorConsumer(serviceUrl);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            PortletRequest portletRequest = (PortletRequest) externalContext
                    .getRequest();
            HttpServletRequest req = PortalUtil.getHttpServletRequest(portletRequest);

            Assertion hcpAssertion = (Assertion) LiferayUtils.getFromSession("hcpAssertion");
            Assertion trcAssertion = (Assertion) LiferayUtils.getFromSession("trcAssertion");
            String selectedCountry = (String) LiferayUtils.getFromSession("selectedCountry");

            String uuid = portletRequest.getParameter("uuid");
            String repositoryId = portletRequest.getParameter("repositoryid");
            String hcid = portletRequest.getParameter("hcid");

            log.info("Retrieving PDF document");
            log.info("uuid: '{}'", uuid);
            log.info("repositoryId: '{}'", repositoryId);
            log.info("hcid: '{}'", hcid);

            DocumentId documentId = DocumentId.Factory.newInstance();
            documentId.setDocumentUniqueId(uuid);
            documentId.setRepositoryUniqueId(repositoryId);

            GenericDocumentCode classCode = GenericDocumentCode.Factory.newInstance();
            String docType = portletRequest.getParameter("docType");
            if (StringUtils.equalsIgnoreCase(docType, "ep")) {
                classCode.setNodeRepresentation(Constants.EP_CLASSCODE);
                classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
                classCode.setValue(Constants.EP_TITLE);
            }
            if (StringUtils.equalsIgnoreCase(docType, "ps")) {
                classCode.setNodeRepresentation(Constants.PS_CLASSCODE);
                classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
                classCode.setValue(Constants.PS_TITLE);
            }

            EpsosDocument1 eps = clientConectorConsumer.retrieveDocument(
                    hcpAssertion, trcAssertion, selectedCountry, documentId,
                    hcid, classCode);
            selectedEpsosDocument.setAuthor(eps.getAuthor() + "");
            selectedEpsosDocument.setCreationDate(eps.getCreationDate());
            selectedEpsosDocument.setDescription(eps.getDescription());
            selectedEpsosDocument.setTitle(eps.getTitle());

            String xmlfile = new String(eps.getBase64Binary(), CharEncoding.UTF_8);

            pdf = EpsosHelperService.extractPdfPartOfDocument(xmlfile.getBytes());
            log.info("PDF SIZE: '{}'", pdf.length);

        } catch (Exception ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }

        return pdf;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

}
