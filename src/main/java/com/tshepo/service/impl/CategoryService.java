package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Category;
import com.tshepo.persistence.repository.ICategoryRepository;
import com.tshepo.service.ICategoryService;
import com.tshepo.util.Utilities;

@Service
public class CategoryService implements ICategoryService{
	
	private ICategoryRepository categoryRepository;
	
	@Autowired
	private void setCategoryService(ICategoryRepository categoryRepository)	{this.categoryRepository = categoryRepository;}
	
	@Override
	public Category newCategory(Category category) 
	{
		category.setCategoryId(Utilities.generateUniqueNumericUUId());
		category.setCreatedAt(LocalDateTime.now());
		return categoryRepository.save(category);
	}
	
	@Override
	public void updateCategory(Category category){ categoryRepository.save(category); }
	
	@Override
	public Page<Category> findAll(String keyword, int evalPage, int evalPageSize) 
	{
		return categoryRepository.findByNameContaining(keyword, PageRequest.of(evalPage, evalPageSize));
	}
	
	@Override
	public Optional<Category> findByCategoryId(String categoryId){ return categoryRepository.findByCategoryId(categoryId); }

	@Override
	public Optional<Category> findByName(String name) { return categoryRepository.findByName(name); }

	@Override
	public void deleteCategory(Category category) { categoryRepository.delete(category);}

	@Override
	public List<Category> categoryList() { return (List<Category>)categoryRepository.findAll(); }	

}
