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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
import org.etsi.uri._01903.v1_3.IdentifierType;
import org.etsi.uri._01903.v1_3.ObjectIdentifierType;
import org.etsi.uri._02231.v2_.AdditionalServiceInformationType;
import org.etsi.uri._02231.v2_.DigitalIdentityListType;
import org.etsi.uri._02231.v2_.DigitalIdentityType;
import org.etsi.uri._02231.v2_.ExtensionType;
import org.etsi.uri._02231.v2_.ExtensionsListType;
import org.etsi.uri._02231.v2_.InternationalNamesType;
import org.etsi.uri._02231.v2_.MultiLangNormStringType;
import org.etsi.uri._02231.v2_.NonEmptyMultiLangURIType;
import org.etsi.uri._02231.v2_.ObjectFactory;
import org.etsi.uri._02231.v2_.TSPServiceInformationType;
import org.etsi.uri._02231.v2_.TSPServiceType;
import org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.CriteriaListType;
import org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.PoliciesListType;
import org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.QualificationElementType;
import org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.QualificationsType;
import org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.QualifierType;
import org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.QualifiersType;
import org.joda.time.DateTime;

public class TrustService {

	private static final Log LOG = LogFactory.getLog(TrustService.class);

	private final TSPServiceType tspService;

	private final ObjectFactory objectFactory;

	private final DatatypeFactory datatypeFactory;

	private final org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.ObjectFactory eccObjectFactory;

	private final org.etsi.uri._01903.v1_3.ObjectFactory xadesObjectFactory;

	private final List<String> qcSSCDStatusAsInCertOids;

	public static final String QC_NO_SSCD_QUALIFIER_URI = "http://uri.etsi.org/TrstSvc/eSigDir-1999-93-EC-TrustedList/SvcInfoExt/QCNoSSCD";

	public static final String QC_SSCD_STATUS_AS_IN_CERT_QUALIFIER_URI = "http://uri.etsi.org/TrstSvc/eSigDir-1999-93-EC-TrustedList/SvcInfoExt/QCSSCDStatusAsInCert";

	public static final String QC_FOR_LEGAL_PERSON_QUALIFIER_URI = "http://uri.etsi.org/TrstSvc/eSigDir-1999-93-EC-TrustedList/SvcInfoExt/QCForLegalPerson";

	public TrustService(TSPServiceType tspService) {
		this.tspService = tspService;
		this.objectFactory = new ObjectFactory();
		try {
			this.datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException("datatype config error: "
					+ e.getMessage(), e);
		}
		this.qcSSCDStatusAsInCertOids = new LinkedList<String>();
		this.eccObjectFactory = new org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.ObjectFactory();
		this.xadesObjectFactory = new org.etsi.uri._01903.v1_3.ObjectFactory();
	}

	public TrustService(X509Certificate certificate) {
		this(certificate, new String[] {});
	}

	public TrustService(X509Certificate certificate, String... noSscdOids) {
		this.qcSSCDStatusAsInCertOids = new LinkedList<String>();
		for (String oid : noSscdOids) {
			this.qcSSCDStatusAsInCertOids.add(oid);
		}

		this.objectFactory = new ObjectFactory();
		try {
			this.datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException("datatype config error: "
					+ e.getMessage(), e);
		}
		this.eccObjectFactory = new org.etsi.uri.trstsvc.svcinfoext.esigdir_1999_93_ec_trustedlist.ObjectFactory();
		this.xadesObjectFactory = new org.etsi.uri._01903.v1_3.ObjectFactory();

		this.tspService = this.objectFactory.createTSPServiceType();
		TSPServiceInformationType tspServiceInformation = this.objectFactory
				.createTSPServiceInformationType();
		this.tspService.setServiceInformation(tspServiceInformation);
		tspServiceInformation
				.setServiceTypeIdentifier("http://uri.etsi.org/TrstSvc/Svctype/CA/QC");
		InternationalNamesType i18nServiceName = this.objectFactory
				.createInternationalNamesType();
		List<MultiLangNormStringType> serviceNames = i18nServiceName.getName();
		MultiLangNormStringType serviceName = this.objectFactory
				.createMultiLangNormStringType();
		serviceNames.add(serviceName);
		serviceName.setLang(Locale.ENGLISH.getLanguage());
		serviceName.setValue(certificate.getSubjectX500Principal().toString());
		tspServiceInformation.setServiceName(i18nServiceName);

		DigitalIdentityListType digitalIdentityList = createDigitalIdentity(certificate);
		tspServiceInformation.setServiceDigitalIdentity(digitalIdentityList);

		tspServiceInformation
				.setServiceStatus("http://uri.etsi.org/TrstSvc/eSigDir-1999-93-EC-TrustedList/Svcstatus/undersupervision");

		GregorianCalendar statusStartingCalendar = new DateTime(certificate
				.getNotBefore()).toGregorianCalendar();
		statusStartingCalendar.setTimeZone(TimeZone.getTimeZone("Z"));
		XMLGregorianCalendar statusStartingTime = this.datatypeFactory
				.newXMLGregorianCalendar(statusStartingCalendar);
		tspServiceInformation.setStatusStartingTime(statusStartingTime);

		if (false == this.qcSSCDStatusAsInCertOids.isEmpty()) {
			ExtensionsListType extensionsList = this.objectFactory
					.createExtensionsListType();
			tspServiceInformation
					.setServiceInformationExtensions(extensionsList);
			List<ExtensionType> extensions = extensionsList.getExtension();
			ExtensionType extension = this.objectFactory.createExtensionType();
			extension.setCritical(true);
			extensions.add(extension);

			QualificationsType qualifications = this.eccObjectFactory
					.createQualificationsType();
			extension.getContent().add(
					this.eccObjectFactory.createQualifications(qualifications));
			List<QualificationElementType> qualificationElements = qualifications
					.getQualificationElement();

			QualificationElementType qualificationElement = this.eccObjectFactory
					.createQualificationElementType();
			qualificationElements.add(qualificationElement);

			QualifiersType qualifiers = this.eccObjectFactory
					.createQualifiersType();
			List<QualifierType> qualifierList = qualifiers.getQualifier();
			QualifierType qcSscdStatusInCertqualifier = this.eccObjectFactory
					.createQualifierType();
			qualifierList.add(qcSscdStatusInCertqualifier);
			qcSscdStatusInCertqualifier
					.setUri(QC_SSCD_STATUS_AS_IN_CERT_QUALIFIER_URI);
			qualificationElement.setQualifiers(qualifiers);

			CriteriaListType criteriaList = this.eccObjectFactory
					.createCriteriaListType();
			qualificationElement.setCriteriaList(criteriaList);
			criteriaList.setAssert("atLeastOne");

			List<PoliciesListType> policySet = criteriaList.getPolicySet();
			PoliciesListType policiesList = this.eccObjectFactory
					.createPoliciesListType();
			policySet.add(policiesList);
			for (String oid : this.qcSSCDStatusAsInCertOids) {
				ObjectIdentifierType objectIdentifier = this.xadesObjectFactory
						.createObjectIdentifierType();
				IdentifierType identifier = this.xadesObjectFactory
						.createIdentifierType();
				identifier.setValue(oid);
				objectIdentifier.setIdentifier(identifier);
				policiesList.getPolicyIdentifier().add(objectIdentifier);
			}

			AdditionalServiceInformationType additionalServiceInformation = this.objectFactory
					.createAdditionalServiceInformationType();
			NonEmptyMultiLangURIType additionalServiceInformationUri = this.objectFactory
					.createNonEmptyMultiLangURIType();
			additionalServiceInformationUri.setLang("en");
			additionalServiceInformationUri
					.setValue("http://uri.etsi.org/TrstSvc/eSigDir-1999-93-EC-TrustedList/SvcInfoExt/RootCA-QC");
			additionalServiceInformation
					.setURI(additionalServiceInformationUri);
			extension
					.getContent()
					.add(
							this.objectFactory
									.createAdditionalServiceInformation(additionalServiceInformation));
		}
	}

	private DigitalIdentityListType createDigitalIdentity(
			X509Certificate certificate) {
		DigitalIdentityListType digitalIdentityList = this.objectFactory
				.createDigitalIdentityListType();
		List<DigitalIdentityType> digitalIdentities = digitalIdentityList
				.getDigitalId();
		DigitalIdentityType digitalIdentity = this.objectFactory
				.createDigitalIdentityType();
		try {
			digitalIdentity.setX509Certificate(certificate.getEncoded());
		} catch (CertificateEncodingException e) {
			throw new RuntimeException(
					"X509 encoding error: " + e.getMessage(), e);
		}
		digitalIdentities.add(digitalIdentity);

		digitalIdentity = this.objectFactory.createDigitalIdentityType();
		digitalIdentity.setX509SubjectName(certificate
				.getSubjectX500Principal().getName());
		digitalIdentities.add(digitalIdentity);

		digitalIdentity = this.objectFactory.createDigitalIdentityType();
		byte[] skiValue = certificate
				.getExtensionValue(X509Extensions.SubjectKeyIdentifier.getId());
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

		return digitalIdentityList;
	}

	public TSPServiceType getTSPService() {
		return this.tspService;
	}

	public String getName(Locale locale) {
		TSPServiceInformationType tspServiceInformation = this.tspService
				.getServiceInformation();
		InternationalNamesType i18nServiceName = tspServiceInformation
				.getServiceName();
		String serviceName = TrustServiceListUtils.getValue(i18nServiceName,
				locale);
		return serviceName;
	}

	public String getName() {
		Locale locale = Locale.getDefault();
		return getName(locale);
	}

	public String getType() {
		TSPServiceInformationType tspServiceInformation = this.tspService
				.getServiceInformation();
		String serviceTypeIdentifier = tspServiceInformation
				.getServiceTypeIdentifier();
		return serviceTypeIdentifier;
	}

	public List<ExtensionType> getExtensions() {
		TSPServiceInformationType tspServiceInformation = this.tspService
				.getServiceInformation();
		ExtensionsListType extensionsList = tspServiceInformation
				.getServiceInformationExtensions();
		if (null == extensionsList) {
			return new LinkedList<ExtensionType>();
		}
		List<ExtensionType> extensions = extensionsList.getExtension();
		return extensions;
	}

	public String getStatus() {
		TSPServiceInformationType tspServiceInformation = this.tspService.getServiceInformation();
                String status = null;
                if (tspServiceInformation != null)
                    status = tspServiceInformation.getServiceStatus();
		return status;
	}

	public DateTime getStatusStartingTime() {
		TSPServiceInformationType tspServiceInformation = this.tspService.getServiceInformation();
                if (tspServiceInformation != null)
                {
		    XMLGregorianCalendar statusStartingTimeXmlCalendar = tspServiceInformation.getStatusStartingTime();
                    if (statusStartingTimeXmlCalendar == null) return null;
                    DateTime statusStartingTimeDateTime = new DateTime(statusStartingTimeXmlCalendar.toGregorianCalendar());
                    return statusStartingTimeDateTime;
                }
                else
                    return null;
	}

	public X509Certificate getServiceDigitalIdentity() {
		TSPServiceInformationType tspServiceInformation = this.tspService
				.getServiceInformation();
		DigitalIdentityListType digitalIdentityList = tspServiceInformation
				.getServiceDigitalIdentity();
		try {
			final CertificateFactory certificateFactory = CertificateFactory
					.getInstance("X.509");
			for (final DigitalIdentityType digitalIdentity : digitalIdentityList
					.getDigitalId()) {
				byte[] x509CertificateData = digitalIdentity
						.getX509Certificate();
				if (x509CertificateData != null) {
					try {
						X509Certificate certificate = (X509Certificate) certificateFactory
								.generateCertificate(new ByteArrayInputStream(
										x509CertificateData));
						return certificate;
					} catch (CertificateException e) {
						throw new RuntimeException("X509 error: "
								+ e.getMessage(), e);
					}
				}
			}
			throw new RuntimeException("No X509Certificate identity specified");
		} catch (CertificateException e) {
			throw new RuntimeException("X509 error: " + e.getMessage(), e);
		}
	}

	public byte[] getServiceDigitalIdentityData() {
		TSPServiceInformationType tspServiceInformation = this.tspService
				.getServiceInformation();
		DigitalIdentityListType digitalIdentityList = tspServiceInformation
				.getServiceDigitalIdentity();
		try {
			final CertificateFactory certificateFactory = CertificateFactory
					.getInstance("X.509");
			for (final DigitalIdentityType digitalIdentity : digitalIdentityList
					.getDigitalId()) {
				byte[] x509CertificateData = digitalIdentity
						.getX509Certificate();
				if (x509CertificateData != null) {
					try {
						X509Certificate certificate = (X509Certificate) certificateFactory
								.generateCertificate(new ByteArrayInputStream(
										x509CertificateData));
						return x509CertificateData;
					} catch (CertificateException e) {
						throw new RuntimeException("X509 error: "
								+ e.getMessage(), e);
					}
				}
			}
			throw new RuntimeException("No X509Certificate identity specified");
		} catch (CertificateException e) {
			throw new RuntimeException("X509 error: " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		return getName();
	}

	private static final QName qualifiersName = new QName(
			"http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#",
			"Qualifications");

	public void addOIDForQCSSCDStatusAsInCert(String oid) {
		addOIDForQCSSCDStatusAsInCert(oid, null);
	}

	public void addOIDForQCSSCDStatusAsInCert(String oid, String description) {
		TSPServiceInformationType tspServiceInformation = this.tspService
				.getServiceInformation();
		ExtensionsListType extensionsList = tspServiceInformation
				.getServiceInformationExtensions();
		if (null == extensionsList) {
			extensionsList = this.objectFactory.createExtensionsListType();
			tspServiceInformation
					.setServiceInformationExtensions(extensionsList);
		}
		List<ExtensionType> extensions = extensionsList.getExtension();
		for (ExtensionType extension : extensions) {
			if (false == extension.isCritical()) {
				continue;
			}
			List<Object> extensionContent = extension.getContent();
			for (Object extensionObject : extensionContent) {
				JAXBElement<?> extensionElement = (JAXBElement<?>) extensionObject;
				QName extensionName = extensionElement.getName();
				LOG.debug("extension name: " + extensionName);
				if (qualifiersName.equals(extensionName)) {
					LOG.debug("extension found");
					QualificationsType qualifications = (QualificationsType) extensionElement
							.getValue();
					List<QualificationElementType> qualificationElements = qualifications
							.getQualificationElement();
					for (QualificationElementType qualificationElement : qualificationElements) {
						QualifiersType qualifiers = qualificationElement
								.getQualifiers();
						List<QualifierType> qualifierList = qualifiers
								.getQualifier();
						boolean qcSscdStatusAsInCertQualifier = false;
						boolean qcForLegalPersonQualifier = false;
						for (QualifierType qualifier : qualifierList) {
							if (QC_SSCD_STATUS_AS_IN_CERT_QUALIFIER_URI
									.equals(qualifier.getUri())) {
								qcSscdStatusAsInCertQualifier = true;
							}
							if (QC_FOR_LEGAL_PERSON_QUALIFIER_URI
									.equals(qualifier.getUri())) {
								qcForLegalPersonQualifier = true;
							}
						}
						if (qcSscdStatusAsInCertQualifier
								&& !qcForLegalPersonQualifier) {
							CriteriaListType criteriaList = qualificationElement
									.getCriteriaList();
							List<PoliciesListType> policySet = criteriaList
									.getPolicySet();
							PoliciesListType policiesList = policySet.get(0);

							ObjectIdentifierType objectIdentifier = this.xadesObjectFactory
									.createObjectIdentifierType();
							IdentifierType identifier = this.xadesObjectFactory
									.createIdentifierType();
							identifier.setValue(oid);
							objectIdentifier.setIdentifier(identifier);
							if (null != description) {
								objectIdentifier.setDescription(description);
							}
							policiesList.getPolicyIdentifier().add(
									objectIdentifier);
							return;
						}
					}
				}
			}
		}
		ExtensionType extension = this.objectFactory.createExtensionType();
		extension.setCritical(true);
		extensions.add(extension);

		QualificationsType qualifications = this.eccObjectFactory
				.createQualificationsType();
		extension.getContent().add(
				this.eccObjectFactory.createQualifications(qualifications));
		List<QualificationElementType> qualificationElements = qualifications
				.getQualificationElement();

		QualificationElementType qualificationElement = this.eccObjectFactory
				.createQualificationElementType();
		qualificationElements.add(qualificationElement);

		QualifiersType qualifiers = this.eccObjectFactory
				.createQualifiersType();
		List<QualifierType> qualifierList = qualifiers.getQualifier();
		QualifierType qcSscdStatusInCertqualifier = this.eccObjectFactory
				.createQualifierType();
		qualifierList.add(qcSscdStatusInCertqualifier);
		qcSscdStatusInCertqualifier
				.setUri(QC_SSCD_STATUS_AS_IN_CERT_QUALIFIER_URI);
		qualificationElement.setQualifiers(qualifiers);

		CriteriaListType criteriaList = this.eccObjectFactory
				.createCriteriaListType();
		qualificationElement.setCriteriaList(criteriaList);
		criteriaList.setAssert("atLeastOne");

		List<PoliciesListType> policySet = criteriaList.getPolicySet();
		PoliciesListType policiesList = this.eccObjectFactory
				.createPoliciesListType();
		policySet.add(policiesList);
		ObjectIdentifierType objectIdentifier = this.xadesObjectFactory
				.createObjectIdentifierType();
		IdentifierType identifier = this.xadesObjectFactory
				.createIdentifierType();
		identifier.setValue(oid);
		objectIdentifier.setDescription(description);
		objectIdentifier.setIdentifier(identifier);
		policiesList.getPolicyIdentifier().add(objectIdentifier);

		AdditionalServiceInformationType additionalServiceInformation = this.objectFactory
				.createAdditionalServiceInformationType();
		NonEmptyMultiLangURIType additionalServiceInformationUri = this.objectFactory
				.createNonEmptyMultiLangURIType();
		additionalServiceInformationUri.setLang("en");
		additionalServiceInformationUri
				.setValue("http://uri.etsi.org/TrstSvc/eSigDir-1999-93-EC-TrustedList/SvcInfoExt/RootCA-QC");
		additionalServiceInformation.setURI(additionalServiceInformationUri);
		extension
				.getContent()
				.add(
						this.objectFactory
								.createAdditionalServiceInformation(additionalServiceInformation));

	}

	public void addOIDForQCForLegalPerson(String oid) {
		TSPServiceInformationType tspServiceInformation = this.tspService
				.getServiceInformation();
		ExtensionsListType extensionsList = tspServiceInformation
				.getServiceInformationExtensions();
		if (null == extensionsList) {
			extensionsList = this.objectFactory.createExtensionsListType();
			tspServiceInformation
					.setServiceInformationExtensions(extensionsList);
		}
		List<ExtensionType> extensions = extensionsList.getExtension();
		for (ExtensionType extension : extensions) {
			if (false == extension.isCritical()) {
				continue;
			}
			List<Object> extensionContent = extension.getContent();
			for (Object extensionObject : extensionContent) {
				JAXBElement<?> extensionElement = (JAXBElement<?>) extensionObject;
				QName extensionName = extensionElement.getName();
				LOG.debug("extension name: " + extensionName);
				if (qualifiersName.equals(extensionName)) {
					LOG.debug("extension found");
					QualificationsType qualifications = (QualificationsType) extensionElement
							.getValue();
					List<QualificationElementType> qualificationElements = qualifications
							.getQualificationElement();
					for (QualificationElementType qualificationElement : qualificationElements) {
						QualifiersType qualifiers = qualificationElement
								.getQualifiers();
						List<QualifierType> qualifierList = qualifiers
								.getQualifier();
						for (QualifierType qualifier : qualifierList) {
							if (QC_FOR_LEGAL_PERSON_QUALIFIER_URI
									.equals(qualifier.getUri())) {
								CriteriaListType criteriaList = qualificationElement
										.getCriteriaList();
								List<PoliciesListType> policySet = criteriaList
										.getPolicySet();
								PoliciesListType policiesList = policySet
										.get(0);

								ObjectIdentifierType objectIdentifier = this.xadesObjectFactory
										.createObjectIdentifierType();
								IdentifierType identifier = this.xadesObjectFactory
										.createIdentifierType();
								identifier.setValue(oid);
								objectIdentifier.setIdentifier(identifier);
								policiesList.getPolicyIdentifier().add(
										objectIdentifier);
								return;
							}
						}
					}
				}
			}
		}
		ExtensionType extension = this.objectFactory.createExtensionType();
		extension.setCritical(true);
		extensions.add(extension);

		QualificationsType qualifications = this.eccObjectFactory
				.createQualificationsType();
		extension.getContent().add(
				this.eccObjectFactory.createQualifications(qualifications));
		List<QualificationElementType> qualificationElements = qualifications
				.getQualificationElement();

		QualificationElementType qualificationElement = this.eccObjectFactory
				.createQualificationElementType();
		qualificationElements.add(qualificationElement);

		QualifiersType qualifiers = this.eccObjectFactory
				.createQualifiersType();
		List<QualifierType> qualifierList = qualifiers.getQualifier();
		QualifierType qcForLegalPersonqualifier = this.eccObjectFactory
				.createQualifierType();
		qualifierList.add(qcForLegalPersonqualifier);
		qcForLegalPersonqualifier.setUri(QC_FOR_LEGAL_PERSON_QUALIFIER_URI);
		// QualifierType qcSscdStatusInCertqualifier = this.eccObjectFactory
		// .createQualifierType();
		// qualifierList.add(qcSscdStatusInCertqualifier);
		// qcSscdStatusInCertqualifier
		// .setUri(QC_SSCD_STATUS_AS_IN_CERT_QUALIFIER_URI);
		qualificationElement.setQualifiers(qualifiers);

		CriteriaListType criteriaList = this.eccObjectFactory
				.createCriteriaListType();
		qualificationElement.setCriteriaList(criteriaList);
		criteriaList.setAssert("atLeastOne");

		List<PoliciesListType> policySet = criteriaList.getPolicySet();
		PoliciesListType policiesList = this.eccObjectFactory
				.createPoliciesListType();
		policySet.add(policiesList);
		ObjectIdentifierType objectIdentifier = this.xadesObjectFactory
				.createObjectIdentifierType();
		IdentifierType identifier = this.xadesObjectFactory
				.createIdentifierType();
		identifier.setValue(oid);
		objectIdentifier.setIdentifier(identifier);
		policiesList.getPolicyIdentifier().add(objectIdentifier);

		AdditionalServiceInformationType additionalServiceInformation = this.objectFactory
				.createAdditionalServiceInformationType();
		NonEmptyMultiLangURIType additionalServiceInformationUri = this.objectFactory
				.createNonEmptyMultiLangURIType();
		additionalServiceInformationUri.setLang("en");
		additionalServiceInformationUri
				.setValue("http://uri.etsi.org/TrstSvc/eSigDir-1999-93-EC-TrustedList/SvcInfoExt/RootCA-QC");
		additionalServiceInformation.setURI(additionalServiceInformationUri);
		extension
				.getContent()
				.add(
						this.objectFactory
								.createAdditionalServiceInformation(additionalServiceInformation));
	}
}
