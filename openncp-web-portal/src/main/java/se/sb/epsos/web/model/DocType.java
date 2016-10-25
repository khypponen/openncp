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

public enum DocType {
	EP("urn:epSOS:ep:pre:2010"), 
        PS("urn:epSOS:ps:ps:2010"), 
        ED("urn:epSOS:ep:dis:2010"), 
        PDF("urn:ihe:iti:xds-sd:pdf:2008");
	
	public String formatCode;
	
	private DocType(String classCode) {
		this.formatCode = classCode;
	}
	
	public static DocType getFromFormatCode(String formatCode) {
		for(DocType type : DocType.values()) {
			if (type.formatCode.equals(formatCode)) {
				return type;
			}
		}
		return null;
	}
}
