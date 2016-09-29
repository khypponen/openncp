package eu.europa.ec.sante.ehdsi.openncp.policymanager;

import org.opensaml.saml2.core.Assertion;

import eu.epsos.assertionvalidator.PolicyManagerInterface;
import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;
import tr.com.srdc.epsos.securityman.exceptions.InvalidFieldException;
import tr.com.srdc.epsos.securityman.exceptions.MissingFieldException;

public class DefaultPolicyManagerImpl implements PolicyManagerInterface {

	@Override
	public void XSPASubjectValidatorForHCP(final Assertion assertion, String documentClass)
			throws MissingFieldException, InvalidFieldException {
		// TODO Auto-generated method stub

	}

	@Override
	public void XSPASubjectValidatorForTRC(final Assertion assertion, String documentClass)
			throws MissingFieldException, InvalidFieldException {
		// TODO Auto-generated method stub

	}

	@Override
	public void XSPARoleValidator(final Assertion assertion, String documentClass)
			throws MissingFieldException, InvalidFieldException {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnBehalfOfValidator(final Assertion assertion, String documentClass)
			throws MissingFieldException, InvalidFieldException {
		// TODO Auto-generated method stub

	}

	@Override
	public void HealthcareFacilityValidator(final Assertion assertion, String documentClass)
			throws MissingFieldException, InvalidFieldException {
		// TODO Auto-generated method stub

	}

	@Override
	public void PurposeOfUseValidator(final Assertion assertion, String documentClass)
			throws MissingFieldException, InsufficientRightsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void XSPALocalityValidator(Assertion assertion, String documentClass)
			throws MissingFieldException, InvalidFieldException {
		// TODO Auto-generated method stub

	}

	@Override
	public void XCPDPermissionValidator(Assertion assertion) throws InsufficientRightsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void XCAPermissionValidator(Assertion assertion, String documentClass)
			throws InsufficientRightsException, MissingFieldException {
		// TODO Auto-generated method stub

	}

	@Override
	public void XDRPermissionValidator(Assertion assertion, String documentClass)
			throws MissingFieldException, InvalidFieldException, InsufficientRightsException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isConsentGiven(String patientId, String countryId) {
		// TODO Auto-generated method stub
		return false;
	}
}
