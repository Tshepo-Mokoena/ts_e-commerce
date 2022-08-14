package com.tshepo.resources;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tshepo.service.ICategoryService;

@RestController
@RequestMapping("/api/ts-ecommerce/category")
public class CategoryController {

	private ICategoryService categoryService;
	
	@Autowired
	private void setCategoryController(ICategoryService categoryService) {this.categoryService = categoryService;}
	
	@GetMapping
	public ResponseEntity<?> getCategories(@RequestParam("keyword") Optional<String>  keyword, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) 
	{		
		return new ResponseEntity<>( categoryService.findAll(keyword.orElse(""), (page.orElse(0) < 1) ? 0 : page.get() - 1, pageSize.orElse(5)),
				HttpStatus.OK);
	}	
	
	@GetMapping("/all")
	public ResponseEntity<?> findAll(){ return new ResponseEntity<>(categoryService.categoryList(), HttpStatus.OK); }

}
