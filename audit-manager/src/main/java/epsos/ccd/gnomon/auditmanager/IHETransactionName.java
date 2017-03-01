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
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package epsos.ccd.gnomon.auditmanager;

/**
 * Enumeration for populating the EventType of the AuditMessage
 * One of the availbale epsos event ids
 * epsos-11: epsosIdentificationService
 * epsos-21: epsosPatientService
 * epsos-31: epsosOrderService
 * epsos-41: epsodDispensationServuceInitialize
 * epsos-42: epsodDispensationServuceDiscard
 * epsos-51: epsodConsentServicePut
 * epsos-52: epsodConsentServiceDiscard
 * epsos-53: epsodConsentServicePin
 * epsos-91: epsosHcpAuthentication
 * epsos-92: epsosTRCAssertion
 * epsos-93: epsosNCPTrustedServiceList
 * epsos-94: epsosPivotTranslation
 * epsos-cf: epsosCommunicationFailure 
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 */

public enum IHETransactionName {
//    epsosIdentificationServiceFindIdentityByTraits("epsosIdentityService::FindIdentityByTraits"),
//    epsosPatientServiceList("epsosPatientService::List"),
//    epsosOrderServiceList("epsosOrderService::List"),
//    epsosDispensationServiceInitialize("epsosDispensationService::Initialize"),
//    epsosDispensationServiceDiscard("epsosDispensationService::Discard"),
//    epsosConsentServicePut("epsosConsentService::Put"),
//    epsosConsentServiceDiscard("epsosConsentService::Discard"),
//    epsosConsentServicePin("epsosConsentService::PIN"),
//    epsosHcpAuthentication("identityProvider::HcpAuthentication"),
//    epsosTRCAssertion("ncp::TrcAssertion"),
//    epsosNCPTrustedServiceList("ncpConfigurationManager::ImportNSL"),
//    epsosPivotTranslation("ncpTransformationMgr::Translate"),
//    epsosCommunicationFailure("epsosCommunicationFailure");

    epsosIdentificationServiceFindIdentityByTraits("XCPD::CrossGatewayPatientDiscovery"),
    epsosPatientServiceList("XCA::CrossGatewayQuery"),
    epsosPatientServiceRetrieve("XCA::CrossGatewayRetrieve"),
    epsosOrderServiceList("XCA::CrossGatewayQuery"),
    epsosOrderServiceRetrieve("XCA::CrossGatewayRetrieve"),
    epsosDispensationServiceInitialize("XDR::ProvideandRegisterDocumentSet-b"),
    epsosDispensationServiceDiscard("XDR::ProvideandRegisterDocumentSet-b"),
    epsosConsentServicePut("XDR::ProvideandRegisterDocumentSet-b"),
    epsosConsentServiceDiscard("XDR::ProvideandRegisterDocumentSet-b"),
    epsosConsentServicePin("epsosConsentService::PIN"),
    epsosHcpAuthentication("XUA::ProvideX-UserAssertion"),
    epsosTRCAssertion("ncp::TrcAssertion"),
    epsosNCPTrustedServiceList("ncpConfigurationManager::ImportNSL"),
    epsosPivotTranslation("ncpTransformationMgr::Translate"),
    epsosCommunicationFailure("epsosCommunicationFailure"),
    epsosPACRetrieve("XCA::CrossGatewayRetrieve"),
    epsosHCERPut("XDR::ProvideandRegisterDocumentSet-b"),
    epsosMroServiceList("XCA::CrossGatewayQuery"),
    epsosMroServiceRetrieve("XCA::CrossGatewayRetrieve"),
    ehealthSMPQuery("SMP::Query"),
    ehealthSMPPush("SMP::Push");
	
    private String code;

    private IHETransactionName(String c) {
        code = c;
    }

    public String getCode() {
        return code;
    }

}
