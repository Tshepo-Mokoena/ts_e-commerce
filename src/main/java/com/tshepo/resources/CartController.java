package com.tshepo.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.service.ICartItemService;
import com.tshepo.service.ICartService;
import com.tshepo.service.IProductService;

@RestController
@RequestMapping("/api//api/ts-ecommerce/carts")
public class CartController {
	
	private ICartService cartService;
	
	private IProductService productService;
	
	private ICartItemService cartItemService;
	
	@Autowired
	private void setCartController(
			ICartService cartService, 
			ICartItemService cartItemService,
			IProductService productService)
	{
		this. cartItemService = cartItemService;
		this.productService = productService;
		this.cartService = cartService;
	}
	
	//Add Products To Cart
	
	//Update Cart
	
	//Clear Cart
	
	//Remove Products to cart
	
	//Get Cart

}
