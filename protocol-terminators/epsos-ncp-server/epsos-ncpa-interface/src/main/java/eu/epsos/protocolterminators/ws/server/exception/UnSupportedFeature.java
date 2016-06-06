package eu.epsos.protocolterminators.ws.server.exception;

public class UnSupportedFeature extends NIException {

	private static final long serialVersionUID = -7044629789540910172L;

	public UnSupportedFeature(String message) {
		super("4201", message);
	}

}
