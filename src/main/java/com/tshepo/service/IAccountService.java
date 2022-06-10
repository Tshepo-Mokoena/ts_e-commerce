package com.tshepo.service;

import java.util.List;
import java.util.Optional;

import com.tshepo.persistence.Account;

public interface IAccountService {
	
	Optional<Account> findByEmail(String email);
	
	Account signUp(Account account);
	
	void enableAccount(String email);
	
	String passwordReset(Account account);

	List<Account> getAllAccounts();

}
