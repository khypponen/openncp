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
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import se.sb.epsos.web.BasePage;
import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.LoginPage;
import se.sb.epsos.web.model.LoadablePersonModel;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.TRC;
import se.sb.epsos.web.model.TRC.TrcPurpose;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.PersonCache;
import se.sb.epsos.web.service.PersonCacheKey;

@AuthorizeInstantiation({ "ROLE_PHARMACIST", "ROLE_DOCTOR", "ROLE_NURSE" })
public class TrcPage extends BasePage {
	private PersonShortInfoPanel personInfoPanel;
	private TRC trc;
	public TrcPage(PageParameters parameters) {
		PersonCacheKey key = new PersonCacheKey(getSession().getId(), parameters.getString("personId"));
		final Person person = PersonCache.getInstance().get(key);
		this.personInfoPanel = new PersonShortInfoPanel("personinfo", new CompoundPropertyModel<Person>(new LoadablePersonModel(person)));
		this.personInfoPanel.setOutputMarkupId(true);
		trc = new TRC(person, null);
		add(this.personInfoPanel);
		TrcForm trcForm = new TrcForm("trcForm", new CompoundPropertyModel<TRC>(trc), parameters);
		trcForm.setOutputMarkupId(true);
		add(trcForm);
		LOGGER.debug("Created new TrcPage");
	}

	public void onTrcChange(AjaxRequestTarget target) {
		target.addComponent(this.personInfoPanel);
	}

	public class TrcForm extends Form<TRC> {
		private static final long serialVersionUID = 1L;
		private PageParameters parametersToPassFurther;
		public TrcForm(String id, final IModel<TRC> model, PageParameters parameters) {
			super(id, model);
			parametersToPassFurther = parameters;
			//			ChoiceRenderer<TRC.TrcPurpose> purposeRenderere = new ChoiceRenderer<TRC.TrcPurpose>() {
			//				private static final long serialVersionUID = 6135223254823184809L;
			//
			//				@Override
			//				public Object getDisplayValue(se.sb.epsos.web.model.TRC.TrcPurpose object) {
			//					return getLocalizer().getString("trc." + object.name(), TrcPage.this);
			//				}
			//			};
			//			RadioChoice<TRC.TrcPurpose> purpose = new RadioChoice<TRC.TrcPurpose>("purpose", Arrays.asList(TRC.TrcPurpose.values()), purposeRenderere);
			//			add(purpose);
			model.getObject().setPurpose(TrcPurpose.TREATMENT.name());
			String confirmTextKey = null;
			if (TrcPage.this.getSession().getUserDetails().isPharmaceut()) {
				confirmTextKey = "trc.confirmed.EP";
			} else {
				confirmTextKey = "trc.confirmed.PS";
			}
			add(new Label("confirmText", getString(confirmTextKey)));
			AjaxButton confirmButton = new AjaxButton("confirmButton"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					((EpsosAuthenticatedWebSession)getSession()).getUserDetails().setTrc(trc);
					model.getObject().setConfirmed(true);
					try {
						getServiceFacade().setTRCAssertion(model.getObject(), ((EpsosAuthenticatedWebSession) getSession()).getUserDetails());
						setResponsePage(new QueryDocumentsPage(parametersToPassFurther));
					} catch (NcpServiceException e) {
						LOGGER.error("TRC service call failed", e);
						if (e.isCausedByLoginRequired()) {
							getSession().invalidateNow();
							throw new RestartResponseAtInterceptPageException(LoginPage.class);
						}
						if(e.isKnownEpsosError()){
							handleKnownNcpException(e, getString("TRC.error.service"));
						}
						else {
							error("TRC service call failed: " + e.getMessage());
						}
					}	
					target.addComponent(getFeedback());
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.addComponent(getFeedback());
				}
				
			};
			add(confirmButton);
		}
	}
}
