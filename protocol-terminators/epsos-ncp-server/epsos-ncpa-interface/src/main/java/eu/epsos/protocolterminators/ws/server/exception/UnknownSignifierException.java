package eu.epsos.protocolterminators.ws.server.exception;

public class UnknownSignifierException extends NIException {

	private static final long serialVersionUID = -8404675303540764793L;

	public UnknownSignifierException(String message) {
		super("4202", message);
	}
}
