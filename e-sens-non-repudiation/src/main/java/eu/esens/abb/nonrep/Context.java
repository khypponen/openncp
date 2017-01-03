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
	private X509Certificate issuercertificate;
	private X509Certificate sendercertificate;
	private X509Certificate recipientcertificate;
	
	private DateTime submissionTime;
	private String event;
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
	
	public Element getRequest1() {
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
		this.issuercertificate = cert;
	}
	
	public X509Certificate getIssuerCertificate() {
		return this.issuercertificate;
	}

	public void setSenderCertificate(X509Certificate cert) {
		this.sendercertificate = cert;
	}
	
	public X509Certificate getSenderCertificate() {
		return this.sendercertificate;
	}
	public void setRecipientCertificate(X509Certificate cert) {
		this.recipientcertificate = cert;
	}
	
	public X509Certificate getRecipientCertificate() {
		return this.recipientcertificate;
	}
	
	public void setSubmissionTime(DateTime dateTime) {
		this.submissionTime = dateTime;
	}
	
	
	public DateTime getSubmissionTime() {
		return this.submissionTime;
	}

	public void setEvent(String string) {
		this.event = string;
	}
	
	public String getEvent() {
		return this.event;
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
