<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" 
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:n1="urn:hl7-org:v3" 
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
				xmlns:epsos="urn:epsos-org:ep:medication">

<xsl:output method="html" 
			indent="yes" 
			version="4.01" 
			doctype-system="http://www.w3.org/TR/html4/strict.dtd" 
			doctype-public="-//W3C//DTD HTML 4.01//EN"/>


<xsl:template name="telecom">
	<xsl:param name="telecomParam"/>
	<xsl:choose>
		<xsl:when test="$telecomParam/@nullFlavor">
				<xsl:call-template name="show-noneFlavor">
					<xsl:with-param name="data" select="$telecomParam/@nullFlavor"/>
				</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$telecomParam/@value"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="facilityName">
	<xsl:param name="name"/>
	<xsl:choose>
		<xsl:when test="$name/@nullFlavor">
				<xsl:call-template name="show-noneFlavor">
					<xsl:with-param name="data" select="$name/@nullFlavor"/>
				</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$name"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="facilityId">
	<xsl:param name="id"/>
	<xsl:choose>
		<xsl:when test="$id/@nullFlavor">
				<xsl:call-template name="show-noneFlavor">
					<xsl:with-param name="data" select="$id/@nullFlavor"/>
				</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$id/@extension"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="country">
	<xsl:param name="name"/>
	<xsl:choose>
		<xsl:when test="$name/@nullFlavor">
				<xsl:call-template name="show-noneFlavor">
					<xsl:with-param name="data" select="$name/@nullFlavor"/>
				</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$name"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>


<xsl:template name="organization">
	<xsl:param name="name"/>
	<xsl:choose>
		<xsl:when test="$name/@nullFlavor">
				<xsl:call-template name="show-noneFlavor">
					<xsl:with-param name="data" select="$name/@nullFlavor"/>
				</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$name"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="epPrescriberDetails">
	


	<table>
		<xsl:attribute name="id">
		<xsl:text>prescriberTable</xsl:text>
		</xsl:attribute>
		<tr>
			<th>
				<!-- Prescriber: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'56'" />
				</xsl:call-template>
			</th>
			<th>
				<!-- Profession: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'64'" />
				</xsl:call-template>
			</th>
			<th>
				<!-- Specialty: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'69'" />
				</xsl:call-template>
			</th>
			<th>
				<!-- Contact Information: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'12'" />
				</xsl:call-template>
			</th>
			<th>
				<!-- Country: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'13'" />
				</xsl:call-template>
			</th>
		</tr>
		<tr>
			<td>
				<!-- Prescriber: -->
				<xsl:call-template name="authorName">
					<xsl:with-param name="authorLocation"
						select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section[n1:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/n1:author" />
				</xsl:call-template>
				&#160;
			</td>
			<td>
				<!-- Profession: -->
				<xsl:call-template name="show-formCode">
					<xsl:with-param name="parameter"
						select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section[n1:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/n1:author/n1:functionCode" />
				</xsl:call-template>
				&#160;
			</td>
			<td>
				<!-- Specialty: -->
				&#160;
			</td>
			<td>
				<!-- Contact Information: -->
				<xsl:call-template name="telecom">
					<xsl:with-param name="telecomParam"
						select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:telecom" />
				</xsl:call-template>
				&#160;
			</td>
			<td>
				<!-- Country: -->
				<xsl:call-template name="country">
					<xsl:with-param name="name"
						select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section[n1:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:addr/n1:country" />
				</xsl:call-template>
				&#160;
			</td>
		</tr>
		<tr>
			<th>
				<!-- Facility Name: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'29'" />
				</xsl:call-template>
			</th>
			<th>
				<!-- Facility ID: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'28'" />
				</xsl:call-template>
			</th>
			<th>
				<!-- Address: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'3'" />
				</xsl:call-template>
			</th>
			<th>
				<!-- Organisation Name: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'47'" />
				</xsl:call-template>
			</th>
			<th>
				<!-- Organisation Identifier: -->
				<xsl:call-template name="show-displayLabels">
					<xsl:with-param name="data" select="'46'" />
				</xsl:call-template>
			</th>
		</tr>
		<tr>
			<td>
				<!-- Facility Name: -->
				<xsl:call-template name="facilityName">
					<xsl:with-param name="name"
						select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section[n1:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
				</xsl:call-template>
				&#160;
			</td>
			<td>
				<!-- Facility ID: -->
				<xsl:call-template name="facilityId">
					<xsl:with-param name="id"
						select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section[n1:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:id" />
				</xsl:call-template>
				&#160;
			</td>
			<td>
				<!-- Address: -->
				<xsl:call-template name="country">
					<xsl:with-param name="name"
						select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section[n1:templateId/@root='1.3.6.1.4.1.12559.11.10.1.3.1.2.1']/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:addr" />
				</xsl:call-template>
				&#160;
			</td>
			<td>
				<!-- Organisation Name: -->
				<xsl:call-template name="organization">
					<xsl:with-param name="name"
						select="//n1:entry/n1:substanceAdministration[n1:templateId[@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']]/n1:participant[@typeCode='AUT']/n1:participantRole[@classCode='LIC']/n1:scopingEntity[@classCode='ORG']/n1:desc" />
				</xsl:call-template>
				&#160;
			</td>
			<td>
				<!-- Organisation Identifier: -->
				<xsl:call-template name="organization">
					<xsl:with-param name="name"
						select="//n1:entry/n1:substanceAdministration[n1:templateId[@root='1.3.6.1.4.1.12559.11.10.1.3.1.3.2']]/n1:participant[@typeCode='AUT']/n1:participantRole[@classCode='LIC']/n1:scopingEntity[@classCode='ORG']/n1:id" />
				</xsl:call-template>
				&#160;
			</td>
		</tr>
	</table>

	<script>
		<xsl:attribute name="type">
			<xsl:text>text/javascript</xsl:text>
		</xsl:attribute>
		<xsl:text disable-output-escaping="yes">showhide(&apos;prescriberTable&apos;)</xsl:text>
	</script>
</xsl:template>
	
</xsl:stylesheet>

