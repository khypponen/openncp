package eu.epsos.protocolterminators.ws.server.exception;

public class PivotDataMissingException extends NIException {
	private static final long serialVersionUID = -8929157858698790358L;

	public PivotDataMissingException(String message) {
		super("4108", message);
	}

}
