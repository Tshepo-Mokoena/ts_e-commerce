package com.tshepo.service;

import com.tshepo.persistence.Account;

public interface IRegistrationService {
	
	Account registerAccount(Account account);

	Account confirmAccount(String token);

	void resendConfirmationToken(Account account);

	void resetPassword(String email);
	
}
