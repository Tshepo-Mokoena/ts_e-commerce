package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.repository.IConfirmationTokenRepository;
import com.tshepo.persistence.tokens.ConfirmationToken;
import com.tshepo.service.IConfirmationTokenService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService implements IConfirmationTokenService{
	
	private IConfirmationTokenRepository confirmationTokenRepository;

	@Override
	public Optional<ConfirmationToken> findByToken(String token) 
	{
		return confirmationTokenRepository.findByToken(token);
	}

	@Override
	public String generateConfirmationToken(Account account) 
	{
		
		String token = RandomStringUtils.randomAlphanumeric(40);
		
		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(30),
				account
				);
		
		confirmationTokenRepository.save(confirmationToken);
		
		return token;
	}

	@Override
	public void confirmedAt(String token) 
	{		
		confirmationTokenRepository.confirmedAt(token, LocalDateTime.now());
	}

	@Override
	public List<ConfirmationToken> findByAccount(Account account) 
	{
		return confirmationTokenRepository.findByAccount(account);
	}

}
