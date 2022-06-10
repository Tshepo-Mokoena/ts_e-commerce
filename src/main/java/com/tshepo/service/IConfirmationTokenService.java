package com.tshepo.service;

import java.util.List;
import java.util.Optional;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.tokens.ConfirmationToken;

public interface IConfirmationTokenService {
	
	Optional<ConfirmationToken> findByToken(String token);
	
	String generateConfirmationToken(Account account);
	
	void confirmedAt(String token);

	List<ConfirmationToken> findByAccount(Account account);

}
