package com.tshepo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.repository.IOrderRepository;
import com.tshepo.service.IOrderService;

@Service
public class OrderService implements IOrderService{
	
	private IOrderRepository orderRepository;
	
	@Autowired
	private void setOrderService(IOrderRepository orderRepository) 
	{
		this.orderRepository = orderRepository;
	}

}
