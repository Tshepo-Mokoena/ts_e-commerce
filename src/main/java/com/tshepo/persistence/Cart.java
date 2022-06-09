package com.tshepo.persistence;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "carts")
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "total")
	private BigDecimal total;
	
	@ManyToMany
	@JoinTable(
			name = "cart_products",
			joinColumns = @JoinColumn(name = "cart_id"),
			inverseJoinColumns = @JoinColumn(name = "product_id")
			)
	private List<Product> products;
	
	@OneToOne(mappedBy = "cart")
	private Account account;

}
