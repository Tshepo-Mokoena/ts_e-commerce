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

import com.tshepo.exception.EmailNotFoundException;
import com.tshepo.persistence.Account;
import com.tshepo.persistence.CartItem;
import com.tshepo.requests.CartRequest;
import com.tshepo.security.AccountPrincipal;
import com.tshepo.service.IAccountService;
import com.tshepo.service.ICartItemService;
import com.tshepo.service.ICartService;

@RestController
@RequestMapping("/api/ts-ecommerce/carts")
public class CartController {

	private ICartItemService cartItemService;
	
	private ICartService cartService;

	private IAccountService accountService;

	@Autowired
	private void setCartController(ICartItemService cartItemService, IAccountService accountService, ICartService cartService) 
	{
		this.cartItemService = cartItemService;
		this.accountService = accountService;
		this.cartService = cartService;;
	}

	@PostMapping
	public ResponseEntity<?> addToCart(@AuthenticationPrincipal AccountPrincipal accountPrincipal, @RequestBody CartRequest cartRequest) 
	{				
		return new ResponseEntity<>(cartService.addToCart(
				findAccount(accountPrincipal.getEmail()), cartRequest), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<?> removeFromCart(@AuthenticationPrincipal AccountPrincipal accountPrincipal,
			@RequestBody CartRequest cartRequest) 
	{		
		return new ResponseEntity<>(cartService.removeFromCart(findAccount(accountPrincipal.getEmail()), cartRequest),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> getCart(@AuthenticationPrincipal AccountPrincipal accountPrincipal) 
	{		
		return new ResponseEntity<>(cartService.getCart(findAccount(accountPrincipal.getEmail())), HttpStatus.OK);
	}
	
	@GetMapping("/cart-items")
	public ResponseEntity<List<CartItem>> getCartItems(@AuthenticationPrincipal AccountPrincipal accountPrincipal) 
	{		
		return new ResponseEntity<>(cartItemService.findByCart(cartService.getCart(findAccount(accountPrincipal.getEmail()))) , HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateCart(@AuthenticationPrincipal AccountPrincipal accountPrincipal,
			@RequestBody CartRequest cartRequest) 
	{		
		return new ResponseEntity<>(cartService.updateCart(findAccount(accountPrincipal.getEmail()), cartRequest),
				HttpStatus.OK);
	}

	private Account findAccount(String email) 
	{
		return accountService.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
	}

}
