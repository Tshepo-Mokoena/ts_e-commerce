package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Product;
import com.tshepo.persistence.repository.IProductRepository;
import com.tshepo.service.IProductService;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class ProductService implements IProductService{
	
	private IProductRepository productRepository;
	
	@Autowired
	private ProductService(IProductRepository productRepository)	
	{
		this.productRepository = productRepository;
	}
	
	@Transactional
	@Override
	public Product newProduct(Product product) 
	{		
		product.setActive(false);
		product.setCreatedAt(LocalDateTime.now());
		return productRepository.save(product);
	}
	
	@Transactional
	@Override
	public Product updateProduct(Product product) 
	{
		return productRepository.save(product);
	}
	
	@Override
	public Page<Product> findAll(int evalPage, int evalPageSize) 
	{
		Pageable pageandSize = PageRequest.of(evalPage, evalPageSize);
		return productRepository.findAll(pageandSize);
	}
	
	@Override
	public Optional<Product> findByProductId(String productId) 
	{
		return productRepository.findByProductId(productId);
	}
	
	@Override
	public Optional<Product> findByProductName(String name) 
	{
		return productRepository.findByName(name);
	}
	
	@Override
	public int productCount() 
	{
		return (int)productRepository.count();
	}
	
	@Override
	public Page<Product> productSearch(String name, int evalPage, int evalPageSize) 
	{
		Pageable pageandSize = PageRequest.of(evalPage, evalPageSize);
		return productRepository.findByNameContaining(name, pageandSize);
	}
	
	@Override
	public Page<Product> activeProducts(int evalPage, int evalPageSize) 
	{
		Pageable pageandSize = PageRequest.of(evalPage, evalPageSize);
		return productRepository.activeProducts(true, pageandSize);
	}
	
	@Override
	public void updateProductActiveStatus(String productId, Boolean active)
	{
		productRepository.activateProduct(productId, active);
	}
	
	@Override
	public void deleteProduct(Product product) 
	{
		productRepository.delete(product);
	}
	
}
