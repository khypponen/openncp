/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2013  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
package eu.epsos.pt.eadc.helper;

import eu.epsos.pt.eadc.datamodel.ObjectFactory;
import eu.epsos.pt.eadc.datamodel.Transaction;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class gathers a set of utilities to manipulate eADC Transactions. For
 * instance, you can convert a Transaction object into a Document (DOM) or
 * insert a CDA Document into a Transaction Document or object.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class TransactionHelper {

    /**
     * This method will insert a CDA document into a Transaction object,
     * producing a Transaction Document.
     *
     * @param transaction the Transaction object
     * @param cda the CDA document
     *
     * @return a Document containing the Transaction Information combined with
     * the CDA document
     *
     * @throws JAXBException thrown by the Transaction conversion process
     * @throws ParserConfigurationException thrown by the Transaction conversion
     * process
     */
    public static Document insertCdaInTransaction(Transaction transaction, Document cda) throws JAXBException, ParserConfigurationException {
        return insertCdaInTransaction(convertTransaction(transaction), cda);
    }

    /**
     * This method will insert a CDA document into a Transaction document. It
     * will add it as an extra node, at the same level as Transaction Info
     * element.
     *
     * @param transaction the Transaction document that will receive the CDA
     * document
     * @param cda the CDA document to be inserted
     * @return a Transaction object filled with a CDA document also
     */
    public static Document insertCdaInTransaction(Document transaction, final Document cda) {

        /* SET-UP */

        Node transactionRoot;

        /* BODY */

        transactionRoot = transaction.getDocumentElement();
        transactionRoot.appendChild(transaction.importNode(cda.getDocumentElement(), true));

        return transaction;
    }

    /**
     * Converts a given transaction object into a Document object (Marshal
     * operation).
     *
     * @param transaction the transaction object to be converted
     * @return a Document object representing the transaction
     *
     * @throws JAXBException thrown by the conversion of the transaction object
     * to the JAXB element
     * @throws ParserConfigurationException
     */
    public static Document convertTransaction(final Transaction transaction) throws JAXBException, ParserConfigurationException {

        /* SETUP */

        DocumentBuilderFactory dbf;
        Document result;
        JAXBContext jaxbContext;
        Marshaller marshaller;
        JAXBElement<Transaction> transElem;

        /* BODY */

        dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        result = dbf.newDocumentBuilder().newDocument(); // Create document to hold Transaction

        jaxbContext = JAXBContext.newInstance("eu.epsos.pt.eadc.datamodel");
        transElem = (new ObjectFactory()).createTransaction(transaction); // Create Transaction element

        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(transElem, result); // Convert transaction element into resulting document

        return result;
    }
}
