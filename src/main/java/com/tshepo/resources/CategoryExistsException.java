package com.tshepo.resources;

public class CategoryExistsException extends RuntimeException {
	private final static String DEFAULT_MESSAGE = "category name exists: ";
	public CategoryExistsException(String message) {
		super(DEFAULT_MESSAGE + message);
	}

}
