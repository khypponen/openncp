package eu.epsos.util.net;

import java.io.Serializable;

/**
 * Bean representing a set of credential required to configure and establish a
 * connection to a Proxy.
 *
 * @author European Commission - DG SANTE-A4-002
 *
 */
public class ProxyCredentials implements Serializable {

	private static final long serialVersionUID = -7621609447164132823L;

	private String proxyUser;
	private String proxyPassword;
	private String proxyHost;
	private String proxyPort;
	private String proxyAuthenticated;

	public ProxyCredentials() {
		super();
	}

	/**
	 * @return the proxyUser
	 */
	public String getProxyUser() {

		return proxyUser;
	}

	/**
	 * @param proxyUser
	 *            the proxyUser to set
	 */
	public void setProxyUser(String proxyUser) {

		this.proxyUser = proxyUser;
	}

	/**
	 * @return the proxyPassword
	 */
	public String getProxyPassword() {

		return proxyPassword;
	}

	/**
	 * @param proxyPassword
	 *            the proxyPassword to set
	 */
	public void setProxyPassword(String proxyPassword) {

		this.proxyPassword = proxyPassword;
	}

	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {

		return proxyHost;
	}

	/**
	 * @param proxyHost
	 *            the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {

		this.proxyHost = proxyHost;
	}

	/**
	 * @return the proxyPort
	 */
	public String getProxyPort() {

		return proxyPort;
	}

	/**
	 * @param proxyPort
	 *            the proxyPort to set
	 */
	public void setProxyPort(String proxyPort) {

		this.proxyPort = proxyPort;
	}

	/**
	 * @return the proxyAuthenticated
	 */
	public String getProxyAuthenticated() {

		return proxyAuthenticated;
	}

	/**
	 * @param proxyAuthenticated
	 *            the proxyAuthenticated to set
	 */
	public void setProxyAuthenticated(String proxyAuthenticated) {

		this.proxyAuthenticated = proxyAuthenticated;
	}
}
