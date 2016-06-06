package eu.epsos.protocolterminators.ws.server.exception;

public class NoMatchException extends NIException {

	private static final long serialVersionUID = -1353577541109670053L;

	public NoMatchException(String message) {
		super("4105", message);
	}
}
