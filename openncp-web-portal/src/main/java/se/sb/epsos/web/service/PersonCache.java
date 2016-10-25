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

import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.model.Person;

/**
 * 
 * @author andreas
 */
public class PersonCache {
	public static final Logger LOGGER = LoggerFactory.getLogger(PersonCache.class);
	private static PersonCache instance;
	private HashMap<PersonCacheKey, Person> cache;
	//private WeakHashMap<PersonCacheKey, Person> cache;

	private PersonCache() {
		this.cache = new HashMap<PersonCacheKey, Person>();
		//this.cache = new WeakHashMap<PersonCacheKey, Person>();
	}

	public static PersonCache getInstance() {
		if (instance == null) {
			instance = new PersonCache();
		}
		return instance;
	}

	public boolean contains(PersonCacheKey key) {
		LOGGER.debug(printCompleteCache());
		boolean found = this.cache.containsKey(key);
		LOGGER.debug("Checking cached person by key: " + key.toString() + " , person is " + (found ? "cached" : "not cached"));
		return found;
	}

	public Person get(PersonCacheKey key) {
		LOGGER.debug(printCompleteCache());
		Person person = this.cache.get(key);
		LOGGER.debug("Retrieveing person with key : " + key.toString() + " , person was " + (person != null ? "found" : "not found") + " in cache");
		return person;
	}

	public void put(PersonCacheKey key, Person person) {
		LOGGER.debug("Caching person with key: " + key.toString());
		this.cache.put(key, person);
		LOGGER.debug(printCompleteCache());
	}

	public void flush() {
		LOGGER.debug("Flushing entire cache");
		this.cache.clear();
		LOGGER.debug(printCompleteCache());
	}

	public void flush(MetaDocument doc) {
		LOGGER.debug("Flushing person with key: " + doc.getDtoCacheKey().toString());
		this.cache.remove(doc.getDtoCacheKey());
		LOGGER.debug(printCompleteCache());
	}

	public void flush(PersonCacheKey key) {
		LOGGER.debug("Flushing person with key: " + key.toString());
		this.cache.remove(key);
		LOGGER.debug(printCompleteCache());
	}

	public void flush(String sessionId) {
		LOGGER.debug("Flushing all persons for sessionId: " + sessionId);
		if (sessionId != null) {
			Iterator<PersonCacheKey> it = this.cache.keySet().iterator();
			while (it.hasNext()) {
				PersonCacheKey key = (PersonCacheKey) it.next();
				if (key.getSessionId() == null || key.getSessionId().equals(sessionId)) {
					LOGGER.debug("Flushing person with key: " + key.toString());
					it.remove();
				}
			}
		}
		LOGGER.debug(printCompleteCache());
	}

	private String printCompleteCache() {
		StringBuffer buf = new StringBuffer("\n****\tDocummentCache current status:\n");
		int i = 1;
		for (PersonCacheKey key : this.cache.keySet()) {
			buf.append("\t" + i + " : " + key.toString() + "\n");
			i++;
		}
		buf.append("****\t total size: " + this.cache.size() + "\n");
		return buf.toString();
	}
}
