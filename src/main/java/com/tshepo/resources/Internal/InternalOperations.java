package com.tshepo.resources.Internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tshepo.persistence.auth.Role;
import com.tshepo.service.IAccountService;

@RequestMapping("/api/ts-ecommerce/internal-operations")
public class InternalOperations {
	
	private IAccountService accountService;
	
	@Autowired
	private void setInternalOperations(IAccountService accountService) 
	{
		this.accountService = accountService;
	}
	
	@PutMapping("/make-admin/{email}")
	@ResponseStatus(HttpStatus.OK)
	public void makeAdmin(@PathVariable String email)
	{
		accountService.changeRole(email, Role.ADMIN);
	}

}
