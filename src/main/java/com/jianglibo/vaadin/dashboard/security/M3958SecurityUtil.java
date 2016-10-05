package com.jianglibo.vaadin.dashboard.security;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jianglibo.vaadin.dashboard.domain.Person;

/**
 * 
 * @author jianglibo@gmail.com
 *
 */
public class M3958SecurityUtil {

	public static void loginAs(Authentication au) {
		au.setAuthenticated(true);
		SecurityContextHolder.getContext().setAuthentication(au);
	}

	public static boolean isLogined() {
		Authentication au = SecurityContextHolder.getContext().getAuthentication();
		if (au == null) {
			return false;
		}

		if (au instanceof PersonAuthenticationToken) {
			return true;
		}

		return false;
	}

	public static Optional<PersonVo> getLoginPersonVo() {
		Authentication au = SecurityContextHolder.getContext().getAuthentication();
		if (au != null && au instanceof PersonAuthenticationToken) {
			return Optional.of((PersonVo) au.getPrincipal());
		} else {
			return Optional.empty();
		}
	}

	public static long getLoginPersonId() {
		if (getLoginPersonVo().isPresent()) {
			return getLoginPersonVo().get().getId();
		} else {
			return Long.MIN_VALUE;
		}
	}

	public static PersonAuthenticationToken getLoginAuthentication() {
		return (PersonAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	}

	public static void doLogin(Person person) {
		doLogin(SecurityContextHolder.getContext(), person);
	}

	public static void doLogin(PersonAuthenticationToken mat) {
		mat.setAuthenticated(true);
		SecurityContextHolder.getContext().setAuthentication(mat);
	}

	public static boolean hasRole(String rn) {
		if (getLoginAuthentication() == null) {
			return false;
		}
		if (getLoginAuthentication().isAuthenticated()) {
			return getLoginAuthentication().getAuthorities().stream().anyMatch(ga -> rn.equals(ga.getAuthority()));
		}
		return false;
	}

	public static void logout() {
		SecurityContextHolder.clearContext();
	}

	public static boolean hasAnyRole(String... rns) {
		return Stream.of(rns).anyMatch(M3958SecurityUtil::hasRole);
	}

	public static void doLogin(SecurityContext context, Person person) {
		PersonAuthenticationToken uat = new PersonAuthenticationToken(new PersonVo.PersonVoBuilder(person).build());
		uat.setAuthenticated(true);
		context.setAuthentication(uat);
		SecurityContextHolder.setContext(context);
	}
}
