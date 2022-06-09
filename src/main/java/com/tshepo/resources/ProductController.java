package com.tshepo.resources;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tshepo.exception.*;
import com.tshepo.persistence.Category;
import com.tshepo.persistence.Product;
import com.tshepo.service.ICategoryService;
import com.tshepo.service.IProductService;
import com.tshepo.service.IUploadDowloadService;
import com.tshepo.util.Utilities;

@RestController
@RequestMapping("/api/ts-ecommerce/products")
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
	
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> createNewProduct(@RequestPart @Valid Product product, Optional<MultipartFile> image) 
	{		
		if(!productService.findByProductName(product.getName()).isEmpty())
			throw new ItemExistException(product.getName());
		
		product.setProductId(Utilities.generateUniqueNumericUUId());
		
		if (!image.isEmpty()) {
			boolean isValid = Utilities.fileExtensionValidator(image.get().getOriginalFilename());
			if (!isValid)
				throw new FileUploadException();
			String imageUrl = uploadDowloadService.uploadFile(image.get(), product.getProductId());
			product.setProductImageURL(imageUrl);
			product.setImageName(product.getProductId()+ "."+ Utilities.getExtension(image.get().getOriginalFilename()));
		}		
		
		Product savedProduct = productService.newProduct(product);
		
		return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> updateProduct(@RequestPart @Valid Product product, Optional<MultipartFile> image) 
	{		
		
		Product getProduct = productService.findByProductId(product.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(product.getProductId()));
		
		Product getProductByName = productService.findByProductName(product.getName()).get();
		
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
			boolean isValid = Utilities.fileExtensionValidator(image.get().getOriginalFilename());
			if (!isValid)
				throw new FileUploadException();
			uploadDowloadService.deleteFile(getProduct.getImageName());
			String imageUrl = uploadDowloadService.uploadFile(image.get(), getProduct.getProductId());
			getProduct.setProductImageURL(imageUrl);
			getProduct.setImageName(getProduct.getProductId()+ "."+ Utilities.getExtension(image.get().getOriginalFilename()));
		}
				
		Product savedProduct = productService.updateProduct(getProduct);
		
		return new ResponseEntity<>(savedProduct, HttpStatus.OK);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable String  productId) 
	{		
		if(productId.isBlank())
			throw new RuntimeException("product id should not be blank");
		
		Product getProduct = productService.findByProductId(productId)
				.orElseThrow(() -> new ItemNotFoundException(productId));
				
		return new ResponseEntity<>(getProduct, HttpStatus.OK);
	}

	@PutMapping("/{productId}/activate")
	public ResponseEntity<?> activateProductById(@PathVariable String  productId) 
	{		
		if(productId.isBlank())
			throw new RuntimeException("product id should not be blank");
		
		productService.updateProductActiveStatus(productId, true);
				
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/{productId}/de-activate")
	public ResponseEntity<?> deActivateProductById(@PathVariable String  productId) 
	{		
		if(productId.isBlank())
			throw new RuntimeException("product id should not be blank");
		
		productService.updateProductActiveStatus(productId, false);
				
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<?> getActiveProducts(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{
		if (productService.productCount() == 0)
			throw new ItemNotFoundException("[]");
		
		int evalPageSize = pageSize.orElse(5);		
		int evalPage = (page.orElse(0) < 1) ? 0 : page.get() - 1;
		
		Page<Product> productList = 
				productService.activeProducts(evalPage, evalPageSize);
		
		return new ResponseEntity<>(productList, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllProducts(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{
		if (productService.productCount() == 0)
			throw new ItemNotFoundException("[]");
		
		int defaultPageSize = pageSize.orElse(5);		
		int evalPage = (page.orElse(0) < 1) ? 0 : page.get() - 1;
		
		Page<Product> productList = 
				productService.findAll(evalPage, defaultPageSize);
		
		return new ResponseEntity<>(productList, HttpStatus.OK);
	}
	
	@GetMapping("/search/{keyword}")
	public ResponseEntity<?> searchProduct(
			@PathVariable String  keyword,
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) 
	{		
		if(keyword.isBlank())
			throw new RuntimeException("product id should not be blank");
		
		if (productService.productCount() == 0)
			throw new ItemNotFoundException("[]");
		
		int defaultPageSize = pageSize.orElse(5);		
		int evalPage = (page.orElse(0) < 1) ? 0 : page.get() - 1;
		
		Page<Product> productList = 
				productService.productSearch(keyword, evalPage, defaultPageSize);
				
		return new ResponseEntity<>(productList, HttpStatus.OK);
	}

	@GetMapping("/{productId}/categories")
	public ResponseEntity<?> getProductCategory(@PathVariable String productId)
	{
		List<Category> getcategoryList = productService.findByProductId(productId).get().getCategories();
		return new ResponseEntity<>(getcategoryList, HttpStatus.OK);
	}
	
	@PutMapping("/{productId}/add-category/{categoryId}")
	public ResponseEntity<?> addToProduct(@PathVariable String productId, @PathVariable String categoryId)
	{
		if(productId.isBlank() && categoryId.isBlank())
			throw new RuntimeException("product & category id should not be blank");
		
		Product getProduct = productService.findByProductId(productId)
				.orElseThrow(() -> new ItemNotFoundException(productId));
		
		Category getCategory = categoryService.findByCategoryId(categoryId)
				.orElseThrow(() -> new ItemNotFoundException(categoryId));
		
		
		getProduct.addCategory(getCategory);
		
		Product savedProduct = productService.updateProduct(getProduct);		
		
		return new ResponseEntity<>(savedProduct, HttpStatus.OK);
	}
	
	@PostMapping("/{productId}/remove-category/{categoryId}")
	public ResponseEntity<?> removeFromProduct(@PathVariable String productId, @PathVariable String categoryId)
	{
		if(productId.isBlank() && categoryId.isBlank())
			throw new RuntimeException("product & category id should not be blank");
		
		Product getProduct = productService.findByProductId(productId)
				.orElseThrow(() -> new ItemNotFoundException(productId));
		
		Category getCategory = categoryService.findByCategoryId(categoryId)
				.orElseThrow(() -> new ItemNotFoundException(categoryId));
		
		
		getProduct.removeCategory(getCategory);
		
		Product savedProduct = productService.updateProduct(getProduct);		
		
		return new ResponseEntity<>(savedProduct, HttpStatus.OK);
	}
	
}

