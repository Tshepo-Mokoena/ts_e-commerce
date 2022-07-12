package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.exception.AccountEnabledException;
import com.tshepo.exception.EmailExistException;
import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.exception.InvalidEmailOrPasswordException;
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
import com.tshepo.util.SecurityUtil;

@Service
public class RegistrationService implements IRegistrationService{
		
	private IAccountService accountService;
	
	private IConfirmationTokenService confirmationTokenService;
	
	private IEmailService emailService;
	
	private IAuthenticationService authenticationService;
	
	private AppConstants appConstants;
	
	
	@Autowired
	public void setRegistrationService(IAccountService accountService, IConfirmationTokenService confirmationTokenService,
			IEmailService emailService, IAuthenticationService authenticationService, AppConstants appConstants) {	
		this.accountService = accountService;
		this.confirmationTokenService = confirmationTokenService;
		this.emailService = emailService;
		this.authenticationService = authenticationService;
		this.appConstants = appConstants;
	}

	@Override
	public Account registerAccount(Account account)  
	{
		if(accountService.findByEmail(account.getEmail()).isPresent())
			throw new EmailExistException();
		
		Account currentAccount = accountService.signUp(account);
		 
		String token = confirmationTokenService.generateConfirmationToken(currentAccount);
		
		String CONFIRMATION_URL = appConstants.appConfirmUrl();
				 
		sendEmail(currentAccount, "Confirm your Account", 
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
		
		sendEmail(currentConfirmationToken.getAccount(), "Account Confirmed", 
				EmailTemplates.accountConfirmed(currentConfirmationToken.getAccount().getFirstName()));	
		
		return currentConfirmationToken.getAccount();
	}
	
	@Override
	public void resendConfirmationToken(Account account) 
	{		
		Account authenticatedAccount = authenticationService.isValidAccount(account.getEmail(), account.getPassword());		
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
				
		sendEmail(account, "Confirm your Account", appConstants.appConfirmUrl() + token);
		
	}	

	@Override
	public void resetPassword(String email) 
	{
		Account account = accountService.findByEmail(email)
				.orElseThrow(() -> new EmailNotFoundException());
		
		String password = accountService.passwordReset(account);
		
		sendEmail(account, "New Password", "Your New Password: " + password);
	}
	
	@Override
	public void newPassword(String email, String password, String newPassword)
	{
		Account validatedAccount = authenticationService.isValidAccount(email, password);
		
		if (StringUtils.isBlank(newPassword))
			throw new InvalidEmailOrPasswordException();
		
		validatedAccount.setPassword(SecurityUtil.passwordEncoder().encode(newPassword));
		
		accountService.saveAccount(validatedAccount);
		
		sendEmail(validatedAccount, "Password Reset Successful", "password was reset successfuly");
	}
	
	private void sendEmail(Account account, String Subject, String Email)
	{
		emailService.sendEmail(account.getEmail(), Subject, Email);
	}
		
}
