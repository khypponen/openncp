/**
 *    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
 *
 *    This file is part of epSOS-WEB.
 *
 *    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
 **/
package se.sb.epsos.web.auth;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUserDetailsService.class);

    private Resource usersXML;

    private XMLConfiguration config;

    @Override
    public UserDetails loadUserByUsername(String username) {

        LOGGER.info("Checking userinfo for username '{}'", username);
        if (config == null) {
            try {
                init();
            } catch (ConfigurationException e) {
                throw new DataAccessException("Failed to load users from XML") {
                    private static final long serialVersionUID = -3848074168433511697L;
                };
            } catch (IOException e) {
                throw new DataAccessException("Failed to read XML file") {
                    private static final long serialVersionUID = 2763171318518245251L;
                };
            }
        }
        String passwd = config.getString("Users." + username + "[@password]");
        if (passwd == null) {
            LOGGER.info("Username '{}' was not found in catalogue", username);
            throw new UsernameNotFoundException("User " + username + " was not recognised");
        }
        LOGGER.info("Username '{}' was found in catalogue, loading user details", username);
        AuthenticatedUser userDetails = new AuthenticatedUser(username, passwd);
        userDetails.setCommonName(config.getString("Users." + username + ".commonName"));
        userDetails.setOrganizationId(config.getString("Users." + username + ".organizationId"));
        userDetails.setOrganizationName(config.getString("Users." + username + ".organizationName"));
        userDetails.setUserId(config.getString("Users." + username + ".userId"));
        userDetails.setGivenName(config.getString("Users." + username + ".givenName"));
        userDetails.setFamilyName(config.getString("Users." + username + ".familyName"));
        userDetails.setTelecom(config.getString("Users." + username + ".telecom"));
        userDetails.setStreet(config.getString("Users." + username + ".street"));
        userDetails.setPostalCode(config.getString("Users." + username + ".postalCode"));
        userDetails.setCity(config.getString("Users." + username + ".city"));

        @SuppressWarnings("unchecked")
        List<String> roles = config.getList("Users." + username + ".roles.role");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        userDetails.setAuthorities(authorities);
        LOGGER.info("Loaded AuthenticatedUser: '{}'", userDetails.toString());
        return userDetails;
    }

    private void init() throws ConfigurationException, IOException {
        LOGGER.info("Initializing user catalogue from XML in '{}'", usersXML.getURL().getPath());
        if (usersXML == null) {
            throw new ConfigurationException("users xml config file path not set");
        }
        config = new XMLConfiguration(usersXML.getURL());
    }

    public Resource getUsersXML() {
        return usersXML;
    }

    public void setUsersXML(Resource usersXML) {
        this.usersXML = usersXML;
    }
}
