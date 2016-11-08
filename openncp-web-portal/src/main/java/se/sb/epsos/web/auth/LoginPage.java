/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.auth;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

import se.sb.epsos.web.BasePage;
import se.sb.epsos.web.service.NcpServiceException;

public class LoginPage extends BasePage {
	public LoginPage() {
		add(new LoginForm("loginForm"));
	}

	/**
	 * Sign in form.
	 */
	public final class LoginForm extends StatelessForm<Form<?>> {
		private static final long serialVersionUID = 1L;

		/**
		 * remember username
		 */
		private boolean rememberMe = true;
		/**
		 * Constructor.
		 * 
		 * @param id id of the form component
		 */
		public LoginForm(final String id) {
			// sets a compound model on this form, every component without an
			// explicit model will use this model too
			super(id, new CompoundPropertyModel(new ValueMap()));
			
			// only remember username, not passwords
			add(new TextField<String>("username").setRequired(true).setPersistent(rememberMe).setOutputMarkupId(false));
			add(new PasswordTextField("password").setRequired(true).setOutputMarkupId(false));
			add(new CheckBox("rememberMe", new PropertyModel<Boolean>(this, "rememberMe")));
			add(new AjaxButton("submitButton") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					handleOnSubmit(form.getModelObject());
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.addComponent(getFeedback());
				}

			});
		}
		
		private void handleOnSubmit(Object object) {
			if (!rememberMe) {
				getPage().removePersistedFormData(LoginForm.class, true);
			}

			ValueMap values = (ValueMap) object;
			String username = values.getString("username");
			String password = values.getString("password");

			AuthenticatedWebSession session = AuthenticatedWebSession.get();
			if (session.signIn(username, password)) {
				try {
					getServiceFacade().bindToSession(getSession().getId());
					getServiceFacade().initUser(LoginPage.this.getSession().getUserDetails());
				} catch (NcpServiceException e) {
					LOGGER.error("Login failed: ",e);
					error(getLocalizer().getString("error.login.init", this));
				}
				if (!continueToOriginalDestination()) {
					setResponsePage(getApplication().getHomePage());
				}
			} else {
				LOGGER.error("Login failed due to bad credentials");
				error(getApplication().getResourceSettings().getLocalizer().getString("error.login", this));
			}
                }
		
		/**
		 * @see org.apache.wicket.Component#getMarkupId()
		 */
		public String getMarkupId() {
			return getId();
		}

		/**
		 * @return true if formdata should be made persistent (cookie) for later logins.
		 */
		public boolean getRememberMe() {
			return rememberMe;
		}

		/**
		 * Remember form values for later logins?.
		 * 
		 * @param rememberMe true if formdata should be remembered
		 */
		public void setRememberMe(boolean rememberMe) {
			this.rememberMe = rememberMe;
			((FormComponent<?>) get("username")).setPersistent(rememberMe);
		}
	}
}
