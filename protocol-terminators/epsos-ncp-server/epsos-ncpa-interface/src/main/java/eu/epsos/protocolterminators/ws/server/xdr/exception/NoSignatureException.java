package eu.epsos.protocolterminators.ws.server.xdr.exception;

import eu.epsos.protocolterminators.ws.server.xdr.DocumentProcessingException;

public class NoSignatureException extends DocumentProcessingException {
	private static final long serialVersionUID = -3360716186178697692L;

	public NoSignatureException() {
		super("The Provide Data service provider only accepts data of the given kind if it is digitally signed by an HP.");
		super.setCode("4704");
	}
}
