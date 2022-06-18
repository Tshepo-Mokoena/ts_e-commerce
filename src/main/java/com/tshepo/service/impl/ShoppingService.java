package com.tshepo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.exception.ItemQtyException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.persistence.Product;
import com.tshepo.requests.CartRequest;
import com.tshepo.service.IAccountService;
import com.tshepo.service.ICartItemService;
import com.tshepo.service.ICartService;
import com.tshepo.service.IProductService;
import com.tshepo.service.IShoppingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShoppingService implements IShoppingService{
	
	private ICartService cartService;
	
	private IProductService productService;
	
	private ICartItemService cartItemService;
	
	@Autowired
	private IAccountService accountService;
		
	@Autowired
	private void setShoppingService(
			ICartService cartService, 
			ICartItemService cartItemService,
			IProductService productService)
	{
		this. cartItemService = cartItemService;
		this.productService = productService;
		this.cartService = cartService;
	}
	
	@Override
	public Cart addToCart(String email, CartRequest cartRequest) 
	{		
		log.info(email);
		
		//Get Account Cart
		Account account = accountService.findByEmail(email)
				.orElseThrow(() -> new EmailNotFoundException(email));
				
		Cart cart = cartService.findByAccount(account);
		//Get Product
		log.info(cart.getId().toString());
		Product product = productService.findByProductId(cartRequest.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(cartRequest.getProductId()) ); 
		//check if product is Cart				
		List<CartItem> cartItems = cartItemService.findByCart(cart);
		for (CartItem cartItem : cartItems)
		{
			if (cartItem.getProduct().getProductId().contains(product.getProductId()))
			{
				//update product{Qty}
				Cart updateCart = updateCartItem(cart, cartItem, cartRequest);
				//return cart
				return updateCart;
			}
		}	
		BigDecimal cartTotal = new BigDecimal(0);
		//Add Product to Cart
		
		CartItem newCartItem = new CartItem(cartRequest.getQuantity(), 
				product.getPrice().multiply(new BigDecimal(cartRequest.getQuantity())), 
				product, cart);
		//Update cart Total Price
		log.info(cart.getId().toString()+" new item");
		cartItemService.saveCartItem(newCartItem);
		for (CartItem cartItem : cartItemService.findByCart(cart))
		{
			cartTotal = cartTotal.add(cartItem.getSubtotal());
		}
		cart.setTotal(cartTotal);
		Cart savedCart = cartService.updateCart(cart);
		//return cart
		return savedCart;
	}

	@Override
	public Cart removeFromCart(String email, CartRequest cartRequest) 
	{		
		//Get Account Cart
		Account account = accountService.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
		Cart cart = cartService.findByAccount(account);
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
	public List<CartItem> getCartItems(String email) 
	{
		//Get Account Cart
		Account account = accountService.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
		Cart cart = cartService.findByAccount(account);
		List<CartItem> cartItems = cartItemService.findByCart(cart);
		return cartItems;
	}

	@Override
	public Cart updateCart(String email, CartRequest cartRequest) {
		//Get Account Cart
		Account account = accountService.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
		Cart cart = cartService.findByAccount(account);
		// Get Product
		Product product = productService.findByProductId(cartRequest.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(cartRequest.getProductId()));
		// check if product is Cart
		List<CartItem> cartItems = cartItemService.findByCart(cart);
		for (CartItem cartItem : cartItems) 
		{
			if (cartItem.getProduct().getProductId().contains(product.getProductId())) 
			{
				// update product{Qty}
				Cart updateCart = updateCartItem(cart, cartItem, cartRequest);
				// return cart
				return updateCart;
			}			
		}
		throw new ItemNotFoundException(cartRequest.getProductId());
	}	

	private Cart updateCartItem(Cart cart, CartItem cartItem, CartRequest cartRequest)
	{
		BigDecimal cartTotal = new BigDecimal(0);
		//Compare product Qty, and request Qty
		if(cartItem.getProduct().getQty() >= cartRequest.getQuantity())
		{
			//If product Qty >= request Qty
			cartItem.setQty(cartRequest.getQuantity());
			cartItem.setSubtotal(
					cartItem.getProduct().getPrice().multiply(
							new BigDecimal(cartRequest.getQuantity())));
			//update cartItem Qty
			cartItemService.updateCartItem(cartItem);
			//update Cart Total
			for (CartItem cartProduct : cartItemService.findByCart(cart))
			{
				cartTotal = cartTotal.add(cartProduct.getSubtotal());			
			}
			cart.setTotal(cartTotal);
			Cart updatedCart = cartService.updateCart(cart);
				//return cart
			return updatedCart;
		}		
		throw new ItemQtyException(""+cartRequest.getQuantity());
	}
		
}
