package eu.epsos.util.proxy;

import java.io.Serializable;

public class ProxyCredentials implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String hostname;

    private String port;

    private boolean proxyAuthenticated;

    public ProxyCredentials() {
        super();
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the proxyAuthenticated
     */
    public boolean isProxyAuthenticated() {
        return proxyAuthenticated;
    }

    /**
     * @param proxyAuthenticated the proxyAuthenticated to set
     */
    public void setProxyAuthenticated(boolean proxyAuthenticated) {
        this.proxyAuthenticated = proxyAuthenticated;
    }
}
