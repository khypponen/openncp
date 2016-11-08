/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.util;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.service.MetaDocument;

public abstract class LinkedPanel<T> extends Panel {

    private static final long serialVersionUID = 5517887196509318125L;
    protected static Logger LOGGER = LoggerFactory.getLogger(LinkedPanel.class);

    public LinkedPanel(final String id, PopupSettings settings, IModel<?> model) {
        super(id);
        LOGGER.info("Model: " + model.getObject());
        add(createAbstractLink("link", settings).add(new Label("label", model)));
    }
    
    public LinkedPanel(final String id, PopupSettings settings, IModel<?> model, DynamicResource resource) {
        super(id);
        LOGGER.info("Model: " + model.getObject());
        add(createResourceLink("link", settings, resource).add(new Label("label", model)));
    }
    
    protected Link<MetaDocument> createResourceLink(String id, PopupSettings settings, DynamicResource resource) {
		return new ResourceLink<MetaDocument>(id, resource) {
			private static final long serialVersionUID = 4551495809366034555L;

			@Override
			public void onClick() {
				LinkedPanel.this.onClick();
			}
		}.setPopupSettings(settings);      
    }

    protected Link<String> createAbstractLink(String id, PopupSettings settings) {
        return new Link<String>(id) {
            private static final long serialVersionUID = -1791116792849757937L;

            @Override
            public void onClick() {
                LinkedPanel.this.onClick();
            }
        }.setPopupSettings(settings);
    }

    public abstract void onClick();
}
