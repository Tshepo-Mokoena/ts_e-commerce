package com.tshepo.resources.Internal;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.exception.ItemExistException;
import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.persistence.Category;
import com.tshepo.service.ICategoryService;

@RestController
@RequestMapping("/api/ts-ecommerce/internal/category")
public class InternalCategoryController {
	
	private ICategoryService categoryService;
	
	@Autowired
	private void setInternalCategoryController(ICategoryService categoryService){ this.categoryService = categoryService; }
	
	@PostMapping
	public ResponseEntity<?> createNewcategory(@RequestBody @Valid Category category) 
	{
		if (!categoryService.findByName(category.getName()).isEmpty()) throw new ItemExistException(category.getName());
		categoryService.newCategory(category);
		return new ResponseEntity<>(HttpStatus.CREATED); 
	}
	
	@PostMapping("/{categoryId}")
	public ResponseEntity<?> updatecategory(@PathVariable String categoryId, @Valid @RequestBody Category category) 
	{
		Category getCategory = findByCategoryId(categoryId).orElseThrow(() -> new ItemNotFoundException(categoryId));		
		Optional<Category> getCategoryName = categoryService.findByName(category.getName());
		
		if (!getCategoryName.isEmpty())
			if(getCategory.getId() !=  getCategoryName.get().getId())
				throw new ItemExistException(category.getName());
		
		getCategory.setName(category.getName());
		categoryService.updateCategory(getCategory);		
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) 
	{			
		categoryService.deleteCategory(findByCategoryId(categoryId).orElseThrow(() -> new ItemExistException(categoryId)));
		return new ResponseEntity<>(HttpStatus.OK);
	}	
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<?> getCategory(@PathVariable String categoryId) 
	{
		return new ResponseEntity<>(findByCategoryId(categoryId)
				.orElseThrow(() -> new ItemExistException(categoryId)),HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<?> getCategories(@RequestParam("keyword") Optional<String>  keyword, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) 
	{
		return new ResponseEntity<>(categoryService.findAll(keyword.orElse(""),(page.orElse(0) < 1) ? 0 : page.get() - 1, pageSize.orElse(10)),HttpStatus.OK);
	}
		
	private Optional<Category> findByCategoryId(String categoryId){ return categoryService.findByCategoryId(categoryId);}

}
