package com.tshepo.security.jwt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import com.tshepo.security.AccountPrincipal;

public interface IJwtProvider {

	String generateJwtToken(AccountPrincipal auth);

	Authentication getAuthenticated(HttpServletRequest req);

	boolean validateToken(HttpServletRequest req);

}
