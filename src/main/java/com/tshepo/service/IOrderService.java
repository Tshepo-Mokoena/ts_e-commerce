package com.tshepo.service;

import java.util.List;
import java.util.Optional;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.Order;

public interface IOrderService {

	Order newOrder(Cart cart, Account account);
	
	List<Order> orderList();
	
	List<Order> findByAccount(Account account);

}
