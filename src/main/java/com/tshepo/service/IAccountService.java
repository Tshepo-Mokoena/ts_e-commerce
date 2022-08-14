package com.tshepo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.auth.Role;

public interface IAccountService {
	
	Optional<Account> findByEmail(String email);
	
	Account signUp(Account account);
	
	void enableAccount(String email);
	
	String passwordReset(Account account);

	void changeRole(String email, Role admin);

	Optional<Account> findByAccountId(String accountId);

	Account lockStatus(Account account, Boolean status);

	Account saveAccount(Account account);

	Page<Account> getAccounts(String keyword, int page, int pageSize);

}
