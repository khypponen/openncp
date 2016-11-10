/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.model;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.service.DocumentCache;
import se.sb.epsos.web.service.DocumentClientDtoCacheKey;
import se.sb.epsos.web.service.MetaDocument;

/**
 * 
 * @author andreas
 */
public class LoadableDocumentModel<T extends MetaDocument> extends LoadableDetachableModel<T> {
	private static final long serialVersionUID = -6009707607503898584L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoadableDocumentModel.class);

	private DocumentClientDtoCacheKey key;

	public LoadableDocumentModel(T obj) {
		LOGGER.debug("Creating LoadableDocumentModel for " + obj.getClass().getSimpleName() + " with key: " + obj.getDtoCacheKey().toString());
		this.key = obj.getDtoCacheKey();
		setObject(obj);
	}

	@Override
	protected T load() {
		LOGGER.debug("Loading object from cache with key:" + this.key.toString());
		return (T) DocumentCache.getInstance().get(this.key);
	}

}
