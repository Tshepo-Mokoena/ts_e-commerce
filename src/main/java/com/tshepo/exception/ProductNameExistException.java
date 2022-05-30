package com.tshepo.exception;

@SuppressWarnings("serial")
public class ProductNameExistException extends RuntimeException {
	private final static String DEFAULT_MESSAEGE = "product name exists: ";
	public ProductNameExistException(String message) {
		super(DEFAULT_MESSAEGE + message);
	}
}
