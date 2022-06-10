package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.persistence.Product;
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

	@Transactional
	@Override
	public CartItem saveCartItem(CartItem cartItem) 
	{
		cartItem.setCreatedAt(LocalDateTime.now());
		cartItem.setUpdatedAt(LocalDateTime.now());
		return cartItemRepository.save(cartItem);
	}

	@Transactional
	@Override
	public CartItem updateCartItem(CartItem cartItem) 
	{
		cartItem.setUpdatedAt(LocalDateTime.now());
		return cartItemRepository.save(cartItem);
	}

	@Override
	public List<CartItem> findByCart(Cart cart) 
	{
		return cartItemRepository.findByCart(cart);
	}

	@Override
	public void removeCartItem(CartItem cartItem) 
	{
		cartItemRepository.delete(cartItem);
	}
	
	@Override
	public CartItem save(CartItem cartItem) 
	{
		return cartItemRepository.save(cartItem);
	}

	@Override
	public Optional<CartItem> findByProduct(Product product) 
	{
		return cartItemRepository.findByProduct(product);
	}
	

}
