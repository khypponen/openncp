<?xml version="1.0" encoding="UTF-8"?>
<!-- Revision = $Revision: 21223 $ --><schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3"
        xmlns:iso="http://purl.oclc.org/dsdl/schematron"
        xmlns:sch="http://www.ascc.net/xml/schematron"
        xmlns:xi="http://www.w3.org/2003/XInclude"
        queryBinding="xslt2"
        defaultPhase="all">
  <title>CTX: 14110: XDS-SD Wrapper Format - HL7 CDA R2 (Schematron)</title>
  <ns prefix="cda" uri="urn:hl7-org:v3"/>
  <ns prefix="epsos" uri="urn:epsos-org:ep:medication"/>
  <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
  <ns prefix="crs" uri="urn:hl7-org:crs"/>
  <!--Phases -->
  <phase id="all">
      <active pattern="variables"/>
      <active pattern="FileCheck-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-codes"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-codes"/>
      <active pattern="ClinicalDocument_common"/>
      <active pattern="ClinicalDocument-codes"/>
      <active pattern="ClinicalDocument-recordTarget"/>
      <active pattern="ClinicalDocument-author"/>
      <active pattern="ClinicalDocument-custodian"/>
      <active pattern="ClinicalDocument-component-nonXMLBody"/>
      <active pattern="ClinicalDocument"/>
   </phase>
  <phase id="no-codes">
      <active pattern="variables"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.1-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-errors"/>
      <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.2.3-errors"/>
      <active pattern="ClinicalDocument_common"/>
      <active pattern="ClinicalDocument-recordTarget"/>
      <active pattern="ClinicalDocument-author"/>
      <active pattern="ClinicalDocument-custodian"/>
      <active pattern="ClinicalDocument-component-nonXMLBody"/>
      <active pattern="ClinicalDocument"/>
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
  <!--Common XDS_sd -->
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors"
            xml:base="templates/errors/header.sch">
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
            xml:base="templates/errors/header.sch">
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
            xml:base="templates/errors/header.sch">
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
            xml:base="templates/errors/header.sch">
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
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.1-codes"
            xml:base="templates/codes/header.sch">
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
            xml:base="templates/codes/header.sch">
      <title>IHE PCC Language Communication - errors validation phase</title>
      <!-- R1.7 The Patient's preferred language code (Data Validation)-->
    <rule context="*[cda:templateId/@root=&#34;1.3.6.1.4.1.19376.1.5.3.1.2.1&#34;]/cda:languageCode">
         <assert test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Error: R1.7 The Patient's preferred language code is not valid. the languageCode element
      describes the language code. It uses the same vocabulary described for the ClinicalDocument/languageCode element described in more detail in HL7 CRS: 2.1.1.</assert>
         <report test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Success: R1.7 The Patient's preferred language code is valid.</report>
      </rule>
  </pattern>
  <pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.2.4-codes"
            xml:base="templates/codes/header.sch">
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
            xml:base="templates/codes/header.sch">
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
  <pattern id="ClinicalDocument_common"
            name="ClinicalDocument child-less elements Reference ITI TF-1:7.1.8.2.1">
      <rule context="cda:ClinicalDocument">
      <!-- ClinicalDocument/id is present, but  with no extension -->
      <assert test="cda:id">ERR: ClinicalDocument/id is missing</assert>
         <report test="cda:id">CTX: ClinicalDocument/id exists (PASS)</report>
         <assert test="cda:id[@extension]">CTX: ClinicalDocument/id@extension is empty (PASS)</assert>
         <report test="cda:id[@extension]">ERR: ClinicalDocument/id is should have no extension</report>
         <assert test="cda:effectiveTime[@value]">ERR: ClinicalDocument/effectiveTime is missing</assert>
         <report test="cda:effectiveTime[@value]">CTX: ClinicalDocument/effectiveTime exists (PASS)</report>
         <!-- ClinicalDocument/CconfidentialityCode@code and confidentialityCode@codeSystem are present -->
      <assert test="cda:confidentialityCode[@code]">ERR: ClinicalDocument/confidentialityCode@code is missing</assert>
         <report test="cda:confidentialityCode[@code]">CTX: ClinicalDocument/confidentialityCode@code exists (PASS)</report>
         <assert test="cda:confidentialityCode[@codeSystem]">ERR: ClinicalDocument/confidentialityCode@codeSystem is missing</assert>
         <report test="cda:confidentialityCode[@codeSystem]">CTX: ClinicalDocument/confidentialityCode@codeSystem exists (PASS)</report>
         <!-- ClinicalDocument/languageCode is present -->
      <assert test="cda:languageCode[@code]">ERR: ClinicalDocument/languageCode@code is missing</assert>
         <report test="cda:languageCode[@code]">CTX: ClinicalDocument/languageCode@code exists (PASS)</report>
      </rule>
  </pattern>
  <pattern id="ClinicalDocument-codes"
            name="ClinicalDocument child-less elements Reference ITI TF-1:7.1.8.2.1">
    <!-- Document language code (Data Validation)-->
    <rule context="/cda:ClinicalDocument/cda:languageCode">
         <assert test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Error: The document language code element "ClinicalDocument/languageCode" is not valid. the
      languageCode element describes the language code. It uses the same vocabulary described for the ClinicalDocument/languageCode element described in more detail in HL7 CRS: 2.1.1.</assert>
         <report test="$pivotcodes//CodeSystem[@displayName = 'languageStringCode']/ValueSet[@displayName = 'epSOSValidLanguages']/Entry/@code = current()/@code">Success: R1.11.10 The document language code element "ClinicalDocument/languageCode" is
      valid.</report>
      </rule>
  </pattern>
  <pattern id="ClinicalDocument-recordTarget"
            name="ClinicalDocument/recordTarget elements Reference ITI TF-1:7.1.8.2.2">
      <rule context="cda:ClinicalDocument">
      <!-- ClinicalDocument/recordTarget/patientRole/id is present with root and extension attributes -->
      <assert test="cda:recordTarget[cda:patientRole[cda:id[@root]]]">ERR: ClinicalDocument/recordTarget/patientRole/id@root is missing</assert>
         <report test="cda:recordTarget[cda:patientRole[cda:id[@root]]]">CTX: ClinicalDocument/recordTarget/patientRole/id@root exists (PASS)</report>
         <assert test="cda:recordTarget[cda:patientRole[cda:id[@extension]]]">ERR: ClinicalDocument/recordTarget/patientRole/id@extension is missing</assert>
         <report test="cda:recordTarget[cda:patientRole[cda:id[@extension]]]">CTX: ClinicalDocument/recordTarget/patientRole/id@extension exists (PASS)</report>
         <!-- ClinicalDocument/recordTarget/patientRole/addr is present with at least country element -->
      <assert test="cda:recordTarget[cda:patientRole[cda:addr[cda:country]]]">ERR: ClinicalDocument/recordTarget/patientRole/addr/country is missing</assert>
         <report test="cda:recordTarget[cda:patientRole[cda:addr[cda:country]]]">CTX: ClinicalDocument/recordTarget/patientRole/addr/country exists (PASS)</report>
         <!-- ClinicalDocument/recordTarget/patientRole/patient/name is present with at least a given and family subelement -->
      <assert test="cda:recordTarget[cda:patientRole[cda:patient[cda:name[cda:given]]]]">ERR: ClinicalDocument/recordTarget/patientRole/patient/name/given is missing</assert>
         <report test="cda:recordTarget[cda:patientRole[cda:patient[cda:name[cda:given]]]]">CTX: ClinicalDocument/recordTarget/patientRole/patient/name/given exists (PASS)</report>
         <assert test="cda:recordTarget[cda:patientRole[cda:patient[cda:name[cda:family]]]]">ERR: ClinicalDocument/recordTarget/patientRole/patient/name/family is missing</assert>
         <report test="cda:recordTarget[cda:patientRole[cda:patient[cda:name[cda:family]]]]">CTX: ClinicalDocument/recordTarget/patientRole/patient/name/family exists (PASS)</report>
         <!-- ClinicalDocument/recordTarget/patientRole/patient/administrativeGenderCode is present -->
      <assert test="cda:recordTarget[cda:patientRole[cda:patient[cda:administrativeGenderCode]]]">ERR: ClinicalDocument/recordTarget/patientRole/patient/administrativeGenderCode is missing</assert>
         <report test="cda:recordTarget[cda:patientRole[cda:patient[cda:administrativeGenderCode]]]">CTX: ClinicalDocument/recordTarget/patient/administrativeGenderCode exists (PASS)</report>
         <!-- ClinicalDocument/recordTarget/patientRole/patient/birthTime is present  with precision to the year-->
      <assert test="cda:recordTarget[cda:patientRole[cda:patient[cda:birthTime[@value]]]]">ERR: ClinicalDocument/recordTarget/patientRole/patient/birthTime is missing</assert>
         <report test="cda:recordTarget[cda:patientRole[cda:patient[cda:birthTime[@value]]]]">CTX: ClinicalDocument/recordTarget/patient/birthTime exists (PASS)</report>
      </rule>
  </pattern>
  <pattern id="ClinicalDocument-author"
            name="ClinicalDocument/author - scanner - elements -- Reference ITI TF-1:7.1.8.2.4">
      <rule context="cda:ClinicalDocument">
         <assert test="cda:author">ERR: ClinicalDocument/author is missing</assert>
         <report test="cda:author">CTX: ClinicalDocument/author exists (PASS)</report>
         <!-- ClinicalDocument/author/time -->
      <assert test="cda:author[cda:time]">ERR: ClinicalDocument/author/time is missing</assert>
         <report test="cda:author[cda:time]">CTX: ClinicalDocument/author/time exists (PASS)</report>
         <!--       NOT WORKING
        <assert test="cda:author[cda:time[@value]]=cda:effectiveTime[@value]" > ERR: ClinicalDocument/author/time must equal ClinicalDocument/effectiveTime </assert>
      
      <report test="cda:author[cda:time[@value]]=cda:effectiveTime[@value]"> CTX: ClinicalDocument/author/time matches ClinicalDocument/effectiveTime (PASS) </report>
      -->
      <!-- ClinicalDocument/author/assignedAuthor/id@root -->
      <assert test="cda:author[cda:assignedAuthor[cda:id[@root]]]">ERR: ClinicalDocument/author/assignedAuthor/id@root is missing</assert>
         <report test="cda:author[cda:assignedAuthor[cda:id[@root]]]">CTX: ClinicalDocument/author/assignedAuthor/id@root exists (PASS)</report>
         <!-- ClinicalDocument/author/assignedAuthor//assignedAuthoringDevice/code -->
      <!-- Removed to fix error https://support.epsos.cz/issues/97 
        <assert test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code]]]"> ERR:
          ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/code is missing </assert>

        <report test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code]]]"> CTX:
          ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/code exists (PASS) </report>

        <assert
          test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@codeSystem='1.2.840.10008.2.16.4']]]]"
          > ERR: ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/code@codeSystem
          wrong value </assert>

        <report
          test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@codeSystem='1.2.840.10008.2.16.4']]]]"
          > CTX: ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/code@codeSystem
          correct (PASS) </report>

        <assert
          test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@code='CAPTURE']]]] or cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@code='WST']]]]"
          > ERR: ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/code@code wrong
          value </assert>

        <report
          test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@code='CAPTURE']]]] or cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@code='WST']]]]"
          > CTX: ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/code@code correct
          (PASS) </report>
        <assert
          test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@displayName='Image Capture']]]] or cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@displayName='Workstation']]]]"
          > ERR: ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/code@displayName
          wrong value </assert>

        <report
          test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@displayName='ImageCapture']]]] or cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:code[@displayName='Workstation']]]]"
          > CTX: ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/code@displayName
          correct (PASS) </report>


        <assert
          test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:manufacturerModelName]]]"
          > ERR:
          ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/manufacturerModelName is
          missing </assert>

        <report
          test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:manufacturerModelName]]]"
          > CTX:
          ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/manufacturerModelName
          exists (PASS) </report>


        <assert test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:softwareName]]]"
          > ERR: ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/softwareName is
          missing </assert>

        <report test="cda:author[cda:assignedAuthor[cda:assignedAuthoringDevice[cda:softwareName]]]"
          > CTX: ClinicalDocument/author/assignedAuthor/assignedAuthoringDevice/softwareModelName
          exists (PASS) </report>

-->
      <!-- ClinicalDocument/author/assignedAuthor/representedOrganization@root -->
      <assert test="cda:author[cda:assignedAuthor[cda:representedOrganization[cda:id[@root]]]]">ERR: ClinicalDocument/author/assignedAuthor/representedOrganization@root is missing</assert>
         <report test="cda:author[cda:assignedAuthor[cda:representedOrganization[cda:id[@root]]]]">CTX: ClinicalDocument/author/assignedAuthor/representedOrganization@root exists (PASS)</report>
      </rule>
  </pattern>
  <pattern id="ClinicalDocument-custodian"
            name="ClinicalDocument/custodian elements -- Reference ITI TF-1:7.1.8.2.6">
      <rule context="cda:ClinicalDocument">
         <assert test="cda:custodian">ERR: ClinicalDocument/custodian is missing</assert>
         <report test="cda:custodian">CTX: ClinicalDocument/custodian exists (PASS)</report>
         <!-- ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/name and addr-->
      <assert test="cda:custodian[cda:assignedCustodian[cda:representedCustodianOrganization[cda:name]]]">ERR: ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/name is missing</assert>
         <report test="cda:custodian[cda:assignedCustodian[cda:representedCustodianOrganization[cda:name]]]">CTX: ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/name exists (PASS)</report>
         <assert test="cda:custodian[cda:assignedCustodian[cda:representedCustodianOrganization[cda:addr]]]">ERR: ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/addr is missing</assert>
         <report test="cda:custodian[cda:assignedCustodian[cda:representedCustodianOrganization[cda:addr]]]">CTX: ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/addr exists (PASS)</report>
         <assert test="cda:custodian[cda:assignedCustodian[cda:representedCustodianOrganization[cda:addr[cda:country]]]]">ERR: ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/addr/country is missing</assert>
         <report test="cda:custodian[cda:assignedCustodian[cda:representedCustodianOrganization[cda:addr[cda:country]]]]">CTX: ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/addr/country exists (PASS)</report>
      </rule>
  </pattern>
  <pattern id="ClinicalDocument-component-nonXMLBody"
            name="ClinicalDocument/component/nonXMLBody -- Reference ITI TF-1:7.1.8.2.9">
      <rule context="cda:ClinicalDocument">
         <assert test="cda:component[cda:nonXMLBody]">ERR: ClinicalDocument/component/nonXMLBody is missing</assert>
         <report test="cda:component[cda:nonXMLBody]">CTX: ClinicalDocument/component/nonXMLBody exists (PASS)</report>
         <!-- ClinicalDocument/component/nonXMLBody/text -->
      <assert test="cda:component[cda:nonXMLBody[cda:text[@mediaType='application/pdf']]] or cda:component[cda:nonXMLBody[cda:text[@mediaType='text/plain']]]">ERR: ClinicalDocument/component/nonXMLBody/text@mediaType wrong value</assert>
         <report test="cda:component[cda:nonXMLBody[cda:text[@mediaType='application/pdf']]] or cda:component[cda:nonXMLBody[cda:text[@mediaType='text/plain']]]">CTX: ClinicalDocument/component/nonXMLBody/text@mediaType correct (PASS)</report>
         <assert test="cda:component[cda:nonXMLBody[cda:text[@representation='B64']]]">ERR: ClinicalDocument/component/nonXMLBody/text@representation must be B64</assert>
         <report test="cda:component[cda:nonXMLBody[cda:text[@representation='B64']]]">CTX: ClinicalDocument/component/nonXMLBody/text@representation correct (PASS)</report>
      </rule>
  </pattern>
  <pattern id="ClinicalDocument"
            name="ClinicalDocument child-less elements Reference ITI TF-1:7.1.8.2.1">
      <rule context="cda:ClinicalDocument">
      <!-- Check for the code begin 
      <assert test="cda:code[@code = 'DOCUMENT_TYPE_CODE'] and cda:code[@displayName = 'DOCUMENT_TYPE_NAME']">Error: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The eDispensation document type code element "@code = 
      <value-of select="cda:code/@code"/>", or "@displayName = 
      <value-of select="cda:code/@displayName"/>" are not valid, the attribute '@code' must be set to the value "60593-1" which is the code corresponding to "Medication dispensed" in the loinc code system. And '@displayName' must be set to the value
      "eDispensation".</assert>
         <report test="cda:code[@code = 'DOCUMENT_TYPE_CODE'] and cda:code[@displayName = 'DOCUMENT_TYPE_NAME']">Success: (cf.Table 2C 
      <value-of select="$newSpecDoc"/>) The eDispensation document type code is fulfilled.</report>
          end of check for the code-->
    </rule>
  </pattern>
</schema>
