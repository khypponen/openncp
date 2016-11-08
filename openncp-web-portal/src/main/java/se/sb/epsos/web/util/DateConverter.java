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

import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateConverter implements Converter {
	protected static Logger logger = LoggerFactory.getLogger(DateConverter.class);
	private SimpleDateFormat dateFormat;
	public DateConverter(String datePattern) {
		dateFormat = new SimpleDateFormat(datePattern);
		dateFormat.setLenient(false);
	}

	public Object convert(Class type, Object value) {
		try {
			return dateFormat.parse((String) value);
		} catch (Exception e) {
                        logger.error("Failed to parse date: " + value.toString() + " with pattern "+ dateFormat.toPattern());
			return null;
		}
	}
}