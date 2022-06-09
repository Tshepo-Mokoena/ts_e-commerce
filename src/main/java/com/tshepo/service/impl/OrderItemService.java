package com.tshepo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.repository.IOrderItemRepository;
import com.tshepo.service.IOrderItemService;

@Service
public class OrderItemService implements IOrderItemService{
	
	private IOrderItemRepository orderItemRepository;
	
	@Autowired
	private void setOrderItemService(IOrderItemRepository orderItemRepository) 
	{
		this.orderItemRepository = orderItemRepository;
	}

}
