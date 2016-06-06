package eu.epsos.protocolterminators.ws.server.exception;

public class NoConsentException extends NIException {

	private static final long serialVersionUID = 2194752799478399763L;

	public NoConsentException(String message) {
		super("4701", message);
	}

}
