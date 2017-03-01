package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.service;

import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import java.net.URI;
import org.apache.commons.lang3.StringUtils;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author Inês Garganta
 */
public class CustomProxy implements IProxyConfiguration {

  private String user;
  private String password;
  private String serverAddress;
  private int serverPort;
  private HttpClient httpclient;
  private HttpGet httpget;

  public CustomProxy(String serverAddress, int serverPort, String user, String password) throws ConnectionException {
  
    if (StringUtils.isEmpty(serverAddress) || serverPort == 0) {
      throw new ConnectionException("Server configuration for Proxy Authentication is missing.");
    }

    this.user = user;
    this.password = password;
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
  }

  @Override
  public void build(URI uri) {
    
    if (this.user != null) {
      CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
      credentialsProvider.setCredentials(
              new AuthScope(this.serverAddress, this.serverPort),
              new UsernamePasswordCredentials(this.user, this.password));
      this.httpclient = HttpClients.custom()
              .setDefaultCredentialsProvider(credentialsProvider).build();
    } else {
      this.httpclient = HttpClients.createDefault();
    }
    
    HttpHost proxy = new HttpHost(this.serverAddress, this.serverPort);
    this.httpget = new HttpGet(uri);
    httpget.setConfig(RequestConfig.custom()
            .setProxy(proxy)
            .build());
  }

  @Override
  public HttpClient getHttpclient() {
    return httpclient;
  }

  @Override
  public HttpGet getHttpget() {
    return httpget;
  }

}
