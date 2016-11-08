<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:ns2="http://www.w3.org/2005/08/addressing"
    xmlns:ns3="http://busdox.org/serviceMetadata/publishing/1.0/" 
    xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
    xmlns:ns4="http://www.w3.org/2000/09/xmldsig#" 
    xmlns:ids="http://busdox.org/transport/identifiers/1.0/" 
    xmlns:ns="urn:esens:smp" 
    xmlns:wsa="http://www.w3.org/2005/08/addressing" 
    xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" 
    xmlns:xades="http://uri.etsi.org/01903/v1.3.2#" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:output indent="no"/>
    <xsl:strip-space elements="*"/>
    
    <!-- Identity Transform -->
    <xsl:template match="node()|@*" name="identity">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- Removes the root element SignedServiceMetadata -->
    <xsl:template match="/*">
        <xsl:apply-templates select="node()" />
    </xsl:template>
    
    <!-- Remove ds: prefix from Signature element -->
    <xsl:template match="/ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:Extension[1]/ns4:Signature[1]">
        <xsl:element name="{local-name()}" namespace="http://www.w3.org/2000/09/xmldsig#">
            <xsl:apply-templates select="node() | @*"/>
        </xsl:element>
    </xsl:template>
    
    <!-- Avoid propagation of busdox namespaces inside the Signature and, when existing, in the search mask -->
    <xsl:template match="/ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:ProcessList[1]/ns3:Process[1]/ns3:ServiceEndpointList[1]/ns3:Endpoint[1]/ns3:Extension[1]/*[namespace-uri()='' and local-name()='root'][1]/*[namespace-uri()='' and local-name()='patientSearch'][1] |
        /ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:Extension[1]/ns4:Signature[1]/ns4:SignedInfo[1] | 
        /ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:Extension[1]/ns4:Signature[1]/ns4:SignatureValue[1] |
        /ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:Extension[1]/ns4:Signature[1]/ns4:KeyInfo[1]">
        <xsl:copy-of select="." copy-namespaces="no"/>
    </xsl:template>
    
    <!-- Copies <root> removing extra namespaces -->
    <xsl:template match="/ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:Extension[1]">
        <xsl:element name="{local-name()}" namespace="{namespace-uri()}">
            <xsl:apply-templates select="node() | @*"/>
        </xsl:element>
    </xsl:template>
    
    <!-- Replaces ServiceMetadata node with correct namespaces -->
    <xsl:template match="/ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]">
        <xsl:element name="{local-name()}" namespace="http://busdox.org/serviceMetadata/publishing/1.0/">
            <xsl:namespace name="ds" select="'http://www.w3.org/2000/09/xmldsig#'"/>
            <xsl:namespace name="ids" select="'http://busdox.org/transport/identifiers/1.0/'"/>
            <xsl:namespace name="ns" select="'urn:esens:smp'"/>
            <xsl:namespace name="wsa" select="'http://www.w3.org/2005/08/addressing'"/>
            <xsl:namespace name="wsu" select="'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd'"/>
            <xsl:namespace name="xades" select="'http://uri.etsi.org/01903/v1.3.2#'"/>
            <xsl:namespace name="xsi" select="'http://www.w3.org/2001/XMLSchema-instance'"/>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    
    <!-- Remove unused ns2:ReferenceParameters -->
    <xsl:template match="/ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:ProcessList[1]/ns3:Process[1]/ns3:ServiceEndpointList[1]/ns3:Endpoint[1]/ns2:EndpointReference[1]/ns2:ReferenceParameters[1]"/>
    
    <!-- Replace ns2:* elements with wsa:* -->
    <xsl:template match="ns2:*">
        <xsl:element name="wsa:{local-name()}">
            <xsl:apply-templates select="node() | @*"/>
        </xsl:element>
    </xsl:template>
    
    <!-- Add ids: prefix to nodes belonging to busdox identifiers namespace -->
    <xsl:template match="//*[namespace-uri()='http://busdox.org/transport/identifiers/1.0/']">
        <xsl:element name="ids:{local-name()}">
            <xsl:apply-templates select="node() | @*"/>
        </xsl:element>
    </xsl:template>
    
    <!-- Remove all namespace prefixes -->
    <xsl:template match="*">
        <xsl:element name="{local-name()}" namespace="http://busdox.org/serviceMetadata/publishing/1.0/">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    
    <!-- Add milliseconds to ServiceActivationDate -->
    <!--<xsl:template match="/ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:ProcessList[1]/ns3:Process[1]/ns3:ServiceEndpointList[1]/ns3:Endpoint[1]/ns3:ServiceActivationDate[1]">
        <xsl:variable name="serviceActivationDate" select="."/>
        <xsl:element name="ServiceActivationDate" namespace="http://busdox.org/serviceMetadata/publishing/1.0/"><xsl:value-of select="format-dateTime($serviceActivationDate, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]Z')"/></xsl:element>
    </xsl:template>-->
    
    <!-- Removes whitespace from the Endpoint Certificate field -->
    <xsl:template match="/ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:ProcessList[1]/ns3:Process[1]/ns3:ServiceEndpointList[1]/ns3:Endpoint[1]/ns3:Certificate[1]/text()[normalize-space()]">
        <xsl:value-of select="translate(normalize-space(),' ','')"/>
    </xsl:template>
    
    <!-- Adds the <root xmlns=""> -->
    <xsl:template match="/ns3:SignedServiceMetadata/ns3:ServiceMetadata[1]/ns3:ServiceInformation[1]/ns3:ProcessList[1]/ns3:Process[1]/ns3:ServiceEndpointList[1]/ns3:Endpoint[1]/ns3:Extension[1]/*[namespace-uri()='' and local-name()='root'][1]">
        <xsl:element name="root" namespace="">
            <xsl:apply-templates select="node() | @*"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of 
            select="translate(.,'&#xA;','')"/>
    </xsl:template>
 
</xsl:stylesheet>