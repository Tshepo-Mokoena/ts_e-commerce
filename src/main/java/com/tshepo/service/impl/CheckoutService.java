package com.tshepo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.service.ICartItemService;
import com.tshepo.service.ICartService;
import com.tshepo.service.ICheckoutService;
import com.tshepo.service.IEmailService;
import com.tshepo.service.IProductService;

@Service
public class CheckoutService implements ICheckoutService{
	
	private ICartService cartService;
	
	private IProductService productService;
	
	private ICartItemService cartItemService;
	
	private IEmailService emailService;
	
	@Autowired
	private void setCheckoutService(
			ICartService cartService, 
			ICartItemService cartItemService,
			IProductService productService,
			IEmailService emailService)
	{
		this. cartItemService = cartItemService;
		this.productService = productService;
		this.cartService = cartService;
		this.emailService = emailService;
	}

}
