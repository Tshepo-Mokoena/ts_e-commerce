package com.tshepo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Address;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.Payment;
import com.tshepo.persistence.auth.Role;
import com.tshepo.persistence.repository.IAccountRepository;
import com.tshepo.service.IAccountService;
import com.tshepo.util.SecurityUtil;
import com.tshepo.util.Utilities;

@Service
public class AccountService implements IAccountService{
	
	private IAccountRepository accountRepository;

	@Autowired
	private void setAccountService(IAccountRepository accountRepository) {this.accountRepository = accountRepository;}
	
	@Override
	public Account signUp(Account account) 
	{				
		account.setAccountId(Utilities.generateUniqueNumericUUId());
		account.setPassword(SecurityUtil.passwordEncoder().encode(account.getPassword()));
		account.setRole(Role.USER);
		account.setActive(true);
		account.setCart(Cart.setCart(new BigDecimal(0), account, LocalDateTime.now(), LocalDateTime.now()));
		account.setAddress(Address.setAddress(account));
		account.setPayment(Payment.setPayment(account));
		account.setUpdatedAt(LocalDateTime.now());
		account.setCreatedAt(LocalDateTime.now());		
		return accountRepository.save(account);
	}	

	@Override
	public void enableAccount(String email) {accountRepository.enableAccount(email, true);}

	@Override
	public String passwordReset(Account account)
	{		
		String password = Utilities.generateUniqueAlphaNumericUUId();
		account.setPassword(SecurityUtil.passwordEncoder().encode(password));
		accountRepository.save(account);
		return password;
	}

	@Override
	public Optional<Account> findByEmail(String email) {return accountRepository.findByEmail(email);}
	
	@Override
	public void changeRole(String email, Role role) {accountRepository.changeRoles(email, role);}

	@Override
	public Optional<Account> findByAccountId(String accountId) {return accountRepository.findByAccountId(accountId);}

	@Override
	public Account lockStatus(Account account, Boolean status) 
	{
		account.setLocked(status);
		return accountRepository.save(account);
	}
	
	@Override
	public Account saveAccount(Account account)
	{
		account.setUpdatedAt(LocalDateTime.now());
		return accountRepository.save(account);
	}

	@Override
	public Page<Account> getAccounts(String keyword, int page, int pageSize){ return accountRepository.findByFirstNameContaining(keyword, PageRequest.of(page, pageSize));}

}