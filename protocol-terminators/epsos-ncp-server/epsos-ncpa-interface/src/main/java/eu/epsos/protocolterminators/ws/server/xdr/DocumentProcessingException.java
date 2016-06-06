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

public class DocumentProcessingException extends NIException {
	private static final long serialVersionUID = 2212600691470466466L;

	public DocumentProcessingException() {
		super("4106", "Invalid Dispensation");
	}
	
	public DocumentProcessingException(String message) {
		super("4106", message);
	}
}
