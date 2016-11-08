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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.BasePage;
import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.model.CdaDocument;
import se.sb.epsos.web.model.DocType;
import se.sb.epsos.web.util.Feature;
import se.sb.epsos.web.util.FeatureFlagsManager;
import se.sb.epsos.web.util.PageConverter;

@AuthorizeInstantiation({"ROLE_PHARMACIST", "ROLE_DOCTOR", "ROLE_NURSE"})
public class PrintPage extends BasePage {

    private static final long serialVersionUID = -6934124409535024312L;
    protected static Logger LOGGER = LoggerFactory.getLogger(PrintPage.class);

    public PrintPage(Class<? extends Page> cl) {
        this(cl, null, null);
    }
    private String backButtonText;

    public PrintPage(Class<? extends Page> cl, final PageParameters parameters, CdaDocument document) {
        String pageAsString = PageConverter.renderPageToString(cl, parameters);
        WebMarkupContainer body = new WebMarkupContainer("body", new Model<String>());
        body.add(new Label("document", pageAsString).setEscapeModelStrings(false));
        if (parameters != null && parameters.containsKey("print") && parameters.getAsBoolean("print")) {
            body.add(new AttributeModifier("onload", new Model<String>("window.print();return false")));
        }
        add(body);
        Form<Object> form = new Form<Object>("form");
        Boolean comesFromReportedDisp = ((EpsosAuthenticatedWebSession) getSession()).isTitleInBreadCrumbList(getString("viewDispensationPage.title"));
        if ("EP".equals(parameters.getString("docType")) && comesFromReportedDisp) {
            backButtonText = getString("dispensation.modelwindow.back");
        } else {
            backButtonText = getString("form.button.back");
            if (!document.getError().isEmpty() && FeatureFlagsManager.check(Feature.SHOW_PARTIALERRORMESSAGES)) {
            	displayListOfWarnings(document.getError());
            }
        }
        form.add(new AjaxButton("back", new PropertyModel<String>(this, "backButtonText")) {
            private static final long serialVersionUID = -3292332439866215153L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOGGER.info("Doctype: " + parameters.getString("docType") + " EP: " + DocType.EP);
                if ("EP".equals(parameters.getString("docType"))) {
                    if (((EpsosAuthenticatedWebSession) getSession()).isTitleInBreadCrumbList(getString("viewDispensationPage.title"))) {
                        stepBackToPage(QueryDocumentsPage.class);
                    }
                    else {
                        stepBackToPage(DispensePrescriptionPage.class);
                    }
                } else if ("PS".equals(parameters.getString("docType"))) {
                    stepBackToPage(QueryDocumentsPage.class);
                }
                target.addComponent(getFeedback());
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.addComponent(getFeedback());
            }
        });

        add(form);
    }
}
