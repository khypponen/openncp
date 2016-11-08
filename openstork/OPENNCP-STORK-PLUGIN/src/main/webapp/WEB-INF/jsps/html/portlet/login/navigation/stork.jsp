<%@ include file="/html/portlet/login/init.jsp" %>

<%
final String STORK_ENABLED = "stork.enabled";
final String STORK_LOGIN_URL = "stork.login.url";
String storkEnabled = PrefsPropsUtil.getString(company.getCompanyId(), STORK_ENABLED, "false");
String storkAuthURL = PrefsPropsUtil.getString(company.getCompanyId(), STORK_LOGIN_URL, "http://localhost:9080/stork-plugin-0.5.0-SNAPSHOT/storkServlet");
//PortalUtil.getPathContext() + "/stork-plugin-0.5.0-SNAPSHOT/storkServlet";

%>

<% if (storkEnabled.equals("true")) { %>
<li>
<a href="<%= storkAuthURL%>">
    <img src="/html/portlet/login/navigation/STORK logo.gif"/>Stork
</a>
</li>
<% } %>


