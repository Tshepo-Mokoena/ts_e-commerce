package com.tshepo.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PasswordResetRequest {
	
	@JsonProperty
	@Email
	private String email;
	
	@JsonProperty
	@NotBlank(message = "Password should atleast be 1 or more characters")
	private String password;
	
	@JsonProperty
	@NotBlank(message = "Password should atleast be 1 or more characters")
	private String newPassword;

}
