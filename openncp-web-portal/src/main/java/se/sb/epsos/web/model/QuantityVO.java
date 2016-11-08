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

public class QuantityVO implements Serializable {
	private static final long serialVersionUID = -7701638759511204075L;
	private String quantityValue;
	private String quantityUnit;
	private String quantityUnitUcum;

	public QuantityVO(String quantityValue, String quantityUnit, String quantityUnitUcum) {
		super();
		this.quantityValue = quantityValue;
		this.quantityUnit = quantityUnit;
		this.quantityUnitUcum = quantityUnitUcum;
	}

	public String getQuantityValue() {
		return quantityValue;
	}

	public void setQuantityValue(String quantityValue) {
		this.quantityValue = quantityValue;
	}

	public String getQuantityUnit() {
		return quantityUnit;
	}

	public String getQuantityUnitUcum() {
		return quantityUnitUcum;
	}
	
	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	public void setQuantityUnitUcum(String quantityUnitUcum) {
		this.quantityUnitUcum = quantityUnitUcum;
	}

	@Override
	public QuantityVO clone() {
		return new QuantityVO(quantityValue, quantityUnit, quantityUnitUcum);
	}
}
