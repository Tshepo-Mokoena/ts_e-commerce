package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void updateProduct(Product product) 
	{
		productRepository.save(product);
	}
	
	@Override
	public List<Product> findAll() {
		
		return (List<Product>)productRepository.findAll();
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
	public List<Product> productSearch(String name) 
	{
		return productRepository.findByNameContaining(name);
	}
	
	@Override
	public List<Product> activeProductList() 
	{
		List<Product> categories = new ArrayList<>();
		List<Product> findCategories = (List<Product>)findAll();
		for(Product product: findCategories)
		{
			if (product.isActive())
				categories.add(product);
		}
		return categories;
	}

}
