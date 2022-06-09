package com.tshepo.service;

import com.tshepo.persistence.Cart;

public interface ICartService {
	
	Cart updateCart(Cart cart);
	
	void clearCart(Cart cart);
	
}
