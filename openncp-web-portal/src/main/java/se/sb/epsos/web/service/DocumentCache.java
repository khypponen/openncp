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
package se.sb.epsos.web.service;

import java.util.Iterator;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author andreas
 */
public class DocumentCache {
	public static final Logger LOGGER = LoggerFactory.getLogger(DocumentCache.class);
	private static DocumentCache instance;
	private WeakHashMap<DocumentClientDtoCacheKey, MetaDocument> cache;

	private DocumentCache() {
		this.cache = new WeakHashMap<DocumentClientDtoCacheKey, MetaDocument>();
	}

	public static DocumentCache getInstance() {
		if (instance == null) {
			instance = new DocumentCache();
		}
		return instance;
	}

	public boolean contains(DocumentClientDtoCacheKey key) {
		boolean found = this.cache.containsKey(key);
		LOGGER.debug("Checking cached document by key: " + key.toString() + " , document is " + (found ? "cached" : "not cached"));
		return found;
	}

	public MetaDocument get(DocumentClientDtoCacheKey key) {
		LOGGER.debug(printCompleteCache());
		MetaDocument doc = this.cache.get(key);
		LOGGER.debug("Retrieveing document with key : " + key.toString() + " , document was " + (doc != null ? "found" : "not found") + " in cache");
		return doc;
	}

	public void put(DocumentClientDtoCacheKey key, MetaDocument doc) {
		LOGGER.debug("Caching " + doc.getClass().getSimpleName() + " document with key: " + key.toString());
		this.cache.put(key, doc);
		LOGGER.debug(printCompleteCache());
	}

	public void flush() {
		LOGGER.debug("Flushing entire cache");
		this.cache.clear();
		LOGGER.debug(printCompleteCache());
	}

	public void flush(MetaDocument doc) {
		LOGGER.debug("Flushing document with key: " + doc.getDtoCacheKey().toString());
		this.cache.remove(doc.getDtoCacheKey());
		LOGGER.debug(printCompleteCache());
	}

	public void flush(DocumentClientDtoCacheKey key) {
		LOGGER.debug("Flushing document with key: " + key.toString());
		this.cache.remove(key);
		LOGGER.debug(printCompleteCache());
	}

	public void flush(String sessionId) {
		LOGGER.debug("Flushing all documents for sessionId: " + sessionId);
		if (sessionId != null) {
			Iterator<DocumentClientDtoCacheKey> it = this.cache.keySet().iterator();
			while (it.hasNext()) {
				DocumentClientDtoCacheKey key = it.next();
				if (key.getSessionId() == null || key.getSessionId().equals(sessionId)) {
					LOGGER.debug("Flushing document with key: " + key.toString());
					it.remove();
				}
			}
		}
		LOGGER.debug(printCompleteCache());
	}

	private String printCompleteCache() {
		StringBuffer buf = new StringBuffer("\n****\tDocummentCache current status:\n");
		int i = 1;
		for (DocumentClientDtoCacheKey key : this.cache.keySet()) {
			buf.append("\t" + i + " : " + key.toString() + "\n");
			i++;
		}
		buf.append("****\t total size: " + this.cache.size() + "\n");
		return buf.toString();
	}
}
