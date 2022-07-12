package com.tshepo.resources.Internal;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Order;
import com.tshepo.persistence.OrderItem;
import com.tshepo.persistence.auth.OrderStatus;
import com.tshepo.service.IAccountService;
import com.tshepo.service.IEmailService;
import com.tshepo.service.IOrderItemService;
import com.tshepo.service.IOrderService;

@RestController
@RequestMapping("/api/ts-ecommerce/internal/orders")
public class InternalOrderController {
	
	private IOrderService orderService;
	
	private IEmailService emailService;
	
	private IAccountService accountService;
	
	private IOrderItemService orderItemService;
	
	@Autowired
	private void setInternalOrderController( 
			IOrderService orderService,
			IEmailService emailService,
			IAccountService accountService,
			IOrderItemService orderItemService)
	{
		this. orderService = orderService;
		this.emailService = emailService;
		this.accountService = accountService;
		this.orderItemService = orderItemService;
	}
	
	@GetMapping
	public ResponseEntity<?> getOrders(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{
		int evalPageSize = pageSize.orElse(5);		
		int evalPage = (page.orElse(0) < 1) ? 0 : page.get() - 1;
		
		Page<Order> orders = orderService.findAll(evalPage, evalPageSize);
		
		if (orders.isEmpty())
			throw new ItemNotFoundException("[]");
		
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}	

	@GetMapping("/new")
	public ResponseEntity<?> getNewOrders(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{
		int evalPageSize = pageSize.orElse(5);		
		int evalPage = (page.orElse(0) < 1) ? 0 : page.get() - 1;
		
		Page<Order> orders = orderService.findOrderStatus(OrderStatus.NEW, evalPage, evalPageSize);
		
		if (orders.isEmpty())
			throw new ItemNotFoundException("[]");
		
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/paid")
	public ResponseEntity<?> getPaidOrders(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{
		int evalPageSize = pageSize.orElse(5);		
		int evalPage = (page.orElse(0) < 1) ? 0 : page.get() - 1;
		
		Page<Order> orders = orderService.findOrderStatus(OrderStatus.PAID, evalPage, evalPageSize);
		
		if (orders.isEmpty())
			throw new ItemNotFoundException("[]");
		
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/pending")
	public ResponseEntity<?> getPendingOrders(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{
		int evalPageSize = pageSize.orElse(5);		
		int evalPage = (page.orElse(0) < 1) ? 0 : page.get() - 1;
		
		Page<Order> orders = orderService.findOrderStatus(OrderStatus.PENDING, evalPage, evalPageSize);
		
		if (orders.isEmpty())
			throw new ItemNotFoundException("[]");
		
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrder(@PathVariable Long orderId)
	{
		
		Order order = orderService.findId(orderId)
				.orElseThrow(() -> new  ItemNotFoundException(orderId.toString()) );
		
		return new ResponseEntity<>(order, HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}/order-items")
	public ResponseEntity<?> getOrderItems(@PathVariable Long orderId)
	{
		
		Order order = orderService.findId(orderId)
				.orElseThrow(() -> new  ItemNotFoundException(orderId.toString()) );
		
		List<OrderItem> orderItems = orderItemService.findByOrder(order);
		
		return new ResponseEntity<>(orderItems, HttpStatus.OK);
	}

	@PostMapping("/{orderId}/payment")
	public ResponseEntity<?> orderPayment(@PathVariable Long orderId)
	{		
		Order order = orderService.findId(orderId)
				.orElseThrow(() -> new  ItemNotFoundException(orderId.toString()) );
		
        order.setOrderStatus(OrderStatus.PAID);
		
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@PostMapping("/{orderId}/pend-order")
	public ResponseEntity<?> orderPending(@PathVariable Long orderId)
	{		
		Order order = orderService.findId(orderId)
				.orElseThrow(() -> new  ItemNotFoundException(orderId.toString()) );
		
        order.setOrderStatus(OrderStatus.PENDING);
		
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@PostMapping("/{orderId}/cancel-order")
	public ResponseEntity<?> cancelOrder(@PathVariable Long orderId)
	{		
		Order order = orderService.findId(orderId)
				.orElseThrow(() -> new  ItemNotFoundException(orderId.toString()) );
		
        order.setOrderStatus(OrderStatus.CANCELLED);
		
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@GetMapping("/account/{email}")
	public ResponseEntity<?> getOrdersByAccount(@PathVariable String email) 
	{
		Account account = findAccount(email);		
		
		return new ResponseEntity<>(findOrdersByAccount(account), HttpStatus.OK);
	}
	
	@GetMapping("/account/{email}/{orderId}")
	public ResponseEntity<?> getOrderByAccount(@PathVariable String email, @PathVariable Long orderId)
	{		
		Account account = findAccount(email);
		
		Order getOrder = null;
		
		for (Order order : findOrdersByAccount(account))
		{
			if (order.getId() == orderId)
				getOrder = order;
			else
				throw new ItemNotFoundException(orderId.toString());
		}
		return new ResponseEntity<>(getOrder, HttpStatus.OK);
	}
	
	@GetMapping("/account/{email}/{orderId}/order-items")
	public ResponseEntity<?> getOrderItemsByAccount(@PathVariable String email, @PathVariable Long orderId)
	{		
		Account account = findAccount(email);
		
		Order getOrder = null;
		
		for (Order order : findOrdersByAccount(account))
		{
			if (order.getId() == orderId)
				getOrder = order;
			else
				throw new ItemNotFoundException(orderId.toString());
		}
		
		List<OrderItem> orderItems = orderItemService.findByOrder(getOrder);
		
		return new ResponseEntity<>(orderItems, HttpStatus.OK);
	}
	
	private Account findAccount(String email) 
	{
		return accountService.findByEmail(email)
		.orElseThrow(() -> new EmailNotFoundException(email));
	}
	
	private List<Order> findOrdersByAccount(Account account)
	{
		return orderService.findByAccount(account);
	}
	
}
