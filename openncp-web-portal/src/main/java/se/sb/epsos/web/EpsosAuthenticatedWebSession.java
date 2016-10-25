/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;

import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.auth.jaas.JaasGrantedAuthorityPrincipal;
import se.sb.epsos.web.model.BreadCrumbVO;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.NcpServiceFacade;
import se.sb.epsos.web.util.Feature;
import se.sb.epsos.web.util.FeatureFlagsManager;
import se.sb.epsos.web.util.MasterConfigManager;

public class EpsosAuthenticatedWebSession extends AuthenticatedWebSession {
    private static final long serialVersionUID = -8046060728516010324L;
    private List<BreadCrumbVO<?>> breadcrumbList = new ArrayList<BreadCrumbVO<?>>();
    private static final Logger LOGGER = LoggerFactory.getLogger(EpsosAuthenticatedWebSession.class);
    @SpringBean(name = "authenticationManager")
    private AuthenticationManager authenticationManager;
    @SpringBean(name = "serviceFacade")
    private NcpServiceFacade serviceFacade;
    private AuthenticatedUser authenticatedUser;

    public EpsosAuthenticatedWebSession(Request request) {
    	super(request);
    	injectDependencies();
        ensureDependenciesNotNull();

    	String locale = MasterConfigManager.get("ApplicationConfigManager.locale");

		Cookie cookie = ((WebRequest)request).getCookie("epSOSlocale");
		if (cookie != null) {
			locale = cookie.getValue();
		}

        if (locale != null && locale.contains("_")) {
            String[] localeSplit = locale.split("_");
            if (localeSplit.length == 2) {
                setLocale(new Locale(localeSplit[0], localeSplit[1]));
            } else {
                setLocale(Locale.ENGLISH);
            }
        } else {
            setLocale(Locale.ENGLISH);
        }
        LOGGER.info("ServiceFacade initialized with: " + serviceFacade.about());
    }

    private void ensureDependenciesNotNull() {
        if (authenticationManager == null) {
            throw new IllegalStateException("AdminSession requires an authenticationManager.");
        }
    }

    private void injectDependencies() {
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    public boolean authenticate(String username, String password) {
        boolean authenticated = false;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            authenticated = authentication.isAuthenticated();
            LOGGER.info("LOGGED IN: " + authenticated);
        } catch (AuthenticationException e) {
            LOGGER.warn(String.format("User '%s' failed to login. Reason: %s", username, e.getMessage()));
            authenticated = false;
        }
        return authenticated;
    }

    @Override
    public Roles getRoles() {
        Roles roles = new Roles();
        getRolesIfSignedIn(roles);
        return roles;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public AuthenticatedUser getUserDetails() {
        Authentication auth = getAuthentication();
        if(FeatureFlagsManager.check(Feature.ENABLE_SWEDISH_JAAS)){
            if(auth != null) {
                if(authenticatedUser == null) {
                    JaasGrantedAuthority jaasGrantedAuthority = null;
                    for(GrantedAuthority grantedAuthority: auth.getAuthorities()) {
                    	jaasGrantedAuthority = (JaasGrantedAuthority)grantedAuthority;
                        LOGGER.info("grantedAuthority:{}", jaasGrantedAuthority.getPrincipal().getName());
                        LOGGER.info("grantedAuthority.getAuthority():{}", jaasGrantedAuthority.getAuthority());
                        JaasGrantedAuthorityPrincipal authorityPrincipal = new JaasGrantedAuthorityPrincipal(jaasGrantedAuthority.getPrincipal());
                        authenticatedUser = new AuthenticatedUser(authorityPrincipal.getId(), null);
                        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                        GrantedAuthority granted = new JaasGrantedAuthority(jaasGrantedAuthority.getAuthority(), jaasGrantedAuthority.getPrincipal());
                        authorities.add(granted);
                        authenticatedUser.setAuthorities(authorities);
                        authenticatedUser.setFamilyName(authorityPrincipal.getLastName());
                        authenticatedUser.setGivenName(authorityPrincipal.getFirstName());
                        authenticatedUser.setUserId(authorityPrincipal.getOrganizationId()); 
                        authenticatedUser.setOrganizationName(authorityPrincipal.getOrganizationName());
                        authenticatedUser.setOrganizationId(authorityPrincipal.getOrganizationId());
                        authenticatedUser.setCommonName(authorityPrincipal.getCommonName());
                        authenticatedUser.setPrmiaryRole(jaasGrantedAuthority.getAuthority());
                    }
                    getServiceFacade().bindToSession(this.getId());
                    try {
                       getServiceFacade().initUser(authenticatedUser);
                    } catch (NcpServiceException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
                return authenticatedUser;
            } 
            return null;
        } else {
            return auth != null ? (AuthenticatedUser) auth.getPrincipal() : null;
        }
    }

    private void getRolesIfSignedIn(Roles roles) {
        if (isSignedIn()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            addRolesFromAuthentication(roles, authentication);
        }
    }

    private void addRolesFromAuthentication(Roles roles, Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(authority.getAuthority());
        }
    }

    /**
     * Used by test to set a mockito mocked facade.
     *
     * @param serviceFacade
     */
    public void setServiceFacade(NcpServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

    public NcpServiceFacade getServiceFacade() {
        return serviceFacade;
    }

    public void removeAllAfterBc(BreadCrumbVO<?> bcVo) {
        for (int i = 0; i < breadcrumbList.size(); i++) {
            if (breadcrumbList.get(i).getTitle().equals(bcVo.getTitle())) {
                for (int j = breadcrumbList.size() - 1; j > i; j--) {
                    breadcrumbList.remove(j);
                }
                break;
            }
        }
    }

    public void addToBreadCrumbList(BreadCrumbVO<?> bcVo) {
        if (bcVo.getWebPage() != null) {
            if (!isClassInBreadCrumbList(bcVo.getWebPage().getClass()) && !isTitleInBreadCrumbList(bcVo.getTitle())) {
                breadcrumbList.add(bcVo);
            }
        } else if (!isTitleInBreadCrumbList(bcVo.getTitle())) {
            breadcrumbList.add(bcVo);
        }
    }

    public List<BreadCrumbVO<?>> getBreadCrumbList() {
        return breadcrumbList;
    }

    public boolean isTitleInBreadCrumbList(String title) {
        for (int i = 0; i < breadcrumbList.size(); i++) {
            if (breadcrumbList.get(i).getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    public boolean isClassInBreadCrumbList(Class<? extends WebPage> page) {
        for (int i = 0; i < breadcrumbList.size(); i++) {
            if (breadcrumbList.get(i).getWebPage() != null && breadcrumbList.get(i).getWebPage().getClass() == page) {
                return true;
            }
        }
        return false;
    }

    public void clearBreadCrumbList() {
        breadcrumbList.clear();
    }

    public void removePostFromBreadCrumbList(String title) {
        for (int i = 0; i < breadcrumbList.size(); i++) {
            if (breadcrumbList.get(i).getTitle().equals(title)) {
                breadcrumbList.remove(i);
            }
        }
    }

    public void removeAllAfterTitle(String title) {
        for (int i = 0; i < breadcrumbList.size(); i++) {
            if (breadcrumbList.get(i).getTitle().equals(title)) {
                for (int j = breadcrumbList.size() - 1; j > i; j--) {
                    breadcrumbList.remove(j);
                }
                break;
            }
        }
    }

    public void disableBreadCrumbClickability(String title) {
        for (int i = 0; i < breadcrumbList.size(); i++) {
            if (breadcrumbList.get(i).getTitle().equals(title)) {
                breadcrumbList.get(i).setClickable(false);
            }
        }
    }
}
