/**
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 Kela (The Social Insurance Institution of Finland)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact email: epsos@kanta.fi or Konstantin.Hypponen@kela.fi
 */
package eu.epsos.protocolterminators.ws.server.xdr;

import eu.epsos.protocolterminators.ws.server.exception.NIException;

/**
 * Thrown if discard request is rejected because the issuing HCPO of 
 * the discard request is not the HCPO that provided the eDispensation.
 */
public class NoMatchingDispensationException extends NIException {
	private static final long serialVersionUID = 5550386714166624536L;

	public NoMatchingDispensationException() {
		super("4703", "Insufficient rights");
	}
	public NoMatchingDispensationException(String message) {
		super("4703", message);
	}
}
