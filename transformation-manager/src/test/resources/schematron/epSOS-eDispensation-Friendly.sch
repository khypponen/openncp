<?xml version="1.0" encoding="UTF-8"?>
<!-- Revision = $Revision: 21223 $ --><schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3"
        xmlns:iso="http://purl.oclc.org/dsdl/schematron"
        xmlns:sch="http://www.ascc.net/xml/schematron"
        xmlns:xi="http://www.w3.org/2003/XInclude"
        queryBinding="xslt2"
        defaultPhase="no-codes">
  <title>Schematron schema for validating epSOS eDispensation Document</title>
  <ns prefix="cda" uri="urn:hl7-org:v3"/>
  <ns prefix="epsos" uri="urn:epsos-org:ep:medication"/>
  <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
  <!--Phases -->
  <phase id="all">
      <active pattern="variables"/>
      <active pattern="FileCheck-codes"/>
      <active pattern="common-cda-components"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.2-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4.eD-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eD-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eD-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.2-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.2-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eD-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eD-codes"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.2-codes"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.3-codes"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-codes"/>
   </phase>
  <phase id="no-codes">
      <active pattern="variables"/>
      <active pattern="common-cda-components"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.2-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4.eD-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eD-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eD-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.2-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-errors"/>
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
  <!--Check common errors -->
  <pattern id="common-cda-components" xml:base="templates/errors/common.sch">
      <rule context="//child::reference//@value">
         <assert test="//child::text//@id=replace(@value,'#', '')">CANNOT FIND THE LINK</assert>
      </rule>
  </pattern>
  <!--Check errors -->
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.2-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
      <title>epSOS eDispensation Document Specification - errors validation phase</title>
      <rule context="/cda:ClinicalDocument">
      <!-- Verify that the template id is eDispensation Document -->
      <assert test="//cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.1.2&#34;]">Error: R3 The eDispensation Document requires the eDispensation Document template ID (1.3.6.1.4.1.12559.11.10.1.3.1.1.2).</assert>
         <report test="//cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.1.2&#34;]">Success: R3 The eDispensation Document correctly includes the eDispensation Document template ID (1.3.6.1.4.1.12559.11.10.1.3.1.1.2).</report>
         <!-- Verify that the correct code system is used -->
      <assert test="cda:code[@code = &#34;60593-1&#34;] and cda:code[@displayName = 'eDispensation']">Error: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The eDispensation document type code element "@code = 
      <value-of select="cda:code/@code"/>", or "@displayName = 
      <value-of select="cda:code/@displayName"/>" are not valid, the attribute '@code' must be set to the value "60593-1" which is the code corresponding to "Medication dispensed" in the loinc code system. And '@displayName' must be set to the value
      "eDispensation".</assert>
         <report test="cda:code[@code = &#34;60593-1&#34;] and cda:code[@displayName = 'eDispensation']">Success: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The eDispensation document type code is fulfilled.</report>
         <assert test="cda:code[@codeSystem = &#34;2.16.840.1.113883.6.1&#34;]">Error: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The eDispensation document type code attribute "@codeSystem = 
      <value-of select="cda:code/@codeSystem"/>", is not valid, it must be set to the value "2.16.840.1.113883.6.1" corresponding to the LOINC Code system OID.</assert>
         <report test="cda:code[@codeSystem = &#34;2.16.840.1.113883.6.1&#34;]">Success: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The eDispensation document type code is correctly set to the value "2.16.840.1.113883.6.1" corresponding to the LOINC Code system OID.</report>
         <assert test="//cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.2.2&#34;]">Error: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The eDispensation Document requires the eDispensation Section name\template ID (1.3.6.1.4.1.12559.11.10.1.3.1.2.2).</assert>
         <report test="//cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.2.2&#34;]">Success: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The eDispensation Document correctly includes the eDispensation Section name\template ID (1.3.6.1.4.1.12559.11.10.1.3.1.2.2).</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.4.eD-errors"
            xml:base="templates/eDtemplates/errors/1.3.6.1.4.1.19376.1.5.3.1.2.4.eD.sch">
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
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eD-errors"
            xml:base="templates/eDtemplates/errors/1.3.6.1.4.1.19376.1.5.3.1.1.1.eD.sch">
      <title>IHE PCC Medical Documents Specification - errors validation phase</title>
      <!--epSOS-->
    <!--(cf.epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc : 8  Common Header Data Elements)-->
    <!-- R1.11.2 Date of last update-->
    <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:effectiveTime">
         <assert test="(cda:high and cda:high/@value and count(cda:high)&lt;2) or not(cda:high) or @nullFlavor">Error: R1.11.2 Date of last update is optional. If it's present, the attribute 'value' of the element "high" shall not be empty. The element
      "high" cannot be repeated (cardinality [1..1]).</assert>
         <report test="(cda:high and cda:high/@value and count(cda:high)&lt;2) or not(cda:high) or @nullFlavor">Success : R1.11.2 requirement is fulfilled</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:legalAuthenticator/cda:assignedEntity/cda:representedOrganization/cda:addr">
      <!-- R1.11.9 Legal Authenticator Facility Address-->
      <assert test="(((cda:streetAddressLine) or (cda:city) or (cda:state) or (cda:country)) and not(@nullFlavor)) or ( (@nullFlavor) and (not(cda:streetAddressLine) and not(cda:city) and not(cda:state) and not(cda:country)))">Error: R1.11.9 Legal
      Authenticator Facility Address element is required. If there is no information, the nullFlavor attribute shall be present and no address parts shall be present, otherwise there shall be no nullFlavor attribute, and at least one of the address parts
      listed below shall be present</assert>
         <!-- R1.11.9 Legal Authenticator Facility Street/Number of Street-->
      <assert test="(cda:streetAddressLine and count(cda:streetAddressLine)&lt;2 and (cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Error: R1.11.9 Legal Authenticator
      Facility Street/Number of Street is required (cf.R1.10.9.3.1). The element (streetAddressLine) shall not be empty.</assert>
         <report test="(cda:streetAddressLine and count(cda:streetAddressLine)&lt;2 and (cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Success : R1.11.9 Legal
      Authenticator Facility Street/Number of Street is fulfilled (cf.R1.10.9.3.1).</report>
         <!-- R1.11.9 Legal Authenticator Facility City-->
      <assert test="(cda:city and count(cda:city)&lt;2 and (cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Error: R1.11.9 Legal Authenticator Facility City is required (cf.R1.10.9.3.2). The
      element (city) shall not be empty.</assert>
         <report test="(cda:city and count(cda:city)&lt;2 and (cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Success : R1.11.9 Legal Authenticator Facility City is fulfilled
      (cf.R1.10.9.3.2).</report>
         <!-- R1.11.9 Legal Authenticator Facility Postal Code-->
      <assert test="(cda:postalCode and count(cda:postalCode)&lt;2 and (cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Error: R1.11.9 Legal Authenticator Facility Postal Code is
      required (cf.R1.10.9.3.3). The element (postalCode) shall not be empty.</assert>
         <report test="(cda:postalCode and count(cda:postalCode)&lt;2 and (cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Success : R1.11.9 Legal Authenticator Facility Postal Code is
      fulfilled (cf.R1.10.9.3.3).</report>
         <!-- R1.11.9 Legal Authenticator Facility State or Province-->
      <assert test="(cda:state and count(cda:state)&lt;2 and (cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0))) or @nullFlavor">Error: R1.11.9 Legal Authenticator Facility State or Province is required
      (cf.R1.10.9.3.4). The element (state) shall not be empty.</assert>
         <report test="(cda:state and count(cda:state)&lt;2 and (cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0))) or @nullFlavor">Success : R1.11.9 Legal Authenticator Facility State or Province is fulfilled
      (cf.R1.10.9.3.4).</report>
         <!-- R1.11.9 Legal Authenticator Facility Country-->
      <assert test="(cda:country and count(cda:country)&lt;2 and (cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Error: R1.11.9 Legal Authenticator Facility Country is required
      (cf.R1.10.9.3.5). The element (country) shall not be empty.</assert>
         <report test="(cda:country and count(cda:country)&lt;2 and (cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Success: R1.11.9 Legal Authenticator Facility Country is present
      (cf.R1.10.9.3.5).</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:legalAuthenticator/cda:assignedEntity/cda:representedOrganization/cda:telecom">
      <!-- R1.11.9 Legal Authenticator Facility Telecommunication-->
      <assert test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Error: R1.11.9 Legal Authenticator Facility telephone number(cf.R1.10.9.4.1) or the Legal Authenticator Facility e-mail address (cf.R1.10.9.4.2) is required. If there is no information, the nullFlavor attribute shall have a value of 'NI' and the
      "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute shall not be present, and the "value" and "use" attributes shall be present and shall not be empty. The e-mail address shall be like :mailto:example@exp.com and the
      telephone number shall be like : "tel:+13176307960" or "tel:+1(317)630-7960". The attribute 'use' shall not be empty.</assert>
         <report test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Success : R1.11.9 Legal Authenticator Facility telephone number(cf.R1.10.9.4.1) or the Legal Authenticator Facility e-mail address (cf.R1.10.9.4.2) are fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eD-errors"
            xml:base="templates/eDtemplates/errors/1.3.6.1.4.1.19376.1.5.3.1.2.3.eD.sch">
    <!--epSOS-->
    <!--(cf.epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc : 8  Common Header Data Elements)-->
    <rule context="/cda:ClinicalDocument/cda:author/cda:assignedAuthor">
      <!-- R1.10.6 HCP Profession (Health Care Professional's Specialty) -->
      <assert test="(parent::node()/cda:functionCode/@code and parent::node()/cda:functionCode[@codeSystem='2.16.840.1.113883.2.9.6.2.7']) or (@nullFlavor) or (parent::node() and not (parent::node()/cda:functionCode))">Error: R1.10.6 HCP Profession is
      Optional. If the element (functionCode) is present in "ClinicalDocument\author\". The attributes (functionCode\@code) and (functionCode\@codeSystem) shall not be empty and the @codeSystem shall be "2.16.840.1.113883.2.9.6.2.7".</assert>
         <report test="parent::node()/cda:functionCode/@code">Success: R1.10.6 HCP Profession is present.</report>
         <!-- R1.10.9.1 Healthcare Facility's name -->
      <assert test="(cda:representedOrganization/cda:name and ((every $i in cda:representedOrganization/cda:name satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:representedOrganization/cda:name satisfies count(($i))&lt; 2)) or (cda:representedOrganization/@nullFlavor) or (@nullFlavor) or (not(cda:representedOrganization))">
      Error: R1.10.9.1 Healthcare Facility's name is required. The element (name) shall not be empty and cannot be repeated (cardinality [1..1]). The element (name) shall be present in
      "ClinicalDocument\author\assignedAuthor\representedOrganization".</assert>
         <report test="cda:representedOrganization/cda:name">Success: R1.10.9.1 Healthcare Facility's name is present.</report>
         <!-- R1.10.9.2 Healthcare Facility's identifier -->
      <assert test="(cda:representedOrganization/cda:id and (every $i in cda:representedOrganization/cda:id satisfies count(($i))&lt; 2)) or (cda:representedOrganization/@nullFlavor) or (@nullFlavor) or (not(cda:representedOrganization))">Error:
      R1.10.9.2 Healthcare Facility's identifier is required. The element (id) cannot be repeated (cardinality [1..1]). The element (id) shall be present in "ClinicalDocument\author\assignedAuthor\representedOrganization".</assert>
         <report test="cda:representedOrganization/cda:id">Success: R1.10.9.2 Healthcare Facility's identifier is present.</report>
         <!-- R1.10.9.3.1 Healthcare Facility's Street -->
      <assert test="(cda:representedOrganization/cda:addr/cda:streetAddressLine and ((every $i in cda:representedOrganization/cda:addr/cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:representedOrganization/cda:addr/cda:streetAddressLine satisfies count(($i))&lt; 2)) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:representedOrganization)) or (cda:representedOrganization/cda:addr/cda:streetAddressLine/@nullFlavor)">
      Error: R1.10.9.3.1 Healthcare Facility's Street is required event if null flavored. The element (streetAddressLine) shall be null flavored or not empty and cannot be repeated (cardinality [1..1]). The element (streetAddressLine) shall be present in
      "ClinicalDocument\author\assignedAuthor\representedOrganization\addr".</assert>
         <report test="cda:representedOrganization/cda:addr/cda:streetAddressLine">Success: R1.10.9.3.1 Healthcare Facility's Street is present.</report>
         <!-- R1.10.9.3.2 Healthcare Facility's City -->
      <assert test="(cda:representedOrganization/cda:addr/cda:city and ((every $i in cda:representedOrganization/cda:addr/cda:city satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:representedOrganization/cda:addr/cda:city satisfies count(($i))&lt; 2)) or (cda:representedOrganization/cda:addr/cda:city/@nullFlavor) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:representedOrganization))">
      Error: R1.10.9.3.2 Healthcare Facility's City is required. The element (city) shall not be empty or shall be null flavored and cannot be repeated (cardinality [1..1]). The element (city) shall be present in
      "ClinicalDocument\author\assignedAuthor\representedOrganization\addr".</assert>
         <report test="cda:representedOrganization/cda:addr/cda:city">Success: R1.10.9.3.2 Healthcare Facility's City is present.</report>
         <!-- R1.10.9.3.3 Healthcare Facility's State or Province -->
      <assert test="(cda:representedOrganization/cda:addr/cda:state and ((every $i in cda:representedOrganization/cda:addr/cda:state satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:representedOrganization/cda:addr/cda:state satisfies count(($i))&lt; 2)) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:representedOrganization)) or (cda:representedOrganization/cda:addr/cda:state/@nullFlavor)">
      Error: R1.10.9.3.3 Healthcare Facility's State or Province is required. The element (state) shall not be empty or shall be null flavored and cannot be repeated (cardinality [1..1]). The element (state) shall be present in
      "ClinicalDocument\author\assignedAuthor\representedOrganization\addr".</assert>
         <report test="cda:representedOrganization/cda:addr/cda:state">Success: R1.10.9.3.3 Healthcare Facility's State or Province is present.</report>
         <!-- R1.10.9.3.4 Healthcare Facility's Zip or Postal Code -->
      <assert test="(cda:representedOrganization/cda:addr/cda:postalCode and ((every $i in cda:representedOrganization/cda:addr/cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:representedOrganization/cda:addr/cda:postalCode satisfies count(($i))&lt; 2)) or (cda:representedOrganization/cda:addr/cda:postalCode/@nullFlavor) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:representedOrganization))">
      Error: R1.10.9.3.4 Healthcare Facility's Zip or Postal Code is required. The element (postalCode) shall not be empty or shall be null flavored and cannot be repeated (cardinality [1..1]). The element (postalCode) shall be present in
      "ClinicalDocument\author\assignedAuthor\representedOrganization\addr".</assert>
         <report test="cda:representedOrganization/cda:addr/cda:postalCode">Success: R1.10.9.3.4 Healthcare Facility's Zip or Postal Code is present.</report>
         <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test="(cda:representedOrganization/cda:telecom) or (@nullFlavor) or (cda:representedOrganization/@nullFlavor) or (not(cda:representedOrganization))">Error: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is required. The
      attributes (telecom\@value) and (telecom\@use) shall not be empty and so the Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient Contact's telephone number shall be like : "tel:+13176307960" or
      "tel:+1(317)630-7960.</assert>
         <report test="cda:representedOrganization/cda:telecom">Success: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is present.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer">
      <!-- R1.10.6 HCP Profession (Health Care Professional's Specialty) -->
      <assert test="(cda:functionCode/@code and cda:functionCode[@codeSystem='2.16.840.1.113883.2.9.6.2.7']) or @nullFlavor or (not(cda:functionCode))">Error: R1.10.6 HCP Profession is Optional. If the element (functionCode) is present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\". The attributes (functionCode\@code) and (functionCode\@codeSystem) shall not be empty and the @codeSystem shall be "2.16.840.1.113883.2.9.6.2.7".</assert>
         <report test="cda:functionCode/@code">Success: R1.10.6 HCP Profession is present.</report>
         <!-- R1.10.9.1 Healthcare Facility's name -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:name and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:name satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:name satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">
      Error: R1.10.9.1 Healthcare Facility's name is required. The element (name) shall not be empty and cannot be repeated (cardinality [1..1]). The element (name) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:name">Success: R1.10.9.1 Healthcare Facility's name is present.</report>
         <!-- R1.10.9.2 Healthcare Facility's identifier -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:id and (every $i in cda:assignedEntity/cda:representedOrganization/cda:id satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">
      Error: R1.10.9.2 Healthcare Facility's identifier is required. The element (id) cannot be repeated (cardinality [1..1]). The element (id) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:id">Success: R1.10.9.2 Healthcare Facility's identifier is present.</report>
         <!-- R1.10.9.3.1 Healthcare Facility's Street -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">
      Error: R1.10.9.3.1 Healthcare Facility's Street is required. The element (streetAddressLine) shall not be empty and cannot be repeated (cardinality [1..1]). The element (streetAddressLine) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization\addr".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:addr/cda:streetAddressLine">Success: R1.10.9.3.1 Healthcare Facility's Street is present.</report>
         <!-- R1.10.9.3.2 Healthcare Facility's City -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">
      Error: R1.10.9.3.2 Healthcare Facility's City is required. The element (city) shall not be empty and cannot be repeated (cardinality [1..1]). The element (city) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization\addr".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:addr/cda:city">Success: R1.10.9.3.2 Healthcare Facility's City is present.</report>
         <!-- R1.10.9.3.3 Healthcare Facility's State or Province -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">
      Error: R1.10.9.3.3 Healthcare Facility's State or Province is required. The element (state) shall not be empty and cannot be repeated (cardinality [1..1]). The element (state) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization\addr".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:addr/cda:state">Success: R1.10.9.3.2 Healthcare Facility's State or Province is present.</report>
         <!-- R1.10.9.3.4 Healthcare Facility's Zip or Postal Code -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode and ((every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) and (every $i in cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode satisfies count(($i))&lt; 2)) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/cda:addr/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">
      Error: R1.10.9.3.4 Healthcare Facility's Zip or Postal Code is required. The element (postalCode) shall not be empty and cannot be repeated (cardinality [1..1]). The element (postalCode) shall be present in
      "\ClinicalDocument\documentationOf\serviceEvent\performer\assignedEntity\representedOrganization\addr".</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:addr/cda:postalCode">Success: R1.10.9.3.2 Healthcare Facility's postalCode is present.</report>
         <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test="(cda:assignedEntity/cda:representedOrganization/cda:telecom) or (cda:assignedEntity/@nullFlavor) or (cda:assignedEntity/cda:representedOrganization/@nullFlavor) or (not(cda:assignedEntity/cda:representedOrganization))">Error:
      R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is required. The attributes (telecom\@value) and (telecom\@use) shall not be empty and so the Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient
      Contact's telephone number shall be like : "tel:+13176307960" or "tel:+1(317)630-7960.</assert>
         <report test="cda:assignedEntity/cda:representedOrganization/cda:telecom">Success: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is present.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:author/cda:assignedAuthor/cda:representedOrganization/cda:telecom">
      <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Error: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is required. If there is no information, the nullFlavor attribute shall have a value of 'NI' and the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute
      shall not be present, and the "value" and "use" attributes shall be present and shall not be empty. The Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient Contact's telephone number shall be like :
      "tel:+13176307960" or "tel:+1(317)630-7960. The attribute 'use' shall not be empty.</assert>
         <report test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Success: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is fulfilled.</report>
      </rule>
      <rule context="/cda:ClinicalDocument/cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:representedOrganization/cda:telecom">
      <!-- R1.10.9.4.1 HCP Telecom telephone/mail address -->
      <assert test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Error: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is required. If there is no information, the nullFlavor attribute shall have a value of 'NI' and the "value" and "use" attributes shall be omitted, otherwise the nullFlavor attribute
      shall not be present, and the "value" and "use" attributes shall be present and shall not be empty. The Patient Contact's e-mail address shall be like :mailto:example@exp.com and the Patient Contact's telephone number shall be like :
      "tel:+13176307960" or "tel:+1(317)630-7960. The attribute 'use' shall not be empty.</assert>
         <report test=" (matches(@value,'tel:\+[0-9/(/)/-]') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0) ) or (matches (@value,'^mailto:([a-zA-Z0-9]+(([\.\-_]?[a-zA-Z0-9]+)+)?)@(([a-zA-Z0-9]+[\.\-_])+[a-zA-Z]{2,4})$') and (every $i in @use satisfies string-length(normalize-space($i)) &gt; 0)) or (@nullFlavor='NI' and not(@value) and not(@use))">
      Success: R1.10.9.4.1/R1.10.9.4.2 HCP Telecom telephone/mail address is fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.2-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
    <!--  <title>epSOS Dispensation Section Specification - errors validation phase</title> -->
    <rule context="//cda:section[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.2']">
      <!-- Verify the document type code -->
      <assert test="cda:code[@code = '60590-7'] and cda:code[@displayName = 'Medication dispensed']">Error: (cf.Â§12.1.3 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The Dispensation Section type code of a eDispensation Document
      attributes "@code = 
      <value-of select="cda:code/@code"/>", or "@displayName = 
      <value-of select="cda:code/@displayName"/>" are not valid, the attribute '@code' must be set to the value "60590-7" which is the code corresponding to "Medication dispensed" in the loinc code system. And '@displayName' must be set to the value
      "Medication dispensed".</assert>
         <!-- Verify that the correct code system is used-->
      <assert test="cda:code[@codeSystem = '2.16.840.1.113883.6.1']">Error: (cf.Â§12.1.3 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The document type code of a Medical Document attribute "@codeSystem = 
      <value-of select="cda:code/@codeSystem"/>", is not valid, it must be "2.16.840.1.113883.6.1" corresponding to the LOINC Code system OID.</assert>
         <!-- Verify that the parents templateId are present. -->
      <assert test="cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.8&#34;]">Error: (cf.Â§12.1.3 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier (2.16.840.1.113883.10.20.1.8) for Dispensation
      Section shall be present.</assert>
         <report test="cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.8&#34;]">Success: (cf.Â§12.1.3 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier (2.16.840.1.113883.10.20.1.8) for Dispensation
      Section is present.</report>
         <!-- Verify that the Dispensation Item Entry Content Module templateId is present. -->
      <assert test="cda:entry/cda:supply/cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.3.3&#34;]">Error: (cf.Â§12.1.3 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The Dispensation Item Entry Content Module identifier
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) for Dispensation Section shall be present.</assert>
         <report test="cda:entry/cda:supply/cda:templateId[@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.3.3&#34;]">Success: (cf.Â§12.1.3 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The Dispensation Item Entry Content Module identifier
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) for Dispensation Section is present.</report>
         <!--R2.1 Dispensation ID -->
      <assert test="cda:id and count(cda:id)&lt;2">Error: R2.1 The Dispensation ID element is required. The element "id" cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:id">Success: R2.1 The Dispensation ID element is present.</report>
         <report test="count(cda:id)&lt;2">Success: R2.1 The Dispensation ID element cannot be repeated (cardinality [1..1]).</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.3-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
    <!--  <title>epSOS Dispensed Medicine Entry Content Module Specification - errors validation phase</title> -->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']">
         <assert test="self::cda:supply[@classCode=&#34;SPLY&#34;]">Error: (cf.Â§12.1.3.4 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The "supply" element shall be present. The classCode attribute shall be 'SPLY'.</assert>
         <assert test="self::cda:supply[@moodCode=&#34;EVN&#34;] or self::cda:supply[@moodCode=&#34;INT&#34;]">Error: (cf.Â§12.1.3.4 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The "supply" element shall be present. The moodCode
      attribute shall be INT to re-flect that a medication has been prescribed, or EVN to indicate that the prescription has been filled.</assert>
         <!-- Verify that the parents templateId are present. -->
      <assert test="cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]">Error: (cf.Â§12.1.3.4 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier (2.16.840.1.113883.10.20.1.34) for Dispensed
      Medicine Entry Content Module shall be present.</assert>
         <assert test="cda:templateId[@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.7.3&#34;]">Error: (cf.Â§12.1.3.4 epSOS_D3.9.1_Appendix_B1_Implementation_v0.9_20100924.doc) The parent template identifier (1.3.6.1.4.1.19376.1.5.3.1.4.7.3) for
      Dispensed Medicine Entry Content Module shall be present.</assert>
         <!-- §11 Body Data Element -->
      <assert test="(cda:performer and count(cda:performer)&lt;2) or @nullFlavor">Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser element is required. The element "performer" shall be present in "\entry\supply\". The element "performer"
      shall not be repeated (cardinality [1..1]).</assert>
         <report test="cda:performer">Success: R3.1 The Dispenser element is required. The element "performer" is present.</report>
         <report test="count(cda:performer)&lt;2">Success: R3.1 The Dispenser element is required. The element "performer" is not repeated (cardinality [1..1]).</report>
         <assert test="cda:performer/cda:assignedEntity or cda:performer/@nullFlavor or not(cda:performer)">Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser "assignedEntity" element is required. The element "assignedEntity" shall be present in
      "performer\".</assert>
         <assert test="cda:performer/cda:assignedEntity/cda:assignedPerson or cda:performer/cda:assignedEntity/@nullFlavor or not(cda:performer/cda:assignedEntity)">Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser "assignedPerson" element is
      required. The element "assignedPerson" shall be present in "performer\assignedEntity\".</assert>
         <report test="cda:performer/cda:assignedEntity/cda:assignedPerson">Success: R3.1 The Dispenser "assignedPerson" element is required. The element "assignedPerson" is present in "performer\assignedEntity\".</report>
         <assert test="cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name or cda:performer/cda:assignedEntity/cda:assignedPerson/@nullFlavor or not(cda:performer/cda:assignedEntity/cda:assignedPerson)">Error: R3.1
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser "name" element is required. The element "name" shall be present in "performer\assignedEntity\assignedPerson".</assert>
         <!-- R3.1 Family Name/Surname -->
      <assert test="(cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name/cda:family and (every $i in cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name/cda:family satisfies string-length(normalize-space($i)) &gt; 0)) or cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name/@nullFlavor or not(cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name)">
      Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser Family Name/Surname element is required. The element "family" shall not be empty.</assert>
         <!-- R3.1 Given Name -->
      <assert test="(cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name/cda:given and (every $i in cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name/cda:given satisfies string-length(normalize-space($i)) &gt; 0)) or cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name/@nullFlavor or not(cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name)">
      Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser Given Name element is required. The element "given" shall not be empty.</assert>
         <!-- R3.1 ID number -->
      <assert test="(cda:performer/cda:assignedEntity/cda:id) or cda:performer/cda:assignedEntity/@nullFlavor or not(cda:performer/cda:assignedEntity/cda:assignedPerson)">Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) ID number element is
      required.</assert>
         <!-- R3.1 Dispenser Facility -->
      <assert test="cda:performer/cda:assignedEntity/cda:representedOrganization or cda:performer/cda:assignedEntity/@nullFlavor or not(cda:performer/cda:assignedEntity)">Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser
      "representedOrganization" element is required. The element "representedOrganization" shall be present in "performer\assignedEntity\".</assert>
         <report test="cda:performer/cda:assignedEntity/cda:representedOrganization">Success: R3.1 The Dispenser "representedOrganization" element is required. The element "representedOrganization" is present in "performer\assignedEntity\".</report>
         <!-- R3.1 Dispenser Facility Name  -->
      <assert test="(cda:performer/cda:assignedEntity/cda:representedOrganization/cda:name and (every $i in cda:performer/cda:assignedEntity/cda:representedOrganization/cda:name satisfies count(($i))&lt; 2) and (every $i in cda:performer/cda:assignedEntity/cda:representedOrganization/cda:name satisfies string-length(normalize-space($i)) &gt; 0)) or cda:performer/cda:assignedEntity/cda:representedOrganization/@nullFlavor or not(cda:performer/cda:assignedEntity/cda:representedOrganization)">
      Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser Facility Name "name" element is required. The element "name" shall be present in "performer\assignedEntity\representedOrganization\".</assert>
         <!-- R3.1 Dispenser Facility Identifier  -->
      <assert test="(cda:performer/cda:assignedEntity/cda:representedOrganization/cda:id and (every $i in cda:performer/cda:assignedEntity/cda:representedOrganization/cda:id satisfies count(($i))&lt; 2)) or cda:performer/cda:assignedEntity/cda:representedOrganization/@nullFlavor or not(cda:performer/cda:assignedEntity/cda:representedOrganization)">
      Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser Facility Identifier "id" element is required. The element "id" shall be present in "performer\assignedEntity\representedOrganization\".</assert>
         <!-- R3.1 Dispenser Facility Address  -->
      <assert test="(cda:performer/cda:assignedEntity/cda:representedOrganization/cda:addr and (every $i in cda:performer/cda:assignedEntity/cda:representedOrganization/cda:addr satisfies count(($i))&lt; 2)) or cda:performer/cda:assignedEntity/cda:representedOrganization/@nullFlavor or not(cda:performer/cda:assignedEntity/cda:representedOrganization)">
      Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser Facility Address "addr" element is required. The element "addr" shall be present in "performer\assignedEntity\representedOrganization\".</assert>
         <!-- R3.2.3 Dispensed Medicine Id -->
      <assert test="cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:id">Error: R3.2.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispensed Medicine Id "id" element is required.</assert>
         <report test="cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:id">Success: R3.2.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispensed Medicine Id "id" element is present.</report>
         <!-- R4.5 Medicinal product package -->
      <assert test="cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode">Error: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The "formCode" element is required even if null
      flavored.</assert>
         <report test="cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode">Success: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The "formCode" element is present.</report>
         <!-- R4.6 Pharmaceutical dose form -->
      <assert test="cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode and (every $i in cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode satisfies count(($i))&lt; 2) and cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode/@code and cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1']">
      Error: R4.6 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Pharmaceutical dose form element "formCode" is required. The attributes 'code' and 'codeSystem' of the element "formCode" shall be filled and the attribute 'codeSystem' shall be
      '1.3.6.1.4.1.12559.11.10.1.3.1.44.1'. The element "formCode" shall have a cardinality [1..1].</assert>
         <report test="cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode and (every $i in cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode satisfies count(($i))&lt; 2)">Success: R4.6
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Pharmaceutical dose form element "formCode" is present and do have a cardinality [1..1].</report>
         <report test="cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode/@code and cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1']">Success: R4.6
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The attributes 'code' and 'codeSystem' of the Pharmaceutical dose form element "formCode" are filled and the 'codeSystem' is correctly set to the value '1.3.6.1.4.1.12559.11.10.1.3.1.44.1' .</report>
         <!-- R4.7 Route of Administration (Optional) -->
      <assert test="(cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration/cda:routeCode and cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration/cda:routeCode/@code and cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration/cda:routeCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1']) or (not(cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration/cda:routeCode) or cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration/cda:routeCode/@nullFlavor)">
      Error: R4.7 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Route of Administration element "routeCode" is Optional. If present, the attributes 'code' and 'codeSystem' of the element "formCode" shall be filled and the attribute 'codeSystem' shall be
      '1.3.6.1.4.1.12559.11.10.1.3.1.44.1'. Otherwise the nullFlavor attribute shall be present.</assert>
         <!-- R4.8 Number of packages -->
      <assert test="cda:quantity and cda:quantity/@value and cda:quantity/@unit">Error: R4.8 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Number of packages element "quantity" is required. The attributes 'value' and 'unit' of the element "quantity" shall be
      filled.</assert>
         <report test="cda:quantity">Success: R4.8 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Number of packages element "quantity" is present.</report>
         <report test="cda:quantity/@value and cda:quantity/@unit">Success: R4.8 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The attributes 'value' and 'unit' of the Number of packages element "quantity" are filled.</report>
         <!--R2.2 related prescription -->
      <assert test="cda:entryRelationship and cda:entryRelationship[@typeCode='REFR']">Error: R2.2 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The related prescription element "entryRelationship" is required. The related prescription item is represented via an
      entry relationship of type code "REFR", and containing a prescription entry as described in section 2.4.3.4. The prescription item ID and the prescription item author are required within the Dispensed Medication Entry.</assert>
         <report test="cda:entryRelationship and cda:entryRelationship[@typeCode='REFR']">Success: R2.2 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The related prescription element "entryRelationship" is present. The related prescription item is represented via an
      entry relationship of type code "REFR", and containing a prescription entry as described in section 2.4.3.4. The prescription item ID and the prescription item author are required within the Dispensed Medication Entry..</report>
         <assert test="cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration">Error: R2.2 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The related prescription element "entryRelationship/substanceAdministration" is required.</assert>
      </rule>
      <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:ingredient[@classCode='ACTI']">
      <!-- R4.4 Strength of the medicinal product -->
      <assert test="epsos:quantity and count(epsos:quantity)&lt;2">Error: R4.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Strength of the medicinal product element "quantity" is required. The element "quantity" shall have a cardinality [1..1].</assert>
         <report test="epsos:quantity">Success: R4.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Strength of the medicinal product element "quantity" is present.</report>
         <report test="count(epsos:quantity)&lt;2">Success: R4.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Strength of the medicinal product element "quantity" do have a cardinality [1..1].</report>
      </rule>
      <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:product/cda:manufacturedProduct/cda:manufacturedMaterial">
      <!-- R4.5 Medicinal product package -->
      <assert test="epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode and (every $i in epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode satisfies count(($i))&lt; 2) and epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode/@code and epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1']">
      Error: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Medicinal product package element "formCode" is required. The attributes 'code' and 'codeSystem' of the element "formCode" shall be filled and the attribute 'codeSystem' shall be
      '1.3.6.1.4.1.12559.11.10.1.3.1.44.1'. The element "formCode" shall have a cardinality [1..1].</assert>
         <report test="epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode and (every $i in epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode satisfies count(($i))&lt; 2)">Success: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The
      Medicinal product package element "formCode" is present and do have a cardinality [1..1].</report>
         <report test="epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode/@code and epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode[@codeSystem='1.3.6.1.4.1.12559.11.10.1.3.1.44.1']">Success: R4.5
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The attributes 'code' and 'codeSystem' of the Medicinal product package element "formCode" are filled and the 'codeSystem' is correctly set to the value '1.3.6.1.4.1.12559.11.10.1.3.1.44.1'.</report>
         <!-- R4.8.1 package size -->
      <assert test="epsos:asContent/epsos:containerPackagedMedicine/epsos:capacityQuantity">Error: R4.8.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The package size element "capacityQuantity" is required.</assert>
         <report test="epsos:asContent/epsos:containerPackagedMedicine/epsos:capacityQuantity">Success: R4.8.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The package size element "capacityQuantity" is present.</report>
      </rule>
      <!-- R3.1 Dispenser Facility Address  -->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:performer/cda:assignedEntity/cda:representedOrganization/cda:addr">
      <!-- R3.1 Dispenser Facility Street  -->
      <assert test="(cda:streetAddressLine and count(cda:streetAddressLine)&lt;2 and (cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0))) or @nullFlavor">Error: R3.1
      (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser Facility Street "streetAddressLine" element is optional. If present, the element "streetAddressLine" shall not be empty and cannot be repeated (cardinality [1..1])</assert>
         <!-- R3.1 Dispenser Facility City  -->
      <assert test="(cda:city and count(cda:city)&lt;2 and (cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0))) or @nullFlavor">Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser Facility City "city"
      element is optional. If present, the element "city" shall not be empty" and cannot be repeated (cardinality [1..1])</assert>
         <!-- R3.1 Dispenser Facility State or Province  -->
      <assert test="(cda:state and count(cda:state)&lt;2 and (cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0))) or @nullFlavor">Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser Facility State or
      Province "state" element is required. The element "state" shall not be empty and cannot be repeated (cardinality [1..1])</assert>
         <!-- R3.1 Dispenser Facility Postal Code  -->
      <assert test="(cda:postalCode and count(cda:postalCode)&lt;2 and (cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0))) or @nullFlavor">Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser
      Facility Postal Code "postalCode" element is required. The element "postalCode" shall not be empty and cannot be repeated (cardinality [1..1])</assert>
         <!-- R3.1 Dispenser Facility Country  -->
      <assert test="(cda:country and count(cda:country)&lt;2 and (cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0))) or @nullFlavor">Error: R3.1 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Dispenser Facility
      Country "country" element is required. The element "country" shall not be empty and cannot be repeated (cardinality [1..1])</assert>
      </rule>
      <!--TODO the requirement R4.16 has a typo on the spec v6 : there is no datatype, no codeSystem and the xpath is not complete-->
    <!--  <rule context="//cda:entry/cda:supply[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:entryRelationship[@typeCode='COMP']/cda:act[@classCode = 'ACT']cda:observation[@classCode='OBS']/cda:value">
       <!-\- R4.16 Substitution (Optional)-\->
       <assert test="@nullFlavor or (@xsi:type='BL' and (@value='false' or @value='true'))">
           Error: R4.16 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Substitution element "value" is optional. If present the attribute 'value' shall take the value 'true' or 'false' and 'xsi:type' shall be 'BL'. Otherwise the nullFlavor attribute shall be present.
       </assert> 
      <report test="@xsi:type='BL' and (@value='false' or @value='true')"> Success: R4.16 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Substitution element "value" is present. The attribute 'value' is correctly set to the value 'true' or 'false' and 'xsi:type' is correctly set to the value 'BL'. 
       </report>   
   </rule>-->
    <!-- R4.3 Active Ingredient (Optional)-->
    <rule context="//cda:entry/cda:supply[cda:templateId/@root=&#34;1.3.6.1.4.1.12559.11.10.1.3.1.3.3&#34;]/cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:ingredient[@classCode=&#34;ACTI&#34;]/epsos:ingredient/epsos:code">
         <assert test="@nullFlavor or (@code and @codeSystem='2.16.840.1.113883.6.73')">Error: R4.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) Active Ingredient element "code" is Optional. If present the attribute 'code' shall be filled and 'codeSystem' shall be
      '2.16.840.1.113883.6.73'. Otherwise the nullFlavor attribute shall be present.</assert>
         <report test="@code and @codeSystem='2.16.840.1.113883.6.73'">Success: R4.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) Active Ingredient element "code" is present. The attribute 'code' is well filled and 'codeSystem' is correctly set to the value
      '2.16.840.1.113883.6.73'.</report>
      </rule>
      <!--R2.2 Prescriber -->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration">
         <assert test="count(cda:author)&lt;2">Error: R2.2 The Prescriber element is optional. If present the element "author" shall not be repeated (cardinality [0..1]).</assert>
         <report test="count(cda:author)&lt;2">Success: R2.2 The Prescriber element is "author" is not repeated (cardinality [0..1]).</report>
         <assert test="cda:author/cda:assignedAuthor or cda:author/@nullFlavor or not(cda:author)">Error: R2.2 The Prescriber "assignedAuthor" element is required. The element "assignedAuthor" shall be present in "author\".</assert>
         <assert test="cda:author/cda:assignedAuthor/cda:assignedPerson or cda:author/cda:assignedAuthor/@nullFlavor or not(cda:author/cda:assignedAuthor)">Error: R2.2 The Prescriber "assignedPerson" element is required. The element "assignedPerson" shall
      be present in "author\assignedAuthor\".</assert>
         <assert test="cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name or cda:author/cda:assignedAuthor/cda:assignedPerson/@nullFlavor or not(cda:author/cda:assignedAuthor/cda:assignedPerson)">Error: R2.2 The Prescriber "name" element is required.
      The element "name" shall be present in "author\assignedAuthor\assignedPerson".</assert>
         <!-- R2.2 Prescriber Family Name/Surname -->
      <assert test="(cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/cda:family and (every $i in cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/cda:family satisfies string-length(normalize-space($i)) &gt; 0)) or cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/@nullFlavor or not(cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name)">
      Error: R2.2 The Prescriber Family Name/Surname element is required. The element (family) shall not be empty.</assert>
         <report test="cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/cda:family">Success: R2.2 The Prescriber Family Name/Surname element is present.</report>
         <!-- R2.2 Prescriber Given Name -->
      <assert test="(cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/cda:given and (every $i in cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/cda:given satisfies string-length(normalize-space($i)) &gt; 0)) or cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/@nullFlavor or not(cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name)">
      Error: R2.2 The Prescriber Given Name element is required. The element (given) shall not be empty.</assert>
         <report test="cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/cda:given">Success: R2.2 The Prescriber Given Name element is present.</report>
         <!-- R2.2 Prescriber ID number -->
      <assert test="(cda:author/cda:assignedAuthor/cda:id) or cda:author/cda:assignedAuthor/@nullFlavor or not(cda:author/cda:assignedAuthor/cda:assignedPerson)">Error: R2.2 The Prescriber ID number element is required.</assert>
         <report test="cda:author/cda:assignedAuthor/cda:id">Success: R2.2 The Prescriber ID number element is present.</report>
         <!-- R2.2 Prescriber Profession -->
      <assert test="(cda:author/cda:functionCode and cda:author/cda:functionCode[@codeSystem='2.16.840.1.113883.2.9.6.2.7'] and cda:author/cda:functionCode/@code ) or cda:author/@nullFlavor or not(cda:author/cda:assignedAuthor/cda:assignedPerson)">Error:
      R2.2 The Prescriber Profession element is required. The attributes (functionCode\@code) and (functionCode\@codeSystem) shall not be empty and the @codeSystem shall be "2.16.840.1.113883.2.9.6.2.7".</assert>
         <report test="cda:author/cda:functionCode">Success: R2.2 The Prescriber Profession element is present.</report>
         <!-- R2.2 Prescriber Facility -->
      <assert test="cda:author/cda:assignedAuthor/cda:representedOrganization or cda:author/cda:assignedAuthor/@nullFlavor or not(cda:author/cda:assignedAuthor)">Error: R2.2 The Prescriber "representedOrganization" element is required. The element
      "representedOrganization" shall be present in "author\assignedAuthor\".</assert>
         <!-- R2.2 Prescriber Facility Name  -->
      <assert test="(cda:author/cda:assignedAuthor/cda:representedOrganization/cda:name and (every $i in cda:author/cda:assignedAuthor/cda:representedOrganization/cda:name satisfies count(($i))&lt; 2) and ((every $i in cda:author/cda:assignedAuthor/cda:representedOrganization/cda:name satisfies string-length(normalize-space($i)) &gt; 0) or cda:author/cda:assignedAuthor/cda:representedOrganization/cda:name/@nullFlavor)) or cda:author/cda:assignedAuthor/cda:representedOrganization/@nullFlavor or not(cda:author/cda:assignedAuthor/cda:representedOrganization)">
      Error: R2.2 The Prescriber Facility Name "name" element is required. The element (name) shall not be empty. The element "name" shall be present in "author\assignedAuthor\representedOrganization\".</assert>
         <report test="cda:author/cda:assignedAuthor/cda:representedOrganization/cda:name">Success: R2.2 The Prescriber Facility Name "name" element is present.</report>
         <!-- R2.2 Prescriber Facility Identifier  -->
      <assert test="(cda:author/cda:assignedAuthor/cda:representedOrganization/cda:id and (every $i in cda:author/cda:assignedAuthor/cda:representedOrganization/cda:id satisfies count(($i))&lt; 2) and (cda:author/cda:assignedAuthor/cda:representedOrganization/cda:id/@root or cda:author/cda:assignedAuthor/cda:representedOrganization/cda:id/@nullFlavor)) or cda:author/cda:assignedAuthor/cda:representedOrganization/@nullFlavor or not(cda:author/cda:assignedAuthor/cda:representedOrganization)">
      Error: R2.2 The Prescriber Facility Identifier "id" element is required. The attribute 'root' of the element (id) shall not be empty. The element "id" shall be present in "author\assignedAuthor\representedOrganization\".</assert>
         <report test="cda:author/cda:assignedAuthor/cda:representedOrganization/cda:id">Success: R2.2 The Prescriber Facility Identifier "id" element is present.</report>
         <!-- R2.2 Prescriber Facility Address  -->
      <assert test="(cda:author/cda:assignedAuthor/cda:representedOrganization/cda:addr and (every $i in cda:author/cda:assignedAuthor/cda:representedOrganization/cda:addr satisfies count(($i))&lt; 2)) or cda:author/cda:assignedAuthor/cda:representedOrganization/@nullFlavor or not(cda:author/cda:assignedAuthor/cda:representedOrganization)">
      Error: R2.2 The Prescriber Facility Address "addr" element is required. The element "addr" shall be present in "author\assignedAuthor\representedOrganization\".</assert>
         <report test="cda:author/cda:assignedAuthor/cda:representedOrganization/cda:addr">Success: R2.2 The Prescriber Facility Address "addr" element is present.</report>
         <!-- R2.4 Prescription Item ID -->
      <assert test="cda:id and count(cda:id)&lt;2">Error: R2.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Prescription Item ID is required. The element "id" cannot be repeated (cardinality [1..1]).</assert>
         <report test="cda:id and count(cda:id)&lt;2">Success: R2.4 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Prescription Item ID element "id" is present and have a cardinality [1..1].</report>
      </rule>
      <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration/cda:author/cda:assignedAuthor/cda:representedOrganization/cda:addr">
      <!-- R2.2 Prescriber Facility Street  -->
      <assert test="(cda:streetAddressLine and count(cda:streetAddressLine)&lt;2 and ((cda:streetAddressLine and (every $i in cda:streetAddressLine satisfies string-length(normalize-space($i)) &gt; 0)) or cda:streetAddressLine/@nullFlavor)) or @nullFlavor">
      Error: R2.2 The Prescriber Facility Street "streetAddressLine" element is optional. If present, the element "streetAddressLine" shall not be empty otherwise the nullFlavor attribute shall be present. The element "streetAddressLine" cannot be
      repeated (cardinality [1..1])</assert>
         <!-- R2.2 Prescriber Facility City  -->
      <assert test="(cda:city and count(cda:city)&lt;2 and ((cda:city and (every $i in cda:city satisfies string-length(normalize-space($i)) &gt; 0)) or cda:city/@nullFlavor)) or @nullFlavor">Error: R2.2 The Prescriber Facility City "city" element is
      optional. If present, the element "city" shall not be empty otherwise the nullFlavor attribute shall be present. The element "city" cannot be repeated (cardinality [1..1])</assert>
         <!-- R2.2 Prescriber Facility State or Province  -->
      <assert test="(cda:state and count(cda:state)&lt;2 and ((cda:state and (every $i in cda:state satisfies string-length(normalize-space($i)) &gt; 0)) or cda:state/@nullFlavor)) or @nullFlavor">Error: R2.2 The Prescriber Facility State or Province
      "state" element is required. The element "state" shall not be empty otherwise the nullFlavor attribute shall be present. The element "state" cannot be repeated (cardinality [1..1])</assert>
         <!-- R2.2 Prescriber Facility Postal Code  -->
      <assert test="(cda:postalCode and count(cda:postalCode)&lt;2 and ((cda:postalCode and (every $i in cda:postalCode satisfies string-length(normalize-space($i)) &gt; 0)) or cda:postalCode/@nullFlavor)) or @nullFlavor">Error: R2.2 The Prescriber
      Facility Postal Code "postalCode" element is required. The element "postalCode" shall not be empty otherwise the nullFlavor attribute shall be present. The element "postalCode" cannot be repeated (cardinality [1..1])</assert>
         <!-- R2.2 Prescriber Facility Country  -->
      <assert test="(cda:country and count(cda:country)&lt;2 and (cda:country and (every $i in cda:country satisfies string-length(normalize-space($i)) &gt; 0)) ) or @nullFlavor">Error: R2.2 The Prescriber Facility Country "country" element is required.
      The element "country" shall not be empty and cannot be repeated (cardinality [1..1])</assert>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
      <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.1']"/>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
      <title>IHE PCC Supply Entry - errors validation phase</title>
      <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.7.3&#34;]">
         <assert test="./@classCode=&#34;SPLY&#34; and (./@moodCode=&#34;INT&#34; or ./@moodCode=&#34;EVN&#34;)">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), the moodCode attribute shall be INT to reflect that a medication has
      been prescribed, or EVN to indicate that the prescription has been filled.</assert>
         <assert test="cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), the parent CCD template ID (2.16.840.1.113883.10.20.1.34) shall be present.</assert>
         <assert test="cda:id">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), each supply entry shall have an identifier to uniquely identify the supply entry.</assert>
         <assert test="not(cda:quantity) or cda:quantity[@value]">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), the value attribute of quantity shall be present if quantity exists.</assert>
         <assert test="not(cda:author) or cda:author/cda:time[@value or @nullFlavor]">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), the &lt;time&gt; element must be present if author exists to indicate when the author created the
      prescription. If this information is unknown, it shall be recorded by setting the nullFlavor attribute.</assert>
         <assert test="not(cda:author) or cda:author/cda:assignedAuthor">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), the &lt;assignedAuthor&gt; element shall be present if an author exists in Supply Entry, and identifies the
      author.</assert>
         <assert test="not(cda:author) or cda:author/cda:assignedAuthor/cda:id">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), an identifier element shall be present for an assigned author if an author exists in Supply Entry.</assert>
         <assert test="not(cda:author) or cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name or cda:author/cda:assignedAuthor/cda:representedOrganization/cda:name">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), an
      &lt;assignedPerson&gt;/&lt;name&gt; and/or &lt;representedOriganization&gt;/&lt;name&gt; element shall be present if an author exists in Supply Entry. This element shall identify the prescriber or their organization.</assert>
         <assert test="not(cda:performer) or not(./@moodCode=&#34;INT&#34;) or cda:performer/cda:time[@value or @nullFlavor]">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), the &lt;time&gt; element shall be present if there is a
      performer in Supply Entry to indicate when the prescription was filled (moodCode='EVN'). If this information is unknown, it shall be recorded by setting the nullFlavor attribute.</assert>
         <assert test="not(cda:performer) or cda:performer/cda:assignedEntity">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), the &lt;assignedEntity&gt; element shall be present in a Supply Entry that contains a performer, and identifies
      the filler of the prescription.</assert>
         <assert test="not(cda:performer) or cda:performer/cda:assignedEntity/cda:assignedPerson/cda:name or cda:performer/cda:assignedEntity/cda:representedOrganization/cda:name">Error: In IHE PCC Supply Entry (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), an
      assignedPerson/name and/or representedOriganization/name element shall be present under the assignedEntity if a performer exists.</assert>
         <assert test="not(cda:entryRelationship[@typeCode=&#34;SUBJ&#34;]) or cda:entryRelationship[@typeCode=&#34;SUBJ&#34;]/cda:act[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.4.3.1&#34;]">Error: In IHE PCC Supply Entry
      (1.3.6.1.4.1.19376.1.5.3.1.4.7.3), an entry relationship may be present to provide the fulfillment instructions. When present, this entry relationship shall contain a Medication Fulfillment Instructions entry.</assert>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-errors"
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
            xml:base="templates/errors/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
  <!--Check codes -->
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.1.2-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
      <rule context="/cda:ClinicalDocument">
         <assert test="cda:relatedDocument">Error: The Original document Identification element "relatedDocument" is required. The relationship between the eD instance and the document (data) in use in the National Infrastructure ("original Country-A
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
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
      <title>IHE PCC Language Communication - errors validation phase</title>
      <!-- R1.7 The Patient's preferred language code (Data Validation)-->
    <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.2.1&#34;]/cda:languageCode">
         <assert test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Error: R1.7 The Patient's preferred language code is not valid. the languageCode element
      describes the language code. It uses the same vocabulary described for the ClinicalDocument/languageCode element described in more detail in HL7 CRS: 2.1.1.</assert>
         <report test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Success: R1.7 The Patient's preferred language code is valid.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.1.eD-codes"
            xml:base="templates/eDtemplates/codes/1.3.6.1.4.1.19376.1.5.3.1.1.1.eD.sch">
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
      <assert test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Error: R1.11.10 The Legal Authenticator Facility Telecommunication
      attribute's '@use = 
      <value-of select="current()/@use"/>' of the element "telecom" is not valid. To see valid '@use' please cf.epSOS_MVC_V1 4.xls (HL7:TelecommunicationAddressUse).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '2.16.840.1.113883.5.1119']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.40']/Entry/@code = current()/@use) or @nullFlavor">Success: R1.11.10 The Legal Authenticator Facility Telecommunication
      attribute's '@use' of the element "telecom" is fulfilled.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.3.eD-codes"
            xml:base="templates/eDtemplates/codes/1.3.6.1.4.1.19376.1.5.3.1.2.3.eD.sch">
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
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.2.2-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch"/>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.3-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
    <!--  <title>epSOS Dispensed Medicine Entry Content Module Specification - errors validation phase</title> -->
    <!-- R4.7 Route of Administration  (Data Validation)-->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration/cda:routeCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.12']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.12']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R4.7 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Route of Administration element "routeCode" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '1.3.6.1.4.1.12559.11.10.1.3.1.44.1', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSRoutesofAdministration which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.12)</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.12']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.12']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R4.7 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Route of Administration element "routeCode" is valid.</report>
      </rule>
      <!-- R4.6 Pharmaceutical dose form  (Data Validation)-->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:formCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R4.6 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Pharmaceutical dose form element "formCode" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '1.3.6.1.4.1.12559.11.10.1.3.1.44.1', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSDoseForm which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.2)</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.2']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R4.6 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Pharmaceutical dose form element "formCode" is valid.</report>
      </rule>
      <!-- R4.5 Medicinal product package  (Data Validation)-->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:asContent/epsos:containerPackagedMedicine/epsos:formCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.3']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.3']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Medicinal product package element "formCode" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '1.3.6.1.4.1.12559.11.10.1.3.1.44.1', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSPackage which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.3)</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.3']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.3']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R4.5 (1.3.6.1.4.1.12559.11.10.1.3.1.3.3) The Medicinal product package element "formCode" is valid.</report>
      </rule>
      <!--R3.1 Dispenser Facility Country (Data Validation)-->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:performer/cda:assignedEntity/cda:representedOrganization/cda:addr/cda:country">
         <assert test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Error: R3.1 Dispenser Facility Country element "country = 
      <value-of select="current()"/>" is not valid. To see valid "country" please cf.
      <value-of select="$usedDataDoc"/>(epSOSCountry which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.4).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Success: R3.1 Dispenser Facility Country element "country" is valid.</report>
      </rule>
      <!-- R4.3 Active Ingredient  (Data Validation)-->
    <rule context="//cda:entry/cda:supply[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:product/cda:manufacturedProduct/cda:manufacturedMaterial/epsos:ingredient[@classCode='ACTI']/epsos:ingredient/epsos:code">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.24']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.24']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Error: R4.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.4) The Active Ingredient element "code" is not valid, one of its attributes @code = 
      <value-of select="current()/@code"/>, or @displayName = 
      <value-of select="current()/@displayName"/>, or @codeSystem = 
      <value-of select="current()/@codeSystem"/>, is not valid. The attribute '@codeSystem' shall be '2.16.840.1.113883.6.73', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSActiveIngredient which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.24).</assert>
         <report test="(($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.24']/Entry/@code = current()/@code and not(current()/@displayName)) or ($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.24']/Entry[@code = current()/@code]/@displayName = current()/@displayName) ) or @nullFlavor">
      Success: R4.3 (1.3.6.1.4.1.12559.11.10.1.3.1.3.4) The Active Ingredient element "code" is valid.</report>
      </rule>
      <!-- R2.2 Prescriber Profession (Data Validation)-->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration/cda:author/cda:functionCode">
         <assert test="$pivotcodes//CodeSystem[@oid = current()/@codeSystem] or @nullFlavor or string-length(normalize-space(current()/@codeSystem)) = 0">Error: The 'codeSystem : 
      <value-of select="current()/@codeSystem"/>' does not exist in the 
      <value-of select="$usedDataDoc"/>.</assert>
         <assert test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.1']/Entry[@code = current()/@code]/@displayName = current()/@displayName )">Error: R2.2 Prescriber Profession element
      "functionCode" is not valid, one of its attributes '@code = 
      <value-of select="current()/@code"/>' or '@displayName = 
      <value-of select="current()/@displayName"/>' or '@codeSystem = 
      <value-of select="current()/@codeSystem"/>' is not valid. The attribute '@codeSystem' shall be '2.16.840.1.113883.2.9.6.2.7', to see valid '@code' and '@displayName' please cf.
      <value-of select="$usedDataDoc"/>(epSOSHealthcareProfessionalRoles which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.1)</assert>
         <report test="($pivotcodes//CodeSystem[@oid = current()/@codeSystem]/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.1']/Entry[@code = current()/@code]/@displayName = current()/@displayName )">Success: R2.2 Prescriber Profession element
      "functionCode" is valid.</report>
      </rule>
      <!--R2.2 Prescriber Facility Country (Data Validation)-->
    <rule context="*[cda:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.3']/cda:entryRelationship[@typeCode='REFR']/cda:substanceAdministration/cda:author/cda:assignedAuthor/cda:representedOrganization/cda:addr/cda:country">
         <assert test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Error: R2.2 Prescriber Facility Country element "country = 
      <value-of select="current()"/>" is not valid. To see valid "country" please cf.
      <value-of select="$usedDataDoc"/>(epSOSCountry which oid = 1.3.6.1.4.1.12559.11.10.1.3.1.42.4).</assert>
         <report test="($pivotcodes//CodeSystem[@oid = '1.0.3166.1']/ValueSet[@oid = '1.3.6.1.4.1.12559.11.10.1.3.1.42.4']/Entry/@code = current()) or @nullFlavor">Success: R2.2 Prescriber Facility Country element "country" is valid.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.12559.11.10.1.3.1.3.1-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch">
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
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch"/>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch"/>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-codes"
            xml:base="templates/codes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch"/>
  <!--Check warnings -->
  <!--    <xi:include parse="xml" href="templates/warnings/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch"
        xpointer="xmlns(x=http://purl.oclc.org/dsdl/schematron) xpointer(//x:pattern)">
        <xi:fallback>
            <!-\- xi:include error : file not found :  templates/warnings/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch -\->
        </xi:fallback>
    </xi:include>-->
  <!--Check notes -->
  <!-- <xi:include parse="xml" href="templates/notes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch"
         xpointer="xmlns(x=http://purl.oclc.org/dsdl/schematron) xpointer(//x:pattern)">
         <xi:fallback>
             <!-\- xi:include error : file not found :  templates/notes/1.3.6.1.4.1.12559.11.10.1.3.1.1.2.sch -\->
         </xi:fallback>
     </xi:include>-->
</schema>
