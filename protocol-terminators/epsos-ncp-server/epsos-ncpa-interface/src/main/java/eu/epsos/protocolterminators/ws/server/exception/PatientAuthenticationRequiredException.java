package eu.epsos.protocolterminators.ws.server.exception;

public class PatientAuthenticationRequiredException extends NIException {
	private static final long serialVersionUID = -159689601257832448L;

	public PatientAuthenticationRequiredException(String message) {
		super(null, message, "1.3.6.1.4.1.12559.11.10.1.3.2.2.1");
	}
}
