package com.tshepo.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CartRequest {
	
	@JsonProperty
	private String productId;
	
	@JsonProperty
	private int quantity;

}
