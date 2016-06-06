package eu.epsos.protocolterminators.ws.server.xdr.exception;

import eu.epsos.protocolterminators.ws.server.xdr.DocumentProcessingException;

public class PivotDataMissingException extends DocumentProcessingException {
	private static final long serialVersionUID = 8197669683897748059L;

	public PivotDataMissingException() {
		super("The service consumer did not provide the epSOS pivot coded document which is requested by the Proivde Data service provider for the given kind of data.");
		super.setCode("4108");
	}
}
