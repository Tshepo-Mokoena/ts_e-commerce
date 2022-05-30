package com.tshepo.exception;

@SuppressWarnings("serial")
public class ProductNotFoundException extends RuntimeException {
	private final static String DEFAULT_MESSAEGE = "product not found: ";
	public ProductNotFoundException(String message) {
		super(DEFAULT_MESSAEGE + message);
	}
}
