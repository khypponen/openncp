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

public enum IHEEventType {
    epsosIdentificationServiceFindIdentityByTraits("ITI-55"),
    epsosPatientServiceList("ITI-38"),
    epsosPatientServiceRetrieve("ITI-39"),
    epsosOrderServiceList("ITI-38"),
    epsosOrderServiceRetrieve("ITI-39"),
    epsosDispensationServiceInitialize("ITI-41"),
    epsosDispensationServiceDiscard("ITI-41"),
    epsosConsentServicePut("ITI-41"),
    epsosConsentServiceDiscard("ITI-41"),
    epsosConsentServicePin("epsos-53"),
    epsosHcpAuthentication("ITI-40"),
    epsosTRCAssertion("epsos-92"),
    epsosNCPTrustedServiceList("epsos-93"),
    epsosPivotTranslation("epsos-94"),
    epsosCommunicationFailure("epsos-cf"),
    epsosPACRetrieve("epsos-95"),
    epsosHCERPut("epsos-96"),
    epsosMroList("ITI-38"),
    epsosMroRetrieve("ITI-39"),
    ehealthSMPQuery("ehealth-193"),
    ehealthSMPPush("ehealth-194");
    
    private String code;

    private IHEEventType(String c) {
        code = c;
    }

    public String getCode() {
        return code;
    }

}
