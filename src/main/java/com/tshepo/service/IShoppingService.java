package com.tshepo.service;

import java.util.List;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.requests.CartRequest;

public interface IShoppingService {

	Cart addToCart(Account account, CartRequest cartRequest);
	
	Cart removeFromCart(Account account, CartRequest cartRequest);

	List<CartItem> getCartItems(Account account);

	Cart updateCart(Account account, CartRequest cartRequest);

}
