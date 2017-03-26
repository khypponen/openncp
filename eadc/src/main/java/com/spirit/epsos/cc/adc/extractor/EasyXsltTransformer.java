package com.spirit.epsos.cc.adc.extractor;

import eu.epsos.pt.eadc.util.EadcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;


/**
 * Wrapper-utility for setting up and transforming an xml-document by using a provided xslt. By initializing an object of this class a transformer object is being cached for improving performance.
 *
 * @author mk
 */
public class EasyXsltTransformer {

    private static Logger log = LoggerFactory.getLogger(EasyXsltTransformer.class);

    private static TransformerFactory objTransformerFact = TransformerFactory.newInstance();
    private Document xsltDocument = null;
    private Transformer transformer = null;

    /**
     * Hide the default-constructor to force using the parameterized constructor.
     */
    private EasyXsltTransformer() {
    }

    /**
     * Constructor for creating an EaysXsltTransformer-object.
     *
     * @param xsltDocument
     * @throws Exception
     */
    public EasyXsltTransformer(Document xsltDocument) throws Exception {
        this();
        if (xsltDocument == null) {
            log.error("The supplied XSLT-Document was null");
            throw new Exception("The supplied XSLT-Document was null");
        }
        DOMSource xsltDomSource = new DOMSource(xsltDocument);
        try {
            this.xsltDocument = xsltDocument;
            this.transformer = EasyXsltTransformer.objTransformerFact.newTransformer(xsltDomSource);
            log.info("An XSLT-Transformer object was initialized successfully");
        } catch (TransformerConfigurationException transformerConfigurationException) {
            log.error("An error occured, when loading the XSLT-Document",
                    transformerConfigurationException);
            log.error("\n"
                    + "|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n"
                    + "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV\n"
                    + "Used XSLT Document:\n"
                    + EadcUtil.convertXMLDocumentToString(xsltDocument)
                    + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n"
                    + "|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n");
            throw new Exception("An error occured, when loading the XSLT-Document",
                    transformerConfigurationException);
        }
    }

    /**
     * SYNCHRONIZED: The transformer object MUST NOT be used by multiple threads at the same time (javax.xml.transform.Transformer)
     *
     * @param xmlSourceNode
     * @return
     * @throws Exception
     * @throws TransformerException
     */
    public synchronized Document transform(Node xmlSourceNode) throws Exception {
        log.debug("Entering synchronous part");
        DOMResult objDomResult = new DOMResult((Node) null);
        try {
            this.transformer.transform(new DOMSource(xmlSourceNode),
                    objDomResult);
        } catch (TransformerException transformerException) {
            log.error("An error occured during an XSLT-Transformation",
                    transformerException);
            log.error("The Documents leading to that exception are:\n"
                    + "|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n"
                    + "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV\n"
                    + "Used XML-Document:\n"
                    + EadcUtil.convertXMLDocumentToString((Document) xmlSourceNode) + "\n"
                    + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n"
                    + "|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n"
                    + "\n"
                    + "|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n"
                    + "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV\n"
                    + "Used XSLT-Document:\n"
                    + EadcUtil.convertXMLDocumentToString(this.xsltDocument) + "\n"
                    + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n"
                    + "|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n");
            log.debug("Leaving synchronous part");
            throw new Exception("An error occured during an XSLT-Transformation",
                    transformerException);
        }
        log.debug("Leaving synchronous part");
        return (Document) objDomResult.getNode();
    }
}
