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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.etsi.uri._02231.v2.AddressType;
import org.etsi.uri._02231.v2.ElectronicAddressType;
import org.etsi.uri._02231.v2.InternationalNamesType;
import org.etsi.uri._02231.v2.NonEmptyMultiLangURIListType;
import org.etsi.uri._02231.v2.NonEmptyMultiLangURIType;
import org.etsi.uri._02231.v2.ObjectFactory;
import org.etsi.uri._02231.v2.PostalAddressListType;
import org.etsi.uri._02231.v2.PostalAddressType;
import org.etsi.uri._02231.v2.TSPInformationType;
import org.etsi.uri._02231.v2.TSPServiceType;
import org.etsi.uri._02231.v2.TSPServicesListType;
import org.etsi.uri._02231.v2.TSPType;

public class TrustServiceProvider {

	private final TSPType tsp;

	private List<TrustService> trustServices;

	private final ObjectFactory objectFactory;

	TrustServiceProvider(TSPType tsp) {
		this.tsp = tsp;
		this.objectFactory = new ObjectFactory();
	}

	public TrustServiceProvider(String name, String tradeName) {
		this.objectFactory = new ObjectFactory();
		this.tsp = this.objectFactory.createTSPType();
		TSPInformationType tspInformation = this.objectFactory
				.createTSPInformationType();
		this.tsp.setTSPInformation(tspInformation);

		InternationalNamesType tspNames = this.objectFactory
				.createInternationalNamesType();
		TrustServiceListUtils.setValue(name, Locale.getDefault(), tspNames);
		tspInformation.setTSPName(tspNames);

		InternationalNamesType tspTradeNames = this.objectFactory
				.createInternationalNamesType();
		TrustServiceListUtils
				.setValue(tradeName, Locale.getDefault(), tspTradeNames);
		tspInformation.setTSPTradeName(tspTradeNames);
	}

	public TSPType getTSP() {
		return this.tsp;
	}

	public void addPostalAddress(Locale locale, String streetAddress,
			String locality, String stateOrProvince, String postalCode,
			String countryName) {
		TSPInformationType tspInformation = getTSPInformation();
		AddressType address = tspInformation.getTSPAddress();
		if (null == address) {
			address = this.objectFactory.createAddressType();
			tspInformation.setTSPAddress(address);
		}
		PostalAddressListType postalAddresses = address.getPostalAddresses();
		if (null == postalAddresses) {
			postalAddresses = this.objectFactory.createPostalAddressListType();
			address.setPostalAddresses(postalAddresses);
		}
		List<PostalAddressType> postalAddressList = postalAddresses
				.getPostalAddress();
		PostalAddressType postalAddress = this.objectFactory
				.createPostalAddressType();
		postalAddressList.add(postalAddress);

		postalAddress.setLang(locale.getLanguage());
		postalAddress.setStreetAddress(streetAddress);
		postalAddress.setLocality(locality);
		postalAddress.setStateOrProvince(stateOrProvince);
		postalAddress.setPostalCode(postalCode);
		postalAddress.setCountryName(countryName);
	}

	public PostalAddressType getPostalAddress() {
		return getPostalAddress(Locale.getDefault());
	}

	public PostalAddressType getPostalAddress(Locale locale) {
		TSPInformationType tspInformation = this.tsp.getTSPInformation();
		if (null == tspInformation) {
			return null;
		}
		AddressType address = tspInformation.getTSPAddress();
		if (null == address) {
			return null;
		}
		PostalAddressListType postalAddresses = address.getPostalAddresses();
		if (null == postalAddresses) {
			return null;
		}
		List<PostalAddressType> postalAddressList = postalAddresses
				.getPostalAddress();
		for (PostalAddressType postalAddress : postalAddressList) {
			String lang = postalAddress.getLang();
			if (0 != locale.getLanguage().compareToIgnoreCase(lang)) {
				continue;
			}
			return postalAddress;
		}
		return null;
	}

	public void addElectronicAddress(String... electronicAddressUris) {
		TSPInformationType tspInformation = getTSPInformation();
		AddressType address = tspInformation.getTSPAddress();
		if (null == address) {
			address = this.objectFactory.createAddressType();
			tspInformation.setTSPAddress(address);
		}
		ElectronicAddressType electronicAddress = address
				.getElectronicAddress();
		if (null == electronicAddress) {
			electronicAddress = this.objectFactory
					.createElectronicAddressType();
			address.setElectronicAddress(electronicAddress);
		}
		List<String> uris = electronicAddress.getURI();
		for (String electronicAddressUri : electronicAddressUris) {
			uris.add(electronicAddressUri);
		}
	}

	public List<String> getElectronicAddress() {
		List<String> resultElectronicAddress = new LinkedList<String>();
		TSPInformationType tspInformation = this.tsp.getTSPInformation();
		if (null == tspInformation) {
			return resultElectronicAddress;
		}
		AddressType address = tspInformation.getTSPAddress();
		if (null == address) {
			return resultElectronicAddress;
		}
		ElectronicAddressType electronicAddress = address
				.getElectronicAddress();
		if (null == electronicAddress) {
			return resultElectronicAddress;
		}
		List<String> uris = electronicAddress.getURI();
		for (String uri : uris) {
			resultElectronicAddress.add(uri);
		}
		return resultElectronicAddress;
	}

	private TSPInformationType getTSPInformation() {
		TSPInformationType tspInformation = this.tsp.getTSPInformation();
		if (null == tspInformation) {
			tspInformation = this.objectFactory.createTSPInformationType();
			this.tsp.setTSPInformation(tspInformation);
		}
		return tspInformation;
	}

	public void addInformationUri(Locale locale, String informationUri) {
		TSPInformationType tspInformation = getTSPInformation();
		NonEmptyMultiLangURIListType tspInformationURI = tspInformation
				.getTSPInformationURI();
		if (null == tspInformationURI) {
			tspInformationURI = this.objectFactory
					.createNonEmptyMultiLangURIListType();
			tspInformation.setTSPInformationURI(tspInformationURI);
		}
		List<NonEmptyMultiLangURIType> uris = tspInformationURI.getURI();
		NonEmptyMultiLangURIType uri = this.objectFactory
				.createNonEmptyMultiLangURIType();
		uri.setLang(locale.getLanguage());
		uri.setValue(informationUri);
		uris.add(uri);
	}

	public List<String> getInformationUris() {
		return getInformationUris(Locale.getDefault());
	}

	public List<String> getInformationUris(Locale locale) {
		List<String> results = new LinkedList<String>();
		TSPInformationType tspInformation = this.tsp.getTSPInformation();
		if (null == tspInformation) {
			return results;
		}
		NonEmptyMultiLangURIListType tspInformationURI = tspInformation
				.getTSPInformationURI();
		if (null == tspInformationURI) {
			return results;
		}
		List<NonEmptyMultiLangURIType> uris = tspInformationURI.getURI();
		for (NonEmptyMultiLangURIType uri : uris) {
			String lang = uri.getLang();
			if (0 != locale.getLanguage().compareToIgnoreCase(lang)) {
				continue;
			}
			results.add(uri.getValue());
		}
		return results;
	}

	public String getName(Locale locale) {
		TSPInformationType tspInformation = this.tsp.getTSPInformation();
		InternationalNamesType i18nTspName = tspInformation.getTSPName();
		String tspName = TrustServiceListUtils.getValue(i18nTspName, locale);
		return tspName;
	}

	public String getTradeName(Locale locale) {
		TSPInformationType tspInformation = this.tsp.getTSPInformation();
		InternationalNamesType i18nTspTradeName = tspInformation
				.getTSPTradeName();
		String tspTradeName = TrustServiceListUtils.getValue(i18nTspTradeName,
				locale);
		return tspTradeName;
	}

	public String getTradeName() {
		return getTradeName(Locale.getDefault());
	}

	public String getName() {
		Locale locale = Locale.getDefault();
		return getName(locale);
	}

	public List<TrustService> getTrustServices() {
		if (null != this.trustServices) {
			return this.trustServices;
		}
		this.trustServices = new LinkedList<TrustService>();
		TSPServicesListType tspServices = this.tsp.getTSPServices();
		if (null == tspServices) {
			return this.trustServices;
		}
		List<TSPServiceType> tspServiceList = tspServices.getTSPService();
		for (TSPServiceType tspService : tspServiceList) {
			TrustService trustService = new TrustService(tspService);
			this.trustServices.add(trustService);
		}
		return this.trustServices;
	}

	public void addTrustService(TrustService trustService) {
		TSPServicesListType tspServicesList = this.tsp.getTSPServices();
		if (null == tspServicesList) {
			tspServicesList = this.objectFactory.createTSPServicesListType();
			this.tsp.setTSPServices(tspServicesList);
		}
		List<TSPServiceType> tspServices = tspServicesList.getTSPService();
		tspServices.add(trustService.getTSPService());
		// reset java model cache
		this.trustServices = null;
	}


        @Override
	public String toString() {
		return this.getName();
	}
}
