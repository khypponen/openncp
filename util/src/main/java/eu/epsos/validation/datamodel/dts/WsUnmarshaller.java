/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.validation.datamodel.dts;

import eu.epsos.validation.datamodel.common.DetailedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;


/**
 * This class provides data transfer services in the form of unmarshall
 * operations. It allows the conversion of a XML web service response to the
 * correspondent set of java objects.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class WsUnmarshaller {

    private static final Logger logger = LoggerFactory.getLogger(WsUnmarshaller.class);

    /**
     * This method performs an unmarshall operation with the provided XML
     * response.
     *
     * @param xmlDetails the web-service response in the form of XML String.
     * @return a filled DetailedResult object.
     */
    public static DetailedResult unmarshal(String xmlDetails) {

        if (xmlDetails == null) {
            logger.error("The provided XML String to unmarshall object is null.");
        }

        if (xmlDetails.isEmpty()) {
            logger.error("The provided XML String object to unmarshall is empty.");
        }

        InputStream is = new ByteArrayInputStream(xmlDetails.getBytes());
        DetailedResult result = null;
        Unmarshaller unmarshaller = null;
        JAXBContext jc = null;

        try {
            jc = JAXBContext.newInstance(DetailedResult.class);
        } catch (JAXBException ex) {
            logger.error(null, ex);
        }

        try {
            unmarshaller = jc.createUnmarshaller();
        } catch (JAXBException ex) {
            logger.error(null, ex);
        }

        try {
            result = (DetailedResult) unmarshaller.unmarshal(is);
        } catch (JAXBException ex) {
            logger.error(null, ex);
        }

        return result;
    }

    /**
     * This method performs marshall operation with the provided object.
     *
     * @param xmlDetails the web-service response in the form of XML String.
     * @return a filled DetailedResult object.
     */
    public static String marshal(DetailedResult detailedResult) {

        if (detailedResult == null) {
            logger.error("The provided object to marshall is null.");
        }

        String result;
        JAXBContext context = null;
        Marshaller m = null;

        try {
            context = JAXBContext.newInstance(detailedResult.getClass());
        } catch (JAXBException ex) {
            logger.error(null, ex);
        }

        try {
            m = context.createMarshaller();
        } catch (JAXBException ex) {
            logger.error(null, ex);
        }

        try {
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (PropertyException ex) {
            logger.error(null, ex);
        }

        StringWriter writer = new StringWriter();
        try {
            m.marshal(detailedResult, writer);
        } catch (JAXBException ex) {
            logger.error(null, ex);
        }

        result = writer.toString();

        return result;
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private WsUnmarshaller() {
    }
}
