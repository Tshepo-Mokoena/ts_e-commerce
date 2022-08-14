package com.tshepo.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "products")
@Data
@Entity
@Table(name = "products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
			name = "id", 
			nullable = false, 
			updatable = false, 
			unique = true
			)
	private Long id;
	
	@Column(
			name = "product_id", 
			nullable = false, 
			updatable = false, 
			unique = true
			)
	private String productId;
	
	@NotBlank(message = "Name should be atleast 2 characters")
	@Size(
			min = 2,
			max = 100, 
			message = "Name should be atleast 2 characters"
			)
	@ApiModelProperty(notes = "Name should be atleast 2 characters")
	@Column(
			name = "name", 
			nullable = false, 
			updatable = true,
			unique = true,
			length = 100)
	private String name;
	
	@NotBlank(message = "Description should be atleast 2 characters")
	@Size(
			min = 2, 
			max = 1000, 
			message = "Description should be atleast 2 characters"
			)
	@ApiModelProperty(notes = "Description should be atleast 2 characters")
	@Column(
			name = "description", 
			nullable = false, 
			updatable = true, 
			columnDefinition = "text",
			length = 1000
			)
	private String desc;
	
	@Column(
			nullable = false,
			name = "quantity"
			)
	private Integer qty;
	
	@Column(
			name = "price",
			nullable = false
			)
	private BigDecimal price;
	
	@Column(
			name = "product_image_url", 
			nullable = true
			)
	private String productImageURL;
	
	@Column(
			name = "imageName", 
			nullable = true
			)
	private String imageName;
	
	@ManyToMany
    @JoinTable(
    		name = "product_categories", 
    		joinColumns = @JoinColumn(name = "product_id"), 
    		inverseJoinColumns = @JoinColumn(name = "category_id")
    		)
    private List<Category> categories = new ArrayList<>();
	
	@Column(
			name = "active", 
			nullable = false
			)
	private boolean active = false;
	
	@Column(
			name = "created_at",
			nullable = false,
			updatable = false
			)
	private LocalDateTime createdAt;		

}