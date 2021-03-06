<?xml version="1.0"  ?>



<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n1="urn:hl7-org:v3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  xmlns:epsos="urn:epsos-org:ep:medication" >
	
	
	<xsl:import href="../common/cdaCommon.xsl"/>
	<xsl:import href="../ps/cdaHeader.xsl"/>
	<xsl:import href="../ps/cdaExtendedHeader.xsl"/>
	<xsl:import href="../ps/cdaAlerts.xsl"/>
	<xsl:import href="../ps/cdaDiagnosticTest.xsl"/>
	<xsl:import href="../ps/cdaCurrentProblems.xsl"/>
	<xsl:import href="../ps/cdaMedicationSummary.xsl"/>
	<xsl:import href="../ps/cdaMedicalDevices.xsl"/>
	<xsl:import href="../ps/cdaSurgicalProcedures.xsl"/>
	<xsl:import href="../ps/cdaHistoryIllness.xsl"/>
	<xsl:import href="../ps/cdaVaccinations.xsl"/>
	<xsl:import href="../ps/cdaTreatment.xsl"/>
	<xsl:import href="../ps/cdaAutonomy.xsl"/>
	<xsl:import href="../ps/cdaSocialHistory.xsl"/>
	<xsl:import href="../ps/cdaPregnancyHistory.xsl"/>
	<xsl:import href="../ps/cdaPhysicalFindings.xsl"/>
	<xsl:import href="../ps/cdaOtherSection.xsl"/>
	
	<!-- xsl:output method="html" indent="yes" version="4.01"   doctype-system="http://www.w3.org/TR/html4/strict.dtd" doctype-public="-//W3C//DTD HTML 4.01//EN"/ -->
	
	

	<xsl:template name="hcerCda">
	
	
	
	<!-- Main -->
	
	<!-- produce browser rendered, human readable clinical document	-->
	
				
				<!-- START display top portion of clinical document -->
				<!--- BASIC HEADER INFORMATION -->
				<xsl:call-template name="basicCdaHeader"/><br/><br/>
				
					<!--- EXTENDED HEADER INFORMATION -->
			<fieldset >
			<legend><a href="javascript: showhide('extendedCdaHeader'); self.focus(); void(0);">	<xsl:call-template  name="show-displayLabels">
								<xsl:with-param name="data" select="'68'"/>
							</xsl:call-template>      </a></legend>		
			<div id="extendedCdaHeader" style="display:none">
				<xsl:call-template name="extendedCdaHeader"/><br/><br/>
			</div>
			</fieldset>	<br/>
				<!-- ALERTS -->
					<xsl:call-template name="alerts"/>
					<!--DIAGNOSTIC TEST -->
					<xsl:call-template name="diagnosticTests"/>
					<!--CurrenTProblems -->	
					<xsl:call-template name="currentProblems"/>
					<!--Medication Summary -->	
					<xsl:call-template name="medicationSummary"/>
				
						<!--Medical Devices Summary -->	
					<xsl:call-template name="medicalDevices"/>
	
					<!--Surgical Procedures-->	
					<xsl:call-template name="surgicalProcedures"/>
	
	
					<!--History Illness-->	
					<xsl:call-template name="historyIllness"/>
					
						<!--Vaccination-->	
					<xsl:call-template name="vaccinations"/>
					
						<!--treatments-->	
					<xsl:call-template name="treatment"/>
	
						<!--autonomy-->	
					<xsl:call-template name="autonomy"/>
	
			<!--social History-->	
					<xsl:call-template name="socialHistory"/>
					
						<!--pregnancy History-->	
					<xsl:call-template name="pregnancyHistory"/>
					
							<!--physical Findings-->	
					<xsl:call-template name="physicalFindings"/>
	
					<!--all other sections-->	
					<xsl:call-template name="otherSections"/>
	</xsl:template>
	<!-- generate table of contents -->
	
	
</xsl:stylesheet>