package com.tshepo.service;

import java.util.List;
import java.util.Optional;

import com.tshepo.persistence.Category;

public interface ICategoryService {

	Category newCategory(Category category);

	void updateCategory(Category category);

	List<Category> findAll();

	Optional<Category> findByCategoryId(String categoryId);

	Optional<Category> findByName(String name);

	List<Category> searchCategory(String keyword);

}
