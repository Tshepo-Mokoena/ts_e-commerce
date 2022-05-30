package com.tshepo.service;

import java.util.List;
import java.util.Optional;

import com.tshepo.persistence.Product;

public interface IProductService {

	Product newProduct(Product product);

	List<Product> findAll();

	List<Product> activeProductList();

	Optional<Product> findByProductId(String productId);

	Optional<Product> findByProductName(String name);

	List<Product> productSearch(String name);

	void updateProduct(Product product);

}
