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

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import se.sb.epsos.web.model.Dispensation;
import se.sb.epsos.web.model.LinkAction;
import se.sb.epsos.web.service.DocumentCache;
import se.sb.epsos.web.service.DocumentClientDtoCacheKey;
import se.sb.epsos.web.util.ActionLinkPanel;

public class DispensePanel extends Panel {

    private static final long serialVersionUID = 3404932175822617286L;

    public DispensePanel(String id, final PageParameters parameters) {
        super(id);
        add(new Label("info", new StringResourceModel("dispensation.modelwindow.info", null)));
        PopupSettings settings = new PopupSettings();
        settings.setWidth(975);
        LinkAction printPrescriptionAction = new LinkAction(new StringResourceModel("dispensation.modelwindow.print.dispensation",
                DispensePanel.this, null), false, null) {

            private static final long serialVersionUID = 1296758447063239399L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                DocumentClientDtoCacheKey docKey = new DocumentClientDtoCacheKey(getSession().getId(), parameters.getString("personId"), parameters.getString("id"));
                Dispensation dispensation = (Dispensation) DocumentCache.getInstance().get(docKey);
                parameters.put("printDisp", null);
                setResponsePage(new PrintPage(ViewDispensationPage.class, parameters, dispensation.getPrescription()));
            }
        };
        LinkAction backToQueryDocuments = new LinkAction(new StringResourceModel("dispensation.modelwindow.back",
                DispensePanel.this, null), false, null) {
            private static final long serialVersionUID = 1296758447063239399L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                ((DispensePrescriptionPage)getWebPage()).stepBackToPage(QueryDocumentsPage.class);
            }
        };
        add(new ActionLinkPanel<LinkAction>("printPrescriptionAction", new Model<LinkAction>(printPrescriptionAction)));
        add(new ActionLinkPanel<LinkAction>("backToQueryDocuments", new Model<LinkAction>(backToQueryDocuments)));
    }
}
