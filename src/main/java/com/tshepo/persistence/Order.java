package com.tshepo.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tshepo.persistence.auth.OrderStatus;

import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "total")
	private BigDecimal total;
	
	@OneToMany(
			mappedBy="order",
			cascade = CascadeType.ALL,
			fetch=FetchType.LAZY
			)
	@JsonIgnore
	private List<OrderItem> orderItems;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "account_id",
			nullable = false
			)
	@JsonIgnore
	private Account account;
	
	@Enumerated(EnumType.STRING)
	@Column(
			name = "order_status", 
			nullable = true
			)
	private OrderStatus orderStatus;
			
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
	
	public static Order createfromCart(Cart cart, Account account)
	{
		Order order = new Order();
		order.setTotal(cart.getTotal());
		order.setAccount(account);
		order.setOrderStatus(OrderStatus.NEW);
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());
		return order;	
	}	

}
