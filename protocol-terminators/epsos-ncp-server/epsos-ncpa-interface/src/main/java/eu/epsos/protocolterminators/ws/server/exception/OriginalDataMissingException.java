package eu.epsos.protocolterminators.ws.server.exception;

public class OriginalDataMissingException extends NIException {
	private static final long serialVersionUID = 4254468101664118588L;

	public OriginalDataMissingException(String code, String message) {
		super("4107", message);
	}

}
