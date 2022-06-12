package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.persistence.Order;
import com.tshepo.persistence.OrderItem;
import com.tshepo.persistence.Product;
import com.tshepo.persistence.repository.IOrderRepository;
import com.tshepo.service.IOrderItemService;
import com.tshepo.service.IOrderService;

import lombok.Synchronized;

@Service
public class OrderService implements IOrderService{
	
	private IOrderRepository orderRepository;
	
	private IOrderItemService orderItemService;
	
	@Autowired
	private void setOrderService(IOrderRepository orderRepository) 
	{
		this.orderRepository = orderRepository;
	}
	
	@Synchronized
	public Order newOrder(Cart cart) 
	{
		Order order = new Order();
		
		List<CartItem> cartItems = cart.getCartItems();
				
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		
		for (CartItem cartItem : cartItems)
		{			
			Product product = cartItem.getProduct();
			OrderItem orderItem = OrderItem.createfromCartItem(cartItem, order);
			orderItems.add(orderItem);
			product.setQty(product.getQty() - cartItem.getQty());;
		}		
		
		order.setTotal(cart.getTotal());
		order.setAccount(cart.getAccount());
		order.setOrderItems(orderItems.stream().collect(Collectors.toList()));
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());
		return  orderRepository.save(order);
	}
	
	public List<Order> orderList() 
	{
		return  null;
	}
	
	public Optional<Order> findByAccount(Long id) 
	{
		return orderRepository.findById(id);
	}

}
