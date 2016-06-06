<%@page import="com.liferay.portal.security.auth.StorkHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Stork Login Page 1</title>       
    </head>
    <body>
        
     <div style="display:none;">  
        <form id="storkForm" action="<%= StorkHelper.getPepsURL() %>" method="POST">
            <input type="text" name="country" value="<%= StorkHelper.getPepsCountry() %>" />
            <input type="text" name="SAMLRequest" value="<%= StorkHelper.createStorkSAML() %>" />
            <input type="submit" />
        </form>
     </div>
            
    <script type="text/javascript">
        document.getElementById("storkForm").submit();
    </script>        
            
            
    </body>
</html>
