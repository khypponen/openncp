/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * <p>
 * This file is part of SRDC epSOS NCP.
 * <p>
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * SRDC epSOS NCP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.securityman.validators;

import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.securityman.exceptions.MissingFieldException;

public class RequiredFieldValidators {

    public static Logger logger = LoggerFactory.getLogger(RequiredFieldValidators.class);

    public static void validateVersion(Assertion assertion) throws MissingFieldException {
        if (assertion.getVersion() == null) {
            throw (new MissingFieldException("Version attribute is required."));
        }
    }

    public static void validateID(Assertion assertion) throws MissingFieldException {
        if (assertion.getID() == null) {
            throw (new MissingFieldException("ID attribute is required."));
        }
    }

    public static void validateIssueInstant(Assertion assertion) throws MissingFieldException {
        if (assertion.getIssueInstant() == null) {
            throw (new MissingFieldException("IssueInstant attribute is required."));
        }
    }

    public static void validateIssuer(Assertion assertion) throws MissingFieldException {
        if (assertion.getIssuer() == null) {
            throw (new MissingFieldException("Issuer attribute is required."));
        }
    }

    public static void validateSubject(Assertion assertion) throws MissingFieldException {
        if (assertion.getSubject() == null) {
            throw (new MissingFieldException("Subject element is required."));
        }
    }

    public static void validateNameID(Assertion assertion) throws MissingFieldException {
        if (assertion.getSubject().getNameID() == null) {
            throw (new MissingFieldException("NameID element is required."));
        }
    }

    public static void validateFormat(Assertion assertion) throws MissingFieldException {
        if (assertion.getSubject().getNameID().getFormat() == null) {
            throw (new MissingFieldException("Format attribute is required."));
        }
    }

    public static void validateSubjectConfirmation(Assertion assertion) throws MissingFieldException {
        if (assertion.getSubject().getSubjectConfirmations().size() == 0) {
            throw (new MissingFieldException("SubjectConfirmation element is required."));
        }
    }

    public static void validateMethod(Assertion assertion) throws MissingFieldException {
        if (assertion.getSubject().getSubjectConfirmations().get(0).getMethod() == null) {
            throw (new MissingFieldException("Method attribute is required."));
        }
    }

    public static void validateConditions(Assertion assertion) throws MissingFieldException {
        if (assertion.getConditions() == null) {
            throw (new MissingFieldException("Conditions element is required."));
        }
    }

    public static void validateNotBefore(Assertion assertion) throws MissingFieldException {
        if (assertion.getConditions().getNotBefore() == null) {
            throw (new MissingFieldException("NotBefore attribute is required."));
        }
    }

    public static void validateNotOnOrAfter(Assertion assertion) throws MissingFieldException {
        if (assertion.getConditions().getNotOnOrAfter() == null) {
            throw (new MissingFieldException("NotOnOrAfter attribute is required."));
        }
    }

    public static void validateAuthnStatement(Assertion assertion) throws MissingFieldException {
        if (assertion.getAuthnStatements().size() == 0) {
            throw (new MissingFieldException("AuthnStatement element is required."));
        }
    }

    public static void validateAuthnInstant(Assertion assertion) throws MissingFieldException {
        if (assertion.getAuthnStatements().get(0).getAuthnInstant() == null) {
            throw (new MissingFieldException("AuthnInstant attribute is required."));
        }
    }

    public static void validateAuthnContext(Assertion assertion) throws MissingFieldException {
        if (assertion.getAuthnStatements().get(0).getAuthnContext() == null) {
            throw (new MissingFieldException("AuthnContext element is required."));
        }
    }

    public static void validateAuthnContextClassRef(Assertion assertion) throws MissingFieldException {
        if (assertion.getAuthnStatements().get(0).getAuthnContext().getAuthnContextClassRef() == null) {
            throw (new MissingFieldException("AuthnContextClassRef element is required."));
        }
    }

    public static void validateAttributeStatement(Assertion assertion) throws MissingFieldException {
        if (assertion.getAttributeStatements().size() == 0) {
            throw (new MissingFieldException("AttributeStatement element is required."));
        }
    }

    public static void validateSignature(Assertion assertion) throws MissingFieldException {
        if (assertion.getSignature() == null) {
            throw (new MissingFieldException("Signature element is required."));
        }
    }

    public static void validateAdvice(Assertion assertion) throws MissingFieldException {
        if (assertion.getAdvice() == null) {
            throw (new MissingFieldException("Advice element is required."));
        }
    }

    public static void validateAssertionIdRef(Assertion assertion) throws MissingFieldException {
        if (assertion.getAdvice().getAssertionIDReferences().size() == 0) {
            throw (new MissingFieldException("AssertionIdRef element is required."));
        }
    }
}
