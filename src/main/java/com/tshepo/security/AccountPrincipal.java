package com.tshepo.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.auth.Role;
import com.tshepo.util.SecurityUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountPrincipal implements UserDetails{
	
	private Long id;
	
	private String email;
	
	transient  private String password;
	
	private Set<GrantedAuthority> authorities;
	
	transient  private Account account;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() 
	{		
		return authorities;
	}

	@Override
	public String getPassword() 
	{
		return password;
	}

	@Override
	public String getUsername() 
	{
		return email;
	}

	@Override
	public boolean isAccountNonExpired() 
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked() 
	{
		return !account.isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() 
	{
		return true;
	}

	@Override
	public boolean isEnabled() 
	{
		return account.isActive();
	}

	public static AccountPrincipal createdSuperUser() {
		
		Set<GrantedAuthority> authorities =
				Set.of(SecurityUtil.convertToAuthority(Role.SYSTEM_MANAGER.name()));
		
		return AccountPrincipal.builder()
			.id(-1L)
			.email("tshepo.william.mokoena@gmail.com")
			.authorities(authorities)
			.build();		
	}

}