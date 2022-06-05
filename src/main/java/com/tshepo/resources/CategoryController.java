package com.tshepo.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.exception.*;
import com.tshepo.persistence.Category;
import com.tshepo.persistence.Product;
import com.tshepo.service.ICategoryService;

@RestController
@RequestMapping("/api/ts-ecommerce")
public class CategoryController {

	private ICategoryService categoryService;
	
	@Autowired
	private CategoryController(ICategoryService categoryService)
	{
		this.categoryService = categoryService;		
	}
	
	@PostMapping("/category")
	public ResponseEntity<?> createNewcategory(@RequestBody @Valid Category category) 
	{
		if (!categoryService.findByName(category.getCategoryName()).isEmpty())
			throw new ItemExistException(category.getCategoryName());
			
		return new ResponseEntity<>(categoryService.newCategory(category), HttpStatus.CREATED); 
	}
	
	@PostMapping("/category/update")
	public ResponseEntity<?> updatecategory(@RequestBody @Valid Category category) 
	{
		Category getCategory = categoryService.findByCategoryId(category.getCategoryId())
				.orElseThrow(() -> new ItemExistException(category.getCategoryId()));
		
		Category getCategoryName = categoryService.findByName(category.getCategoryName()).get();
		
		if (getCategoryName != null)
			if(getCategory.getCategoryId() !=  getCategoryName.getCategoryId())
				throw new ItemExistException(category.getCategoryName());
		
		getCategory.setCategoryName(category.getCategoryName());
				
		categoryService.updateCategory(getCategory);
		
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<?> getCategory(@PathVariable String categoryId) 
	{
		if (categoryId.isBlank())
			throw new RuntimeException("category id should not be blank");
		
		Category getCategory = categoryService.findByCategoryId(categoryId)
				.orElseThrow(() -> new ItemExistException(categoryId));
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/category/{categoryId}/products")
	public ResponseEntity<?> getCategoryProducts(@PathVariable String categoryId) 
	{
		List<Product> getProducts = categoryService.findByCategoryId(categoryId).get().getProducts();
		
		if (getProducts.isEmpty()) 
			throw new ItemNotFoundException(categoryId);
		
		return new ResponseEntity<>(categoryService.findByCategoryId(categoryId).get(), HttpStatus.OK);
	}

}
