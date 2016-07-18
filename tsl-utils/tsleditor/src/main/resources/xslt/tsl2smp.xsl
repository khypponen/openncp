<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tsl="http://uri.etsi.org/02231/v2#" 
    xmlns:ds="http://www.w3.org/2000/09/xmldsig#" 
    xmlns:ecc="http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#" 
    xmlns:tslx="http://uri.etsi.org/02231/v2/additionaltypes#" 
    xmlns:xades="http://uri.etsi.org/01903/v1.3.2#"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:ns="urn:esens:smp"
    xmlns="http://busdox.org/serviceMetadata/publishing/1.0/"
    xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
    xmlns:ids="http://busdox.org/transport/identifiers/1.0/"
    xmlns:wsa="http://www.w3.org/2005/08/addressing"
    exclude-result-prefixes="xs tsl ecc tslx"
    version="2.0">
    <xsl:output indent="yes"/>
    <xsl:strip-space elements="*"/>
    <!-- removed namespace:  xmlns="http://docs.oasis-open.org/bdxr/ns/SMP/2014/07" -->

    <!-- XSLT parameters -->
    <xsl:param name="ism"/>
    <xsl:param name="ismCreationDate"/>
    <xsl:param name="outputFolder"/>

    <xsl:template match="/">
        <xsl:variable name="country" select="substring-after(tsl:TrustServiceStatusList/@Id, 'NCPConfiguration-')"/>
        <xsl:variable name="technicalContactUrl" select="tsl:TrustServiceStatusList/tsl:SchemeInformation[1]/tsl:SchemeOperatorAddress[1]/tsl:ElectronicAddress[1]/tsl:URI[1]"/>
        <!--<xsl:variable name="technicalContactUrl" select="concat(tsl:TrustServiceStatusList/tsl:SchemeInformation[1]/tsl:SchemeOperatorName[1]/tsl:Name[1], 
            ' | ', 
            tsl:TrustServiceStatusList/tsl:SchemeInformation[1]/tsl:SchemeOperatorAddress[1]/tsl:PostalAddresses[1]/tsl:PostalAddress[1]/tsl:StreetAddress[1],
            ' , ', 
            tsl:TrustServiceStatusList/tsl:SchemeInformation[1]/tsl:SchemeOperatorAddress[1]/tsl:PostalAddresses[1]/tsl:PostalAddress[1]/tsl:Locality[1],
            ' , ',
            tsl:TrustServiceStatusList/tsl:SchemeInformation[1]/tsl:SchemeOperatorAddress[1]/tsl:PostalAddresses[1]/tsl:PostalAddress[1]/tsl:PostalCode[1],
            ' , ',
            tsl:TrustServiceStatusList/tsl:SchemeInformation[1]/tsl:SchemeOperatorAddress[1]/tsl:PostalAddresses[1]/tsl:PostalAddress[1]/tsl:CountryName[1],
            ' | ',
            tsl:TrustServiceStatusList/tsl:SchemeInformation[1]/tsl:SchemeOperatorAddress[1]/tsl:ElectronicAddress[1]/tsl:URI[1]
            )"/>-->
        <xsl:for-each select="tsl:TrustServiceStatusList/tsl:TrustServiceProviderList/tsl:TrustServiceProvider">
            <xsl:variable name="ncpFace" select="tsl:TSPInformation/tsl:TSPName/tsl:Name[1]"/>
            <xsl:variable name="tlsCertificate" select="tsl:TSPServices/tsl:TSPService/tsl:ServiceInformation[tsl:ServiceTypeIdentifier='http://uri.epsos.eu/Svc/Svctype/NCP']/tsl:ServiceDigitalIdentity/tsl:DigitalId[1]/tsl:X509Certificate[1]"/>
            <xsl:variable name="signatureCertificate" select="tsl:TSPServices/tsl:TSPService/tsl:ServiceInformation[tsl:ServiceTypeIdentifier='http://uri.epsos.eu/Svc/Svctype/NCP']/tsl:ServiceDigitalIdentity/tsl:DigitalId[2]/tsl:X509Certificate[1]"/> 
            <xsl:variable name="vpnCertificate" select="tsl:TSPServices/tsl:TSPService/tsl:ServiceInformation[tsl:ServiceTypeIdentifier='http://uri.epsos.eu/Svc/Svctype/VPNGateway']/tsl:ServiceDigitalIdentity/tsl:DigitalId[1]/tsl:X509Certificate[1]"/>
            <xsl:if test="$ncpFace = 'NCP-A'">
                <xsl:for-each select="tsl:TSPServices/tsl:TSPService">
                    <xsl:variable name="serviceTypeIdentifier" select="tsl:ServiceInformation/tsl:ServiceTypeIdentifier"/>
                    <xsl:variable name="serviceName" select="substring-after($serviceTypeIdentifier, 'http://uri.epsos.eu/Svc/Svctype/')"/>
                    <xsl:variable name="activationDate" select="tsl:ServiceInformation/tsl:StatusStartingTime"/>
                    <xsl:if test="$serviceName = 'VPNGateway'">
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::105'"/>
                            <xsl:with-param name="processIdentifier" select="'urn:ehealth:ncp:vpngateway'"/>
                            <xsl:with-param name="transportProfile" select="''"/>
                            <xsl:with-param name="endpointURI" select="'ipsec://'"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="$vpnCertificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the VPN Server configuration of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="true()"/>
                            <xsl:with-param name="filename" select="'VPN_Gateway_A'"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if test="$serviceName = 'PatientIdenitificationService'"> <!-- TODO: Fix this typo after TSL-Editor/Sync are fixed -->
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::11'"/>
                            <xsl:with-param name="processIdentifier" select="'urn:epsosIdentityService::FindIdentityByTraits'"/>
                            <xsl:with-param name="transportProfile" select="'urn:ihe:iti:2013:xcpd'"/>
                            <xsl:with-param name="endpointURI" select="''"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="$tlsCertificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the epSOS Identity Service - Find Identity By Traits of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="true()"/>
                            <xsl:with-param name="filename" select="'Patient_Identification_Service'"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if test="$serviceName = 'PatientService'">
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::21'"/>
                            <xsl:with-param name="processIdentifier" select="'urn:epsosPatientService::List'"/>
                            <xsl:with-param name="transportProfile" select="'urn:ihe:iti:2013:xcf'"/>
                            <xsl:with-param name="endpointURI" select="''"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="$tlsCertificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the epSOS Patient Service List of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="true()"/>
                            <xsl:with-param name="filename" select="'Patient_Service'"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if test="$serviceName = 'OrderService'">
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::31'"/>
                            <xsl:with-param name="processIdentifier" select="'urn:epsosOrderService::List'"/>
                            <xsl:with-param name="transportProfile" select="'urn:ihe:iti:2013:xcf'"/>
                            <xsl:with-param name="endpointURI" select="''"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="$tlsCertificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the epSOS Order Service List of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="true()"/>
                            <xsl:with-param name="filename" select="'Order_Service'"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if test="$serviceName = 'DispensationService'">
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::41'"/>
                            <xsl:with-param name="processIdentifier" select="'urn:epsosDispensationService::Initialize'"/>
                            <xsl:with-param name="transportProfile" select="'urn:ihe:iti:2013:xdr'"/>
                            <xsl:with-param name="endpointURI" select="''"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="$tlsCertificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the epSOS Dispensation Service Initialize of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="true()"/>
                            <xsl:with-param name="filename" select="'Dispensation_Service_Initialize'"/>
                        </xsl:call-template>  
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::42'"/>
                            <xsl:with-param name="processIdentifier" select="'urn:epsosDispensationService::Discard'"/>
                            <xsl:with-param name="transportProfile" select="'urn:ihe:iti:2013:xdr'"/>
                            <xsl:with-param name="endpointURI" select="''"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="$tlsCertificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the epSOS Dispensation Service Discard of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="true()"/>
                            <xsl:with-param name="filename" select="'Dispensation_Service_Discard'"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if test="$serviceName = 'ConsentService'">
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::51'"/>
                            <xsl:with-param name="processIdentifier" select="'urn:epsosConsentService::Put'"/>
                            <xsl:with-param name="transportProfile" select="'urn:ihe:iti:2013:xdr'"/>
                            <xsl:with-param name="endpointURI" select="''"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="$tlsCertificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the epSOS Consent Service Put of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="true()"/>
                            <xsl:with-param name="filename" select="'Consent_Service_Put'"/>
                        </xsl:call-template>  
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::52'"/>
                            <xsl:with-param name="processIdentifier" select="'urn:epsosConsentService::Discard'"/>
                            <xsl:with-param name="transportProfile" select="'urn:ihe:iti:2013:xdr'"/>
                            <xsl:with-param name="endpointURI" select="''"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="$tlsCertificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the epSOS Consent Service Discard of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="true()"/>
                            <xsl:with-param name="filename" select="'Consent_Service_Discard'"/>
                        </xsl:call-template>
                    </xsl:if>
                </xsl:for-each>
                <!-- We only need to generate the ISM for countries that have NCP-A and if they provide ISM -->
                <xsl:if test="$ism and $ismCreationDate">
	                <xsl:call-template name="ServiceMetadata">
	                    <xsl:with-param name="country" select="$country"/>
	                    <xsl:with-param name="documentIdentifier" select="'epsos::107'"/>
	                    <xsl:with-param name="processIdentifier" select="concat('urn:ehealth:ncp:',lower-case($country),':ism')"/>
	                    <xsl:with-param name="transportProfile" select="'urn:ehealth:transport:none'"/>
	                    <xsl:with-param name="endpointURI" select="''"/>
	                    <xsl:with-param name="serviceActivationDate" select="$ismCreationDate"/>
	                    <xsl:with-param name="certificate" select="''"/>
	                    <xsl:with-param name="serviceDescription" select="concat('This is the International Search Mask of ',$country)"/>
	                    <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
	                    <xsl:with-param name="hasServiceSupplyPoints" select="false()"/>
	                    <xsl:with-param name="endpointExtension" select="document($ism)"/>
	                    <xsl:with-param name="filename" select="'International_Search_Mask'"/>
	                </xsl:call-template>
                </xsl:if>
            </xsl:if>
            <xsl:if test="$ncpFace = 'NCP-B'">
                <xsl:for-each select="tsl:TSPServices/tsl:TSPService">
                    <xsl:variable name="serviceTypeIdentifier" select="tsl:ServiceInformation/tsl:ServiceTypeIdentifier"/>
                    <xsl:variable name="serviceName" select="substring-after($serviceTypeIdentifier, 'http://uri.epsos.eu/Svc/Svctype/')"/>
                    <xsl:variable name="activationDate" select="tsl:ServiceInformation/tsl:StatusStartingTime"/>
                    <xsl:if test="$serviceName = 'VPNGateway'">
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::106'"/>
                            <xsl:with-param name="processIdentifier" select="'urn:ehealth:ncp:vpngateway'"/>
                            <xsl:with-param name="transportProfile" select="''"/>
                            <xsl:with-param name="endpointURI" select="''"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="$vpnCertificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the VPN Client configuration of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="false()"/>
                            <xsl:with-param name="filename" select="'VPN_Gateway_B'"/>
                        </xsl:call-template>
                    </xsl:if>
                    <!-- Some countries have a misconfigured entry for this (e.g. 'IdP' vs 'IdV') -->
                    <xsl:if test="$serviceName = 'IdV' or $serviceName = 'IdP'">
                        <xsl:call-template name="ServiceMetadata">
                            <xsl:with-param name="country" select="$country"/>
                            <xsl:with-param name="documentIdentifier" select="'epsos::91'"/>
                            <xsl:with-param name="processIdentifier" select="concat('urn:ehealth:ncp:',lower-case($country),':ncpb-idp')"/>
                            <xsl:with-param name="transportProfile" select="'urn:oasis:names:tc:SAML:2.0:protocol'"/>
                            <xsl:with-param name="endpointURI" select="'urn:idp:countryB'"/>
                            <xsl:with-param name="serviceActivationDate" select="$activationDate"/>
                            <xsl:with-param name="certificate" select="./tsl:ServiceInformation/tsl:ServiceDigitalIdentity/tsl:DigitalId[1]/tsl:X509Certificate"/>
                            <xsl:with-param name="serviceDescription" select="concat('This is the epSOS Identity Provider of the ',$country,' NCP')"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="hasServiceSupplyPoints" select="false()"/>
                            <xsl:with-param name="filename" select="'Identity_Provider'"/>
                        </xsl:call-template>
                    </xsl:if>
                </xsl:for-each>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="ServiceMetadata">
        <xsl:param name="country"/>
        <xsl:param name="documentIdentifier"/>
        <xsl:param name="processIdentifier"/>
        <xsl:param name="transportProfile"/>
        <xsl:param name="endpointURI"/>
        <xsl:param name="serviceActivationDate"/>
        <xsl:param name="certificate"/>
        <xsl:param name="serviceDescription"/>
        <xsl:param name="technicalContactUrl"/>
        <xsl:param name="hasServiceSupplyPoints"/>
        <xsl:param name="endpointExtension"/>
        <xsl:param name="filename"/>
        <xsl:result-document href="{concat('file:///',$outputFolder,'/',$country,'/',$filename,'_',$country,'.xml')}">
            <ServiceMetadata>
                <ServiceInformation>
                    <xsl:call-template name="ParticipantIdentifier">
                        <xsl:with-param name="country" select="$country"/>
                    </xsl:call-template>
                    <xsl:call-template name="DocumentIdentifier">
                        <xsl:with-param name="documentIdentifier" select="$documentIdentifier"/>
                    </xsl:call-template>
                    <xsl:call-template name="ProcessList">
                        <xsl:with-param name="processIdentifier" select="$processIdentifier"/>
                        <xsl:with-param name="transportProfile" select="$transportProfile"/>
                        <xsl:with-param name="endpointURI" select="$endpointURI"/>
                        <xsl:with-param name="serviceActivationDate" select="$serviceActivationDate"/>
                        <xsl:with-param name="certificate" select="$certificate"/>
                        <xsl:with-param name="serviceDescription" select="$serviceDescription"/>
                        <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                        <xsl:with-param name="hasServiceSupplyPoints" select="$hasServiceSupplyPoints"/>
                        <xsl:with-param name="endpointExtension" select="$endpointExtension"/>
                    </xsl:call-template>
                </ServiceInformation>
            </ServiceMetadata>
        </xsl:result-document>
        <!-- newline: <xsl:text>&#xa;</xsl:text> -->
    </xsl:template>
    
    <xsl:template name="ParticipantIdentifier">
        <xsl:param name="country"/>
        <ids:ParticipantIdentifier scheme="ehealth-actorid-qns">urn:ehealth:<xsl:value-of select="lower-case($country)"/>:ncpb-idp</ids:ParticipantIdentifier>
    </xsl:template>
    
    <xsl:template name="DocumentIdentifier">
        <xsl:param name="documentIdentifier"/>
        <ids:DocumentIdentifier scheme="ehealth-resid-qns">urn::epsos##services:extended:<xsl:value-of select="$documentIdentifier"/></ids:DocumentIdentifier>
    </xsl:template>
    
    <xsl:template name="ProcessList">
        <xsl:param name="processIdentifier"/>
        <xsl:param name="transportProfile"/>
        <xsl:param name="endpointURI"/>
        <xsl:param name="serviceActivationDate"/>
        <xsl:param name="certificate"/>
        <xsl:param name="serviceDescription"/>
        <xsl:param name="technicalContactUrl"/>
        <xsl:param name="hasServiceSupplyPoints"/>
        <xsl:param name="endpointExtension"/>
        <ProcessList>
            <Process>
                <xsl:call-template name="ProcessIdentifier">
                    <xsl:with-param name="processIdentifier" select="$processIdentifier"/>
                </xsl:call-template>
                <ServiceEndpointList>
                    <xsl:if test="$hasServiceSupplyPoints = true()"> <!-- All except IdP, VPN-B, ISM -->
                        <xsl:for-each select="tsl:ServiceInformation/tsl:ServiceSupplyPoints/tsl:ServiceSupplyPoint">
                            <xsl:call-template name="Endpoint">
                                <xsl:with-param name="transportProfile" select="$transportProfile"/>
                                <xsl:with-param name="endpointURI"><xsl:value-of select="$endpointURI"/><xsl:value-of select="."/></xsl:with-param>
                                <xsl:with-param name="serviceActivationDate" select="$serviceActivationDate"/>
                                <xsl:with-param name="certificate" select="$certificate"/>
                                <xsl:with-param name="serviceDescription" select="$serviceDescription"/>
                                <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            </xsl:call-template>
                        </xsl:for-each>
                    </xsl:if>
                    <xsl:if test="$hasServiceSupplyPoints = false()"> <!-- IdP, VPN-B, ISM -->
                        <xsl:call-template name="Endpoint">
                            <xsl:with-param name="transportProfile" select="$transportProfile"/>
                            <xsl:with-param name="endpointURI" select="$endpointURI"/>
                            <xsl:with-param name="serviceActivationDate" select="$serviceActivationDate"/>
                            <xsl:with-param name="certificate" select="$certificate"/>
                            <xsl:with-param name="serviceDescription" select="$serviceDescription"/>
                            <xsl:with-param name="technicalContactUrl" select="$technicalContactUrl"/>
                            <xsl:with-param name="endpointExtension" select="$endpointExtension"/>
                        </xsl:call-template>
                    </xsl:if>
                </ServiceEndpointList>
            </Process>
        </ProcessList>
    </xsl:template>
    
    <xsl:template name="ProcessIdentifier">
        <xsl:param name="processIdentifier"/>
        <ids:ProcessIdentifier scheme="ehealth-procid-qns"><xsl:value-of select="$processIdentifier"/></ids:ProcessIdentifier>
    </xsl:template>
    
    <xsl:template name="Endpoint">
        <xsl:param name="transportProfile"/>
        <xsl:param name="endpointURI"/>
        <xsl:param name="serviceActivationDate"/>
        <xsl:param name="certificate"/>
        <xsl:param name="serviceDescription"/>
        <xsl:param name="technicalContactUrl"/>
        <xsl:param name="endpointExtension"/>
        <Endpoint>
            <xsl:attribute name="transportProfile" select="$transportProfile"/>           
            <wsa:EndpointReference>
                <wsa:Address><xsl:value-of select="normalize-space($endpointURI)"/></wsa:Address>    
            </wsa:EndpointReference>
            <RequireBusinessLevelSignature>false</RequireBusinessLevelSignature>
            <MinimumAuthenticationLevel>urn:epSOS:loa:1</MinimumAuthenticationLevel>
            <ServiceActivationDate><xsl:value-of select="$serviceActivationDate"/></ServiceActivationDate>
            <!-- Service will expire 10 years after its activation date -->
            <ServiceExpirationDate><xsl:value-of select="xs:dateTime($serviceActivationDate) + xs:yearMonthDuration('P10Y')"/></ServiceExpirationDate>
            <Certificate><xsl:value-of select="$certificate"/></Certificate>
            <ServiceDescription><xsl:value-of select="$serviceDescription"/></ServiceDescription>
            <TechnicalContactUrl><xsl:value-of select="$technicalContactUrl"/></TechnicalContactUrl>
            <TechnicalInformationUrl><xsl:value-of select="$technicalContactUrl"/></TechnicalInformationUrl>
            <!-- Every SMP file will have Endpoint/Extension, either empty or not (e.g. search mask) -->
            <Extension><xsl:copy-of select="$endpointExtension"/></Extension>
        </Endpoint>
    </xsl:template>
 
</xsl:stylesheet>