package com.tshepo.service.impl;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tshepo.exception.AccountEnabledException;
import com.tshepo.exception.EmailExistException;
import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.exception.LinkExpiredOrNotValidException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.tokens.ConfirmationToken;
import com.tshepo.service.IAccountService;
import com.tshepo.service.IAuthenticationService;
import com.tshepo.service.IConfirmationTokenService;
import com.tshepo.service.IEmailService;
import com.tshepo.service.IRegistrationService;
import com.tshepo.util.AppConstants;
import com.tshepo.util.EmailTemplates;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService implements IRegistrationService{
		
	private IAccountService accountService;
	
	private IConfirmationTokenService confirmationTokenService;
	
	private IEmailService emailService;
	
	private IAuthenticationService authenticationService;
	
	private AppConstants appConstants;
		
	@Override
	public Account registerAccount(Account account)  
	{
		if(accountService.findByEmail(account.getEmail()).isPresent())
			throw new EmailExistException();
		
		Account currentAccount = accountService.signUp(account);
		 
		String token = confirmationTokenService.generateConfirmationToken(currentAccount);
		log.info(token);
		String CONFIRMATION_URL = appConstants.appConfirmUrl();
		log.info(CONFIRMATION_URL); 
		 
		emailService.sendEmail(currentAccount.getEmail(), "Confirm your Account", 
				EmailTemplates.comfirmAccount(currentAccount.getFirstName(), CONFIRMATION_URL + token));		  
		 
		return currentAccount;
		 
	}
	
	@Override
	public Account confirmAccount(String token)
	{
		ConfirmationToken currentConfirmationToken = confirmationTokenService.findByToken(token)
				.orElseThrow(() -> new LinkExpiredOrNotValidException());
		
		if(currentConfirmationToken.getConfirmedAt() != null)
			throw new LinkExpiredOrNotValidException();
		
		if(currentConfirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
			throw new LinkExpiredOrNotValidException();
				
		confirmationTokenService.confirmedAt(token);
		accountService.enableAccount(currentConfirmationToken.getAccount().getEmail());
		emailService.sendEmail(currentConfirmationToken.getAccount().getEmail(), "Account Confirmed", 
				EmailTemplates.accountConfirmed(currentConfirmationToken.getAccount().getFirstName()));	
		
		return currentConfirmationToken.getAccount();
	}
	
	@Override
	public void resendConfirmationToken(Account account) 
	{		
		Account authenticatedAccount = authenticationService.isValidAccount(account);		
		List<ConfirmationToken> confirmationTokens = confirmationTokenService.findByAccount(authenticatedAccount);
		for(ConfirmationToken token: confirmationTokens)
		{
			if(token.getConfirmedAt() != null)
			{
				if(authenticatedAccount.isActive())
					throw new AccountEnabledException();
				else 
					accountService.enableAccount(authenticatedAccount.getEmail());
			}				
		}		
		String token = confirmationTokenService.generateConfirmationToken(authenticatedAccount);
		String CONFIRMATION_URL = appConstants.appConfirmUrl();		
		log.info(CONFIRMATION_URL);
		emailService.sendEmail(account.getEmail(), "Confirm your Account", CONFIRMATION_URL + token);
		
	}	

	@Override
	public void resetPassword(String email) 
	{
		Account account = accountService.findByEmail(email)
				.orElseThrow(() -> new EmailNotFoundException());
		
		String password = accountService.passwordReset(account);
		
		log.info(password);
		//TODO SEND RESET EMAIL
		String PASSWORD_RESET_URL = appConstants.appConfirmUrl();
		String token = confirmationTokenService.generateConfirmationToken(account);
		emailService.sendEmail(account.getEmail(), "Confirm your Account", PASSWORD_RESET_URL + token);
	}	
		
}
