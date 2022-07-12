package com.tshepo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.auth.Role;
import com.tshepo.persistence.repository.IAccountRepository;
import com.tshepo.service.IAccountService;
import com.tshepo.util.SecurityUtil;
import com.tshepo.util.Utilities;

@Service
public class AccountService implements IAccountService{
	
	private IAccountRepository accountRepository;

	@Autowired
	private void setAccountService(IAccountRepository accountRepository)
	{
		this.accountRepository = accountRepository;
	}
	
	@Override
	public Account signUp(Account account) 
	{				
		account.setAccountId(Utilities.generateUniqueNumericUUId());
		account.setPassword(SecurityUtil.passwordEncoder().encode(account.getPassword()));
		account.setRole(Role.USER);
		account.setCart(Cart.setCart(new BigDecimal(0), account, LocalDateTime.now(), LocalDateTime.now()));
		account.setCreatedAt(LocalDateTime.now());		
		return accountRepository.save(account);
	}	

	@Override
	public void enableAccount(String email) 
	{		
		accountRepository.enableAccount(email, true);
	}

	@Override
	public String passwordReset(Account account)
	{		
		String password = Utilities.generateUniqueAlphaNumericUUId();
		account.setPassword(SecurityUtil.passwordEncoder().encode(password));
		accountRepository.save(account);
		return password;
	}

	@Override
	public Optional<Account> findByEmail(String email) 
	{
		return accountRepository.findByEmail(email);
	}
	
	@Override
	public List<Account> getAllAccounts() 
	{
		return (List<Account>) accountRepository.findAll();
	}

	@Override
	public void changeRole(String email, Role role) 
	{
		accountRepository.changeRoles(email, role);
	}

	@Override
	public Optional<Account> findByAccountId(String accountId) 
	{
		return accountRepository.findByAccountId(accountId);
	}

	@Override
	public Account lockStatus(Account account, Boolean status) 
	{
		account.setLocked(status);
		return accountRepository.save(account);
	}
	
	@Override
	public void saveAccount(Account account)
	{
		accountRepository.save(account);
	}

}