/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.model;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.model.StringResourceModel;

import se.sb.epsos.web.util.DynamicResource;


public abstract class LinkAction implements Serializable {
	private static final long serialVersionUID = 5588028284306327085L;

	private StringResourceModel action;
	private boolean ajax;
	private boolean resource;
	private PopupSettings popupSettings;
	private DynamicResource dynamicResource;
	private boolean disabled;

	public LinkAction(StringResourceModel action, boolean ajax, PopupSettings popupSettings) {
		this.action = action;
		this.ajax = ajax;
		this.popupSettings = popupSettings;
	}

	public LinkAction(StringResourceModel action, boolean resource, DynamicResource dynamicResource, PopupSettings popupSettings) {
		this(action, false, popupSettings);
		this.resource = resource;
		this.dynamicResource = dynamicResource;
	}

	public LinkAction(StringResourceModel stringResourceModel) {
		this(stringResourceModel, true, null);
	}

	public StringResourceModel getAction() {
		return action;
	}

	public PopupSettings getPopupSettings() {
		return popupSettings;
	}

	public boolean isAjax() {
		return ajax;
	}

	public void setResource(boolean resource) {
		this.resource = resource;
	}

	public boolean isResource() {
		return resource;
	}

	public void setDynamicResource(DynamicResource dynamicResource) {
		this.dynamicResource = dynamicResource;
	}

	public DynamicResource getDynamicResource() {
		return dynamicResource;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public abstract void onClick(AjaxRequestTarget target);
}
