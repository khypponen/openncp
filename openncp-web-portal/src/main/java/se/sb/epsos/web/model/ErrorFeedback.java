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
import java.util.Date;

/**
 * This class represents an error which should be displayed to the end user.
 * 
 * @author Daniel
 * 
 */
public class ErrorFeedback implements Serializable {
	private static final long serialVersionUID = 1L;
	String errorCode;
	String errorMessage;
	String timeOfError;
	String severityType;
	transient Exception exception;

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public ErrorFeedback() {
		timeOfError = new Date().toString();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTimeOfError() {
		return timeOfError;
	}

	public void setTimeOfError(String timeOfError) {
		this.timeOfError = timeOfError;
	}

	public String getSeverityType() {
		return severityType.substring(severityType.lastIndexOf(":")+1, severityType.length());
	}

	public void setSeverityType(String severityType) {
		this.severityType = severityType;
	}
}
