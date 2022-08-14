package com.tshepo.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Address;
import com.tshepo.persistence.Payment;
import com.tshepo.requests.PasswordResetRequest;
import com.tshepo.security.AccountPrincipal;
import com.tshepo.service.IAccountService;
import com.tshepo.service.IAddressService;
import com.tshepo.service.IAuthenticationService;
import com.tshepo.service.IPaymentService;
import com.tshepo.service.IRegistrationService;

@RestController
@RequestMapping("/api/ts-ecommerce/accounts")
public class AccountController {
	
	private IRegistrationService registrationService;
	
	private IAccountService accountService;
	
	private IPaymentService paymentService;
	
	private IAddressService addressService;
	
	private IAuthenticationService authenticationService;
	
	@Autowired
	private void setAccountController(IAuthenticationService authenticationService, IAccountService accountService, IRegistrationService registrationService, IPaymentService paymentService, IAddressService addressService) { 
		this.accountService = accountService;
		this.registrationService = registrationService;
		this.paymentService = paymentService;
		this.addressService = addressService;
		this.authenticationService = authenticationService;
	}
	
	@PostMapping
	public ResponseEntity<?> updateAccount(@AuthenticationPrincipal AccountPrincipal accountPrincipal, @Valid @RequestBody Account account)
	{
		Account signInAccount = findAccount(accountPrincipal.getEmail());
		if (signInAccount.getId() != account.getId()) throw new RuntimeException();
		
		signInAccount.setFirstName(account.getFirstName());
		signInAccount.setLastName(account.getLastName());
		signInAccount.setPhone(account.getPhone());		
		accountService.saveAccount(signInAccount);
		return new ResponseEntity<>( HttpStatus.OK);
	}
	
	@PostMapping("/password")
	public ResponseEntity<?> updatePassword(@AuthenticationPrincipal AccountPrincipal accountPrincipal, @Valid @RequestBody PasswordResetRequest passwordResetRequest)
	{
		if (!accountPrincipal.getEmail().contains(passwordResetRequest.getEmail())) throw new RuntimeException();
		registrationService.newPassword( passwordResetRequest.getEmail(), passwordResetRequest.getPassword(), passwordResetRequest.getNewPassword());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/payment")
	public ResponseEntity<?> getPayment(@AuthenticationPrincipal AccountPrincipal accountPrincipal)
	{
		return new ResponseEntity<>(paymentService.findByAccount(findAccount(accountPrincipal.getEmail())), HttpStatus.OK);
	}
	
	@PostMapping("/payment")
	public ResponseEntity<?> updatePayment(@AuthenticationPrincipal AccountPrincipal accountPrincipal, @Valid @RequestBody Payment payment)
	{
		Payment getPayment = paymentService.findByAccount(findAccount(accountPrincipal.getEmail()));
		if (payment.getId() != getPayment.getId())  throw new RuntimeException();
				
		return new ResponseEntity<>(paymentService.updatePayment(getPayment.updatePayment(payment)), HttpStatus.OK);
	}
	
	@GetMapping("/address")
	public ResponseEntity<?> getAddress(@AuthenticationPrincipal AccountPrincipal accountPrincipal)
	{
		return new ResponseEntity<>(addressService.findByAccount(findAccount(accountPrincipal.getEmail())), HttpStatus.OK);
	}
	
	@PostMapping("/address")
	public ResponseEntity<?> updateAddress(@AuthenticationPrincipal AccountPrincipal accountPrincipal,  @Valid @RequestBody Address address)
	{
		Address getAddress = addressService.findByAccount(findAccount(accountPrincipal.getEmail()));
		if (getAddress.getId() != address.getId())  throw new RuntimeException();
		return new ResponseEntity<>(addressService.updateAddress(getAddress.updateAddress(address)), HttpStatus.OK);
	}
	
	
	private Account findAccount(String email) 
	{
		return accountService.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
	}
	
}
