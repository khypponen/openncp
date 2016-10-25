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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.BasePage;
import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.BreadCrumbVO;
import se.sb.epsos.web.model.Dispensation;
import se.sb.epsos.web.model.DispensationRow;
import se.sb.epsos.web.model.Ingredient;
import se.sb.epsos.web.model.LinkAction;
import se.sb.epsos.web.model.LoadableDocumentModel;
import se.sb.epsos.web.model.LoadablePersonModel;
import se.sb.epsos.web.model.PdfDocument;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.Prescription;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.PersonCache;
import se.sb.epsos.web.service.PersonCacheKey;
import se.sb.epsos.web.util.ActionLinkPanel;
import se.sb.epsos.web.util.DispensationResource;
import se.sb.epsos.web.util.EpsosWebConstants;
import se.sb.epsos.web.util.Feature;
import se.sb.epsos.web.util.FeatureFlagsManager;
import se.sb.epsos.web.util.OpenOnLoadModalWindow;
import se.sb.epsos.web.util.PageConverter;

@AuthorizeInstantiation({"ROLE_PHARMACIST", "ROLE_DOCTOR", "ROLE_NURSE"})
public class DispensePrescriptionPage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispensePrescriptionPage.class);
    private ModalWindow dispenseModalWindow = new ModalWindow("dispenseModalWindow"){
		private static final long serialVersionUID = -861061699044803964L;

		@Override
        public void close(AjaxRequestTarget target) {
            super.close(target);
            stepBackToPage(QueryDocumentsPage.class);
        }
    };

    public DispensePrescriptionPage(PageParameters parameters) {
    	
    	//this prevents the dialog to ask user a confirmation when moving to print prescription
    	dispenseModalWindow.add(new AbstractBehavior() {
            @Override
            public void renderHead(IHeaderResponse response) {
                response.renderOnLoadJavascript("Wicket.Window.unloadConfirmation = false;");
            }
        });
    	
        add(dispenseModalWindow);
        LOGGER.error("PARAMETERS: " + parameters.toString());
        LOGGER.debug("Creating new instance of: " + this.getClass().getSimpleName());
        ((EpsosAuthenticatedWebSession) getSession()).addToBreadCrumbList(new BreadCrumbVO<DispensePrescriptionPage>(getString("dispensePrescription.title"), this));
        PersonCacheKey key = new PersonCacheKey(getSession().getId(), parameters.getString("personId"));
        Person person = PersonCache.getInstance().get(key);
        String personId = person.getEpsosId();
        add(new PersonShortInfoPanel("personinfo", new CompoundPropertyModel<Person>(new LoadablePersonModel(person))));
        LoadableDocumentModel<Prescription> prescription = (LoadableDocumentModel<Prescription>) parameters.get("prescription");
        if (!prescription.getObject().getError().isEmpty() && FeatureFlagsManager.check(Feature.SHOW_PARTIALERRORMESSAGES)) {
            displayListOfWarnings(prescription.getObject().getError());
        }
        Dispensation disp = new Dispensation(getSession().getId(), personId, prescription.getObject());
        DispensationForm form = new DispensationForm("dispensationForm", new CompoundPropertyModel<Dispensation>(
                new LoadableDocumentModel<Dispensation>(disp)), new LoadablePersonModel(person), parameters);
        form.setOutputMarkupId(true);
        add(form);

        Form<Object> backForm = new Form<Object>("form");
        backForm.add(new AjaxButton("back") {

            private static final long serialVersionUID = -3292332439866215153L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                stepBackToPage(QueryDocumentsPage.class);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.addComponent(getFeedback());
            }
        });
        add(backForm);
    }

    public class DispensationForm extends Form<Dispensation> {

        private static final long serialVersionUID = 8130220498926453924L;
        private ModalWindow prescriberInfoModalWindow;
        private LoadablePersonModel personModel;

        public DispensationForm(String id, final IModel<Dispensation> model, final LoadablePersonModel personModel, final PageParameters parameters) {
            super(id, model);
            this.personModel = personModel;
            add(new Label("prescription.performer"));
            add(new Label("prescription.profession"));
            add(new Label("prescription.facility"));
            add(new Label("prescription.address"));
            add(new Label("prescription.createDate"));
            prescriberInfoModalWindow = new ModalWindow("prescriberInfoModalWindow");
            PrescriberInfoPanel personInfoPanel = new PrescriberInfoPanel(prescriberInfoModalWindow.getContentId(),
                    new CompoundPropertyModel<Prescription>(new LoadableDocumentModel<Prescription>(model.getObject().getPrescription())));
            prescriberInfoModalWindow.setTitle(getLocalizer().getString("dispensation.prescriberinfo", this));
            prescriberInfoModalWindow.setContent(personInfoPanel);
            prescriberInfoModalWindow.setCookieName("prescriberInfoModalWindow");
            add(prescriberInfoModalWindow);
            LinkAction moreInfoAction = new LinkAction(new StringResourceModel("dispensation.actions.prescriber.details",
                    DispensePrescriptionPage.this, null)) {

                private static final long serialVersionUID = 5875810644411109392L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    prescriberInfoModalWindow.show(target);
                }
            };
            add(new ActionLinkPanel<LinkAction>("prescriberMoreInfo", new Model<LinkAction>(moreInfoAction)));

            LinkAction showPrescriptionPdf = new LinkAction(new StringResourceModel("dispensation.actions.pdf", DispensePrescriptionPage.this, null),
                    true, null) {

                private static final long serialVersionUID = 1296758447063239399L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    ((EpsosAuthenticatedWebSession) getSession()).removePostFromBreadCrumbList(getString("viewDocumentPage.title.EP"));
                    PageParameters parameters = new PageParameters();
                    parameters.add("personId", personModel.getObject().getEpsosId());
                    parameters.add("id", model.getObject().getPrescription().getDoc().getUuid());
                    parameters.add("print", "true");
                    parameters.add("docType", "EP");
                    PdfDocument pdfDoc = null;
                    try {
                        pdfDoc = (PdfDocument) getServiceFacade().retrieveDocument(
                                new MetaDocument(getSession().getId(), personModel.getObject().getEpsosId(), model.getObject().getPrescription().getChildDocumentPdf()));
                        LOGGER.debug("PDF doc retrieved");
                        setResponsePage(new PdfPage(new LoadableDocumentModel<PdfDocument>(pdfDoc), parameters));

                    } catch (NcpServiceException ex) {
                        LOGGER.error("Failed to retrieve PDF document:" + model.getObject().getPrescription().getChildDocumentPdf().getUuid(), ex);
                        if (ex.isKnownEpsosError()) {
                            handleKnownNcpExceptionAfterAjax(ex, getLocalizer().getString("PDF.error.service", DispensePrescriptionPage.this), target);
                        } else {
                            error(getLocalizer().getString("PDF.error.service", DispensePrescriptionPage.this) + ": " + ex.getMessage());
                        }
                    }
                }
            };
            add(new ActionLinkPanel<LinkAction>("showPrescriptionPdfAction", new Model<LinkAction>(showPrescriptionPdf)));
//            ########## DisplayTool Print ##########  
//            LinkAction printPrescriptionAction = new LinkAction(new StringResourceModel("dispensation.actions.print.prescription",
//                    DispensePrescriptionPage.this, null), true, null) {
//
//                private static final long serialVersionUID = 1296758447063239399L;
//
//                @Override
//                public void onClick(AjaxRequestTarget target) {
//                    ((EpsosAuthenticatedWebSession) getSession()).removePostFromBreadCrumbList(getString("originalPdf.title"));
//                    PageParameters parameters = new PageParameters();
//                    parameters.add("personId", personModel.getObject().getEpsosId());
//                    parameters.add("id", model.getObject().getPrescription().getDoc().getUuid());
//                    parameters.add("print", "true");
//                    parameters.add("docType", "EP");
//                    try {
//                        /**
//                         * Pre fetch the document to print. Give feedback on the
//                         * current page if it is not retrievable.
//                         */
//                        CdaDocument cda = ((EpsosAuthenticatedWebSession) getSession()).getServiceFacade().retrieveDocument(model.getObject().getPrescription());
//                        setResponsePage(new PrintPage(ViewDocumentPage.class, parameters, cda));
//                    } catch (NcpServiceException e) {
//                        if (e.isCausedByLoginRequired()) {
//                            getSession().invalidateNow();
//                            throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
//                        } else if (e.isKnownEpsosError()) {
//                            handleKnownNcpExceptionAfterAjax(e, getString("EP.error.service"), target);
//                        } else {
//                            error(model.getObject().getType().name() + ".error.service");
//                        }
//                        LOGGER.error("Failed to retreive document", e);
//                    }
//                }
//            };
//            add(new ActionLinkPanel<LinkAction>("printPrescriptionAction", new Model<LinkAction>(printPrescriptionAction)));

            ListView<DispensationRow> dispenseRows = new ListView<DispensationRow>("dispenseRows", getModelObject().getRows()) {

                private static final long serialVersionUID = 1L;
                private TextField<DispensationRow> productName = new TextField<DispensationRow>("productName");
                private TextField productId = new TextField<DispensationRow>("productId");
                private TextField packageSize = new TextField<DispensationRow>("packageSize.quantityValue");
                private TextField nbrPackages = new TextField<DispensationRow>("nbrPackages.quantityValue");

                @Override
                protected void populateItem(final ListItem<DispensationRow> item) {
                    item.setDefaultModel(new CompoundPropertyModel<DispensationRow>(item.getModelObject()));

                    List<IColumn<Ingredient>> columns = new ArrayList<IColumn<Ingredient>>();
                    columns.add(new PropertyColumn<Ingredient>(new StringResourceModel("prescription.activeIngredient", this, null), null,
                            "activeIngredient"));
                    columns.add(new PropertyColumn<Ingredient>(new StringResourceModel("prescription.strength", this, null), null, "strength"));
                    SortableDataProvider<Ingredient> provider = new SortableDataProvider<Ingredient>() {

                        private static final long serialVersionUID = 1L;

                        public Iterator<Ingredient> iterator(int start, int count) {
                            if (item.getModelObject().getPrescriptionRow().getIngredient() == null
                                    || item.getModelObject().getPrescriptionRow().getIngredient().isEmpty()) {
                                return Collections.<Ingredient>emptyList().iterator();
                            } else {
                                return item.getModelObject().getPrescriptionRow().getIngredient().subList(start, start + count).iterator();
                            }
                        }

                        public int size() {
                            return item.getModelObject().getPrescriptionRow().getIngredient() != null ? item.getModelObject().getPrescriptionRow().getIngredient().size() : 0;
                        }

                        public IModel<Ingredient> model(Ingredient ingredient) {
                            return Model.of(ingredient);
                        }
                    };
                    provider.setSort("description", true);
                    item.add(new DefaultDataTable<Ingredient>("dataTable", columns, provider, EpsosWebConstants.DATATABLE_DEFAULT_PAGE_SIZE));

                    item.add(new Label("prescriptionRow.description"));
                    item.add(new Label("prescriptionRow.packageSize.quantityValue"));
                    item.add(new Label("prescriptionRow.packageSize.quantityUnit", item.getModelObject().getPrescriptionRow().getPackageSize().getQuantityUnit()));
                    item.add(new Label("prescriptionRow.nbrPackages.quantityValue"));
                    item.add(new Label("prescriptionRow.nbrPackages.quantityUnit", item.getModelObject().getPrescriptionRow().getNbrPackages().getQuantityUnit()));
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

                    item.getModelObject().setProductName(item.getModelObject().getPrescriptionRow().getProductName());
                    item.getModelObject().setProductId(item.getModelObject().getPrescriptionRow().getProductId());
                    item.getModelObject().setPackageSize(item.getModelObject().getPrescriptionRow().getPackageSize().clone());
                    item.getModelObject().setNbrPackages(item.getModelObject().getPrescriptionRow().getNbrPackages().clone());

                    item.add(new Label("packageSize.quantityUnit", item.getModelObject().getPrescriptionRow().getPackageSize().getQuantityUnit()));
                    item.add(new Label("nbrPackages.quantityUnit", item.getModelObject().getPrescriptionRow().getNbrPackages().getQuantityUnit()));
                    item.add(productName.setRequired(true));//.setEnabled(item.getModelObject().getPrescriptionRow().isSubstitutionPermitted()));
                    item.add(productId.setRequired(true).add(StringValidator.lengthBetween(1, 20)));
                    item.add(packageSize.setRequired(true).add(new AbstractValidator<String>() {

						@Override
						protected void onValidate(IValidatable<String> validatable) {
							validateInput(validatable, "dispensation.packageSize", EpsosWebConstants.PATTERN_INTEGER_AND_DOUBLE);
						}
                    }));
                    item.add(nbrPackages.setRequired(true).add(new AbstractValidator<String>() {

						@Override
						protected void onValidate(IValidatable<String> validatable) {
							validateInput(validatable, "dispensation.nbrPackages", EpsosWebConstants.PATTERN_INTEGER);
						}                 	
                    }));
                    item.add(new CheckBox("substitute").setEnabled(item.getModelObject().getPrescriptionRow().isSubstitutionPermitted()));

                    AjaxButton submit = new AjaxButton("submit") {
                        private static final long serialVersionUID = -8073839780380360791L;

                        @Override
                        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        	((EpsosAuthenticatedWebSession) getSession()).removePostFromBreadCrumbList(getString("viewDocumentPage.title.EP"));
                            ((EpsosAuthenticatedWebSession) getSession()).removePostFromBreadCrumbList(getString("originalPdf.title"));

                            item.getModelObject().setDispense(true);
                            AuthenticatedUser user = ((EpsosAuthenticatedWebSession) getSession()).getUserDetails();
                            MetaDocument doc = (MetaDocument) model.getObject();
                            
                            String dispensationKey = doc.getDoc().getUuid();
                            String eD_PageAsString = PageConverter.renderPageToString(ViewDispensationPage.class, new PageParameters("id="
                                    + dispensationKey + ",personId=" + personModel.getObject().getEpsosId()));
                            try {
                                final byte[] bytes = getServiceFacade().submitDocument(model.getObject(), user, personModel.getObject(),
                                        eD_PageAsString);
                                ResourceReference pdfReference = new ResourceReference(DispensePrescriptionPage.class, dispensationKey) {

                                    private static final long serialVersionUID = 3593575150143454033L;
                                    private DispensationResource dispResource = new DispensationResource();

                                    protected Resource newResource() {
                                        dispResource.setBytes(bytes);
                                        LOGGER.info("Bytes: " + bytes.toString());
                                        return dispResource;
                                    }
                                };
                                ((EpsosAuthenticatedWebSession) getSession()).disableBreadCrumbClickability(getString("dispensePrescription.title"));
                                target.addComponent(getBreadcrumbPanel());
                                String url = getRequestCycle().urlFor(pdfReference).toString();
                                getSession().info(getLocalizer().getString("dispensation.info.service", this));
                                PageParameters parameters = new PageParameters();
                                parameters.add("docType", "EP");
                                parameters.add("visible", "true");
                                parameters.add("print", "true");
                                parameters.add("popup", url);
                                parameters.add("personId", personModel.getObject().getEpsosId());
                                parameters.add("id", dispensationKey);

                                DispensePanel dispensePanel = new DispensePanel(dispenseModalWindow.getContentId(), parameters);

                                dispenseModalWindow.setVisible(true);
                                dispenseModalWindow.setTitle(getLocalizer().getString("dispensation.modelwindow.title", this));
                                dispenseModalWindow.setContent(dispensePanel);
                                dispenseModalWindow.setCookieName("dispenseModalWindow");
                                dispenseModalWindow.setInitialHeight(dispenseModalWindow.getMinimalHeight());
                                dispenseModalWindow.setInitialWidth(500);
                                dispenseModalWindow.show(target);
                                target.addComponent(dispenseModalWindow);
                            } catch (NcpServiceException e) {
                                if (e.isCausedByLoginRequired()) {
                                    getSession().invalidateNow();
                                    throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
                                } else if (e.isKnownEpsosError()) {
                                    handleKnownNcpExceptionAfterAjax(e, getLocalizer().getString("dispensation.error.service", this), target);
                                } else {
                                    error(getLocalizer().getString("dispensation.error.service", this) + ": " + e.getMessage());
                                }
                                LOGGER.error("Failed to submit dispensation", e);
                            }
                            target.addComponent(getFeedback());
                        }

                        @Override
                        protected void onError(AjaxRequestTarget target, Form<?> form) {
                            target.addComponent(getFeedback());
                        }
                    };
                                                            
                    submit.setMarkupId("dispenseButton");
                    item.add(submit);
                }
            };
            dispenseRows.setReuseItems(true);
            add(dispenseRows);
        }
        
        private void validateInput(IValidatable<String> validatable, String componentValue, String regexp) {
			String value = validatable.getValue();
			ValidationError error = new ValidationError();
			error.setVariable("value", getString(componentValue));
			error.setVariable("regexp", regexp);
			if (!value.matches(regexp)) {
				error.addMessageKey("format.notcorrect");
			} else if (value.equals("0")) {
				error.addMessageKey("value.zero");
			}
			
			if(!error.getKeys().isEmpty()) {
				validatable.error(error);
			}
		}
    }
}
