package eu.esens.abb.nonrep;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;
import org.apache.log4j.Logger;
import org.opensaml.Configuration;
import org.opensaml.xacml.policy.PolicySetType;
import org.opensaml.xacml.policy.PolicyType;
import org.opensaml.xacml.policy.impl.PolicySetTypeMarshaller;
import org.opensaml.xacml.policy.impl.PolicyTypeMarshaller;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionQueryType;
import org.opensaml.xacml.profile.saml.XACMLPolicyStatementType;
import org.opensaml.xacml.profile.saml.impl.XACMLAuthzDecisionQueryTypeMarshaller;
import org.opensaml.xacml.profile.saml.impl.XACMLPolicyStatementTypeMarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.impl.XSDateTimeMarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Utilities {

    public static void checkForNull(final NodeList nl, final String toCheck, final Logger l) throws MalformedIHESOAPException {

        if (nl == null || nl.getLength() != 1) {
            String err = "No " + toCheck + " found";
            l.error(err);
            throw new MalformedIHESOAPException(err);
        }
    }

    public static Element toElement(XMLObject a) throws TOElementException {

        MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();

        if (marshallerFactory == null) {
            throw new TOElementException("No MarshallerFactory available. Did you endorse "
                    + "xml libraries? Did you bootstrap opensaml?");
        }

        // register some marshaller
        marshallerFactory.registerMarshaller(XACMLPolicyStatementType.DEFAULT_ELEMENT_NAME_XACML20,
                new XACMLPolicyStatementTypeMarshaller());
        marshallerFactory.registerMarshaller(XACMLAuthzDecisionQueryType.DEFAULT_ELEMENT_NAME_XACML20,
                new XACMLAuthzDecisionQueryTypeMarshaller());
        marshallerFactory.registerMarshaller(PolicySetType.DEFAULT_ELEMENT_NAME,
                new PolicySetTypeMarshaller());
        marshallerFactory.registerMarshaller(PolicyType.DEFAULT_ELEMENT_NAME,
                new PolicyTypeMarshaller());
        marshallerFactory.registerMarshaller(org.opensaml.xml.schema.XSDateTime.TYPE_NAME, new XSDateTimeMarshaller());

        Marshaller marshaller = marshallerFactory.getMarshaller(a);

        if (marshaller == null) {

            // The XACMLPolicyStatementType needs a separate marhsaller
            if (a instanceof XACMLPolicyStatementType) {
                Marshaller policyStmtMarshaller = marshallerFactory
                        .getMarshaller(XACMLPolicyStatementType.DEFAULT_ELEMENT_NAME_XACML20);

                try {
                    Element policyEl = policyStmtMarshaller.marshall(a);
                    return policyEl;
                } catch (MarshallingException e) {
                    throw new TOElementException(e);
                }
            }
            throw new TOElementException("No marshaller found for the xmlobject");
        }

        Element assertionElement = null;
        try {
            assertionElement = marshaller.marshall(a);
            return assertionElement;
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new TOElementException("Unable to marshall the assertion: "
                    + e1.getMessage(), e1);
        }
    }

    public static void serialize(Element request) throws IOException {
        serialize(request, System.out);
    }

    /**
     * Added for handling alternative outputs (instead of only sysout)
     *
     * @param request
     * @param out
     * @throws IOException
     */
    public static void serialize(Element request, OutputStream out) throws IOException {
        OutputFormat format = new OutputFormat();

        format.setLineWidth(65);
        format.setIndenting(false);
        format.setIndent(2);
        format.setEncoding("UTF-8");
        format.setOmitComments(true);
        format.setOmitXMLDeclaration(false);
        format.setVersion("1.0");
        format.setStandalone(true);

        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(request.getOwnerDocument());
    }

    public synchronized static SOAPMessage toSoap(Document doc, MimeHeaders requestMimeHeaders) throws SOAPException {
        MessageFactory messageFactory = new SOAPMessageFactory1_2Impl();
        SOAPMessage message = messageFactory.createMessage();

        DOMSource domSource = new DOMSource(doc);
        message.getSOAPPart().setContent(domSource);

        System.out.println("Checking if I need to add mime headers");

        // If I have some mime headers, I have to remove them first.
        if (requestMimeHeaders != null) {

//	            System.out.println("I have to add mime headers, removing them first");
//	            Iterator<?> removerIterator = message.getMimeHeaders().getAllHeaders();
//	            while (removerIterator.hasNext()) {
//	                MimeHeader t = (MimeHeader) removerIterator.next();
//	                message.getMimeHeaders().removeHeader(t.getName());
//	                System.out.println("Removed: " + t.getName());
//	            }
            System.out.println("Now adding the ones requested");
            Iterator<?> it = requestMimeHeaders.getAllHeaders();
            while (it.hasNext()) {
                MimeHeader mimeItem = (MimeHeader) it.next();

                String retValue = mimeItem.getValue().replace("Multipart/Related", "multipart/related");

                System.out.println("ADDING MIME: " + mimeItem.getName() + ": " + retValue);
                message.getMimeHeaders().addHeader(mimeItem.getName(), retValue);

            }

            message.saveChanges();

        }
        if (message.saveRequired()) {
            System.out.println("Saving changes");
            message.saveChanges();
        }

        MimeHeaders mh = message.getMimeHeaders();
        String content = mh.getHeader("Content-Type")[0].replace("Multipart/Related", "multipart/related");

        mh.setHeader("Content-Type", content + ";");

        return message;
    }
}
