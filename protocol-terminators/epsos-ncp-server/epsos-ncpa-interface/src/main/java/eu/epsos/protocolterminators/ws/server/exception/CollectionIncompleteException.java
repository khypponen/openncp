package eu.epsos.protocolterminators.ws.server.exception;

public class CollectionIncompleteException extends NIException {

	private static final long serialVersionUID = -8728511084030064015L;

	public CollectionIncompleteException(String message) {
		super("4102", message);
	}
}
