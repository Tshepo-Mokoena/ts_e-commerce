package com.tshepo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.exception.ItemQtyException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.persistence.Product;
import com.tshepo.persistence.repository.ICartRepository;
import com.tshepo.requests.CartRequest;
import com.tshepo.service.ICartItemService;
import com.tshepo.service.ICartService;
import com.tshepo.service.IProductService;

@Service
public class CartService implements ICartService{
	
	private IProductService productService;
	
	private ICartItemService cartItemService;
	
	private ICartRepository cartRepository;
	
	@Autowired
	private void setCartService(ICartItemService cartItemService, IProductService productService, ICartRepository cartRepository) 
	{
		this.cartRepository = cartRepository;
		this.cartItemService = cartItemService;
		this.productService = productService; 
	}	

	@Override
	public Cart addToCart(Account account, CartRequest cartRequest) 
	{
		Cart cart = findByAccount(account);
		
		if(cartRequest.getQuantity() < 1) {cartRequest.setQuantity(1);}
		
		Product product = productService.findByProductId(cartRequest.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(cartRequest.getProductId()) ); 
				
		List<CartItem> cartItems = cartItemService.findByCart(cart);
		
		for (CartItem cartItem : cartItems)
		{
			if (cartItem.getProduct().getProductId().contains(product.getProductId()))
			{		
				Cart updateCart = updateCartItem(cart, cartItem, cartRequest);				
				return updateCart;
			}
		}	
		BigDecimal cartTotal = new BigDecimal(0);
		
		CartItem newCartItem = new CartItem(cartRequest.getQuantity(), 
				product.getPrice().multiply(new BigDecimal(cartRequest.getQuantity())), 
				product, cart);
		
		cartItemService.saveCartItem(newCartItem);
		
		for (CartItem cartItem : cartItemService.findByCart(cart))
		{
			cartTotal = cartTotal.add(cartItem.getSubtotal());
		}
		
		cart.setTotal(cartTotal);
		cart.setUpdatedAt(LocalDateTime.now());
		cart.setCreatedAt(LocalDateTime.now());
		return cartRepository.save(cart);	
	}

	@Override
	public Cart removeFromCart(Account account, String productId) 
	{
		Cart cart = findByAccount(account);
		
		Product product = productService.findByProductId(productId)
				.orElseThrow(() -> new ItemNotFoundException(productId));
		
		
		List<CartItem> cartItems = cartItemService.findByCart(cart);
		
		for (CartItem cartItem : cartItems) {
			
			if ( cartItem.getProduct().getProductId().contains(product.getProductId()) )
				cartItemService.removeCartItem(cartItem);
			
		}
				
		BigDecimal cartTotal = new BigDecimal(0);
		
		for (CartItem cartItem : cartItemService.findByCart(cart))
		{
			cartTotal = cartTotal.add(cartItem.getSubtotal());
		}
		
		cart.setTotal(cartTotal);
		cart.setUpdatedAt(LocalDateTime.now());
		return cartRepository.save(cart);
			
	}

	@Override
	public Cart clearCart(Account account) 
	{
		Cart cart = getCart(account);
		List<CartItem> cartItems = cartItemService.findByCart(cart);				
		for (CartItem cartItem : cartItems)
		{
			cartItemService.removeCartItem(cartItem);
		}		
		cart.setTotal(BigDecimal.ZERO);
		cart.setUpdatedAt(LocalDateTime.now());
		return cartRepository.save(cart);
	}
	
	private Cart updateCartItem(Cart cart, CartItem cartItem, CartRequest cartRequest)
	{
		BigDecimal cartTotal = new BigDecimal(0);
		
		if(cartItem.getProduct().getQty() >= cartRequest.getQuantity())
		{
			
			cartItem.setQty(cartRequest.getQuantity());
			
			cartItem.setSubtotal(
					cartItem.getProduct().getPrice().multiply(
							new BigDecimal(cartRequest.getQuantity())));
			
			cartItemService.updateCartItem(cartItem);
			
			for (CartItem cartProduct : cartItemService.findByCart(cart))
			{
				cartTotal = cartTotal.add(cartProduct.getSubtotal());			
			}
			cart.setTotal(cartTotal);
			cart.setUpdatedAt(LocalDateTime.now());		
			return cartRepository.save(cart);
		}		
		throw new ItemQtyException(""+cartRequest.getQuantity());
	}

	@Override
	public Cart updateCart(Account account, CartRequest cartRequest) 
	{
		Cart cart = findByAccount(account);
		
		if(cartRequest.getQuantity() < 1) {cartRequest.setQuantity(1);}
		
		Product product = productService.findByProductId(cartRequest.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(cartRequest.getProductId()));
		
		List<CartItem> cartItems = cartItemService.findByCart(cart);
		
		for (CartItem cartItem : cartItems) 
		{
			if (cartItem.getProduct().getProductId().contains(product.getProductId())) 
			{
				
				Cart updateCart = updateCartItem(cart, cartItem, cartRequest);
				
				return updateCart;
			}			
		}
		throw new ItemNotFoundException(cartRequest.getProductId());
	}

	@Override
	public Cart getCart(Account account) { return findByAccount(account); }
	
	@Override
	public Cart findByAccount(Account account) { return cartRepository.findByAccount(account); }

}
