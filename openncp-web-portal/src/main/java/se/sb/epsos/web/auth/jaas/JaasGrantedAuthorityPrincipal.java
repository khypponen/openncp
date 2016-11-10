package se.sb.epsos.web.auth.jaas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.security.Principal;

/**
 * This class parses user information passed to EpSOS-web from the JAAS module.
 * The information is 'hidden' in the JAAS Principal.
 */
public class JaasGrantedAuthorityPrincipal implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(JaasGrantedAuthorityPrincipal.class);
    private static final String DELIMITER = ";";

    private String id;
    private String firstName;
    private String lastName;
    private String organizationName;
    private String organizationId;
    private String commonName;

    public JaasGrantedAuthorityPrincipal(final Principal principal) {
        parsePrincipalName(principal.getName());
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getCommonName() {
        return commonName;
    }

    private void parsePrincipalName(String principalName){
        String[] fields = principalName.split(DELIMITER);
        if(fields.length < 5){
            logger.error("To few fields in Jaas principal:{}",principalName);
        }else{
            id = fields[0];
            firstName = fields[1];
            lastName = fields[2];
            organizationName = fields[3];
            organizationId = fields[4];
            commonName = firstName + " " + lastName;
        }
    }

}
