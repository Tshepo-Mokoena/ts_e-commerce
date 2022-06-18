package com.tshepo.service;

import java.util.List;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.requests.CartRequest;

public interface IShoppingService {

	Cart addToCart(String account, CartRequest cartRequest);
	
	Cart removeFromCart(String email, CartRequest cartRequest);

	List<CartItem> getCartItems(String email);

	Cart updateCart(String email, CartRequest cartRequest);

}
