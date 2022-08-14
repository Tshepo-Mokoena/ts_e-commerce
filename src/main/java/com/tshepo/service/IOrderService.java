package com.tshepo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.Order;
import com.tshepo.persistence.auth.OrderStatus;

public interface IOrderService {

	Order newOrder(Cart cart, Account account);
	
	Order updateOrder(Order order);
	
	Page<Order> findAll(int evalPage, int evalPageSize);
	
	Page<Order> findOrderStatus(OrderStatus orderStatus, int evalPage, int evalPageSize);

	Optional<Order> findById(Long orderId);

	Page<Order> findByAccount(Account account, int evalPage, int evalPageSize);

}
