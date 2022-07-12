package com.tshepo.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.tshepo.exception.InvalidEmailOrPasswordException;
import com.tshepo.persistence.Account;
import com.tshepo.security.AccountPrincipal;
import com.tshepo.security.jwt.IJwtProvider;
import com.tshepo.service.IAccountService;
import com.tshepo.service.IAuthenticationService;
import com.tshepo.util.SecurityUtil;


@Service
public class AuthenticationService implements IAuthenticationService{
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private IJwtProvider jwtProvider;

	@Override
	public Account signInAndReturnJwt(Account account) 
	{		
		Authentication authentication = 
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
		
		AccountPrincipal accountPrincipal = (AccountPrincipal) authentication.getPrincipal();
		
		String jwt = jwtProvider.generateJwtToken(accountPrincipal);	
		
		Account signInAccount = accountPrincipal.getAccount();
		signInAccount.setToken(jwt);
		
		return signInAccount;
	}
	
	@Override
	public Account isValidAccount(String email, String password) 
	{
		if (StringUtils.isBlank(email) && StringUtils.isBlank(password))
			throw new InvalidEmailOrPasswordException();
		
		Optional<Account> currentAccount = accountService.findByEmail(email);
		
		if(!currentAccount.isPresent())
			throw new InvalidEmailOrPasswordException();
		
		if (isValidPassword(password, currentAccount.get().getPassword()))
			return currentAccount.get();
		else 
			throw new InvalidEmailOrPasswordException();
	}
	
	private boolean isValidPassword(String password, String accountPassword) 
	{
		if (SecurityUtil.passwordEncoder().matches(password, accountPassword))
			return true;		
		return false;
	}

}
