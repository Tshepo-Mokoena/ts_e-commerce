package com.tshepo.persistence.tokens;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tshepo.persistence.Account;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "confirmation_token")
public class ConfirmationToken {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	
	@Column(name = "token", nullable = false, updatable = false)
	private String token;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "expires_at", nullable = false, updatable = false)
	private LocalDateTime expiresAt;
	
	@Column(name = "confirmed_at", updatable = false)
	private LocalDateTime confirmedAt;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "account_id")
	private Account account;

	public static ConfirmationToken setConfirmationToken(String token, Account account) 
	{
		ConfirmationToken confirmationToken = new ConfirmationToken();
		confirmationToken.setToken(token);
		confirmationToken.setAccount(account);
		confirmationToken.setCreatedAt(LocalDateTime.now());
		confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
		return confirmationToken;
	} 

}
