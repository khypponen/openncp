/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
 *
 *    This file is part of epSOS-WEB.
 *
 *    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
 **/
package se.sb.epsos.web.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;


/**
 * Created by IntelliJ IDEA.
 * User: andreas
 * Date: 2011-06-27
 * Time: 16.01
 * To change this template use File | Settings | File Templates.
 */
public class XmlUtil {

    /**
     * @param node
     * @return
     * @throws TransformerException
     */
    public static byte[] doc2bytes(Node node) throws TransformerException {

        Source source = new DOMSource(node);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(out);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.transform(source, result);
        return out.toByteArray();
    }

    /**
     * @param obj
     * @return
     * @throws JAXBException
     */
    public static String marshallJaxbObject(XmlTypeWrapper<?> obj) throws JAXBException {
        StringWriter writer = new StringWriter();
        if (obj != null && obj.getTypeClass() != null) {
            JAXBContext context = JAXBContext.newInstance(obj.getClass(), obj.getTypeClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, writer);
        }
        return writer.toString();
    }

    /**
     * @param doc
     * @return
     * @throws TransformerException
     */
    public String prettyPrint(Document doc) throws TransformerException {

        StringWriter stringWriter = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));
        return stringWriter.toString();
    }
}
