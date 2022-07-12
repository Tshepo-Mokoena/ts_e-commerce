package com.tshepo.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Order;
import com.tshepo.persistence.OrderItem;
import com.tshepo.service.IAccountService;
import com.tshepo.service.IOrderItemService;
import com.tshepo.service.IOrderService;
import com.tshepo.service.IRegistrationService;

@RestController
@RequestMapping("/api/ts-ecommerce/accounts")
public class AccountController {
	
	private IAccountService accountService;
	
	private IRegistrationService registrationService;
	
	private IOrderService orderService;
	
	private IOrderItemService orderItemService;
	
	@Autowired
	private void setAccountController(IOrderItemService orderItemService, IOrderService orderService, IAccountService accountService, IRegistrationService registrationService)
	{
		this.accountService = accountService;
		this.registrationService = registrationService;
		this.orderItemService = orderItemService;
		this.orderService = orderService;
	}
	
	@GetMapping
	public ResponseEntity<?> getAccounts() 
	{
		return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);		
	}
	
	@GetMapping("/{email}")
	public ResponseEntity<?> getAccount(@PathVariable String email) 
	{
		return new ResponseEntity<>(findAccount(email), HttpStatus.OK);		
	}
	
	@GetMapping("/{email}/resend-token")
	public ResponseEntity<?> resendConfirmToken(@PathVariable String email) 
	{
		registrationService.resendConfirmationToken(findAccount(email));
		return new ResponseEntity<>(HttpStatus.OK);		
	}

	@PutMapping("/{email}/lock-account")
	public ResponseEntity<?> lockAccount(@PathVariable String email) 
	{
		return new ResponseEntity<>(accountService.lockStatus(findAccount(email), true), HttpStatus.OK);		
	}
	
	@PutMapping("/{email}/unlock-account")
	public ResponseEntity<?> unlockAccount(@PathVariable String email) 
	{
		return new ResponseEntity<>(accountService.lockStatus(findAccount(email), false), HttpStatus.OK);		
	}

	private Account findAccount(String email) 
	{
		return accountService.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));		
	}
	
}
