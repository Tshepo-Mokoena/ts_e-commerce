package com.tshepo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tshepo.exception.UnauthorizedException;
import com.tshepo.util.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManagersApiAuthenticationFilter extends OncePerRequestFilter{
	
	private String accessKey;
	
	public ManagersApiAuthenticationFilter(String accessKey)
	{
		this.accessKey = accessKey;	
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException 
	{
		return !request.getRequestURI().startsWith("/api/ts-ecommerce/internal-operations");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{	
		
		try {

			String requestKey = SecurityUtil.extractAuthTokenFromRequest(request);

			if (requestKey == null || !requestKey.equals(accessKey)) {
				log.warn("manager endpoint rquested without/wrong key uri {}");
				throw new UnauthorizedException();
			}

			AccountPrincipal account = AccountPrincipal.createdSuperUser();
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account, null,
					account.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (Exception e) {
			logger.error("could not set account auth in security context", e);
		}

		filterChain.doFilter(request, response);
	}

}
