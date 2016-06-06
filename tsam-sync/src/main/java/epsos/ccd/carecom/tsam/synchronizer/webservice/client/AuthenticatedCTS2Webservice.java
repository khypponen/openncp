package epsos.ccd.carecom.tsam.synchronizer.webservice.client;

import com.sun.xml.ws.client.ClientTransportException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;

import epsos.ccd.carecom.tsam.synchronizer.UnableToConvertException;
import epsos.ccd.carecom.tsam.synchronizer.Util;
import eu.epsos.util.net.ProxyUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

/**
 * Implements a decoration of the web service by adding the user credentials,
 * delta dates. Provides a method for each web method that also handle
 * conversion of data types.
 */
public class AuthenticatedCTS2Webservice {
	private CommonTerminologyService20_Service service;
	private final CommonTerminologyService20 port;
	private final XMLGregorianCalendar deltaStartDate;
	private final XMLGregorianCalendar deltaEndDate;

	/**
	 * Default constructor that instantiates the web service, adds
	 * authentication, and some default filter data.
	 * 
	 * @param webserviceURL
	 *            {@link URL} pointing to the location of the web service.
	 * @param userName
	 *            The user id of the user that has access to the web service.
	 * @param password
	 *            The corresponding password to the user id.
	 * @param deltaStartDate
	 *            Delta start date.
	 * @param deltaEndDate
	 *            Delta end date.
	 * @throws InaccessibleWebServiceURLException
	 *             Thrown when the web service is not accessible at the location
	 *             specified by the URL.
	 * @throws UnableToConvertException
	 *             Thrown if the one of the delta dates cannot be converted to
	 *             {@link XMLGregorianCalendar}.
	 */
	public AuthenticatedCTS2Webservice(URL webserviceURL, String userName, String password, Date deltaStartDate,
			Date deltaEndDate) throws InaccessibleWebServiceURLException, UnableToConvertException {

		ProxyUtil.initProxyConfiguration();

		// The URL object can validate a correct URL but it cannot verify that
		// the web service is available at the location
		// specified by the URL. We need to handle a situation when the URL is
		// pointing at wrong location, which is possible
		// as the URL is user input.
		try {
			this.service = new CommonTerminologyService20_Service(webserviceURL,
					new QName("http://cts2.webservice.ht.carecom.dk/", "CommonTerminologyService2.0"));
		} catch (InaccessibleWSDLException e) {
			throw new InaccessibleWebServiceURLException(webserviceURL, e);
		}
		this.port = this.service.getCommonTerminologyService20Port();

		BindingProvider bindingProvider = (BindingProvider) this.port;
		bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, userName);
		bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
		bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webserviceURL.toString());

		this.deltaStartDate = Util.convertDateToXMLGregorianCalendar(deltaStartDate);
		this.deltaEndDate = Util.convertDateToXMLGregorianCalendar(deltaEndDate);
	}

	/**
	 * Convenient overloaded class that calls the the original method with all
	 * arguments as null.
	 * 
	 * @return A list of found code systems.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 * @see getCodeSystems(List<Long> codeSystemIDs,List
	 *      <String> codeSystemOIDs,String codeSystemName,Date
	 *      codeSystemsValidFrom,String codeSystemDescription)
	 */
	public List<CodeSystem> getCodeSystems() throws IncorrectUserCredentialsException, ClientTransportException,
			HealthTermWebServiceException_Exception {
		return this.getCodeSystems(null, null, null, null, null);
	}

	/**
	 * Performs a call to the web method with the arguments as a filter to
	 * obtain a list of code systems.
	 * 
	 * @param codeSystemIDs
	 *            A list of code system IDs.
	 * @param codeSystemOIDs
	 *            A list of code system OIDs.
	 * @param codeSystemName
	 *            A name of a code system.
	 * @param codeSystemsValidFrom
	 *            The date from which the returned code system(s) are valid.
	 * @param codeSystemDescription
	 *            A description of a code system.
	 * @return A list of found code systems.
	 * @throws UnableToConvertException
	 *             Thrown when the <code>codeSystemValidFrom</code> argument
	 *             cannot be converted to a {@link XMLGregorianCalendar}.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 */
	public List<CodeSystem> getCodeSystems(List<Long> codeSystemIDs, List<String> codeSystemOIDs, String codeSystemName,
			Date codeSystemsValidFrom, String codeSystemDescription) throws UnableToConvertException,
			IncorrectUserCredentialsException, ClientTransportException, HealthTermWebServiceException_Exception {
		try {
			return this.port.listCodeSystems(codeSystemIDs, codeSystemOIDs, codeSystemName,
					Util.convertDateToXMLGregorianCalendar(codeSystemsValidFrom), codeSystemDescription,
					this.deltaStartDate, this.deltaEndDate);
		} catch (ClientTransportException e) {
			// We know the cause of this type of exception
			if (e.getArguments() != null && Arrays.asList(e.getArguments()).contains(new Integer(401))) {
				throw new IncorrectUserCredentialsException("getCodeSystems", e);
			}
			// The rest is out of our control so we re-throw the exception.
			throw e;
		}
	}

	/**
	 * Overloaded method that calls the original method with default values.
	 * 
	 * @param codeSystemIds
	 *            A list of code system IDs.
	 * @return A list of value sets.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 */
	public List<ValueSet> getValueSets(List<Long> codeSystemIds) throws IncorrectUserCredentialsException,
			ClientTransportException, HealthTermWebServiceException_Exception {
		return this.getValueSets(codeSystemIds, null, null, null, null, null, null);
	}

	/**
	 * Performs a call to a web method with the arguments as a filter to obtain
	 * a list of value sets.
	 * 
	 * @param codeSystemIDs
	 *            A list of code system IDs.
	 * @param codeSystemOIDs
	 *            A list of code system OIDs.
	 * @param valueSetIDs
	 *            A list of value set IDs.
	 * @param valueSetOIDs
	 *            A list of value set OIDs.
	 * @param valueSetName
	 *            A value set name.
	 * @param valueSetsValidFrom
	 *            The date from which all returned value sets are valid.
	 * @param valueSetStatus
	 *            A list of status' that the returned value sets have.
	 * @return A list of value sets.
	 * @throws UnableToConvertException
	 *             Thrown when the <code>valueSetsValidFrom</code> argument
	 *             cannot be converted to a {@link XMLGregorianCalendar}.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 */
	public List<ValueSet> getValueSets(List<Long> codeSystemIDs, List<String> codeSystemOIDs, List<Long> valueSetIDs,
			List<String> valueSetOIDs, String valueSetName, Date valueSetsValidFrom, List<Integer> valueSetStatus)
			throws UnableToConvertException, IncorrectUserCredentialsException, ClientTransportException,
			HealthTermWebServiceException_Exception {
		try {
			return this.port.listValueSets(codeSystemIDs, codeSystemOIDs, valueSetIDs, valueSetOIDs, valueSetName,
					Util.convertDateToXMLGregorianCalendar(valueSetsValidFrom), valueSetStatus, this.deltaStartDate,
					this.deltaEndDate);
		} catch (ClientTransportException e) {
			// We know the cause of this type of exception
			if (e.getArguments() != null && Arrays.asList(e.getArguments()).contains(new Integer(401))) {
				throw new IncorrectUserCredentialsException("getValueSets", e);
			}
			// The rest is out of our control so we re-throw the exception.
			throw e;
		}
	}

	/**
	 * Overloaded method that calls the original method with default values.
	 * 
	 * @param codeSystemIds
	 *            A list of code system IDs.
	 * @param valueSetIds
	 *            A list of value set IDs.
	 * @return A list of concepts.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 */
	public List<CodeSystemConcept> getConcepts(List<Long> codeSystemIds, List<Long> valueSetIds)
			throws IncorrectUserCredentialsException, ClientTransportException,
			HealthTermWebServiceException_Exception {
		return this.getConcepts(codeSystemIds, null, valueSetIds, null, null, null);
	}

	/**
	 * Performs a call to a web method with the arguments as a filter, to obtain
	 * a list of code system concepts.
	 * 
	 * @param codeSystemIDs
	 *            A list of code system IDs.
	 * @param codeSystemOIDs
	 *            A list of code system OIDs.
	 * @param valueSetIDs
	 *            A list of value set IDs.
	 * @param valueSetOIDs
	 *            A list of value set OIDs.
	 * @param conceptValidFrom
	 *            The date from which all returned concepts are valid.
	 * @param conceptStatus
	 *            A list of status' that the returned concepts have.
	 * @return A list of code system concepts.
	 * @throws UnableToConvertException
	 *             Thrown when the <code>conceptValidFrom</code> argument cannot
	 *             be converted to a {@link XMLGregorianCalendar}.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 */
	public List<CodeSystemConcept> getConcepts(List<Long> codeSystemIDs, List<String> codeSystemOIDs,
			List<Long> valueSetIDs, List<String> valueSetOIDs, Date conceptValidFrom, List<Integer> conceptStatus)
			throws UnableToConvertException, IncorrectUserCredentialsException, ClientTransportException,
			HealthTermWebServiceException_Exception {
		try {
			return this.port.listCodeSystemConcepts(codeSystemIDs, codeSystemOIDs, valueSetIDs, valueSetOIDs,
					Util.convertDateToXMLGregorianCalendar(conceptValidFrom), conceptStatus, this.deltaStartDate,
					this.deltaEndDate);
		} catch (ClientTransportException e) {
			// We know the cause of this type of exception
			if (e.getArguments() != null && Arrays.asList(e.getArguments()).contains(new Integer(401))) {
				throw new IncorrectUserCredentialsException("getConcepts", e);
			}
			// The rest is out of our control so we re-throw the exception.
			throw e;
		}
	}

	/**
	 * Overloaded method that calls the original method with default argument
	 * values.
	 * 
	 * @param codeSystemIds
	 *            A list of code system IDs.
	 * @param conceptIds
	 *            A list of concept IDs.
	 * @return A list of designations.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 */
	public List<Designation> getDesignations(List<Long> codeSystemIds, List<Long> conceptIds)
			throws IncorrectUserCredentialsException, ClientTransportException,
			HealthTermWebServiceException_Exception {
		return this.getDesignations(codeSystemIds, null, conceptIds, null, null, null, null, null);
	}

	/**
	 * Performs a call to a web method with the arguments as a filter, to obtain
	 * a list of designations.
	 * 
	 * @param codeSystemIDs
	 *            A list of code system IDs.
	 * @param codeSystemOIDs
	 *            A list of code system OIDs.
	 * @param conceptIDs
	 *            A list of concept IDs.
	 * @param conceptExternalCodes
	 *            A list of concept external codes.
	 * @param designationTypes
	 *            A list of types the the returned designations have.
	 * @param designationLanguageCodes
	 *            A list of designation codes.
	 * @param designationValidFrom
	 *            The date from which all returned designations are valid.
	 * @param designationStatus
	 *            A list of status' the the returned designations have.
	 * @return A list of designations.
	 * @throws UnableToConvertException
	 *             Thrown when the <code>designationValidFrom</code> argument
	 *             cannot be converted to a {@link XMLGregorianCalendar}.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 */
	public List<Designation> getDesignations(List<Long> codeSystemIDs, List<String> codeSystemOIDs,
			List<Long> conceptIDs, List<String> conceptExternalCodes, List<Integer> designationTypes,
			List<String> designationLanguageCodes, Date designationValidFrom, List<Integer> designationStatus)
			throws UnableToConvertException, IncorrectUserCredentialsException, ClientTransportException,
			HealthTermWebServiceException_Exception {
		try {
			return this.port.listDesignations(codeSystemIDs, codeSystemOIDs, conceptIDs, conceptExternalCodes,
					designationTypes, designationLanguageCodes,
					Util.convertDateToXMLGregorianCalendar(designationValidFrom), designationStatus,
					this.deltaStartDate, this.deltaEndDate);
		} catch (ClientTransportException e) {
			// We know the cause of this type of exception
			if (e.getArguments() != null && Arrays.asList(e.getArguments()).contains(new Integer(401))) {
				throw new IncorrectUserCredentialsException("getDesignations", e);
			}
			// The rest is out of our control so we re-throw the exception.
			throw e;
		}
	}

	/**
	 * Overloaded method that calls the original method with all arguments as
	 * null pointers.
	 * 
	 * @param sourceConceptIDs
	 *            A list of source code system IDs.
	 * @param targetConcetIDs
	 *            A list of target code system IDs.
	 * @return A list of transcodings.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 */
	public List<Transcoding> getTranscodings(List<Long> sourceConceptIDs, List<Long> targetConcetIDs)
			throws IncorrectUserCredentialsException, ClientTransportException,
			HealthTermWebServiceException_Exception {
		return this.getTranscodings(sourceConceptIDs, null, targetConcetIDs, null, null, null);
	}

	/**
	 * Performs a call to a web method with the arguments as filter, to obtain a
	 * list of transcodings.
	 * 
	 * @param sourceConceptIDs
	 *            A list of source code system IDs.
	 * @param sourceConceptExternalCodes
	 *            A list of source concept external codes.
	 * @param targetConcetIDs
	 *            A list of target code system IDs.
	 * @param targetConceptExternalCodes
	 *            A list of target concept external codes.
	 * @param transcodingValidFrom
	 *            The date from which all returned transcodings are valid.
	 * @param transcodingStatus
	 *            A list of status' the the returned transcodings have.
	 * @return A list of transcodings.
	 * @throws UnableToConvertException
	 *             Thrown when the <code>transcodingValidFrom</code> argument
	 *             cannot be converted to a {@link XMLGregorianCalendar}.
	 * @throws IncorrectUserCredentialsException
	 *             Thrown when the a call to the web service returns a HTTP 401
	 *             error code, meaning that the user is unauthorized.
	 * @throws ClientTransportException
	 *             Thrown when the web service returns any other HTTP error
	 *             code.
	 * @throws HealthTermWebServiceException_Exception
	 *             Thrown by the HealthTerm server is something went wrong on
	 *             the server side.
	 */
	public List<Transcoding> getTranscodings(List<Long> sourceConceptIDs, List<String> sourceConceptExternalCodes,
			List<Long> targetConcetIDs, List<String> targetConceptExternalCodes, Date transcodingValidFrom,
			List<Integer> transcodingStatus) throws UnableToConvertException, IncorrectUserCredentialsException,
			ClientTransportException, HealthTermWebServiceException_Exception {
		try {
			return this.port.listTranscodings(sourceConceptIDs, sourceConceptExternalCodes, targetConcetIDs,
					targetConceptExternalCodes, Util.convertDateToXMLGregorianCalendar(transcodingValidFrom),
					transcodingStatus, this.deltaStartDate, this.deltaEndDate);
		} catch (ClientTransportException e) {
			// We know the cause of this type of exception
			if (e.getArguments() != null && Arrays.asList(e.getArguments()).contains(new Integer(401))) {
				throw new IncorrectUserCredentialsException("getTranscodings", e);
			}
			// The rest is out of our control so we re-throw the exception.
			throw e;
		}
	}
}
