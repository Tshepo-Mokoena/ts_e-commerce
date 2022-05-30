package com.tshepo.persistence;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "category")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "category_id", nullable = false, updatable = false, unique = true)
	private String categoryId;
	
	@NotBlank(message = "category name should be atleast 2 characters")
	@Size(min = 2, max = 50, message = "category name should be atleast 2 characters")
	@Column(name = "category_name", nullable = false, updatable = true, length = 50)
	private String categoryName;
	
	@ManyToMany(mappedBy = "categories")
    private List<Product> products;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
}
