package com.tshepo.util;

import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SecurityUtil {
	
	private static final String SALT = "salt";
	
	public static final String ROLE_PREFIX = "ROLE_";
	
	public static final String AUTH_HEADER = "authorization";
	
	public static final String AUTH_TOKEN_TYPE = "Bearer";
	
	public static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " ";
	
	public static SimpleGrantedAuthority convertToAuthority(String role)
	{
		String formattedRole = role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX +role;
		return new SimpleGrantedAuthority(formattedRole);
	}
	
	public static String extractAuthTokenFromRequest(HttpServletRequest req)
	{
		String bearerToken = req.getHeader(AUTH_HEADER);
		
		if(StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_PREFIX))
			return bearerToken.substring(7);
		
		return null;
	}
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() 
	{
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}

}
