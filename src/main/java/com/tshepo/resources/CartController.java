package com.tshepo.resources;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.persistence.CartItem;
import com.tshepo.requests.CartRequest;
import com.tshepo.security.AccountPrincipal;
import com.tshepo.service.IShoppingService;

@RestController
@RequestMapping("/api/api/ts-ecommerce/carts")
public class CartController {
	
	private IShoppingService shoppingService;
	
	@Autowired
	private void setCartController(IShoppingService shoppingService) {
		this.shoppingService = shoppingService;
	}
		
	@PostMapping
	public ResponseEntity<?> addToCart(
			@AuthenticationPrincipal AccountPrincipal accountPrincipal,
			@RequestBody CartRequest cartRequest)	
	{					
		return new ResponseEntity<>(
				shoppingService.addToCart(accountPrincipal.getAccount(), 
						cartRequest),HttpStatus.OK);
	}
	
	@DeleteMapping
	public ResponseEntity<?> removeFromCart(
			@AuthenticationPrincipal AccountPrincipal accountPrincipal,
			@RequestBody CartRequest cartRequest)	
	{					
		return new ResponseEntity<>(
				shoppingService.removeFromCart(accountPrincipal.getAccount(), 
						cartRequest),HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<CartItem>> getCart(@AuthenticationPrincipal AccountPrincipal accountPrincipal)
	{
		return new ResponseEntity<>(
				shoppingService.getCartItems(accountPrincipal.getAccount()),HttpStatus.OK);		
	}
	
	@PostMapping
	public ResponseEntity<?> updateCart(
			@AuthenticationPrincipal AccountPrincipal accountPrincipal,
			@RequestBody CartRequest cartRequest)	
	{					
		return new ResponseEntity<>(
				shoppingService.updateCart(accountPrincipal.getAccount(), 
						cartRequest),HttpStatus.OK);
	}

}
