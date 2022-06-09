package com.tshepo.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Product;

@Repository
@Transactional(readOnly = true)
public interface IProductRepository  extends PagingAndSortingRepository<Product, Long>{
	
	Page<Product> findByNameContaining(String keyword, Pageable pageable);
	
	Optional<Product> findByProductId(@Param("productId") String productId);

	Optional<Product> findByName(@Param("name") String name);	
	
	@Query("select s from Product s where active = :active")
	Page<Product> activeProducts(@Param("active") Boolean active, @Param("pageable") Pageable pageable);
	
	@Modifying
	@Query("update Product set active = :active where productId = :productId")
	void activateProduct(@Param("productId") String productId, @Param("active") Boolean active);

}