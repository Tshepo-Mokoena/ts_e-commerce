package com.tshepo.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
	
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
	
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(
			nullable = false, 
			name = "product_id"
			)
	private Product product;
	
	@ManyToOne
	@JoinColumn(
			nullable = false, 
			name = "order_id"
			)
	@JsonIgnore
	private Order order;
	

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

	public static OrderItem createfromCartItem(CartItem cartItem, Order order)
	{
		Product product = cartItem.getProduct();
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setQty(cartItem.getQty());
		orderItem.setSubtotal(cartItem.getSubtotal());;
		orderItem.setProduct(product);
		orderItem.getProduct().setQty(product.getQty() - cartItem.getQty());
		return orderItem;	
	}

}
