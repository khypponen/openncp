/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.pages;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.model.BreadCrumbVO;
import se.sb.epsos.web.model.CdaDocument;
import se.sb.epsos.web.model.LoadableDocumentModel;
import se.sb.epsos.web.service.DocumentCache;
import se.sb.epsos.web.service.DocumentClientDtoCacheKey;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.NcpServiceException;

import com.gnomon.xslt.EpsosXSLTransformer;

@AuthorizeInstantiation({ "ROLE_PHARMACIST", "ROLE_DOCTOR", "ROLE_NURSE" })
public class ViewDocumentPage extends WebPage {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewDocumentPage.class);

	public ViewDocumentPage() {
		this(null);
	}
 
	public ViewDocumentPage(PageParameters parameters) {
		DocumentClientDtoCacheKey docKey = new DocumentClientDtoCacheKey(getSession().getId(), parameters.getString("personId"), parameters.getString("id"));
		MetaDocument metaDoc = DocumentCache.getInstance().get(docKey);	
                
		((EpsosAuthenticatedWebSession) getSession()).addToBreadCrumbList(new BreadCrumbVO<ViewDocumentPage>(getString("viewDocumentPage.title." + parameters.getString("docType"))));
		LoadableDocumentModel<MetaDocument> doc = new LoadableDocumentModel<MetaDocument>(metaDoc);
		LOGGER.debug("New page instance: " + this.getClass().getSimpleName());
		LOGGER.debug("Metadoc: " + doc.getObject());
		CdaDocument document = null;
		if (doc.getObject() instanceof CdaDocument) {
			document = (CdaDocument) doc.getObject();
		} else {
			try {
				document = ((EpsosAuthenticatedWebSession) getSession()).getServiceFacade().retrieveDocument(doc.getObject());
			} catch (NcpServiceException e) {
				if (e.isCausedByLoginRequired()) {
					getSession().invalidateNow();
					throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
				} 
				LOGGER.error("Failed to retreive document", e);
				error(doc.getObject().getType().name() + ".error.service");
			} 
		}

//		Starts Kostas stuff:
		String xmlfile = null;
		try {
			xmlfile = new String(document.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Failed to convert CDA bytes to string", e);
			error(doc.getObject().getType().name() + ".error.service");
		}

		String transformedResult = null;
		try {
			URL xmlResourceUrl = getClass().getClassLoader().getResource("displaytool/EpsosRepository");
			EpsosXSLTransformer xslTransformer = new EpsosXSLTransformer(xmlResourceUrl.getPath());
			transformedResult = xslTransformer.transform(xmlfile, "sv-SE", null, "css/style.css");
		} catch (Exception e) {
			LOGGER.error("Failed to transform CDA document to HTML", e);
			error("Xslt transformation failed: " + e.getMessage());
		}
//		End Kostas stuff
		
		add(new Label("document", transformedResult).setEscapeModelStrings(false));
	}
}
