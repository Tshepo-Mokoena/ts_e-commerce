package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.repository.IConfirmationTokenRepository;
import com.tshepo.persistence.tokens.ConfirmationToken;
import com.tshepo.service.IConfirmationTokenService;


@Service
public class ConfirmationTokenService implements IConfirmationTokenService{
	
	private IConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	private void setConfirmationTokenService(IConfirmationTokenRepository confirmationTokenRepository) 
	{
		this.confirmationTokenRepository = confirmationTokenRepository;
	}

	@Override
	public Optional<ConfirmationToken> findByToken(String token) 
	{
		return confirmationTokenRepository.findByToken(token);
	}

	@Override
	public String generateConfirmationToken(Account account) 
	{
		String token = RandomStringUtils.randomAlphanumeric(40);
		confirmationTokenRepository.save(
				ConfirmationToken.setConfirmationToken(token, account));
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
