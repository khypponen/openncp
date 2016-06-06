/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * 
 * This file is part of SRDC epSOS NCP.
 * 
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SRDC epSOS NCP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.securityman.exceptions;

import java.util.HashMap;
import java.util.Map;

public class InsufficientRightsException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final Map<Integer, String> errorMessages = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
	{
		put(1002, "The given TRC Assertion does not validate against the Identity Assertion");
		put(4701, "No consent.");
		put(4703, "Either the security policy of country A or a privacy policy of the patient (that was given in country A) does not allow the requested operation to be performed by the HCP.");
	}};
	
	private String message;
	private String errorCode;

	public InsufficientRightsException() {
		errorCode = "4703";
		message = errorMessages.get(Integer.parseInt(errorCode));
	}
	
	public InsufficientRightsException(int errorCode) {
		message = errorMessages.get(errorCode);
		this.errorCode = Integer.toString(errorCode);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getCode() {
		return errorCode;
	}
}
