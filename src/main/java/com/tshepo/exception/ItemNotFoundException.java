package com.tshepo.exception;

@SuppressWarnings("serial")
public class ItemNotFoundException extends RuntimeException {
	private static final String DEFAULT_MESSAGE = "Item requested not found: ";
	public ItemNotFoundException(String message) {
		super(DEFAULT_MESSAGE + message);
	}
}
