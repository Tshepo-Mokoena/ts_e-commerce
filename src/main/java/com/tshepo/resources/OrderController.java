package com.tshepo.resources;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.Cart;
import com.tshepo.persistence.Order;
import com.tshepo.security.AccountPrincipal;
import com.tshepo.service.IAccountService;
import com.tshepo.service.ICartService;
import com.tshepo.service.IEmailService;
import com.tshepo.service.IOrderItemService;
import com.tshepo.service.IOrderService;
import com.tshepo.util.EmailTemplates;

@RestController
@RequestMapping("/api/ts-ecommerce/orders")
public class OrderController {

    private ICartService cartService;
	
	private IOrderService orderService;
	
	private IEmailService emailService;
	
	private IAccountService accountService;
	
	private IOrderItemService orderItemService;
	
	@Autowired
	private void setOrderController(ICartService cartService, IOrderService orderService, IEmailService emailService, IAccountService accountService, IOrderItemService orderItemService)
	{
		this.orderService = orderService;
		this.cartService = cartService;
		this.emailService = emailService;
		this.accountService = accountService;
		this.orderItemService = orderItemService;
	}

	@PostMapping("/submit")
	public ResponseEntity<?> submitOrder(@AuthenticationPrincipal AccountPrincipal accountPrincipal) 
	{
		
		Account account = findAccount(accountPrincipal.getEmail());
		
		Cart cart = cartService.findByAccount(account);
				
		Order order = orderService.newOrder(cart, account);
				
//		EmailTemplates email = new EmailTemplates();
//		
//		email.orderConfirm(account, order, orderItemService.findByOrder(order));
		
		return new ResponseEntity<>(order, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<?> getOrders(@AuthenticationPrincipal AccountPrincipal accountPrincipal,
			@RequestParam("pageSize") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page) 
	{
		return new ResponseEntity<>(findOrdersByAccount(findAccount(accountPrincipal.getEmail()), page.orElse(0), pageSize.orElse(5)), 
				HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrder(@AuthenticationPrincipal AccountPrincipal accountPrincipal, @PathVariable Long orderId)
	{					
		return new ResponseEntity<>(findById(orderId).orElseThrow(() -> new  ItemNotFoundException(orderId.toString())), HttpStatus.OK);
	}	
	
	@GetMapping("/{orderId}/order-items")
	public ResponseEntity<?> getOrderItems(@AuthenticationPrincipal AccountPrincipal accountPrincipal, @PathVariable Long orderId)
	{			
		return new ResponseEntity<>(orderItemService.findByOrder(findById(orderId).orElseThrow(() -> new  ItemNotFoundException(orderId.toString()))), 
				HttpStatus.OK);
	}
	
	private Account findAccount(String email) { return accountService.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email)); }
	
	private Page<Order> findOrdersByAccount(Account account, int evalPage, int evalPageSize) { return orderService.findByAccount(account, evalPage, evalPageSize); }
	
	private Optional<Order> findById(Long orderId) { return orderService.findById(orderId); }	
	

}
