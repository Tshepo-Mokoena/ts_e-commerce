package com.tshepo.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Category;

@Repository
@Transactional(readOnly = true)
public interface ICategoryRepository extends CrudRepository<Category, Long>{
	
	Optional<Category> findByName(String name);
	
	Optional<Category> findByCategoryId(String categoryId);

	List<Category> findByNameContaining(String keyword);

}