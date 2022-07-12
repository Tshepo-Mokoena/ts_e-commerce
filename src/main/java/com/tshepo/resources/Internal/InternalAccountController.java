package com.tshepo.resources.Internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.persistence.auth.Role;
import com.tshepo.service.IAccountService;

@RestController
@RequestMapping("/api/ts-ecommerce/internal/accounts")
public class InternalAccountController {
	
	private IAccountService accountService;
	
	@Autowired
	private void setInternalAccountController(IAccountService accountService)
	{
		this.accountService = accountService;
	}
	
	@GetMapping
	public ResponseEntity<?> getAccounts() 
	{
		return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);		
	}
	
	@PutMapping("/{email}/make-admin")
	public ResponseEntity<?> makeAdmin(@PathVariable String email) 
	{
		accountService.changeRole(email, Role.ADMIN);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/{email}/disable-admin")
	public ResponseEntity<?> disableAdmin(@PathVariable String email) 
	{
		accountService.changeRole(email, Role.USER);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
		
}
