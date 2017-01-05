/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * <p>
 * This file is part of SRDC epSOS NCP.
 * <p>
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Modifications by Kela (The Social Insurance Institution of Finland) GNU
 * Public License v3
 */
package tr.com.srdc.epsos.securityman.helper;

import org.opensaml.common.xml.SAMLSchemaBuilder;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.com.srdc.epsos.securityman.exceptions.MissingFieldException;
import tr.com.srdc.epsos.util.saml.SAML;

import javax.xml.transform.dom.DOMSource;

/**
 * TODO: improve the implementation by implementing a method which picks
 * attribute values by attribute names (avoid repetition in current methods)
 */
public class Helper {

    public static Logger logger = LoggerFactory.getLogger(Helper.class);

    public static Assertion getHCPAssertion(Element sh) {
        try {
            // TODO: Since the XCA simulator sends this value in a wrong way, we are trying like this for the moment
            NodeList securityList = sh.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
            //NodeList securityList = sh.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0","Security");
            Element security = null;
            if (securityList.getLength() > 0) {
                security = (Element) securityList.item(0);
            } else {
                throw (new MissingFieldException("Security element is required."));
            }
            NodeList assertionList = security.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion");
            Element hcpAss = null;
            Assertion hcpAssertion = null;

            if (assertionList.getLength() > 0) {
                for (int i = 0; i < assertionList.getLength(); i++) {
                    hcpAss = (Element) assertionList.item(i);
                    SAMLSchemaBuilder.getSAML11Schema().newValidator().validate(new DOMSource(hcpAss));

                    hcpAssertion = (Assertion) SAML.fromElement(hcpAss);
                    if (hcpAssertion.getAdvice() == null) {
                        break;
                    }
                }
            }
            if (hcpAssertion == null) {
                throw (new MissingFieldException("HCP Assertion element is required."));
            }
            return hcpAssertion;

        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static String getUserID(Element sh) {
        String result = "N/A";

        try {
            Assertion assertion = getHCPAssertion(sh);
            String val = assertion.getSubject().getNameID().getValue();
            if (val != null && !val.equals("")) {
                result = val;
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }

        return result;
    }

    public static String getAlternateUserID(Element sh) {
        String result = getXSPAAttributeByName(sh, "urn:oasis:names:tc:xacml:1.0:subject:subject-id", false);
        if (result == null) {
            return "N/A";
        }
        return result;
    }

    public static String getRoleID(Element sh) {
        String result = getXSPAAttributeByName(sh, "urn:oasis:names:tc:xacml:2.0:subject:role", false);
        if (result == null) {
            return "N/A";
        }
        return result;
    }

    public static String getPC_UserID(Element sh) {
        String result = getXSPAAttributeByName(sh, "urn:oasis:names:tc:xspa:1.0:subject:organization", false);
        if (result == null) {
            return "N/A";
        }
        return result;
    }

    public static String getOrganizationId(Element sh) {
        String result = getXSPAAttributeByName(sh, "urn:oasis:names:tc:xspa:1.0:subject:organization-id", false);
        if (result == null) {
            return "N/A";
        }
        return result;
    }

    public static String getOrganization(Element sh) {
        String result = getXSPAAttributeByName(sh, "urn:oasis:names:tc:xspa:1.0:subject:organization", false);
        if (result == null) {
            return "N/A";
        }
        return result;
    }

    public static String getPC_RoleID(Element sh) {
        String result = getXSPAAttributeByName(sh, "urn:epsos:names:wp3.4:subject:healthcare-facility-type", false);
        if (result == null) {
            return "N/A";
        }
        return result;
    }

    /**
     * @author Konstantin.Hypponen@kela.fi
     */
    public static Assertion getTRCAssertion(Element sh) {
        try {
            NodeList securityList = sh.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
            Element security = null;
            if (securityList.getLength() > 0) {
                security = (Element) securityList.item(0);
            } else {
                throw (new MissingFieldException("Security element is required."));
            }
            NodeList assertionList = security.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion");
            Element trcAss = null;
            Assertion trcAssertion = null;

            if (assertionList.getLength() > 0) {
                for (int i = 0; i < assertionList.getLength(); i++) {
                    trcAss = (Element) assertionList.item(i);

                    trcAssertion = (Assertion) SAML.fromElement(trcAss);
                    if (trcAssertion.getAdvice() != null) {
                        break;
                    }
                }
            }
            if (trcAssertion == null) {
                throw (new MissingFieldException("TRC Assertion element is required."));
            }
            return trcAssertion;

        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * Extracts XDS-formatted patient ID from TRCAssertion
     *
     * @param sh SOAP header which includes TRC assertion
     * @return Patient ID in XDS format
     */
    public static String getDocumentEntryPatientIdFromTRCAssertion(Element sh) {
        String patientId = getXSPAAttributeByName(sh, "urn:oasis:names:tc:xacml:1.0:resource:resource-id", true);
        if (patientId == null) {
            logger.error("Patient ID not found in TRC assertion");
        }
        return patientId;
    }

    /**
     *
     * @param sh SOAP Header
     * @param attributeName Attribute name
     * @param trc true, if attribute should be picked from TRC assertion
     * @return attribute value
     */
    private static String getXSPAAttributeByName(Element sh, String attributeName, boolean trc) {
        String result = null;

        try {
            Assertion assertion = null;
            if (trc == true) {
                assertion = getTRCAssertion(sh);
            } else {
                assertion = getHCPAssertion(sh);
            }
            if (assertion == null) {
                return null;
            }
            for (Attribute attr : assertion.getAttributeStatements().get(0).getAttributes()) {
                if (attr.getName().equals(attributeName)) {
                    String val = attr.getAttributeValues().get(0).getDOM().getTextContent();
                    if (val != null && !val.equals("")) {
                        result = val;
                    }
                }
            }
        } catch (Exception e) {
            String assertionType = trc ? "TRC" : "HCP";
            logger.error("XSPA attribute " + attributeName + " not found in " + assertionType + " assertion");
            logger.debug(e.getMessage());
        }

        return result;
    }
}
