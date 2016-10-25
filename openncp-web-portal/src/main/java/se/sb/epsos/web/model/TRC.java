/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/

package se.sb.epsos.web.model;

import java.io.Serializable;
import java.util.Date;

public class TRC implements Serializable {

	private static final long serialVersionUID = 9019658231297834083L;

	private boolean confirmed;
	private Date confirmationTimestamp;
	private String purpose;
	private LoadablePersonModel person;

	public TRC() {
		super();
	}

	public TRC(Person person, TrcPurpose type) {
		if (type != null) {
			this.purpose = type.name();
			this.confirmed = true;
			this.confirmationTimestamp = new Date();
		}
		this.person = new LoadablePersonModel(person);
	}

	public void setPerson(Person person) {
		this.person = new LoadablePersonModel(person);
	}

	public Person getPerson() {
		return person.getObject();
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
		this.confirmationTimestamp = new Date();
	}

	public Date getConfirmationTimestamp() {
		return confirmationTimestamp;
	}

	public void setConfirmationTimestamp(Date confirmationTimestamp) {
		this.confirmationTimestamp = confirmationTimestamp;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public enum TrcPurpose {
		TREATMENT, EMERGENCY
	}

}
