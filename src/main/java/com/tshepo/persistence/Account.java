package com.tshepo.persistence;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tshepo.persistence.auth.Role;

import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_id", nullable = false, updatable = false, unique = true)
	private String accountId;

	@NotBlank(message = "first Name should not be blank")
	@Size(min = 2, max = 50, message = "first name should be between 2 to 50 charaters")
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@NotBlank(message = "Last Name should not be blank")
	@Size(min = 2, max = 50, message = "Last name should be between 2 to 50 charaters")
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Email
	@Column(name = "email", nullable = false, updatable = true, unique = true)
	private String email;

	@NotBlank(message = "Phone should not be blank")
	@Size(min = 10, max = 12, message = "Phone should be between 10 to 12 numbers")
	@Column(name = "phone", nullable = false, updatable = true, length = 12)
	private String phone;

	@NotBlank(message = "Password should not be blank")
	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Transient
	private String token;

	@Column(name = "locked", nullable = false)
	private boolean locked = false;

	@Column(name = "active", nullable = false)
	private boolean active = false;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "account", fetch = FetchType.LAZY)
	@JsonIgnore
	private Cart cart;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

}
