package com.tshepo.service;

import java.util.List;
import java.util.Optional;

import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.persistence.Product;

public interface ICartItemService {
	
	CartItem saveCartItem(CartItem cartItem);
	
	CartItem updateCartItem(CartItem cartItem);
	
	List<CartItem> findByCart(Cart cart);
	
	void removeCartItem(CartItem cartItem);
	
	CartItem save(CartItem cartItem);

	Optional<CartItem> findByProduct(Product product);

}
