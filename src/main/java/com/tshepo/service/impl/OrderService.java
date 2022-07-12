package com.tshepo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.exception.ItemQtyException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.persistence.Order;
import com.tshepo.persistence.OrderItem;
import com.tshepo.persistence.Product;
import com.tshepo.persistence.auth.OrderStatus;
import com.tshepo.persistence.repository.IOrderRepository;
import com.tshepo.service.ICartItemService;
import com.tshepo.service.IOrderItemService;
import com.tshepo.service.IOrderService;

import lombok.Synchronized;

@Service
public class OrderService implements IOrderService{
	
	private IOrderRepository orderRepository;
	
	private IOrderItemService orderItemService;
	
	private ICartItemService cartItemService;
	
	@Autowired
	private void setOrderService(IOrderRepository orderRepository, 
			IOrderItemService orderItemService, ICartItemService cartItemService) 
	{
		this.orderRepository = orderRepository;
		this.orderItemService = orderItemService;
		this.cartItemService = cartItemService;
	}
			
	@Override
	@Transactional
	@Synchronized
	public Order newOrder(Cart cart, Account account) 
	{			
		List<CartItem> cartItems = cartItemService.findByCart(cart);
		
		if (cartItems.isEmpty()) throw new ItemNotFoundException("[]");
		
		for (CartItem cartItem : cartItems)
		{
			if( cartItem.getProduct().getQty() < cartItem.getQty())
				throw new ItemQtyException(cartItem.getQty().toString());
		}
		
		Order savedOrder = orderRepository.save(Order.createfromCart(cart, account));
		
		for (CartItem cartItem : cartItems)
		{			
			orderItemService.newOrderItem(OrderItem.createfromCartItem(cartItem, savedOrder));	
		}
		
		return savedOrder;		
	}
	
	@Override
	public Page<Order> findAll(int evalPage, int evalPageSize) 
	{
		Pageable pageandSize = PageRequest.of(evalPage, evalPageSize);
		return orderRepository.findAll(pageandSize);
	}
	
	@Override
	public Page<Order> findByAccount(Account account, int evalPage, int evalPageSize) 
	{
		Pageable pageandSize = PageRequest.of(evalPage, evalPageSize);
		return orderRepository.findByAccount(account, pageandSize);
	}

	@Override
	public Optional<Order> findId(Long orderId) 
	{
		return orderRepository.findById(orderId);
	}

	@Override
	public Page<Order> findOrderStatus(OrderStatus orderStatus, int evalPage, int evalPageSize) 
	{
		Pageable pageandSize = PageRequest.of(evalPage, evalPageSize);
		return orderRepository.findByOrderStatus(orderStatus, pageandSize);
	}

	@Override
	public Order updateOrder(Order order) 
	{
		return orderRepository.save(order);
	}

	@Override
	public List<Order> findByAccount(Account account) 
	{
		return orderRepository.findByAccount(account);
	}
	
}
