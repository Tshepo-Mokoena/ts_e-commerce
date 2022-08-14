package com.tshepo.resources.Internal;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Order;
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
		return new ResponseEntity<>(orderService.findAll(page.orElse(0), pageSize.orElse(5)), HttpStatus.OK);
	}	

	@GetMapping("/new")
	public ResponseEntity<?> getNewOrders(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{		
		return new ResponseEntity<>(findByOrderStatus(OrderStatus.NEW, page, pageSize), HttpStatus.OK);
	}
	
	@GetMapping("/paid")
	public ResponseEntity<?> getPaidOrders(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{		
		return new ResponseEntity<>(findByOrderStatus(OrderStatus.PAID, page, pageSize), HttpStatus.OK);
	}
	
	@GetMapping("/pending")
	public ResponseEntity<?> getPendingOrders(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{
		return new ResponseEntity<>(findByOrderStatus(OrderStatus.PENDING, page, pageSize), HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrder(@PathVariable Long orderId)
	{
		return new ResponseEntity<>(findById(orderId).orElseThrow(() -> new  ItemNotFoundException(orderId.toString())),
				HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}/order-items")
	public ResponseEntity<?> getOrderItems(@PathVariable Long orderId)
	{	
		return new ResponseEntity<>(orderItemService.findByOrder(
				findById(orderId).orElseThrow(() -> new  ItemNotFoundException(orderId.toString()) )), HttpStatus.OK);
	}

	@PostMapping("/{orderId}/payment")
	public ResponseEntity<?> orderPayment(@PathVariable Long orderId, @RequestBody Order order)
	{		
		Order currentOrder = findById(orderId).orElseThrow(() -> new  ItemNotFoundException(orderId.toString()) );		
		currentOrder.setOrderStatus(OrderStatus.PAID);		
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@PostMapping("/{orderId}/pend-order")
	public ResponseEntity<?> orderPending(@PathVariable Long orderId)
	{		
		Order order = findById(orderId).orElseThrow(() -> new  ItemNotFoundException(orderId.toString()) );		
        order.setOrderStatus(OrderStatus.PENDING);		
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@PostMapping("/{orderId}/cancel-order")
	public ResponseEntity<?> cancelOrder(@PathVariable Long orderId)
	{		
		Order order = findById(orderId).orElseThrow(() -> new  ItemNotFoundException(orderId.toString()) );
        order.setOrderStatus(OrderStatus.CANCELLED);		
		return new ResponseEntity<>(order, HttpStatus.OK);
	}	
	
	private Account findAccount(String email) 
	{
		return accountService.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
	}
	
	private Page<Order> findOrdersByAccount(Account account, Optional<Integer> pageSize, Optional<Integer> page)
	{
		return orderService.findByAccount(account, page.orElse(0), pageSize.orElse(5));
	}
	
	private Optional<Order> findById(Long orderId) { return orderService.findById(orderId); }
	
	private Page<Order> findByOrderStatus(OrderStatus orderStatus,Optional<Integer> pageSize, Optional<Integer> page){
		return orderService.findOrderStatus(orderStatus, page.orElse(0), pageSize.orElse(5));
	}
	
}
