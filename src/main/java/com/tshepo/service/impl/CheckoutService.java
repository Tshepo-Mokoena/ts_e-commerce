package com.tshepo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.Order;
import com.tshepo.service.IAccountService;
import com.tshepo.service.ICartService;
import com.tshepo.service.ICheckoutService;
import com.tshepo.service.IEmailService;
import com.tshepo.service.IOrderService;

@Service
public class CheckoutService implements ICheckoutService{
	
	private ICartService cartService;
	
	private IOrderService orderService;
	
	private IEmailService emailService;
	
	private IAccountService accountService;
	
	@Autowired
	private void setCheckoutService(
			ICartService cartService, 
			IOrderService orderService,
			IEmailService emailService,
			IAccountService accountService)
	{
		this. orderService = orderService;
		this.cartService = cartService;
		this.emailService = emailService;
		this.accountService =accountService;
	}
	
	@Override
	public Order createOrder(String email) 
	{
		Account account = accountService.findByEmail(email)
				.orElseThrow(() -> new EmailNotFoundException(email));
		Cart cart = cartService.findByAccount(account);
		Order order = orderService.newOrder(cart, account);
		//TODO
		emailService.sendEmail(account.getEmail(), "Order Confirmation" , "emailMessage");
		return order;
	}

	@Override
	public List<Order> findByAccount(String email) 
	{
		Account account = accountService.findByEmail(email)
				.orElseThrow(() -> new EmailNotFoundException(email));
		List<Order> order = orderService.findByAccount(account);
				;
		return order;
	}

}
