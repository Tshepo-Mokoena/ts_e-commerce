package com.tshepo.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.persistence.Product;
import com.tshepo.requests.CartRequest;
import com.tshepo.service.ICartItemService;
import com.tshepo.service.ICartService;
import com.tshepo.service.IEmailService;
import com.tshepo.service.IProductService;
import com.tshepo.service.IShoppingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShoppingService implements IShoppingService{
	
	private ICartService cartService;
	
	private IProductService productService;
	
	private ICartItemService cartItemService;
	
	private IEmailService emailService;
	
	@Autowired
	private void setShoppingService(
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

	@Override
	public Cart addToCart(Account account, CartRequest cartRequest) 
	{
		
		Cart cart = account.getCart();
		
		Product product = productService.findByProductId(cartRequest.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(cartRequest.getProductId()));
		
		CartItem productCartItem = cartItemService.findByProduct(product).get();
		
		List<CartItem> getCartItems = cartItemService.findByCart(productCartItem.getCart());
		
		BigDecimal cartTotal = new BigDecimal(0);
		
		if (productCartItem != null)
		{
			productCartItem.setQty(cartRequest.getQuantity());
			productCartItem.setSubtotal(
					product.getPrice().multiply(
										new BigDecimal(
												cartRequest.getQuantity())));
			cartItemService.updateCartItem(productCartItem);
						
			for (CartItem cartItem : getCartItems)
			{
				cartTotal = cartTotal.add(cartItem.getSubtotal());
			}
			cart.setTotal(cartTotal);
			cartService.updateCart(cart);
			return cart;
		}
		
		CartItem savedCartItem = 
				cartItemService.saveCartItem(
						new CartItem(
						cartRequest.getQuantity(),
						product.getPrice().multiply(
										new BigDecimal(
												cartRequest.getQuantity())),
						product,
						cart));
		return savedCartItem.getCart();
	}

	@Override
	public Cart removeFromCart(Account account, CartRequest cartRequest) 
	{		
		Cart cart = account.getCart();
		Product product = productService.findByProductId(cartRequest.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(cartRequest.getProductId()));
		
		CartItem cartItem = cartItemService.findByProduct(product).get();
		
		if (cartItem != null)
		{
			cartItemService.removeCartItem(cartItem);
			cartService.updateCart(cart);
			return cart;
		}
		else
			throw new ItemNotFoundException(cartRequest.getProductId());
	}

	@Override
	public List<CartItem> getCartItems(Account account) 
	{
		List<CartItem> cartItems = cartItemService.findByCart(account.getCart());
		return cartItems;
	}

	@Override
	public Cart updateCart(Account account, CartRequest cartRequest) 
	{
		Cart cart = account.getCart();
		
		Product product = productService.findByProductId(cartRequest.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(cartRequest.getProductId()));
		
		CartItem productCartItem = cartItemService.findByProduct(product).get();
		
		if (productCartItem != null)
		{
			productCartItem.setQty(cartRequest.getQuantity());
			productCartItem.setSubtotal(
					product.getPrice().multiply(
										new BigDecimal(
												cartRequest.getQuantity())));
			cartItemService.updateCartItem(productCartItem);
			cartService.updateCart(cart);
			return cart;
		}
		return null;
	}
}
