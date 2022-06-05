package com.tshepo.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tshepo.exception.*;
import com.tshepo.persistence.Category;
import com.tshepo.persistence.Product;
import com.tshepo.service.ICategoryService;
import com.tshepo.service.IProductService;
import com.tshepo.service.IUploadDowloadService;
import com.tshepo.util.Utilities;

@RestController
@RequestMapping("/api/ts-ecommerce")
public class ProductController {
	
	private IProductService productService;
	
	private IUploadDowloadService uploadDowloadService;
	
	private ICategoryService categoryService;
	
	@Autowired
	private ProductController(IProductService productService, ICategoryService categoryService, IUploadDowloadService uploadDowloadService)
	{
		this.productService = productService;
		this.uploadDowloadService =uploadDowloadService;
		this.categoryService = categoryService;		
	}
	
	@PostMapping(value = "/products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> createNewProduct(@RequestPart @Valid Product product, Optional<MultipartFile> image) 
	{		
		if(!productService.findByProductName(product.getName()).isEmpty())
			throw new ItemExistException(product.getName());
		
		product.setProductId(Utilities.generateUniqueNumericUUId());
		
		if (!image.isEmpty()) {
			String imageUrl = uploadDowloadService.uploadFile(image.get(), product.getProductId());
			product.setProductImageURL(imageUrl);
		}		
		
		Product savedProduct = productService.newProduct(product);
		
		URI locationUri = 
				ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{productId}")
				.buildAndExpand(savedProduct.getProductId()).toUri();
		
		return new ResponseEntity<>(locationUri, HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/products/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> updateProduct(@RequestPart @Valid Product product, Optional<MultipartFile> image) 
	{		
		
		Product getProduct = productService.findByProductId(product.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(product.getProductId()));
		
		Product getProductByName = productService.findByProductId(product.getName()).get();
		
		if (getProductByName != null)
			if (getProduct.getProductId() != getProductByName.getProductId())
				throw new ItemExistException(product.getName());		
		
		if (product.getName() != null)
			getProduct.setName(product.getName());
		
		if (product.getDesc() != null)
			getProduct.setDesc(product.getDesc());
		
		if (product.getQty() > -1)
			getProduct.setQty(product.getQty());
				
		if (product.getPrice().compareTo(getProduct.getPrice()) > -1)
			getProduct.setPrice(product.getPrice());
		
		if (!image.isEmpty())
		{
			uploadDowloadService.deleteFile(getProduct.getProductId()+".png");
			String imageUrl = uploadDowloadService.uploadFile(image.get(), getProduct.getProductId());
			getProduct.setProductImageURL(imageUrl);
		}
				
		productService.updateProduct(getProduct);
		
		URI locationUri = 
				ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{productId}")
				.buildAndExpand(getProduct.getProductId()).toUri();
		
		return new ResponseEntity<>(locationUri, HttpStatus.OK);
	}
	
	@GetMapping("/products/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable String  productId) 
	{		
		if(productId.isBlank())
			throw new RuntimeException("product id should not be blank");
		
		Product getProduct = productService.findByProductId(productId)
				.orElseThrow(() -> new ItemNotFoundException(productId));
				
		return new ResponseEntity<>(getProduct, HttpStatus.OK);
	}
	
	@GetMapping("/products")
	public ResponseEntity<?> getActiveProducts()
	{
		List<Product> getProductList = productService.activeProductList();
		
		if(!getProductList.isEmpty())
			throw new ItemNotFoundException("[]");
		
		return new ResponseEntity<>(getProductList, HttpStatus.OK);
	}
	
	@GetMapping("/products/all")
	public ResponseEntity<?> getAllProducts()
	{
		List<Product> getProductList = productService.findAll();
		
		if(!getProductList.isEmpty())
			throw new ItemNotFoundException("[]");
		
		return new ResponseEntity<>(getProductList, HttpStatus.OK);
	}
	
	@GetMapping("/products/search/{keyword}")
	public ResponseEntity<?> searchProduct(@PathVariable String  keyword) 
	{		
		if(keyword.isBlank())
			throw new RuntimeException("product id should not be blank");
		
		List<Product> getProducts = productService.productSearch(keyword);
		
		if (getProducts.isEmpty()) 
			throw new ItemNotFoundException("[]");
				
		return new ResponseEntity<>(getProducts, HttpStatus.OK);
	}

	@GetMapping("/products/{productId}/categories")
	public ResponseEntity<?> getProductCategory(@PathVariable String productId)
	{
		List<Category> getcategoryList = productService.findByProductId(productId).get().getCategories();
		return new ResponseEntity<>(getcategoryList, HttpStatus.OK);
	}
	
	@GetMapping("/products/{productId}/category/{categoryId}")
	public ResponseEntity<?> setProductCategory(@PathVariable String productId, @PathVariable String categoryId)
	{
		if(productId.isBlank() && categoryId.isBlank())
			throw new RuntimeException("product & category id should not be blank");
		
		Product getProduct = productService.findByProductId(productId)
				.orElseThrow(() -> new ItemNotFoundException(productId));
		
		Category getCategory = categoryService.findByCategoryId(categoryId)
				.orElseThrow(() -> new ItemNotFoundException(categoryId));
		
		List<Category> categories = new ArrayList<>();
		categories.add(getCategory);
		
		List<Product> Products = new ArrayList<>();
		Products.add(getProduct);
		
		getProduct.setCategories(categories);
		getCategory.setProducts(Products);
		
		productService.updateProduct(getProduct);
		categoryService.updateCategory(getCategory);
		
		
		return new ResponseEntity<>( HttpStatus.OK);
	}
}
