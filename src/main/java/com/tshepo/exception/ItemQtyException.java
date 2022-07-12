package com.tshepo.exception;

@SuppressWarnings("serial")
public class ItemQtyException extends RuntimeException {
	private final static String defualtMessage = "item quantity not available / not enough stock: ";
	public ItemQtyException(String message)
	{
		super(defualtMessage + message);
	}

}
