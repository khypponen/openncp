package eu.epsos.protocolterminators.ws.server.exception;

public class RenderingIncompleteException extends NIException {

	private static final long serialVersionUID = -8555270493456361182L;

	public RenderingIncompleteException(String message) {
		super("4101", message);
	}
}
