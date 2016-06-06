package eu.epsos.protocolterminators.ws.server.exception;

public class WeakAuthenticationException extends NIException {
	private static final long serialVersionUID = -3723212345611879399L;

	public WeakAuthenticationException(String message) {
		super("4702", message);
	}

}
