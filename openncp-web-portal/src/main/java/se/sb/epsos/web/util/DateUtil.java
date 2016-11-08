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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String formatDate(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	public static String formatDate(Date date) {
		return formatDate(date, EpsosWebConstants.DATEFORMAT);
	}
	
	public static Date formatStringToDate(String dateString) throws Exception {
		try {
			return parseDateString(dateString, EpsosWebConstants.DATEFORMAT);
		} catch(ParseException pe) {
			return parseDateString(dateString, EpsosWebConstants.DATEFORMATSEC);
		}
	}

	private static Date parseDateString(String dateString, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		return sdf.parse(dateString);
	}
}
