package com.tshepo.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Product;

@Repository
@Transactional(readOnly = true)
public interface IProductRepository  extends CrudRepository<Product, Long>{
	
	List<Product> findByNameContaining(String keyword);
	
	Optional<Product> findByProductId(String categoryId);

	Optional<Product> findByName(String name);	

}