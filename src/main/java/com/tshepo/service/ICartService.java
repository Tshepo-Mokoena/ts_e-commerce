package com.tshepo.service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.requests.CartRequest;

public interface ICartService {
	
	Cart addToCart(Account account, CartRequest cartRequest);
	
	Cart removeFromCart(Account account, CartRequest cartRequest);
	
	Cart updateCart(Account account, CartRequest cartRequest);
		
	Cart clearCart(Cart cart);

	Cart findByAccount(Account account);

	Cart getCart(Account account);
	
}
