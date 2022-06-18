package com.tshepo.service;

import java.util.List;

import com.tshepo.persistence.Order;

public interface ICheckoutService {

	Order createOrder(String email);
	
	List<Order> findByAccount(String email);

}
