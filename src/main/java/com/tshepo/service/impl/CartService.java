package com.tshepo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Cart;
import com.tshepo.persistence.repository.ICartRepository;
import com.tshepo.service.ICartService;

@Service
public class CartService implements ICartService{
	
	private ICartRepository cartRepository;
	
	@Autowired
	private void setCartService(ICartRepository cartRepository) 
	{
		this.cartRepository = cartRepository;
	}

	@Override
	public Cart updateCart(Cart cart) 
	{		
		cart.setUpdatedAt(LocalDateTime.now());
		return cartRepository.save(cart);
	}

	@Override
	public void clearCart(Cart cart) 
	{
		cart.setCartProducts(null);
		cart.setTotal(BigDecimal.ZERO);
		cartRepository.save(cart);
	}

}
