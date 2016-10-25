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

public class StrengthVO implements Serializable {
	private static final long serialVersionUID = 1359041696120681345L;

	private QuantityVO numerator;
	private QuantityVO denumerator;

	public StrengthVO(QuantityVO numerator, QuantityVO denumerator) {
		super();
		this.numerator = numerator;
		this.denumerator = denumerator;
	}

	public QuantityVO getNumerator() {
		return numerator;
	}

	public void setNumerator(QuantityVO numerator) {
		this.numerator = numerator;
	}

	public QuantityVO getDenumerator() {
		return denumerator;
	}

	public void setDenumerator(QuantityVO denumerator) {
		this.denumerator = denumerator;
	}
}
