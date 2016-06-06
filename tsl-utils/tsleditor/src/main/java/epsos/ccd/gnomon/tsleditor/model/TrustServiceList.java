/***Licensed to the Apache Software Foundation (ASF) under one
*or more contributor license agreements.  See the NOTICE file
*distributed with this work for additional information
*regarding copyright ownership.  The ASF licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
**/package epsos.ccd.gnomon.tsleditor.model;

import epsos.ccd.gnomon.tsleditor.ValidatorUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignContext;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.utils.Constants;
import org.apache.xpath.XPathAPI;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
import org.etsi.uri._01903.v1_3.CertIDListType;
import org.etsi.uri._01903.v1_3.CertIDType;
import org.etsi.uri._01903.v1_3.DigestAlgAndValueType;
import org.etsi.uri._01903.v1_3.QualifyingPropertiesType;
import org.etsi.uri._01903.v1_3.SignedPropertiesType;
import org.etsi.uri._01903.v1_3.SignedSignaturePropertiesType;
import org.etsi.uri._02231.v2_.AdditionalInformationType;
import org.etsi.uri._02231.v2_.AddressType;
import org.etsi.uri._02231.v2_.AnyType;
import org.etsi.uri._02231.v2_.DigitalIdentityListType;
import org.etsi.uri._02231.v2_.DigitalIdentityType;
import org.etsi.uri._02231.v2_.ElectronicAddressType;
import org.etsi.uri._02231.v2_.InternationalNamesType;
import org.etsi.uri._02231.v2_.MultiLangNormStringType;
import org.etsi.uri._02231.v2_.MultiLangStringType;
import org.etsi.uri._02231.v2_.NextUpdateType;
import org.etsi.uri._02231.v2_.NonEmptyMultiLangURIListType;
import org.etsi.uri._02231.v2_.NonEmptyMultiLangURIType;
import org.etsi.uri._02231.v2_.NonEmptyURIListType;
import org.etsi.uri._02231.v2_.ObjectFactory;
import org.etsi.uri._02231.v2_.OtherTSLPointerType;
import org.etsi.uri._02231.v2_.OtherTSLPointersType;
import org.etsi.uri._02231.v2_.PolicyOrLegalnoticeType;
import org.etsi.uri._02231.v2_.PostalAddressListType;
import org.etsi.uri._02231.v2_.PostalAddressType;
import org.etsi.uri._02231.v2_.ServiceDigitalIdentityListType;
import org.etsi.uri._02231.v2_.TSLSchemeInformationType;
import org.etsi.uri._02231.v2_.TSPType;
import org.etsi.uri._02231.v2_.TrustServiceProviderListType;
import org.etsi.uri._02231.v2_.TrustStatusListType;
import org.joda.time.DateTime;
import org.w3._2000._09.xmldsig_.DigestMethodType;
import org.w3._2000._09.xmldsig_.X509IssuerSerialType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI;

public class TrustServiceList {

	private static final Log LOG = LogFactory.getLog(TrustServiceList.class);

	private static final String XADES_TYPE = "http://uri.etsi.org/01903#SignedProperties";

	public static final String SCHEME_RULE_COMMON = "http://uri.etsi.org/TrstSvc/eSigDir-1999-93-EC-TrustedList/schemerules/common";

	private TrustStatusListType trustStatusList;

	public Document tslDocument;

	private File tslFile;

	private List<TrustServiceProvider> trustServiceProviders;

	private boolean changed;

	private final List<ChangeListener> changeListeners;

	private final ObjectFactory objectFactory;

	private final DatatypeFactory datatypeFactory;

	private final org.etsi.uri._01903.v1_3.ObjectFactory xadesObjectFactory;

	private final org.w3._2000._09.xmldsig_.ObjectFactory xmldsigObjectFactory;

	private final org.etsi.uri._02231.v2.additionaltypes_.ObjectFactory tslxObjectFactory;

	protected TrustServiceList() {
		super();
		this.changed = true;
		this.changeListeners = new LinkedList<ChangeListener>();
		this.objectFactory = new ObjectFactory();
		this.xadesObjectFactory = new org.etsi.uri._01903.v1_3.ObjectFactory();
		this.xmldsigObjectFactory = new org.w3._2000._09.xmldsig_.ObjectFactory();
		this.tslxObjectFactory = new org.etsi.uri._02231.v2.additionaltypes_.ObjectFactory();
		try {
			this.datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException("datatype config error: "
					+ e.getMessage(), e);
		}
                this.trustStatusList = new TrustStatusListType();
	}

	protected TrustServiceList(TrustStatusListType trustStatusList,
			Document tslDocument, File tslFile) {
		this.trustStatusList = trustStatusList;
		this.tslDocument = tslDocument;
		this.tslFile = tslFile;
		this.changeListeners = new LinkedList<ChangeListener>();
		this.objectFactory = new ObjectFactory();
		this.xadesObjectFactory = new org.etsi.uri._01903.v1_3.ObjectFactory();
		this.xmldsigObjectFactory = new org.w3._2000._09.xmldsig_.ObjectFactory();
		this.tslxObjectFactory = new org.etsi.uri._02231.v2.additionaltypes_.ObjectFactory();
		try {
			this.datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException("datatype config error: "
					+ e.getMessage(), e);
		}
	}

	public void addChangeListener(ChangeListener changeListener) {
		this.changeListeners.add(changeListener);
	}

	public void removeChangeListener(ChangeListener changeListener) {
		this.changeListeners.remove(changeListener);
	}

	private void notifyChangeListeners() {
		for (ChangeListener changeListener : changeListeners) {
			changeListener.changed();
		}
	}

	public boolean hasChanged() {
		return this.changed;
	}

	public String getSchemeName() {
		Locale locale = Locale.getDefault();
		return getSchemeName(locale);
	}

	/**
	 * Sets the scheme name according to the default locale.
	 * 
	 * @param schemeName
	 */
	public void setSchemeName(String schemeName) {
		Locale locale = Locale.getDefault();
		setSchemeName(schemeName, locale);
	}

	public void setSchemeOperatorName(String schemeOperatorName) {
		Locale locale = Locale.getDefault();
		setSchemeOperatorName(schemeOperatorName, locale);
	}

	private void clearDocumentCacheAndSetChanged() {
		/*
		 * The XML signature should be regenerated anyway, so clear the TSL DOM
		 * object.
		 */
		this.tslDocument = null;
		setChanged();
	}

	private void setChanged() {
		this.changed = true;
		notifyChangeListeners();
	}

	public TrustStatusListType getTrustStatusList() {
		if (null == this.trustStatusList) {
			this.trustStatusList = this.objectFactory
					.createTrustStatusListType();
		}
		return this.trustStatusList;
	}

	private TSLSchemeInformationType getSchemeInformation() {
		TrustStatusListType trustStatusList = getTrustStatusList();
		TSLSchemeInformationType tslSchemeInformation = trustStatusList
				.getSchemeInformation();
		if (null == tslSchemeInformation) {
			tslSchemeInformation = this.objectFactory
					.createTSLSchemeInformationType();
			trustStatusList.setSchemeInformation(tslSchemeInformation);
		}
		return tslSchemeInformation;
	}

	public void setSchemeName(String schemeName, Locale locale) {
		TSLSchemeInformationType tslSchemeInformation = getSchemeInformation();
		InternationalNamesType i18nSchemeName = tslSchemeInformation
				.getSchemeName();
		if (null == i18nSchemeName) {
			i18nSchemeName = this.objectFactory.createInternationalNamesType();
			tslSchemeInformation.setSchemeName(i18nSchemeName);
		}
		TrustServiceListUtils.setValue(schemeName, locale, i18nSchemeName);
		/*
		 * Also notify the listeners that we've changed content.
		 */
		clearDocumentCacheAndSetChanged();
	}

	public void setSchemeOperatorName(String schemeOperatorName, Locale locale) {
		TSLSchemeInformationType tslSchemeInformation = getSchemeInformation();
		InternationalNamesType i18nSchemeOperatorName = tslSchemeInformation
				.getSchemeOperatorName();
		if (null == i18nSchemeOperatorName) {
			i18nSchemeOperatorName = this.objectFactory
					.createInternationalNamesType();
			tslSchemeInformation.setSchemeOperatorName(i18nSchemeOperatorName);
		}
		TrustServiceListUtils.setValue(schemeOperatorName, locale,
				i18nSchemeOperatorName);
		clearDocumentCacheAndSetChanged();
	}

	private AddressType getSchemeOperatorAddress() {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		AddressType schemeOperatorAddress = schemeInformation
				.getSchemeOperatorAddress();
		if (null == schemeOperatorAddress) {
			schemeOperatorAddress = this.objectFactory.createAddressType();
			schemeInformation.setSchemeOperatorAddress(schemeOperatorAddress);
		}
		return schemeOperatorAddress;
	}

	public void setSchemeOperatorPostalAddress(PostalAddressType postalAddress,
			Locale locale) {
		AddressType schemeOperatorAddress = getSchemeOperatorAddress();
		PostalAddressListType postalAddresses = schemeOperatorAddress
				.getPostalAddresses();
		if (null == postalAddresses) {
			postalAddresses = this.objectFactory.createPostalAddressListType();
			schemeOperatorAddress.setPostalAddresses(postalAddresses);
		}
		/*
		 * First try to locate an existing address for the given locale.
		 */
		PostalAddressType existingPostalAddress = null;
		for (PostalAddressType currentPostalAddress : postalAddresses
				.getPostalAddress()) {
			if (currentPostalAddress.getLang().toLowerCase().equals(
					locale.getLanguage())) {
				existingPostalAddress = currentPostalAddress;
				break;
			}
		}
		if (null != existingPostalAddress) {
			/*
			 * Update the existing postal address.
			 */
			existingPostalAddress.setStreetAddress(postalAddress
					.getStreetAddress());
			existingPostalAddress.setPostalCode(postalAddress.getPostalCode());
			existingPostalAddress.setLocality(postalAddress.getLocality());
			existingPostalAddress.setStateOrProvince(postalAddress
					.getStateOrProvince());
			existingPostalAddress
					.setCountryName(postalAddress.getCountryName());
		} else {
			LOG.debug("add postal address: " + locale.getLanguage());
			/*
			 * Add the new postal address. We really have to create a copy into
			 * a new JAXB object. This allows a caller to reuse a postal address
			 * JAXB data structure without running into trouble with the JAXB
			 * marshaller.
			 */
			PostalAddressType newPostalAddress = this.objectFactory
					.createPostalAddressType();
			newPostalAddress.setLang(locale.getLanguage());
			newPostalAddress.setStreetAddress(postalAddress.getStreetAddress());
			newPostalAddress.setPostalCode(postalAddress.getPostalCode());
			newPostalAddress.setLocality(postalAddress.getLocality());
			newPostalAddress.setStateOrProvince(postalAddress
					.getStateOrProvince());
			newPostalAddress.setCountryName(postalAddress.getCountryName());
			postalAddresses.getPostalAddress().add(newPostalAddress);
		}
	}

	public List<String> getSchemeOperatorElectronicAddresses() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType tslSchemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == tslSchemeInformation) {
			return null;
		}
		AddressType address = tslSchemeInformation.getSchemeOperatorAddress();
		if (null == address) {
			return null;
		}
		ElectronicAddressType electronicAddress = address
				.getElectronicAddress();
		if (null == electronicAddress) {
			return null;
		}
		return electronicAddress.getURI();
	}

	public void setSchemeOperatorElectronicAddresses(List<String> addresses) {
		AddressType schemeOperatorAddress = getSchemeOperatorAddress();
		ElectronicAddressType electronicAddress = schemeOperatorAddress
				.getElectronicAddress();
		if (null == electronicAddress) {
			electronicAddress = this.objectFactory
					.createElectronicAddressType();
			schemeOperatorAddress.setElectronicAddress(electronicAddress);
		}
		List<String> electronicAddresses = electronicAddress.getURI();
		electronicAddresses.clear();
		for (String address : addresses) {
			electronicAddresses.add(address);
		}
	}

	public String getSchemeName(Locale locale) {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType tslSchemeInformation = this.trustStatusList
				.getSchemeInformation();
		InternationalNamesType i18nSchemeName = tslSchemeInformation
				.getSchemeName();
		String name = TrustServiceListUtils.getValue(i18nSchemeName, locale);
		return name;
	}

	public String getSchemeOperatorName() {
		Locale locale = Locale.getDefault();
		return getSchemeOperatorName(locale);
	}

	public String getSchemeOperatorName(Locale locale) {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType tslSchemeInformation = this.trustStatusList
				.getSchemeInformation();
		InternationalNamesType i18nSchemeOperatorName = tslSchemeInformation
				.getSchemeOperatorName();
		String name = TrustServiceListUtils.getValue(i18nSchemeOperatorName,
				locale);
		return name;
	}

	public void addSchemeInformationUri(String uri, Locale locale) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		NonEmptyMultiLangURIListType schemeInformationUriList = schemeInformation
				.getSchemeInformationURI();
		if (null == schemeInformationUriList) {
			schemeInformationUriList = this.objectFactory
					.createNonEmptyMultiLangURIListType();
			schemeInformation.setSchemeInformationURI(schemeInformationUriList);
		}
		NonEmptyMultiLangURIType i18nUri = this.objectFactory
				.createNonEmptyMultiLangURIType();
		i18nUri.setLang(locale.getLanguage());
		i18nUri.setValue(uri);
		schemeInformationUriList.getURI().add(i18nUri);
	}

	public List<String> getSchemeInformationUris() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType schemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			return null;
		}
		NonEmptyMultiLangURIListType schemeInformationUriList = schemeInformation
				.getSchemeInformationURI();
		if (null == schemeInformationUriList) {
			return null;
		}
		List<NonEmptyMultiLangURIType> uris = schemeInformationUriList.getURI();
		List<String> results = new LinkedList<String>();
		for (NonEmptyMultiLangURIType uri : uris) {
			results.add(uri.getValue());
		}
		return results;
	}

	public void setStatusDeterminationApproach(
			String statusDeterminationApproach) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		schemeInformation
				.setStatusDeterminationApproach(statusDeterminationApproach);
	}

	public String getStatusDeterminationApproach() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType schemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			return null;
		}
		String statusDeterminationApproach = schemeInformation
				.getStatusDeterminationApproach();
		return statusDeterminationApproach;
	}

	public List<TrustServiceProvider> getTrustServiceProviders() {
		if (null != this.trustServiceProviders) {
			// only load once
			return this.trustServiceProviders;
		}
		this.trustServiceProviders = new LinkedList<TrustServiceProvider>();
		if (null == this.trustStatusList) {
			return this.trustServiceProviders;
		}
		TrustServiceProviderListType trustServiceProviderList = this.trustStatusList
				.getTrustServiceProviderList();
		if (null == trustServiceProviderList) {
			return this.trustServiceProviders;
		}
		List<TSPType> tsps = trustServiceProviderList.getTrustServiceProvider();
		for (TSPType tsp : tsps) {
			TrustServiceProvider trustServiceProvider = new TrustServiceProvider(
					tsp);
			this.trustServiceProviders.add(trustServiceProvider);
		}
		return this.trustServiceProviders;
	}

	public String getType() {
		if (null == this.tslDocument) {
			try {
				marshall();
			} catch (Exception e) {
				throw new RuntimeException("marshall error: " + e.getMessage(),
						e);
			}
		}
		TSLSchemeInformationType tslSchemeInformation = this.trustStatusList
				.getSchemeInformation();
		String type = tslSchemeInformation.getTSLType();
		return type;
	}

	public BigInteger getSequenceNumber() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType tslSchemeInformation = this.trustStatusList
				.getSchemeInformation();
		BigInteger sequenceNumber = tslSchemeInformation.getTSLSequenceNumber();
		return sequenceNumber;
	}

	public Date getIssueDate() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType tslSchemeInformation = this.trustStatusList
				.getSchemeInformation();
		XMLGregorianCalendar xmlGregorianCalendar = tslSchemeInformation
				.getListIssueDateTime();
		return xmlGregorianCalendar.toGregorianCalendar().getTime();
	}

	public boolean hasSignature() {
		if (null == this.tslDocument) {
			/*
			 * Even if the JAXB TSL still has a signature, it's probably already
			 * invalid.
			 */
			return false;
		}
		Node signatureNode = getSignatureNode();
		if (null == signatureNode) {
			return false;
		}
		return true;
	}

	public X509Certificate verifySignature() {
		if (null == this.tslDocument) {
			LOG.debug("first save the document");
			return null;
		}

		Node signatureNode = getSignatureNode();
		if (null == signatureNode) {
			LOG.debug("no ds:Signature element present");
			return null;
		}

		KeyInfoKeySelector keyInfoKeySelector = new KeyInfoKeySelector();
		DOMValidateContext valContext = new DOMValidateContext(
				keyInfoKeySelector, signatureNode);
		XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory
				.getInstance("DOM");
		XMLSignature signature;
		try {
			signature = xmlSignatureFactory.unmarshalXMLSignature(valContext);
		} catch (MarshalException e) {
			throw new RuntimeException("XML signature parse error: "
					+ e.getMessage(), e);
		}
		boolean coreValidity;
		try {
			coreValidity = signature.validate(valContext);
		} catch (XMLSignatureException e) {
			throw new RuntimeException(
					"XML signature error: " + e.getMessage(), e);
		}

		// TODO: check what has been signed

		if (coreValidity) {
			LOG.debug("signature valid");
			return keyInfoKeySelector.getCertificate();
		}
		LOG.debug("signature invalid");

		return null;
	}

	private Node getSignatureNode() {
		Element nsElement = this.tslDocument.createElement("ns");
		nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:ds",
				XMLSignature.XMLNS);
		nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:tsl",
				"http://uri.etsi.org/02231/v2#");

		Node signatureNode;
		try {
			signatureNode = XPathAPI.selectSingleNode(this.tslDocument,
					"tsl:TrustServiceStatusList/ds:Signature", nsElement);
		} catch (TransformerException e) {
			throw new RuntimeException("XPath error: " + e.getMessage(), e);
		}
		return signatureNode;
	}

	private void marshall() throws JAXBException, ParserConfigurationException {
		/*
		 * Assign a unique XML Id to the TSL for signing purposes.
		 */
		//String tslId = "tsl-" + UUID.randomUUID().toString();
		TrustStatusListType trustStatusList = getTrustStatusList();
		//trustStatusList.setId(tslId);

		/*
		 * TSLTag
		 */
		trustStatusList.setTSLTag(ValidatorUtil.TSL_TAG);

		/*
		 * Scheme Information - TSL version identifier.
		 */
		TSLSchemeInformationType schemeInformation = trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			schemeInformation = this.objectFactory
					.createTSLSchemeInformationType();
			trustStatusList.setSchemeInformation(schemeInformation);
		}
		schemeInformation.setTSLVersionIdentifier(BigInteger.valueOf(3));

		/*
		 * Scheme Information - TSL sequence number
		 */
		if (null == schemeInformation.getTSLSequenceNumber()) {
			schemeInformation.setTSLSequenceNumber(BigInteger.valueOf(1));
		}

		/*
		 * Scheme Information - TSL Type
		 */
		schemeInformation.setTSLType(ValidatorUtil.TSL_TYPE);

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		Document document = documentBuilder.newDocument();

		JAXBContext jaxbContext = JAXBContext
				.newInstance(
						ObjectFactory.class,
						org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.ObjectFactory.class,
						org.etsi.uri._02231.v2.additionaltypes_.ObjectFactory.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		LOG.debug("marshaller type: " + marshaller.getClass().getName());

		//marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
			//	new TSLNamespacePrefixMapper());
		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<TrustStatusListType> trustStatusListElement = objectFactory
				.createTrustServiceStatusList(trustStatusList);
		marshaller.marshal(trustStatusListElement, document);

		this.tslDocument = document;
	}

	public void sign(PrivateKey privateKey, X509Certificate certificate)
			throws IOException {
		LOG.debug("sign with: " + certificate.getSubjectX500Principal());
		if (null == this.tslDocument) {
			/*
			 * Marshall to DOM.
			 */
			try {
				marshall();
			} catch (Exception e) {
				throw new IOException("marshaller error: " + e.getMessage(), e);
			}
		}

		/*
		 * Remove existing XML signature from DOM.
		 */
		Node signatureNode = getSignatureNode();
		if (null != signatureNode) {
			signatureNode.getParentNode().removeChild(signatureNode);
		}

		String tslId = this.trustStatusList.getId();

		/*
		 * Create new XML signature.
		 */
		try {
			xmlSign(privateKey, certificate, tslId);
		} catch (Exception e) {
			throw new IOException("XML sign error: " + e.getMessage(), e);
		}
		setChanged();
	}

	private void xmlSign(PrivateKey privateKey, X509Certificate certificate,
			String tslId) throws NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, MarshalException,
			XMLSignatureException {
		XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance(
				"DOM", new XMLDSigRI());
		LOG.debug("xml signature factory: "
				+ signatureFactory.getClass().getName());
		LOG.debug("loader: " + signatureFactory.getClass().getClassLoader());
		XMLSignContext signContext = new DOMSignContext(privateKey,
				this.tslDocument.getDocumentElement());
		signContext.putNamespacePrefix(XMLSignature.XMLNS, "ds");

		DigestMethod digestMethod = signatureFactory.newDigestMethod(
				DigestMethod.SHA1, null);
		List<Reference> references = new LinkedList<Reference>();
		List<Transform> transforms = new LinkedList<Transform>();
		transforms.add(signatureFactory.newTransform(Transform.ENVELOPED,
				(TransformParameterSpec) null));
		Transform exclusiveTransform = signatureFactory
				.newTransform(CanonicalizationMethod.EXCLUSIVE,
						(TransformParameterSpec) null);
		transforms.add(exclusiveTransform);

		Reference reference = signatureFactory.newReference("#" + tslId,
				digestMethod, transforms, null, null);
		references.add(reference);

		String signatureId = "xmldsig-" + UUID.randomUUID().toString();
		List<XMLObject> objects = new LinkedList<XMLObject>();
		addXadesBes(signatureFactory, this.tslDocument, signatureId,
				certificate, references, objects);

		SignatureMethod signatureMethod= signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
		
		CanonicalizationMethod canonicalizationMethod = signatureFactory
				.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE,
						(C14NMethodParameterSpec) null);
		SignedInfo signedInfo = signatureFactory.newSignedInfo(
				canonicalizationMethod, signatureMethod, references);

		List<Object> keyInfoContent = new LinkedList<Object>();

		KeyInfoFactory keyInfoFactory = KeyInfoFactory.getInstance();
		List<Object> x509DataObjects = new LinkedList<Object>();
		x509DataObjects.add(certificate);
		x509DataObjects.add(keyInfoFactory.newX509IssuerSerial(certificate
				.getIssuerX500Principal().toString(), certificate
				.getSerialNumber()));
		X509Data x509Data = keyInfoFactory.newX509Data(x509DataObjects);
		keyInfoContent.add(x509Data);

		KeyValue keyValue;
		try {
			keyValue = keyInfoFactory.newKeyValue(certificate.getPublicKey());
		} catch (KeyException e) {
			throw new RuntimeException("key exception: " + e.getMessage(), e);
		}
		keyInfoContent.add(keyValue);

		KeyInfo keyInfo = keyInfoFactory.newKeyInfo(keyInfoContent);

		String signatureValueId = signatureId + "-signature-value";
		XMLSignature xmlSignature = signatureFactory.newXMLSignature(
				signedInfo, keyInfo, objects, signatureId, signatureValueId);
		xmlSignature.sign(signContext);
	}

	
	public void addXadesBes(XMLSignatureFactory signatureFactory,
			Document document, String signatureId,
			X509Certificate signingCertificate, List<Reference> references,
			List<XMLObject> objects) throws NoSuchAlgorithmException,
			InvalidAlgorithmParameterException {
		LOG.debug("preSign");

		// QualifyingProperties
		QualifyingPropertiesType qualifyingProperties = this.xadesObjectFactory
				.createQualifyingPropertiesType();
		qualifyingProperties.setTarget("#" + signatureId);

		// SignedProperties
		SignedPropertiesType signedProperties = this.xadesObjectFactory
				.createSignedPropertiesType();
		String signedPropertiesId = signatureId + "-xades";
		signedProperties.setId(signedPropertiesId);
		qualifyingProperties.setSignedProperties(signedProperties);

		// SignedSignatureProperties
		SignedSignaturePropertiesType signedSignatureProperties = this.xadesObjectFactory
				.createSignedSignaturePropertiesType();
		signedProperties
				.setSignedSignatureProperties(signedSignatureProperties);

		// SigningTime
		GregorianCalendar signingTime = new GregorianCalendar();
		signingTime.setTimeZone(TimeZone.getTimeZone("Z"));
		signedSignatureProperties.setSigningTime(this.datatypeFactory
				.newXMLGregorianCalendar(signingTime));

		// SigningCertificate
		CertIDListType signingCertificates = this.xadesObjectFactory
				.createCertIDListType();
		CertIDType signingCertificateId = this.xadesObjectFactory
				.createCertIDType();

		X509IssuerSerialType issuerSerial = this.xmldsigObjectFactory
				.createX509IssuerSerialType();
		issuerSerial.setX509IssuerName(signingCertificate
				.getIssuerX500Principal().toString());
		issuerSerial.setX509SerialNumber(signingCertificate.getSerialNumber());
		signingCertificateId.setIssuerSerial(issuerSerial);

		DigestAlgAndValueType certDigest = this.xadesObjectFactory
				.createDigestAlgAndValueType();
		DigestMethodType jaxbDigestMethod = xmldsigObjectFactory
				.createDigestMethodType();
		jaxbDigestMethod.setAlgorithm(DigestMethod.SHA256);
		certDigest.setDigestMethod(jaxbDigestMethod);
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] digestValue;
		try {
			digestValue = messageDigest.digest(signingCertificate.getEncoded());
		} catch (CertificateEncodingException e) {
			throw new RuntimeException("certificate encoding error: "
					+ e.getMessage(), e);
		}
		certDigest.setDigestValue(digestValue);
		signingCertificateId.setCertDigest(certDigest);

		signingCertificates.getCert().add(signingCertificateId);
		signedSignatureProperties.setSigningCertificate(signingCertificates);

		// marshall XAdES QualifyingProperties
		Node qualifyingPropertiesNode = marshallQualifyingProperties(document,
				qualifyingProperties);

		// add XAdES ds:Object
		List<XMLStructure> xadesObjectContent = new LinkedList<XMLStructure>();
		xadesObjectContent.add(new DOMStructure(qualifyingPropertiesNode));
		XMLObject xadesObject = signatureFactory.newXMLObject(
				xadesObjectContent, null, null, null);
		objects.add(xadesObject);

		// add XAdES ds:Reference
		DigestMethod digestMethod = signatureFactory.newDigestMethod(
				DigestMethod.SHA256, null);
		List<Transform> transforms = new LinkedList<Transform>();
		Transform exclusiveTransform = signatureFactory
				.newTransform(CanonicalizationMethod.EXCLUSIVE,
						(TransformParameterSpec) null);
		transforms.add(exclusiveTransform);
		Reference reference = signatureFactory.newReference("#"
				+ signedPropertiesId, digestMethod, transforms, XADES_TYPE,
				null);
		references.add(reference);
	}

	private Node marshallQualifyingProperties(Document document,
			QualifyingPropertiesType qualifyingProperties) {
		Node marshallNode = document.createElement("marshall-node");
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(org.etsi.uri._01903.v1_3.ObjectFactory.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
//			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
	//				new TSLNamespacePrefixMapper());
			marshaller.marshal(this.xadesObjectFactory
					.createQualifyingProperties(qualifyingProperties),
					marshallNode);
		} catch (JAXBException e) {
			throw new RuntimeException("JAXB error: " + e.getMessage(), e);
		}
		Node qualifyingPropertiesNode = marshallNode.getFirstChild();
		return qualifyingPropertiesNode;
	}

	private void clearChanged() {
		this.changed = false;
	}

	public void save() throws IOException {
		if (null == this.tslFile) {
			throw new IllegalStateException("no TSL file set");
		}
		LOG.debug("save to: " + this.tslFile.getAbsolutePath());
		if (null == this.tslDocument) {
			try {
				marshall();
			} catch (Exception e) {
				throw new IOException("marshall error: " + e.getMessage(), e);
			}
			/*
			 * Only remove existing XML signature from new (or changed) DOM
			 * documents.
			 */
			Node signatureNode = getSignatureNode();
			if (null != signatureNode) {
				signatureNode.getParentNode().removeChild(signatureNode);
			}
		}
		try {
			toFile(this.tslFile);
		} catch (Exception e) {
			throw new IOException(
					"DOM transformation error: " + e.getMessage(), e);
		}
		clearChanged();
	}

	public void saveAs(File tslFile) throws IOException {
		this.tslFile = tslFile;
		save();
	}

	private void toFile(File tslFile)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException, IOException {
		Source source = new DOMSource(this.tslDocument);
                FileWriter out = new FileWriter(this.tslFile);
		Result result = new StreamResult(out);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		/*
		 * We have to omit the ?xml declaration if we want to embed the
		 * document.
		 */
		// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
		// "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(source, result);
	}

	public PostalAddressType getSchemeOperatorPostalAddress(Locale locale) {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType schemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			return null;
		}
		AddressType schemeOperatorAddress = schemeInformation
				.getSchemeOperatorAddress();
		if (null == schemeOperatorAddress) {
			return null;
		}
		PostalAddressListType postalAddresses = schemeOperatorAddress
				.getPostalAddresses();
		if (null == postalAddresses) {
			return null;
		}
		for (PostalAddressType postalAddress : postalAddresses
				.getPostalAddress()) {
			if (postalAddress.getLang().toLowerCase().equals(
					locale.getLanguage())) {
				return postalAddress;
			}
		}
		return null;
	}

	public void addSchemeType(String schemeType) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		NonEmptyURIListType schemeTypeList = schemeInformation
				.getSchemeTypeCommunityRules();
		if (null == schemeTypeList) {
			schemeTypeList = this.objectFactory.createNonEmptyURIListType();
			schemeInformation.setSchemeTypeCommunityRules(schemeTypeList);
		}
		schemeTypeList.getURI().add(schemeType);
	}

	public List<String> getSchemeTypes() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType schemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			return null;
		}
		NonEmptyURIListType schemeTypeList = schemeInformation
				.getSchemeTypeCommunityRules();
		if (null == schemeTypeList) {
			return null;
		}
		List<String> schemeTypes = schemeTypeList.getURI();
		return schemeTypes;
	}

	public void setSchemeTerritory(String schemeTerritory) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		schemeInformation.setSchemeTerritory(schemeTerritory);
	}

	public String getSchemeTerritory() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType schemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			return null;
		}
		String schemeTerritory = schemeInformation.getSchemeTerritory();
		return schemeTerritory;
	}

	public void addLegalNotice(String legalNotice, Locale locale) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		PolicyOrLegalnoticeType policyOrLegalnotice = schemeInformation
				.getPolicyOrLegalNotice();
		if (null == policyOrLegalnotice) {
			policyOrLegalnotice = this.objectFactory
					.createPolicyOrLegalnoticeType();
			schemeInformation.setPolicyOrLegalNotice(policyOrLegalnotice);
		}
		List<MultiLangStringType> tslLegalNotices = policyOrLegalnotice
				.getTSLLegalNotice();

		MultiLangStringType tslLegalNotice = this.objectFactory
				.createMultiLangStringType();
		tslLegalNotice.setLang(locale.getLanguage());
		tslLegalNotice.setValue(legalNotice);

		tslLegalNotices.add(tslLegalNotice);
	}

	public String getLegalNotice() {
		return getLegalNotice(Locale.ENGLISH);
	}

	public String getLegalNotice(Locale locale) {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType schemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			return null;
		}
		PolicyOrLegalnoticeType policyOrLegalnotice = schemeInformation
				.getPolicyOrLegalNotice();
		if (null == policyOrLegalnotice) {
			return null;
		}
		List<MultiLangStringType> tslLegalNotices = policyOrLegalnotice
				.getTSLLegalNotice();
		for (MultiLangStringType tslLegalNotice : tslLegalNotices) {
			String lang = tslLegalNotice.getLang();
			if (locale.getLanguage().equals(lang)) {
				return tslLegalNotice.getValue();
			}
		}
		return null;
	}

	public void setHistoricalInformationPeriod(int historicalInformationPeriod) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		schemeInformation.setHistoricalInformationPeriod(BigInteger
				.valueOf(historicalInformationPeriod));
	}

	public Integer getHistoricalInformationPeriod() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType schemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			return null;
		}
		BigInteger historicalInformationPeriod = schemeInformation
				.getHistoricalInformationPeriod();
		if (null == historicalInformationPeriod) {
			return null;
		}
		return historicalInformationPeriod.intValue();
	}

	public void setListIssueDateTime(DateTime listIssueDateTime) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		GregorianCalendar listIssueCalendar = listIssueDateTime
				.toGregorianCalendar();
		listIssueCalendar.setTimeZone(TimeZone.getTimeZone("Z"));
		schemeInformation.setListIssueDateTime(this.datatypeFactory
				.newXMLGregorianCalendar(listIssueCalendar));
	}

	public DateTime getListIssueDateTime() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType schemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			return null;
		}
		XMLGregorianCalendar listIssueDateTime = schemeInformation
				.getListIssueDateTime();
		if (null == listIssueDateTime) {
			return null;
		}
		GregorianCalendar listIssueCalendar = listIssueDateTime
				.toGregorianCalendar();
		DateTime dateTime = new DateTime(listIssueCalendar);
		return dateTime;
	}

	public void setNextUpdate(DateTime nextUpdateDateTime) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		GregorianCalendar nextUpdateCalendar = nextUpdateDateTime
				.toGregorianCalendar();
		nextUpdateCalendar.setTimeZone(TimeZone.getTimeZone("Z"));

		NextUpdateType nextUpdate = schemeInformation.getNextUpdate();
		if (null == nextUpdate) {
			nextUpdate = this.objectFactory.createNextUpdateType();
			schemeInformation.setNextUpdate(nextUpdate);
		}
		nextUpdate.setDateTime(this.datatypeFactory
				.newXMLGregorianCalendar(nextUpdateCalendar));
	}

	public void setTSLSequenceNumber(BigInteger sequenceNumber) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		schemeInformation.setTSLSequenceNumber(sequenceNumber);
	}

	public DateTime getNextUpdate() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType schemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == schemeInformation) {
			return null;
		}

		NextUpdateType nextUpdate = schemeInformation.getNextUpdate();
		if (null == nextUpdate) {
			return null;
		}
		XMLGregorianCalendar nextUpdateXmlCalendar = nextUpdate.getDateTime();
		DateTime nextUpdateDateTime = new DateTime(nextUpdateXmlCalendar
				.toGregorianCalendar());
		return nextUpdateDateTime;
	}

	public void addTrustServiceProvider(
			TrustServiceProvider trustServiceProvider) {
		TrustStatusListType trustStatusList = getTrustStatusList();
		TrustServiceProviderListType trustServiceProviderList = trustStatusList
				.getTrustServiceProviderList();
		if (null == trustServiceProviderList) {
			trustServiceProviderList = this.objectFactory
					.createTrustServiceProviderListType();
			trustStatusList
					.setTrustServiceProviderList(trustServiceProviderList);
		}
		List<TSPType> tspList = trustServiceProviderList
				.getTrustServiceProvider();
		tspList.add(trustServiceProvider.getTSP());
		// reset Java model cache
		this.trustServiceProviders = null;
	}

	public void humanReadableExport(File pdfExportFile) {
		new Tsl2PdfExporter().humanReadableExport(this, pdfExportFile);
	}

	public void addDistributionPoint(String distributionPointUri) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		ElectronicAddressType distributionPoints = schemeInformation
				.getDistributionPoints();
		if (null == distributionPoints) {
			distributionPoints = this.objectFactory
					.createElectronicAddressType();
			schemeInformation.setDistributionPoints(distributionPoints);
		}
		List<String> uris = distributionPoints.getURI();
		uris.add(distributionPointUri);
	}

	public String getSha1Fingerprint() {
		String fingerprint = DigestUtils.shaHex(toByteArray());
		return fingerprint;
	}

	private byte[] toByteArray() throws TransformerFactoryConfigurationError {
		if (null != this.tslFile) {
			try {
				return FileUtils.readFileToByteArray(this.tslFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		Source source = new DOMSource(this.tslDocument);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Result result = new StreamResult(byteArrayOutputStream);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
		// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
		// "yes");
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
		return byteArrayOutputStream.toByteArray();
	}

	public String getSha256Fingerprint() {
		String fingerprint = org.apache.commons.codec.digest.DigestUtils.sha256Hex(toByteArray());
		return fingerprint;
	}

	public List<OtherTSLPointerType> getOtherTSLPointers() {
		if (null == this.trustStatusList) {
			return null;
		}
		TSLSchemeInformationType tslSchemeInformation = this.trustStatusList
				.getSchemeInformation();
		if (null == tslSchemeInformation) {
			return null;
		}
		OtherTSLPointersType otherTSLpointers = tslSchemeInformation
				.getPointersToOtherTSL();
		if (null == otherTSLpointers) {
			return null;
		}
		List<OtherTSLPointerType> otherTSLpointer = otherTSLpointers
				.getOtherTSLPointer();

		if (null == otherTSLpointer) {
			return null;
		}
		return otherTSLpointer;
	}

	public void addOtherTSLPointer(String location, String mimeType,
			String tslType, String schemeTerritory, String schemeOperatorName,
			String schemeTypeCommunityRuleUri) {
		addOtherTSLPointer(location, mimeType, tslType, schemeTerritory,
				schemeOperatorName, schemeTypeCommunityRuleUri, null);
	}

	public void addOtherTSLPointer(String location, String mimeType,
			String tslType, String schemeTerritory, String schemeOperatorName,
			String schemeTypeCommunityRuleUri,
			X509Certificate digitalIdentityCertificate) {
		TSLSchemeInformationType schemeInformation = getSchemeInformation();
		OtherTSLPointersType otherTSLPointers = schemeInformation
				.getPointersToOtherTSL();
		if (null == otherTSLPointers) {
			otherTSLPointers = this.objectFactory.createOtherTSLPointersType();
			schemeInformation.setPointersToOtherTSL(otherTSLPointers);
		}
		List<OtherTSLPointerType> pointerList = otherTSLPointers
				.getOtherTSLPointer();
		OtherTSLPointerType otherTSLPointer = this.objectFactory
				.createOtherTSLPointerType();
		pointerList.add(otherTSLPointer);

		otherTSLPointer.setTSLLocation(location);
		AdditionalInformationType additionalInformation = this.objectFactory
				.createAdditionalInformationType();
		otherTSLPointer.setAdditionalInformation(additionalInformation);

		List<Object> objects = additionalInformation
				.getTextualInformationOrOtherInformation();
		{
			JAXBElement<String> mimeTypeElement = this.tslxObjectFactory
					.createMimeType(mimeType);
			AnyType anyType = this.objectFactory.createAnyType();
			anyType.getContent().add(mimeTypeElement);
			objects.add(anyType);
		}
		{
			JAXBElement<String> tslTypeElement = this.objectFactory
					.createTSLType(tslType);
			AnyType anyType = this.objectFactory.createAnyType();
			anyType.getContent().add(tslTypeElement);
			objects.add(anyType);
		}
		{
			JAXBElement<String> schemeTerritoryElement = this.objectFactory
					.createSchemeTerritory(schemeTerritory);
			AnyType anyType = this.objectFactory.createAnyType();
			anyType.getContent().add(schemeTerritoryElement);
			objects.add(anyType);
		}
		{
			InternationalNamesType i18nNames = this.objectFactory
					.createInternationalNamesType();
			MultiLangNormStringType i18nName = this.objectFactory
					.createMultiLangNormStringType();
			i18nName.setLang("en");
			i18nName.setValue(schemeOperatorName);
			i18nNames.getName().add(i18nName);
			JAXBElement<InternationalNamesType> schemeOperatorNameElement = this.objectFactory
					.createSchemeOperatorName(i18nNames);
			AnyType anyType = this.objectFactory.createAnyType();
			anyType.getContent().add(schemeOperatorNameElement);
			objects.add(anyType);
		}
		{
			NonEmptyURIListType uriList = this.objectFactory
					.createNonEmptyURIListType();
			uriList.getURI().add(schemeTypeCommunityRuleUri);
			JAXBElement<NonEmptyURIListType> schemeTypeCommunityRulesElement = this.objectFactory
					.createSchemeTypeCommunityRules(uriList);
			AnyType anyType = this.objectFactory.createAnyType();
			anyType.getContent().add(schemeTypeCommunityRulesElement);
			objects.add(anyType);
		}
		if (null != digitalIdentityCertificate) {
			ServiceDigitalIdentityListType serviceDigitalIdentityList = this.objectFactory
					.createServiceDigitalIdentityListType();
			DigitalIdentityListType digitalIdentityList = this.objectFactory
					.createDigitalIdentityListType();
			List<DigitalIdentityType> digitalIdentities = digitalIdentityList
					.getDigitalId();
			DigitalIdentityType digitalIdentity = this.objectFactory
					.createDigitalIdentityType();

			try {
				digitalIdentity.setX509Certificate(digitalIdentityCertificate
						.getEncoded());
			} catch (CertificateEncodingException e) {
				throw new RuntimeException("X509 encoding error: "
						+ e.getMessage(), e);
			}
			digitalIdentities.add(digitalIdentity);

			digitalIdentity = this.objectFactory.createDigitalIdentityType();
			digitalIdentity.setX509SubjectName(digitalIdentityCertificate
					.getSubjectX500Principal().getName());
			digitalIdentities.add(digitalIdentity);

			digitalIdentity = this.objectFactory.createDigitalIdentityType();
			byte[] skiValue = digitalIdentityCertificate
					.getExtensionValue(X509Extensions.SubjectKeyIdentifier
							.getId());
			SubjectKeyIdentifierStructure subjectKeyIdentifierStructure;
			try {
				subjectKeyIdentifierStructure = new SubjectKeyIdentifierStructure(
						skiValue);
			} catch (IOException e) {
				throw new RuntimeException("X509 SKI decoding error: "
						+ e.getMessage(), e);
			}
			digitalIdentity.setX509SKI(subjectKeyIdentifierStructure
					.getKeyIdentifier());
			digitalIdentities.add(digitalIdentity);

			List<DigitalIdentityListType> digitalIdentityListList = serviceDigitalIdentityList
					.getServiceDigitalIdentity();
			digitalIdentityListList.add(digitalIdentityList);

			otherTSLPointer
					.setServiceDigitalIdentities(serviceDigitalIdentityList);
		}
	}

        @Override
        public String toString()
        {
            return this.getSchemeName();
        }
}
