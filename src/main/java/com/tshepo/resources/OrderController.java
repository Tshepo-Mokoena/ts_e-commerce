package com.tshepo.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.service.ICartItemService;
import com.tshepo.service.ICartService;
import com.tshepo.service.IOrderItemService;
import com.tshepo.service.IOrderService;
import com.tshepo.service.IProductService;

@RestController
@RequestMapping("/api/ts-ecommerce/orders")
public class OrderController {
	
	private ICartService cartService;
	
	private IProductService productService;
	
	private ICartItemService cartItemService;
	
	private IOrderItemService orderItemService;
	
	private IOrderService orderService;
	
	@Autowired
	private void setCartController(
			ICartService cartService, 
			ICartItemService cartItemService,
			IProductService productService,
			IOrderService orderService,
			IOrderItemService orderItemService)
	{
		this. cartItemService = cartItemService;
		this.productService = productService;
		this.cartService = cartService;
	}
	
	@PostMapping("/submit")
	public ResponseEntity<?> submitOrder()
	{
		//Get Account
			//else throw Exception
		//Get Account Cart
		//Submit Order
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//get Order History
	@GetMapping
	public ResponseEntity<?> getOrders()
	{
		//Get Account
			//else throw Exception
		//Find Order by Account
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
