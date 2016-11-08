<%-- 
    Document   : stork.jsp
    Created on : Jun 13, 2014, 3:26:13 PM
    Author     : Marcelo Fonseca <marcelo.fonseca@iuz.pt>
--%>

<%@ include file="/html/portlet/portal_settings/init.jsp" %>

<%

    final String SP_PROPERTIES = "sp.properties";
    final String SP_QAALEVEL = "sp.qaalevel";
    final String SP_RETURN = "sp.return";
    //   final String SP_URL = "sp.url";
    final String PROVIDER_NAME = "provider.name";
    final String SP_SECTOR = "sp.sector";
    final String SP_APLICATION = "sp.aplication";
    final String SP_COUNTRY = "sp.country";
    final String POWER_VALIDATION_URL = "powerValidationUrl";
    final String POWER_VALIDATION_REPRESENTATIVE = "powerValidationRepresentative";
    final String POWER_VALIDATION_REPRESENTED = "powerValidationRepresented";
    final String POWER_VALIDATION_MANDATE_CONTENT = "powerValidationMandateContent";
    final String SP_TAB = "sptab";
    final String SP_TAB_POWER_VALIDATION = "tab3";
    final String COUNTRY_NUMBER = "country.number";
    final String ATTRIBUTE_NUMBER = "attribute.number";
    final String BUSINESS_ATTRIBUTE_NUMBER = "businessAttribute.number";
    final String LEGAL_ATTRIBUTE_NUMBER = "legalAttribute.number";
    final String POWER_VALIDATION_ATTRIBUTE_NUMBER = "powerValidationAttribute.number";
    final String SP_CONF = "SP";
    final String PEPS_URL = "peps.url";
    final String IS_STORK_ENABLED = "stork.enabled";
    final String SP_MANDATORY_PERSONAL_ATTRIBUTES = "sp.mandatory.personal.attributes";
    final String SP_MANDATORY_BUSINESS_ATTRIBUTES = "sp.mandatory.business.attributes";
    final String SP_MANDATORY_LEGAL_ATTRIBUTES = "sp.mandatory.legal.attributes";
    final String SP_OPTIONAL_ATTRIBUTES = "sp.optional.attributes";
    final String STORK_LOGIN_URL = "stork.login.url";

    boolean casIsStorkEnabled = PrefsPropsUtil.getBoolean(company.getCompanyId(),IS_STORK_ENABLED);
    String casPEPSURL = PrefsPropsUtil.getString(company.getCompanyId(),PEPS_URL);
    String casProviderName = PrefsPropsUtil.getString(company.getCompanyId(),PROVIDER_NAME);
    String casSpSector = PrefsPropsUtil.getString(company.getCompanyId(),SP_SECTOR);
    String casSpApplication = PrefsPropsUtil.getString(company.getCompanyId(),SP_APLICATION);
    String casSpCountry = PrefsPropsUtil.getString(company.getCompanyId(),SP_COUNTRY);
    String casSpQaaLevel = PrefsPropsUtil.getString(company.getCompanyId(),SP_QAALEVEL);
    String casSpReturn = PrefsPropsUtil.getString(company.getCompanyId(),SP_RETURN);
    String casSpMandatoryPersonalAttributes = PrefsPropsUtil.getString(company.getCompanyId(),SP_MANDATORY_PERSONAL_ATTRIBUTES);
    String casSpMandatoryBusinessAttributes = PrefsPropsUtil.getString(company.getCompanyId(),SP_MANDATORY_BUSINESS_ATTRIBUTES);
    String casSpMandatoryLegalAttributes = PrefsPropsUtil.getString(company.getCompanyId(),SP_MANDATORY_LEGAL_ATTRIBUTES);
    String casSpOptionalAttributes = PrefsPropsUtil.getString(company.getCompanyId(),SP_OPTIONAL_ATTRIBUTES);
    String casLoginUrl = PrefsPropsUtil.getString(company.getCompanyId(),STORK_LOGIN_URL);

%>

<!--STORK Specific Properties-->

<aui:fieldset>

    <aui:input label="enabled" name='<%= "settings--" + IS_STORK_ENABLED + "--"%>' type="checkbox" value="<%= casIsStorkEnabled%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-provider-name-help" label="stork-provider-name" name='<%= "settings--" + PROVIDER_NAME + "--"%>' type="text" value="<%= casProviderName%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-sp-sector-help" label="stork-sp-sector" name='<%= "settings--" + SP_SECTOR + "--"%>' type="text" value="<%= casSpSector%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-sp-application-help" label="stork-sp-application" name='<%= "settings--" + SP_APLICATION + "--"%>' type="text" value="<%= casSpApplication%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-sp-country-help" label="stork-sp-country" name='<%= "settings--" + SP_COUNTRY + "--"%>' type="text" value="<%= casSpCountry%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-sp-qaalevel-help" label="stork-sp-qaalevel" name='<%= "settings--" + SP_QAALEVEL + "--"%>' type="text" value="<%= casSpQaaLevel%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-sp-return-help" label="stork-sp-return" name='<%= "settings--" + SP_RETURN + "--"%>' type="text" value="<%= casSpReturn%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-peps-url-help" label="stork-peps-url" name='<%= "settings--" + PEPS_URL + "--"%>' type="text" value="<%= casPEPSURL%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-mandatory-personal-attributes-help" label="stork-mandatory-personal-attributes" name='<%= "settings--" + SP_MANDATORY_PERSONAL_ATTRIBUTES + "--"%>' type="text" value="<%= casSpMandatoryPersonalAttributes%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-mandatory-business-attributes-help" label="stork-mandatory-business-attributes" name='<%= "settings--" + SP_MANDATORY_BUSINESS_ATTRIBUTES + "--"%>' type="text" value="<%= casSpMandatoryBusinessAttributes%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-mandatory-legal-attributes-help" label="stork-mandatory-legal-attributes" name='<%= "settings--" + SP_MANDATORY_LEGAL_ATTRIBUTES + "--"%>' type="text" value="<%= casSpMandatoryLegalAttributes%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-sp-optional-attributes-help" label="stork-sp-optional-attributes" name='<%= "settings--" + SP_OPTIONAL_ATTRIBUTES + "--"%>' type="text" value="<%= casSpOptionalAttributes%>" />
    <aui:input cssClass="lfr-input-text-container" helpMessage="stork-login-url" label="stork-login-url" name='<%= "settings--" + STORK_LOGIN_URL + "--"%>' type="text" value="<%= casLoginUrl%>" />

</aui:fieldset>
