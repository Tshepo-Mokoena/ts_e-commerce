package com.tshepo.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Category;
import com.tshepo.persistence.Product;

@Repository
@Transactional(readOnly = true)
public interface IProductRepository  extends PagingAndSortingRepository<Product, Long>{
	
	Page<Product> findByNameContaining(String keyword, Pageable pageable);
	
	Optional<Product> findByProductId(@Param("productId") String productId);
	
	Page<Product> findByNameContainingAndActive(String name, Boolean active, Pageable pageable);

	Optional<Product> findByName(String name);
	
	@Modifying
	@Query("update Product set active = :active where productId = :productId")
	void activateProduct(@Param("productId") String productId, @Param("active") Boolean active);	
	
//	@Query("select s from Product s where active = :active and name like %?1%")
//	Page<Product> activeProducts(
//			@Param("name") Optional<String> name, 
//			@Param("active") Boolean active, 
//			@Param("pageable") Pageable pageable);
//	

	Page<Product> findByCategoriesAndActive(Category category, Boolean active, Pageable pageable);
	
//	@Query("select s from Product s where active = :active and category = :category")
//	Page<Product> activeCategoryProducts(
//			@Param("category")  Category category, 
//			@Param("active") Boolean active, 
//			Pageable pageable);

	Page<Product> findByCategories(Category category, Pageable pageable);
	
//	@Query("select s from Product s where category = :category")
//	Page<Product> allCategoryProducts(
//			@Param("category")  Category category, 
//			Pageable pageable);

}