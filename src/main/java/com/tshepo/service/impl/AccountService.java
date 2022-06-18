package com.tshepo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.auth.Role;
import com.tshepo.persistence.repository.IAccountRepository;
import com.tshepo.service.IAccountService;
import com.tshepo.util.SecurityUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService implements IAccountService{
	
	private IAccountRepository accountRepository;

	@Transactional
	@Override
	public Account signUp(Account account) 
	{
		Cart cart = 
				Cart.setCart(new BigDecimal(0), account, LocalDateTime.now(), LocalDateTime.now());		
		account.setAccountId(generateAccountId());
		account.setPassword(SecurityUtil.passwordEncoder().encode(account.getPassword()));
		account.setRole(Role.USER);
		account.setCart(cart);
		account.setCreatedAt(LocalDateTime.now());		
		return accountRepository.save(account);
	}	

	@Override
	public void enableAccount(String email) 
	{		
		accountRepository.enableAccount(email, true);
	}

	@Transactional
	@Override
	public String passwordReset(Account account)
	{		
		String password = generatePassword();
		account.setPassword(SecurityUtil.passwordEncoder().encode(password));
		accountRepository.save(account);
		log.info(password);
		return password;
	}

	@Override
	public Optional<Account> findByEmail(String email) 
	{
		return accountRepository.findByEmail(email);
	}
	
	private String generateAccountId()
	{
		return RandomStringUtils.randomNumeric(15);
	}
	
	private String generatePassword()
	{
		return RandomStringUtils.randomAlphanumeric(10);
	}

	@Override
	public List<Account> getAllAccounts() 
	{
		return (List<Account>) accountRepository.findAll();
	}

}