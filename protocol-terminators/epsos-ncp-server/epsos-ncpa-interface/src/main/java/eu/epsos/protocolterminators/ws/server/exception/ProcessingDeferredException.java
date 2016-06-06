package eu.epsos.protocolterminators.ws.server.exception;

public class ProcessingDeferredException extends NIException {

	private static final long serialVersionUID = 4872216168488255110L;

	public ProcessingDeferredException(String message) {
		super("2201", message);
	}
}
