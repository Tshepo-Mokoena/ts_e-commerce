package com.tshepo.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductRequest {
	
	@JsonProperty
	private String name;
	
	@JsonProperty
	private String desc;
	
	@JsonProperty
	private String qty;
	
	@JsonProperty
	private String price;	

}
