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

package se.sb.epsos.web.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.BreadCrumbVO;
import se.sb.epsos.web.model.Dispensation;
import se.sb.epsos.web.model.DispensationRow;
import se.sb.epsos.web.model.Ingredient;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.service.DocumentCache;
import se.sb.epsos.web.service.DocumentClientDtoCacheKey;
import se.sb.epsos.web.service.PersonCache;
import se.sb.epsos.web.service.PersonCacheKey;

import java.text.DateFormat;
import java.util.Date;

public class ViewDispensationPage extends WebPage {
	public ViewDispensationPage(PageParameters parameters) {
		super();
        if(parameters.containsKey("printDisp")){
            ((EpsosAuthenticatedWebSession) getSession()).addToBreadCrumbList(new BreadCrumbVO<ViewDispensationPage>(getString("viewDispensationPage.print")));
        }
        else{
            ((EpsosAuthenticatedWebSession) getSession()).addToBreadCrumbList(new BreadCrumbVO<ViewDispensationPage>(getString("viewDispensationPage.title")));
        }
        PersonCacheKey key = new PersonCacheKey(getSession().getId(), parameters.getString("personId"));
		final Person person = PersonCache.getInstance().get(key);

		setDefaultModel(new CompoundPropertyModel<Person>(person));   
		add(new Label("id"));
        add(new Label("fullname"));
        add(new Label("country"));
        add(new Label("birthdate"));
        add(new Label("gender"));
		
		AuthenticatedUser user = ((EpsosAuthenticatedWebSession)getSession()).getUserDetails();
		add(new Label("commonName", user.getCommonName()));
		add(new Label("organizationName", user.getOrganizationName()));
		add(new Label("organizationId", user.getOrganizationId()));
		add(new Label("telecom", user.getTelecom()));
		add(new Label("streetaddress", user.getStreet()));
		add(new Label("city", user.getCity()));
		add(new Label("postalCode", user.getPostalCode()));
		
		DocumentClientDtoCacheKey docKey = new DocumentClientDtoCacheKey(getSession().getId(), parameters.getString("personId"), parameters.getString("id"));
		Dispensation dispensation = (Dispensation) DocumentCache.getInstance().get(docKey);
		ListView<DispensationRow> dispenseRows = new ListView<DispensationRow>("dispenseRows", dispensation.getRows()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<DispensationRow> item) {
				item.setDefaultModel(new CompoundPropertyModel<DispensationRow>(item.getModelObject()));

				ListView<Ingredient> ingredientRow = new ListView<Ingredient>("ingredientRow", item.getModelObject().getPrescriptionRow().getIngredient()) {
					private static final long serialVersionUID = -6772752979591906276L;

					@Override
					protected void populateItem(ListItem<Ingredient> item) {
						item.setDefaultModel(new CompoundPropertyModel<Ingredient>(item.getModelObject()));
						
						item.add(new Label("activeIngredient"));
						item.add(new Label("strength"));
					}
				};
				item.add(ingredientRow);
				
				item.add(new Label("prescriptionRow.description"));
				item.add(new Label("prescriptionRow.packageSize.quantityValue"));
				item.add(new Label("prescriptionRow.packageSize.quantityUnit"));
				item.add(new Label("prescriptionRow.nbrPackages.quantityValue"));
				item.add(new Label("prescriptionRow.nbrPackages.quantityUnit"));
				item.add(new Label("prescriptionRow.formName"));

				item.add(new Label("prescriptionRow.typeOfPackage"));
				item.add(new Label("prescriptionRow.startDate"));
				item.add(new Label("prescriptionRow.endDate"));
				item.add(new Label("prescriptionRow.frequency"));
				item.add(new Label("prescriptionRow.dosage"));

				item.add(new Label("prescriptionRow.productName"));
				item.add(new Label("prescriptionRow.route"));
				item.add(new Label("prescriptionRow.productId"));
				item.add(new Label("prescriptionRow.prescriptionId"));
				
				item.add(new Label("prescriptionRow.substitutionPermittedText"));
				                    
                Label labelPatientInstructions = new Label("prescriptionRow.patientInstructions");
                labelPatientInstructions.setEscapeModelStrings(false);
                item.add(labelPatientInstructions);
                
                Label labelPharmacistInstructions = new Label("prescriptionRow.pharmacistInstructions");
                labelPharmacistInstructions.setEscapeModelStrings(false);
                item.add(labelPharmacistInstructions);
				item.add(new Label("productName"));
				item.add(new Label("productId"));
				item.add(new Label("packageSize.quantityValue"));
				item.add(new Label("packageSize.quantityUnit"));
				item.add(new Label("nbrPackages.quantityValue"));
				item.add(new Label("nbrPackages.quantityUnit"));
				item.add(new Label("dispensationDate"));
			}
		};
		add(dispenseRows);
	}
}
