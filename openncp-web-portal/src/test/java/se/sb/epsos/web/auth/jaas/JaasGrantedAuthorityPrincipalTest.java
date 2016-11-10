package se.sb.epsos.web.auth.jaas;

import org.junit.Test;

import java.security.Principal;

import static org.junit.Assert.*;

/**
 * Created:
 * Date: 2013-09-19
 * Time: 08:49
 */
public class JaasGrantedAuthorityPrincipalTest {

    @Test
    public void shouldReturnValidData(){
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "Donald.Duck@apse.se;Donald;Duck;Apoteket;987123567123";
            }
        };
        JaasGrantedAuthorityPrincipal grantedAuthorityPrincipal = new JaasGrantedAuthorityPrincipal(principal);
        assertEquals(grantedAuthorityPrincipal.getId(), "Donald.Duck@apse.se");
        assertEquals(grantedAuthorityPrincipal.getFirstName(), "Donald");
        assertEquals(grantedAuthorityPrincipal.getLastName(), "Duck");
        assertEquals(grantedAuthorityPrincipal.getCommonName(), "Donald Duck");
        assertEquals(grantedAuthorityPrincipal.getOrganizationName(), "Apoteket");
        assertEquals(grantedAuthorityPrincipal.getOrganizationId(), "987123567123");
    }

    @Test
    public void shouldReturnInValidDataWhenTooFewFields(){
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "Donald.Duck@apse.se;Donald;Duck;Apoteket";
            }
        };
        JaasGrantedAuthorityPrincipal grantedAuthorityPrincipal = new JaasGrantedAuthorityPrincipal(principal);
        assertEquals(grantedAuthorityPrincipal.getId(), null);
        assertEquals(grantedAuthorityPrincipal.getFirstName(), null);
        assertEquals(grantedAuthorityPrincipal.getLastName(), null);
        assertEquals(grantedAuthorityPrincipal.getCommonName(), null);
        assertEquals(grantedAuthorityPrincipal.getOrganizationName(), null);
    }

    @Test
    public void shouldReturnValidDataWhenTooManyFields(){
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "Donald.Duck@apse.se;Donald;Duck;Apoteket;987123567123;extra field";
            }
        };
        JaasGrantedAuthorityPrincipal grantedAuthorityPrincipal = new JaasGrantedAuthorityPrincipal(principal);
        assertEquals(grantedAuthorityPrincipal.getId(), "Donald.Duck@apse.se");
        assertEquals(grantedAuthorityPrincipal.getFirstName(), "Donald");
        assertEquals(grantedAuthorityPrincipal.getLastName(), "Duck");
        assertEquals(grantedAuthorityPrincipal.getCommonName(), "Donald Duck");
        assertEquals(grantedAuthorityPrincipal.getOrganizationName(), "Apoteket");
        assertEquals(grantedAuthorityPrincipal.getOrganizationId(), "987123567123");
    }

}
