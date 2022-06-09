package com.tshepo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.tshepo.persistence.Product;

public interface IProductService {

	Product newProduct(Product product);

	Page<Product> findAll(int evalPage, int evalPageSize);

	Page<Product> activeProducts(int evalPage, int evalPageSize);

	Optional<Product> findByProductId(String productId);

	Optional<Product> findByProductName(String name);

	Page<Product> productSearch(String name, int evalPage, int evalPageSize);

	Product updateProduct(Product product);

	void updateProductActiveStatus(String productId, Boolean active);

	int productCount();

	void deleteProduct(Product product);

}
