package com.tshepo.resources.Internal;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.auth.Role;
import com.tshepo.service.IAccountService;

@RestController
@RequestMapping("/api/ts-ecommerce/internal/accounts")
public class InternalAccountController {
	
	private IAccountService accountService;
	
	@Autowired
	private void setInternalAccountController(IAccountService accountService){ this.accountService = accountService; }
	
	@GetMapping
	public ResponseEntity<?> getAccounts(@RequestParam("keyword") Optional<String> keyword, 
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("pageSize") Optional<Integer> pageSize) 
	{
		return new ResponseEntity<>(accountService.getAccounts(keyword.orElse(""), page.orElse(0), pageSize.orElse(10)), HttpStatus.OK);		
	}
	
	@GetMapping("/{email}")
	public ResponseEntity<?> getAccount(@PathVariable String email)
	{
		return new ResponseEntity<>(findByEmail(email).orElseThrow(()-> new EmailNotFoundException(email)), HttpStatus.OK);
	}
		
	@PostMapping
	public ResponseEntity<?> updateAccount(@Valid @RequestBody Account account)
	{		
		return new ResponseEntity<>(accountService.saveAccount(account), HttpStatus.OK);
	}
	
	@GetMapping("/{email}/lock")
	public ResponseEntity<?> lockAccount(@PathVariable String email) 
	{
		accountService.lockStatus(findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email)), true);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{email}/unlock")
	public ResponseEntity<?> unlockAccount(@PathVariable String email) 
	{		
		accountService.lockStatus(findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email)), false);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{email}/make-admin")
	public ResponseEntity<?> makeAdmin(@PathVariable String email) 
	{
		accountService.changeRole(email, Role.ADMIN);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{email}/disable-admin")
	public ResponseEntity<?> disableAdmin(@PathVariable String email) 
	{
		accountService.changeRole(email, Role.USER);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private Optional<Account> findByEmail(String email){ return accountService.findByEmail(email); }
	
		
}
