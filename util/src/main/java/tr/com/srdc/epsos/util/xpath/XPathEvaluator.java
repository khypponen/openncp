/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.util.xpath;

import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XPathEvaluator {

    private XPathFactory xpf = null;
    private XPath xPath = null;
    XPathExpression xPathExp = null;

    public XPathEvaluator(HashMap namespaces, String xPathExpStr) {
        this(namespaces);

        try {
            xPathExp = xPath.compile(xPathExpStr);
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public XPathEvaluator(HashMap namespaces) {
        init();
        xPath.setNamespaceContext(new NamespaceContextImpl(namespaces));
    }

    private void init() {
        if (xpf == null) {
            xpf = XPathFactory.newInstance();
            xPath = xpf.newXPath();
            //System.err.println("Loaded XPath Provider " + xPath.getClass().getName());
        }
    }

    public NodeList evaluate(Document doc) {
        NodeList matchedNodes = null;
        try {
            if (xPathExp != null) {
                matchedNodes = (NodeList) xPathExp.evaluate(doc, XPathConstants.NODESET);
            }
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return matchedNodes;
    }

    public NodeList evaluate(Document doc, String xPathExpStr) {
        NodeList matchedNodes = null;
        try {
            XPathExpression localXPathExp = xPath.compile(xPathExpStr);
            matchedNodes = (NodeList) localXPathExp.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return matchedNodes;
    }
}
