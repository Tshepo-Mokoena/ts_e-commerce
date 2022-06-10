package com.tshepo.service;

import com.tshepo.persistence.Account;

public interface IAuthenticationService {
	
	Account signInAndReturnJwt(Account account);

	Account isValidAccount(Account currentAccount);

}
