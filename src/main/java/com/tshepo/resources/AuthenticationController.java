package com.tshepo.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.persistence.Account;
import com.tshepo.service.IAccountService;
import com.tshepo.service.IAuthenticationService;
import com.tshepo.service.IRegistrationService;

@RestController
@RequestMapping("api/ts-ecommerce/authentication/")
public class AuthenticationController {
	
	@Autowired
	private IRegistrationService registrationService;
	
	@Autowired
	private IAuthenticationService authenticationService;
	
	@Autowired
	private IAccountService accountService;
	
	@PostMapping("sign-in")
	public ResponseEntity<?> signIn(@RequestBody Account account) 
	{	
		return new ResponseEntity<>(authenticationService.signInAndReturnJwt(account), HttpStatus.OK);
	}
	
	@PostMapping("register")
	public ResponseEntity<?> registerAccount(@RequestBody @Valid Account account) 
	{			
		return new ResponseEntity<>(registrationService.registerAccount(account), HttpStatus.CREATED);
	}
	
	@GetMapping("confirm-account/{token}")
	public ResponseEntity<?> confirmAccount(@PathVariable String token) 
	{			
		return new ResponseEntity<>(registrationService.confirmAccount(token), HttpStatus.OK);
	}
	
	@PostMapping("resend-confirm-link")
	public ResponseEntity<?> resendConfirmLink(@RequestBody Account account) 
	{			
		registrationService.resendConfirmationToken(account);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("reset-password/{email}")
	public ResponseEntity<?> resetPassword(@PathVariable String email) 
	{			
		registrationService.resetPassword(email);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("get-accounts")
	public ResponseEntity<List<Account>> getAccounts() 
	{			
		return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
	}
	
}
