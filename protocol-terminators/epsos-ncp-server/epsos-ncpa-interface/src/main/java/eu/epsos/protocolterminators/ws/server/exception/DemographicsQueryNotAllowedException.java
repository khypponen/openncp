package eu.epsos.protocolterminators.ws.server.exception;

public class DemographicsQueryNotAllowedException extends NIException {
	private static final long serialVersionUID = -1360702816426111325L;

	public DemographicsQueryNotAllowedException(String message) {
		super(null, message, "1.3.6.1.4.1.12559.11.10.1.3.2.2.1");
	}
}
