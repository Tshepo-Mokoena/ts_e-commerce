package com.tshepo.service;

import java.util.List;
import java.util.Optional;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.auth.Role;

public interface IAccountService {
	
	Optional<Account> findByEmail(String email);
	
	Account signUp(Account account);
	
	void enableAccount(String email);
	
	String passwordReset(Account account);

	List<Account> getAllAccounts();

	void changeRole(String email, Role admin);

	Optional<Account> findByAccountId(String accountId);

	Account lockStatus(Account account, Boolean status);

	void saveAccount(Account account);

}
