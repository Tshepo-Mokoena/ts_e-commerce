package com.tshepo.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(
			name = "qty",
			nullable = false			
			)
	private Integer qty;
	
	@Column(
			name = "sub_total",
			nullable = false			
			)
	private BigDecimal subtotal;
	
	@OneToOne
	@JoinColumn(
			nullable = false, 
			name = "product_id"
			)
	private Product product;
	
	@ManyToOne
	@JoinColumn(
			nullable = false, 
			name = "cart_id"
			)
	private Cart cart;
	
	@Column(
			name = "updated_at",
			nullable = false
			)
	private LocalDateTime updatedAt;
	
	@Column(
			name = "created_at",
			nullable = false
			)
	private LocalDateTime createdAt;

	public CartItem(Integer qty, BigDecimal subtotal, Product product, Cart cart) 
	{
		this.qty = qty;
		this.subtotal = subtotal;
		this.product = product;
		this.cart = cart;
	}
	
	

}
