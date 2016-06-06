package eu.epsos.protocolterminators.ws.server.exception;

public class UnknownFilterException extends NIException {

	private static final long serialVersionUID = -483878991638325728L;

	public UnknownFilterException(String message) {
		super("4204", message);
	}
}
