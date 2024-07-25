package com.tshepo.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
@Table(name = "payment")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id", nullable = false, updatable = false, unique = true)
	private Long id;
	
	@Column(name = "type", nullable = false, updatable = true, unique = false)
	private String type;
		
	@Column(name = "card_name", nullable = false, updatable = true, unique = false)
	private String cardName;
	
	@Column(name ="card_number", nullable = false, updatable = true, unique = false)
	private String cardNumber;
	
	@Column(name = "expiry_date", nullable = false, updatable = true, unique = false)
	private Date expiryDate;
		
	@Column(name = "cvc", nullable = false, updatable = true, unique = false)
	private int cvc;
	
	@Column(name ="holder_name", nullable = false, updatable = true, unique = false)
	private String holderName;
		
	@OneToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
	
	public static Payment setPayment(Account account) {
		Payment pay = Payment.builder()
				.account(account)
				.build();
		return pay;
	}
	
	public Payment updatePayment(Payment payment){
		Payment pay = Payment.builder()
				.cardName(payment.getCardName())
				.cardNumber(payment.cardNumber)
				.cvc(payment.getCvc())
				.expiryDate(payment.getExpiryDate())
				.holderName(payment.getHolderName())
				.type(payment.getType())
				.build();
		return pay;
	}

}
