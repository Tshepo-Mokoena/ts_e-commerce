package com.tshepo.service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.requests.CartRequest;

public interface ICartService {
	
	Cart addToCart(Account account, CartRequest cartRequest);
	
	Cart removeFromCart(Account account, String productId);
	
	Cart updateCart(Account account, CartRequest cartRequest);
		
	Cart clearCart(Account account);

	Cart findByAccount(Account account);

	Cart getCart(Account account);
	
}
