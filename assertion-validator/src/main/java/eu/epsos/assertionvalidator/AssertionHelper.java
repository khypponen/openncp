package eu.epsos.assertionvalidator;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;
import tr.com.srdc.epsos.securityman.exceptions.MissingFieldException;

import java.util.List;


/**
 * Created:
 * Date: 2012-11-26
 * Time: 14:41
 * By: fredrik.dahlman@cag.se
 */
public class AssertionHelper {

    private static final Logger logger = LoggerFactory.getLogger(AssertionHelper.class);

    /**
     * Get attribute value from assertion.
     *
     * @param assertion the assertion
     * @param attribute the attribute to search for
     * @return the attribute value
     * @throws MissingFieldException If attribute is missing
     */
    public static String getAttributeFromAssertion(Assertion assertion, String attribute) throws MissingFieldException {
        for (AttributeStatement as : assertion.getAttributeStatements()) {
            for (Attribute a : as.getAttributes()) {
                if (a.getName().equals(attribute)) {
                    if (a.getAttributeValues().size() > 0) {
                        return a.getAttributeValues().get(0).getDOM().getTextContent();
                    } else {
                        throw new MissingFieldException("'" + attribute + "' - attribute should contain AttributeValue element.");
                    }
                }
            }
        }
        throw new MissingFieldException("'" + attribute + "' - attribute should contain AttributeValue element.");
    }

    /**
     * Get attribute values from assertion.
     *
     * @param assertion the assertion
     * @param attribute the attribute to search for
     * @return the attribute values
     * @throws MissingFieldException If attribute is missing
     */
    public static List<XMLObject> getAttributeValuesFromAssertion(Assertion assertion, String attribute) throws MissingFieldException {
        for (AttributeStatement as : assertion.getAttributeStatements()) {
            for (Attribute a : as.getAttributes()) {
                if (a.getName().equals(attribute)) {
                    return a.getAttributeValues();
                }
            }
        }
        throw new MissingFieldException("'" + attribute + "' - attribute should contain AttributeValue element.");
    }

    public static List<XMLObject> getPermissionValuesFromAssertion(Assertion assertion) throws InsufficientRightsException {
        try {
            return getAttributeValuesFromAssertion(assertion, PolicyManagerInterface.URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION);
        } catch (MissingFieldException e) {
            // this is to get the behavior as before...
            logger.error("InsufficientRightsException");
            throw new InsufficientRightsException(4703);
        }
    }
}
