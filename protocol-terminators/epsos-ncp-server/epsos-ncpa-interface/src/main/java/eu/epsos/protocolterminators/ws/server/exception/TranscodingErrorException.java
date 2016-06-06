package eu.epsos.protocolterminators.ws.server.exception;

public class TranscodingErrorException extends NIException {

	private static final long serialVersionUID = -8381001130860083595L;

	public TranscodingErrorException(String message) {
		super("4203", message);
	}

}
