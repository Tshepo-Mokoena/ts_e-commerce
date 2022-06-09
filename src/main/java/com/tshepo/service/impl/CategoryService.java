package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Category;
import com.tshepo.persistence.repository.ICategoryRepository;
import com.tshepo.service.ICategoryService;
import com.tshepo.util.Utilities;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class CategoryService implements ICategoryService{
	
	private ICategoryRepository categoryRepository;
	
	@Autowired
	private CategoryService(ICategoryRepository categoryRepository) 
	{
		this.categoryRepository = categoryRepository;
	}
	
	@Override
	@Transactional
	public Category newCategory(Category category) 
	{
		category.setCategoryId(Utilities.generateUniqueNumericUUId());
		category.setCreatedAt(LocalDateTime.now());
		return categoryRepository.save(category);
	}
	
	@Override
	@Transactional
	public void updateCategory(Category category) 
	{
		categoryRepository.save(category);
	}
	
	@Override
	public List<Category> findAll() 
	{
		return (List<Category>) categoryRepository.findAll();
	}
	
	@Override
	public Optional<Category> findByCategoryId(String categoryId) 
	{
		return categoryRepository.findByCategoryId(categoryId);
	}

	@Override
	public Optional<Category> findByName(String name) 
	{
		return categoryRepository.findByName(name);		
	}
	
	@Override
	public List<Category> searchCategory(String keyword) 
	{
		return categoryRepository.findByNameContaining(keyword);		
	}

}
