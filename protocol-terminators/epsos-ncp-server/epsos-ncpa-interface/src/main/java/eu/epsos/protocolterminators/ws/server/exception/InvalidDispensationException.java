package eu.epsos.protocolterminators.ws.server.exception;

public class InvalidDispensationException extends NIException {
	private static final long serialVersionUID = -4968105055699416626L;

	public InvalidDispensationException(String code, String message) {
		super("4106", message);
	}

}
