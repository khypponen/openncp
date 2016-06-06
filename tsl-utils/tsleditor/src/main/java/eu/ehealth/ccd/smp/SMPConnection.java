/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehealth.ccd.smp;

/**
 *
 * @author joao.cunha
 * 
 * Instances of this class store the necessary information to connect to an SMP server
 */
public class SMPConnection {
    private String uri;
    private String username;
    private String password;
    
    private SMPConnection() {}

    public SMPConnection(String uri, String username, String password) {
        this.uri = uri;
        this.username = username;
        this.password = password;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nSMP Connection\n");
        sb.append("\t").append("URI: ").append(uri).append("\n");
        sb.append("\t").append("Username: ").append(username).append("\n");
        sb.append("\t").append("Password: ").append(password).append("\n");
        return sb.toString();
    }
}
