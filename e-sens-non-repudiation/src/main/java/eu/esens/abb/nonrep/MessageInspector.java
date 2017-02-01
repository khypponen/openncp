package eu.esens.abb.nonrep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.mail.internet.MimeMessage;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;


public class MessageInspector {

    public static final String SOAP_LOCAL_NAME = "Envelope";
    public static final String SOAP12_NAMESPACE = "http://www.w3.org/2003/05/soap-envelope";
    public static final String WS_ADDRESSING_NS = "http://www.w3.org/2005/08/addressing";
    public static final String IHE_ITI_XCA_RETRIEVE = "urn:ihe:iti:2007:CrossGatewayRetrieve";

    public final static Logger l = LoggerFactory.getLogger(MessageInspector.class);

    private MessageType messageType;
    private String messageUUID;


    public MessageInspector(final MimeMessage incomingMsg) throws MalformedMIMEMessageException {
        throw new MalformedMIMEMessageException("Not yet implemented");
    }

    public MessageInspector(final SOAPMessage incomingMsg) throws MalformedMIMEMessageException, MalformedIHESOAPException, SOAPException {
        this(incomingMsg.getSOAPBody().getOwnerDocument());
    }

    /**
     * @param incomingMsg
     * @throws MalformedIHESOAPException
     */
    public MessageInspector(final Document incomingMsg) throws MalformedIHESOAPException {

        if (incomingMsg == null) {
            throw new NullPointerException("No message has been passed");
        }
        l.debug("MessageInspector, called with a document. Checking headers");
        checkHeaders(incomingMsg);
    }

    private void checkHeaders(Document incomingMsg) throws MalformedIHESOAPException {

		/*
		 * Here call the message inspectors which are configured for this system. Fake check for the moment
		 */
		
		
		/*
		 * First, check if it is a SOAP
		 */
        Element docElement = incomingMsg.getDocumentElement();

        l.debug("Checking if it is a SOAP document");
        if (docElement.getLocalName().equals(SOAP_LOCAL_NAME) &&
                docElement.getNamespaceURI().equals(SOAP12_NAMESPACE)) {
            // I found a soap element, now proceed with the addressing
            l.debug("Found a SOAP message");

            // The SOAP Message must be well structured to avoid MITM attacks
            // e.g., it must have one soap header and one single addressing
            // No WSSE4j is used here (which doesn't check for it).

            NodeList nl = docElement.getElementsByTagNameNS(SOAP12_NAMESPACE, "Header");
            Utilities.checkForNull(nl, "Header", l);

            // get the body
            NodeList nlBody = docElement.getElementsByTagNameNS(SOAP12_NAMESPACE, "Body");
            Utilities.checkForNull(nlBody, "Body", l);
            Element body = (Element) nlBody.item(0);

            // it can only be an element here, no classcasts
            Element header = (Element) nl.item(0);

            // header must have one addressing action
            nl = header.getElementsByTagNameNS(WS_ADDRESSING_NS, "Action");
            Utilities.checkForNull(nl, "WS-Addressing action", l);


            Element action = (Element) nl.item(0);
            String actionText = action.getTextContent();
            if (actionText == null) {
                throw new MalformedIHESOAPException("No action text found");
            }

            if (actionText.equals(IHE_ITI_XCA_RETRIEVE)) {
                l.debug("Found an IHE ITI XCA RETRIEVE");
                IHEXCARetrieve xcaRetrieve = new IHEXCARetrieve(body);
                this.setMessageType(xcaRetrieve);
            } else {
                l.error("Action not recognized: " + actionText);

                // I differentiate here, since one may do some other guesses, to see if it is a valid message
                //TODO
                UnknownMessageType umt = new UnknownMessageType(incomingMsg);
                this.setMessageType(umt);
            }

            nl = header.getElementsByTagNameNS(WS_ADDRESSING_NS, "MessageID");
            Utilities.checkForNull(nl, "WS-Addressing MessageID", l);

            Element uuidEl = (Element) nl.item(0);
            String uuidText = uuidEl.getTextContent();
            if (uuidText == null) {
                throw new MalformedIHESOAPException("No uuid can be found in the WS-Addressing header");
            } else {
                this.messageUUID = uuidText;
            }


        } else {
            l.error("The document passed is not a SOAP.");
            UnknownMessageType umt = new UnknownMessageType(incomingMsg);
            this.setMessageType(umt);
        }

    }

    public MessageType getMessageType() {
        return messageType;
    }

    private void setMessageType(final MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageUUID() {
        return this.messageUUID;
    }


}
