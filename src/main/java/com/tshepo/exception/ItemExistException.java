package com.tshepo.exception;

@SuppressWarnings("serial")
public class ItemExistException extends RuntimeException {
	private final static String DEFAULT_MESSAGE = "Item exists: ";
	public ItemExistException(String message) {
		super(DEFAULT_MESSAGE + message);
	}
}
