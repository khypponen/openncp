package eu.epsos.protocolterminators.ws.server.exception;

public class NoDataException extends NIException {

	private static final long serialVersionUID = -6429013545938611399L;

	public NoDataException(String message) {
		super("1102", message);
	}

}
