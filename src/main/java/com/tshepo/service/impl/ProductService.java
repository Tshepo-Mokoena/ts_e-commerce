package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tshepo.exception.FileUploadException;
import com.tshepo.persistence.Category;
import com.tshepo.persistence.Product;
import com.tshepo.persistence.repository.IProductRepository;
import com.tshepo.service.ICategoryService;
import com.tshepo.service.IProductService;
import com.tshepo.service.IUploadDowloadService;
import com.tshepo.util.Utilities;

@Service
public class ProductService implements IProductService{
	
	private IProductRepository productRepository;
	
	private ICategoryService categoryService;
	
	private IUploadDowloadService uploadDowloadService;
	
	@Autowired
	private void setProductService( IProductRepository productRepository, IUploadDowloadService uploadDowloadService, ICategoryService categoryService)	
	{
		this.productRepository = productRepository;
		this.uploadDowloadService =uploadDowloadService;
		this.categoryService = categoryService;
	}
	
	@Transactional
	@Override
	public Product addProduct(Product product, Optional<MultipartFile> image) 
	{		
		product.setProductId(Utilities.generateUniqueNumericUUId());
		if (!image.isEmpty()) 
		{
			boolean isValid = Utilities.fileExtensionValidator(image.get().getOriginalFilename());
			if (!isValid) throw new FileUploadException();
			String imageUrl = uploadDowloadService.uploadFile(image.get(), product.getProductId());
			product.setProductImageURL(imageUrl);
			product.setImageName(product.getProductId()+ "."+ Utilities.getExtension(image.get().getOriginalFilename()));
		}
		product.setActive(false);
		product.setCreatedAt(LocalDateTime.now());
		return productRepository.save(product);
	}
	
	@Transactional
	@Override
	public Product updateProduct(Product product, Optional<MultipartFile> image) 
	{
		if (!image.isEmpty())
		{			
			boolean isValid = Utilities.fileExtensionValidator(image.get().getOriginalFilename());
			if (!isValid) throw new FileUploadException();
			deleteFile(product.getImageName());
			String imageUrl = uploadDowloadService.uploadFile(image.get(), product.getProductId());
			product.setProductImageURL(imageUrl);
			product.setImageName(product.getProductId()+ "."+ Utilities.getExtension(image.get().getOriginalFilename()));
		}		
		return productRepository.save(product);
	}
	
	@Transactional
	@Override
	public void deleteProduct(Product product)
	{
		deleteFile(product.getImageName());
		productRepository.delete(product); 
	}
	
	@Override
	public Page<Product> allProducts(String keyword, int evalPage, int evalPageSize) 
	{
		return productRepository.findByNameContaining(keyword, PageRequest.of(evalPage, evalPageSize));
	}
	
	@Override
	public Page<Product> activeProducts(String keyword, int evalPage, int evalPageSize) 
	{
		return productRepository.findByNameContainingAndActive(keyword,true, PageRequest.of(evalPage, evalPageSize));
	}

	@Override
	public Product saveProduct(Product getProduct) { return productRepository.save(getProduct);}

	@Override
	public Optional<Product> findByProductId(String productId){ return productRepository.findByProductId(productId); }
	
	@Override
	public void updateProductActiveStatus(String productId, Boolean active){ productRepository.activateProduct(productId, active); }
	
	@Override
	public Optional<Product> findByName(String name){ return productRepository.findByName(name); }
	
	public Category findCategoryByName(String name) { return categoryService.findByName(name).get();}
	
	@Override
	public Page<Product> activeCategoryProducts(String keyword, int evalPage, int evalPageSize) 
	{
		return productRepository.findByCategoriesAndActive(findCategoryByName(keyword), true, PageRequest.of(evalPage, evalPageSize));
	}

	@Override
	public Page<Product> allCategoryProducts(String keyword, int evalPage, int evalPageSize) 
	{
		return productRepository.findByCategories(findCategoryByName(keyword), PageRequest.of(evalPage, evalPageSize));
	}
	
	public List<Category> getCategory(Category category){
		List<Category> categoryList = new ArrayList<>();
		categoryList.add(category);
		return categoryList;
	}
	
	private boolean deleteFile(String name) { return uploadDowloadService.deleteFile(name); }

}
