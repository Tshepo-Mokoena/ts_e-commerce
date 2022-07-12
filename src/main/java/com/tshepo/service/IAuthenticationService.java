package com.tshepo.service;

import com.tshepo.persistence.Account;

public interface IAuthenticationService {
	
	Account signInAndReturnJwt(Account account);

	Account isValidAccount(String email, String password);

}
