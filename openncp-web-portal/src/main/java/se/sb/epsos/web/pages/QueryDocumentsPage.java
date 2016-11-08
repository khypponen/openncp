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
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.BasePage;
import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.BreadCrumbVO;
import se.sb.epsos.web.model.CdaDocument;
import se.sb.epsos.web.model.DocType;
import se.sb.epsos.web.model.LinkAction;
import se.sb.epsos.web.model.LoadableDocumentModel;
import se.sb.epsos.web.model.LoadablePersonModel;
import se.sb.epsos.web.model.PdfDocument;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.Prescription;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.NcpServiceConfigManager;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.PersonCache;
import se.sb.epsos.web.service.PersonCacheKey;
import se.sb.epsos.web.util.AbstractListViewPanel;
import se.sb.epsos.web.util.ActionLinkPanel;
import se.sb.epsos.web.util.CustomDataTable;
import se.sb.epsos.web.util.EpsosWebConstants;

@AuthorizeInstantiation({"ROLE_PHARMACIST", "ROLE_DOCTOR", "ROLE_NURSE"})
public class QueryDocumentsPage extends BasePage {
	protected static Logger LOGGER = LoggerFactory.getLogger(QueryDocumentsPage.class);
	
	private CustomDataTable<MetaDocument> dataTable = null;
	
    public QueryDocumentsPage() {
        this(new PageParameters("docType=" + DocType.EP.name() + "visible=" + false));
    }

    public QueryDocumentsPage(final PageParameters parameters) {
        super(parameters);
        final DocType docType = parameters.getAsEnum("docType", DocType.class);
        AuthenticatedUser userDetils = getSession().getUserDetails();
        LOGGER.info("Parameters: " + parameters);
        LOGGER.info("PersonId: " + parameters.getString("personId"));
        PersonCacheKey key = new PersonCacheKey(getSession().getId(), parameters.getString("personId"));
        final LoadablePersonModel personModel = new LoadablePersonModel(PersonCache.getInstance().get(key));
        if (!checkTrc(userDetils, personModel)) {
            throw new RestartResponseAtInterceptPageException(new TrcPage(parameters));
        }
        ((EpsosAuthenticatedWebSession) getSession()).addToBreadCrumbList(new BreadCrumbVO<QueryDocumentsPage>(getLocalizer().getString(docType.name() + ".pagetitle", this), this));
        
        add(new Label("title", getLocalizer().getString(docType.name() + ".pagetitle", this)));

        add(new PersonShortInfoPanel("personinfo", new CompoundPropertyModel<Person>(personModel)));

        List<IColumn<MetaDocument>> columns = new ArrayList<IColumn<MetaDocument>>();
        columns.add(new PropertyColumn<MetaDocument>(new StringResourceModel(docType.name() + ".title", this, null), null, "title"));
        columns.add(new PropertyColumn<MetaDocument>(new StringResourceModel(docType.name() + ".description", this, null), null, "description"));
        columns.add(new PropertyColumn<MetaDocument>(new StringResourceModel(docType.name() + ".author", this, null), null, "author"));
        columns.add(new PropertyColumn<MetaDocument>(new StringResourceModel(docType.name() + ".creationdate", this, null), null, "creationdate"));
        columns.add(new AbstractColumn<MetaDocument>(new Model<String>()) {
            private static final long serialVersionUID = 0L;

            public void populateItem(Item<ICellPopulator<MetaDocument>> item, String componentId, final IModel<MetaDocument> rowModel) {
            	item.add(new SimpleAttributeModifier("style", "width: 10%; text-align: right;"));
            	item.add(new SimpleAttributeModifier("nowrap", "true"));
            	List<LinkAction> actions = new ArrayList<LinkAction>();
            	if (docType.equals(DocType.PS)) {
            		LinkAction pdfAction = new LinkAction(new StringResourceModel("document.actions.pdf",
            				QueryDocumentsPage.this, null), true, null) {
            			private static final long serialVersionUID = 1L;
            			
            			@Override
            			public void onClick(AjaxRequestTarget target) {
            				((EpsosAuthenticatedWebSession)getSession()).removePostFromBreadCrumbList(getString("viewDocumentPage.title.PS"));
            				
            				PdfDocument pdfDoc = null;
            				try {
            					pdfDoc = (PdfDocument) getServiceFacade().retrieveDocument(
            							new MetaDocument(getSession().getId(),
            									personModel.getObject().getEpsosId(),
            									rowModel.getObject().getChildDocumentPdf()));
            					LOGGER.debug("PDF doc retrieved");
            					
            					setResponsePage(new PdfPage(new LoadableDocumentModel<PdfDocument>(pdfDoc), parameters));

            				} catch (NcpServiceException ex) {
            					LOGGER.error("Failed to retrieve PDF document:" + rowModel.getObject().getChildDocumentPdf().getUuid(), ex);
            					if(ex.isKnownEpsosError()){
            						handleKnownNcpExceptionAfterAjax(ex, getLocalizer().getString("PDF.error.service", QueryDocumentsPage.this), target);
            					}
            					else {
            						error(getLocalizer().getString("PDF.error.service", QueryDocumentsPage.this) + ": " + ex.getMessage());
            						target.addComponent(getFeedback());
            					}
            				}
            			}
            		};
            		actions.add(pdfAction);
            		actions.add(new LinkAction(new StringResourceModel("document.actions.view", QueryDocumentsPage.this, null), true, null) {
            			private static final long serialVersionUID = 1L;

            			@Override
            			public void onClick(AjaxRequestTarget target) {
            				((EpsosAuthenticatedWebSession)getSession()).removePostFromBreadCrumbList(getString("originalPdf.title"));
            				
            				parameters.add("id", rowModel.getObject().getDoc().getUuid());
            				try {
                            	/**
                            	 * Pre fetch the document. Give feedback on the current page if it is not retrievable.
                            	 */
                				CdaDocument cda = ((EpsosAuthenticatedWebSession) getSession()).getServiceFacade().retrieveDocument(rowModel.getObject());
                				setResponsePage(new PrintPage(ViewDocumentPage.class, parameters, cda));
                			} catch (NcpServiceException e) {
                				if (e.isCausedByLoginRequired()) {
                					getSession().invalidateNow();
                					throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
                				}
                				else if(e.isKnownEpsosError()){
                					handleKnownNcpExceptionAfterAjax(e, getString("PS.error.service"), target);
                				}
                				else{
                					error(rowModel.getObject().getType().name() + ".error.service");        					
                				}
                				LOGGER.error("Failed to retreive document", e);
                			} 
            			}
            		});
            	}
                if (docType.equals(DocType.EP)) {
                    actions.add(new LinkAction(new StringResourceModel("document.actions.dispense", QueryDocumentsPage.this, null)) {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onClick(AjaxRequestTarget target) {
                        	MetaDocument doc = rowModel.getObject();
                        	try {
                                Prescription prescription = (Prescription) getServiceFacade().retrieveDocument(doc);
                                LOGGER.debug("****FD:" + prescription.toString());
                                parameters.put("prescription",  new LoadableDocumentModel<Prescription>(prescription));
                                if(dataTable != null) {
                                    dataTable.saveCurrentPage();
                                }
                                setResponsePage(new DispensePrescriptionPage(parameters));
                            } catch (NcpServiceException e) {
                                if (e.isCausedByLoginRequired()) {
                                    getSession().invalidateNow();
                                    throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
                                } else if (e.isKnownEpsosError()){
                                	handleKnownNcpExceptionAfterAjax(e, getString(doc.getType().name() + ".error.service"), target);
                                } else {
                    	            error(getString(doc.getType().name() + ".error.service") + ": " + e.getMessage());
                    	            target.addComponent(getFeedback());
                    	        }
                            } 
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
        List<LoadableDocumentModel<MetaDocument>> wrappedDocumentList = null;
        try {
        	List<MetaDocument> documentList = getServiceFacade().queryDocuments(personModel.getObject(), docType.name(), getSession().getUserDetails());
        	wrappedDocumentList = wrapDocumentListIntoLoadableModel(documentList);
        } catch (NcpServiceException e) {
            if (e.isCausedByLoginRequired()) {
            	error(getString(docType.name() + ".error.service") + ": " + e.getMessage());
                getSession().invalidateNow();
                throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
            }

            // Handle known and unknown errors, because user needs to notify that error occurred.
            // The actual error shall not be shown to user, because of security reasons, but a more
            // general error.epsos.unknown is shown.
            handleKnownNcpException(e, getString(docType.name() + ".error.service"));
            LOGGER.error("Failed to query for documents", e);
        }
        
        SortableDataProvider<MetaDocument> provider = new SortableMetaDocumentProvider(wrappedDocumentList);
        provider.setSort("description", true);
        dataTable = new CustomDataTable<MetaDocument>("dataTable", columns, provider, getDocumentsDatatablePageSize());
        dataTable.setOutputMarkupId(true);
        dataTable.restoreCurrentPage();
        add(dataTable);
    }

    private int getDocumentsDatatablePageSize() {
    	int value = EpsosWebConstants.DATATABLE_DEFAULT_PAGE_SIZE;

    	try {
	    	String sValue = NcpServiceConfigManager.getDocumentsDatatablePageSize();
	    	value = Integer.parseInt(sValue);
    	} catch(Exception e) {
    		LOGGER.warn("Failed to fetch documentsDataTablePageSize from database. Using default value.");
    	}

    	return value;
    }
    
	private boolean checkTrc(AuthenticatedUser userDetils, final LoadablePersonModel personModel) {
        return userDetils.getTrc() != null && userDetils.getTrc().getPerson().equals(personModel.getObject()) && userDetils.getTrc().isConfirmed();
    }

    private List<LoadableDocumentModel<MetaDocument>> wrapDocumentListIntoLoadableModel(List<MetaDocument> documentList) {
        LOGGER.debug("Wrapping docList into LoadableModel, size="+documentList.size());
        List<LoadableDocumentModel<MetaDocument>> newList = new ArrayList<LoadableDocumentModel<MetaDocument>>();
        for(MetaDocument doc : documentList) {
        	if (doc.getDoc().getFormatCode().getNodeRepresentation().equals("urn:epSOS:ep:pre:2010") || 
        			doc.getDoc().getFormatCode().getNodeRepresentation().equals("urn:epSOS:ps:ps:2010")) {
        		newList.add(new LoadableDocumentModel<MetaDocument>(doc));
        	}
        }
        return newList;
    }
}
