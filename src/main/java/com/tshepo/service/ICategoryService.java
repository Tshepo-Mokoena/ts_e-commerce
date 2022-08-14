package com.tshepo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.tshepo.persistence.Category;

public interface ICategoryService {

	Category newCategory(Category category);

	void updateCategory(Category category);

	Page<Category> findAll(String keyword, int evalPage, int evalPageSize);
	
	List<Category> categoryList();

	Optional<Category> findByCategoryId(String categoryId);

	Optional<Category> findByName(String name);

	void deleteCategory(Category category);

}
