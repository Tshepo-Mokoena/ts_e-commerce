package com.tshepo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.tshepo.persistence.Product;

public interface IProductService {

	Product addProduct(Product product, Optional<MultipartFile> image);

	Product updateProduct(Product product, Optional<MultipartFile> image);

	Product saveProduct(Product getProduct);

	Page<Product> allProducts(String keyword, int evalPage, int evalPageSize);

	Page<Product> activeProducts(String keyword, int evalPage, int evalPageSize);

	Optional<Product> findByProductId(String productId);

	void updateProductActiveStatus(String productId, Boolean active);

	void deleteProduct(Product product);

	Optional<Product> findByName(String name);

	Page<Product> activeCategoryProducts(String keyword, int evalPage, int evalPageSize);

	Page<Product> allCategoryProducts(String keyword, int evalPage, int evalPageSize);

}
