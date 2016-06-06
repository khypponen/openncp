package eu.esens.abb.nonrep;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.mail.internet.MimeMessage;
import javax.xml.soap.SOAPMessage;

import org.joda.time.DateTime;
import org.opensaml.xml.XMLObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.esens.abb.nonrep.EnforcePolicy;

public class Context {

	private SOAPMessage incomingMsg;
	private Element requestDOM;
	private XMLObject requestXMLObj;
	private EnforcePolicy enforcer;
	private String user;
	private String currentHost;
	private String remoteHost;
	private X509Certificate certificate;
	private DateTime submissionTime;
	private String epSOSEvent;
	private String messageUUID;
	private String authenticationMethod;
	private PrivateKey key;
	private Document icomingMsgAsDocument;
	public final String getCurrentHost() {
		return currentHost;
	}

	public final void setCurrentHost(String currentHost) {
		this.currentHost = currentHost;
	}

	public final String getRemoteHost() {
		return remoteHost;
	}

	public final void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public Context() {
	}

	public void setMessage(MimeMessage message) {
		
	}


	public SOAPMessage getIncomingMsg() {
		return incomingMsg;
	}

	public void setIncomingMsg(SOAPMessage incomingMsg) {
		this.incomingMsg = incomingMsg;
	}

	public void setIncomingMsg(Document incomingMsg) {
		this.icomingMsgAsDocument = incomingMsg;
	}
	
	public Document getIncomingMsgAsDocument() {
		return this.icomingMsgAsDocument;
	}
	public void setRequest(XMLObject request) {
		this.requestXMLObj = request;
	}
	
	public XMLObject getRequestAsObject() {
		return this.requestXMLObj;
	}
	public void setRequest(Element request) {
		this.requestDOM = request;
	}
	
	public Element getRequest() {
		return this.requestDOM;
	}

	public void setEnforcer(EnforcePolicy enforcePolicy) {
		this.enforcer = enforcePolicy;
	}
	
	public EnforcePolicy getEnforcer() {
		return this.enforcer;
	}

	public void setUsername(String user) {
		this.user = user;
	}
	
	public String getUsername() {
		return this.user;
	}

	public void setIssuerCertificate(X509Certificate cert) {
		this.certificate = cert;
	}
	
	public X509Certificate getIssuerCertificate() {
		return this.certificate;
	}

	public void setSubmissionTime(DateTime dateTime) {
		this.submissionTime = dateTime;
	}
	
	
	public DateTime getSubmissionTime() {
		return this.submissionTime;
	}

	public void setEpsosEvent(String string) {
		this.epSOSEvent = string;
	}
	
	public String getEpsosEvent() {
		return this.epSOSEvent;
	}

	public void setMessageUUID(String messageUUID) {
		this.messageUUID = messageUUID;
	}
	
	public String getMessageUUID() {
		return this.messageUUID;
	}

	public void setAuthenticationMethod(String string) {
		this.authenticationMethod = string;
	}
	
	public String getAuthenticationMethod() {
		return this.authenticationMethod;
	}

	public void setSigningKey(PrivateKey key) {
		this.key = key;
	}
	public PrivateKey getSigningKey() {
		return this.key;
	}

}
