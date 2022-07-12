package com.tshepo.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CartRequest {
	
	@JsonProperty
	@Size(min = 12, max = 12, message = "Product Id should atleast be 12 characters")
	@NotBlank(message = "Product Id should atleast be 12 characters")
	private String productId;
	
	@JsonProperty
	@NotNull(message = "Product quantity should not be empty")
	private int quantity;

}
