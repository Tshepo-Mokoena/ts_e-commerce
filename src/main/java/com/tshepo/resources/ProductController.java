package com.tshepo.resources;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.exception.*;
import com.tshepo.service.IProductService;

@RestController
@RequestMapping("/api/ts-ecommerce/products")
public class ProductController {
	
	private IProductService productService;
	
	@Autowired
	private void setProductController(IProductService productService) {this.productService = productService;}
	
	@GetMapping("/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable String  productId) 
	{			
		return new ResponseEntity<>(
				productService.findByProductId(productId).orElseThrow(() -> new ItemNotFoundException(productId)), HttpStatus.OK);
	}
		
	@GetMapping
	public ResponseEntity<?> getProducts(
			@RequestParam("keyword") Optional<String>  keyword,
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{				
		return new ResponseEntity<>(
				productService.activeProducts(
						keyword.orElse(""),
						page.orElse(0), pageSize.orElse(5)), 
				HttpStatus.OK);
	}
	
	@GetMapping("/category")
	public ResponseEntity<?> getCategoryProducts(
			@RequestParam("keyword") Optional<String>  keyword,
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{				
		return new ResponseEntity<>(
				productService.activeCategoryProducts(
						keyword.orElse(""),
						page.orElse(0), pageSize.orElse(5)), 
				HttpStatus.OK);
	}
	
	@GetMapping("/{productId}/categories")
	public ResponseEntity<?> getProductCategory(@PathVariable String productId)
	{
		return new ResponseEntity<>(
				productService.findByProductId(productId).get().getCategories(), 
				HttpStatus.OK);
	}
			
}

