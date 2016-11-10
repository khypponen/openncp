package se.sb.epsos.web.auth.jaas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.jaas.AuthorityGranter;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

/**
 *  Used for JAAS authorization by spring security
 */
public class EpsosAuthorityGranter implements AuthorityGranter {
    private static final Logger logger = LoggerFactory.getLogger(EpsosAuthorityGranter.class);


    // TODO: Use the principal
    public Set<String> grant(Principal principal) {
        logger.info("EpsosAuthorityGranter, Principal name: {}", principal.getName());
        return Collections.singleton("ROLE_PHARMACIST");
    }
}
