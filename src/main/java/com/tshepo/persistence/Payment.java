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

import lombok.Data;

@Data
@Entity
@Table(name = "payment")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String type;
	
	@Column(name ="card_name")
	private String cardName;
	
	@Column(name ="card_number")
	private String cardNumber;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
		
	@Column
	private int cvc;
	
	@Column(name ="holder_name")
	private String holderName;
		
	@OneToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
	
	public static Payment setPayment(Account account) {
		Payment pay = new Payment();
		pay.setAccount(account);
		return pay;
	}
	
	public Payment updatePayment(Payment payment)
	{
		Payment pay = new Payment();
		pay.setCardName(payment.getCardName());
		pay.setCardNumber(payment.getCardNumber());
		pay.setCvc(payment.getCvc());
		pay.setExpiryDate(payment.getExpiryDate());
		pay.setHolderName(payment.getHolderName());
		pay.setType(payment.getType());
		return pay;
	}

}
