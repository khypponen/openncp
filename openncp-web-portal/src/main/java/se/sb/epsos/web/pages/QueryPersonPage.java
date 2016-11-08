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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.BasePage;
import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.BreadCrumbVO;
import se.sb.epsos.web.model.CountryVO;
import se.sb.epsos.web.model.LinkAction;
import se.sb.epsos.web.model.LoadablePersonModel;
import se.sb.epsos.web.model.PatientIdVO;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.QueryPerson;
import se.sb.epsos.web.service.CountryConfigManager;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.util.AbstractListViewPanel;
import se.sb.epsos.web.util.ActionLinkPanel;
import se.sb.epsos.web.util.CustomDataTableCurrentPageMetaDataKey;
import se.sb.epsos.web.util.EpsosWebConstants;
import se.sb.epsos.web.util.Feature;
import se.sb.epsos.web.util.FeatureFlagsManager;
import se.sb.epsos.web.util.SessionUtils;


@AuthorizeInstantiation({ "ROLE_PHARMACIST", "ROLE_DOCTOR", "ROLE_NURSE" })
public class QueryPersonPage extends BasePage {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryPersonPage.class);

	private List<LoadablePersonModel> personList;
	private DefaultDataTable<Person> datatablePersonList;

	private ModalWindow personInfoModalWindow;

	public QueryPersonPage() {
		final CompoundPropertyModel<QueryPerson> queryPerson = new CompoundPropertyModel<QueryPerson>(new QueryPerson());
		getSession().clearBreadCrumbList();
		getSession().addToBreadCrumbList(new BreadCrumbVO<QueryPersonPage>(getString("queryPersonPage.title"), this));
		add(new QueryPersonForm("form", queryPerson)).setOutputMarkupId(true);

		personInfoModalWindow = new ModalWindow("personInfoModalWindow");
		PersonInfoPanel personInfoPanel = new PersonInfoPanel(personInfoModalWindow.getContentId(), new CompoundPropertyModel<Person>(
				new LoadablePersonModel(new Person())));
		personInfoModalWindow.setContent(personInfoPanel);
		personInfoModalWindow.setCookieName("personInfoModalWindow");
		add(personInfoModalWindow);

		List<IColumn<Person>> columns = new ArrayList<IColumn<Person>>();

		columns.add(new PropertyColumn<Person>(new StringResourceModel("person.firstname", this, null), null, "firstname"));
		columns.add(new PropertyColumn<Person>(new StringResourceModel("person.lastname", this, null), null, "lastname"));
		PropertyColumn<Person> sexColumn = new PropertyColumn<Person>(new StringResourceModel("person.gender", this, null), null, "gender") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Person>> item, String componentId, IModel<Person> rowModel) {
				item.add(new Label(componentId, new StringResourceModel("person.gender." + rowModel.getObject().getGender(), QueryPersonPage.this, null)));
			}
		};

		columns.add(sexColumn);
		columns.add(new PropertyColumn<Person>(new StringResourceModel("person.birthdate", this, null), null, "birthdate"));
		columns.add(new PropertyColumn<Person>(new StringResourceModel("person.id", this, null), null, "id"));
		columns.add(new AbstractColumn<Person>(new Model<String>()) {
			private static final long serialVersionUID = 0L;

			public void populateItem(Item<ICellPopulator<Person>> item, String componentId, final IModel<Person> rowModel) {
				item.add(new SimpleAttributeModifier("style", "width: 10%"));
				item.add(new SimpleAttributeModifier("nowrap", "true"));
				List<LinkAction> actions = new ArrayList<LinkAction>();
				actions.add(new LinkAction(new StringResourceModel("person.actions.details", QueryPersonPage.this, null)) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Person person = rowModel.getObject();
						personInfoModalWindow.setTitle(person.getCommonName());
						personInfoModalWindow.setContent(new PersonInfoPanel(personInfoModalWindow.getContentId(), new CompoundPropertyModel<Person>(
								new LoadablePersonModel(person))));
						personInfoModalWindow.show(target);
					}

				});
				if (getSession().getUserDetails().isDoctor() || getSession().getUserDetails().isNurse() || getSession().getUserDetails().isAdmin()) {
					actions.add(new LinkAction(new StringResourceModel("person.actions.patientsummary", QueryPersonPage.this, null)) {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							final Person person = rowModel.getObject();
							LOGGER.info("EpsosId: " + person.getEpsosId());
							setResponsePage(new QueryDocumentsPage(new PageParameters("docType=PS,personId=" + person.getEpsosId())));
						}

					});
				}
				if (getSession().getUserDetails().isPharmaceut() || getSession().getUserDetails().isAdmin()) {
 					actions.add(new LinkAction(new StringResourceModel("person.actions.prescriptions", QueryPersonPage.this, null)) {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							Person person = rowModel.getObject();
							LOGGER.info("EpsosId: " + person.getEpsosId());
							setResponsePage(new QueryDocumentsPage(new PageParameters("docType=EP,personId=" + person.getEpsosId())));
						}

					});
				}
				item.add(new AbstractListViewPanel<LinkAction>(componentId, actions) {
					private static final long serialVersionUID = 1L;

					public void populateItem(String componentId, final ListItem<LinkAction> item) {
						item.add(new ActionLinkPanel<LinkAction>(componentId, new Model<LinkAction>(item.getModelObject())));
					}
				});
			}
		});

		SortableDataProvider<Person> provider = new SortableDataProvider<Person>() {
			private static final long serialVersionUID = 1L;

			public Iterator<Person> iterator(int start, int count) {
				if (personList.isEmpty()) {
					return Collections.<Person> emptyList().iterator();
				} else {
					List<Person> newList = new ArrayList<Person>();
					for (LoadablePersonModel p : personList.subList(start, start + count)) {
						newList.add(p.getObject());
					}
					return newList.iterator();
				}
			}

			public int size() {
				return personList != null ? personList.size() : 0;
			}

			public IModel<Person> model(Person person) {
				for (LoadablePersonModel model : personList) {
					if (model.getObject().equals(person)) {
						return model;
					}
				}
				return null;
			}
		};
		provider.setSort("id", true);
		datatablePersonList = new DefaultDataTable<Person>("dataTable", columns, provider, EpsosWebConstants.DATATABLE_DEFAULT_PAGE_SIZE);
		datatablePersonList.setOutputMarkupId(true);
		add(datatablePersonList);
	}

	@Override
	public void onPageAttached() {
		LOGGER.info("Clear current page information from session.");
		SessionUtils.setSessionMetaData(CustomDataTableCurrentPageMetaDataKey.KEY, -1);
	}
	
	public class QueryPersonForm extends Form<QueryPerson> {
		private static final long serialVersionUID = 1L;

		private DropDownChoice<CountryVO> country;
		private ListView<PatientIdVO> patientIds;

		public QueryPersonForm(String id, CompoundPropertyModel<QueryPerson> queryPerson) {
			super(id, queryPerson);	
			List<CountryVO> countries = CountryConfigManager.getCountries();
                        for(CountryVO country: countries){
                            country.setName(getString("country."+country.getId()));
                        }
			ChoiceRenderer<CountryVO> countryRenderer = new ChoiceRenderer<CountryVO>("name", "id");
			
			country = new DropDownChoice<CountryVO>("country", countries, countryRenderer);
			country.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 9036401857071497613L;

				@Override
				protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
					clearErrorList(ajaxRequestTarget);
					if (personList != null) {
						personList.clear();
					}

					QueryPerson query = (QueryPerson) QueryPersonForm.this.getDefaultModelObject();
					CountryVO selectedCountry = query.getCountry();
					List<PatientIdVO> countryPatientIds = CountryConfigManager.getPatientIdentifiers(selectedCountry);
					query.setPatientIds(countryPatientIds);
					String info = CountryConfigManager.getText(selectedCountry);
					if(info == null)
						info = "";
					if (FeatureFlagsManager.check(Feature.SHOW_HELP_TEXT_FOR_TEST)) {
						info = info + "<br/>" + CountryConfigManager.getHelpTextForTest(selectedCountry);
					}
					query.setHelpLabel(getString("queryperson.help"));
					query.setHelpLink(CountryConfigManager.getIdentificationHelpLink(selectedCountry));
					query.setCountryInfo(info);
					
					// TODO - different external link for different country, maybe put in countryconfig. Where is this information?
					ajaxRequestTarget.addComponent(getFeedback());
					ajaxRequestTarget.addComponent(QueryPersonForm.this);
					ajaxRequestTarget.addComponent(datatablePersonList);
				}

				@Override
				protected void onError(AjaxRequestTarget target, RuntimeException e) {
					clearErrorList(target);
					target.addComponent(getFeedback());
				}
				
			});
			country.setRequired(true);
            country.setMarkupId("country");
			add(country);
			patientIds = new ListView<PatientIdVO>("patientIds") {
				private static final long serialVersionUID = 2849670383290897403L;

				@Override
				protected void populateItem(ListItem<PatientIdVO> patientIdVOListItem) {
					PatientIdVO id = patientIdVOListItem.getModelObject();
					patientIdVOListItem.add(new Label("idLabel", getString(id.getLabel(), null, id.getLabel())));
					TextField<String> idTextField = new TextField<String>("idTextField", new PropertyModel<String>(id, "value"));
					idTextField.setLabel(new ResourceModel(id.getLabel(), id.getLabel()));
					if (id.getMin() != null && id.getMax() != null) {
						idTextField.add(StringValidator.lengthBetween(id.getMin(), id.getMax()));
						idTextField.setRequired(true);
					}
					patientIdVOListItem.add(idTextField);
				}
			};
			add(patientIds);
			add(new Label("countryInfo").setEscapeModelStrings(false));
			ExternalLink link = new ExternalLink("helpLink", queryPerson.getObject().getHelpLink()) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					String helpLink = ((QueryPerson) getParent().getDefaultModelObject()).getHelpLink();
					if (helpLink == null || helpLink.isEmpty())
						return false;
					else return true;
				}
			};
			link.add(new Label("helpLabel").setEscapeModelStrings(false));
			add(link).setOutputMarkupId(true);
			AjaxButton searchButton = new AjaxButton("submit") {
				private static final long serialVersionUID = 6559902058605309072L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    clearErrorList(target);
					AuthenticatedUser userDetails = ((EpsosAuthenticatedWebSession) getSession()).getUserDetails();
					CountryVO countryVO = ((QueryPerson) form.getDefaultModelObject()).getCountry();
					List<PatientIdVO> patientList = ((QueryPerson) form.getDefaultModelObject()).getPatientIds();

					if (personList != null) {
						personList.clear();
					}

					try {
						LOGGER.info("Country: " + countryVO.getId());
						List<Person> qResult = getServiceFacade().queryForPatient(userDetails, patientList, countryVO);
						personList = wrapPersonListIntoLoadableModel(qResult);
					} catch (NcpServiceException e) {
						if (e.isCausedByLoginRequired()) {
							getSession().invalidate();
							throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
						}
						if(e.isKnownEpsosError()){
							handleKnownNcpExceptionAfterAjax(e, getLocalizer().getString("error.ncpservice.query", this), target);
						} else {
							error(getLocalizer().getString("error.ncpservice.query", this) + ": " + e.getMessage());							
						}
						LOGGER.error("Failed to query for patient, " + e.getMessage(), e);
					}
					
					target.addComponent(getFeedback());
					target.addComponent(datatablePersonList);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					clearErrorList(target);
					target.addComponent(getFeedback());
				}

			};
            searchButton.setMarkupId("submit");
            add(searchButton);
		}

		private List<LoadablePersonModel> wrapPersonListIntoLoadableModel(List<Person> qResult) {
			LOGGER.debug("Wrapping person List into LoadableModel, size=" + (qResult != null ? qResult.size() : 0));
			List<LoadablePersonModel> newList = new ArrayList<LoadablePersonModel>();
			for (Person p : qResult) {
				newList.add(new LoadablePersonModel(p));
			}
			return newList;
		}
	}
}
