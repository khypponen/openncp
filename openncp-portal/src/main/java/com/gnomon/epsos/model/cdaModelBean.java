package com.gnomon.epsos.model;

import com.gnomon.LiferayUtils;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.util.PortalUtil;
import epsos.ccd.gnomon.xslt.EpsosXSLTransformer;
import epsos.openncp.protocolterminator.ClientConnectorConsumer;
import epsos.openncp.protocolterminator.clientconnector.DocumentId;
import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.GenericDocumentCode;
import eu.epsos.util.IheConstants;
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
public class cdaModelBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger("cdaModelBean");
    private String cda;

    public cdaModelBean() {
        log.info("CDA MODEL BEAN requesting");
        cda = null;
//	cda=getCDA();
    }

    public String getCDA(PatientDocument document) {

        try {
            EpsosDocument selectedEpsosDocument = new EpsosDocument();
            String serviceUrl = EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);
            ClientConnectorConsumer clientConectorConsumer = new ClientConnectorConsumer(
                    serviceUrl);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            PortletRequest portletRequest = (PortletRequest) externalContext
                    .getRequest();
            HttpServletRequest req = PortalUtil.getHttpServletRequest(portletRequest);

            Assertion hcpAssertion = (Assertion) LiferayUtils.getFromSession("hcpAssertion");
            Assertion trcAssertion = (Assertion) LiferayUtils.getFromSession("trcAssertion");
            String selectedCountry = (String) LiferayUtils.getFromSession("selectedCountry");

            String uuid = document.getUuid();//portletRequest.getParameter("uuid");
            String repositoryId = document.getRepositoryId();//portletRequest.getParameter("repositoryid");
            String hcid = document.getHcid();//portletRequest.getParameter("hcid");
//		String exportType = ParamUtil.getString(portletRequest, "exportType");

            log.info("Retrieving CDA document");
            log.info("uuid: " + uuid);
            log.info("repositoryId: " + repositoryId);
            log.info("hcid: " + hcid);
            DocumentId documentId = DocumentId.Factory.newInstance();
            documentId.setDocumentUniqueId(uuid);
            documentId.setRepositoryUniqueId(repositoryId);

            GenericDocumentCode classCode = GenericDocumentCode.Factory.newInstance();
            String docType = document.getDocType(); //portletRequest.getParameter("docType");
            if (docType.equals("ep")) {
                classCode.setNodeRepresentation(Constants.EP_CLASSCODE);
                classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
                classCode.setValue(Constants.EP_TITLE);
            }
            if (docType.equals("ps")) {
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

            String xmlfile = new String(eps.getBase64Binary(), "UTF-8");
            EpsosXSLTransformer xlsClass = new EpsosXSLTransformer();
            String lang1 = LiferayUtils.getPortalLanguage().replace("_", "-");
            String actionURL = "someurl";
            cda = xlsClass.transform(xmlfile, lang1, actionURL);

        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ExceptionUtils.getStackTrace(ex));
        }

        return "cda";
    }

    public String getCda() {
        return cda;
    }

    public void setCda(String cda) {
        this.cda = cda;
    }

}
