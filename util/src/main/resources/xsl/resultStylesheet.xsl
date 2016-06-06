<?xml version="1.0" encoding="UTF-8"?>
<!-- 
	This stylesheet is copied from the IHE Gazelle EVSClient web application for testing purposes.
	http://gazelle.ihe.net/jenkins/view/EVS/job/EVSClient-mvn/ws/EVSClient-ui/src/main/webapp/xsl/
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output encoding="UTF-8" indent="yes" method="html" media-type="text/html"/>
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Jul 5, 2010</xd:p>
            <xd:p><xd:b>Modified on:</xd:b> Aug 24, 2010</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anne-Gaëlle BERGE, IHE Development, INRIA Rennes</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>
    <xsl:template match="/">
        <html>
            <head>
                <title>External Validation Report</title>
                <link href="resultStyle.css" rel="stylesheet" type="text/css" media="screen" />                
            </head>
            <body>
                <h2>External Validation Report</h2>
                <br/>
                <div class="rich-panel">
                    <div class="rich-panel-header">General Information</div>
                    <div class="rich-panel-body">
                        <table border="0">
                            <tr>
                                <td><b>Validation Date</b></td>
                                <td><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationDate"/> - <xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTime"/></td> 
                            </tr>
                            <tr>
                                <td><b>Validation Service</b></td>
                                <td><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationServiceName"/> (<xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationServiceVersion"/>)</td>         
                            </tr>
                            <xsl:if test="count(detailedResult/ValidationResultsOverview/Oid) = 1">
                                <tr>
                                    <td>
                                        <b>Profile OID</b>
                                    </td>
                                    <td>
                                        <xsl:value-of
                                            select="detailedResult/ValidationResultsOverview/Oid" />
                                    </td>
                                </tr>
                            </xsl:if>
                            <tr>
                                <td><b>Validation Test Status</b></td>
                                <td>
                                    <xsl:if test="contains(detailedResult/ValidationResultsOverview/ValidationTestResult, 'PASSED')">
                                        <div class="PASSED"><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTestResult"/></div>
                                    </xsl:if>
                                    <xsl:if test="contains(detailedResult/ValidationResultsOverview/ValidationTestResult, 'FAILED')">
                                        <div class="FAILED"><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTestResult"/></div>
                                    </xsl:if>
                                    <xsl:if test="contains(detailedResult/ValidationResultsOverview/ValidationTestResult, 'ABORTED')">
                                        <div class="ABORTED"><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTestResult"/></div>
                                    </xsl:if>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <br/>
                <xsl:if test="count(detailedResult/ValidationResultsOverview/ReferencedStandard) = 1">
                    <div class="rich-panel">
                        <div class="rich-panel-header">Referenced Standard</div>
                        <div class="rich-panel-body">
                            <table border="0">
                                <tr>
                                    <td>
                                        <b>Name</b>
                                    </td>
                                    <td>
                                        <xsl:value-of
                                            select="detailedResult/ValidationResultsOverview/ReferencedStandard/StandardName" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <b>Version</b>
                                    </td>
                                    <td>
                                        <xsl:value-of
                                            select="detailedResult/ValidationResultsOverview/ReferencedStandard/StandardVersion" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <b>Extension</b>
                                    </td>
                                    <td>
                                        <xsl:value-of
                                            select="detailedResult/ValidationResultsOverview/ReferencedStandard/StandardExtension" />
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <br/>
                </xsl:if>
                
                <xsl:if test="count(detailedResult/ValidationResultsOverview/documentWellFormed) = 1">
                    <div class="rich-panel">
                        <div class="rich-panel-header">XSD Validation detailed Results</div>
                        <div class="rich-panel-body">
                            <i>The document you have validated is an XML document. The validator has checked that it is well-formed and has validated it against one ore several XSD schemas, results of those validations are gathered in this part.</i>
                            <xsl:choose>
                                <xsl:when test="detailedResult/ValidationResultsOverview/documentWellFormed/result = 'yes'">
                                    <p class="PASSED">The XML document is well-formed</p>                        
                                </xsl:when>
                                <xsl:otherwise>
                                    <p class="FAILED">The XML document is not well-formed</p>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:if test="count(detailedResult/ValidationResultsOverview/documentValidCDA) = 1">
                                <xsl:choose>
                                    <xsl:when test="detailedResult/ValidationResultsOverview/documentValidCDA/result = 'yes'">
                                        <p class="PASSED">The XML document is a valid CDA regarding HL7 specifications</p>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <p class="FAILED">The XML document is not a valid CDA regarding HL7 specifications because of the following reasons: </p>
                                        <xsl:if test="count(detailedResult/ValidationResultsOverview/documentValidCDA/*) &gt; 3">
                                            <ul>
                                                <xsl:for-each select="detailedResult/ValidationResultsOverview/documentValidCDA/*">
                                                    <xsl:if test="contains(current(), 'error')">
                                                        <li><xsl:value-of select="current()"/></li>
                                                    </xsl:if>
                                                </xsl:for-each>
                                            </ul>
                                        </xsl:if>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:if>
                            <xsl:if test="count(detailedResult/ValidationResultsOverview/documentValidEpsos) = 1">
                                            <xsl:choose>
                                                <xsl:when test="detailedResult/ValidationResultsOverview/documentValidEpsos/result = 'yes'">
                                                    <p class="PASSED">The XML document is a valid CDA regarding epSOS specifications</p>                           
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <p  class="FAILED">The XML document is not a valid CDA regarding epSOS specifications because of the following reasons: </p>
                                                    <xsl:if test="count(detailedResult/ValidationResultsOverview/documentValidEpsos/*) &gt; 3">
                                                        <ul>
                                                            <xsl:for-each select="detailedResult/ValidationResultsOverview/documentValidEpsos/*">
                                                                <xsl:if test="contains(current(), 'error')">
                                                                    <li><xsl:value-of select="current()"/></li>
                                                                </xsl:if>
                                                            </xsl:for-each>
                                                        </ul>
                                                    </xsl:if>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:if>
                            <xsl:if test="count(detailedResult/ValidationResultsOverview/documentValid) = 1">
                                <xsl:choose>
                                    <xsl:when test="detailedResult/ValidationResultsOverview/documentValid/result = 'yes'">
                                        <p class="PASSED">The XML document is valid</p>                        
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <p class="FAILED">The XML document is not valid because of the following reasons: </p>
                                        <xsl:if test="count(detailedResult/ValidationResultsOverview/documentValid/*) &gt; 3">
                                            <ul>
                                                <xsl:for-each select="detailedResult/ValidationResultsOverview/documentValid/*">
                                                    <xsl:if test="contains(current(), 'error')">
                                                        <li><xsl:value-of select="current()"/></li>
                                                    </xsl:if>
                                                </xsl:for-each>
                                            </ul>
                                        </xsl:if>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:if>
                        </div>
                    </div>
                </xsl:if>
                <br/>
                <div class="rich-panel">
                    <div class="rich-panel-header">Validation counters</div>
                    <div class="rich-panel-body">
                        <xsl:choose>
                            <xsl:when test="detailedResult/ValidationCounters/NrOfChecks &gt; 0">
                                <xsl:value-of select="detailedResult/ValidationCounters/NrOfChecks"/> check(s) performed
                            </xsl:when>
                            <xsl:otherwise>
                                No check was performed
                            </xsl:otherwise>
                        </xsl:choose>
                        <br/>
                        <xsl:choose>
                            <xsl:when test="detailedResult/ValidationCounters/NrOfValidationErrors &gt; 0">
                                <a href="#errors"><xsl:value-of select="detailedResult/ValidationCounters/NrOfValidationErrors"/> error(s)</a>
                            </xsl:when>
                            <xsl:otherwise>
                                No error
                            </xsl:otherwise>
                        </xsl:choose>
                        <br/>
                        <xsl:choose>
                            <xsl:when test="detailedResult/ValidationCounters/NrOfValidationWarnings &gt; 0">
                                <a href="#warnings"><xsl:value-of select="detailedResult/ValidationCounters/NrOfValidationWarnings"/> warning(s)</a>
                            </xsl:when>
                            <xsl:otherwise>
                                No warning
                            </xsl:otherwise>
                        </xsl:choose>
                        <br/>
                        <xsl:choose>
                            <xsl:when test="detailedResult/ValidationCounters/NrOfValidationNotes &gt; 0">
                                <a href="#notes"><xsl:value-of select="detailedResult/ValidationCounters/NrOfValidationNotes"/> note(s)</a>
                            </xsl:when>
                            <xsl:otherwise>
                                No note
                            </xsl:otherwise>
                        </xsl:choose>
                        <br/>
                        <xsl:choose>
                            <xsl:when test="detailedResult/ValidationCounters/NrOfValidationReports &gt; 0">
                                <a href="#reports"><xsl:value-of select="detailedResult/ValidationCounters/NrOfValidationReports"/> successful check(s)</a>
                            </xsl:when>
                            <xsl:otherwise>
                                No successful check
                            </xsl:otherwise>
                        </xsl:choose>
                        <br/>
                        <xsl:choose>
                            <xsl:when test="detailedResult/ValidationCounters/NrOfValidationUnknown &gt; 0">
                                <a href="#unknown"><xsl:value-of select="detailedResult/ValidationCounters/NrOfValidationUnknown"/> unknown exception(s)</a>
                            </xsl:when>
                            <xsl:otherwise>
                                No unknown exception
                            </xsl:otherwise>
                        </xsl:choose>
                    </div>
                </div>
                <br/>
                <div class="rich-panel">
                    <div class="rich-panel-header">Validation details</div>
                    <div class="rich-panel-body">
                        <xsl:if test="detailedResult/ValidationCounters/NrOfValidationErrors &gt; 0">
                            <p id="errors"><b>Errors</b></p>
                            <xsl:for-each select="detailedResult/ValidationResults/ResultXML/Error">
                                <table class="error">
                                    <tr>
                                        <td valign="top"><b>Test</b></td>
                                        <td><xsl:value-of select="Test"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Location</b></td>
                                        <td><xsl:value-of select="Location"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Description</b></td>
                                        <td><xsl:value-of select="Description"/></td>
                                    </tr>
                                </table>
                                <br/>
                            </xsl:for-each>
                        </xsl:if>
                        <xsl:if test="detailedResult/ValidationCounters/NrOfValidationWarnings &gt; 0">
                            <p id="warnings"><b>Warnings</b></p>
                            <xsl:for-each select="detailedResult/ValidationResults/ResultXML/Warning">
                                <table class="warning">
                                    <tr>
                                        <td valign="top"><b>Test</b></td>
                                        <td><xsl:value-of select="Test"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Location</b></td>
                                        <td><xsl:value-of select="Location"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Description</b></td>
                                        <td><xsl:value-of select="Description"/></td>
                                    </tr>
                                </table>
                                <br/>
                            </xsl:for-each>
                        </xsl:if>
                        <xsl:if test="detailedResult/ValidationCounters/NrOfValidationNotes &gt; 0">
                            <p id="notes"><b>Notes</b></p>
                            <xsl:for-each select="detailedResult/ValidationResults/ResultXML/Note">
                                <table class="note">
                                    <tr>
                                        <td valign="top"><b>Test</b></td>
                                        <td><xsl:value-of select="Test"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Location</b></td>
                                        <td><xsl:value-of select="Location"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Description</b></td>
                                        <td><xsl:value-of select="Description"/></td>
                                    </tr>
                                </table>
                                <br/>
                            </xsl:for-each>
                        </xsl:if>
                        <xsl:if test="detailedResult/ValidationCounters/NrOfValidationUnknown &gt; 0">
                            <p id="unknown"><b>Unknown exceptions</b></p>
                            <xsl:for-each select="detailedResult/ValidationResults/ResultXML/Unknown">
                                <table class="unknown">
                                    <tr>
                                        <td valign="top"><b>Test</b></td>
                                        <td><xsl:value-of select="Test"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Location</b></td>
                                        <td><xsl:value-of select="Location"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Description</b></td>
                                        <td><xsl:value-of select="Description"/></td>
                                    </tr>
                                </table>
                                <br/>
                            </xsl:for-each>
                        </xsl:if>
                        <xsl:if test="detailedResult/ValidationCounters/NrOfValidationReports &gt; 0">
                            <p id="reports"><b>Reports</b></p>
                            <xsl:for-each select="detailedResult/ValidationResults/ResultXML/Report">
                                <table class="report">
                                    <tr>
                                        <td valign="top"><b>Test</b></td>
                                        <td><xsl:value-of select="Test"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Location</b></td>
                                        <td><xsl:value-of select="Location"/></td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Description</b></td>
                                        <td><xsl:value-of select="Description"/></td>
                                    </tr>
                                </table>
                                <br/>
                            </xsl:for-each>
                        </xsl:if>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
