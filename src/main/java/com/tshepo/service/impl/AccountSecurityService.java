package com.tshepo.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.repository.IAccountRepository;
import com.tshepo.security.AccountPrincipal;
import com.tshepo.util.SecurityUtil;

@Service
public class AccountSecurityService implements UserDetailsService{
	
	@Autowired
	private IAccountRepository accountRepository;	

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException 
	{
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Email not found: "+ email));
		
		Set<GrantedAuthority> authorities = Set.of(SecurityUtil.convertToAuthority(account.getRole().name()));
		
		return AccountPrincipal.builder()
				.account(account)
				.id(account.getId())
				.email(account.getEmail())
				.password(account.getPassword())
				.authorities(authorities)
				.build();
	}	

}
