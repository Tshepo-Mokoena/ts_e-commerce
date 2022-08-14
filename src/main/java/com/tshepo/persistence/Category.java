package com.tshepo.persistence;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "category")
@Data
@Entity
@Table(name = "category")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(
			name = "category_id", 
			nullable = false, 
			updatable = false, 
			unique = true
			)
	private String categoryId;
	
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
			length = 100)
	private String name;
	
	@ManyToMany
    @JoinTable(
    		name = "product_categories", 
    		joinColumns = @JoinColumn(name = "category_id"), 
    		inverseJoinColumns = @JoinColumn(name = "product_id")
    		)
	@JsonIgnore
    private List<Product> products;
	
	@Column(
			name = "created_at", 
			nullable = false, updatable = false
			)
	private LocalDateTime createdAt;	
	
	
}
