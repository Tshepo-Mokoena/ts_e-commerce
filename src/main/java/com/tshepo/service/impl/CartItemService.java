package com.tshepo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.repository.ICartItemRepository;
import com.tshepo.service.ICartItemService;

@Service
public class CartItemService implements ICartItemService{
	
	private ICartItemRepository cartItemRepository;
	
	@Autowired
	private void setCartItemService(ICartItemRepository cartItemRepository) 
	{
		this.cartItemRepository = cartItemRepository;
	}

}
