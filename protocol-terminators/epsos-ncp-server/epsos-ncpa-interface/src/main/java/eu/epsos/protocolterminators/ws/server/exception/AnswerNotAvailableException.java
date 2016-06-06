package eu.epsos.protocolterminators.ws.server.exception;

public class AnswerNotAvailableException extends NIException {
	private static final long serialVersionUID = 7640387067196506306L;

	public AnswerNotAvailableException(String message) {
		super(null, message, "1.3.6.1.4.1.19376.1.2.27.3");
	}
}
