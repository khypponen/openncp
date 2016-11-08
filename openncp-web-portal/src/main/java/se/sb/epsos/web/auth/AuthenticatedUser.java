/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.opensaml.saml2.core.Assertion;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import se.sb.epsos.web.model.TRC;

public class AuthenticatedUser implements Serializable, UserDetails {
	private static final long serialVersionUID = -3571113493930947811L;

	private Collection<GrantedAuthority> authorities;
	private final String password;
	private final String username;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	private String userId;
	private String commonName;
	private String givenName;
	private String familyName;
	private String organizationName;
	private String organizationId;
	private String telecom;
	private String telefax;
	private String street;
	private String city;
	private String postalCode;
	private List<Locale> locales = new ArrayList<Locale>();
	private List<String> roles;
	private TRC trc;

	transient private Assertion assertion;
	transient private Assertion trcAssertion;

	public AuthenticatedUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
		this.roles = new ArrayList<String>();
		for (GrantedAuthority auth : authorities) {
			this.roles.add(auth.getAuthority());
		}
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExired) {
		this.accountNonExpired = accountNonExired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setAssertion(Assertion assertion) {
		this.assertion = assertion;
	}

	public void setTrcAssertion(Assertion assertion) {
		this.trcAssertion = assertion;
	}

	public TRC getTrc() {
		return this.trc;
	}

	public void setTrc(TRC trc) {
		this.trc = trc;
	}

	public Assertion getAssertion() {
		return assertion;
	}

	public Assertion getTrcAssertion() {
		return trcAssertion;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getTelecom() {
		return telecom;
	}

	public void setTelecom(String telecom) {
		this.telecom = telecom;
	}

	public String getTelefax() {
		return telefax;
	}

	public void setTelefax(String telefax) {
		this.telefax = telefax;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public boolean isPharmaceut() {
		return !this.roles.isEmpty() && this.roles.get(0).equals("ROLE_PHARMACIST");
	}

	public boolean isDoctor() {
		return !this.roles.isEmpty() && this.roles.get(0).equals("ROLE_DOCTOR");
	}

	public boolean isNurse() {
		return !this.roles.isEmpty() && this.roles.get(0).equals("ROLE_NURSE");
	}

	public boolean isAdmin() {
		return !this.roles.isEmpty() && this.roles.contains("ROLE_ADMIN");
	}

	public List<Locale> getLocales() {
		return locales;
	}

	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}

	public void setPrmiaryRole(String newRole) {
		if (newRole != null && newRole.length() > 0 && this.roles.indexOf(newRole) != -1) {
			int currentIndex = this.roles.indexOf(newRole);
			String currentPrimaryRole = this.roles.get(0);
			this.roles.set(0, newRole);
			this.roles.set(currentIndex, currentPrimaryRole);
		}
	}

	@Override
	public String toString() {
		return username + "[name=" + commonName + ", organization=" + organizationName + ":" + organizationId + ", roles="
				+ (roles != null ? Arrays.toString(roles.toArray()) : "") + "]";
	}
}
