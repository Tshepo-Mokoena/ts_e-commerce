package com.tshepo.service;

import java.util.List;

import com.tshepo.persistence.Order;
import com.tshepo.persistence.OrderItem;

public interface IOrderItemService {

	OrderItem newOrderItem(OrderItem orderItem);
	
	List<OrderItem> findByOrder(Order order); 

}
