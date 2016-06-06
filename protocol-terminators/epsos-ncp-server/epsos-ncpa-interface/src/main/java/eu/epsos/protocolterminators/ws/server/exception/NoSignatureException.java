package eu.epsos.protocolterminators.ws.server.exception;

public class NoSignatureException extends NIException {
	private static final long serialVersionUID = -2813019150881427805L;

	public NoSignatureException(String message) {
		super("4704", message);
	}
}
