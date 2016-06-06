<?xml version="1.0" encoding="UTF-8"?>
<!-- Revision = $Revision: 21223 $ --><schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3"
        xmlns:iso="http://purl.oclc.org/dsdl/schematron"
        xmlns:sch="http://www.ascc.net/xml/schematron"
        xmlns:xi="http://www.w3.org/2003/XInclude"
        queryBinding="xslt2"
        defaultPhase="all">
  <title>Schematron schema for validating epSOS ePrescription Document</title>
  <ns prefix="cda" uri="urn:hl7-org:v3"/>
  <ns prefix="epsos" uri="urn:epsos-org:ep:medication"/>
  <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
  <!--Phases -->
  <phase id="all">
      <active pattern="variables"/>
      <active pattern="FileCheck-codes"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4.eP-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eP-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eP-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.2-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.9-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eP-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eP-codes"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.2-codes"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-codes"/>
   </phase>
  <phase id="no-codes">
      <active pattern="variables"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4.eP-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eP-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eP-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.2-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.9-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-errors"/>
   </phase>
  <!--Setting Variables -->
  <pattern id="variables">
      <let name="usedDataDoc" value="'epSOS_MVC_V1_6'"/>
      <let name="newSpecDoc"
           value="'epSOS_D3.9.1_Appendix_B1_Implementation_v1.2_20101220'"/>
      <let name="oldSpecDoc"
           value="'epSOS_D3.9.1_Appendix_B1_Implementation_v0.0.5.20100809'"/>
      <let name="BPPCSpecDoc" value="'IHE_ITI_TF_Rev7-0_Vol3_FT_2010-08-10.doc'"/>
  </pattern>
  <!--Check codes files -->
  <pattern id="FileCheck-codes">
      <let name="usedDataxml" value="'http://gazelle.ihe.net/epSOS/codes/epSOS-pivot.xml'"/>
      <let name="pivotcodes" value="document($usedDataxml)"/>
      <let name="pivotcodesavailable" value="doc-available($usedDataxml)"/>
      <!--Check if the data xml document is present-->
    <rule context="//cda:ClinicalDocument">
         <assert test="$pivotcodesavailable">Error: file not found : 'epSOS-pivot.xml'.</assert>
         <report test="$pivotcodesavailable">Success: The epSOS-pivot.xml file is present.</report>
      </rule>
  </pattern>
  <!--Check errors -->
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
    <!-- <title>epSOS ePrescription Documents Specification - errors validation phase</title>-->
    <rule context="/cda:ClinicalDocument">
      <!-- R2 Verify that the template id is ePrescription Document -->
      <assert test="//cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.1.1&#34;]">Error: R2 The ePrescription Document requires the ePrescription Document template ID (1.3.6.1.4.1.12559.11.10.1.3.1.1.1).</assert>
         <report test="//cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.1.1&#34;]">Success: R2 ePrescription Document template ID (1.3.6.1.4.1.12559.11.10.1.3.1.1.1) is included.</report>
         <!-- Verify that the correct code system is used -->
      <assert test="cda:code[@code = &#34;57833-6&#34;] and cda:code[@displayName = 'ePrescription']">Error: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The ePrescription document type code element "@code = 
      <value-of select="cda:code/@code"/>", or "@displayName = 
      <value-of select="cda:code/@displayName"/>" are not valid, the attribute '@code' must be set to the value "57833-6" which is the code corresponding to "Prescription for medication" in the loinc code system. And '@displayName' must be set to the
      value "ePrescription".</assert>
         <report test="cda:code[@code = &#34;57833-6&#34;] and cda:code[@displayName = 'ePrescription']">Success: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The ePrescription document type code is fulfilled.</report>
         <assert test="cda:code[@codeSystem = &#34;2.16.840.1.113883.6.1&#34;]">Error: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The ePrescription document type code attribute "@codeSystem = 
      <value-of select="cda:code/@codeSystem"/>", is not valid, it must be set to the value "2.16.840.1.113883.6.1" corresponding to the LOINC Code system OID.</assert>
         <report test="cda:code[@codeSystem = &#34;2.16.840.1.113883.6.1&#34;]">Success: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The ePrescription document type code is correctly set to the value "2.16.840.1.113883.6.1" corresponding to the LOINC Code system OID.</report>
         <assert test="//cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.2.1&#34;]">Error: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The ePrescription Document requires the ePrescription Section name\template ID (1.3.6.1.4.1.12559.11.10.1.3.1.2.1).</assert>
         <report test="//cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.2.1&#34;]">Success: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The ePrescription Document correctly includes the ePrescription Section name\template ID (1.3.6.1.4.1.12559.11.10.1.3.1.2.1).</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Medical Documents Specification - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.1.1&#34;]">
         <assert test="self::cda:ClinicalDocument">Error: IHE PCC Medical Document Specification template ID (1.3.6.1.4.1.19376.1.5.3.1.1.1) is in the wrong location. An IHE PCC Medical Document Specification template ID shall be only used on a
      ClinicalDocument.</assert>
         <report test="self::cda:ClinicalDocument">Success: IHE PCC Medical Document Specification template ID (1.3.6.1.4.1.19376.1.5.3.1.1.1) is in the right location. An IHE PCC Medical Document Specification template ID shall be only used on a
      ClinicalDocument.</report>
         <assert test="cda:code[@codeSystem = &#34;2.16.840.1.113883.6.1&#34;]">Error: In IHE PCC Medical Document Specification template ID (1.3.6.1.4.1.19376.1.5.3.1.1.1), the document type code must come from the LOINC code system
      (2.16.840.1.113883.6.1).</assert>
         <report test="cda:code[@codeSystem = &#34;2.16.840.1.113883.6.1&#34;]">Success: In IHE PCC Medical Document Specification template ID (1.3.6.1.4.1.19376.1.5.3.1.1.1), the document type code is from the LOINC code system
      (2.16.840.1.113883.6.1).</report>
      </rule>
      <!--epSOS-->
    <!--(cf.epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc : 8  Common Header Data Elements)-->
    <!-- R1.11.1 Date of creation-->
    <rule context="/cda:ClinicalDocument">
         <assert test="(matches(cda:effectiveTime/@value,'\d{4}') or matches(cda:effectiveTime/@value,'\d{6}') or matches(cda:effectiveTime/@value,'\d{8}')) and cda:effectiveTime and count(cda:effectiveTime)&lt;2">Error: R1.11.1 Date of creation is
      required. The attribute "value" of the element "effectiveTime" shall contain 4,6 or 8 digits, it may be a partial date such as only the year. Expecting a string such as YYYY[MM[DD]]. The element "effectiveTime" cannot be repeated (cardinality
      [1..1]).</assert>
         <report test="(matches(cda:effectiveTime/@value,'\d{4}') or matches(cda:effectiveTime/@value,'\d{6}') or matches(cda:effectiveTime/@value,'\d{8}')) and cda:effectiveTime and count(cda:effectiveTime)&lt;2">Success: R1.11.1 Date of creation of the
      document is fulfilled.</report>
         <!-- R1.11.3 Document ID-->
      <assert test="cda:id and count(cda:id)&lt;2">Error: R1.11.3 Document ID is required. The element "id" cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:id and count(cda:id)&lt;2">Success: R1.11.3 Document ID is fulfilled</report>
         <!-- R1.11.6 Clinical document code-->
      <assert test="cda:code and cda:code[@codeSystem='2.16.840.1.113883.6.1'] and cda:code/@code and count(cda:code)&lt;2">Error: R1.11.3 Clinical document code is required. The attributes "codeSystem" and "code" of the element "code" shall not be
      empty. The attribute "codeSystem" sould be "2.16.840.1.113883.6.1". The element "code" cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:code and cda:code[@codeSystem='2.16.840.1.113883.6.1'] and cda:code/@code and count(cda:code)&lt;2">Success: R1.11.3 Clinical document code is fulfilled.</report>
         <!-- R1.11.7 Clinical document title-->
      <assert test="cda:title and (every $i in cda:title satisfies string-length(normalize-space($i)) &gt; 0) and count(cda:title)&lt;2">Error: R1.11.7 Clinical document title is required. The element "title" shall not be empty. The element "title"
      cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:title and (every $i in cda:title satisfies string-length(normalize-space($i)) &gt; 0) and count(cda:title)&lt;2">Success: R1.11.7 Clinical document title is fulfilled.</report>
         <!-- R1.11.8 Confidentiality code-->
      <assert test="cda:confidentialityCode and ((cda:confidentialityCode[@codeSystem='2.16.840.1.113883.5.25'] and cda:confidentialityCode/@code) or cda:confidentialityCode/@nullFlavor) and count(cda:confidentialityCode)&lt;2">Error: R1.11.8
      Confidentiality code is required. The attributes 'codeSystem' and 'code' of the element "confidentialityCode" shall not be empty otherwise the nullFlavor attribute shall be present. The attribute 'codeSystem' sould be '2.16.840.1.113883.5.25'. The
      element "confidentialityCode" cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:confidentialityCode and ((cda:confidentialityCode[@codeSystem='2.16.840.1.113883.5.25'] and cda:confidentialityCode/@code) or cda:confidentialityCode/@nullFlavor) and count(cda:confidentialityCode)&lt;2">Success: R1.11.8
      Confidentiality code is fulfilled.</report>
         <!-- R1.11.10 Document Language Code -->
      <assert test="cda:languageCode and cda:languageCode/@code and count(cda:languageCode)&lt;2">Error: R1.11.10 The document Language Code is required. The attribute'code' of the element "languageCode" shall not be empty. The element "languageCode"
      cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:languageCode and cda:languageCode/@code and count(cda:languageCode)&lt;2">Success: R1.11.10 The document Language Code is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:legalAuthenticator/cda:assignedEntity">
      <!-- R1.11.9 Legal Authenticator Family Name/Surname -->
      <assert test="(cda:assignedPerson/cda:name/cda:family and (every $i in cda:assignedPerson/cda:name/cda:family satisfies string-length(normalize-space($i)) &gt; 0)) or cda:assignedPerson/cda:name/@nullFlavor or cda:assignedPerson/@nullFlavor">Error:
      R1.11.9 Legal Authenticator Family Name/Surname is required (like R1.10.1). The element "family" shall not be empty.</assert>
         <report test="(cda:assignedPerson/cda:name/cda:family and (every $i in cda:assignedPerson/cda:name/cda:family satisfies string-length(normalize-space($i)) &gt; 0)) or cda:assignedPerson/cda:name/@nullFlavor or cda:assignedPerson/@nullFlavor">
      Success: R1.11.9 Legal Authenticator Family Name/Surname is fulfilled.</report>
         <!-- R1.11.9 Legal Authenticator Given Name-->
      <assert test="(cda:assignedPerson/cda:name/cda:given and (every $i in cda:assignedPerson/cda:name/cda:given satisfies string-length(normalize-space($i)) &gt; 0)) or cda:assignedPerson/cda:name/@nullFlavor or cda:assignedPerson/@nullFlavor">Error:
      R1.11.9 Legal Authenticator Given Name is required (like R1.10.2). The element "given" shall not be empty.</assert>
         <report test="(cda:assignedPerson/cda:name/cda:given and (every $i in cda:assignedPerson/cda:name/cda:given satisfies string-length(normalize-space($i)) &gt; 0)) or cda:assignedPerson/cda:name/@nullFlavor or cda:assignedPerson/@nullFlavor">Success:
      R1.11.9 Legal Authenticator Given Name is fulfilled (like R1.10.2).</report>
         <!-- R1.11.9 Legal Authenticator ID number -->
      <assert test="(cda:id) or @nullFlavor">Error: R1.11.9 Legal Authenticator ID number is required (like R1.10.5).</assert>
         <report test="(cda:id) or @nullFlavor">Success: R1.11.9 Legal Authenticator ID number is fulfilled.</report>
         <assert test="(cda:code and cda:code/@code and cda:code/@codeSystem) or @nullFlavor or not(cda:code)">Error: R1.11.9 Legal Authenticator Code is optional (like R1.10.7). If it's present, the attributes 'code' and 'codeSystem' of the element "code"
      shall not be empty.</assert>
         <report test="(cda:code and cda:code/@code and cda:code/@codeSystem) or @nullFlavor or not(cda:code)">Success: R1.11.9 Legal Authenticator Code is fulfilled.</report>
         <!-- R1.11.10 Legal Authenticator Facility -->
      <assert test="cda:representedOrganization">Error: R1.11.10 representedOrganization is required (like R1.10.9).</assert>
         <report test="cda:representedOrganization">Success: R1.11.10 representedOrganization is present.</report>
         <!-- R1.11.10 Legal Authenticator Facility name -->
      <assert test="(cda:representedOrganization/cda:name and (every $i in cda:representedOrganization/cda:name satisfies count(($i))&lt; 2) and ((every $i in cda:representedOrganization/cda:name satisfies string-length(normalize-space($i)) &gt; 0) or cda:representedOrganization/cda:name/@nullFlavor )) or cda:representedOrganization/@nullFlavor">
      Error: R1.11.10 Legal Authenticator Facility name is required (like R1.10.9.1). The element (name) shall not be empty otherwise the nullFlavor attribute shall be present.</assert>
         <report test="(cda:representedOrganization/cda:name and (every $i in cda:representedOrganization/cda:name satisfies count(($i))&lt; 2) and ((every $i in cda:representedOrganization/cda:name satisfies string-length(normalize-space($i)) &gt; 0) or cda:representedOrganization/cda:name/@nullFlavor )) or cda:representedOrganization/@nullFlavor">
      Success: R1.11.10 Legal Authenticator Facility name is fulfilled.</report>
         <!-- R1.11.10 Legal Authenticator Facility ID -->
      <assert test="(cda:representedOrganization/cda:id and (every $i in cda:representedOrganization/cda:id satisfies count(($i))&lt; 2) and (cda:representedOrganization/cda:id/@root or cda:representedOrganization/cda:id/@nullFlavor)) or cda:representedOrganization/@nullFlavor">
      Error: R1.11.10 Legal Authenticator Facility ID is required (like R1.10.9.2). The attribute 'root' of the element (id) shall not be empty otherwise the nullFlavor attribute shall be present.</assert>
         <report test="(cda:representedOrganization/cda:id and (every $i in cda:representedOrganization/cda:id satisfies count(($i))&lt; 2) and (cda:representedOrganization/cda:id/@root or cda:representedOrganization/cda:id/@nullFlavor)) or cda:representedOrganization/@nullFlavor">
      Success: R1.11.10 Legal Authenticator Facility ID is fulfilled (like R1.10.9.2).</report>
         <!-- R1.11.10 Legal Authenticator Facility address -->
      <assert test="cda:representedOrganization/cda:addr">Error: R1.11.10 Legal Authenticator address is required (like R1.10.9.3).</assert>
         <report test="cda:representedOrganization/cda:addr">Success: R1.11.10 Legal Authenticator address is present (like R1.10.9.3).</report>
      </rule>
      <!-- R1.11.9 Legal Authenticator Telecommunication-->
    <rule context="/cda:ClinicalDocument/cda:legalAuthenticator/cda:assignedEntity/cda:telecom">
         <assert test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Error: R1.11.9 Legal Authenticator Telecom telephone/mail address is required. If there is no information, the nullFlavor attribute shall have a value of 'NI' and the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute
      shall not be present, and the "value" and "use" attributes shall be present and shall not be empty. The Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient Contact's telephone number shall be like :
      "tel:+13176307960" or "tel:+1(317)630-7960. The attribute 'use' shall not be empty.</assert>
         <report test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Success: R1.11.9 Legal Authenticator Telecom telephone/mail address is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient">
      <!-- R1.1 Patient Name-->
      <assert test="cda:name">Error: R1.1 Patient Name is required.</assert>
         <report test="cda:name">Success: R1.1 Patient Name is present.</report>
         <!-- R1.2 Gender-->
      <assert test="cda:administrativeGenderCode and count(cda:administrativeGenderCode)&lt;2">Error: R1.2 administrativeGenderCode is required. The element "administrativeGenderCode" cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:administrativeGenderCode and count(cda:administrativeGenderCode)&lt;2">Success: R1.2 administrativeGenderCode is present and have a cadinality[1..1].</report>
         <assert test="(cda:administrativeGenderCode[@codeSystem = &#34;2.16.840.1.113883.5.1&#34;] and not(@nullFlavor)) or cda:administrativeGenderCode[@nullFlavor='UNK']">Error: R1.2 The Gender attribute '@codeSystem' shall be 2.16.840.1.113883.5.1
      currently set to 
      <value-of select="*/cda:administrativeGenderCode/@codeSystem"/>otherwise the nullFlavor attribute shall be set to 'UNK'.</assert>
         <report test="(cda:administrativeGenderCode[@codeSystem = &#34;2.16.840.1.113883.5.1&#34;] and not(@nullFlavor)) or cda:administrativeGenderCode[@nullFlavor='UNK']">Success: R1.2 Gender attribute 'codeSystem' is fulfilled.</report>
         <!-- R1.3 Date of Birth-->
      <assert test="cda:birthTime and count(cda:birthTime)&lt;2">Error: R1.3 Date of Birth is required. The element "birthTime" cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:birthTime and count(cda:birthTime)&lt;2">Success: R1.3 Date of Birth is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:name">
      <!-- R1.1.1 Family name/Surname-->
      <assert test="(cda:family and (every $i in cda:family satisfies string-length(normalize-space($i)) &gt; 0)) or @nullFlavor">Error: R1.1.1 Patient Family name/Surname is required. The element (family) shall not be empty.</assert>
         <report test="(cda:family and (every $i in cda:family satisfies string-length(normalize-space($i)) &gt; 0)) or @nullFlavor">Success: R1.1.1 Patient Family name/Surname is fulfilled.</report>
         <!-- R1.1.3 Patient Given Name-->
      <assert test="(cda:given and (every $i in cda:given satisfies string-length(normalize-space($i)) &gt; 0)) or @nullFlavor">Error: R1.1.3 Patient Given Name is required. The element (given) shall not be empty.</assert>
         <report test="(cda:given and (every $i in cda:given satisfies string-length(normalize-space($i)) &gt; 0)) or @nullFlavor">Success: R1.1.3 Patient Given Name is fulfilled.</report>
         <!-- R1.1.2 Patient Prefix-->
      <assert test="(cda:prefix and (every $i in cda:prefix satisfies string-length(normalize-space($i)) &gt; 0)) or not(cda:prefix)">Error: R1.1.2 Patient Prefix is optional, if the element "prefix" is present, it shall not be empty.</assert>
         <report test="(cda:prefix and (every $i in cda:prefix satisfies string-length(normalize-space($i)) &gt; 0)) or not(cda:prefix)">Success: R1.1.2 Patient Prefix is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:birthTime">
      <!-- R1.3 Date of Birth-->
      <assert test="(matches(@value,'\d{4}') or matches(@value,'\d{6}') or matches(@value,'\d{8}'))">Error: R1.3 Date of Birth shall contain 4,6 or 8 digits. DOB may be a partial date such as only the year. Expecting a string such as
      YYYY[MM[DD]]</assert>
         <report test="(matches(@value,'\d{4}') or matches(@value,'\d{6}') or matches(@value,'\d{8}'))">Success: R1.3 Date of Birth Patient Date of Birth is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole">
      <!-- R1.4 Primary Patient Identifier-->
      <assert test="cda:id and cda:id/@root">Error: R1.4.1 Primary Patient Identifier (Regional/National Health Id) is required. The attribute "root" of the element (id) shall not be empty.</assert>
         <report test="cda:id and cda:id/@root">Success: R1.4.1 Primary Patient Identifier (Regional/National Health Id) is fulfilled.</report>
         <!-- R1.5 Patient's Address-->
      <assert test="cda:addr">Error: R1.5 Patient's Address element is required.</assert>
         <!-- R1.6 Patient's Telecommunication-->
      <assert test="cda:telecom">Error: R1.6 The patient telephone or e-mail element is required.</assert>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:addr">
      <!-- R1.5 Patient's Address-->
      <assert test="(((cda:streetAddressLine) or (cda:city) or (cda:state) or (cda:country)) and not(@nullFlavor)) or ( (@nullFlavor ='NI') and (not(cda:streetAddressLine) and not(cda:city) and not(cda:state) and not(cda:country)))">Error: R1.5 Patient's
      Address element is required. If there is no information, the nullFlavor attribute shall have a value of 'NI' and no address parts shall be present, otherwise there shall be no nullFlavor attribute, and at least one of the address parts listed below
      shall be present</assert>
         <!-- R1.5.1/R1.5.2 Patient's Street/Patient's Number of Street-->
      <assert test="@nullFlavor or (not(cda:streetAddressLine)) or ((cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) or cda:streetAddressLine/@nullFlavor)">Error: R1.5.1 Patient's Street
      Address Line is optional. If present the element "streetAddressLine" shall not be empty, otherwise the nullFlavor attribute shall be present .</assert>
         <report test="((cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) or cda:streetAddressLine/@nullFlavor)">Success: R1.5.1 Patient's Street Address Line is fulfilled.</report>
         <!-- R1.5.3 Patient's City-->
      <assert test="@nullFlavor or (not(cda:city)) or ((cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) or cda:city/@nullFlavor)">Error: R1.5.3 Patient's City is optional. If present the element "city" shall not
      be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="((cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) or cda:city/@nullFlavor)">Success: R1.5.3 Patient's City is fulfilled.</report>
         <!-- R1.5.4 Patient's Postal Code-->
      <assert test="@nullFlavor or (not(cda:postalCode)) or ((cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) or cda:postalCode/@nullFlavor)">Error: R1.5.4 Patient's Postal Code is optional. If present
      the element "postalCode" shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="((cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) or cda:postalCode/@nullFlavor)">Success: R1.5.4 Patient's Postal Code is fulfilled.</report>
         <!-- R1.5.5 Patient's State or Province-->
      <assert test="@nullFlavor or (not(cda:state)) or ((cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0)) or cda:state/@nullFlavor)">Error: R1.5.5 Patient's State or Province is optional. If present the element
      "state" shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="((cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0)) or cda:state/@nullFlavor)">Success: R1.5.5 Patient's State or Province is fulfilled.</report>
         <!-- R1.5.6 Patient's Country-->
      <assert test="@nullFlavor or (not(cda:country)) or ((cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) or cda:country/@nullFlavor)">Error: R1.5.6 Patient's Country is optional. If present the element
      "country" shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="((cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) or cda:country/@nullFlavor)">Success: R1.5.6 Patient's Country is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:telecom">
      <!-- R1.6 Patient's Telecommunication-->
      <assert test=" ((@nullFlavor='NI') and not(@value) and not(@use)) or (not(@nullFlavor) and (@value and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)))">Error: R1.6 The patient telephone or e-mail element is required. If
      there is no information, the nullFlavor attribute shall have a value of 'NI' and the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute shall not be present, and the "value" and "use" attributes shall be
      present</assert>
         <!-- R1.6 Patient's Telecommunication-->
      <assert test="((@nullFlavor='NI') and not(@value) and not(@use)) or (not(@nullFlavor) and (matches (@value,'tel:\+[0-9/(/)/-]'))) or (not(@nullFlavor) and (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$')))">
      Error: R1.6 The Patient's telephone number(R1.6.1) or the Patient's e-mail address (R1.6.2) are miswritten : the Patient's e-mail address shall be like : mailto:example@exp.com and the Patient's telephone number shall be like : "tel:+13176307960"
      or "tel:+1(317)630-7960"</assert>
         <report test="((@nullFlavor='NI') and not(@value) and not(@use)) or (not(@nullFlavor) and (matches (@value,'tel:\+[0-9/(/)/-]'))) or (not(@nullFlavor) and (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$')))">
      Success: R1.6 The Patient's telephone number(R1.6.1) or the Patient's e-mail address (R1.6.2) are fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Language Communication - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.2.1&#34;]">
      <!-- Verify that the template id is used on the appropriate type of object
   -->
      <assert test="../cda:languageCommunication">Error: In IHE PCC Language Communication (1.3.6.1.4.1.19376.1.5.3.1.2.1), the IHE PCC LanguageCommunication shall describe this information using the languageCommunication element.</assert>
         <!-- the Patient's preferred language is optional (cf.epSOS_D3.9.1_Appendix_B1_Implementation_v1.2_20101220.doc:  Common Header Data Elements R1.7) -->
      <report test="cda:languageCode">Success:R1.7 The Patient's preferred language element "languageCode" is present.</report>
         <assert test="not(cda:modeCode) or cda:modeCode[@codeSystem=&#34;2.16.840.1.113883.5.60&#34;]">Error: In IHE PCC Language Communication (1.3.6.1.4.1.19376.1.5.3.1.2.1), the modeCode element describes the mode of use, and is only necessary when
      there are differences between expressive and receptive abilities. This element is optional. When not present, the assumption is that any further detail provided within the languageCommunication element refers to all common modes of communication.
      The coding system used shall be the HL7 LanguageAbilityMode vocabulary when this element is communicated.</assert>
         <assert test="not(cda:proficiencyLevelCode) or cda:proficiencyLevelCode[@codeSystem=&#34;2.16.840.1.113883.5.61&#34;]">Error: In IHE PCC Language Communication (1.3.6.1.4.1.19376.1.5.3.1.2.1), the proficiencyLevelCode element describes the
      proficiency of the patient (with respect to the mode if specified). This element is optional. The coding system used shall be the HL7 LanguageProficiencyCode vocabulary when this element is communicated.</assert>
         <assert test="not(cda:preferenceInd) or cda:preferenceInd[@value=&#34;true&#34;] or cda:preferenceInd[@value=&#34;false&#34;]">Error: In IHE PCC Language Communication (1.3.6.1.4.1.19376.1.5.3.1.2.1), the preferenceInd shall be valued "true" if
      this language is the patient's preferred language for communication, or "false" if this is not the patient's preferred language.</assert>
      </rule>
      <!--epSOS-->
    <!--(cf.epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc : 8  Common Header Data Elements)-->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.2.1']/cda:languageCode">
         <assert test="(@code and (matches(@code,'^[a-z][a-z]\-[A-Z][A-Z]$')) and (not(@nullFlavor))) or @nullFlavor">Error: R1.7 The Patient's preferred language code element "languageCode\@code" SHALL be in the form nn-CC.The nn portion SHALL be an
      ISO-639-1 language code in lower case derived by the Value Set epSOSLanguage 1.3.6.1.4.1.12559.11.10.1.3.1.42.6. The CC portion, if present, SHALL be an ISO-3166 country code in upper case derived by the value Set epSOSCountry
      1.3.6.1.4.1.12559.11.10.1.3.1.42.4. Otherwise the nullFlavor attribute shall be present.</assert>
         <report test="(@code and (matches(@code,'^[a-z][a-z]\-[A-Z][A-Z]$')))">Success:R1.7 The Patient's preferred language code element "languageCode\@code" is fullfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Patient Contacts - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.2.4&#34;]">
         <assert test="not(../cda:participant) or ../cda:participant[@typeCode=&#34;IND&#34;]">Error: In IHE PCC Patient Contacts (1.3.6.1.4.1.19376.1.5.3.1.2.4), contacts that are recorded as participant elements shall have the classCode attribute shall
      set to 'IND'.</assert>
         <assert test="not(../cda:participant) or cda:associatedEntity[@classCode = &#34;AGNT&#34; or @classCode = &#34;CAREGIVER&#34; or @classCode = &#34;ECON&#34; or @classCode = &#34;NOK&#34; or @classCode = &#34;PRS&#34;]">Error: In IHE PCC
      Patient Contacts (1.3.6.1.4.1.19376.1.5.3.1.2.4), the associatedEntity element identifies the type of contact. The classCode attribute shall be present, and contains a value from the set AGNT, CAREGIVER, ECON, NOK, or PRS to identify contacts that
      are agents of the patient, care givers, emergency contacts, next of kin, or other relations respectively.</assert>
         <assert test="not(../cda:participant) or not(cda:associatedEntity/cda:code) or cda:associatedEntity/cda:code[@code and @codeSystem=&#34;2.16.840.1.113883.5.111&#34;]">Error: In IHE PCC Patient Contacts (1.3.6.1.4.1.19376.1.5.3.1.2.4), in the
      associatedEntity code, the code attribute is required and comes from the HL7 PersonalRelationshipRoleType vocabulary. The codeSystem attribute is required and shall be represented exactly as shown.</assert>
      </rule>
      <!--epSOS-->
    <!--(cf.epSOS_D3.9.1_Appendix_B1_Implementation_v1.2_20101220.doc : 8  Common Header Data Elements)-->
    <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient">
      <!-- R1.7.A Patient's Guardian-->
      <report test="cda:guardian">Success: R1.7.A The Patient's Guardian element "guardian" is present.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:guardian">
      <!-- R1.7.A Patient's Guardian-->
      <!--People reported problems with the guardian element :it seems that when nullflavored the validator expects that the guardian doesn't have any other subitem, this is not coherent with the CDA standard schema that instead requires either the guardianPerson or the guardianOrganization to be present (even if nullFlavored)-->
      <!--            <assert test="(@nullFlavor and not(cda:addr) and not(cda:telecom) and not(cda:guardianPerson) and not(cda:guardianPerson)) or (not(@nullFlavor))">
                Error: R1.7.A If the Patient's Guardian is not applicable, or not known, the appropriate nullFlavor shall be used and no sub elements (name, adress...)are expected to be used.   
            </assert>-->
      <assert test="(@nullFlavor and not(@classCode) ) or (@classCode = 'GUARD' and not(@nullFlavor))">Error: R1.7.A The Patient's Guardian is required.If there is no information, the nullFlavor attribute shall be present and the classCode attribute
      shall not be present, otherwise there shall be no nullFlavor attribute and The classCode attribute shall contains the value 'GUARD'.</assert>
         <!-- R1.7.A.3 Guardian's Address-->
      <assert test="cda:addr or @nullFlavor">Error: R1.7.A.3 The Guardian's Address element is required.</assert>
         <report test="cda:addr">Success: R1.7.A.3 The Guardian's Address element is present.</report>
         <!-- R1.7.A.4 Guardian's Telecommunication-->
      <assert test="cda:telecom or @nullFlavor">Error: R1.7.A.4 The Guardian telephone or e-mail element is required.</assert>
         <report test="cda:telecom">Success: R1.7.A.3 The Guardian telephone or e-mail element is present.</report>
         <!-- R1.7.A.1 Guardian's-->
      <assert test="cda:guardianPerson or @nullFlavor">Error: R1.7.A.1 The "guardianPerson" element is required in 'guardian\' otherwise the nullFlavor attribute shall be present.</assert>
         <!-- R1.7.A.1 Guardian's Name-->
      <assert test="cda:guardianPerson/cda:name or @nullFlavor or cda:guardianPerson/@nullFlavor">Error: R1.7.A.1 The guardian's "name" element is required in 'guardian\guardianPerson\' otherwise the nullFlavor attribute shall be present.</assert>
         <report test="cda:guardianPerson/cda:name">Success: R1.7.A.1 The guardian's "name" element is present.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:guardianPerson/cda:name">
      <!-- R1.7.A.1 Guardian's Family Name/Surname -->
      <assert test="(cda:family and (every $i in cda:family satisfies string-length(normalize-space($i)) &gt; 0) and not(cda:family[@nullFlavor])) or (cda:family/@nullFlavor) or @nullFlavor">Error: R1.7.A.1 Guardian's Family Name/Surname is required.The
      element (family) shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <!-- R1.7.A.2 Guardian's Given Name -->
      <assert test="(cda:given and (every $i in cda:given satisfies string-length(normalize-space($i)) &gt; 0) and not(cda:given[@nullFlavor])) or (cda:given/@nullFlavor) or @nullFlavor">Error: R1.7.A.2 Guardian's Given Name is required.The element
      (given) shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:addr">
      <!-- R1.7.A.3 Guardian's Address-->
      <assert test="(((cda:streetAddressLine) or (cda:city) or (cda:state) or (cda:country)) and not(@nullFlavor)) or ((@nullFlavor='NI') and (not(cda:streetAddressLine) and not(cda:city) and not(cda:state) and not(cda:country)))">Error: R1.7.A.3
      Guardian's Address element is optional. If there is no information, the nullFlavor attribute shall be set to 'IN' and no address parts shall be present, otherwise there shall be no nullFlavor attribute, and at least one of the address parts listed
      below shall be present.</assert>
         <!-- R1.7.A.3.1/R1.7.A.3.2 Guardian's Street/Guardian's Number of Street-->
      <assert test="@nullFlavor or (not(cda:streetAddressLine)) or ((cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) or cda:streetAddressLine/@nullFlavor)">Error: R1.7.A.3.1 Guardian's
      Street is optional. If present the element (streetAddressLine) shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="((cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) or cda:streetAddressLine/@nullFlavor)">Success: R1.7.A.3.1 Guardian's Street Address Line is fulfilled.</report>
         <!-- R1.7.A.3.2.3 Guardian's City-->
      <assert test="@nullFlavor or (not(cda:city)) or ((cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) or cda:city/@nullFlavor)">Error: R1.7.A.3.2.3 Guardian's City is optional. If present the element (city)
      shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="((cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) or cda:city/@nullFlavor)">Success: R1.7.A.3.2.3 Guardian's City is fulfilled.</report>
         <!-- R1.7.A.3.2.4 Guardian's Postal Code-->
      <assert test="@nullFlavor or (not(cda:postalCode)) or ((cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) or cda:postalCode/@nullFlavor)">Error: R1.7.A.3.2.4 Guardian's Postal Code is optional. If
      present the element (postalCode) shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="((cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) or cda:postalCode/@nullFlavor)">Success: R1.7.A.3.2.4 Guardian's Postal Code is fulfilled.</report>
         <!-- R1.7.A.3.2.5 Guardian's State or Province-->
      <assert test="@nullFlavor or (not(cda:state)) or ((cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0)) or cda:state/@nullFlavor)">Error: R1.7.A.3.2.5 Guardian's State or Province is optional. If present the
      element (state) shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="((cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0)) or cda:state/@nullFlavor)">Success: R1.7.A.3.2.5 Guardian's State or Province is fulfilled.</report>
         <!-- R1.7.A.3.2.6 Guardian's Country-->
      <assert test="@nullFlavor or (not(cda:country)) or ((cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) or cda:country/@nullFlavor)">Error: R1.7.A.3.2.6 Guardian's Country is optional. If present the
      element (country) shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="((cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) or cda:country/@nullFlavor)">Success: R1.7.A.3.2.6 Guardian's Country is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:telecom">
      <!-- R1.7.A.4 Guardian's Telecommunication-->
      <assert test=" ((@nullFlavor='NI') and not(@value) and not(@use)) or (not(@nullFlavor) and (@value and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)))">Error: R1.7.A.4 The Guardian telephone or e-mail element is required.
      If there is no information, the nullFlavor attribute shall have a value of 'NI' and the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute shall not be present, and the "value" and "use" attributes shall be
      present.</assert>
         <!-- R1.7.A.4 Patient's Telecommunication-->
      <assert test="((@nullFlavor='NI') and not(@value) and not(@use)) or (not(@nullFlavor) and (matches (@value,'tel:\+[0-9/(/)/-]'))) or (not(@nullFlavor) and (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$')))">
      Error: R1.7.A.4 The Guardian's telephone number(R1.7.A.4.1) or the Guardian's e-mail address (R1.7.A.4.2) are miswritten : the Guardian's e-mail address shall be like :mailto:example@exp.com and the Guardian's telephone number shall be like :
      "tel:+13176307960" or "tel:+1(317)630-7960"</assert>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Healthcare Providers and Pharmacies - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.2.3&#34;]">
         <assert test="ancestor::cda:documentationOf/cda:serviceEvent[@classCode = &#34;PCPR&#34;]">Error: In IHE PCC Healthcare Providers and Pharmacies (1.3.6.1.4.1.19376.1.5.3.1.2.3), the documentationOf element shall be present. The serviceEvent
      element shall be present, and shall have a classCode attribute of 'PCPR'.</assert>
         <assert test="preceding-sibling::cda:effectiveTime/cda:low and preceding-sibling::cda:effectiveTime/cda:high">Error: In IHE PCC Healthcare Providers and Pharmacies (1.3.6.1.4.1.19376.1.5.3.1.2.3), in effectiveTime there shall be a low element which
      records the starting date of care provision, and a high element which records the ending date of care provision.</assert>
         <assert test="cda:time/cda:low and cda:time/cda:high">Error: In IHE PCC Healthcare Providers and Pharmacies (1.3.6.1.4.1.19376.1.5.3.1.2.3), the time element is used to show the time period over which the provider gave care to the patient. The low
      and high elements must be present, and indicate the time over which care was (or is to be) provided.</assert>
         <assert test="cda:assignedEntity">Error: In IHE PCC Healthcare Providers and Pharmacies (1.3.6.1.4.1.19376.1.5.3.1.2.3), the assignedEntity element contains elements that identify the individual provider, and shall be present.</assert>
         <assert test="cda:assignedEntity/cda:assignedPerson/cda:name or cda:assignedEntity/cda:representedOrganization">Error: In IHE PCC Healthcare Providers and Pharmacies (1.3.6.1.4.1.19376.1.5.3.1.2.3), the providers name shall be present. If not
      present, then the representedOrganization shall be present.</assert>
      </rule>
      <!--epSOS-->
    <!--(cf.epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc : 8  Common Header Data Elements)-->
    <rule context="/cda:ClinicalDocument">
      <!-- R1.10 HCP Identification ("ClinicalDocument\documentationOf\serviceEvent\performer" is optional, see the following rule)-->
      <assert test="cda:author/cda:assignedAuthor">Error: R1.10 HCP Information are required. The element "ClinicalDocument\author\assignedAuthor" shall be present.</assert>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:author/cda:assignedAuthor">
      <!-- R1.10.1 HCP Family Name/Surname -->
      <assert test="(cda:assignedPerson/cda:name/cda:family and (every $i in cda:assignedPerson/cda:name/cda:family satisfies string-length(normalize-space($i)) &gt; 0)) or (cda:assignedPerson/cda:name/@nullFlavor) or (cda:assignedPerson/@nullFlavor) or (not(cda:assignedPerson ))">
      Error: R1.10.1 HCP Family Name/Surname is required. The element (family) shall not be empty. The element (family) shall be present in "ClinicalDocument\author\assignedAuthor\assignedPerson\name\".</assert>
         <report test="cda:assignedPerson/cda:name/cda:family">Success: R1.10.1 HCP Family Name/Surname is present.</report>
         <!-- R1.10.2 HCP Given Name -->
      <assert test="(cda:assignedPerson/cda:name/cda:given and (every $i in cda:assignedPerson/cda:name/cda:given satisfies string-length(normalize-space($i)) &gt; 0)) or (cda:assignedPerson/cda:name/@nullFlavor) or (cda:assignedPerson/@nullFlavor) or (not(cda:assignedPerson))">
      Error: R1.10.2 HCP Given Name is required. The element (given) shall not be empty. The element (given) shall be present in "ClinicalDocument\author\assignedAuthor\assignedPerson\name\".</assert>
         <report test="cda:assignedPerson/cda:name/cda:given">Success: R1.10.2 HCP Given Name is present.</report>
         <!-- R1.10.5 HCP ID number -->
      <assert test="(cda:id) or (@nullFlavor) or (not(cda:assignedPerson))">Error: R1.10.5 HCP ID number is required. The element (id) shall be present in "ClinicalDocument\author\assignedAuthor\" .</assert>
         <report test="cda:id">Success: R1.10.5 HCP ID number is present.</report>
         <!-- R1.10.7 Specialty (Health Care Professional's Specialty) -->
      <assert test="(cda:code and cda:code/@code and cda:code/@codeSystem ) or (@nullFlavor) or (not(cda:code ))">Error: R1.10.7 HCP Specialty is optional. If the element (code) is present, the attributes (code\@code) and (code\@codeSystem) shall not be
      empty.</assert>
         <report test="cda:code">Success: R1.10.7 HCP Specialty is present.</report>
         <!-- R1.10.8 HCP Telecom  -->
      <assert test="(cda:telecom) or (@nullFlavor) or (not(cda:telecom))">Error: R1.10.8 HCP Telecom is optional. If the element (telecom) is present, the attributes (telecom\@value) and (telecom\@use) shall not be empty and so the Patient Contact's
      e-mail address shall be like : mailto:example@exp.com and the Patient Contact's telephone number shall be like : "tel:+13176307960" or "tel:+1(317)630-7960.</assert>
         <report test="cda:telecom">Success: R1.10.8 HCP Telecom is present.</report>
         <!-- R1.10.9.3.5 Healthcare Facility's Country -->
      <assert test="(cda:representedOrganization/cda:addr/cda:country and ((every $i in cda:representedOrganization/cda:addr/cda:country satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:representedOrganization/cda:addr/cda:country satisfies count(($i))&lt; 2)) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:representedOrganization))">
      Error: R1.10.9.3.5 Healthcare Facility's Country is required. The element (country) shall not be empty and cannot be repeated (cardinality [1..1]).The element (country) shall be present in
      "ClinicalDocument\author\assignedAuthor\representedOrganization\addr".</assert>
         <report test="cda:representedOrganization/cda:addr/cda:country">Success: R1.10.9.3.5 Healthcare Facility's Country is present.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer">
      <!-- R1.10.1 HCP Family Name/Surname -->
      <assert test="(cda:assignedEntity/cda:assignedPerson/cda:name/cda:family and (every $i in cda:assignedEntity/cda:assignedPerson/cda:name/cda:family satisfies string-length(normalize-space($i)) &gt; 0)) or (cda:assignedEntity/cda:assignedPerson/cda:name/@nullFlavor) or (cda:assignedEntity/cda:assignedPerson/@nullFlavor) or (cda:assignedEntity/@nullFlavor) or (not(cda:assignedEntity/cda:assignedPerson))">
      Error: R1.10.1 HCP Family Name/Surname is required. The element (family) shall not be empty. The element (family) shall be present in "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\assignedPerson\name\".</assert>
         <report test="cda:assignedEntity/cda:assignedPerson/cda:name/cda:family">Success: R1.10.1 HCP Family Name/Surname is present.</report>
         <!-- R1.10.2 HCP Given Name -->
      <assert test="(cda:assignedEntity/cda:assignedPerson/cda:name/cda:given and (every $i in cda:assignedEntity/cda:assignedPerson/cda:name/cda:given satisfies string-length(normalize-space($i)) &gt; 0)) or (cda:assignedEntity/cda:assignedPerson/cda:name/@nullFlavor) or (cda:assignedEntity/cda:assignedPerson/@nullFlavor) or (cda:assignedEntity/@nullFlavor) or (not(cda:assignedEntity/cda:assignedPerson))">
      Error: R1.10.2 HCP Given Name is required. The element (given) shall not be empty. The element (given) shall be present in "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\assignedPerson\name\".</assert>
         <report test="cda:assignedEntity/cda:assignedPerson/cda:name/cda:given">Success: R1.10.2 HCP Given Name is present.</report>
         <!-- R1.10.5 HCP ID number -->
      <assert test="(cda:assignedEntity/cda:id ) or (cda:assignedEntity/@nullFlavor) or (not(cda:assignedEntity/cda:assignedPerson))">Error: R1.10.5 HCP ID number is required. The element (id) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\".</assert>
         <report test="cda:assignedEntity/cda:id">Success: R1.10.5 HCP ID number is present.</report>
         <!-- R1.10.7 Specialty (Health Care Professional's Specialty) -->
      <assert test="(cda:assignedEntity/cda:code and cda:assignedEntity/cda:code/@code and cda:assignedEntity/cda:code/@codeSystem) or (cda:assignedEntity/@nullFlavor) or (not(cda:assignedEntity/cda:code))">Error: R1.10.7 HCP Specialty is optional. If
      the element (code) is present, the attributes (code\@code) and (code\@codeSystem) shall not be empty.</assert>
         <report test="cda:assignedEntity/cda:code">Success: R1.10.7 HCP Specialty is present.</report>
         <!-- R1.10.8 HCP Telecom  -->
      <assert test="(cda:assignedEntity/cda:telecom) or (cda:assignedEntity/@nullFlavor) or (not(cda:assignedEntity/cda:telecom))">Error: R1.10.8 HCP Telecom is optional. If the element (telecom) is present, the attributes (telecom\@value) and
      (telecom\@use) shall not be empty and so the Patient Contact's e-mail address shall be like : mailto:example@exp.com and the Patient Contact's telephone number shall be like : "tel:+13176307960" or "tel:+1(317)630-7960.</assert>
         <report test="cda:assignedEntity/cda:telecom">Success: R1.10.8 HCP Telecom is present.</report>
         <!-- R1.10.9.3.5 Healthcare Facility's Country -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:country and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:country satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:country satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">
      Error: R1.10.9.3.5 Healthcare Facility's Country is required. The element (country) shall not be empty and cannot be repeated (cardinality [1..1]).The element (country) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization\addr".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:addr/cda:country">Success: R1.10.9.3.5 Healthcare Facility's Country is present.</report>
      </rule>
      <!-- R1.10.8 HCP Telecom  -->
    <rule context="/cda:ClinicalDocument/cda:author/cda:assignedAuthor/cda:telecom">
         <assert test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Error: R1.10.8 HCP Telecom telephone/mail address is required. If there is no information, the nullFlavor attribute shall have a value of 'NI' and the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute shall not be
      present, and the "value" and "use" attributes shall be present and shall not be empty. The Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient Contact's telephone number shall be like : "tel:+13176307960" or
      "tel:+1(317)630-7960. The attribute 'use' shall not be empty.</assert>
      </rule>
      <!-- R1.10.8 HCP Telecom  -->
    <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:telecom">
         <assert test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Error: R1.10.8 HCP Telecom telephone/mail address is required. the nullFlavor attribute shall have a value of 'NI' and the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute shall not be present, and the "value" and
      "use" attributes shall be present and shall not be empty. The Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient Contact's telephone number shall be like : "tel:+13176307960" or "tel:+1(317)630-7960. The
      attribute 'use' shall not be empty.</assert>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.4.eP-errors"
            xml:base="templates/ePtemplates/errors/1.3.6.1.4.1.19376.1.5.3.1.2.4.eP.sch">
      <title>IHE PCC Patient Contacts (ePrescription)- errors validation phase</title>
      <!-- R1.8 Contact Person-->
    <rule context="/cda:ClinicalDocument">
         <assert test="not(cda:participant/cda:associatedEntity/cda:associatedPerson)">Error: R1.8 The Contact Person SHALL not be provided.</assert>
         <report test="not(cda:participant/cda:associatedEntity/cda:associatedPerson)">Success: R1.8 The Contact Person is not provided</report>
      </rule>
      <!-- R1.9 Prefered HCP -->
    <rule context="/cda:ClinicalDocument">
         <assert test="not(cda:participant/cda:associatedEntity/cda:scopingOrganization)">Error: R1.9 Legal Organization SHALL not be provided.</assert>
         <report test="not(cda:participant/cda:associatedEntity/cda:scopingOrganization)">Success: R1.9 The Legal Organization is not provided</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eP-errors"
            xml:base="templates/ePtemplates/errors/1.3.6.1.4.1.19376.1.5.3.1.1.1.eP.sch">
      <title>IHE PCC Medical Documents Specification - errors validation phase</title>
      <!--epSOS-->
    <!--(cf.epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc : 8  Common Header Data Elements)-->
    <!-- R1.11.2 Date of last update-->
    <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:effectiveTime">
         <assert test="(cda:high and cda:high/@value and count(cda:high)&lt;2) or not(cda:high) or @nullFlavor">Error: R1.11.2 Date of last update is optional. If it's present, the attribute 'value' of the element "high" shall not be empty. The element
      "high" cannot be repeated (cardinality [1..1]).</assert>
         <report test="(cda:high and cda:high/@value and count(cda:high)&lt;2) or not(cda:high) or @nullFlavor">Success: R1.11.2 Date of last update is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:legalAuthenticator/cda:assignedEntity/cda:representedOrganization/cda:addr">
      <!-- R1.11.10 Legal Authenticator Facility Address-->
      <assert test="(((cda:streetAddressLine) or (cda:city) or (cda:state) or (cda:country)) and not(@nullFlavor)) or ( (@nullFlavor) and (not(cda:streetAddressLine) and not(cda:city) and not(cda:state) and not(cda:country)))">Error: R1.11.10 Legal
      Authenticator Facility Address element is required. If there is no information, the nullFlavor attribute shall be present and no address parts shall be present, otherwise there shall be no nullFlavor attribute, and at least one of the address parts
      listed below shall be present</assert>
         <!-- R1.11.10 Legal Authenticator Facility Street/Number of Street-->
      <assert test="(cda:streetAddressLine and ((cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) or cda:streetAddressLine/@nullFlavor) and count(cda:streetAddressLine)&lt;2 ) or @nullFlavor or (not(cda:streetAddressLine))">
      Error: R1.11.10 Legal Authenticator Facility Street/Number of Street is optional (cf.R1.10.9.3.1). If present the element (streetAddressLine) shall not be empty, otherwise the nullFlavor attribute shall be present .</assert>
         <report test="(cda:streetAddressLine and ((cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) or cda:streetAddressLine/@nullFlavor) and count(cda:streetAddressLine)&lt;2 ) or @nullFlavor">
      Success: R1.11.10 Legal Authenticator Facility Street/Number of Street is fulfilled (cf.R1.10.9.3.1).</report>
         <!-- R1.11.10 Legal Authenticator Facility City-->
      <assert test="(cda:city and count(cda:city)&lt;2 and ((cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) or cda:city/@nullFlavor)) or @nullFlavor or (not(cda:city))">Error: R1.11.10 Legal Authenticator
      Facility City is optional (cf.R1.10.9.3.2). If present the element (city) shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="(cda:city and count(cda:city)&lt;2 and ((cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) or cda:city/@nullFlavor)) or @nullFlavor">Success: R1.11.10 Legal Authenticator Facility City is
      fulfilled (cf.R1.10.9.3.2).</report>
         <!-- R1.11.10 Legal Authenticator Facility Postal Code-->
      <assert test="(cda:postalCode and count(cda:postalCode)&lt;2 and ((cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) or cda:postalCode/@nullFlavor)) or @nullFlavor or (not(cda:postalCode))">Error:
      R1.11.10 Legal Authenticator Facility Postal Code is optional (cf.R1.10.9.3.3). If present the element (postalCode) shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="(cda:postalCode and count(cda:postalCode)&lt;2 and ((cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) or cda:postalCode/@nullFlavor)) or @nullFlavor">Success: R1.11.10 Legal
      Authenticator Facility Postal Code is fulfilled (cf.R1.10.9.3.3) .</report>
         <!-- R1.11.10 Legal Authenticator Facility State or Province-->
      <assert test="(cda:state and count(cda:state)&lt;2 and ((cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0)) or cda:state/@nullFlavor)) or @nullFlavor or (not(cda:state))">Error: R1.11.10 Legal Authenticator
      Facility State or Province is optional (cf.R1.10.9.3.4). If present the element (state) shall not be empty, otherwise the nullFlavor attribute shall be present.</assert>
         <report test="(cda:state and count(cda:state)&lt;2 and ((cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0)) or cda:state/@nullFlavor)) or @nullFlavor">Success: R1.11.10 Legal Authenticator Facility State or
      Province is fulfilled (cf.R1.10.9.3.4).</report>
         <!-- R1.11.10 Legal Authenticator Facility Country-->
      <assert test="(cda:country and count(cda:country)&lt;2 and (cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Error: R1.11.10 Legal Authenticator Facility Country is required
      (cf.R1.10.9.3.5). The element (country) shall not be empty.</assert>
         <report test="(cda:country and count(cda:country)&lt;2 and (cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Success: R1.11.10 Legal Authenticator Facility Country is present
      (cf.R1.10.9.3.5).</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:legalAuthenticator/cda:assignedEntity/cda:representedOrganization/cda:telecom">
      <!-- R1.11.10 Legal Authenticator Facility Telecommunication-->
      <assert test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Error: R1.11.10 Legal Authenticator Facility telephone number(cf.R1.10.9.4.1) or the Legal Authenticator Facility e-mail address (cf.R1.10.9.4.2) are miswritten. If there is no information, the nullFlavor attribute shall have a value of 'NI' and
      the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute shall not be present, and the "value" and "use" attributes shall be present and shall not be empty. The e-mail address shall be like :mailto:example@exp.com and
      the telephone number shall be like : "tel:+13176307960" or "tel:+1(317)630-7960"</assert>
         <report test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Success: R1.11.10 Legal Authenticator Facility telephone number(cf.R1.10.9.4.1) or the Legal Authenticator Facility e-mail address (cf.R1.10.9.4.2) are fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eP-errors"
            xml:base="templates/ePtemplates/errors/1.3.6.1.4.1.19376.1.5.3.1.2.3.eP.sch">
    <!--epSOS-->
    <!--(cf.epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc : 8  Common Header Data Elements)-->
    <rule context="/cda:ClinicalDocument/cda:author/cda:assignedAuthor">
      <!-- R1.10.6 HCP Profession (Health Care Professional's Specialty) -->
      <assert test="(parent::node()/cda:functionCode/@code and parent::node()/cda:functionCode[@codeSystem='2.16.840.1.113883.2.9.6.2.7']) or (@nullFlavor) or (parent::node() and not (cda:assignedPerson))">Error: R1.10.6 HCP Profession is required. The
      element (functionCode) shall be present in "ClinicalDocument\author\". The attributes (functionCode\@code) and (functionCode\@codeSystem) shall not be empty and the @codeSystem shall be "2.16.840.1.113883.2.9.6.2.7".</assert>
         <report test="parent::node()/cda:functionCode/@code">Success: R1.10.6 HCP Profession is present.</report>
         <!-- R1.10.9.1 Healthcare Facility's name -->
      <assert test="(cda:representedOrganization/cda:name and ((every $i in cda:representedOrganization/cda:name satisfies string-length(normalize-space($i)) &gt; 0) or cda:representedOrganization/cda:name/@nullFlavor) and (every $i in cda:representedOrganization/cda:name satisfies count(($i))&lt; 2)) or (cda:representedOrganization/@nullFlavor) or (@nullFlavor) or (not(cda:representedOrganization))">
      Error: R1.10.9.1 Healthcare Facility's name is required. The element (name) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element (name) shall be present in
      "ClinicalDocument\author\assignedAuthor\representedOrganization".</assert>
         <report test="cda:representedOrganization/cda:name">Success: R1.10.9.1 Healthcare Facility's name is present.</report>
         <!-- R1.10.9.2 Healthcare Facility's identifier -->
      <assert test="(cda:representedOrganization/cda:id and (every $i in cda:representedOrganization/cda:id satisfies count(($i))&lt; 2)) or (cda:representedOrganization/@nullFlavor) or (@nullFlavor) or (not(cda:representedOrganization))">Error:
      R1.10.9.2 Healthcare Facility's identifier is required. The element (id) cannot be repeated (cardinality [1..1]). The element (id) shall be present in "ClinicalDocument\author\assignedAuthor\representedOrganization".</assert>
         <report test="cda:representedOrganization/cda:id">Success: R1.10.9.2 Healthcare Facility's identifier is present.</report>
         <!-- R1.10.9.3.1 Healthcare Facility's Street -->
      <assert test="(cda:representedOrganization/cda:addr/cda:streetAddressLine and ((every $i in cda:representedOrganization/cda:addr/cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0) or cda:representedOrganization/cda:addr/cda:streetAddressLine/@nullFlavor) and (every $i in cda:representedOrganization/cda:addr/cda:streetAddressLine satisfies count(($i))&lt; 2)) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:representedOrganization)) or (not(cda:representedOrganization/cda:addr/cda:streetAddressLine))">
      Error: R1.10.9.3.1 Healthcare Facility's Street is optional. If present the element (streetAddressLine) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element
      (streetAddressLine) shall be present in "ClinicalDocument\author\assignedAuthor\representedOrganization\addr".</assert>
         <report test="cda:representedOrganization/cda:addr/cda:streetAddressLine">Success: R1.10.9.3.1 Healthcare Facility's Street is present.</report>
         <!-- R1.10.9.3.2 Healthcare Facility's City -->
      <assert test="(cda:representedOrganization/cda:addr/cda:city and ((every $i in cda:representedOrganization/cda:addr/cda:city satisfies string-length(normalize-space($i)) &gt; 0) or cda:representedOrganization/cda:addr/cda:city/@nullFlavor) and (every $i in cda:representedOrganization/cda:addr/cda:city satisfies count(($i))&lt; 2)) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:representedOrganization)) or (not(cda:representedOrganization/cda:addr/cda:city))">
      Error: R1.10.9.3.2 Healthcare Facility's City is optional. If present the element (city) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element (city) shall be present in
      "ClinicalDocument\author\assignedAuthor\representedOrganization\addr".</assert>
         <report test="cda:representedOrganization/cda:addr/cda:city">Success: R1.10.9.3.2 Healthcare Facility's City is present.</report>
         <!-- R1.10.9.3.3 Healthcare Facility's State or Province -->
      <assert test="(cda:representedOrganization/cda:addr/cda:state and ((every $i in cda:representedOrganization/cda:addr/cda:state satisfies string-length(normalize-space($i)) &gt; 0) or cda:representedOrganization/cda:addr/cda:state/@nullFlavor) and (every $i in cda:representedOrganization/cda:addr/cda:state satisfies count(($i))&lt; 2)) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:representedOrganization)) or (not(cda:representedOrganization/cda:addr/cda:state))">
      Error: R1.10.9.3.3 Healthcare Facility's State or Province is optional. If present the element (state) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element (state) shall be
      present in "ClinicalDocument\author\assignedAuthor\representedOrganization\addr".</assert>
         <report test="cda:representedOrganization/cda:addr/cda:state">Success: R1.10.9.3.3 Healthcare Facility's State or Province is present.</report>
         <!-- R1.10.9.3.4 Healthcare Facility's Zip or Postal Code -->
      <assert test="(cda:representedOrganization/cda:addr/cda:postalCode and ((every $i in cda:representedOrganization/cda:addr/cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0) or cda:representedOrganization/cda:addr/cda:postalCode/@nullFlavor) and (every $i in cda:representedOrganization/cda:addr/cda:postalCode satisfies count(($i))&lt; 2)) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:representedOrganization)) or (not(cda:representedOrganization/cda:addr/cda:postalCode))">
      Error: R1.10.9.3.4 Healthcare Facility's Zip or Postal Code is optional. If present the element (postalCode) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element
      (postalCode) shall be present in "ClinicalDocument\author\assignedAuthor\representedOrganization\addr".</assert>
         <report test="cda:representedOrganization/cda:addr/cda:postalCode">Success: R1.10.9.3.4 Healthcare Facility's Zip or Postal Code is present.</report>
         <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test="(cda:representedOrganization/cda:telecom) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (not(cda:representedOrganization/cda:telecom))">Error: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is optional.
      If the element "telecom" exists, the attributes (telecom\@value) and (telecom\@use) shall not be empty and so the Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient Contact's telephone number shall be like :
      "tel:+13176307960" or "tel:+1(317)630-7960.</assert>
         <report test="cda:representedOrganization/cda:telecom">Success: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is present.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer">
      <!-- R1.10.6 HCP Profession (Health Care Professional's Specialty) -->
      <assert test="(cda:functionCode/@code and cda:functionCode[@codeSystem='2.16.840.1.113883.2.9.6.2.7']) or @nullFlavor or (not(cda:assignedEntity/cda:assignedPerson))">Error: R1.10.6 HCP Profession is required. The element (functionCode) shall be
      present in "\ClinicalDocument\documentationOf\serviceEvent\performer\". The attributes (functionCode\@code) and (functionCode\@codeSystem) shall not be empty and the @codeSystem shall be "2.16.840.1.113883.2.9.6.2.7".</assert>
         <report test="cda:functionCode/@code">Success: R1.10.6 HCP Profession is present.</report>
         <!-- R1.10.9.1 Healthcare Facility's name -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:name and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:name satisfies string-length(normalize-space($i)) &gt; 0) or cda:assignedEntity/cda:representedOrganization/cda:name/@nullFlavor) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:name satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">
      Error: R1.10.9.1 Healthcare Facility's name is required. The element (name) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element (name) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:name">Success: R1.10.9.1 Healthcare Facility's name is present.</report>
         <!-- R1.10.9.2 Healthcare Facility's identifier -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:id and (every $i in cda:assignedEntity/cda:representedOrganization/cda:id satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">
      Error: R1.10.9.2 Healthcare Facility's identifier is required. The element (id) cannot be repeated (cardinality [1..1]). The element (id) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:id">Success: R1.10.9.2 Healthcare Facility's identifier is present.</report>
         <!-- R1.10.9.3.1 Healthcare Facility's Street -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0) or cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine/@nullFlavor ) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization)) or (not(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine))">
      Error: R1.10.9.3.1 Healthcare Facility's Street is optional. If present the element (streetAddressLine) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element
      (streetAddressLine) shall be present in "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization\addr".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine">Success: R1.10.9.3.1 Healthcare Facility's Street is present.</report>
         <!-- R1.10.9.3.2 Healthcare Facility's City -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city satisfies string-length(normalize-space($i)) &gt; 0) or cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city/@nullFlavor ) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization)) or (not(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city))">
      Error: R1.10.9.3.2 Healthcare Facility's City is optional. If present the element (city) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element (city) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization\addr".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city">Success: R1.10.9.3.2 Healthcare Facility's City is present.</report>
         <!-- R1.10.9.3.3 Healthcare Facility's State or Province -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state satisfies string-length(normalize-space($i)) &gt; 0) or cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state/@nullFlavor ) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization)) or (not(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state))">
      Error: R1.10.9.3.3 Healthcare Facility's State or Province is optional. If present the element (state) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element (state) shall be
      present in "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization\addr".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state">Success: R1.10.9.3.2 Healthcare Facility's State or Province is present.</report>
         <!-- R1.10.9.3.4 Healthcare Facility's Zip or Postal Code -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0) or cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode/@nullFlavor) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization)) or (not(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode))">
      Error: R1.10.9.3.4 Healthcare Facility's Zip or Postal Code is optional. If present the element (postalCode) shall not be empty otherwise the nullFlavor attribute shall be present, and cannot be repeated (cardinality [1..1]). The element
      (postalCode) shall be present in "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization\addr".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode">Success: R1.10.9.3.2 Healthcare Facility's postalCode is present.</report>
         <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:telecom) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization/cda:telecom))">
      Error: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is optional. If the element "telecom" exists, the attributes (telecom\@value) and (telecom\@use) shall not be empty and so the Patient Contact's e-mail address shall be like
      :mailto:example@exp.com and the Patient Contact's telephone number shall be like : "tel:+13176307960" or "tel:+1(317)630-7960.</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:telecom">Success: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is present.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:author/cda:assignedAuthor/cda:representedOrganization/cda:telecom">
      <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Error: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is required. If there is no information, the nullFlavor attribute shall have a value of 'NI' and the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute
      shall not be present, and the "value" and "use" attributes shall be present and shall not be empty. The Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient Contact's telephone number shall be like :
      "tel:+13176307960" or "tel:+1(317)630-7960. The attribute 'use' shall not be empty.</assert>
         <report test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Success: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address are fulfilled (cf.R1.10.9.3.5).</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:representedOrganization/cda:telecom">
      <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Error: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is required. If there is no information, the nullFlavor attribute shall have a value of 'NI' and the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute
      shall not be present, and the "value" and "use" attributes shall be present and shall not be empty. The Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient Contact's telephone number shall be like :
      "tel:+13176307960" or "tel:+1(317)630-7960. The attribute 'use' shall not be empty.</assert>
         <report test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Success: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address are fulfilled (cf.R1.10.9.3.5).</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
    <!--  <title>epSOS Prescription Section Specification - errors validation phase</title> -->
    <rule context="//cda:section[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']">
      <!-- Verify the document type code -->
      <assert test="cda:code[@code = '57828-6'] and cda:code[@displayName = 'Prescriptions']">Error: (cf.Â§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The Prescription Section type code of a ePrescription Documents
      attributes "@code = 
      <value-of select="cda:code/@code"/>", or "@displayName = 
      <value-of select="cda:code/@displayName"/>" are not valid, the attribute '@code' must be set to the value "57828-6" which is the code corresponding to "Prescriptions" in the loinc code system. And '@displayName' must be set to the value
      "Prescriptions".</assert>
         <report test="cda:code[@code = '57828-6'] and cda:code[@displayName = 'Prescriptions']">Success: (cf.Â§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The Prescription Section type code of a ePrescription Documents is
      fulfilled.</report>
         <!-- Verify that the correct code system is used-->
      <assert test="cda:code[@codeSystem = '2.16.840.1.113883.6.1']">Error: (cf.Â§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The document type code of a Medical Documents attribute "@codeSystem = 
      <value-of select="cda:code/@codeSystem"/>", is not valid, it must be "2.16.840.1.113883.6.1" corresponding to the LOINC Code system OID</assert>
         <report test="cda:code[@codeSystem = '2.16.840.1.113883.6.1']">Success: (cf.Â§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The document type code of a Medical Documents is correctly set to "2.16.840.1.113883.6.1"
      corresponding to the LOINC Code system OID</report>
         <!-- Verify that the parents templateId are present. -->
      <!--Removed : update for the epSOS_Implementation_v0.0.6_20100830_translation_1 -->
      <!--<assert test="cda:templateId[@root=&quot;2.16.840.1.113883.10.20.1.8&quot;]"> Error: (cf.§12.1.2
    epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier
    (2.16.840.1.113883.10.20.1.8) for Prescription Section is not present. </assert>
   <report test="cda:templateId[@root=&quot;2.16.840.1.113883.10.20.1.8&quot;]"> Success: (cf.§12.1.2
    epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier
    (2.16.840.1.113883.10.20.1.8) for Prescription Section is present. </report>
   
   <assert test="cda:templateId[@root=&quot;1.3.6.1.4.1.19376.1.5.3.1.3.19&quot;]"> Error:
    (cf.§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier
    (1.3.6.1.4.1.19376.1.5.3.1.3.19) for Prescription Section shall be present. </assert>
   <report test="cda:templateId[@root=&quot;1.3.6.1.4.1.19376.1.5.3.1.3.19&quot;]"> Success:
    (cf.§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier
    (1.3.6.1.4.1.19376.1.5.3.1.3.19) for Prescription Section is present. </report>
   
   <assert test="cda:templateId[@root=&quot;1.3.6.1.4.1.12559.11.10.1.3.1.2.3&quot;]"> Error:
    (cf.§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier
    (1.3.6.1.4.1.12559.11.10.1.3.1.2.3) for Prescription Section shall be present. </assert>
   <report test="cda:templateId[@root=&quot;1.3.6.1.4.1.12559.11.10.1.3.1.2.3&quot;]"> Success:
    (cf.§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier
    (1.3.6.1.4.1.12559.11.10.1.3.1.2.3) for Prescription Section is present. </report>-->
      <!-- Verify that the Prescription Item Entry Content Module templateId is present. -->
      <assert test="cda:entry/cda:substanceAdministration/cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.3.2&#34;]">Error: (cf.Â§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The Prescription Item Entry Content
      Module identifier (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) for Prescription Section shall be present.</assert>
         <report test="cda:entry/cda:substanceAdministration/cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.3.2&#34;]">Success: (cf.Â§12.1.2 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The Prescription Item Entry Content
      Module identifier (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) for Prescription Section is present.</report>
         <!--R2.1 Prescription ID -->
      <assert test="cda:id and count(cda:id)&lt;2">Error: R2.1 The Prescription ID element is required. The element "id" cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:id">Success: R2.1 The Prescription ID element is present.</report>
         <report test="count(cda:id)&lt;2">Success: R2.1 The Prescription ID element cannot be repeated (cardinality [1..1]).</report>
         <!--R2.2 Prescriber -->
      <assert test="root()/node()/cda:author">Error: R2.2 The Prescription element 'author' is required in (ClinicalDocument/author).</assert>
         <report test="root()/node()/cda:author">Success: R2.2 The Prescription element 'author' is present in (ClinicalDocument/author)</report>
      </rule>
      <rule context="//cda:section[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/cda:author">
      <!--R2.2 Prescriber -->
      <assert test="count(current())&lt;2">Error: R2.2 The Prescriber element is optional. If element "author" is present, it cannot be repeated (cardinality [1..1]).</assert>
         <report test="count(cda:author)&lt;2">Success: R2.2 The Prescriber element is have cardinality [1..1].</report>
         <assert test="cda:assignedAuthor or @nullFlavor">Error: R2.2 The Prescriber "assignedAuthor" element is required. The element "assignedAuthor" shall be present in "author\".</assert>
         <assert test="cda:assignedAuthor/cda:assignedPerson or cda:assignedAuthor/@nullFlavor or not(cda:assignedAuthor)">Error: R2.2 The Prescriber "assignedPerson" element is required. The element "assignedPerson" shall be present in
      "author\assignedAuthor\".</assert>
         <assert test="cda:assignedAuthor/cda:assignedPerson/cda:name or cda:assignedAuthor/cda:assignedPerson/@nullFlavor or not(cda:assignedAuthor/cda:assignedPerson)">Error: R2.2 The Prescriber "name" element is required. The element "name" shall be
      present in "author\assignedAuthor\assignedPerson".</assert>
         <!-- R2.2 Prescriber Family Name/Surname -->
      <assert test="(cda:assignedAuthor/cda:assignedPerson/cda:name/cda:family and (every $i in cda:assignedAuthor/cda:assignedPerson/cda:name/cda:family satisfies string-length(normalize-space($i))&gt; 0)) or cda:assignedAuthor/cda:assignedPerson/cda:name/@nullFlavor or not(cda:assignedAuthor/cda:assignedPerson/cda:name)">
      Error: R2.2 The Prescriber Family Name/Surname element is required. The element (family) shall not be empty.</assert>
         <report test="cda:assignedAuthor/cda:assignedPerson/cda:name/cda:family">Success: R2.2 The Prescriber Family Name/Surname element is present.</report>
         <!-- R2.2 Prescriber Given Name -->
      <assert test="(cda:assignedAuthor/cda:assignedPerson/cda:name/cda:given and (every $i in cda:assignedAuthor/cda:assignedPerson/cda:name/cda:given satisfies string-length(normalize-space($i)) &gt; 0)) or cda:assignedAuthor/cda:assignedPerson/cda:name/@nullFlavor or not(cda:assignedAuthor/cda:assignedPerson/cda:name)">
      Error: R2.2 The Prescriber Given Name element is required. The element (given) shall not be empty.</assert>
         <report test="cda:assignedAuthor/cda:assignedPerson/cda:name/cda:given">Success: R2.2 The Prescriber Given Name element is present.</report>
         <!-- R2.2 Prescriber ID number -->
      <assert test="(cda:assignedAuthor/cda:id) or cda:assignedAuthor/@nullFlavor or not(cda:assignedAuthor/cda:assignedPerson)">Error: R2.2 The Prescriber ID number element is required.</assert>
         <report test="cda:assignedAuthor/cda:id">Success: R2.2 The Prescriber ID number element is present.</report>
         <!-- R2.2 Prescriber Profession -->
      <assert test="(cda:functionCode and cda:functionCode[@codeSystem='2.16.840.1.113883.2.9.6.2.7'] and cda:functionCode/@code ) or @nullFlavor or not(cda:assignedAuthor/cda:assignedPerson)">Error: R2.2 The Prescriber Profession element is required.
      The attributes (functionCode\@code) and (functionCode\@codeSystem) shall not be empty and the @codeSystem shall be "2.16.840.1.113883.2.9.6.2.7".</assert>
         <report test="cda:functionCode">Success: R2.2 The Prescriber Profession element is present.</report>
         <!-- R2.2 Prescriber Facility -->
      <assert test="cda:assignedAuthor/cda:representedOrganization or cda:assignedAuthor/@nullFlavor or not(cda:assignedAuthor)">Error: R2.2 The Prescriber "representedOrganization" element is required. The element "representedOrganization" shall be
      present in "author\assignedAuthor\".</assert>
         <!-- R2.2 Prescriber Facility Name  -->
      <assert test="(cda:assignedAuthor/cda:representedOrganization/cda:name and (every $i in cda:assignedAuthor/cda:representedOrganization/cda:name satisfies count(($i))&lt; 2) and ((every $i in cda:assignedAuthor/cda:representedOrganization/cda:name satisfies string-length(normalize-space($i)) &gt; 0) or cda:assignedAuthor/cda:representedOrganization/cda:name/@nullFlavor)) or cda:assignedAuthor/cda:representedOrganization/@nullFlavor or not(cda:assignedAuthor/cda:representedOrganization)">
      Error: R2.2 The Prescriber Facility Name "name" element is required. The element (name) shall not be empty. The element "name" shall be present in "author\assignedAuthor\representedOrganization\".</assert>
         <report test="cda:assignedAuthor/cda:representedOrganization/cda:name">Success: R2.2 The Prescriber Facility Name "name" element is present.</report>
         <!-- R2.2 Prescriber Facility Identifier  -->
      <assert test="(cda:assignedAuthor/cda:representedOrganization/cda:id and (every $i in cda:assignedAuthor/cda:representedOrganization/cda:id satisfies count(($i))&lt; 2) and (cda:assignedAuthor/cda:representedOrganization/cda:id/@root or cda:assignedAuthor/cda:representedOrganization/cda:id/@nullFlavor)) or cda:assignedAuthor/cda:representedOrganization/@nullFlavor or not(cda:assignedAuthor/cda:representedOrganization)">
      Error: R2.2 The Prescriber Facility Identifier "id" element is required. The attribute 'root' of the element (id) shall not be empty. The element "id" shall be present in "author\assignedAuthor\representedOrganization\".</assert>
         <report test="cda:assignedAuthor/cda:representedOrganization/cda:id">Success: R2.2 The Prescriber Facility Identifier "id" element is present.</report>
         <!-- R2.2 Prescriber Facility Address  -->
      <assert test="(cda:assignedAuthor/cda:representedOrganization/cda:addr and (every $i in cda:assignedAuthor/cda:representedOrganization/cda:addr satisfies count(($i))&lt; 2)) or cda:assignedAuthor/cda:representedOrganization/@nullFlavor or not(cda:assignedAuthor/cda:representedOrganization)">
      Error: R2.2 The Prescriber Facility Address "addr" element is required. The element "addr" shall be present in "author\assignedAuthor\representedOrganization\".</assert>
         <report test="cda:assignedAuthor/cda:representedOrganization/cda:addr">Success: R2.2 The Prescriber Facility Address "addr" element is present.</report>
      </rule>
      <rule context="//cda:section[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/cda:author/cda:assignedAuthor/cda:representedOrganization/cda:addr">
      <!-- R2.2 Prescriber Facility Street  -->
      <assert test="(cda:streetAddressLine and count(cda:streetAddressLine)&lt;2 and ((cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) or cda:streetAddressLine/@nullFlavor)) or @nullFlavor or (not(cda:streetAddressLine))">
      Error: R2.2 The Prescriber Facility Street "streetAddressLine" element is optional. If present, the element "streetAddressLine" shall not be empty otherwise the nullFlavor attribute shall be present. The element "streetAddressLine" cannot be
      repeated (cardinality [1..1])</assert>
         <!-- R2.2 Prescriber Facility City  -->
      <assert test="(cda:city and count(cda:city)&lt;2 and ((cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) or cda:city/@nullFlavor)) or @nullFlavor or (not(cda:city))">Error: R2.2 The Prescriber Facility City
      "city" element is optional. If present, the element "city" shall not be empty otherwise the nullFlavor attribute shall be present. The element "city" cannot be repeated (cardinality [1..1])</assert>
         <!-- R2.2 Prescriber Facility State or Province  -->
      <assert test="(cda:state and count(cda:state)&lt;2 and ((cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0)) or cda:state/@nullFlavor)) or @nullFlavor">Error: R2.2 The Prescriber Facility State or Province
      "state" element is optional. If present the element "state" shall not be empty otherwise the nullFlavor attribute shall be present. The element "state" cannot be repeated (cardinality [1..1])</assert>
         <!-- R2.2 Prescriber Facility Postal Code  -->
      <assert test="(cda:postalCode and count(cda:postalCode)&lt;2 and ((cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) or cda:postalCode/@nullFlavor)) or @nullFlavor or (not(cda:postalCode))">Error:
      R2.2 The Prescriber Facility Postal Code "postalCode" element is optional. If present the element "postalCode" shall not be empty otherwise the nullFlavor attribute shall be present. The element "postalCode" cannot be repeated (cardinality
      [1..1])</assert>
         <!-- R2.2 Prescriber Facility Country  -->
      <assert test="(cda:country and count(cda:country)&lt;2 and (cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Error: R2.2 The Prescriber Facility Country "country" element is required.
      The element "country" shall not be empty and cannot be repeated (cardinality [1..1])</assert>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.2-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
    <!--  <title>epSOS Prescription Item Entry Content Module Specification - errors validation phase</title> -->
    <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']">
      <!-- Verify that the parents templateId are present. -->
      <!--Removed : update for the epSOS_Implementation_v0.0.6_20100830_translation_1 -->
      <!--        <assert test='cda:templateId[@root="1.3.6.1.4.1.12559.11.10.1.3.1.3.4"]'>
         Error: (cf.§12.1.2.4 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier (1.3.6.1.4.1.12559.11.10.1.3.1.3.4) for Prescription Item Entry Content Module shall be
         present.
        </assert> 
        <assert test='cda:templateId[@root="1.3.6.1.4.1.19376.1.5.3.1.4.7"]'>
         Error: (cf.§12.1.2.4 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier (1.3.6.1.4.1.19376.1.5.3.1.4.7) for Prescription Item Entry Content Module shall be
         present.
        </assert> -->
      <assert test="cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]">Error: (cf.Â§12.1.2.4 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier (2.16.840.1.113883.10.20.1.24) for Prescription
      Item Entry Content Module shall be present.</assert>
         <!--$9 Body Data elements -->
      <!-- R2.4 Prescription Item ID -->
      <assert test="cda:id and count(cda:id)&lt;2">Error: R2.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Prescription Item ID is required. The element "id" cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:id and count(cda:id)&lt;2">Success: R2.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Prescription Item ID element "id" is present and have a cardinality [1..1].</report>
         <!-- R4.2 Brand name of the medicinal product prescribed in country A -->
      <assert test="cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/cda:name and (every $i in cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/cda:name satisfies string-length(normalize-space($i)) &gt; 0)">Error: R4.2
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Brand name of the medicinal product element "name" is required. The element "name" shall be filled.</assert>
         <report test="cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/cda:name">Success: R4.2 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Brand name of the medicinal product element "name" is present.</report>
         <report test="(every $i in cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/cda:name satisfies string-length(normalize-space($i)) &gt; 0)">Success: R4.2 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Brand name of the medicinal product
      element "name" is filled.</report>
         <!-- R4.5 Medicinal product package -->
      <!-- Removed this is not anymore required after v1.2 20101220
     <assert test="cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode">
      Error: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The "formCode" element is required even if null flavored.
     </assert>
     <report test="cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode">
      Success: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The "formCode" element is present.
     </report>
     -->
      <!-- R4.6 Pharmaceutical dose form -->
      <assert test="cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode and (every $i in cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode satisfies count(($i))&lt; 2) and cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode/@code and cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1']">
      Error: R4.6 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Pharmaceutical dose form element "formCode" is required. The attributes 'code' and 'codeSystem' of the element "formCode" shall be filled and the attribute 'codeSystem' shall be
      '1.3.6.1.4.1.12559.11.10.1.3.1.44.1'. The element "formCode" shall have a cardinality [1..1].</assert>
         <report test="cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode and (every $i in cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode satisfies count(($i))&lt; 2)">Success: R4.6
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Pharmaceutical dose form element "formCode" is present and do have a cardinality [1..1].</report>
         <report test="cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode/@code and cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1']">Success: R4.6
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The attributes 'code' and 'codeSystem' of the Pharmaceutical dose form element "formCode" are filled and the 'codeSystem' is correctly set to the value '1.3.6.1.4.1.12559.11.10.1.3.1.44.1'.</report>
         <!-- R4.7 Route of Administration (Optional) -->
      <assert test="(cda:routeCode and cda:routeCode/@code and cda:routeCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1']) or (not(cda:routeCode) or cda:routeCode/@nullFlavor)">Error: R4.7 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Route of
      Administration element "routeCode" is Optional. If present, the attributes 'code' and 'codeSystem' of the element "formCode" shall be filled and the attribute 'codeSystem' shall be '1.3.6.1.4.1.12559.11.10.1.3.1.44.1'. Otherwise the nullFlavor
      attribute shall be present.</assert>
         <!--R4.9 Number of units per intake (cf.8.1.1.2.4.7, @value should not be in doseQuantity, so we require low and high element ) -->
      <assert test="cda:doseQuantity and ((cda:doseQuantity/cda:low/@value and cda:doseQuantity/cda:high/@value) or cda:doseQuantity/@nullFlavor)">Error: R4.9 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Number of units per intake value "doseQuantity" element
      is required. The element "low\@value" and "high\value" shall be present in the element "doseQuantity" otherwise the nullFlavor attribute shall be present.</assert>
         <report test="cda:doseQuantity">Success: R4.9 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Number of units per intake "doseQuantity" is present</report>
         <report test="cda:doseQuantity/cda:low/@value and cda:doseQuantity/cda:high/@value">Success: R4.9 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Number of units per intake elements "doseQuantity\high\@value" and "doseQuantity\low\@value" are
      present.</report>
         <!--R4.10 Frequency of intake -->
      <assert test="(cda:effectiveTime[2][@operator='A'] and (cda:effectiveTime[2][matches(normalize-space(@xsi:type),'TS$')] or cda:effectiveTime[2][matches(normalize-space(@xsi:type),'IVL_TS$')] or cda:effectiveTime[2][matches(normalize-space(@xsi:type),'PIVL_TS$')] or cda:effectiveTime[2][matches(normalize-space(@xsi:type),'EIVL_TS$')])) or cda:effectiveTime[2]/@nullFlavor">
      Error: R4.10 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Frequency of intake element "effectiveTime[2]" is required. The attribute 'xsi:type' of the element "effectiveTime[2]" shall take a value between "TS, IVL_TS, PIVL_TS or EIVL_TS" and the
      attribute 'operator' shall take the value 'A'. Otherwise the nullFlavor attribute shall be present.</assert>
         <report test="cda:effectiveTime[2]/@xsi:type">Success: R4.10 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Frequency of intake element "effectiveTime[2]" is present. The attribute 'xsi:type' is also present.</report>
         <!--R4.11 Duration of treatment -->
      <!--low -->
      <assert test="cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:low/@nullFlavor or (cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:low/@value and (matches(cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:low/@value,'^\d{6,8}$') or matches(cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:low/@value,'^\d{6,12}(\-|\+)\d{2,4}$')))">
      Error: R4.11 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Duration of treatment element "low/@value" is required. The attribute 'value' of the element "low" shall contain 6 to 8 digits exemple : March 01, 2000 will be "20000301 or 200003", or could
      contain the date end the time, exmple : March 01, 2000 @ 3:00 PM will be "200003011500-0700". Otherwise the nullFlavor attribute shall be present.</assert>
         <report test="cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:low/@value">Success: R4.11 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Duration of treatment element "low/@value" is present for the
      effectiveTime[1]\matches(normalize-space(@xsi:type),'IVL_TS$').</report>
         <!--high -->
      <assert test="cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:high/@nullFlavor or (cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:high/@value and (matches(cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:high/@value,'^\d{6,8}$') or matches(cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:high/@value,'^\d{6,12}(\-|\+)\d{2,4}$')))">
      Error: R4.11 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Duration of treatment element "high/@value" is required. The attribute 'value' of the element "high" shall contain 6 to 8 digits exemple : March 01, 2000 will be "20000301 or 200003", or could
      contain the date end the time, exmple : March 01, 2000 @ 3:00 PM will be "200003011500-0700". Otherwise the nullFlavor attribute shall be present.</assert>
         <report test="cda:effectiveTime[1][matches(normalize-space(@xsi:type),'IVL_TS$')]/cda:high/@value">Success: R4.11 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Duration of treatment element "high/@value" is present for the
      effectiveTime[1]\matches(normalize-space(@xsi:type),'IVL_TS$').</report>
      </rule>
      <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:ingredient[@classCode='ACTI']">
      <!-- R4.3 Active Ingredient -->
      <assert test="epsos:ingredient/epsos:code and epsos:ingredient/epsos:code/@code and epsos:ingredient/epsos:code/@codeSystem and (every $i in epsos:ingredient/epsos:code satisfies count(($i))&lt; 2)">Error: R4.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2)
      The Active Ingredient element "code" is required. The attributes 'code' and 'codeSystem' of the element "code" shall be filled. The element "code" shall have a cardinality [1..1].</assert>
         <report test="epsos:ingredient/epsos:code and (every $i in epsos:ingredient/epsos:code satisfies count(($i))&lt; 2)">Success: R4.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Active Ingredient element "code" is present and do have cardinality
      [1..1].</report>
         <report test="epsos:ingredient/epsos:code/@code and epsos:ingredient/epsos:code/@codeSystem">Success: R4.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The attributes 'code' and 'codeSystem' of the Active Ingredient element "code" are filled.</report>
         <!-- R4.4 Strength of the medicinal product -->
      <assert test="epsos:quantity and count(epsos:quantity)&lt;2">Error: R4.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Strength of the medicinal product element "quantity" is required. The element "quantity" shall have a cardinality [1..1].</assert>
         <report test="epsos:quantity and count(epsos:quantity)&lt;2">Success: R4.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Strength of the medicinal product element "quantity" is present and do have a cardinality [1..1].</report>
      </rule>
      <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine">
      <!-- R4.5 Medicinal product package -->
      <assert test="epsos:formCode[@nullFlavor] or (epsos:formCode and (every $i in epsos:formCode satisfies count(($i))&lt; 2) and epsos:formCode/@code and epsos:formCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1'])">Error: R4.5
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Medicinal product package element "formCode" is required. The attributes 'code' and 'codeSystem' of the element "formCode" shall be filled and the attribute 'codeSystem' shall be
      '1.3.6.1.4.1.12559.11.10.1.3.1.44.1'. The element "formCode" shall have a cardinality [1..1].</assert>
         <report test="epsos:formCode[@nullFlavor] or (epsos:formCode and (every $i in epsos:formCode satisfies count(($i))&lt; 2))">Success: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Medicinal product package element "formCode" is present and do have a
      cardinality [1..1].</report>
         <report test="epsos:formCode[@nullFlavor] or (epsos:formCode/@code and epsos:formCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1'])">Success: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The attributes 'code' and 'codeSystem' of the Medicinal
      product package element "formCode" are filled and the 'codeSystem' is correctly set to the value '1.3.6.1.4.1.12559.11.10.1.3.1.44.1'.</report>
         <!-- R4.8.1 package size -->
      <assert test="epsos:capacityQuantity">Error: R4.8.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The package size element "capacityQuantity" is required.</assert>
         <report test="epsos:capacityQuantity">Success: R4.8.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The package size element "capacityQuantity" is present.</report>
      </rule>
      <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:entryRelationship[@typeCode='COMP']/cda:supply[@moodCode= 'RQO' and cda:independentInd/@value='false']">
      <!-- R4.8 Number of packages -->
      <assert test="cda:quantity and cda:quantity/@value and cda:quantity/@unit">Error: R4.8 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Number of packages element "quantity" is required.</assert>
         <report test="cda:quantity">Success: R4.8 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Number of packages element "quantity" is present.</report>
         <report test="cda:quantity/@value and cda:quantity/@unit">Success: R4.8 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The attributes 'value' and 'unit' of The Number of packages element "quantity" are filled.</report>
      </rule>
      <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:participant[@typeCode='AUT']/cda:participantRole[@classCode='LIC']/cda:scopingEntity[@classCode='ORG']/cda:desc">
      <!--R2.3.1 Prescriber Credentialing Organization (College) Name (Optional)-->
      <assert test="string-length(normalize-space(current())) &gt; 1 or @nullFlavor">Error: R2.3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Prescriber Credentialing Organization (College) Name element "desc" is optional. If present this element shall be
      filled. Otherwise the nullFlavor attribute shall be present.</assert>
         <report test="string-length(normalize-space(current())) &gt; 1 or @nullFlavor">Success: R2.3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Prescriber Credentialing Organization (College) Name element "desc" is fulfilled.</report>
      </rule>
      <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:participant[@typeCode='AUT']/cda:participantRole[@classCode='LIC']/cda:scopingEntity[@classCode='ORG']/cda:id">
      <!-- R2.3.2 Prescribing Credentialing Organization Identifier (Optional)-->
      <assert test="string-length(normalize-space(@root)) &gt; 1 or @nullFlavor">Error: R2.3.2 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Prescribing Credentialing Organization Identifier element "id" is optional. If present the attribute 'root' shall be
      filled. Otherwise the nullFlavor attribute shall be present.</assert>
         <report test="string-length(normalize-space(@root)) &gt; 1 or @nullFlavor">Success: R2.3.2 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Prescribing Credentialing Organization Identifier element "id" is fulfilled.</report>
      </rule>
      <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:entryRelationship[@typeCode='SUBJ'][@inversionInd='true']/cda:observation[@classCode='OBS'][cda:code/@code='SUBST' and cda:code/@codeSystem='2.16.840.1.113883.5.6']/cda:value">

      <!-- R4.16 Substitution (Optional)-->
      <assert test="@nullFlavor or (matches(normalize-space(@xsi:type),'CE$') and @codeSystem = '2.16.840.1.113883.5.1070' and string-length(normalize-space(@code)))">Error: R4.16 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Substitution element "value" is
      optional. If present, the attributes '@code' shall be filled and 'xsi:type' shall be 'CE' and the '@codeSystem = 
      <value-of select="@codeSystem"/>' shall be '2.16.840.1.113883.5.1070' which is the OID for epSOS:SubstitutionCodes. Otherwise the nullFlavor attribute shall be present.</assert>
         <report test="@nullFlavor or (matches(normalize-space(@xsi:type),'CE$') and @codeSystem = '2.16.840.1.113883.5.1070' and string-length(normalize-space(@code)))">Success: R4.16 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Substitution element "value" is
      fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.1']"/>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Medications - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.7&#34;]">
         <assert test="cda:templateId[@root = &#34;2.16.840.1.113883.10.20.1.24&#34;]">Error: In IHE PCC Medications (1.3.6.1.4.1.19376.1.5.3.1.4.7), all Medications entries use the templateId element specified (2.16.840.1.113883.10.20.1.24) to indicate
      that they are medication acts. This element is required.</assert>
         <assert test="count(cda:templateId[@root = &#34;1.3.6.1.4.1.19376.1.5.3.1.4.7.1&#34;] | cda:templateId[@root = &#34;1.3.6.1.4.1.19376.1.5.3.1.4.8&#34;] | cda:templateId[@root = &#34;1.3.6.1.4.1.19376.1.5.3.1.4.9&#34;] | cda:templateId[@root = &#34;1.3.6.1.4.1.19376.1.5.3.1.4.10&#34;] | cda:templateId[@root = &#34;1.3.6.1.4.1.19376.1.5.3.1.4.11&#34;]) = 1">
      Error: In IHE PCC Medications (1.3.6.1.4.1.19376.1.5.3.1.4.7), the &lt;templateId&gt; element identifies the Medications &lt;entry&gt; as a particular type of medication event, allowing for validation of the content. As a side effect, readers of
      the CDA can quickly locate and identify medication events. The templateId must use one of the values in the table in the specification.</assert>
         <assert test="not(cda:templateId[@root = &#34;1.3.6.1.4.1.19376.1.5.3.1.4.7.1&#34;]) or count(.//cda:substanceAdministration) = 0">Error: In IHE PCC Medications (1.3.6.1.4.1.19376.1.5.3.1.4.7), template ID 1.3.6.1.4.1.19376.1.5.3.1.4.7.1
      indicates a "normal" &lt;substanceAdministration&gt; act that may not contain any subordinate &lt;substanceAdministration&gt; acts.</assert>
         <assert test="count(cda:id) = 1">Error: In IHE PCC Medications (1.3.6.1.4.1.19376.1.5.3.1.4.7), a top level &lt;substanceAdministration&gt; element must be uniquely identified in Medications. If there is no explicit identifier for the observation
      in the source EMR system, a GUID may be used for the root attribute, and the extension may be omitted. Although HL7 allows for multiple identifiers, the profile requires that one and only one be used.</assert>
         <assert test="cda:statusCode[@code = &#34;completed&#34;]">Error: In IHE PCC Medications (1.3.6.1.4.1.19376.1.5.3.1.4.7), the status of all Medications &lt;substanceAdministration&gt; elements must be "completed". The act has either occurred, or
      the request or order has been placed.</assert>
         <assert test="cda:consumable/cda:manufacturedProduct/cda:templateId[@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.7.2&#34;]">Error: In IHE PCC Medications (1.3.6.1.4.1.19376.1.5.3.1.4.7), the &lt;consumable&gt; element shall be present, and shall
      contain a &lt;manufacturedProduct&gt; entry conforming to the Product Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.2) template.</assert>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Product Entry - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.7.2&#34;]">
         <assert test="cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.53&#34;]">Error: In IHE PCC Product Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.2), the template ID for CCD parent (2.16.840.1.113883.10.20.1.53) is required.</assert>
      </rule>
      <rule context="*[cda:manufacturedProduct/cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.7.2&#34; and manufacturedProduct/cda:manufacturedMaterial/cda:code]">
         <assert test="cda:manufacturedMaterial/cda:code/cda:originalText/cda:reference">Error: In IHE PCC Product Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.2), the name and strength of the medication are specified in the elements under the
      &lt;manufacturedMaterial&gt; element. The &lt;originalText&gt; shall contain a &lt;reference&gt; whose URI value points to the generic name and strength of the medication, or just the generic name alone if strength is not relevant.</assert>
      </rule>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.7.2&#34;]">
         <report test="cda:id">Success: R2.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Prescription Item ID element "id" is present.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Product Entry - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.11&#34;]">
         <report test="//cda:substanceAdministation">Success: In IHE PCC Combination Medications: 1.3.6.1.4.1.19376.1.5.3.1.4.11), In the case of the prepackaged combination, it is sufficient to supply the name of the combination drug product, and its
      strength designation in a single "substanceAdministation" entry. The dosing information shall then be recorded as simply a count of administration units. .</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Normal Dosing - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.7.1&#34;]">
         <report test="//cda:substanceAdministation">Error: In IHE PCC Normal Dosing: 1.3.6.1.4.1.19376.1.5.3.1.4.7.1), medications that use this template identifier shall not use subordinate &lt;substanceAdministation&gt; acts.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Product Entry - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.11&#34;]">
         <report test="//cda:substanceAdministation">Success: In IHE PCC Combination Medications: 1.3.6.1.4.1.19376.1.5.3.1.4.11), In the case of the prepackaged combination, it is sufficient to supply the name of the combination drug product, and its
      strength designation in a single "substanceAdministation" entry. The dosing information shall then be recorded as simply a count of administration units. .</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.9-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Product Entry - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.9&#34;]"/>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Medication Fulfillment Instructions - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.3.1&#34;]">
         <assert test="cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.43&#34;]">Error: In IHE PCC Medication Fulfillment Instructions (1.3.6.1.4.1.19376.1.5.3.1.4.3.1), the parent CCD template ID (2.16.840.1.113883.10.20.1.43) for Medication
      Fulfillment Instructions shall exist.</assert>
         <assert test="cda:code[@code=&#34;FINSTRUCT&#34; and @codeSystem=&#34;1.3.5.1.4.1.19376.1.5.3.2&#34; and @codeSystemName=&#34;IHEActCode&#34;]">Error: In IHE PCC Medication Fulfillment Instructions (1.3.5.1.4.1.19376.1.5.3.1.4.3.1), the code
      for Medication Fulfillment Instructions shall be recorded exactly as specified: &lt;code code='FINSTRUCT' codeSystem='1.3.5.1.4.1.19376.1.5.3.2' codeSystemName='IHEActCode' /&gt;.</assert>
         <assert test="cda:text/cda:reference">Error: The &lt;text&gt; element of Medication Fulfillment Instructions contains a free text representation of the instruction. For CDA this SHALL contain a provides a &lt;reference&gt; element to the link text
      of the comment in the narrative portion of the document. The comment itself is not the act being coded, so it appears in the &lt;text&gt; of the &lt;observation&gt;, not as part of the &lt;code&gt;.</assert>
         <assert test="cda:statusCode[@code=&#34;completed&#34;]">Error: In IHE PCC Medication Fulfillment Instructions (1.3.6.1.4.1.19376.1.5.3.1.4.3.1), the code attribute of &lt;statusCode&gt; for all Medication Fulfillment Instriction comments must be
      completed.</assert>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Patient Medication Instructions - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.3&#34;]">
         <assert test="cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.49&#34;]">Error: In IHE PCC Patient Medication Instructions (1.3.6.1.4.1.19376.1.5.3.1.4.3), the parent CCD template ID (2.16.840.1.113883.10.20.1.49) of Patient Medication
      Instructions shall be present.</assert>
         <assert test="cda:code[@code=&#34;PINSTRUCT&#34; and @codeSystem=&#34;1.3.5.1.4.1.19376.1.5.3.2&#34; and @codeSystemName=&#34;IHEActCode&#34;]">Error: In IHE PCC Patient Medication Instructions (1.3.5.1.4.1.19376.1.5.3.1.4.3), the code for
      Patient Medication Instructions shall be recorded exactly as specified: &lt;code code='PINSTRUCT' codeSystem='1.3.5.1.4.1.19376.1.5.3.2' codeSystemName='IHEActCode' /&gt;.</assert>
         <assert test="cda:text/cda:reference">Error: The &lt;text&gt; element indicates the text of the comment in Patient Medication Instructions. For CDA, this SHALL be represented as a &lt;reference&gt; element that points at the narrative portion of
      the document. The comment itself is not the act being coded, so it appears in the &lt;text&gt; of the &lt;observation&gt;, not as part of the &lt;code&gt;.</assert>
         <assert test="cda:statusCode[@code=&#34;completed&#34;]">Error: In IHE PCC Patient Medication Instructions (1.3.6.1.4.1.19376.1.5.3.1.4.3), the code attribute of &lt;statusCode&gt; for all comments in Patient Medication Instructions must be
      completed.</assert>
      </rule>
  </pattern>
  <!--Check warnings -->
  <!--    <xi:include parse="xml" href="templates/warnings/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch"
        xpointer="xmlns(x=http://purl.oclc.org/dsdl/schematron) xpointer(//x:pattern)">
        <xi:fallback>
        <!-\- xi:include error : file not found :  templates/warnings/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch -\->
        </xi:fallback>
    </xi:include>-->
  <!--Check codes -->
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.1-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <rule context="/cda:ClinicalDocument">
         <assert test="cda:relatedDocument">Error: The Original document Identification element "relatedDocument" is required. The relationship between the eP instance and the document (data) in use in the National Infrastructure ("original Country-A
      document") is kept via the XFRM relationship. (cf.Â§11.1.2.1 of 
      <value-of select="$newSpecDoc"/>)</assert>
         <report test="cda:relatedDocument">Success: The Original document Identification element "relatedDocument" is present.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:relatedDocument">
         <assert test="@typeCode ='XFRM'">Error: The Original document Identification attribute's 'typeCode' of the "relatedDocument" element shall take the value 'XFRM'.(cf.Â§11.1.2.1 of 
      <value-of select="$newSpecDoc"/>)</assert>
         <report test="cda:relatedDocument">Success: The Original document Identification attribute's 'typeCode' of the "relatedDocument" element is fulfilled.</report>
         <assert test="cda:parentDocument/cda:id/@root">Error: The Original document Identification parentDocument ID 'parentDocument/id' is required and it's 'root' attribute shall be filled.(cf.Â§11.1.2.1 of 
      <value-of select="$newSpecDoc"/>)</assert>
         <report test="cda:parentDocument/cda:id/@root">Success: The Original document Identification parentDocument ID 'parentDocument/id' is fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Medical Documents Specification - errors validation phase</title>
      <!-- R1.11.8 Confidentiality code  (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:confidentialityCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.31']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.31']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R1.11.8 (1.3.6.1.4.1.19376.1.5.3.1.1.1) The Confidentiality code element "confidentialityCode" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '2.16.840.1.113883.5.25', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(HL7:Confidentiality which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.31).</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.31']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.31']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R1.11.8 (1.3.6.1.4.1.19376.1.5.3.1.1.1) The Confidentiality code element "confidentialityCode" is valid.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:languageCode">
      <!-- R1.11.10 Document language code (Data Validation)-->
      <assert test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Error: R1.11.10 The document language code is not valid. the languageCode element describes the
      language code. It uses the same vocabulary described for the ClinicalDocument/languageCode element described in more detail in HL7 CRS: 2.1.1.</assert>
         <report test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Success: R1.11.10 The document language code is valid.</report>
      </rule>
      <!-- R1.11.9 Legal Authenticator Telecommunication-->
    <rule context="/cda:ClinicalDocument/cda:legalAuthenticator/cda:assignedEntity/cda:telecom">
         <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.11.9 The Legal Authenticator Telecommunication attribute's
      '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.
      <value-of select="$usedDataDoc"/>(HL7:TelecommunicationAddressUse which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.40).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.11.9 The Legal Authenticator Telecommunication attribute's
      '@use' of the element "telecom" is valid.</report>
      </rule>
      <!--R1.2 Gender (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:administrativeGenderCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.34']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.34']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R1.2 Gender element "administrativeGenderCode" is not valid, one of its attributes '@code = 
      <value-of select="current()/@code"/>' or '@displayName = 
      <value-of select="current()/@displayName"/>' or '@codeSystem = 
      <value-of select="current()/@codeSystem"/>' is not valid. The attribute '@codeSystem' shall be '2.16.840.1.113883.5.1', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSGender which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.34)</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.34']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.34']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R1.2 Gender element "administrativeGenderCode" is valid.</report>
      </rule>
      <!-- R1.1.2 Pharmaceutical dose form  (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:name/cda:prefix">
         <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.43']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.33']/Entry/@code = current()) or @nullFlavor">Error: R1.1.2 (1.3.6.1.4.1.19376.1.5.3.1.1.1) The Patient Prefix element "prefix
      = 
      <value-of select="current()"/>" is not valid, to see valid prefix please cf.
      <value-of select="$usedDataDoc"/>(HL7:EntityNamePartQualifier which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.33)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.43']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.33']/Entry/@code = current()) or @nullFlavor">Success: R1.1.2 (1.3.6.1.4.1.19376.1.5.3.1.1.1) The Patient Prefix element
      "prefix" is valid.</report>
      </rule>
      <!--R1.5.6 Patient's Country (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:addr/cda:country">
         <assert test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Error: R1.5.6 Patient's Country element "country = 
      <value-of select="current()"/>" is not valid. To see valid "country" please cf.
      <value-of select="$usedDataDoc"/>(epSOSCountry which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.4)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Success: R1.5.6 Patient's Country element "country" is valid.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:telecom">
      <!-- R1.6 Patient's Telecommunication-->
      <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.6 The Patient's Telecommunication attribute's '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.
      <value-of select="$usedDataDoc"/>(HL7:TelecommunicationAddressUse which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.40)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.6 The Patient's Telecommunication attribute's '@use' of the
      element "telecom" is valid.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Language Communication - errors validation phase</title>
      <!-- R1.7 The Patient's preferred language code (Data Validation)-->
    <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.2.1&#34;]/cda:languageCode">
         <assert test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Error: R1.7 The Patient's preferred language code is not valid. the languageCode element
      describes the language code. It uses the same vocabulary described for the ClinicalDocument/languageCode element described in more detail in HL7 CRS: 2.1.1.</assert>
         <report test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Success: R1.7 The Patient's preferred language code is valid.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Patient Contacts - errors validation phase</title>
      <!--R1.7.A.3.2.6 Guardian's Country (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:addr/cda:country">
         <assert test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Error: R1.7.A.3.2.6 Guardian's Country element "country = 
      <value-of select="current()"/>" is not valid. To see valid "country" please cf.
      <value-of select="$usedDataDoc"/>(epSOSCountry which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.4)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Success: R1.7.A.3.2.6 Guardian's Country element "country" is valid.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:telecom">
      <!-- R1.7.A.4 Guardian's Telecommunication-->
      <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.7.A.4 (1.3.6.1.4.1.19376.1.5.3.1.2.4) The Guardian's
      Telecommunication attribute's '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.
      <value-of select="$usedDataDoc"/>(HL7:TelecommunicationAddressUse which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.40)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.7.A.4 (1.3.6.1.4.1.19376.1.5.3.1.2.4) The Guardian's
      Telecommunication attribute's '@use' of the element "telecom" is valid.</report>
      </rule>
      <!--R1.9.2.6 Prefered HCP/Legal Organization Country (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:participant[cda:functionCode/@code='PCP' and cda:functionCode/@codeSystem='2.16.840.1.113883.5.88']/cda:associatedEntity[@classCode='PRS']/cda:addr/cda:country">
         <assert test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Error: R1.9.2.6 Prefered HCP/Legal Organization Country element "country = 
      <value-of select="current()"/>" is not valid. To see valid "country" please cf.
      <value-of select="$usedDataDoc"/>(epSOSCountry which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.4)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Success: R1.9.2.6 Prefered HCP/Legal Organization Country element "country" is
      valid.</report>
      </rule>
      <!-- R1.9.3 Prefered HCP Legal Organization Telecom-->
    <rule context="/cda:ClinicalDocument/cda:participant[cda:functionCode/@code='PCP' and cda:functionCode/@codeSystem='2.16.840.1.113883.5.88']/cda:associatedEntity[@classCode='PRS']/cda:telecom">
         <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.9.3 (1.3.6.1.4.1.19376.1.5.3.1.2.4) The HCP Telecommunication
      attribute's '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.
      <value-of select="$usedDataDoc"/>(HL7:TelecommunicationAddressUse which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.40)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.9.3 (1.3.6.1.4.1.19376.1.5.3.1.2.4) The HCP Telecommunication
      attribute's '@use' of the element "telecom" is valid.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:participant[cda:functionCode/@code='PCP' and cda:functionCode/@codeSystem='2.16.840.1.113883.5.88']/cda:associatedEntity[@classCode='PRS']/cda:scopingOrganization/cda:telecom">
         <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.9.3 (1.3.6.1.4.1.19376.1.5.3.1.2.4) The HCP Telecommunication
      attribute's '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.
      <value-of select="$usedDataDoc"/>(HL7:TelecommunicationAddressUse which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.40).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.9.3 (1.3.6.1.4.1.19376.1.5.3.1.2.4) The HCP Telecommunication
      attribute's '@use' of the element "telecom" is valid.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <title>IHE PCC Healthcare Providers and Pharmacies - errors validation phase</title>
      <!--R1.10.9.3.5 Healthcare Facility's Country (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:author/cda:assignedAuthor/cda:representedOrganization/cda:addr/cda:country">
         <assert test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Error: R1.10.9.3.5 Healthcare Facility's Country element "country = 
      <value-of select="current()"/>" is not valid. To see valid "country" please cf.
      <value-of select="$usedDataDoc"/>(epSOSCountry which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.4)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Success: R1.10.9.3.5 Healthcare Facility's Country element "country" is valid.</report>
      </rule>
      <!--R1.10.9.3.5 Healthcare Facility's Country (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:representedOrganization/cda:addr/cda:country">
         <assert test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Error: R1.10.9.3.5 Healthcare Facility's Country element "country = 
      <value-of select="current()"/>" is not valid. To see valid "country" please cf.
      <value-of select="$usedDataDoc"/>(epSOSCountry which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.4)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Success: R1.10.9.3.5 Healthcare Facility's Country element "country" is valid.</report>
      </rule>
      <!-- R1.10.8 HCP Telecom  -->
    <rule context="/cda:ClinicalDocument/cda:author/cda:assignedAuthor/cda:telecom">
         <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.10.8 (1.3.6.1.4.1.19376.1.5.3.1.2.3) The HCP Telecommunication
      attribute's '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.
      <value-of select="$usedDataDoc"/>(HL7:TelecommunicationAddressUse which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.40)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.10.8 (1.3.6.1.4.1.19376.1.5.3.1.2.3) The HCP
      Telecommunication attribute's '@use' of the element "telecom" is valid.</report>
      </rule>
      <!-- R1.10.8 HCP Telecom  -->
    <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:telecom">
         <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.10.8 (1.3.6.1.4.1.19376.1.5.3.1.2.3) The HCP Telecommunication
      attribute's '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.
      <value-of select="$usedDataDoc"/>(HL7:TelecommunicationAddressUse which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.40)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.10.8 (1.3.6.1.4.1.19376.1.5.3.1.2.3) The HCP
      Telecommunication attribute's '@use' of the element "telecom" is valid.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eP-codes"
            xml:base="templates/ePtemplates/codes/1.3.6.1.4.1.19376.1.5.3.1.1.1.eP.sch">
      <title>IHE PCC Medical Documents Specification - errors validation phase</title>
      <!--R1.11.10 Legal Authenticator Facility Country (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:legalAuthenticator/cda:assignedEntity/cda:representedOrganization/cda:addr/cda:country">
         <assert test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Error: R1.11.10 Legal Authenticator Facility Country element "country" is not valid.
      (cf.epSOS_MVC_V1 4.xls)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Success: R1.11.10 Legal Authenticator Facility Country element "country" is
      fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:legalAuthenticator/cda:assignedEntity/cda:representedOrganization/cda:telecom">
      <!-- R1.11.10 Legal Authenticator Facility Telecommunication-->
      <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.11.10 The Legal Authenticator Facility Telecom attribute's
      '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.epSOS_MVC_V1 4.xls (HL7:TelecommunicationAddressUse).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.11.10 The Legal Authenticator Facility Telecom attribute's
      '@use' of the element "telecom" is fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eP-codes"
            xml:base="templates/ePtemplates/codes/1.3.6.1.4.1.19376.1.5.3.1.2.3.eP.sch">
    <!--epSOS-->
    <!--(cf.epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc : 8  Common Header Data Elements)-->
    <!-- R1.10.6 HCP Profession (Health Care Professional's Specialty) (Data Validation) -->
    <rule context="/cda:ClinicalDocument/cda:author/cda:functionCode">
         <assert test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.1']/Entry[@code = current()/@code]/@displayName = current()/@displayName )">Error: R1.10.6 HCP Profession element "functionCode"
      is not valid, one of the attributes '@code' or '@displayName' is not valid. (cf.epSOS_MVC_V1 4.xls)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.1']/Entry[@code = current()/@code]/@displayName = current()/@displayName )">Success: R1.10.6 HCP Profession element
      "functionCode" is fulfilled.</report>
      </rule>
      <!-- R1.10.6 HCP Profession (Health Care Professional's Specialty) (Data Validation) -->
    <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer/cda:functionCode">
         <assert test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.1']/Entry[@code = current()/@code]/@displayName = current()/@displayName )">Error: R1.10.6 HCP Profession element "functionCode"
      is not valid, one of the attributes '@code' or '@displayName' is not valid. (cf.epSOS_MVC_V1 4.xls)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.1']/Entry[@code = current()/@code]/@displayName = current()/@displayName )">Success: R1.10.6 HCP Profession element
      "functionCode" is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:author/cda:assignedAuthor/cda:representedOrganization/cda:telecom">
      <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.10.9.4.1/R1.10.9.4.2 The HCP Telecom attribute's '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.epSOS_MVC_V1 4.xls (HL7:TelecommunicationAddressUse).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.10.9.4.1/R1.10.9.4.2 The HCP Telecom attribute's '@use' of
      the element "telecom" is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:representedOrganization/cda:telecom">
      <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.10.9.4.1/R1.10.9.4.2 The HCP Telecom attribute's '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.epSOS_MVC_V1 4.xls (HL7:TelecommunicationAddressUse).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.10.9.4.1/R1.10.9.4.2 The HCP Telecom attribute's '@use' of
      the element "telecom" is fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.1-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
    <!-- R2.2 Prescriber Profession (Data Validation)-->
    <rule context="//cda:section[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/cda:author/cda:functionCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.1']/Entry[@code = current()/@code]/@displayName = current()/@displayName )">Error: R2.2 The Prescriber Profession element
      "functionCode" is not valid, one of its attributes '@code = 
      <value-of select="current()/@code"/>' or '@displayName = 
      <value-of select="current()/@displayName"/>' or '@codeSystem = 
      <value-of select="current()/@codeSystem"/>' is not valid. The attribute '@codeSystem' shall be '2.16.840.1.113883.2.9.6.2.7', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSHealthcareProfessionalRoles which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.1).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.1']/Entry[@code = current()/@code]/@displayName = current()/@displayName )">Success: R2.2 The Prescriber Profession element
      "functionCode" is valid.</report>
      </rule>
      <!--R2.2 The Prescriber Facility Country (Data Validation)-->
    <rule context="//cda:section[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/cda:author/cda:assignedAuthor/cda:representedOrganization/cda:addr/cda:country">
         <assert test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Error: R2.2 The Prescriber Facility Country element "country = 
      <value-of select="current()"/>" is not valid. To see valid "country" please cf.
      <value-of select="$usedDataDoc"/>(epSOSCountry which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.4).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Success: R2.2 The Prescriber Facility Country element "country" is valid.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.2-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
    <!-- R4.6 Pharmaceutical dose form  (Data Validation)-->
    <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R4.6 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Pharmaceutical dose form element "formCode" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '1.3.6.1.4.1.12559.11.10.1.3.1.44.1', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSDoseForm which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.2).</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R4.6 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Pharmaceutical dose form element "formCode" is valid.</report>
      </rule>
      <!-- R4.7 Route of Administration  (Data Validation)-->
    <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:routeCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.12']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.12']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R4.7 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Route of Administration element "routeCode" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '1.3.6.1.4.1.12559.11.10.1.3.1.44.1', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSRoutesofAdministration which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.12).</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.12']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.12']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R4.7 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Route of Administration element "routeCode" is valid.</report>
      </rule>
      <!--R4.9 Number of units per intake  (Data Validation)-->
    <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:doseQuantity/cda:low">
         <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.6.8']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.16']/Entry/@code = current()/@unit) or @nullFlavor or ($pivotcodes//CodeSystem[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.44.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@unit) or @unit='1'">
      Error: R4.9 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Number of units per intake attribute low/@unit = 
      <value-of select="current()/@unit"/>, is not valid. The unit attribute shall be derived from the Value Sets epSOSUnits, 1.3.6.1.4.1.12559.11.10.1.3.1.42.16 based on the UCUM code system, otherwise it shall the value '1'. The countable units
      attribute shall be derived from the value set epSOSDoseForm, OID 1.3.6.1.4.1.12559.11.10.1.3.1.42.2. To see valid '@unit' please cf.
      <value-of select="$usedDataDoc"/>.</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.6.8']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.16']/Entry/@code = current()/@unit) or @nullFlavor or ($pivotcodes//CodeSystem[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.44.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@unit) or @unit='1'">
      Success: R4.9 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Number of units per intake attribute "low/@unit" is valid.</report>
      </rule>
      <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:doseQuantity/cda:high">
         <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.6.8']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.16']/Entry/@code = current()/@unit) or @nullFlavor or ($pivotcodes//CodeSystem[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.44.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@unit) or @unit='1'">
      Error: R4.9 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Number of units per intake attribute high/@unit = 
      <value-of select="current()/@unit"/>, is not valid. The unit attribute shall be derived from the Value Sets epSOSUnits, 1.3.6.1.4.1.12559.11.10.1.3.1.42.16 based on the UCUM code system, otherwise it shall the value '1'. The countable units
      attribute shall be derived from the value set epSOSDoseForm, OID 1.3.6.1.4.1.12559.11.10.1.3.1.42.2. To see valid '@unit' please cf.
      <value-of select="$usedDataDoc"/>.</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.6.8']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.16']/Entry/@code = current()/@unit) or @nullFlavor or ($pivotcodes//CodeSystem[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.44.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@unit) or @unit='1'">
      Success: R4.9 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Number of units per intake attribute "high/@unit" is valid.</report>
      </rule>
      <!-- R4.10 Frequency of intake  (Data Validation)-->
    <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:effectiveTime[2][matches(normalize-space(@xsi:type),'EIVL_TS$')]/cda:event">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.41']/Entry/@code = current()/@code) or @nullFlavor">Error: R4.10 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) Frequency of intake element
      "event" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>,or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute 'codeSystem' shall be '2.16.840.1.113883.5.139', to see valid '@code' please cf.
      <value-of select="$usedDataDoc"/>(HL7:TimingEvent which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.41) (HL7:TimingEvent which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.41).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.41']/Entry/@code = current()/@code) or @nullFlavor">Success: R4.10 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) Frequency of intake
      element "event" is valid.</report>
      </rule>
      <!-- R4.3 Active Ingredient  (Data Validation)-->
    <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:ingredient[@classCode='ACTI']/epsos:ingredient/epsos:code">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.24']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.24']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R4.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Active Ingredient element "code" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '2.16.840.1.113883.6.73', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSActiveIngredient which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.24).</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.24']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.24']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R4.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Active Ingredient element "code" is valid.</report>
      </rule>
      <!-- R4.5 Medicinal product package  (Data Validation)-->
    <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:consumable/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.3']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.3']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Medicinal product package element "formCode" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '1.3.6.1.4.1.12559.11.10.1.3.1.44.1', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSPackage which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.3)</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.3']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.3']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Medicinal product package element "formCode" is valid.</report>
      </rule>
      <rule context="//cda:entry/cda:substanceAdministration[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']/cda:entryRelationship[@typeCode='SUBJ'][@inversionInd='true']/cda:observation[@classCode='OBS'][cda:code/@code='SUBST' and cda:code/@codeSystem='2.16.840.1.113883.5.6']/cda:value">

      <!-- R4.16 Substitution (Optional)-->
      <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.7']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.7']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R4.16 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Substitution element "value" is not valid. one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '2.16.840.1.113883.5.1070', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSSubstitutionCode which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.7)</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.7']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.7']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R4.16 (1.3.6.1.4.1.12559.11.10.1.3.1.3.2) The Substitution element "value" is valid.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch">
      <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.1']"/>
      <!--  29. Medication Form Code (Data Validation)-->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.1']/cda:manufacturedMaterial/epsos:formCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: (1.3.6.1.4.1.12559.11.10.1.3.1.3.1) The Medication Form Code element "formCode" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '1.3.6.1.4.1.12559.11.10.1.3.1.44.1', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSDoseForm which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.2)</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: (1.3.6.1.4.1.12559.11.10.1.3.1.3.1) The Medication Form Code element "formCode" is valid.</report>
      </rule>
      <!--  30. Medication Packaging Form Code (Data Validation)-->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.1']/cda:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine/epsos:capacityQuantity">
         <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.6.8']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.16']/Entry/@code = current()/@unit) or @nullFlavor or normalize-space(@unit)= '1'">Error: (1.3.6.1.4.1.12559.11.10.1.3.1.3.1)
      The Medication Packaging element "capacityQuantity" is not valid, Its attributes '@unit = 
      <value-of select="current()/@unit"/>' is not valid. In the cases where the unit attribute is not 1, UCUM units shall be used. The value set is epSOSUnits, OID 1.3.6.1.4.1.12559.11.10.1.3.1.42.16, to see valid '@unit' please cf.
      <value-of select="$usedDataDoc"/>(epSOSUnits which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.16).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.6.8']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.16']/Entry/@code = current()/@unit) or @nullFlavor or normalize-space(@unit)= '1'">Success: (1.3.6.1.4.1.12559.11.10.1.3.1.3.1)
      The Medication Packaging element "capacityQuantity" is valid.</report>
      </rule>
      <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.1']/cda:manufacturedMaterial/cda:code">
      <!-- EP commented the following requirement http://gazelle.ihe.net/jira/browse/EPSOSSCH-57
      <assert test="((string-length(normalize-space(@code)) and @codeSystem = '2.16.840.1.113883.6.73')) or @nullFlavor or (not(@codeSystem) and not(@code))">
        Error: (1.3.6.1.4.1.12559.11.10.1.3.1.3.1) The Medication Code element "code" describes the medication. If coded, it must provide a code and codeSystem attribute using controled vocabulary for medications. ATC which oid = 2.16.840.1.113883.6.73 is the codeSystem used.
      </assert>
      <report test="((string-length(normalize-space(@code)) and @codeSystem = '2.16.840.1.113883.6.73')) or @nullFlavor or (not(@codeSystem) and not(@code))"> 
        Success: (1.3.6.1.4.1.12559.11.10.1.3.1.3.1) The Medication Code element "code" is fulfilled. 
      </report>
    -->
      <assert test="(string-length(normalize-space(@code))) or @nullFlavor or (not(@codeSystem) and not(@code))">Error: (1.3.6.1.4.1.12559.11.10.1.3.1.3.1) The Medication Code element "code" describes the medication. If coded, it must provide a code and
      codeSystem attribute using controled vocabulary for medications.</assert>
         <report test="(string-length(normalize-space(@code))) or @nullFlavor or (not(@codeSystem) and not(@code))">Success: (1.3.6.1.4.1.12559.11.10.1.3.1.3.1) The Medication Code element "code" is fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch"/>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch"/>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch"/>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch"/>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.11-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch"/>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch"/>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch"/>
  <!--Check notes -->
  <!--<xi:include parse="xml" href="templates/notes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch"
        xpointer="xmlns(x=http://purl.oclc.org/dsdl/schematron) xpointer(//x:pattern)">
        <xi:fallback>
        <!-\- xi:include error : file not found :  templates/notes/1.3.6.1.4.1.12559.11.10.1.3.1.1.1.sch -\->
        </xi:fallback>
    </xi:include>-->
</schema>
